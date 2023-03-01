package kr.co.pulmuone.v1.comm.api.dto.vo;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.pulmuone.v1.comm.util.JsonUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString(of = { "isSuccess", "statusCode", "responseCode", "responseMessage" })
public class BaseApiResponseVo {

    // ERP API 호출시 성공 응답 코드 : 통신 결과가 200 OK && 응답코드가 "성공", "데이터 없음" 인 경우에만 호출 성공임
    private static final String API_CALL_SUCCESS_CODE = "000";

    // ERP API 호출시 "데이터 없음" 응답 코드
    private static final String NO_DATA_CODE = "103";

    // ERP API 조회시 수신된 Response Body 내 응답 메시지, 응답 코드 Key 값
    private static final String RESPONSE_MESSAGE = "responseMessage"; // ERP API 응답 메시지
    private static final String RESPONSE_CODE = "responseCode"; // ERP API 응답 코드

    /*
     * GET 방식으로 ERP API 조회시 수신된 Response Body 내 추가 응답코드 Key 값
     */
    private static final String DATA_HEADER = "header"; // 실제 데이터의 key 값
    private static final String TOTAL_PAGE = "totalPage"; // 총 페이지 Key 값
    private static final String CURRENT_PAGE = "currentPage"; // 현재 페이지 Key 값
    private static final String PAGE_SIZE = "pageSize"; // 페이지당 조회 건수

    /*
     * POST, PUT 방식으로 ERP API 호출 후 수신된 Response Body 내 Key 값
     */
    private static final String UN_AFFECTED = "unAffected"; // 업데이트되지 않은 조건들

    /*
     * json => Java Object 로 역직렬화시 사용할 objectMapper 객체
     * => JsonUtil 내에서 deserializer 별도 적용됨
     */
    public static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

    /*
     * 기초 데이터 : restTemplate 으로 수신된 ResponseEntity<String> 객체의 statusCode, responseHeaders, responseBody 데이터
     */
    @Getter
    private HttpStatus statusCode; // HTTP 상태코드

    @Getter
    private HttpHeaders responseHeaders; // Response Header 정보

    @Getter
    private String responseBody; // Response Body : 실제 json data 객체

    /*
     * ERP API 호출 성공 여부 : HTTP 통신 결과가 200 OK && 응답코드가 "000" 인 경우에만 true 세팅됨
     */
    @Getter
    private boolean isSuccess;

    /*
     * responseBody ( Json Data ) 를 Jackson ObjectMapper 로 역직렬화한 JsonNode 객체
     */
    @JsonIgnore // JSON 직렬화에서 제외
    private JsonNode jsonNode;

    /*
     * Response Body 내 응답 메시지, 응답 코드
     */
    @Getter
    private String responseMessage; // ERP API 응답 메시지

    @Getter
    private String responseCode; // ERP API 응답 코드

    /*
     * GET 방식으로 ERP API 조회시 추가 응답 결과 => getter 메서드를 별도 구현
     */
    @JsonInclude(JsonInclude.Include.NON_NULL) // 값이 null 인 경우 json 직렬화에서 제외
    private Integer totalPage; // 총 페이지

    @JsonInclude(JsonInclude.Include.NON_NULL) // 값이 null 인 경우 json 직렬화에서 제외
    private Integer currentPage; // 현재 페이지 번호

    @JsonInclude(JsonInclude.Include.NON_NULL) // 값이 null 인 경우 json 직렬화에서 제외
    private Integer pageSize; // 페이지당 조회 건수

    /*
     * API 호출 성공시 전체 페이지 수, 아닌 경우 null 반환
     *
     * => 해당 필드는 GET 방식 API 조회시에만 세팅됨, PUT / POST API 조회 후 해당 getter 호출시에도 null 반환
     */
    public Integer getTotalPage() {
        return (isSuccess ? this.totalPage : null);
    }

    /*
     * API 호출 성공시 현재 페이지 수, 아닌 경우 0 ( 데이터 미조회 ) 반환
     *
     * => 해당 필드는 GET 방식 API 조회시에만 세팅됨, PUT / POST API 조회 후 해당 getter 호출시에도 null 반환
     */
    public Integer getCurrentPage() {
        return (isSuccess ? this.currentPage : null);
    }

