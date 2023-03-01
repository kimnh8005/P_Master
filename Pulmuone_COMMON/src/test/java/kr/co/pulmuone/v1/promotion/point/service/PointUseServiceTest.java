package kr.co.pulmuone.v1.promotion.point.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.FeedbackEnums;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.mapper.promotion.point.PointUseMapper;
import kr.co.pulmuone.v1.promotion.point.dto.DepositPointDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointDepositReservationDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointPartialDepositOverLimitDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointRefundRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PmOrganizationVo;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointUsedDetailVo;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class PointUseServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    PointUseService pointUseService;

    @InjectMocks
    PointUseService mockPointUseService;

    @Mock
    PointUseMapper mockPointUseMapper;

    private DepositPointDto depositPointDto;

    @BeforeEach
    void setUp() {
        mockPointUseService = new PointUseService(mockPointUseMapper);

        depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(10000L)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_FEEDBACK_REWARD)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .pmPointId(11L)
                .build();
    }

    @Test
    void 적립금_적립_처리시_회원번호_존재하지않은_실패코드() throws Exception{
        depositPointDto = DepositPointDto.builder()
                .urUserId(null)
                .build();

        ApiResult<?> apiResult = pointUseService.depositPoints(depositPointDto);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.NEED_LOGIN.getCode());
    }

    @Test
    void 적립금_적립_처리시_회원번호_존재(){
        depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .build();

        assertTrue(depositPointDto.getUrUserId()>0);
    }

    @Test
    void 적립금_적립_처리시_적립금_필수값_없는경우(){
        depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(null)
                .build();

        assertTrue(depositPointDto.getAmount()<1);
    }

    @Test
    void 적립금_적립_처리시_적립금_0보다_작은경우(){
        depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(-1L)
                .build();

        assertTrue(depositPointDto.getAmount() <= 0);
    }

    @Test
    void 적립금_적립_처리시_적립금_필수값_존재(){
        depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(1000L)
                .build();

        assertTrue(depositPointDto.getAmount()>0);
    }

    @Test
    void 적립금_적립_처리시_적립금_0보다_작은경우_실패코드() throws Exception{
        depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(-1L)
                .build();

        ApiResult<?> apiResult = pointUseService.depositPoints(depositPointDto);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.MANDATORY_MISSING.getCode());
    }

    @Test
    void 적립금_적립_처리시_적립지불유형_존재하지않는경우(){
        depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(10000L)
                .pointPaymentType(null)
                .build();

        assertNull(depositPointDto.getPointPaymentType());
    }

    @Test
    void 적립금_적립_처리시_적립지불유형_존재하지않는경우_실패코드() throws Exception{
        depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(10000L)
                .pointPaymentType(null)
                .build();

        ApiResult<?> apiResult = pointUseService.depositPoints(depositPointDto);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.MANDATORY_MISSING.getCode());
    }

    @Test
    void 적립금_적립_처리시_적립지불유형_적립_확인(){
        depositPointDto = DepositPointDto.builder()
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .build();

        assertEquals(depositPointDto.getPointPaymentType(), PointEnums.PointPayment.PROVISION);
    }



    @Test
    void 적립금_적립_처리시_적립상세유형_존재하지않는경우(){
        depositPointDto = DepositPointDto.builder()
                .pointProcessType(null)
                .build();

        assertNull(depositPointDto.getPointProcessType());
    }

    @Test
    void 적립금_적립_처리시_적립상세유형_존재하지않는경우_실패코드() throws Exception{
        depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(10000L)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointProcessType(null)
                .build();

        ApiResult<?> apiResult = pointUseService.depositPoints(depositPointDto);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.MANDATORY_MISSING.getCode());
    }

    @Test
    void 적립금_적립_처리시_적립상세유형_프로모션_리워드_적립(){
        depositPointDto = DepositPointDto.builder()
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_FEEDBACK_REWARD)
                .build();

        assertEquals(depositPointDto.getPointProcessType(), PointEnums.PointProcessType.DEPOSIT_POINT_FEEDBACK_REWARD);
    }

    @Test
    void 적립금_적립_처리시_적립정산유형_존재하지않는경우(){
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .pointSettlementType(null)
                .build();

        assertNull(depositPointDto.getPointSettlementType());
    }

    @Test
    void 적립금_적립_처리시_적립정산유형_존재하지않는경우_실패코드() throws Exception{
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(10000L)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_FEEDBACK_REWARD)
                .pointSettlementType(null)
                .build();

        ApiResult<?> apiResult = pointUseService.depositPoints(depositPointDto);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.MANDATORY_MISSING.getCode());
    }

    @Test
    void 적립금_적립_처리시_적립정산유형_무상지급(){
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .build();

        assertEquals(depositPointDto.getPointSettlementType(), PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE);
    }

    @Test
    void 적립금_적립_처리시_적립설정고유번호_존재하지않는경우_실패코드() throws Exception{
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(10000L)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_FEEDBACK_REWARD)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .pmPointId(null)
                .build();

        ApiResult<?> apiResult = pointUseService.depositPoints(depositPointDto);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.MANDATORY_MISSING.getCode());
    }

    @Test
    void 보유가능한_최대_적립금_확인(){
        int amount = 500000;

        assertEquals(amount, Constants.DEPOSIT_MAXIMUM_POSSIBLE_POINT);
    }

    @Test
    void 보유가능한_최대_적립금_이상인경우() throws Exception{
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(550000L)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_FEEDBACK_REWARD)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .pmPointId(2L)
                .build();


        ApiResult<?> apiResult = pointUseService.depositPoints(depositPointDto);

        assertEquals(apiResult.getCode(), PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED.getCode());
    }

    @Test
    void 적립금_설정_정보_조회_실패(){
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .pmPointId(99999L)
                .build();

        //적립금 설정 조회
        PointVo pointInfo = mockPointUseMapper.getPmPoint(depositPointDto.getPmPointId());

        assertNull(pointInfo);
    }

    @Test
    void 적립금_설정_정보_조회_실패_코드() throws Exception{
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(1000L)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_FEEDBACK_REWARD)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .pmPointId(99999L)
                .build();

        ApiResult<?> apiResult = pointUseService.depositPoints(depositPointDto);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.MANDATORY_MISSING.getCode());
    }

    @Test
    void 적립금_설정_분담률_조직정보_조회_실패(){
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .pmPointId(99999L)
                .build();

        //분담 조직
        PmOrganizationVo pmOrganization = mockPointUseMapper.getPmOrganization(depositPointDto.getPmPointId());

        assertNull(pmOrganization);
    }

    @Test
    void 적립금_적립_처리시_내역_등록() {
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(111L)
                .amount(1000L)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_FEEDBACK_REWARD)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .pmPointId(2L)
                .build();

        PointVo pointVo = new PointVo();
        pointVo.setPmPointId(45L);
        pointVo.setValidityTp(PointEnums.ValidityType.PERIOD);
        pointVo.setValidityEndDt("2020-12-10");
        pointVo.setIssueDeptCd(Constants.PULMUONE_SETTLEMENT_CORPORATION_CODE);

        mockPointUseService.addPointUsedInfo(depositPointDto, pointVo);
    }

    @Test
    void 적립가능최대금액_초과로_최대적립가능금액_확인(){
        Long amount = 497000L; //요청 적립금
        Long urUserId = 1647296L;

        given(mockPointUseMapper.getUserAvailablePoints(any())).willReturn(4000);

        // when
        Long maxinumAccrualAmount = mockPointUseService.getMaximumAccrualAmount(amount, urUserId);

        // then
        assertEquals(496000, maxinumAccrualAmount);
    }

    @Test
    void 요청적립금이_모두_적립가능할때(){
        Long amount = 5000L; //요청 적립금
        Long urUserId = 1647296L;

        given(mockPointUseMapper.getUserAvailablePoints(any())).willReturn(4000);

        // when
        Long maxinumAccrualAmount = mockPointUseService.getMaximumAccrualAmount(amount, urUserId);

        // then
        assertEquals(5000, maxinumAccrualAmount);
    }

    @Test
    void 적립가능최대금액_초과로_적립가능금액_없음(){
        Long amount = 497000L;
        Long urUserId = 1647296L;

        given(mockPointUseMapper.getUserAvailablePoints(any())).willReturn(500000);

        // when
        Long maxinumAccrualAmount = mockPointUseService.getMaximumAccrualAmount(amount, urUserId);

        // then
        assertEquals(0, maxinumAccrualAmount);
    }
    @Test
    void 가용_적립금_조회시_회원번호_존재하지않은_실패코드(){
        ApiResult<?> apiResult = pointUseService.getUserAvailablePoints(null);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.NEED_LOGIN.getCode());
    }

    @Test
    void 가용_적립금_조회시_회원번호_존재(){
        ApiResult<?> apiResult = pointUseService.getUserAvailablePoints(111L);

        assertEquals(apiResult.getCode(), BaseEnums.Default.SUCCESS.getCode());
    }

    @Test
    void 이전_올가_회원_적립금_적립시_최대보유가능적립금_초과시() throws Exception {
        //적립
        Long urUserId = 888888L;
        Long amount = 550000L;
        String previusOrgaUserId = "goldenc30";

        given(mockPointUseMapper.getUserAvailablePoints(any())).willReturn(500000);

        // when
        ApiResult<?> apiResult = mockPointUseService.depositOrgaTransferPoints(urUserId, amount, previusOrgaUserId);

        // then
        assertEquals(PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED.getCode(), apiResult.getCode());
    }

    @Test
    void 이전_올가_회원_적립금_적립() throws Exception {
        //적립
        Long urUserId = 888888L;
        Long amount = 20000L;
        String previusOrgaUserId = "goldenc30";

        ApiResult<?> apiResult = mockPointUseService.depositOrgaTransferPoints(urUserId, amount, previusOrgaUserId);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());

    }

    @Test
    void 이전_풀무원_회원_적립금_적립시_최대보유가능적립금_초과시() throws Exception {
        //적립
        Long urUserId = 888888L;
        Long amount = 1000L;
        String previusPmoUserId = "goldenc30";

        given(mockPointUseMapper.getUserAvailablePoints(any())).willReturn(500000);

        // when
        ApiResult<?> apiResult = mockPointUseService.depositPreviousPulmuoneMemberPoints(urUserId, amount, previusPmoUserId);

        // then
        assertEquals(PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED.getCode(), apiResult.getCode());

    }

    @Test
    void 이전_풀무원_회원_적립금_적립() throws Exception {
        //적립
        Long urUserId = 888888L;
        Long amount = 20000L;
        String previusPmoUserId = "goldenc30";

        ApiResult<?> apiResult = mockPointUseService.depositPreviousPulmuoneMemberPoints(urUserId, amount, previusPmoUserId);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());

    }

    @Test
    void 후기_적립금_즉시_적립() throws Exception {
        Long urUserId = 888888L;
        Long urGroupId = 1L;
        FeedbackEnums.FeedbackType feedbackType = FeedbackEnums.FeedbackType.NORMAL;


        ApiResult<?> apiResult = pointUseService.goodsFeedbackPointReward(urUserId, urGroupId, feedbackType);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());
    }

    @Test
    void 후기_적립금_지급기준일로_적립() {
        //적립금 예약 적립 등록
        PointDepositReservationDto pointDepositReservationDto = PointDepositReservationDto.builder()
                .pmPointId(514L)
                .pmPointUserGradeId(535L)
                .urUserId(888888L)
                .amount(3000L)
                .depositDt("20201117")
                .expirationDt("20201212")
                .build();

        pointUseService.addPointDepositReservation(pointDepositReservationDto);

        String depositDate = "20201117";

        ApiResult<Boolean> apiResult = pointUseService.depositReservationGoodsFeedbackPoint(depositDate);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());
    }

    @Test
    void 관리자_지급_적립금_적용() throws Exception{
        Long pmPointId = 937L;

        ApiResult<Boolean> apiResult = pointUseService.applyAdminPoint(pmPointId);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());
    }

    @Test
    void 관리자_차감_적립금_적용() throws Exception{
        Long pmPointId = 594L;

        ApiResult<Boolean> apiResult = pointUseService.applyAdminPoint(pmPointId);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());
    }

    @Test
    void 이벤트_적립금_적립() throws Exception{

        Long urUserId = 8888888L;
        Long pmPointId = 524L;
        Long evEventId = 11L;
        String refNo2 = "3";

        ApiResult<?> apiResult = pointUseService.depositEventPoint(urUserId, pmPointId, evEventId, refNo2);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());
    }

    @Test
    void 추천인_적립금_적립() throws Exception {
        long urUserId = 888888L; //회원 ID
        long pmPointId = 20000L;
        long recommenderId = 999999L; //추천인 ID
        String refNo2 = ""; //참조값
        long amount = 15000L; //적립금
        int validityDay = 15; //유효일
        String deptCd = "102"; //온라인사업부 코드

        //적립금 설정
        PointVo pointInfo = new PointVo();
        pointInfo.setPmPointId(pmPointId);
        pointInfo.setPointProcessTp(PointEnums.PointProcessType.DEPOSIT_POINT_AUTO_RECOMMENDER);
        pointInfo.setPointSettlementTp(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE);
        pointInfo.setIssueVal(amount);
        pointInfo.setValidityDay(validityDay);
        pointInfo.setIssueDeptCd(deptCd);

        given(mockPointUseMapper.getPmPoint(any())).willReturn(pointInfo);

        ApiResult<?> apiResult = mockPointUseService.depositRecommendationPoint(urUserId, pmPointId, recommenderId, refNo2);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());

    }

    @Test
    void test_적립금_적립() throws Exception {
        DepositPointDto dto = DepositPointDto.builder()
                .amount(2000L)
                .urUserId(1646827L)
                .pmPointId(42L)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_MANUAL_EVENT)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .build();

        ApiResult result = pointUseService.depositPoints(dto);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), result.getCode());
    }

    @Test
    void test_적립금차감_유효성_체크__마이너스금액() {
        DepositPointDto dto = DepositPointDto.builder()
                .amount(100000L)
                .urUserId(1646827L)
                .pointProcessType(PointEnums.PointProcessType.WITHDRAW_POINT_PAYMENT)
                .pointPaymentType(PointEnums.PointPayment.DEDUCTION)
                .build();
        ApiResult result = pointUseService.validateMinusPointRequest(dto);
        assertEquals(PointEnums.PointUseMessage.INVALID_POINT_AMOUNT.getCode(), result.getCode());
    }

    @Test
    void test_적립금차감_유효성_체크__잔여적립금부족() {
        DepositPointDto dto = DepositPointDto.builder()
                .amount(-200000000L)
                .urUserId(1646827L)
                .pointProcessType(PointEnums.PointProcessType.WITHDRAW_POINT_PAYMENT)
                .pointPaymentType(PointEnums.PointPayment.DEDUCTION)
                .build();
        ApiResult result = pointUseService.validateMinusPointRequest(dto);
        assertEquals(PointEnums.PointUseMessage.USER_POINT_LACK.getCode(), result.getCode());
    }

    @Test
    void test_적립금차감() throws Exception {

        //적립
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .amount(3000L)
                .urUserId(1646827L)
                .pmPointId(42L)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .build();
        pointUseService.depositPoints(depositPointDto);

        //차감
        DepositPointDto dto = DepositPointDto.builder()
                .amount(-3000L)
                .urUserId(1646827L)
                .pointProcessType(PointEnums.PointProcessType.WITHDRAW_POINT_PAYMENT)
                .pointPaymentType(PointEnums.PointPayment.DEDUCTION)
                .build();

        ApiResult result = pointUseService.minusPoint(dto);
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), result.getCode());
    }


    @Test
    void test_적립금차감__주문시사용() throws Exception {

        //적립
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .amount(3000L)
                .urUserId(1646827L)
                .pmPointId(42L)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .build();
        pointUseService.depositPoints(depositPointDto);

        //주문사용
        DepositPointDto dto = DepositPointDto.builder()
                .amount(-3000L)
                .urUserId(1646827L)
                .pointPaymentType(PointEnums.PointPayment.DEDUCTION)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_CONFIRM_ORDER)
                .refNo1("2020110212345678")
                .build();

        ApiResult result = pointUseService.minusPoint(dto);
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), result.getCode());
    }

    @Test
    void test_적립금_환불_재적립__환불가능금액초과() throws Exception {

        PointRefundRequestDto dto = PointRefundRequestDto.builder()
                .amount(3000L)
                .urUserId(1646827L)
                .orderNo("2020110212345678")
                .reasonAttributableType(ClaimEnums.ReasonAttributableType.COMPANY)
                .build()
                ;
        ApiResult result = pointUseService.refundPoint(dto);
        assertEquals(PointEnums.PointUseMessage.EXCEEDS_REFUNDABLE_POINT.getCode(), result.getCode());
    }

    @Test
    void test_적립금_환불_재적립() throws Exception {
        PointRefundRequestDto refundDto = PointRefundRequestDto.builder()
                .amount(1500L)
                .urUserId(1646827L)
                .orderNo("2020110212347777")
                .reasonAttributableType(ClaimEnums.ReasonAttributableType.COMPANY)
                .build()
                ;

        ApiResult result = pointUseService.refundPoint(refundDto);
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), result.getCode());
    }

    @Test
    void 만료된_적립금_소멸_처리_호출() throws Exception {
        //적립
        DepositPointDto depositPointDto = DepositPointDto.builder()
                .urUserId(8888888L)
                .amount(10000L)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_FEEDBACK_REWARD)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .pmPointId(364L)
                .build();

        PointVo pointVo = new PointVo();
        pointVo.setPmPointId(364L);
        pointVo.setValidityTp(PointEnums.ValidityType.PERIOD);
        pointVo.setValidityEndDt("2011-11-10");
        pointVo.setIssueDeptCd("102");

        pointUseService.addPointUsedInfo(depositPointDto, pointVo);

        //소멸
        String expireDate = "2011-11-11";

        ApiResult<?> apiResult = pointUseService.expirePoint(expireDate);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());


    }

    @Test
    void test_시리얼번호_적립금_적립__유효하지않은시리얼번호() {
        long urUserId = 1646827L;
        String serialNumber = "INVALID_12345";

        ApiResult result = pointUseService.depositPointsBySerialNumber(urUserId, serialNumber);

        assertEquals(false, result.getData());
        assertEquals(PointEnums.PointUseMessage.INVALID_POINT_SERIAL_NUMBER, result.getMessageEnum());
    }


    @Test
    void test_시리얼번호_적립금_적립() {
        long urUserId = 1646827L;
        String serialNumber = "5GIYV76XT1FP";

        ApiResult result = pointUseService.depositPointsBySerialNumber(urUserId, serialNumber);

        assertEquals(true, result.getData());
    }


    @Test
    void test_시리얼번호_적립금_적립__발급제한초과() {
        long urUserId = 1646827L;
        String serialNumber = "BQGTE8PJALQL";
        ApiResult deposit1 = pointUseService.depositPointsBySerialNumber(urUserId, serialNumber);

        serialNumber = "JM6RX4HR4Y7W";
        ApiResult deposit2 = pointUseService.depositPointsBySerialNumber(urUserId, serialNumber);

        serialNumber = "W4MG50DKG7DY";
        ApiResult result = pointUseService.depositPointsBySerialNumber(urUserId, serialNumber);

        assertEquals(false, result.getData());
        assertEquals(PointEnums.PointUseMessage.EXCEEDS_ISSUE_LIMIT_PER_ACCOUNT, result.getMessageEnum());
    }


    @Test
    void test_탈퇴회원_적립금_만료처리() throws Exception {
        long urUserId = 1646827L;
        ApiResult result = pointUseService.expireWithdrawalMemberPoint(urUserId);

        assertEquals(true, result.getData());
    }

    @Test
    void 환불_적립금_만료로_즉시_소멸_필수값_회원고유값_누락(){
        PointRefundRequestDto dto = PointRefundRequestDto.builder()
                .amount(3000L)
                .orderNo("2020110212345678")
                .reasonAttributableType(ClaimEnums.ReasonAttributableType.COMPANY)
                .build()
                ;

        ApiResult<?> apiResult = pointUseService.expireImmediateRefundPointValidation(dto);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.NEED_LOGIN.getCode());
    }

    @Test
    void 환불_적립금_만료로_즉시_소멸_필수값_회원고유값_1보다작을때(){
        PointRefundRequestDto dto = PointRefundRequestDto.builder()
                .amount(3000L)
                .orderNo("2020110212345678")
                .urUserId(-1L)
                .reasonAttributableType(ClaimEnums.ReasonAttributableType.COMPANY)
                .build()
                ;

        ApiResult<?> apiResult = pointUseService.expireImmediateRefundPointValidation(dto);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.NEED_LOGIN.getCode());
    }

    @Test
    void 환불_적립금_만료로_즉시_소멸_필수값_귀책사유_누락(){
        PointRefundRequestDto dto = PointRefundRequestDto.builder()
                .amount(3000L)
                .orderNo("2020110212345678")
                .urUserId(111233L)
                .build()
                ;

        ApiResult<?> apiResult = pointUseService.expireImmediateRefundPointValidation(dto);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.MANDATORY_MISSING.getCode());
    }

    @Test
    void 환불_적립금_만료로_즉시_소멸_필수값_주문번호_누락(){
        PointRefundRequestDto dto = PointRefundRequestDto.builder()
                .amount(3000L)
                .urUserId(111233L)
                .reasonAttributableType(ClaimEnums.ReasonAttributableType.COMPANY)
                .build()
                ;

        ApiResult<?> apiResult = pointUseService.expireImmediateRefundPointValidation(dto);

        assertEquals(apiResult.getCode(), BaseEnums.CommBase.MANDATORY_MISSING.getCode());
    }

    @Test
    void 환불_적립금_만료로_즉시_소멸_필수값_귀책사유_구매자귀책_아닐때(){
        PointRefundRequestDto dto = PointRefundRequestDto.builder()
                .amount(3000L)
                .urUserId(111233L)
                .orderNo("2020110212345678")
                .reasonAttributableType(ClaimEnums.ReasonAttributableType.COMPANY)
                .build()
                ;

        ApiResult<?> apiResult = pointUseService.expireImmediateRefundPoint(dto);

        assertEquals(apiResult.getCode(), PointEnums.PointUseMessage.INVALID_EXPIRE_IMMEDIATE_REFUND_REASON.getCode());
    }

    @Test
    void 환불_적립금_만료로_즉시_소멸_처리(){
        PointRefundRequestDto dto = PointRefundRequestDto.builder()
                .amount(1000L)
                .urUserId(789456L)
                .orderNo("11234567890")
                .reasonAttributableType(ClaimEnums.ReasonAttributableType.BUYER)
                .build()
                ;

        ApiResult<?> apiResult = pointUseService.expireImmediateRefundPoint(dto);

        assertEquals(apiResult.getCode(), BaseEnums.Default.SUCCESS.getCode());
    }

    @Test
    void 한도_초과_부분_적립_미적립_내역_생성(){
        PointPartialDepositOverLimitDto pointPartialDepositOverLimitDto = PointPartialDepositOverLimitDto.builder()
                .urUserId(789456L)
                .maximumAccrualAmount(1000L)
                .requestAmount(10000L)
                .pointProcessType(PointEnums.PointProcessType.DEPOSIT_POINT_ORGA_TRANSFER)
                .pointSettlementType(PointEnums.PointSettlementType.SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE)
                .expirationDate("2021-05-11")
                .issueDeptCd(Constants.ORGA_SETTLEMENT_CORPORATION_CODE)
                .pmPointId(111L)
                .refNo1("1")
                .refNo2("2")
                .refDproPointUsedDetlId(null)
                .availableDate(null)
                .build();

        pointUseService.addPartialDepositOverLimitHistory(pointPartialDepositOverLimitDto);
    }


    @Test
    void 단건_미지급건_재적립_처리() throws Exception {
    	Long pmPointNotIssueId = 30L;
    	Long urUserId = 1647397L;
    	ApiResult<?> apiResult = pointUseService.depositNotIssuePoints(pmPointNotIssueId, urUserId);

    	assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());
    }


    @Test
    void 단건_미지급_적립금_업데이트() {
    	Long urUserId = 1647397L;
    	Long maximumAccrualAmount = 2000000L;
    	Long requestAmount = 5000L;
    	Long pmPointNotIssueId = 30L;
    	PointPartialDepositOverLimitDto dto = PointPartialDepositOverLimitDto.builder()
                .urUserId(urUserId)
                .maximumAccrualAmount(maximumAccrualAmount)
                .requestAmount(requestAmount)
                .pmPointNotIssueId(pmPointNotIssueId)
                .pointProcessType(PointEnums.PointProcessType.getRefundPointProcessType(depositPointDto.getPointProcessType()))
                .pointSettlementType(PointEnums.PointSettlementType.getRefundPointSettlementType(depositPointDto.getPointSettlementType()))
                .modifyId(urUserId)
                .expirationDate("2021-05-18")
                .build();

    	pointUseService.putDepositNotIssuePoints(dto);
    }


    @Test
    void 미적립된_환불_적립금_재적립_만료일() throws ParseException {
    	String expirationDate = "2021-05-10";
    	String depositDate = "2021-05-18";
    	String date = pointUseService.resetRefundNotIssuePointExpirationDate(expirationDate, depositDate);
    	assertNotEquals(date, "2021-05-20");
    }



    @Test
    void 미적립된_환불_적립금_재적립_처리() throws Exception {

        ApiResult result = pointUseService.depositRefundOrderNotIssuePoints();
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), result.getCode());
    }


    @Test
    void 미적립된_환불_적립금_재적립_사용상세_미지급_적립금_정보_수정() throws ParseException {

    	List <PointUsedDetailVo> addNotIssuedList = new ArrayList<>();
    	PointUsedDetailVo vo = new PointUsedDetailVo();
    	vo.setRedepositPointVal(5000L);
    	vo.setPartPointVal(0L);
    	vo.setIssueVal(5000L);
    	vo.setUrUserId(1647397L);
    	vo.setDeptCd("23015");
    	vo.setExpirationDt("2021-05-10");
    	vo.setDepositDt("2021-05-18");
    	addNotIssuedList.add(vo);
    	PointRequestDto pointRequestDto = new PointRequestDto();

    	Long masterPointUsedId = 101010L;
    	Long unpaidRefundAmount = 1000L;
    	String orderNo = "1111";
    	String claimId = "2222";
        pointRequestDto.setPointUsedDetailVoList(addNotIssuedList);
    	ApiResult result = pointUseService.putPointUsedDetail(pointRequestDto, masterPointUsedId, unpaidRefundAmount, orderNo, claimId);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), result.getCode());
    }

    @Test
    void CS환불_적립금_적립처리() throws Exception {
        Long urUserId = 77777L;
        String orderNo = "21022223450";
        Long amount = 4500L;
        Boolean isCsRoleManager = false;
        String finOrganizationCode = "12434";
        Integer csPointValidityDay = 30;

        ApiResult<?> apiResult = mockPointUseService.depositCsRefundOrderPoint(urUserId, orderNo, amount, isCsRoleManager, finOrganizationCode, csPointValidityDay);

        assertEquals(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());
    }

    @Test
    void CS역할그룹아닐때_정산코드조회() throws Exception {
        Boolean isCsRoleManager = false;
        String finOrganizationCode = "12434";

        String issueDeptCd = mockPointUseService.getCsRefundOrderSettlementCorporationCode(isCsRoleManager, finOrganizationCode);

        assertEquals(Constants.ONLINE_MARKETING_SETTLEMENT_CORPORATION_CODE, issueDeptCd);
    }
}
