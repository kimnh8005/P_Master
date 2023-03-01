package kr.co.pulmuone.bos.item.master;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.OrgaDiscountRequestDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsOrgaDisBiz;

/**
 * <PRE>
* Forbiz Korea
* 올가 할인 연동 리스트 Controller
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 11. 13.               정형진         최초작성
* =======================================================================
 * </PRE>
 */
@RestController
public class GoodsOrgaDisListController {

	@Autowired
	private GoodsOrgaDisBiz goodsOrgaDisBiz;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
     * 올가 할인연동 리스트 조회
     *
     * @param MasterItemListRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "올가 할인연동 리스트 조회")
    @PostMapping(value = "/admin/item/master/getOrgaDisList")
    public ApiResult<?> getOrgaDisList(HttpServletRequest request, OrgaDiscountRequestDto orgaDiscountRequestDto) throws Exception {

    	orgaDiscountRequestDto = BindUtil.bindDto(request, OrgaDiscountRequestDto.class);
        return goodsOrgaDisBiz.getOrgaDisList(orgaDiscountRequestDto);

    }

    /**
     * 올가 할인연동 리스트 엑셀 다운로드 목록 조회
     *
     * @param OrgaDiscountRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "올가 할인연동 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/item/master/orgaDisExportExcel")
    public ModelAndView itemExportExcel(@RequestBody OrgaDiscountRequestDto paramDto) {

        ExcelDownloadDto excelDownloadDto = goodsOrgaDisBiz.getOrgaDisListExcel(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

}
