package kr.co.pulmuone.v1.shopping.recently.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.shopping.recently.ShoppingRecentlyMapper;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserResponseDto;
import kr.co.pulmuone.v1.shopping.recently.dto.vo.RecentlyViewVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200824   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingRecentlyService {

    private final ShoppingRecentlyMapper shoppingRecentlyMapper;


    /**
     * 최근 본 상품 조회
     *
     * @param urUserId, ilGoodsId
     * @return HashMap
     * @throws Exception
     */
    protected HashMap<String, Object> getGoodsRecentlyView(String urUserId, Long ilGoodsId) throws Exception {
        return shoppingRecentlyMapper.getGoodsRecentlyView(urUserId, ilGoodsId);
    }


    /**
     * 최근 본 상품 추가
     *
     * @param urUserId, ilGoodsId
     * @throws Exception
     */
    protected void addGoodsRecentlyView(String urUserId, Long ilGoodsId) throws Exception {
        shoppingRecentlyMapper.addGoodsRecentlyView(urUserId, ilGoodsId);
    }


    /**
     * 최근 본 상품 일자 업데이트
     *
     * @param urUserId, ilGoodsId
     * @throws Exception
     */
    protected void putGoodsRecentlyViewLastViewDate(String urUserId, Long ilGoodsId) throws Exception {
        shoppingRecentlyMapper.putGoodsRecentlyViewLastViewDate(urUserId, ilGoodsId);
    }


    /**
     * 최근 본 상품 삭제
     *
     * @param urUserId, ilGoodsId
     * @throws Exception
     */
    protected void delGoodsRecentlyViewLimit(String urUserId) throws Exception {
        List<Long> list = shoppingRecentlyMapper.getGoodsRecentlyViewLimit(urUserId);
		if (list != null && !list.isEmpty()) {
			shoppingRecentlyMapper.delRecentlyViewLimitBySpRecentlyViewId(list);
		}
    }

    /**
     * 최근 본 상품 조회 by PcidCd
     *
     * @param urPcidCd String
     * @param ilGoodsId String
     * @return RecentlyViewVo
     * @throws Exception Exception
     */
    protected RecentlyViewVo getGoodsRecentlyViewByUrPcidCd(String urPcidCd, Long ilGoodsId) throws Exception {
        return shoppingRecentlyMapper.getGoodsRecentlyViewByUrPcidCd(urPcidCd, ilGoodsId);
    }


    /**
     * 최근 본 상품 추가 by PcidCd
     *
     * @param urPcidCd String
     * @param ilGoodsId String
     * @throws Exception Exception
     */
    protected void addRecentlyViewFromNonMember(String urPcidCd, Long ilGoodsId) throws Exception {
        shoppingRecentlyMapper.addRecentlyViewFromNonMember(urPcidCd, ilGoodsId);
    }


    /**
     * 최근 본 상품 일자 업데이트 by PcidCd
     *
     * @param urPcidCd String
     * @param ilGoodsId String
     * @throws Exception Exception
     */
    protected void putRecentlyViewLastViewDateByPcidCd(String urPcidCd, Long ilGoodsId) throws Exception {
        shoppingRecentlyMapper.putRecentlyViewLastViewDateByPcidCd(urPcidCd, ilGoodsId);
    }

    /**
     * 최근 본 상품 삭제 by PcidCd
     *
     * @param urPcidCd String
     * @param ilGoodsId String
     * @throws Exception Exception
     */
    protected void delRecentlyViewLimitByPcidCd(String urPcidCd) throws Exception {
		List<Long> list = shoppingRecentlyMapper.getRecentlyViewLimitByPcidCd(urPcidCd);
		if (list != null && !list.isEmpty()) {
			shoppingRecentlyMapper.delRecentlyViewLimitBySpRecentlyViewId(list);
		}
    }

    /**
     * 최근 본 상품 비회원정보 -> 회원정보 Mapping
     *
     * @param urPcidCd String
     * @param urUserId Long
     * @throws Exception Exception
     */
    protected void mapRecentlyViewUserId(String urPcidCd, Long urUserId) throws Exception {
        // 예외처리 겹치는 경우
        // 최종일자로 업데이트 이후 삭제
        shoppingRecentlyMapper.putRecentlyViewNonMember(urPcidCd, urUserId);
        List<Long> list = shoppingRecentlyMapper.getRecentlyViewNonMemberOverlap(urPcidCd, urUserId);
        if (list != null && !list.isEmpty()) {
			shoppingRecentlyMapper.delRecentlyViewLimitBySpRecentlyViewId(list);
		}

        // 비회원 -> 회원 반영
        shoppingRecentlyMapper.putRecentlyViewUserId(urPcidCd, urUserId);

        // 100개 이상 삭제처리
        delGoodsRecentlyViewLimit(String.valueOf(urUserId));
    }

    /**
     * 최근 본 상품 조회 By User
     *
     * @param dto CommonGetRecentlyViewListByUserRequestDto
     * @return CommonGetRecentlyViewListByUserResponseDto
     * @throws Exception exception
     */
    protected CommonGetRecentlyViewListByUserResponseDto getRecentlyViewListByUser(CommonGetRecentlyViewListByUserRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<Long> rows = shoppingRecentlyMapper.getRecentlyViewListByUser(dto);
        return CommonGetRecentlyViewListByUserResponseDto.builder()
                .total((int) rows.getTotal())
                .goodsIdList(rows.getResult())
                .build();
    }

    /**
     * 최근 본 상품 삭제 By 상품코드
     *
     * @param ilGoodsId Long
     * @param urUserId  Long
     * @throws Exception exception
     */
    protected void delRecentlyViewByGoodsId(Long ilGoodsId, Long urUserId) throws Exception {
        shoppingRecentlyMapper.delRecentlyViewByGoodsId(ilGoodsId, urUserId);
    }

    /**
     * 최근 본 상품 삭제 By 유저 코드
     *
     * @param urUserId  Long
     * @throws Exception exception
     */
    protected void delRecentlyViewByUserId(Long urUserId) throws Exception {
        shoppingRecentlyMapper.delRecentlyViewByUserId(urUserId);
    }

}
