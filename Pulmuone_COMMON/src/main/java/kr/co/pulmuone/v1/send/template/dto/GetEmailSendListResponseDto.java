package kr.co.pulmuone.v1.send.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendListResultVo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel(description = " GetEmailSendListResponseDto")
public class GetEmailSendListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "자동발송메일/SMS 목록")
    private List<GetEmailSendListResultVo> rows = new ArrayList<>();

    @ApiModelProperty(value = "자동발송메일/SMS 카운트")
    private int total;

}
