package kr.co.pulmuone.v1.user.warehouse.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.WarehouseEnums;
import kr.co.pulmuone.v1.comm.exception.BosCustomException;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseModifyDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseModifyResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseTemplateRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseTemplateResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.ShippingTemplateVo;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseResultVo;

@Service
public class WarehouseBizImpl implements WarehouseBiz{

	@Autowired
    WarehouseService warehouseService;


    @Override
    public ApiResult<?> getWarehouseList(WarehouseRequestDto warehouseRequestDto) throws Exception {
    	WarehouseResponseDto result = new WarehouseResponseDto();

        Page<WarehouseResultVo> warehouseResultVoList = warehouseService.getWarehouseList(warehouseRequestDto);

        result.setRows(warehouseResultVoList.getResult());
        result.setTotal(warehouseResultVoList.getTotal());

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> getWarehouse(WarehouseRequestDto warehouseRequestDto) throws Exception{

    	WarehouseModifyResponseDto result = new WarehouseModifyResponseDto();

    	WarehouseResultVo vo = warehouseService.getWarehouse(warehouseRequestDto);

    	List<WarehouseResultVo> supplierList = warehouseService.getSupplierList(warehouseRequestDto);
		vo.setSupplierList(supplierList);

		result.setRows(vo);

    	return ApiResult.success(result);
    }


    @Override
    public ApiResult<?> addWarehouse(WarehouseModifyDto warehouseModifyDto) throws Exception{

    	WarehouseResponseDto result = new WarehouseResponseDto();
    	warehouseService.addWarehouse(warehouseModifyDto);
		ArrayList<String> array = warehouseModifyDto.getSupplierCompanyList();
		Iterator<String> iterator = array.iterator();
		while (iterator.hasNext()) {
			warehouseModifyDto.setSupplierCompany(iterator.next());
			warehouseService.addSupplierWarehouse(warehouseModifyDto);
		}

    	return ApiResult.success();
    }


    @Override
    public ApiResult<?> putWarehouse(WarehouseModifyDto warehouseModifyDto) throws Exception{

		WarehouseRequestDto selectDto =new WarehouseRequestDto();
		selectDto.setUrWarehouseId(warehouseModifyDto.getUrWarehouseId());
		List<WarehouseResultVo> supplierList = warehouseService.getSupplierList(selectDto);

		ArrayList<String> deleteArray = warehouseModifyDto.getDeleteSupplierCompanyList();
		Iterator<String> deleteIterator = deleteArray.iterator();
		if(deleteArray.size() >0) {
			while (deleteIterator.hasNext()) {
				warehouseModifyDto.setDeleteSupplierCompany(deleteIterator.next());
				warehouseService.delSupplierWarehouse(warehouseModifyDto);
			}
		}
		ArrayList<String> array = warehouseModifyDto.getSupplierCompanyList();
		if(array.size() > 0) {

			for(int k=0;k<array.size();k++) {
				String arrayValue = array.get(k).trim();
				boolean idCheck = true;
				for(int i= 0 ;i<supplierList.size();i++) {
					String supplierId = supplierList.get(i).getUrSupplierId().trim();
					if(supplierId.equals(arrayValue)) {
						idCheck = false;
					}
				}
				if(idCheck) {
					warehouseModifyDto.setSupplierCompany(arrayValue);
					warehouseService.addSupplierWarehouse(warehouseModifyDto);
				}

			}
		}

    	warehouseService.putWarehouse(warehouseModifyDto);
    	return ApiResult.success();
    }


    @Override
    public ApiResult<?> getShippingTemplate(WarehouseTemplateRequestDto warehouseTemplateRequestDto) throws Exception{
    	WarehouseTemplateResponseDto result = new WarehouseTemplateResponseDto();

        WarehouseResultVo warehouseTemplateInfo = warehouseService.getWarehouseTemplateInfo(warehouseTemplateRequestDto);
        List<ShippingTemplateVo> shippingTemplateList = warehouseService.getShippingTemplateList(warehouseTemplateRequestDto);

        result.setWarehouseResultTemplate(warehouseTemplateInfo);
        result.setShippingTemplateList(shippingTemplateList);

        return ApiResult.success(result);
    }

	@Override
	public List<String> getWarehouseCompanyName() throws Exception {
		return warehouseService.getWarehouseCompanyName();
	}

	@Override
	public ExcelDownloadDto getWarehouseExcelDownload(WarehouseRequestDto warehouseRequestDto){
		return warehouseService.getWarehouseExcelDownload(warehouseRequestDto);
	}
}
