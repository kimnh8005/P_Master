package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "주문I/F 출고지시일 목록 조회 RequestDto")
public class OrderDetailIfDateListRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "주문상세 PK")
	private Long odOrderDetlId;

	@ApiModelProperty(value = "출고처ID(출고처PK)")
	private Long urWarehouseId;

	@ApiModelProperty(value = "상품ID(상품PK)")
	private Long ilGoodsId;

	@ApiModelProperty(value = "새벽배송여부")
	private Boolean isDawnDelivery;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsDailyCycleType;

	@ApiModelProperty(value = "주문배송지 건물번호")
	private String recvBldNo;

	@ApiModelProperty(value = "주문배송지 우편번호")
	private String zipCode;

	@ApiModelProperty(value = "주1일 선택시 요일코드")
	private String weekCode;

	@ApiModelProperty(value = "주문생성 여부")
	private String orderCreateYn;

	@ApiModelProperty(value = "주문생성 > 상품리스트")
	private List<OrderDetailIfDateListRequestDto> orderCreateGoodsList;

	@ApiModelProperty(value = "회원여부")
	private boolean isMember;
}