package kr.co.pulmuone.v1.outmall.order.dto.vo;

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
public class OutMallExcelInfoVo {

    @ApiModelProperty(value = "외부몰 엑셀 정보 PK")
    private long ifOutmallExcelInfoId;

    @ApiModelProperty(value = "외부몰 타입")
    private String outMallType;

    @ApiModelProperty(value = "외부몰 타입 명")
    private String outMallTypeName;

    @ApiModelProperty(value = "전체 주문 건수")
    private int totalCount = 0;

    @ApiModelProperty(value = "정상 주문 건수")
    private int successCount = 0;

    @ApiModelProperty(value = "실패 주문 건수")
    private int failCount = 0;

    @ApiModelProperty(value = "주문 생성 건수")
    private int orderCreateCnt;

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

    @ApiModelProperty(value = "주문생성 라인 건수")
    private String orderCreateDetlCount;

    @ApiModelProperty(value = "업로드 실패 건수")
    private String uploadFailCount;

    @ApiModelProperty(value = "주문생성 실패 건수")
    private String batchFailCount;

    @ApiModelProperty(value = "업로드 파일명")
    private String fileNm;
}
