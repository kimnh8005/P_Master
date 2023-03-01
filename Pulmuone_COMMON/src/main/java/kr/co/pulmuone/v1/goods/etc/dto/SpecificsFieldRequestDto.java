package kr.co.pulmuone.v1.goods.etc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsFieldVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "상품정보고시 상세항목 관리 RequestDto")
public class SpecificsFieldRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "상품정보제공고시분류 PK")
    private Long specificsMasterId;

    @ApiModelProperty(value = "상품정보제공고시항목 PK")
    private Long specificsFieldId;

    @ApiModelProperty(value = "상품정보제공고시항목 코드")
    private String specificsFieldCode;

    @ApiModelProperty(value = "상품정보제공고시항목 명")
    private String specificsFieldName;

    @ApiModelProperty(value = "기본값")
    private String basicValue;

    @ApiModelProperty(value = "상세설명")
    private String specificsDesc;

    @ApiModelProperty(value="상품정보제공고시항목 PK 들")
    private String specificsFieldIds;

    @ApiModelProperty(value = " 리스트")
    List<SpecificsFieldVo> specificsFieldVoList;
}
