package kr.co.pulmuone.v1.batch.goods.stock;

import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.base.service.StComnBiz;
import kr.co.pulmuone.v1.batch.send.template.service.SendTemplateBatchBiz;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddEmailIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.comm.api.constant.StockWarehouseTypes;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfStock3PlSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfStockSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 재고수량 조회 API 조회 후 BOS 재고 계산 배치 Biz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2020-10-15   박주형            최초작성
 *        2020-12-21   이성준            기능추가
 * =======================================================================
 * </PRE>
 */

@Service
public class BatchGoodsStockBizImpl implements BatchGoodsStockBiz {

    // ERP API 의 재고 수량 조회 인터페이스 ID
    private static final String STOCK_SEARCH_INTERFACE_ID = "IF_STOCK_SRCH";

    // ERP API 의 3PL 재고 수량 조회 인터페이스 ID
    private static final String STOCK_3PL_SEARCH_INTERFACE_ID = "IF_STOCK_3PL_SRCH";

    // runGoodsStockJob 으로 실행하는 해당 배치 ID
    private static final long BATCH_ID = 0;

    @Autowired
    StComnBiz stComnBiz;

    @Autowired
    private ErpApiExchangeService erpApiExchangeService;

    @Autowired
    private BatchGoodsStockService batchGoodsStockService;

    @Autowired
    private BatchGoodsStockReCalQtyService batchGoodsStockReCalQtyService;

    @Autowired
    private BatchGoodsStock3PlSearchService batchGoodsStock3PlSearchService;

    @Autowired
    private BatchGoodsStock3PlSearchBizImpl batchGoodsStock3PlSearchBizImpl;

