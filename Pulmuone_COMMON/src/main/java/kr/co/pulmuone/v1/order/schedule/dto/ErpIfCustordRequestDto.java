package kr.co.pulmuone.v1.order.schedule.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpIfCustordRequestDto extends ErpIfCustordConditionRequestDto {


	@JsonProperty("line")
    private List<ErpIfCustordConditionRequestDto> line;

}
