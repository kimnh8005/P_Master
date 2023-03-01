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
public enum PurchaseOrderTypes implements CodeCommEnum { // ( ERP API ) 발주유형 Enum

    /*
     * ERP 발주유형
     *
     * 올가(OGH) - 센터(R1) / 센터(R2) / 매장(R3)
     * 푸드머스(FDM) - D-0 / D-1 / D-2 / D-3 / D-4 / D-5 / D-7 / ST (양지재고대응품목)
     */

    NONE("", ""), //

    // 올가 (OGH)
    OGH_R1("센터(R1)", "센터(R1)"), //
    OGH_R2("센터(R2)", "센터(R2)"), //
    OGH_R3("매장(R3)", "매장(R3)"), //

    // 푸드머스 (FDM)
    FDM_D_0("D-0", "D-0"), //
    FDM_D_1("D-1", "D-1"), //
    FDM_D_2("D-2", "D-2"), //
    FDM_D_3("D-3", "D-3"), //
    FDM_D_4("D-4", "D-4"), //
    FDM_D_5("D-5", "D-5"), //
    FDM_D_7("D-7", "D-7"), //
    FDM_ST("ST", "ST") // 양지재고대응품목
    ;

    @JsonValue // 해당 속성의 값을 Json 직렬화시 값으로 지정
    private final String code;

    private final String codeName;

    // ERP API 에서 사용하는 해당 코드의 키 값 => Json 데이터 내에서 해당 키 값 사용
    public static final String CODE_KEY = "poTyp";

    /*
     * Json => Java Object 역직렬화시 @JsonProperty 로 지정된 해당 코드에 해당하는 Enum 반환 메서드
     *
     * @Param String code : ERP API 에서 전송한 Json 데이터 내 CODE_KEY 에 해당하는 값
     */
    @JsonCreator // @JsonCreator로 지정된 static 메서드를 Jackson ObjectMapper 가 역직렬화시 사용함
    public static PurchaseOrderTypes deserializeToEnumByCode( //
            @JsonProperty(PurchaseOrderTypes.CODE_KEY) String code // ERP API 에서 전송한 발주유형
    ) {

        if (code == null) {
            return PurchaseOrderTypes.NONE;
        }

        return EnumUtil.getEnum(PurchaseOrderTypes.class, code);

    }

}
