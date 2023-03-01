package kr.co.pulmuone.v1.user.urcompany.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.urcompany.dto.vo.GetWarehouseListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetWarehouseListResponseDto")
public class GetWarehouseListResponseDto extends BaseResponseDto{

	private List<GetWarehouseListResultVo> rows = new ArrayList<>();

	private long total;
}
