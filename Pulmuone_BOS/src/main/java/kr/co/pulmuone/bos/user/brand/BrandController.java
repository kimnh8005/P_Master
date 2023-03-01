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
import kr.co.pulmuone.v1.user.brand.dto.AddBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandListResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.PutBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.service.BrandBiz;

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
*  1.0    2020. 6. 26.                jg          최초작성
* =======================================================================
* </PRE>
*/

@RestController
public class BrandController {

	@Autowired
	private BrandBiz brandBiz;

	@Autowired(required=true)
	private HttpServletRequest request;


	/**
	 * 브랜드 목록 조회
	 *   - post
	 * @param GetBrandListRequestDto
	 * @return GetBrandListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/brand/getBrandList")
	@ApiOperation(value = "브랜드 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetBrandListResponseDto.class)
	})
	public ApiResult<?> getBrandList(GetBrandListRequestDto dto) throws Exception {
        return brandBiz.getBrandList((GetBrandListRequestDto) BindUtil.convertRequestToObject(request, GetBrandListRequestDto.class));
	}


	/**
	 * 브랜드 목록 조회 (리스트박스 조회용)
	 *   - post
	 * @param GetBrandListRequestDto
	 * @return GetBrandListResponseDto
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ur/brand/searchBrandList")
	@ApiOperation(value = "브랜드 목록 조회 (리스트박스 조회용)")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetBrandListResponseDto.class)
	})
	public ApiResult<?> searchBrandList(GetBrandRequestDto dto) throws Exception {
        return brandBiz.searchBrandList(dto);
	}



    /**
     * 브랜드 상세 조회
     *   - post
     * @param GetBrandRequestDto
     * @return GetBrandResponseDto
     * @throws Exception
    */
	@PostMapping(value = "/admin/ur/brand/getBrand")
	@ApiOperation(value = "브랜드 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetBrandResponseDto.class)
	})
	public ApiResult<?> getBrand(GetBrandRequestDto dto) throws Exception {
        return brandBiz.getBrand(dto);
	}

	/**
     * 브랜드 등록
     *   - post
     * @param AddBrandRequestDto
     * @return AddBrandResponseDto
     * @throws Exception
    */
	@PostMapping(value = "/admin/ur/brand/addBrand")
	@ApiOperation(value = "브랜드 등록")
    public ApiResult<?> addBrand(AddBrandRequestDto dto) throws Exception {

        return brandBiz.addBrand(dto);
    }

    /**
     * 브랜드 수정
     *   - post
     * @param PutBrandRequestDto
     * @return PutBrandResponseDto
     * @throws Exception
    */
    @PostMapping(value = "/admin/ur/brand/putBrand")
    @ApiOperation(value = "브랜드 수정")
    public ApiResult<?> putBrand(PutBrandRequestDto dto) throws Exception {
        return brandBiz.putBrand(dto);
    }



}
