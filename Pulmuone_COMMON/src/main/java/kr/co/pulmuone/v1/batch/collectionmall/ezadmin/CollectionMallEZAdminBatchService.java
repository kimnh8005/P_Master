package kr.co.pulmuone.v1.batch.collectionmall.ezadmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.*;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.excel.validate.OrderRowItemValidator;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.collectionmall.CollectionMallEZAdminBatchMapper;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.RestTemplateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.outmall.sellers.service.SellersBiz;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeListDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.vo.NonDeliveryAreaInfoVo;
import kr.co.pulmuone.v1.policy.shiparea.service.PolicyShipareaBiz;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.store.delivery.dto.WarehouseUnDeliveryableInfoDto;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollectionMallEZAdminBatchService {

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    private final CollectionMallEZAdminBatchMapper collectionMallEZAdminBatchMapper;

    private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

    @Autowired
    private StoreWarehouseBiz storeWarehouseBiz;

    @Autowired
    private SellersBiz sellersBiz;

    @Value("${ezadmin.tracking.scheme}")
    private String trackingScheme;

    @Value("${ezadmin.tracking.host}")
    private String trackingHost;

    @Value("${ezadmin.tracking.path}")
    private String trackingPath;

    @Value("${ezadmin.partner-key}")
    private String partnerKey;

    @Value("${ezadmin.domain-key}")
    private String domainKey;
    
    private int EZADMIN_QNA_BATCH_MINUTE = 10; //이지어드민 문의글 수집배치 시간(분)

    @Autowired
    private OrderEmailSendBiz orderEmailSendBiz;

    @Autowired
    private OrderEmailBiz orderEmailBiz;

    @Autowired
    private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

    @Autowired
    private SendTemplateBiz sendTemplateBiz;

    @Autowired
    private OrderRowItemValidator orderRowItemValidator;

    /**
     * @Desc EZ Admin 조회
     * @param paramMap
     * @param dataType
     *
     * @return ApiResult
     * \
     *
     */
    protected <T> ApiResult<?> get(MultiValueMap<String, String> paramMap, Class<T> dataType) {
    	EZAdminResponseDefaultDto resDto = null;
        final String RESPONSE_CODE = "error";
        final String RESPONSE_MSG = "msg";
        final String RESPONSE_DATA = "data";

        paramMap.add("partner_key", partnerKey);
        paramMap.add("domain_key", domainKey);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.newInstance()
                                                              .scheme(trackingScheme)
                                                              .host(trackingHost)
                                                              .path(trackingPath)
                                                              .queryParams(paramMap);

        ResponseEntity<String> responseEntity = restTemplateUtil.get(urlBuilder, requestEntity, String.class);

        // 통신 결과 체크
        if( responseEntity.getStatusCode() != HttpStatus.OK ) {
            return ApiResult.result(ApiEnums.EzAdminStatus.HTTP_STATUS_FAIL);
        }

        String responseBody = responseEntity.getBody();
        log.info("responseBody={}", responseBody);

        // responseBody null 체크
        if( StringUtils.isEmpty(responseBody) ) {
            return ApiResult.result(responseBody, ApiEnums.EzAdminStatus.RESPONSEBODY_NO_DATA);
        }

        JsonNode jsonNode;

        try {
            jsonNode = OBJECT_MAPPER.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ApiResult.result(responseBody, ApiEnums.EzAdminStatus.JSON_PARSING_ERROR);
        }

        // 이지어드민 송장조회 API일 경우, response data 없어서 msg가 success면 성공처리 & responeBody return
        if(ApiEnums.EZAdminApiAction.SET_TRANS_NO.getCode().equals(paramMap.get("action").get(0))){

            if(jsonNode.get("msg").asText().equals("success") && jsonNode.get("error").asInt() == 0){
                return ApiResult.result(responseBody, BaseEnums.Default.SUCCESS);
            }else{
                return ApiResult.result(responseBody, ApiEnums.EzAdminStatus.RESPONSE_FAIL);
            }
        }


        // API 결과 체크
        if(jsonNode.get(RESPONSE_CODE) == null || !jsonNode.get(RESPONSE_CODE).toString().equals("0")) {
        	return ApiResult.result(responseBody, ApiEnums.EzAdminStatus.RESPONSE_FAIL);
        }

        if(StringUtils.isEmpty( jsonNode.get(RESPONSE_MSG).asText() ) ) {
        	return ApiResult.result(responseBody, ApiEnums.EzAdminStatus.RESPONSE_FAIL);
        }
        if(jsonNode.get(RESPONSE_DATA) == null
        		|| jsonNode.get(RESPONSE_DATA).isEmpty()
        		|| jsonNode.get(RESPONSE_DATA).toString().isEmpty()
        		) {
        	return ApiResult.result(responseBody, ApiEnums.EzAdminStatus.RESPONSEBODY_NO_DATA);
        }

        String jsonData = jsonNode.get(RESPONSE_DATA).toString();

        log.info("jsonData={}", jsonData);
        // DATA 유무 체크
        if( StringUtils.isEmpty(jsonData)){
            return ApiResult.result(responseBody, ApiEnums.EzAdminStatus.NO_DATA);
        }

        resDto = OBJECT_MAPPER.convertValue(jsonNode, EZAdminResponseDefaultDto.class);

        resDto.setResponseData(responseBody);

        List<T> dataList = null;

        if(dataType != null) {
        	JavaType listType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, dataType);
        	try {
        		dataList = OBJECT_MAPPER.readValue(jsonData, listType);
        		resDto.setData(dataList);
        	} catch (JsonProcessingException e) {
        		log.error(e.getMessage());
        		return ApiResult.result(responseBody, ApiEnums.EzAdminStatus.JSON_PARSING_ERROR);
        	}
        }

        return ApiResult.result(resDto, BaseEnums.Default.SUCCESS);
    }

    /**
     * @Desc EZ Admin 처리
     * @param paramMap
     * @return
     * @return ApiResult
     */
    protected ApiResult<?> set(MultiValueMap<String, String> paramMap) {
    	EZAdminResponseDefaultDto resDto = null;
        final String RESPONSE_CODE = "error";
        final String RESPONSE_MSG = "msg";

        paramMap.add("partner_key", partnerKey);
        paramMap.add("domain_key", domainKey);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.newInstance()
                                                              .scheme(trackingScheme)
                                                              .host(trackingHost)
                                                              .path(trackingPath)
                                                              .queryParams(paramMap);

        ResponseEntity<String> responseEntity = restTemplateUtil.get(urlBuilder, requestEntity, String.class);

        // 통신 결과 체크
        if( responseEntity.getStatusCode() != HttpStatus.OK ) {
            return ApiResult.result(ApiEnums.EzAdminStatus.HTTP_STATUS_FAIL);
        }

        String responseBody = responseEntity.getBody();
        log.info("responseBody={}", responseBody);

        // responseBody null 체크
        if( StringUtils.isEmpty(responseBody) ) {
            return ApiResult.result(ApiEnums.EzAdminStatus.RESPONSEBODY_NO_DATA);
        }

        JsonNode jsonNode;

        try {
            jsonNode = OBJECT_MAPPER.readTree(responseBody);
        } catch (JsonProcessingException e) {
        	e.printStackTrace();
            return ApiResult.result(ApiEnums.EzAdminStatus.JSON_PARSING_ERROR);
        }

        // API 결과 체크
        if(jsonNode.get(RESPONSE_CODE) == null || !jsonNode.get(RESPONSE_CODE).toString().equals("0")) {
        	return ApiResult.result(ApiEnums.EzAdminStatus.RESPONSE_FAIL);
        }

//        if( StringUtils.isEmpty( jsonNode.get(RESPONSE_CODE).asText() ) ) {
//        	return ApiResult.result(ApiEnums.EzAdminStatus.NO_DATA);
//        }

        if(StringUtils.isEmpty( jsonNode.get(RESPONSE_MSG).asText() ) ) {
        	return ApiResult.result(ApiEnums.EzAdminStatus.RESPONSE_FAIL);
        }

        resDto = OBJECT_MAPPER.convertValue(jsonNode, EZAdminResponseDefaultDto.class);
        log.info("resDto={}", resDto);

        return ApiResult.result(resDto, BaseEnums.Default.SUCCESS);
    }


    /**
     * @Desc 이지어드민 정보 추가
     * @param ezadminResponseDefaultDto
     */
    protected int addEasyAdminInfo(EZAdminResponseDefaultDto ezadminResponseDefaultDto){
    	return collectionMallEZAdminBatchMapper.addEasyAdminInfo(ezadminResponseDefaultDto);
    }

    /**
     * @Desc 이지어드민 정보 추가
     * @param ezadminResponseDefaultDto
     */
    protected int addEasyAdminInfoReqData(EZAdminResponseDefaultDto ezadminResponseDefaultDto){
        return collectionMallEZAdminBatchMapper.addEasyAdminInfoReqData(ezadminResponseDefaultDto);
    }

    /**
     * @Desc 이지어드민 주문 성공 정보 추가
     * @param ezadminOrderVo
     */
    protected int addEasyAdminOrderSuccess(EZAdminOrderInfoOrderVo ezadminOrderVo) throws Exception{
    	return collectionMallEZAdminBatchMapper.addEasyAdminOrderSuccess(ezadminOrderVo);
    }

    /**
     * @Desc 이지어드민 주문 성공 정보 상세 추가
     * @param ezadminProductVo
     */
    protected int addEasyAdminOrderSuccessDetail(EZAdminOrderInfoOrderProductVo ezadminProductVo) throws Exception{
    	return collectionMallEZAdminBatchMapper.addEasyAdminOrderSuccessDetail(ezadminProductVo);
    }

    /**
     * @Desc 이지어드민 주문 실패 정보 추가
     * @param ezadminOrderVo
     */
    protected int addEasyAdminOrderFail(EZAdminOrderInfoOrderVo ezadminOrderVo) throws Exception{
    	return collectionMallEZAdminBatchMapper.addEasyAdminOrderFail(ezadminOrderVo);
    }

    /**
     * @Desc 이지어드민 주문 실패 정보 상세 추가
     * @param ezadminProductVo
     */
    protected int addEasyAdminOrderFailDetail(EZAdminOrderInfoOrderProductVo ezadminProductVo) throws Exception{
    	return collectionMallEZAdminBatchMapper.addEasyAdminOrderFailDetail(ezadminProductVo);
    }

    /**
     * @Desc 이지어드민 주문 클레임 정보 추가
     * @param ezadminOrderVo
     */
    protected int addEasyAdminOrderClaim(EZAdminOrderInfoOrderVo ezadminOrderVo) throws Exception{
    	return collectionMallEZAdminBatchMapper.addEasyAdminOrderClaim(ezadminOrderVo);
    }

    /**
     * @Desc 이지어드민 주문 클레임 정보 상세 추가
     * @param ezadminProductVo
     */
    protected int addEasyAdminOrderClaimDetail(EZAdminOrderInfoOrderProductVo ezadminProductVo) throws Exception{
    	return collectionMallEZAdminBatchMapper.addEasyAdminOrderClaimDetail(ezadminProductVo);
    }

    /**
     * 판매처PK 조회
     * @param orderInfoList
     * @return
     * @throws Exception
     */
    protected List<EZAdminSellersInfoDto> getOmSellesInfoList(List<EZAdminOrderInfoOrderVo> orderInfoList) throws Exception {
        List<EZAdminSellersInfoDto> omSellersInfoList = collectionMallEZAdminBatchMapper.getOmSellesInfoList(orderInfoList);

        if(CollectionUtils.isNotEmpty(omSellersInfoList)){
            for(EZAdminSellersInfoDto sellerDto : omSellersInfoList){
                List<OmBasicFeeListDto> supplierList = sellersBiz.getApplyOmBasicFeeList(sellerDto.getOmSellersId());
                sellerDto.setSupplierList(supplierList);
            }
        }

    	return omSellersInfoList;
    }

    /**
     * 이지어드민 정보 처리상태 업데이트
     * @param ezadminInfoDto
     * @return
     * @throws Exception
     */
    protected int putEasyAdminInfo(EZAdminInfoDto ezadminInfoDto) throws Exception {
    	return collectionMallEZAdminBatchMapper.putEasyAdminInfo(ezadminInfoDto);
    }

    /**
     * 이지어드민 입력 데이터 가공
     * @param orderInfo
     * @throws Exception
     */
    protected void validEZAdminOrderInfo(EZAdminOrderInfoOrderVo orderInfo) throws Exception {
		// 합포번호 확인
		if(orderInfo.getPack() < 1) {
			orderInfo.setPack(orderInfo.getSeq());
		}

		// 주소 확인
		String address = orderInfo.getRecv_address();
		orderInfo.setRecv_address(ExcelUploadUtil.splitAdress(address, 1));
		orderInfo.setRecv_address2(ExcelUploadUtil.splitAdress(address, 2));

		// 우편번호 확인
        orderInfo.setRecv_zip(orderInfo.getRecv_zip().replaceAll("-", ""));

		// 주문자 확인
		orderInfo.setOrder_name(StringUtil.nvl(orderInfo.getOrder_name(), orderInfo.getRecv_name()));

		// 주문자 전화번호 확인
		orderInfo.setOrder_tel(ExcelUploadUtil.defaultPhoneNumber(orderInfo.getOrder_tel(), orderInfo.getRecv_tel()));

		// 주문자 휴대폰 확인
		orderInfo.setOrder_mobile(ExcelUploadUtil.defaultPhoneNumber(orderInfo.getOrder_mobile(), orderInfo.getRecv_mobile(), orderInfo.getOrder_tel()));
		
		// 수령인 전화번호 확인
        orderInfo.setRecv_tel(ExcelUploadUtil.defaultPhoneNumber(orderInfo.getRecv_tel(), orderInfo.getRecv_mobile(),orderInfo.getOrder_mobile()));

        // 수령인 휴대폰 확인
        orderInfo.setRecv_mobile(ExcelUploadUtil.defaultPhoneNumber(orderInfo.getRecv_mobile(), orderInfo.getRecv_tel(),orderInfo.getOrder_mobile()));
    }

    /**
     * 상품 ID 목록 얻기
     * @param orderDetlList
     * @return
     */
    protected List<String> getGoodsIdList(List<EZAdminOrderInfoOrderProductVo> orderDetlList) {
    	return orderDetlList.stream()
							.map(EZAdminOrderInfoOrderProductVo::getBrand)
					        .filter(Objects::nonNull)
					        .distinct()
					        .collect(Collectors.toList());
    }

    /**
     * 상품 유효성 체크
     * @param productVo
     * @param goodsIdMap
     * @return
     * @throws Exception
     */
    protected EZAdminGoodsVaildResponseDto getGoodsItemValidateEZAdmin(EZAdminOrderInfoOrderProductVo productVo, Map<Long, GoodsSearchOutMallVo> goodsIdMap, EZAdminOrderInfoOrderVo orderInfo, List<EZAdminSellersInfoDto> sellersList) throws Exception {

    	EZAdminGoodsVaildResponseDto ezadminGoodsVaildResponseDto = new EZAdminGoodsVaildResponseDto();
    	ezadminGoodsVaildResponseDto.setStatus(EZAdminEnums.EZAdminGoodsValidMessage.SUCCESS.getCode());


    	System.out.println("productVo.getBrand() : " + productVo.getBrand().trim());
    	System.out.println("goodsIdMap : " + goodsIdMap);

		// 상품 정보가 존재 하지 않을 경우 FAIL
		GoodsSearchOutMallVo goodsSearchOutMall = goodsIdMap.get(Long.parseLong(productVo.getBrand().trim()));

        if (goodsSearchOutMall == null){
            boolean checkSearchFlag = false;
            for(long key : goodsIdMap.keySet()) {
                goodsSearchOutMall = goodsIdMap.get(key);
                if (productVo.getBrand().equals(Long.toString(goodsSearchOutMall.getGoodsNo()))){
                    productVo.setBrand(Long.toString(goodsSearchOutMall.getGoodsId()));
                    checkSearchFlag = true;
                    break;
                }
            }
            if (checkSearchFlag == false) {
                ezadminGoodsVaildResponseDto.setStatus(EZAdminEnums.EZAdminGoodsValidMessage.REGIST_GOODS_NONE.getCode());
                ezadminGoodsVaildResponseDto.setMessage(EZAdminEnums.EZAdminGoodsValidMessage.REGIST_GOODS_NONE.getMessage());
                return ezadminGoodsVaildResponseDto;
            }
        }

        // 상품이 판매중 상태가 아닐 경우
        if (       !GoodsEnums.SaleStatus.WAIT.getCode().equals(goodsSearchOutMall.getSaleStatus())
                && !GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goodsSearchOutMall.getSaleStatus())
                && !GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode().equals(goodsSearchOutMall.getSaleStatus())
        ) {
            ezadminGoodsVaildResponseDto.setStatus(EZAdminEnums.EZAdminGoodsValidMessage.GOODS_STATUS_ERROR.getCode());
            ezadminGoodsVaildResponseDto.setMessage(EZAdminEnums.EZAdminGoodsValidMessage.GOODS_STATUS_ERROR.getMessage());
            return ezadminGoodsVaildResponseDto;
        }

		try {

		    // 배송불가지역 체크
            boolean isUndeliveableArea = orderRowItemValidator.isUndeliverableArea(Long.parseLong(goodsSearchOutMall.getUrWarehouseId()), goodsSearchOutMall.getGoodsId(), orderInfo.getRecv_zip(), goodsSearchOutMall.getUndeliverableAreaTp());

            if(isUndeliveableArea){
                ezadminGoodsVaildResponseDto.setStatus(EZAdminEnums.EZAdminGoodsValidMessage.WAREHOUSE_UNDELIVERABLE_AREA.getCode());
                ezadminGoodsVaildResponseDto.setMessage(EZAdminEnums.EZAdminGoodsValidMessage.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                return ezadminGoodsVaildResponseDto;
            }

            // 판매처에 등록된 공급업체의 상품인지 확인
            Long omSellersId = orderInfo.getOmSellersId();                     // 판매처
            long goodsUrSupplierId = goodsSearchOutMall.getUrSupplierId();     // 상품의 공급업체
            for(EZAdminSellersInfoDto sellerDto : sellersList){
                if(omSellersId.equals(sellerDto.getOmSellersId())){

                    boolean isExistSupplierGoods = sellerDto.getSupplierList().stream().anyMatch(f->f.getUrSupplierId() == goodsUrSupplierId);
                    if(!isExistSupplierGoods){
                        ezadminGoodsVaildResponseDto.setStatus(EZAdminEnums.EZAdminGoodsValidMessage.NOT_REGISTRATION_GOODS_IN_SELLER.getCode());
                        ezadminGoodsVaildResponseDto.setMessage(EZAdminEnums.EZAdminGoodsValidMessage.NOT_REGISTRATION_GOODS_IN_SELLER.getMessage());
                        return ezadminGoodsVaildResponseDto;
                    }
                }
            }

			if(StringUtil.nvlLong(goodsSearchOutMall.getUrWarehouseId()) > 0) {
				List<List<ArrivalScheduledDateDto>> groupStockList = new ArrayList<>();
				List<ArrivalScheduledDateDto> stockList = goodsGoodsBiz.getArrivalScheduledDateDtoList(StringUtil.nvlLong(goodsSearchOutMall.getUrWarehouseId()), StringUtil.nvlLong(productVo.getBrand()), false, StringUtil.nvlInt(productVo.getQty()), null);
				groupStockList.add(stockList);

				LocalDate firstDt = LocalDate.now();
				List<LocalDate> allDate = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(groupStockList);
				if (!allDate.isEmpty()) {
					firstDt = allDate.get(0);
				}
				ArrivalScheduledDateDto arrivalScheduledDateDto = goodsGoodsBiz.getArrivalScheduledDateDtoByArrivalScheduledDate(stockList, firstDt);

				if(arrivalScheduledDateDto == null || Objects.isNull(arrivalScheduledDateDto.getOrderDate())) {
					ezadminGoodsVaildResponseDto.setStatus(EZAdminEnums.EZAdminGoodsValidMessage.ORDER_IF_DATE_NONE.getCode());
					ezadminGoodsVaildResponseDto.setMessage(EZAdminEnums.EZAdminGoodsValidMessage.ORDER_IF_DATE_NONE.getMessage());
					return ezadminGoodsVaildResponseDto;
				}
			}
			else {
				ezadminGoodsVaildResponseDto.setStatus(EZAdminEnums.EZAdminGoodsValidMessage.WAREHOUSE_ID_NONE.getCode());
				ezadminGoodsVaildResponseDto.setMessage(EZAdminEnums.EZAdminGoodsValidMessage.WAREHOUSE_ID_NONE.getMessage());
				return ezadminGoodsVaildResponseDto;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

		return ezadminGoodsVaildResponseDto;
    }

    public int addEasyAdminOrderRatioPrice(long ifEasyadminInfoId, long ifEasyadminInfoReqDataId) {
        return collectionMallEZAdminBatchMapper.addEasyAdminOrderRatioPrice(ifEasyadminInfoId, ifEasyadminInfoReqDataId);
    }

    public int putEasyAdminOrderRatioPrice(long ifEasyadminInfoId, List<Long> succIdList) {
        return collectionMallEZAdminBatchMapper.putEasyAdminOrderRatioPrice(ifEasyadminInfoId, succIdList);
    }

    public int putEasyAdminOrderSuccRatioPrice(long ifEasyadminInfoId, List<Long> succIdList) {
        return collectionMallEZAdminBatchMapper.putEasyAdminOrderSuccRatioPrice(ifEasyadminInfoId, succIdList);
    }

    protected int getOrderCnt(String collectionMallId){
        return collectionMallEZAdminBatchMapper.getOrderCnt(collectionMallId);
    }

    protected int getOrderDetailCnt(String collectionMallDetailId){  // prdSeq = collectionMallDetailId
        return collectionMallEZAdminBatchMapper.getOrderDetailCnt(collectionMallDetailId);
    }

    /**
     * 이지어드민 송장입력 API 호출 대상 주문 정보 조회
     * @param startDIDate
     * @param endDIDate
     * @return List<EZAdminTransNoTargetVo>
     */
    protected List<EZAdminTransNoTargetVo> getTransNoTargetList(@Param("startDIDate")String startDIDate, @Param("endDIDate")String endDIDate){
        return collectionMallEZAdminBatchMapper.getTransNoTargetList(startDIDate, endDIDate);
    }

    /**
     * @Desc 이지어드민 문의글 정보 추가
     * @param ezadminResponseDefaultDto
     */
    protected int addEasyAdminQnaInfo(EZAdminResponseDefaultDto ezadminResponseDefaultDto){
    	return collectionMallEZAdminBatchMapper.addEasyAdminQnaInfo(ezadminResponseDefaultDto);
    }
    
    /**
     * @Desc 이지어드민 문의글 정보 상세 추가
     * @param qnaInfo
     */
    protected int addCsOutmallQnaEasyAdminQnaDetail(EZAdminQnaInfoVo qnaInfo) throws Exception{
    	return collectionMallEZAdminBatchMapper.addCsOutmallQnaEasyAdminQnaDetail(qnaInfo);
    }
    
    /**
     * @Desc 이지어드민 문의글 답변 상세 추가
     * @param qnaInfo
     */
    protected int addCsOutmallQnaEasyAdminQnaAnswerDetail(EZAdminQnaInfoVo qnaInfo) throws Exception{
    	return collectionMallEZAdminBatchMapper.addCsOutmallQnaEasyAdminQnaAnswerDetail(qnaInfo);
    }

    /**
     * @Desc 이지어드민 주문조회 API 성공 후 주문 성공,실패 건수 업데이트
     */
    protected int putSuccessInsertOrderCount(long ifEasyadminInfoId){
        return collectionMallEZAdminBatchMapper.putSuccessInsertOrderCount(ifEasyadminInfoId);
    }

    /**
     *  이지어드민 주문조회 API후 수집성공여부 업데이트
     */
    protected int putIfEasyadminInfoReqDateCollectCd(long ifEasyadminInfoId){
        return collectionMallEZAdminBatchMapper.putIfEasyadminInfoReqDateCollectCd(ifEasyadminInfoId);
    }

    protected EZAdminRunOrderInfoRequestDto calcBatchDateTime(String action, String batchTp) {
        LocalDateTime startDateTime = LocalDateTime.now().minusMinutes(35);
        LocalDateTime endDateTime = LocalDateTime.now().minusMinutes(5).truncatedTo(ChronoUnit.MINUTES);

        String batchEndTime = collectionMallEZAdminBatchMapper.getEasyAdminInfo(action, batchTp);
        if (!StringUtil.isEmpty(batchEndTime)) {
            startDateTime = LocalDateTime.parse(batchEndTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).plusSeconds(1);
        }

        return EZAdminRunOrderInfoRequestDto.builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }

    protected EZAdminRunOrderInfoRequestDto calcQnaBatchDateTime(String ezadminBatchTypeCd) {
        LocalDateTime startDateTime = LocalDateTime.now().minusMinutes(EZADMIN_QNA_BATCH_MINUTE * 2);
        LocalDateTime endDateTime = LocalDateTime.now();

        String batchEndTime = collectionMallEZAdminBatchMapper.getEasyAdminQnaInfo(ezadminBatchTypeCd);
        if (!StringUtil.isEmpty(batchEndTime)) {
            startDateTime = LocalDateTime.parse(batchEndTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).minusMinutes(EZADMIN_QNA_BATCH_MINUTE * 2);
        }

        return EZAdminRunOrderInfoRequestDto.builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }

    /**
     * @Desc 이지어드민 주문조회 API 호출
     * @param reqDto
     * @return EZAdminOrderInfoResponseDto
     */
    protected EZAdminQnaInfoResponseDto callGetQnaInfo(EZAdminQnaInfoRequestDto reqDto) throws Exception{

    	log.info("===========EZAdmin callGetQnaInfo() CALL===============");

		EZAdminQnaInfoResponseDto responseDto = new EZAdminQnaInfoResponseDto();
    	MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
    	List<EZAdminQnaInfoVo> qnaInfoList = new ArrayList<>();
    	ApiResult<?> result = null;

    	String endFlag = "N";
    	long ifEasyadminInfoId = 0L;
		int failCount = 0; // API 호출 실패 count
    	int page = 1;
		int total = 0;
		int totalPage = 1;
		int limit = reqDto.getLimit();
    	 do {
         	reqDto.setPage(page);



 	    	ObjectMapper objectMapper = new ObjectMapper();
 	    	Map<String, String> map = objectMapper.convertValue(reqDto, new TypeReference<Map<String, String>>(){});
 	    	paramMap.setAll(map);

 	    	log.info("===========EZAdmin callGetQnaInfo() START=============== Page " + page);

 	    	// 이지어드민 문의글 API 호출
 	    	result = get(paramMap, EZAdminQnaInfoVo.class);

 	    	if(result.getCode().equals(ApiResult.success().getCode())) {

 	    		log.info("===========EZAdmin callGetQnaInfo() SUCCESS===============");

 	    		EZAdminResponseDefaultDto responseDefaultDto = (EZAdminResponseDefaultDto)result.getData();
				total		= Integer.parseInt(responseDefaultDto.getTotal());
				totalPage	= total / limit;


				if(total > (limit*page)) totalPage += 1;

				if (page == 1) {
					// CS_OUTMALL_QNA_EASYADMIN_INFO 이지어드민 정보 table insert
					responseDefaultDto.setAction(reqDto.getAction());
					responseDefaultDto.setMap(map.toString());
					responseDefaultDto.setSyncCd(EZAdminEnums.EZAdminSyncCd.SYNC_CD_WAIT.getCode()); // 배치 연동 상태
					responseDefaultDto.setPage(String.valueOf(totalPage));								// 총 page
					responseDefaultDto.setReqStartDate(reqDto.getStart_date());							// 요청 시작 일자
					responseDefaultDto.setReqEndDate(reqDto.getEnd_date());								// 요청 종료 일자
					responseDefaultDto.setEasyadminBatchTp(EZAdminEnums.EZAdminBatchTypeCd.QNA_SEARCH.getCode()); //이미어드민 배치타입
					addEasyAdminQnaInfo(responseDefaultDto);
				}

 	    		// 문의글 정보
 	    		List<EZAdminQnaInfoVo> qnaInfoResponseList = (List<EZAdminQnaInfoVo>)responseDefaultDto.getData();

 	    		for(EZAdminQnaInfoVo qnaInfo : qnaInfoResponseList) {
 	    			qnaInfo.setCsOutmallQnaEasyadminInfoId(responseDefaultDto.getCsOutmallQnaEasyadminInfoId());
 	    			qnaInfoList.add(qnaInfo);
 	    		}

 	    		page++;

 	    	} else {
 	    		if (!result.getCode().equals(ApiEnums.EzAdminStatus.NO_DATA.getCode())
						&& !result.getCode().equals(ApiEnums.EzAdminStatus.RESPONSEBODY_NO_DATA.getCode())
				) { // 데이터가 없는경우는 실패 제외
					failCount++;
				} else {
 	    			endFlag = "Y";
				}
 	    		log.error("===========EZAdmin callGetQnaInfo() FAIL==============={}", result.getMessage());
 	    	}

			// API 3번 실패할때까지만 호출
 	    	if (failCount >= 3){
				endFlag = "Y";
			}

			if (page > totalPage){
				 endFlag = "Y";
			 }



		 } while("N".equals(endFlag));
         //}while(result.getCode().equals(ApiResult.success().getCode()) || failCount < 4);

    	 if(CollectionUtils.isNotEmpty(qnaInfoList)){
			 responseDto.setQnaInfoList(qnaInfoList);
		 }

    	 // API 호출 3회 실패시 -> BOS 관리자 자동메일 발송
    	 if(failCount >= 3){
			 responseDto.setFailCount(failCount);
		 }

    	 log.info("===========EZAdmin callGetQnaInfo() END===============");

    	return responseDto;
    }

    /**
     * @Desc 이지어드민 문의글 DB insert
     * @param qnaInfoList
     */
     protected void qnaExecute(List<EZAdminQnaInfoVo> qnaInfoList) throws Exception{

    	if(CollectionUtils.isNotEmpty(qnaInfoList)) {

    		for(EZAdminQnaInfoVo qnaInfo : qnaInfoList) {
    		    //중복키 조회
                int duplicateCheck = duplicateCsOutmallQnaSeq(qnaInfo.getSeq());

                if(duplicateCheck == 0) {
                     switch (qnaInfo.getStatus()) {
                        case 0: // 답변대기
                            qnaInfo.setOutmallQnaStatus("QNA_STATUS.RECEPTION");
                            break;
                        case 3: // 답변완료
                            qnaInfo.setOutmallQnaStatus("QNA_STATUS.ANSWER_COMPLETED");
                            break;
                        default: // 답변입력(1), 전송실패(2)
                            qnaInfo.setOutmallQnaStatus("QNA_STATUS.ANSWER_CHECKING");
                            break;
                    }
					addCsOutmallQnaEasyAdminQnaDetail(qnaInfo);

                     if(qnaInfo.getStatus() != 0) { // 답변입력, 전송실패, 답변완료 시, CS_OUTMALL_QNA_ANSWER 답변 추가
                        addCsOutmallQnaEasyAdminQnaAnswerDetail(qnaInfo);
                     }
                }

    		}
    	}
    }

    /**
     *  이지어드민 송장입력 API 전송여부 업데이트
     */
    protected int putOdTrackingNumberEzadminApiYn(EZAdminTransNoTargetVo eZAdminTransNoTargetVo){
        // eZAdminTransNoTargetVo.setPrd_seq("'" + eZAdminTransNoTargetVo.getPrd_seq().replaceAll(",","','") + "'");
        return collectionMallEZAdminBatchMapper.putOdTrackingNumberEzadminApiYn(eZAdminTransNoTargetVo);
    }

    /**
     *  CS_OUTMALL_QNA의 SEQ 중복검사
     */
    protected int duplicateCsOutmallQnaSeq(int seq){
        return collectionMallEZAdminBatchMapper.duplicateCsOutmallQnaSeq(seq);
    }

    /**
     * @Desc 이지어드민 API 호출 정보 추가
     * @param ezAdminApiInfoVo
     */
    protected int addIfEasyadminApiInfo(EZAdminApiInfoVo ezAdminApiInfoVo){
        return collectionMallEZAdminBatchMapper.addIfEasyadminApiInfo(ezAdminApiInfoVo);
    }

    /**
     * @Desc 이지어드민 API 호출 실패여부 확인 -> API 호출 연속 3회 실패시 BOS 알림메일 발송
     */
    protected void checkBosCollectionMallInterfaceFail() throws Exception{

        // API 호출 연속 3회 실패여부 체크
        int failCnt = 0;

        List<String> orderCsList = new ArrayList<>();
        orderCsList.add(ApiEnums.EZAdminGetOrderInfoOrderCs.ORDER.getOrder_cs()); // 정상 주문
        orderCsList.add(ApiEnums.EZAdminGetOrderInfoOrderCs.CLAIM.getOrder_cs()); // 클레임 주문

        if(CollectionUtils.isNotEmpty(orderCsList)){
            for(String orderCs : orderCsList){
                failCnt += collectionMallEZAdminBatchMapper.isBosCollectionMallInterfaceFail(orderCs);
            }
        }

        // 정상 주문 or 클레임 주문 중 하나라도 3회이상 실패했을 경우 알림메일 발송
        if(failCnt > 0){
            orderEmailSendBiz.bosCollectionMallInterfaceFailNotification();
        }

    }

}
