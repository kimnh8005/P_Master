package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetSystemAuthorityResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "권한관리 Response")
public class SystemAuthorityResponseDto {

    @ApiModelProperty(value = "권한관리 리스트")
    private List<GetSystemAuthorityResultVo> rows;
}
