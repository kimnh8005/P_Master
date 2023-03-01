package kr.co.pulmuone.v1.order.schedule.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErpIfCustordSerachLineResponseDto {

    /*
     * ERP API 스캐줄 dto
     */

	/* Response line */
    @JsonAlias({ "ordNoDtl" })
    private Integer ordNoDtl; //라인 번호(주문상세번호)

    @JsonAlias({ "schLinNo" })
    private Integer schLinNo; //스캐줄라인번호

    @JsonAlias({ "dlvReqDat" })
    private String dlvReqDat; //배송요청일

    @JsonAlias({ "erpItmNo" })
    private String erpItmNo; //품목코드

    @JsonAlias({ "ordCnt" })
    private String ordCnt; //수량

}
