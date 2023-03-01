package kr.co.pulmuone.v1.shopping.recently.service;

import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.BuyerFromMypageResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingRecentlyBizImpl implements ShoppingRecentlyBiz {

    @Autowired
    private ShoppingRecentlyService shoppingRecentlyService;

    @Autowired
    private UserBuyerBiz userBuyerBiz;


    /**
     * 최근 본 상품 추가
     *
     * @param userStatusType UserEnums.UserStatusType
     * @param urUserId String
     * @param urPcidCd String
     * @param ilGoodsId Long
     * @throws Exception
     */
    @Override
    public void viewsGoods(UserEnums.UserStatusType userStatusType, String urUserId, String urPcidCd, Long ilGoodsId) throws Exception {
        if(userStatusType.equals(UserEnums.UserStatusType.NONMEMBER)){
            // 비회원
            if (shoppingRecentlyService.getGoodsRecentlyViewByUrPcidCd(urPcidCd, ilGoodsId) != null) {
                shoppingRecentlyService.putRecentlyViewLastViewDateByPcidCd(urPcidCd, ilGoodsId);
            } else {
                shoppingRecentlyService.addRecentlyViewFromNonMember(urPcidCd, ilGoodsId);
                shoppingRecentlyService.delRecentlyViewLimitByPcidCd(urPcidCd);
            }
        }else{
            // 회원정보 조회
            BuyerFromMypageResultVo buyerInfo = userBuyerBiz.getBuyerFromMypage(Long.valueOf(urUserId));
            if (buyerInfo.getRecentlyViewYn().equals("N")) {
                return;
            }

            if (shoppingRecentlyService.getGoodsRecentlyView(urUserId, ilGoodsId) != null) {
                shoppingRecentlyService.putGoodsRecentlyViewLastViewDate(urUserId, ilGoodsId);
            } else {
                shoppingRecentlyService.addGoodsRecentlyView(urUserId, ilGoodsId);
                shoppingRecentlyService.delGoodsRecentlyViewLimit(urUserId);
            }
        }
    }

    /**
     * 최근 본 상품 조회
     *
     * @param dto GetRecentlyViewListByUserRequestDto
     * @return GetRecentlyViewListByUserResponseDto
     * @throws Exception exception
     */
    @Override
    public CommonGetRecentlyViewListByUserResponseDto getRecentlyViewListByUser(CommonGetRecentlyViewListByUserRequestDto dto) throws Exception {
        return shoppingRecentlyService.getRecentlyViewListByUser(dto);
    }

    /**
     * 최근 본 상품 삭제
     *
     * @param ilGoodsId Long
     * @param urUserId  Long
     * @throws Exception exception
     */
    @Override
    public void delRecentlyViewByGoodsId(Long ilGoodsId, Long urUserId) throws Exception {
        shoppingRecentlyService.delRecentlyViewByGoodsId(ilGoodsId, urUserId);
    }

    @Override
    public void delRecentlyViewByUserId(Long urUserId) throws Exception {
        shoppingRecentlyService.delRecentlyViewByUserId(urUserId);
    }

    @Override
    public void mapRecentlyViewUserId(String urPcidCd, Long urUserId) throws Exception {
        shoppingRecentlyService.mapRecentlyViewUserId(urPcidCd, urUserId);
    }

}
