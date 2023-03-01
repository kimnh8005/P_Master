package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPackageListVo;

@Mapper
public interface GoodsPackageListMapper {

	/**
     * @Desc 묶음 상품 목록 조회
     * @param goodsRequestDto
     * @return Page<GoodsVo>
     */
    Page<GoodsPackageListVo> getGoodsPackageList(GoodsPackageListRequestDto paramDto);

    /**
     * @Desc 묶음 상품 상세 목록 조회
     * @param goodsRequestDto
     * @return Page<GoodsVo>
     */
    List<GoodsPackageListVo> getGoodsPackageDetailList(GoodsPackageListRequestDto paramDto);

    /**
     * @Desc 묶음 상품 목록 조회 - 엑셀 기본양식
     * @param goodsRequestDto
     * @return Page<GoodsVo>
     */
    List<GoodsPackageListVo> getGoodsPackageListExcel(GoodsPackageListRequestDto paramDto);

    /**
     * @Desc 묶음 상품 목록 조회 - 구성상품 정보양식
     * @param goodsRequestDto
     * @return Page<GoodsVo>
     */
    List<GoodsPackageListVo> getGoodsPackageDetailListExcel(GoodsPackageListRequestDto paramDto);




}
