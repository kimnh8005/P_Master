package kr.co.pulmuone.v1.scenario;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CompanyEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.discount.service.GoodsDiscountBiz;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDiscountRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPriceInfoResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsListBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsRegistBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsRegistBizImpl;
import kr.co.pulmuone.v1.goods.item.dto.ItemRegisterRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPriceVo;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemModifyBiz;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemRegisterBiz;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class PriceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private GoodsItemModifyBiz goodsItemModifyBiz;

    @Autowired
    private GoodsItemRegisterBiz goodsItemRegisterBiz;

    @Autowired
    private GoodsRegistBiz goodsRegistBiz;

    @Autowired
    private GoodsRegistBizImpl goodsRegistBizImpl;

    @Autowired
    private GoodsDiscountBiz goodsDiscountBiz;

    @Autowired
    private GoodsListBiz goodsListBiz;

    private Long gIlGoodsId;
    private String gIlItemCd;

    private void bosLogin() {
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLoginName("포비즈");
        userVO.setUserType(null);
        userVO.setStatusType("EMPLOYEE_STATUS.NORMAL");
        userVO.setCompanyName("(주)풀무원");
        userVO.setRoleId(Constants.ADMIN_LEVEL_1_AUTH_ST_ROLE_TP_ID);
        userVO.setPasswordChangeYn("N");
        userVO.setLastLoginElapsedDay(0);
        userVO.setConnectionId(53308);
        userVO.setTemporaryYn("N");
        userVO.setPersonalInformationAccessYn("N");
        userVO.setLangCode("1");
        userVO.setListAuthSupplierId(new ArrayList<>());
        userVO.setListAuthWarehouseId(new ArrayList<>());
        userVO.setListAuthStoreId(new ArrayList<>());
        userVO.setListAuthSellersId(new ArrayList<>());
        userVO.setCompanyType(CompanyEnums.CompanyType.HEADQUARTERS.getCode());
        SessionUtil.setUserVO(userVO);
    }

    private void given_price1() {
        gIlGoodsId = 90018292L;
        gIlItemCd = "PF301608";
    }

    @Test
    @DisplayName("가격")
    void 가격1() throws Exception {
        // given - 테스트 환경설정
        bosLogin();
        given_price1();

        // given - 2030-07-01 11000, 25000 등록
        ItemRegisterRequestDto itemRegisterRequestDto = new ItemRegisterRequestDto();
        List<ItemPriceVo> priceList = new ArrayList<>();
        ItemPriceVo itemPriceVo1 = new ItemPriceVo();
        itemPriceVo1.setApproval1st("");
        itemPriceVo1.setApprovalConfirm("");
        itemPriceVo1.setApprovalStatusCode(ApprovalEnums.ApprovalStatus.NONE.getCode());
        itemPriceVo1.setIlItemCode(gIlItemCd);
        itemPriceVo1.setPriceApplyEndDate("2999-12-31");
        itemPriceVo1.setPriceApplyStartDate("2030-07-01");
        itemPriceVo1.setPriceRatio(45);
        itemPriceVo1.setRecommendedPrice(25000);
        itemPriceVo1.setStandardPrice(11000);
        priceList.add(itemPriceVo1);
        itemRegisterRequestDto.setPriceList(priceList);

        // when - 2030-07-01 11000, 25000 등록
        ApiResult<?> addPriceResult = goodsItemRegisterBiz.addItemPriceOrig(itemRegisterRequestDto);

        // then - 2030-07-01 11000, 25000 등록
        assertEquals(BaseEnums.Default.SUCCESS, addPriceResult.getMessageEnum());

        // then - 2030-07-01 11000, 25000 등록 - 품목정보
        ItemPriceVo itemPriceVo = goodsItemModifyBiz.getItemPriceListByDate(gIlItemCd, "2030-06-30").get(0);
        assertEquals(10000, itemPriceVo.getStandardPrice());
        assertEquals(20000, itemPriceVo.getRecommendedPrice());
        itemPriceVo = goodsItemModifyBiz.getItemPriceListByDate(gIlItemCd, "2030-07-01").get(0);
        assertEquals(11000, itemPriceVo.getStandardPrice());
        assertEquals(25000, itemPriceVo.getRecommendedPrice());

        // then - 2030-07-01 11000, 25000 등록 - 상품정보
        GoodsPriceInfoResultVo goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-30 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-07-01 00:00:00").get(0);
        assertEquals(11000, goodsPriceVo.getStandardPrice());
        assertEquals(25000, goodsPriceVo.getRecommendedPrice());
        assertEquals(25000, goodsPriceVo.getSalePrice());

        // given - 2030-07-01 12000, 23000 업데이트
        priceList.clear();
        itemPriceVo1.setApproval1st("");
        itemPriceVo1.setApprovalConfirm("");
        itemPriceVo1.setApprovalStatusCode(ApprovalEnums.ApprovalStatus.NONE.getCode());
        itemPriceVo1.setIlItemCode(gIlItemCd);
        itemPriceVo1.setPriceApplyEndDate("2999-12-31");
        itemPriceVo1.setPriceApplyStartDate("2030-07-01");
        itemPriceVo1.setPriceRatio(45);
        itemPriceVo1.setRecommendedPrice(23000);
        itemPriceVo1.setStandardPrice(12000);
        priceList.add(itemPriceVo1);
        itemRegisterRequestDto.setPriceList(priceList);

        // when - 2030-07-01 12000, 23000 업데이트
        addPriceResult = goodsItemRegisterBiz.addItemPriceOrig(itemRegisterRequestDto);

        // then - 2030-07-01 12000, 23000 업데이트
        assertEquals(BaseEnums.Default.SUCCESS, addPriceResult.getMessageEnum());

        // then - 2030-07-01 12000, 23000 업데이트 - 품목정보
        itemPriceVo = goodsItemModifyBiz.getItemPriceListByDate(gIlItemCd, "2030-06-30").get(0);
        assertEquals(10000, itemPriceVo.getStandardPrice());
        assertEquals(20000, itemPriceVo.getRecommendedPrice());
        itemPriceVo = goodsItemModifyBiz.getItemPriceListByDate(gIlItemCd, "2030-07-01").get(0);
        assertEquals(12000, itemPriceVo.getStandardPrice());
        assertEquals(23000, itemPriceVo.getRecommendedPrice());

        // then - 2030-07-01 12000, 23000 업데이트 - 상품정보
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-30 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-07-01 00:00:00").get(0);
        assertEquals(12000, goodsPriceVo.getStandardPrice());
        assertEquals(23000, goodsPriceVo.getRecommendedPrice());
        assertEquals(23000, goodsPriceVo.getSalePrice());

        // given - 2030-07-01 삭제
        String priceApplyStartDate = "2030-07-01";

        // when - 2030-07-01 삭제
        int delResult = goodsItemModifyBiz.deleteItemPriceOrigin(gIlItemCd, priceApplyStartDate);

        // then - 2030-07-01 삭제
        assertEquals(1, delResult);

        // then - 2030-07-01 삭제 - 품목정보
        itemPriceVo = goodsItemModifyBiz.getItemPriceListByDate(gIlItemCd, "2030-06-30").get(0);
        assertEquals(10000, itemPriceVo.getStandardPrice());
        assertEquals(20000, itemPriceVo.getRecommendedPrice());
        itemPriceVo = goodsItemModifyBiz.getItemPriceListByDate(gIlItemCd, "2030-07-01").get(0);
        assertEquals(10000, itemPriceVo.getStandardPrice());
        assertEquals(20000, itemPriceVo.getRecommendedPrice());

        // then - 2030-07-01 삭제 - 상품정보
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-30 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-07-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());

        // given - 즉시할인 등록 (2030-02-01 00:00:00 ~ 2030-06-30 23:59:59) 18000
        GoodsDiscountRequestDto goodsDiscountRequestDto = new GoodsDiscountRequestDto();
        goodsDiscountRequestDto.setGoodsId(String.valueOf(gIlGoodsId));
        goodsDiscountRequestDto.setDiscountTypeCode(GoodsEnums.GoodsDiscountType.IMMEDIATE.getCode());
        goodsDiscountRequestDto.setDiscountStartDateTime("2030-02-01 00:00:00");
        goodsDiscountRequestDto.setDiscountEndDateTime("2030-06-30 23:59");
        goodsDiscountRequestDto.setDiscountMethodTypeCode(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode());
        goodsDiscountRequestDto.setDiscountRatio(0);
        goodsDiscountRequestDto.setDiscountSalePrice(18000);
        goodsDiscountRequestDto.setCreateId("0");

        // when - 즉시할인 등록 (2030-02-01 00:00:00 ~ 2030-06-30 23:59:59) 18000
        ApiResult<?> discountResult = goodsRegistBizImpl.addGoodsDiscountWithValidation(goodsDiscountRequestDto);
        goodsDiscountBiz.spGoodsPriceUpdateWhenGoodsDiscountChanges(String.valueOf(gIlGoodsId));

        // then - 즉시할인 등록 (2030-02-01 00:00:00 ~ 2030-06-30 23:59:59) 18000
        assertEquals(BaseEnums.Default.SUCCESS, discountResult.getMessageEnum());

        // then - 즉시할인 등록 (2030-02-01 00:00:00 ~ 2030-06-30 23:59:59) 18000 - 상품정보
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-01-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-02-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-30 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-07-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());

        // given - 행사할인 등록 (2030-03-01 00:00:00 ~ 2030-05-31 23:59:59) 16000
        goodsDiscountRequestDto.setGoodsId(String.valueOf(gIlGoodsId));
        goodsDiscountRequestDto.setDiscountTypeCode(GoodsEnums.GoodsDiscountType.ERP_EVENT.getCode());
        goodsDiscountRequestDto.setDiscountStartDateTime("2030-03-01 00:00:00");
        goodsDiscountRequestDto.setDiscountEndDateTime("2030-05-31 23:59");
        goodsDiscountRequestDto.setDiscountMethodTypeCode(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode());
        goodsDiscountRequestDto.setDiscountRatio(0);
        goodsDiscountRequestDto.setDiscountSalePrice(16000);
        goodsDiscountRequestDto.setCreateId("0");

        // when - 행사할인 등록 (2030-03-01 00:00:00 ~ 2030-05-31 23:59:59) 16000
        discountResult = goodsRegistBizImpl.addGoodsDiscountWithValidation(goodsDiscountRequestDto);
        goodsDiscountBiz.spGoodsPriceUpdateWhenGoodsDiscountChanges(String.valueOf(gIlGoodsId));

        // then - 행사할인 등록 (2030-03-01 00:00:00 ~ 2030-05-31 23:59:59) 16000
        assertEquals(BaseEnums.Default.SUCCESS, discountResult.getMessageEnum());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-01-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-02-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-02-28 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-03-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-05-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-30 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-07-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());

        // given - 우선할인 등록 (2030-04-01 00:00:00 ~ 2030-04-30 23:59:59) 14000
        goodsDiscountRequestDto.setGoodsId(String.valueOf(gIlGoodsId));
        goodsDiscountRequestDto.setDiscountTypeCode(GoodsEnums.GoodsDiscountType.PRIORITY.getCode());
        goodsDiscountRequestDto.setDiscountStartDateTime("2030-04-01 00:00:00");
        goodsDiscountRequestDto.setDiscountEndDateTime("2030-04-30 23:59");
        goodsDiscountRequestDto.setDiscountMethodTypeCode(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode());
        goodsDiscountRequestDto.setDiscountRatio(0);
        goodsDiscountRequestDto.setDiscountSalePrice(14000);
        goodsDiscountRequestDto.setCreateId("0");

        // when - 우선할인 등록 (2030-04-01 00:00:00 ~ 2030-04-30 23:59:59) 14000
        discountResult = goodsRegistBizImpl.addGoodsDiscountWithValidation(goodsDiscountRequestDto);
        goodsDiscountBiz.spGoodsPriceUpdateWhenGoodsDiscountChanges(String.valueOf(gIlGoodsId));

        // then - 우선할인 등록 (2030-04-01 00:00:00 ~ 2030-04-30 23:59:59) 14000
        assertEquals(BaseEnums.Default.SUCCESS, discountResult.getMessageEnum());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-01-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-02-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-02-28 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-03-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-03-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-04-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(14000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-04-30 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(14000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-05-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-05-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-30 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-07-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());

        // given - 우선할인 삭제 (2030-04-01 00:00:00 ~ 2030-04-30 23:59:59) 14000
        String discountTypeCode = GoodsEnums.GoodsDiscountType.PRIORITY.getCode();
        String discountStartDateTime = "2030-04-01 00:00:00";

        // when - 우선할인 삭제 (2030-04-01 00:00:00 ~ 2030-04-30 23:59:59) 14000
        int delDiscountResult = goodsDiscountBiz.putGoodsDiscount(gIlGoodsId, discountTypeCode, discountStartDateTime);
        goodsDiscountBiz.spGoodsPriceUpdateWhenGoodsDiscountChanges(String.valueOf(gIlGoodsId));

        // then - 우선할인 삭제 (2030-04-01 00:00:00 ~ 2030-04-30 23:59:59) 14000
        assertTrue(delDiscountResult > 0);
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-01-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-02-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-02-28 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-03-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-03-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-04-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-04-30 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-05-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-05-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(16000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-30 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-07-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());

        // given - 행사할인 삭제 (2030-03-01 00:00:00 ~ 2030-05-31 23:59:59) 16000
        discountTypeCode = GoodsEnums.GoodsDiscountType.ERP_EVENT.getCode();
        discountStartDateTime = "2030-03-01 00:00:00";

        // when - 행사할인 삭제 (2030-03-01 00:00:00 ~ 2030-05-31 23:59:59) 16000
        delDiscountResult = goodsDiscountBiz.putGoodsDiscount(gIlGoodsId, discountTypeCode, discountStartDateTime);
        goodsDiscountBiz.spGoodsPriceUpdateWhenGoodsDiscountChanges(String.valueOf(gIlGoodsId));

        // then - 행사할인 삭제 (2030-03-01 00:00:00 ~ 2030-05-31 23:59:59) 16000
        assertTrue(delDiscountResult > 0);
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-01-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-02-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-02-28 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-03-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-05-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-30 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(18000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-07-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());

        // given - 즉시할인 삭제 (2030-02-01 00:00:00 ~ 2030-06-30 23:59:59) 18000
        discountTypeCode = GoodsEnums.GoodsDiscountType.IMMEDIATE.getCode();
        discountStartDateTime = "2030-02-01 00:00:00";

        // when - 즉시할인 삭제 (2030-02-01 00:00:00 ~ 2030-06-30 23:59:59) 18000
        delDiscountResult = goodsDiscountBiz.putGoodsDiscount(gIlGoodsId, discountTypeCode, discountStartDateTime);
        goodsRegistBiz.spGoodsPriceUpdateWhenItemPriceChanges(gIlItemCd);
        goodsDiscountBiz.spGoodsPriceUpdateWhenGoodsDiscountChanges(String.valueOf(gIlGoodsId));

        // then - 즉시할인 삭제 (2030-02-01 00:00:00 ~ 2030-06-30 23:59:59) 18000
        assertTrue(delDiscountResult > 0);
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-01-31 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-02-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-06-30 23:59:59").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
        goodsPriceVo = goodsRegistBiz.getGoodsPriceList(gIlGoodsId, "2030-07-01 00:00:00").get(0);
        assertEquals(10000, goodsPriceVo.getStandardPrice());
        assertEquals(20000, goodsPriceVo.getRecommendedPrice());
        assertEquals(20000, goodsPriceVo.getSalePrice());
    }

}
