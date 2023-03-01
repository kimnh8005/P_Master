package kr.co.pulmuone.v1.base.service;

import javax.servlet.http.HttpServletRequest;

import kr.co.pulmuone.v1.base.dto.ExcelDownLogRequestDto;
import kr.co.pulmuone.v1.base.dto.GetClientPopupRequestDto;
import kr.co.pulmuone.v1.base.dto.GetGrantAuthEmployeePopupRequestDto;
import kr.co.pulmuone.v1.base.dto.GoodsSearchRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponRequestDto;

public interface PopupCommonBiz {

    ApiResult<?> getClientList(GetClientPopupRequestDto popupCommonRequestDto);
    ApiResult<?> getGrantAuthEmployeeList(GetGrantAuthEmployeePopupRequestDto getGrantAuthEmployeePopupRequestDto);
    ApiResult<?> getGoodsList(GoodsSearchRequestDto goodsSearchRequestDto);
	ApiResult<?> addExcelDownReason(ExcelDownLogRequestDto excelDownLogRequestDto, HttpServletRequest req);
	ApiResult<?> getCouponList(CouponRequestDto couponRequestDto) throws Exception;
}
