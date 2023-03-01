package kr.co.pulmuone.v1.policy.shippingarea.util;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.policy.shippingarea.dto.vo.ShippingAreaExcelUploadInfoVo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ShippingAreaExcelUploadUtil {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ShippingAreaExcelUploadInfoVo setShippingAreaExcelInfo(){
        //Session 정보
        long adminId = 0;
        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null) {
            adminId = Long.valueOf(userVo.getUserId());
        }

        LocalDateTime createTime = LocalDateTime.now();

        ShippingAreaExcelUploadInfoVo infoVo = new ShippingAreaExcelUploadInfoVo();

        infoVo.setCreateId(adminId);                                // 등록자 ID
        infoVo.setCreateDt(createTime.format(dateTimeFormatter));   // 등록일시
        infoVo.setUploadStatusCd(ExcelUploadValidateEnums.UploadStatusCode.UPLOAD_IN_PROGRESS.getCode());

        return infoVo;
    }

    // 성공, 대체배송, 실패 건수 업데이트
    public ShippingAreaExcelUploadInfoVo setShippingAreaExcelResultUpdate(ShippingAreaExcelUploadInfoVo infoVo){
        infoVo.setUploadStatusCd(ExcelUploadValidateEnums.UploadStatusCode.UPLOAD_DONE.getCode());
        return infoVo;
    }
}
