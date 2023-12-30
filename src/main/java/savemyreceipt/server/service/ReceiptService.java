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
import savemyreceipt.server.util.gcp.ImageReaderUtil;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;
    private final DataBucketUtil dataBucketUtil;
    private final ImageReaderUtil imageReaderUtil;

    private final static String IMAGE_URI_PREFIX = "https://storage.googleapis.com/";
    private final static String BUCKET_NAME = "savemyreceipt";

    @Transactional
    public String upload(String username, MultipartFile image) throws IOException {
        User user = userRepository.getUserByEmail(username);
        if (image.isEmpty()) {
            throw new NotFoundException(ErrorStatus.NO_IMAGE, ErrorStatus.NO_IMAGE.getMessage());
        }
        if (image.getContentType() == null || !image.getContentType().startsWith("image")) {
            throw new NotFoundException(ErrorStatus.INVALID_FILE_TYPE, ErrorStatus.INVALID_FILE_TYPE.getMessage());
        }

        // GCS에 이미지 업로드 후 이미지 URI 반환
        String imageUri = IMAGE_URI_PREFIX + dataBucketUtil.uploadToBucket(image);
        Receipt receipt = Receipt.builder()
            .imageUri(imageUri)
            .user(user)
            .group(user.getGroup())
            .build();
        receiptRepository.save(receipt);
        return extractText(imageUri);
    }

    private String extractLabels(String imageUri) {
        Map<String, Float> labels = imageReaderUtil.extractLabels(imageUri);
        return labels.keySet().toString();
    }

    private String extractText(String imageUri) {
        return imageReaderUtil.extractText(imageUri);
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
}
