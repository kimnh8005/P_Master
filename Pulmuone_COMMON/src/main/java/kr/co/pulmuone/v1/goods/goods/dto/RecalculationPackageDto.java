package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "묶음상품 재계산 dto")
public class RecalculationPackageDto
{

	@ApiModelProperty(value = "묶음상품 상품 재고")
	private int stockQty;

	@ApiModelProperty(value = "묶음상품 판매상태")
	private String saleStatus;

	@ApiModelProperty(value = "배송 가능 도착 예정일 DTO 리스트")
	private List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList;

	@ApiModelProperty(value = "상품 배송 도착 예정일 DTO")
	private ArrivalScheduledDateDto arrivalScheduledDateDto;

}
