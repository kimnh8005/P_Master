package kr.co.pulmuone.v1.order.order.service;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.order.order.dto.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.mapper.order.order.OrderListMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 *  1.1    2020. 12. 15.            석세동         수정
 *  1.2    2021. 01. 11             김명진         엑셀다운로드 추가
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderListService {

    private final OrderListMapper orderListMapper;

    /**
     * @Desc 검색키 -> 검색키 리스트 변환
     *       검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
     * @param searchKey
     * @param splitKey
     * @return List<String>
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();
        if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                                       .map(String::trim)
                                       .filter( x -> StringUtils.isNotEmpty(x) )
                                       .collect(Collectors.toList()));
        }
        return searchKeyList;
    }

    protected void setOrderListSearchParam(OrderListRequestDto orderListRequestDto) {

    	orderListRequestDto.setOmSellersIdList(getSearchKeyToSearchKeyList(orderListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처
    	orderListRequestDto.setOrderStateList(getSearchKeyToSearchKeyList(orderListRequestDto.getOrderState(), Constants.ARRAY_SEPARATORS)); // 주문상태
    	orderListRequestDto.setClaimStateList(getSearchKeyToSearchKeyList(orderListRequestDto.getClaimState(), Constants.ARRAY_SEPARATORS)); // 클레임상태
    	orderListRequestDto.setPaymentMethodCodeList(getSearchKeyToSearchKeyList(orderListRequestDto.getPaymentMethodCode(), Constants.ARRAY_SEPARATORS)); // 결제수단
    	orderListRequestDto.setBuyerTypeCodeList(getSearchKeyToSearchKeyList(orderListRequestDto.getBuyerTypeCode(), Constants.ARRAY_SEPARATORS)); // 주문자유형
    	orderListRequestDto.setAgentTypeCodeList(getSearchKeyToSearchKeyList(orderListRequestDto.getAgentTypeCode(), Constants.ARRAY_SEPARATORS)); // 유형


		// 상태 검색 A: 조건 없음 Y: 주문상태/클레임상태 조건 적용 N: 클레임 조건 제외(클레임경우만 사용
		orderListRequestDto.setNotOrderStateYn("A");
		orderListRequestDto.setNotClaimStateYn("A");

		if (StringUtil.nvl(orderListRequestDto.getOrderState(), "").indexOf("ALL") < 0){
			if (orderListRequestDto.getOrderStateList().size() > 0) {
				orderListRequestDto.setNotOrderStateYn("Y");
			}
		}

		if ("".equals(orderListRequestDto.getOrderState())) {
			orderListRequestDto.setNotOrderStateYn("N");
		}

		if (StringUtil.nvl(orderListRequestDto.getClaimState(), "").indexOf("ALL") < 0){
			if (orderListRequestDto.getClaimStateList().size() > 0) {
				orderListRequestDto.setNotClaimStateYn("Y");
			}
		}

		if ("".equals(orderListRequestDto.getClaimState())) {
			orderListRequestDto.setNotClaimStateYn("N");
		}

    	ArrayList<String> orderCdArray = null;

       	if(orderListRequestDto.getSelectConditionType().equals("singleSection") && !StringUtil.isEmpty(orderListRequestDto.getSearchSingleType())) {

       		//화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String orderCodeListStr = orderListRequestDto.getCodeSearch().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
            orderCdArray = StringUtil.getArrayListComma(orderCodeListStr);
            orderListRequestDto.setCodeSearchList(orderCdArray);
       	}
    }

    protected OrderListResponseDto getOrderList(OrderListRequestDto orderListRequestDto) {
    	UserVo userVo = SessionUtil.getBosUserVO();

        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthStoreId = userVo.getListAuthStoreId();
        listAuthStoreId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSellersId = userVo.getListAuthSellersId();
        listAuthSellersId.removeIf(s->StringUtils.isEmpty(s));

        orderListRequestDto.setListAuthWarehouseId(listAuthWarehouseId);
    	orderListRequestDto.setListAuthSupplierId(listAuthSupplierId);
    	orderListRequestDto.setListAuthStoreId(listAuthStoreId);
    	orderListRequestDto.setListAuthSellersId(listAuthSellersId);

    	OrderListResponseDto orderListResponseDto = new OrderListResponseDto();
		long totalCnt = orderListMapper.getOrderListCount(orderListRequestDto);
		List<OrderListDto> orderList = new ArrayList<> ();
		if(totalCnt > 0) {
			orderList = orderListMapper.getOrderList(orderListRequestDto);
		}
		orderListResponseDto.setTotal(totalCnt);
		orderListResponseDto.setRows(orderList);
        return orderListResponseDto;
    }

    protected List<OrderExcelListDto> getOrderExcelList(OrderListRequestDto orderListRequestDto) {
    	UserVo userVo = SessionUtil.getBosUserVO();

        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthStoreId = userVo.getListAuthStoreId();
        listAuthStoreId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSellersId = userVo.getListAuthSellersId();
        listAuthSellersId.removeIf(s->StringUtils.isEmpty(s));

    	orderListRequestDto.setListAuthWarehouseId(listAuthWarehouseId);
    	orderListRequestDto.setListAuthSupplierId(listAuthSupplierId);
    	orderListRequestDto.setListAuthStoreId(listAuthStoreId);
    	orderListRequestDto.setListAuthSellersId(listAuthSellersId);

    	return orderListMapper.getOrderExcelList(orderListRequestDto);
    }

    protected OrderDetailListResponseDto getOrderDetailList(OrderListRequestDto orderListRequestDto) {
    	UserVo userVo = SessionUtil.getBosUserVO();

        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthStoreId = userVo.getListAuthStoreId();
        listAuthStoreId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSellersId = userVo.getListAuthSellersId();
        listAuthSellersId.removeIf(s->StringUtils.isEmpty(s));

    	orderListRequestDto.setListAuthWarehouseId(listAuthWarehouseId);
    	orderListRequestDto.setListAuthSupplierId(listAuthSupplierId);
    	orderListRequestDto.setListAuthStoreId(listAuthStoreId);
    	orderListRequestDto.setListAuthSellersId(listAuthSellersId);


		OrderDetailListResponseDto orderDetailListResponseDto = new OrderDetailListResponseDto();
    	//PageMethod.startPage(orderListRequestDto.getPage(), orderListRequestDto.getPageSize());

    	long totalCnt = orderListMapper.getOrderDetailListCount(orderListRequestDto);

    	List<OrderDetailListDto> orderDetailList = null;

    	if(totalCnt > 0) {
    		orderDetailList = orderListMapper.getOrderDetailList(orderListRequestDto);
    	}
    	else {
    		orderDetailList = new ArrayList<> ();
    	}

		orderDetailListResponseDto.setTotal(totalCnt);
		orderDetailListResponseDto.setRows(orderDetailList);

        return orderDetailListResponseDto;
    }

    protected void setOrderDetailListSearchParam(OrderListRequestDto orderListRequestDto) {

		orderListRequestDto.setOmSellersIdList(getSearchKeyToSearchKeyList(orderListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처
    	orderListRequestDto.setOrderStateList(getSearchKeyToSearchKeyList(orderListRequestDto.getOrderState(), Constants.ARRAY_SEPARATORS)); // 주문상태
    	orderListRequestDto.setClaimStateList(getSearchKeyToSearchKeyList(orderListRequestDto.getClaimState(), Constants.ARRAY_SEPARATORS)); // 클레임상태
    	orderListRequestDto.setPaymentMethodCodeList(getSearchKeyToSearchKeyList(orderListRequestDto.getPaymentMethodCode(), Constants.ARRAY_SEPARATORS)); // 결제수단
    	orderListRequestDto.setBuyerTypeCodeList(getSearchKeyToSearchKeyList(orderListRequestDto.getBuyerTypeCode(), Constants.ARRAY_SEPARATORS)); // 주문자유형
    	orderListRequestDto.setAgentTypeCodeList(getSearchKeyToSearchKeyList(orderListRequestDto.getAgentTypeCode(), Constants.ARRAY_SEPARATORS)); // 유형
		orderListRequestDto.setDelivTypeList(getSearchKeyToSearchKeyList(orderListRequestDto.getSearchDelivType(), Constants.ARRAY_SEPARATORS)); // 배송방법
		orderListRequestDto.setOrderTypeCodeList(getSearchKeyToSearchKeyList(orderListRequestDto.getOrderTypeCode(), Constants.ARRAY_SEPARATORS)); // 주문유형
		orderListRequestDto.setCsRefundApproveCdList(getSearchKeyToSearchKeyList(orderListRequestDto.getCsRefundApproveCd(), Constants.ARRAY_SEPARATORS)); // CS환불 승인상태
		orderListRequestDto.setCsRefundTpList(getSearchKeyToSearchKeyList(orderListRequestDto.getCsRefundTp(), Constants.ARRAY_SEPARATORS)); // CS환불 구분


		// 상태 검색 A: 조건 없음 Y: 주문상태/클레임상태 조건 적용 N: 클레임 조건 제외(클레임경우만 사용
		orderListRequestDto.setNotOrderStateYn("A");
		orderListRequestDto.setNotClaimStateYn("A");

		if (StringUtil.nvl(orderListRequestDto.getOrderState(), "").indexOf("ALL") < 0){
			if (orderListRequestDto.getOrderStateList().size() > 0) {
				orderListRequestDto.setNotOrderStateYn("Y");
			}
		}

		if (StringUtil.nvl(orderListRequestDto.getClaimState(), "").indexOf("ALL") < 0){
			if (orderListRequestDto.getClaimStateList().size() > 0) {
				orderListRequestDto.setNotClaimStateYn("Y");
			}
		}

		if ("".equals(orderListRequestDto.getClaimState())) {
			orderListRequestDto.setNotClaimStateYn("N");
		}

    	ArrayList<String> orderCdArray = null;

       	if(orderListRequestDto.getSelectConditionType().equals("singleSection") && !StringUtil.isEmpty(orderListRequestDto.getSearchSingleType())) {
//       		if(orderListRequestDto.getOrderDetailType().equals("returnOrder")){
//       			orderListRequestDto.setClaimStateList(getSearchKeyToSearchKeyList(orderListRequestDto.getClaimStateSingle(), Constants.ARRAY_SEPARATORS)); // 반품 단일조건 주문상태
//       		}
       		//화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String orderCodeListStr = orderListRequestDto.getCodeSearch().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
            orderCdArray = StringUtil.getArrayListComma(orderCodeListStr);
            orderListRequestDto.setCodeSearchList(orderCdArray);
       	}
    }

	/**
	 * CS환불 리스트 검색 조건 Set
	 * @param orderListRequestDto
	 */
	protected void setCSRefundRequestParam(OrderListRequestDto orderListRequestDto) {

		String csRefundApproveCd = orderListRequestDto.getCsRefundApproveCd();
		if(StringUtil.isNotEmpty(csRefundApproveCd)) {
			// 승인상태 조건에 승인완료가 포함되어 있을 경우, 승인완료(부) 와 승인완료(시스템) 포함 해야 함
			if(csRefundApproveCd.indexOf(ApprovalEnums.ApprovalStatus.APPROVED.getCode()) > -1) {
				csRefundApproveCd += Constants.ARRAY_SEPARATORS + ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode();
				csRefundApproveCd += Constants.ARRAY_SEPARATORS + ApprovalEnums.ApprovalStatus.APPROVED_BY_SYSTEM.getCode();
				orderListRequestDto.setCsRefundApproveCd(csRefundApproveCd);
			}
		}
		orderListRequestDto.setOmSellersIdList(getSearchKeyToSearchKeyList(orderListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처
		orderListRequestDto.setCsRefundApproveCdList(getSearchKeyToSearchKeyList(orderListRequestDto.getCsRefundApproveCd(), Constants.ARRAY_SEPARATORS)); // CS환불 승인상태
		orderListRequestDto.setCsRefundTpList(getSearchKeyToSearchKeyList(orderListRequestDto.getCsRefundTp(), Constants.ARRAY_SEPARATORS)); // CS환불 구분

		ArrayList<String> orderCdArray = null;
		if("singleSection".equals(orderListRequestDto.getSelectConditionType()) && !StringUtil.isEmpty(orderListRequestDto.getSearchSingleType())) {
			//화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
			String orderCodeListStr = orderListRequestDto.getCodeSearch().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
			orderCdArray = StringUtil.getArrayListComma(orderCodeListStr);
			orderListRequestDto.setCodeSearchList(orderCdArray);
		}
	}

	/**
	 * CS 환불 리스트 조회
	 * @param csRefundRequest
	 */
	protected OrderCSRefundListResponseDto getCSRefundList(OrderListRequestDto csRefundRequest) {
		UserVo userVo = SessionUtil.getBosUserVO();

		List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
		listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthStoreId = userVo.getListAuthStoreId();
        listAuthStoreId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSellersId = userVo.getListAuthSellersId();
        listAuthSellersId.removeIf(s->StringUtils.isEmpty(s));

		csRefundRequest.setListAuthWarehouseId(listAuthWarehouseId);
		csRefundRequest.setListAuthSupplierId(listAuthSupplierId);
		csRefundRequest.setListAuthStoreId(listAuthStoreId);
		csRefundRequest.setListAuthSellersId(listAuthSellersId);


		OrderCSRefundListResponseDto csRefundResponseDto = new OrderCSRefundListResponseDto();

		OrderCSRefundTotalInfoDto totalInfo = orderListMapper.getCSRefundListCount(csRefundRequest);

		List<OrderCSRefundListDto> csRefundList = null;

		if(totalInfo.getTotal() > 0) {
			csRefundList = orderListMapper.getCSRefundList(csRefundRequest);
		}
		else {
			csRefundList = new ArrayList<> ();
		}

		csRefundResponseDto.setTotal(totalInfo.getTotal());
		csRefundResponseDto.setTotalPrice(totalInfo.getTotalRefundPrice());
		csRefundResponseDto.setRows(csRefundList);

		return csRefundResponseDto;
	}

	/**
	 * CS 환불 엑셀 리스트 조회
	 * @param csRefundRequest
	 */
	protected List<OrderCSRefundListDto> getCSRefundExcelList(OrderListRequestDto csRefundRequest) {
		UserVo userVo = SessionUtil.getBosUserVO();

		List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
		listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthStoreId = userVo.getListAuthStoreId();
        listAuthStoreId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSellersId = userVo.getListAuthSellersId();
        listAuthSellersId.removeIf(s->StringUtils.isEmpty(s));

		csRefundRequest.setListAuthWarehouseId(listAuthWarehouseId);
		csRefundRequest.setListAuthSupplierId(listAuthSupplierId);
		csRefundRequest.setListAuthStoreId(listAuthStoreId);
		csRefundRequest.setListAuthSellersId(listAuthSellersId);

		return orderListMapper.getCSRefundList(csRefundRequest);
	}

    protected List<OrderDetailExcelListDto> getOrderDetailExcelList(OrderListRequestDto orderListRequestDto) {
    	UserVo userVo = SessionUtil.getBosUserVO();

        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(StringUtils::isEmpty);
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(StringUtils::isEmpty);
        List<String> listAuthStoreId = userVo.getListAuthStoreId();
        listAuthStoreId.removeIf(StringUtils::isEmpty);
        List<String> listAuthSellersId = userVo.getListAuthSellersId();
        listAuthSellersId.removeIf(StringUtils::isEmpty);

    	orderListRequestDto.setListAuthWarehouseId(listAuthWarehouseId);
    	orderListRequestDto.setListAuthSupplierId(listAuthSupplierId);
    	orderListRequestDto.setListAuthStoreId(listAuthStoreId);
    	orderListRequestDto.setListAuthSellersId(listAuthSellersId);

    	return orderListMapper.getOrderDetailExcelList(orderListRequestDto);
    }

	protected List<LinkedHashMap<String, Object>> getOrderDetailExcelListMap(OrderListRequestDto orderListRequestDto) {
		return orderListMapper.getOrderDetailExcelListMap(orderListRequestDto);
	}

	protected OrderDetailTotalCountResponseDto getOrderTotalCountInfo(OrderListRequestDto orderListRequestDto) {
		UserVo userVo = SessionUtil.getBosUserVO();

        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthStoreId = userVo.getListAuthStoreId();
        listAuthStoreId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthSellersId = userVo.getListAuthSellersId();
        listAuthSellersId.removeIf(s->StringUtils.isEmpty(s));

    	orderListRequestDto.setListAuthWarehouseId(listAuthWarehouseId);
    	orderListRequestDto.setListAuthSupplierId(listAuthSupplierId);
    	orderListRequestDto.setListAuthStoreId(listAuthStoreId);
    	orderListRequestDto.setListAuthSellersId(listAuthSellersId);

		OrderDetailTotalCountResponseDto orderDetailTotalCountResponseDto = new OrderDetailTotalCountResponseDto();
		OrderDetailTotalCountDto info = orderListMapper.getOrderTotalCountInfo(orderListRequestDto);

		orderDetailTotalCountResponseDto.setRows(info);

		return orderDetailTotalCountResponseDto;
	}

	/**
	 * 반품 사유 코드 목록 조회
	 * @param
	 * @return
	 */
	protected List<GetCodeListResultVo> getReturnReasonList() {
		return orderListMapper.getReturnReasonList();
	}
}
