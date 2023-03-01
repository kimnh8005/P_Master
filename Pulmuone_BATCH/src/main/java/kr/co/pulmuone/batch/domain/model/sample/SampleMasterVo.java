package kr.co.pulmuone.batch.domain.model.sample;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SampleMasterVo {

    private long no;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
