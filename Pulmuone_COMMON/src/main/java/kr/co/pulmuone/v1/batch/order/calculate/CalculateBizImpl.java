package kr.co.pulmuone.v1.batch.order.calculate;

import kr.co.pulmuone.v1.batch.order.calculate.dto.*;
import kr.co.pulmuone.v1.batch.order.calculate.dto.vo.CaSettleEmployeeMasterVo;
import kr.co.pulmuone.v1.batch.order.calculate.dto.vo.CaSettleOuMngVo;
import kr.co.pulmuone.v1.batch.order.etc.dto.vo.UnreleasedDetailConditionVo;
import kr.co.pulmuone.v1.batch.order.etc.dto.vo.UnreleasedDetailInfoConditionVo;
import kr.co.pulmuone.v1.batch.order.etc.dto.vo.UnreleasedHeaderConditionVo;
import kr.co.pulmuone.v1.batch.order.etc.dto.vo.UnreleasedInfoUpdateVo;
import kr.co.pulmuone.v1.calculate.employee.service.EmployeeBiz;
import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.constants.CalculateConstants;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.CalculateEnums;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 정산 배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculateBizImpl implements CalculateBiz {

	private final CalculateService calculateService;

	private final ErpApiExchangeService erpApiExchangeService;

	@Autowired
	private EmployeeBiz employeeBiz;

	/**
	 * 임직원 정산 일마감
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void employeeCalculateDayDeadline() throws BaseException {

		// 전일 임직원정산 마감여부 체크
		int checkCount = calculateService.selectDayBeforeEmployeeCalculateDeadlineCheck();

		log.info("===================전일 임직원정산 마감여부 체크:"+checkCount);

		if(checkCount <= 0) {
			// 전일 임직원정산 일마감
			calculateService.addDayBeforeEmployeeCalculateDayDeadline();
			// 임직원정산 마스터 저장
			addEmployeeCalculateMaster();
		}

	}

	/**
	 * 임직원정산 마스터 저장
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	protected void addEmployeeCalculateMaster() throws BaseException {

		// 정산용 OU 목록 조회
		List<CaSettleOuMngVo> CaSettleOuMngList = calculateService.selectCalculateOuList();
		for(CaSettleOuMngVo vo : CaSettleOuMngList){

			String ouId = vo.getOuId();

			// 임직원정산 마스터 저장여부 체크
			int checkCount = calculateService.selectEmployeeCalculateMasterCheck(ouId);

			log.info("===================임직원정산 마스터 저장여부 체크:"+checkCount);

			if(checkCount > 0) calculateService.putEmployeeCalculateMaster(ouId); // update
			else calculateService.addEmployeeCalculateMaster(ouId); // insert

		}

	}

	/**
	 * 정산을 위한 임직원 정보 저장
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void addEmployeeCalculateInfo() throws BaseException {

		// 당월 임직원 정보 삭제
		calculateService.putEmployeeCalculateInfo();

		// 당월 임직원 정보 저장
		calculateService.addEmployeeCalculateInfo();

	}

	/**
	 * 매출확정 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void salesConfirmSearch() throws BaseException {

		String erpInterfaceId = CalculateEnums.ErpInterfaceId.SALES_CONFIRM_SEARCH_INTERFACE_ID.getCode();

		try {
			BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(getSalesConfirmSearchParam(null), erpInterfaceId);

			log.info("===================TotalPage: "+baseApiResponseVo.getTotalPage()+" | baseApiResponseVo: "+baseApiResponseVo);

			int startPage = baseApiResponseVo.getCurrentPage();	// 최초 조회한 페이지 (1 페이지)
			int totalPage = baseApiResponseVo.getTotalPage(); 	// 해당 검색조건으로 조회시 전체 페이지 수

			for (int pageNo = startPage; pageNo <= totalPage; pageNo++) {

				List<ErpIfSalSrchHeaderResponseDto> erpIfSalSrchList = new ArrayList<>();
				baseApiResponseVo = erpApiExchangeService.get(getSalesConfirmSearchParam("1"), erpInterfaceId);

				if(baseApiResponseVo != null) {
					erpIfSalSrchList.addAll(baseApiResponseVo.deserialize(ErpIfSalSrchHeaderResponseDto.class));

					log.info("===================pageNo: "+pageNo+" | baseApiResponseVo: "+baseApiResponseVo+" | TotalPage: "+baseApiResponseVo.getTotalPage());

					if (erpIfSalSrchList != null && erpIfSalSrchList.size() > 0) {

						// 조회한 매출확정된 데이터 저장 및 API 확정조회완료 전송
						addSalesConfirmList(erpIfSalSrchList);

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(e.getMessage());
		}

	}

	/**
	 * 매출확정 데이터 API 조회 파라미터
	 * @param pageNo
	 * @return Map<String, String> paramMap
	 * @throws Exception
	 */
	protected Map<String, String> getSalesConfirmSearchParam(String pageNo) throws Exception {

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("page", pageNo);
		paramMap.put("srcSvr"	, SourceServerTypes.ESHOP.getCode()); // 입력 시스템 코드 ESHOP
		paramMap.put("itfSetFlg", CalculateConstants.ITF_SALES_FLG); // 인터페이스 수신여부
		paramMap.put("setUpdDat",
					DateUtil.addDays(DateUtil.getCurrentDate(), CalculateConstants.ITF_SALES_DATE_FROM_TERM, "yyyyMMdd")
							+ CalculateConstants.ITF_SALES_DATE_FROM_TIME + "~"
							+ DateUtil.getCurrentDate() + CalculateConstants.ITF_SALES_DATE_TO_TIME); // 조회기간

		return paramMap;
	}

	/**
	 * 조회한 매출확정된 데이터 저장 및 API 확정조회완료 전송
	 * @param erpIfSalSrchList
	 * @return void
	 */
	protected void addSalesConfirmList(List<ErpIfSalSrchHeaderResponseDto> erpIfSalSrchList) {

		// Header Request Dto List
		List<ErpIfSalFlagHeaderRequestDto> headerRequestDtoList = new ArrayList<>();

		log.info("===================erpIfSalSrchList.size():" + erpIfSalSrchList.size());

		// Header Response Dto List Loop
		for(ErpIfSalSrchHeaderResponseDto headerResponseDto : erpIfSalSrchList) {

			// Line Request Dto List
			List<ErpIfSalFlagLineRequestDto> lineRequestDtoList = new ArrayList<>();

			// Line Response Dto List
			List<ErpIfSalSrchLineResponseDto> lineResponseDtoList = headerResponseDto.getLine();

			// Header 유형 주문:1, 반품:4, 매출:5
			String hdrTyp = headerResponseDto.getHdrTyp();

			if (lineResponseDtoList != null && lineResponseDtoList.size() > 0) {

				// Line Response Dto List Loop
				for(ErpIfSalSrchLineResponseDto lineResponseDto : lineResponseDtoList){

					try {

						// 하이톡이 아니면
						if(!SourceServerTypes.HITOK.getCode().equals(lineResponseDto.getCrpCd())) {


							// 정산처리 여부가 Y이면
							if ("Y".equals(lineResponseDto.getSetYn())) {

								// 매출확정된 데이터 저장
								calculateService.addSalesConfirm(lineResponseDto, hdrTyp);

								// Line Request Dto List 셋팅 (저장 성공한 데이터만)
								lineRequestDtoList.add(lineRequestDtoListSetting(lineResponseDto));

							}

						}

					} catch (BaseException be) {
						be.printStackTrace();
						be.getMessage();

					}

				}

				log.info("===================lineRequestDtoList.size():" + lineRequestDtoList.size());
				log.info("===================lineRequestDtoList:" + lineRequestDtoList);

			}

			// Header Request Dto List 셋팅
			if(lineRequestDtoList.size() > 0) headerRequestDtoList.add(headerRequestDtoListSetting(headerResponseDto, lineRequestDtoList));

		}

		log.info("===================headerRequestDtoList.size():" + headerRequestDtoList.size());
		log.info("===================headerRequestDtoList:" + headerRequestDtoList);

		// 매출확정 조회완료 API 전송
		if(headerRequestDtoList.size() > 0) addSalesConfirmApi(headerRequestDtoList);

	}

	/**
	 * Line Request Dto List 셋팅
	 * @param lineResponseDto
	 * @return ErpIfSalFlagLineRequestDto
	 * @throws
	 */
	protected ErpIfSalFlagLineRequestDto lineRequestDtoListSetting(ErpIfSalSrchLineResponseDto lineResponseDto) {

		if(StringUtil.isNotEmpty(lineResponseDto.getSchLinNo())) {
			// Line Condition Request Dto 셋팅
			ErpIfHitokSalFlagLineConditionRequestDto lineConditionRequestDto = ErpIfHitokSalFlagLineConditionRequestDto.builder()
					.hrdSeq(lineResponseDto.getHrdSeq())
					.oriSysSeq(lineResponseDto.getOriSysSeq())
					.ordNum(lineResponseDto.getOrdNum())
					.ordNoDtl(lineResponseDto.getOrdNoDtl())
					.schLinNo(lineResponseDto.getSchLinNo())
					.build();

			// Line Request Dto 셋팅
			return ErpIfSalFlagLineRequestDto.builder()
					.condition(lineConditionRequestDto)
					.build();

		} else {
			// Line Condition Request Dto 셋팅
			ErpIfSalFlagLineConditionRequestDto lineConditionRequestDto = ErpIfSalFlagLineConditionRequestDto.builder()
					.hrdSeq(lineResponseDto.getHrdSeq())
					.oriSysSeq(lineResponseDto.getOriSysSeq())
					.ordNum(lineResponseDto.getOrdNum())
					.ordNoDtl(lineResponseDto.getOrdNoDtl())
					.build();

			// Line Request Dto 셋팅
			return ErpIfSalFlagLineRequestDto.builder()
					.condition(lineConditionRequestDto)
					.build();
		}
	}

	/**
	 * Header Request Dto List 셋팅
	 * @param headerResponseDto
	 * @param lineRequestDtoList
	 * @return ErpIfSalFlagHeaderRequestDto
	 * @throws
	 */
	protected ErpIfSalFlagHeaderRequestDto headerRequestDtoListSetting(ErpIfSalSrchHeaderResponseDto headerResponseDto, List<ErpIfSalFlagLineRequestDto> lineRequestDtoList) {

		// Header Condition Request Dto 셋팅
		ErpIfSalFlagHeaderConditionRequestDto headerConditionRequestDto = ErpIfSalFlagHeaderConditionRequestDto.builder()
						.hrdSeq(headerResponseDto.getHrdSeq())
						.oriSysSeq(headerResponseDto.getOriSysSeq())
						.ordNum(headerResponseDto.getOrdNum())
						.build();

		// Header Request Dto 셋팅
		ErpIfSalFlagHeaderRequestDto headerRequestDto = ErpIfSalFlagHeaderRequestDto.builder()
						.condition(headerConditionRequestDto)
						.line(lineRequestDtoList)
						.build();

		return headerRequestDto;
	}

	/**
	 * 매출확정 조회완료 API 전송
	 * @param headerRequestDtoList
	 * @return void
	 */
	protected void addSalesConfirmApi(List<ErpIfSalFlagHeaderRequestDto> headerRequestDtoList) {

		String erpInterfaceId = CalculateEnums.ErpInterfaceId.SALES_CONFIRM_FLAG_INTERFACE_ID.getCode();

		// API 전송
		BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(headerRequestDtoList, erpInterfaceId);

		log.info("===================baseApiResponseVo:" + baseApiResponseVo);

		// 실패시 재시도 총 3회
		erpApiFailRetry(baseApiResponseVo, headerRequestDtoList, erpInterfaceId);

	}

	/**
	 * ERP API 송신 실패시 재시도 총 3회
	 * @param baseApiResponseVo
	 * @param headerRequestDtoList
	 * @param erpInterfaceId
	 * @return void
	 * @throws
	 */
	protected void erpApiFailRetry(BaseApiResponseVo baseApiResponseVo, List<ErpIfSalFlagHeaderRequestDto> headerRequestDtoList, String erpInterfaceId) {

		// 실패
		if(!baseApiResponseVo.isSuccess()) {

			for (int failCnt = 0; failCnt < BatchConstants.BATCH_FAIL_RETRY_COUNT; failCnt++) {

				BaseApiResponseVo retryBaseApiResponseVo = erpApiExchangeService.put(headerRequestDtoList, erpInterfaceId);

				// 성공
				if (retryBaseApiResponseVo.isSuccess()) {
					log.info("===================baseApiResponseVo: " + baseApiResponseVo);
					break;
				}
				// 3번 재시도 모두 실패
				else {
					log.info("===================baseApiResponseVo: "+ failCnt + " " + baseApiResponseVo);
					if(failCnt == BatchConstants.BATCH_FAIL_RETRY_COUNT-1) {
						// 실패알람
						// TODO SMS, Slack 작업필요
					}
				}

			}

		}

	}

	/**
	 * 하이톡 일배 매출확정 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void hitokDaliySalesConfirmSearch() throws BaseException {

		String erpInterfaceId = CalculateEnums.ErpInterfaceId.SALES_CONFIRM_SEARCH_INTERFACE_ID.getCode();

		try {
			BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(getSalesConfirmSearchParam(null), erpInterfaceId);

			log.info("===================TotalPage: "+baseApiResponseVo.getTotalPage()+" | baseApiResponseVo: "+baseApiResponseVo);

			int startPage = baseApiResponseVo.getCurrentPage();	// 최초 조회한 페이지 (1 페이지)
			int totalPage = baseApiResponseVo.getTotalPage(); 	// 해당 검색조건으로 조회시 전체 페이지 수

			for (int pageNo = startPage; pageNo <= totalPage; pageNo++) {

				List<ErpIfSalSrchHeaderResponseDto> erpIfSalSrchList = new ArrayList<>();
				baseApiResponseVo = erpApiExchangeService.get(getSalesConfirmSearchParam("1"), erpInterfaceId);

				if(baseApiResponseVo != null) {
					erpIfSalSrchList.addAll(baseApiResponseVo.deserialize(ErpIfSalSrchHeaderResponseDto.class));

					log.info("===================pageNo: "+pageNo+" | baseApiResponseVo: "+baseApiResponseVo+" | TotalPage: "+baseApiResponseVo.getTotalPage());

					if (erpIfSalSrchList != null && erpIfSalSrchList.size() > 0) {

						// 조회한 하이톡 매출확정된 데이터 저장 및 API 확정조회완료 전송
						addHitokDaliySalesConfirmList(erpIfSalSrchList);

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(e.getMessage());
		}

	}

	/**
	 * 조회한 하이톡 일배 매출확정된 데이터 저장 및 API 확정조회완료 전송
	 * @param erpIfSalSrchList
	 * @return void
	 */
	protected void addHitokDaliySalesConfirmList(List<ErpIfSalSrchHeaderResponseDto> erpIfSalSrchList) {

		// Header Request Dto List
		List<ErpIfSalFlagHeaderRequestDto> headerRequestDtoList = new ArrayList<>();

		log.info("===================erpIfSalSrchList.size():" + erpIfSalSrchList.size());

		// Header Response Dto List Loop
		for(ErpIfSalSrchHeaderResponseDto headerResponseDto : erpIfSalSrchList) {

			// Line Request Dto List
			List<ErpIfSalFlagLineRequestDto> lineRequestDtoList = new ArrayList<>();

			// Line Response Dto List
			List<ErpIfSalSrchLineResponseDto> lineResponseDtoList = headerResponseDto.getLine();

			// Header 유형 주문:1, 반품:4, 매출:5
			String hdrTyp = headerResponseDto.getHdrTyp();

			if (lineResponseDtoList != null && lineResponseDtoList.size() > 0) {

				// Line Response Dto List Loop
				for(ErpIfSalSrchLineResponseDto lineResponseDto : lineResponseDtoList){

					try {

						// 하이톡이고 스케줄 라인 있으면
						if(SourceServerTypes.HITOK.getCode().equals(lineResponseDto.getCrpCd()) && StringUtil.isNotEmpty(lineResponseDto.getSchLinNo())) {

							// 정산처리 여부가 Y이면
							if ("Y".equals(lineResponseDto.getSetYn())) {

								// 매출확정된 데이터 저장
								calculateService.addHitokSalesConfirm(lineResponseDto, hdrTyp);

								// Line Request Dto List 셋팅 (저장 성공한 데이터만)
								lineRequestDtoList.add(lineRequestDtoListSetting(lineResponseDto));

							}

						}

					} catch (BaseException be) {
						be.printStackTrace();
						be.getMessage();

					}

				}

				log.info("===================lineRequestDtoList.size():" + lineRequestDtoList.size());
				log.info("===================lineRequestDtoList:" + lineRequestDtoList);

			}

			// Header Request Dto List 셋팅
			if(lineRequestDtoList.size() > 0) headerRequestDtoList.add(headerRequestDtoListSetting(headerResponseDto, lineRequestDtoList));

		}

		log.info("===================headerRequestDtoList.size():" + headerRequestDtoList.size());
		log.info("===================headerRequestDtoList:" + headerRequestDtoList);

		// 매출확정 조회완료 API 전송
		if(headerRequestDtoList.size() > 0) addSalesConfirmApi(headerRequestDtoList);

	}

	/**
	 * 하이톡 택배 매출확정 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void hitokSalesConfirmSearch() throws BaseException {

		String erpInterfaceId = CalculateEnums.ErpInterfaceId.SALES_CONFIRM_SEARCH_INTERFACE_ID.getCode();

		try {
			BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(getSalesConfirmSearchParam(null), erpInterfaceId);

			log.info("===================TotalPage: "+baseApiResponseVo.getTotalPage()+" | baseApiResponseVo: "+baseApiResponseVo);

			int startPage = baseApiResponseVo.getCurrentPage();	// 최초 조회한 페이지 (1 페이지)
			int totalPage = baseApiResponseVo.getTotalPage(); 	// 해당 검색조건으로 조회시 전체 페이지 수

			for (int pageNo = startPage; pageNo <= totalPage; pageNo++) {

				List<ErpIfSalSrchHeaderResponseDto> erpIfSalSrchList = new ArrayList<>();
				baseApiResponseVo = erpApiExchangeService.get(getSalesConfirmSearchParam("1"), erpInterfaceId);

				if(baseApiResponseVo != null) {
					erpIfSalSrchList.addAll(baseApiResponseVo.deserialize(ErpIfSalSrchHeaderResponseDto.class));

					log.info("===================pageNo: "+pageNo+" | baseApiResponseVo: "+baseApiResponseVo+" | TotalPage: "+baseApiResponseVo.getTotalPage());

					if (erpIfSalSrchList != null && erpIfSalSrchList.size() > 0) {

						// 조회한 하이톡 매출확정된 데이터 저장 및 API 확정조회완료 전송
						addHitokSalesConfirmList(erpIfSalSrchList);

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(e.getMessage());
		}

	}

	/**
	 * 조회한 하이톡 택배 매출확정된 데이터 저장 및 API 확정조회완료 전송
	 * @param erpIfSalSrchList
	 * @return void
	 */
	protected void addHitokSalesConfirmList(List<ErpIfSalSrchHeaderResponseDto> erpIfSalSrchList) {

		// Header Request Dto List
		List<ErpIfSalFlagHeaderRequestDto> headerRequestDtoList = new ArrayList<>();

		log.info("===================erpIfSalSrchList.size():" + erpIfSalSrchList.size());

		// Header Response Dto List Loop
		for(ErpIfSalSrchHeaderResponseDto headerResponseDto : erpIfSalSrchList) {

			// Line Request Dto List
			List<ErpIfSalFlagLineRequestDto> lineRequestDtoList = new ArrayList<>();

			// Line Response Dto List
			List<ErpIfSalSrchLineResponseDto> lineResponseDtoList = headerResponseDto.getLine();

			// Header 유형 주문:1, 반품:4, 매출:5
			String hdrTyp = headerResponseDto.getHdrTyp();

			if (lineResponseDtoList != null && lineResponseDtoList.size() > 0) {

				// Line Response Dto List Loop
				for(ErpIfSalSrchLineResponseDto lineResponseDto : lineResponseDtoList){

					try {

						// 하이톡이고 스케줄 라인 없으면
						if(SourceServerTypes.HITOK.getCode().equals(lineResponseDto.getCrpCd()) && StringUtil.isEmpty(lineResponseDto.getSchLinNo())) {

							// 정산처리 여부가 Y이면
							if ("Y".equals(lineResponseDto.getSetYn())) {

								// 매출확정된 데이터 저장
								calculateService.addHitokSalesConfirm(lineResponseDto, hdrTyp);

								// Line Request Dto List 셋팅 (저장 성공한 데이터만)
								lineRequestDtoList.add(lineRequestDtoListSetting(lineResponseDto));

							}

						}

					} catch (BaseException be) {
						be.printStackTrace();
						be.getMessage();

					}

				}

				log.info("===================lineRequestDtoList.size():" + lineRequestDtoList.size());
				log.info("===================lineRequestDtoList:" + lineRequestDtoList);

			}

			// Header Request Dto List 셋팅
			if(lineRequestDtoList.size() > 0) headerRequestDtoList.add(headerRequestDtoListSetting(headerResponseDto, lineRequestDtoList));

		}

		log.info("===================headerRequestDtoList.size():" + headerRequestDtoList.size());
		log.info("===================headerRequestDtoList:" + headerRequestDtoList);

		// 매출확정 조회완료 API 전송
		if(headerRequestDtoList.size() > 0) addSalesConfirmApi(headerRequestDtoList);

	}

	/**
	 * 임직원 정산 월마감 erp 전송
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
	public void employeeCalculateMonthDeadlineErpSend() throws Exception {

		// 임직원 정산 월마감 대상 조회
		List<CaSettleEmployeeMasterVo> caSettleEmployeeMasterList = calculateService.selectEmployeeCalculateMonthDeadlineList();
		List<String> settleMonthList = caSettleEmployeeMasterList.stream().map(CaSettleEmployeeMasterVo::getSettleMonth).collect(Collectors.toList());
		List<String> ouIdList = caSettleEmployeeMasterList.stream().map(CaSettleEmployeeMasterVo::getOuId).collect(Collectors.toList());
		List<Long> sessionIdList = caSettleEmployeeMasterList.stream().map(CaSettleEmployeeMasterVo::getSessionId).collect(Collectors.toList());

		employeeBiz.putCalculateConfirmProc(settleMonthList, ouIdList, sessionIdList, StringUtil.nvl(Constants.BATCH_CREATE_USER_ID));

	}
}