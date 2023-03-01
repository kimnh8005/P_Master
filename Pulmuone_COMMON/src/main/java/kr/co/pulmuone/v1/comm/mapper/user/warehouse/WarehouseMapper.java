package kr.co.pulmuone.v1.comm.mapper.user.warehouse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseModifyDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseTemplateRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.ShippingTemplateVo;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseResultVo;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WarehouseMapper {

	Page<WarehouseResultVo> getWarehouseList(WarehouseRequestDto warehouseRequestDto) throws Exception;

	WarehouseResultVo getWarehouse(WarehouseRequestDto warehouseRequestDto) throws Exception;

	List<WarehouseResultVo> getSupplierList(WarehouseRequestDto warehouseRequestDto) throws Exception;

	int addWarehouse(WarehouseModifyDto warehouseModifyDto);

	int putWarehouse(WarehouseModifyDto warehouseModifyDto);

	int addSupplierWarehouse(WarehouseModifyDto warehouseModifyDto);

	int delSupplierWarehouse(WarehouseModifyDto warehouseModifyDto);

	WarehouseResultVo getWarehouseTemplateInfo(WarehouseTemplateRequestDto warehouseTemplateRequestDto) throws Exception;

	List<ShippingTemplateVo> getShippingTemplateList(WarehouseTemplateRequestDto warehouseTemplateRequestDto) throws Exception;

	int getDuplicateCompanyName(WarehouseModifyDto warehouseModifyDto) throws Exception;

	List<String> getWarehouseCompanyName() throws Exception;

	List<WarehouseResultVo> getWarehouseExcelDownload(WarehouseRequestDto warehouseRequestDto);

	String getDicNames(@Param("getNames") List<String> getNames);
}
