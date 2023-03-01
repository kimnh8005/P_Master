package kr.co.pulmuone.v1.customer.faq.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.customer.faq.dto.vo.FaqBosDetailVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "FaqBosDetailResponseDto")
public class FaqBosDetailResponseDto  extends BaseResponseDto{

	@ApiModelProperty(value = "FAQ 관리 상세정보")
	private	FaqBosDetailVo row;

}
