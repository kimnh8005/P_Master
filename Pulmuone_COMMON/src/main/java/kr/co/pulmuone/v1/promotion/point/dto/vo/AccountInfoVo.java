package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "AccountInfoVo")
public class AccountInfoVo {

	@ApiModelProperty(value = "회원 ID")
	private String urUserId;

	@ApiModelProperty(value = "개별난수번호")
	private String serialNumber ;

	@ApiModelProperty(value = "No")
	private String no ;

	@ApiModelProperty(value = "회원 LOGIN ID")
	private String loginId;

	@ApiModelProperty(value = "회원명")
	private String userName;

	@ApiModelProperty(value = "개별 적립금")
	private int issueVal;


}
