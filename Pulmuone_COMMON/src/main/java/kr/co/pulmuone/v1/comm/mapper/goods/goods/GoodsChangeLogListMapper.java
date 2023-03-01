package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsChangeLogListVo;

@Mapper
public interface GoodsChangeLogListMapper {

	/**
	 * @Desc 상품 업데이트 내역 조회
	 * @param goodsRequestDto
	 * @return Page<GoodsChangeLogListVo>
	 */
	Page<GoodsChangeLogListVo> getGoodsChangeLogList(GoodsChangeLogListRequestDto goodsChangeLogListRequestDto);

	/**
	 * @Desc 상품 업데이트 내역 엑셀 다운로드
	 * @param goodsRequestDto
	 * @return Page<GoodsChangeLogListVo>
	 */
	List<GoodsChangeLogListVo> getGoodsChangeLogListExcel(GoodsChangeLogListRequestDto goodsChangeLogListRequestDto);

	/**
	 * @Desc 업데이트 상세 내역
	 * @param goodsChangeLogListRequestDto
	 * @return List<GoodsChangeLogListVo>
	 */
	List<GoodsChangeLogListVo> getGoodsChangeLogPopup(GoodsChangeLogListRequestDto goodsChangeLogListRequestDto);
}
