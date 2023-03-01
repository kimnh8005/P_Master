package kr.co.pulmuone.v1.comm.mappers.batch.master.denormalization;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DenormalizationBatchMapper {
    void displayCategoryDenormalizationBatch();
}
