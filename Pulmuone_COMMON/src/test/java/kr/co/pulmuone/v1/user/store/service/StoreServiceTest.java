package kr.co.pulmuone.v1.user.store.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.store.dto.StoreDeliveryAreaListRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDetailRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreListRequestDto;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreDeliveryAreaVo;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreDetailVo;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreListVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StoreServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
 	StoreService storeService;

    @InjectMocks
    private StoreService mockStoreService;

    @Mock
    StoreBiz storeBiz;

    @Test
    void getShopDeliveryAreaList_매장배송권역관리리스트() throws Exception {

    	StoreDeliveryAreaListRequestDto dto = new StoreDeliveryAreaListRequestDto();
    	dto.setStoreType("STORE_TYPE.DIRECT");
    	dto.setSearchZip("06551");
    	dto.setSearchType("ZIP_CD");
    	Page<StoreDeliveryAreaVo> voList = storeService.getStoreDeliveryAreaList(dto);

    	Assertions.assertTrue(voList.getResult().size() > 0);

    }


    @Test
    void getShopDeliveryAreaList_가맹점배송권역관리리스트() throws Exception {

    	StoreDeliveryAreaListRequestDto dto = new StoreDeliveryAreaListRequestDto();
    	dto.setStoreType("STORE_TYPE.BRANCH");
		dto.setSearchZip("61015");
		dto.setSearchType("ZIP_CD");
    	Page<StoreDeliveryAreaVo> voList = storeService.getStoreDeliveryAreaList(dto);

    	Assertions.assertTrue(voList.getResult().size() > 0);

    }

    @Test
    void getStoreList_매장목록리스트() throws Exception {

    	StoreListRequestDto dto = new StoreListRequestDto();
    	dto.setSearchValue("O05005");
    	dto.setSearchType("STORE_ID");
    	Page<StoreListVo> voList = storeService.getStoreList(dto);

    	Assertions.assertTrue(voList.getResult().size() > 0);

    }


    @Test
    void getStoreDetail_정상() throws Exception {
    	//given
    	StoreDetailRequestDto dto = new StoreDetailRequestDto();
    	dto.setUrStoreId("-1");
    	//when
    	StoreDetailVo vo = storeService.getStoreDetail(dto);

    	Assertions.assertNull(vo);
    }

    @Test
    void getStoreDeliveryList_정상() throws Exception {
    	StoreDetailRequestDto dto = new StoreDetailRequestDto();
    	dto.setUrStoreId("-1");
    	List<StoreDeliveryAreaVo> result = storeService.getStoreDeliveryList(dto);

    	Assertions.assertFalse(result.size()> 0);

    }


    @Test
    void modifyStoreDetail_정상() throws Exception {
        //given
    	StoreDetailRequestDto dto = new StoreDetailRequestDto();
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

    	dto.setUseYn("Y");
    	dto.setUrStoreId("-1");
    	
    	List<UploadFileDto> filePcDtoList = new ArrayList<>();
    	UploadFileDto fileDto = new UploadFileDto();
    	fileDto.setServerSubPath("PC path");
    	fileDto.setPhysicalFileName("PC fileName");
    	fileDto.setOriginalFileName("PC originalName");
    	
    	filePcDtoList.add(fileDto);
    	
    	dto.setStorePcImageUploadResultList(filePcDtoList);
    	
    	List<UploadFileDto> fileMobileDtoList = new ArrayList<>();
    	UploadFileDto fileMobileDto = new UploadFileDto();
    	fileMobileDto.setServerSubPath("Mobile path");
    	fileMobileDto.setPhysicalFileName("Mobile fileName");
    	fileMobileDto.setOriginalFileName("Mobile originalName");
    	
    	fileMobileDtoList.add(fileDto);
    	
    	dto.setStoreMobileImageUploadResultList(fileMobileDtoList);
    	
    	

        //when, then
    	storeService.modifyStoreDetail(dto);
    }


}
