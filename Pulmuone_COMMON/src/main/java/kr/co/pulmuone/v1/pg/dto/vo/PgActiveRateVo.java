package kr.co.pulmuone.v1.pg.dto.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PgActiveRateVo {

	private int kcpRate;

	private int inicisRate;
}
