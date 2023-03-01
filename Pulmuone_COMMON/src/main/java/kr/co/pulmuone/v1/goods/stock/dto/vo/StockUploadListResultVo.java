package kr.co.pulmuone.v1.goods.stock.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ERP 재고 엑셀 업로드 내역 Result Vo")
public class StockUploadListResultVo {

	@ApiModelProperty(value = "엑셀로드 ID")
	private String ilStockExcelUploadLogId;

	@ApiModelProperty(value = "성공건수")
    private String successCnt;

	@ApiModelProperty(value = "실패건수")
    private String failCnt;

	@ApiModelProperty(value = "등록일")
	private String createDt;

	@ApiModelProperty(value = "관리자명")
	@UserMaskingUserName
	private String userNm;

	@ApiModelProperty(value = "관리자 로그인ID")
	@UserMaskingLoginId
	private String loginId;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "오류내역")
	private String msg;

}
