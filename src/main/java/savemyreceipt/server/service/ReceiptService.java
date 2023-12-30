package savemyreceipt.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import savemyreceipt.server.DTO.receipt.ReceiptUpdateRequestDto;
import savemyreceipt.server.domain.Receipt;
import savemyreceipt.server.domain.User;
import savemyreceipt.server.exception.ErrorStatus;
import savemyreceipt.server.exception.model.CustomException;
import savemyreceipt.server.exception.model.NotFoundException;
import savemyreceipt.server.infrastructure.ReceiptRepository;
import savemyreceipt.server.infrastructure.UserRepository;
import savemyreceipt.server.util.gcp.DataBucketUtil;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;
    private final DataBucketUtil dataBucketUtil;

    private final static String IMAGE_URI_PREFIX = "https://storage.googleapis.com/";

    @Transactional
    public String upload(String username, MultipartFile image) throws IOException {
        User user = userRepository.getUserByEmail(username);
        if (image.isEmpty()) {
            throw new NotFoundException(ErrorStatus.NO_IMAGE, ErrorStatus.NO_IMAGE.getMessage());
        }
        if (image.getContentType() == null || !image.getContentType().startsWith("image")) {
            throw new NotFoundException(ErrorStatus.INVALID_FILE_TYPE, ErrorStatus.INVALID_FILE_TYPE.getMessage());
        }
        String imageUri = IMAGE_URI_PREFIX + dataBucketUtil.uploadToBucket(image);
        Receipt receipt = Receipt.builder()
            .imageUri(imageUri)
            .user(user)
            .group(user.getGroup())
            .build();
        receiptRepository.save(receipt);
        return imageUri;
    }

    @Transactional
    public void update(String username, ReceiptUpdateRequestDto receiptUpdateRequestDto) {
        User user = userRepository.getUserByEmail(username);
        Receipt receipt = receiptRepository.getReceiptById(receiptUpdateRequestDto.getId());

        if (!receipt.getUser().equals(user)) {
            throw new CustomException(ErrorStatus.RECEIPT_NOT_AUTHORIZED, ErrorStatus.RECEIPT_NOT_AUTHORIZED.getMessage());
        }
        receipt.update(receiptUpdateRequestDto);
        receiptRepository.save(receipt);
    }

}
