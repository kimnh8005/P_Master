package kr.co.pulmuone.v1.order.email.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "직접배송 미등록 송장 알림 DTO")
public class DirectShippingUnregistetedInvoiceDto {

    @ApiModelProperty(value = "메일 수신")
    private String mailDate;

    @ApiModelProperty(value = "미등록 건수")
	private int unregisteredCount;

    @ApiModelProperty(value = "미등록 송장 정보 목록")
	private List<UnregistetedInvoiceDto> list;
}
