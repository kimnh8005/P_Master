package kr.co.pulmuone.batch.eon.domain.model.system;

import lombok.Getter;

@Getter
public enum BatchStatus {

    STARTED("실행중"), FAILED("오류"), COMPLETED("정상"), NOT_EXECUTED("미실행");

    private String result;

    BatchStatus(String result) {
        this.result = result;
    }
}
