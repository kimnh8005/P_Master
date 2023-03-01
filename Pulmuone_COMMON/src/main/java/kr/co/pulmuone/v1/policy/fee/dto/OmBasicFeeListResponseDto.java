package kr.co.pulmuone.v1.policy.fee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "OmBasicFeeListResponseDto")
public class OmBasicFeeListResponseDto extends BaseResponseDto {

    @ApiModelProperty
	private	List<OmBasicFeeListDto> rows;

    @ApiModelProperty
	private	long total;

}
