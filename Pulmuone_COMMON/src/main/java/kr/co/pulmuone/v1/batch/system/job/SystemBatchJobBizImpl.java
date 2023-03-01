package kr.co.pulmuone.v1.batch.system.job;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SystemBatchJobBizImpl implements SystemBatchJobBiz{
	private final SystemBatchJobService systemBatchJobService;
	
	@Override
	public ApiResult<?> getBatchJobStockClosed() {
		boolean isBatchComplete = systemBatchJobService.getBatchJobStockClosed(BatchConstants.IL_ITEM_STOCK_BATCH_NO) > 0;
		return ApiResult.success(isBatchComplete);
	}
}
