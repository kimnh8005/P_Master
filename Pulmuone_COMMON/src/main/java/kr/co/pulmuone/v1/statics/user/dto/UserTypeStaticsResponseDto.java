package kr.co.pulmuone.v1.statics.user.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.statics.claim.dto.vo.ClaimStaticsVo;
import kr.co.pulmuone.v1.statics.user.dto.vo.UserTypeStaticsVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class UserTypeStaticsResponseDto {

    @ApiModelProperty(value = "리스트전체건수")
    private long total;

    @ApiModelProperty(value = "리스트")
    private List<UserTypeStaticsVo> rows;

}
