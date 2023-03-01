package kr.co.pulmuone.v1.goods.etc.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.etc.dto.GoodsNutritionRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.GoodsNutritionVo;

public interface GoodsNutritionBiz {

	ApiResult<?> getGoodsNutritionList(GoodsNutritionRequestDto goodsNutritionRequestDto);

	ApiResult<?> getGoodsNutrition(String ilNutritionCode);

	ApiResult<?> putGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) throws Exception;

	ApiResult<?> addGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) throws Exception;

	ApiResult<?> delGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) throws Exception;

	List<GoodsNutritionVo> getGoodsNutritionExcelList() throws Exception;

	ExcelDownloadDto getGoodsNutritionExportExcel(GoodsNutritionRequestDto dto);

}
