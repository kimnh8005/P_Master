package kr.co.pulmuone.v1.send.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(description = "선택회원 PUSH 발송  Response")
public class AddPushIssueSelectResponseDto extends BaseResponseDto{

    @ApiModelProperty(value = "선택회원 PUSH 발송 등록 ID 리스트")
    List<AddPushIssueSelectResultDto> rows;
}
