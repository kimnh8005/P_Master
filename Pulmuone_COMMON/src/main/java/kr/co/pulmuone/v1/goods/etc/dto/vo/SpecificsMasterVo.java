package kr.co.pulmuone.v1.goods.etc.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품정보제공고시분류 Vo")
public class SpecificsMasterVo {

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

    @ApiModelProperty(value = "등록자")
    private String createId;
}