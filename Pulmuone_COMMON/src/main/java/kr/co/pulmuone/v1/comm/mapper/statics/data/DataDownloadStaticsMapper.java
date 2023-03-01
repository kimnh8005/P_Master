package kr.co.pulmuone.v1.comm.mapper.statics.data;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.statics.data.dto.DataDownloadStaticsRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.ResultHandler;

import java.util.LinkedHashMap;
import java.util.List;

@Mapper
public interface DataDownloadStaticsMapper {

    // 적립금 회원 잔액 조회
    void getOnlineMemberReserveBalance(DataDownloadStaticsRequestDto dto, ResultHandler<LinkedHashMap<String, Object>> resultHandler) throws BaseException;
    // 적립금 임직원 잔액 조회
    void getEmployeeMemberReserveBalance(DataDownloadStaticsRequestDto dto, ResultHandler<LinkedHashMap<String, Object>> resultHandler) throws BaseException;
    // 적립금 정산 조회
    void getSettlementPoint(DataDownloadStaticsRequestDto dto, ResultHandler<LinkedHashMap<String, Object>> resultHandler) throws BaseException;
    // 쿠폰 정산 조회
    void getSettlementCoupon(DataDownloadStaticsRequestDto dto, ResultHandler<LinkedHashMap<String, Object>> resultHandler) throws BaseException;
    // 내부회계통제용 쿠폰 지급 조회
    void getInternalAccountingCouponPayment(DataDownloadStaticsRequestDto dto, ResultHandler<LinkedHashMap<String, Object>> resultHandler) throws BaseException;
    // 쿠폰비용 사용 조회
    void getUseCouponCost(DataDownloadStaticsRequestDto dto, ResultHandler<LinkedHashMap<String, Object>> resultHandler) throws BaseException;
    // 적립금비용 사용 조회
    void getUseReserveCost(DataDownloadStaticsRequestDto dto, ResultHandler<LinkedHashMap<String, Object>> resultHandler) throws BaseException;
    // 임직원 할인지원액 조회
    void getEmployeeDiscountSupport(DataDownloadStaticsRequestDto dto, ResultHandler<LinkedHashMap<String, Object>> resultHandler) throws BaseException;
    // 용인물류 품목별 폐기 기준 조회
    void getDisposalDateByDistributionPeriod(DataDownloadStaticsRequestDto dto, ResultHandler<LinkedHashMap<String, Object>> resultHandler) throws BaseException;
    // 객단가 조회
    void getCustomerPriceCost(DataDownloadStaticsRequestDto dto, ResultHandler<LinkedHashMap<String, Object>> resultHandler) throws BaseException;

}
