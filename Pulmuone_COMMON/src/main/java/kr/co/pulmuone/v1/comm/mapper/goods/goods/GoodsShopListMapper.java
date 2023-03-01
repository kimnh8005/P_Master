package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsShopListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsShopListVo;

@Mapper
public interface GoodsShopListMapper {

	/**
     * @Desc 매장 상품 목록 조회
     * @param goodsRequestDto
     * @return Page<GoodsVo>
     */
    Page<GoodsShopListVo> getGoodsShopList(GoodsShopListRequestDto paramDto);

    /**
     * @Desc 매장 상품 목록 조회 - 엑셀 다운로드
     * @param goodsRequestDto
     * @return Page<GoodsVo>
     */
    List<GoodsShopListVo> getGoodsShopListExcel(GoodsShopListRequestDto paramDto);

}
