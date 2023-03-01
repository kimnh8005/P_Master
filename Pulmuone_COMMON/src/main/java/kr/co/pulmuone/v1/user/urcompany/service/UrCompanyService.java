package kr.co.pulmuone.v1.user.urcompany.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.base.dto.WarehouseResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.WarehouseVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.mapper.user.urcompany.UrCompanyMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.urcompany.dto.*;
import kr.co.pulmuone.v1.user.urcompany.dto.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UrCompanyService {

	private final UrCompanyMapper urCompanyMapper;

    /**
     * 거래처 목록 조회
     * @param getCompanyListRequestDto
     * @return
     * @throws Exception
     */
    protected Page<GetCompanyListResultVo> getCompanyList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception {
    	PageMethod.startPage(getCompanyListRequestDto.getPage(), getCompanyListRequestDto.getPageSize());
        return urCompanyMapper.getCompanyList(getCompanyListRequestDto);
    }


    /**
     * 거래처 상세조회
     * @param getClientRequestDto
     * @return
     * @throws Exception
     */
    protected GetClientResultVo getClient(GetClientRequestDto getClientRequestDto)throws Exception {
    	return urCompanyMapper.getClient(getClientRequestDto);

    }

    protected List<GetClientResultVo> getClientSupplierWarehouseInfo(GetClientRequestDto getClientRequestDto)throws Exception {
    	return urCompanyMapper.getClientSupplierWarehouseInfo(getClientRequestDto);

    }

    protected List<GetClientResultVo> getClientSellerInfo(GetClientRequestDto getClientRequestDto)throws Exception {
    	return urCompanyMapper.getClientSellerInfo(getClientRequestDto);

    }



    protected GetClientResultVo getClientShop(GetClientRequestDto getClientRequestDto)throws Exception {
    	return urCompanyMapper.getClientShop(getClientRequestDto);
    }

    protected GetClientResultVo getClientVendor(GetClientRequestDto getClientRequestDto)throws Exception {
    	return urCompanyMapper.getClientVendor(getClientRequestDto);
    }

    /**
     * 공급업체 조회
     * @param getSupplierListRequestDto
     * @return
     * @throws Exception
     */
    protected List<GetSupplierListResultVo> getSupplierCompanyList(GetSupplierListRequestDto getSupplierListRequestDto) throws Exception{
    	return urCompanyMapper.getSupplierCompanyList(getSupplierListRequestDto);
    }


    /**
     * 출고처 별 공급업체 조회
     * @param getSupplierListRequestDto
     * @return
     * @throws Exception
     */
    protected List<GetSupplierListResultVo> getSupplierCompanyListByWhareHouse(GetSupplierListRequestDto getSupplierListRequestDto) throws Exception{
        return urCompanyMapper.getSupplierCompanyListByWhareHouse(getSupplierListRequestDto);
    }


    /**
     * 출고처 검색 팝업
     * @param getWarehouseListRequestDto
     * @return
     * @throws Exception
     */
    protected Page<GetWarehouseListResultVo> getWarehouseList(GetWarehouseListRequestDto getWarehouseListRequestDto) throws Exception {
    	PageMethod.startPage(getWarehouseListRequestDto.getPage(), getWarehouseListRequestDto.getPageSize());
        return urCompanyMapper.getWarehouseList(getWarehouseListRequestDto);
    }

	/**
	 * 매장 리스트 조회
	 * @param getStoreListRequestDto
	 * @return
	 * @throws Exception
	 */
    protected List<GetStoreListResultVo> getStoreList(GetStoreListRequestDto getStoreListRequestDto) throws Exception {
        UserVo userVo = SessionUtil.getBosUserVO();

        List<String> listAuthStoreId = userVo.getListAuthStoreId();
        listAuthStoreId.removeIf(s->StringUtils.isEmpty(s));

    	getStoreListRequestDto.setListAuthStoreId(listAuthStoreId);
    	return urCompanyMapper.getStoreList(getStoreListRequestDto);
    }


    /**
     * @Desc 회사정보 등록
     * @param addClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int addCompany(AddClientRequestDto addClientRequestDto) throws Exception{
        return urCompanyMapper.addCompany(addClientRequestDto);
    }

    /**
     * @Desc 공급처 등록
     * @param addClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int addSupplier(AddClientRequestDto addClientRequestDto) throws Exception{
        return urCompanyMapper.addSupplier(addClientRequestDto);
    }

    /**
     * @Desc 거래처 등록
     * @param addClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int addClient(AddClientRequestDto addClientRequestDto) throws Exception{
        return urCompanyMapper.addClient(addClientRequestDto);
    }



    /**
     * @Desc 거래처_공급처_출고처정보 등록
     * @param addClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int addClientSupplierWarehouse(AddClientRequestDto addClientRequestDto) throws Exception{
        return urCompanyMapper.addClientSupplierWarehouse(addClientRequestDto);
    }


    /**
     * @Desc 회사정보 수정
     * @param putClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int putCompany(PutClientRequestDto putClientRequestDto) throws Exception{
        return urCompanyMapper.putCompany(putClientRequestDto);
    }

    /**
     * @Desc 거래처_공급처 정보 삭제
     * @param putClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int delClientSupplierWarehouse(PutClientRequestDto putClientRequestDto) throws Exception{
        return urCompanyMapper.delClientSupplierWarehouse(putClientRequestDto);
    }


    /**
     * @Desc 거래처 정보 수정
     * @param putClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int putClient(PutClientRequestDto putClientRequestDto) throws Exception{
        return urCompanyMapper.putClient(putClientRequestDto);
    }


    /**
     * @Desc 거래처_공급처 정보 수정
     * @param putClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int putClientSupplierWarehouse(PutClientRequestDto putClientRequestDto) throws Exception{
        return urCompanyMapper.putClientSupplierWarehouse(putClientRequestDto);
    }

    /**
     * @Desc 거래처 등록
     * @param putClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int addClient(PutClientRequestDto putClientRequestDto) throws Exception{
        return urCompanyMapper.addClient(putClientRequestDto);
    }



    /**
     * @Desc 거래처 정보 삭제
     * @param putClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int delClient(PutClientRequestDto putClientRequestDto) throws Exception{
        return urCompanyMapper.delClient(putClientRequestDto);
    }



    /**
     * @Desc 중복벤더 체크
     * @param addClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int getDuplicateClient(AddClientRequestDto addClientRequestDto) throws Exception{
        return urCompanyMapper.getDuplicateClient(addClientRequestDto);
    }



    /**
	 * 출고처PK기준 공급업체 조회
	 *
	 * @param getCompanyListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
    protected ApiResult<?> getWarehouseGroupByWarehouseList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception {
    	WarehouseResponseDto result = new WarehouseResponseDto();

        List<WarehouseVo> rows = urCompanyMapper.getWarehouseGroupByWarehouseList(getCompanyListRequestDto);

        result.setRows(rows);

        return ApiResult.success(result);
    }


    /**
	 * 출고처PK기준 출고처그룹 조회
	 *
	 * @param getCompanyListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
    protected ApiResult<?> getSupplierCompanyByWarehouseList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception {
    	WarehouseResponseDto result = new WarehouseResponseDto();

        List<WarehouseVo> rows = urCompanyMapper.getSupplierCompanyByWarehouseList(getCompanyListRequestDto);

        result.setRows(rows);

        return ApiResult.success(result);
    }


    /**
     * @Desc 중복매장 체크
     * @param addClientRequestDto
     * @throws Exception
     * @return int
     */
    protected int getDuplicateShop(AddClientRequestDto addClientRequestDto) throws Exception{
        return urCompanyMapper.getDuplicateShop(addClientRequestDto);
    }


}
