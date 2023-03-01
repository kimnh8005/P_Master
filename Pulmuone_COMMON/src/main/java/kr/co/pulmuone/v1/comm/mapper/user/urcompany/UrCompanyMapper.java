package kr.co.pulmuone.v1.comm.mapper.user.urcompany;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.dto.vo.WarehouseVo;
import kr.co.pulmuone.v1.user.urcompany.dto.*;
import kr.co.pulmuone.v1.user.urcompany.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UrCompanyMapper {

	Page<GetCompanyListResultVo> getCompanyList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception;

	GetClientResultVo getClient(GetClientRequestDto getClientRequestDto);

	List<GetClientResultVo> getClientSupplierWarehouseInfo(GetClientRequestDto getClientRequestDto);

	List<GetClientResultVo> getClientSellerInfo(GetClientRequestDto getClientRequestDto);

	GetClientResultVo getClientShop(GetClientRequestDto getClientRequestDto);

	GetClientResultVo getClientVendor(GetClientRequestDto getClientRequestDto);

	List<GetSupplierListResultVo> getSupplierCompanyList(GetSupplierListRequestDto getSupplierListRequestDto);

	List<GetSupplierListResultVo> getSupplierCompanyListByWhareHouse(GetSupplierListRequestDto getSupplierListRequestDto);

	Page<GetWarehouseListResultVo> getWarehouseList(GetWarehouseListRequestDto getWarehouseListRequestDto) throws Exception;

	List<GetStoreListResultVo> getStoreList(GetStoreListRequestDto getStoreListRequestDto);

	int addCompany(AddClientRequestDto addClientRequestDto) throws Exception;

	int addSupplier(AddClientRequestDto addClientRequestDto) throws Exception;

	int addClient(AddClientRequestDto addClientRequestDto) throws Exception;

	int addClient(PutClientRequestDto putClientRequestDto) throws Exception;

	int addClientSupplierWarehouse(AddClientRequestDto addClientRequestDto) throws Exception;

	int putCompany(PutClientRequestDto putClientRequestDto) throws Exception;
	int delClientSupplierWarehouse(PutClientRequestDto putClientRequestDto) throws Exception;
	int putClient(PutClientRequestDto putClientRequestDto) throws Exception;
	int putClientSupplierWarehouse(PutClientRequestDto putClientRequestDto) throws Exception;

	int delClient(PutClientRequestDto putClientRequestDto) throws Exception;

	int getDuplicateClient(AddClientRequestDto addClientRequestDto) throws Exception;

	int getDuplicateShop(AddClientRequestDto addClientRequestDto) throws Exception;

	List<WarehouseVo> getWarehouseGroupByWarehouseList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception;

	List<WarehouseVo> getSupplierCompanyByWarehouseList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception;
}
