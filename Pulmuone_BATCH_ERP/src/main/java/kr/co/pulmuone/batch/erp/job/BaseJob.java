package kr.co.pulmuone.batch.erp.job;

public interface BaseJob {

    void run(String[] params) throws Exception;
}
