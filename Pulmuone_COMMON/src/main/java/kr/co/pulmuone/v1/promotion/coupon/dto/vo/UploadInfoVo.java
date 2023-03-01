package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "UploadInfoVo")
public class UploadInfoVo {

	@ApiModelProperty(value = "회원 ID")
	private String urUserId;

	@ApiModelProperty(value = "난수번호")
	private String serialNumber;

	@ApiModelProperty(value = "회원 Login ID")
	private String loginId;

}
