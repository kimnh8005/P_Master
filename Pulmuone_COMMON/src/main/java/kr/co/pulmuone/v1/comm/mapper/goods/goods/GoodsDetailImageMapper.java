package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import kr.co.pulmuone.v1.goods.etc.dto.SpecificsFieldRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.GoodsDetailImageVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDetailImageDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDetailImageListRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsDetailImageMapper {

    List<GoodsDetailImageDto> getGoodsDetailImageList(GoodsDetailImageListRequestDto goodsDetailImageListRequestDto);

    Long getGoodsDetailImageListCount(GoodsDetailImageListRequestDto goodsDetailImageListRequestDto);

    List<GoodsDetailImageVo> getSpecGoodsIdList(SpecificsFieldRequestDto specificsFieldRequestDto);

    List<GoodsDetailImageVo> getDetailGoodsIdList(SpecificsFieldRequestDto specificsFieldRequestDto);

    List<GoodsDetailImageVo> getUpdateItemInfo(String itemCode);

    List<GoodsDetailImageVo> getUpdateGoodsInfoForDetailImage(String ilGoodsId);

    boolean getImageBatchYn(GoodsDetailImageVo goodsDetailImageVo);

    int putUpdateGoodsIdInfoForDetailImage(GoodsDetailImageVo goodsDetailImageVo);
}
