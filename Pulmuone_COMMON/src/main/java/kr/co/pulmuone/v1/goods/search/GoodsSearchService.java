package kr.co.pulmuone.v1.goods.search;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.constants.GoodsSortComparator;
import kr.co.pulmuone.v1.comm.mapper.goods.search.GoodsSearchMapper;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchNewGoodsRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchAdditionalVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchExperienceVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchHmrVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.search.searcher.constants.SortCode;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service("CommonGoodsSearchService")
@RequiredArgsConstructor
public class GoodsSearchService {

    private final GoodsSearchMapper goodsSearchMapper;

    /**
     * 상품정보조회 - 체험단
     *
     * @param ilGoodsId Long
     * @return GoodsSearchExperienceVo
     * @throws Exception
     */
    protected GoodsSearchExperienceVo getGoodsSearchExperience(Long ilGoodsId) throws Exception {
        return goodsSearchMapper.getGoodsSearchExperience(ilGoodsId);
    }


    /**
     * 상품아이디로 상품리스트 조회
     *
     * @param GoodsSearchByGoodsIdRequestDto
     * @return List<GoodsSearchResultDto>
     * @throws Exception
     */
    protected List<GoodsSearchResultDto> searchGoodsByGoodsIdList(GoodsSearchByGoodsIdRequestDto reqDto) throws Exception {
		List<GoodsSearchResultDto> result = new ArrayList<>();
		// 상품 아이디 있을때만 조회
		if (reqDto.getGoodsIdList() != null && !reqDto.getGoodsIdList().isEmpty()) {
            // 정렬순서 보정
            if(reqDto.isEmployee()){    // 임직원
                if (!StringUtils.isEmpty(reqDto.getGoodsSortCode())) {
                    if(reqDto.getGoodsSortCode().equals(SortCode.LOW_PRICE.name())){
                        reqDto.setGoodsSortCode(SortCode.EMPLOYEE_LOW_PRICE.name());
                    }
                    if(reqDto.getGoodsSortCode().equals(SortCode.HIGH_PRICE.name())){
                        reqDto.setGoodsSortCode(SortCode.EMPLOYEE_HIGH_PRICE.name());
                    }
                    if(reqDto.getGoodsSortCode().equals(SortCode.HIGH_DISCOUNT_RATE.name())){
                        reqDto.setGoodsSortCode(SortCode.HIGH_EMPLOYEE_DISCOUNT_RATE.name());
                    }
                }
            }

			result = goodsSearchMapper.searchGoodsByGoodsIdList(reqDto);

			// 정렬순서 지정하지 않은 경우, goodsIdList 순서와 동일하게 정렬
			if (StringUtils.isEmpty(reqDto.getGoodsSortCode())) {
				Collections.sort(result, new GoodsSortComparator(reqDto.getGoodsIdList()));
			}
		}
        return result;
    }

    /**
     * 일일배송 상품 조회
     *
     * @param getDailyGoods String
     * @return List<Long>
     * @throws Exception
     */
    protected List<Long> getDailyGoods(String getDailyGoods) throws Exception {
        return goodsSearchMapper.getDailyGoods(getDailyGoods);
    }

    /**
     * 상품조회 - 아웃몰 Validation
     *
     * @param goodsIdList List<Long>
     * @return List<GoodsSearchOutMallVo>
     * @throws Exception
     */
    protected List<GoodsSearchOutMallVo> getGoodsFromOutMall(List<String> goodsIdList) throws Exception {
        // PS_CONFIG 설정 조회
        String psKey = Constants.PULSHOP_GOODS_MAPPING_KEY;
        String psVal = goodsSearchMapper.getPsConfig(psKey);
        if ("Y".equals(psVal)){ // 풀샵 조회
            return goodsSearchMapper.getGoodsFromOutMall_PULSHOP(goodsIdList);
        } else { // 샵풀무원 조회
            return goodsSearchMapper.getGoodsFromOutMall(goodsIdList);
        }
    }

    /**
     * 신규 상품 조회
     *
     * @return List<Long>
     * @throws Exception
     */
    protected List<Long> getNewGoods(GoodsSearchNewGoodsRequestDto dto)  {
        return goodsSearchMapper.getNewGoods(dto);
    }

    /**
     * 상품조회 - 추가상품
     *
     * @param goodsIdList List<Long>
     * @return List<GoodsSearchAdditionalVo>
     * @throws Exception Exception
     */
    protected List<GoodsSearchAdditionalVo> getGoodsAdditional(List<Long> goodsIdList) throws Exception {
        return goodsSearchMapper.getGoodsAdditional(goodsIdList);
    }

    /**
     * 상품조회 - 마이페이지 - 쿠폰적용대상
     *
     * @param wareHouseIdList List<Long>
     * @return List<String>
     * @throws Exception Exception
     */
    protected List<String> getGoodsCouponCoverageByUrWareHouseId(List<Long> wareHouseIdList) throws Exception {
        return goodsSearchMapper.getGoodsCouponCoverageByUrWareHouseId(wareHouseIdList);
    }

    /**
     * 상품조회 - 올가 매장 전용관 - HMR/손질 상품
     *
     * @param dto GoodsSearchStoreGoodsRequestDto
     * @return List<GoodsSearchHmrVo>
     * @throws Exception Exception
     */
    protected List<GoodsSearchHmrVo> getGoodsSearchHmr(GoodsSearchStoreGoodsRequestDto dto) throws Exception {
        return goodsSearchMapper.getGoodsSearchHmr(dto);
    }

    /**
     * 상품조회 - 올가 매장 전용관 - 매장 전용 상품
     *
     * @param dto GoodsSearchStoreGoodsRequestDto
     * @return Page<Long>
     * @throws Exception Exception
     */
    protected Page<Long> getGoodsSearchShopOnly(GoodsSearchStoreGoodsRequestDto dto) throws Exception {
        PageMethod.startPage(0, 999);
        return goodsSearchMapper.getGoodsSearchShopOnly(dto);
    }

    /**
     * 상품조회 - 올가 매장 전용관 - 매장 전용 상품
     *
     * @param discountType
     * @return Page<Long>
     * @throws Exception Exception
     */
    protected Page<Long> getOrgaFlyerGoods(String discountType) throws Exception {
        return goodsSearchMapper.getOrgaFlyerGoods(discountType);
    }

}
