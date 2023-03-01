package kr.co.pulmuone.v1.user.store.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.store.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDeliveryAreaListRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDeliveryAreaListResponseDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDetailRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDetailResponseDto;
import kr.co.pulmuone.v1.user.store.dto.StoreListRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreListResponseDto;

import kr.co.pulmuone.v1.user.store.dto.vo.StoreDeliveryAreaVo;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreDetailVo;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreBizImpl implements StoreBiz{

	@Autowired
	StoreService storeService;

	@Override
	public ApiResult<?> getStoreDeliveryAreaList(StoreDeliveryAreaListRequestDto storeDeliveryAreaListRequestDto) throws Exception {
		StoreDeliveryAreaListResponseDto storeDeliveryAreaListResponseDto = new StoreDeliveryAreaListResponseDto();

        Page<StoreDeliveryAreaVo> resultVo = storeService.getStoreDeliveryAreaList(storeDeliveryAreaListRequestDto);

        storeDeliveryAreaListResponseDto.setTotal(resultVo.getTotal());
        storeDeliveryAreaListResponseDto.setRows(resultVo.getResult());

        return ApiResult.success(storeDeliveryAreaListResponseDto);
	}




	/**
	 * 매장목록 조회
	 *
	 * @param StoreListRequestDto
	 * @return StoreListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStoreList(StoreListRequestDto storeListRequestDto) throws Exception {
		StoreListResponseDto result = new StoreListResponseDto();

		Page<StoreListVo> shopList = storeService.getStoreList(storeListRequestDto);

		result.setTotal(shopList.getTotal());
		result.setRows(shopList.getResult());

		return ApiResult.success(result);
	}


	// 매장 상세 정보 조회
	@Override
	public ApiResult<?> getStoreDetail(StoreDetailRequestDto storeDetailRequestDto) throws Exception{
		StoreDetailResponseDto storeDetailResponseDto = new StoreDetailResponseDto();
		StoreDetailVo storeDetailVo = storeService.getStoreDetail(storeDetailRequestDto);
		List<StoreDeliveryAreaVo> storeDeliveryAreaList = storeService.getStoreDeliveryList(storeDetailRequestDto);

		storeDetailResponseDto.setRow(storeDetailVo);
		storeDetailResponseDto.setStoreDeliveryArea(storeDeliveryAreaList);
		return ApiResult.success(storeDetailResponseDto);

	}

	// 매장 상세 정보 수정
	@Override
	public ApiResult<?> modifyStoreDetail(StoreDetailRequestDto storeDetailRequestDto) throws Exception {
		StoreDetailResponseDto responseDto = new StoreDetailResponseDto();

		// 매장 노출 여부 수정
		storeService.modifyStoreDetail(storeDetailRequestDto);

		return ApiResult.success(responseDto);
	}

	/**
	 * 매장목록 권역정보 드롭다운 리스트 조회
	 *
	 * @param StoreDetailRequestDto
	 * @return storeDetailResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStoreDeliveryList(StoreDetailRequestDto storeDetailRequestDto) throws Exception {
		StoreDetailResponseDto storeDetailResponseDto = new StoreDetailResponseDto();
		List<StoreDeliveryAreaVo> storeDeliveryAreaList = storeService.getStoreDeliveryList(storeDetailRequestDto);
		storeDetailResponseDto.setStoreDeliveryArea(storeDeliveryAreaList);

		return ApiResult.success(storeDetailResponseDto);
	}

	// 매장/가맹점_배달불가일로 인한 스케줄 변경
	@Override
	public List<ArrivalScheduledDateDto> rmoveUnDeliveryDate(String urStoreId, List<ArrivalScheduledDateDto> scheduledDateList) throws Exception {
		return storeService.rmoveUnDeliveryDate(urStoreId, scheduledDateList);
	}
}
