package kr.co.pulmuone.v1.comm.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonAlias;

import kr.co.pulmuone.v1.comm.api.constant.LegalTypes;
import kr.co.pulmuone.v1.comm.api.constant.StockWarehouseTypes;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErpIfStockSearchResponseDto {

    /*
     * ERP API 재고 수량 조회 dto
     */

    @JsonAlias({ "srcSvr" })
    private String sourceServer; // 입력 시스템 (예:ERP, ORGA …)

    /*
     * ERP 재고 수량 조회시 출고처 ID 값 정의
     *
     * 802 : 식품 용인
     * 803 : 식품 백암
     * O10 : 올가 용인
     */
    @JsonAlias({ "shiFroOrgId" })
    private StockWarehouseTypes stockWarehouseType; // 출고처 ID

    @JsonAlias({ "inpDat" })
    private String inputDate; // 입력일 ( 8자리 년월일 )

    @JsonAlias({ "erpItmNo" })
    private String erpItemNo; // ERP 품목번호

    @JsonAlias({ "stkSetCnt" })
    private Integer stockClosingCount; // 전일 마감수량

    @JsonAlias({ "stkCfmCnt" })
    private Integer stockConfirmedCount; // 입고 확정량

    @JsonAlias({ "stkTyp" })
    private Integer stockType; // 푸드머스 제품만 전송가능 (D0, D1, D2)

    @JsonAlias({ "stkSchCnt" })
    private Integer stockScheduledCount; // 입고 예정수량

    @JsonAlias({ "wmsCnt" })
    private Integer wmsCount; // 오프라인 물류 수량

    @JsonAlias({ "slfLifDat" })
    private String shelfLifeDate; // 유통기한 ( 14자리 년월일시분초 )

    @JsonAlias({ "corCd" })
    private LegalTypes erpLegalType; // 법인코드 ( PGS,PFF,FDM,OGH )

    @JsonAlias({ "ifSeq" })
    private Integer interfaceSequence; // I/F 시퀀스 번호

    @JsonAlias({ "itfFlg" })
    private Boolean interfaceReceivingFlag; // I/F 수신 여부 (Y / N)

    @JsonAlias({ "itfDat" })
    private String interfaceReceivingDate; // I/F 수신 일시 (14자리 년월일시분초)

}
