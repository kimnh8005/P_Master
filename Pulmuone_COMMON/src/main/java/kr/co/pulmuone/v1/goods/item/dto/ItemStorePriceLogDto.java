package kr.co.pulmuone.v1.goods.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemStorePriceLogDto {

	private String storeName; // 매장명

	private String priceStartDate; // 가격시작일

	private String priceEndDate; // 가격종료일

	private int storeSalePrice; // 매장 판매가
}
