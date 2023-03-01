package kr.co.pulmuone.v1.comm.api.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import kr.co.pulmuone.v1.comm.enums.CodeCommEnum;
import kr.co.pulmuone.v1.comm.util.EnumUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum SupplierTypes implements CodeCommEnum { // 공급업체 Enum : ERP API 로 수신된 정보 기반으로 판단함

    /**
     * 공급업체
     */

    NONE("", "미지정"),

    /**
     * 풀무원식품
     */
    FOOD("1", "풀무원식품"),

    /**
     * 올가홀푸드
     */
    ORGA("2", "올가홀푸드"),

    /**
     * 풀무원푸드머스
     */
    FOODMERCE("3", "풀무원푸드머스"),

    /**
     * 풀무원녹즙1, 풀무원녹즙(FDD)
     */
    FDD("4", "풀무원녹즙(FDD)"),

    /**
     * 풀무원녹즙2, 풀무원녹즙(PDM)
     */
    PDM("5", "풀무원녹즙(PDM)"),

    /**
     * CAF, 씨에이에프
     */
    CAF("6", "씨에이에프"),

    /**
     * 풀무원건강생활
     */
    LOHAS("7", "풀무원건강생활"),

    /**
     * 풀무원 샘물
     */
    WATER("8", "풀무원샘물");

    @JsonValue // 해당 속성의 값을 Json 직렬화시 값으로 지정
    private final String code; // BOS 상에 등록된 공급업체 PK 값

    private final String codeName;

    /*
     * Json => Java Object 역직렬화시 @JsonProperty 로 지정된 해당 코드에 해당하는 Enum 반환 메서드
     *
     * SupplierTypes 을 사용하는 dto 의 @JsonCreator 로 지정된 생성자 안에서 호출해야 함
     *
     * @Param String legalTypeJsonValue : Json 데이터 내 ERP API 에서 전송한 법인코드 값
     *
     * @Param String taxInvoiceTypeJsonValue : Json 데이터 내 ERP API 에서 전송한 세금계산서 발행구분 값
     */
    public static SupplierTypes deserializeToEnumByCode( //
            String legalTypeJsonValue // Json 데이터 내 ERP API 에서 전송한 법인코드 값
            , String taxInvoiceTypeJsonValue // Json 데이터 내 ERP API 에서 전송한 세금계산서 발행구분 값
    ) {

        LegalTypes legarType = null;
        TaxInvoiceTypes taxInvoiceType = null;

        try {

            // 법인코드 Enum 검색 : 전달받은 값이 null 인 경우 LegalTypes.NONE 반환
            legarType = (legalTypeJsonValue == null ? //
                    LegalTypes.NONE : EnumUtil.getEnum(LegalTypes.class, legalTypeJsonValue) //
            );

            // 세금계산서 발행구분 Enum 검색 : 전달받은 값이 null 인 경우 TaxInvoiceTypes.NONE 반환
            taxInvoiceType = (taxInvoiceTypeJsonValue == null ? //
                    TaxInvoiceTypes.NONE : EnumUtil.getEnum(TaxInvoiceTypes.class, taxInvoiceTypeJsonValue) //
            );

            return getSupplier(legarType, taxInvoiceType);

        } catch (IllegalArgumentException e) { // 존재하지 않는 Enum 인 경우 IllegalArgumentException 발생

            return NONE; // 공급업체 미지정 ( NONE ) 반환

        }

    }

    /*
     * 법인코드, 세금계산서 발생구분에 따른 공급업체 구분 메서드
     */
    private static SupplierTypes getSupplier(LegalTypes legalType, TaxInvoiceTypes taxInvoiceType) {

        switch (legalType) {

        case FOOD:

            return SupplierTypes.FOOD;

        case ORGA:

            return SupplierTypes.ORGA;

        case FOODMERCE:

            return SupplierTypes.FOODMERCE;

        case LOHAS:

            return getSupplierTypeByTaxInvoiceType(taxInvoiceType);

        case NONE:
        default:

            return NONE;

        }

    }

    /**
     * 건강생활 ERP 인 경우, 세금계산서 발행구분값으로 공급업체 확인
     */
    private static SupplierTypes getSupplierTypeByTaxInvoiceType(TaxInvoiceTypes taxInvoiceType) {

        switch (taxInvoiceType) {

        case GREEN_JUICE: // 녹즙정품

            return FDD;

        case GREENCHE: // 그린체정품

            return LOHAS;

        case CAF: // 월합

            return CAF;

        case BABY_FOOD: // 이유식

            return PDM;

        case NONE: //
        case POS: // POS/홈쇼핑
        case TASTING: // 시식/시음/기타
        case UNDEFINED: // 미정의

            return NONE;

        default:

            return NONE;

        }

    }
}
