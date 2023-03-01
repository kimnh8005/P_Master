package kr.co.pulmuone.mall.promotion.exhibit.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import kr.co.pulmuone.v1.comm.util.CookieUtil;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.promotion.exhibit.dto.*;
import kr.co.pulmuone.v1.promotion.exhibit.service.PromotionExhibitBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.mall.user.buyer.service.UserBuyerMallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Service
public class PromotionExhibitMallServiceImpl implements PromotionExhibitMallService {
    @Autowired
    PromotionExhibitBiz promotionExhibitBiz;

    @Autowired
    GoodsGoodsBiz goodsGoodsBiz;

    @Autowired
    GoodsSearchBiz goodsSearchBiz;

    @Autowired
    private UserCertificationBiz userCertificationBiz;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserBuyerMallService userBuyerMallService;

    @Override
    public ApiResult<?> getExhibitListByUser(ExhibitListByUserRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        dto.setUserStatus(userBuyerMallService.getUserStatus(buyerVo).getCode());
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        return ApiResult.success(promotionExhibitBiz.getExhibitListByUser(dto));
    }

    @Override
    public ApiResult<?> getNormalByUser(Long evExhibitId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        return promotionExhibitBiz.getNormalByUser(ExhibitRequestDto.builder()
                .evExhibitId(evExhibitId)
                .deviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode())
                .urGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null)
                .userStatus(userBuyerMallService.getUserStatus(buyerVo).getCode())
                .urUserId(getUrUserId(buyerVo))
                .build());
    }

    @Override
    public ApiResult<?> getSelectListByUser(ExhibitListByUserRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        dto.setUserStatus(userBuyerMallService.getUserStatus(buyerVo).getCode());
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        return ApiResult.success(promotionExhibitBiz.getSelectListByUser(dto));
    }

    @Override
    public ApiResult<?> getSelectByUser(Long evExhibitId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        return promotionExhibitBiz.getSelectByUser(ExhibitRequestDto.builder()
                .evExhibitId(evExhibitId)
                .deviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode())
                .urGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null)
                .userStatus(userBuyerMallService.getUserStatus(buyerVo).getCode())
                .urErpEmployeeCode(buyerVo.getUrErpEmployeeCode())
                .urUserId(getUrUserId(buyerVo))
                .build());
    }

    @Override
    public ApiResult<?> getGiftByUser(Long evExhibitId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        return promotionExhibitBiz.getGiftByUser(ExhibitRequestDto.builder()
                .evExhibitId(evExhibitId)
                .deviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode())
                .urGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null)
                .userStatus(userBuyerMallService.getUserStatus(buyerVo).getCode())
                .urUserId(getUrUserId(buyerVo))
                .build());
    }

    @Override
    public ApiResult<?> getGreenJuicePageInfo(GreenJuicePageRequestDto dto) throws Exception {
        if(dto.getBuildingCode() == null || dto.getBuildingCode().equals("")){
            GetSessionShippingResponseDto shippingAddress = userCertificationBiz.getSessionShipping();
            dto.setBuildingCode(shippingAddress.getBuildingCode());
            dto.setZipCode(shippingAddress.getReceiverZipCode());
        }

        return ApiResult.success(goodsGoodsBiz.getGreenJuiceDailyCycleList(dto.getZipCode(), dto.getBuildingCode()));
    }

    @Override
    public ApiResult<?> getGreenJuiceGoods() throws Exception {
        List<Long> goodsIdList = promotionExhibitBiz.getGreenJuiceGoods(GoodsConstants.GREEN_JUICE_UR_SUPPLIER_ID);

        // '상품 통합검색' 상품ID로 조회
        GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                .needNonMemberShowMemberGoods(true)
                .goodsIdList(goodsIdList)
                .build();
        List<GoodsSearchResultDto> searchResultDto = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto);

        return ApiResult.success(searchResultDto);
    }

    @Override
    public ApiResult<?> addSelectOrder(SelectOrderRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        dto.setUrGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null);
        dto.setUserStatus(userBuyerMallService.getUserStatus(buyerVo).getCode());
        dto.setUrUserId(getUrUserId(buyerVo));
        dto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
        dto.setPmAdExternalCd(CookieUtil.getCookie(request, PromotionConstants.COOKIE_AD_EXTERNAL_CODE_KEY));
        return promotionExhibitBiz.addSelectOrder(dto);
    }

    @Override
    public ApiResult<?> addGreenJuiceOrder(GreenJuiceOrderRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        dto.setUrGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null);
        dto.setUserStatus(userBuyerMallService.getUserStatus(buyerVo).getCode());
        dto.setUrUserId(getUrUserId(buyerVo));
        dto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
        dto.setPmAdExternalCd(CookieUtil.getCookie(request, PromotionConstants.COOKIE_AD_EXTERNAL_CODE_KEY));
        return promotionExhibitBiz.addGreenJuiceOrder(dto);
    }

    private Long getUrUserId(BuyerVo vo) {
        long urUserIdL = 0L;
        String urUserId = "";
        if (vo != null) urUserId = StringUtil.nvl(vo.getUrUserId());
        if (StringUtil.isNotEmpty(urUserId)) urUserIdL = Long.parseLong(urUserId);

        return urUserIdL;
    }
}
