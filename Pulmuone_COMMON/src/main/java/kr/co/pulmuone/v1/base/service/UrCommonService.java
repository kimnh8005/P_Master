package kr.co.pulmuone.v1.base.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import kr.co.pulmuone.v1.base.dto.*;
import kr.co.pulmuone.v1.base.dto.vo.SellersVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.base.dto.vo.GetUserGroupListResultVo;
import kr.co.pulmuone.v1.base.dto.vo.SupplierVo;
import kr.co.pulmuone.v1.base.dto.vo.WarehouseVo;
import kr.co.pulmuone.v1.comm.mapper.base.UrCommonMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.buyer.dto.vo.AddBuyerChangeHististoryParamVo;

@Service
public class UrCommonService {

    @Autowired
    UrCommonMapper urCommonMapper;

	/**
	 * @Desc 회원그룹 조회
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	protected ApiResult<?> getUserGroupList(GetUserGroupListRequestDto dto) throws Exception {
	    GetUserGroupListResponseDto result = new GetUserGroupListResponseDto();

		List<GetUserGroupListResultVo> rows = urCommonMapper.getUserGroupList(dto);

		result.setRows(rows);

		return ApiResult.success(result);
	}

    /**
     * @Desc 공급처 검색 DropDown
     * @param supplierRequestDto
     * @return SupplierResponseDto
     * @throws Exception
     */
    protected ApiResult<?> getDropDownSupplierList(SupplierRequestDto supplierRequestDto, boolean authCheck) {
        SupplierResponseDto result = new SupplierResponseDto();

        if (authCheck) {
            // 권한 설정
            UserVo userVo = SessionUtil.getBosUserVO();
            List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
            listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
            supplierRequestDto.setListAuthSupplierId(listAuthSupplierId);
        }

        List<SupplierVo> rows = urCommonMapper.getDropDownSupplierList(supplierRequestDto);

        result.setRows(rows);

        return ApiResult.success(result);
    }

    /**
     * @Desc 공급처에 해당되는 출고처 검색 DropDown
     * @param warehouseRequestDto
     * @return WarehouseResponseDto
     * @throws Exception
     */
    protected ApiResult<?> getDropDownSupplierByWarehouseList(WarehouseRequestDto warehouseRequestDto, boolean authCheck) {
        WarehouseResponseDto result = new WarehouseResponseDto();

        if (authCheck) {
            // 권한 설정
            UserVo userVo = SessionUtil.getBosUserVO();
            List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
            listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
            warehouseRequestDto.setListAuthSupplierId(listAuthSupplierId);
            List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
            listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
            warehouseRequestDto.setListAuthWarehouseId(listAuthWarehouseId);
        }

        List<WarehouseVo> rows = urCommonMapper.getDropDownSupplierByWarehouseList(warehouseRequestDto);

        result.setRows(rows);

        return ApiResult.success(result);
    }

    /**
     * @Desc 출고처그룹에 해당되는 출고처 검색 DropDown
     * @param warehouseRequestDto
     * @return WarehouseResponseDto
     * @throws Exception
     */
    protected ApiResult<?> getDropDownWarehouseGroupByWarehouseList(WarehouseRequestDto warehouseRequestDto, boolean authCheck) {
        WarehouseResponseDto result = new WarehouseResponseDto();

        if (authCheck) {
            // 권한 설정
            UserVo userVo = SessionUtil.getBosUserVO();
            List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
            listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
            warehouseRequestDto.setListAuthWarehouseId(listAuthWarehouseId);
        }

        List<WarehouseVo> rows = urCommonMapper.getDropDownWarehouseGroupByWarehouseList(warehouseRequestDto);

        result.setRows(rows);

        return ApiResult.success(result);
    }

    /**
     * @Desc  회원정보변경 이력 추가
     * @param AddBuyerChangeHististoryParamVo
     * @throws Exception
     */
    protected int addBuyerChangeHististory(AddBuyerChangeHististoryParamVo vo) throws Exception{
        return urCommonMapper.addBuyerChangeHististory(vo);
    }


    /**
     * @Desc 출고처목록 DropDown
     * @param
     * @return WarehouseResponseDto
     */
    protected ApiResult<?> getDropDownWarehouseList(WarehouseRequestDto warehouseRequestDto, boolean authCheck) {
        WarehouseResponseDto result = new WarehouseResponseDto();

        if (authCheck) {
            // 권한 설정
            UserVo userVo = SessionUtil.getBosUserVO();
            List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
            listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
            warehouseRequestDto.setListAuthWarehouseId(listAuthWarehouseId);
        }

        List<WarehouseVo> rows = urCommonMapper.getDropDownWarehouseList(warehouseRequestDto);

        result.setRows(rows);

        return ApiResult.success(result);
    }

    /**
     * 외부몰그룹에 해당되는 외부몰 조회
     *
     * @param sellersRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    protected ApiResult<?> getDropDownSellersGroupBySellersList(SellersRequestDto sellersRequestDto) throws Exception {
        SellersResponseDto result = new SellersResponseDto();

        List<SellersVo> rows = urCommonMapper.getDropDownSellersGroupBySellersList(sellersRequestDto);

        result.setRows(rows);

        return ApiResult.success(result);
    }

    /**
     * 정산용 출고처 조회
     *
     * @param warehouseRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    protected ApiResult<?> getDropDownWarehouseStlmnList(WarehouseRequestDto warehouseRequestDto) throws Exception {

        WarehouseResponseDto result = new WarehouseResponseDto();

        List<WarehouseVo> rows = urCommonMapper.getDropDownWarehouseStlmnList(warehouseRequestDto);

        result.setRows(rows);

        return ApiResult.success(result);
    }

	/**
	 * 출고처PK기준 공급업체 조회
	 *
	 * @param warehouseRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
    protected ApiResult<?> getDropDownSupplierListByWarehouseId(WarehouseRequestDto warehouseRequestDto) throws Exception {
    	SellersResponseDto result = new SellersResponseDto();

        List<SellersVo> rows = urCommonMapper.getDropDownSupplierListByWarehouseId(warehouseRequestDto);

        result.setRows(rows);

        return ApiResult.success(result);
    }

}
