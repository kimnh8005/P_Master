package kr.co.pulmuone.v1.batch.system.cache;

import kr.co.pulmuone.v1.comm.exception.BaseException;

import java.io.IOException;

public interface SystemCacheBatchBiz {

    void runMakeCache() throws IOException, BaseException;
}
