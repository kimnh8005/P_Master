package kr.co.pulmuone.v1.promotion.point.dto;

import java.util.List;

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
@ApiModel(description = "적립금 설정 목록 조회 ResponseDto")
public class PointSettingListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "적립금 설정 조회 리스트")
	private	List<PointSettingResultVo> rows;

	@ApiModelProperty(value = "적립금 설정 조회 총 Count")
	private long total;

}
