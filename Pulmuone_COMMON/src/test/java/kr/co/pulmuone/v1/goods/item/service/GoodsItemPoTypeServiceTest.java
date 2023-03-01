package kr.co.pulmuone.v1.goods.item.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoTypeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class GoodsItemPoTypeServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
    private GoodsItemPoTypeService goodsItemPoTypeService;

	@BeforeEach
	void setUp(){
		preLogin();
	}

	@Test
    void 발주유형관리목록_결과있음()  {

    	// given
    	ItemPoTypeListRequestDto dto = new ItemPoTypeListRequestDto();

        // when
    	Page<ItemPoTypeVo> result = goodsItemPoTypeService.getItemPoTypeList(dto);

    	result.stream().forEach(
                i -> log.info(" result : {}",  i)
        );

    	//then
        assertTrue(CollectionUtils.isNotEmpty(result));
    }

	@Test
	void 발주유형관리_상세조회_결과있음() {

		// given
		String ilPoTpId ="12106";

		// when
    	ItemPoTypeVo itemPoTypeVo = goodsItemPoTypeService.getItemPoType(ilPoTpId);

    	ItemPoTypeResponseDto dto = new ItemPoTypeResponseDto();
    	dto.setRows(itemPoTypeVo);

        log.info(" 발주유형목록_상세조회_결과있음 itemPoTypeVo : {}",  itemPoTypeVo);

        // then
        assertEquals(ilPoTpId, dto.getRows().getIlPoTpId());
    }

	@Test
    void 발주유형관리_신규등록() {
		String ilPoTpId = "34";
    	String urSupplierId = "1";
    	String poType = "PO_TYPE.MOVING";
    	Integer stockPlannedDays = 1;
    	String erpPoType = "ERP_PO_TP.PRODUCTION";
    	String poSunYn = "Y";
    	String poDeadlineHour = "01";
    	String poDeadlineMin = "01";

    	 // given
    	ItemPoTypeRequestDto dto = new ItemPoTypeRequestDto();
        dto.setIlPoTpId(ilPoTpId);
        dto.setUrSupplierId(urSupplierId);
        dto.setPoTp(poType);
        dto.setErpPoTp(erpPoType);
        dto.setCheckSun(poSunYn);
        dto.setCheckMon("N");
		dto.setCheckTue("N");
		dto.setCheckWed("N");
		dto.setCheckThu("N");
		dto.setCheckFri("N");
		dto.setCheckSat("N");
        dto.setPoDeadlineHour(poDeadlineHour);
        dto.setPoDeadlineMin(poDeadlineMin);
        dto.setPoTpNm("test");
        dto.setPoPerItemYn("N");

    	log.info("dto: {}", dto);

        // when
        int result = goodsItemPoTypeService.addItemPoType(dto);

        log.info(" 발주유형관리_등록_성공 result : "+  result);

        // then
        assertTrue(result > 0);
    }

	@Test
    void 발주유형관리_수정() {

		String ilPoTpId = "12106";
    	String urSupplierId = "1";
    	String poType = "PO_TYPE.MOVING";
    	Integer stockPlannedDays = 2;
    	String erpPoType = "ERP_PO_TP.PRODUCTION";
    	String poSunYn = "Y";
    	String poDeadlineHour = "02";
    	String poDeadlineMin = "02";

    	// given
    	ItemPoTypeRequestDto dto = new ItemPoTypeRequestDto();
        dto.setIlPoTpId(ilPoTpId);
        dto.setUrSupplierId(urSupplierId);
        dto.setPoTp(poType);
        dto.setErpPoTp(erpPoType);
        dto.setPoTpNm("test");
        dto.setCheckSun("N");
        dto.setCheckMon("N");
        dto.setCheckTue("N");
        dto.setCheckWed("N");
        dto.setCheckThu("N");
        dto.setCheckFri("N");
        dto.setCheckSat("N");
        dto.setPoPerItemYn("N");
        dto.setPoDeadlineHour("1");
        dto.setPoDeadlineMin("1");

    	log.info("dto: {}", dto);

    	// when
        int count = goodsItemPoTypeService.putItemPoType(dto);

        // then
        assertTrue(count > 0);
    }

	@Test
    void 발주유형관리_등록_중복_체크() {

		String ilPoTpId = "33";
    	String poTypeName = "품목별상이(0915553)";

    	// given
    	ItemPoTypeVo dto = new ItemPoTypeVo();
        dto.setIlPoTpId(ilPoTpId);
        dto.setPoTpNm(poTypeName);

    	log.info("dto: {}", dto);

    	// when
        int count = goodsItemPoTypeService.duplicateItemPoTypeCount(dto);

        // then
        assertTrue(count > 0);
    }

}
