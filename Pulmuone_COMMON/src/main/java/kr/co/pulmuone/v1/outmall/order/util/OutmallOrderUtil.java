package kr.co.pulmuone.v1.outmall.order.util;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutmallOrderUtil {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OutMallExcelInfoVo setOutmallExcelInfo() {
        //Session 정보
        long adminId = 0;
        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null) {
            adminId = Long.valueOf(userVo.getUserId());
        }
        OutMallExcelInfoVo infoVo = new OutMallExcelInfoVo();

        LocalDateTime startTime = LocalDateTime.now();
        infoVo.setUploadStartDateTime(startTime.format(dateTimeFormatter));
        infoVo.setUploadStatusCode(ExcelUploadValidateEnums.UploadStatusCode.UPLOAD_IN_PROGRESS.getCode());
        infoVo.setCreateId(adminId);
        return infoVo;
    }

    public void setOutmallExcelUpdateInfo(OutMallExcelInfoVo infoVo, int successCnt, int failCnt) {
        infoVo.setSuccessCount(successCnt);
        infoVo.setFailCount(failCnt);
        LocalDateTime endTime = LocalDateTime.now();
        infoVo.setUploadEndDateTime(endTime.format(dateTimeFormatter));
        infoVo.setUploadExecutionTime((int) ChronoUnit.SECONDS.between(LocalDateTime.parse(infoVo.getUploadStartDateTime(), dateTimeFormatter), endTime));

        if(successCnt == 0) {
            infoVo.setUploadStatusCode(ExcelUploadValidateEnums.UploadStatusCode.UPLOAD_FAILED.getCode());
        } else {
            infoVo.setUploadStatusCode(ExcelUploadValidateEnums.UploadStatusCode.UPLOAD_DONE.getCode());
        }
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


    public static  List<Map<String, Object>> setCollectionMallFail(List<Map<String, Object>> collectionMalllFailList, String succId, String failMessage) {
        List<Map<String, Object>> returnList = new ArrayList<>();

        boolean existsFlag = false;
        for (Map<String, Object> item : collectionMalllFailList){
            Map<String, Object> collectionMalllFailMap = new HashMap<>();
            collectionMalllFailMap.put("succId",                item.get("succId"));
            collectionMalllFailMap.put("failMessage",           item.get("failMessage"));

            if(succId.equals(item.get("succId"))){
                if (!"".equals(failMessage.trim())) {
                    collectionMalllFailMap.put("failMessage", item.get("failMessage") + " / " + failMessage);
                    existsFlag = true;
                }
            }

            returnList.add(collectionMalllFailMap);
        }

        if (existsFlag != true && !"".equals(failMessage.trim())){
            Map<String, Object> collectionMalllFailMap = new HashMap<>();
            collectionMalllFailMap.put("succId",            succId);
            collectionMalllFailMap.put("failMessage",       failMessage);
            returnList.add(collectionMalllFailMap);
        }
        return returnList;
    }


}
