package kr.co.pulmuone.v1.goods.stock.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExprRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExprResultVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class GoodsStockExprServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
    private GoodsStockExprService goodsStockExprService;

	@BeforeEach
	void setUp(){
		preLogin();
	}

	@Test
	void 유통기한별_재고_연동_내역_관리_조회_성공() {
		StockExprRequestDto dto = new StockExprRequestDto();

		dto.setIlItemCd("0040102");//품목코드

		Page<StockExprResultVo> stockExprList = goodsStockExprService.getStockExprList(dto);

		stockExprList.stream().forEach(
                i -> log.info(" stockExprList : {}",  i)
        );

		assertTrue(CollectionUtils.isNotEmpty(stockExprList));
	}

	@Test
	void 통합ERP_재고_연동_내역_관리_조회_성공() {
		StockExprRequestDto dto = new StockExprRequestDto();

		dto.setIlItemCd("0040102");//품목코드

		Page<StockExprResultVo> stockErpList = goodsStockExprService.getStockErpList(dto);

		stockErpList.stream().forEach(
                i -> log.info(" stockExprList : {}",  i)
        );

		assertTrue(CollectionUtils.isNotEmpty(stockErpList));
	}

	@Test
	void 재고_미연동_품목리스트_조회_성공() {
		StockExprRequestDto dto = new StockExprRequestDto();

		dto.setIlItemCd("0062273");//품목코드

		Page<StockExprResultVo> stockNonErpList = goodsStockExprService.getStockNonErpList(dto);

		stockNonErpList.stream().forEach(
                i -> log.info(" stockExprList : {}",  i)
        );

		assertTrue(CollectionUtils.isNotEmpty(stockNonErpList));
	}

	@Test
	void 재고_미연동_품목리스트_재고수정_성공() {
		StockExprRequestDto dto = new StockExprRequestDto();

		dto.setPopIlItemWarehouseId(714);//품목별 출고처 관리SEQ
		dto.setPopStockCnt(10);//미연동 재고 수량
		dto.setPopUnlimitStockYn("N");//재고미연동시 재고무제한 사용여부(Y: 재고 무제한)

		int result = goodsStockExprService.putStockNonErp(dto);

		 log.info("재고_미연동_품목리스트_재고수정_성공 result : ",  result);

		 assertTrue(result > 0);
	}

	@Test
	void 유통기한별_재고연동_내역관리_엑셀다운로드_성공() {
		StockExprRequestDto dto = new StockExprRequestDto();

		dto.setIlItemCd("0041360");//품목코드

		ExcelDownloadDto stockExprList = goodsStockExprService.getStockExprExportExcel(dto);

		assertNotNull(stockExprList);
	}

	@Test
	void 통합ERP_재고연동_내역관리_엑셀다운로드_성공() {
		StockExprRequestDto dto = new StockExprRequestDto();

		dto.setIlItemCd("0041360");//품목코드

		ExcelDownloadDto stockErpList = goodsStockExprService.getStockErpExportExcel(dto);

		assertNotNull(stockErpList);
	}

	@Test
	void 재고_미연동_품목리스트_엑셀다운로드_성공() {
		StockExprRequestDto dto = new StockExprRequestDto();

		dto.setIlItemCd("0934397");//품목코드

		ExcelDownloadDto stockErpList = goodsStockExprService.getStockNonErpExportExcel(dto);

		assertNotNull(stockErpList);
	}

}
