package kr.co.pulmuone.v1.order.claim.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimAttcVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상품정보 조회 > 첨부파일 리스트 관련 dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02.16.	최윤지		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ApiModel(description = "주문 클레임 상품정보 조회 > 첨부파일 리스트 관련 dto")
public class OrderClaimAttcListDto {

	@ApiModelProperty(value = "첨부파일 리스트")
	private List<ClaimAttcVo> rows;
}
