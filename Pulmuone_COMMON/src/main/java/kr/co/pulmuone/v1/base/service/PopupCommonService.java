package kr.co.pulmuone.v1.base.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import kr.co.pulmuone.v1.base.dto.*;
import kr.co.pulmuone.v1.base.dto.vo.GetClientPopupResultVo;
import kr.co.pulmuone.v1.base.dto.vo.GetGrantAuthEmployeePopupResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.mapper.base.PopupCommonMapper;
import kr.co.pulmuone.v1.comm.mapper.system.log.SystemLogMapper;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponListResultVo;
import kr.co.pulmuone.v1.comm.constants.Constants;

@Service
public class PopupCommonService {

    @Autowired
    PopupCommonMapper popupCommonMapper;

	@Autowired
	SystemLogMapper systemLogMapper;

	/**
	 * @Desc 거래처 조회
	 * @param getClientPopupRequestDto
	 * @return
	 * @throws Exception
	 */
	protected ApiResult<?> getClientList(GetClientPopupRequestDto getClientPopupRequestDto) {
		GetClientPopupResponseDto result = new GetClientPopupResponseDto();

	    if( StringUtils.isNotEmpty(getClientPopupRequestDto.getClientType()) ) {
	        getClientPopupRequestDto.setClientTypeList(Stream.of(getClientPopupRequestDto.getClientType().split(Constants.ARRAY_SEPARATORS))
	                                                      .map(String::trim)
	                                                      .filter(StringUtils::isNotEmpty)
	                                                      .collect(Collectors.toList()));
	    }

		List<GetClientPopupResultVo> rows = popupCommonMapper.getClientList(getClientPopupRequestDto);

		result.setRows(rows);

		return ApiResult.success(result);
	}

	/**
	 * @Desc 담당자 조회
	 * @param getGrantAuthEmployeePopupRequestDto
	 * @return
	 * @throws Exception
	 */
	protected ApiResult<?> getGrantAuthEmployeeList(GetGrantAuthEmployeePopupRequestDto getGrantAuthEmployeePopupRequestDto) {
		GetGrantAuthEmployeePopupResponseDto result = new GetGrantAuthEmployeePopupResponseDto();

		List<GetGrantAuthEmployeePopupResultVo> rows = popupCommonMapper.getGrantAuthEmployeeList(getGrantAuthEmployeePopupRequestDto);

		result.setRows(rows);

	    return ApiResult.success(result);
	}

    /**
     * @Desc 상품 검색
     * @param goodsSearchRequestDto
     * @return GoodsSearchResponseDto
     * @throws Exception
     */
	protected Page<GoodsSearchVo> getGoodsList(GoodsSearchRequestDto goodsSearchRequestDto) {

		if( StringUtils.isNotEmpty(goodsSearchRequestDto.getFindKeyword()) ) {
            goodsSearchRequestDto.setFindKeywordList(Stream.of(goodsSearchRequestDto.getFindKeyword().split(Constants.ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS))
                                                           .map(String::trim)
                                                           .filter(StringUtils::isNotEmpty)
                                                           .collect(Collectors.toList()));
        }

        if( StringUtils.isNotEmpty(goodsSearchRequestDto.getGoodsType()) ) {
            goodsSearchRequestDto.setGoodsTypeList(Stream.of(goodsSearchRequestDto.getGoodsType().split(Constants.ARRAY_SEPARATORS))
                                                           .map(String::trim)
                                                           .filter(StringUtils::isNotEmpty)
                                                           .collect(Collectors.toList()));
        }

        if( StringUtils.isNotEmpty(goodsSearchRequestDto.getSaleStatus()) ) {
            goodsSearchRequestDto.setSaleStatusList(Stream.of(goodsSearchRequestDto.getSaleStatus().split(Constants.ARRAY_SEPARATORS))
                                                           .map(String::trim)
                                                           .filter(StringUtils::isNotEmpty)
                                                           .collect(Collectors.toList()));
        }

        if( StringUtils.isNotEmpty(goodsSearchRequestDto.getSaleType()) ) {
        	goodsSearchRequestDto.setSaleTypeList(Stream.of(goodsSearchRequestDto.getSaleType().split(Constants.ARRAY_SEPARATORS))
                    										.map(String::trim)
                    										.filter(StringUtils::isNotEmpty)
                    										.collect(Collectors.toList()));
        }
        PageMethod.startPage(goodsSearchRequestDto.getPage(), goodsSearchRequestDto.getPageSize());

        return popupCommonMapper.getGoodsList(goodsSearchRequestDto);
    }

    /**
     * @Desc 엑셀 다운로드 사유 등록
     * @param excelDownLogRequestDto
     * @param req
     * @return
     * @throws Exception
     * @return ApiResult<?>
     */
    protected int addExcelDownReason(ExcelDownLogRequestDto excelDownLogRequestDto, HttpServletRequest req)  {

        //접속 정보 받기
	    String addressIp = req.getHeader("X-FORWARDED-FOR");
	    if (addressIp == null){
	    	addressIp = req.getRemoteAddr();
	    }
	    excelDownLogRequestDto.setIp(addressIp);

        return systemLogMapper.addExcelDownReason(excelDownLogRequestDto);

    }

    /**
     * @Desc 쿠폰 리스트 조회
     * @param CouponRequestDto
     * @return Page<CouponListResultVo>
     */
    protected Page<CouponListResultVo> getCouponList(CouponRequestDto couponRequestDto) throws Exception{
        PageMethod.startPage(couponRequestDto.getPage(), couponRequestDto.getPageSize());
        return popupCommonMapper.getCouponList(couponRequestDto);
    }
}
