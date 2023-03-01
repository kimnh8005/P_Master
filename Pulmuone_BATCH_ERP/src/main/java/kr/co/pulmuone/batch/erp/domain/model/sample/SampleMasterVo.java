package kr.co.pulmuone.batch.erp.domain.model.sample;

import lombok.Data;

@Data
public class SampleMasterVo {

    private long batchNo;
    private String batchName;
    private String description;
    private String jobClassFullPath;
    private String useYn;
}
