package kr.co.pulmuone.v1.batch.system.cache.auth;

import kr.co.pulmuone.v1.comm.mappers.batch.master.system.SystemAuthBatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemAuthBatchService {

    private final SystemAuthBatchMapper systemAuthBatchMapper;

    protected List<Long> getSystemProgramUserList(String pgId) {
        return systemAuthBatchMapper.getSystemProgramUserList(pgId);
    }

}
