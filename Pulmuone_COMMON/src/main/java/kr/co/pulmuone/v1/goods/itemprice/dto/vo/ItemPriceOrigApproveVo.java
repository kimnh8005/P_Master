package kr.co.pulmuone.v1.goods.itemprice.dto.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "품목가격 원가 반영")
public class ItemPriceOrigApproveVo {

    @ApiModelProperty(value = "품목가격원본 SEQ")
    private long ilItemPriceOrigId;

    @ApiModelProperty(value = "원가")
    private String standardPrice;

    @ApiModelProperty(value = "현재 원가")
    private String standardPriceNow;

    @ApiModelProperty(value = "수정자")
    private long modifyId;

}


