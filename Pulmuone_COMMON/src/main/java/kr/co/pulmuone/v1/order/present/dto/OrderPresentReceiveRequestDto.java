package kr.co.pulmuone.v1.order.present.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "선물 받기 요청 DTO")
public class OrderPresentReceiveRequestDto extends IsShippingPossibilityRequestDto {
	// isShippingPossibility 선물하기 배송가능 체크 요청과 동일 구조가 동일 하지만
	// 차후 변경가능성이 있어 별도 DTO 생성 후 동일하여 우선 상속 받는것으로 처리
}
