package kr.co.pulmuone.v1.order.order.dto.mall;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimAttcInfoDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 리스트 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 20.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문상세 리스트 Response Dto")
public class MallOrderDetailListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문일자(등록일자)")
    private String createDt;

    @ApiModelProperty(value = "주문자정보")
    private MallOrderDto order;

    @ApiModelProperty(value = "주문배송지정보")
    private MallOrderDetailShippingZoneDto shippingAddress;

    @ApiModelProperty(value = "결제정보")
    private MallOrderDetailPayResultDto payInfo;

    @ApiModelProperty(value = "주문상세 목록")
	private	List<MallOrderDetailDeliveryTypeDto> orderDetailList;

    @ApiModelProperty(value = "증정품 목록")
    private	List<MallOrderDetailGoodsDto> giftGoodsList;

    @ApiModelProperty(value = "주문 취소/반품 신청내역")
    private List<MallOrderDetailClaimListDto> claimList;

    @ApiModelProperty(value = "할인정보 신청내역")
    private List<MallOrderDetailPayDiscountDto> discountList;

    @ApiModelProperty(value = "정기배송정보")
    private MallOrderRegularDto regularInfo;

    @ApiModelProperty(value = "반품 수거지")
    private MallClaimSendShippingZoneDto claimShippingAddress;

    @ApiModelProperty(value = "클레임 정보")
    private MallClaimInfoDto claimInfo;

    @ApiModelProperty(value = "클레임 첨부파일")
    private List<OrderClaimAttcInfoDto> claimAttcList;

    @ApiModelProperty(value = "클레임 결제정보")
    private MallOrderDetailPayResultDto claimPayInfo;


    @ApiModelProperty(value = "상품 리스트")
    List<MallOrderDetailGoodsDto> goodsDetailList;

    @ApiModelProperty(value = "선물하기정보")
    private OrderPresentDto present;
}