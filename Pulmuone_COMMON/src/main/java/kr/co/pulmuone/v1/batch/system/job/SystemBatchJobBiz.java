package kr.co.pulmuone.v1.batch.system.job;

import kr.co.pulmuone.v1.comm.base.ApiResult;

public interface SystemBatchJobBiz {
	ApiResult<?> getBatchJobStockClosed();
}
