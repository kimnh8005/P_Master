package kr.co.pulmuone.v1.batch.system.job;

import kr.co.pulmuone.v1.comm.mappers.batch.master.system.SystemBatchJobMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SystemBatchJobService {
	private final SystemBatchJobMapper systemBatchJobMapper;
	
	protected int getBatchJobStockClosed(int batchNo) {
		return systemBatchJobMapper.getBatchJobStockClosed(batchNo);
	}
}
