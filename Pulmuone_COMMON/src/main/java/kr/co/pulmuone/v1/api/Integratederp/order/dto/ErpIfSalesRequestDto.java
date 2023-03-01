package kr.co.pulmuone.v1.api.Integratederp.order.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfSalesRequestDto")
public class ErpIfSalesRequestDto  extends ErpIfOrderConditionRequestDto {


	@JsonProperty("line")
    private List<ErpIfOrderConditionRequestDto> line;



}
