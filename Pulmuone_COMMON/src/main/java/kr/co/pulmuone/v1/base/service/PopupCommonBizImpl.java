package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.ExcelDownLogRequestDto;
import kr.co.pulmuone.v1.base.dto.GetClientPopupRequestDto;
import kr.co.pulmuone.v1.base.dto.GetGrantAuthEmployeePopupRequestDto;
import kr.co.pulmuone.v1.base.dto.GoodsSearchRequestDto;
import kr.co.pulmuone.v1.base.dto.GoodsSearchResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponListResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponListResultVo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;


@Service
public class PopupCommonBizImpl implements PopupCommonBiz {

    @Autowired
    PopupCommonService popupCommonService;

    @Override
    public ApiResult<?> getClientList(GetClientPopupRequestDto popupCommonRequestDto) {
        return popupCommonService.getClientList(popupCommonRequestDto);
    }

    @Override
    public ApiResult<?> getGrantAuthEmployeeList(GetGrantAuthEmployeePopupRequestDto getGrantAuthEmployeePopupRequestDto) {
        return popupCommonService.getGrantAuthEmployeeList(getGrantAuthEmployeePopupRequestDto);
    }

    @Override
    public ApiResult<?> getGoodsList(GoodsSearchRequestDto goodsSearchRequestDto) {

    	GoodsSearchResponseDto result = new GoodsSearchResponseDto();

    	Page<GoodsSearchVo> rows = popupCommonService.getGoodsList(goodsSearchRequestDto);

    	result.setTotal(rows.getTotal());
    	result.setRows(rows.getResult());

        return ApiResult.success(result);

//        return popupCommonService.getGoodsList(goodsSearchRequestDto);
    }

    @Override
    public ApiResult<?> addExcelDownReason(ExcelDownLogRequestDto excelDownLogRequestDto, HttpServletRequest req) {
    	return ApiResult.success(popupCommonService.addExcelDownReason(excelDownLogRequestDto, req));
    }

	/**
	 * 쿠폰목록 조회
	 *
	 * @param CouponRequestDto
	 * @return CouponListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getCouponList(CouponRequestDto couponRequestDto) throws Exception {
		CouponListResponseDto result = new CouponListResponseDto();

		Page<CouponListResultVo> couponList = popupCommonService.getCouponList(couponRequestDto);

		result.setTotal(couponList.getTotal());
		result.setRows(couponList.getResult());

		return ApiResult.success(result);
	}

}
