package kr.co.pulmuone.v1.user.join.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " AddUrShippingAddrRequestDto")
public class AddUrShippingAddrRequestDto extends BaseRequestDto
{
	
	// UR_SHIPPING_ADDR TABLE
	
	@ApiModelProperty(value = "유저아이디", required = true)
	private String urUserId;
	
	@ApiModelProperty(value = "기본주소여부")
	private String defaultYn;
	
	@ApiModelProperty(value = "배송명칭")
	private String shippingName;
	
	@ApiModelProperty(value = "수신자명")
	private String receiverName;
	
	@ApiModelProperty(value = "수신자핸드폰번호")
	private String receiverMobile;
	
	@ApiModelProperty(value = "수신자전화번호")
	private String receiverTel;
	
	@ApiModelProperty(value = "수신자우편번호")
	private String receiverZipCd;
	
	@ApiModelProperty(value = "수신자기본주소")
	private String receiverAddr1;
	
	@ApiModelProperty(value = "수신자상세주소")
	private String receiverAddr2;
	
	@ApiModelProperty(value = "건물관리번호", required = true)
	private String buildingCode;
	
	@ApiModelProperty(value = "최종배송요청사항")
	private String shippingComment;
	
}
