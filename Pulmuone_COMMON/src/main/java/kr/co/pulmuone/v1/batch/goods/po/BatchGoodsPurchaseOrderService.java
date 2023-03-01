package kr.co.pulmuone.v1.batch.goods.po;

import kr.co.pulmuone.v1.batch.goods.po.dto.ErpIfPurchaseOrdHeaderRequestDto;
import kr.co.pulmuone.v1.batch.goods.po.dto.ErpIfPurchaseOrdLineRequestDto;
import kr.co.pulmuone.v1.batch.goods.po.dto.ErpIfPurchaseOrdRequestDto;
import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ItemErpStockResultVo;
import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import kr.co.pulmuone.v1.comm.api.constant.TransferServerTypes;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.mapper.goods.stock.ItemErpStockCommonMapper;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.po.BatchGoodsPoInputMapper;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.stock.BatchItemErpStockMapper;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 *
 * <PRE>
 * Forbiz Korea
 * ERP 구매발주 입력 배치 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.12.21    이성준       최초작성
 *  1.1    2020.03.24    남기승		발주배치 재개발.
 * =======================================================================
 * </PRE>
 *
 ** addPffGoodsPurchaseOrderJob
 * -- SIPU : 식품의 발주
 * -- SIMO : 식품의 이동
 * -- CJMO : CJ 물류 이동
 *
 ** addOghGoodsPurchaseOrderJob
 * -- OGPUR1 : 올가의 R1 발주
 * -- OGPUR2 : 올가의 R2 발주
 * -- OGMO : 올가의 이동
 *
 *
 * -- FMMO : 푸드머스 이동
 * -- COUPANG : 쿠팡 직매입
 */
@Service
public class BatchGoodsPurchaseOrderService {

	private static final Logger log = LoggerFactory.getLogger(BatchGoodsPurchaseOrderService.class);

	//ERP API 인터페이스 ID
	private static final String PURCHASE_ORDER_INSERT_INTERFACE_ID = "IF_PURCHASE_INP";	//구매발주 입력 ERP IF

	// 백암물류 발주위한 온도값 추가
	private static final String TMP_FROZEN = "1";	// 냉동
	private static final String TMP_COOL = "8";	// 냉장


	@Autowired
	private BatchGoodsPoInputMapper batchGoodsPoInputMapper;

	@Autowired
	private BatchItemErpStockMapper batchItemErpStockMapper;

	@Autowired
	private ItemErpStockCommonMapper itemErpStockCommonMapper;

	@Autowired
	ErpApiExchangeService erpApiExchangeService;

