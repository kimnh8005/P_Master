package kr.co.pulmuone.bos.user.brand;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandListResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandResponseDto;
import kr.co.pulmuone.v1.user.brand.service.DisplayBrandBiz;

/**
* <PRE>
* Forbiz Korea
* Class의 기능과 역할을 상세히 기술한다.
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 03.    안치열             최초작성
* =======================================================================
* </PRE>
*/

@RestController
public class DisplayBrandController {

	@Autowired
	private DisplayBrandBiz displayBrandBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 전시 브랜드 목록 조회
	 *   - post
	 * @param DisplayBrandListRequestDto
	 * @return DisplayBrandListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/brand/getDisplayBrandList")
	@ApiOperation(value = "전시 브랜드 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = DisplayBrandListResponseDto.class)
	})
	public ApiResult<?> getDisplayBrandList(DisplayBrandListRequestDto displayBrandListRequestDto) throws Exception {
        return displayBrandBiz.getDisplayBrandList((DisplayBrandListRequestDto) BindUtil.convertRequestToObject(request, DisplayBrandListRequestDto.class));
	}


	/**
	 * 전시 브랜드 목록 조회 (리스트박스 조회용)
	 *   - post
	 * @param DisplayBrandRequestDto
	 * @return DisplayBrandListResponseDto
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ur/brand/searchDisplayBrandList")
	@ApiOperation(value = "전시 브랜드 목록 조회 (리스트박스 조회용)")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = DisplayBrandListResponseDto.class)
	})
	public ApiResult<?> searchDisplayBrandList(DisplayBrandRequestDto displayBrandRequestDto) throws Exception {
        return displayBrandBiz.searchDisplayBrandList(displayBrandRequestDto);
	}



    /**
     * 전시 브랜드 상세 조회
     *   - post
     * @param DisplayBrandRequestDto
     * @return DisplayBrandResponseDto
     * @throws Exception
    */
	@PostMapping(value = "/admin/ur/brand/getDisplayBrand")
	@ApiOperation(value = "전시 브랜드 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = DisplayBrandResponseDto.class)
	})
	public ApiResult<?> getDisplayBrand(DisplayBrandRequestDto displayBrandRequestDto) throws Exception {
        return displayBrandBiz.getDisplayBrand(displayBrandRequestDto);
	}

	/**
     * 전시 브랜드 등록
     *   - post
     * @param DisplayBrandRequestDto
     * @return
     * @throws Exception
    */
	@PostMapping(value = "/admin/ur/brand/addDisplayBrand")
	@ApiOperation(value = "전시 브랜드 등록")
    public ApiResult<?> addDisplayBrand(DisplayBrandRequestDto displayBrandRequestDto) throws Exception {

        return displayBrandBiz.addDisplayBrand(displayBrandRequestDto);
    }

    /**
     * 전시브랜드 수정
     *   - post
     * @param DisplayBrandRequestDto
     * @return
     * @throws Exception
    */
    @PostMapping(value = "/admin/ur/brand/putDisplayBrand")
    @ApiOperation(value = "전시 브랜드 수정")
    public ApiResult<?> putDisplayBrand(DisplayBrandRequestDto displayBrandRequestDto) throws Exception {
        return displayBrandBiz.putDisplayBrand(displayBrandRequestDto);
    }


}
