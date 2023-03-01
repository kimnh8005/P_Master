package kr.co.pulmuone.v1.comm.mapper.goods.etc;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.etc.dto.GoodsNutritionRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.GoodsNutritionVo;

@Mapper
public interface GoodsNutritionMapper {

	Page<GoodsNutritionVo> getGoodsNutritionList(GoodsNutritionRequestDto goodsNutritionRequestDto);

	List<GoodsNutritionVo> getGoodsNutritionExcelList();

	GoodsNutritionVo getGoodsNutrition(String ilNutritionCode);

	int duplicateGoodsNutritionByNameCount(GoodsNutritionRequestDto goodsNutritionRequestDto);

	int addGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto);

    int putGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto);

    int delGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto);

}
