package savemyreceipt.server.util.gcp;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import savemyreceipt.server.exception.ErrorStatus;
import savemyreceipt.server.exception.model.CustomException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ImageReaderUtil {

    private final ResourceLoader resourceLoader;
    private final CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    public ImageReaderUtil(ResourceLoader resourceLoader, CloudVisionTemplate cloudVisionTemplate) {
        this.resourceLoader = resourceLoader;
        this.cloudVisionTemplate = cloudVisionTemplate;
    }

    public Map<String, Float> extractLabels(String imageUri) {
        AnnotateImageResponse response = cloudVisionTemplate.analyzeImage(
            resourceLoader.getResource(imageUri), Feature.Type.LABEL_DETECTION);

        return response.getLabelAnnotationsList().stream()
            .collect(
                Collectors.toMap(
                    EntityAnnotation::getDescription,
                    EntityAnnotation::getScore,
                    (u, v) -> {
                        throw new CustomException(ErrorStatus.GOOGLE_VISION_API_DUPLICATE_KEY_ERROR,
                            ErrorStatus.GOOGLE_VISION_API_DUPLICATE_KEY_ERROR.getMessage());
                    }
                    , LinkedHashMap::new));
    }

    public String extractText(String imageUrl) {
        return cloudVisionTemplate.extractTextFromImage(resourceLoader.getResource(imageUrl));
    }


}
