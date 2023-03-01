package kr.co.pulmuone.v1.comm.mapper.display;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.display.popup.dto.AddDisplayPopupRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.GetDisplayPopupListRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.PreviewPopupResponseDto;
import kr.co.pulmuone.v1.display.popup.dto.PutDisplayPopupDetailResponseDto;
import kr.co.pulmuone.v1.display.popup.dto.PutDisplayPopupRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetDisplayPopupListResultVo;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetPopupInfoMallResultVo;

@Mapper
public interface DisplayPopupMapper {

	List<GetPopupInfoMallResultVo> getPopupInfo(@Param("dpTargetTypeList") List<String> dpTargetTypeList,
												@Param("dpRangeTypeList") List<String> dpRangeTypeList,
												@Param("todayStopIdList") List<String> todayStopIdList);

    Page<GetDisplayPopupListResultVo> getPopupList(GetDisplayPopupListRequestDto getDisplayPopupListRequestDto);

    int addPopup(AddDisplayPopupRequestDto addDisplayPopupRequestDto);

	PutDisplayPopupDetailResponseDto putPopupDetail(String displayFrontPopupId);

	int putPopup(PutDisplayPopupRequestDto putDisplayPopupRequestDto);

	int delPopup(String displayFrontPopupId);

	PreviewPopupResponseDto previewPopup(String displayFrontPopupId);
}
