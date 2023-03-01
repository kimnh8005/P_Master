package kr.co.pulmuone.v1.user.brand.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.brand.dto.AddBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.AddBrandAndLogoMappingParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandListResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandResultVo;

public class DisplayBrandSrviceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
 	DisplayBrandService displayBrandService;

	@Mock
    DisplayBrandBiz displayBrandBiz;

	 @Test
	    void getDisplayBrandList_정상() throws Exception {

		    DisplayBrandListRequestDto dto = new DisplayBrandListRequestDto();

	    	Page<DisplayBrandListResultVo> voList = displayBrandService.getDisplayBrandList(dto);

	    	Assertions.assertTrue(voList.getResult().size() > 0);

	    }


	    @Test
	    void getDisplayBrandList_조회내역없음() throws Exception {

	    	DisplayBrandListRequestDto dto = new DisplayBrandListRequestDto();
	    	dto.setSearchUseYn("K");
	    	Page<DisplayBrandListResultVo> voList = displayBrandService.getDisplayBrandList(dto);

	    	Assertions.assertTrue(voList.getResult().size() == 0);

	    }

	    @Test
	    void searchDisplayBrandList_정상() throws Exception {
	    	DisplayBrandRequestDto dto = new DisplayBrandRequestDto();

	    	List<DisplayBrandListResultVo> voList = displayBrandService.searchDisplayBrandList(dto);

	    	Assertions.assertTrue(voList.size() > 0);
	    }

	    @Test
	    void getDisplayBrand_정상() throws Exception {

	    	DisplayBrandRequestDto dto = new DisplayBrandRequestDto();

	    	dto.setDpBrandId("-1");

	    	DisplayBrandParamVo getParamVo = DisplayBrandParamVo.builder()
	                .dpBrandId(dto.getDpBrandId())
	                .build() ;

	    	DisplayBrandResultVo vo = displayBrandService.getDisplayBrand(getParamVo);

	    	Assertions.assertNull(vo);
	    }

	    @Test
	    void addDisplayBrand_정상() throws Exception {

	    	UserVo userVO = new UserVo();
	        userVO.setUserId("1");
	        userVO.setLoginId("forbiz");
	        userVO.setLangCode("1");
	        SessionUtil.setUserVO(userVO);

	        AddBrandRequestDto dto = new AddBrandRequestDto();

	        dto.setBrandName("TEST DISPLAY BRAND 1207");
	        dto.setUrSupplierId("7");
	        dto.setUseYn("Y");

	        DisplayBrandParamVo addBrandParamVo = DisplayBrandParamVo.builder()
	                .dpBrandName ("TEST")
	                .useYn ("Y")
	                .brandPavilionYn ("Y")
	                .build() ;

	        displayBrandService.addDisplayBrand(addBrandParamVo);

	        FileVo vo1 = new FileVo();
	        vo1.setFieldName("filePcMain");
	        vo1.setOriginalFileName("file1.png");
	        vo1.setPhysicalFileName("1D67BCB47DE04C05A183.png");
	        vo1.setContentType("image/png");
	        vo1.setFileSize(Long.parseLong("9073"));
	        vo1.setServerSubPath("BOS/ur/test/2020/10/29/");
	        vo1.setSaveResult("SUCCESS");

	        AddBrandAndLogoMappingParamVo addMappingParamVo = AddBrandAndLogoMappingParamVo.builder()
					.dpBrandId("1")
					.imageType("BRAND_LOGO_TYPE.PC_MAIN")
					.subPath(vo1.getServerSubPath())
					.physicalName(vo1.getPhysicalFileName())
					.originName(vo1.getOriginalFileName())
					.build();

	        displayBrandService.addDisplayBrandAttachfileMapping(addMappingParamVo);

	    }


	    @Test
	    void addDisplayBrandAttachfileMapping_정상() throws Exception {

	    	UserVo userVO = new UserVo();
	        userVO.setUserId("1");
	        userVO.setLoginId("forbiz");
	        userVO.setLangCode("1");
	        SessionUtil.setUserVO(userVO);

	    	AddBrandAndLogoMappingParamVo addMappingParamVo = AddBrandAndLogoMappingParamVo.builder()
					.dpBrandId("1")
					.imageType("BRAND_LOGO_TYPE.PC_MAIN")
					.subPath("BOS/ur/test/2020/12/07/")
					.physicalName("1D67BCB47DE04C05A183.png")
					.originName("file1.png")
					.build();

    		displayBrandService.addDisplayBrandAttachfileMapping(addMappingParamVo);
	    }

	    @Test
	    void delDisplayBrandLogoMappingInfo_정상() throws Exception {

	    	DisplayBrandParamVo delLogoMappingVo = DisplayBrandParamVo.builder()
					.dpBrandId("1")
					.imageType("BRAND_LOGO_TYPE.PC_MAIN")
					.build();

    		displayBrandService.delDisplayBrandLogoMappingInfo(delLogoMappingVo);
	    }

	    @Test
	    void putDisplayBrand_정상() throws Exception {

	    	UserVo userVO = new UserVo();
	        userVO.setUserId("1");
	        userVO.setLoginId("forbiz");
	        userVO.setLangCode("1");
	        SessionUtil.setUserVO(userVO);

	        DisplayBrandParamVo putParamVo = DisplayBrandParamVo.builder()
                    .dpBrandId   ("-1"  )
                    .dpBrandName   ("TEST DISPLAY NAME")
                    .useYn       ("N")
                    .brandPavilionYn       ("Y")
                    .build() ;

	        displayBrandService.putDisplayBrand(putParamVo);

	        FileVo vo1 = new FileVo();
	        vo1.setFieldName("filePcMain");
	        vo1.setOriginalFileName("file1.png");
	        vo1.setPhysicalFileName("1D67BCB47DE04C05A183.png");
	        vo1.setContentType("image/png");
	        vo1.setFileSize(Long.parseLong("9073"));
	        vo1.setServerSubPath("BOS/ur/test/2020/12/07/");
	        vo1.setSaveResult("SUCCESS");

	        AddBrandAndLogoMappingParamVo putMappingParamVo = AddBrandAndLogoMappingParamVo.builder()
					.dpBrandId("1")
					.imageType("BRAND_LOGO_TYPE.PC_MAIN")
					.subPath(vo1.getServerSubPath())
					.physicalName(vo1.getPhysicalFileName())
					.originName(vo1.getOriginalFileName())
					.build();


	        displayBrandService.addDisplayBrandAttachfileMapping(putMappingParamVo);

	    }
}
