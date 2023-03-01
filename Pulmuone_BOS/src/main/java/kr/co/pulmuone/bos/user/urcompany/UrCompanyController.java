package kr.co.pulmuone.bos.user.urcompany;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.base.dto.WarehouseRequestDto;
import kr.co.pulmuone.v1.base.dto.WarehouseResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.urcompany.dto.AddClientRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetClientRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetClientResponseDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetCompanyListRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetCompanyListResponseDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetStoreListRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetStoreListResponsetDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetSupplierListRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetSupplierListResponseDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetWarehouseListRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetWarehouseListResponseDto;
import kr.co.pulmuone.v1.user.urcompany.dto.PutClientRequestDto;
import kr.co.pulmuone.v1.user.urcompany.service.UrCompanyBiz;

@RestController
public class UrCompanyController {

	@Autowired
	private UrCompanyBiz urCompanyBiz;

	@Autowired(required=true)
	private HttpServletRequest request;


	/**
	 * 거래처 목록 조회
	 * @param getCompanyListRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/urCompany/getCompanyList")
	@ApiOperation(value = "거래처 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCompanyListResponseDto.class)
	})
	public ApiResult<?> getCompanyList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception {
		return urCompanyBiz.getCompanyList((GetCompanyListRequestDto) BindUtil.convertRequestToObject(request, GetCompanyListRequestDto.class));
	}


	/**
	 * 거래처 상세조회
	 * @param getClientRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/urCompany/getClient")
	@ApiOperation(value = "거래처 상세조회")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetClientResponseDto.class)
	})
	public ApiResult<?> getClient(GetClientRequestDto getClientRequestDto) throws Exception {
		return urCompanyBiz.getClient(getClientRequestDto);
	}


	/**
	 * 공급업체 조회
	 * @param getSupplierListRequestDto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ur/urCompany/getSupplierCompanyList")
	@ApiOperation(value = "공급업체 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetSupplierListResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getSupplierCompanyList(GetSupplierListRequestDto getSupplierListRequestDto) throws Exception {
		return urCompanyBiz.getSupplierCompanyList(getSupplierListRequestDto);
	}


	/**
	 * 출고처 별 공급업체 조회
	 * @param getSupplierListRequestDto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ur/urCompany/getSupplierCompanyListByWhareHouse")
	@ApiOperation(value = "출고처 별 공급업체 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetSupplierListResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getSupplierCompanyListByWhareHouse(GetSupplierListRequestDto getSupplierListRequestDto) throws Exception {
		return urCompanyBiz.getSupplierCompanyListByWhareHouse(getSupplierListRequestDto);
	}


	/**
	 * 출고처 검색 팝업
	 * @param getWarehouseListRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/urCompany/getWarehouseList")
	@ApiOperation(value = "출고처 검색 팝업")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetWarehouseListResponseDto.class)
	})
	public ApiResult<?> getWarehouseList(GetWarehouseListRequestDto getWarehouseListRequestDto) throws Exception {
		return urCompanyBiz.getWarehouseList((GetWarehouseListRequestDto) BindUtil.convertRequestToObject(request, GetWarehouseListRequestDto.class));
	}

	/**
	 * 출고처 검색 (Get방식)
	 * @param getWarehouseListRequestDto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ur/urCompany/getWarehouseList")
	@ApiOperation(value = "출고처 검색 (Get방식)")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetWarehouseListResponseDto.class)
	})
	public ApiResult<?> getWarehouseListGet(@ModelAttribute("GetWarehouseListRequestDto") GetWarehouseListRequestDto getWarehouseListRequestDto) throws Exception {
		return urCompanyBiz.getWarehouseList(getWarehouseListRequestDto);
	}


	/**
	 * 매장 조회
	 * @param getStoreListRequestDto
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/admin/ur/urCompany/getStoreList")
	@ApiOperation(value = "매장 조회")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetStoreListResponsetDto.class)
	})
	public ApiResult<?> getStoreList(GetStoreListRequestDto getStoreListRequestDto) throws Exception {
		return urCompanyBiz.getStoreList(getStoreListRequestDto);
	}



	/**
	 * 출고처 등록
	 * @param addClientRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/urCompany/addClient")
	@ApiOperation(value = "출고처 등록", httpMethod = "POST")
	public ApiResult<?> addClient(AddClientRequestDto addClientRequestDto) throws Exception {
		return urCompanyBiz.addClient(addClientRequestDto);
	}


	/**
	 * 출고처 수정
	 * @param putClientRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/urCompany/putClient")
	@ApiOperation(value = "출고처 등록", httpMethod = "POST")
	public ApiResult<?> putClient(PutClientRequestDto putClientRequestDto) throws Exception {
		return urCompanyBiz.putClient(putClientRequestDto);
	}


	@GetMapping(value = "/admin/comn/getWarehouseGroupByWarehouseList")
	@ApiOperation(value = "출고처그룹에 해당되는 출고처 조회", notes = "출고처그룹에 해당되는 출고처 검색 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	public ApiResult<?> getWarehouseGroupByWarehouseList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception{
	    return urCompanyBiz.getWarehouseGroupByWarehouseList(getCompanyListRequestDto);

	}




	@GetMapping(value = "/admin/comn/getSupplierCompanyByWarehouseList")
	@ApiOperation(value = "출고처그룹에 해당되는 출고처 조회", notes = "출고처그룹에 해당되는 출고처 검색 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	public ApiResult<?> getSupplierCompanyByWarehouseList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception{
	    return urCompanyBiz.getSupplierCompanyByWarehouseList(getCompanyListRequestDto);

	}


}
