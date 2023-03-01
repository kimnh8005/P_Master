package kr.co.pulmuone.v1.system.basic.dto;

import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetEnvironmentListResponseDto")
public class GetEnvironmentListResponseDto extends BaseResponseDto {

    private List<GetEnvironmentListResultVo> rows = new ArrayList<GetEnvironmentListResultVo>();
    private int total;

	@ApiModelProperty(value = "시스템환경설정 Vo")
    private GetEnvironmentListResultVo  getEnvironmentListResultVo;

}