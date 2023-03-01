package kr.co.pulmuone.v1.order.claim.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 15.    천혜현        최초작성
 *
 *
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "미출 주문 상세리스트 > 일괄 취소완료,재배송 팝업 저장 Dto")
public class OrderChangeClaimStatusList{

	@ApiModelProperty(value = "일괄 취소완료,재배송 팝업 정보 리스트")
	private List<OrderChangeClaimStatusCCDto> claimDataList;

}

