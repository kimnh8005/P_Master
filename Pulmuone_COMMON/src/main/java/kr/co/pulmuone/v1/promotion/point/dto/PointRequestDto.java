package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointUsedDetailVo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "적립금 설정 상세조회 RequestDto")
public class PointRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "적립금 설정 Id")
	private String pmPointId;

	@ApiModelProperty(value = "설정구분")
	private String pointType;

	@ApiModelProperty(value = "설정상세구분")
	private String pointDetailType;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "1차승인담당자 ID")
	private String apprSubUserId;

	@ApiModelProperty(value = "2차승인담당자 ID")
	private String apprUserId;

	@ApiModelProperty(value = "승인 처리 타입")
	private String apprKindType;

	@ApiModelProperty(value = "이용권 수금 여부")
	private String ticketCollectYn;

	@ApiModelProperty(value = "복사타입")
	private String copyType;

	@ApiModelProperty(value = "적립금 설정 그룹화 번호")
	private String grPmPointId;

	@Builder.Default
    @ApiModelProperty(value = "적립금 사용 상세 List")
	List<PointUsedDetailVo> pointUsedDetailVoList = new ArrayList<>();

}
