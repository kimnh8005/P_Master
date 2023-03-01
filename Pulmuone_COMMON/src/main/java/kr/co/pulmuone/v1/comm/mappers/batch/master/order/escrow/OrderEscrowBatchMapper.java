package kr.co.pulmuone.v1.comm.mappers.batch.master.order.escrow;

import kr.co.pulmuone.v1.batch.order.escrow.dto.EscrowRegistDeliveryDto;
import kr.co.pulmuone.v1.user.company.dto.vo.BusinessInformationVo;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderEscrowBatchMapper {

    List<EscrowRegistDeliveryDto> getTagetEscrowRegistDelivery();

    int putEscrowConnectSuccess(Long odPaymentMasterId);

	/**
	 * @Desc 사업자정보관리 조회
	 * @return BusinessInformationVo
	 */
	BusinessInformationVo getBizInfoForOrderEscrow();
}
