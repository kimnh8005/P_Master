package kr.co.pulmuone.batch.eon.job;

public interface BaseJob {

    void run(String[] params) throws Exception;
}
