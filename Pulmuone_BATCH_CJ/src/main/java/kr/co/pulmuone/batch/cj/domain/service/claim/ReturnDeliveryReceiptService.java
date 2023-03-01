package kr.co.pulmuone.batch.cj.domain.service.claim;

import kr.co.pulmuone.batch.cj.domain.model.claim.CJLogisticsOrderAcceptDto;
import kr.co.pulmuone.batch.cj.infra.mapper.claim.master.ReturnDeliveryReceiptMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * CJ 반품택배 접수 배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class ReturnDeliveryReceiptService {

    private final ReturnDeliveryReceiptMapper returnDeliveryReceiptMapper;

    protected List<CJLogisticsOrderAcceptDto> selectReturnDeliveryReceiptTargetList() {
        return returnDeliveryReceiptMapper.selectReturnDeliveryReceiptTargetList();
    }

    protected int putReturnDeliveryReceiptBatchExecFl(CJLogisticsOrderAcceptDto item) {
        return returnDeliveryReceiptMapper.putReturnDeliveryReceiptBatchExecFl(item);
    }
}
