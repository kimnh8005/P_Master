package kr.co.pulmuone.v1.order.ifday.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IfDayExcelInfoVo {

    @ApiModelProperty(value = "I/F 일자 변경 엑셀 정보 PK")
    private long ifIfDayExcelInfoId;

    @ApiModelProperty(value = "전체 주문 건수")
    private int totalCount = 0;

    @ApiModelProperty(value = "정상 건수")
    private int successCount = 0;

    @ApiModelProperty(value = "실패 건수")
    private int failCount = 0;

    @ApiModelProperty(value = "업데이트 건수")
    private int updateCount = 0;

    @ApiModelProperty(value = "업로드 시작 일자")
    private String uploadStartDateTime;

    @ApiModelProperty(value = "업로드 종료 일자")
    private String uploadEndDateTime;

    @ApiModelProperty(value = "업로드 처리시간 초단위")
    private int uploadExecutionTime;

    @ApiModelProperty(value = "연동상태")
    private String uploadStatusCode;

    @ApiModelProperty(value = "연동상태 명")
    private String uploadStatusCodeName;

    @ApiModelProperty(value = "배치 처리 상태")
    private String batchStatusCode = ExcelUploadValidateEnums.BatchStatusCode.BATCH_READY.getCode();

    @ApiModelProperty(value = "관리자 urUserId")
    private long createId;

    @ApiModelProperty(value = "관리자 명")
    @UserMaskingUserName
    private String createName;

    @ApiModelProperty(value = "주문생성건수")
    private String orderCount;

    @ApiModelProperty(value = "Batch 시작일시")
    private String batchStartDateTime;

    @ApiModelProperty(value = "Batch 종료일시")
    private String batchEndDateTime;

    @ApiModelProperty(value = "Batch 처리시간")
    private String batchExecutionTime;

    @ApiModelProperty(value = "Batch 상태")
    private String batchStatusCd;

    @ApiModelProperty(value = "등록일자")
    private String createDateName;

    @ApiModelProperty(value = "관리자 로그인 ID")
    @UserMaskingLoginId
    private String loginId;

    @ApiModelProperty(value = "업로드 데이터 (JSON 형식)")
    private String uploadJsonData;

    @ApiModelProperty(value = "업로드 파일명")
    private String fileNm;
}
