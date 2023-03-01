package kr.co.pulmuone.bos.base.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.base.dto.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.pulmuone.v1.base.service.UrCommonBiz;

/**
* <PRE>
* Forbiz Korea
* Ur 공통 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 6. 29.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@RestController
public class UrCommonController {

    @Autowired
	private UrCommonBiz urCommonBiz;

	@GetMapping(value = "/admin/comn/getUserGroupList")
	@ApiOperation(value = "회원그룹 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetUserGroupListResponseDto.class)
	})
	public ApiResult<?> getUserGroupList(GetUserGroupListRequestDto dto) throws Exception{
		return urCommonBiz.getUserGroupList(dto);
	}

	@GetMapping(value = "/admin/comn/getDropDownSupplierList")
	@ApiOperation(value = "공급처 조회", notes = "공급처 검색 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = SupplierResponseDto.class)
	})
    public ApiResult<?> getDropDownSupplierList(SupplierRequestDto supplierRequestDto) {

        return urCommonBiz.getDropDownSupplierList(supplierRequestDto, false);
    }

	@GetMapping(value = "/admin/comn/getDropDownAuthSupplierList")
	@ApiOperation(value = "공급처 조회", notes = "공급처 검색 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = SupplierResponseDto.class)
	})
    public ApiResult<?> getDropDownAuthSupplierList(SupplierRequestDto supplierRequestDto) {

        return urCommonBiz.getDropDownSupplierList(supplierRequestDto, true);
    }

	@GetMapping(value = "/admin/comn/getDropDownSupplierByWarehouseList")
	@ApiOperation(value = "공급처에 해당되는 출고처 조회", notes = "공급처에 해당되는 출고처 검색 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
    public ApiResult<?> getDropDownSupplierByWarehouseList(WarehouseRequestDto warehouseRequestDto) {
        return urCommonBiz.getDropDownSupplierByWarehouseList(warehouseRequestDto, false);

    }

	@GetMapping(value = "/admin/comn/getDropDownAuthSupplierByWarehouseList")
	@ApiOperation(value = "공급처에 해당되는 출고처 조회", notes = "공급처에 해당되는 출고처 검색 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
    public ApiResult<?> getDropDownAuthSupplierByWarehouseList(WarehouseRequestDto warehouseRequestDto) {
        return urCommonBiz.getDropDownSupplierByWarehouseList(warehouseRequestDto, true);

    }

	@GetMapping(value = "/admin/comn/getDropDownWarehouseGroupByWarehouseList")
	@ApiOperation(value = "출고처그룹에 해당되는 출고처 조회", notes = "출고처그룹에 해당되는 출고처 검색 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	public ApiResult<?> getDropDownWarehouseGroupByWarehouseList(WarehouseRequestDto warehouseRequestDto) {
	    return urCommonBiz.getDropDownWarehouseGroupByWarehouseList(warehouseRequestDto, false);

	}

	@GetMapping(value = "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList")
	@ApiOperation(value = "출고처그룹에 해당되는 출고처 조회", notes = "출고처그룹에 해당되는 출고처 검색 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	public ApiResult<?> getDropDownAuthWarehouseGroupByWarehouseList(WarehouseRequestDto warehouseRequestDto) {
	    return urCommonBiz.getDropDownWarehouseGroupByWarehouseList(warehouseRequestDto, true);

	}


	@GetMapping(value = "/admin/comn/getDropDownWarehouseList")
	@ApiOperation(value = "출고처 목록 조회", notes = "출고처 목록 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	public ApiResult<?> getDropDownWarehouseList(WarehouseRequestDto warehouseRequestDto) {
	    return urCommonBiz.getDropDownWarehouseList(warehouseRequestDto, false);

	}

	@GetMapping(value = "/admin/comn/getDropDownAuthWarehouseList")
	@ApiOperation(value = "출고처 목록 조회", notes = "출고처 목록 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	public ApiResult<?> getDropDownAuthWarehouseList(WarehouseRequestDto warehouseRequestDto) {
	    return urCommonBiz.getDropDownWarehouseList(warehouseRequestDto, true);

	}

	/**
	 * 외부몰그룹에 해당되는 외부몰 조회
	 *
	 * @param sellersRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@GetMapping(value = "/admin/comn/getDropDownSellersGroupBySellersList")
	@ApiOperation(value = "외부몰그룹에 해당되는 외부몰 조회", notes = "외부몰그룹에 해당되는 외부몰 검색 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = SellersResponseDto.class)
	})
	public ApiResult<?> getDropDownSellersGroupBySellersList(SellersRequestDto sellersRequestDto) throws Exception {
		return urCommonBiz.getDropDownSellersGroupBySellersList(sellersRequestDto);

	}

	/**
	 * 정산용 출고처 조회
	 *
	 * @param warehouseRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@GetMapping(value = "/admin/comn/getDropDownWarehouseStlmnList")
	@ApiOperation(value = "정산용 출고처 조회", notes = "정산용 출고처 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	public ApiResult<?> getDropDownWarehouseStlmnList(WarehouseRequestDto warehouseRequestDto) throws Exception {

		return urCommonBiz.getDropDownWarehouseStlmnList(warehouseRequestDto);
	}

	/**
	 * 출고처PK기준 공급업체 조회
	 *
	 * @param warehouseRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@GetMapping(value = "/admin/comn/getDropDownSupplierListByWarehouseId")
	@ApiOperation(value = "출고처PK기준 공급업체 조회", notes = "출고처PK기준 공급업체 조회 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = SupplierResponseDto.class)
	})
	public ApiResult<?> getDropDownSupplierListByWarehouseId(WarehouseRequestDto warehouseRequestDto) throws Exception {

		return urCommonBiz.getDropDownSupplierListByWarehouseId(warehouseRequestDto);
	}

}
