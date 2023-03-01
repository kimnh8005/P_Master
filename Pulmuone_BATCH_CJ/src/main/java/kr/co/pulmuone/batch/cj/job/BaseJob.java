package kr.co.pulmuone.batch.cj.job;

public interface BaseJob {

    void run(String[] params) throws Exception;
}
