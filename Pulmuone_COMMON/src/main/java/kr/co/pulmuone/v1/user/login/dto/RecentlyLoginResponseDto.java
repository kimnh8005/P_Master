package kr.co.pulmuone.v1.user.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.login.dto.vo.RecentlyLoginResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "최근 로그인 Response Dto")
public class RecentlyLoginResponseDto {

    @ApiModelProperty(value = "RecentlyLoginResultVo")
    private RecentlyLoginResultVo recentlyLoginResultVo;
}
