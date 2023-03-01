package kr.co.pulmuone.v1.user.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.DeliveryPatternVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "DeliveryPatternDetailResponseDto")
public class DeliveryPatternDetailResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "배송패턴 상세 Vo")
	private	DeliveryPatternVo rows;
}
