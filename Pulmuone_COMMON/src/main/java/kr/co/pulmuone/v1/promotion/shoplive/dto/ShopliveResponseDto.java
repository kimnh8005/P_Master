package kr.co.pulmuone.v1.promotion.shoplive.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.shoplive.dto.vo.ShopliveInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ShopliveResponseDto {

    @ApiModelProperty(value = "샵라이브 목록")
    private List<ShopliveInfoVo> rows;

    @ApiModelProperty(value = "샵라이브 목록 수")
    private long total;
}
