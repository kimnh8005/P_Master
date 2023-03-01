package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니getCartData에 상품정보 응답DTO")
public class GetCartDataGoodsDataListResposeDto {
	// 현재 배송타입의 배송정책에 증정품만 포함되어 있는지 여부
	boolean isOnlyGiftShippingTemplate;

	// 배송 정책별 상품 리스트
	private List<SpCartVo> goodsDataList;
}
