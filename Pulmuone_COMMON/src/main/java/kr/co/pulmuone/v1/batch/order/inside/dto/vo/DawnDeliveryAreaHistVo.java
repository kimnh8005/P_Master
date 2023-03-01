package kr.co.pulmuone.v1.batch.order.inside.dto.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 새벽배송권역 이력  Vo
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
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "새벽배송권역 이력  Vo")
public class DawnDeliveryAreaHistVo {

	@ApiModelProperty(value = "우편번호")
	private String zipCd;

	@ApiModelProperty(value = "건물번호")
	private String buildingNo;

	@ApiModelProperty(value = "구분 : 삭제, 추가")
	private String actTp;

	@ApiModelProperty(value = "비고")
	private String remark;

}
