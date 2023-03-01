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
public enum SourceServerTypes implements CodeCommEnum { // ( ERP API ) 입력 시스템 코드 Enum

	//코드값
    NONE("", "오류"),
    ESHOP("ESHOP", "온라인몰"),
    OERP("OERP", "온라인물류ERP"),
    DMPHI("DMPHI", "DM PHI"),
    HITOK("HITOK", "하이톡"),
    ORGAOMS("ORGAOMS", "ORGA OMS"),
    LDSPHI("LDSPHI", "LDS PHI"),
    CJWMS("CJWMS", "CJ 물류"),
    ORGAERP("ORGAERP", "올가ERP");

    @JsonValue // 해당 속성의 값을 Json 직렬화시 값으로 지정
    private final String code; // ERP API 에서 전송하는 입력시스템 코드

    private final String codeName; // 입력시스템 명

    // ERP API 에서 사용하는 해당 코드의 키 값 => Json 데이터 내에서 해당 키 값 사용
    public static final String CODE_KEY = "srcSvr";

    /*
     * Json => Java Object 역직렬화시 @JsonProperty 로 지정된 해당 코드에 해당하는 Enum 반환 메서드
     *
     * @Param String code : ERP API 에서 전송한 Json 데이터 내 CODE_KEY 에 해당하는 값
     */
    @JsonCreator // @JsonCreator로 지정된 static 메서드를 Jackson ObjectMapper 가 역직렬화시 사용함
    public static SourceServerTypes deserializeToEnumByCode( //
            @JsonProperty(SourceServerTypes.CODE_KEY) String code // ERP API 에서 전송한 법인코드
    ) {

        if (code == null) {
            return SourceServerTypes.NONE;
        }

        return EnumUtil.getEnum(SourceServerTypes.class, code);

    }

}
