package kr.co.pulmuone.v1.order.delivery.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문생성 엑셀 업로드 상품 정보  리스트 Response")
public class OrderDeliveryTrackingResponseDto {

    @ApiModelProperty(value = "응답 코드")
    private String responseCode;

    @ApiModelProperty(value = "응답 메시지")
    private String responseMessage;

    @ApiModelProperty(value = "트랙킹 목록")
    private List<OrderDeliveryTrackingDto> tracking;

}
