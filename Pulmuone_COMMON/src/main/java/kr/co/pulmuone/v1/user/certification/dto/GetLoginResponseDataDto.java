package kr.co.pulmuone.v1.user.certification.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetChangeClauseNoAgreeListResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetStopReasonResultVo;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataAutoLoginDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataCertificationDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataMakettingDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataNotiDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetLoginResponseDataDto")
public class GetLoginResponseDataDto
{
	private DoLoginResponseDataCertificationDto certification;

	private GetStopReasonResultVo stop;

	private List<GetChangeClauseNoAgreeListResultVo> clause;

	private DoLoginResponseDataNotiDto noti;

	private DoLoginResponseDataMakettingDto maketting;

	private String urUserId;

	private DoLoginResponseDataAutoLoginDto autoLogin;
}
