package kr.co.pulmuone.v1.promotion.serialnumber.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.vo.GetSerialNumberListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetSerialNumberListResponseDto")
public class GetSerialNumberListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "이용권 내역 조회 리스트")
	private	List<GetSerialNumberListResultVo> rows;

	@ApiModelProperty(value = "이용권 내약 조회 총 Count")
	private long total;
}
