package kr.co.pulmuone.v1.comm.mapper.goods.item;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoTypeVo;

@Mapper
public interface GoodsItemPoTypeMapper {
	/**
	 * @Desc 발주 유형관리 목록 조회
	 * @param ItemPoTypeListRequestDto
	 * @return List<ItemPoTypeVo>
	 */
	Page<ItemPoTypeVo> getItemPoTypeList(ItemPoTypeListRequestDto itemPoTypeListRequestDto);

	/**
	 * @Desc  발주 유형관리 상세
	 * @param ilPoTpId 발주유형 PK
	 * @return ItemPoTypeVo
	 */
	ItemPoTypeVo getItemPoType(String ilPoTpId);

	/**
	 * @Desc 이벤트 발주일 조회
	 * @param ItemPoTypeVo
	 * @return int
	 */
	HashMap getItemPoDayForEvent(@Param("ilPoTpId") String ilPoTpId, @Param("eventStartDt") String eventStartDt);

	/**
	 * @Desc 발주 유형 중복 체크
	 * @param ItemPoTypeVo
	 * @return int
	 */
	int duplicateItemPoTypeCount(ItemPoTypeVo itemPoTypeVo);

	/**
	 * @Desc  발주 유형관리 추가
	 * @param itemPoTypeVo
	 * @return int
	 */
	int addItemPoType(ItemPoTypeRequestDto itemPoTypeVo);

	/**
	 * @Desc  발주 유형관리 수정
	 * @param itemPoTypeVo
	 * @return int
	 */
	int putItemPoType(ItemPoTypeRequestDto itemPoTypeVo);

	/**
	 * @Desc  발주 유형관리 삭제
	 * @param itemPoTypeVo
	 * @return int
	 */
	int delItemPoType(String ilPoTpId);
}
