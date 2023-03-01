package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsCertificationListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsNutritionListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsSpecListResultVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "장바구니 옵션 항목 응답 DTO")
public class GetOptionInfoResponseDto
{

	@ApiModelProperty(value = "상품 타입")
	private String goodsType;

	@ApiModelProperty(value = "판매유형")
	private String saleType;

	@ApiModelProperty(value = "상품 상태")
	private String saleStatus;

	@ApiModelProperty(value = "최소 구매수량")
	private int limitMinCnt;

	@ApiModelProperty(value = "최대 구매 수량 제한 여부")
	private String limitMaxCntYn;

	@ApiModelProperty(value = "최대 구매 수량 유형")
	private String limitMaxType;

	@ApiModelProperty(value = "최대 구매 수량 (설정값)")
	private int systemLimitMaxCnt;

	@ApiModelProperty(value = "최대 구매 수량 (주문수량 계산)")
	private int limitMaxCnt;

	@ApiModelProperty(value = "재고수량")
	private int stockQty;

	@ApiModelProperty(value = "매장 재고 수량")
	private int storeStockQty;

	@ApiModelProperty(value = "추가 상품 리스트")
	private List<AdditionalGoodsDto> additionalGoodsList;

	@ApiModelProperty(value = "예약판매 옵션 리스트")
	private List<GoodsReserveOptionListDto> reserveOption;

	@ApiModelProperty(value = "일일상품 유형")
	private String goodsDailyType;

	@ApiModelProperty(value = "일일배송 주기 정보 리스트")
	private List<GoodsDailyCycleDto> goodsDailyCycle;

	@ApiModelProperty(value = "알러지 식단 선택여부")
	private String goodsDailyAllergyYn;

	@ApiModelProperty(value = "일괄 배송 가능 여부")
	private String goodsDailyBulkYn;

	@ApiModelProperty(value = "일괄배송 배송 세트 정보 리스트")
	private List<HashMap<String,String>> goodsDailyBulk;

	@ApiModelProperty(value = "주소기반으로 배송 가능 여부")
	private Boolean isShippingPossibility;

	@ApiModelProperty(value = "STORE_DELIVERY_INTERVAL")
	private String storeDeliveryInterval;
}
