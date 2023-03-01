package kr.co.pulmuone.v1.send.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.send.push.dto.AddPushIssueAllResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ApiModel(description = "모바일 푸시 발송(전체) PUSH 발송  Response")
public class AddPushIssueAllResponseDto extends BaseResponseDto{

    @ApiModelProperty(value = "모바일 푸시 발송(전체) 등록 ID 리스트")
    List<AddPushIssueAllResultDto> rows;
}
