package kr.co.pulmuone.v1.comm.mapper.base;

import java.util.List;

import kr.co.pulmuone.v1.base.dto.SellersRequestDto;
import kr.co.pulmuone.v1.base.dto.vo.SellersVo;
import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.base.dto.GetUserGroupListRequestDto;
import kr.co.pulmuone.v1.base.dto.SupplierRequestDto;
import kr.co.pulmuone.v1.base.dto.WarehouseRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.AddBuyerChangeHististoryParamVo;
import kr.co.pulmuone.v1.base.dto.vo.GetUserGroupListResultVo;
import kr.co.pulmuone.v1.base.dto.vo.SupplierVo;
import kr.co.pulmuone.v1.base.dto.vo.WarehouseVo;

@Mapper
public interface UrCommonMapper {

    /**
     * 회원그룹 조회
     * @param vo
     * @return
     * @throws Exception
     */
    List<GetUserGroupListResultVo> getUserGroupList(GetUserGroupListRequestDto vo) throws Exception;

    /**
     * 회원정보변경 이력 추가
     * @param AddBuyerChangeHististoryParamVo
     * @throws Exception
     */
    int addBuyerChangeHististory(AddBuyerChangeHististoryParamVo vo) throws Exception;


    /**
     * @Desc 공급처 검색 DropDown
     * @param supplierRequestDto
     * @return List<SupplierVo>
     */
    List<SupplierVo> getDropDownSupplierList(SupplierRequestDto supplierRequestDto);

    /**
     * @Desc 공급처에 해당되는 출고처 검색 DropDown
     * @param warehouseRequestDto
     * @return List<WarehouseVo>
     */
    List<WarehouseVo> getDropDownSupplierByWarehouseList(WarehouseRequestDto warehouseRequestDto);

    /**
     * @Desc 출고처그룹에 해당되는 출고처 검색 DropDown
     * @param warehouseRequestDto
     * @return List<WarehouseVo>
     */
    List<WarehouseVo> getDropDownWarehouseGroupByWarehouseList(WarehouseRequestDto warehouseRequestDto);


    /**
     * @Desc 출고차 목록  DropDown
     * @return
     * @return List<WarehouseVo>
     */
    List<WarehouseVo> getDropDownWarehouseList(WarehouseRequestDto warehouseRequestDto);

    /**
     * 외부몰그룹에 해당되는 외부몰 조회
     *
     * @param sellersRequestDto
     * @return List<SellersVo>
     * @throws Exception
     */
    List<SellersVo> getDropDownSellersGroupBySellersList(SellersRequestDto sellersRequestDto) throws Exception;

    /**
     * 정산용 출고처 조회
     *
     * @param warehouseRequestDt
     * @return List<WarehouseVo>
     * @throws Exception
     */
    List<WarehouseVo> getDropDownWarehouseStlmnList(WarehouseRequestDto warehouseRequestDt) throws Exception;

	/**
	 * 출고처PK기준 공급업체 조회
	 *
	 * @param warehouseRequestDto
	 * @return List<SellersVo>
	 * @throws Exception
	 */
    List<SellersVo> getDropDownSupplierListByWarehouseId(WarehouseRequestDto warehouseRequestDto) throws Exception;
}
