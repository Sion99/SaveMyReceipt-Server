package savemyreceipt.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import savemyreceipt.server.DTO.receipt.ReceiptUpdateRequestDto;
import savemyreceipt.server.DTO.receipt.response.ReceiptResponseDto;
import savemyreceipt.server.domain.Receipt;
import savemyreceipt.server.domain.User;
import savemyreceipt.server.exception.ErrorStatus;
import savemyreceipt.server.exception.model.CustomException;
import savemyreceipt.server.exception.model.NotFoundException;
import savemyreceipt.server.infrastructure.ReceiptRepository;
import savemyreceipt.server.infrastructure.UserRepository;
import savemyreceipt.server.util.gcp.DataBucketUtil;
import savemyreceipt.server.util.gcp.GeminiUtil;
import savemyreceipt.server.util.gcp.ImageReaderUtil;
import savemyreceipt.server.util.gcp.ReceiptInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;
    private final DataBucketUtil dataBucketUtil;
    private final ImageReaderUtil imageReaderUtil;
    private final GeminiUtil geminiUtil;

    private final static String IMAGE_URI_PREFIX = "https://storage.googleapis.com/";
    private final static String BUCKET_NAME = "savemyreceipt";

    public ReceiptResponseDto upload(String username, MultipartFile image) {
        User user = userRepository.getUserByEmail(username);
        if (image.isEmpty()) {
            throw new NotFoundException(ErrorStatus.NO_IMAGE, ErrorStatus.NO_IMAGE.getMessage());
        }
        if (image.getContentType() == null || !image.getContentType().startsWith("image")) {
            throw new NotFoundException(ErrorStatus.INVALID_FILE_TYPE, ErrorStatus.INVALID_FILE_TYPE.getMessage());
        }

        // Google Cloud Storage 에 이미지 업로드 후 이미지 URI 반환
        try {
            Receipt receipt = uploadToBucket(user, image);
            log.info("uploadToBucket 통과 receipt: " + receipt);

            // 이미지에서 텍스트 추출
            String text = extractText(receipt.getImageUri());
            log.info("extractText 통과 text: " + text);

            // 추출한 텍스트로 자동으로 필드 채우기
            autoFill(receipt, text);
            return ReceiptResponseDto.convertToDto(receipt);
        } catch (CustomException e) {
            log.info("CustomException 발생");
            throw e;
        } catch (Exception e) {
            log.info("Exception 발생");
            throw new CustomException(ErrorStatus.GOOGLE_VISION_API_NOT_RECEIPT, ErrorStatus.GOOGLE_VISION_API_NOT_RECEIPT.getMessage());
        }

    }

    /**
     * Google Cloud Storage 에 이미지 업로드 후 이미지 URI 반환
     */
    private Receipt uploadToBucket(User user, MultipartFile image) throws IOException {
        // Google Cloud Storage 에 이미지 업로드 후 이미지 URI 반환
        String imageUri = IMAGE_URI_PREFIX + dataBucketUtil.uploadToBucket(image);
        Receipt receipt = Receipt.builder()
            .imageUri(imageUri)
            .user(user)
            .group(user.getGroup())
            .build();
        receiptRepository.save(receipt);
        return receipt;
    }

    /**
     * 이미지에서 라벨 추출하고 제일 첫번째가 receipt 이 아니면 반려 (삭제)
     */
    public void checkLabel(Receipt receipt) {
        try {
            List<String> labels = extractLabels(receipt.getImageUri());
            if (!labels.get(0).equalsIgnoreCase("Receipt")) {
                throw new CustomException(ErrorStatus.NOT_RECEIPT, ErrorStatus.NOT_RECEIPT.getMessage());
            }
        } catch (Exception e) {
            log.info("checkLabel 통과 후 예외 발생");
            directDelete(receipt);
            throw new CustomException(ErrorStatus.GOOGLE_VISION_API_BAD_IMAGE_DATA, ErrorStatus.GOOGLE_VISION_API_BAD_IMAGE_DATA.getMessage());
        }

    }

    private List<String> extractLabels(String imageUri) {
        Map<String, Float> labels = imageReaderUtil.extractLabels(imageUri);
        for (String key : labels.keySet()) {
            log.info("key: " + key + ", value: " + labels.get(key));
        }
        return new ArrayList<>(labels.keySet());
    }

    private String extractText(String imageUri) {
        try {
            return imageReaderUtil.extractText(imageUri);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.GOOGLE_VISION_API_BAD_IMAGE_DATA, ErrorStatus.GOOGLE_VISION_API_BAD_IMAGE_DATA.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ReceiptResponseDto getReceipt(String username, Long receiptId) {
        User user = userRepository.getUserByEmail(username);
        Receipt receipt = receiptRepository.getReceiptById(receiptId);

        checkReceipt(user, receipt);
        return ReceiptResponseDto.convertToDto(receipt);
    }

    @Transactional
    public void update(String username, ReceiptUpdateRequestDto receiptUpdateRequestDto) {
        User user = userRepository.getUserByEmail(username);
        Receipt receipt = receiptRepository.getReceiptById(receiptUpdateRequestDto.getId());

        checkReceipt(user, receipt);
        receipt.update(receiptUpdateRequestDto);
        receiptRepository.save(receipt);
    }

    @Transactional
    public void delete(String username, Long receiptId) {
        User user = userRepository.getUserByEmail(username);
        Receipt receipt = receiptRepository.getReceiptById(receiptId);

        checkReceipt(user, receipt);
        log.info("receipt.getImageUri(): " + receipt.getImageUri());
        String uuid = receipt.getImageUri().substring(IMAGE_URI_PREFIX.length() + BUCKET_NAME.length());
        log.info("uuid: " + uuid);
        dataBucketUtil.deleteFromBucket(uuid);
        receiptRepository.delete(receipt);
    }

    @Transactional
    private void directDelete(Receipt receipt) {
        String uuid = receipt.getImageUri().substring(IMAGE_URI_PREFIX.length() + BUCKET_NAME.length());
        log.info("uuid: " + uuid);
        dataBucketUtil.deleteFromBucket(uuid);
        receiptRepository.delete(receipt);
    }

    private void checkReceipt(User user, Receipt receipt) {
        if (!receipt.getUser().equals(user)) {
            throw new CustomException(ErrorStatus.RECEIPT_NOT_AUTHORIZED, ErrorStatus.RECEIPT_NOT_AUTHORIZED.getMessage());
        }
    }

    @Transactional
    private void autoFill(Receipt receipt, String text) throws IOException {
        // GeminiUtil 에서 자동으로 필드 채우기
        ReceiptInfo receiptInfo = geminiUtil.sendPostRequest(text);
        receipt.autoFill(receiptInfo);
        receiptRepository.save(receipt);
    }
}
