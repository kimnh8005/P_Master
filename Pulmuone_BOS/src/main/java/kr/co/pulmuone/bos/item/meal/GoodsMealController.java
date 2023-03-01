package kr.co.pulmuone.bos.item.meal;

import io.swagger.annotations.ApiResponse;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.GoodsMealListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.GoodsMealListResponseDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsMealBizImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GoodsMealController {

    @Autowired
    private GoodsMealBizImpl goodsMealBizImpl;


    /**
     * 일일상품 식단 등록 내역 조회
     *
     * @param goodsMealListRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @PostMapping(value = "/admin/il/meal/getGoodsMealList")
    @ApiResponse(code = 900, message = "response data", response = GoodsMealListResponseDto.class)
    public ApiResult<?> getGoodsMealList(HttpServletRequest request, GoodsMealListRequestDto goodsMealListRequestDto) throws Exception {
        return goodsMealBizImpl.getGoodsMealList((GoodsMealListRequestDto) BindUtil.convertRequestToObject(request, GoodsMealListRequestDto.class));
    }

}
