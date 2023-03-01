package kr.co.pulmuone.batch.cj.domain.service.claim;

import kr.co.pulmuone.batch.cj.infra.mapper.claim.master.ReturnTrackingNumberMapper;
import kr.co.pulmuone.batch.cj.infra.mapper.claim.slaveCj.CjReturnTrackingNumberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class ReturnTrackingNumberService {

    private final ReturnTrackingNumberMapper returnTrackingNumberMapper;

    //private final CjReturnTrackingNumberMapper cjReturnTrackingNumberMapper;

    public List<String> selectTargetList() {
        return returnTrackingNumberMapper.selectTargetList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public int addReturnTrackingNumber(HashMap<String, String> params) {
        return returnTrackingNumberMapper.addReturnTrackingNumber(params);
    }
}
