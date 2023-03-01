package kr.co.pulmuone.bos.item.meal;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.MealInfoExcelRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MealPatternListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.MealPatternRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MealScheduleRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternDetailListVo;
import kr.co.pulmuone.v1.goods.item.service.MealScheduleListBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
/**
 * <PRE>
* Forbiz Korea
* 식단 스케쥴 Controller
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021.09.06               최윤지        최초작성
* =======================================================================
 * </PRE>
 */
@RestController
public class MealScheduleListController {

    @Autowired
    private MealScheduleListBiz mealScheduleListBiz;

    @Autowired(required=true)
	private HttpServletRequest request;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    /**
     * 식단 패턴 리스트 조회
     *
     * @param mealPatternRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 리스트 조회")
    @PostMapping(value = "/admin/item/meal/getMealPatternList")
    @ApiResponse(code = 900, message = "response data", response = MealPatternListResponseDto.class)
	public ApiResult<?> getMealPatternList(MealPatternRequestDto mealPatternRequestDto) throws Exception {
		return mealScheduleListBiz.getMealPatternList((MealPatternRequestDto) BindUtil.convertRequestToObject(request, MealPatternRequestDto.class));
	}
    
	/**
     * 식단 패턴 리스트 내 삭제
     * @param mealPatternRequestDto
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/item/meal/delMealPattern")
	@ApiOperation(value = "식단 패턴 삭제", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class")
	})
    public ApiResult<?> delMealPattern(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        return mealScheduleListBiz.delMealPattern(mealPatternRequestDto);
    }

    /**
     * @Desc 식단 패턴 수정/상세조회 - 연결상품 조회
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 수정/상세조회 - 연결상품 조회")
	@PostMapping(value = "/admin/item/meal/getMealPatternGoodsList")
	public ApiResult<?> getMealPatternGoodsList(String patternCd) throws Exception {
		return mealScheduleListBiz.getMealPatternGoodsList(patternCd);
	}
	
	/**
     * @Desc 식단 패턴 수정/상세조회 - 패턴정보 조회
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 수정/상세조회 - 패턴정보 조회")
	@PostMapping(value = "/admin/item/meal/getMealPatternDetailList")
	public ApiResult<?> getMealPatternDetailList(String patternCd) throws Exception {
		return mealScheduleListBiz.getMealPatternDetailList(patternCd);
	}
	
	/**
     * @Desc 식단 패턴 수정/상세조회 - 기본정보 조회
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 수정/상세조회 - 기본정보 조회")
	@PostMapping(value = "/admin/item/meal/getMealPatternInfo")
	public ApiResult<?> getMealPatternInfo(String patternCd) throws Exception {
		return mealScheduleListBiz.getMealPatternInfo(patternCd);
	}
	
	/**
     * @Desc 식단 패턴 수정/상세조회 - 기본정보 수정
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 수정/상세조회 - 기본정보 수정")
	@PostMapping(value = "/admin/item/meal/putMealPatternInfo")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class")
	})
	public ApiResult<?> putMealPatternInfo(@RequestBody MealPatternRequestDto mealPatternRequestDto) throws Exception {
		return mealScheduleListBiz.putMealPatternInfo(mealPatternRequestDto);
	}

	/**
     * @Desc 식단 패턴 수정/상세조회 - 연결상품 추가확인
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 수정/상세조회 - 연결상품 추가확인")
	@PostMapping(value = "/admin/item/meal/checkMealPatternGoods")
	public ApiResult<?> checkMealPatternGoods(String mallDiv, long ilGoodsId) throws Exception {
		return mealScheduleListBiz.checkMealPatternGoods(mallDiv, ilGoodsId);
	}
	
	/**
     * @Desc 식단정보 패턴 엑셀 다운로드
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단정보 패턴 엑셀 다운로드")
	@PostMapping(value = "/admin/il/meal/getMealPatternExportExcel")
	public ModelAndView getMealPatternExportExcel(@RequestBody MealInfoExcelRequestDto mealInfoExcelRequestDto) throws Exception {
		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, mealScheduleListBiz.getMealPatternExportExcel(mealInfoExcelRequestDto));

        return modelAndView;
	}

	/**
     * @Desc 식단정보 스케쥴 엑셀 다운로드
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단정보 스케쥴 엑셀 다운로드")
	@PostMapping(value = "/admin/il/meal/getMealScheduleExportExcel")
	public ModelAndView getMealScheduleExportExcel(@RequestBody MealInfoExcelRequestDto mealInfoExcelRequestDto) throws Exception {
		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, mealScheduleListBiz.getMealScheduleExportExcel(mealInfoExcelRequestDto));

        return modelAndView;
	}

	/**
     * @Desc 식단 패턴 상세저장 (패턴저장 -> 스케쥴생성)
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 상세저장 (패턴저장 -> 스케쥴생성)")
	@PostMapping(value = "/admin/item/meal/addMealPatternDetail")
	public ApiResult<?> addMealPatternDetail(@RequestBody MealPatternRequestDto mealPatternRequestDto) throws Exception {
		return mealScheduleListBiz.addMealPatternDetail(mealPatternRequestDto);
	}

	/**
     * @Desc 식단패턴 / 연결상품 등록
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단패턴 / 연결상품 등록")
	@PostMapping(value = "/admin/item/meal/addMealPatternInfo")
	public ApiResult<?> addMealPatternInfo(@RequestBody MealPatternRequestDto mealPatternRequestDto) throws Exception {
		return mealScheduleListBiz.addMealPatternInfo(mealPatternRequestDto);
	}
	
	/**
     * @Desc 식단 스케쥴 상세조회
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 스케쥴 상세조회")
	@PostMapping(value = "/admin/item/meal/getMealScheduleDetailList")
	public ApiResult<?> getMealScheduleDetailList(MealScheduleRequestDto mealScheduleRequestDto) throws Exception {
		return mealScheduleListBiz.getMealScheduleDetailList((MealScheduleRequestDto) BindUtil.convertRequestToObject(request, MealScheduleRequestDto.class));
	}
	
	/**
     * @Desc 식단 패턴 수정 (패턴저장 -> 스케쥴생성)
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 수정 (패턴저장 -> 스케쥴생성)")
	@PostMapping(value = "/admin/item/meal/putMealPatternDetail")
	public ApiResult<?> putMealPatternDetail(@RequestBody MealPatternRequestDto mealPatternRequestDto) throws Exception {
		return mealScheduleListBiz.putMealPatternDetail(mealPatternRequestDto);
	}

	/**
     * @Desc 식단 패턴 개별 수정 (패턴저장 -> 스케쥴생성)
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 개별 수정 (패턴저장 -> 스케쥴생성)")
	@PostMapping(value = "/admin/item/meal/putMealPatternDetailRow")
	public ApiResult<?> putMealPatternDetailRow(@RequestBody MealPatternRequestDto mealPatternRequestDto) throws Exception {
		return mealScheduleListBiz.putMealPatternDetailRow(mealPatternRequestDto);
	}

	/**
     * @Desc 식단 스케쥴 개별 수정 (스케쥴 내 상세수정)
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 개별 수정 (스케쥴 내 상세수정)")
	@PostMapping(value = "/admin/item/meal/putMealSchRow")
	public ApiResult<?> putMealSchRow(@RequestBody MealScheduleRequestDto mealScheduleRequestDto) throws Exception {
		return mealScheduleListBiz.putMealSchRow(mealScheduleRequestDto);
	}

	/**
     * @Desc 식단 패턴 업로드 시 식단품목명, 알러지식단 조회
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 업로드 시 식단품목명, 알러지식단 조회")
	@PostMapping(value = "/admin/item/meal/getMealPatternUploadData")
	public ApiResult<?> getMealPatternUploadData(String mealContsCd) throws Exception {
		return mealScheduleListBiz.getMealPatternUploadData(mealContsCd);
	}

	/**
     * @Desc 식단 패턴 업로드 시 식단품목명, 알러지식단 조회
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 패턴 업로드 시 식단품목명, 알러지식단 리스트 조회")
	@PostMapping(value = "/admin/item/meal/getMealPatternUploadDataList")
	public ApiResult<?> getMealPatternUploadDataList(@RequestParam(value = "mealContsCdArray[]" , required = true) List<String> mealContsCdArray) throws Exception {
		return mealScheduleListBiz.getMealPatternUploadDataList(mealContsCdArray);
	}





}
