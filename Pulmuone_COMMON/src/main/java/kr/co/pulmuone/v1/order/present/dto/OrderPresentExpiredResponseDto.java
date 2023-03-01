package kr.co.pulmuone.v1.order.present.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@ApiModel(description = "선물하기 만료 대상자 DTO")
public class OrderPresentExpiredResponseDto {

	@ApiModelProperty(value = "주문 PK")
	private Long odOrderId;

	@ApiModelProperty(value = "사용자 PK")
	private Long urUserId;
	
	@ApiModelProperty(value = "보내시는분")
	private String userName;

	@ApiModelProperty(value = "받는분")
	private String presentReceiveName;

	@ApiModelProperty(value = "받는분 전화번호")
	private String presentReceiveHp;

	@ApiModelProperty(value = "상품명")
	private String goodsName;

	@ApiModelProperty(value = "만료일")
	private String presentExpirationDate;

}
