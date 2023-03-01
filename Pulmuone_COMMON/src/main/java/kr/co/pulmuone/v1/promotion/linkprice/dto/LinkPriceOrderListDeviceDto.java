package kr.co.pulmuone.v1.promotion.linkprice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "링크프라이스 주문디바이스정보 API DTO")
public class LinkPriceOrderListDeviceDto {

    @ApiModelProperty(value = "IP Address")
    private String remote_addr;

    @ApiModelProperty(value = "연동키")
    private String lpinfo;

    @ApiModelProperty(value = "접속 방")
    private String device_type;

    @ApiModelProperty(value = "연동키")
    private String merchant_id = PromotionConstants.LP_MERCHANT_ID;

    @ApiModelProperty(value = "사용자 브라우저 정보")
    private String user_agent;
}
