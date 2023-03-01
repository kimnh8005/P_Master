package kr.co.pulmuone.v1.goods.itemprice.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.mapper.goods.itemprice.ItemPriceMapper;
import kr.co.pulmuone.v1.comm.mapper.goods.itemprice.ItemPriceOrigMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigVo;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class GoodsItemPriceServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private GoodsItemPriceService goodsItemPriceService;

//    @Test
//    void addItemPriceOrigByErpIfPriceSrchApi() throws Exception {
//        // given, when
//        int count = Optional.ofNullable(goodsItemPriceService.addItemPriceOrigByErpIfPriceSrchApi())
//                                    .orElse(0);
//
//        // then
//        assertTrue(count >= 0);
//    }

    @Test
    void getErpIfPriceSrchApi() throws Exception {
        // given, when
        String fromDate = DateUtil.getCurrentDate() + "000000";
        String toDate = DateUtil.getCurrentDate() + "235959";
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("updFlg", "Y");	//정보 업데이트 여부(Y / N)
        parameterMap.put("salTyp", "정상");    //행사구분 (정상 / 행사) -> 정상 행사 따로 호출 할 경우
        parameterMap.put("updDat", fromDate +"~" + toDate);   //수정날짜(당일기준)

        // when
        Map<String, ?> resultMap = goodsItemPriceService.getErpIfPriceSrchApi(parameterMap);

        // then
        BaseApiResponseVo baseApiResponseVo = (BaseApiResponseVo) resultMap.get("baseApiResponseVo");
        assertTrue(baseApiResponseVo.isSuccess());
    }

    // 테스트케이스 제외처리
    // 처리내역이 많아서 무한루프 
//    @Test
//    void addItemPriceByOrig() throws Exception {
//        // given, when
//        int count = goodsItemPriceService.addItemPriceByOrig();
//
//        // then
//        assertTrue(count >= 0);
//    }
}