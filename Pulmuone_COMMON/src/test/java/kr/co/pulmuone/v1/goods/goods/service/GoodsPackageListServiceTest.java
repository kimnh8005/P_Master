package kr.co.pulmuone.v1.goods.goods.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPackageListVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GoodsPackageListServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    GoodsPackageListService goodsPackageListService;

    @BeforeEach
    void setup() {
        preLogin();
    }

    @Test
    void 묶음상품조회_테스트() {

        GoodsPackageListRequestDto paramDto = new GoodsPackageListRequestDto();

        paramDto.setSearchType("single");
        Page<GoodsPackageListVo> result = goodsPackageListService.getGoodsPackageList(paramDto);

        // then
        assertNotNull(result.getResult());
    }
}
