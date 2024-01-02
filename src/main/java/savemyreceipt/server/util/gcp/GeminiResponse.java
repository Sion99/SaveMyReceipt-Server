package savemyreceipt.server.util.gcp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiResponse {
    private List<Candidate> candidates;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Candidate {
    private Content content;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Content {
    private List<Part> parts;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Part {
    private String text;
}
