package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.goods.dto.GoodsAllModifyRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistCategoryRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsAllModifyVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistAdditionalGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistVo;
import kr.co.pulmuone.v1.goods.item.dto.ItemNutritionDetailDto;

@Mapper
public interface GoodsAllModifyMapper {

	List<GoodsAllModifyVo> getGoodsAllModifyList(GoodsAllModifyRequestDto paramDto);

	int putPromotionInfoModify(GoodsAllModifyRequestDto paramDto);

	String selectBasicDispCategory(@Param("ilGoodsId") String ilGoodsId);

	int putResetBasicYnOfDispCategory(@Param("ilGoodsId") String ilGoodsId);

	int putCategoryInfoModify(GoodsRegistCategoryRequestDto paramDto);

	int putPurchasModify(GoodsAllModifyRequestDto paramDto);

	int putNoticeGoodsModify(GoodsAllModifyRequestDto paramDto);

	int putGoodsAddModify(GoodsAllModifyRequestDto paramDto);

	int delGoodsAddModify(GoodsAllModifyRequestDto paramDto);

	int getGoodsAdditionCount(GoodsAllModifyRequestDto paramDto);

	int createGoodsAddModify(GoodsAllModifyRequestDto paramDto);

	//추가상품
	List<GoodsRegistAdditionalGoodsVo> goodsAdditionalGoodsMappingList(GoodsAllModifyRequestDto paramDto);

	//상품 마스터 정보
	GoodsRegistVo getGoodsNoticeInfo(GoodsAllModifyRequestDto paramDto);

	List<GoodsAllModifyVo> getGoodsAllModifyListExcel(GoodsAllModifyRequestDto paramDto);

	GoodsRegistVo getGoodsInfoByGoodsId(@Param("ilGoodsId") String ilGoodsId);

}
