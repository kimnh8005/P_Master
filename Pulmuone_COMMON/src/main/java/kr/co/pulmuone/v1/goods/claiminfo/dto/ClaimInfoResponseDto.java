package kr.co.pulmuone.v1.goods.claiminfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.claiminfo.dto.vo.ClaimInfoVo;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ClaimInfoResponseDto")
public class ClaimInfoResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	ClaimInfoVo rows;

}
