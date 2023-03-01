package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDirectLinkCategoryMappingRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDirectLinkCategoryMappingListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface GoodsDirectLinkCategoryMappingMapper {

	/**
     * @Desc 네이버 표준 카테고리 맵핑 조회
     */
    Page<GoodsDirectLinkCategoryMappingListVo> getGoodsDirectLinkCategoryMappingList(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException;

    /**
     * @Desc 네이버 표준 카테고리 맵핑 조회내역 다운로드
     */
    List<GoodsDirectLinkCategoryMappingListVo> getGoodsDirectLinkCategoryMappingListExcel(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException;

    /**
     * @Desc 표준 카테고리 매핑 조회
     */
    List<GoodsDirectLinkCategoryMappingListVo> getIfNaverCategoryList(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException;

    /**
     * @Desc 네이버 표준 카테고리 맵핑 등록
     */
    int addGoodsDirectLinkCategoryMapping(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException;

    /**
     * @Desc 네이버 표준 카테고리 맵핑 수정
     */
    int putGoodsDirectLinkCategoryMapping(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException;

}
