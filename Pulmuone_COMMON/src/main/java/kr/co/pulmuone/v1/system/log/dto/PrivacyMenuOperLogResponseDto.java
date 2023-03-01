package kr.co.pulmuone.v1.system.log.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.log.dto.vo.PrivacyMenuOperLogResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PrivacyMenuOperLogResponseDto")
public class PrivacyMenuOperLogResponseDto  extends BaseResponseDto {

	@ApiModelProperty(value = "결과값 row")
	private	List<PrivacyMenuOperLogResultVo> rows;

	@ApiModelProperty(value = "총 count")
	private long total;


}
