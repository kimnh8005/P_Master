package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointSettingResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 설정 상세 조회 ResponseDto")
public class PointResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "적립금 상세 Vo")
	private	PointSettingResultVo rows;
}
