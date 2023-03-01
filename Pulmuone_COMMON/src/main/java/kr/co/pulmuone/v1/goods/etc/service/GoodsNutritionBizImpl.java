package kr.co.pulmuone.v1.goods.etc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.etc.dto.GoodsNutritionListResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.GoodsNutritionRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.GoodsNutritionResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.GoodsNutritionVo;

/**
 * <PRE>
 * Forbiz Korea
 * 상품영양정보 ServiceImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.07.07  박영후           최초작성
 *  1.0    2020.10.26  이성준           리팩토링
 * =======================================================================
 * </PRE>
 */
@Service
public class GoodsNutritionBizImpl implements GoodsNutritionBiz{

	@Autowired
	GoodsNutritionService goodsNutritionService;

	/**
	 * 상품영양정보 목록 조회
	 * @return GoodsItemNutritionListResponseDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getGoodsNutritionList(GoodsNutritionRequestDto goodsNutritionRequestDto) {
		GoodsNutritionListResponseDto result = new GoodsNutritionListResponseDto();

		Page<GoodsNutritionVo> rows = goodsNutritionService.getGoodsNutritionList(goodsNutritionRequestDto);// rows

		result.setTotal(rows.getTotal());
		result.setRows(rows.getResult());

		return ApiResult.success(result);
	}

	/**
	 * 상품영양정보 상세 조회
	 * @param ilNutritionCode 상품 영양정보 분류(ERP 분류정보) 코드 PK
	 * @return GoodsNutritionResponseDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getGoodsNutrition(String ilNutritionCode) {
		GoodsNutritionResponseDto result = new GoodsNutritionResponseDto();

		GoodsNutritionVo vo = goodsNutritionService.getGoodsNutrition(ilNutritionCode);

		result.setRows(vo);

		return ApiResult.success(result);
	}


	/**
	 * 상품영양정보 수정
	 * @return GoodsItemNutritionResponseDto
	 * @return ApiResult
     * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) throws Exception {
        //중복체크
        if(goodsNutritionService.duplicateGoodsNutritionByNameCount(goodsNutritionRequestDto) > 0) {
        	return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
        } else {
        	goodsNutritionService.putGoodsNutrition(goodsNutritionRequestDto);
        }

        return ApiResult.success();
	}


	/**
	 * 상품영양정보 추가
	 * @param GoodsNutritionRequestDto
	 * @return GoodsItemNutritionResponseDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) throws Exception {
        //중복체크
        if(goodsNutritionService.duplicateGoodsNutritionByNameCount(goodsNutritionRequestDto) > 0) {
        	return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
        } else {
        	goodsNutritionService.addGoodsNutrition(goodsNutritionRequestDto);
        }

		return ApiResult.success();
	}

	/**
	 * 상품영양정보 삭제
	 * @param itemNutritionRequestDto
	 * @return ItemNutritionResponseDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> delGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) throws Exception {

		goodsNutritionService.delGoodsNutrition(goodsNutritionRequestDto);

		return ApiResult.success();
	}

	/**
	 * 상품영양정보 엑셀용 데이타 조회
	 * @return List<ItemNutritionVo>
	 * @throws Exception
	 */

	  @Override public List<GoodsNutritionVo> getGoodsNutritionExcelList() throws Exception {

	      return goodsNutritionService.getGoodsNutritionExcelList(); // rows

	  }

	/**
	 * 상품영양정보 엑셀다운로드
	 * @param GetPolicyOriginListRequestDto
	 * @return ExcelDownloadDto
	 */
	@Override
    public ExcelDownloadDto getGoodsNutritionExportExcel(GoodsNutritionRequestDto dto) {
        return goodsNutritionService.getGoodsNutritionExportExcel(dto);
    }

}
