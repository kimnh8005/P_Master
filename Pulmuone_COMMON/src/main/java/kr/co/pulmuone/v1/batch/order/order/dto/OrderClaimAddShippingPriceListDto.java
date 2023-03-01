package kr.co.pulmuone.v1.batch.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "입금기한 지난 추가배송비 결제 대상 목록 DTO")
public class OrderClaimAddShippingPriceListDto {

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문클레임PK")
	private long odClaimId;

	@ApiModelProperty(value = "클레임상태타입")
	private String claimStatusTp;

	@ApiModelProperty(value = "귀책구분")
	private String targetTp;

	@ApiModelProperty(value = "반품회수여부")
	private String returnsYn;

	@ApiModelProperty(value = "직접결제여부")
	private String directPaymentYn;

	@ApiModelProperty(value = "추가결제구분")
	private String addPaymentTp;

	@ApiModelProperty(value = "우편번호")
	private String recvZipCd;
}
