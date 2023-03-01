package kr.co.pulmuone.batch.job.application.system.cache;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.system.cache.SystemCacheBatchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("systemCacheJob")
@Slf4j
public class SystemCacheJob implements BaseJob {

    @Autowired
    private SystemCacheBatchBiz systemCacheBatchBiz;

    /***
     * System Cache 파일 생성
     * https://s.pulmuone.app/BATCH/cache/apiCacheList.json
     * @param params
     * @throws BaseException
     */
    @Override
    public void run(String[] params) throws BaseException  {
        try {
            systemCacheBatchBiz.runMakeCache();
        } catch (IOException | BaseException e) {
            throw new BaseException(e.getMessage());
        }
    }
}
