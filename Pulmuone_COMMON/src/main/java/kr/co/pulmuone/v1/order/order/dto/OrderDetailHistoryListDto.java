package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 처리이력 리스트 관련 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 12.  석세동         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 처리이력 리스트 관련 Dto")
public class OrderDetailHistoryListDto {

    @ApiModelProperty(value = "처리이력PK")
    private long historyId;

    @ApiModelProperty(value = "주문상세번호")
    private long orderDetailId;

    @ApiModelProperty(value = "변경일자")
    private String regDate;

    @ApiModelProperty(value = "처리상태")
    private String procStatus;

	@ApiModelProperty(value = "작업자")
	private String userNm;

    @ApiModelProperty(value = "비고")
    private String histMsg;

}
