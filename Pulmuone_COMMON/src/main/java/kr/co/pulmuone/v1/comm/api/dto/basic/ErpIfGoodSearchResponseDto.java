package kr.co.pulmuone.v1.comm.api.dto.basic;

import java.io.IOException;
import java.text.DecimalFormat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import kr.co.pulmuone.v1.comm.api.constant.LegalTypes;
import kr.co.pulmuone.v1.comm.api.constant.O2oExposureTypes;
import kr.co.pulmuone.v1.comm.api.constant.OrganizationTypes;
import kr.co.pulmuone.v1.comm.api.constant.ProductTypes;
import kr.co.pulmuone.v1.comm.api.constant.PurchaseOrderTypes;
import kr.co.pulmuone.v1.comm.api.constant.SupplierTypes;
import kr.co.pulmuone.v1.comm.api.constant.TaxInvoiceTypes;
import kr.co.pulmuone.v1.comm.api.constant.TaxTypes;
import kr.co.pulmuone.v1.comm.api.constant.TemperatureTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class ErpIfGoodSearchResponseDto {

    /*
     * ERP API 상품정보 조회 dto
     */

    @JsonCreator // 해당 생성자를 Jackson ObjectMapper 가 역직렬화시 사용함
    public ErpIfGoodSearchResponseDto( //
            @JsonProperty(LegalTypes.CODE_KEY) final String legalType // Json 데이터 내 ERP API 에서 전송한 법인코드 값
            , @JsonProperty(TaxInvoiceTypes.CODE_KEY) final String erpTaxInvoiceType // Json 데이터 내 ERP API 에서 전송한 세금계산서 발행구분 값
    ) {

        super();

        /*
         * 역직렬화시 @JsonCreator 로 지정된 현재 생성자를 사용하여 법인코드, 세금계산서 발행구분 Enum 의 @JsonCreator 메서드는 동작하지 않음
         * => 현재 생성자 안에서 별도 세팅
         */
        this.erpLegalType = LegalTypes.deserializeToEnumByCode(legalType); // 법인코드 Enum 값 지정
        this.taxInvoiceType = TaxInvoiceTypes.deserializeToEnumByCode(erpTaxInvoiceType); // 세금계산서 발행구분 Enum 지정

        // Json 데이터 내 ERP API 에서 전송한 법인코드, 세금계산서 발행 구분 값으로 공급업체 Enum 세팅
        this.supplierType = SupplierTypes.deserializeToEnumByCode(legalType, erpTaxInvoiceType);

    }

    @JsonAlias({ "srcSvr" })
    private String sourceServer; // 입력 시스템(예:ERP, ORGA …)

    // @JsonCreator 에서 값 세팅하므로 @JsonAlias 미지정
    @JsonProperty("legalType")
    private LegalTypes erpLegalType; // 법인코드 (PGS,PFF,FDM,OGH) -- 건생,식품,푸드머스,올가 순으로 보면됨

    @JsonAlias({ "cat1" })
    private String erpCategoryLevel1Id; // 카테고리1

    @JsonAlias({ "cat2" })
    private String erpCategoryLevel2Id; // 카테고리2

    @JsonAlias({ "cat3" })
    private String erpCategoryLevel3Id; // 카테고리3

    @JsonAlias({ "cat4" })
    private String erpCategoryLevel4Id; // 카테고리4

    @JsonAlias({ "itmNam" })
    private String erpItemName; // ERP 품목명

    @JsonAlias({ "itmNo" })
    private String erpItemNo; // ERP 품목번호

    @JsonAlias({ "kanCd" })
    private String erpItemBarcode; // KANCODE, 품목 바코드

    @JsonAlias({ "spl" })
    private String erpSupplierName; // ERP 상 공급업체명

    // @JsonCreator 에서 값 세팅하므로 @JsonAlias 미지정
    @Setter
    @JsonProperty("erpTaxInvoiceType")
    private TaxInvoiceTypes taxInvoiceType; // 세금계산서 발행구분 (POS/홈쇼핑 , 미정의,시식/시음/기타,이유식,녹즙정품,월합,그린체정품,NULL)

    @JsonAlias({ "brn" })
    private String erpBrandName; // 브랜드

    @Setter
    @JsonAlias({ "dpBrn" })
    private String dpBrandName; // 전시브랜드

    @JsonAlias({ "itmTyp" })
    private String erpItemGroup; // 상품군

    @JsonAlias({ TemperatureTypes.CODE_KEY })
    private TemperatureTypes erpStorageMethod; // 온도구분 (상온,미정,냉동,냉장,실온,NULL)

    @Setter
    @JsonAlias({ "ori" })
    private String erpOriginName; // 원산지

    @JsonAlias({ "oriDtl" })
    private String erpOriginDetailName; // 원산지 상세 ( 해외일 경우에만 )

    @JsonAlias({ "shlLif" })
    private Integer erpDistributionPeriod; // 유통기한

    @JsonAlias({ "boxHor" })
    @JsonSerialize(using = ErpIfGoodSearchResponseDto.DoubleSerializer.class)
    private Double erpBoxWidth; // 박스_가로 ( ERP 에서는 소숫점 2자리까지 존재 : 수집시는 소숫점 3자리까지 수집 )

    @JsonAlias({ "boxVer" })
    @JsonSerialize(using = ErpIfGoodSearchResponseDto.DoubleSerializer.class)
    private Double erpBoxDepth; // 박스_세로 ( ERP 에서는 소숫점 2자리까지 존재 : 수집시는 소숫점 3자리까지 수집 )

    @JsonAlias({ "boxHei" })
    @JsonSerialize(using = ErpIfGoodSearchResponseDto.DoubleSerializer.class)
    private Double erpBoxHeight; // 박스_높이 ( ERP 에서는 소숫점 2자리까지 존재 : 수집시는 소숫점 3자리까지 수집 )

    @JsonAlias({ "itmHor" })
    @JsonSerialize(using = ErpIfGoodSearchResponseDto.DoubleSerializer.class)
    private Double erpItemWidth; // 품목 상품 낱개 가로 ( ERP 에서는 소숫점 2자리까지 존재 : 수집시는 소숫점 3자리까지 수집 )

    @JsonAlias({ "itmVer" })
    @JsonSerialize(using = ErpIfGoodSearchResponseDto.DoubleSerializer.class)
    private Double erpItemDepth; // 품목 상품 낱개 세로 ( ERP 에서는 소숫점 2자리까지 존재 : 수집시는 소숫점 3자리까지 수집 )

    @JsonAlias({ "itmHei" })
    @JsonSerialize(using = ErpIfGoodSearchResponseDto.DoubleSerializer.class)
    private Double erpItemHeight; // 품목 상품 낱개 높이 ( ERP 에서는 소숫점 2자리까지 존재 : 수집시는 소숫점 3자리까지 수집 )

    @JsonAlias({ "eaDisWei" })
    @JsonSerialize(using = ErpIfGoodSearchResponseDto.DoubleSerializer.class)
    private Double itemDispWeight; // 단품표시중량 ( ERP 에서는 소숫점 2자리까지 존재 : 수집시는 소숫점 3자리까지 수집 )

    @JsonAlias({ "eaRelWei" })
    @JsonSerialize(using = ErpIfGoodSearchResponseDto.DoubleSerializer.class)
    private Double itemRealWeight; // 단품실중량 ( ERP 에서는 소숫점 2자리까지 존재 : 수집시는 소숫점 3자리까지 수집 )

    @JsonAlias({ "boxEa" })
    private Integer erpPiecesPerBox; // 입수수량, 박스 입수량

    @Setter
    @JsonAlias({ "stdPrc" })
    private String erpStandardPrice; // 표준단가, 원가

    @Setter
    @JsonAlias({ "stdSelPrc" })
    private String erpRecommendedPrice; // 권장소비자가, 정상가

    @JsonAlias({ TaxTypes.CODE_KEY })
    private TaxTypes erpTaxType; // 과세구분 (과세[영업사용],서울-면세일반,본점-과세일반,면세[영업사용],NULL)

    /*
     * ERP 발주유형
     *
     * 올가(OGH) - 센터(R1) / 센터(R2) / 매장(R3)
     * 푸드머스(FDM) - D-0 / D-1 / D-2 / D-3 / D-4 / D-5 / D-7 / ST (양지재고대응품목)
     */
    @JsonAlias({ PurchaseOrderTypes.CODE_KEY })
    private PurchaseOrderTypes erpPoType; // ERP 발주유형

    @JsonAlias({ "poFlg" })
    private Boolean erpCanPoYn; // 발주가능여부 (품목의 종료일자와 연동되는 값임) (Y,N)

    @JsonAlias({ "uom" })
    private String erpUnitOfMeasurement; // ERP UOM ( 측정단위 )

    @JsonAlias({ OrganizationTypes.CODE_KEY })
    private OrganizationTypes organizationType; // 온라인 통합몰 구분값 (228,165,3836,3810) -- 건생, 식품, 올가, 푸드머스

    @JsonAlias({ "useOshYn" })
    private Boolean useOnlineShopYn; // 온라인 통합몰 취급 상품 여부 (온라인 통합몰 사용 시 SHOP)

    @JsonAlias({ O2oExposureTypes.CODE_KEY })
    private O2oExposureTypes o2oExposureType; // 매장품목유형

    @JsonAlias({ ProductTypes.CODE_KEY })
    private ProductTypes productType; // 제상품구분 (제품,상품), 상품판매유형

    @JsonAlias({ "updFlg" })
    private Boolean updateFlag; // 정보 업데이트 여부(Y / N)

    @JsonAlias({ "updDat" })
    private String updateDate; // 정보 업데이트 일시

    /*
     * ERP API 수신 데이터 외 정보
     */

    @JsonProperty("bosSupplier")
    private SupplierTypes supplierType; // 공급업체 : @JsonCreator 생성자에서 값 세팅

    @JsonProperty("bosSupplierName")
    private String bosSupplierName() { // 공급업체명
        return this.supplierType.getCodeName();
    }

    @Setter
    @JsonProperty("isRegistrationAvailable")
    private boolean isRegistrationAvailable; // BOS 상에 등록 가능한 품목코드인지 여부 : Service 단에서 세팅

    @Setter
    @JsonProperty("registerationAvailableMsg")
    private String registerationAvailableMsg; // BOS 상에 등록된 품목코드 선택 불가 사유

    @JsonProperty("hasTax")
    public boolean hasTax() { // 과세/면세 구분
        return TaxTypes.hasTax(this.erpTaxType);
    }

    @JsonProperty("o2oExposureTypeName")
    public String o2oExposureTypeName() { // 매장품목유형 이름

        if (this.o2oExposureType != null) {
            return this.o2oExposureType.getCodeName();
        } else {
            return null;
        }

    }

    @JsonProperty("bosStorageTypeCode")
    public String bosStorageTypeCode() { // BOS 상에 등록된 보관방법 Code 값

        if (this.erpStorageMethod != null) {
            return this.erpStorageMethod.getBosStorageTypeCode();
        } else {
            return null;
        }

    }

    /*
     * 현재 클래스에서 사용할 Double Type Serializer
     *
     * => DecimalFormat 으로 소수점 넷째 자리에서 반올림 ( 셋째자리까지 표현 ), 소수점 마지막 0 은 출력값에서 제외
     *
     */
    private static class DoubleSerializer extends JsonSerializer<Double> {

        private DecimalFormat df = new DecimalFormat("#.###"); // DecimalFormat : 소수점 이하 셋째자리까지 표현

        @Override
        public void serialize(Double originalValue, JsonGenerator jgen, SerializerProvider provider) throws IOException {

            jgen.writeString(df.format(originalValue));

        }
    }

}
