package kr.co.pulmuone.batch.eon.domain.model.email.eon.dto.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class EmailEonVo implements Serializable {
    private static final long serialVersionUID = -994686534193013078L;

    private long seq;
    private String content;
    private String subject;
    private String sendEmail;
    private String sendName;
    private String returnEmail;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
