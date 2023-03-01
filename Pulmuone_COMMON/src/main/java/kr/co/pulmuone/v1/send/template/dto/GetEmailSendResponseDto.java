package kr.co.pulmuone.v1.send.template.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = " GetEmailSendListResponseDto")
public class GetEmailSendResponseDto extends BaseResponseDto {

    private GetEmailSendResultVo rows;

}
