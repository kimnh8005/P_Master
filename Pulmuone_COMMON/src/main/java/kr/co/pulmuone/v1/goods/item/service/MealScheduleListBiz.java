package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.item.dto.MealInfoExcelRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MealPatternRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MealScheduleRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MealScheduleListBiz {

    ApiResult<?> getMealPatternList(MealPatternRequestDto mealPatternRequestDto);

    ApiResult<?> delMealPattern(MealPatternRequestDto mealPatternRequestDto) throws Exception;

    ApiResult<?> getMealPatternGoodsList(String patternCd);

    ApiResult<?> getMealPatternDetailList(String patternCd);

    ApiResult<?> getMealPatternInfo(String patternCd);

    ApiResult<?> putMealPatternInfo(MealPatternRequestDto mealPatternRequestDto) throws Exception;

    ApiResult<?> checkMealPatternGoods(String mallDiv,long ilGoodsId) throws Exception;

    ExcelDownloadDto getMealPatternExportExcel(MealInfoExcelRequestDto mealInfoExcelRequestDto) throws Exception;

    ExcelDownloadDto getMealScheduleExportExcel(MealInfoExcelRequestDto mealInfoExcelRequestDto) throws Exception;

    ApiResult<?> addMealPatternDetail(MealPatternRequestDto mealPatternRequestDto) throws Exception;

    ApiResult<?> addMealPatternInfo(MealPatternRequestDto mealPatternRequestDto) throws Exception;

    ApiResult<?> getMealScheduleDetailList(MealScheduleRequestDto mealScheduleRequestDto);

    ApiResult<?> putMealPatternDetail(MealPatternRequestDto mealPatternRequestDto) throws Exception;

    ApiResult<?> putMealPatternDetailRow(MealPatternRequestDto mealPatternRequestDto) throws Exception;

    ApiResult<?> putMealSchRow(MealScheduleRequestDto mealScheduleRequestDto) throws Exception;

    ApiResult<?> getMealPatternUploadData(String mealContsCd) throws Exception;

    ApiResult<?> getMealPatternUploadDataList(List<String> mealContsCdArray) throws Exception;
}
