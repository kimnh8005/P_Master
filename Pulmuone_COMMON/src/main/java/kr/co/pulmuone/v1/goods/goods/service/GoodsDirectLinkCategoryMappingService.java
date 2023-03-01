package kr.co.pulmuone.v1.goods.goods.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.NaverCategoryEnums.NaverCategoryMessage;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsDirectLinkCategoryMappingMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDirectLinkCategoryMappingRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDirectLinkCategoryMappingResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDirectLinkCategoryMappingListVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* <PRE>
*
* 직연동 표준 카테고리 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 12. 27.              송지윤        최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class GoodsDirectLinkCategoryMappingService {

    private final GoodsDirectLinkCategoryMappingMapper goodsDirectLinkCategoryMappingMapper;

	 /**
     * @Desc 네이버 표준 카테고리 맵핑 조회
     * @param paramDto
     * @return GoodsDirectLinkCategoryMappingResponseDto
     */
    protected GoodsDirectLinkCategoryMappingResponseDto getGoodsDirectLinkCategoryMappingList(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException {

        PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());
        Page<GoodsDirectLinkCategoryMappingListVo> rows = goodsDirectLinkCategoryMappingMapper.getGoodsDirectLinkCategoryMappingList(paramDto);

        GoodsDirectLinkCategoryMappingResponseDto result = new GoodsDirectLinkCategoryMappingResponseDto();
        result.setRows(rows.getResult());
        result.setTotal(rows.getTotal());

        return result;
    }

    /**
     * @Desc 네이버 표준 카테고리 맵핑 조회내역 다운로드
     * @param paramDto
     * @return GoodsDirectLinkCategoryMappingResponseDto
     */
    protected List<GoodsDirectLinkCategoryMappingListVo> getGoodsDirectLinkCategoryMappingListExcel(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException {

        return goodsDirectLinkCategoryMappingMapper.getGoodsDirectLinkCategoryMappingListExcel(paramDto);
    }

    /**
     * @Desc 표준 카테고리 매핑 조회
     * @param paramDto
     * @return GoodsDirectLinkCategoryMappingResponseDto
     */
    protected GoodsDirectLinkCategoryMappingResponseDto getIfNaverCategoryList(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException {

        List<GoodsDirectLinkCategoryMappingListVo> rows = goodsDirectLinkCategoryMappingMapper.getIfNaverCategoryList(paramDto);
        GoodsDirectLinkCategoryMappingResponseDto result = new GoodsDirectLinkCategoryMappingResponseDto();
        result.setRows(rows);

        return result;
    }

    /**
     * @Desc 네이버 표준 카테고리 맵핑 등록
     * @param paramDto
     * @return Page<GoodsDirectLinkCategoryMappingListVo>
     */
    protected GoodsDirectLinkCategoryMappingResponseDto addGoodsDirectLinkCategoryMapping(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException {

        GoodsDirectLinkCategoryMappingResponseDto result = new GoodsDirectLinkCategoryMappingResponseDto();
        // # 사용자 정보를 받자.
        UserVo userVo = null;
        try {
            userVo = SessionUtil.getBosUserVO();
        } catch (Exception e){ }

        String createId = "0";
        if (userVo != null) {
            createId = userVo.getUserId();
        } else {
            BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
            if (buyerVo != null) {
                createId = StringUtil.nvl(buyerVo.getUrUserId(), "0");
            }
        }

        paramDto.setCreateId(createId);

        int resultInt = goodsDirectLinkCategoryMappingMapper.addGoodsDirectLinkCategoryMapping(paramDto);

        if (resultInt <= 0) System.out.println("# 방송상품 저장 실패하였습니다.");
        result.setResultCode(resultInt <= 0?NaverCategoryMessage.NAVER_CATEGORY_ADD_FAIL_INPUT_TARGET.getCode():NaverCategoryMessage.NAVER_CATEGORY_ADD_SUCCESS_INPUT_TARGET.getCode());
        result.setResultMessage(resultInt <= 0?NaverCategoryMessage.NAVER_CATEGORY_ADD_FAIL_INPUT_TARGET.getMessage():NaverCategoryMessage.NAVER_CATEGORY_ADD_SUCCESS_INPUT_TARGET.getMessage());
        if (resultInt <= 0) throw new BaseException(NaverCategoryMessage.NAVER_CATEGORY_ADD_FAIL_INPUT_TARGET);
        return result;
    }

    /**
     * @Desc 네이버 표준 카테고리 맵핑 수정
     * @param paramDto
     * @return Page<GoodsDirectLinkCategoryMappingListVo>
     */
    protected GoodsDirectLinkCategoryMappingResponseDto putGoodsDirectLinkCategoryMapping(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException {

        GoodsDirectLinkCategoryMappingResponseDto result = new GoodsDirectLinkCategoryMappingResponseDto();
        // # 사용자 정보를 받자.
        UserVo userVo = null;
        try {
            userVo = SessionUtil.getBosUserVO();
        } catch (Exception e){ }

        String modifyId = "0";
        if (userVo != null) {
            modifyId = userVo.getUserId();
        } else {
            BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
            if (buyerVo != null) {
                modifyId = StringUtil.nvl(buyerVo.getUrUserId(), "0");
            }
        }

        paramDto.setModifyId(modifyId);

        int resultInt = goodsDirectLinkCategoryMappingMapper.putGoodsDirectLinkCategoryMapping(paramDto);

        if (resultInt <= 0) System.out.println("# 방송상품 저장 실패하였습니다.");
        result.setResultCode(resultInt <= 0?NaverCategoryMessage.NAVER_CATEGORY_PUT_FAIL_INPUT_TARGET.getCode():NaverCategoryMessage.NAVER_CATEGORY_PUT_SUCCESS_INPUT_TARGET.getCode());
        result.setResultMessage(resultInt <= 0?NaverCategoryMessage.NAVER_CATEGORY_PUT_FAIL_INPUT_TARGET.getMessage():NaverCategoryMessage.NAVER_CATEGORY_PUT_SUCCESS_INPUT_TARGET.getMessage());
        if (resultInt <= 0) throw new BaseException(NaverCategoryMessage.NAVER_CATEGORY_PUT_FAIL_INPUT_TARGET);
        return result;
    }




}
