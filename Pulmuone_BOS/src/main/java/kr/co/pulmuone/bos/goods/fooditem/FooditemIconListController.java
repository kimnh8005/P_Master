package kr.co.pulmuone.bos.goods.fooditem;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconListRequestDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconRequestDto;
import kr.co.pulmuone.v1.goods.fooditem.service.FooditemIconListBiz;
import kr.co.pulmuone.v1.user.store.dto.StoreDetailRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
* <PRE>
* 식단품목 아이콘 리스트 Controller
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class FooditemIconListController {
    private final FooditemIconListBiz fooditemIconListBiz;

    /**
     * 식단품목아이콘 목록 조회
     * @param request
     * @param fooditemIconListRequestDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "식단품목아이콘 목록 조회")
	@PostMapping(value = "/admin/goods/fooditem/getFooditemIconList")
	public ApiResult<?> getIlCertificationList(HttpServletRequest request, FooditemIconListRequestDto fooditemIconListRequestDto) throws Exception{

        return ApiResult.success(fooditemIconListBiz.getFooditemIconList(BindUtil.bindDto(request, FooditemIconListRequestDto.class)));
	}

    /**
     * 식단품목아이콘 상세 조회
     * @param ilFooditemIconId
     * @return
     */
    @ApiOperation(value = "식단품목아이콘 상세 조회")
    @GetMapping(value = "/admin/goods/fooditem/getFooditemIcon")
    @ApiImplicitParams({ @ApiImplicitParam(name = "ilFooditemIconId", value = "식단품목아이콘 PK", required = true, dataType = "long") })
    public ApiResult<?> getFooditemIcon(@RequestParam(value = "ilFooditemIconId") long ilFooditemIconId){

        return ApiResult.success(fooditemIconListBiz.getFooditemIcon(ilFooditemIconId));
    }

    /**
     * 식단품목아이콘 추가
     * @param fooditemIconRequestDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "식단품목아이콘 추가")
	@PostMapping(value = "/admin/goods/fooditem/addFooditemIcon")
	public ApiResult<?> addFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto) throws Exception{

        fooditemIconRequestDto.setAddFileList(BindUtil.convertJsonArrayToDtoList(fooditemIconRequestDto.getAddFile(), FileVo.class));

        return fooditemIconListBiz.addFooditemIcon(fooditemIconRequestDto);
	}

    /**
     * 식단품목아이콘 수정
     * @param fooditemIconRequestDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "식단품목아이콘 수정")
	@PostMapping(value = "/admin/goods/fooditem/putFooditemIcon")
	public ApiResult<?> putFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto) throws Exception{

        if (fooditemIconRequestDto.getAddFile() != null) {
            fooditemIconRequestDto.setAddFileList(BindUtil.convertJsonArrayToDtoList(fooditemIconRequestDto.getAddFile(), FileVo.class));
        }

        return fooditemIconListBiz.putFooditemIcon(fooditemIconRequestDto);
	}

    /**
     * 식단품목아이콘 삭제
     * @param fooditemIconRequestDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "식단품목아이콘 삭제")
	@PostMapping(value = "/admin/goods/fooditem/delFooditemIcon")
	public ApiResult<?> delFooditemIcon(@RequestBody FooditemIconRequestDto fooditemIconRequestDto) throws Exception{

        return fooditemIconListBiz.delFooditemIcon(fooditemIconRequestDto);
	}

    /**
     * 식단품목아이콘 목록 드롭다운 리스트
     * @param fooditemIconListRequestDto
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/admin/goods/fooditem/getFooditemIconDropDownList")
    @ApiOperation(value = "식단품목아이콘 목록 드롭다운 리스트")
    @ResponseBody
    public ApiResult<?> getFooditemIconDropDownList (FooditemIconListRequestDto fooditemIconListRequestDto) throws Exception {
        return ApiResult.success(fooditemIconListBiz.getFooditemIconDropDownList(fooditemIconListRequestDto));
    }
}