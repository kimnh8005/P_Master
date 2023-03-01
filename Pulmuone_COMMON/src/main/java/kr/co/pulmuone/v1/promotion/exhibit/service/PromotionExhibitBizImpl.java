package kr.co.pulmuone.v1.promotion.exhibit.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.PriceUtil;
import kr.co.pulmuone.v1.goods.goods.dto.EmployeeDiscountInfoDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingDataResultDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.DetailSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchAdditionalVo;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeBrandGroupByUserVo;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeByUserVo;
import kr.co.pulmuone.v1.policy.benefit.service.PolicyBenefitEmployeeBiz;
import kr.co.pulmuone.v1.promotion.exhibit.dto.*;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.*;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartPickGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.SpCartPickGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.service.ShoppingCartBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PromotionExhibitBizImpl implements PromotionExhibitBiz {

    @Autowired
    private PromotionExhibitService promotionExhibitService;

    @Autowired
    GoodsSearchBiz goodsSearchBiz;

    @Autowired
    GoodsGoodsBiz goodsGoodsBiz;

    @Autowired
    ShoppingCartBiz shoppingCartBiz;

    @Autowired
    PolicyBenefitEmployeeBiz policyBenefitEmployeeBiz;

    @Autowired
    public GoodsShippingTemplateBiz goodsShippingTemplateBiz;

    @Override
    public ExhibitListByUserResponseDto getExhibitListByUser(ExhibitListByUserRequestDto dto) throws Exception {
        return promotionExhibitService.getExhibitListByUser(dto);
    }

    @Override
    public ApiResult<?> getNormalByUser(ExhibitRequestDto dto) throws Exception {
        NormalByUserVo exhibitInfo = promotionExhibitService.getNormalByUser(dto.getEvExhibitId(), dto.getDeviceType());
        if (exhibitInfo == null) return ApiResult.result(ExhibitEnums.GetValidation.NO_EXHIBIT);

        // Event Validation Check
        ExhibitValidationRequestDto validationRequestDto = new ExhibitValidationRequestDto(dto, exhibitInfo);
        MessageCommEnum validation = promotionExhibitService.getExhibitValidation(validationRequestDto);
        if (!validation.getCode().equals(ExhibitEnums.GetValidation.PASS_VALIDATION.getCode())) {
            if (validation.getCode().equals(ExhibitEnums.GetValidation.NOT_GROUP.getCode()) ||
                    validation.getCode().equals(ExhibitEnums.GetValidation.NOT_DEVICE.getCode())) {
                return ApiResult.result(getExhibitReturnData(validation, validationRequestDto), validation);
            }
            return ApiResult.result(validation);
        }

        NormalByUserResponseDto result = new NormalByUserResponseDto(exhibitInfo);

        //상품 정보
        List<ExhibitGroupByUserVo> groupList = promotionExhibitService.getGroupByUser(dto.getEvExhibitId());
        for (ExhibitGroupByUserVo groupVo : groupList) {
            List<Long> groupGoodsIdList = promotionExhibitService.getGroupDetailByUser(groupVo.getEvExhibitGroupId());

            //상품 ID로 상품검색
            GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                    .goodsIdList(groupGoodsIdList)
                    .build();
            List<GoodsSearchResultDto> goodsList = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto).stream()
                    .limit(groupVo.getDisplayCount())
                    .collect(Collectors.toList());

            groupVo.setGoods(goodsList);
        }
        result.setGroup(groupList);

        // dDay 계산
        if (exhibitInfo.getAlwaysYn().equals("N")) {
            result.setDday(DateUtil.getBetweenDays(exhibitInfo.getEndDate()));
        }

        return ApiResult.success(result);
    }

    @Override
    public SelectListByUserResponseDto getSelectListByUser(ExhibitListByUserRequestDto dto) throws Exception {
        return promotionExhibitService.getSelectListByUser(dto);
    }

    @Override
    public ApiResult<?> getSelectByUser(ExhibitRequestDto dto) throws Exception {
        SelectByUserVo exhibitInfo = promotionExhibitService.getSelectByUser(dto.getEvExhibitId(), dto.getDeviceType());
        if (exhibitInfo == null) return ApiResult.result(ExhibitEnums.GetValidation.NO_EXHIBIT);

        // Common Validation Check
        ExhibitValidationRequestDto validationRequestDto = new ExhibitValidationRequestDto(dto, exhibitInfo);
        MessageCommEnum validation = promotionExhibitService.getExhibitValidation(validationRequestDto);
        if (!validation.getCode().equals(ExhibitEnums.GetValidation.PASS_VALIDATION.getCode())) {
            if (validation.getCode().equals(ExhibitEnums.GetValidation.NOT_GROUP.getCode()) ||
                    validation.getCode().equals(ExhibitEnums.GetValidation.NOT_DEVICE.getCode())) {
                return ApiResult.result(getExhibitReturnData(validation, validationRequestDto), validation);
            }
            return ApiResult.result(validation);
        }

        SelectByUserResponseDto result = new SelectByUserResponseDto(exhibitInfo);

        //상품 정보
        List<Long> groupGoodsIdList = promotionExhibitService.getSelectGoodsByUser(dto.getEvExhibitId());
        int lowLimitPrice = PriceUtil.getSelectPrice(exhibitInfo.getSelectPrice(), exhibitInfo.getDefaultBuyCount());

        // 대표상품 추가
        groupGoodsIdList.add(exhibitInfo.getIlGoodsId());
        result.setGoods(getSelectGoodsResult(groupGoodsIdList, dto, lowLimitPrice, exhibitInfo.getIlGoodsId()));

        boolean isEmployee = UserEnums.UserStatusType.EMPLOYEE.getCode().equals(dto.getUserStatus());
        List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo = null;
        if (isEmployee) {
            policyBenefitEmployeeInfo = policyBenefitEmployeeBiz.getEmployeeDiscountBrandByUser(dto.getUrErpEmployeeCode());
        }

        //추가상품 정보
        List<SelectAddGoodsVo> groupAddGoodsIdList = promotionExhibitService.getSelectAddGoods(dto.getEvExhibitId());
        for (SelectAddGoodsVo addGoodsVo : groupAddGoodsIdList) {
            List<GoodsSearchAdditionalVo> goodsSearchResult = goodsSearchBiz.getGoodsAdditional(Collections.singletonList(addGoodsVo.getIlGoodsId()));
            if (goodsSearchResult != null && goodsSearchResult.size() > 0) {
                addGoodsVo.setGoods(goodsSearchResult.get(0));
            }

            if (isEmployee) {
                PolicyBenefitEmployeeByUserVo findPolicyBenefitEmployeeVo = policyBenefitEmployeeBiz.findEmployeeDiscountBrandByUser(policyBenefitEmployeeInfo, addGoodsVo.getUrBrandId());

                PolicyBenefitEmployeeBrandGroupByUserVo policyBenefitEmployeeBrandByUserVo = policyBenefitEmployeeBiz.getDiscountRatioEmployeeDiscountBrand(findPolicyBenefitEmployeeVo, addGoodsVo.getUrBrandId());

                if (policyBenefitEmployeeBrandByUserVo != null) {
                    EmployeeDiscountInfoDto employeeDiscountInfoDto = goodsGoodsBiz.employeeDiscountCalculation(policyBenefitEmployeeBrandByUserVo.getDiscountRatio(), BuyerConstants.EMPLOYEE_MAX_POINT, addGoodsVo.getRecommendedPrice(), 1);
                    addGoodsVo.setSalePrice(employeeDiscountInfoDto.getDiscountAppliedPrice());
                }
            }
        }
        result.setAddGoods(groupAddGoodsIdList);

        // dDay 계산
        if (exhibitInfo.getAlwaysYn().equals("N")) {
            result.setDday(DateUtil.getBetweenDays(exhibitInfo.getEndDate()));
        }

        return ApiResult.success(result);
    }

    private List<SelectGoodsResultDto> getSelectGoodsResult(List<Long> groupGoodsIdList, ExhibitRequestDto dto, int lowLimitPrice, Long repGoodsId) throws Exception {
        List<SelectGoodsResultDto> resultList = new ArrayList<>();

        // 대표상품 정보
        BasicSelectGoodsVo repGoodsInfo = goodsGoodsBiz.getGoodsBasicInfo(GoodsRequestDto.builder().ilGoodsId(repGoodsId).build());
        ShippingDataResultDto repGoodsShippingInfo = goodsShippingTemplateBiz.getShippingInfo(repGoodsInfo.getSaleType(), repGoodsId, repGoodsInfo.getUrWareHouseId());

        if (groupGoodsIdList.size() > 0) {
            //상품 ID로 상품검색
            GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                    .goodsIdList(groupGoodsIdList)
                    .build();

            List<GoodsSearchResultDto> goodsList = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto);

            for (GoodsSearchResultDto goodsDto : goodsList) {
                // 상품 단가가 골라담기 단가보다 낮을 경우 제외 처리
                if (goodsDto.getSalePrice() < lowLimitPrice) {
                    continue;
                }
                // 비회원, 회원, 임직원 예외처리
                if (dto.getUserStatus().equals(UserEnums.UserStatusType.NONMEMBER.getCode())) {
                    if (goodsDto.getPurchaseNonMemberYn().equals("N")) continue;
                }
                if (dto.getUserStatus().equals(UserEnums.UserStatusType.MEMBER.getCode())) {
                    if (goodsDto.getPurchaseMemberYn().equals("N")) continue;
                }
                if (dto.getUserStatus().equals(UserEnums.UserStatusType.EMPLOYEE.getCode())) {
                    if (goodsDto.getPurchaseEmployeeYn().equals("N")) continue;
                }

                SelectGoodsResultDto result = new SelectGoodsResultDto(goodsDto);

                boolean isMember = !dto.getUserStatus().equals(UserEnums.UserStatusType.NONMEMBER.getCode());
                boolean isEmployee = dto.getUserStatus().equals(UserEnums.UserStatusType.EMPLOYEE.getCode());

                // 상품 기본 정보
                BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(GoodsRequestDto.builder()
                        .ilGoodsId(goodsDto.getGoodsId())
                        .deviceInfo(DeviceUtil.getDirInfo())
                        .isApp(DeviceUtil.isApp())
                        .isMember(isMember)
                        .isEmployee(isEmployee)
                        .isDawnDelivery(false)
                        .build());

                if (goods == null) continue; // 상품 기본정보 값 없을시 제외처리

                result.setStockQty(goods.getStockQty());
                ShippingDataResultDto goodsShippingInfo = goodsShippingTemplateBiz.getShippingInfo(goods.getSaleType(), goodsDto.getGoodsId(), goods.getUrWareHouseId());

                //대표상품 출고처 비교
                if (!repGoodsInfo.getUrWareHouseId().equals(goods.getUrWareHouseId())) continue;

                //대표상품 배송정책 비교
                if (!repGoodsShippingInfo.getDeliveryType().equals(goodsShippingInfo.getDeliveryType()) ||
                        !repGoodsShippingInfo.getShippingPriceText().equals(goodsShippingInfo.getShippingPriceText()) ||
                        !repGoodsShippingInfo.getAreaShippingYn().equals(goodsShippingInfo.getAreaShippingYn()) ||
                        repGoodsShippingInfo.getJejuShippingPrice() != goodsShippingInfo.getJejuShippingPrice() ||
                        repGoodsShippingInfo.getIslandShippingPrice() != goodsShippingInfo.getIslandShippingPrice()) {
                    continue;
                }

                // 상품 상세 정보
                DetailSelectGoodsVo goodsDetail = goodsGoodsBiz.getDetailSelectGoods(goodsDto.getGoodsId());
                result.setBasicDescription(goodsDetail.getBasicDescription());
                result.setDetailDescription(goodsDetail.getDetailDescription());

                resultList.add(result);
            }
        }

        return resultList;
    }

    @Override
    public ApiResult<?> addSelectOrder(SelectOrderRequestDto dto) throws Exception {
        SelectByUserVo exhibitInfo = promotionExhibitService.getSelectByUser(dto.getEvExhibitId(), dto.getDeviceType());
        if (exhibitInfo == null) return ApiResult.result(ExhibitEnums.GetValidation.NO_EXHIBIT);
        exhibitInfo.setGoodsList(promotionExhibitService.getSelectGoodsByUser(dto.getEvExhibitId()));

        // Common Validation Check
        ExhibitValidationRequestDto validationRequestDto = new ExhibitValidationRequestDto(dto, exhibitInfo);
        MessageCommEnum validation = promotionExhibitService.getExhibitValidation(validationRequestDto);
        if (!validation.getCode().equals(ExhibitEnums.GetValidation.PASS_VALIDATION.getCode())) {
            return ApiResult.result(validation);
        }

        // Select Validation Check
        ApiResult<?> selectValidation = promotionExhibitService.addSelectExhibitValidation(dto, exhibitInfo);
        if (!selectValidation.getMessageEnum().equals(ExhibitEnums.GetValidation.PASS_VALIDATION)) {
            return selectValidation;
        }

        // 장바구니_바로구매 진행
        AddCartPickGoodsRequestDto addCartDto = new AddCartPickGoodsRequestDto();
        addCartDto.setUrPcidCd(dto.getUrPcidCd());
        addCartDto.setUrUserId(dto.getUrUserId());
        addCartDto.setDeliveryType(ShoppingEnums.DeliveryType.NORMAL.getCode());
        addCartDto.setIlGoodsId(exhibitInfo.getIlGoodsId());
        addCartDto.setQty(1);
        addCartDto.setBuyNowYn(dto.getBuyNowYn());
        addCartDto.setCartPromotionType(ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode());
        addCartDto.setEvExhibitId(dto.getEvExhibitId());
        addCartDto.setPickGoodsList(dto.getPickGoodsList());
        addCartDto.setAdditionalGoodsList(dto.getAdditionalGoodsList());
        addCartDto.setPmAdExternalCd(dto.getPmAdExternalCd());
        addCartDto.setPmAdInternalPageCd(dto.getPmAdInternalPageCd());
        addCartDto.setPmAdInternalContentCd(dto.getPmAdInternalContentCd());

        return ApiResult.success(SelectOrderResponseDto.builder()
                .cartType(ShoppingEnums.CartType.NORMAL.getCode())
                .spCartId(shoppingCartBiz.addCartPickGoods(addCartDto))
                .build());
    }

    @Override
    public ApiResult<?> addGreenJuiceOrder(GreenJuiceOrderRequestDto dto) throws Exception {
        // GreenJuice Validation Check
        ExhibitEnums.GetValidation selectValidation = promotionExhibitService.addGreenJuiceExhibitValidation(dto);
        if (!selectValidation.getCode().equals(ExhibitEnums.GetValidation.PASS_VALIDATION.getCode())) {
            return ApiResult.result(selectValidation);
        }

        // 바로구매 진행
        List<SpCartPickGoodsRequestDto> pickGoodsList = dto.getPickGoodsList();
        AddCartPickGoodsRequestDto addCartDto = new AddCartPickGoodsRequestDto();
        addCartDto.setUrPcidCd(dto.getUrPcidCd());
        addCartDto.setUrUserId(dto.getUrUserId());
        addCartDto.setDeliveryType(ShoppingEnums.DeliveryType.DAILY.getCode());
        addCartDto.setIlGoodsId(pickGoodsList.get(0).getIlGoodsId());
        addCartDto.setQty(1);
        addCartDto.setBuyNowYn("Y");
        addCartDto.setGoodsDailyCycleType(dto.getGoodsDailyCycleType());
        addCartDto.setGoodsDailyCycleTermType(dto.getGoodsDailyCycleTermType());
        addCartDto.setGoodsDailyCycleGreenJuiceWeekType(dto.getGoodsDailyCycleGreenJuiceWeekType());
        addCartDto.setCartPromotionType(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode());
        addCartDto.setPickGoodsList(pickGoodsList);
        addCartDto.setPmAdExternalCd(dto.getPmAdExternalCd());
        addCartDto.setPmAdInternalPageCd(dto.getPmAdInternalPageCd());
        addCartDto.setPmAdInternalContentCd(dto.getPmAdInternalContentCd());

        return ApiResult.success(SelectOrderResponseDto.builder()
                .cartType(ShoppingEnums.CartType.NORMAL.getCode())
                .spCartId(shoppingCartBiz.addCartPickGoods(addCartDto))
                .build());
    }

    @Override
    public List<GiftListVo> getGiftList(GiftListRequestDto dto) throws Exception {
        return promotionExhibitService.getGiftList(dto);
    }

    @Override
    public ApiResult<?> getGiftByUser(ExhibitRequestDto dto) throws Exception {
        GiftByUserVo exhibitInfo = promotionExhibitService.getGiftByUser(dto.getEvExhibitId(), dto.getDeviceType());
        if (exhibitInfo == null) return ApiResult.result(ExhibitEnums.GetValidation.NO_EXHIBIT);

        // Event Validation Check
        ExhibitValidationRequestDto validationRequestDto = new ExhibitValidationRequestDto(dto, exhibitInfo);
        MessageCommEnum validation = promotionExhibitService.getExhibitValidation(validationRequestDto);
        if (!validation.getCode().equals(ExhibitEnums.GetValidation.PASS_VALIDATION.getCode())) {
            if (validation.getCode().equals(ExhibitEnums.GetValidation.NOT_GROUP.getCode()) ||
                    validation.getCode().equals(ExhibitEnums.GetValidation.NOT_DEVICE.getCode())) {
                return ApiResult.result(getExhibitReturnData(validation, validationRequestDto), validation);
            }
            return ApiResult.result(validation);
        }

        GiftByUserResponseDto result = new GiftByUserResponseDto(exhibitInfo);

        //상품 정보
        List<ExhibitGroupByUserVo> groupList = promotionExhibitService.getGroupByUser(dto.getEvExhibitId());
        for (ExhibitGroupByUserVo groupVo : groupList) {
            List<Long> groupGoodsIdList = promotionExhibitService.getGroupDetailByUser(groupVo.getEvExhibitGroupId());

            //상품 ID로 상품검색
            GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                    .goodsIdList(groupGoodsIdList)
                    .build();

            List<GoodsSearchResultDto> goodsList = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto).stream()
                    .limit(groupVo.getDisplayCount())
                    .collect(Collectors.toList());

            groupVo.setGoods(goodsList);
        }
        result.setGroup(groupList);

        // dDay 계산
        if (exhibitInfo.getAlwaysYn().equals("N")) {
            result.setDday(DateUtil.getBetweenDays(exhibitInfo.getEndDate()));
        }

        return ApiResult.success(result);
    }

    @Override
    public SelectExhibitResponseDto getSelectExhibit(Long evExhibitId) throws Exception {
        SelectExhibitVo exhibitInfo = promotionExhibitService.getSelectExhibit(evExhibitId);

        MessageCommEnum validationResponse = promotionExhibitService.getSelectExhibitValidation(exhibitInfo);
        boolean active = true;
        if (!validationResponse.equals(BaseEnums.Default.SUCCESS)) {
            active = false;
        }

        return SelectExhibitResponseDto.builder()
                .evExhibitId(exhibitInfo.getEvExhibitId())
                .title(exhibitInfo.getTitle())
                .selectPrice(exhibitInfo.getSelectPrice())
                .isActive(active)
                .build();
    }

    @Override
    public List<Long> getGreenJuiceGoods(Long urSupplierId) throws Exception {
        return promotionExhibitService.getGreenJuiceGoods(urSupplierId);
    }

    @Override
    public ExhibitInfoFromMetaVo getExhibitFromMeta(Long evExhibitId) throws Exception {
        return promotionExhibitService.getExhibitInfoFromMeta(evExhibitId);
    }

    private String getExhibitReturnData(MessageCommEnum validation, ExhibitValidationRequestDto validationRequestDto) {
        if (validation.getCode().equals(ExhibitEnums.GetValidation.NOT_GROUP.getCode())) {
            return validationRequestDto.getUserGroupList().stream()
                    .map(vo -> vo.getGroupMasterName() + "_" + vo.getGroupName())
                    .collect(Collectors.joining(", "));
        }

        if (validation.getCode().equals(ExhibitEnums.GetValidation.NOT_DEVICE.getCode())) {
            Set<String> deviceSet = new HashSet<>(Arrays.asList("PC", "MOBILE", "APP"));
            if (validationRequestDto.getDisplayWebPcYn().equals("N")) {
                deviceSet.remove("PC");
            }
            if (validationRequestDto.getDisplayWebMobileYn().equals("N")) {
                deviceSet.remove("MOBILE");
            }
            if (validationRequestDto.getDisplayAppYn().equals("N")) {
                deviceSet.remove("APP");
            }
            return deviceSet.toString();
        }
        return "";
    }

}