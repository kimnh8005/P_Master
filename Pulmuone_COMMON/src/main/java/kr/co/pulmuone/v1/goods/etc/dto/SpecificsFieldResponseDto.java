package kr.co.pulmuone.v1.goods.etc.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsFieldVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품정보고시 상세항목 관리 ResponseDto")
public class SpecificsFieldResponseDto {

    @ApiModelProperty(value = "상품정보고시 상세항목 관리 리스트")
    private List<SpecificsFieldVo> specificsFieldList;

    @ApiModelProperty(value = "품목별 상품정보제공고시 값 사용유무")
    private boolean itemSpecificsValueUseYn;

    @ApiModelProperty(value = "상품정보제공고시항목 명 중복유무")
    private boolean specificsFieldNameDuplicateYn;
}
