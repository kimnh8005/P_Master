package kr.co.pulmuone.v1.goods.etc.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterFieldVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품정보고시관리 리스트 RequestDto")
public class SpecificsListRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "상품정보제공고시분류 PK")
    private Long specificsMasterId;

    @ApiModelProperty(value = "상품정보제공고시분류 코드")
    private String specificsMasterCode;

    @ApiModelProperty(value = "상품정보제공고시분류 명")
    private String specificsMasterName;

    @ApiModelProperty(value = "노출순서")
    private int sort;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "상품정보제공고시 분류항목관계 리스트")
    private List<SpecificsMasterFieldVo> specificsMasterFieldList;

    @ApiModelProperty(value = "삭제 할 품목별 상품정보제공고시 값 리스트")
    private List<SpecificsMasterFieldVo> delItemSpecificsValueList;
}
