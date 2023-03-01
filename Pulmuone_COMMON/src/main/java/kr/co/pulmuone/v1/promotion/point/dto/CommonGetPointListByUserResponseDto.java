package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.point.dto.vo.CommonGetPointListByUserVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "적립금 정보 목록 조회 ResponseDto")
public class CommonGetPointListByUserResponseDto {

    @ApiModelProperty(value = "적립금 정보 목록 resultVo")
    private List<CommonGetPointListByUserVo> rows;

    @ApiModelProperty(value = "적립금 정보 목록 총 갯수")
    private int total;
}
