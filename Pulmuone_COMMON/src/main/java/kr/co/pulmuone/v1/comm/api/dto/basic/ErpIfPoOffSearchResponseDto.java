package kr.co.pulmuone.v1.comm.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpIfPoOffSearchResponseDto {

    /*
     * 올가오프라인발주상태 조회 dto
     */
    @JsonAlias({ "srcSvr" })
    private String sourceServer; // 입력 시스템 (예:ERP, ORGA …)

    @JsonAlias({ "kanCd" })
    private String ilGoodsId;//상품코드

    @JsonAlias({ "itmCd" })
    private String ilItemCd; // ERP 품목번호

    @JsonAlias({ "itmDes" })
    private String itemNm; //품목명

    @JsonAlias({ "vndId" })
    private String venderId; //협력업체ID

    @JsonAlias({ "vndNam" })
    private String venderNm; //협력업체명

    @JsonAlias({ "vndSitCd" })
    private String venderSiteCd; //사업자코드

    @JsonAlias({ "effStDat" })
    private String startDt; // 시작일

    @JsonAlias({ "poProRea" })
    private String poProRea; // 사유

    @JsonAlias({ "ifSeq" })
    private Integer interfaceSequence; // I/F 시퀀스 번호

    @JsonAlias({ "itfFlg" })
    private Boolean interfaceReceivingFlag; // I/F 수신 여부 (Y / N)

}
