package kr.co.pulmuone.v1.batch.send.template.service.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.batch.send.template.service.dto.vo.GetEmailSendBatchResultVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = " GetEmailSendBatchResponseDto")
public class GetEmailSendBatchResponseDto {

    private GetEmailSendBatchResultVo rows;

}
