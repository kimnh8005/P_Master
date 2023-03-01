package kr.co.pulmuone.v1.display.popup.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.display.popup.dto.AddDisplayPopupRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.GetDisplayPopupListRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.GetDisplayPopupListResponseDto;
import kr.co.pulmuone.v1.display.popup.dto.PreviewPopupResponseDto;
import kr.co.pulmuone.v1.display.popup.dto.PutDisplayPopupDetailResponseDto;
import kr.co.pulmuone.v1.display.popup.dto.PutDisplayPopupRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetDisplayPopupListResultVo;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetPopupInfoMallResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * BOS 팝업 관리 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    2020. 11. 18.             최성현          최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class DisplayPopupBizImpl implements DisplayPopupBiz {

	@Autowired
	private DisplayPopupService displayPopupService;

	/**
	 * @Desc mall 메인 팝업 리스트 조회(mobile)
	 * @param List<String>
	 * @param List<String>
	 * @param List<String>
	 * @return List<GetPopupInfoMallResultVo>
	 */
	@Override
	public List<GetPopupInfoMallResultVo> getPopupInfoMobile(List<String> dpTargetTypeList,
			List<String> dpRangeTypeList, List<String> todayStopIdList) {
		return displayPopupService.getPopupInfoMobile(dpTargetTypeList, dpRangeTypeList, todayStopIdList);
	}

	/**
	 * @Desc mall 메인 팝업 리스트 조회(pc)
	 * @param List<String>
	 * @param List<String>
	 * @param List<String>
	 * @return List<GetPopupInfoMallResultVo>
	 */
	@Override
	public List<GetPopupInfoMallResultVo> getPopupInfoPc(List<String> dpTargetTypeList, List<String> dpRangeTypeList,
			List<String> todayStopIdList) {
		return displayPopupService.getPopupInfoPc(dpTargetTypeList, dpRangeTypeList, todayStopIdList);
	}

	/**
	 * @Desc 팝업 설정 조회
	 * @param getDisplayPopupListRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getPopupList(GetDisplayPopupListRequestDto getDisplayPopupListRequestDto) {

		GetDisplayPopupListResponseDto getDisplayPopupListResponseDto = new GetDisplayPopupListResponseDto();
		if (getDisplayPopupListRequestDto.getDisplayTargetType() != "") {
			getDisplayPopupListRequestDto
					.setDisplayTargetTypeList(Arrays.asList(getDisplayPopupListRequestDto.getDisplayTargetType().split("∀")));
		}
		if (getDisplayPopupListRequestDto.getExposureState() != "") {
			getDisplayPopupListRequestDto
					.setExposureStateList(Arrays.asList(getDisplayPopupListRequestDto.getExposureState().split("∀")));
		}
		if (getDisplayPopupListRequestDto.getDisplayRangeType() != "") {
			getDisplayPopupListRequestDto
					.setDisplayRangeTypeList(Arrays.asList(getDisplayPopupListRequestDto.getDisplayRangeType().split("∀")));
		}
		Page<GetDisplayPopupListResultVo> getDisplayPopupListResultList = displayPopupService
				.getPopupList(getDisplayPopupListRequestDto);

		getDisplayPopupListResponseDto.setTotal(getDisplayPopupListResultList.getTotal());
		getDisplayPopupListResponseDto.setRows(getDisplayPopupListResultList.getResult());

		return ApiResult.success(getDisplayPopupListResponseDto);
	}

	/**
	 * @Desc 팝업 등록
	 * @param addDisplayPopupRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> addPopup(AddDisplayPopupRequestDto addDisplayPopupRequestDto) {
		return ApiResult.success(displayPopupService.addPopup(addDisplayPopupRequestDto));
	}

	/**
	 * @Desc 팝업 내용 상세 조회
	 * @param displayFrontPopupId
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> putPopupDetail(String displayFrontPopupId) {

		PutDisplayPopupDetailResponseDto putDisplayDetailResponseDto = displayPopupService
				.putPopupDetail(displayFrontPopupId);

		return ApiResult.success(putDisplayDetailResponseDto);
	}

	/**
	 * @Desc 팝업 수정
	 * @param addDisplayPopupRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> putPopup(PutDisplayPopupRequestDto putDisplayPopupRequestDto) {
		return ApiResult.success(displayPopupService.putPopup(putDisplayPopupRequestDto));
	}

	/**
	 * @Desc 팝업 내용 삭제
	 * @param displayFrontPopupId
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> delPopup(String displayFrontPopupId) {
		return ApiResult.success(displayPopupService.delPopup(displayFrontPopupId));
	}

	/**
	 * @Desc 팝업 내용 미리보기
	 * @param displayFrontPopupId
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> previewPopup(String displayFrontPopupId) {
		PreviewPopupResponseDto previewPopupResponseDto = displayPopupService
				.previewPopup(displayFrontPopupId);
		return ApiResult.success(previewPopupResponseDto);
	}

}