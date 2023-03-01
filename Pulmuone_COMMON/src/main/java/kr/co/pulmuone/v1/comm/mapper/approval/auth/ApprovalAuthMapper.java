package kr.co.pulmuone.v1.comm.mapper.approval.auth;

import java.util.List;

import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalDuplicateRequestVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthManagerHistoryByTaskDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthInfoVo;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthManagerVo;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;

@Mapper
public interface ApprovalAuthMapper {

	List<ApprovalAuthManagerVo> getApprovalAuthManagerList(@Param("apprKindType")String apprKindType, @Param("apprManagerType")String apprManagerType);

	int delApprovalAuthManager(@Param("apprKindType")String apprKindType, @Param("apprManagerType")String apprManagerType);

	int addApprovalAuthManager(ApprovalAuthManagerVo managerVo);

	String getItemApprItemCode(@Param("taskPk") String taskPk);

	String getGoodsApprItemCode(@Param("taskPk") String taskPk);

	String getItemPriceApprItemCode(@Param("taskPk") String taskPk);

	String getGoodsDiscountApprItemCode(@Param("taskPk") String taskPk);

	ApprovalAuthManagerHistoryByTaskDto getApprovalAuthManagerHistoryCoupon(@Param("userId")String userId);

	ApprovalAuthManagerHistoryByTaskDto getApprovalAuthManagerHistoryPoint(@Param("userId")String userId);

	ApprovalAuthManagerHistoryByTaskDto getApprovalAuthManagerHistoryExhibit(@Param("userId")String userId, @Param("exhibitTp")String exhibitTp);

	ApprovalAuthManagerHistoryByTaskDto getApprovalAuthManagerHistoryItem(@Param("userId")String userId, @Param("apprKindTp")String apprKindTp);

	ApprovalAuthManagerHistoryByTaskDto getApprovalAuthManagerHistoryGoods(@Param("userId")String userId, @Param("apprKindTp")String apprKindTp);

	ApprovalAuthManagerHistoryByTaskDto getApprovalAuthManagerHistoryItemPrice(@Param("userId")String userId);

	ApprovalAuthManagerHistoryByTaskDto getApprovalAuthManagerHistoryGoodsDiscount(@Param("userId")String userId);

	ApprovalAuthManagerHistoryByTaskDto getApprovalAuthManagerHistoryCsRefund(@Param("userId")String userId);

	ApprovalAuthInfoVo getApprovalProcessCouponInfo(@Param("pmCouponId") String pmCouponId);

	ApprovalAuthInfoVo getApprovalProcessPointInfo(@Param("pmPointId") String pmPointId);

	ApprovalAuthInfoVo getApprovalProcessExhibitInfo(@Param("evExhibitId") String evExhibitId);

	ApprovalAuthInfoVo getApprovalProcessItemRegistInfo(@Param("ilItemApprId") String ilItemApprId);

	ApprovalAuthInfoVo getApprovalProcessGoodsRegistInfo(@Param("ilGoodsApprId") String ilGoodsApprId);

	ApprovalAuthInfoVo getApprovalProcessGoodsDiscountInfo(@Param("ilGoodsDiscountApprId") String ilGoodsDiscountApprId);

	ApprovalAuthInfoVo getApprovalProcessItemPriceInfo(@Param("ilItemPriceApprId") String ilItemPriceApprId);

	ApprovalAuthInfoVo getApprovalProcessCsRefundInfo(@Param("odCsId") String odCsId);

	List<ApprovalStatusVo> getApprovalHistoryCoupon(@Param("pmCouponId") String pmCouponId);

	List<ApprovalStatusVo> getApprovalHistoryPoint(@Param("pmPointId") String pmPointId);

	List<ApprovalStatusVo> getApprovalHistoryExhibit(@Param("taskPk") String taskPk, @Param("exhibitTp")String exhibitTp);

	List<ApprovalStatusVo> getApprovalHistoryGoods(@Param("ilGoodsApprId") String ilGoodsId, @Param("apprKindTp") String apprKindTp);

	List<ApprovalStatusVo> getApprovalHistoryItem(@Param("ilItemApprId") String ilItemId, @Param("apprKindTp") String apprKindTp);

	List<ApprovalStatusVo> getApprovalHistoryItemPrice(@Param("ilItemPriceApprId") String ilItemPriceApprId);

	List<ApprovalDuplicateRequestVo> getDuplicateApprovalRegist(@Param("ilItemCode") String ilItemCode);

	List<ApprovalDuplicateRequestVo> getDuplicateApprovalPrice(@Param("ilItemCode") String ilItemCode);

}
