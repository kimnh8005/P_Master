package kr.co.pulmuone.v1.goods.etc.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.goods.etc.GoodsSpecificsMapper;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsFieldRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsFieldVo;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterFieldVo;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterVo;
import kr.co.pulmuone.v1.goods.item.dto.ItemSpecValueRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class GoodsSpecificsServiceTest extends CommonServiceTestBaseForJunit5{

    @Autowired
    private GoodsSpecificsService goodsSpecificsService;

    @InjectMocks
    private GoodsSpecificsService mockGoodsSpecificsService;

    @Mock
    private GoodsSpecificsMapper mockGoodsSpecificsMapper;

    @BeforeEach
    void SetUp() {
        mockGoodsSpecificsService = new GoodsSpecificsService(mockGoodsSpecificsMapper);
    }

    @Test
    void 상품군_목록_조회() {
        SpecificsListRequestDto specificsListRequestDto = new SpecificsListRequestDto();

        List<SpecificsMasterVo> specificsMasterList = goodsSpecificsService.getSpecificsMasterList(specificsListRequestDto);

        assertTrue(CollectionUtils.isNotEmpty(specificsMasterList));
    }

    @Test
    void 상품군별_정보고시_항목_조회() {
        SpecificsListRequestDto specificsListRequestDto = new SpecificsListRequestDto();
        specificsListRequestDto.setSpecificsMasterId(1L);

        List<SpecificsMasterFieldVo> specificsMasterFieldList = goodsSpecificsService.getSpecificsMasterFieldList(specificsListRequestDto);

        assertTrue(CollectionUtils.isNotEmpty(specificsMasterFieldList));
    }

    @Test
    void 상품정보제공고시항목_목록_조회() {
        SpecificsFieldRequestDto specificsFieldRequestDto = new SpecificsFieldRequestDto();

        List<SpecificsFieldVo> specificsFieldList = goodsSpecificsService.getSpecificsFieldList(specificsFieldRequestDto);

        assertTrue(CollectionUtils.isNotEmpty(specificsFieldList));
    }

    @Test
    void 품목별_상품정보제공고시_값_사용유무() {
        SpecificsFieldRequestDto specificsFieldRequestDto = new SpecificsFieldRequestDto();
        specificsFieldRequestDto.setSpecificsFieldId(3L);

        boolean itemSpecificsValueUseYn = goodsSpecificsService.getItemSpecificsValueUseYn(specificsFieldRequestDto);

        assertTrue(itemSpecificsValueUseYn);
    }

    @Test
    void 상품정보제공고시_분류_항목_관계_삭제() {
        SpecificsMasterFieldVo specificsMasterFieldVo = new SpecificsMasterFieldVo();
        specificsMasterFieldVo.setSpecificsFieldId(3L);

        int count = goodsSpecificsService.delSpecificsMasterField(specificsMasterFieldVo);

        assertTrue(count > 0);
    }

    @Test
    void 상품정보제공고시_분류_항목_관계_등록() {
        SpecificsMasterFieldVo specificsMasterFieldVo = new SpecificsMasterFieldVo();
        specificsMasterFieldVo.setSpecificsMasterId(1L);
        specificsMasterFieldVo.setSpecificsFieldId(9999L);
        specificsMasterFieldVo.setSort(1);
        specificsMasterFieldVo.setCreateId("1");

        int count = goodsSpecificsService.addSpecificsMasterField(specificsMasterFieldVo);

        assertTrue(count > 0);
    }

    @Test
    void 상품정보제공고시항목_삭제() {
        SpecificsFieldVo specificsFieldVo = new SpecificsFieldVo();
        specificsFieldVo.setSpecificsFieldId(3L);

        int count = goodsSpecificsService.delSpecificsField(specificsFieldVo);

        assertTrue(count > 0);
    }

    @Test
    void 상품정보제공고시항목_등록() {
        SpecificsFieldVo specificsFieldVo = new SpecificsFieldVo();
        specificsFieldVo.setSpecificsFieldName("테스트");
        specificsFieldVo.setCreateId("1");

        int count = goodsSpecificsService.addSpecificsField(specificsFieldVo);

        assertTrue(count > 0);
    }

    @Test
    void 상품정보제공고시항목_수정() {
        SpecificsFieldVo specificsFieldVo = new SpecificsFieldVo();
        specificsFieldVo.setSpecificsFieldId(3L);
        specificsFieldVo.setSpecificsFieldName("테스트");
        specificsFieldVo.setCreateId("1");

        int count = goodsSpecificsService.putSpecificsField(specificsFieldVo);

        assertTrue(count > 0);
    }

    @Test
    void 품목에_상품정보제공고시분류_적용_유무() {
        SpecificsListRequestDto specificsListRequestDto = new SpecificsListRequestDto();
        specificsListRequestDto.setSpecificsMasterId(1L);

        boolean itemSpecificsMasterUseYn = goodsSpecificsService.getItemSpecificsMasterUseYn(specificsListRequestDto);

        assertTrue(itemSpecificsMasterUseYn);
    }

    @Test
    void 상품정보제공고시분류_삭제() {
        SpecificsMasterVo specificsMasterVo = new SpecificsMasterVo();
        specificsMasterVo.setSpecificsMasterId(1L);

        int count = goodsSpecificsService.delSpecificsMaster(specificsMasterVo);

        assertTrue(count > 0);
    }

    @Test
    void 상품정보제공고시분류_등록() {
        SpecificsMasterVo specificsMasterVo = new SpecificsMasterVo();
        specificsMasterVo.setSpecificsMasterName("테스트");
        specificsMasterVo.setSort(1);
        specificsMasterVo.setUseYn("Y");
        specificsMasterVo.setCreateId("1");

        int count = goodsSpecificsService.addSpecificsMaster(specificsMasterVo);

        assertTrue(count > 0);
    }

    @Test
    void 상품정보제공고시분류_수정() {
        SpecificsMasterVo specificsMasterVo = new SpecificsMasterVo();
        specificsMasterVo.setSpecificsMasterId(1L);
        specificsMasterVo.setSpecificsMasterName("테스트");
        specificsMasterVo.setSort(1);
        specificsMasterVo.setUseYn("Y");
        specificsMasterVo.setCreateId("1");

        int count = goodsSpecificsService.putSpecificsMaster(specificsMasterVo);

        assertTrue(count > 0);
    }

    @Test
    void 상품군명_중복_유무() {
        SpecificsListRequestDto specificsListRequestDto = new SpecificsListRequestDto();
        specificsListRequestDto.setSpecificsMasterName("기타");

        boolean specificsMasterNameDuplicateYn = goodsSpecificsService.getSpecificsMasterNameDuplicateYn(specificsListRequestDto);

        assertTrue(specificsMasterNameDuplicateYn);
    }

    @Test
    void 정보고시항목명_중복_유무() {
        SpecificsFieldRequestDto specificsFieldRequestDto = new SpecificsFieldRequestDto();
        specificsFieldRequestDto.setSpecificsFieldName("색상");

        boolean specificsFieldNameDuplicateYn = goodsSpecificsService.getSpecificsFieldNameDuplicateYn(specificsFieldRequestDto);

        assertTrue(specificsFieldNameDuplicateYn);
    }

    @Test
    void delSpecificsMasterField() {
        // given
        given(mockGoodsSpecificsMapper.delSpecificsMasterField(any())).willReturn(1);
        SpecificsMasterFieldVo specificsMasterFieldVo = new SpecificsMasterFieldVo();

        // when
        int count = mockGoodsSpecificsService.delSpecificsMasterField(specificsMasterFieldVo);

        // then
        assertTrue(count > 0);
    }

    @Test
    void delSpecificsField() {
        // given
        given(mockGoodsSpecificsMapper.delSpecificsField(any())).willReturn(1);
        SpecificsFieldVo specificsFieldVo = new SpecificsFieldVo();

        // when
        int count = mockGoodsSpecificsService.delSpecificsField(specificsFieldVo);

        // then
        assertTrue(count > 0);
    }

    @Test
    void addSpecificsMasterField() {
        // given
        SpecificsMasterFieldVo specificsMasterFieldVo = new SpecificsMasterFieldVo();
        specificsMasterFieldVo.setSpecificsMasterId(1L);
        specificsMasterFieldVo.setSpecificsFieldId(1L);
        specificsMasterFieldVo.setSort(1);
        specificsMasterFieldVo.setCreateId("1");

        // when
        int count = goodsSpecificsService.addSpecificsMasterField(specificsMasterFieldVo);

        // then
        assertTrue(count > 0);
    }

    @Test
    void delSpecificsMaster() {
        given(mockGoodsSpecificsMapper.delSpecificsMaster(any())).willReturn(1);

        // when
        int count = mockGoodsSpecificsService.delSpecificsMaster(null);

        // then
        assertTrue(count > 0);
    }

    @Test
    void 품목별_상품고시_조회() {
        //given
    	String specificsMasterId = "1";

    	//when
        List<MasterItemVo> itemSpecificsList = goodsSpecificsService.getItemSpecificsList(specificsMasterId);

        //then
        assertTrue(CollectionUtils.isNotEmpty(itemSpecificsList));
    }

    @Test
    void 품목별_상품고시_업데이트() {
    	ItemSpecValueRequestDto itemSpecValueRequestDto = new ItemSpecValueRequestDto();
    	itemSpecValueRequestDto.setIlItemCode("0060238");
    	itemSpecValueRequestDto.setIlSpecFieldId(180);
    	itemSpecValueRequestDto.setSpecFieldValue("상품 상세 정보를 참조하세요.");
    	itemSpecValueRequestDto.setCreateId(Long.valueOf("1"));
    	itemSpecValueRequestDto.setModifyid(Long.valueOf("1"));

    	int count = goodsSpecificsService.putItemSpecificsValue(itemSpecValueRequestDto);

        assertTrue(count > 0);
    }

}
