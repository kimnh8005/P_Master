package kr.co.pulmuone.batch.domain.model.sample;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SampleSlaveVo {

    private Long seq;
    private String title;
    private String text;
    private int readCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
