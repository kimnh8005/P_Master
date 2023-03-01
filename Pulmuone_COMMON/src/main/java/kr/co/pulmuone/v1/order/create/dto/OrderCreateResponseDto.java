package kr.co.pulmuone.v1.order.create.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문생성 엑셀 업로드 저장 결과 Response")
public class OrderCreateResponseDto {
	private OrderEnums.OrderRegistrationResult orderRegistrationResult;
}
