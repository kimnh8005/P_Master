package kr.co.pulmuone.v1.goods.item.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecificsValueVo;

class GoodsItemSpecificsValueServiceTest extends CommonServiceTestBaseForJunit5{

    @Autowired
    private GoodsItemSpecificsValueService goodsItemSpecificsValueService;

    @Test
    void 품목별_상품정보제공고시_값_삭제() {
        ItemSpecificsValueVo itemSpecificsValueVo = new ItemSpecificsValueVo();
        itemSpecificsValueVo.setSpecificsFieldId(3L);

       int count = goodsItemSpecificsValueService.delItemSpecificsValue(itemSpecificsValueVo);

       assertTrue(count > 0);
    }
}
