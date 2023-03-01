package kr.co.pulmuone.v1.promotion.coupon.service;

import java.util.List;

import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.*;
import org.springframework.web.multipart.MultipartFile;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.CouponEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.DeviceType;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.goods.dto.DiscountCalculationResultDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsApplyCouponDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponApplicationListByUserVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponCountByUserVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponGoodsByUserJoinVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponInfoByUserJoinVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponValidationInfoVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.IssueListResultVo;
import kr.co.pulmuone.v1.shopping.cart.dto.CartDeliveryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GoodsCouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.ShippingCouponDto;

public interface PromotionCouponBiz {


	/**
	 * 쿠폰목록  조회
	 */
	ApiResult<?> getCpnMgm(CouponRequestDto couponRequestDto) throws Exception;

	/**
	 * 쿠폰 상세조회
	 */
	ApiResult<?> getCoupon(CouponRequestDto couponRequestDto) throws Exception;

	/**
	 * 조직 조회
	 */
	ApiResult<?> getOrganizationPopupList(OrganizationPopupListRequestDto organizationPopupListRequestDto) throws Exception;

	/**
	 * 쿠폰 정보 등록
	 * @param couponRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> addCoupon(CouponRequestDto couponRequestDto) throws Exception;

	/**
	 * 쿠폰 정보 수정
	 * @param couponRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putCoupon(CouponRequestDto couponRequestDto) throws Exception;

	/**
	 * 쿠폰 삭제
	 * @param couponRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> removeCoupon(CouponRequestDto couponRequestDto) throws Exception;

	/**
	 * 쿠폰 명 수정
	 * @param couponRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> updateCouponName(CouponRequestDto couponRequestDto) throws Exception;

	/**
	 * 쿠폰 복사
	 * @param couponRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getCopyCoupon(CouponRequestDto couponRequestDto) throws Exception;

	/**
	 * 쿠폰지급 내역 조회
	 * @param couponRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getCpnMgmList(CouponRequestDto couponRequestDto) throws Exception;

	/**
	 * 쿠폰지급(선택회원)
	 * @param couponRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putCouponIssueList(CouponRequestDto couponRequestDto) throws Exception;

	/**
	 * 쿠폰지정 조회
	 * @param buyerInfoRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getCpnMgmIssueList(BuyerInfoRequestDto buyerInfoRequestDto) throws Exception;

	/**
	 * 쿠폰지급  (검색회원)
	 * @param issueUserRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putSearchCouponIssueList(IssueUserRequestDto issueUserRequestDto) throws Exception;

	/**
	 * 쿠폰선택 회수
	 * @param couponRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putCancelDepositList(CouponRequestDto couponRequestDto) throws Exception;


	/**
	 * 쿠폰 승인요청
	 * @param couponRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putCouponStatus(CouponRequestDto couponRequestDto) throws Exception;

	/**
	 * 쿠폰 지급내역 엑셀 선택 다운로드
	 * @param couponRequestDto
	 * @return
	 * @throws Exception
	 */
	ExcelDownloadDto getCouponIssueListExcelDownload(CouponRequestDto couponRequestDto) throws Exception;

	List<IssueListResultVo> getCouponIssueListExcel(CouponRequestDto couponRequestDto) throws Exception;






    List<GoodsApplyCouponDto> getGoodsApplyCouponList(Long ilGoodsId, Long urUserId) throws Exception;

    DiscountCalculationResultDto discountCalculation(int salePrice, String goodsDiscountMethodType, int regularShippingBasicDiscountRate, int maxDiscountValue) throws Exception;

    int getNewBuyerSpecialsCouponByNonMember(Long ilGoodsId, String deviceInfo, boolean isApp) throws Exception;

	CouponListByUserResponseDto getCouponListByUser(CouponListByUserRequestDto dto) throws Exception;

	CouponCoverageResponseDto getCouponCoverage(Long pmCouponId) throws Exception;

    CouponValidationByUserResponseDto checkCouponValidationByUser(Long urUserId, Long pmCouponId) throws Exception;

