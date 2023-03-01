package kr.co.pulmuone.v1.calculate.pov.dto;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import kr.co.pulmuone.v1.calculate.pov.dto.vo.CalPovProcessVo;
import kr.co.pulmuone.v1.comm.enums.PovEnums.ProcessStatus;
import kr.co.pulmuone.v1.comm.enums.PovEnums.Scenario;
import lombok.Getter;

@Getter
public class CalPovProcessDto {
	private boolean canTemporaryUpload = false;
	private String temporaryUploadText = "-";

	private boolean canTemporaryInterface = false;
	private String temporaryInterfaceText = "-";

	private boolean canFinalUpload = false;
	private String finalUploadText = "-";

	private boolean canFinalInterface = false;
	private String finalInterfaceText = "-";

	public CalPovProcessDto(Map<String, CalPovProcessVo> process) {
		setProcess(process);
	}

	private void setProcess(Map<String, CalPovProcessVo> process) {
		setDate(process);
		ProcessStatus status = ProcessStatus.getStatus(process);
		switch (status) {
		case NONE:
			setButtonAvailable(true, false, false, false);
			break;
		case TEMPORARY_UPLOADED:
			setButtonAvailable(true, true, false, false);
			break;
		case TEMPORARY_INTERFACED:
			setButtonAvailable(false, false, true, false);
			break;
		case FINAL_UPLOADED:
			setButtonAvailable(false, false, true, true);
			break;
		case FINAL_INTERFACED:
			setButtonAvailable(false, false, false, false);
			break;
		}
	}

	private void setDate(Map<String, CalPovProcessVo> process) {
		if (MapUtils.isEmpty(process)) {
			return;
		}
		for (String key : process.keySet()) {
			CalPovProcessVo vo = process.get(key);
			if (vo == null) {
				continue;
			}
			if (Scenario.TEMPORARY.getCode().equals(vo.getScenario())) {
				this.temporaryUploadText = formatText(vo.getCreateDate(), vo.getCreator());
				this.temporaryInterfaceText = formatText(vo.getUpdateDate(), vo.getUpdater());
			} else if (Scenario.FINAL.getCode().equals(vo.getScenario())) {
				this.finalUploadText = formatText(vo.getCreateDate(), vo.getCreator());
				this.finalInterfaceText = formatText(vo.getUpdateDate(), vo.getUpdater());
			}
		}
	}

	private void setButtonAvailable(boolean... available) {
		this.canTemporaryUpload = available[0];
		this.canTemporaryInterface = available[1];
		this.canFinalUpload = available[2];
		this.canFinalInterface = available[3];
	}

	private String formatText(Date date, String user) {
		if (date == null) {
			return "-";
		}
		return DateFormatUtils.format(date, "yyyy.MM.dd HH:mm:ss") + " - " + user;
	}
}
