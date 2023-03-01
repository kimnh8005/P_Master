package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 매장정보 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.06.10.      최윤지         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@ApiModel(description = "주문 상세 매장정보 VO")
public class OrderShopStoreVo {

	@ApiModelProperty(value = "매장명")
    private String urStoreNm;
    
	@ApiModelProperty(value = "배송유형")
    private String deliveryType;
	
	@ApiModelProperty(value = "매장(배송/픽업) 회차")
    private long storeScheduleNo;

    @ApiModelProperty(value = "매장(배송/픽업) - 주문배송시작시간")
    private String storeStartTime;

    @ApiModelProperty(value = "매장(배송/픽업) - 주문배송종료시간")
    private String storeEndTime;

    @ApiModelProperty(value = "매장(배송/픽업) - 도착예정일")
    private String deliveryDt;
    
}
