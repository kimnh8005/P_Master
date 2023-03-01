package kr.co.pulmuone.v1.promotion.pointhistory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.vo.PointDetailHistoryVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 내역 리스트 조회 ResponseDto")
public class PointDetailHistoryResponseDto  extends BaseResponseDto{

	@ApiModelProperty(value = "적립금 상세 내역 ")
	private	List<PointDetailHistoryVo> rows;

	@ApiModelProperty(value = "적립금 내역 리스트 카운트")
	private	long total;

}
