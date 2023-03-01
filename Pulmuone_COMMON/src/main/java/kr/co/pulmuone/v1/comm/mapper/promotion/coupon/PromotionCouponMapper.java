package kr.co.pulmuone.v1.comm.mapper.promotion.coupon;

import java.util.List;

import kr.co.pulmuone.v1.promotion.coupon.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthManagerVo;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsApplyCouponDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.AccountInfoVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.BuyerInfoListResultVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponApplicationListByUserVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponApprovalResultVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponCountByUserVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponCoverageVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponDetailVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponGoodsByUserJoinVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponInfoByUserJoinVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponIssueVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponListByUserVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponListResultVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponValidationInfoVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CoverageVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.IssueListResultVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.OrganizationListResultVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.OrganizationPopupListResultVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.UploadInfoVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.UseCouponValidationInfoVo;
import kr.co.pulmuone.v1.shopping.cart.dto.CouponApplicationListRequestDto;

@Mapper
public interface PromotionCouponMapper {

	Page<CouponListResultVo> getCpnMgm(CouponRequestDto couponRequestDto) throws Exception;

	CouponDetailVo getCoupon(String pmCouponId);

	Page<OrganizationPopupListResultVo> getOrganizationPopupList(OrganizationPopupListRequestDto organizationPopupListRequestDto) throws Exception;

	int addCoupon(CouponRequestDto couponRequestDto) throws Exception;

	int addCouponCoverage(List<CoverageVo> coverageVoList) throws Exception;

	int putCoupon(CouponRequestDto couponRequestDto) throws Exception;

	int addOrganization(CouponRequestDto couponRequestDto) throws Exception;

	int addSecondOrganization(CouponRequestDto couponRequestDto) throws Exception;

	ApprovalStatusVo getCouponStatusHistory(CouponRequestDto couponRequestDto) throws Exception;

	int addCouponStatusHistory(ApprovalStatusVo history);

	int putCouponIssueList(CouponIssueParamDto couponIssueParamDto) throws Exception;

	int putCouponIssueHistoryList(CouponIssueParamDto couponIssueParamDto) throws Exception;

	int putShippingFeeCouponReIssueYn(Long pmCouponIssueId) throws Exception;

	int addSerialNumber(CouponRequestDto couponRequestDto) throws Exception;

	int getDuplicateSerialNumber(UploadInfoVo uploadInfoVo) throws Exception;

	int getDuplicateFixedNumber(CouponRequestDto couponRequestDto) throws Exception;

	int removeCouponCoverage(CouponRequestDto couponRequestDto) throws Exception;

	int removeOrganization(CouponRequestDto couponRequestDto) throws Exception;

	int removeCouponIssue(CouponRequestDto couponRequestDto) throws Exception;

	int removeSerialNumber(CouponRequestDto couponRequestDto) throws Exception;

	int removeCoupon(CouponRequestDto couponRequestDto) throws Exception;

	int updateCouponName(CouponRequestDto couponRequestDto) throws Exception;

	int getIssueCouponChk(CouponRequestDto couponRequestDto) throws Exception;

	int putPmCouponIssueCancel(CouponRequestDto couponRequestDto) throws Exception;

	int getUserIdCnt(CouponIssueParamDto couponIssueParamDto) throws Exception;

	CouponDetailVo getCopyCoupon(CouponRequestDto couponRequestDto) throws Exception;

	Page<IssueListResultVo> getCpnMgmList(CouponRequestDto couponRequestDto) throws Exception;

	Page<BuyerInfoListResultVo> putCouponIssueList(CouponRequestDto couponRequestDto) throws Exception;

	Page<BuyerInfoListResultVo> getCpnMgmIssueList(BuyerInfoRequestDto buyerInfoRequestDto) throws Exception;

	List<BuyerInfoListResultVo> getCpnMgmIssueDuplicateParamList(IssueUserRequestDto issueUserRequestDto) throws Exception;

	int putCancelDepositList(CouponIssueParamDto couponIssueParamDto) throws Exception;

	int putCouponIssueWithdraw(CouponRequestDto couponRequestDto) throws Exception;

	int putPmCouponStatus(CouponRequestDto couponRequestDto) throws Exception;

	List<IssueListResultVo> getCouponIssueInfo(CouponRequestDto couponRequestDto) throws Exception;

	int putPmCouponIssueStatus(CouponRequestDto couponRequestDto) throws Exception;

	List<IssueListResultVo> getCouponIssueListExcel(CouponRequestDto couponRequestDto) throws Exception;

	OrganizationListResultVo getOrganizationList(CouponRequestDto couponRequestDto) throws Exception;

	List<CoverageVo> getCoverageList(CouponRequestDto couponRequestDto) throws Exception;

