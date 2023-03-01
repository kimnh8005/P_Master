package kr.co.pulmuone.v1.comm.mapper.promotion.point;

import java.util.List;

import kr.co.pulmuone.v1.promotion.point.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthManagerVo;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.promotion.point.dto.CommonGetPointListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.DepositPointDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointApprovalRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointSettingListRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointSettingMgmRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointUsedDetailAddRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointUsedDetailDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointUsedDto;

@Mapper
public interface PromotionPointMapper {

	Page<PointSettingResultVo> getPointSettingList(PointSettingListRequestDto pointSettingListRequestDto) throws Exception;

	int getPointSettingListCount(PointSettingListRequestDto pointSettingListRequestDto) throws Exception;

	int addPointSetting(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	int addSerialNumber(PointSettingMgmRequestDto pointSettingMgmRequestDto);

	int getDuplicateSerialNumber(UploadInfoVo uploadInfoVo);

	int addOrganization(PointSettingMgmRequestDto pointSettingMgmRequestDto);

	//int addPointStatusHistory(ApprovalHistoryVo approvalHistoryVo);

	void addPointUserGrade(List<PointUserGradeVo> voList) throws Exception;

	int addBosPointIssue(PointSettingMgmRequestDto pointSettingMgmRequestDto);

	int getUserIdCnt(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	PointSettingResultVo getGroupValidityDay(String loginId);

	PointSettingResultVo getPointDetail(String pmPointId);

	PointSettingResultVo getPointInfoById(String pmPointId);

	ApprovalStatusVo getPointStatusHistory(PointSettingMgmRequestDto pointSettingMgmRequestDto);

//	ApprovalHistoryVo getPointStatusHistory(String pmPointId);

	List<AccountInfoVo> getSerialNumberList(PointRequestDto pointRequestDto);

	List<AccountInfoVo> getUserList(PointRequestDto pointRequestDto);

	List<PointUserGradeVo> getUserGradeList(PointRequestDto pointRequestDto);

	List<ApprovalAuthManagerVo> getApprUserList(PointRequestDto pointRequestDto) throws Exception;

	int putPointSetting(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	String getPmPointId(@Param("userId") String userId, @Param("grPmPointId") String grPmPointId)throws Exception;

	int removeGroupPointInfo(@Param("grPmPointId") String grPmPointId) throws Exception;

	int insertPmPointExcel(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	int removeOrganization(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	int remomveBosPointIssue(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	int removePointUserGrade(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	int removeSerialNumber(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	int putPmPointStatus(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	int putGrPmPointStatus(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	List<PointSettingResultVo> getPmPointIdList(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	int updatePointName(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	int updatePointIssueReason(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	UploadInfoVo getUserPoint(PointSettingMgmRequestDto pointSettingMgmRequestDto);

	int putPmSerialNumberApproval(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;


	UploadInfoVo getAdminAmountCheck(PointRequestDto pointRequestDto) throws Exception;

	int removePoint(PointRequestDto pointRequestDto) throws Exception;


	PointVo getPmPoint(Long pmPointId);

	PmOrganizationVo getPmOrganization(Long pmPointId);

	int addDepositPointsPmPointUsed(PointUsedDto pointUsedDto);

	int addDepositPointsPmPointUsedDetl(PointUsedDetailDto pointUsedDetailDto);

    String getPointExpectExpired(@Param("urUserId") Long urUserId, @Param("startDate") String startDate, @Param("endDate") String endDate) throws Exception;

    Page<CommonGetPointListByUserVo> getPointListByUser(CommonGetPointListByUserRequestDto dto) throws Exception;

    List<CommonGetPointExpectExpireListResultVo> getPointExpectExpireList(@Param("urUserId") Long urUserId, @Param("startDate") String startDate, @Param("endDate") String endDate) throws Exception;

    CommonGetAddPointValidationInfoVo getAddPointValidationInfo(@Param("urUserId") Long urUserId, @Param("pmPointId") Long pmPointId) throws Exception;

    void addPointIssue(@Param("urUserId") Long urUserId, @Param("pmPointId") Long pmPointId) throws Exception;

    List<PointUsedDetailVo> selectUsablePointListOrderByExpiryDate(@Param("urUserId") Long urUserId);

	void insertPointUsedDetail(PointUsedDetailAddRequestDto pointDetailAddRequestList);

	void closeUsedPoint(@Param("urUserId") Long urUserId, @Param("closePointRefList") List<Long> closePointRefList);

	int insertPointUsed(DepositPointDto depositPointDto);

	List<PointUsedDetailVo> selectPointDetailListUsedOrder(@Param("urUserId") long urUserId, @Param("orderNo") String orderNo);

	List<PointUsedDetailVo> selectExpiredPointList(@Param("expireDate") String expireDate);

	int addPointStatusHistory(ApprovalStatusVo history);

	Page<PointApprovalResultVo> getApprovalPointList(PointApprovalRequestDto requestDto);

	int putCancelRequestApprovalPoint(ApprovalStatusVo approvalVo);

	int putDisposalApprovalPoint(ApprovalStatusVo approvalVo);

	int putApprovalProcessPoint(ApprovalStatusVo approvalVo);

	PointExpiredForEmailVo getPointExpectExpiredForEmail(Long urUserId);

	List<PointExpiredListForEmailVo> getPointExpectExpireListForEmail(Long urUserId);

	List<PointSettingResultVo> getEventCallPointInfo(PointRequestDto pointRequestDto);

	List<PointSettingResultVo> getPointSearchStatus(PointRequestDto pointRequestDto);

	int putTicketCollectStatus(PointRequestDto pointRequestDto) throws Exception;

	int putSerialNumberStatus(PointRequestDto pointRequestDto) throws Exception;

	int getDuplicateFixedNumber(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	int putPointApprDateBySystem(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception;

	List<PointPayInfoVo> getPointPayInfo(@Param("pmPointId") String pmPointId);

	List<PointPayListVo> getPointPayListExportExcel(PointRequestDto pointRequestDto);

}
