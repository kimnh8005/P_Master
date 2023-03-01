package kr.co.pulmuone.v1.user.warehouse.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.DeliveryPatternListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "DeliveryPatternResponseDto")
public class DeliveryPatternResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "")
	private	List<DeliveryPatternListVo> rows;

	@ApiModelProperty(value = "배송패턴 조회 총 Count")
	private long total;
}
