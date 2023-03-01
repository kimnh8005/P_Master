package kr.co.pulmuone.v1.comm.mapper.promotion.point;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.promotion.point.dto.*;
import kr.co.pulmuone.v1.promotion.point.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PointUseMapper {

	PointVo getPmPoint(Long pmPointId);

	PmOrganizationVo getPmOrganization(Long pmPointId);

	int addDepositPointsPmPointUsed(PointUsedDto pointUsedDto);

	int addDepositPointsPmPointUsedDetl(PointUsedDetailDto pointUsedDetailDto);

	int getUserAvailablePoints(Long urUserId);

    String getPointExpectExpired(@Param("urUserId") Long urUserId, @Param("startDate") String startDate, @Param("endDate") String endDate) throws Exception;

    Page<CommonGetPointListByUserVo> getPointListByUser(CommonGetPointListByUserRequestDto dto) throws Exception;

    List<CommonGetPointExpectExpireListResultVo> getPointExpectExpireList(@Param("urUserId") Long urUserId, @Param("startDate") String startDate, @Param("endDate") String endDate) throws Exception;

    CommonGetAddPointValidationInfoVo getAddPointValidationInfo(@Param("urUserId") Long urUserId, @Param("pmPointId") Long pmPointId) throws Exception;

    List<GoodsFeedbackPointRewardSettingVo> getGoodsFeedbackPointRewardSettingList(@Param("urGroupId") Long urGroupId);

    PointVo getManagerReflectionPointInfo(@Param("pmPointId") Long pmPointId);

    List<PointIssueVo> getPointIssueList(@Param("pmPointId") Long pmPointId);

    void addPointDepositReservation(PointDepositReservationDto pointDepositReservationDto);

    List<PointDepositReservationVo> getPointDepositReservationList(@Param("depositDate") String depositDate);

    void putReservationGoodsFeedbackPointStatus(@Param("depositReservationId") Long depositReservationId, @Param("pointUseMessage") PointEnums.PointUseMessage pointUseMessage);

    List<PointUsedDetailVo> selectUsablePointListOrderByExpiryDate(@Param("urUserId") Long urUserId);

	void insertPointUsedDetail(PointUsedDetailAddRequestDto pointDetailAddRequestList);

	boolean isValidationOverUsedPoint(@Param("refPointUsedDetlId") long refPointUsedDetlId);

	void closeUsedPoint(@Param("urUserId") Long urUserId, @Param("closePointRefList") List<Long> closePointRefList);

	int insertPointUsed(DepositPointDto depositPointDto);

	List<PointUsedDetailVo> selectPointDetailListUsedOrder(@Param("urUserId") long urUserId, @Param("orderNo") String orderNo);

	List<PointUsedDetailVo> selectExpiredPointList(@Param("expireDate") String expireDate);

    PointVo selectPointInfoBySerialNumber(@Param("pointSerialNumber") String pointSerialNumber);

	List<PointUsedDetailVo> selectWithdrawalMemberPointList(@Param("urUserId") Long urUserId);

	int selectCountSerialNumberIssuedPerAccount(@Param("urUserId") Long urUserId, @Param("pmPointId") Long pmPointId);

	int selectCountFixedSerialNumberIssuedPerAccount(@Param("serialNumber") String serialNumber);

	List<PointUsedDetailVo> selectOrderRefundPointThatCanExpireImmediate(@Param("urUserId") long urUserId, @Param("orderNo") String orderNo);

	void insertPointNotIssue(PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto);

	List<PointUnpaidIssueVo> selectUnpaidRefundList(@Param("urUserId") long urUserId, @Param("orderNo") String orderNo);

	PointUsedDetailVo getNotIssuePointDetail(@Param("pmPointNotIssueId") long pmPointNotIssueId);

	void updatePointNotIssue(PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto);

	List<PointUsedDetailVo> getRefundNotIssuedList();

	List<PointUsedDetailVo> getAddNotIssuedList(PointUsedDetailVo pointUsedDetailVo);
}
