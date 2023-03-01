package kr.co.pulmuone.batch.domain.model.sample.api;

import lombok.Data;

import java.util.Date;

@Data
public class SampleApiVo {

    private long no;
    private String title;
    private String content;
    private Date createDate;
    private Date updateDate;
}
