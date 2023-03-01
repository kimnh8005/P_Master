package kr.co.pulmuone.v1.order.email.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "유통기한별 재고 리스트 mail 정보용 vo")
public class GoodsStockDisposalForEmailVo {

    @ApiModelProperty(value = "기준년")
    private String baseYear;

    @ApiModelProperty(value = "기준달")
    private String baseMonth;

    @ApiModelProperty(value = "기준일")
    private String baseDay;

    @ApiModelProperty(value = "prfile별 url")
    private String profileUrl;

}
