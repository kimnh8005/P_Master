package kr.co.pulmuone.v1.send.push.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.send.push.dto.PushSendListRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@ApiModel(description = "모바일 푸시 발송 (전체) 발송회원 기기정보 조회 Param")
public class GetSendUserDeviceListParamVo extends BaseRequestDto {

    @ApiModelProperty(value = "발송그룹")
    private String sendGroup;

    @ApiModelProperty(value = "업로드 회원 리스트")
    private List<PushSendListRequestDto> uploadUserList;
}
