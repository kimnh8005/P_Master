package kr.co.pulmuone.v1.comm.mappers.batch.master.user.store;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.batch.user.store.dto.vo.StoreDeliveryAreaVo;
import kr.co.pulmuone.v1.batch.user.store.dto.vo.StoreDeliveryZoneVo;
import kr.co.pulmuone.v1.batch.user.store.dto.vo.StoreInfoVo;
import kr.co.pulmuone.v1.batch.user.store.dto.vo.StoreScheduleVo;
import kr.co.pulmuone.v1.batch.user.store.dto.vo.StoreUndeliveryDateVo;

@Mapper
public interface StoreErpBatchMapper {


	int addStoreInfo(StoreInfoVo vo);

	int putStoreInfo(StoreInfoVo vo);

	int putStoreNotUse(StoreInfoVo vo);

	int addStoreDeliveryArea(StoreDeliveryZoneVo vo);

	int putStoreDeliveryArea(StoreDeliveryZoneVo vo);

	int putStoreDeliveryAreaNotUse(StoreDeliveryZoneVo vo);

	int addStoreOrdtime(StoreScheduleVo vo);

	int putStoreOrdtime(StoreScheduleVo vo);

	int putStoreOrdtimeNotUse(StoreScheduleVo vo);

	int addDeliveryArea(StoreDeliveryAreaVo vo);

	int putDeliveryArea(StoreDeliveryAreaVo vo);

	int putDeliveryAreaNotUse(StoreDeliveryAreaVo vo);

	int addUnDeliveryDate(StoreUndeliveryDateVo vo);

	int putUnDeliveryDate(StoreUndeliveryDateVo vo);

	int putUnDeliveryDateNotUse(StoreUndeliveryDateVo vo);


}
