package kr.co.pulmuone.v1.comm.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErpIfGoodsOrgaPoResponseDto {

	@JsonAlias({ "srcSvr" })
    private String sourceServer; 	// 입력 시스템(예:ERP, ORGA …)

	@JsonAlias({ "itmNo" })
    private String erpItemNo; 		// ItemNo

	@JsonAlias({ "updFlg" })
    private String updFlg; 			// 수정여부

	@JsonAlias({ "inDat" })
    private String inDat; 			// 입고예정일

	@JsonAlias({ "updDat" })
    private String updDat; 			// 수정일

	@JsonAlias({ "ifSeq" })
    private String ifSeq;

	@JsonAlias({ "reqDat" })
    private String reqDat; 			// 발주일

	@JsonAlias({ "itfFlg" })
    private String itfFlg; 			// I/F 수신 여부(Y / N)

	@JsonAlias({ "outDat" })
    private String outDat; 			// PO요청일

	@JsonAlias({ "rn" })
    private String rn;

    @JsonAlias({ "ordStpYn" })
    private String orderStopYn; // 발주금지여부(Y/N)

}
