package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "해당 품목의 매장 가격정보 조회 response dto")
public class ItemStorePriceLogResponseDto {
	// 해당 품목의 매장 가격정보 이력 목록
	private List<ItemStorePriceLogDto> rows;

	// 해당 품목의 매장 가격정보 전체 이력 개수
	private long total;
}
