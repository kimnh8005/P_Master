package kr.co.pulmuone.v1.comm.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpIfPoSearchResponseDto {

    /*
     * 올가R2발주스케쥴 조회 dto
     */

    @JsonAlias({ "srcSvr" })
    private String sourceServer; // 입력 시스템 (예:ERP, ORGA …)

    @JsonAlias({ "inDat" })
    private String inputDay; // 입고예정일 (월,화,수,목,금..)

    @JsonAlias({ "reqDat" })
    private String reqDay; // 발주일 (월,화,수,목,금..)

    @JsonAlias({ "itmNo" })
    private String ilItemCd; // ERP 품목번호

    @JsonAlias({ "outDat" })
    private String outDat; // PO요청일 (월,화,수,목,금..)

    @JsonAlias({ "ifSeq" })
    private Integer interfaceSequence; // I/F 시퀀스 번호

    @JsonAlias({ "itfFlg" })
    private Boolean interfaceReceivingFlag; // I/F 수신 여부 (Y / N)

    @JsonAlias({ "ordStpYn" })
    private String orderStopYn; // 발주금지여부(Y/N)

}
