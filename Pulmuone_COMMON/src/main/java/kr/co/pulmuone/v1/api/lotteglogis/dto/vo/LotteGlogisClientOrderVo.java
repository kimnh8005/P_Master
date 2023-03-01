package kr.co.pulmuone.v1.api.lotteglogis.dto.vo;

import com.fasterxml.jackson.annotation.JsonAlias;

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
@ApiModel(description = "롯데 거래처 주문 Vo")
public class LotteGlogisClientOrderVo {

    @JsonAlias({ "rtnCd" })
    @ApiModelProperty(value = "결과 값 (S:성공 / E:오류)")
    private String returnCode;

    @JsonAlias({ "rtnMsg" })
    @ApiModelProperty(value = "오류메시지")
    private String returnMessage;

    @JsonAlias({ "ustRtgSctCd" })
    @ApiModelProperty(value = "출고반품구분 (01:출고 02:반품)")
    private String shippingReturnTypeCode;

    @JsonAlias({ "ordSct" })
    @ApiModelProperty(value = "오더구분 (1:일반 2:교환 3:AS)")
    private String orderTypeCode;

    @JsonAlias({ "fareSctCd" })
    @ApiModelProperty(value = "운임구분 (03:신용 04:복합)")
    private String fareTypeCode;

    @JsonAlias({ "ordNo" })
    @ApiModelProperty(value = "주문번호")
    private String orderNumber;

    @JsonAlias({ "invNo" })
    @ApiModelProperty(value = "운송장번호")
    private String trackingNumber;
}
