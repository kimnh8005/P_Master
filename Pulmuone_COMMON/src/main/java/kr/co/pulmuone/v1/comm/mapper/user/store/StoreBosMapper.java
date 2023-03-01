package kr.co.pulmuone.v1.comm.mapper.user.store;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDeliveryAreaListRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDetailRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreListRequestDto;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreDeliveryAreaVo;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreDetailVo;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreImageVo;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreListVo;

@Mapper
public interface StoreBosMapper {

	Page<StoreDeliveryAreaVo> getStoreDeliveryAreaList(StoreDeliveryAreaListRequestDto storeDeliveryAreaListRequestDto) throws Exception;

	Page<StoreListVo> getStoreList(StoreListRequestDto storeListRequestDto) throws Exception;

	StoreDetailVo getStoreDetail(StoreDetailRequestDto storeDetailRequestDto);

	List<StoreDeliveryAreaVo> getStoreDeliveryList(StoreDetailRequestDto storeDetailRequestDto);

	int modifyStoreDetail(StoreDetailRequestDto storeDetailRequestDto);

	int addStoreImage(StoreImageVo storeImageVo);

	int delStoreImage(StoreDetailRequestDto storeDetailRequestDto);

	List<LocalDate> getCheckStoreUnDeliveryDateList(@Param("urStoreId") String urStoreId, @Param("scheduledDateList") List<ArrivalScheduledDateDto> scheduledDateList) throws Exception;
}
