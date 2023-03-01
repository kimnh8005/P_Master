package kr.co.pulmuone.batch.eon.domain.model.sample;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SampleSlaveVo {

    private Long seq;
    private String title;
    private String text;
    private int readCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
