package kr.co.pulmuone.v1.company.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "GetNoticeRequestDto")
public class GetNoticeRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "")
	String csCompanyBbsId;

	@ApiModelProperty(value = "")
	String viewMode;

}
