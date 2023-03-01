package kr.co.pulmuone.mall.display.popup.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.DisplayEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.util.CookieUtil;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetPopupInfoMallResultVo;
import kr.co.pulmuone.v1.display.popup.service.DisplayPopupBiz;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * Class 의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20201123   	 	천혜현           최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class DisplayPopupMallServiceImpl implements DisplayPopupMallService {

    @Autowired
    DisplayPopupBiz displayPopupBiz;

    @Override
    public ApiResult<?> getPopupInfoMobile(HttpServletRequest request) throws Exception {
    	BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    	String urUserId = StringUtil.nvl(buyerVo.getUrUserId());

    	//오늘 그만 보기 관련 쿠키 정보 가져오기
    	List<String> todayStopIdList = CookieUtil.getLikeNameCookieList(request, "todayStop");

    	//팝업 목록 조회 요청 파라미터값 세팅
        //전시대상 타입
        List<String> dpTargetTypeList = new ArrayList<>();
        dpTargetTypeList.add(DisplayEnums.DpTargetTp.ALL.getCode());
        if(StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode())){
            dpTargetTypeList.add(DisplayEnums.DpTargetTp.EMPLOYEE.getCode());
        } else {
            if(StringUtil.isNotEmpty(urUserId)){
                dpTargetTypeList.add(DisplayEnums.DpTargetTp.NORMAL.getCode());
            }
        }

        //전시범위 타입
        List<String> dpRangeTypeList = new ArrayList<>();
        dpRangeTypeList.add(DisplayEnums.DpRangeTp.ALL.getCode());
        if(DeviceUtil.getDirInfo().equalsIgnoreCase(GoodsEnums.DeviceType.PC.getCode())) {
        	dpRangeTypeList.add(DisplayEnums.DpRangeTp.PC.getCode());
        }else {
        	dpRangeTypeList.add(DisplayEnums.DpRangeTp.MOBILE.getCode());
        }

    	//팝업 목록 조회ei
        List<GetPopupInfoMallResultVo> popupList = displayPopupBiz.getPopupInfoMobile(dpTargetTypeList, dpRangeTypeList, todayStopIdList);
        return ApiResult.success(popupList);
    }

    @Override
    public ApiResult<?> getPopupInfoPc(HttpServletRequest request) throws Exception {
    	BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    	String urUserId = StringUtil.nvl(buyerVo.getUrUserId());

    	//오늘 그만 보기 관련 쿠키 정보 가져오기
    	List<String> todayStopIdList = CookieUtil.getLikeNameCookieList(request, "todayStop");

    	//팝업리스트 조회 요청 파라미터값 세팅
        //전시대상 타입
        List<String> dpTargetTypeList = new ArrayList<>();
        dpTargetTypeList.add(DisplayEnums.DpTargetTp.ALL.getCode());
        if(StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode())){
            dpTargetTypeList.add(DisplayEnums.DpTargetTp.EMPLOYEE.getCode());
        }else{
            if(StringUtil.isNotEmpty(urUserId)){
                dpTargetTypeList.add(DisplayEnums.DpTargetTp.NORMAL.getCode());
            }
        }

        //전시범위 타입
        List<String> dpRangeTypeList = new ArrayList<>();
        dpRangeTypeList.add(DisplayEnums.DpRangeTp.ALL.getCode());
        if(DeviceUtil.getDirInfo().equalsIgnoreCase(GoodsEnums.DeviceType.PC.getCode())) {
        	dpRangeTypeList.add(DisplayEnums.DpRangeTp.PC.getCode());
        }else {
        	dpRangeTypeList.add(DisplayEnums.DpRangeTp.MOBILE.getCode());
        }

    	//팝업 리스트 조회
        List<GetPopupInfoMallResultVo> popupList = displayPopupBiz.getPopupInfoPc(dpTargetTypeList, dpRangeTypeList, todayStopIdList);

        return ApiResult.success(popupList);
    }

}
