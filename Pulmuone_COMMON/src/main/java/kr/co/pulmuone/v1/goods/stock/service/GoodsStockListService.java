package kr.co.pulmuone.v1.goods.stock.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.goods.stock.GoodsStockListMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockListResultVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoodsStockListService {

	@Autowired
	private final GoodsStockListMapper goodsStockListMapper;


	/**
	 * 품목별 재고리스트 조회
	 * @param	StockListRequestDto
	 * @return	StockListResponseDto
	 * @throws Exception
	 */
	protected Page<StockListResultVo> getStockList(StockListRequestDto dto) {

		ArrayList<String> ilItemCdArray = null;

        // 권한 체크
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        dto.setListAuthSupplierId(listAuthSupplierId);
        dto.setListAuthWarehouseId(listAuthWarehouseId);

		if(!StringUtil.isEmpty(dto.getSelectConditionType())) {
			if(!StringUtil.isEmpty(dto.getItemCodes()) && dto.getSelectConditionType().equals("codeSearch")) {
				// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
				String ilItemCodeListStr = dto.getItemCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
				ilItemCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
			}

			// 품목코드 or 품목바코드로 조회시에는 ERP 연동여부 외 다른 검색 조건 무시
	        if (ilItemCdArray != null && !ilItemCdArray.isEmpty()) {
	        	StockListRequestDto stockListRequestDto = new StockListRequestDto();

	        	stockListRequestDto.setBaseDt(dto.getBaseDt());
	        	stockListRequestDto.setItemCodes(dto.getItemCodes());
	        	stockListRequestDto.setSelectConditionType(dto.getSelectConditionType());
	        	stockListRequestDto.setIlItemCodeArray(ilItemCdArray);
	        	stockListRequestDto.setPage(dto.getPage());
	        	stockListRequestDto.setPageSize(dto.getPageSize());

	        	PageMethod.startPage(stockListRequestDto.getPage(), stockListRequestDto.getPageSize());

	            return goodsStockListMapper.getStockList(stockListRequestDto);
	        }

	        if(dto.getSelectConditionType().equals("condSearch")) {
	           dto.setSaleStatusList(getSearchKeyToSearchKeyList(dto.getSaleStatus(), Constants.ARRAY_SEPARATORS));       // 판매상태
	           dto.setGoodsTypeList(getSearchKeyToSearchKeyList(dto.getGoodsType(), Constants.ARRAY_SEPARATORS));         // 상품유형
			   dto.setIsNoGoodsItem("N");
	           if (dto.getGoodsTypeList() != null && dto.getGoodsTypeList().size() > 0) {
	        	   int i = 0;
	        	   for (String status : dto.getGoodsTypeList()) {
	        		   if ("NO_GOODS".equals(status)) { // 상품 미생성 품목 체크
	        			   dto.setIsNoGoodsItem("Y");
	        			   dto.getGoodsTypeList().remove(i);
	        			   break;
	        		   }
	        		   i++;
	        	   }
	           }
	           dto.setStorageMethodTypeList(getSearchKeyToSearchKeyList(dto.getKeepMethod(), Constants.ARRAY_SEPARATORS));// 보관방법
	        }
		}

           PageMethod.startPage(dto.getPage(), dto.getPageSize());

        return goodsStockListMapper.getStockList(dto);
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

        if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                                       .map(String::trim)
                                       .filter( x -> StringUtils.isNotEmpty(x) )
                                       .collect(Collectors.toList()));
        }

        return searchKeyList;
    }


	/**
	 * 품목별 재고리스트 주문정보 조회
	 * @param	StockListRequestDto
	 * @return	StockListResponseDto
	 * @throws Exception
	 */
	protected StockListResultVo getStockInfo(StockListRequestDto stockListRequestDto) throws Exception {
		return goodsStockListMapper.getStockInfo(stockListRequestDto);
	}

	/**
	 * 품목별 재고상세 목록 조회
	 * @param	StockListRequestDto
	 * @return	StockListResponseDto
	 * @throws Exception
	 */
	protected Page<StockListResultVo> getStockDetailList(StockListRequestDto stockListRequestDto) throws Exception {
        PageMethod.startPage(stockListRequestDto.getPage(), stockListRequestDto.getPageSize());
        return goodsStockListMapper.getStockDetailList(stockListRequestDto);
	}

	/**
	 * @Desc  공통 선주문 설정 팝업 정보 조회
	 * @param StockListRequestDto
	 * @return int
	 */
	protected StockListResultVo getStockPreOrderPopupInfo(StockListRequestDto stockListRequestDto) {
		return goodsStockListMapper.getStockPreOrderPopupInfo(stockListRequestDto);
	}

	/**
	 * @Desc  품목별 재고리스트 선주문 여부 수정
	 * @param StockListRequestDto
	 * @return int
	 */
	protected int putStockPreOrder(StockListRequestDto stockListRequestDto) {
		return goodsStockListMapper.putStockPreOrder(stockListRequestDto);
	}

	/**
	 * 출고기준 관리 조회
	 * @param	StockListRequestDto
	 * @return	StockListResponseDto
	 * @throws Exception
	 */
	protected List<StockListResultVo> getStockDeadlineDropDownList(StockListRequestDto dto) {
        return goodsStockListMapper.getStockDeadlineDropDownList(dto);
	}

	/**
	 * @Desc  출고기준관리 수정
	 * @param StockListRequestDto
	 * @return int
	 */
	protected int putStockDeadlineInfo(StockListRequestDto stockListRequestDto) {
		return goodsStockListMapper.putStockDeadlineInfo(stockListRequestDto);
	}

	/**
	 * @Desc  출고기준관리 기본설정 수정
	 * @param StockListRequestDto
	 * @return int
	 */
	protected int putStockDeadlineInfoBasicYn(StockListRequestDto stockListRequestDto) {
		return goodsStockListMapper.putStockDeadlineInfoBasicYn(stockListRequestDto);
	}

	/**
	 * 묶음 상품 품목별 재고리스트 주문정보 조회
	 * @param stockListRequestDto
	 * @return
	 * @throws Exception
	 */
	protected StockListResultVo getPackageGoodsStockInfo(StockListRequestDto stockListRequestDto) throws Exception {
		return goodsStockListMapper.getPackageGoodsStockInfo(stockListRequestDto);
	}


}
