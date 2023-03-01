package kr.co.pulmuone.v1.user.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "UserGroupResponseDto")
public class UserGroupResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "결과값 row")
    private UserGroupResultVo rows;

}
