package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfGoodSearchResponseDto;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.MasterItemTypes;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoodsItemRegisterServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
    private GoodsItemRegisterService goodsItemRegisterService;

	@Test
    void ERP조회후_가격정보조회_테스트() {
		List<ErpIfGoodSearchResponseDto> dtoList = null;
		try {
			dtoList = goodsItemRegisterService.getErpItemApiListByItemCode("0060972", MasterItemTypes.COMMON);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	// ERP 조회결과에서 가격정보 조회
    	dtoList = goodsItemRegisterService.processErpItemPriceaApiList(dtoList, MasterItemTypes.COMMON);

    	assertTrue(dtoList.size() >  0);

    }
}
