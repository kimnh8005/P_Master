package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetShippingAddressListResultVo")
public class GetShippingAddressListResultVo
{
	@ApiModelProperty(value = "배송지 코드")
	private String shippingAddressType;

	@ApiModelProperty(value = "배송지 코드명")
	private String shippingAddressTypeName;

	@ApiModelProperty(value = "배송지 수령인명")
	private String receiverName;

	@ApiModelProperty(value = "배송지 우편번호")
	private String receiverZipCode;

	@ApiModelProperty(value = "배송지 주소")
	private String receiverAddress1;

	@ApiModelProperty(value = "배송지 상세주소")
	private String receiverAddress2;

	@ApiModelProperty(value = "건물관리번호")
	private String buildingCode;

	@ApiModelProperty(value = "배송지 모바일")
	private String receiverMobile;

	@ApiModelProperty(value = "출입정보타입")
	private String accessInformationType;

	@ApiModelProperty(value = "출입정보 비밀번호(암호화)")
	private String accessInformationPassword;

	@ApiModelProperty(value = "배송 요청 사항")
	private String shippingComment;

	@ApiModelProperty(value = "배송 가능 정보")
	private ShippingAddressPossibleDeliveryInfoDto delivery;

	@ApiModelProperty(value = "배송지 PK")
	private Long shippingAddressId;

}
