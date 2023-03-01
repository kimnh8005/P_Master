package kr.co.pulmuone.v1.shopping.recently.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserResponseDto;

import java.util.HashMap;

import kr.co.pulmuone.v1.shopping.recently.dto.vo.RecentlyViewVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingRecentlyServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    ShoppingRecentlyService shoppingRecentlyService;

    @Test
    void getRecentlyViewListByUser_조회_정상() throws Exception {
        //given
        CommonGetRecentlyViewListByUserRequestDto dto = new CommonGetRecentlyViewListByUserRequestDto();
        dto.setUrUserId(1646893L);
        dto.setPage(0);
        dto.setLimit(20);

        //when
        CommonGetRecentlyViewListByUserResponseDto result = shoppingRecentlyService.getRecentlyViewListByUser(dto);

        //then
        assertTrue(result.getGoodsIdList().size() > 0);
    }

    @Test
    void getRecentlyViewListByUser_조회_조회내역없음() throws Exception {
        //given
        CommonGetRecentlyViewListByUserRequestDto dto = new CommonGetRecentlyViewListByUserRequestDto();
        dto.setUrUserId(null);
        dto.setPage(0);
        dto.setLimit(20);

        //when
        CommonGetRecentlyViewListByUserResponseDto result = shoppingRecentlyService.getRecentlyViewListByUser(dto);

        //then
        assertTrue(result.getGoodsIdList().size() == 0);
    }

    @Test
    void delRecentlyViewByGoodsId() throws Exception {
        //given
        Long ilGoodsId = 1359L;
        Long urUserId = 1646893L;

        //when, then
        shoppingRecentlyService.delRecentlyViewByGoodsId(ilGoodsId, urUserId);
    }

    @Test
    void getGoodsRecentlyView_성공() throws Exception{
    	String urUserId = "1646831";
    	Long ilGoodsId = 15373L;

    	HashMap<String, Object> goodsRecentlyView = shoppingRecentlyService.getGoodsRecentlyView(urUserId, ilGoodsId);

    	Assertions.assertNotNull(goodsRecentlyView);
    }

    @Test
    void getGoodsRecentlyView_조회결과없음() throws Exception{
    	String urUserId = "1";
    	Long ilGoodsId = 1359L;

    	HashMap<String, Object> goodsRecentlyView = shoppingRecentlyService.getGoodsRecentlyView(urUserId, ilGoodsId);

    	Assertions.assertNull(goodsRecentlyView);
    }

    @Test
    void addGoodsRecentlyView() throws Exception{
    	String urUserId = "100";
    	Long ilGoodsId = 177L;

    	shoppingRecentlyService.addGoodsRecentlyView(urUserId, ilGoodsId);
    }

    @Test
    void addGoodsRecentlyView_실패() throws Exception{
    	String urUserId = "TEST";
    	Long ilGoodsId = 175L;

    	Assertions.assertThrows(Exception.class, () -> {
    		shoppingRecentlyService.addGoodsRecentlyView(urUserId, ilGoodsId);
    	});
    }

    @Test
    void putGoodsRecentlyViewLastViewDate() throws Exception{
    	String urUserId = "100";
    	Long ilGoodsId = 175L;

    	shoppingRecentlyService.putGoodsRecentlyViewLastViewDate(urUserId, ilGoodsId);
    }

    @Test
    void delGoodsRecentlyViewLimit() throws Exception{
    	String urUserId = "100";

    	shoppingRecentlyService.delGoodsRecentlyViewLimit(urUserId);
    }

    @Test
    void delRecentlyViewByUserId() throws Exception{
        //given
        Long urUserId = 0L;

        //when, then
        shoppingRecentlyService.delRecentlyViewByUserId(urUserId);
    }

    @Test
    void getGoodsRecentlyViewByUrPcidCd_조회_정상() throws Exception {
        //given
        String urPcidCd = "de45b908-5c3e-4342-a34b-5c381193b001";
        Long ilGoodsId = 900043L;

        //when
        RecentlyViewVo result = shoppingRecentlyService.getGoodsRecentlyViewByUrPcidCd(urPcidCd, ilGoodsId);

        //then
        assertTrue(result.getSpRecentlyViewId() > 0L);
    }

    @Test
    void addRecentlyViewFromNonMember_추가_정상() throws Exception {
        //given
        String urPcidCd = "test";
        Long ilGoodsId = 2433L;

        //when, then
        shoppingRecentlyService.addRecentlyViewFromNonMember(urPcidCd, ilGoodsId);
    }

    @Test
    void putRecentlyViewLastViewDateByPcidCd_수정_정상() throws Exception {
        //given
        String urPcidCd = "0024471b-69ef-4c11-8b22-b5b7dbe68806";
        Long ilGoodsId = 2433L;

        //when, then
        shoppingRecentlyService.putRecentlyViewLastViewDateByPcidCd(urPcidCd, ilGoodsId);
    }

    @Test
    void delRecentlyViewLimitByPcidCd_삭제_정상() throws Exception {
        //given
        String urPcidCd = "0024471b-69ef-4c11-8b22-b5b7dbe68806";

        //when, then
        shoppingRecentlyService.delRecentlyViewLimitByPcidCd(urPcidCd);
    }

    @Test
    void mapRecentlyViewUserId() throws Exception {
        //given
        String urPcidCd = "test";
        Long urUserId = 0L;

        //when, then
        shoppingRecentlyService.mapRecentlyViewUserId(urPcidCd, urUserId);
    }

}