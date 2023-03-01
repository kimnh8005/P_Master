package kr.co.pulmuone.v1.comm.mapper.goods.item;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.item.dto.ItemPoListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoListRequestVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoListResultVo;

@Mapper
public interface GoodsItemPoListMapper {

    /**
     * @Desc 발주용 기본 데이터 생성
     * @param ItemPoListRequestDto
     * @return int
     */
    int addItemPoBasicData(ItemPoListRequestDto paramDto);
    int putItemPoBasicData(ItemPoListRequestDto paramDto);
    int putItemPo(ItemPoListRequestVo vo);
    int putItemPoIFResult(ItemPoListRequestDto paramDto);
    int addItemPoSavedLog(ItemPoListRequestVo vo);

    /**
	 * @Desc 발주리스트 조회
	 * @param ItemPoListRequestDto
	 * @return List<ItemPoListResultVo>
	 */
	Page<ItemPoListResultVo> getPoList(ItemPoListRequestDto paramDto);

	/**
	 * @Desc 발주유형 목록조회
	 * @param ItemPoListRequestDto
	 * @return List<ItemPoListResultVo>
	 */
	List<ItemPoListResultVo> getPoTpList(ItemPoListRequestDto paramDto);

	/**
	 * @Desc 발주유형 목록조회(onChange)
	 * @param ItemPoListRequestDto
	 * @return List<ItemPoListResultVo>
	 */
	List<ItemPoListResultVo> getOnChangePoTpList(ItemPoListRequestDto paramDto);


	/**
	 * @Desc ERP 카테고리 (대분류) 목록조회
	 * @param ItemPoListRequestDto
	 * @return List<ItemPoListResultVo>
	 */
	List<ItemPoListResultVo> getErpCtgryList(ItemPoListRequestDto paramDto);

	/**
	 * @Desc 발주정보 목록조회
	 * @param ItemPoListRequestDto
	 * @return List<ItemPoListResultVo>
	 */
	List<ItemPoListResultVo> getPoTypeInfoList(ItemPoListRequestDto paramDto);

	/**
	 * @Desc 발주정보 목록조회
	 * @param ItemPoListRequestDto
	 * @return ItemPoListResultVo
	 */
	ItemPoListResultVo getSavedPoInfo(ItemPoListRequestDto paramDto);

}
