package kr.co.pulmuone.bos.user.store;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.comm.constant.BosStorageInfoEnum;
import kr.co.pulmuone.bos.comm.constant.StoreType;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType.StorageType;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistResponseDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDeliveryAreaListRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDeliveryAreaListResponseDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDetailRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDetailResponseDto;
import kr.co.pulmuone.v1.user.store.dto.StoreListRequestDto;
import kr.co.pulmuone.v1.user.store.service.StoreBiz;

/**
 * <PRE>
 * BOS 매장/가맹점 관리 & 배송권역 관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200717		       민동진            최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
public class StoreController {

	@Autowired
	private StoreBiz storeBiz;

	@Autowired(required=true)
	private HttpServletRequest request;



	/**
	 * @description : 매장 배송권역 관리 리스트
	 * @Date : 2020. 7. 17.	 *
	 * @author : Dongjin, Min
	 * @param StoreDeliveryAreaListRequestDto
	 * @return GetStoreDeliveryAreaListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/store/getShopDeliveryAreaList")
	@ApiOperation(value = "매장 배송권역 관리 리스트")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StoreDeliveryAreaListResponseDto.class)
	})
    @ResponseBody
	public ApiResult<?> getShopDeliveryAreaList (HttpServletRequest request, StoreDeliveryAreaListRequestDto dto) throws Exception {
		StoreDeliveryAreaListRequestDto storeDeliveryAreaListRequestDto = (StoreDeliveryAreaListRequestDto) BindUtil.convertRequestToObject(request, StoreDeliveryAreaListRequestDto.class);
		storeDeliveryAreaListRequestDto.setStoreType(StoreType.DIRECT.getCode());

        return storeBiz.getStoreDeliveryAreaList(storeDeliveryAreaListRequestDto);
	}


	/**
	 * @description : 가맹점 배송권역 관리 리스트
	 * @Date : 2020. 7. 17.	 *
	 * @author : Dongjin, Min
	 * @param StoreDeliveryAreaListRequestDto
	 * @return GetStoreDeliveryAreaListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/store/getBranchDeliveryAreaList")
	@ApiOperation(value = "가맹점 배송권역 관리 리스트")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StoreDeliveryAreaListResponseDto.class)
	})
    @ResponseBody
	public ApiResult<?> getBranchDeliveryAreaList (HttpServletRequest request, StoreDeliveryAreaListRequestDto dto) throws Exception {
		StoreDeliveryAreaListRequestDto storeDeliveryAreaListRequestDto = (StoreDeliveryAreaListRequestDto) BindUtil.convertRequestToObject(request, StoreDeliveryAreaListRequestDto.class);
		storeDeliveryAreaListRequestDto.setStoreType(StoreType.BRANCH.getCode());

        return storeBiz.getStoreDeliveryAreaList(storeDeliveryAreaListRequestDto);
	}


	/**
	 * @description : 매장 목록 리스트
	 * @param StoreDeliveryAreaListRequestDto
	 * @return GetStoreDeliveryAreaListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/store/getStoreList")
	@ApiOperation(value = "매장목록 리스트")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StoreDeliveryAreaListResponseDto.class)
	})
    @ResponseBody
	public ApiResult<?> getStoreList (HttpServletRequest request, StoreListRequestDto dto) throws Exception {
		StoreListRequestDto storeListRequestDto = (StoreListRequestDto) BindUtil.convertRequestToObject(request, StoreListRequestDto.class);

        return storeBiz.getStoreList(storeListRequestDto);
	}


	@ApiOperation(value = "매장 상세정보 ")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = StoreDetailResponseDto.class)
	})
	@PostMapping(value = "/admin/ur/store/getStoreDetail")
	public ApiResult<?> getStoreDetail(StoreDetailRequestDto storeDetailRequestDto) throws Exception {
		return storeBiz.getStoreDetail(storeDetailRequestDto);
	}

	@ApiOperation(value = "매장 상세 수정")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = StoreDetailResponseDto.class)
	})
	@PostMapping(value = "/admin/ur/store/modifyStoreDetail")
	public ApiResult<?> modifyStoreDetail(@RequestBody StoreDetailRequestDto storeDetailRequestDto) throws Exception {

		String publicRootStoragePath = BosStorageInfoEnum.getRootStoragePath(StorageType.PUBLIC.name());
		storeDetailRequestDto.setImageRootStoragePath(publicRootStoragePath);

		return storeBiz.modifyStoreDetail(storeDetailRequestDto);
	}

	@GetMapping(value = "/admin/ur/store/getStoreDeliveryDropDownList")
	@ApiOperation(value = "매장목록 권역정보 드롭다운 리스트")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StoreDetailResponseDto.class)
	})
    @ResponseBody
	public ApiResult<?> getStoreDropDownList (StoreDetailRequestDto storeDetailRequestDto) throws Exception {

        return storeBiz.getStoreDeliveryList(storeDetailRequestDto);
	}

}
