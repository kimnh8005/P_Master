package kr.co.pulmuone.bos.outmall.sellers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.api.ezadmin.dto.EZAdminResponseDefaultDto;
import kr.co.pulmuone.v1.api.ezadmin.service.EZAdminBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersListRequestDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersListResponseDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersRequestDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersResponseDto;
import kr.co.pulmuone.v1.outmall.sellers.service.SellersBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
* Forbiz Korea
* 마스터 품목 리스트 Controller
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 11. 18.               이명         최초작성
* =======================================================================
 * </PRE>
 */
@RestController
public class SellersController {

    @Autowired
    private SellersBiz sellersBiz;

	@Autowired
	private EZAdminBiz ezAdminBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView;

    /**
     * 외부몰 리스트 조회
     *
     * @param sellersListRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "외부몰 리스트 조회")
    @PostMapping(value = "/admin/outmall/sellers/getSellersList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SellersListResponseDto.class)
    })
    public ApiResult<?> getSellersList(HttpServletRequest request, SellersListRequestDto sellersListRequestDto) throws Exception {
        return sellersBiz.getSellersList(BindUtil.bindDto(request, SellersListRequestDto.class));
    }

    /**
     * 외부몰 리스트 조회
     *
     * @param sellersListRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "외부몰 리스트 조회")
    @PostMapping(value = "/admin/outmall/sellers/getSellersExcelList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SellersListResponseDto.class)
    })
    public ModelAndView getSellersExcelList(HttpServletRequest request, @RequestBody SellersListRequestDto sellersListRequestDto) throws Exception {
        ExcelDownloadDto excelDownloadDto = sellersBiz.getSellersExcelList(sellersListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    /**
     * 외부몰 리스트 조회
     *
     * @param sellersListRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "외부몰 판매처(외부몰)그룹별 리스트 조회")
    @GetMapping(value = "/admin/outmall/sellers/getSellersGroupList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SellersListResponseDto.class)
    })
    public ApiResult<?> getSellersGroupList(SellersListRequestDto sellersListRequestDto) throws Exception {
        return sellersBiz.getSellersGroupList(sellersListRequestDto);
    }

    /**
     * 외부몰 상세 조회
     *
     * @param SellersResponseDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "외부몰 상세조회")
    @PostMapping(value = "/admin/outmall/sellers/getSellers")
    @ResponseBody
    @ApiResponses(value = {
        @ApiResponse(code = 900, message = "response data", response = SellersResponseDto.class)
    })
    public ApiResult<?> getSellers(SellersRequestDto sellersRequestDto) throws Exception {
        return sellersBiz.getSellers(sellersRequestDto);
    }

    /**
     * 외부몰 정보 등록
     *
     * @param SellersResponseDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "외부몰 정보 등록", httpMethod = "POST")
    @PostMapping(value = "/admin/outmall/sellers/addSellers")
    public ApiResult<?> addSellers(@RequestBody SellersRequestDto sellersRequestDto) throws Exception {
        return sellersBiz.addSellers(sellersRequestDto);
    }

    /**
     * 외부몰 정보 수정
     *
     * @param SellersResponseDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "외부몰 정보 수정", httpMethod = "POST")
    @PostMapping(value = "/admin/outmall/sellers/putSellers")
    public ApiResult<?> putSellers(@RequestBody SellersRequestDto sellersRequestDto) throws Exception {
        return sellersBiz.putSellers(sellersRequestDto);
    }

	/**
     *외부몰 목록 조회 EZ Admin API 호출
     * @return EZAdminResponseDefaultDto
     *
     */
	@PostMapping(value = "/admin/outmall/sellers/getEZAdminEtcShopInfo")
	@ApiOperation(value = "외부몰 목록 조회 EZ Admin API 호출", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "response data", response = EZAdminResponseDefaultDto.class)
	})
	public ApiResult<?> getEZAdminEtcShopInfo(){
		return ezAdminBiz.getEtcInfo("shopinfo");
	}

    /**
     * 외부몰 물류IF 일괄 수정
     *
     * @return ApiResult<?>
     */
    @ApiOperation(value = "외부몰 물류IF 일괄 수정", httpMethod = "POST")
    @PostMapping(value = "/admin/outmall/sellers/putErpInterfaceStatusChg")
    public ApiResult<?> putErpInterfaceStatusChg(@RequestBody SellersRequestDto sellersRequestDto) throws Exception {
        return ApiResult.success(sellersBiz.putErpInterfaceStatusChg(sellersRequestDto));
    }
}