	List<AccountInfoVo> getSerialNumberList(CouponRequestDto couponRequestDto) throws Exception;

	List<AccountInfoVo> getUserList(CouponRequestDto couponRequestDto) throws Exception;

	List<ApprovalAuthManagerVo> getApprUserList(CouponRequestDto couponRequestDto) throws Exception;

	int putTicketCollectStatus(CouponRequestDto couponRequestDto) throws Exception;


	Integer getNewBuyerSpecialsCouponByNonMember(@Param("ilGoodsId") Long ilGoodsId, @Param("deviceInfo") String deviceInfo, @Param("isApp") boolean isApp) throws Exception;

    List<GoodsApplyCouponDto> getGoodsApplyCouponList(@Param("ilGoodsId") Long ilGoodsId, @Param("urUserId") Long urUserId) throws Exception;

    Page<CouponListByUserVo> getCouponListByUser(CouponListByUserRequestDto dto) throws Exception;

    List<CouponCoverageVo> getCouponCoverage(@Param("pmCouponId") Long pmCouponId, @Param("includeYn") String includeYn) throws Exception;

    CouponValidationInfoVo getAddCouponValidationInfo(@Param("urUserId") Long urUserId, @Param("pmCouponId") Long pmCouponId) throws Exception;

	int putCouponIssue(@Param("pmCouponId") Long pmCouponId) throws Exception;

	UseCouponValidationInfoVo getUseCouponValidationInfo(@Param("urUserId") Long urUserId, @Param("pmCouponIssueId") Long pmCouponIssueId) throws Exception;

	int putCouponIssueUse(@Param("pmCouponIssueId") Long pmCouponIssueId) throws Exception;

    void addCouponIssue(CouponIssueRequestDto dto) throws Exception;

    void addCouponIssueStatusHistory(@Param("pmCouponIssueId") Long pmCouponIssueId, @Param("status") String status, @Param("urUserId") Long urUserId) throws Exception;

    CouponCountByUserVo getCouponCountByUser(@Param("urUserId") Long urUserId, @Param("status") String status) throws Exception;

    List<CouponInfoByUserJoinVo> getCouponInfoByUserJoin() throws Exception;

    List<CouponGoodsByUserJoinVo> getUserJoinGoods() throws Exception;

    List<CouponApplicationListByUserVo> getCouponApplicationListByUser(CouponApplicationListRequestDto couponApplicationListRequestDto) throws Exception;

    List<CouponApplicationListByUserVo> getShippingCouponApplicationListByUser(@Param("urUserId")Long urUserId, @Param("urWarehouseId")Long urWarehouseId) throws Exception;

    List<CouponApplicationListByUserVo> getCartCouponApplicationListByUser(Long urUserId) throws Exception;

    List<CouponApplicationListByUserVo> getCouponApplicationListByPmCouponIssueId(Long pmCouponIssueId) throws Exception;

	List<GoodsApplyCouponDto> getCouponByPmCouponIdList(@Param("pmCouponIdList") List<Long> pmCouponIdList, @Param("urUserId") Long urUserId) throws Exception;

	Page<CouponApprovalResultVo> getApprovalCouponList(CouponApprovalRequestDto requestDto);

	int putCancelRequestApprovalCoupon(ApprovalStatusVo approvalVo);

	int putDisposalApprovalCoupon(ApprovalStatusVo approvalVo);

	int putApprovalProcessCoupon(ApprovalStatusVo approvalVo);

	void putWithdrawalMemberCoupon(Long urUserId) throws Exception;

	List<CouponListResultVo> getEventCallCouponInfo(CouponRequestDto couponRequestDto);

	List<CouponListResultVo> getCouponSearchStatus(CouponRequestDto couponRequestDto);

	CouponIssueVo getCouponIssueByPmCouponIssueId(Long pmCouponIssueId) throws Exception;

	int addPmCouponCoverageByPmCouponId(@Param("pmCouponId")Long pmCouponId, @Param("originPmCouponId")Long originPmCouponId) throws Exception;

	int addPmCouponPointShareOrganizaionByPmCouponId(@Param("pmCouponId")Long pmCouponId, @Param("originPmCouponId")Long originPmCouponId) throws Exception;

	List<CouponListResultVo> getOrgInfo(CouponRequestDto couponRequestDto);

	int putSerialNumberStatus(CouponRequestDto couponRequestDto) throws Exception;

	String getCouponNameById(Long pmCouponId) throws Exception;

	int addPmCouponJoinEventListByJoinUrUserId(CouponJoinCertEventRequestDto couponJoinCertEventRequestDto) throws Exception;

	int getPmCouponJoinDupleCnt(String customerNumber) throws Exception;
}
