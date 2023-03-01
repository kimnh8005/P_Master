package kr.co.pulmuone.v1.send.template.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetEmailSendListRequestDto")
public class GetEmailSendListRequestDto extends BaseRequestPageDto {

}
