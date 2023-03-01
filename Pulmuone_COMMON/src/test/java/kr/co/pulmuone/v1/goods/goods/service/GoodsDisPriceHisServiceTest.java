package kr.co.pulmuone.v1.goods.goods.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDisPriceHisRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDisPriceHisVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GoodsDisPriceHisServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private GoodsDisPriceHisService goodsDisPriceHisService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 상품할인조회_테스트() {
        //given
        GoodsDisPriceHisRequestDto paramDto = new GoodsDisPriceHisRequestDto();
        paramDto.setDiscountTpList(new ArrayList<>());

        //when
        Page<GoodsDisPriceHisVo> result = goodsDisPriceHisService.getGoodsDisPriceHisList(paramDto);

        //then
        assertNotNull(result.getResult());
    }

}
