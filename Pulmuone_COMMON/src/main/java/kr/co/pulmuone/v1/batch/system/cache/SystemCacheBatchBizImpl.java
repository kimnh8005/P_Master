package kr.co.pulmuone.v1.batch.system.cache;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class SystemCacheBatchBizImpl implements SystemCacheBatchBiz {

    private final SystemCacheBatchService service;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runMakeCache() throws IOException, BaseException {
        service.runMakeCache();
    }
}