    @Autowired
    private SendTemplateBatchBiz sendTemplateBatchBiz;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
    public void runGoodsStockDailyJob() throws BaseException {

    	/* 0. 3PL재고 조회(백암)
         * ERP 재고 Data 전체 조회 / BOS 상에 등록된 연동 품목인 경우 IL_ITEM_STOCK_EXPR, IL_ITEM_ERP_STOCK 테이블에 저장
         */
        List<ErpIfStock3PlSearchResponseDto> erpStock3PlSearchList = getErpStock3PlSearchList();
        batchGoodsStock3PlSearchService.runGoodsStock3PlSearchJob(erpStock3PlSearchList, BATCH_ID);

        /*
         * 각 출고처의 ERP 재고 Data 전체 조회 / BOS 상에 등록된 연동 품목인 경우 IL_ITEM_ERP_STOCK 테이블에 재고 유형별로 저장
         */
        LocalDate now = LocalDate.now();//현재일짜
        LocalDate agoDay = now.minusDays(1);//하루전 날짜
        String inpDat = agoDay.toString().replaceAll("-", ""); // 일 배치(전일마감재고)는 하루전 날짜로 데이터 조회

        // 1. 식품 용인물류 ( shiFroOrgId "802" )
        List<ErpIfStockSearchResponseDto> pffYongInErpStockSearchList = getErpStockSearchList(StockWarehouseTypes.PFF_YONGIN.getCode(), inpDat);
        batchGoodsStockService.runGoodsStockJobByShiFroOrgId(pffYongInErpStockSearchList, StockWarehouseTypes.PFF_YONGIN.getCode(), BATCH_ID);

        // 2. 식품 백암물류 ( shiFroOrgId "803" )
        List<ErpIfStockSearchResponseDto> pffBaegAmErpStockSearchList = getErpStockSearchList(StockWarehouseTypes.PFF_BAEGAM.getCode(), inpDat);
        batchGoodsStockService.runGoodsStockJobByShiFroOrgId(pffBaegAmErpStockSearchList, StockWarehouseTypes.PFF_BAEGAM.getCode(), BATCH_ID);

        // 3. 올가 용인물류 ( shiFroOrgId "O10" )
        List<ErpIfStockSearchResponseDto> orgaYongInErpStockSearchList = getErpStockSearchList(StockWarehouseTypes.ORGA_YONGIN.getCode(), inpDat);
        batchGoodsStockService.runGoodsStockJobByShiFroOrgId(orgaYongInErpStockSearchList, StockWarehouseTypes.ORGA_YONGIN.getCode(), BATCH_ID);

        // 4.재고수량 재계산
        batchGoodsStockReCalQtyService.runGoodsStockReCalQtyJob();

        // 5.엑셀업로드  계산(용인)
        batchGoodsStockService.runExcelUploadCal();

        // 6.SP_ITEM_STOCK_CACULATED_PREPARE 프로시저 호출
        batchGoodsStockService.spItemStockCaculatedPrepare();

        // 7. 3PL재고수량 조회 확인
        batchGoodsStock3PlSearchService.runGoodsStock3PlSearchFlagJob(erpStock3PlSearchList);

        // 8. 재고수량 조회 확인-식품 용인물류 ( shiFroOrgId "802" )
        batchGoodsStockService.runGoodsStockSearchFlagJob(pffYongInErpStockSearchList, StockWarehouseTypes.PFF_YONGIN.getCode(), BATCH_ID);

        // 9. 재고수량 조회 확인-식품 백암물류 ( shiFroOrgId "803" )
        batchGoodsStockService.runGoodsStockSearchFlagJob(pffBaegAmErpStockSearchList, StockWarehouseTypes.PFF_BAEGAM.getCode(), BATCH_ID);

        // 10. 재고수량 조회 확인-올가 용인물류 ( shiFroOrgId "O10" )
        batchGoodsStockService.runGoodsStockSearchFlagJob(orgaYongInErpStockSearchList, StockWarehouseTypes.ORGA_YONGIN.getCode(), BATCH_ID);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
    public void runGoodsStockHourlyJob() throws BaseException {

        /*
         * 올가 출고처의 ERP 재고 Data 전체 조회 / BOS 상에 등록된 연동 품목인 경우 IL_ITEM_ERP_STOCK 테이블에 재고 유형별로 저장
         */
        LocalDate now = LocalDate.now();//현재일짜
        String inpDat = now.toString().replaceAll("-", ""); // 올가 배치(입고확정, 오프라인 재고)는 오늘 날짜로 데이터 조회
        // 1. 올가 용인물류 ( shiFroOrgId "O10" )
        List<ErpIfStockSearchResponseDto> orgaYongInErpStockSearchList = getErpStockSearchList(StockWarehouseTypes.ORGA_YONGIN.getCode(), inpDat);
        batchGoodsStockService.runOrgaGoodsStockJobByShiFroOrgId(orgaYongInErpStockSearchList, StockWarehouseTypes.ORGA_YONGIN.getCode(), BATCH_ID);

        //SP_ITEM_STOCK_CACULATED_PREPARE 프로시저 호출
        batchGoodsStockService.spItemStockCaculatedPrepare();

    }

    /**
     * 유통기한별 재고 리스트 메일 시스템
     * @throws BaseException
     */
    @Override
    public void runGoodsStockDisposalDailyJob() throws BaseException {
        //메일 전송 대상자
        List<String> fullEmployeeList = new ArrayList<>();

        GetCodeListRequestDto convertRequestToObject = new GetCodeListRequestDto();
        convertRequestToObject.setStCommonCodeMasterCode("STOCK_DISPOSAL_MAIL_LIST");
        convertRequestToObject.setUseYn("Y");

        ApiResult<?> apiResult = stComnBiz.getCodeList(convertRequestToObject);

        GetCodeListResponseDto codeResponseDto = (GetCodeListResponseDto) apiResult.getData();
        List<GetCodeListResultVo> codeListResultVoList = codeResponseDto.getRows();

        codeListResultVoList.forEach(e -> fullEmployeeList.add(e.getAttr1()));

        String content = sendTemplateBatchBiz.getDomainManagement() + "/admin/system/emailtmplt/goodsStockDisposalAlarm";
        String title = "임박/폐기 예정품목 안내";
        String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
        String senderName = sendTemplateBatchBiz.getPsValue("SEND_EMAIL_SENDER");
        String senderMail = sendTemplateBatchBiz.getPsValue("SEND_EMAIL_ADDRESS");

        AddEmailIssueSelectBatchRequestDto addEmailIssueSelectBatchRequestDto = AddEmailIssueSelectBatchRequestDto.builder()
                .senderName(senderName) // SEND_EMAIL_SENDER
                .senderMail(senderMail) // SEND_EMAIL_ADDRESS
                .reserveYn(reserveYn)
                .content(content)
                .title(title)
                .urUserId("0")
                .build();

        fullEmployeeList.forEach(e -> {
            addEmailIssueSelectBatchRequestDto.setMail(e);
            sendTemplateBatchBiz.addEmailIssueSelect(addEmailIssueSelectBatchRequestDto);
        });
    }



    /*
     * ERP 재고 조회 API 의 해당 shiFroOrgId 에 대응되는 출고처의 전체 재고 Data 조회
     *
     * @param String shiFroOrgId : ERP 재고조회 API 의 shiFroOrgId 에서 사용하는 출고처 ID ( StockWarehouseTypes enum 참조 )
     *
     * @return List<ErpIfStockSearchResponseDto> totalErpStockSearchList : 해당 shiFroOrgId 로 ERP 재고조회 API 를 통해 수집한 재고 Data
     */
    private List<ErpIfStockSearchResponseDto> getErpStockSearchList(String shiFroOrgId, String inpDat) throws BaseException {

        List<ErpIfStockSearchResponseDto> totalErpStockSearchList = new ArrayList<>(); // 재고 Data 전체 취합할 ArrayList

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put(StockWarehouseTypes.CODE_KEY, shiFroOrgId); // 해당 shiFroOrgId 출고처 ID 를 API 검색조건에 추가
        parameterMap.put("inpDat", inpDat);
        parameterMap.put("_order", "header-ifSeq-asc"); // 조회시 정렬 조건 추가

        BaseApiResponseVo baseApiResponseVo = null;
        List<ErpIfStockSearchResponseDto> eachPageList = null;

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, STOCK_SEARCH_INTERFACE_ID);

            // baseApiResponseVo => List<T> 역직렬화
            eachPageList = baseApiResponseVo.deserialize(ErpIfStockSearchResponseDto.class);

        } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
            throw new BaseException(e.getMessage());
        }

        totalErpStockSearchList.addAll(eachPageList);

        int startPage = baseApiResponseVo.getCurrentPage(); // 최초 조회한 페이지 ( 1 페이지 )
        int totalPage = baseApiResponseVo.getTotalPage(); // 해당 검색조건으로 조회시 전체 페이지 수

        if (totalPage > 1) {

            // 최초 조회한 페이지의 다음 페이지부터 조회
            for (int page = startPage + 1; page <= totalPage; page++) {

                parameterMap.put("page", String.valueOf(page));

                try {

                    // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
                	baseApiResponseVo = null; // 초기화
                	baseApiResponseVo = erpApiExchangeService.get(parameterMap, STOCK_SEARCH_INTERFACE_ID);

                    // baseApiResponseVo => List<T> 역직렬화
                    eachPageList = null; // 초기화
                    eachPageList = baseApiResponseVo.deserialize(ErpIfStockSearchResponseDto.class);

                } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
                    throw new BaseException(e.getMessage());
                }

                totalErpStockSearchList.addAll(eachPageList);

            }

        }

        return totalErpStockSearchList;

    }

    /*
     * ERP 재고 조회 API 출고처의 전체 재고 Data 조회
     *
     * @return List<ErpIfStock3PlSearchResponseDto> totalErpStock3PlSearchList : ERP 3PL재고조회 API를 통해 수집한 재고 Data
     */
	private List<ErpIfStock3PlSearchResponseDto> getErpStock3PlSearchList() throws BaseException {
		List<ErpIfStock3PlSearchResponseDto> totalErpStock3PlSearchList = new ArrayList<>(); // 재고 Data 전체 취합할 ArrayList

		LocalDate now = LocalDate.now();//현재일짜
        LocalDate agoDay = now.minusDays(1);//하루전 날짜

        String inpDat = agoDay.toString().replaceAll("-", "");

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("inpDat", inpDat); //하루전 기준으로 조회 (API 검색조건에 추가)

        BaseApiResponseVo baseApiResponseVo = null;
        List<ErpIfStock3PlSearchResponseDto> eachPageList = null;

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, STOCK_3PL_SEARCH_INTERFACE_ID);

            // baseApiResponseVo => List<T> 역직렬화
            eachPageList = baseApiResponseVo.deserialize(ErpIfStock3PlSearchResponseDto.class);

        } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
            throw new BaseException(e.getMessage());
        }

        totalErpStock3PlSearchList.addAll(eachPageList);

        int startPage = baseApiResponseVo.getCurrentPage(); // 최초 조회한 페이지 ( 1 페이지 )
        int totalPage = baseApiResponseVo.getTotalPage(); // 해당 검색조건으로 조회시 전체 페이지 수

        if (totalPage > 1) {

            // 최초 조회한 페이지의 다음 페이지부터 조회
            for (int page = startPage + 1; page <= totalPage; page++) {

                parameterMap.put("page", String.valueOf(page));

                try {

                    // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, STOCK_3PL_SEARCH_INTERFACE_ID);

                    // baseApiResponseVo => List<T> 역직렬화
                    eachPageList = baseApiResponseVo.deserialize(ErpIfStock3PlSearchResponseDto.class);

                } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
                    throw new BaseException(e.getMessage());
                }

                totalErpStock3PlSearchList.addAll(eachPageList);

            }

        }

        return totalErpStock3PlSearchList;
	}

}
