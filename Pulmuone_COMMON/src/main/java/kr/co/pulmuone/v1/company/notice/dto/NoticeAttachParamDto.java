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
@ApiModel(description = "NoticeAttachParamDto")
public class NoticeAttachParamDto extends BaseRequestDto {

	@ApiModelProperty(value = "")
	String csCompanyBbsId;

}
