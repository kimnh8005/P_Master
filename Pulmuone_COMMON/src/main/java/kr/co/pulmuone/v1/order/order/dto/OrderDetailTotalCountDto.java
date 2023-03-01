package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 응답 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 16.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문상세 카운트 정보 응답 Response Dto")
public class OrderDetailTotalCountDto extends BaseResponseDto {

    @ApiModelProperty(value = "주문별 카운트")
	private	int totalCnt;

    @ApiModelProperty(value = "주문상세 카운트")
    private	int lineCnt;

    @ApiModelProperty(value = "주문상세 상품 주문 건수 합")
    private	int orderGoodsCnt;

    @ApiModelProperty(value = "주문상세 상품 취소 건수 합")
    private	int cancelGoodsCnt;
}
