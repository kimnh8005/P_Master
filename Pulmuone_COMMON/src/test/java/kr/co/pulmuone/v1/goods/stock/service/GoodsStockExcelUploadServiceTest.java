package kr.co.pulmuone.v1.goods.stock.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;

import kr.co.pulmuone.v1.comm.util.StringUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.StockEnums;
import kr.co.pulmuone.v1.comm.mapper.goods.stock.GoodsStockExcelUploadMapper;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExcelUploadResultVo;

class GoodsStockExcelUploadServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private GoodsStockExcelUploadService goodsStockExcelUploadService;

    @InjectMocks
    private GoodsStockExcelUploadService mockGoodsStockExcelUploadService;

    @Mock
    GoodsStockExcelUploadMapper mockGoodsStockExcelUploadMapper;

    @BeforeEach
    void setUp() {
        mockGoodsStockExcelUploadService = new GoodsStockExcelUploadService(mockGoodsStockExcelUploadMapper);
    }

    @Test
    void addExcelUpload() {
        given(mockGoodsStockExcelUploadMapper.addExcelUpload(any())).willReturn(1);
        int n = mockGoodsStockExcelUploadService.addExcelUpload(null);
        assertTrue(n > 0);
    }

    @Test
    void addSuccessExcelDetlLog() {
        given(mockGoodsStockExcelUploadMapper.addSuccessExcelDetlLog(any())).willReturn(1);
        int n = mockGoodsStockExcelUploadService.addSuccessExcelDetlLog(null);
        assertTrue(n > 0);
    }

    @Test
    void addFailExcelDetlLog() {
        given(mockGoodsStockExcelUploadMapper.addFailExcelDetlLog(any())).willReturn(1);
        int n = mockGoodsStockExcelUploadService.addFailExcelDetlLog(null);
        assertTrue(n > 0);
    }

    @Test
    void addExcelUploadLog() {
        given(mockGoodsStockExcelUploadMapper.addExcelUploadLog()).willReturn(1);
        int n = mockGoodsStockExcelUploadService.addExcelUploadLog();
        assertTrue(n > 0);
    }

    @Test
    void putExcelUploadLog() {
        given(mockGoodsStockExcelUploadMapper.putExcelUploadLog("test", "test", 0, 0)).willReturn(1);
        int n = mockGoodsStockExcelUploadService.putExcelUploadLog("test", "test", 0, 0);
        assertTrue(n > 0);
    }

    @Test
    void getEnvironment() {
    	String env = goodsStockExcelUploadService.getEnvironment();
        assertFalse(StringUtil.isEmpty(env));
    }

    @Test
    void getStockInfoList() {
    	StockExcelUploadListRequestDto dto = new StockExcelUploadListRequestDto();
    	dto.setIlItemCd("0040102");//품목코드
    	dto.setUrWarehouseId(85);  //용인 출고처
    	List<StockExcelUploadResultVo> resultList = goodsStockExcelUploadService.getStockInfoList(dto);//품목정보 조회
    	assertTrue(resultList.size() > 0);
    }

    @Test
    void getConfigValue() {
    	String psKey = StockEnums.UrWarehouseId.WAREHOUSE_YONGIN_ID.getCode();
		String urWarehouseId = goodsStockExcelUploadService.getConfigValue(psKey);//용인 출고처 조회
		assertTrue(urWarehouseId != null && !urWarehouseId.equals(""));
    }

    @Test
    void getSuccessCnt() {
    	int successCnt = goodsStockExcelUploadService.getSuccessCnt();
    	assertTrue(successCnt >= 0);
    }

    @Test
    void getFailCnt() {
    	int failCnt = goodsStockExcelUploadService.getFailCnt();
    	assertTrue(failCnt >= 0);
    }

    @Test
    void getStockExprList() {
    	List<StockExcelUploadResultVo> resultList = goodsStockExcelUploadService.getStockExprList();
    	assertTrue(resultList.size() > 0);
    }
}