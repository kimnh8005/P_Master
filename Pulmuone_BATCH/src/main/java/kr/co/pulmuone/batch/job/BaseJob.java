package kr.co.pulmuone.batch.job;

import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface BaseJob {

    void run(String[] params) throws Exception;
}
