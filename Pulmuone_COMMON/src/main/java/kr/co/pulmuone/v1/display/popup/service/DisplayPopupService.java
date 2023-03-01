package kr.co.pulmuone.v1.display.popup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.display.DisplayPopupMapper;
import kr.co.pulmuone.v1.display.popup.dto.AddDisplayPopupRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.GetDisplayPopupListRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.PreviewPopupResponseDto;
import kr.co.pulmuone.v1.display.popup.dto.PutDisplayPopupDetailResponseDto;
import kr.co.pulmuone.v1.display.popup.dto.PutDisplayPopupRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetDisplayPopupListResultVo;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetPopupInfoMallResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * BOS 팝업관리 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 11. 18.                최성현          최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class DisplayPopupService {

    @Autowired
    private DisplayPopupMapper displayPopupMapper;

    protected List<GetPopupInfoMallResultVo> getPopupInfoMobile(List<String> dpTargetTypeList, List<String> dpRangeTypeList, List<String> todayStopIdList) {
        return displayPopupMapper.getPopupInfo(dpTargetTypeList, dpRangeTypeList, todayStopIdList);
    }

    protected List<GetPopupInfoMallResultVo> getPopupInfoPc(List<String> dpTargetTypeList, List<String> dpRangeTypeList, List<String> todayStopIdList) {
        return displayPopupMapper.getPopupInfo(dpTargetTypeList, dpRangeTypeList, todayStopIdList);
    }


    protected Page<GetDisplayPopupListResultVo> getPopupList(GetDisplayPopupListRequestDto getDisplayPopupListRequestDto) {
        PageMethod.startPage(getDisplayPopupListRequestDto.getPage(), getDisplayPopupListRequestDto.getPageSize());
        return displayPopupMapper.getPopupList(getDisplayPopupListRequestDto);
    }

    protected int addPopup(AddDisplayPopupRequestDto addDisplayPopupRequestDto) {

        if(addDisplayPopupRequestDto.getAddFileList().size() != 0) {
            String PopupImagePath= addDisplayPopupRequestDto.getAddFileList().get(0).getServerSubPath() + addDisplayPopupRequestDto.getAddFileList().get(0).getPhysicalFileName();
            String PopupImageOriginName=addDisplayPopupRequestDto.getAddFileList().get(0).getOriginalFileName();
            addDisplayPopupRequestDto.setPopupImagePath(PopupImagePath);
            addDisplayPopupRequestDto.setPopupImageOriginName(PopupImageOriginName);
        }
        return displayPopupMapper.addPopup(addDisplayPopupRequestDto);
    }

	protected PutDisplayPopupDetailResponseDto putPopupDetail(String displayFrontPopupId) {
		return displayPopupMapper.putPopupDetail(displayFrontPopupId);
	}

	protected int putPopup(PutDisplayPopupRequestDto putDisplayPopupRequestDto) {
		 if(putDisplayPopupRequestDto.getAddFileList().size() != 0) {
	            String popupImagePath= putDisplayPopupRequestDto.getAddFileList().get(0).getServerSubPath() + putDisplayPopupRequestDto.getAddFileList().get(0).getPhysicalFileName();
	            String popupImageOriginName=putDisplayPopupRequestDto.getAddFileList().get(0).getOriginalFileName();
	            putDisplayPopupRequestDto.setPopupImagePath(popupImagePath);
	            putDisplayPopupRequestDto.setPopupImageOriginName(popupImageOriginName);
	        }

	        return displayPopupMapper.putPopup(putDisplayPopupRequestDto);
	}

	protected int delPopup(String displayFrontPopupId) {
		return displayPopupMapper.delPopup(displayFrontPopupId);
	}

	protected PreviewPopupResponseDto previewPopup(String displayFrontPopupId) {
		return displayPopupMapper.previewPopup(displayFrontPopupId);
	}


}

