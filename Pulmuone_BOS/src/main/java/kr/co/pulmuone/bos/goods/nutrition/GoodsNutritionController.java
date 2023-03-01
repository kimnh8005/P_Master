package kr.co.pulmuone.bos.goods.nutrition;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.etc.dto.GoodsNutritionListResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.GoodsNutritionRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.GoodsNutritionResponseDto;
import kr.co.pulmuone.v1.goods.etc.service.GoodsNutritionBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 상품영양정보 관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.07.16	 박영후            최초작성
 *  1.0    2020.10.26	 이성준            리팩토링
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class GoodsNutritionController {

	private final GoodsNutritionBiz goodsNutritionBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


    @ApiOperation(value = "상품영양정보 목록 조회")
	@PostMapping(value = "/admin/goods/nutrition/getGoodsNutritionList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GoodsNutritionListResponseDto.class)
        })
	public ApiResult<?> getGoodsNutritionList(HttpServletRequest request,GoodsNutritionRequestDto goodsNutritionRequestDto) throws Exception{
		return goodsNutritionBiz.getGoodsNutritionList(BindUtil.bindDto(request, GoodsNutritionRequestDto.class));
	}


    @ApiOperation(value = "상품영양정보 상세 조회")
	@PostMapping(value = "/admin/goods/nutrition/getGoodsNutrition")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GoodsNutritionResponseDto.class)
        })
	public ApiResult<?> getGoodsNutrition(@RequestParam(value = "ilNutritionCode", required = true) String ilNutritionCode) {
		return goodsNutritionBiz.getGoodsNutrition(ilNutritionCode);
	}


    @ApiOperation(value = "상품영양정보 추가")
	@PostMapping(value = "/admin/goods/nutrition/addGoodsItemNutrition")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GoodsNutritionRequestDto.class),
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
        })
	public ApiResult<?> addGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) {
       try {
    	   return goodsNutritionBiz.addGoodsNutrition(goodsNutritionRequestDto);
       } catch(Exception e) {
           log.error("GoodsNutritionController.addGoodsNutrition : {}", e);
           return ApiResult.fail();
       }
	}


    @ApiOperation(value = "상품영양정보 수정")
	@PostMapping(value = "/admin/goods/nutrition/putGoodsNutrition")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "request data", response = GoodsNutritionRequestDto.class),
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
        })
	public ApiResult<?> putGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) {
    	try {
              return goodsNutritionBiz.putGoodsNutrition(goodsNutritionRequestDto);
    	} catch(Exception e) {
            log.error("GoodsNutritionController.putGoodsItemNutrition : {}", e);
           return ApiResult.fail();
       }
    }


    @ApiOperation(value = "상품영양정보 삭제")
	@PostMapping(value = "/admin/goods/nutrition/delGoodsItemNutrition")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "request data", response = GoodsNutritionRequestDto.class)
        })
	public ApiResult<?> delGoodsItemNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) {
       try {
		     return goodsNutritionBiz.delGoodsNutrition(goodsNutritionRequestDto);
       } catch(Exception e) {
           log.error("GoodsNutritionController.delGoodsNutrition : {}", e);
          return ApiResult.fail();
      }
   }


	@PostMapping(value = "/admin/goods/nutrition/exportExcel")
	@ApiOperation(value = "상품 영양정보 엑셀다운로드", httpMethod = "POST", notes = "상품 영양정보 엑셀다운로드")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GoodsNutritionRequestDto.class),
	})
	public ModelAndView getGoodsNutritionExportExcel(@RequestBody GoodsNutritionRequestDto dto) {
		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, goodsNutritionBiz.getGoodsNutritionExportExcel(dto));
		return modelAndView;
	}

}
