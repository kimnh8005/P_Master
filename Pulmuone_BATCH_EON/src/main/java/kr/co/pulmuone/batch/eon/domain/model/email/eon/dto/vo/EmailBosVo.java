package kr.co.pulmuone.batch.eon.domain.model.email.eon.dto.vo;

import lombok.Data;

@Data
public class EmailBosVo implements java.io.Serializable {
    private static final long serialVersionUID = 6468650509652315804L;

    private long snMailSendId;
    private String mail;
    private String title;
    private String content;
    private String senderNm;
    private String senderMail;
    private String returnMail;
    private long seq;
}
