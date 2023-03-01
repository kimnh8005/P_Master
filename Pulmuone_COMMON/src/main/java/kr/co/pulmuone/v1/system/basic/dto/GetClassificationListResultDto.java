package kr.co.pulmuone.v1.system.basic.dto;


import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetClassificationListResultVo;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetClassificationListResultDto")
public class GetClassificationListResultDto extends BaseDto {
	
	private List<GetClassificationListResultVo> rows = new ArrayList<GetClassificationListResultVo>();
	private int total;
	
}
