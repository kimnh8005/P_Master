package kr.co.pulmuone.v1.user.dormancy.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "AddUserBlackResponseDto")
public class AddUserBlackResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private String urUserId;

	@ApiModelProperty(value = "성공횟수")
	private int successCnt;

}
