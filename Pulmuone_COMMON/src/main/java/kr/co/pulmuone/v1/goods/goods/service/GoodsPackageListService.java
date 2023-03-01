package kr.co.pulmuone.v1.goods.goods.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsPackageListMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPackageListVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 묶음 상품 리스트 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 11. 20.               정형진          최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class GoodsPackageListService {

	@Autowired
    private final GoodsPackageListMapper goodsPackageListMapper;

    @Autowired
    GoodsListService goodsListService;

    /**
     * @Desc 묶음 상품 목록 조회
     * @param goodsListRequestDto
     * @return Page<GoodsVo>
     */
    protected Page<GoodsPackageListVo> getGoodsPackageList(GoodsPackageListRequestDto paramDto) {
        ArrayList<String> goodsCdArray = null;
        // 권한 설정
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        paramDto.setListAuthSupplierId(listAuthSupplierId);
        paramDto.setListAuthWarehouseId(listAuthWarehouseId);

        if(!StringUtil.isEmpty(paramDto.getSearchType())) {
        	if(!StringUtil.isEmpty(paramDto.getGoodsCodes()) && paramDto.getSearchType().equals("single")) {

           		//화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
                String ilItemCodeListStr = paramDto.getGoodsCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
                goodsCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
                paramDto.setGoodsCodeArray(goodsCdArray);
           	}

        	if(paramDto.getSearchType().equals("multi")) {
           		paramDto.setSaleStatusList(goodsListService.getSearchKeyToSearchKeyList(paramDto.getSaleStatus(), Constants.ARRAY_SEPARATORS)); // 판매상태
           	}

        }

       	PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());

        return goodsPackageListMapper.getGoodsPackageList(paramDto);
    }

	/**
     * @Desc 묶음 상품 상세 목록 조회
     * @param goodsListRequestDto
     * @return Page<GoodsVo>
     */
    protected List<GoodsPackageListVo> getGoodsPackageDetailList(GoodsPackageListRequestDto paramDto) {
        return goodsPackageListMapper.getGoodsPackageDetailList(paramDto);
    }

	/**
     * @Desc 묶음 상품 목록 조회 - 엑셀 기본양식
     * @param goodsListRequestDto
     * @return Page<GoodsVo>
     */
    protected List<GoodsPackageListVo> getGoodsPackageListExcel(GoodsPackageListRequestDto paramDto) {
        // 권한 설정
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        paramDto.setListAuthSupplierId(listAuthSupplierId);
        paramDto.setListAuthWarehouseId(listAuthWarehouseId);

        return goodsPackageListMapper.getGoodsPackageListExcel(paramDto);
    }

    /**
     * @Desc 묶음 상품 목록 조회 - 엑셀 구성상품 정보양식
     * @param goodsListRequestDto
     * @return Page<GoodsVo>
     */
    protected List<GoodsPackageListVo> getGoodsPackageDetailListExcel(GoodsPackageListRequestDto paramDto) {
        // 권한 설정
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        paramDto.setListAuthSupplierId(listAuthSupplierId);
        paramDto.setListAuthWarehouseId(listAuthWarehouseId);

        return goodsPackageListMapper.getGoodsPackageDetailListExcel(paramDto);
    }







}
