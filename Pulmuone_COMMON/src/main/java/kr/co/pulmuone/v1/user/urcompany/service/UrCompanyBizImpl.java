package kr.co.pulmuone.v1.user.urcompany.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.CompanyEnums;
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
import kr.co.pulmuone.v1.user.urcompany.dto.vo.GetClientResultVo;
import kr.co.pulmuone.v1.user.urcompany.dto.vo.GetCompanyListResultVo;
import kr.co.pulmuone.v1.user.urcompany.dto.vo.GetStoreListResultVo;
import kr.co.pulmuone.v1.user.urcompany.dto.vo.GetSupplierListResultVo;
import kr.co.pulmuone.v1.user.urcompany.dto.vo.GetWarehouseListResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UrCompanyBizImpl implements UrCompanyBiz {

	@Autowired
	UrCompanyService urCompanyService;


	@Override
	public ApiResult<?> getCompanyList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception {
		GetCompanyListResponseDto result = new GetCompanyListResponseDto();

		Page<GetCompanyListResultVo> resultVoList = urCompanyService.getCompanyList(getCompanyListRequestDto);

		result.setRows(resultVoList.getResult());
		result.setTotal(resultVoList.getTotal());

		return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> getClient(GetClientRequestDto getClientRequestDto) throws Exception{
		GetClientResponseDto result = new GetClientResponseDto();
		GetClientResultVo vo = new GetClientResultVo();
		if(getClientRequestDto.getClientTp().equals("CLIENT_TYPE.CLIENT")) {
			vo = urCompanyService.getClient(getClientRequestDto);
			vo.setClientSupplierWarehouseList(urCompanyService.getClientSupplierWarehouseInfo(getClientRequestDto));
		}else if(getClientRequestDto.getClientTp().equals("CLIENT_TYPE.SHOP")) {
			vo = urCompanyService.getClientShop(getClientRequestDto);
		}else {
			vo = urCompanyService.getClientVendor(getClientRequestDto);
			vo.setClientSellerList(urCompanyService.getClientSellerInfo(getClientRequestDto));
		}

		result.setRows(vo);

		return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> getSupplierCompanyList(GetSupplierListRequestDto getSupplierListRequestDto) throws Exception{
		GetSupplierListResponseDto result = new GetSupplierListResponseDto();

		List<GetSupplierListResultVo> rows = urCompanyService.getSupplierCompanyList(getSupplierListRequestDto);

		result.setRows(rows);

		return ApiResult.success(result);
	}


	@Override
	public ApiResult<?> getSupplierCompanyListByWhareHouse(GetSupplierListRequestDto getSupplierListRequestDto) throws Exception {
		GetSupplierListResponseDto result = new GetSupplierListResponseDto();

		List<GetSupplierListResultVo> rows = urCompanyService.getSupplierCompanyListByWhareHouse(getSupplierListRequestDto);

		result.setRows(rows);

		return ApiResult.success(result);
	}


	@Override
	public ApiResult<?> getWarehouseList(GetWarehouseListRequestDto getWarehouseListRequestDto) throws Exception{
		GetWarehouseListResponseDto result = new GetWarehouseListResponseDto();

		Page<GetWarehouseListResultVo> resultVoList = urCompanyService.getWarehouseList(getWarehouseListRequestDto);	// rows

		result.setTotal(resultVoList.getTotal());
		result.setRows(resultVoList.getResult());

		return ApiResult.success(result);

	}

	@Override
	public ApiResult<?> getStoreList(GetStoreListRequestDto getStoreListRequestDto) throws Exception {

		GetStoreListResponsetDto result = new GetStoreListResponsetDto();

		List<GetStoreListResultVo> rows = urCompanyService.getStoreList(getStoreListRequestDto);	// rows

		result.setRows(rows);

		return ApiResult.success(result);
	}


	@Override
	public ApiResult<?> addClient(AddClientRequestDto addClientRequestDto) throws Exception{

		if(addClientRequestDto.getInputTpCode().equals("CLIENT_TYPE.CLIENT")) {
			urCompanyService.addCompany(addClientRequestDto);
			urCompanyService.addSupplier(addClientRequestDto);
			urCompanyService.addClient(addClientRequestDto);
			ArrayList<String> warehouseArray = addClientRequestDto.getUrSupplierWarehouseList();
			Iterator<String> warehouseIterator = warehouseArray.iterator();
			while (warehouseIterator.hasNext()) {
				addClientRequestDto.setUrSupplierWarehouse(warehouseIterator.next());
				urCompanyService.addClientSupplierWarehouse(addClientRequestDto);
			}
		}else if(addClientRequestDto.getInputTpCode().equals("CLIENT_TYPE.VENDOR")) {
			ArrayList<String> clientArray = addClientRequestDto.getOmSellsersIdList();
			Iterator<String> clientIterator = clientArray.iterator();
			while (clientIterator.hasNext()) {
				String clientId = clientIterator.next();
				AddClientRequestDto dupDto = new AddClientRequestDto();
				dupDto.setOmSellersId(clientId);
				int dupCount = urCompanyService.getDuplicateClient(dupDto);
				if(dupCount > 0) {
					return ApiResult.result(CompanyEnums.vendorCheckMessage.DUPCHECK_CMT);
				}
			}

			urCompanyService.addCompany(addClientRequestDto);
			Iterator<String> clientPutIterator = clientArray.iterator();
			while (clientPutIterator.hasNext()) {
				addClientRequestDto.setOmSellersId(clientPutIterator.next());
				urCompanyService.addClient(addClientRequestDto);
			}
		}else if(addClientRequestDto.getInputTpCode().equals("CLIENT_TYPE.SHOP")) {

			int dupCount = urCompanyService.getDuplicateShop(addClientRequestDto);
			if(dupCount > 0) {
				return ApiResult.result(CompanyEnums.shopCheckMessage.DUPCHECK_CMT);
			}

			urCompanyService.addCompany(addClientRequestDto);
			urCompanyService.addClient(addClientRequestDto);

		}

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> putClient(PutClientRequestDto putClientRequestDto) throws Exception{

		if(putClientRequestDto.getInputTpCode().equals("CLIENT_TYPE.CLIENT")) {
			urCompanyService.putCompany(putClientRequestDto);
			urCompanyService.delClientSupplierWarehouse(putClientRequestDto);
			urCompanyService.putClient(putClientRequestDto);

			ArrayList<String> warehouseArray = putClientRequestDto.getUrSupplierWarehouseList();
			Iterator<String> warehouseIterator = warehouseArray.iterator();
			while (warehouseIterator.hasNext()) {
				putClientRequestDto.setUrSupplierWarehouse(warehouseIterator.next());
				urCompanyService.putClientSupplierWarehouse(putClientRequestDto);
			}
		}else if(putClientRequestDto.getInputTpCode().equals("CLIENT_TYPE.VENDOR")) {
			ArrayList<String> clientArray = putClientRequestDto.getOmSellsersIdList();
			Iterator<String> clientIterator = clientArray.iterator();
			while (clientIterator.hasNext()) {
				String clientId = clientIterator.next();
				AddClientRequestDto dupDto = new AddClientRequestDto();
				dupDto.setOmSellersId(clientId);
				dupDto.setUrCompanyId(putClientRequestDto.getUrCompanyId());
				int dupCount = urCompanyService.getDuplicateClient(dupDto);
				if(dupCount > 0) {
					return ApiResult.result(CompanyEnums.vendorCheckMessage.DUPCHECK_CMT);
				}
			}

			urCompanyService.putCompany(putClientRequestDto);
			urCompanyService.delClient(putClientRequestDto);
			Iterator<String> clientPutIterator = clientArray.iterator();
			while (clientPutIterator.hasNext()) {
				putClientRequestDto.setOmSellersId(clientPutIterator.next());
				urCompanyService.addClient(putClientRequestDto);
			}

		}else{
			AddClientRequestDto dto = new AddClientRequestDto();
			dto.setStore(putClientRequestDto.getStore());
			int dupCount = urCompanyService.getDuplicateShop(dto);
			if(dupCount > 0) {
				return ApiResult.result(CompanyEnums.shopCheckMessage.DUPCHECK_CMT);
			}

			urCompanyService.putCompany(putClientRequestDto);
			urCompanyService.delClient(putClientRequestDto);
			urCompanyService.addClient(putClientRequestDto);
		}


		return ApiResult.success();
	}


	@Override
	public ApiResult<?> getWarehouseGroupByWarehouseList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception{
		return urCompanyService.getWarehouseGroupByWarehouseList(getCompanyListRequestDto);
	}



	@Override
	public ApiResult<?> getSupplierCompanyByWarehouseList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception{
		return urCompanyService.getSupplierCompanyByWarehouseList(getCompanyListRequestDto);
	}

}
