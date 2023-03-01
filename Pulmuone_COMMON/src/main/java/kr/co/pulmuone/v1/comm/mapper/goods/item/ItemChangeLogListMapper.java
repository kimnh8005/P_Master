package kr.co.pulmuone.v1.comm.mapper.goods.item;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.item.dto.ItemChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemChangeLogListVo;

@Mapper
public interface ItemChangeLogListMapper {

	/**
	 * @Desc 상품 업데이트 내역 조회
	 * @param goodsRequestDto
	 * @return Page<ItemChangeLogListVo>
	 */
	Page<ItemChangeLogListVo> getItemChangeLogList(ItemChangeLogListRequestDto itemChangeLogListRequestDto);

	/**
	 * @Desc 상품 업데이트 내역 엑셀 다운로드
	 * @param goodsRequestDto
	 * @return Page<ItemChangeLogListVo>
	 */
	List<ItemChangeLogListVo> getItemChangeLogListExcel(ItemChangeLogListRequestDto itemChangeLogListRequestDto);

	/**
	 * @Desc 업데이트 상세 내역
	 * @param goodsChangeLogListRequestDto
	 * @return List<ItemChangeLogListVo>
	 */
	List<ItemChangeLogListVo> getItemChangeLogPopup(ItemChangeLogListRequestDto itemChangeLogListRequestDto);
}
