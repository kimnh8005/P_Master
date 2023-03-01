package kr.co.pulmuone.v1.goods.item.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceApprovalRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceDelRequestDto;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalItemRegistRequestDto;
import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemMapper;
import kr.co.pulmuone.v1.goods.item.dto.ItemApprovalResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemInfoVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemListVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class GoodsItemServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    GoodsItemService goodsItemService;

    @InjectMocks
	private GoodsItemService mockGoodsItemService;

	@Mock
	private GoodsItemMapper mockGoodsItemMapper;

	@BeforeEach
	void setUp() {
        preLogin();
	    mockGoodsItemService = new GoodsItemService(mockGoodsItemMapper);
	}

    @Test
    void 상품ID_별_품목상세_조회_성공() {
        Long goodsId = 15488L;

        log.info("상품ID_별_품목상세_조회_성공 goodsId :{}", goodsId);

        ItemInfoVo itemInfoVo = goodsItemService.getGoodsIdByItemInfo(goodsId);

        assertEquals(goodsId, itemInfoVo.getGoodsId());
    }

    @Test
    void 상품ID_별_품목상세_조회_실패() {
        Long goodsId = 0L;

        log.info("상품ID_별_품목상세_조회_실패 goodsId :{}", goodsId);

        ItemInfoVo itemInfoVo = goodsItemService.getGoodsIdByItemInfo(goodsId);

        assertFalse(!ObjectUtils.isEmpty(itemInfoVo));
    }

    @Test
    void 마스터_품목_리스트_조회_성공() {

        String ilItemCode = "0915553";

        MasterItemListRequestDto masterItemListRequestDto = new MasterItemListRequestDto();

        masterItemListRequestDto.setPage(1); // 페이지네이션 기본값 세팅
        masterItemListRequestDto.setPageSize(20);

        masterItemListRequestDto.setIlItemCodeKind("1"); // 품목 코드로 검색
        masterItemListRequestDto.setIlItemCode(ilItemCode);

        Page<MasterItemListVo> itemList = goodsItemService.getItemList(masterItemListRequestDto);

        // 해당 품목코드로 1건 또는 0건 조회되어야 함
        assertTrue(itemList.size() == 1 || itemList.size() == 0);

        // 해당 품목코드로 1건 조회시 품목 코드 일치 / 1건 아닌 경우 0건 조회되어야 함
        assertTrue((itemList.size() == 1 ? itemList.get(0).getIlItemCode().equals(ilItemCode) : itemList.size() == 0));

    }

    @Test
    void 마스터_품목_리스트_조회_실패() {

        String ilItemCode = "999"; // 존재하지 않는 품목 코드

        MasterItemListRequestDto masterItemListRequestDto = new MasterItemListRequestDto();

        masterItemListRequestDto.setPage(1); // 페이지네이션 기본값 세팅
        masterItemListRequestDto.setPageSize(20);

        masterItemListRequestDto.setIlItemCodeKind("1"); // 품목 코드로 검색
        masterItemListRequestDto.setIlItemCode(ilItemCode);

        Page<MasterItemListVo> itemList = goodsItemService.getItemList(masterItemListRequestDto);

        assertFalse(itemList.size() != 0); // 0건 조회가 아닌 경우 실패 처리

    }

    @Test
    void 마스터_품목_리스트_엑셀_다운로드_목록_조회_성공() {

        String ilItemCode = "0915553";

        MasterItemListRequestDto masterItemListRequestDto = new MasterItemListRequestDto();

        masterItemListRequestDto.setIlItemCodeKind("1"); // 품목 코드로 검색
        masterItemListRequestDto.setIlItemCode(ilItemCode);

        List<MasterItemListVo> itemExcelList = goodsItemService.getItemListExcel(masterItemListRequestDto);

        // 해당 품목코드로 1건 또는 0건 조회되어야 함
        assertTrue(itemExcelList.size() == 1 || itemExcelList.size() == 0);

        // 해당 품목코드로 1건 조회시 품목 코드 일치 / 1건 아닌 경우 0건 조회되어야 함
        assertTrue((itemExcelList.size() == 1 ? itemExcelList.get(0).getIlItemCode().equals(ilItemCode) : itemExcelList.size() == 0));

    }

    @Test
    void 마스터_품목_리스트_엑셀_다운로드_목록_조회_실패() {

        String ilItemCode = "999"; // 존재하지 않는 품목 코드

        MasterItemListRequestDto masterItemListRequestDto = new MasterItemListRequestDto();

        masterItemListRequestDto.setIlItemCodeKind("1"); // 품목 코드로 검색
        masterItemListRequestDto.setIlItemCode(ilItemCode);

        List<MasterItemListVo> itemExcelList = goodsItemService.getItemListExcel(masterItemListRequestDto);

        assertFalse(itemExcelList.size() != 0); // 0건 조회가 아닌 경우 실패 처리

    }

    @Test
    void test_품목등록상태이력등록_성공() {
    	//given
    	given(mockGoodsItemMapper.addItemRegistStatusHistory(any())).willReturn(1);
        // when
		int result = mockGoodsItemService.addItemRegistStatusHistory(null);

        // then
		assertTrue(result == 1);
    }

    @Test
    void test_품목등록상태이력등록_실패() {
    	//given
    	given(mockGoodsItemMapper.addItemRegistStatusHistory(any())).willReturn(0);
    	// when
    	int result = mockGoodsItemService.addItemRegistStatusHistory(null);

    	// then
    	assertFalse(result == 1);
    }
    @Test
    void test_품목등록승인목록조회_결과있음() {
    	//given
    	ApprovalItemRegistRequestDto requestDto = new ApprovalItemRegistRequestDto();

    	// when
    	ItemApprovalResponseDto result = goodsItemService.getApprovalItemRegistList(requestDto);

    	// then
    	assertNotNull(result.getRows());
    }
    @Test
    void test_품목등록승인목록조회_결과없음() {
    	//given
    	ApprovalItemRegistRequestDto requestDto = new ApprovalItemRegistRequestDto();

    	// when
    	ItemApprovalResponseDto result = goodsItemService.getApprovalItemRegistList(requestDto);

    	// then
    	assertTrue(CollectionUtils.isEmpty(result.getRows()));
    }
    @Test
    void test_품목등록승인요청철회_성공() throws Exception {
    	//given
    	given(mockGoodsItemMapper.putCancelRequestApprovalItemRegist(any())).willReturn(1);
    	given(mockGoodsItemService.addItemRegistStatusHistory(any())).willReturn(1);
    	// when
    	MessageCommEnum result = mockGoodsItemService.putCancelRequestApprovalItemRegist(null);

    	// then
        assertTrue(result.getCode().equals(ApiResult.success().getCode()));
    }

    @Test
    void test_품목등록승인요청철회_실패() throws Exception {
    	//given
    	given(mockGoodsItemMapper.putCancelRequestApprovalItemRegist(any())).willReturn(0);
    	given(mockGoodsItemService.addItemRegistStatusHistory(any())).willReturn(0);

    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockGoodsItemService.putCancelRequestApprovalItemRegist(null);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.PROGRAM_ERROR, myException.getMessageEnum());
    }

    @Test
    void test_품목등록승인처리_성공() throws Exception {
    	//given
    	given(mockGoodsItemMapper.putApprovalProcessItemRegist(any())).willReturn(1);
    	given(mockGoodsItemService.addItemRegistStatusHistory(any())).willReturn(1);
    	// when
    	MessageCommEnum result = mockGoodsItemService.putCancelRequestApprovalItemRegist(null);

    	// then
    	assertTrue(result.getCode().equals(ApiResult.success().getCode()));
    }
    @Test
    void test_품목등록승인처리_실패() throws Exception {
    	//given
    	given(mockGoodsItemMapper.putApprovalProcessItemRegist(any())).willReturn(0);
    	given(mockGoodsItemService.addItemRegistStatusHistory(any())).willReturn(0);

    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockGoodsItemService.putApprovalProcessItemRegist(null);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.PROGRAM_ERROR, myException.getMessageEnum());
    }


    @Test
    void test_품목가격상태이력등록_성공() {
    	//given
    	given(mockGoodsItemMapper.addItemPriceStatusHistory(any())).willReturn(1);
        // when
		int result = mockGoodsItemService.addItemPriceStatusHistory(null);

        // then
		assertTrue(result == 1);
    }

    @Test
    void test_품목가격상태이력등록_실패() {
    	//given
    	given(mockGoodsItemMapper.addItemPriceStatusHistory(any())).willReturn(0);
    	// when
    	int result = mockGoodsItemService.addItemPriceStatusHistory(null);

    	// then
    	assertFalse(result == 1);
    }
    @Test
    void test_품목가격승인목록조회_결과있음() {
    	//given
    	ApprovalItemRegistRequestDto requestDto = new ApprovalItemRegistRequestDto();

    	// when
    	ItemApprovalResponseDto result = goodsItemService.getApprovalItemPriceList(requestDto);

    	// then
    	assertNotNull(result.getRows());
    }
    @Test
    void test_품목가격승인목록조회_결과없음() {
    	//given
    	ApprovalItemRegistRequestDto requestDto = new ApprovalItemRegistRequestDto();

    	// when
    	ItemApprovalResponseDto result = goodsItemService.getApprovalItemPriceList(requestDto);

    	// then
    	assertTrue(CollectionUtils.isEmpty(result.getRows()));
    }
    @Test
    void test_품목가격승인요청철회_성공() throws Exception {
    	//given
    	given(mockGoodsItemMapper.putCancelRequestApprovalItemPrice(any())).willReturn(1);
    	given(mockGoodsItemService.addItemPriceStatusHistory(any())).willReturn(1);
    	// when
    	MessageCommEnum result = mockGoodsItemService.putCancelRequestApprovalItemPrice(null);

    	// then
        assertTrue(result.getCode().equals(ApiResult.success().getCode()));
    }

    @Test
    void test_품목가격승인요청철회_실패() throws Exception {
    	//given
    	given(mockGoodsItemMapper.putCancelRequestApprovalItemPrice(any())).willReturn(0);
    	given(mockGoodsItemService.addItemPriceStatusHistory(any())).willReturn(0);

    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockGoodsItemService.putCancelRequestApprovalItemPrice(null);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.PROGRAM_ERROR, myException.getMessageEnum());
    }

    @Test
    void test_품목가격승인처리_성공() throws Exception {
    	//given
    	given(mockGoodsItemMapper.putApprovalProcessItemPrice(any())).willReturn(1);
    	given(mockGoodsItemService.addItemPriceStatusHistory(any())).willReturn(1);
    	// when
    	MessageCommEnum result = mockGoodsItemService.putCancelRequestApprovalItemPrice(null);

    	// then
    	assertTrue(result.getCode().equals(ApiResult.success().getCode()));
    }
    @Test
    void test_품목가격승인처리_실패() throws Exception {
    	//given
    	given(mockGoodsItemMapper.putApprovalProcessItemPrice(any())).willReturn(0);
    	given(mockGoodsItemService.addItemPriceStatusHistory(any())).willReturn(0);

    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockGoodsItemService.putApprovalProcessItemPrice(null);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.PROGRAM_ERROR, myException.getMessageEnum());
    }


    @Test
    void getApprovalDeniedInfo_조회_성공() {
	    //given
        Long ilItemPriceApprId = 4L;

        //when
        String result = goodsItemService.getApprovalDeniedInfo(ilItemPriceApprId);

        //then
        assertTrue(result.length() > 0);
    }

    @Test
    void addItemPriceOrig_품목가격_반영_성공() throws Exception {
	    //given
        ApprovalStatusVo approvalVo = new ApprovalStatusVo();
        approvalVo.setTaskPk("2");
        approvalVo.setCreateId("0");

        //when
        MessageCommEnum result = goodsItemService.addItemPriceOrigByApproval(approvalVo);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result);
    }

    @Test
    void delItemPriceOrig_품목가격_삭제_성공() throws Exception {
	    //given
        ItemPriceDelRequestDto dto = new ItemPriceDelRequestDto();
        dto.setIlItemPriceOrigId("9493");

        //when
        MessageCommEnum result = goodsItemService.delItemPriceOrig(dto);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result);
    }

    @Test
    void addApprovalItemPrice_품목가격_승인요청_성공() throws Exception {
	    //given
        ItemPriceApprovalRequestDto dto = new ItemPriceApprovalRequestDto();
        dto.setIlItemCode("111");
        dto.setPriceApplyStartDate("20210518");
        dto.setStandardPrice("100");
        dto.setStandardPriceChange("100");
        dto.setRecommendedPrice("200");
        dto.setRecommendedPriceChange("100");
        dto.setApprovalSubUserId("1");
        dto.setApprovalUserId("1");
        dto.setApprovalRequestUserId(0);
        dto.setCreateId(0);

        //when
        MessageCommEnum result = goodsItemService.addApprovalItemPrice(dto);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result);
    }

}
