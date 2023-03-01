package kr.co.pulmuone.v1.goods.brand.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.goods.brand.GoodsBrandMapper;
import kr.co.pulmuone.v1.goods.brand.dto.vo.DpBrandListResultVo;
import kr.co.pulmuone.v1.goods.brand.dto.vo.UrBrandListResultVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;


class GoodsBrandServiceTest extends CommonServiceTestBaseForJunit5 {

    @InjectMocks
    private GoodsBrandService mockGoodsBrandService;

    @Mock
    GoodsBrandMapper mockGoodsBrandMapper;

    @Autowired
    GoodsBrandService goodsBrandService;

    @BeforeEach
    void setUp() {
        mockGoodsBrandService = new GoodsBrandService(mockGoodsBrandMapper);
    }

    @Test
    void getBrandList() throws Exception {

        List<UrBrandListResultVo> list = new ArrayList<>();

        given(mockGoodsBrandMapper.getUrBrandList()).willReturn(list);

        mockGoodsBrandService.getUrBrandList();
    }

    @Test
    void getDpBrandList_조회_정상() throws Exception {
        //given, when
        List<DpBrandListResultVo> result = goodsBrandService.getDpBrandList();

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getDpBrandTitleById_조회_정상() throws Exception {
        //given
        Long dpBrandId = 1L;

        //when
        String result = goodsBrandService.getDpBrandTitleById(dpBrandId);

        //then
        assertTrue(result.length() > 0);
    }
}