package kr.co.pulmuone.v1.comm.mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.base.dto.GetClientPopupRequestDto;
import kr.co.pulmuone.v1.base.dto.GetGrantAuthEmployeePopupRequestDto;
import kr.co.pulmuone.v1.base.dto.GoodsSearchRequestDto;
import kr.co.pulmuone.v1.base.dto.vo.GetClientPopupResultVo;
import kr.co.pulmuone.v1.base.dto.vo.GetGrantAuthEmployeePopupResultVo;
import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponListResultVo;

@Mapper
public interface PopupCommonMapper {

    /**
     * @Desc 거래처 조회
     * @param popupCommonRequestDto
     * @return
     * @throws Exception
     */
    List<GetClientPopupResultVo> getClientList(GetClientPopupRequestDto popupCommonRequestDto);

    /**
     * @Desc 담당자 조회
     * @param getGrantAuthEmployeePopupRequestDto
     * @return
     * @throws Exception
     */
    List<GetGrantAuthEmployeePopupResultVo> getGrantAuthEmployeeList(GetGrantAuthEmployeePopupRequestDto getGrantAuthEmployeePopupRequestDto);

    /**
     * @Desc 상품 검색 목록
     * @param goodsSearchRequestDto
     * @return List<GoodsSearchVo>
     */
    Page<GoodsSearchVo> getGoodsList(GoodsSearchRequestDto goodsSearchRequestDto);

    /**
     * @Desc 쿠폰 리스트 조회
     * @param CouponRequestDto
     * @return Page<CouponListResultVo>
     */
	Page<CouponListResultVo> getCouponList(CouponRequestDto couponRequestDto) throws Exception;

}
