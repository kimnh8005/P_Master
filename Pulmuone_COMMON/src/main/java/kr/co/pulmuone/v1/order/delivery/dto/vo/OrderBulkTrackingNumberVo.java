package kr.co.pulmuone.v1.order.delivery.dto.vo;

import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* <PRE>
* Forbiz Korea
* 일괄송장정보 Vo
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 24.        이규한          	  최초작성
* =======================================================================
* </PRE>
*/

@Getter
@Setter
@ToString
public class OrderBulkTrackingNumberVo {

	@ApiModelProperty(value = "No")
	private int rowNum;

	@ApiModelProperty(value = "일괄송장입력PK")
	private Long odBulkTrackingNumberId;

	@ApiModelProperty(value = "전체건수")
	private int totalCnt;

	@ApiModelProperty(value = "정상건수")
	private int successCnt;

	@ApiModelProperty(value = "실패건수")
	private int failureCnt;

	@ApiModelProperty(value = "등록자ID")
	private Long createId;

	@ApiModelProperty(value = "로그인ID")
	private String loginId;

	@ApiModelProperty(value = "등록자명")
	private String createNm;

	@ApiModelProperty(value = "등록일")
	private String createDt;

	@ApiModelProperty(value = "원본파일명")
	private String originNm;
}