	/**
	 * 풀무원식품 발주 (용인, 백암)
	 * ERP 연동 후 결과값 return
	 * @return
	 */
	protected void addPffGoodsPurchaseOrderJob(String poBatchTp, String transferServerType) {

		// OriSysReq 생성
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("yyMMddHHmmss").toFormatter();

		String ordNoDtlSeq = now.format(dateTimeFormatter);
		String oriSysSeq = "PO_" + ordNoDtlSeq;

		boolean isSuccess = false;

		ErpIfPurchaseOrdLineRequestDto dto = new ErpIfPurchaseOrdLineRequestDto();
		dto.setOriSysSeq(oriSysSeq);
		dto.setPoBatchTp(poBatchTp);

		List<ErpIfPurchaseOrdLineRequestDto> preOrderList = batchGoodsPoInputMapper.getPoIFList(dto);	// 원본데이터

		// 2021.07.09 발주저장된 품목에 대해서만 전송후 재고 업데이트를 하여 저장안된 품목도 전체 재고 업데이트 하기위해 추가한 로직.
		dto.setGetPoAllYn("Y");			// 	발주내역 전체 조회
		List<ErpIfPurchaseOrdLineRequestDto> getPreOrderList = batchGoodsPoInputMapper.getPoIFList(dto);	// 발주전체데이터

		List<ErpIfPurchaseOrdLineRequestDto> cjWmsList = new ArrayList<>(preOrderList.size());

		Iterator<ErpIfPurchaseOrdLineRequestDto> iterator = preOrderList.iterator();
		while(iterator.hasNext()){
			cjWmsList.add(iterator.next().clone());
		}

		// 식품 발주시 발주데이터 생성
		if (TransferServerTypes.PFF.getCode().equals(transferServerType)) {
			if(!preOrderList.isEmpty()){
				isSuccess = createErpSendPoList(preOrderList, oriSysSeq, transferServerType);
			}
		}

		// 백암물류 발주시 ORG_GUB 값은 CJMO로 전달한다.
		if (TransferServerTypes.CJWMS.getCode().equals(transferServerType)) {

			for(int i=0; i < preOrderList.size(); i++) {
				preOrderList.get(i).setOrdGub(Constants.CJMO);
				preOrderList.get(i).setReqDat(preOrderList.get(i).getMoveReqDat());		// 발주요청일은 D+1
				preOrderList.get(i).setOriSysSeq(oriSysSeq);
				if (preOrderList.get(i).getOrdCnt() == 0) {
					preOrderList.get(i).setReqDat(preOrderList.get(i).getMoveReqDat());	// 발주요청일은 D+1
					preOrderList.get(i).setOrdCnt(preOrderList.get(i).getReqCnt()); 	// 이동 수량은 ordCnt에 넣어준다.
				}
			}

      		if (!preOrderList.isEmpty()) {
      			// 헤더 분할후 생성위한 객체 추가
      			List<ErpIfPurchaseOrdLineRequestDto> tmp1List = new ArrayList<>();	// 냉동
				List<ErpIfPurchaseOrdLineRequestDto> tmp2List = new ArrayList<>();	// 냉장
				List<ErpIfPurchaseOrdLineRequestDto> tmp3List = new ArrayList<>();	// 그 외. 상온, 실온, 기타

      			String tmpVal = "";
				String lineOriSysSeq = "";
      			// 정렬된 순서대로 온도값 같은 녀석들만 따로따로 보낼거임.
				for(ErpIfPurchaseOrdLineRequestDto lineRequestDto : preOrderList){

					tmpVal = lineRequestDto.getTmpVal();				// 온도값 조회
					lineOriSysSeq = lineRequestDto.getOriSysSeq();

					if(TMP_FROZEN.equals(tmpVal)){
						lineRequestDto.setOriSysSeq(lineOriSysSeq + "_1");			// 냉동
						tmp1List.add(lineRequestDto);
					} else if(TMP_COOL.equals(tmpVal)){
						lineRequestDto.setOriSysSeq(lineOriSysSeq + "_8");			// 냉장
						tmp2List.add(lineRequestDto);
					} else {
						lineRequestDto.setOriSysSeq(lineOriSysSeq + "_2");			// 그 외 상온값으로 전달.
						tmp3List.add(lineRequestDto);
					}
				}

				// 냉동발주 있을 경우
				if(!tmp1List.isEmpty()) {
					isSuccess = createErpSendPoList(tmp1List, oriSysSeq + "_1", transferServerType);
				}

				// 냉장발주가 있을경우
				if(!tmp2List.isEmpty()) {
					isSuccess = createErpSendPoList(tmp2List, oriSysSeq + "_8", transferServerType);
				}

				// 상온발주가 있을경우
				if(!tmp3List.isEmpty()) {
					isSuccess = createErpSendPoList(tmp3List, oriSysSeq + "_2", transferServerType);
				}
			}
		}

		if (isSuccess){

			LocalDateTime now2 = now.plusSeconds(10);
			String nowDate = now2.format(dateTimeFormatter);

			String poOriSysReq = "";		// PO발주용 OriSysReq
			String moveOriSysReq = ""; 		// 이동발주 OriSysSeq

			// 풀무원식품 생산발주(SIPU)인 경우 이동발주(SIMO)를 한번 더 넣는다.
			if(TransferServerTypes.PFF.getCode().equals(transferServerType)){

				List<ErpIfPurchaseOrdLineRequestDto> sipuIfList = new ArrayList<>();
				moveOriSysReq = "PO_"+nowDate; 											// 이동발주 OriSysSeq : PO-yyMMddHHmmss 로 설정

				for(int i=0; i < preOrderList.size(); i++) {
					// 식품 이동발주를 추가하기 위해 SIPU만 담아서 SIMO로 생성한다.
					if(preOrderList.get(i).getOrdGub().equals(Constants.SIPU)){
						preOrderList.get(i).setOrdGub(Constants.SIMO);						// 이동발주로 설정
						preOrderList.get(i).setOriSysSeq(moveOriSysReq);					// OriSysReq는 새로운 값으로 설정
						preOrderList.get(i).setReqDat(preOrderList.get(i).getMoveReqDat());	// 발주요청일은 이동일로 변경
						preOrderList.get(i).setPrmDat(preOrderList.get(i).getReqDat());		// 출고예정일.
						preOrderList.get(i).setReqCnt(preOrderList.get(i).getOrdCnt());		// 주문수량 -> 발주수량으로 변경
						preOrderList.get(i).setOrdCnt(0);									// 주문수량 0
						sipuIfList.add(preOrderList.get(i));
					}
				}

				// PO발주가 있을 경우 이동발주를 생성한다.
				if(!sipuIfList.isEmpty()){
					isSuccess = createErpMovePoList(sipuIfList, moveOriSysReq);			// 이동발주
				}
			}

			// 백암물류의 경우 동일 발주데이터를 용인으로 한번 더 보내야 한다.
			// PFF 로 전송.
			if(TransferServerTypes.CJWMS.getCode().equals(transferServerType)){

				LocalDateTime now3 = now2.plusSeconds(10);
				nowDate = now3.format(dateTimeFormatter);

				poOriSysReq = "PO_"+nowDate; // 발주 OriSysSeq : PO-yyMMddHHmmss 로 설정

				// 생산발주로 넘어간 값은 이동으로도 한번 더 넣는다.
				List<ErpIfPurchaseOrdLineRequestDto> cjIfList = new ArrayList<>();

				for(int j=0; j < cjWmsList.size(); j++) {
					cjWmsList.get(j).setCrpCd(TransferServerTypes.PFF.getCode());	// CRP_CD : PFF로 전송
					cjWmsList.get(j).setOriSysSeq(poOriSysReq);						// 중복되지 않게 키값 재설정
					cjWmsList.get(j).setDvlReqDat(cjWmsList.get(j).getDvlReqDat());	// 입고예정일
					cjWmsList.get(j).setReqDat(cjWmsList.get(j).getPoReqDat());		// PO 발주일자 (이동발주시 SYSDATE+1)
					cjWmsList.get(j).setPrmDat(cjWmsList.get(j).getPoReqDat());		// 고객요청일
				}

				// PFF로 수정하여 PO발주 전송
				if(!cjWmsList.isEmpty()){
					isSuccess = createErpSendPoList(cjWmsList, poOriSysReq, transferServerType);
				}

				LocalDateTime now4 = now3.plusSeconds(10);
				String nowDate2 = now4.format(dateTimeFormatter);

				moveOriSysReq = "PO_"+nowDate2; // 발주 OriSysSeq : PO-yyMMddHHmmss 로 설정

				for(int k=0; k < cjWmsList.size(); k++) {
					// 식품 이동발주를 추가하기 위해 SIPU만 담아서 호출한다.
					if(cjWmsList.get(k).getOrdGub().equals(Constants.SIPU)){
						cjWmsList.get(k).setOrdGub(Constants.SIMO);						// 이동발주로 설정
						cjWmsList.get(k).setCrpCd(TransferServerTypes.PFF.getCode());	// CRP_CD : PFF로 전송
						cjWmsList.get(k).setReqDat(cjWmsList.get(k).getMoveReqDat());	// 이동발주일자
//						cjWmsList.get(k).setDvlReqDat(cjWmsList.get(k).getMoveReqDat());// ERP 요청일 (이동은 발주일+1)
						cjWmsList.get(k).setPrmDat(cjWmsList.get(k).getMoveReqDat());	// 출고예정일
						cjWmsList.get(k).setOrgCd("803");								// ORG_CD : 803(백암)
						cjWmsList.get(k).setOriSysSeq(moveOriSysReq);					// OriSysReq는 새로운 값으로 설정
						cjWmsList.get(k).setReqCnt(cjWmsList.get(k).getOrdCnt());		// 주문수량 -> 발주수량으로 변경
						cjWmsList.get(k).setOrdCnt(0);									// 주문수량 0
						cjIfList.add(cjWmsList.get(k));
					}
				}

				// PFF로 수정하여 이동발주 전송
				if(!cjIfList.isEmpty()){
					isSuccess = createErpMovePoList(cjIfList, moveOriSysReq);
				}
			}
		}

		// 결과 업데이트 처리
		if (isSuccess) {
			preOrderResultUpdate(preOrderList, poBatchTp);		// 발주한 품목에 대한 발주이력 업데이트
			preOrderStockUpdate(getPreOrderList, poBatchTp);	// 발주여부와 상관없이 전체 품목 재고 업데이트
		}
	}

