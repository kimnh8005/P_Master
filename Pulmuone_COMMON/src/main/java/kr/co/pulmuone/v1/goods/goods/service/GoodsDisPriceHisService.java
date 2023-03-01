package kr.co.pulmuone.v1.goods.goods.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsDisPriceHisMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDisPriceHisRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDisPriceHisVo;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 상품 할인 업데이트 리스트 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 01.             정형진          최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class GoodsDisPriceHisService {

	@Autowired
    private final GoodsDisPriceHisMapper goodsDisPriceHisMapper;

	/**
     * @Desc 상품할인 업데이트 목록 조회
     * @param goodsListRequestDto
     * @return Page<GoodsVo>
     */
    protected Page<GoodsDisPriceHisVo> getGoodsDisPriceHisList(GoodsDisPriceHisRequestDto paramDto) {
    	// 권한 설정
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        paramDto.setListAuthSupplierId(listAuthSupplierId);
        paramDto.setListAuthWarehouseId(listAuthWarehouseId);

		paramDto.setDiscountTpList(this.getSearchKeyToSearchKeyList(paramDto.getDiscountTp(), Constants.ARRAY_SEPARATORS)); // 판매상태

        PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());
        return goodsDisPriceHisMapper.getGoodsDisPriceHisList(paramDto);
    }

    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();

        if (StringUtils.isNotEmpty(searchKey)) {
            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .collect(Collectors.toList()));

            for (String key : searchKeyList) {
            	if ("ALL".equals(key)) {
            		return new ArrayList<String>();
            	}
            }
        }

        return searchKeyList;
    }

}
