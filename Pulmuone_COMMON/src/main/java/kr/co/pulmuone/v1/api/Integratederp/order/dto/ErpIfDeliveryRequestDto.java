package kr.co.pulmuone.v1.api.Integratederp.order.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpIfDeliveryRequestDto extends ErpIfDlvConditionRequestDto {


	@JsonProperty("line")
    private List<ErpIfDlvConditionRequestDto> line;

}