	/**
	 * 올가 R1, R2발주
	 * ERP 연동 후 결과값 return
	 * @return
	 */
	protected void addOghGoodsPurchaseOrderJob(String poBatchTp, String transferServerType) {

		String ordNoDtlSeq = getPoOrdNoDtl();		// Sequence 조회
		String oriSysSeq = "R6_" + ordNoDtlSeq; 	// 올가 발주 형식은 'R6_000001'

		boolean isSuccess = false;

		ErpIfPurchaseOrdLineRequestDto dto = new ErpIfPurchaseOrdLineRequestDto();
		dto.setOriSysSeq(oriSysSeq);
		dto.setPoBatchTp(poBatchTp);

		// 올가R1, R2발주시 PO발주 생성위해 사용
		List<ErpIfPurchaseOrdLineRequestDto> preOrderList = batchGoodsPoInputMapper.getPoIFList(dto);	// 원본데이터

		// 2021.07.09 발주저장된 품목에 대해서만 전송후 재고 업데이트를 하여 저장안된 품목도 전체 재고 업데이트 하기위해 추가한 로직.
		dto.setGetPoAllYn("Y");			// 	발주내역 전체 조회
		List<ErpIfPurchaseOrdLineRequestDto> getPreOrderList = batchGoodsPoInputMapper.getPoIFList(dto);	// 발주전체데이터

		// 올가R1, R2발주시 이동발주 생성위해 사용
		List<ErpIfPurchaseOrdLineRequestDto> moveList = new ArrayList<>(preOrderList.size());

		// preOrderList -> moveList Deep Copy
		Iterator<ErpIfPurchaseOrdLineRequestDto> iterator = preOrderList.iterator();
		while(iterator.hasNext()){
			moveList.add(iterator.next().clone());
		}

		String ordNoDtl = "";

		for(int i=0; i < preOrderList.size(); i++) {

			ordNoDtl = oriSysSeq + "_" + preOrderList.get(i).getOrdNoDtl();
			preOrderList.get(i).setOrdNoDtl(ordNoDtl);
		}

		if(!preOrderList.isEmpty()){
			// R1, R2 PO발주 생성
			isSuccess = createErpSendPoList(preOrderList, oriSysSeq, transferServerType);
		}

		// [S]올가 용인 배치의 경우 이동 IF를 한번 더 호출.
		if (isSuccess){
			String moveOriSysReq = ""; 		// 이동발주 OriSysSeq

			String ordNoDtlSeq2 = getMoOrdNoDtl();		// Sequence 조회
			moveOriSysReq = "R6_"+ordNoDtlSeq2;			// OriSysSeq

			String ordNoDtl2 = "";

			// 올가 용인 이동 IF를 위한 추가 데이터 설정
			for(int i=0; i < moveList.size(); i++) {
				ordNoDtl2 = moveList.get(i).getOrdNoDtl();

				moveList.get(i).setOrdNoDtl(moveOriSysReq + "_" + ordNoDtl2);	// ORD_NO_DTL 재설정.
				moveList.get(i).setTrnCnt(moveList.get(i).getPoIfQty());        // 이동수량
				moveList.get(i).setOrdGub(Constants.OGMO);						// 이동발주(OGMO)로 설정
				moveList.get(i).setOriSysSeq(moveOriSysReq);					// 중복되지 않게 키값 재설정

				moveList.get(i).setReqCnt(moveList.get(i).getOrdCnt());			// 발주수량
				moveList.get(i).setOrdCnt(0);									// 주문수량   0
				moveList.get(i).setReqDat(moveList.get(i).getMoveReqDat());		// 발주일자	D-2
				moveList.get(i).setPrmDat(moveList.get(i).getReqDat());			// 출고예정일  D-2
				moveList.get(i).setDvlReqDat(moveList.get(i).getMoveReqDat());	// 이동요청일  D-1

				moveList.get(i).setFroOrgId(Constants.FRO_ORG_ID);				// (ERP 필요정보) 재고이동조직 7870
				moveList.get(i).setToOrgId(Constants.TO_ORG_ID);				// (ERP 필요정보) 재고보관조직 7870
				moveList.get(i).setToSubInvCd(Constants.TO_SUB_INV_CD);			// (ERP 필요정보) 재고보관창고 O1098
			}

			// 발주데이터 있는경우 이동발주 추가 생성
			if(!moveList.isEmpty()){
				isSuccess = createErpMovePoList(moveList, moveOriSysReq);
			}

		}

		// 결과 업데이트 처리
		if (isSuccess) {
			preOrderResultUpdate(preOrderList, poBatchTp);
			preOrderStockUpdate(getPreOrderList, poBatchTp);	// 발주여부와 상관없이 전체 품목 재고 업데이트
		}
	}


