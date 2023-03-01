package kr.co.pulmuone.v1.batch.order.inside.dto.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 새벽배송권역 추가 또는 삭제 결과 조회 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 18.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "새벽배송권역 추가 또는 삭제 결과 조회 Dto")
public class DawnDeliveryAreaTempVo {

	@ApiModelProperty(value = "우편번호")
	private long zipCd;

	@ApiModelProperty(value = "건물번호")
	private String buildingNo;

}
