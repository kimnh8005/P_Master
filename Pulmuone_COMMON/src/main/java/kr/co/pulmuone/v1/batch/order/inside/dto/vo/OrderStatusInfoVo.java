package kr.co.pulmuone.v1.batch.order.inside.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문상태 변경  Vo
 * </PRE>
 */

@Getter
@Setter
@ToString
public class OrderStatusInfoVo {

	@ApiModelProperty(value = "주문 상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문상태")
    private String orderStatusCd;

	@ApiModelProperty(value = "택배사 PK")
	private Long psShippingCompId;

	@ApiModelProperty(value = "송장번호")
	private String trackingNo;

    @ApiModelProperty(value = "등록자ID")
    private Long createId;

    @ApiModelProperty(value = "매장(배송/픽업)-스토어PK")
    private String urStoreId;

    @ApiModelProperty(value = "주문상세 순번")
    private long odOrderDetlSeq;

}