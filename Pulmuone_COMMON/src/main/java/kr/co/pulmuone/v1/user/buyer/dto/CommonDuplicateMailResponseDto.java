package kr.co.pulmuone.v1.user.buyer.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "공통 이메일 중복 체크 ResponseDto")
public class CommonDuplicateMailResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "중복 이메일 수")
	private int count;

}
