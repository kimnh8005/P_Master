package kr.co.pulmuone.batch.cj.domain.model.sample;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SampleMasterVo {

    private long batchNo;
    private String batchName;
    private String description;
    private String jobClassFullPath;
    private String useYn;
}
