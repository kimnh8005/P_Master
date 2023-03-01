package kr.co.pulmuone.batch.cj.domain.service.system;

public interface BatchJobManagementService {

    void run(long batchNo, String userId) throws Exception;

    void run(long batchNo, String userId, String[] params) throws Exception;
}
