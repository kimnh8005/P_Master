package kr.co.pulmuone.batch.erp.domain.model.system;

import java.util.Date;
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
}
