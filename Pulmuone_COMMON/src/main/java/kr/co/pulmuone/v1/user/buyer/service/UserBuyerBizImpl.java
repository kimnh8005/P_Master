package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgServiceType;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.comm.util.asis.AsisUserApiUtil;
import kr.co.pulmuone.v1.comm.util.asis.dto.SearchCustomerRsrvTotalResponseDto;
import kr.co.pulmuone.v1.comm.util.asis.dto.UserInfoCheckResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingPriceResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingInfoByOdOrderIdResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
import kr.co.pulmuone.v1.order.order.dto.mall.OrderDetailByOdShippingZondIdResultDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.order.order.service.MallOrderDailyDetailBiz;
import kr.co.pulmuone.v1.order.order.service.MallOrderDetailBiz;
import kr.co.pulmuone.v1.order.order.service.OrderDetailBiz;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleDeliverableListResponseDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBiz;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleBiz;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.shopping.recently.service.ShoppingRecentlyBiz;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.dto.WarehouseUnDeliveryableInfoDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.*;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.login.dto.UnlinkAccountRequestDto;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class UserBuyerBizImpl implements UserBuyerBiz {
  @Autowired
  private UserBuyerService     userBuyerService;

  @Autowired
  private UserBuyerSnsService  userBuyerSnsService;

  @Autowired
  private StoreDeliveryBiz     storeDeliveryBiz;

  @Autowired
  private OrderFrontBiz orderFrontBiz;

  @Autowired
  private PgBiz pgBiz;

  @Autowired
  private UserBuyerShippingAddrBiz userBuyerShippingAddrBiz;

  @Autowired
  private OrderScheduleBiz orderScheduleBiz;

  @Autowired
  private ShoppingRecentlyBiz shoppingRecentlyBiz;

  @Autowired
  private AsisUserApiUtil asisUserApiUtil;

  @Autowired
  private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

  @Autowired
  private MallOrderDetailBiz mallOrderDetailBiz;

  @Autowired
  private OrderOrderBiz orderOrderBiz;

  @Autowired
  private OrderDetailBiz orderDetailBiz;

  @Autowired
  private MallOrderScheduleBiz mallOrderScheduleBiz;

  @Autowired
  private MallOrderDailyDetailBiz mallOrderDailyDetailBiz;

  @Autowired
  private StoreWarehouseBiz storeWarehouseBiz;


  /**
   * 이메일 중복체크
   */
  public int checkDuplicateMail(CommonDuplicateMailRequestDto dto) throws Exception {
    return userBuyerService.checkDuplicateMail(dto);
  }

  /**
   * 환불계좌 정보 조회
   */
  public CommonGetRefundBankResultVo getRefundBank(CommonGetRefundBankRequestDto dto) throws Exception {

    return userBuyerService.getRefundBank(dto);
  }

  /**
   * 환불계좌 수정
   */
  public int putRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {

    return userBuyerService.putRefundBank(dto);
  }

  /**
   * 환불계좌 추가
   */
  public int addRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {

    return userBuyerService.addRefundBank(dto);
  }

  /**
   * 환불계좌 삭제
   */
  public void delRefundBank(Long urRefundBankId) throws Exception {
    userBuyerService.delRefundBank(urRefundBankId);
  }

  /**
   * 배송지 리스트 조회
   */
  public CommonGetShippingAddressListResponseDto getShippingAddressList(CommonGetShippingAddressListRequestDto dto) throws Exception {
    CommonGetShippingAddressListResponseDto result = userBuyerService.getShippingAddressList(dto.getUrUserId());
    List<CommonGetShippingAddressListResultVo> list = result.getRows();
    for (CommonGetShippingAddressListResultVo vo : list ) {
      vo.setDelivery(storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(vo.getReceiverZipCode(), vo.getBuildingCode()));
    }

    OrderDetailScheduleDeliverableListResponseDto resultDto = null;
    if(StringUtil.nvlLong(dto.getOdOrderDetlId()) > 0) {
    	resultDto = (OrderDetailScheduleDeliverableListResponseDto)orderScheduleBiz.getOrderDeliverableScheduleList(dto.getOdOrderDetlId()).getData();
    }

    if(resultDto != null) {
    	result.setScheduleDelvDateList(resultDto.getScheduleDelvDateList());
    } else {
    	result.setScheduleDelvDateList(null);
    }

    return result;
  }

  /**
   * 배송지 단건 조회
   */
  public CommonGetShippingAddressResultVo getShippingAddress(CommonGetShippingAddressRequestDto dto) throws Exception {

    return userBuyerService.getShippingAddress(dto);
  }

  /**
   * 배송지 추가
   */
  public int addShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception {

    return userBuyerService.addShippingAddress(dto);
  }

  /**
   * 배송지 수정
   */
  public int putShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception {
    return userBuyerService.putShippingAddress(dto);
  }

  public int putMaketingReceiptAgreementSms(String urUserId) throws Exception {
    return userBuyerService.putMaketingReceiptAgreementSms(urUserId);
  }

  public int putMaketingReceiptAgreementMail(String urUserId) throws Exception {
    return userBuyerService.putMaketingReceiptAgreementMail(urUserId);
  }

  public ApiResult<?> addChangeClauseAgree(AddChangeClauseAgreeRequestDto addChangeClauseAgreeRequestDto,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
    return userBuyerService.addChangeClauseAgree(addChangeClauseAgreeRequestDto, request, response);
  }

  public GetShippingAddressListResultVo getBasicShippingAddress(String databaseEncryptionKey, String urUserId) throws Exception {
    return userBuyerService.getBasicShippingAddress(databaseEncryptionKey, urUserId);
  }

  public ApiResult<?> getShippingAddressList(GetShippingAddressListRequestDto getShippingAddressListRequestDto) throws Exception {
    return userBuyerService.getShippingAddressList(getShippingAddressListRequestDto);
  }

  // 장바구니 배송목록
  public ApiResult<?> getCartShippingAddressList() throws Exception {
    return userBuyerService.getCartShippingAddressList();
  }

  // 배송지 리스트 조회
  public ApiResult<?> getShippingAddressList() throws Exception {
    return userBuyerService.getShippingAddressList();
  }

  public ApiResult<?> delUserSearchWordLog(List<Integer> urUserSearchWordLogId) throws Exception {
    return userBuyerService.delUserSearchWordLog(urUserSearchWordLogId);
  }

  public List<GetSearchWordResultVo> getUserSearchWordLogList(String urUserId) throws Exception {
    return userBuyerService.getUserSearchWordLogList(urUserId);
  }

  public void addUserSearchWordLog(String keyword, String urUserId) throws Exception {
    userBuyerService.addUserSearchWordLog(keyword, urUserId);
    // 10개이상 저장 안되도록 처리
    List<GetSearchWordResultVo> list = userBuyerService.getUserSearchWordLogList(urUserId);
    if (list.size() > 10) {
      userBuyerService.delUserSearchWordLog(list.stream()
                                                .map(GetSearchWordResultVo::getUrUserSearchWordLogId)
                                                .skip(10)
                                                .collect(Collectors.toList()));
    }
  }

  public List<GetChangeClauseNoAgreeListResultVo> getChangeClauseNoAgreeList(String urUserId) throws Exception {
    return userBuyerService.getChangeClauseNoAgreeList(urUserId);
  }

  /**
   * 신규회원특가 쿠폰 조회
   */
  public int getNewBuyerSpecialsCouponByUser(Long ilGoodsId, String urUserId, String deviceInfo, boolean isApp) throws Exception {
    return userBuyerService.getNewBuyerSpecialsCouponByUser(ilGoodsId, urUserId, deviceInfo, isApp);
  }

  /**
   * 마이페이지 회원정보조회
   */
  @Override
  public BuyerFromMypageResultVo getBuyerFromMypage(Long urUserId) throws Exception {
    BuyerFromMypageResultVo result = userBuyerService.getBuyerFromMypage(urUserId);
    result.setOrderCount(orderFrontBiz.getOrderCountFromUserDrop(urUserId));
    return result;
  }

  /**
   * 마이페이지 회원정보수정
   */
  @Override
  public void putBuyerFromMypage(CommonPutBuyerFromMypageRequestDto dto) throws Exception {
      if(dto.getRecentlyViewYn().equals("N")){
          shoppingRecentlyBiz.delRecentlyViewByUserId(dto.getUrUserId());
      }

      userBuyerService.putBuyerFromMypage(dto);
  }

  /**
   * 배송지 삭제
   */
  @Override
  public void delShippingAddress(Long urShippingAddrId) throws Exception {
    userBuyerService.delShippingAddress(urShippingAddrId);
  }

  /**
   * 기본배송지 설정
   */
  @Override
  public void putShippingAddressSetDefault(Long urUserId, Long urShippingAddrId) throws Exception {
    userBuyerService.putShippingAddressSetDefault(urUserId, urShippingAddrId);
  }

  @Override
  public ShippingAddressListFromMyPageResponseDto getShippingAddressListFromMyPage(ShippingAddressListFromMyPageRequestDto dto) throws Exception {
    ShippingAddressListFromMyPageResponseDto result = userBuyerService.getShippingAddressListFromMyPage(dto);
    for (ShippingAddressListFromMyPageResultVo vo : result.getRows()) {
      vo.setDelivery(storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(vo.getReceiverZipCode(), vo.getBuildingCode()));
    }

    return result;
  }

  /**
   * 배송지 관련정보 조회
   */
  @Override
  public ApiResult<?> getShippingAddressInfo() throws Exception {

    GetCodeListRequestDto getCodeListRequestDto = new GetCodeListRequestDto();
    getCodeListRequestDto.setStCommonCodeMasterCode("ACCESS_INFORMATION");
    getCodeListRequestDto.setUseYn("Y");
    return ApiResult.success(userBuyerService.getCommonCode(getCodeListRequestDto));

  }

  /**
   * 은행목록
   */
  @Override
  public ApiResult<?> getRefundBankInfo() throws Exception {
    GetCodeListRequestDto getCodeListRequestDto = new GetCodeListRequestDto();
    getCodeListRequestDto.setStCommonCodeMasterCode("BANK_CODE");
    getCodeListRequestDto.setUseYn("Y");
    return ApiResult.success(userBuyerService.getCommonCode(getCodeListRequestDto));

  }

  @Override
  public boolean isValidationBankAccountNumber(String bankCode, String accountNumber, String holderName) throws Exception {
	String pgBankCode = pgBiz.getPgBankCode(PgServiceType.INICIS.getCode(), PaymentType.VIRTUAL_BANK.getCode(), bankCode);
	if(pgBankCode == null) {
		return false;
	} else {
		return userBuyerService.isValidationBankAccountNumber(pgBankCode, accountNumber, holderName);
	}
  }

  @Override
  public int getPromotionRecaptchaFailCount() throws Exception {
    return userBuyerService.getPromotionRecaptchaFailCount();
  }

  @Override
  public void putPromotionRecaptchaFailCount(int count) throws Exception {
    userBuyerService.putPromotionRecaptchaFailCount(count);
  }

  /**
   * SNS 로그인 (네이버)
   */
  @Override
  public ApiResult<?> getUrlNaver() throws Exception {
    /* userSnsLoginBiz 대신에 userBuyerSnsService 이용한다. */
    return ApiResult.success(userBuyerSnsService.getUrlNaver());
  }

  /**
   * SNS 로그인 응답 (네이버)
   */
  @Override
  public ApiResult<?> callbackNaver(HttpServletRequest request) throws Exception {
    /* userSnsLoginBiz 대신에 userBuyerSnsService 이용한다. */
    return userBuyerSnsService.callbackNaver(request);
  }

  /**
   * SNS 로그인 응답 (카카오)
   */
  @Override
  public ApiResult<?> callbackKakao(UserSocialInformationDto userSocialInformationDto) throws Exception {
    /* userSnsLoginBiz 대신에 userBuyerSnsService 이용한다. */
    return userBuyerSnsService.callbackKakao(userSocialInformationDto);
  }

  /**
    * SNS 로그인 응답 (구글)
  */
  @Override
  public ApiResult<?> callbackGoogle(UserSocialInformationDto userSocialInformationDto) throws Exception {
      /* userSnsLoginBiz 대신에 userBuyerSnsService 이용한다. */
      return userBuyerSnsService.callbackGoogle(userSocialInformationDto);
  }

  /**
   * SNS 로그인 응답 (페이스북)
   */
  @Override
  public ApiResult<?> callbackFacebook(UserSocialInformationDto userSocialInformationDto) throws Exception {
      /* userSnsLoginBiz 대신에 userBuyerSnsService 이용한다. */
      return userBuyerSnsService.callbackFacebook(userSocialInformationDto);
  }

  /**
   * SNS 로그인 응답 (애플)
  */
  @Override
  public ApiResult<?> callbackApple(UserSocialInformationDto userSocialInformationDto) throws Exception {
      /* userSnsLoginBiz 대신에 userBuyerSnsService 이용한다. */
      return userBuyerSnsService.callbackApple(userSocialInformationDto);
  }


    /**
   * SNS 로그인 계정 연결끊기
   */
  @Override
  public ApiResult<?> delSyncAccount(String urSocialId,String provider) throws Exception {

    UnlinkAccountRequestDto unlinkAccountRequestDto = new UnlinkAccountRequestDto();
    unlinkAccountRequestDto.setUser_id(urSocialId);
    unlinkAccountRequestDto.setProvider(provider);

    return ApiResult.success(userBuyerSnsService.unlinkAccount(unlinkAccountRequestDto));
  }

  /**
   * 공통 코드 조회
   */
  @Override
  public List<CodeInfoVo> getCommonCode(String masterCode) throws Exception {
    GetCodeListRequestDto getCodeListRequestDto = new GetCodeListRequestDto();
    getCodeListRequestDto.setStCommonCodeMasterCode(masterCode);
    getCodeListRequestDto.setUseYn("Y");
    return userBuyerService.getCommonCode(getCodeListRequestDto).getRows();
  }

  /**
   * 회원 사용 카드 정보 조회
   */
  @Override
  public HashMap<String,String> getUserPaymentInfo(String urUserId) throws Exception {
    return userBuyerService.getUserPaymentInfo(urUserId);
  }

	/**
	 * 회원 사용 결제정보 저장
	 */
	@Override
	public void putUserPaymentInfo(Long urUserId, String psPayCd, String cardCode) throws Exception {
		userBuyerService.putUserPaymentInfo(urUserId, psPayCd, cardCode);
	}

	/**
	 * @Desc 배송지 목록에서 주문배송지로 설정
	 */
	@Override
	public ApiResult<?> putShippingAddressIntoOrderShippingZone(CommonSaveShippingAddressRequestDto dto) throws Exception {
		boolean isChangeDawnToNormal = false;

		// 베송지 변경 가능 여부 체크
        ApiResult result = orderDetailBiz.isPossibleChangeDeliveryAddress(dto);

        if(UserEnums.ChangeDeliveryAddress.CHANGE_DAWN_TO_NORMAL.getCode().equals(result.getCode())){
            // 택배배송지로 변경
            isChangeDawnToNormal = true;
        }else if(!BaseEnums.Default.SUCCESS.getCode().equals(result.getCode())){
            // 배송지 변경 불가능
            return result;
        }


        // 녹즙 & 녹즙-내맘대로 인 경우 -> 배송지 등록, 스케쥴 수정, ERP 스케줄 입력
        if(GoodsEnums.GoodsDailyType.GREENJUICE.getCode().equals(dto.getGoodsDailyType())){
            OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = new OrderDetailScheduleListRequestDto();

            // 1. 주문 상품별 도착일 정보 리스트 조회
            MallOrderDetailGoodsDto goodsDto = new MallOrderDetailGoodsDto();
            goodsDto.setPromotionTp(dto.getPromotionTp());
            goodsDto.setOdOrderId(dto.getOdOrderId());
            goodsDto.setGoodsDailyTp(dto.getGoodsDailyType());

            List<OrderDetailScheduleListDto> orderDetailScheduleArrivalDateList = mallOrderDailyDetailBiz.getOrderDetailScheduleArrivalDateList(dto.getDeliveryDt(), goodsDto);
            if(CollectionUtils.isEmpty(orderDetailScheduleArrivalDateList)){
                return ApiResult.result(BaseEnums.Default.FAIL);
            }

            //2. OD_SHIPPING_ZONE -> 새로운 주소지 등록
            long odShippingZoneId = mallOrderDailyDetailBiz.getOrderShippingZoneSeq();

            OrderShippingZoneVo orderShippingZoneVo = OrderShippingZoneVo.builder()
                    .odOrderId(dto.getOdOrderId())
                    .deliveryType(ShoppingEnums.DeliveryType.DAILY.getCode())
                    .shippingType(1)
                    .odShippingZoneId(odShippingZoneId)
                    .recvNm(dto.getReceiverName())
                    .recvHp(dto.getReceiverMobile())
                    .recvZipCd(dto.getReceiverZipCode())
                    .recvAddr1(dto.getReceiverAddress1())
                    .recvAddr2(dto.getReceiverAddress2())
                    .recvBldNo(dto.getBuildingCode())
                    .deliveryMsg(dto.getShippingComment())
                    .doorMsgCd(dto.getAccessInformationType())
                    .doorMsg(dto.getAccessInformationPassword())
                    .build();
            // 권역정보가 존재할 경우 권역정보 Set
            if(!StringUtils.isEmpty(dto.getUrStoreId())) {
                orderShippingZoneVo.setUrStoreId(Long.parseLong(dto.getUrStoreId()));
            }
            orderDetailBiz.addShippingZone(orderShippingZoneVo);

            // 주문상세PK로 일일상품 정보 조회
            MallOrderDetailGoodsDto orderDetailGoodsDto =  mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderDetlId(dto.getOdOrderDetlId());

            // 3. OD_ORDER_DETL_DAILY_SCH 스케쥴 수정, ERP 스케줄 입력
            if(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(dto.getPromotionTp())){
                ApiResult<?> apiResult = mallOrderDailyDetailBiz.putOrderDailyGreenjuiceShippingZone(orderDetailGoodsDto,orderDetailScheduleArrivalDateList, odShippingZoneId, dto.getDeliveryDt());
                if(!apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
                    return apiResult;
                }
            }else{

                List<MallOrderDetailGoodsDto>  orderDetailGoodsList = mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderId(orderDetailGoodsDto.getOdOrderId(), orderDetailGoodsDto.getGoodsDailyTp());

                // 녹즙인 경우 - > 같은 주문건에서 녹즙주문건 모두 변경
                for(MallOrderDetailGoodsDto mallOrderDetailGoodsDto : orderDetailGoodsList){
                    orderDetailScheduleListRequestDto.setOdOrderDetlId(mallOrderDetailGoodsDto.getOdOrderDetlId());
                    orderDetailScheduleListRequestDto.setChangeDate(dto.getDeliveryDt());
                    List<OrderDetailScheduleListDto>  orderDetailList = mallOrderScheduleBiz.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);

                    ApiResult<?> apiResult = mallOrderDailyDetailBiz.putOrderDailyGreenjuiceShippingZone(mallOrderDetailGoodsDto,orderDetailList, odShippingZoneId, dto.getDeliveryDt());
                    if(!apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
                        return apiResult;
                    }
                }
            }

            // 주문배송지 이력 등록
            OrderShippingZoneHistVo orderShippingZoneHistVo = userBuyerService.setAddShippingZoneHistRequestParam(odShippingZoneId, dto);
            userBuyerService.addShippingZoneHist(orderShippingZoneHistVo);

        // 2. 일반상품,일일상품(베이비밀,잇슬림인 경우) 주문배송지 업데이트
        }else if (GoodsEnums.GoodsDailyType.BABYMEAL.getCode().equals(dto.getGoodsDailyType())
				||GoodsEnums.GoodsDailyType.EATSSLIM.getCode().equals(dto.getGoodsDailyType())
		){

            // 같은 주문건에서 동일 브랜드 주소 모두 변경(베이비밀 일괄배송 제외)
            List<MallOrderDetailGoodsDto> orderDetailGoodsList = mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderId(dto.getOdOrderId(), dto.getGoodsDailyType());

            for(MallOrderDetailGoodsDto orderDetailGoodsDto : orderDetailGoodsList){
                // 주문배송지 업데이트
                dto.setOdShippingZoneId(orderDetailGoodsDto.getOdShippingZoneId());
                userBuyerService.putShippingAddressIntoOrderShippingZone(dto);

                // 주문배송지 이력 등록
                OrderShippingZoneHistVo orderShippingZoneHistVo = userBuyerService.setAddShippingZoneHistRequestParam(orderDetailGoodsDto.getOdShippingZoneId(),dto);
                userBuyerService.addShippingZoneHist(orderShippingZoneHistVo);

                if(!StringUtils.isEmpty(dto.getUrStoreId())) {
                    // OD_ORDER_DETL_DAILY 테이블의 주문상세번호로 스토어PK 업데이트 처리
                    orderScheduleBiz.putOrderDetlDailyUrStoreId(orderDetailGoodsDto.getOdOrderId(), 0, orderDetailGoodsDto.getOdShippingZoneId());
                }
            }

        }else{
			// 3. 일반상품 주문배송지 업데이트
			// 주문배송지 업데이트
			userBuyerService.putShippingAddressIntoOrderShippingZone(dto);

			// 주문 배송 정보 이력 등록
			OrderShippingZoneHistVo orderShippingZoneHistVo = userBuyerService.setAddShippingZoneHistRequestParam(dto.getOdShippingZoneId(), dto);
			userBuyerService.addShippingZoneHist(orderShippingZoneHistVo);
		}

		// 새벽배송 -> 택배배송 변경인 경우
		if(isChangeDawnToNormal) {

			// 주문건의 배송유형 새벽-> 택배로 변경
            String goodsDeliveryType = GoodsEnums.GoodsDeliveryType.NORMAL.getCode();
            String orderStatusDetailType = OrderEnums.OrderStatusDetailType.NORMAL.getCode();

			orderOrderBiz.putOrderDetailGoodsDeliveryType(dto.getOdShippingZoneId(), goodsDeliveryType, orderStatusDetailType);
			return ApiResult.success(UserEnums.ChangeDeliveryAddress.CHANGE_DAWN_TO_NORMAL);
		}

		return ApiResult.success(UserEnums.ChangeDeliveryAddress.SUCCESS_CHANGE_DELIVERY_ADDRESS);
	}


    /**
     * 마이페이지 배송지 목록 조회
     * @param urUserId
     * @param ilGoodsId
     * @param odOrderDetlId
     * @return List<CommonGetShippingAddressListResultVo>
     * @throws Exception
     */
	@Override
	public List<CommonGetShippingAddressListResultVo> getMypageShippingAddressList(long urUserId, long ilGoodsId, long odOrderDetlId) throws Exception {

		List<CommonGetShippingAddressListResultVo> result = userBuyerService.getMypageShippingAddressList(urUserId);

	      for(CommonGetShippingAddressListResultVo shippingAddressVo : result){

	    	  // 배송지코드
	    	  if("Y".equals(shippingAddressVo.getLatelyYn())) {
	    		  shippingAddressVo.setShippingAddressType(DeliveryEnums.ShippingType.RECENT_SHIPPING_TYPE.getCode());
	    		  shippingAddressVo.setShippingAddressTypeName(DeliveryEnums.ShippingType.RECENT_SHIPPING_TYPE.getCodeName());
	    	  }else {
	    		  if("Y".equals(shippingAddressVo.getDefaultYn())) {
	    			  shippingAddressVo.setShippingAddressType(DeliveryEnums.ShippingType.BASIC_SHIPPING_TYPE.getCode());
		    		  shippingAddressVo.setShippingAddressTypeName(DeliveryEnums.ShippingType.BASIC_SHIPPING_TYPE.getCodeName());
	    		  }else {
	    			  shippingAddressVo.setShippingAddressType(DeliveryEnums.ShippingType.NORMAL_SHIPPING_TYPE.getCode());
		    		  shippingAddressVo.setShippingAddressTypeName(DeliveryEnums.ShippingType.NORMAL_SHIPPING_TYPE.getCodeName());
	    		  }
	    	  }

	    	  // 주소 기반 배송가능 타입정보
	    	  shippingAddressVo.setDelivery(storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(shippingAddressVo.getReceiverZipCode(), shippingAddressVo.getBuildingCode()));

	    	  // ilGoodsId, odOrderDetlId 넘어올때만 처리(일일상품)
	    	  if (ilGoodsId > 0 && odOrderDetlId > 0){

	    	      // 같은 주문건내 같은 일일상품유형의 상품PK 조회
	    	      List<Long> orderGoodsIdList = mallOrderDailyDetailBiz.getOrderGoodsIdListByOdOrderDetlId(odOrderDetlId);

	    	      int shippingPossibilityDtoNullCount = 0;
                  for(Long orderIlGoodsId : orderGoodsIdList){
                      // 배송 권역정보
                      ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityDto = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(orderIlGoodsId,shippingAddressVo.getReceiverZipCode(), shippingAddressVo.getBuildingCode());
                      if(shippingPossibilityDto == null){
                          shippingPossibilityDtoNullCount++;
                      }
                  }

		    	  if(shippingPossibilityDtoNullCount == 0) {
		    		  shippingAddressVo.setShippingPossibilityStoreDeliveryArea(true);

                      // 녹즙 - 배송지 변경 가능한 가장 빠른 도착예정일 리스트 세팅
                      String defaultDelvDate = "";
                      List<String> delvDateList = new ArrayList<>();
                      List<String> delvDateWeekDayList = new ArrayList<>();


                      // 1. 일일배송 스케쥴 정보 조회
                      OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = new OrderDetailScheduleListRequestDto();
                      List<OrderDetailScheduleListDto>  orderDetailScheduleArrivalDateList = new ArrayList<>();

                      // 1.1 - 주문상세PK로 일일상품 정보 조회
                      MallOrderDetailGoodsDto orderDetailGoodsDto =  mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderDetlId(odOrderDetlId);

                      if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetailGoodsDto.getGoodsTpCd())
                            && ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetailGoodsDto.getPromotionTp())){
                          // 1.2 - 녹즙-내맘대로 인 경우 -> OD_ORDER_ID 세팅
                          orderDetailScheduleListRequestDto.setOdOrderId(String.valueOf(orderDetailGoodsDto.getOdOrderId()));
                          orderDetailScheduleListRequestDto.setPromotionYn("Y");

                          orderDetailScheduleArrivalDateList = mallOrderScheduleBiz.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);
                      }else{
                          // 1.3 - 녹즙인 경우 - > 같은 주문건에서 녹즙주문건 모두 조회
                          List<MallOrderDetailGoodsDto> orderDetailGoodsList = mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderId(orderDetailGoodsDto.getOdOrderId(), orderDetailGoodsDto.getGoodsDailyTp());

                          for(MallOrderDetailGoodsDto dto : orderDetailGoodsList){
                              // OD_ORDER_DETL_ID 세팅
                              orderDetailScheduleListRequestDto.setOdOrderDetlId(dto.getOdOrderDetlId());
                              List<OrderDetailScheduleListDto>  orderDetailList = mallOrderScheduleBiz.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);
                              orderDetailScheduleArrivalDateList.addAll(orderDetailList);
                          }
                      }


                      // 2. 스케쥴정보에서 변경 가능한 도착예정일 리스트만 추출해서 세팅
                      // 2.1 - 도착일 중복제거
                      orderDetailScheduleArrivalDateList = orderDetailScheduleArrivalDateList.stream().filter(distinctByKey(f -> Arrays.asList(f.getDelvDate()))).collect(toList());

                      // 2-2 - 도착예정일 리스트 세팅
                      if(CollectionUtils.isNotEmpty(orderDetailScheduleArrivalDateList)){

                          //리스트 정렬
                          List<OrderDetailScheduleListDto>  sortedOrderDetailScheduleArrivalDateList = orderDetailScheduleArrivalDateList.stream()
                                  .sorted(Comparator.comparing(OrderDetailScheduleListDto::getDelvDate))
                                  .collect(toList());

                          try{
                              for(OrderDetailScheduleListDto schDto : sortedOrderDetailScheduleArrivalDateList){
                                  String delvDate = schDto.getDelvDate().replaceAll("-", ""); // 도착예정일
                                  String delvDateWeekDay = schDto.getDelvDateWeekDay(); // 도착예정일의 요일
                                  int addDate = OrderScheduleEnums.ScheduleChangeDate.findByCode(delvDateWeekDay).getAddDate();

                                  String possibleChangeDate = DateUtil.addDays(delvDate, addDate, "yyyyMMdd");// 해당 도착예정일의 스케쥴 변경 가능일자
                                  String currentDate = DateUtil.getCurrentDate();
                                  if (Integer.parseInt(currentDate) <= Integer.parseInt(possibleChangeDate)){
                                      if(delvDateList.size() < 5){
                                          delvDateList.add(delvDate);
                                          delvDateWeekDayList.add(delvDateWeekDay);
                                      }
                                  }

                              }
                          }catch(Exception e){
                          }
                      }
                      if(CollectionUtils.isNotEmpty(delvDateList)){
                          Date date = new SimpleDateFormat("yyyyMMdd").parse(delvDateList.get(0));
                          defaultDelvDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                      }

                      shippingAddressVo.setDefaultDelvDate(defaultDelvDate);
                      shippingAddressVo.setDelvDate(delvDateList);
                      shippingAddressVo.setDelvDateWeekDay(delvDateWeekDayList);
                  }
		      }
	    }

	    return result;
	}


	/**
	 * @Desc 최근배송지 저장
	 * @param urUserId
	 * @param odOrderId
	 * @return int
	 */
	@Override
	public int saveLatelyShippingAddress(long urUserId, long odOrderId) throws Exception{

		if(urUserId > 0) {

			// 주문시 최근배송지 존재 유무 확인
			Long urLatelyShippingAddrId = userBuyerShippingAddrBiz.getOrderLatelyShippingAddressCount(urUserId, odOrderId);

			// 최근배송지 날짜 업데이트
			if(urLatelyShippingAddrId != null) {

				userBuyerShippingAddrBiz.putLatelyShippingAddress(urLatelyShippingAddrId);
			// 최근배송지 추가
			}else {
				userBuyerShippingAddrBiz.addLatelyShippingAddress(urUserId, odOrderId);
			}
		}

		return 1;
	}

    @Override
    public ApiResult<?> getAsisUserPoint(AsisUserPointRequestDto dto) throws Exception {

	    // 임직원여부
	    String employeeYn = "N";    // (Y-임직원, N-일반회원)
        String customerNumber = "";     // 고객번호 또는 임직원 사번

        // 임직원 검증
        employeeYn = (dto.getLoginId().matches("[0-9]{5,6}")?"Y":"N");

        if("N".equals(employeeYn)) {
            // as-is 일반 회원 API
            UserInfoCheckResponseDto userInfoCheckDto = asisUserApiUtil.userInfoCheck(dto.getLoginId(), dto.getPassword());
            if(userInfoCheckDto.getResultCode() == null || !userInfoCheckDto.getResultCode().equals(UserEnums.AsisUserInfoCheckResult.SUCCESS.getCode())) {
                // 1208 로그인 실패
                return ApiResult.result(UserEnums.Join.LOGIN_FAIL);
            }

            customerNumber = userInfoCheckDto.getCustomerNumber();
        } else {
            customerNumber = dto.getLoginId();
        }

        AsisUserPointResponseDto responseDto = new AsisUserPointResponseDto();

        //기존 적립금 정보 조회
        SearchCustomerRsrvTotalResponseDto searchCustomerRsrvTotalResponseDto = asisUserApiUtil.searchCustomerRsrvTotal(customerNumber, employeeYn, dto.getPassword());
        if(searchCustomerRsrvTotalResponseDto.getStatus() == 98) {
            // 1208 로그인 실패
            return ApiResult.result(UserEnums.Join.LOGIN_FAIL);
        } else if(searchCustomerRsrvTotalResponseDto.getStatus() == 200) {
            responseDto.setPulmuonePoint(searchCustomerRsrvTotalResponseDto.getPulmuoneShopPoint());
            responseDto.setOrgaPoint(searchCustomerRsrvTotalResponseDto.getOrgaPoint());
            return ApiResult.success(responseDto);
        } else {
            return ApiResult.fail();
        }
    }

    /***
     * 포인트 이관 이벤트
     * @param dto
     * @return
     * @throws Exception
     */
    @Override
    public ApiResult<?> depositPointByAsisPoint(AsisUserPointRequestDto dto) throws Exception {

        // 임직원여부
        String employeeYn = "N";    // (Y-임직원, N-일반회원)
        String customerNumber = "";     // 고객번호 또는 임직원 사번

        // 임직원 검증
        employeeYn = (dto.getLoginId().matches("[0-9]{5,6}")?"Y":"N");

        if("N".equals(employeeYn)) {
            // as-is회원 API
            UserInfoCheckResponseDto userInfoCheckDto = asisUserApiUtil.userInfoCheck(dto.getLoginId(), dto.getPassword());
            if (userInfoCheckDto.getResultCode() == null || !userInfoCheckDto.getResultCode().equals(UserEnums.AsisUserInfoCheckResult.SUCCESS.getCode())) {
                // 1208 로그인 실패
                return ApiResult.result(UserEnums.Join.LOGIN_FAIL);
            }
            dto.setCustomerNumber(userInfoCheckDto.getCustomerNumber());
        } else {
            dto.setCustomerNumber(dto.getLoginId());
        }

        //기존 적립금 정보 조회
        SearchCustomerRsrvTotalResponseDto searchCustomerRsrvTotalResponseDto = asisUserApiUtil.searchCustomerRsrvTotal(dto.getCustomerNumber(), "N", "");

        if(searchCustomerRsrvTotalResponseDto.getStatus() == 98) {
            // 1208 로그인 실패
            return ApiResult.result(UserEnums.Join.LOGIN_FAIL);
        } else if(searchCustomerRsrvTotalResponseDto.getStatus() == 200) {
            dto.setAsIsPulmuonePoint(searchCustomerRsrvTotalResponseDto.getPulmuoneShopPoint());
            dto.setAsIsOrgaPoint(searchCustomerRsrvTotalResponseDto.getOrgaPoint());
        } else {
            return ApiResult.fail();
        }

        //validation
        ApiResult<?> validationResponse = userBuyerService.validationDepositPointByAsIsPoint(dto);
        if(!validationResponse.getMessageEnum().equals(BaseEnums.Default.SUCCESS)){
            return validationResponse;
        }

        // 풀무원 적립금 적립
        ApiResult<?> pulmuoneResponse = userBuyerService.depositPointByAsIsPointProcess(dto, GoodsEnums.MallDiv.PULMUONE);
        if(!pulmuoneResponse.getMessageEnum().equals(BaseEnums.Default.SUCCESS)){
            return pulmuoneResponse;
        }

        // 올가 적립금 적립
        ApiResult<?> orgaResponse = userBuyerService.depositPointByAsIsPointProcess(dto, GoodsEnums.MallDiv.ORGA);
        if(!orgaResponse.getMessageEnum().equals(BaseEnums.Default.SUCCESS)){
            return orgaResponse;
        }

        // return 값 생성
        AsisUserPointResponseDto responseDto = new AsisUserPointResponseDto();
        responseDto.setPulmuonePoint(dto.getAsIsPulmuonePoint() - dto.getPulmuonePoint());
        responseDto.setOrgaPoint(dto.getAsIsOrgaPoint() - dto.getOrgaPoint());
        return ApiResult.success(responseDto);
    }

    @Override
    public ApiResult<?> isPossibleChangeDeliveryAddress(IsPossibleChangeDeliveryAddressRequestDto reqDto) throws Exception{

        // 출고처별 배송 불가 지역 체크
        List<HashMap> orderInfoMap = mallOrderDetailBiz.getOrderInfoForShippingPossibility(reqDto.getOdOrderId(), null);
        if(CollectionUtils.isNotEmpty(orderInfoMap)){
            for(HashMap map : orderInfoMap){
                boolean isDawnDelivery = Integer.parseInt(String.valueOf(map.get("IS_DAWN_DELIVERY"))) > 0 ? true : false;
                // 출고처 주소 기반 배송 가능 정보 조회
                WarehouseUnDeliveryableInfoDto warehouseUnDeliveryableInfoDto = storeWarehouseBiz.getUnDeliverableInfo(Long.parseLong(String.valueOf(map.get("UR_WAREHOUSE_ID"))), reqDto.getZipCode(), isDawnDelivery);

                boolean isShippingPossibility = warehouseUnDeliveryableInfoDto.isShippingPossibility();
                String shippingImpossibilityMsg = warehouseUnDeliveryableInfoDto.getShippingImpossibilityMsg();

                if(!isShippingPossibility){
                    return ApiResult.result(shippingImpossibilityMsg, UserEnums.ChangeDeliveryAddress.WAREHOUSE_UNDELIVERABLE_AREA);
                }
            }
        }

    	// 변경 배송지 배송 가능 타입정보 조회
    	ShippingAddressPossibleDeliveryInfoDto shippingPossibleInfo = storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(reqDto.getZipCode(), reqDto.getBuildingCode());

    	// 1.일일배송
    	if(ShoppingEnums.DeliveryType.DAILY.getCode().equals(reqDto.getDeliveryType())) {

    		// 주문배송지 PK 기준 상품PK,스토어PK별 정보 조회
    		List<OrderDetailByOdShippingZondIdResultDto> orderDetailDtoList =  mallOrderDetailBiz.getOrderDetailInfoByOdShippingZoneId(reqDto.getOdShippingZoneId());

    		for(OrderDetailByOdShippingZondIdResultDto dto : orderDetailDtoList) {

    			// 변경 배송지의 스토어 배송권역정보 조회
    			ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityDto = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(dto.getIlGoodsId(), reqDto.getZipCode(), reqDto.getBuildingCode());

    			// 기존 주문 배송권역의 배송방식이 매일이고 변경 배송지 배송권역이 격일일 경우(매일권역 -> 격일권역으로 변경불가)
    			if(shippingPossibilityDto == null || (StoreEnums.StoreDeliveryIntervalTpye.EVERY.getCode().equals(dto.getStoreDeliveryIntervalTp())
    					&& StoreEnums.StoreDeliveryIntervalTpye.TWO_DAYS.getCode().equals(shippingPossibilityDto.getStoreDeliveryIntervalType()))) {
    				// 배송지 변경 불가 -> 배송불가 상품 존재
    				return ApiResult.result(UserEnums.ChangeDeliveryAddress.EXIST_DELIVERY_NOT_POSSIBLE_GOODS);
    			}

    		}

    	// 2.일반배송, 예약배송, 정기배송
    	}else {

    		// 2-1. 주문 PK기준 배송템플릿별 주문정보 조회
    		List<ShippingInfoByOdOrderIdResultVo> shippingInfoList = mallOrderDetailBiz.getShippingInfoByOdOrderId(reqDto.getOdOrderId());

    		for(ShippingInfoByOdOrderIdResultVo resultVo : shippingInfoList) {
    			Long ilShippingTmlpId = resultVo.getIlShippingTmplId();

    			// 기존 배송템플릿 배송정보
    			ShippingDataResultVo originShippingDataResultVo = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(ilShippingTmlpId);

    			int goodsPrice = resultVo.getTotalPaymentPrice();
    			int goodsQty = resultVo.getTotalOrderCnt();

    			// 변경 배송지 지역별 배송비 조회
    			ShippingPriceResponseDto changeShippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(originShippingDataResultVo, goodsPrice, goodsQty, reqDto.getZipCode());

    			// 2-1-1. 배송비 결제 금액 변경된 경우
    			int originShippingPrice = resultVo.getOrgShippingPrice();  				// 기존 배송지의 배송비
    			int changeShippingPrice = changeShippingPriceDto.getShippingPrice();  	// 변경 배송지의 배송비

    			if(originShippingPrice > changeShippingPrice) {
    				// 배송지 변경 불가 -> 이미 추가배송비가 결제된 주문
    				return ApiResult.result(UserEnums.ChangeDeliveryAddress.PAID_ADDITIONAL_SHIPPING_PRICE);
    			}
    			if(originShippingPrice < changeShippingPrice) {
    				// 배송지 변경 불가 -> 추가배송비 결제 필요
    				return ApiResult.result(UserEnums.ChangeDeliveryAddress.NEED_ADDITIONAL_SHIPPING_PRICE);
    			}

    		}

    		// 2-2. 주문 PK기준 상품별 주문정보 조회
    		List<ShippingInfoByOdOrderIdResultVo> orderInfoList = mallOrderDetailBiz.getOrderDetailInfoByOdOrderId(reqDto.getOdOrderId());
    		for(ShippingInfoByOdOrderIdResultVo resultVo : orderInfoList) {

    			// 상품 배송정책 PK별 배송정책 정보
    			ShippingDataResultVo goodsShippingData = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(resultVo.getIlGoodsShippingTemplateId());

    			//2-2-1. 배송정책의 배송불가지역 체크
    			String undeliverableAreaTypeByShippingTemplate = goodsShippingData.getUndeliverableAreaType();
				boolean isUnDeliverableAreaByShippingTemplate = goodsShippingTemplateBiz.isUnDeliverableArea(undeliverableAreaTypeByShippingTemplate, reqDto.getZipCode());
				if(isUnDeliverableAreaByShippingTemplate) {
					// 배송지 변경 불가 -> 배송불가 상품 존재
					return ApiResult.result(UserEnums.ChangeDeliveryAddress.EXIST_DELIVERY_NOT_POSSIBLE_GOODS);
				}

				// 2-2-2. 상품의 배송불가지역 체크
				String undeliverableAreaTypeByItem = resultVo.getUndeliverableAreaTp();
				boolean isUnDeliverableAreaByItem = goodsShippingTemplateBiz.isUnDeliverableArea(undeliverableAreaTypeByItem, reqDto.getZipCode());
				if(isUnDeliverableAreaByItem) {
					// 배송지 변경 불가 -> 배송불가 상품 존재
					return ApiResult.result(UserEnums.ChangeDeliveryAddress.EXIST_DELIVERY_NOT_POSSIBLE_GOODS);
				}

				// 2-2-3. 새벽배송이고 변경 배송지가 택배배송만 가능한 경우
				if(GoodsEnums.GoodsDeliveryType.DAWN.getCode().equals(resultVo.getGoodsDeliveryType())
						&& !shippingPossibleInfo.isDawnDelivery() && shippingPossibleInfo.isShippingCompDelivery()) {

					// 배송지 변경 불가 -> 기존 배송유형과 상이
					return ApiResult.result(UserEnums.ChangeDeliveryAddress.DIFFERENT_DELIVERY_TYPE);
				}
	    	}

    	}

    	return ApiResult.success();
    }

    @Override
    public String getUserStatusByMeta() {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return UserEnums.MetaUserStatusType.NOT_LOGIN.getCode();
        }

        boolean isMember = StringUtil.isNotEmpty(buyerVo.getUrUserId());
        boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
        boolean isNonMember = StringUtil.isNotEmpty(buyerVo.getNonMemberCiCd());

        if (isEmployee) {
            return UserEnums.MetaUserStatusType.EMPLOYEE.getCode();
        }
        if (isMember) {
            return UserEnums.MetaUserStatusType.MEMBER.getCode();
        }
        if (isNonMember) {
            return UserEnums.MetaUserStatusType.NONMEMBER.getCode();
        }

        return UserEnums.MetaUserStatusType.NOT_LOGIN.getCode();
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
