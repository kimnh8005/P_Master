package kr.co.pulmuone.v1.order.email.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.email.dto.vo.BosOrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderRegularReqInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 자동메일 발송위한 주문 정보 DTO")
public class OrderInfoForEmailResultDto {

    @ApiModelProperty(value = "주문정보, 결제정보, 배송지정보 VO")
    private OrderInfoForEmailVo orderInfoVo;

    @ApiModelProperty(value = "주문상세 목록")
	private	List<OrderDetailShippingTypeDto> orderDetailList;

    @ApiModelProperty(value = "증정품 목록")
	private List<OrderDetailGoodsDto> orderGiftList;

    @ApiModelProperty(value = "SMS 발송위한  주문 정보 Dto")
    private OrderInfoForSmsDto orderInfoForSmsDto;

    @ApiModelProperty(value = "정기배송 전용 주문정보, 결제정보, 배송지 정보 VO")
    private OrderRegularReqInfoVo orderRegularReqInfoVo;

    @ApiModelProperty(value = "정기배송주문신청 결과 DTO")
    private OrderRegularResultDto orderRegularResultDto;

    @ApiModelProperty(value = "정기배송주문신청 결과 리스트")
    private List<OrderRegularResultDto> orderRegularResultList;

    @ApiModelProperty(value = "정기배송 상품가격 변동 정보 DTO")
    private OrderRegularGoodsPriceChangeDto orderRegularGoodsPriceChangeDto;

    @ApiModelProperty(value = "프론트 구분 (0 : Bos, 1:Front 2:Batch)")
    private int frontTp;

    @ApiModelProperty(value = "BOS 알림 메일 정보 VO")
    private BosOrderInfoForEmailVo bosOrderInfoForEmailVo;
}
