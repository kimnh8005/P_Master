package kr.co.pulmuone.v1.calculate.pov.dto.vo;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import kr.co.pulmuone.v1.comm.util.DateUtil;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CalPovProcessVo {

	private String year;
	private String month;
	private String scenario;
	private boolean isInterfaced;
	private String creator;
	private Date createDate;
	private String updater;
	private Date updateDate;

	@Builder
	public CalPovProcessVo(String year, String month, String scenario, String creator) {
		this.year = year;
		this.month = month;
		this.scenario = scenario;
		this.isInterfaced = false;
		this.creator = creator;
		this.createDate = new Date();
		validate();
	}

	public void doInterface(String updater) {
		this.isInterfaced = true;
		this.updater = updater;
		this.updateDate = new Date();
	}

	private void validate() {
		validateYear();
		validateMonth();
		validateNotFuture();
		validateScenario();
	}

	private void validateNotFuture() {
		Calendar param = createCalendar(this.year, this.month);
		Calendar now = createCalendar(DateUtil.getCurrentDate("yyyy"), DateUtil.getCurrentDate("MM"));
		if (param.compareTo(now) > 0) {
			throw new IllegalArgumentException("현재 또는 과거만 가능합니다.");
		}
	}

	private Calendar createCalendar(String year, String month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Integer.parseInt(year));
		c.set(Calendar.MONTH, Integer.parseInt(month));
		return c;
	}

	private void validateYear() {
		if (StringUtils.isEmpty(year)) {
			throw new IllegalArgumentException("[년도]가 존재하지 않습니다.");
		}
		if (year.length() != 4) {
			throw new IllegalArgumentException("yyyy 포맷이 아닙니다.");
		}
	}

	private void validateMonth() {
		if (StringUtils.isEmpty(this.month)) {
			throw new IllegalArgumentException("[월]이 존재하지 않습니다.");
		}
		if (this.month.length() != 2) {
			throw new IllegalArgumentException("MM 포맷이 아닙니다.");
		}
		try {
			int month = Integer.parseInt(this.month);
			if (month > 12 && month < 1) {
				throw new IllegalArgumentException("MM 포맷이 아닙니다.");
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("MM 포맷이 아닙니다.");
		}
	}

	private void validateScenario() {
		if (scenario == null) {
			throw new IllegalArgumentException("scenario 값이 존재하지 않습니다.");
		}
	}

}
