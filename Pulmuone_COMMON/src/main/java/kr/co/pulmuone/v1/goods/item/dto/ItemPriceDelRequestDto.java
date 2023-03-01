package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "가격정보 삭제 요청 request dto")
public class ItemPriceDelRequestDto {

    @ApiModelProperty(value = "품목가격 반영 PK")
    private String ilItemPriceOrigId;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "품목가격 승인 ID")
    private String ilItemPriceApprId;

}