package kr.co.pulmuone.batch.cj.infra.mapper.claim.master;

import kr.co.pulmuone.batch.cj.domain.model.claim.CJLogisticsOrderAcceptDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * CJ 반품택배 접수 배치 Mapper
 * </PRE>
 */

@Mapper
public interface ReturnDeliveryReceiptMapper {

    List<CJLogisticsOrderAcceptDto> selectReturnDeliveryReceiptTargetList();

    int putReturnDeliveryReceiptBatchExecFl(CJLogisticsOrderAcceptDto item);

}
