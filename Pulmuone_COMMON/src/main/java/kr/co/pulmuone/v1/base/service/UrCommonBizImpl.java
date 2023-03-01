package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.GetUserGroupListRequestDto;
import kr.co.pulmuone.v1.base.dto.SellersRequestDto;
import kr.co.pulmuone.v1.base.dto.SupplierRequestDto;
import kr.co.pulmuone.v1.base.dto.WarehouseRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.vo.AddBuyerChangeHististoryParamVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrCommonBizImpl implements UrCommonBiz {

    @Autowired
    UrCommonService urCommonService;

    @Override
    public ApiResult<?> getUserGroupList(GetUserGroupListRequestDto dto) throws Exception {
        return urCommonService.getUserGroupList(dto);
    }

    @Override
    public ApiResult<?> getDropDownSupplierList(SupplierRequestDto supplierRequestDto, boolean authCheck) {
        return urCommonService.getDropDownSupplierList(supplierRequestDto, authCheck);
    }

    @Override
    public ApiResult<?> getDropDownSupplierByWarehouseList(WarehouseRequestDto warehouseRequestDto, boolean authCheck) {
        return urCommonService.getDropDownSupplierByWarehouseList(warehouseRequestDto, authCheck);
    }

    @Override
    public ApiResult<?> getDropDownWarehouseGroupByWarehouseList(WarehouseRequestDto warehouseRequestDto, boolean authCheck) {
        return urCommonService.getDropDownWarehouseGroupByWarehouseList(warehouseRequestDto, authCheck);
    }

    @Override
    public int addBuyerChangeHististory(AddBuyerChangeHististoryParamVo vo) throws Exception{
    	return urCommonService.addBuyerChangeHististory(vo);
    }

    @Override
    public ApiResult<?> getDropDownWarehouseList(WarehouseRequestDto warehouseRequestDto, boolean authCheck) {
        return urCommonService.getDropDownWarehouseList(warehouseRequestDto, authCheck);
    }

    /**
     * 외부몰그룹에 해당되는 외부몰 조회
     *
     * @param sellersRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @Override
    public ApiResult<?> getDropDownSellersGroupBySellersList(SellersRequestDto sellersRequestDto) throws Exception {
        return urCommonService.getDropDownSellersGroupBySellersList(sellersRequestDto);
    }

    /**
     * 정산용 출고처 조회
     *
     * @param warehouseRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @Override
    public ApiResult<?> getDropDownWarehouseStlmnList(WarehouseRequestDto warehouseRequestDto) throws Exception {
        return urCommonService.getDropDownWarehouseStlmnList(warehouseRequestDto);
    }

	/**
	 * 출고처PK기준 공급업체 조회
	 *
	 * @param warehouseRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
    @Override
    public ApiResult<?> getDropDownSupplierListByWarehouseId(WarehouseRequestDto warehouseRequestDto) throws Exception{
    	return urCommonService.getDropDownSupplierListByWarehouseId(warehouseRequestDto);
    }


}
