package kr.co.pulmuone.v1.comm.api.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import kr.co.pulmuone.v1.comm.enums.CodeCommEnum;
import kr.co.pulmuone.v1.comm.util.EnumUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum SalesTypes implements CodeCommEnum { // ( ERP API ) 행사구분 Enum

    NONE("", ""), //
    NORMAL("정상", "정상"), //
    EVENT("행사", "행사");

    @JsonValue // 해당 속성의 값을 Json 직렬화시 값으로 지정
    private final String code; // ERP API 에서 전송하는 행사구분

    private final String codeName;

    // ERP API 에서 사용하는 해당 코드의 키 값 => Json 데이터 내에서 해당 키 값 사용
    public static final String CODE_KEY = "salTyp";

    /*
     * Json => Java Object 역직렬화시 @JsonProperty 로 지정된 해당 코드에 해당하는 Enum 반환 메서드
     *
     * @Param String code : ERP API 에서 전송한 Json 데이터 내 CODE_KEY 에 해당하는 값
     */
    @JsonCreator // @JsonCreator로 지정된 static 메서드를 Jackson ObjectMapper 가 역직렬화시 사용함
    public static SalesTypes deserializeToEnumByCode( //
            @JsonProperty(SalesTypes.CODE_KEY) String code // ERP API 에서 전송한 행사구분
    ) {

        if (code == null) {
            return SalesTypes.NONE;
        }

        return EnumUtil.getEnum(SalesTypes.class, code);
    }

}
