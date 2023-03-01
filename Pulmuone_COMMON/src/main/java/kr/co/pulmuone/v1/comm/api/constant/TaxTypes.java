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
public enum TaxTypes implements CodeCommEnum { // ( ERP API ) 과세구분 Enum

    NONE("", ""), //
    TAX1("과세[영업사용]", "과세[영업사용]"), //
    TAX2("서울-면세일반", "서울-면세일반"), //
    TAX3("본점-과세일반", "본점-과세일반"), //
    TAX4("면세[영업사용]", "면세[영업사용]");

    @JsonValue // 해당 속성의 값을 Json 직렬화시 값으로 지정
    private final String code; // ERP API 에서 전송하는 세금계산서 발행구분

    private final String codeName;

    // ERP API 에서 사용하는 해당 코드의 키 값 => Json 데이터 내에서 해당 키 값 사용
    public static final String CODE_KEY = "taxCd";

    /*
     * Json => Java Object 역직렬화시 @JsonProperty 로 지정된 해당 코드에 해당하는 Enum 반환 메서드
     *
     * @Param String code : ERP API 에서 전송한 Json 데이터 내 CODE_KEY 에 해당하는 값
     */
    @JsonCreator // @JsonCreator로 지정된 static 메서드를 Jackson ObjectMapper 가 역직렬화시 사용함
    public static TaxTypes deserializeToEnumByCode( //
            @JsonProperty(TaxTypes.CODE_KEY) String code // ERP API 에서 전송한 과세구분
    ) {
        if (code == null) {
            return TaxTypes.NONE;
        }

        return EnumUtil.getEnum(TaxTypes.class, code);
    }

    /*
     * 마스터 품목 등록 화면에서 과세구분의 "과세" (true), "면세" (false) 판단 메서드
     */
    public static boolean hasTax(TaxTypes erpTaxType) {

        if (erpTaxType == null) {
            return true; // 기본값 : 과세
        }

        switch (erpTaxType) {

        case NONE:
        case TAX1: // 과세[영업사용]
        case TAX3: // 본점-과세일반

            return true; // 과세

        case TAX2: // 서울-면세일반
        case TAX4: // 면세[영업사용]

            return false; // 면세

        default:
            return true; // 위에 해당하지 않는 경우 : 과세

        }

    }

}
