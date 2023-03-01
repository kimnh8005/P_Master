package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "가격정보 이력 조회 request dto")
public class ItemPriceHistoryRequestDto extends BaseRequestPageDto {

    /*
     * 가격정보 이력 조회 request dto
     */

    @ApiModelProperty(value = "품목코드", required = true)
    private String ilItemCode;

    @ApiModelProperty(value = "과거 가격정보 이력 포함 여부", required = false)
    private String includePastPriceHistory;

}
