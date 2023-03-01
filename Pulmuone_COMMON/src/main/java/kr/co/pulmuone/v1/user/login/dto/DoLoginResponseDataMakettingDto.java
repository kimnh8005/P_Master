package kr.co.pulmuone.v1.user.login.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " DoLoginResponseDataMakettingDto")
public class DoLoginResponseDataMakettingDto
{
	private String smsYn;

	private String smsYnDate;

	private String mailYn;

	private String mailYnDate;
}
