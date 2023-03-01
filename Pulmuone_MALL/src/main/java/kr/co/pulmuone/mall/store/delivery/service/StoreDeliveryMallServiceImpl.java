package kr.co.pulmuone.mall.store.delivery.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBiz;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryByGoodsIdDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * Class 의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20210216   	 	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class StoreDeliveryMallServiceImpl implements StoreDeliveryMallService {

    @Autowired
    private StoreDeliveryBiz storeDeliveryBiz;

    @Autowired
    private OrderScheduleBiz orderScheduleBiz;

    @Autowired
    private GoodsGoodsBiz goodsGoodsBiz;


    /**
     * 상품,우편번호,건물번호로 스토어 배송가능여부 조회
     *
     * @param ilGoodsId
     * @param zipCode
     * @param buildingCode
     * @throws Exception
     */
    @Override
    public ApiResult<?> isShippingPossibilityStoreDeliveryAreaByGoodsId(Long ilGoodsId, String zipCode, String buildingCode) throws Exception{
    	ShippingPossibilityStoreDeliveryByGoodsIdDto result = new ShippingPossibilityStoreDeliveryByGoodsIdDto();
    	boolean isShippingPossibility = false;

    	ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityDto
    			= storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(ilGoodsId, zipCode, buildingCode);

    	if(shippingPossibilityDto != null) {
    		isShippingPossibility = true;
	    	// 일일배송 > 변경가능한 배송일자
    		// 상품 출고처 정보 조회
	  		GoodsRequestDto goodsRequestDto = new GoodsRequestDto();
	  		goodsRequestDto.setIlGoodsId(ilGoodsId);
	  		BasicSelectGoodsVo basicGoodsVo =  goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);
	  		Long urWarehouseId = basicGoodsVo.getUrWareHouseId();

	  		HashMap<String,List<String>> scheduleDelvDate = orderScheduleBiz.getScheduleDelvDateList(shippingPossibilityDto, urWarehouseId);
	  		String defaultDelvDate = "";
	  		List<String> delvDate = new ArrayList<>();
	  		List<String> delvDateWeekDay = new ArrayList<>();

	  		if(!scheduleDelvDate.isEmpty()) {
	  			defaultDelvDate = scheduleDelvDate.get("scheduleDelvDateList").get(0);
	  			delvDate = scheduleDelvDate.get("scheduleDelvDateList");
	  			delvDateWeekDay = scheduleDelvDate.get("scheduleDelvDayOfWeekList");
	  		}

	  		result.setDefaultDelvDate(defaultDelvDate);
	  		result.setDelvDate(delvDate);
	  		result.setDelvDateWeekDay(delvDateWeekDay);

    	}
    	result.setShippingPossibility(isShippingPossibility);



    	return ApiResult.success(result);
    }

}