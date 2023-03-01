package kr.co.pulmuone.v1.goods.item.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.goods.item.ItemChangeLogListMapper;
import kr.co.pulmuone.v1.goods.item.dto.ItemChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemChangeLogListVo;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* 마스터 품목리스트 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자	  :  작성내역
* -----------------------------------------------------------------------
*  1.0	2021. 04. 21.				임상건		  최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class ItemChangeLogListService {


	@Autowired
	private final ItemChangeLogListMapper itemChangeLogListMapper;

	/**
	 * @Desc 검색키 -> 검색키 리스트 변환
	 *	   검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
	 * @param searchKey
	 * @param splitKey
	 * @return List<String>
	 */
	protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
		List<String> searchKeyList = new ArrayList<String>();

		if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0 ) {

			searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
									.map(String::trim)
									.filter( x -> StringUtils.isNotEmpty(x) )
									.collect(Collectors.toList()));
		}

		return searchKeyList;
	}

	/**
	 * @Desc 마스터 품목 업데이트 내역 목록 조회
	 * @param goodsChangeLogListRequestDto
	 * @return Page<ItemChangeLogListVo>
	 */
	protected Page<ItemChangeLogListVo> getItemChangeLogList(ItemChangeLogListRequestDto itemChangeLogListRequestDto) {
		PageMethod.startPage(itemChangeLogListRequestDto.getPage(), itemChangeLogListRequestDto.getPageSize());
		return itemChangeLogListMapper.getItemChangeLogList(itemChangeLogListRequestDto);
	}

	/**
	 * @Desc 마스터 품목 업데이트 내역 엑셀 다운로드
	 * @param ItemListRequestDto : 마스터 품목 리스트 검색 조건 request dto
	 * @return List<ItemChangeLogListVo> : 마스터 품목 리스트 엑셀 다운로드 목록
	 */
	public List<ItemChangeLogListVo> getItemChangeLogListExcel(ItemChangeLogListRequestDto itemChangeLogListRequestDto) {
		return itemChangeLogListMapper.getItemChangeLogListExcel(itemChangeLogListRequestDto);
	}

	/**
	 * @Desc 업데이트 상세 내역
	 * @param goodsChangeLogListRequestDto
	 * @return Page<ItemChangeLogListVo>
	 */
	protected List<ItemChangeLogListVo> getItemChangeLogPopup(ItemChangeLogListRequestDto itemChangeLogListRequestDto) {
		return itemChangeLogListMapper.getItemChangeLogPopup(itemChangeLogListRequestDto);
	}
}
