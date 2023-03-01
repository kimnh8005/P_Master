package kr.co.pulmuone.v1.goods.stock.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.stock.dto.StockListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockListResultVo;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class GoodsStockListServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
    private GoodsStockListService goodsStockListService;

	@BeforeEach
	void setUp(){
		preLogin();
	}

	@Test
	void 품목별_재고리스트_조회_성공() {
		StockListRequestDto dto = new StockListRequestDto();

		dto.setBaseDt("20201201");//기준일

		Page<StockListResultVo> stockList = goodsStockListService.getStockList(dto);

		stockList.stream().forEach(
                i -> log.info(" stockList : {}",  i)
        );

		assertTrue(CollectionUtils.isNotEmpty(stockList));
	}

	@Test
	void 품목별_재고리스트_주문정보_조회_성공() throws Exception {
		StockListRequestDto dto = new StockListRequestDto();
		dto.setBaseDt("2021-02-02");
		dto.setIlItemWarehouseId(24);

		StockListResultVo getStockInfo = goodsStockListService.getStockInfo(dto);

		log.info(" 품목별_재고리스트_주문정보_조회_성공  StockListResultVo : {}",  getStockInfo);

        assertNotNull(getStockInfo);
	}

	@Test
	void 품목별_재고상세정보_목록_조회_성공() throws Exception {
		StockListRequestDto dto = new StockListRequestDto();

		dto.setIlItemWarehouseId(266);//품목별 출고처 관리ID
		dto.setBaseDt("20201201");    //기준일

		Page<StockListResultVo> getStockDetailList = goodsStockListService.getStockDetailList(dto);

		getStockDetailList.stream().forEach(
                i -> log.info(" getStockDetailList : {}",  i)
        );

		assertTrue(CollectionUtils.isNotEmpty(getStockDetailList));
	}

	@Test
    void 품목별_선주문_설정_팝업정보조회() throws Exception {

		StockListRequestDto dto = new StockListRequestDto();

		dto.setIlItemWarehouseId(266);//품목별 출고처 관리ID

		StockListResultVo result = goodsStockListService.getStockPreOrderPopupInfo(dto);

        log.info("품목별_선주문 설정 팝업 정보 조회 result : "+  result.toString());

        assertTrue(result != null);
    }

	@Test
    void 품목별_재고리스트_선주문_여부_수정_성공() throws Exception {

		StockListRequestDto dto = new StockListRequestDto();

		dto.setIlItemWarehouseId(266);//품목별 출고처 관리ID

    	int result = goodsStockListService.putStockPreOrder(dto);

        log.info(" 품목별_재고리스트_선주문_여부_수정_성공 result : "+  result);

        assertTrue(result > 0);
    }

	@Test
	void 출고기준_관리_조회_성공() {
		//given
		StockListRequestDto dto = new StockListRequestDto();
		dto.setIlItemWarehouseId(24);

		//when
		List<StockListResultVo> getStockDeadlineDropDownList = goodsStockListService.getStockDeadlineDropDownList(dto);

		//then
		assertTrue(CollectionUtils.isNotEmpty(getStockDeadlineDropDownList));
	}

	@Test
	void 출고기준관리_수정_성공() throws Exception {
		StockListRequestDto dto = new StockListRequestDto();

		dto.setPopIlStockDeadlineId("1");//출고기준관리ID

		int result = goodsStockListService.putStockDeadlineInfo(dto);

		log.info(" 출고기준관리_수정_성공 result : "+  result);

		assertTrue(result > 0);
	}

	@Test
	void 출고기준관리_기본설정_수정_성공() throws Exception {
		StockListRequestDto dto = new StockListRequestDto();

		dto.setUrSupplierId("1");  //공급처ID
		dto.setUrWarehouseId("85");//출고처ID

		int result = goodsStockListService.putStockDeadlineInfoBasicYn(dto);

		log.info("출고기준관리_기본설정_수정_성공 result : "+  result);

		assertTrue(result > 0);
	}
}
