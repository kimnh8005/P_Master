package kr.co.pulmuone.v1.goods.etc.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterFieldVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품정보제공고시 분류 항목 관계 ResponseDto")
public class SpecificsMasterFieldResponseDto {

    @ApiModelProperty(value = "상품정보제공고시 분류 항목 관계 리스트")
    private List<SpecificsMasterFieldVo> specificsMasterFieldList;
}
