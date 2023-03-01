package kr.co.pulmuone.v1.user.store.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDeliveryAreaListRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDetailRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreListRequestDto;

public interface StoreBiz {

	/**
	 * 매장 배송권역 관리 리스트
	 */
	ApiResult<?> getStoreDeliveryAreaList(StoreDeliveryAreaListRequestDto storeDeliveryAreaListRequestDto) throws Exception;

//	/**
//	 * 가맹점 배송권역 관리 리스트
//	 */
//	ApiResult<?> getBranchDeliveryAreaList(StoreDeliveryAreaListRequestDto storeDeliveryAreaListRequestDto) throws Exception;

	/**
	 * 매장 목록 리스트
	 */
	ApiResult<?> getStoreList(StoreListRequestDto storeListRequestDto) throws Exception;

	// 매장 상세정보
	ApiResult<?> getStoreDetail(StoreDetailRequestDto storeDetailRequestDto) throws Exception;

	ApiResult<?> modifyStoreDetail(StoreDetailRequestDto storeDetailRequestDto) throws Exception;

	ApiResult<?> getStoreDeliveryList(StoreDetailRequestDto storeDetailRequestDto) throws Exception;

	List<ArrivalScheduledDateDto> rmoveUnDeliveryDate(String urStoreId, List<ArrivalScheduledDateDto> scheduledDateList) throws Exception;
}
