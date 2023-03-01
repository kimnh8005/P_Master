package kr.co.pulmuone.v1.goods.fooditem.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconListRequestDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconListResponseDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconRequestDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconResponseDto;

/**
 * <PRE>
 * 식단품목 아이콘 리스트 Biz
 * </PRE>
 */
public interface FooditemIconListBiz {

    /**
     * 식단품목아이콘 목록 조회
     * @param fooditemIconListRequestDto
     * @return
     */
    FooditemIconListResponseDto getFooditemIconList(FooditemIconListRequestDto fooditemIconListRequestDto);

    /**
     * 식단품목아이콘 상세 조회
     * @param ilFooditemIconId
     * @return
     */
    FooditemIconResponseDto getFooditemIcon(long ilFooditemIconId);

    /**
     * 식단품목아이콘 추가
     * @param fooditemIconRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto) throws Exception;

    /**
     * 식단품목아이콘 수정
     * @param fooditemIconRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> putFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto) throws Exception;

    /**
     * 식단품목아이콘 삭제
     * @param fooditemIconRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> delFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto) throws Exception;

    /**
     * 식단품목아이콘 목록 드롭다운 리스트
     * @param fooditemIconListRequestDto
     * @return
     */
    FooditemIconListResponseDto getFooditemIconDropDownList(FooditemIconListRequestDto fooditemIconListRequestDto);
}
