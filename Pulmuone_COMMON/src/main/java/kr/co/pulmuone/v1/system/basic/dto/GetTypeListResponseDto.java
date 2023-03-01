package kr.co.pulmuone.v1.system.basic.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "GetTypeListResponseDto")
public class GetTypeListResponseDto extends BaseResponseDto {


	@ApiModelProperty(value = "리스트")
	private	List<GetTypeListResultVo> rows;

}
