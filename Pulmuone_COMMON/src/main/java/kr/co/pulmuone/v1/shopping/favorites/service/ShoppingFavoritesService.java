package kr.co.pulmuone.v1.shopping.favorites.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.shopping.favorites.ShoppingFavoritesMapper;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserResponseDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.ShoppingFavoritesParamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class ShoppingFavoritesService {

    private final ShoppingFavoritesMapper shoppingFavoritesMapper;


    /**
     * 찜 상품 조회
     *
     * @param ShoppingFavoritesParamDto
     * @return spFavoritesGoodsId
     * @throws Exception
     */
    protected Long getGoodsFavorites(ShoppingFavoritesParamDto shoppingFavoritesParamDto) throws Exception {
        return shoppingFavoritesMapper.getGoodsFavorites(shoppingFavoritesParamDto);
    }


    /**
     * 찜 추가
     *
     * @param ShoppingFavoritesParamDto
     * @return spFavoritesGoodsId
     * @throws Exception
     */
    protected Long addGoodsFavorites(ShoppingFavoritesParamDto shoppingFavoritesParamDto) throws Exception {
        shoppingFavoritesMapper.addGoodsFavorites(shoppingFavoritesParamDto);
        return shoppingFavoritesParamDto.getSpFavoritesGoodsId();
    }


    /**
     * 찜 삭제
     *
     * @param ShoppingFavoritesParamDto
     * @throws Exception
     */
    protected void delGoodsFavorites(ShoppingFavoritesParamDto shoppingFavoritesParamDto) throws Exception {
        shoppingFavoritesMapper.delGoodsFavorites(shoppingFavoritesParamDto);
    }

    /**
     * 찜 상품 조회 By User
     *
     * @param dto CommonGetFavoritesGoodsListByUserRequestDto
     * @return CommonGetFavoritesGoodsListByUserResponseDto
     * @throws Exception exception
     */
    protected CommonGetFavoritesGoodsListByUserResponseDto getFavoritesGoodsListByUser(CommonGetFavoritesGoodsListByUserRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<Long> rows = shoppingFavoritesMapper.getFavoritesGoodsListByUser(dto);
        return CommonGetFavoritesGoodsListByUserResponseDto.builder()
                .total((int) rows.getTotal())
                .goodsIdList(rows.getResult())
                .build();
    }

    /**
     * 찜 삭제
     *
     * @param ilGoodsId Long
     * @param urUserId  Long
     * @throws Exception Exception
     */
    protected void delFavoritesGoodsByGoodsId(Long ilGoodsId, Long urUserId) throws Exception {
        shoppingFavoritesMapper.delFavoritesGoodsByGoodsId(ilGoodsId, urUserId);
    }

}
