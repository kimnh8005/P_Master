package kr.co.pulmuone.mall.shopping.recently;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserRequestDto;
import kr.co.pulmuone.mall.shopping.recently.dto.GetRecentlyViewListByUserResponseDto;
import kr.co.pulmuone.mall.shopping.recently.service.ShoppingRecentlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
 * 1.0    20200831   	 	이원호            최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
public class ShoppingRecentlyController {
    @Autowired
    public ShoppingRecentlyService shoppingRecentlyService;

    @PostMapping(value = "/shopping/recently/getRecentlyViewListByUser")
    @ApiOperation(value = "최근 본 상품 조회", httpMethod = "POST", notes = "최근 본 상품 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetRecentlyViewListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "NEED_LOGIN - 로그인필요"
            )
    })
    public ApiResult<?> getRecentlyViewListByUser(CommonGetRecentlyViewListByUserRequestDto dto) throws Exception {
        return shoppingRecentlyService.getRecentlyViewListByUser(dto);
    }

    @PostMapping(value = "/shopping/recently/delRecentlyViewByGoodsId")
    @ApiOperation(value = "최근 본 상품 삭제", httpMethod = "POST", notes = "최근 본 상품 삭제")
    @ApiImplicitParams({@ApiImplicitParam(name = "ilGoodsId", value = "상품 PK", required = true, dataType = "Long")})
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" +
                    "NEED_LOGIN - 로그인필요"
            )
    })
    public ApiResult<?> delRecentlyViewByGoodsId(Long ilGoodsId) throws Exception {
        return shoppingRecentlyService.delRecentlyViewByGoodsId(ilGoodsId);
    }

}
