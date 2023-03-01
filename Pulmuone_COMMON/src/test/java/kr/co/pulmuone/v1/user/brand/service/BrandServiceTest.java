package kr.co.pulmuone.v1.user.brand.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.brand.dto.AddBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.PutBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandListResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandResultVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrandServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
 	BrandService brandService;

    @InjectMocks
    private BrandService mockBrandService;

    @Mock
    BrandBiz brandBiz;


    @Test
    void getBrandList_정상() throws Exception {

    	GetBrandListRequestDto dto = new GetBrandListRequestDto();
    	dto.setBrandSearchValue("140");
    	dto.setBrandSearchType("BRAND_CODE");
    	Page<GetBrandListResultVo> voList = brandService.getBrandList(dto);

    	Assertions.assertTrue(voList.getResult().size() > 0);

    }


    @Test
    void getBrandList_조회내역없음() throws Exception {

    	GetBrandListRequestDto dto = new GetBrandListRequestDto();
    	dto.setSearchUrSupplierId("-1");
    	Page<GetBrandListResultVo> voList = brandService.getBrandList(dto);

    	Assertions.assertTrue(voList.getResult().size() == 0);

    }

    @Test
    void searchBrandList_정상() throws Exception {
    	GetBrandRequestDto dto = new GetBrandRequestDto();

    	List<GetBrandListResultVo> voList = brandService.searchBrandList(dto);

    	Assertions.assertTrue(voList.size() > 0);
    }

    @Test
    void getBrand_정상() throws Exception {

    	GetBrandRequestDto dto = new GetBrandRequestDto();

    	dto.setUrBrandId("-1");

    	GetBrandParamVo getParamVo = GetBrandParamVo.builder()
                .urBrandId(dto.getUrBrandId())
                .build() ;

    	GetBrandResultVo vo = brandService.getBrand(getParamVo);

    	Assertions.assertNull(vo);
    }

    @Test
    void addBrand_정상() throws Exception {

    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        AddBrandRequestDto dto = new AddBrandRequestDto();

        dto.setBrandName("TEST BRAND 1029");
        dto.setUrSupplierId("7");
        dto.setUseYn("Y");

    	brandBiz.addBrand(dto);

    }

    @Test
    void putBrand_정상() throws Exception {

    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        PutBrandRequestDto dto = new PutBrandRequestDto();

        dto.setBrandName("TEST BRAND 1029");
        dto.setUrSupplierId("7");
        dto.setUseYn("Y");
        dto.setUrBrandId("-1");

    	brandBiz.putBrand(dto);

    }


}
