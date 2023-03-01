package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetSessionShippingResponseDTO")
public class GetSessionShippingResponseDto
{
	private String receiverName;

	private String receiverZipCode;

	private String receiverAddress1;

	private String receiverAddress2;

	private String buildingCode;

	private String receiverMobile;

	private String accessInformationType;

	private String accessInformationPassword;

	private String shippingComment;

	private String selectBasicYn;

	private Long shippingAddressId;

	private String bosTp;
}
