package kr.co.pulmuone.batch.eon.common;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String JOB_APP_BASE_PACKAGE = "kr.co.pulmuone.batch.eon.job.application";

    // 실행중여부 체크 제외 배치번호(이미 실행중이어도 에러없이 실행이 되어야만 하는 배치등.. )
    public static final List<Long> EXCLUDE_DUPLICATE_LIST = Arrays.asList(9992L);
}
