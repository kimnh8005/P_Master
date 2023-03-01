package kr.co.pulmuone.v1.batch.system.cache.auth;

import java.util.List;

public interface SystemAuthBatchBiz {

    List<Long> getSystemProgramUserList(String pgId);

}
