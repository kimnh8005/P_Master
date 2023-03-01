package kr.co.pulmuone.v1.batch.system.cache.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SystemAuthBatchBizImpl implements SystemAuthBatchBiz {

    private final SystemAuthBatchService service;

    @Override
    public List<Long> getSystemProgramUserList(String pgId) {
        return service.getSystemProgramUserList(pgId);
    }
}
