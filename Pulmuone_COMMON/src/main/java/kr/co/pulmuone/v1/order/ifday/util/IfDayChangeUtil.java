package kr.co.pulmuone.v1.order.ifday.util;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelInfoVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class IfDayChangeUtil {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public IfDayExcelInfoVo setIfDayExcelInfo() {
        //Session 정보
        long adminId = 0;
        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null) {
            adminId = Long.valueOf(userVo.getUserId());
        }
        IfDayExcelInfoVo infoVo = new IfDayExcelInfoVo();

        LocalDateTime startTime = LocalDateTime.now();
        infoVo.setUploadStartDateTime(startTime.format(dateTimeFormatter));
        infoVo.setUploadStatusCode(ExcelUploadValidateEnums.UploadStatusCode.UPLOAD_IN_PROGRESS.getCode());
        infoVo.setCreateId(adminId);
        return infoVo;
    }

    public IfDayExcelInfoVo setIfDayExcelUpdateInfo(IfDayExcelInfoVo infoVo, int successCnt, int failCnt) {
        infoVo.setSuccessCount(successCnt);
        infoVo.setFailCount(failCnt);
        LocalDateTime endTime = LocalDateTime.now();
        infoVo.setUploadEndDateTime(endTime.format(dateTimeFormatter));
        infoVo.setUploadExecutionTime((int) ChronoUnit.SECONDS.between(LocalDateTime.parse(infoVo.getUploadStartDateTime(), dateTimeFormatter), endTime));
        infoVo.setUploadStatusCode(ExcelUploadValidateEnums.UploadStatusCode.UPLOAD_DONE.getCode());
        return infoVo;
    }

    /**
     * 수집몰 타입
     * @param row
     * @return String
     */
    public OutMallExcelInfoVo getOutmallType(OutMallExcelInfoVo infoVo, Row row) {
        String returnOutmallType = "";
            Cell cell = row.getCell(0);

            if (cell.getRichStringCellValue().getString().trim().equals("합포번호")) {
                returnOutmallType = ExcelUploadEnums.ExcelUploadType.EASYADMIN.getCode();
            } else if (cell.getRichStringCellValue().getString().trim().equals("수집몰주문번호")) {
                returnOutmallType = ExcelUploadEnums.ExcelUploadType.SABANGNET.getCode();
            }

        infoVo.setOutMallType(returnOutmallType);
        return infoVo;
    }

}
