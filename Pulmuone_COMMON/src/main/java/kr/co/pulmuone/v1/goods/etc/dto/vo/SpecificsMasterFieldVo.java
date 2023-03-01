package kr.co.pulmuone.v1.goods.etc.dto.vo;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품정보제공고시 분류 항목 관계 Vo")
public class SpecificsMasterFieldVo {

    @ApiModelProperty(value = "상품정보제공고시 분류 항목 관계 PK")
    private Long specificsMasterFieldId;

    @ApiModelProperty(value = "상품정보제공고시분류 PK")
    private Long specificsMasterId;

    @ApiModelProperty(value = "상품정보제공고시항목 PK")
    private Long specificsFieldId;

    @ApiModelProperty(value = "상품정보제공고시항목 코드")
    private String specificsFieldCode;

    @ApiModelProperty(value = "상품정보제공고시항목 명")
    private String specificsFieldName;

    @ApiModelProperty(value = "노출순서")
    private int sort;

    @ApiModelProperty(value = "등록자")
    private String createId;

    @ApiModelProperty(value = "상품정보제공고시항목 등록일자")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "상품정보제공고시 분류 항목 관계 사용유무")
    private boolean specificsMasterFieldUseYn;

    @ApiModelProperty(value = "기본 값")
    private String basicValue;
}