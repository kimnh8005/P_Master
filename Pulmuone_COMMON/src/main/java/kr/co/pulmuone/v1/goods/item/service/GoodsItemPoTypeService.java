package kr.co.pulmuone.v1.goods.item.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemPoTypeMapper;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoTypeVo;
import lombok.RequiredArgsConstructor;

/**
 * dto, vo import 하기
 * <PRE>
 * Forbiz Korea
 * 발주 유형관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.07.28  박영후          최초작성
 *  1.0    2020.10.27  이성준          리팩토링
 * =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class GoodsItemPoTypeService {

	@Autowired
    private final GoodsItemPoTypeMapper goodsItemPoTypeMapper;

	/**
	 * @Desc 발주 유형관리 목록 조회
	 * @param ItemPoTypeListRequestDto
	 * @return List<ItemPoTypeVo>
	 */
	protected Page<ItemPoTypeVo> getItemPoTypeList(ItemPoTypeListRequestDto itemPoTypeListRequestDto){
		PageMethod.startPage(itemPoTypeListRequestDto.getPage(), itemPoTypeListRequestDto.getPageSize());
		return goodsItemPoTypeMapper.getItemPoTypeList(itemPoTypeListRequestDto);
	}

	/**
	 * @Desc  발주 유형관리 상세
	 * @param ilPoTpId 발주유형 PK
	 * @return ItemPoTypeVo
	 */
    @UserMaskingRun
	protected ItemPoTypeVo getItemPoType(String ilPoTpId) {
		return goodsItemPoTypeMapper.getItemPoType(ilPoTpId);
	}

	/**
	 * @Desc  이벤트 발주일 조회
	 * @param ilPoTpId 발주유형 PK
	 * @return ItemPoTypeVo
	 */
	protected HashMap getItemPoDayForEvent(String ilPoTpId, String eventStartDt) {
		return goodsItemPoTypeMapper.getItemPoDayForEvent(ilPoTpId, eventStartDt);
	}

	/**
	 * @Desc  발주 유형관리 중복 체크
	 * @param ilPoTpId 발주유형 PK
	 * @return ItemPoTypeVo
	 */
	protected int duplicateItemPoTypeCount(ItemPoTypeVo itemPoTypeVo) {
		return goodsItemPoTypeMapper.duplicateItemPoTypeCount(itemPoTypeVo);
	}

	/**
	 * @Desc  발주 유형관리 추가
	 * @param itemPoTypeVo
	 * @return ItemPoTypeVo
	 */
	protected int addItemPoType(ItemPoTypeRequestDto itemPoTypeVo) {
		return goodsItemPoTypeMapper.addItemPoType(itemPoTypeVo);
	}

	/**
	 * @Desc  발주 유형관리 수정
	 * @param itemPoTypeVo
	 * @return ItemPoTypeVo
	 */
	protected int putItemPoType(ItemPoTypeRequestDto itemPoTypeVo) {
		return goodsItemPoTypeMapper.putItemPoType(itemPoTypeVo);
	}

	/**
	 * @Desc  발주 유형관리 삭제
	 * @param ilPoTpId
	 * @return int
	 */
	protected int delItemPoType(String ilPoTpId) {
		return goodsItemPoTypeMapper.delItemPoType(ilPoTpId);
	}

}
