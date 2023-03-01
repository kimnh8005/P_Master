package kr.co.pulmuone.batch.domain.model.system;

import kr.co.pulmuone.batch.common.util.BooleanToYnConverter;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@EqualsAndHashCode(of = {"batchNo"})
public class BatchJob {

    private Long batchNo;

    private String batchName;

    private String description;

    private String jobClassFullPath;

    private boolean useYn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    private String schedule;

    private String importance;

    private String manager;

    private String managerSub;

    private String domain;
}
