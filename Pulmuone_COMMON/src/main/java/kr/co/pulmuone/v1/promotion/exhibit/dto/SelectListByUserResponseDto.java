package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.SelectListByUserVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class SelectListByUserResponseDto {

    @ApiModelProperty(value = "목록 총 갯수")
    private int total;

    @ApiModelProperty(value = "기획전 정보")
    private List<SelectListByUserVo> select;

}
