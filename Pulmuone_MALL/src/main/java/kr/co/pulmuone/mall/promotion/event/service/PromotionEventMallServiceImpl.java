package kr.co.pulmuone.mall.promotion.event.service;

import kr.co.pulmuone.mall.user.buyer.service.UserBuyerMallService;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.event.dto.*;
import kr.co.pulmuone.v1.promotion.event.service.PromotionEventBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class PromotionEventMallServiceImpl implements PromotionEventMallService {
    @Autowired
    PromotionEventBiz promotionEventBiz;

    @Autowired
    private UserBuyerMallService userBuyerMallService;

    @Override
    public ApiResult<?> getEventListByUser(EventListByUserRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        dto.setUserStatus(userBuyerMallService.getUserStatus(buyerVo).getCode());
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        return ApiResult.success(promotionEventBiz.getEventListByUser(dto));
    }

    @Override
    public ApiResult<?> getNormalByUser(HttpServletRequest request, Long evEventId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        return promotionEventBiz.getNormalByUser(EventRequestDto.builder()
                .evEventId(evEventId)
                .deviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode())
                .urGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null)
                .urUserId(getUrUserId(buyerVo))
                .userStatus(userBuyerMallService.getUserStatus(buyerVo).getCode())
                .previewYn(getPreviewYn(request))
                .build());
    }

    @Override
    @UserMaskingRun(system = "MALL")
    public ApiResult<?> addNormalJoin(NormalJoinRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        dto.setUrUserId(getUrUserId(buyerVo));
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        dto.setUserStatus(userBuyerMallService.getUserStatus(buyerVo).getCode());
        dto.setUrGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null);

        return promotionEventBiz.addNormalJoin(dto);
    }

    @Override
    @UserMaskingRun(system = "MALL")
    public ApiResult<?> getNormalJoinByUser(EventJoinByUserRequestDto dto) throws Exception {
        return ApiResult.success(promotionEventBiz.getNormalJoinByUser(dto));
    }

    @Override
    public ApiResult<?> getStampByUser(HttpServletRequest request, Long evEventId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        return promotionEventBiz.getStampByUser(EventRequestDto.builder()
                .evEventId(evEventId)
                .deviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode())
                .urGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null)
                .urUserId(getUrUserId(buyerVo))
                .userStatus(userBuyerMallService.getUserStatus(buyerVo).getCode())
                .previewYn(getPreviewYn(request))
                .build());
    }

    @Override
    public ApiResult<?> addStampJoin(StampJoinRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        dto.setUrUserId(getUrUserId(buyerVo));
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        dto.setUserStatus(userBuyerMallService.getUserStatus(buyerVo).getCode());
        dto.setUrGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null);

        return promotionEventBiz.addStampJoin(dto);
    }

    @Override
    public ApiResult<?> getRouletteByUser(HttpServletRequest request, Long evEventId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        return promotionEventBiz.getRouletteByUser(EventRequestDto.builder()
                .evEventId(evEventId)
                .deviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode())
                .urGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null)
                .urUserId(getUrUserId(buyerVo))
                .userStatus(userBuyerMallService.getUserStatus(buyerVo).getCode())
                .previewYn(getPreviewYn(request))
                .build());
    }

    @Override
    public ApiResult<?> addRouletteJoin(RouletteJoinRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        dto.setUrUserId(getUrUserId(buyerVo));
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        dto.setUserStatus(userBuyerMallService.getUserStatus(buyerVo).getCode());
        dto.setUrGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null);

        return promotionEventBiz.addRouletteJoin(dto);
    }

    @Override
    public ApiResult<?> getSurveyByUser(HttpServletRequest request, Long evEventId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        return promotionEventBiz.getSurveyByUser(EventRequestDto.builder()
                .evEventId(evEventId)
                .deviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode())
                .urGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null)
                .urUserId(getUrUserId(buyerVo))
                .userStatus(userBuyerMallService.getUserStatus(buyerVo).getCode())
                .previewYn(getPreviewYn(request))
                .build());
    }

    @Override
    public ApiResult<?> addSurveyJoin(SurveyJoinRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        dto.setUrUserId(getUrUserId(buyerVo));
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        dto.setUserStatus(userBuyerMallService.getUserStatus(buyerVo).getCode());
        dto.setUrGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null);

        return promotionEventBiz.addSurveyJoin(dto);
    }

    @Override
    public ApiResult<?> getExperienceByUser(HttpServletRequest request, Long evEventId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        return promotionEventBiz.getExperienceByUser(EventRequestDto.builder()
                .evEventId(evEventId)
                .deviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode())
                .urGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null)
                .urUserId(getUrUserId(buyerVo))
                .userStatus(userBuyerMallService.getUserStatus(buyerVo).getCode())
                .previewYn(getPreviewYn(request))
                .build());
    }

    @Override
    @UserMaskingRun(system = "MALL")
    public ApiResult<?> getExperienceJoinByUser(EventJoinByUserRequestDto dto) throws Exception {
        return ApiResult.success(promotionEventBiz.getExperienceJoinByUser(dto));
    }

    @Override
    @UserMaskingRun(system = "MALL")
    public ApiResult<?> addExperienceJoin(ExperienceJoinRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        dto.setUrUserId(getUrUserId(buyerVo));
        dto.setDeviceType(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
        dto.setUserStatus(userBuyerMallService.getUserStatus(buyerVo).getCode());
        dto.setUrGroupId(buyerVo != null ? buyerVo.getUrGroupId() : null);

        return promotionEventBiz.addExperienceJoin(dto);
    }

    @Override
    public ApiResult<?> getGroupList(Long evEventId, String deviceType, String userType) throws Exception {
        return ApiResult.success(promotionEventBiz.getGroupList(evEventId, deviceType, userType));
    }

    private Long getUrUserId(BuyerVo buyerVo) {
        long urUserIdL = 0L;
        String urUserId = "";
        if (buyerVo != null) urUserId = StringUtil.nvl(buyerVo.getUrUserId());
        if (StringUtil.isNotEmpty(urUserId)) urUserIdL = Long.parseLong(urUserId);
        return urUserIdL;
    }

    /**
     * request Referer 의 preview 여부를 세팅하는 함수
     */
    private String getPreviewYn(HttpServletRequest request) {
        String previewYn = "N";
        String referer = request.getHeader("referer");
        if (StringUtils.isEmpty(referer)) {
            return previewYn;
        }
        int indexOf = referer.indexOf("?");
        if (indexOf == -1) return previewYn;
        String rtnPreview = getQueryStringValue(referer.substring(indexOf), "preview");
        if (!StringUtils.isEmpty(rtnPreview)) {
            previewYn = rtnPreview;
        }
        return previewYn;
    }

    /**
     * request 특정 querystring value 값 return
     */
    private String getQueryStringValue(String queryString, String key) {
        String rtnValue = "";
        String[] parameters = queryString.split("&");
        for (String parameter : parameters) {
            String[] keyValuePair = parameter.split("=");
            if (keyValuePair.length == 2) {
                if (keyValuePair[0].trim().equals(key)) {
                    return keyValuePair[1].trim();
                }
            }
        }
        return rtnValue;
    }
}
