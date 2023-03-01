package kr.co.pulmuone.v1.goods.etc.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterFieldVo;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품정보고시관리 ResponseDto")
public class SpecificsListResponseDto {

    @ApiModelProperty(value = "상품정보제공고시분류 리스트")
    private List<SpecificsMasterVo> specificsMasterList;

    @ApiModelProperty(value = "상품정보제공고시 분류 항목 관계 리스트")
    private List<SpecificsMasterFieldVo> specificsMasterFieldList;

    @ApiModelProperty(value = "품목별 상품군 사용유무")
    private boolean itemSpecificsMasterUseYn;

    @ApiModelProperty(value = "상품정보제공고시분류 명 중복유무")
    private boolean specificsMasterNameDuplicateYn;
}
