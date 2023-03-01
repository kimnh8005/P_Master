package kr.co.pulmuone.v1.goods.etc.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.etc.dto.GoodsNutritionRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.GoodsNutritionResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.GoodsNutritionVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class GoodsNutritionServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	private GoodsNutritionService goodsNutritionService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

	@Test
	void 상품영양정보관리_목록_결과있음()  {

    	// given
		GoodsNutritionRequestDto dto = new GoodsNutritionRequestDto();

        // when
		Page<GoodsNutritionVo> result = goodsNutritionService.getGoodsNutritionList(dto);// rows

    	result.forEach(
                i -> log.info(" result : {}",  i)
        );

    	//then
        assertTrue(CollectionUtils.isNotEmpty(result));
    }

	@Test
	void 상품영양정보관리_상세조회_결과있음() {

		// given
		String ilNutritionCode = "iron";

		// when
		GoodsNutritionVo vo = goodsNutritionService.getGoodsNutrition(ilNutritionCode);
		GoodsNutritionResponseDto result = new GoodsNutritionResponseDto();

    	result.setRows(vo);

        log.info(" 상품영양정보관리_상세조회_결과있음 result : {}",  result);

        // then
        assertEquals("iron", result.getRows().getIlNutritionCode());
    }

	@Test
    void 상품영양정보관리_신규등록() {
    	// given
    	GoodsNutritionRequestDto dto = new GoodsNutritionRequestDto();
        dto.setIlNutritionCode("NU-0031");
        dto.setNutritionName("철12345");
        dto.setNutritionUnit("mg");
        dto.setNutritionPercentYn("Y");
        dto.setSort(40);

        // when
        int result = goodsNutritionService.addGoodsNutrition(dto);

        // then
        assertTrue(result > 0);
    }

	@Test
    void 상품영양정보관리_수정() {
		String ilNutritionCode = "NU-0030";
		String nutritionName = "철1234567";
		String nutritionUnit = "mg";
		String nutritionPercentYn = "N";
		int sort = 45;

    	// given
    	GoodsNutritionRequestDto dto = new GoodsNutritionRequestDto();
        dto.setIlNutritionCode(ilNutritionCode);
        dto.setNutritionName(nutritionName);
        dto.setNutritionUnit(nutritionUnit);
        dto.setNutritionPercentYn(nutritionPercentYn);
        dto.setSort(sort);

    	log.info("dto: {}", dto);

        // when
        int result = goodsNutritionService.putGoodsNutrition(dto);

        log.info(" 상품영양정보관리_수정_성공 result : "+  result);

        // then
        assertTrue(result > 0);
    }

	@Test
    void 상품영양정보관리_삭제() {
		String ilNutritionCode = "NU-0030";

    	// given
    	GoodsNutritionRequestDto dto = new GoodsNutritionRequestDto();
        dto.setIlNutritionCode(ilNutritionCode);

    	log.info("dto: {}", dto);

        // when
        int result =goodsNutritionService.delGoodsNutrition(dto); //goodsNutritionService.duplicateGoodsNutritionByNameCount(goodsNutritionRequestDto)

        log.info(" 상품영양정보관리_삭제_성공 result : "+  result);

        // then
        assertTrue(result > 0);
    }

	@Test
    void 상품영양정보관리_중복_체크() {
    	// given
    	GoodsNutritionRequestDto dto = new GoodsNutritionRequestDto();
        dto.setIlNutritionCode("NU-0030");
        dto.setNutritionName("철1");

        // when
        int result = goodsNutritionService.duplicateGoodsNutritionByNameCount(dto);

        // then
        assertTrue(result > 0);
    }

    @Test
    void addGoodsNutrition() {
	    // given
        GoodsNutritionRequestDto goodsNutritionRequestDto = new GoodsNutritionRequestDto();
        goodsNutritionRequestDto.setIlNutritionCode("CD130");
        goodsNutritionRequestDto.setNutritionName("테스트");
        goodsNutritionRequestDto.setNutritionPercentYn("Y");
        goodsNutritionRequestDto.setNutritionUnit("1");
        goodsNutritionRequestDto.setSort(1);

        // given
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        // when
        int count = goodsNutritionService.addGoodsNutrition(goodsNutritionRequestDto);

        // then
        assertTrue(count > 0);
        SessionUtil.setUserVO(null);
    }

    @Test
    void getGoodsNutritionExcelList() {
	    // given, when
        List<GoodsNutritionVo> result = goodsNutritionService.getGoodsNutritionExcelList();

        // then
        assertTrue(result.size() > 0);
    }

    @Test
    void 상품영양정보_엑셀다운로드() {
    	GoodsNutritionRequestDto goodsNutritionRequestDto = new GoodsNutritionRequestDto();
    	ExcelDownloadDto result = goodsNutritionService.getGoodsNutritionExportExcel(goodsNutritionRequestDto);

        assertNotNull(result);
    }

}
