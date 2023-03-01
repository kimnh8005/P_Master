package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsFieldRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDetailImageListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistApprVo;
import kr.co.pulmuone.v1.goods.item.dto.ItemModifyRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemNutritionDetailVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecValueVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;

import java.util.List;

public interface GoodsDetailImageBiz {

    ApiResult<?> getGoodsDetailImageList(GoodsDetailImageListRequestDto goodsDetailImageListRequestDto);

    ExcelDownloadDto getGoodsDetailImageListExportExcel(GoodsDetailImageListRequestDto goodsDetailImageListRequestDto);

    ApiResult<?> putUpdateGoodsIdInfoForDetailImage(SpecificsFieldRequestDto specificsFieldRequestDto) throws Exception;

    ApiResult<?> addUpdateGoodsIdInfoForDetailImage(List<String> goodsIdList) throws Exception;

    ApiResult<?> getUpdateItemInfoForDetailImage(ItemModifyRequestDto itemModifyRequestDto, MasterItemVo beforeItemDetail, MasterItemVo afterMasterItemVo, List<ItemNutritionDetailVo> beforeItemNurList, List<ItemNutritionDetailVo> afterItemNurList , List<ItemSpecValueVo> beforeItemSpecList, List<ItemSpecValueVo> afterItemSpecList) throws Exception;

    ApiResult<?> getUpdateGoodsInfoForDetailImage(GoodsRegistResponseDto beforeGoodsDatas, GoodsRegistRequestDto goodsRegistRequestDto, GoodsRegistApprVo goodsRegistApprVo) throws Exception;
}
