package kr.co.pulmuone.v1.comm.mapper.user.buyer;

import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetShippingAddressListResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserBuyerShippingAddrMapper {

  Long getOrderLatelyShippingAddressCount(@Param("urUserId") long urUserId, @Param("odOrderId") long odOrderId);

  int addLatelyShippingAddress(@Param("urUserId") long urUserId, @Param("odOrderId") long odOrderId);

  int putLatelyShippingAddress(long urLatelyShippingAddrId);

  List<CommonGetShippingAddressListResultVo> getMypageShippingAddressList(long urUserId) throws Exception;

}
