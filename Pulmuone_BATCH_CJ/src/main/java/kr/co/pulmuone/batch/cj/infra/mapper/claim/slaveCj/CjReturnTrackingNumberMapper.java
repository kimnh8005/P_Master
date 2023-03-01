package kr.co.pulmuone.batch.cj.infra.mapper.claim.slaveCj;

import kr.co.pulmuone.batch.cj.domain.model.claim.CJLogisticsOrderAcceptDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

/**
 * <PRE>
 * Forbiz Korea
 * 클레임 회수 송장 API 배치 Mapper
 * </PRE>
 */

@Mapper
public interface CjReturnTrackingNumberMapper {

    int selectTracePulmuoneInfo(HashMap<String, String> params);

    int addReturnDeliveryReceipt(CJLogisticsOrderAcceptDto item);
}
