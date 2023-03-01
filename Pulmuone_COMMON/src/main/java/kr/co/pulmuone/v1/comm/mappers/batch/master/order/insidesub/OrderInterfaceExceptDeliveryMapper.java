package kr.co.pulmuone.v1.comm.mappers.batch.master.order.insidesub;

import kr.co.pulmuone.v1.batch.order.insidesub.dto.vo.InterfaceExceptOrderVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 I/F 외 배송준비중 배치 Mapper
 * </PRE>
 */

@Mapper
public interface OrderInterfaceExceptDeliveryMapper {

    /**
     * I/F 외 주문 조회
     * @param orderChangeTp
     * @param orderStatusCd
     * @return List<InterfaceExceptOrderVo>
     * @throws BaseException
     */
    List<InterfaceExceptOrderVo> selectInterfaceExceptOrder(@Param("orderChangeTp") String[] orderChangeTp, @Param("orderStatusCd") String orderStatusCd) throws BaseException;

}
