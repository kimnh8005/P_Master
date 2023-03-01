package kr.co.pulmuone.v1.comm.mappers.batch.master.shopping;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoppingCartBatchMapper {
    void delCart(@Param("spCartId") Long spCartId);

    void delCartAddGoodsBySpCartId(@Param("spCartId") Long spCartId);

    void delCartPickGoodsBySpCartId(@Param("spCartId") Long spCartId);

    List<Long> getMaintenanceCart(String cartMaintenancePeriod);
}
