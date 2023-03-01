package kr.co.pulmuone.bos.calculate.affiliate;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.promotion.linkprice.dto.LinkPriceRequestDto;
import kr.co.pulmuone.v1.promotion.linkprice.dto.LinkPriceResponseDto;
import kr.co.pulmuone.v1.promotion.linkprice.service.LinkPriceBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LinkPriceController {

	@Autowired
	private LinkPriceBiz linkPriceBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 링크프라이스 내역조회
	 */
	@ApiOperation(value = "링크프라이스 내역조회")
	@PostMapping(value = "/admin/calculate/affiliate/getLinkPriceList")
	@ApiResponse(code = 900, message = "response data", response = LinkPriceResponseDto.class)
	public ApiResult<?> getLinkPriceList(LinkPriceRequestDto dto) throws Exception {
		return linkPriceBiz.getLinkPriceList((LinkPriceRequestDto) BindUtil.convertRequestToObject(request, LinkPriceRequestDto.class));
	}

    /**
     * 링크프라이스 내역조회 엑셀 다운로드
     */
    @ApiOperation(value = "링크프라이스 내역조회 엑셀 다운로드")
    @PostMapping(value = "/admin/calculate/affiliate/getLinkPriceListExcel")
    public ModelAndView getLinkPriceListExcel(@RequestBody LinkPriceRequestDto dto) throws Exception {
        ExcelDownloadDto excelDownloadDto = linkPriceBiz.getLinkPriceListExcel(dto);
        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);
        return modelAndView;
    }

	/**
	 * 링크프라이스 내역조회 Total data 조회
	 */
	@PostMapping(value = "/admin/calculate/affiliate/getLinkPriceListTotal")
	@ApiOperation(value = "링크프라이스 내역조회 Total data 조회")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = LinkPriceResponseDto.class)
	})
	public ApiResult<?> getLinkPriceListTotal(LinkPriceRequestDto dto) throws Exception {
		return linkPriceBiz.getLinkPriceListTotal(dto);
	}
}
