package kr.co.pulmuone.v1.display.popup.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.display.popup.dto.AddDisplayPopupRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.GetDisplayPopupListRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.PutDisplayPopupRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetPopupInfoMallResultVo;


public interface DisplayPopupBiz {

	List<GetPopupInfoMallResultVo> getPopupInfoMobile(List<String> dpTargetTypeList, List<String>dpRangeTypeList, List<String> todayStopIdList);

	List<GetPopupInfoMallResultVo> getPopupInfoPc(List<String> dpTargetTypeList, List<String>dpRangeTypeList, List<String> todayStopIdList);

    ApiResult<?> getPopupList(GetDisplayPopupListRequestDto getDisplayPopupListRequestDto);

    ApiResult<?> addPopup(AddDisplayPopupRequestDto addDisplayPopupRequestDto);

	ApiResult<?> putPopupDetail(String displayFrontPopupId);

	ApiResult<?> putPopup(PutDisplayPopupRequestDto putDisplayPopupRequestDto);

	ApiResult<?> delPopup(String displayFrontPopupId);

	ApiResult<?> previewPopup(String displayFrontPopupId);
}