	/**
	 * @Desc ERP 재고 저장
	 * @param itemErpStockResultVo
	 * @return int
	 */
	protected long addErpStock(ItemErpStockResultVo itemErpStockResultVo) {
		return batchItemErpStockMapper.addErpStock(itemErpStockResultVo);
	}

	/**
	 * @Desc ERP 재고 History 저장
	 * @param ilItemErpStockIdd
	 * @return int
	 */
	protected int addItemErpStockHistory(long ilItemErpStockIdd) {
		return batchItemErpStockMapper.addItemErpStockHistory(ilItemErpStockIdd);
	}

	/**
	 * @Desc 품목 연동재고  > 품목 재고 저장(Procedure 호출)
	 * @param
	 * @return int
	 */
	protected int callSpItemStockCaculatedPrepare() {
		return batchItemErpStockMapper.spItemStockCaculatedPrepare();
	}


	/**
	 * @Desc 품목 연동재고  > 품목 재고 저장(SP_ITEM_STOCK_CACULATED Procedure 호출)
	 * @param
	 * @param ilItemWarehouseId
	 * @return int
	 */
	protected int callSpItemStockCaculated(long ilItemWarehouseId) {
		return itemErpStockCommonMapper.callSpItemStockCaculated(ilItemWarehouseId);
	}

