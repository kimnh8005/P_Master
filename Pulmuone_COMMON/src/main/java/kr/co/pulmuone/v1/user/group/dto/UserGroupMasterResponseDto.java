package kr.co.pulmuone.v1.user.group.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupMasterResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGroupMasterResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "Master 결과값 row")
    private UserGroupMasterResultVo rows;

}
