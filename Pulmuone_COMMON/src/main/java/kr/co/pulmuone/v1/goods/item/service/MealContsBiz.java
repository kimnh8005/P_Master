package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import kr.co.pulmuone.v1.goods.item.dto.MealContsDto;
import kr.co.pulmuone.v1.goods.item.dto.MealContsListRequestDto;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface MealContsBiz {

    ApiResult<?> getMealContsList(MealContsListRequestDto mealContsListRequestDto);

    ExcelDownloadDto getExportExcelMealContsList(MealContsListRequestDto mealContsListRequestDto) throws Exception;

    ApiResult<?> mealContsExcelUpload(MultipartFile file, String createId) throws Exception;

    ApiResult<?> addMealConts(MealContsDto mealContsDto);

    ApiResult<?> getMealConts(String ilGoodsDailyMealContsCd);

    ApiResult<?> putMealConts(MealContsDto mealContsDto);

    ApiResult<?> delMealConts(String ilGoodsDailyMealContsCd);

    List<FooditemIconVo> getFooditemIconList(String ilGoodsDailyMealContsCd);
}
