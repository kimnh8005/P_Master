package kr.co.pulmuone.v1.comm.enums;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import kr.co.pulmuone.v1.calculate.pov.dto.vo.CalPovProcessVo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class PovEnums {

	@Getter
    @RequiredArgsConstructor
    public enum Corporation implements CodeCommEnum {
		PULMUONE_FOOD("11", "식품"),
    	ORGA("45", "올가"),
    	GREEN_JUICE("46", "녹즙"),
    	FOODMERCE("51", "푸드머스"),
		PULMUONE("20", "(주)풀무원")
        ;
        private final String code;
        private final String codeName;

        public static Corporation findByCode(String code) {
			return Arrays.stream(Corporation.values())
		            .filter(corporation -> corporation.getCode().equals(code))
		            .findAny()
		            .orElse(null);

        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum Scenario implements CodeCommEnum {
    	TEMPORARY("T", "가마감"),
    	FINAL("F", "확정마감"),
        ;
        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ProcessStatus implements CodeCommEnum {
    	NONE("NONE", "진행안함"),
    	TEMPORARY_UPLOADED("TEMPORARY_UPLOADED", "가마감 업로드"),
    	TEMPORARY_INTERFACED("TEMPORARY_INTERFACED", "가마감 ERP I/F"),
    	FINAL_UPLOADED("FINAL_UPLOADED", "마감 업로드"),
    	FINAL_INTERFACED("FINAL_INTERFACED", "마감 ERP I/F"),
        ;
        private final String code;
        private final String codeName;

		public static ProcessStatus getStatus(Map<String, CalPovProcessVo> process) {
			if (MapUtils.isEmpty(process)) {
				return NONE;
			}
			if (process.get(Scenario.FINAL.getCode()) != null) {
				return process.get(Scenario.FINAL.getCode()).isInterfaced() ? FINAL_INTERFACED : FINAL_UPLOADED;
			}
			if (process.get(Scenario.TEMPORARY.getCode()) != null) {
				return process.get(Scenario.TEMPORARY.getCode()).isInterfaced() ? TEMPORARY_INTERFACED
						: TEMPORARY_UPLOADED;
			}
			return NONE;
		}

		public static boolean canExcelUpload(String scenario, String status) {
			if (Scenario.TEMPORARY.getCode().equals(scenario)) {
				return status == NONE.getCode() || status == TEMPORARY_UPLOADED.getCode();
			} else if (Scenario.FINAL.getCode().equals(scenario)) {
				return status == TEMPORARY_INTERFACED.getCode() || status == FINAL_UPLOADED.getCode();
			} else {
				return false;
			}
		}

		public static boolean canInterface(String scenario, String status) {
			if (Scenario.TEMPORARY.getCode().equals(scenario)) {
				return status == TEMPORARY_UPLOADED.getCode();
			} else if (Scenario.FINAL.getCode().equals(scenario)) {
				return status == FINAL_UPLOADED.getCode();
			} else {
				return false;
			}
		}
    }

    @Getter
    @RequiredArgsConstructor
    public enum PovAllocationType implements CodeCommEnum {
    	ME("ME", "마케팅 예산", 0, "POV"),
    	OV("OV", "수수료", 1, "POV"),
	    VDC("VDC", "물류비", 2, "POV_VDC"),
		MOGE("MOGE", "인건비", 3, "POV")
        ;
        private final String code;
        private final String codeName;
        private final int sheetIndex;
        private final String excelUploadType;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ExcelUploadResult implements MessageCommEnum {
    	NOT_STATUS_EXCEL_UPLOAD("NOT_STATUS_EXCEL_UPLOAD", "엑셀 업로드가 가능한 상태가 아닙니다."),
    	NOT_UPLOAD_DATA("NOT_UPLOAD_DATA", "업로드할 데이터가 존재하지 않습니다."),
    	NOT_STATUS_INTERFACE("NOT_STATUS_INTERFACE", "인터페이스가 가능한 상태가 아닙니다."),
    	NO_REMOTE_POV_DATA("NO_REMOTE_POV_DATA", "POV에 해당 데이터가 존재하지 않습니다."),

        ;

        private final String code;
        private final String message;
    }
}
