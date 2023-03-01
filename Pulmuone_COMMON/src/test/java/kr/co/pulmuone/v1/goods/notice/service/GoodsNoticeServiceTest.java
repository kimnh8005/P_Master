package kr.co.pulmuone.v1.goods.notice.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.mapper.goods.notice.GoodsNoticeMapper;
import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeDto;
import kr.co.pulmuone.v1.goods.notice.dto.vo.GoodsNoticeVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
class GoodsNoticeServiceTest extends CommonServiceTestBaseForJunit5 {


	@Autowired
    private GoodsNoticeService goodsNoticeService;

	@InjectMocks
	private GoodsNoticeService mockGoodsNoticeService;

	@Mock
	private GoodsNoticeMapper mockGoodsNoticeMapper;

	@BeforeEach
	void setUp() {
		mockGoodsNoticeService = new GoodsNoticeService(mockGoodsNoticeMapper);
	}

	@Test
	public void test_상품공지상세_결과있음()  {
		//given
		String ilNoticeId = "1";
    	log.info("ilNoticeId: {}", ilNoticeId);

    	// when
    	GoodsNoticeVo result = goodsNoticeService.getGoodsNoticeInfo(ilNoticeId);

		// then
		assertNotNull(result);
	}
    @Test
    public void test_상품공지목록_결과있음()  {

    	// given
    	String goodsNoticeTp = "";
    	String useYn = "";

    	GoodsNoticeDto dto = new GoodsNoticeDto();
    	dto.setGoodsNoticeTp(goodsNoticeTp);
    	dto.setUseYn(useYn);

        // when
    	GoodsNoticeDto result = goodsNoticeService.getGoodsNoticeList(dto);

    	// then
        assertNotNull(result.getRows());
    }

    @Test
	public void test_상품공지_신규등록() {
    	GoodsNoticeDto dto = new GoodsNoticeDto();
		dto.setGoodsNoticeTp("GOODS_NOTICE_TP.TOP");
		dto.setNoticeNm("테스트제목");
		dto.setDispAllYn("Y");
		dto.setUrWarehouseId("");
		dto.setNoticeStartDt("2020-12-07");
		dto.setNoticeEndDt("2021-03-31");
		dto.setUseYn("Y");
		dto.setDetlDesc("테스트상세정보");

		given(mockGoodsNoticeMapper.addGoodsNotice(any())).willReturn(1);

        // when
		MessageCommEnum msgEnum = mockGoodsNoticeService.addGoodsNotice(null);

        // then
        assertTrue(msgEnum.getCode().equals(ApiResult.success().getCode()));
	}

	@Test
	public void test_상품공지_신규등록_오류() {
		GoodsNoticeDto dto = new GoodsNoticeDto();
		dto.setGoodsNoticeTp("GOODS_NOTICE_TP.TOP");
		dto.setNoticeNm("테스트제목");
		dto.setDispAllYn("Y");
		dto.setUrWarehouseId("");
		dto.setNoticeStartDt("2020-12-07");
		dto.setNoticeEndDt("2021-03-31");
		dto.setUseYn("Y");
		dto.setDetlDesc("테스트상세정보");

		given(mockGoodsNoticeMapper.addGoodsNotice(any())).willReturn(0);

        // when
		MessageCommEnum msgEnum = mockGoodsNoticeService.addGoodsNotice(null);

        // then
        assertTrue(msgEnum.getCode().equals(ApiResult.fail().getCode()));
	}

    @Test
    public void test_상품공지_수정() {
    	GoodsNoticeDto dto = new GoodsNoticeDto();
		dto.setGoodsNoticeTp("GOODS_NOTICE_TP.TOP");
		dto.setNoticeNm("테스트제목");
		dto.setDispAllYn("Y");
		dto.setUrWarehouseId("");
		dto.setNoticeStartDt("2020-12-07");
		dto.setNoticeEndDt("2021-03-31");
		dto.setUseYn("Y");
		dto.setDetlDesc("테스트상세정보");

		given(mockGoodsNoticeMapper.putGoodsNotice(any())).willReturn(1);

        // when
		MessageCommEnum msgEnum = mockGoodsNoticeService.putGoodsNotice(null);

        // then
        assertTrue(msgEnum.getCode().equals(ApiResult.success().getCode()));
    }
    @Test
    public void test_상품공지_수정_오류() {
    	GoodsNoticeDto dto = new GoodsNoticeDto();
		dto.setGoodsNoticeTp("GOODS_NOTICE_TP.TOP");
		dto.setNoticeNm("테스트제목");
		dto.setDispAllYn("Y");
		dto.setUrWarehouseId("");
		dto.setNoticeStartDt("2020-12-07");
		dto.setNoticeEndDt("2021-03-31");
		dto.setUseYn("Y");
		dto.setDetlDesc("테스트상세정보");

		given(mockGoodsNoticeMapper.putGoodsNotice(any())).willReturn(0);

        // when
		MessageCommEnum msgEnum = mockGoodsNoticeService.putGoodsNotice(null);
        // then
        assertTrue(msgEnum.getCode().equals(ApiResult.fail().getCode()));
    }

    @Test
    void getGoodsNoticeListByUser_조회_성공() {
		//given
		String warehouseGroupCode = "WAREHOUSE_GROUP.ACCOUNT_RE";
		Long urWarehouseId = 123L;

		//when
		List<GoodsNoticeResponseDto> result = goodsNoticeService.getGoodsNoticeListByUser(warehouseGroupCode, urWarehouseId);

		//then
		assertTrue(result.size() > 0);
    }
}
