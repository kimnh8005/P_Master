package kr.co.pulmuone.batch.esl.domain.model.sample;

import lombok.Data;

@Data
public class SampleMasterVo {

    private long batchNo;
    private String batchName;
    private String description;
    private String jobClassFullPath;
    private String useYn;
}
