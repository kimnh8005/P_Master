package kr.co.pulmuone.v1.base.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetShopInfoResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetShopInfoResponseDto")
public class GetShopInfoResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	GetShopInfoResultVo rows;

}
