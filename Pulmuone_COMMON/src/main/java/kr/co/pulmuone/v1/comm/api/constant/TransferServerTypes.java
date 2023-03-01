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
public enum TransferServerTypes implements CodeCommEnum { // ( ERP API ) 전송 대상 시스템 코드 Enum

	//코드값
    NONE("", "오류"),
    FDM("FDM", "ERP 푸드머스"),
    PFF("PFF", "ERP 식품"),
    OGH("OGH", "ERP 올가"),
    HITOK("HITOK", "하이톡"),
    DMPHI("DMPHI", "DM PHI"),
    LDSPHI("LDSPHI", "LDS PHI"),
    ORGAOMS("ORGAOMS", "ORGA OMS"),
    TOKTOK("TOKTOK", "톡톡"),
	CJWMS("CJWMS", "CJ 물류"),
	ORGAERP("ORGAERP", "올가ERP");

    @JsonValue // 해당 속성의 값을 Json 직렬화시 값으로 지정
    private final String code; // ERP API 에서 전송하는 입력시스템 코드

    private final String codeName; // 입력시스템 명

    // ERP API 에서 사용하는 해당 코드의 키 값 => Json 데이터 내에서 해당 키 값 사용
    public static final String CODE_KEY = "crpCd";

    /*
     * Json => Java Object 역직렬화시 @JsonProperty 로 지정된 해당 코드에 해당하는 Enum 반환 메서드
     *
     * @Param String code : ERP API 에서 전송한 (법인코드)Json 데이터 내 CODE_KEY 에 해당하는 값
     * @JsonCreator로 지정된 static 메서드를 Jackson ObjectMapper 가 역직렬화시 사용함
     */
	@JsonCreator
	public static TransferServerTypes deserializeToEnumByCode(@JsonProperty(TransferServerTypes.CODE_KEY) String code) {
		if (code == null) return TransferServerTypes.NONE;
		return EnumUtil.getEnum(TransferServerTypes.class, code);
	}
}