package kr.co.pulmuone.v1.goods.item.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsOrgaDisMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.OrgaDiscountRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.OrgaDiscountListVo;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
* Forbiz Korea
* 올가 할인 연동 Service
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 11. 13.                정형진         최초작성
* =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class GoodsOrgaDisService {

	@Autowired
	private GoodsOrgaDisMapper goodsOrgaDisMapper;

	 /**
     * @Desc 올가 할인연동 품목 리스트 조회
     * @param OrgaDiscountRequestDto : 마스터 품목 리스트 검색 조건 request dto
     * @return Page<OrgaDiscountListVo> : 마스터 품목 리스트 검색 결과 목록 ( 페이지네이션 적용 )
     */
    protected Page<OrgaDiscountListVo> getOrgaDisList(OrgaDiscountRequestDto paramDto) {

        ArrayList<String> itemCdArray = null;

        if (!StringUtil.isEmpty(paramDto.getItemCodes())) {

            // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String ilItemCodeListStr = paramDto.getItemCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
            itemCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
            paramDto.setItemCodesArray(itemCdArray);
        }



        PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());

        return goodsOrgaDisMapper.getOrgaDisList(paramDto);

    }

    /**
     * @Desc 올가 할인연동 리스트 엑셀 다운로드 목록 조회
     * @param MasterItemListRequestDto : 마스터 품목 리스트 검색 조건 request dto
     * @return List<OrgaDiscountListVo> : 마스터 품목 리스트 엑셀 다운로드 목록
     */
    public List<OrgaDiscountListVo> getOrgaDisListExcel(OrgaDiscountRequestDto paramDto) {

        // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
    	 ArrayList<String> itemCdArray = null;

         if (!StringUtil.isEmpty(paramDto.getItemCodes())) {

             // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
             String ilItemCodeListStr = paramDto.getItemCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
             itemCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
             paramDto.setItemCodesArray(itemCdArray);
         }

         List<OrgaDiscountListVo> itemList = goodsOrgaDisMapper.getOrgaDisListExcel(paramDto);

        return itemList;

    }




}
