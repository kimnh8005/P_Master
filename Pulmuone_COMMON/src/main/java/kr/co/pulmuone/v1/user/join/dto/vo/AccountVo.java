package kr.co.pulmuone.v1.user.join.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원정보활동정보 Vo")
public class AccountVo extends BaseRequestDto{

	@ApiModelProperty(value = "회원정보활동정보 ID")
	private Long accountId;

	@ApiModelProperty(value = "회원 ID")
	private Long userId;

	@ApiModelProperty(value = "잔여마일리지")
	private long mileage;

	@ApiModelProperty(value = "잔여포인트")
	private long point;

	@ApiModelProperty(value = "잔여예치금")
	private long deposit;

	@ApiModelProperty(value = "최근 로그인 날짜")
	private String lastLoginDate;

}
