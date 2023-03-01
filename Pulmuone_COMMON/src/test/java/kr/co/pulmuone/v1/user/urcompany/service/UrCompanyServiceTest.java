package kr.co.pulmuone.v1.user.urcompany.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.mapper.user.urcompany.UrCompanyMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.urcompany.dto.*;
import kr.co.pulmuone.v1.user.urcompany.dto.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UrCompanyServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	UrCompanyService urCompanyService;

    @InjectMocks
    private UrCompanyService mockUrCompanyService;

    @Mock
    UrCompanyMapper mockUrCompanyMapper;

    @Test
    void getCompanyList_정상() throws Exception {
    	//given
    	GetCompanyListRequestDto dto = new GetCompanyListRequestDto();

    	//when
    	Page<GetCompanyListResultVo> result = urCompanyService.getCompanyList(dto);

        //then
        Assertions.assertTrue(result.getTotal()> 0);
    }


    @Test
    void getCompanyList_조회내역없음() throws Exception {
    	//given
    	GetCompanyListRequestDto dto = new GetCompanyListRequestDto();
    	dto.setCompanyType("ACCOUNT_SEARCH_TYPE.NAME");
    	dto.setCompanyTypeValue("20201027");

    	//when
    	Page<GetCompanyListResultVo> result = urCompanyService.getCompanyList(dto);

        //then
    	assertFalse( CollectionUtils.isNotEmpty(result.getResult()) );
    }


    @Test
    void getClient_정상() throws Exception {
    	//given
    	GetClientRequestDto dto = new GetClientRequestDto();
    	dto.setUrClientId("-1");
    	//when
    	GetClientResultVo vo = urCompanyService.getClient(dto);

        //then
    	//Assertions.assertEquals(result.getCompanyType(), "COMPANY_TYPE.CLIENT");

    	Assertions.assertNull(vo);
    }


    @Test
    void getClientSupplierWarehouseInfo_정상() throws Exception {
    	GetClientRequestDto dto = new GetClientRequestDto();
    	dto.setUrClientId("10");
    	List<GetClientResultVo> result = urCompanyService.getClientSupplierWarehouseInfo(dto);

    	Assertions.assertTrue(result.size()> 0);

    }


    @Test
    void getClientShop_정상() throws Exception {
    	GetClientRequestDto dto = new GetClientRequestDto();
    	dto.setUrClientId("-1");
    	GetClientResultVo vo = urCompanyService.getClientShop(dto);

    	//Assertions.assertEquals(result.getCompanyType(),"CLIENT_TYPE.SHOP");
    	Assertions.assertNull(vo);
    }


    @Test
    void getClientVendor_정상() throws Exception {
    	GetClientRequestDto dto = new GetClientRequestDto();
    	dto.setUrCompanyId("-1");
    	GetClientResultVo vo = urCompanyService.getClientVendor(dto);

    	//Assertions.assertEquals(result.getCompanyType(),"COMPANY_TYPE.CLIENT");

    	Assertions.assertNull(vo);

    }

    @Test
    void getSupplierCompanyList_정상() throws Exception {

    	GetSupplierListRequestDto dto = new GetSupplierListRequestDto();
    	List<GetSupplierListResultVo> result = urCompanyService.getSupplierCompanyList(dto);

    	Assertions.assertTrue(result.size()> 0);

    }

    @Test
    void getWarehouseList_정상() throws Exception {

    	GetWarehouseListRequestDto dto = new GetWarehouseListRequestDto();

    	Page<GetWarehouseListResultVo> result = urCompanyService.getWarehouseList(dto);

    	Assertions.assertTrue(result.getTotal()> 0);

    }

    @Test
    void getStoreList_정상() throws Exception {
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        List<String> listAuthStoreId = new ArrayList<>();
        listAuthStoreId.add("O01004");
        userVO.setListAuthStoreId(listAuthStoreId);

        SessionUtil.setUserVO(userVO);

    	GetStoreListRequestDto dto = new GetStoreListRequestDto();
    	List<GetStoreListResultVo> result = urCompanyService.getStoreList(dto);

    	Assertions.assertTrue(result.size()> 0);

    }

    @Test
    void addClient_정상() throws Exception {
        //given
    	AddClientRequestDto dto = new AddClientRequestDto();
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
    	dto.setCompanyName("TEST 20210227");
    	dto.setOmSellersId("1");
    	dto.setUrCompanyId("-1");
    	dto.setInputTpCode("CLIENT_TYPE.VENDOR");

        //when, then
        urCompanyService.addClient(dto);
    }

    @Test
    void addClient_Put_정상() throws Exception {
        //given
    	PutClientRequestDto dto = new PutClientRequestDto();
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
    	dto.setCompanyName("TEST 20210227");
    	dto.setOmSellersId("1");
    	dto.setUrCompanyId("-1");
    	dto.setInputTpCode("CLIENT_TYPE.VENDOR");

        //when, then
        urCompanyService.addClient(dto);
    }


    @Test
    void addCompany_정상() throws Exception {
        //given
    	AddClientRequestDto dto = new AddClientRequestDto();
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
    	dto.setCompanyName("TEST 20201027");
    	dto.setInputTpCode("CLIENT_TYPE.CLIENT");
    	dto.setAccountMail("test@mail.com");
    	dto.setAccountMemo("MEMO TEST");
    	dto.setInputUseYn("Y");
    	dto.setOrderAlarmRevYn("Y");
    	dto.setDeliveryStatChgYn("Y");

        //when, then
        urCompanyService.addCompany(dto);
    }



    @Test
    void addSupplier_정상() throws Exception {
        //given
    	AddClientRequestDto dto = new AddClientRequestDto();
    	dto.setUrCompanyId("-1");

        //when, then
        urCompanyService.addSupplier(dto);
    }




    @Test
    void addClientSupplierWarehouse_정상() throws Exception {
        //given
    	AddClientRequestDto dto = new AddClientRequestDto();
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

    	dto.setUrSupplierWarehouse("1");
    	dto.setUrClientId("1");

        //when, then
        urCompanyService.addClientSupplierWarehouse(dto);
    }



    @Test
    void putCompany_정상() throws Exception {
        //given
    	PutClientRequestDto dto = new PutClientRequestDto();
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
    	dto.setCompanyName("TEST 20201027");
    	dto.setInputTpCode("CLIENT_TYPE.CLIENT");
    	dto.setAccountMail("test@mail.com");
    	dto.setAccountMemo("MEMO TEST");
    	dto.setInputUseYn("Y");

        //when, then
        urCompanyService.putCompany(dto);
    }



    @Test
    void delClientSupplierWarehouse_정상() throws Exception {
        //given
    	PutClientRequestDto dto = new PutClientRequestDto();

    	dto.setUrClientId("1");

        //when, then
        urCompanyService.delClientSupplierWarehouse(dto);
    }



    @Test
    void putClient_정상() throws Exception {
        //given
    	PutClientRequestDto dto = new PutClientRequestDto();
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
    	dto.setCompanyName("TEST 20210227");
    	dto.setOmSellersId("1");
    	dto.setInputTpCode("CLIENT_TYPE.VENDOR");

        //when, then
        urCompanyService.putClient(dto);
    }



    @Test
    void putClientSupplierWarehouse_정상() throws Exception {
        //given
    	PutClientRequestDto dto = new PutClientRequestDto();
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

    	dto.setUrSupplierWarehouse("1");
    	dto.setUrClientId("1");

        //when, then
        urCompanyService.putClientSupplierWarehouse(dto);
    }


    @Test
    void getWarehouseGroupByWarehouseList_정상() throws Exception {

    	GetCompanyListRequestDto dto = new GetCompanyListRequestDto();
    	ApiResult result = urCompanyService.getWarehouseGroupByWarehouseList(dto);

        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());

    }


    @Test
    void getDuplicateShop_정상() throws Exception {

        AddClientRequestDto dto = new AddClientRequestDto();
        dto.setStore("O01006");
        int result = urCompanyService.getDuplicateShop(dto);

        Assertions.assertTrue(result > 0);

    }

}
