package kr.co.pulmuone.v1.batch.order.inside.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.batch.order.inside.dto.vo.DawnDeliveryAreaVo;
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
public class DawnDeliveryInfoDto {

	List<DawnDeliveryAreaVo> tempList;

	List<DawnDeliveryAreaVo> insertList;

	List<DawnDeliveryAreaVo> deleteList;

	List<DawnDeliveryAreaVo> histList;

	@ApiModelProperty(value = "추가 / 삭제 구분")
	private String addDeleteTp;

	@ApiModelProperty(value = "추가 / 삭제 구분 명")
	private String addDeleteTpNm;
}
