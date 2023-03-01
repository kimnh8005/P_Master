package kr.co.pulmuone.v1.order.regular.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 상품 건너뛰기/취소 정보 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 17.    김명진            최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 상품 건너뛰기/취소 정보 Response Dto")
public class RegularReqGoodsSkipCancelResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "상품PK")
	private long ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "상품이미지")
	private String thumbnailPath;

	@ApiModelProperty(value = "상품주문금액")
	private int goodsSalePrice;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "배송정책명")
	private String deliveryTmplNm;

	@ApiModelProperty(value = "변동배송비")
	private int changeShippingPrice;

	@ApiModelProperty(value = "총배송비")
	private int shippingPrice;

	@ApiModelProperty(value = "총상품금액")
	private int recommendedPrice;

	@ApiModelProperty(value = "주문금액")
	private int orderPrice;

	@ApiModelProperty(value = "총할인금액")
	private int totDiscountPrice;

	@ApiModelProperty(value = "결제예정금액")
	private int paidPrice;

	@ApiModelProperty(value = "정기배송기본할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "정기배송기본할인율")
	private int discountRate;

	@ApiModelProperty(value = "정기배송추가할인금액")
	private int addDiscountPrice;

	@ApiModelProperty(value = "정기배송추가할인율")
	private int addDiscountRate;

	@ApiModelProperty(value = "정기배송추가할인회차")
	private int addDiscountReqRound;

	@ApiModelProperty(value = "현재회차")
	private int reqRound;

	@ApiModelProperty(value = "추가할인기준회차")
	private int addDiscountStdReqRound;

	@ApiModelProperty(value = "상품결제여부")
	private String paymentYn;

	@ApiModelProperty(value = "상품 할인정보 목록")
	private List<RegularResultGoodsDiscountListDto> discountList;
}
