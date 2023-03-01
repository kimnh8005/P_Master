package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원상태(정상,정지) 결과 VO")
public class BuyerStatusResultVo extends BaseRequestDto{

	@ApiModelProperty(value="회원코드")
	private Long urUserId;

	@ApiModelProperty(value = "전환일시")
	private String createDate;

	@ApiModelProperty(value = "사유")
	private String reason;

	@ApiModelProperty(value = "소명날짜")
	private String callDate;

	@ApiModelProperty(value = "고객센터 이메일")
	private String serviceMail;

	@ApiModelProperty(value = "이메일")
	private String mail;

	@ApiModelProperty(value = "모바일 전화번호")
	private String mobile;


}
