package kr.co.pulmuone.v1.send.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.send.push.dto.AddPushIssueSearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ApiModel(description = "검색회원 PUSH 발송  Response")
public class AddPushIssueSearchResponseDto extends BaseResponseDto{

    @ApiModelProperty(value = "검색회원 PUSH 발송 등록 ID 리스트")
    List<AddPushIssueSearchResultDto> rows;
}
