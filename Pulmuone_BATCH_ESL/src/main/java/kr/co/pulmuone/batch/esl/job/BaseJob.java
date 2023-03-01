package kr.co.pulmuone.batch.esl.job;

public interface BaseJob {

    void run(String[] params) throws Exception;
}
