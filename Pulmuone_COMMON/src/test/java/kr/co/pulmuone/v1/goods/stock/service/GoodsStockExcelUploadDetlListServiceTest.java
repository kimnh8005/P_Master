package kr.co.pulmuone.v1.goods.stock.service;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadDetlListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExcelUploadDetlListResultVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class GoodsStockExcelUploadDetlListServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
    private GoodsStockExcelUploadDetlListService goodsStockExcelUploadDetlListService;

	@Test
	void ERP_재고_엑셀_업로드_상세내역_조회_성공() throws Exception {

		StockExcelUploadDetlListRequestDto dto = new StockExcelUploadDetlListRequestDto();

		dto.setIlStockExcelUploadLogId("0");    //일반조회
        dto.setItemName("[기획] 매콤달콤생쫄면 (4인)");//상품명

		Page<StockExcelUploadDetlListResultVo> stockDetlList = goodsStockExcelUploadDetlListService.getStockUploadDetlList(dto);

		stockDetlList.stream().forEach(
                i -> log.info(" stockDetlList : {}",  i)
        );

		assertTrue(CollectionUtils.isNotEmpty(stockDetlList));
	}

	@Test
	void ERP_재고_엑셀_업로드_상세내역_조회_실패() throws Exception {

		StockExcelUploadDetlListRequestDto dto = new StockExcelUploadDetlListRequestDto();

		dto.setIlStockExcelUploadLogId("0");//일반조회

		Page<StockExcelUploadDetlListResultVo> stockDetlList = goodsStockExcelUploadDetlListService.getStockUploadDetlList(dto);

		stockDetlList.stream().forEach(
                i -> log.info(" stockDetlList : {}",  i)
        );

		assertFalse(CollectionUtils.isEmpty(stockDetlList));
	}

}
