package kr.co.pulmuone.v1.statics.user.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.statics.user.dto.vo.UserGroupStaticsVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class UserGroupStaticsResponseDto {

    @ApiModelProperty(value = "리스트전체건수")
    private long total;

    @ApiModelProperty(value = "리스트")
    private List<UserGroupStaticsVo> rows;

}
