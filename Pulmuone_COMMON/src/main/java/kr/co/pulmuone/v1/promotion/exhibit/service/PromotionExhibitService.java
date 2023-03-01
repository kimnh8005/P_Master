package kr.co.pulmuone.v1.promotion.exhibit.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.mapper.promotion.exhibit.PromotionExhibitMapper;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.promotion.exhibit.dto.*;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.*;
import kr.co.pulmuone.v1.shopping.cart.dto.SpCartPickGoodsRequestDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionExhibitService {
    @Autowired
    PromotionExhibitMapper promotionExhibitMapper;

    @Autowired
    GoodsSearchBiz goodsSearchBiz;

    private final GoodsGoodsBiz goodsGoodsBiz;

    private final StoreDeliveryBiz storeDeliveryBiz;

    private final UserCertificationBiz userCertificationBiz;

    /**
     * 기획전 목록조회
     *
     * @param dto ExhibitListByUserRequestDto
     * @return ExhibitListByUserResponseDto
     * @throws Exception Exception
     */
    protected ExhibitListByUserResponseDto getExhibitListByUser(ExhibitListByUserRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<ExhibitListByUserVo> result = promotionExhibitMapper.getExhibitListByUser(dto);

        return ExhibitListByUserResponseDto.builder()
                .total((int) result.getTotal())
                .exhibit(result.getResult())
                .build();
    }

    /**
     * 일반 기획전 조회
     *
     * @param evExhibitId Long
     * @param deviceType  String
     * @return NormalByUserVo
     * @throws Exception Exception
     */
    protected NormalByUserVo getNormalByUser(Long evExhibitId, String deviceType) throws Exception {
        NormalByUserVo result = promotionExhibitMapper.getNormalByUser(evExhibitId, deviceType);
        if (result != null) {
            result.setUserGroupList(promotionExhibitMapper.getUserGroup(evExhibitId));
        }
        return result;
    }

    /**
     * 기획전 그룹 조회
     *
     * @param evExhibitId Long
     * @return List<ExhibitGroupByUserVo>
     * @throws Exception Exception
     */
    protected List<ExhibitGroupByUserVo> getGroupByUser(Long evExhibitId) throws Exception {
        return promotionExhibitMapper.getGroupByUser(evExhibitId);
    }

    /**
     * 기획전 그룹 상세 조회
     *
     * @param evExhibitGroupId Long
     * @return List<Long>
     * @throws Exception Exception
     */
    protected List<Long> getGroupDetailByUser(Long evExhibitGroupId) throws Exception {
        return promotionExhibitMapper.getGroupDetailByUser(evExhibitGroupId);
    }

    /**
     * 골라담기 목록 조회
     *
     * @param dto ExhibitListByUserRequestDto
     * @return SelectListByUserResponseDto
     * @throws Exception Exception
     */
    protected SelectListByUserResponseDto getSelectListByUser(ExhibitListByUserRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<SelectListByUserVo> resultPage = promotionExhibitMapper.getSelectListByUser(dto);
        List<SelectListByUserVo> resultVo = resultPage.getResult();
        SelectListByUserResponseDto result = SelectListByUserResponseDto.builder()
                .total((int) resultPage.getTotal())
                .select(resultVo)
                .build();
        if (resultVo.size() == 0) return result;


//        for (SelectListByUserVo vo : resultVo) {
//            //대표상품 정보 설정
//            GoodsSearchByGoodsIdRequestDto mainGoodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
//                    .goodsIdList(Collections.singletonList(vo.getIlGoodsId()))
//                    .build();
//            List<GoodsSearchResultDto> mainSearchGoods = goodsSearchBiz.searchGoodsByGoodsIdList(mainGoodsSearchByGoodsIdReqDto);
//            if (mainSearchGoods.size() > 0) {
//                vo.setMainGoods(mainSearchGoods.get(0));
//            }
//
//            //상품 정보
//            List<Long> goodsIdList = promotionExhibitMapper.getSelectGoodsByUser(vo.getEvExhibitId());
//            GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
//                    .goodsIdList(goodsIdList)
//                    .build();
//            vo.setGoods(goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto));
//
//            //추가 상품 정보
//            List<SelectAddGoodsVo> addGoodsList = promotionExhibitMapper.getSelectAddGoods(vo.getEvExhibitId());
//            for (SelectAddGoodsVo addGoodsVo : addGoodsList) {
//                List<GoodsSearchAdditionalVo> goodsSearchResult = goodsSearchBiz.getGoodsAdditional(Collections.singletonList(addGoodsVo.getIlGoodsId()));
//                if (goodsSearchResult != null && goodsSearchResult.size() > 0) {
//                    addGoodsVo.setGoods(goodsSearchResult.get(0));
//                }
//            }
//            vo.setAddGoods(addGoodsList);
//        }

        result.setSelect(resultVo);
        return result;
    }

    /**
     * 골라담기 상세 조회
     *
     * @param evExhibitId Long
     * @param deviceType  String
     * @return SelectByUserVo
     * @throws Exception Exception
     */
    protected SelectByUserVo getSelectByUser(Long evExhibitId, String deviceType) throws Exception {
        SelectByUserVo result = promotionExhibitMapper.getSelectByUser(evExhibitId, deviceType);
        if (result != null) {
            result.setUserGroupList(promotionExhibitMapper.getUserGroup(evExhibitId));
        }
        return result;
    }

    /**
     * 골라담기 상품 조회
     *
     * @param evExhibitId Long
     * @return List<Long>
     * @throws Exception Exception
     */
    protected List<Long> getSelectGoodsByUser(Long evExhibitId) throws Exception {
        return promotionExhibitMapper.getSelectGoodsByUser(evExhibitId);
    }

    /**
     * 골라담기 추가 상품 조회
     *
     * @param evExhibitId Long
     * @return List<SelectAddGoodsVo>
     * @throws Exception Exception
     */
    protected List<SelectAddGoodsVo> getSelectAddGoods(Long evExhibitId) throws Exception {
        return promotionExhibitMapper.getSelectAddGoods(evExhibitId);
    }

    /**
     * 기획전 상세 조회 Validation
     *
     * @param dto ExhibitValidationRequestDto
     * @return ExhibitEnums.GetValidation
     */
    protected MessageCommEnum getExhibitValidation(ExhibitValidationRequestDto dto) {
        if (dto == null) return ExhibitEnums.GetValidation.NO_EXHIBIT;

        //기획전 기간 - 상시는 제외
        if (dto.getAlwaysYn().equals("N")) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate now = LocalDate.now();
            LocalDate eventStartDate = LocalDate.parse(dto.getStartDate(), dateFormatter);
            if (now.isBefore(eventStartDate)) {                   // 시작일 이전
                return ExhibitEnums.GetValidation.NOT_DATE_BEFORE;
            }
        }

        //임직원 전용 기획전
        if (dto.getEvEmployeeType().equals(EventEnums.EvEmployeeType.EMPLOYEE_ONLY.getCode())) {
            if (!dto.getUserStatus().equals(UserEnums.UserStatusType.EMPLOYEE.getCode())) {
                return ExhibitEnums.GetValidation.ONLY_EMPLOYEE;
            }
        }

        //임직원 제외 기획전 (회원 전용 기획전)
        if (dto.getEvEmployeeType().equals(EventEnums.EvEmployeeType.EMPLOYEE_EXCEPT.getCode())) {
            if (dto.getUserStatus().equals(UserEnums.UserStatusType.EMPLOYEE.getCode())) {
                return ExhibitEnums.GetValidation.NOT_EMPLOYEE;
            }
        }

        //디바이스
        if (dto.getDisplayWebPcYn().equals("N")) {
            if (dto.getDeviceType().equals(GoodsEnums.DeviceType.PC.getCode())) {
                return ExhibitEnums.GetValidation.NOT_DEVICE;
            }
        }
        if (dto.getDisplayWebMobileYn().equals("N")) {
            if (dto.getDeviceType().equals(GoodsEnums.DeviceType.MOBILE.getCode())) {
                return ExhibitEnums.GetValidation.NOT_DEVICE;
            }
        }
        if (dto.getDisplayAppYn().equals("N")) {
            if (dto.getDeviceType().equals(GoodsEnums.DeviceType.APP.getCode())) {
                return ExhibitEnums.GetValidation.NOT_DEVICE;
            }
        }

        //비회원 노출여부
        if (dto.getDisplayNonmemberYn().equals("N")) {
            if (dto.getUrUserId().equals(0L)) {
                return ExhibitEnums.GetValidation.NOT_GROUP_NONE;
            }
        }

        //등급
        if (dto.getUserGroupList() != null && dto.getUserGroupList().size() > 0) {
            List<Long> userGroupIdList = dto.getUserGroupList().stream()
                    .map(ExhibitUserGroupByUserVo::getUrGroupId)
                    .collect(Collectors.toList());
            if (userGroupIdList.size() > 0 && !userGroupIdList.contains(dto.getUrGroupId())) {
                return ExhibitEnums.GetValidation.NOT_GROUP;
            }
        }

        return ExhibitEnums.GetValidation.PASS_VALIDATION;
    }

    /**
     * 골라담기 기획전 등록 Validation
     *
     * @param dto         SelectOrderRequestDto
     * @param exhibitInfo SelectByUserVo
     * @return ExhibitEnums.GetValidation
     */
    protected ApiResult<?> addSelectExhibitValidation(SelectOrderRequestDto dto, SelectByUserVo exhibitInfo) throws Exception {
        if (exhibitInfo == null) return ApiResult.result(ExhibitEnums.GetValidation.NO_EXHIBIT);

        //구매하기 일경우 비회원은 안됨
        if (dto.getBuyNowYn().equals("Y")) {
            if (dto.getUrUserId().equals(0L)) {
                return ApiResult.result(ExhibitEnums.GetValidation.NOT_GROUP_NONE);
            }
        }

        // 종료일시 확인
        if (exhibitInfo.getEndYn().equals("Y")) {
            return ApiResult.result(ExhibitEnums.GetValidation.NO_EXHIBIT);
        }

        //상품정보 맞는지 체크
        exhibitInfo.getGoodsList().add(exhibitInfo.getIlGoodsId()); // 대표상품 상품리스트에 포함
        int goodsBuyLimitCount = exhibitInfo.getGoodsBuyLimitCount();
        Set<Long> goodsSet = new HashSet<>(exhibitInfo.getGoodsList());
        List<SpCartPickGoodsRequestDto> pickGoodsList = dto.getPickGoodsList();
        for (SpCartPickGoodsRequestDto selectInfo : pickGoodsList) {
            if (!goodsSet.contains(selectInfo.getIlGoodsId())) {
                return ApiResult.result(ExhibitEnums.GetValidation.NOT_GOODS);
            }

            //수량 체크 - 상품별 구매가능 수량
            if (goodsBuyLimitCount != 0 && selectInfo.getQty() > goodsBuyLimitCount) {
                return ApiResult.result(ExhibitEnums.GetValidation.NOT_QTY);
            }
        }

        //수량 체크 - 기본 구매수량
        int defaultBuyCount = exhibitInfo.getDefaultBuyCount();
        int qtySum = pickGoodsList.stream().mapToInt(SpCartPickGoodsRequestDto::getQty).sum();
        if (defaultBuyCount < qtySum) {
            return ApiResult.result(ExhibitEnums.GetValidation.NOT_QTY);
        }
        if (qtySum % defaultBuyCount != 0) {
            return ApiResult.result(ExhibitEnums.GetValidation.NOT_QTY);
        }


        //재고확인
        String dirInfo = DeviceUtil.getDirInfo();
        boolean isApp = DeviceUtil.isApp();
        boolean isMember = dto.getUserStatus().equals(UserEnums.UserStatusType.MEMBER.getCode());
        boolean isEmployee = dto.getUserStatus().equals(UserEnums.UserStatusType.EMPLOYEE.getCode());

        List<SelectOrderNoSaleResponseDto> noSaleList = new ArrayList<>();
        List<SelectOrderNoStockResponseDto> noStockList = new ArrayList<>();

        for (SpCartPickGoodsRequestDto selectInfo : pickGoodsList) {
            GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder()
                    .ilGoodsId(selectInfo.getIlGoodsId())
                    .deviceInfo(dirInfo)
                    .isApp(isApp)
                    .isMember(isMember)
                    .isEmployee(isEmployee)
                    .build();

            BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);

            // 상품이 없을 경우
            if (goods == null) {
                return ApiResult.result(ExhibitEnums.GetValidation.NO_STOCK);
            } else {
                // 상품 재고 정보
                int stock = goods.getStockQty();

                // 상품이 판매중이 아닐때
                if (!GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goods.getSaleStatus())) {
                    noSaleList.add(SelectOrderNoSaleResponseDto.builder()
                            .ilGoodsId(selectInfo.getIlGoodsId())
                            .goodsName(goods.getGoodsName())
                            .build());
                }

                // 상품 재고가 구매 수량보다 작을경우
                if (stock - selectInfo.getQty() < 0) {
                    noStockList.add(SelectOrderNoStockResponseDto.builder()
                            .ilGoodsId(selectInfo.getIlGoodsId())
                            .goodsName(goods.getGoodsName())
                            .stockCount(stock)
                            .build());
                }
            }
        }

        if (noSaleList.size() > 0 || noStockList.size() > 0) {
            SelectOrderResponseDto responseDto = SelectOrderResponseDto.builder()
                    .noSale(noSaleList)
                    .noStock(noStockList)
                    .build();
            if (noSaleList.size() > 0) {
                return ApiResult.result(responseDto, ExhibitEnums.GetValidation.NO_SALE);
            } else {
                return ApiResult.result(responseDto, ExhibitEnums.GetValidation.NO_STOCK);
            }
        }

        return ApiResult.result(ExhibitEnums.GetValidation.PASS_VALIDATION);
    }

    /**
     * 녹즙 등록 Validation
     *
     * @param dto GreenJuiceOrderRequestDto
     * @return ExhibitEnums.GetValidation
     */
    protected ExhibitEnums.GetValidation addGreenJuiceExhibitValidation(GreenJuiceOrderRequestDto dto) throws Exception {
        if (dto == null) return ExhibitEnums.GetValidation.NO_EXHIBIT;

        //비회원은 안됨
        if (dto.getUrUserId().equals(0L)) {
            return ExhibitEnums.GetValidation.NOT_GROUP_NONE;
        }

        //배송권역 확인
//        if (!isShippingPossibility(dto.getPickGoodsList().get(0).getIlGoodsId())) {
//            return ExhibitEnums.GetValidation.NO_SHIPPING_ADDRESS;
//        }

        return ExhibitEnums.GetValidation.PASS_VALIDATION;
    }

    private boolean isShippingPossibility(Long ilGoodsId) throws Exception {
        GetSessionShippingResponseDto shippingAddress = userCertificationBiz.getSessionShipping();

        // 일일상품인 경우 스토어 배송권역 정보, 세션의 우편번호/건물번호 조회하여 데이터가 있으면 isShippingPossibility 가능
        ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo =
                storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(ilGoodsId, shippingAddress.getReceiverZipCode(), shippingAddress.getBuildingCode());

        return shippingPossibilityStoreDeliveryAreaInfo != null;
    }

    /**
     * 증정기획전 - 상품상세
     *
     * @param dto GiftFromGoodsRequestDto
     * @return List<GiftFromGoodsVo>
     * @throws Exception Exception
     */
    protected List<GiftListVo> getGiftList(GiftListRequestDto dto) throws Exception {
        List<GiftListVo> result = promotionExhibitMapper.getGiftList(dto);
        for (GiftListVo vo : result) {
            vo.setGoods(promotionExhibitMapper.getGiftGoods(vo.getEvExhibitId()));
            vo.setTargetGoods(promotionExhibitMapper.getGiftTargetGoods(vo.getEvExhibitId()));
            vo.setTargetBrand(promotionExhibitMapper.getGiftTargetBrand(vo.getEvExhibitId()));
        }
        return result;
    }

    /**
     * 증정기획전 - 유저
     *
     * @param evExhibitId Long
     * @param deviceType  String
     * @return GiftByUserVo
     * @throws Exception Exception
     */
    protected GiftByUserVo getGiftByUser(Long evExhibitId, String deviceType) throws Exception {
        GiftByUserVo result = promotionExhibitMapper.getGiftByUser(evExhibitId, deviceType);
        if (result == null) return null;
        result.setUserGroupList(promotionExhibitMapper.getUserGroup(evExhibitId));
        List<GiftGoodsVo> giftGoods = promotionExhibitMapper.getGiftGoods(evExhibitId);
        if (giftGoods != null && giftGoods.size() > 0) {
            result.setGoods(giftGoods);
        }
        return result;
    }

    /**
     * 골라담기 상세 조회
     *
     * @param evExhibitId Long
     * @return SelectExhibitVo
     * @throws Exception Exception
     */
    protected SelectExhibitVo getSelectExhibit(Long evExhibitId) throws Exception {
        return promotionExhibitMapper.getSelectExhibit(evExhibitId);
    }

    /**
     * 기획전 상세 조회 Validation
     *
     * @param dto SelectExhibitVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum getSelectExhibitValidation(SelectExhibitVo dto) {
        if (dto == null) return ExhibitEnums.GetValidation.NO_EXHIBIT;

        //이벤트 기간 - 상시는 제외
        if (dto.getAlwaysYn().equals("N")) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate now = LocalDate.now();
            LocalDate eventStartDate = LocalDate.parse(dto.getStartDate(), dateFormatter);
            LocalDate eventEndDate = LocalDate.parse(dto.getEndDate(), dateFormatter);
            if (now.isBefore(eventStartDate)) {                   // 시작일 이전
                return ExhibitEnums.GetValidation.NO_EXHIBIT;
            } else if (now.isAfter(eventEndDate)) {               // 종료일 지남
                return ExhibitEnums.GetValidation.NO_EXHIBIT;
            }
        }

        //삭제 여부
        if (dto.getDelYn().equals("Y")) {
            return ExhibitEnums.GetValidation.NO_EXHIBIT;
        }

        //사용 여부
        if (dto.getUseYn().equals("N")) {
            return ExhibitEnums.GetValidation.NO_EXHIBIT;
        }

        return BaseEnums.Default.SUCCESS;
    }

    /**
     * 녹즙내맘대로 담기 대상 상품 조회
     *
     * @param urSupplierId Long
     * @return List<Long>
     * @throws Exception Exception
     */
    protected List<Long> getGreenJuiceGoods(Long urSupplierId) throws Exception {
        return promotionExhibitMapper.getGreenJuiceGoods(urSupplierId);
    }

    protected ExhibitInfoFromMetaVo getExhibitInfoFromMeta(Long evExhibitId) throws Exception {
        return promotionExhibitMapper.getExhibitInfoFromMeta(evExhibitId);
    }
}