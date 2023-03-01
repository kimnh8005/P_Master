package kr.co.pulmuone.v1.promotion.shoplive.service;

import com.google.gson.Gson;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveRequestDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.vo.ShopliveGoodsVo;
import kr.co.pulmuone.v1.promotion.shoplive.dto.vo.ShopliveInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
class ShopliveEventServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private ShopliveEventService shopliveEventService;

    private Gson gson = new Gson();

    @Test
    void getShopliveGoodsList() throws Exception {
        log.info("==============  getShopliveGoodsList start ===========");
        String abc = shopliveEventService.getShopliveGoodsList();
        Map shopliveGoodsResultMap = gson.fromJson(abc, HashMap.class);
        List shopliveGoodsList = (List)shopliveGoodsResultMap.get("results");
        List<String> shopliveGoodsSkuList = new ArrayList<>();
        for(Object shopliveGoodsObject: shopliveGoodsList) {
            Map shopliveGoods = (Map)shopliveGoodsObject;
            shopliveGoodsSkuList.add(String.valueOf(shopliveGoods.get("sku").toString()));
        }
        log.info("shopliveGoodsSkuList.toString()" + shopliveGoodsSkuList.toString());
        log.info("============== getShopliveGoodsList end ===========");
    }

    @Test
    void putShopliveGoodsSync() throws Exception {
        List<ShopliveGoodsVo> goodsList= new ArrayList<>();
        ShopliveGoodsVo goodsVo = new ShopliveGoodsVo();
        goodsVo.setIlGoodsId("15");
        goodsVo.setGoodsNm("상품테스트");
        goodsVo.setGoodsImagePath("/BOS/il/test/2020/09/29/640_3BB7F61D72FA4F389A61.jpg");
        goodsVo.setDpBrandNm("Test");
        goodsVo.setRecommendedPrice(6000);
        goodsVo.setSalePrice(4400);
        goodsVo.setDescription("테스트 상품입니다.");
        ShopliveGoodsVo goodsVo2 = new ShopliveGoodsVo();
        goodsVo2.setIlGoodsId("90018112");
        goodsVo2.setGoodsNm("상품테스트2");
        goodsVo2.setGoodsImagePath("/BOS/il/2021/04/27/640_D9F6FD148A5947168E0A.jpg");
        goodsVo2.setDpBrandNm("Test");
        goodsVo2.setRecommendedPrice(8000);
        goodsVo2.setSalePrice(4500);
        goodsVo2.setDescription("테스트 2 상품입니다.");
        goodsList.add(goodsVo);
        goodsList.add(goodsVo2);

        log.info(shopliveEventService.postShopliveGoodsSync(goodsList));
    }

    @Test
    void getRemoteShopliveInfo() throws Exception {
        log.info("==============  getRemoteShopliveInfo start ===========");
        ShopliveRequestDto dto = new ShopliveRequestDto();
        dto.setCampaignKey("f9fb6f3198e0");
        ShopliveInfoVo abc = shopliveEventService.getRemoteShopliveInfo(dto);

        log.info("ShopliveInfoVo.toString()" + abc.toString());
        log.info("==============  getRemoteShopliveInfo finish ===========");
    }

    @Test
    void deleteShopliveGoods() throws Exception {
        shopliveEventService.deleteShopliveGoods("90018112");
    }
}