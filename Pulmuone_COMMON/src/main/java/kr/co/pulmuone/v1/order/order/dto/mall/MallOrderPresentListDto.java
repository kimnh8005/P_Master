package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 보낸선물함 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 07. 19.	홍진영		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 보낸선물함 리스트 Dto")
public class MallOrderPresentListDto {

	@ApiModelProperty(value = "주문 PK")
	private String odOrderId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문일자(등록일시)/주문 클레임(등록일시), YYYY-MM-DD")
	private String createDt;

    @ApiModelProperty(value = "대표 상품 PK")
    private String ilGoodsId;

	@ApiModelProperty(value = "대표 상품 이미지")
	private String repGoodsImg;

	@ApiModelProperty(value = "대표 상품의 주문상태")
	private String repGoodsOrderStatusNm;

	@ApiModelProperty(value = "대표 상품의 주문상태 코드명")
	private String repGoodsOrderStatusCd;

	@ApiModelProperty(value = "주문 총 상품개수 -1")
	private int orderGoodsCount;

	@ApiModelProperty(value = "대표 상품명")
	private String goodsNm;

	@ApiModelProperty(value = "선물받는사람명")
	private String presentReceiveNm;

	@ApiModelProperty(value = "선물받는사람핸드폰번호")
	private String presentReceiveHp;

	@ApiModelProperty(value = "선물하기상태")
	private String presentOrderStatus;

	@ApiModelProperty(value = "메시지 재발송 횟수")
	private int presentMsgSendCnt;
}