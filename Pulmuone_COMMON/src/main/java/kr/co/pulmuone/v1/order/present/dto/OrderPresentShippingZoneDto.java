package kr.co.pulmuone.v1.order.present.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "선물하기 배송지 주소 DTO")
public class OrderPresentShippingZoneDto {

	@ApiModelProperty(value = "받는사람 이름")
	private String recvNm;

	@ApiModelProperty(value = "받는사람 우편번호")
	private String recvZipCd;

	@ApiModelProperty(value = "받는사람 주소")
	private String recvAddr1;

	@ApiModelProperty(value = "받는사람 상세주소")
	private String recvAddr2;

	@ApiModelProperty(value = "건물관리번호")
	private String recvBldNo;

	@ApiModelProperty(value = "배송지 모바일")
	private String recvHp;

	@ApiModelProperty(value = "출입정보타입")
	private String doorMsgCd;

	@ApiModelProperty(value = "출입정보 비밀번호")
	private String doorMsg;

	@ApiModelProperty(value = "배송 요청 사항")
	private String deliveryMsg;
}
