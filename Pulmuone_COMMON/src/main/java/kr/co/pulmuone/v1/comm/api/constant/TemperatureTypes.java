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
public enum TemperatureTypes implements CodeCommEnum { // ( ERP API ) 온도구분 Enum

    NONE("", "기타", "ERP_STORAGE_TYPE.ETC"), // BOS 상에서는 코드명 "기타" 로 사용됨
    UNDEFINED("미정", "미정", "ERP_STORAGE_TYPE.NOT_DEFINED"), //
    AMBIENT("상온", "상온", "ERP_STORAGE_TYPE.ORDINARY"), //
    ROOM("실온", "실온", "ERP_STORAGE_TYPE.ROOM"), //
    FREEZING("냉동", "냉동", "ERP_STORAGE_TYPE.FROZEN"), //
    REFRIGERATING("냉장", "냉장", "ERP_STORAGE_TYPE.COOL") //
    ;

    @JsonValue // 해당 속성의 값을 Json 직렬화시 값으로 지정
    private final String code;

    private final String codeName;

    private final String bosStorageTypeCode; // BOS 상에 등록된 보관방법 Code 값

    // ERP API 에서 사용하는 해당 코드의 키 값 => Json 데이터 내에서 해당 키 값 사용
    public static final String CODE_KEY = "tmpFlg";

    /*
     * Json => Java Object 역직렬화시 @JsonProperty 로 지정된 해당 코드에 해당하는 Enum 반환 메서드
     *
     * @Param String code : ERP API 에서 전송한 Json 데이터 내 CODE_KEY 에 해당하는 값
     */
    @JsonCreator // @JsonCreator로 지정된 static 메서드를 Jackson ObjectMapper 가 역직렬화시 사용함
    public static TemperatureTypes deserializeToEnumByCode( //
            @JsonProperty(TemperatureTypes.CODE_KEY) String code // ERP API 에서 전송한 온도구분
    ) {

        if (code == null) {
            return TemperatureTypes.NONE;
        }

        return EnumUtil.getEnum(TemperatureTypes.class, code);

    }

}
