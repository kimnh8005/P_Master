package kr.co.pulmuone.v1.batch.collectionmall.ezadmin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.*;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo.*;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.validate.EZAdminValidator;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.enums.EZAdminEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import kr.co.pulmuone.v1.comm.excel.util.OrderExcelGoodsIdListData;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings("unchecked")
@Service
public class CollectionMallEZAdminBatchBizImpl implements CollectionMallEZAdminBatchBiz {

    @Autowired
    private CollectionMallEZAdminBatchService collectionMallEZAdminBatchService;

    @Autowired
    private OrderExcelGoodsIdListData orderExcelGoodsIdListData;

    @Autowired
	private CollectionMallEZAdminBatchBiz collectionMallEZAdminBatchBiz;

    public static final int LIMIT = 1000;

	public static final int DEFAULT_THREAD_MAX_COUNT = 3; // 기본 최대 스레드 개수


	/**
     * 시간 얻기
     * @param nowTime
     * @param minutes
     * @return
     */
    private LocalDateTime getEndDateTime(LocalDateTime nowTime, int minutes) {
    	return LocalDateTime.of(nowTime.getYear(), nowTime.getMonthValue(), nowTime.getDayOfMonth(), nowTime.getHour(), minutes, 0);
    }

    /**
     * @Desc 이지어드민 주문 조회  API 호출
     * @param dto
     */
    @Override
    //@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
    public void runOrderInfo(EZAdminRunOrderInfoRequestDto dto) throws Exception{

    	log.info("===========EZAdmin runOrderInfo() START===============");

    	//조회 항목 요청 DTO 세팅
        EZAdminOrderInfoRequestDto reqDto = new EZAdminOrderInfoRequestDto();
        String action = ApiEnums.EZAdminApiAction.GET_ORDER_INFO.getCode();
        String batchTp = dto.getOrderCsEnum().getBatchTp();
        int limit = LIMIT;

//		LocalDateTime nowTime = LocalDateTime.now();
//		LocalDateTime startDt = LocalDateTime.now();
//		LocalDateTime endDt = LocalDateTime.now();

		// 인자값으로 IfEasyadminInfoId 받았을경우
//		if(paramIfEasyadminInfoId > 0){
//
//			// TODO IfEasyadminInfoId의 배치실행시간 조회
//
//
//		}else{
//
//			// 10분에 돌았을 경우 전 시각 35분 ~ 현시각 5분 까지
//			if(5 <= nowTime.getMinute() && nowTime.getMinute() <= 15) {
//				endDt = getEndDateTime(nowTime, 5);
//				startDt = endDt.minusMinutes(30);
//				endDt = endDt.minusSeconds(1);
//			}
//			// 40분에 돌았을 경우 현시각 5분 ~ 현시각 35분 까지
//			else if(35 <= nowTime.getMinute() && nowTime.getMinute() <= 45) {
//				endDt = getEndDateTime(nowTime, 35);
//				startDt = endDt.minusMinutes(30).minusSeconds(1);
//				endDt = endDt.minusSeconds(1);
//			}
//		}

        reqDto.setAction(action);
		if(ApiEnums.EZAdminGetOrderInfoOrderCs.CLAIM == dto.getOrderCsEnum()) {
			reqDto.setDate_type(ApiEnums.EZAdminGetOrderInfoDateType.CANCEL_DATE.getCode());
		} else {
			reqDto.setDate_type(ApiEnums.EZAdminGetOrderInfoDateType.SHOPSTAT_DATE.getCode());
		}
        reqDto.setStart_date(dto.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        reqDto.setEnd_date(dto.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		reqDto.setShop_id(dto.getShopIdList());
        reqDto.setLimit(limit);

        // 주문 CS 상태별 API 호출
		reqDto.setOrder_cs(dto.getOrderCsEnum().getOrder_cs());

		// 주문조회 API 호출
		callGetOrderInfo(reqDto, batchTp, dto.getParamThreadMaxCount());

    	log.info("===========EZAdmin runOrderInfo() END===============");

    }

    /**
     * @Desc 이지어드민 주문조회 API 호출
     * @param reqDto
	 * @param batchTp
     * @return EZAdminOrderInfoResponseDto
     */
    @Override
	//@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
    public EZAdminOrderInfoResponseDto callGetOrderInfo(EZAdminOrderInfoRequestDto reqDto, String batchTp, int paramThreadMaxCount) throws Exception{

    	log.info("===========EZAdmin callGetOrderInfo() CALL===============");

		EZAdminOrderInfoResponseDto responseDto = new EZAdminOrderInfoResponseDto();
    	MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
    	ApiResult<?> result = null;

    	long ifEasyadminInfoId = 0L;
		int failCount = 0; 	// API 호출 실패 count
    	int page = 1;		// 현재 페이지
		int total = 0;		// 총 주문건수
		int totalPage = 1;	// 총 페이지수
		int limit = reqDto.getLimit(); // limit
		int threadMaxCount = paramThreadMaxCount > 0 ? paramThreadMaxCount : DEFAULT_THREAD_MAX_COUNT; 		// 최대 스레드 개수


		// 처음 1page 호출해서 응답값 master 테이블에 저장
		reqDto.setPage(page);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> map = objectMapper.convertValue(reqDto, new TypeReference<Map<String, String>>(){});
		paramMap.setAll(map);

		log.info("===========EZAdmin callGetOrderInfo() START=============== Page " + page);

		// 이지어드민 주문조회 API 호출
		result = collectionMallEZAdminBatchService.get(paramMap, EZAdminOrderInfoOrderVo.class);

		// IF_EASYADMIN_API_INFO 이지어드민 API 호출 정보 테이블에 저장할 요청 데이터 세팅
		EZAdminApiInfoVo ezAdminApiInfoVo = new EZAdminApiInfoVo();
		ezAdminApiInfoVo.setActionNm(reqDto.getAction());
		ezAdminApiInfoVo.setOrderCs(String.valueOf(reqDto.getOrder_cs()));
		ezAdminApiInfoVo.setReqData(map.toString());

		if(result.getCode().equals(ApiResult.success().getCode())) {
			log.info("===========EZAdmin callGetOrderInfo() SUCCESS =============== Page " + page);

			EZAdminResponseDefaultDto responseDefaultDto = (EZAdminResponseDefaultDto)result.getData();
			total		= Integer.parseInt(responseDefaultDto.getTotal());
			totalPage	= total / limit +1;
			int pageCount = (totalPage-1) / threadMaxCount +1; 	// 스레드당 페이지개수

			// IF_EASYADMIN_API_INFO 이지어드민 API 호출 정보 테이블에 저장
			ezAdminApiInfoVo.setSuccessYn("Y");
			ezAdminApiInfoVo.setResError(responseDefaultDto.getError());
			ezAdminApiInfoVo.setResMsg(responseDefaultDto.getMsg());
			collectionMallEZAdminBatchService.addIfEasyadminApiInfo(ezAdminApiInfoVo);

			// IF_EASYADMIN_INFO 이지어드민 정보 테이블에 저장
			responseDefaultDto.setIfEasyadminApiInfoId(ezAdminApiInfoVo.getIfEasyadminApiInfoId());	// 이지어드민 API 호출 정보 PK
			responseDefaultDto.setAction(reqDto.getAction());
			responseDefaultDto.setMap(map.toString());
			responseDefaultDto.setSyncCd(EZAdminEnums.EZAdminSyncCd.SYNC_CD_WAIT.getCode());	// 배치 연동 상태
			responseDefaultDto.setEasyadminBatchTp(batchTp); 									// 이지어드민 배치 타입
			responseDefaultDto.setPage(String.valueOf(totalPage));								// 총 page
			responseDefaultDto.setReqStartDate(reqDto.getStart_date());							// 요청 시작 일자
			responseDefaultDto.setReqEndDate(reqDto.getEnd_date());								// 요청 종료 일자
			collectionMallEZAdminBatchService.addEasyAdminInfo(responseDefaultDto);

			ifEasyadminInfoId = responseDefaultDto.getIfEasyadminInfoId();

			// 기본 스래드 개수보다 총 페이지수가 작을 경우
			if(totalPage < threadMaxCount){
				threadMaxCount = totalPage;
				pageCount = 1;
			}

			System.out.println("################################################");
			System.out.println("############### [" + (new SimpleDateFormat("yyyy / MM / dd / HH:mm:ss").format(Calendar.getInstance().getTime())) + "] start");
			System.out.println("################################################");

			Thread[] threads = new Thread[threadMaxCount];
			for(int i=0; i < threadMaxCount; i++){
				int startPage = (i*pageCount) + 1;			// 시작페이지
				int endPage = (i*pageCount) + pageCount;	// 종료페이지

				// 마지막페이지일 경우
				if(i == threadMaxCount-1){
					endPage = totalPage;
				}

				System.out.println("------------ " + ("Thread #" + i) + " ::: start : " + startPage + ", end : " + endPage);

				// 스래드 실행
				Runnable ezadminOrderInsertRunnable = new CollectionMallEZAdminBatchThread(startPage, endPage, ifEasyadminInfoId, reqDto, batchTp, collectionMallEZAdminBatchBiz);
				threads[i] = new Thread(ezadminOrderInsertRunnable);
				threads[i].setName("Thread # " + i);
				threads[i].start();

			}

			// 실행중인 스래드가 없으면 종료처리
			while(true){
				try{

//					Thread.sleep(1000 * 60 * 28); // 28분후 배치 강제종료위해 28분으로 세팅
					Thread.sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}

				int finishCount = 0;
				for(int j=0; j<threadMaxCount ; j++){
					if(threads[j].getState() == Thread.State.TERMINATED){
						finishCount++;
					}
				}

				if(finishCount == threadMaxCount) {

					// API 수집 성공여부 업데이트
					collectionMallEZAdminBatchService.putIfEasyadminInfoReqDateCollectCd(ifEasyadminInfoId);

					// 성공,실패 건수 업데이트
					collectionMallEZAdminBatchService.putSuccessInsertOrderCount(ifEasyadminInfoId);
					break;
				}
			}

		}else{

			log.error("===========EZAdmin callGetOrderInfo() FAIL==============={}", result.getMessage());

			if (result.getCode().equals(ApiEnums.EzAdminStatus.NO_DATA.getCode())
				|| result.getCode().equals(ApiEnums.EzAdminStatus.RESPONSEBODY_NO_DATA.getCode())
			) {
				// 데이터가 없는경우 성공 처리
				ezAdminApiInfoVo.setSuccessYn("Y");
			}else{
				// 그 외 에러인 경우 실패
				ezAdminApiInfoVo.setSuccessYn("N");
			}

			// IF_EASYADMIN_API_INFO 이지어드민 API 호출 정보 테이블에 저장
			ezAdminApiInfoVo.setResMsg(result.getMessage());
			collectionMallEZAdminBatchService.addIfEasyadminApiInfo(ezAdminApiInfoVo);

		}

    	 log.info("===========EZAdmin callGetOrderInfo() END===============");

    	return responseDto;
    }


    /**
	 * 이지어드민 주문조회 API 호출
	 * */
	public EZAdminResponseDefaultDto getOrderInforResponseDto(int page, EZAdminOrderInfoRequestDto reqDto) {

		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		reqDto.setPage(page);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> map = objectMapper.convertValue(reqDto, new TypeReference<Map<String, String>>(){});
		paramMap.setAll(map);

		// 이지어드민 주문조회 API 호출
		ApiResult<?> result = collectionMallEZAdminBatchService.get(paramMap, EZAdminOrderInfoOrderVo.class);

		EZAdminResponseDefaultDto responseDefaultDto = null;

		if(result.getCode().equals(ApiResult.success().getCode())) {
			log.info("===========EZAdmin addOrderInfo call API SUCCESS =============== Page " + page);

			responseDefaultDto = (EZAdminResponseDefaultDto) result.getData();
			responseDefaultDto.setAction(reqDto.getAction());
			responseDefaultDto.setMap(map.toString());
		}

		return responseDefaultDto;
	}

    @Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
    public void addOrderInfo(int page, long ifEasyadminInfoId, EZAdminOrderInfoRequestDto reqDto,String batchTp) throws Exception{

		// API 호출
		EZAdminResponseDefaultDto responseDefaultDto = getOrderInforResponseDto(page, reqDto);

		if(responseDefaultDto != null){
			log.info("===========EZAdmin addOrderInfo call API SUCCESS =============== Page " + page);

			// IF_EASYADMIN_INFO_REQ_DATA 이지어드민 호출 정보 table insert
			responseDefaultDto.setIfEasyadminInfoId(ifEasyadminInfoId);
			responseDefaultDto.setCollectCd(OutmallEnums.CollectionCode.ING.getCode());	// API 수집상태 (ING: 수집중)
			collectionMallEZAdminBatchService.addEasyAdminInfoReqData(responseDefaultDto);

			// 주문 정보 유효성검사 후 저장
			List<EZAdminOrderInfoOrderVo> orderInfoResponseList = (List<EZAdminOrderInfoOrderVo>)responseDefaultDto.getData();
			orderInfoResponseList.forEach(f -> f.setIfEasyadminInfoId(ifEasyadminInfoId));
			orderInfoResponseList.forEach(f -> f.setIfEasyadminInfoReqDataId(responseDefaultDto.getIfEasyadminInfoReqDataId()));

			if(CollectionUtils.isNotEmpty(orderInfoResponseList)) {

				if(batchTp.equals(ApiEnums.EZAdminGetOrderInfoOrderCs.ORDER.getBatchTp())) {
					// 주문 CS상태 정상
					orderExecute(orderInfoResponseList);
				}else {
					// 주문 CS상태 클레임
					claimExecute(orderInfoResponseList);
				}
			}

		}else{
			log.error("===========EZAdmin addOrderInfo call API FAIL =============== Page " + page);
		}
	}

	/**
     * @Desc 이지어드민 주문 유효성검사 & 주문 DB insert
     * @param orderInfoList
     */
    @Override
	//@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
    public void orderExecute(List<EZAdminOrderInfoOrderVo> orderInfoList) throws Exception{

    	EZAdminValidator ezadminValidator = new EZAdminValidator();
		List<EZAdminOrderInfoOrderVo> successOrderInfoList = new ArrayList<>();
		List<EZAdminOrderInfoOrderVo> failOrderInfoList = new ArrayList<>();
		EZAdminInfoDto ezadminInfo = new EZAdminInfoDto();

		String outmallType = OutmallEnums.OutmallType.EASYADMIN.getCode();

		// 판매처 정보 목록 조회
		List<EZAdminSellersInfoDto> sellersInfoList = collectionMallEZAdminBatchService.getOmSellesInfoList(orderInfoList);

		// 판매처 수집몰 연동여부가 Y인 경우(HGRM-9038 테스트위한 임시코드)
		boolean isExistSellersInfo = true;


        Map<String, List<EZAdminSellersInfoDto>> sellersInfo = sellersInfoList.stream().collect(Collectors.groupingBy(EZAdminSellersInfoDto::getOutmallCd,LinkedHashMap::new,Collectors.toList()));
		String errorMsg = "";
		boolean successFlag = true;
        System.out.println("--------------------------------------------------------------------------");
		List<String> testGoodsIdList = new ArrayList<>();
        for(EZAdminOrderInfoOrderVo orderInfo : orderInfoList) {
    		ezadminInfo.setIfEasyadminInfoId(orderInfo.getIfEasyadminInfoId());
			successFlag = true;
    		collectionMallEZAdminBatchService.validEZAdminOrderInfo(orderInfo);
    		//try {

			// 합포번호 중복체크
			int collectionMallIdCnt = collectionMallEZAdminBatchService.getOrderCnt(StringUtil.nvl(orderInfo.getPack(), ""));
			if (collectionMallIdCnt > 0) {
				orderInfo.setErrorMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.EASYADMIN_COLLECTION_MALL_ID_OVERLAP.getMessage(), orderInfo.getPack()));
				successFlag = false;
			}

			// 주문자명 체크
			if(!"".equals(ezadminValidator.validationOrderName(orderInfo.getRecv_name()))){
				orderInfo.setErrorMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.RECEIVER_INFO_ERROR.getMessage(), orderInfo.getRecv_name()));
				successFlag = false;
			}

