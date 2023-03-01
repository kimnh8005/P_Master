package kr.co.pulmuone.mall.display.layout.dto;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.promotion.event.dto.MissionStampByUserResponseDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataAutoLoginDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataCertificationDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataMakettingDto;
import kr.co.pulmuone.v1.user.login.dto.DoLoginResponseDataNotiDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetChangeClauseNoAgreeListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetSearchWordResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetStopReasonResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetLayoutInfoResultDto")
public class GetLayoutInfoResultDto{

	private HashMap<String,Integer> cart;

	private HashMap<String,Object> user;

	private List<GetSearchWordResultVo> searchWord;

	private DoLoginResponseDataCertificationDto certification;

	private GetStopReasonResultVo stop;

	private List<GetChangeClauseNoAgreeListResultVo> clause;

	private DoLoginResponseDataNotiDto noti;

	private DoLoginResponseDataMakettingDto maketting;

	private List<MissionStampByUserResponseDto> stamp;

	private DoLoginResponseDataAutoLoginDto autoLogin;
}
