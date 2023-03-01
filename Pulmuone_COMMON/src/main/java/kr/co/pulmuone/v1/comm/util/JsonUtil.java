package kr.co.pulmuone.v1.comm.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

public class JsonUtil {

    private JsonUtil() {
    }

    /*
     * objectMapper 에 추가할 javaTimeModule 생성 : LocalDateTimeDeserializer 클래스 세팅
     */
    static {

        JsonUtil.javaTimeModule = new JavaTimeModule();

        // Json => Java Object 역직렬화시 String 타입의 날짜 데이터를 LocalDateTime 으로 변환하는 LocalDateTimeDeserializer 클래스
        LocalDateTimeDeserializer erpApiDatetimeFormatDeserializer = new LocalDateTimeDeserializer( //
                DateTimeFormatter.ofPattern(JsonUtil.ERP_API_DATETIME_FORMAT).withZone(ZoneId.of("Asia/Seoul")) //
        );

        LocalDateDeserializer erpApiDateFormatDeserializer = new LocalDateDeserializer( //
                DateTimeFormatter.ofPattern(JsonUtil.ERP_API_DATE_FORMAT).withZone(ZoneId.of("Asia/Seoul")) //
        );

        // javaTimeModule 에 LocalDateTimeDeserializer, LocalDateDeserializer 클래스 추가
        JsonUtil.javaTimeModule.addDeserializer(LocalDateTime.class, erpApiDatetimeFormatDeserializer);
        JsonUtil.javaTimeModule.addDeserializer(LocalDate.class, erpApiDateFormatDeserializer);

    }

    // ERP API 에서 수신되는 DATETIME 포맷
    private static final String ERP_API_DATETIME_FORMAT = "yyyyMMddHHmmss";

    // ERP API 에서 수신되는 DATE 포맷
    private static final String ERP_API_DATE_FORMAT = "yyyyMMdd";

    private static JavaTimeModule javaTimeModule;

    public static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json() //
            .modules(javaTimeModule) // JavaTimeModule 클래스 모듈 등록
            .deserializerByType(Boolean.class, new BooleanTypeConvertDeserializer()) // "Y", "N" => Boolean 클래스의 true, false 로 변환하는 deserializer 클래스 등록
            .featuresToEnable(SerializationFeature.INDENT_OUTPUT) // Pretty Printing 활성화
            .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // dto 에 존재 하지 않는 속성이 있는 경우 무시
            .visibility(PropertyAccessor.FIELD, Visibility.ANY) // public 필드 / getter 메서드 유무 상관 없이 직렬화
            .build();

    /*
     * 단건 Json 데이터를 자바 객체로 역직렬화 후 ArrayList 에 담아 반환
     */
    public static <T> List<T> deserializeJsonObject(String json, Class<T> classType) throws IOException {

        T result = null;
        List<T> list = new ArrayList<>();

        JsonNode jsonNode = OBJECT_MAPPER.readTree(json);

        result = OBJECT_MAPPER.convertValue(jsonNode, classType);
        list.add(result);

        return list;

    }

    /*
     * 여러 건 Json 데이터를 ArrayList<T> 타입으로 역직렬화 후 반환
     */
    public static <T> List<T> deserializeJsonArray(String json, Class<T> classType) throws IOException {

        List<T> result = null;

        JavaType listType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, classType);

        result = OBJECT_MAPPER.readValue(json, listType);

        return result;

    }

    /*
     * Pretty Pretting 적용하여 Java Object => Json 직렬화된 String 반환
     *
     * @Param Object javaObject : Json 문자열로 직렬화할 Java Object
     */
    public static String serializeWithPrettyPrinting(Object javaObject) {

        String jsonString = null;

        try {

            jsonString = OBJECT_MAPPER.writeValueAsString(javaObject);

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Serializing to json falied", e);

        }

        return jsonString;

    }

    /*
     * Json => Java Object 역직렬화시 "Y", "N" 값을 Boolean 클래스의 true, false 로 변환하는 JsonDeserializer 클래스
     *
     * Json 내 값이 null 인 경우 : false 세팅
     *
     */
    public static class BooleanTypeConvertDeserializer extends JsonDeserializer<Boolean> {

        protected static final String NO = "N";
        protected static final String YES = "Y";

        @Override
        public Boolean deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {

            JsonToken currentToken = jsonParser.getCurrentToken();

            if (currentToken.equals(JsonToken.VALUE_STRING)) {
                String text = jsonParser.getText().trim();

                if (YES.equalsIgnoreCase(text)) {
                    return Boolean.TRUE;
                } else if (NO.equalsIgnoreCase(text)) {
                    return Boolean.FALSE;
                }

                throw ctxt.weirdStringException(text, Boolean.class, //
                        "Only \"" + YES + "\" or \"" + NO + "\" values supported" //
                );

            } else if (currentToken.equals(JsonToken.VALUE_NULL)) {
                return getNullValue();
            }

            return (Boolean) ctxt.handleUnexpectedToken(Boolean.class, jsonParser);
        }

        @Override
        public Boolean getNullValue() {
            return Boolean.FALSE;
        }

    }

}
