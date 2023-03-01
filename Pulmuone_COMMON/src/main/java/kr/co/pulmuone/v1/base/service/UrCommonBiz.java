package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.GetUserGroupListRequestDto;
import kr.co.pulmuone.v1.base.dto.SellersRequestDto;
import kr.co.pulmuone.v1.base.dto.SupplierRequestDto;
import kr.co.pulmuone.v1.base.dto.WarehouseRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.vo.AddBuyerChangeHististoryParamVo;

public interface UrCommonBiz {

    ApiResult<?> getUserGroupList(GetUserGroupListRequestDto dto) throws Exception;
    ApiResult<?> getDropDownSupplierList(SupplierRequestDto supplierRequestDto, boolean authCheck);
    ApiResult<?> getDropDownSupplierByWarehouseList(WarehouseRequestDto warehouseRequestDto, boolean authCheck);
    ApiResult<?> getDropDownWarehouseGroupByWarehouseList(WarehouseRequestDto warehouseRequestDto, boolean authCheck);

    int addBuyerChangeHististory(AddBuyerChangeHististoryParamVo vo) throws Exception;

    ApiResult<?> getDropDownWarehouseList(WarehouseRequestDto warehouseRequestDto, boolean authCheck);

    /**
     * 외부몰그룹에 해당되는 외부몰 조회
     *
     * @param sellersRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    ApiResult<?> getDropDownSellersGroupBySellersList(SellersRequestDto sellersRequestDto) throws Exception;

    /**
     * 정산용 출고처 조회
     *
     * @param warehouseRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    ApiResult<?> getDropDownWarehouseStlmnList(WarehouseRequestDto warehouseRequestDto) throws Exception;

	/**
	 * 출고처PK기준 공급업체 조회
	 *
	 * @param warehouseRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
    ApiResult<?> getDropDownSupplierListByWarehouseId(WarehouseRequestDto warehouseRequestDto) throws Exception;

}
