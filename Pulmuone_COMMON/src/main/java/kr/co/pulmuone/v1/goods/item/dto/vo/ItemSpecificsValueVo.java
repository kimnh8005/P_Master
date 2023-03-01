package kr.co.pulmuone.v1.goods.item.dto.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "품목별 상품정보제공고시 값 Vo")
public class ItemSpecificsValueVo {

    @ApiModelProperty(value = "품목별 상품정보제공고시 값 PK")
    private Long itemSpecificsValueId;

    @ApiModelProperty(value = "품목코드")
    private String itemCode;

    @ApiModelProperty(value = "상품정보제공고시항목 PK")
    private Long specificsFieldId;

    @ApiModelProperty(value = "상품정보제공고시 상세 항목 정보")
    private String specificsValue;

    @ApiModelProperty(value = "직접입력")
    private String directYn;

    @ApiModelProperty(value = "등록자")
    private String createId;

    @ApiModelProperty(value = "상품정보제공고시분류 PK")
    private Long specificsMasterId;
}
