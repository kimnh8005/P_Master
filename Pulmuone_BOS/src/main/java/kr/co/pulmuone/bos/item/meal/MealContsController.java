package kr.co.pulmuone.bos.item.meal;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.item.dto.MealContsDto;
import kr.co.pulmuone.v1.goods.item.dto.MealContsListRequestDto;
import kr.co.pulmuone.v1.goods.item.service.MealContsBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

@RestController
public class MealContsController {

    @Autowired
    private MealContsBiz mealContsBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


    /**
     * 식단 컨텐츠 조회
     *
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/il/meal/getMealContsList")
    @ApiResponse(code = 900, message = "response data", response = MealContsListRequestDto.class)
    public ApiResult<?> getMealContsList(HttpServletRequest request, MealContsListRequestDto mealContsListRequestDto) throws Exception {
        return mealContsBiz.getMealContsList((MealContsListRequestDto) BindUtil.convertRequestToObject(request, MealContsListRequestDto.class));
    }

    /**
     * 식단 컨텐츠 엑셀다운로드
     * @param
     * @return
     */
    @PostMapping(value = "/admin/il/meal/getExportExcelMealContsList")
    public ModelAndView getExportExcelMealContsList(@RequestBody MealContsListRequestDto mealContsListRequestDto) throws Exception {
        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, mealContsBiz.getExportExcelMealContsList(mealContsListRequestDto));

        return modelAndView;
    }

    /**
     * 식단 컨텐츠 일괄 업로드
     *
     * @param request
     * @return ApiResult<?>
     */
    @ApiOperation(value = "식단 컨텐츠 일괄 업로드", httpMethod = "POST")
    @PostMapping(value = "/admin/il/meal/mealContsExcelUpload")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> mealContsExcelUpload(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = null;
        Iterator<String> iterator = request.getFileNames();
        if (iterator.hasNext()) {
            file = request.getFile(iterator.next());
        }

        String createId = (SessionUtil.getBosUserVO()).getUserId();

        return mealContsBiz.mealContsExcelUpload(file,createId);
    }

    /**
     * 식단 컨텐츠 등록
     * @param mealContsDto
     * @return
     */
    @ApiOperation(value = "식단 컨텐츠 등록")
    @PostMapping(value= "/admin/il/meal/addMealConts")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addMealConts(@RequestBody MealContsDto mealContsDto) throws Exception {

        mealContsDto.setCreateId((SessionUtil.getBosUserVO()).getUserId());

        if (!mealContsDto.getAddFile().isEmpty()) {
            mealContsDto.setAddFileList(
                    BindUtil.convertJsonArrayToDtoList(mealContsDto.getAddFile(), FileVo.class));
        }

        return mealContsBiz.addMealConts(mealContsDto);
    }

    /**
     * 식단 컨텐츠 단건 조회
     *
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/il/meal/getMealConts")
    @ApiResponse(code = 900, message = "response data", response = MealContsListRequestDto.class)
    public ApiResult<?> getMealConts(String ilGoodsDailyMealContsCd) throws Exception {
        return mealContsBiz.getMealConts(ilGoodsDailyMealContsCd);
    }

    /**
     * 식단 컨텐츠 수정
     * @param mealContsDto
     * @return
     */
    @ApiOperation(value = "식단 컨텐츠 수정")
    @PostMapping(value= "/admin/il/meal/putMealConts")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putMealConts(@RequestBody MealContsDto mealContsDto) throws Exception {

        mealContsDto.setModifyId((SessionUtil.getBosUserVO()).getUserId());

        if (!mealContsDto.getAddFile().isEmpty()) {
            mealContsDto.setAddFileList(
                    BindUtil.convertJsonArrayToDtoList(mealContsDto.getAddFile(), FileVo.class));
        }

        return mealContsBiz.putMealConts(mealContsDto);
    }

    /**
     * 식단 컨텐츠 삭제
     * @param ilGoodsDailyMealContsCd
     * @return
     */
    @ApiOperation(value = "식단 컨텐츠 삭제")
    @PostMapping(value= "/admin/il/meal/delMealConts")
    public ApiResult<?> delMealConts(String ilGoodsDailyMealContsCd) throws Exception {
        return mealContsBiz.delMealConts(ilGoodsDailyMealContsCd);
    }

}
