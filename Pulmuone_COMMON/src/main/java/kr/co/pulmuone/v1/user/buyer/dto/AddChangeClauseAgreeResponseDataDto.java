package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataAutoLoginDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataMakettingDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataNotiDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " AddChangeClauseAgreeResponseDataDto")
public class AddChangeClauseAgreeResponseDataDto
{
	private DoLoginResponseDataNotiDto noti;

	private DoLoginResponseDataMakettingDto maketting;

	private String urUserId;

	private DoLoginResponseDataAutoLoginDto autoLogin;
}
