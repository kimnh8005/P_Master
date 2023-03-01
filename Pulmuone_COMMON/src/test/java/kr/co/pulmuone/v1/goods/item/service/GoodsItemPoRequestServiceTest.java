package kr.co.pulmuone.v1.goods.item.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemPoRequestMapper;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoRequestResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoRequestVo;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
public class GoodsItemPoRequestServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
    private GoodsItemPoRequestService goodsItemPoRequestService;

	@Mock
    GoodsItemPoRequestMapper goodsItemPoRequestMapper;

	@BeforeEach
	void setUp(){
		preLogin();
	}

	@Test
    void 행사발주관리_목록조회()  {

    	// given
    	ItemPoRequestDto paramDto = new ItemPoRequestDto();

        // when
    	Page<ItemPoRequestVo> result = goodsItemPoRequestService.getPoRequestList(paramDto);

    	result.stream().forEach(i -> log.info(" result : {}",  i)
        );

    	//then
        assertTrue(CollectionUtils.isNotEmpty(result));
    }

	@Test
	void 행사발주관리_상세조회() {

		// given
		String ilPoEventId ="5";
		// when
    	ItemPoRequestVo itemPoRequestVo = goodsItemPoRequestService.getPoRequest(ilPoEventId);

    	ItemPoRequestResponseDto dto = new ItemPoRequestResponseDto();
    	dto.setDetail(itemPoRequestVo);

        log.info(" 발주유형목록_상세조회_결과있음 itemPoTypeVo : {}",  itemPoRequestVo);

        // then
        assertEquals(ilPoEventId, dto.getDetail().getIlPoEventId());
    }

	@Test
    void 행사발주관리_신규등록() {
		String ilGoodsId = "34";
    	String omSellersId = "1";
    	String poEventQty = "3";
    	String recevingReqDt = "2021-01-25";
    	String poScheduleDt = "2021-01-27";
    	String eventEndDt = "2021-01-30";
    	String memo = "test";

    	 // given
    	ItemPoRequestDto paramDto = new ItemPoRequestDto();
    	paramDto.setIlGoodsId(ilGoodsId);
    	paramDto.setInputSellersDetail(omSellersId);
    	paramDto.setPoEventQty(poEventQty);
    	paramDto.setRecevingReqDt(recevingReqDt);
    	paramDto.setPoScheduleDt(poScheduleDt);
    	paramDto.setEventEndDt(eventEndDt);
    	paramDto.setMemo(memo);

        // when
        int result = goodsItemPoRequestService.addItemPoRequest(paramDto);

        log.info(" 발주유형관리_등록_성공 result : "+  result);

        // then
        assertTrue(result > 0);
    }

	@Test
    void 행사발주관리_수정() {

		String ilGoodsId = "15552";
    	String omSellersId = "1";
    	String poEventQty = "3";
    	String recevingReqDt = "2021-01-25";
    	String poScheduleDt = "2021-01-27";
    	String eventEndDt = "2021-01-30";
    	String memo = "test";

    	// given
    	ItemPoRequestDto paramDto = new ItemPoRequestDto();
    	paramDto.setIlGoodsId(ilGoodsId);
    	paramDto.setInputSellersDetail(omSellersId);
    	paramDto.setPoEventQty(poEventQty);
    	paramDto.setRecevingReqDt(recevingReqDt);
    	paramDto.setPoScheduleDt(poScheduleDt);
    	paramDto.setEventEndDt(eventEndDt);
    	paramDto.setMemo(memo);
    	paramDto.setIlPoEventId("3");

        // when
        int result = goodsItemPoRequestService.putPoRequest(paramDto);

        log.info(" 발주유형관리_등록_성공 result : "+  result);

        // then
        assertTrue(result > 0);
    }

	@Test
    void 행사발주관리_삭제() {

		String ilPoEventId = "3";
    	ItemPoRequestDto paramDto = new ItemPoRequestDto();
    	paramDto.setIlPoEventId(ilPoEventId);

    	// when
        int count = goodsItemPoRequestService.delPoRequest(paramDto);

        // then
        assertTrue(count > 0);
    }

	@Test
    void 행사발주관리_상품조회() {

		String ilGoodsId = "15488";

    	GoodsSearchVo goodsSearchVo = goodsItemPoRequestService.getGoodsIdInfo(ilGoodsId);

        // then
    	assertEquals(Long.valueOf(ilGoodsId), goodsSearchVo.getGoodsId());
    }

	@Test
    void 행사발주관리_판매처조회() {

		String omSellersId = "0";

		SellersVo sellersVo = goodsItemPoRequestService.getSellersInfo(omSellersId);

        // then
    	assertEquals(Long.valueOf(omSellersId), sellersVo.getOmSellersId());
    }

	@Test
    void 행사발주관리_업로드() {

		given(goodsItemPoRequestMapper.addItemPoRequestUpload(any())).willReturn(1);
        int n = goodsItemPoRequestMapper.addItemPoRequestUpload(null);
        assertTrue(n > 0);
    }

	@Test
    void 행사발주관리_업로드_저장() {

		String ilGoodsId = "34";
    	String omSellersId = "1";
    	String poEventQty = "3";
    	String recevingReqDt = "2021-01-25";
    	String poScheduleDt = "2021-01-27";
    	String eventEndDt = "2021-01-30";
    	String memo = "test";

    	 // given
    	ItemPoRequestVo paramDto = new ItemPoRequestVo();
    	paramDto.setIlGoodsId(ilGoodsId);
    	paramDto.setOmSellersId(omSellersId);
    	paramDto.setPoEventQty(poEventQty);
    	paramDto.setRecevingReqDt(recevingReqDt);
    	paramDto.setPoScheduleDt(poScheduleDt);
    	paramDto.setEventEndDt(eventEndDt);
    	paramDto.setMemo(memo);
    	paramDto.setCreateId("1");

        // when
        int result = goodsItemPoRequestService.addItemPoRequestUpload(paramDto);

        log.info(" 발주유형관리_등록_성공 result : "+  result);

        // then
        assertTrue(result > 0);
    }

	@Test
    void 행사발주관리_업로드_수정() {

		String logId = "5";
    	int successCnt = 1;
    	int failCnt = 3;

    	 // given
    	ItemPoRequestDto paramDto = new ItemPoRequestDto();
    	paramDto.setLogId(logId);
    	paramDto.setSuccessCnt(successCnt);
    	paramDto.setFailCnt(failCnt);

        // when
        int result = goodsItemPoRequestService.putPoEventUploadLog(paramDto);
        // then
        assertTrue(result > 0);
    }

	@Test
    void 행사발주관리_실패내역조회()  {

    	// given
		ItemPoRequestDto paramDto = new ItemPoRequestDto();
		paramDto.setLogId("9");

        // when
    	List<ItemPoRequestVo> result = goodsItemPoRequestService.getPoRequestUploadFailList(paramDto);

    	result.forEach(i -> log.info(" result : {}",  i));

    	//then
        assertTrue(CollectionUtils.isNotEmpty(result));
    }

	@Test
    void 행사발주관리_엑셀다운로드()  {

    	// given
		ItemPoRequestDto paramDto = new ItemPoRequestDto();

        // when
    	List<ItemPoRequestVo> result = goodsItemPoRequestService.getPoRequestList(paramDto);

    	result.stream().forEach(i -> log.info(" result : {}",  i)
        );

    	//then
        assertTrue(CollectionUtils.isNotEmpty(result));
    }

	@Test
    void 행사발주관리_업로드조회()  {

    	// given
    	ItemPoRequestDto paramDto = new ItemPoRequestDto();

        // when
    	Page<ItemPoRequestVo> result = goodsItemPoRequestService.getPoRequestUploadList(paramDto);

    	result.stream().forEach(i -> log.info(" result : {}",  i)
        );

    	//then
        assertTrue(CollectionUtils.isNotEmpty(result));
    }


}