    			// 판매처PK가 없을 경우 실패 처리
    			if(!sellersInfo.containsKey(String.valueOf(orderInfo.getShop_id()))) {
					orderInfo.setErrorMessage("판매처 정보 없음");
    				//throw new BaseException("판매처 정보 없음", null, null);
					successFlag = false;
					isExistSellersInfo = false; // 판매처 정보가 없거나, 판매처 수집몰 연동여부가 N인 경우(HGRM-9038 테스트위한 임시코드)
    			}
    			else {
    				orderInfo.setOmSellersId(sellersInfo.get(String.valueOf(orderInfo.getShop_id())).get(0).getOmSellersId());
    			}

    			// 상품ID 정보 List 생성
    			List<String> goodsIdList = collectionMallEZAdminBatchService.getGoodsIdList(orderInfo.getOrder_products());
				for(String goodsId : goodsIdList){
					testGoodsIdList.add(goodsId);
				}
    			// 상품 정보 얻기
    			Map<Long, GoodsSearchOutMallVo> goodsIdMap = orderExcelGoodsIdListData.getGoodsMaps(goodsIdList);

    			// 주문 상품 정보
				List<EZAdminOrderInfoOrderProductVo> order_products = new ArrayList<>();
    			for(EZAdminOrderInfoOrderProductVo productVo : orderInfo.getOrder_products()) {
					errorMsg = "";
					errorMsg = ezadminValidator.validationProductSeq(productVo.getPrd_seq());
					if (!"".equals(errorMsg)){
						if (successFlag == true) {
							//orderInfo.setErrorMessage(errorMsg);
							productVo.setErrorMessage(errorMsg);
						}
						successFlag = false;
					}
					if ("".equals(errorMsg)) {
						errorMsg = ezadminValidator.validationProductId(productVo.getProduct_id());
						if (!"".equals(errorMsg)) {
							if (successFlag == true) {
								//orderInfo.setErrorMessage(errorMsg);
								productVo.setErrorMessage(errorMsg);
							}
							successFlag = false;
						}
						if ("".equals(errorMsg)) {
							EZAdminGoodsVaildResponseDto goodsItemValid = collectionMallEZAdminBatchService.getGoodsItemValidateEZAdmin(productVo, goodsIdMap, orderInfo, sellersInfoList);

							if (!EZAdminEnums.EZAdminGoodsValidMessage.SUCCESS.getCode().equals(goodsItemValid.getStatus())) {
								if (successFlag == true) {
									//orderInfo.setErrorMessage(goodsItemValid.getMessage());
									productVo.setErrorMessage(goodsItemValid.getMessage());
								}
								//throw new BaseException(goodsItemValid.getMessage(), null, null);
								successFlag = false;
							}


							// 관리번호 중복 체크
							int collectionMallDetailIdCnt = collectionMallEZAdminBatchService.getOrderDetailCnt(StringUtil.nvl(StringUtil.nvl(productVo.getPrd_seq())));
							if (collectionMallDetailIdCnt > 0) {
								productVo.setErrorMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.EASYADMIN_COLLECTION_MALL_DETAIL_ID_OVERLAP.getMessage(), StringUtil.nvl(productVo.getPrd_seq())));
								successFlag = false;
							}

						}
					}

					order_products.add(productVo);
    			}
				orderInfo.setOrder_products(order_products);
			//successFlag = true;
    			// 주문 성공 list
				if (successFlag == true) {
					successOrderInfoList.add(orderInfo);
				} else {
					//failOrderInfoList.add(orderInfo);
					// 판매처 수집몰 연동여부가 Y인 경우만 실패내역 저장(HGRM-9038 테스트위한 임시코드)
					if(isExistSellersInfo){
						failOrderInfoList.add(orderInfo);
					}
				}
    		/*
    		}
    		catch(BaseException e) {
    			// 주문 실패 list
    			orderInfo.setErrorMessage(e.getMessage());
    			failOrderInfoList.add(orderInfo);
    		}
    		*/
    	}


        System.out.println("--------------------------------------------------------------------------");
    	// DB insert
    	if(CollectionUtils.isNotEmpty(failOrderInfoList)) {

    		// 실패건이 존재할 경우 성공 목록 중 실패와 동일한 합포번호를 가진 상품들 실패 처리
    		for(int i=0; i<failOrderInfoList.size(); i++) {
    			EZAdminOrderInfoOrderVo failOrderInfo = failOrderInfoList.get(i);
    			int failPack = failOrderInfo.getPack();
    			for(int j=0; j<successOrderInfoList.size(); j++) {
    				EZAdminOrderInfoOrderVo succOrderInfo = successOrderInfoList.get(j);
        			int succPack = succOrderInfo.getPack();
        			if(succPack == failPack) {
        				log.debug("success -> fail Move [" + failPack + "]");
        				succOrderInfo.setErrorMessage(failOrderInfo.getErrorMessage());
        				failOrderInfoList.add(i, succOrderInfo);
        				successOrderInfoList.remove(j);
        				j--;
        			}
    			}
    		}

    		for(EZAdminOrderInfoOrderVo orderInfo : failOrderInfoList) {
    			orderInfo.setFailType(OutmallEnums.OutmallFailType.UPLOAD.getCode());	// 수집몰 연동 실패구분(U:업로드)
    			collectionMallEZAdminBatchService.addEasyAdminOrderFail(orderInfo);

    			for(EZAdminOrderInfoOrderProductVo productVo : orderInfo.getOrder_products()) {
    				productVo.setIfEasyadminInfoId(orderInfo.getIfEasyadminInfoId());
    				productVo.setIfEasyadminOrderFailId(orderInfo.getIfEasyadminOrderFailId());
					//productVo.setErrorMessage(orderInfo.getErrorMessage());
    				collectionMallEZAdminBatchService.addEasyAdminOrderFailDetail(productVo);
    			}
    		}
    	}

    	if(CollectionUtils.isNotEmpty(successOrderInfoList)) {

			long ifEasyadminInfoId = 0;
			long ifEasyadminInfoReqDataId = 0;
    		for(EZAdminOrderInfoOrderVo orderInfo : successOrderInfoList) {

    			collectionMallEZAdminBatchService.addEasyAdminOrderSuccess(orderInfo);
				ifEasyadminInfoId = orderInfo.getIfEasyadminInfoId();
				ifEasyadminInfoReqDataId = orderInfo.getIfEasyadminInfoReqDataId();
				int cnt = 0;
    			for(EZAdminOrderInfoOrderProductVo productVo : orderInfo.getOrder_products()) {
    				productVo.setIfEasyadminInfoId(orderInfo.getIfEasyadminInfoId());
    				productVo.setIfEasyadminOrderSuccId(orderInfo.getIfEasyadminOrderSuccId());

    				if (cnt == 0){
    					productVo.setPrd_amount(orderInfo.getAmount());
					} else {
    					productVo.setPrd_amount(0);
					}
    				collectionMallEZAdminBatchService.addEasyAdminOrderSuccessDetail(productVo);
    				cnt++;
    			}
    		}

    		List<Long> succIdList = successOrderInfoList.stream().map(m->m.getIfEasyadminOrderSuccId()).collect(Collectors.toList());

			// 상품가격 비율을 적용해서 재계산
			collectionMallEZAdminBatchService.addEasyAdminOrderRatioPrice(ifEasyadminInfoId,ifEasyadminInfoReqDataId);

			// 재계산 적용 업데이트
			collectionMallEZAdminBatchService.putEasyAdminOrderRatioPrice(ifEasyadminInfoId, succIdList);

			// 재계산 성공 정보에 업데이트
			collectionMallEZAdminBatchService.putEasyAdminOrderSuccRatioPrice(ifEasyadminInfoId, succIdList);

    	}

    }

    /**
     * @Desc 이지어드민 클레임 주문 DB insert
     * @param claimInfoList
     */
    @Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
    public void claimExecute(List<EZAdminOrderInfoOrderVo> claimInfoList) throws Exception{

    	if(CollectionUtils.isNotEmpty(claimInfoList)) {

    		for(EZAdminOrderInfoOrderVo claimInfo : claimInfoList) {

				collectionMallEZAdminBatchService.validEZAdminOrderInfo(claimInfo);

				claimInfo.setProcessCd(EZAdminEnums.EZAdminProcessCd.PROCESS_CD_WAIT.getCode());
				collectionMallEZAdminBatchService.addEasyAdminOrderClaim(claimInfo);

				for (EZAdminOrderInfoOrderProductVo productVo : claimInfo.getOrder_products()) {
					productVo.setIfEasyadminInfoId(claimInfo.getIfEasyadminInfoId());
					productVo.setIfEasyadminOrderClaimId(claimInfo.getIfEasyadminOrderClaimId());

					collectionMallEZAdminBatchService.addEasyAdminOrderClaimDetail(productVo);
				}
    		}
    	}
    }


    /**
     * @Desc 이지어드민 송장입력
     */
    @Override
    public void runTransNo() throws Exception{

    	log.info("===========EZAdmin runTransNo() START===============");

		LocalDateTime nowTime = LocalDateTime.now();
		LocalDateTime startDt = LocalDateTime.now();
		LocalDateTime endDt = LocalDateTime.now();

		// 10분에 돌았을 경우 전 시각 35분 ~ 현시각 5분 까지
//		if(5 <= nowTime.getMinute() && nowTime.getMinute() <= 15) {
//			endDt = getEndDateTime(nowTime, 5);
//			startDt = endDt.minusMinutes(30);
//			endDt = endDt.minusSeconds(1);
//		}
//		// 40분에 돌았을 경우 현시각 5분 ~ 현시각 35분 까지
//		else if(35 <= nowTime.getMinute() && nowTime.getMinute() <= 45) {
//			endDt = getEndDateTime(nowTime, 35);
//			startDt = endDt.minusMinutes(30).minusSeconds(1);
//			endDt = endDt.minusSeconds(1);
//		}

		// 배송중일자기준 (현재시간-1시간전 ~ 현재시간) 인 주문건 조회
		startDt = nowTime.minusHours(1);

		String startDIDate = startDt.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
		String endDIDate = endDt.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));

    	// 1. 송장입력 대상 조회
		List<EZAdminTransNoTargetVo> targetOrderList = collectionMallEZAdminBatchService.getTransNoTargetList(startDIDate,endDIDate);

		if(CollectionUtils.isNotEmpty(targetOrderList)){

			// 2. 이지어드민 송장입력 API 호출
			for(EZAdminTransNoTargetVo vo : targetOrderList){
				try{
					callSetTransNo(vo);
				}catch(Exception e){
					e.printStackTrace();
					log.error("===========EZAdmin callSetTransNo() ERROR =============== EZAdminTransNoTargetVo :: {}",vo);
				}
			}
		}

    	log.info("===========EZAdmin runTransNo() END===============");
    }

	/**
     * @Desc 이지어드민 문의글 조회  API 호출
     */
    @Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
    public int runOutmallQnaInfo(EZAdminQnaInfoRequestDto reqDto) throws Exception{

    	log.info("===========EZAdmin runOutmallQnaInfo() START===============");

    	//조회 항목 요청 DTO 세팅
        String action = ApiEnums.EZAdminApiAction.GET_AUTO_CS_SYNC_DATA.getCode();
        String status = "0,1,2,3";
        int limit = LIMIT;
		int failCount = 0; //API 호출 실패 count


        reqDto.setAction(action);
        reqDto.setDate_type(ApiEnums.EZAdminGetAutoCsSyncDataDateType.REG_DATE.getCode());
        reqDto.setStatus(status);
        reqDto.setLimit(limit);
		
        // 문의글 리스트
        List<EZAdminQnaInfoVo> qnaInfoList = new ArrayList<>();
		
        // 문의글조회 API 호출
		EZAdminQnaInfoResponseDto responseQnaInfoList = collectionMallEZAdminBatchService.callGetQnaInfo(reqDto);
		if(CollectionUtils.isNotEmpty(responseQnaInfoList.getQnaInfoList())){
			qnaInfoList = ListUtils.union(qnaInfoList, responseQnaInfoList.getQnaInfoList());
		}
		failCount += responseQnaInfoList.getFailCount();

        // validation check & DB insert
        if(CollectionUtils.isNotEmpty(qnaInfoList)) {
			collectionMallEZAdminBatchService.qnaExecute(qnaInfoList);
        }


    	log.info("===========EZAdmin runOutmallQnaInfo() END===============");

        return failCount;
    }

	@Override
	public EZAdminRunOrderInfoRequestDto calcBatchDateTime(String action, String batchTp) {
		return collectionMallEZAdminBatchService.calcBatchDateTime(action, batchTp);
	}

	@Override
	public EZAdminRunOrderInfoRequestDto calcQnaBatchDateTime(String ezadminBatchTypeCd) {
		return collectionMallEZAdminBatchService.calcQnaBatchDateTime(ezadminBatchTypeCd);
	}

	@Override
	public void checkBosCollectionMallInterfaceFail() throws Exception{
    	collectionMallEZAdminBatchService.checkBosCollectionMallInterfaceFail();
	}

	/**
	 * @Desc 이지어드민 송장입력  API 호출
	 */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
    protected void callSetTransNo(EZAdminTransNoTargetVo vo) throws Exception{
        log.info("===========EZAdmin callSetTransNo() START===============");

		//조회 항목 요청 param 세팅
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		String action = ApiEnums.EZAdminApiAction.SET_TRANS_NO.getCode();

		EZAdminTransNoRequestDto reqDto = new EZAdminTransNoRequestDto();
		reqDto.setAction(action);
		reqDto.setPrd_seq(vo.getPrd_seq());
		reqDto.setTrans_corp(vo.getTrans_corp());
		reqDto.setTrans_no(vo.getTrans_no());
		reqDto.setTrans_pos(1); 	// 0:배송처리 안함, 1:배송처리

		ApiResult<?> result = null;

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> map = objectMapper.convertValue(reqDto, new TypeReference<Map<String, String>>(){});
		paramMap.setAll(map);

		log.info("===========EZAdmin callSetTransNo() CALL===============");

		// 이지어드민 송장입력 API 호출
        result = collectionMallEZAdminBatchService.get(paramMap, null);

		// IF_EASYADMIN_INFO 이지어드민 정보 table insert
		EZAdminResponseDefaultDto responseDefaultDto = new  EZAdminResponseDefaultDto();
		responseDefaultDto.setAction(action);
		responseDefaultDto.setMap(map.toString());
		responseDefaultDto.setResponseData((result != null || result.getData() != null ? result.getData().toString() : ""));
		responseDefaultDto.setTotal("1");
		responseDefaultDto.setPack_cnt("1");
		responseDefaultDto.setProduct_sum("1");
		responseDefaultDto.setPage("1");
		responseDefaultDto.setLimit("1");
		responseDefaultDto.setSyncCd("E");
		responseDefaultDto.setEasyadminBatchTp(ApiEnums.EZAdminGetOrderInfoOrderCs.TRANS.getBatchTp());
		LocalDateTime nowTime = LocalDateTime.now();
		String nowTimeStr = nowTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
		responseDefaultDto.setReqStartDate(nowTimeStr);
		responseDefaultDto.setReqEndDate(nowTimeStr);

		if(result != null && result.getCode() != null && ApiResult.success().getCode().equals(result.getCode())) {

			log.info("===========EZAdmin callSetTransNo() SUCCESS===============");

			// IF_EASYADMIN_INFO 이지어드민 정보 table insert
            collectionMallEZAdminBatchService.addEasyAdminInfo(responseDefaultDto);

			vo.setStatus("Y");		// 성공
			// OD_TRACKING_NUMBER 이지어드민 송장입력API 전송여부 업데이트
			collectionMallEZAdminBatchService.putOdTrackingNumberEzadminApiYn(vo);
		} else {

			log.info("===========EZAdmin callSetTransNo() FAIL===============");

			responseDefaultDto.setError(vo.getTrans_no());

			// IF_EASYADMIN_INFO 이지어드민 정보 table insert
			collectionMallEZAdminBatchService.addEasyAdminInfo(responseDefaultDto);

			vo.setStatus("E");		// 에러
			// OD_TRACKING_NUMBER 이지어드민 송장입력API 전송여부 업데이트
			collectionMallEZAdminBatchService.putOdTrackingNumberEzadminApiYn(vo);
			log.error("===========EZAdmin callSetTransNo() FAIL=============", result.getMessage());
			log.error("===========EZAdmin callSetTransNo() FAIL============= requestDto:{}",reqDto);
		}

		log.info("===========EZAdmin callSetTransNo() END===============");
    }


}