    void addCouponWithoutValidation(CouponValidationInfoVo dto, Long urUserId, Long pmSerialNumberId) throws Exception;

    CouponCountByUserVo getCouponCountByUser(Long urUserId, String status) throws Exception;

    ApiResult<?> addCouponByList(List<Long> pmCouponIds, Long urUserId) throws Exception;

	CouponEnums.AddCouponValidation addCouponByOne(Long pmCouponId, Long urUserId) throws Exception;

	boolean isUseCouponListValidation(Long urUserId, List<Long> pmCouponIssueIds) throws Exception;

	ApiResult<?> useCoupon(Long urUserId, Long pmCouponIssueId) throws Exception;

    List<CouponInfoByUserJoinVo> getCouponInfoByUserJoin() throws Exception;

	List<CouponGoodsByUserJoinVo> getUserJoinGoods() throws Exception;

    List<GoodsCouponDto> getGoodsCouponApplicationListByUser(Long urUserId, List<CartGoodsDto> cartGoodsList, DeviceType deviceType) throws Exception;

    List<CouponDto> getCartCouponApplicationListByUser(Long urUserId, List<CartGoodsDto> cartGoodsList, DeviceType deviceType) throws Exception;

    List<ShippingCouponDto> getShippingCouponApplicationListByUser(Long urUserId, List<CartDeliveryDto> cartDateDto, DeviceType deviceType) throws Exception;

    CouponDto getGoodsCouponApplicationListByUser(Long urUserId, CartGoodsDto cartGoods, DeviceType deviceType,Long pmCouponIssueId) throws Exception;

    CouponDto getShippingCouponApplicationListByUser(Long urUserId, CartShippingDto cartShippingDto, DeviceType deviceType,Long pmCouponIssueId) throws Exception;

    CouponDto getCartCouponApplicationListByUser(Long urUserId, List<CartGoodsDto> cartGoodsList, DeviceType deviceType, Long pmCouponIssueId) throws Exception;

	List<GoodsApplyCouponDto> getCouponByPmCouponIdList(List<Long> pmCouponIdList, Long urUserId) throws Exception;

	ApiResult<?> getApprovalCouponList(CouponApprovalRequestDto requestDto);

	ApiResult<?> putCancelRequestApprovalCoupon(CouponApprovalRequestDto requestDto) throws Exception;

	ApiResult<?> putDisposalApprovalCoupon(CouponApprovalRequestDto requestDto) throws Exception;

	ApiResult<?> putApprovalProcessCoupon(CouponApprovalRequestDto requestDto) throws Exception;

	void putWithdrawalMemberCoupon(Long urUserid) throws Exception;

	CouponApplicationListByUserVo getCouponApplicationListByPmCouponIssueId(Long pmCouponIssueId) throws Exception;

	ApiResult<?> addUserExcelUpload(MultipartFile file) throws Exception;

	ApiResult<?> addUserExcelUploadByAdm(MultipartFile file) throws Exception;

	ApiResult<?> addTicketExcelUpload(MultipartFile file) throws Exception;

	ApiResult<?> getEventCallCouponInfo(CouponRequestDto couponRequestDto);

	ApiResult<?> getCouponSearchStatus(CouponRequestDto couponRequestDto);

	ApiResult<?> putTicketCollectStatus(CouponRequestDto couponRequestDto) throws Exception;

	void reissueCoupon(Long odOrderId, Long pmCouponIssueId, Long odClaimId, List<OrderClaimGoodsInfoDto> goodsInfoList, String customUrUserId) throws Exception;

	ApiResult<?> getOrgInfo(CouponRequestDto couponRequestDto);

	String getCouponNameById(Long pmCouponId) throws Exception;

	int addPmCouponJoinEventListByJoinUrUserId(CouponJoinCertEventRequestDto couponJoinCertEventRequestDto) throws Exception;

	boolean isPmCouponJoinDup(String customerNumber) throws Exception;

	CouponEnums.UseCouponValidation checkUseCouponValidation(Long urUserId, Long pmCouponIssueId) throws Exception;

}