package kr.co.pulmuone.v1.comm.util.asis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "통합몰 회원가입시 AS-IS 회원 배송지 정보 DTO")
public class SearchCustomerDeliveryDto {

	@ApiModelProperty(value = "배송지명")
    private String shippingNm;

	@ApiModelProperty(value = "수신자명")
    private String receiverNm;

	@ApiModelProperty(value = "수신자 핸드폰번호")
    private String receiverMo;

	@ApiModelProperty(value = "수신자 전화번호")
    private String receiverTel;

	@ApiModelProperty(value = "기본배송지여부")
    private String basicYn;

	@ApiModelProperty(value = "우편번호")
    private String receiverZipCd;

	@ApiModelProperty(value = "도로명주소")
    private String receiverAddr1;

	@ApiModelProperty(value = "상세주소")
    private String receiverAddr2;

	@ApiModelProperty(value = "건물번호")
    private String buildingCd;

}
