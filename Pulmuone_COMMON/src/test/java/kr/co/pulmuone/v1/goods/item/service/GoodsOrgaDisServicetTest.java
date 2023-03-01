package kr.co.pulmuone.v1.goods.item.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.item.dto.OrgaDiscountRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.OrgaDiscountListVo;

public class GoodsOrgaDisServicetTest extends CommonServiceTestBaseForJunit5{

	@Autowired
    GoodsOrgaDisService goodsOrgaDisService;

	@Test
    void 올가할인상품조회_테스트() {

		OrgaDiscountRequestDto paramDto = new OrgaDiscountRequestDto();

		Page<OrgaDiscountListVo> result = goodsOrgaDisService.getOrgaDisList(paramDto);

		// then
        assertNotNull(result.getResult());

    }


}