    /*
     * API 호출 성공시 페이지당 조회 건수, 아닌 경우 0 ( 데이터 미조회 ) 반환
     *
     * => 해당 필드는 GET 방식 API 조회시에만 세팅됨, PUT / POST API 조회 후 해당 getter 호출시에도 null 반환
     */
    public Integer getPageSize() {
        return (isSuccess ? this.pageSize : null);
    }

    /*
     * POST, PUT 방식으로 ERP API 조회시 추가 응답 결과 => getter 메서드를 별도 구현
     */
    @JsonInclude(JsonInclude.Include.NON_NULL) // 값이 null 인 경우 json 직렬화에서 제외
    private String unAffected; // 업데이트되지 않은 조건들

    /*
     * API 호출 성공시 업데이트되지 않은 조건들, 아닌 경우 null 반환
     *
     * => 해당 필드는 line, header 2 가지 key 값을 가지는 json object 형식으로 반환된다고 API 명세서에 기술되어 있으나,
     *
     * 실제 호출 결과 / 사용 여부가 명확하지 않아 일단 String 타입으로 선언함
     */
    public String getUnAffected() {
        return (isSuccess ? this.unAffected : null);
    }

    /*
     * ERP API 로 조회한 데이터를 역직렬화한 List 반환
     */
    public <T> List<T> deserialize(Class<T> classType) {

        if (jsonNode == null) {
            throw new RuntimeException("jsonNode is null");
        }

        List<T> result = null;
        String jsonData = jsonNode.get(DATA_HEADER).toString();

        if (jsonData != null) {

            try {

                result = JsonUtil.deserializeJsonArray(jsonData, classType);

            } catch (IOException e) {
                throw new RuntimeException("deserialize failed");
            }

        } else {
            throw new RuntimeException(DATA_HEADER + " not exist in responseBody");
        }

        return result;

    }

    /*
     * 별도 생성자로 @Builder 구현 : 기초 데이터 세팅 후 초기화 메서드 init() 실행
     */
    @Builder
    public BaseApiResponseVo(HttpStatus statusCode, HttpHeaders responseHeaders, String responseBody) {
        this.statusCode = statusCode;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
        this.init(); // 초기화 메서드 : 내부에서 기초 데이터 제외한 jsonNode 등 세팅
    }

    /*
     * 초기화 메서드
     */
    public void init() {

        if (responseBody != null) {

            /*
             * 최초 객체 생성시 responseBody => responseBody 로 1차 변환
             */
            try {

                this.jsonNode = OBJECT_MAPPER.readTree(responseBody);

            } catch (JsonProcessingException e) {
                throw new RuntimeException("Json Parsing Error", e);
            }

            /*
             * ERP API 응답 메시지, 응답 코드, API 호출 성공 여부
             */
            if (this.jsonNode.get(RESPONSE_MESSAGE) != null) {
                this.responseMessage = this.jsonNode.get(RESPONSE_MESSAGE).asText();
            }

            if (this.jsonNode.get(RESPONSE_CODE) != null) {
                this.responseCode = this.jsonNode.get(RESPONSE_CODE).asText();
            }

            // API 호출 성공 여부 : 통신 결과 200 OK && 응답 코드가 "성공", "데이터 없음" 에 해당하는 코드인 경우에만 true 세팅
            this.isSuccess = (this.statusCode != null && this.statusCode == HttpStatus.OK //
                    && this.responseCode != null //
                    && (this.responseCode.equals(API_CALL_SUCCESS_CODE) || this.responseCode.equals(NO_DATA_CODE)) //
            );

            /*
             * API 호출 성공시 : 추가 응답 결과 세팅
             */
            if (this.isSuccess) {

                // GET 방식 조회시
                if (this.jsonNode.get(TOTAL_PAGE) != null) {
                    this.totalPage = Integer.valueOf(this.jsonNode.get(TOTAL_PAGE).asText());
                }

                if (this.jsonNode.get(CURRENT_PAGE) != null) {
                    this.currentPage = Integer.valueOf(this.jsonNode.get(CURRENT_PAGE).asText());
                }

                if (this.jsonNode.get(PAGE_SIZE) != null) {
                    this.pageSize = Integer.valueOf(this.jsonNode.get(PAGE_SIZE).asText());
                }

                // POST, PUT 방식 조회시
                if (this.jsonNode.get(UN_AFFECTED) != null) {
                    this.unAffected = this.jsonNode.get(UN_AFFECTED).asText();
                }

            }

        } else {
            throw new RuntimeException("responseBody is null");
        }

    }

}
