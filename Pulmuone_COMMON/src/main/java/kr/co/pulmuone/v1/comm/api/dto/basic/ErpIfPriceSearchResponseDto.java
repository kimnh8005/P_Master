package kr.co.pulmuone.v1.comm.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonAlias;

import kr.co.pulmuone.v1.comm.api.constant.LegalTypes;
import kr.co.pulmuone.v1.comm.api.constant.SalesTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class ErpIfPriceSearchResponseDto {

    /*
     * ERP API 품목 가격 정보 조회 dto
     */

    @JsonAlias({ "srcSvr" })
    private String soureServer; // 입력 시스템 (예:ERP, ORGA …)

    @JsonAlias({ LegalTypes.CODE_KEY })
    private LegalTypes legalType; // ERP 법인코드

    @JsonAlias({ "itmNo" })
    private String erpItemNo; // ERP 상품번호 (Header 와 연결할 key 값)

    @JsonAlias({ "stDat" })
    private String erpPriceApplyStartDate; // 가격 적용 시작일 ( 8자리 년월일 )

    @JsonAlias({ "endDat" })
    private String erpPriceApplyEndDate; // 가격 적용 종료일 ( 8자리 년월일 )

    @Setter
    @Getter
    @JsonAlias({ "uniLstPrc" })
    private String erpStandardPrice; // 공급가

    @Setter
    @Getter
    @JsonAlias({ "uniStdPrc" })
    private String erpRecommendedPrice; // 판매가

    @JsonAlias({ "norPurPrc" })
    private String normalPurPrice; // 행사가격 조회시 최근 정상항목의 공급가 데이터

    @JsonAlias({ "norSelPrc" })
    private String normalSelPrice; // 행사가격 조회시 최근 정상항목의 판매가 데이터

    @JsonAlias({ "cur" })
    private String currency; // 통화 ( KRW )

    @JsonAlias({ SalesTypes.CODE_KEY })
    private SalesTypes erpSalesType; // 행사구분 (정상 / 행사)

    @JsonAlias({ "ifSeq" })
    private Integer ifSeq; // 중계서버 DB 테이블의 unique 키

//    @JsonAlias({ "shiToOrgNam" })
//    private String erpSupplierName; // 납품처

//    @JsonAlias({ "prcGrp" })
//    private String priceGroup; //

//    @JsonAlias({ "itmUseYn" })
//    private Boolean erpItemUseYn; // 사용여부

    @JsonAlias({ "updFlg" })
    private Boolean updateFlag; // 정보 업데이트 여부(Y / N)

    @JsonAlias({ "updDat" })
    private String updateDate; // 정보 업데이트 일시

}
