package kr.co.pulmuone.v1.goods.stock.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.StockEnums;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineHistParamDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockDeadlineHistResultVo;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockDeadlineResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class GoodsStockDeadlineServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private GoodsStockDeadlineService stockDeadlineService;

    @Test
    void 재고기한관리_목록_조회_성공() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	Page<StockDeadlineResultVo> stockDeadlineResultVo = stockDeadlineService.getStockDeadlineList(stockDeadlineRequestDto);

    	stockDeadlineResultVo.stream().forEach(
                i -> log.info(" stockDeadlineResultVo : {}",  i)
        );

        assertTrue(CollectionUtils.isNotEmpty(stockDeadlineResultVo));
    }


    @Test
    void 재고기한관리_목록_조회_실패() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	Page<StockDeadlineResultVo> stockDeadlineResultVo = stockDeadlineService.getStockDeadlineList(stockDeadlineRequestDto);

    	stockDeadlineResultVo.stream().forEach(
                i -> log.info(" stockDeadlineResultVo : {}",  i)
        );

    	assertFalse(CollectionUtils.isEmpty(stockDeadlineResultVo));
    }


    @Test
    void 재고기한관리_데이터_확인_성공() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	int count = stockDeadlineService.getStockDeadlineForCheck(stockDeadlineRequestDto);

        log.info("재고기한관리_데이터_확인_성공 count : ",  count);

        assertTrue(count == 0);
    }



    @Test
    void 재고기한관리_데이터_확인_실패() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setUrSupplierId(1);
    	stockDeadlineRequestDto.setUrWarehouseId("0");
    	stockDeadlineRequestDto.setDistributionPeriod("1");

    	int count = stockDeadlineService.getStockDeadlineForCheck(stockDeadlineRequestDto);

        log.info(" 재고기한관리_데이터_확인_실패 count : ",  count);

		assertEquals(0, count);
    }


    @Test
    void 유통기간에의한재고기한관리수량_데이터_확인() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setUrSupplierId(1);
    	stockDeadlineRequestDto.setUrWarehouseId("0");
    	stockDeadlineRequestDto.setDistributionPeriod("1");
    	HashMap countMap = stockDeadlineService.getStockDeadlineCheckCountByPeriod(stockDeadlineRequestDto);

    	int totalCount = Integer.parseInt(countMap.get("totalCount").toString());
		int totalYesCount = Integer.parseInt(countMap.get("totalYesCount").toString());
        log.info("재고기한관리_데이터_확인_성공 totalCount : ",  totalCount);
        log.info("재고기한관리_데이터_확인_성공 totalYesCount : ",  totalYesCount);

        assertTrue(totalCount >= 0);
        assertTrue(totalYesCount >= 0);
    }


    @Test
    void 재고기한관리_상세조회_성공() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setIlStockDeadlineId(166);

    	StockDeadlineResultVo stockDeadlineResultVo = stockDeadlineService.getStockDeadline(stockDeadlineRequestDto);

        log.info(" 재고기한관리_상세조회_성공 stockDeadlineResultVo : {}",  stockDeadlineResultVo);

        assertEquals(166, stockDeadlineResultVo.getIlStockDeadlineId());
    }


    @Test
    void 재고기한관리_상세조회_실패() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setIlStockDeadlineId(999999999);

    	StockDeadlineResultVo stockDeadlineResultVo = stockDeadlineService.getStockDeadline(stockDeadlineRequestDto);

        log.info(" 재고기한관리_상세조회_실패 stockDeadlineResultVo : {}",  stockDeadlineResultVo);

        assertFalse(stockDeadlineResultVo != null);
    }



    @Test
    void 재고기한관리_등록처리_성공() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setUrSupplierId(1);
    	stockDeadlineRequestDto.setUrWarehouseId("0");
    	stockDeadlineRequestDto.setDistributionPeriod("2");
    	stockDeadlineRequestDto.setImminent(1);
    	stockDeadlineRequestDto.setDelivery(1);
    	stockDeadlineRequestDto.setTargetStockRatio(1);
		stockDeadlineRequestDto.setPopBasicYn("N");

    	int result = stockDeadlineService.addStockDeadline(stockDeadlineRequestDto);

        log.info(" 재고기한관리_등록처리_성공 result : "+  result);

        assertTrue(result > 0);
    }



    @Test
    void 재고기한관리_등록처리_실패() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setUrSupplierId(1);
    	stockDeadlineRequestDto.setUrWarehouseId("0");
    	stockDeadlineRequestDto.setDistributionPeriod("1");
    	stockDeadlineRequestDto.setImminent(1);
    	stockDeadlineRequestDto.setDelivery(1);
    	stockDeadlineRequestDto.setTargetStockRatio(1);

        // when, then
        assertThrows(Exception.class, () -> {
        	stockDeadlineService.addStockDeadline(stockDeadlineRequestDto);
        });
    }



    @Test
    void 재고기한관리_이력저장_성공() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setUrSupplierId(1);
    	stockDeadlineRequestDto.setUrWarehouseId("0");
    	stockDeadlineRequestDto.setDistributionPeriod("2");
    	stockDeadlineRequestDto.setImminent(1);
    	stockDeadlineRequestDto.setDelivery(1);
    	stockDeadlineRequestDto.setTargetStockRatio(1);
    	stockDeadlineRequestDto.setPopBasicYn("N");

    	stockDeadlineService.addStockDeadline(stockDeadlineRequestDto);

    	StockDeadlineHistParamDto stockDeadlineHistParamDto = StockDeadlineHistParamDto.builder()
				 .histTp(StockEnums.HistTpCode.INSERT.getCode())
				 .ilStockDeadlineId(stockDeadlineRequestDto.getIlStockDeadlineId())
				 .urSupplierId(stockDeadlineRequestDto.getUrSupplierId())
				 .urWarehouseId(stockDeadlineRequestDto.getUrWarehouseId())
				 .distributionPeriod(stockDeadlineRequestDto.getDistributionPeriod())
				 .imminent(stockDeadlineRequestDto.getImminent())
				 .delivery(stockDeadlineRequestDto.getDelivery())
				 .targetStockRatio(stockDeadlineRequestDto.getTargetStockRatio())
				 .origCreateId(stockDeadlineRequestDto.getOrigCreateId())
				 .origCreateDate(stockDeadlineRequestDto.getOrigCreateDate())
				 .origModifyId(stockDeadlineRequestDto.getOrigModifyId())
				 .origModifyDate(stockDeadlineRequestDto.getOrigModifyDate())
				 .createId(1)
	    		 .build();
    	int result = stockDeadlineService.addStockDeadlineHist(stockDeadlineHistParamDto);

        log.info(" 재고기한관리_이력저장_성공 result : "+  result);

        assertTrue(result > 0);
    }



    @Test
    void 재고기한관리_이력저장_실패() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setUrSupplierId(1);
    	stockDeadlineRequestDto.setUrWarehouseId("0");
    	stockDeadlineRequestDto.setDistributionPeriod("2");
    	stockDeadlineRequestDto.setImminent(1);
    	stockDeadlineRequestDto.setDelivery(1);
    	stockDeadlineRequestDto.setTargetStockRatio(1);
    	stockDeadlineRequestDto.setIlStockDeadlineId(1);

    	StockDeadlineHistParamDto stockDeadlineHistParamDto = StockDeadlineHistParamDto.builder()
				 .ilStockDeadlineId(stockDeadlineRequestDto.getIlStockDeadlineId())
				 .urSupplierId(stockDeadlineRequestDto.getUrSupplierId())
				 .urWarehouseId(stockDeadlineRequestDto.getUrWarehouseId())
				 .distributionPeriod(stockDeadlineRequestDto.getDistributionPeriod())
				 .imminent(stockDeadlineRequestDto.getImminent())
				 .delivery(stockDeadlineRequestDto.getDelivery())
				 .targetStockRatio(stockDeadlineRequestDto.getTargetStockRatio())
				 .origCreateId(stockDeadlineRequestDto.getOrigCreateId())
				 .origCreateDate(stockDeadlineRequestDto.getOrigCreateDate())
				 .origModifyId(stockDeadlineRequestDto.getOrigModifyId())
				 .origModifyDate(stockDeadlineRequestDto.getOrigModifyDate())
				 .createId(1)
	    		 .build();

        // when, then
        assertThrows(Exception.class, () -> {
        	stockDeadlineService.addStockDeadlineHist(stockDeadlineHistParamDto);
        });
    }



    @Test
    void 재고기한관리_수정처리_성공() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setUrSupplierId(1);
    	stockDeadlineRequestDto.setUrWarehouseId("0");
    	stockDeadlineRequestDto.setDistributionPeriod("1");
    	stockDeadlineRequestDto.setImminent(11);
    	stockDeadlineRequestDto.setDelivery(11);
    	stockDeadlineRequestDto.setTargetStockRatio(11);
    	stockDeadlineRequestDto.setModifyId(11);
    	stockDeadlineRequestDto.setIlStockDeadlineId(1);
		stockDeadlineRequestDto.setPopBasicYn("N");

    	int result = stockDeadlineService.putStockDeadline(stockDeadlineRequestDto);

        log.info(" 재고기한관리_수정처리_성공 result : "+  result);

        assertTrue(result > 0);
    }



    @Test
    void 재고기한관리_수정처리_실패() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setUrSupplierId(999999999);
    	stockDeadlineRequestDto.setUrWarehouseId("0");
    	stockDeadlineRequestDto.setDistributionPeriod("1");
    	stockDeadlineRequestDto.setImminent(11);
    	stockDeadlineRequestDto.setDelivery(11);
    	stockDeadlineRequestDto.setTargetStockRatio(11);
    	stockDeadlineRequestDto.setModifyId(11);

        // when, then
        assertThrows(Exception.class, () -> {
        	stockDeadlineService.putStockDeadline(stockDeadlineRequestDto);
        });
    }


    @Test
    void 재고기한관리_이력목록_조회_성공() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	Page<StockDeadlineHistResultVo> stockDeadlineHistResultVo = stockDeadlineService.getStockDeadlineHistList(stockDeadlineRequestDto);

    	stockDeadlineHistResultVo.stream().forEach(
                i -> log.info("재고기한관리_이력목록_조회_성공  stockDeadlineHistResultVo : {}",  i)
        );

        assertTrue(CollectionUtils.isNotEmpty(stockDeadlineHistResultVo));
    }


    @Test
    void 재고기한관리_이력목록_조회_실패() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	Page<StockDeadlineHistResultVo> stockDeadlineHistResultVo = stockDeadlineService.getStockDeadlineHistList(stockDeadlineRequestDto);

    	stockDeadlineHistResultVo.stream().forEach(
                i -> log.info("재고기한관리_이력목록_조회_실패 stockDeadlineHistResultVo : {}",  i)
        );

    	assertFalse(CollectionUtils.isEmpty(stockDeadlineHistResultVo));
    }

    @Test
    void 재고기한관리_기본설정_Y값_건수_조회_성공() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setUrSupplierId(2);   //공급처ID
    	stockDeadlineRequestDto.setUrWarehouseId("0");//출고처ID

    	int count = stockDeadlineService.getStockDeadlineBasicYnCount(stockDeadlineRequestDto);

        log.info(" 재고기한관리_기본설정_Y값_건수_조회_성공 count : ",  count);

        assertTrue(count > 0);
    }

    @Test
    void 재고기한관리_기본설정_Y값_체크_조회_성공() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setUrSupplierId(2);          //공급처ID
    	stockDeadlineRequestDto.setUrWarehouseId("0");       //출고처ID
    	stockDeadlineRequestDto.setDistributionPeriod("999");//유통기간

    	String basicYnCheck = stockDeadlineService.getStockDeadlineBasicYnCheck(stockDeadlineRequestDto);

        log.info(" 재고기한관리_기본설정_Y값_건수_조회_성공 basicYnCheck : ",  basicYnCheck);

        assertTrue(basicYnCheck.equals("Y"));
    }

    @Test
    void 재고기한관리_기본설정_수정_성공() throws Exception {

    	StockDeadlineRequestDto stockDeadlineRequestDto = new StockDeadlineRequestDto();
    	stockDeadlineRequestDto.setUrSupplierId(2);          //공급처ID
    	stockDeadlineRequestDto.setUrWarehouseId("0");       //출고처ID
    	stockDeadlineRequestDto.setDistributionPeriod("999");//유통기간

    	int count = stockDeadlineService.putStockDeadlineBasicYn(stockDeadlineRequestDto);

        log.info("재고기한관리_기본설정_수정_성공  count : ", count);

        assertTrue(count > 0);
    }
}