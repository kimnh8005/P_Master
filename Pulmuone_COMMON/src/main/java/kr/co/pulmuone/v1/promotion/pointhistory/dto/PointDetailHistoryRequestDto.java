package kr.co.pulmuone.v1.promotion.pointhistory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PointDetailHistoryRequestDto")
public class PointDetailHistoryRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "적립금 사용 ID")
    private String pmPointUsedId;

}
