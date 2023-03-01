package kr.co.pulmuone.v1.pg.service.kcp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KcpApplyRegularBatchKeyDataRequestDto {

	private String odid;

	private String urUserId;

	private String buyerName;

	private int paymentPrice;

	private String orderInputUrl;

	private String goodsName;
}