	protected String getPoOrdNoDtl() { return batchGoodsPoInputMapper.getPoSeq(); }

	protected String getMoOrdNoDtl() { return batchGoodsPoInputMapper.getMoSeq(); }

	/**
	 * @Desc PO 발주 생성
	 *
	 * @param poList, oriSysReq, transferServerType
	 * @return successFlag
	 */
	protected boolean createErpSendPoList(List<ErpIfPurchaseOrdLineRequestDto> poList, String oriSysReq, String transferServerType){

		List<ErpIfPurchaseOrdHeaderRequestDto> erpIfPurchaseOrderHeaderList = new ArrayList<>();

		BaseApiResponseVo baseApiResponseVo;
		BaseApiResponseVo retryBaseApiResponseVo;
		ErpIfPurchaseOrdHeaderRequestDto purchaseOrderHeaderRequestDto;

		boolean successFlag = false;

		//Header DTO 생성
		if(TransferServerTypes.OGH.getCode().equals(transferServerType)){
			purchaseOrderHeaderRequestDto = ErpIfPurchaseOrdHeaderRequestDto.builder()
					.srcSvr(SourceServerTypes.ESHOP.getCode())
					.oriSysSeq(oriSysReq)
					.ordCls(ErpApiEnums.ErpOrderClass.TRANSFER_ORDER.getCode())					// TRANSFER ORDER
					.ordSrc(ErpApiEnums.ErpOrderType.NO_SHOPPING_MALL.getCodeName())			// 쇼핑몰
					.divCd("100")
					.ordCloGrpCd("ORGA")
					.ordDat(poList.get(0).getBaseDt())
					.line(poList)
					.build();
		} else {
			purchaseOrderHeaderRequestDto = ErpIfPurchaseOrdHeaderRequestDto.builder()
					.srcSvr(SourceServerTypes.ESHOP.getCode())
					.oriSysSeq(oriSysReq)
					.ordCls(ErpApiEnums.ErpOrderClass.TRANSFER_ORDER.getCode())
					.line(poList)
					.build();
		}

		erpIfPurchaseOrderHeaderList.add(purchaseOrderHeaderRequestDto);


		//Request DTO 생성
		ErpIfPurchaseOrdRequestDto purchaseOrderRequestDto = ErpIfPurchaseOrdRequestDto.builder()
				.totalPage(1)
				.currentPage(1)
				.header(erpIfPurchaseOrderHeaderList)
				.build();

		// baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
		baseApiResponseVo = erpApiExchangeService.post(purchaseOrderRequestDto, PURCHASE_ORDER_INSERT_INTERFACE_ID);

		log.info("addGoodsPurchaseOrderJob baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

		//ERP IF실패시 재시도 총 3회
		if (!baseApiResponseVo.isSuccess()) {

			for (int failCnt = 0; failCnt < BatchConstants.BATCH_FAIL_RETRY_COUNT; failCnt++ ) {
				retryBaseApiResponseVo = erpApiExchangeService.post(purchaseOrderRequestDto, PURCHASE_ORDER_INSERT_INTERFACE_ID);

				if (retryBaseApiResponseVo.isSuccess()) {
					successFlag = true;
					log.info("retryBaseApiResponseVo:" + ToStringBuilder.reflectionToString(retryBaseApiResponseVo));
					break;
				} else {
					//3번 재시도 모두 실패시 알람
				}
			}
		} else {
			successFlag = true;
		}

		//ERP IF된 구매발주 입력 IF여부
		erpIfPurchaseOrderHeaderList.clear();

		return successFlag;
	}

	/**
	 * @Desc 이동발주 생성
	 *
	 * @param poList, oriSysReq
	 * @return successFlag
	 */
	protected boolean createErpMovePoList(List<ErpIfPurchaseOrdLineRequestDto> poList, String oriSysReq) {

		boolean successFlag = false;

		ErpIfPurchaseOrdHeaderRequestDto erpMovePoHeaderRequestDto = ErpIfPurchaseOrdHeaderRequestDto.builder()
				.srcSvr(SourceServerTypes.ESHOP.getCode())
				.oriSysSeq(oriSysReq)
				.ordCls(ErpApiEnums.ErpOrderClass.TRANSFER_ORDER.getCode())
				.line(poList)
				.build();

		List<ErpIfPurchaseOrdHeaderRequestDto> erpMoveErpIfPoHeaderList = new ArrayList<>();
		erpMoveErpIfPoHeaderList.add(erpMovePoHeaderRequestDto);

		ErpIfPurchaseOrdRequestDto erpMovePoRequestDto = ErpIfPurchaseOrdRequestDto.builder()
				.totalPage(1)
				.currentPage(1)
				.header(erpMoveErpIfPoHeaderList)
				.build();

		BaseApiResponseVo erpMoveBaseApiResponseVo = erpApiExchangeService.post(erpMovePoRequestDto, PURCHASE_ORDER_INSERT_INTERFACE_ID);

		// ERP IF실패시 재시도 총 3회
		if (!erpMoveBaseApiResponseVo.isSuccess()) {
			for (int failCnt = 0; failCnt < BatchConstants.BATCH_FAIL_RETRY_COUNT; failCnt++) {
				BaseApiResponseVo erpMoveRetryBaseApiResponseVo =
						erpApiExchangeService.post(erpMovePoRequestDto, PURCHASE_ORDER_INSERT_INTERFACE_ID);
				if (erpMoveRetryBaseApiResponseVo.isSuccess()) {
					successFlag = true;
					log.info("erpMoveRetryBaseApiResponseVo:" + ToStringBuilder.reflectionToString(erpMoveRetryBaseApiResponseVo));
					break;
				} else {
					successFlag = false;
				}
			}
		} else {
			successFlag = true;
		}

		// ERP IF된 구매발주 입력 IF여부
		erpMoveErpIfPoHeaderList.clear();

		return successFlag;
	}

	/**
	 * @Desc 발주성공이후 발주이력 업데이트
	 *
	 * @param poList, poBatchTp
	 * @return
	 */
	protected void preOrderResultUpdate(List<ErpIfPurchaseOrdLineRequestDto> poList, String poBatchTp){
		ErpIfPurchaseOrdLineRequestDto vo = new ErpIfPurchaseOrdLineRequestDto();

		for(int i=0; i < poList.size(); i++) {

			// 배치 결과 업데이트
			vo.setPoSystemQty(poList.get(i).getPoSystemQty());
			vo.setPoIfYn("Y");
			vo.setPoBatchTp(poBatchTp);
			vo.setPoIfQty(poList.get(i).getPoIfQty());
			vo.setIlPoId(poList.get(i).getIlPoId());
			batchGoodsPoInputMapper.putItemPoIFResult(vo);
		}
	}

	/**
	 * @Desc 발주이후 재고 및 이력 업데이트
	 *
	 * @param poList, poBatchTp
	 * @return
	 */
	protected void preOrderStockUpdate(List<ErpIfPurchaseOrdLineRequestDto> poList, String poBatchTp){
		ErpIfPurchaseOrdLineRequestDto vo = new ErpIfPurchaseOrdLineRequestDto();

		for(int i=0; i < poList.size(); i++) {

			// [S]입고예정 재고 처리 로직 - 발주의 입고예정일을 기준으로 재고 테이블에 입고예정 정보 생성
			ItemErpStockResultVo itemErpStockVo = ItemErpStockResultVo.builder()
					.ilItemWarehouseId(poList.get(i).getIlItemWarehouseId()) // 품목출고처 PK
					.baseDate(poList.get(i).getStockScheduledDt()) // 기준일 : 입고예정일로 설정
					.stockType("ERP_STOCK_TP.SCHEDULED") // 재고 종류 : 입고예정
					.stockQuantity(poList.get(i).getPoIfQty()) // 입고예정 수량 : 발주수량으로 설정
					.scheduleDate(poList.get(i).getStockScheduledDt()) // 입고예정일
					.build();

			addErpStock(itemErpStockVo); //IL_ITEM_ERP_STOCK 테이블에 저장

			// [E]입고예정 재고 처리 로직 - 발주의 입고예정일을 기준으로 재고 테이블에 입고예정 정보 생성
			addItemErpStockHistory(itemErpStockVo.getIlItemErpStockId()); // IL_ITEM_ERP_STOCK_HISTORY 테이블에 저장

			// 재고 업데이트 위한 프로시저 호출 - 건별 호출.
			callSpItemStockCaculated(poList.get(i).getIlItemWarehouseId());
		}
		//callSpItemStockCaculatedPrepare(); // 재고 업데이트를 위해 SP_ITEM_STOCK_CACULATED_PREPARE 프로시저 호출
	}

	/**
	 * @Desc 발주용 기초데이터(평균 주문량) 생성
	 * @param baseDt
	 * @return int
	 */
	protected int addGoodsPoOrderCalculate(String baseDt) {
		return batchGoodsPoInputMapper.addGoodsPoOrderCalculate(baseDt);
	}
}
