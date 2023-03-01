package kr.co.pulmuone.v1.base.dto;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "Ecs 카테고리 리스트 DTO")
public class EcsResponseDto {

	@ApiModelProperty(value = "Ecs 카테고리 리스트")
	private List<HashMap<String,String>> rows;

}
