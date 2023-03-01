package kr.co.pulmuone.mall.shopping.favorites;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.ShoppingFavoritesDto;
import kr.co.pulmuone.mall.shopping.favorites.dto.GetFavoritesGoodsListByUserResponseDto;
import kr.co.pulmuone.mall.shopping.favorites.service.ShoppingFavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200824   	 	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class ShoppingFavoritesController {

    @Autowired
    public ShoppingFavoritesService shoppingFavoritesService;


    /**
     * 찜 추가
     *
     * @param ilGoodsId
     * @throws Exception
     */
    @PostMapping(value = "/shopping/favorites/addGoodsFavorites")
    @ApiOperation(value = "찜 추가", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "ilGoodsId", value = "상품 PK", required = true, dataType = "Long")})
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ShoppingFavoritesDto.class),
            @ApiResponse(code = 901, message = "" +
                    "NEED_LOGIN - 로그인필요"
            )
    })
    public ApiResult<?> addGoodsFavorites(@RequestParam(value = "ilGoodsId", required = true) Long ilGoodsId) throws Exception {

        return shoppingFavoritesService.addGoodsFavorites(ilGoodsId);
    }


    /**
     * 찜 삭제
     *
     * @param spFavoritesGoodsId
     * @throws Exception
     */
    @PostMapping(value = "/shopping/favorites/delGoodsFavorites")
    @ApiOperation(value = "찜 삭제", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "spFavoritesGoodsId", value = "찜한 상품 PK", required = true, dataType = "Long")})
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" +
                    "NEED_LOGIN - 로그인필요"
            )
    })
    public ApiResult<?> delGoodsFavorites(@RequestParam(value = "spFavoritesGoodsId", required = true) Long spFavoritesGoodsId) throws Exception {

        return shoppingFavoritesService.delGoodsFavorites(spFavoritesGoodsId);
    }

    /**
     * 찜 상품 조회
     *
     * @param dto CommonGetFavoritesGoodsListByUserRequestDto
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/shopping/favorites/getFavoritesGoodsListByUser")
    @ApiOperation(value = "찜 상품 조회", httpMethod = "POST", notes = "찜 상품 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetFavoritesGoodsListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "NEED_LOGIN - 로그인필요"
            )
    })
    public ApiResult<?> getFavoritesGoodsListByUser(CommonGetFavoritesGoodsListByUserRequestDto dto) throws Exception {
        return shoppingFavoritesService.getFavoritesGoodsListByUser(dto);
    }

    /**
     * 찜 상품 삭제
     *
     * @param ilGoodsId Long
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @PostMapping(value = "/shopping/favorites/delFavoritesGoodsByGoodsId")
    @ApiOperation(value = "찜 상품 삭제 - 상품 PK", httpMethod = "POST", notes = "찜 상품 삭제 - 상품 PK")
    @ApiImplicitParams({@ApiImplicitParam(name = "ilGoodsId", value = "상품 PK", required = true, dataType = "Long")})
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" +
                    "NEED_LOGIN - 로그인필요"
            )
    })
    public ApiResult<?> delFavoritesGoodsByGoodsId(Long ilGoodsId) throws Exception {
        return shoppingFavoritesService.delFavoritesGoodsByGoodsId(ilGoodsId);
    }
}