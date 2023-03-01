package kr.co.pulmuone.v1.comm.mappers.batch.master.system;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemBatchJobMapper {
	int getBatchJobStockClosed(int batchNo);
}
