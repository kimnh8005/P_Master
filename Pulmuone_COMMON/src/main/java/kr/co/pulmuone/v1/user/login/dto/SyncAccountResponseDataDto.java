package kr.co.pulmuone.v1.user.login.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetChangeClauseNoAgreeListResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetStopReasonResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " SyncAccountResponseDataDto")
public class SyncAccountResponseDataDto
{
	private DoLoginResponseDataCertificationDto certification;

	private GetStopReasonResultVo stop;

	private List<GetChangeClauseNoAgreeListResultVo> clause;

	private DoLoginResponseDataNotiDto noti;

	private DoLoginResponseDataMakettingDto maketting;

	private String urUserId;
}
