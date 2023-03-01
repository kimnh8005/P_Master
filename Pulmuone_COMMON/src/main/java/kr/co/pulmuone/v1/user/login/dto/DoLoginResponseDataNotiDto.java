package kr.co.pulmuone.v1.user.login.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " DoLoginResponseDataNotiDto")
public class DoLoginResponseDataNotiDto
{
	private boolean changePassword = false;

	private boolean maketing = false;

}
