package savemyreceipt.server.util.gcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Component
public class GeminiUtil {

    @Value("${spring.cloud.gcp.ai.api-url}")
    private String API_URL;

    public ReceiptInfo sendPostRequest(String text) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            log.info("API_URL: " + API_URL);

            // JSON 데이터 생성
            String json = "{\n" +
                "    \"contents\": [\n" +
                "        {\n" +
                "            \"parts\": [\n" +
                "                {\n" +
                "                    \"text\": \"receipt [ 영수증]\\\\n[매장명] 핸즈커피 효자강변점\\\\n[사업자] 456-22-00618\\\\n[주소] 경북 포항시 남구 효성로64번길 16 (효자\\\\n동)\\\\n[대표자] 여규영\\\\n[매출일] 2022-05-28 18:57:54\\\\n[영수증] 20220528-01-0138\\\\n상품명\\\\nICED 피치애플티\\\\nICED_롱블랙\\\\nICED_소이크림라떼\\\\n오틀리 변경\\\\n망고스무디\\\\n토마토생과일주스\\\\n롱블랙\\\\nICED_얼그레이 레몬\\\\nICED_녹차라떼\\\\n복숭아아이스티\\\\n합계 금액\\\\n카카할판부승승승가\\\\n드드부매「인인인\\\\n종번개금 금번일\\\\n[TEL] 054-275-3476\\\\n월: 일시불\\\\n단가 수량\\\\n5,500 2\\\\n4,500 3\\\\n5,500 1\\\\n1,000\\\\n6,000\\\\n6,000\\\\n4,500\\\\n*** 신용승인정보 (고객용) [1] ***\\\\n: 75354696\\\\n5,500 1\\\\n5,500\\\\n1\\\\n4,300 1\\\\n호: 9425-20**-****-2602\\\\n일 시: 2022-05-28 18:57:54\\\\n가맹점 번호 : 708601250\\\\n금액\\\\n11,000\\\\n13,500\\\\n5,500\\\\n1,000\\\\n6,000\\\\n6,000\\\\n4,500\\\\n5,500\\\\n5,500\\\\n4,300\\\\n62,800\\\\nWIFI 비밀번호 : hands144\\\\n우리신용\\\\n맛있는 핸즈커피입니다 :)\\\\n57, 094\\\\n5,706\\\\n62,800\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt_info { \\\"purchase_date\\\": \\\"2022-05-28\\\", \\\"total_price\\\": 62800\\\" }\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt 4 7 0 9 6 2 5 ) 영 수 증 ( 고 객 용 ) ( A E X 0 7 3 )단 말 기 I D : 4 4 4 7 0 2 3 0 1 ( 1 2 2 1 )가 맹 점 명 : 포 항 개 인 택 시 ( 5 0 6 2 1 5 9 6 2 5 )차 량 번 호 : 경 북 1 5 바 7 3 3 7 ( 3 4 7 1 5 0 0 6 7 3 3 7 )대 표 자 : 박 재 득 ( 0 1 0 4 6 8 6 7 3 3 7 )거 래 일 시 : 2 2 - 1 1 - 1 9 1 7 : 1 6 : 0 2탑 승 시 간 : 1 1 - 1 9 1 6 : 4 9 ~ 1 7 : 1 5 ( 9 . 9 4 K M )승 차 / 기 타 : 1 1 , 3 0 0 원 / 0 원 합 계 1 1 , 3 0 0 카 드 번 호 : 4 6 5 5 - 8 3 K - K o k k o k - 2 1 1 9 ( R ) 승 인 번 호 : 0 7 3 6 3 5 1 2 / 하 나 체 크 카 드캐 시 비 고 객 센 터 : 1 6 4 4 - 6 0 0 1\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt_info { \\\"purchase_date\\\": \\\"2023-11-19\\\", \\\"total_price\\\": 11300 }\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt 영수증\\\\n[아로니피자]\\\\n주소: 포항시남구효자동 401-1번지\\\\n사업자등록번호: 506-14-42387\\\\n대표자: 정성훈 전화:275-7727\\\\n[주문일시:] 2022/01/15 12:14\\\\n주문내역:\\\\n세트1\\\\n1\\\\n14,900\\\\n피자변경-고구마\\\\n1\\\\n500\\\\nzero 제로콜라한잔\\\\n3\\\\n5,400\\\\n반반피자F\\\\n2\\\\n33,000\\\\n--삼겹바베큐F 선택\\\\n2\\\\n2,000\\\\n-화이트고구마 선택\\\\n2\\\\n2,000\\\\n-치즈바이트추가!!\\\\n- 1\\\\n5,500\\\\n반반피자SL\\\\n1\\\\n13,000\\\\n-아로니아SL 선택\\\\n1\\\\n2,000\\\\n-페페로니SL 선택\\\\n1\\\\n0\\\\n코카콜라한잔\\\\n5\\\\n7,500\\\\n-치즈크러스트추가\\\\n1\\\\n3,500\\\\n치즈떡볶이\\\\n1\\\\n4,900\\\\n아이스티\\\\n1\\\\n2,000\\\\n합계금액\\\\n96,200\\\\n결전주\\\\n결제방법: 카드\\\\n화: 0\\\\n소: [매장1층04]\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt_info { \\\"purchase_date\\\": \\\"2022-01-15\\\", \\\"total_price\\\": \\\"96200 }\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt Easy Check\\\\nKICC 여신금융협회: 02-2011-0777\\\\n가맹점명, 가맹점주소가 실제와 다른\\\\n경우 신고안내 (포상금 10만원 지급)\\\\n본가설렁탕 / 심재원\\\\n436-12-00482/ TID: 7154372/ Tel: 02-865-1255\\\\n서울 구로구 디지털로32길 97-39, 1층\\\\n[Icag70|)\\\\n카카오뱅크체크 가맹No:00080050145\\\\n5365-1019-****-037* / 2023/08/12\\\\n(일시불)\\\\n19:44:28\\\\n거래\\\\n금액:\\\\n16,364원\\\\n부가세 :\\\\n합\\\\n1,636원\\\\n계:\\\\n18.00071\\\\n승인 번호: 2624 1520\\\\n[회원용]\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt_info { \\\"purchase_date\\\": \\\"2023-08-12\\\", \\\"total_price\\\": 18000 }\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt McDonald'\\''s\\\\n하나체크 승인\\\\nTID: 11****37-[9946]-N\\\\n카드번호: 532092******8380\\\\n거래일시: 22/05/01_16:28:11\\\\n[고객용]\\\\n할부: [일시불]\\\\n금액:\\\\n부가세 :\\\\n16,000원\\\\n1,600원\\\\n합계:\\\\n17,600원\\\\n하나카드\\\\n승인번호 16580506 [EC]\\\\n사업자/가맹점\\\\n506-85-24832/00961474095\\\\n맥도날드포항남부DT\\\\n대표자: 마티네즈\\\\nTEL: (070-7017-4434)\\\\n경상북도 포항시 남구 새천년대로 470 (대잠동)\\\\n1842\\\\nMFY Side 1\\\\nORD #42 -REG #18- 01/05/2022 16:27:37\\\\nQTY ITEM\\\\nTOTAL\\\\n1 빅맥/라지세트\\\\n6.500\\\\n1 콜라ㄴ\\\\n1 후렌치 후라이-L/단품\\\\n1 슈슈버거\\\\n4,500\\\\n1 불고기 버거/세트\\\\n4.300\\\\n1 콜라-M\\\\n1 후렌치 후라이-M/단품\\\\n1 불고기버거\\\\n2.300\\\\nSubtotal\\\\n17.600\\\\nTake-Out Total (incl VA\\\\n17.600\\\\n신용카드\\\\n17.600\\\\nChange\\\\n0\\\\nTOTAL INCLUDES VAT OF\\\\n1,600\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt_info { \\\"purchase_date\\\": \\\"2022-05-01\\\", \\\"total_price\\\": 17600 }\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt [영수증]\\\\n[매장명] 부천성\\\\n[사업자] 503-52-02807\\\\n[주소] 경북 포항시 남구 대이로143번길 22-\\\\n20 (이동)\\\\n[대표자] 조형제\\\\n[TEL] 054-277-1753\\\\n[매출일] 2022-05-14 20:10:12 01-조형제\\\\n[영수증] 20220514-01-0047\\\\n상품명\\\\n단가 수량 금액\\\\n탕수육\\\\n000216\\\\n27.000 4 108.000\\\\n짜장면-곱빼기\\\\n000006\\\\n8.000 1\\\\n8,000\\\\n짜장면\\\\n000006\\\\n7,000\\\\n7,000\\\\n삼선간짜장\\\\n000007\\\\n8.000 4\\\\n32.000\\\\n소고기짜장면\\\\n000009\\\\n8.000\\\\n1 8.000\\\\n짬뽕\\\\n000011\\\\n9,000 1\\\\n9.000\\\\n해물볶음면\\\\n000014\\\\n10,000 3 30.000\\\\n게살볶음밥\\\\n000017\\\\n10,000\\\\n10.000\\\\n새우볶음밥\\\\n000018\\\\n10.000 2\\\\n20,000\\\\n232,000\\\\n합계금액\\\\n부가세 과세물품가액\\\\n210.911\\\\n부\\\\n가\\\\n세\\\\n21.089\\\\n은\\\\n맥액\\\\n발발\\\\n을은\\\\n232,000\\\\n232,000\\\\n232.000\\\\n신용 카드\\\\n*신용승인정보 (고객용) [1] ***\\\\nㅂ크체크\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt_info { \\\"purchase_date\\\": \\\"2022-05-14\\\", \\\"total_price\\\": 232000 }\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"text\": \"receipt " + text + "\"" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"generationConfig\": {\n" +
                "        \"temperature\": 0.9,\n" +
                "        \"topK\": 1,\n" +
                "        \"topP\": 1,\n" +
                "        \"maxOutputTokens\": 2048,\n" +
                "        \"stopSequences\": []\n" +
                "    },\n" +
                "    \"safetySettings\": [\n" +
                "        {\n" +
                "            \"category\": \"HARM_CATEGORY_HARASSMENT\",\n" +
                "            \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"category\": \"HARM_CATEGORY_HATE_SPEECH\",\n" +
                "            \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"category\": \"HARM_CATEGORY_SEXUALLY_EXPLICIT\",\n" +
                "            \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"category\": \"HARM_CATEGORY_DANGEROUS_CONTENT\",\n" +
                "            \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);
            log.info(response.getBody());
            return jsonParse(response.getBody());
        } catch (Exception e) {
            log.info("에러 발생");
            e.printStackTrace();
        }
        return ReceiptInfo.builder().build();
    }

    public ReceiptInfo jsonParse(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            GeminiResponse response = objectMapper.readValue(json, GeminiResponse.class);
            String text = response.getCandidates().get(0).getContent().getParts().get(0).getText();
            String jsonPart = text.replace("receipt_info", "");
            return objectMapper.readValue(jsonPart, ReceiptInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
