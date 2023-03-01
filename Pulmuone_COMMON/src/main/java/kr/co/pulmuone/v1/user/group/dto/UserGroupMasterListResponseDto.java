package kr.co.pulmuone.v1.user.group.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupMasterResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserGroupMasterListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "Master 결과값 row")
    private List<UserGroupMasterResultVo> rows;

}
