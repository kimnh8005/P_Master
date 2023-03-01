package kr.co.pulmuone.v1.base.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.base.dto.vo.GetGrantAuthEmployeePopupResultVo;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "담당자팝업 Response")
public class GetGrantAuthEmployeePopupResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "담당자 리스트")
	private	List<GetGrantAuthEmployeePopupResultVo> rows;

}
