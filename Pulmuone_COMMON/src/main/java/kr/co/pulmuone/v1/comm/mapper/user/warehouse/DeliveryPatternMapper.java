package kr.co.pulmuone.v1.comm.mapper.user.warehouse;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.DeliveryPatternListVo;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.DeliveryPatternVo;

@Mapper
public interface DeliveryPatternMapper {

	Page<DeliveryPatternListVo> getDeliveryPatternList(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception;

	DeliveryPatternVo getShippingPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception;

	int addDeliveryPattern(DeliveryPatternRequestDto deliveryPatternRequestDto);

	int putDeliveryPattern(DeliveryPatternRequestDto deliveryPatternRequestDto);

	int addShippingPatternDay(DeliveryPatternRequestDto deliveryPatternRequestDto);

	int removeShippingPatternDay(DeliveryPatternRequestDto deliveryPatternRequestDto);


}
