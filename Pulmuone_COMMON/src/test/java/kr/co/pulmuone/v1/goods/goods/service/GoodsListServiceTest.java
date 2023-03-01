package kr.co.pulmuone.v1.goods.goods.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kr.co.pulmuone.v1.goods.goods.dto.GoodsApprovalResponseDto;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalGoodsRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsListMapper;
import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class GoodsListServiceTest extends CommonServiceTestBaseForJunit5{

    @Autowired
    private GoodsListService goodsListService;

    @InjectMocks
	private GoodsListService mockGoodsListService;

	@Mock
	private GoodsListMapper mockGoodsListMapper;

	@BeforeEach
	void setUp() {
        preLogin();
		mockGoodsListService = new GoodsListService(mockGoodsListMapper);
	}

    @Test
    void 검색키_리스트_변환() {
        String searchKey = "1,2,3,4,5,6,7,8";

        List<String> keyList = goodsListService.getSearchKeyToSearchKeyList(searchKey, Constants.ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS);

        assertTrue(CollectionUtils.isNotEmpty(keyList));

        log.info("keyList : {}", keyList.toString());
    }

    @Test
    void 상품_목록_조회() {
        GoodsListRequestDto goodsListRequestDto = new GoodsListRequestDto();
        goodsListRequestDto.setPage(1);
        goodsListRequestDto.setPageSize(20);

        Page<GoodsVo> goodsList = goodsListService.getGoodsList(goodsListRequestDto);

        assertTrue(CollectionUtils.isNotEmpty(goodsList.getResult()));
    }

    @Test
    void 상품목록_판매상태_변경_성공() {
        GoodsVo goodsVo = new GoodsVo();
        goodsVo.setSaleStatusCode("SALE_STATUS.ON_SALE");
        goodsVo.setCreateId("1");
        goodsVo.setGoodsId(1L);

        int count = goodsListService.putGoodsSaleStatusChange(goodsVo);

        assertTrue(count > 0);

    }

    @Test
    void 상품목록_판매상태_변경_실패() {
        GoodsVo goodsVo = new GoodsVo();

        int count = goodsListService.putGoodsSaleStatusChange(goodsVo);

        assertFalse(count > 0);
    }

    @Test
    void test_상품등록상태이력등록_성공() {
    	//given
    	given(mockGoodsListMapper.addGoodsRegistStatusHistory(any())).willReturn(1);
        // when
		int result = mockGoodsListService.addGoodsRegistStatusHistory(null);

        // then
		assertTrue(result == 1);
    }

    @Test
    void test_상품등록상태이력등록_실패() {
    	//given
    	given(mockGoodsListMapper.addGoodsRegistStatusHistory(any())).willReturn(0);
    	// when
    	int result = mockGoodsListService.addGoodsRegistStatusHistory(null);

    	// then
    	assertFalse(result == 1);
    }
    @Test
    void test_상품등록승인목록조회_결과있음() {
    	//given
    	ApprovalGoodsRequestDto requestDto = new ApprovalGoodsRequestDto();

    	// when
    	GoodsApprovalResponseDto result = goodsListService.getApprovalGoodsRegistList(requestDto);

    	// then
    	assertNotNull(result.getRows());
    }
    @Test
    void test_상품등록승인목록조회_결과없음() {
    	//given
    	ApprovalGoodsRequestDto requestDto = new ApprovalGoodsRequestDto();

    	// when
    	GoodsApprovalResponseDto result = goodsListService.getApprovalGoodsRegistList(requestDto);

    	// then
    	assertTrue(CollectionUtils.isEmpty(result.getRows()));
    }
    @Test
    void test_상품등록승인요청철회_성공() throws Exception {
    	//given
    	given(mockGoodsListMapper.putCancelRequestApprovalGoodsRegist(any())).willReturn(1);
    	given(mockGoodsListService.addGoodsRegistStatusHistory(any())).willReturn(1);
    	// when
    	MessageCommEnum result = mockGoodsListService.putCancelRequestApprovalGoodsRegist(null);

    	// then
        assertTrue(result.getCode().equals(ApiResult.success().getCode()));
    }

    @Test
    void test_상품등록승인요청철회_실패() throws Exception {
    	//given
    	given(mockGoodsListMapper.putCancelRequestApprovalGoodsRegist(any())).willReturn(0);
    	given(mockGoodsListService.addGoodsRegistStatusHistory(any())).willReturn(0);

    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockGoodsListService.putCancelRequestApprovalGoodsRegist(null);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.PROGRAM_ERROR, myException.getMessageEnum());
    }

    @Test
    void test_상품등록승인처리_성공() throws Exception {
    	//given
    	given(mockGoodsListMapper.putApprovalProcessGoodsRegist(any())).willReturn(1);
    	given(mockGoodsListService.addGoodsRegistStatusHistory(any())).willReturn(1);
    	// when
    	MessageCommEnum result = mockGoodsListService.putApprovalProcessGoodsRegist(null);

    	// then
    	assertTrue(result.getCode().equals(ApiResult.success().getCode()));
    }
    @Test
    void test_상품등록승인처리_실패() throws Exception {
    	//given
    	given(mockGoodsListMapper.putApprovalProcessGoodsRegist(any())).willReturn(0);
    	given(mockGoodsListService.addGoodsRegistStatusHistory(any())).willReturn(0);

    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockGoodsListService.putApprovalProcessGoodsRegist(null);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.PROGRAM_ERROR, myException.getMessageEnum());
    }

    @Test
    void test_상품할인승인목록조회_결과있음() throws Exception {
        //given
    	ApprovalGoodsRequestDto requestDto = new ApprovalGoodsRequestDto();

    	// when
    	GoodsApprovalResponseDto result = goodsListService.getApprovalGoodsDiscountList(requestDto);

    	// then
    	assertNotNull(result.getRows());
    }

    @Test
    void test_상품할인승인목록조회_결과없음() throws Exception {
        //given
    	ApprovalGoodsRequestDto requestDto = new ApprovalGoodsRequestDto();

    	// when
    	GoodsApprovalResponseDto result = goodsListService.getApprovalGoodsDiscountList(requestDto);

    	// then
    	assertTrue(CollectionUtils.isEmpty(result.getRows()));
    }

    @Test
    void test_상품할인승인요청철회_성공() throws Exception {
        //given
    	given(mockGoodsListMapper.putCancelRequestApprovalGoodsDiscount(any())).willReturn(1);
    	given(mockGoodsListService.addGoodsDiscountStatusHistory(any())).willReturn(1);
    	// when
    	MessageCommEnum result = mockGoodsListService.putCancelRequestApprovalGoodsDiscount(null);

    	// then
        assertTrue(result.getCode().equals(ApiResult.success().getCode()));
    }

    @Test
    void test_상품할인승인요청철회_실패() throws Exception {
        //given
    	given(mockGoodsListMapper.putCancelRequestApprovalGoodsDiscount(any())).willReturn(0);
    	given(mockGoodsListService.addGoodsDiscountStatusHistory(any())).willReturn(0);

    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockGoodsListService.putCancelRequestApprovalGoodsDiscount(null);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.PROGRAM_ERROR, myException.getMessageEnum());
    }

    @Test
    void test_상품할인승인처리_성공() throws Exception {
        //given
    	given(mockGoodsListMapper.putApprovalProcessGoodsDiscount(any())).willReturn(1);
    	given(mockGoodsListService.addGoodsDiscountStatusHistory(any())).willReturn(1);
    	// when
    	MessageCommEnum result = mockGoodsListService.putApprovalProcessGoodsDiscount(null);

    	// then
    	assertTrue(result.getCode().equals(ApiResult.success().getCode()));
    }

    @Test
    void test_상품할인승인처리_실패() throws Exception {
        //given
    	given(mockGoodsListMapper.putApprovalProcessGoodsDiscount(any())).willReturn(0);
    	given(mockGoodsListService.addGoodsDiscountStatusHistory(any())).willReturn(0);

    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockGoodsListService.putApprovalProcessGoodsDiscount(null);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.PROGRAM_ERROR, myException.getMessageEnum());
    }

    @Test
    void test_상품할인상태이력등록_성공() {
        //given
    	given(mockGoodsListMapper.addGoodsDiscountStatusHistory(any())).willReturn(1);
        // when
		int result = mockGoodsListService.addGoodsDiscountStatusHistory(null);

        // then
		assertTrue(result == 1);
    }

    @Test
    void test_상품할인상태이력등록_실패() {
        //given
    	given(mockGoodsListMapper.addGoodsDiscountStatusHistory(any())).willReturn(0);
    	// when
    	int result = mockGoodsListService.addGoodsDiscountStatusHistory(null);

    	// then
    	assertFalse(result == 1);
    }

    void 상품목록_판매상태_조회_성공() {
        GoodsVo goodsVo = new GoodsVo();
        goodsVo.setGoodsId(1L);

        GoodsRegistRequestDto goodsData = goodsListService.getGoodsSaleStatusInfo(goodsVo);

        assertNotNull(goodsData);
    }

    @Test
    void 상품목록_판매상태_조회_실패() {
        GoodsVo goodsVo = new GoodsVo();

        GoodsRegistRequestDto goodsData = goodsListService.getGoodsSaleStatusInfo(goodsVo);

        assertFalse(goodsData != null);
    }

}
