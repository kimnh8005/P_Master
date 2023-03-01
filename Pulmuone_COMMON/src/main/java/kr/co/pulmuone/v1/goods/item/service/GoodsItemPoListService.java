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

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemPoListMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoListRequestVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoListResultVo;
import lombok.RequiredArgsConstructor;

/**
 * dto, vo import 하기
 * <PRE>
 * Forbiz Korea
 * 발주리스트
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2021.02.05  이성준
 * =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class GoodsItemPoListService {

	@Autowired
    private final GoodsItemPoListMapper goodsItemPoListMapper;

	/**
	 * @Desc 발주목록 조회
	 * @param ItemPoListRequestDto
	 * @return List<ItemPoListResultVo>
	 */
	protected Page<ItemPoListResultVo> getPoList(ItemPoListRequestDto dto){

		ArrayList<String> ilItemCdArray = null;

		if (!StringUtil.isEmpty(dto.getSearchItemCd())) {
            // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String ilItemCodeListStr = dto.getSearchItemCd().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
            ilItemCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
        }

		// 품목코드 or 품목바코드로 조회시에는 다른 검색 조건 무시
		if (ilItemCdArray != null && !ilItemCdArray.isEmpty()) {
			ItemPoListRequestDto itemPoListRequestDto = new ItemPoListRequestDto();

			itemPoListRequestDto.setSearchBaseDt(dto.getSearchBaseDt());
			itemPoListRequestDto.setSearchUrSupplierId(dto.getSearchUrSupplierId());
			itemPoListRequestDto.setSearchUrWarehouseId(dto.getSearchUrWarehouseId());
			itemPoListRequestDto.setSearchPoIfYn(dto.getSearchPoIfYn());
			itemPoListRequestDto.setSearchItemCd(dto.getSearchItemCd());
			itemPoListRequestDto.setSearchItemCdArray(ilItemCdArray);
			itemPoListRequestDto.setPage(dto.getPage());
			itemPoListRequestDto.setPageSize(dto.getPageSize());

			PageMethod.startPage(itemPoListRequestDto.getPage(), itemPoListRequestDto.getPageSize());

			// 발주완료 목록 조회시는 기초데이터 생성하지 않는다.
			if (!("Y".equals(dto.getPoResultYn()))) {
				//발주 기초데이타 생성
				addItemPoBasicData(itemPoListRequestDto);
				//발주 기초데이타 수정
				putItemPoBasicData(itemPoListRequestDto);
			}

			return goodsItemPoListMapper.getPoList(itemPoListRequestDto);

		} else {
			dto.setSearchPoTypeList(getSearchKeyToSearchKeyList(dto.getSearchPoType(), Constants.ARRAY_SEPARATORS));//발주유형

			if(!dto.getSearchPoTypeList().isEmpty()) {
			   int size = dto.getSearchPoTypeList().size();

			   for(int i=0; i < size; i++) {
				   String searchPoType = dto.getSearchPoTypeList().get(i);
				   if(searchPoType.equals("N")) {
					  dto.setPoPerItemYn("Y");//품목별 상이가 존재하면 Y
					  dto.getSearchPoTypeList().remove(i);//품목별 상이 제거후 리셋
					  dto.setSearchPoTypeList(dto.getSearchPoTypeList());
				   }
			   }
			}

			dto.setSearchErpCtgryList(getSearchKeyToSearchKeyList(dto.getSearchErpCtgry(), Constants.ARRAY_SEPARATORS));//ERP 카테고리(대분류)
	        dto.setSearchStorageMethodList(getSearchKeyToSearchKeyList(dto.getSearchStorageMethod(), Constants.ARRAY_SEPARATORS));  //보관방법
	        dto.setSearchSaleStatusList(getSearchKeyToSearchKeyList(dto.getSearchSaleStatus(), Constants.ARRAY_SEPARATORS));  //판매상태
	     	dto.setSearchGoodsDisplayStatusList(getSearchKeyToSearchKeyList(dto.getSearchGoodsDisplayStatus(), Constants.ARRAY_SEPARATORS));//상품 전시상태
			dto.setGoodsOutMallSaleStatusList(getSearchKeyToSearchKeyList(dto.getGoodsOutMallSaleStatus(), Constants.ARRAY_SEPARATORS)); // 외부몰 판매상태
		}

		PageMethod.startPage(dto.getPage(), dto.getPageSize());

		// 발주완료 목록 조회시는 기초데이터 생성하지 않는다.
		if (!("Y".equals(dto.getPoResultYn()))) {
			//발주 기초데이타 생성
			addItemPoBasicData(dto);
			//발주 기초데이타 수정
			putItemPoBasicData(dto);
		}

		//발주 데이터 조회
		return goodsItemPoListMapper.getPoList(dto);
	}

	/**
     * @Desc 검색키 -> 검색키 리스트 변환
     *       검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
     * @param searchKey
     * @param splitKey
     * @return List<String>
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();

        // ALL 값이 0째 오지 않는경우.  (전체의 경우 무조건 0부터 시작)
        if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") != 0 ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                                       .map(String::trim)
                                       .filter( x -> StringUtils.isNotEmpty(x) )
                                       .collect(Collectors.toList()));
        }

        return searchKeyList;
    }

    /**
   	 * @Desc 발주정보 목록조회
   	 * @param ItemPoListRequestDto
   	 * @return List<ItemPoListResultVo>
   	 */
    protected List<ItemPoListResultVo> getPoTypeInfoList(ItemPoListRequestDto dto){
   	   return goodsItemPoListMapper.getPoTypeInfoList(dto);
    }

    /**
   	 * @Desc 발주정보 목록조회
   	 * @param ItemPoListRequestDto
   	 * @return List<ItemPoListResultVo>
   	 */
    protected ItemPoListResultVo getSavedPoInfo(ItemPoListRequestDto dto){
   	   return goodsItemPoListMapper.getSavedPoInfo(dto);
    }

    /**
     * @Desc 발주용 기본 데이터 생성
     * @param ItemPoListRequestDto
     * @return int
     */
    protected int addItemPoBasicData(ItemPoListRequestDto dto) {
    	return goodsItemPoListMapper.addItemPoBasicData(dto);
    }

    /**
     * @Desc 발주 기본 데이타 수정
     * @param ItemPoListRequestDto
     * @return int
     */
    protected int putItemPoBasicData(ItemPoListRequestDto dto) {
        return goodsItemPoListMapper.putItemPoBasicData(dto);
    }

    /**
	 * @Desc 발주유형 목록조회
	 * @param ItemPoListRequestDto
	 * @return List<ItemPoListResultVo>
	 */
    protected List<ItemPoListResultVo> getPoTpList(ItemPoListRequestDto dto){
    	return goodsItemPoListMapper.getPoTpList(dto);
    }

    /**
   	 * @Desc 발주유형 목록조회(onChange)
   	 * @param ItemPoListRequestDto
   	 * @return List<ItemPoListResultVo>
   	 */
    protected List<ItemPoListResultVo> getOnChangePoTpList(ItemPoListRequestDto dto){
   	    return goodsItemPoListMapper.getOnChangePoTpList(dto);
    }

    /**
	 * @Desc ERP 카테고리 (대분류) 목록조회
	 * @param ItemPoListRequestDto
	 * @return List<ItemPoListResultVo>
	 */
    protected List<ItemPoListResultVo> getErpCtgryList(ItemPoListRequestDto dto){
    	return goodsItemPoListMapper.getErpCtgryList(dto);
    }

    /**
     * @Desc 발주수량 변경
     * @param ItemPoListRequestDto
     * @return int
     */
    protected int putItemPo(ItemPoListRequestVo vo) {
        return goodsItemPoListMapper.putItemPo(vo);
    }

    /**
     * @Desc 발주저장 이력 생성
     * @param ItemPoListRequestDto
     * @return int
     */
    protected int addItemPoSavedLog(ItemPoListRequestVo vo) {
        return goodsItemPoListMapper.addItemPoSavedLog(vo);
    }

}
