package kr.co.pulmuone.v1.shopping.recently.service;

import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserResponseDto;

public interface ShoppingRecentlyBiz {

    void viewsGoods(UserEnums.UserStatusType userStatusType, String urUserId, String urPcidCd, Long ilGoodsId) throws Exception;

    CommonGetRecentlyViewListByUserResponseDto getRecentlyViewListByUser(CommonGetRecentlyViewListByUserRequestDto dto) throws Exception;

    void delRecentlyViewByGoodsId(Long ilGoodsId, Long urUserId) throws Exception;

    void delRecentlyViewByUserId(Long urUserId) throws Exception;

    void mapRecentlyViewUserId(String urPcidCd, Long urUserId) throws Exception;

}
