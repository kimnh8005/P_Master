package kr.co.pulmuone.v1.order.regular.dto.vo;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 히스토리 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 07.	김명진 		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 히스토리 VO")
public class OrderRegularReqHistoryVo {

	@ApiModelProperty(value = "정기배송주문 히스토리 PK")
	private long odRegularReqHistoryId;

	@ApiModelProperty(value = "정기배송주문신청 PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "구분")
	private String regularReqGbnCd;

	@ApiModelProperty(value = "처리상태")
	private String regularReqStatusCd;

	@ApiModelProperty(value = "내용")
	private String regularReqCont;

	@ApiModelProperty(value = "등록자")
	private long createId;

	@ApiModelProperty(value = "등록일")
	private LocalDate createDt;
}
