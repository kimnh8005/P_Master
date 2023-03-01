package kr.co.pulmuone.v1.user.group.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserGroupMasterRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "회원그룹 마스터명")
    private String groupMasterName;

    @ApiModelProperty(value = "산정기간(months)")
    private String calculatePeriod;

    @ApiModelProperty(value = "적용시작 년도")
    private int startDateYear;

    @ApiModelProperty(value = "적용시작 월")
    private int startDateMonth;

    @ApiModelProperty(value = "적용종료일")
    private String endDate;

    @ApiModelProperty(value = "회원그룹 마스터명 PK")
    private Long urGroupMasterId;

    @ApiModelProperty(value = "SEQ")
    private Long urGroupId;

    @ApiModelProperty(value = "회원그룹유형(UR_GROUP_TP.NORMAL)")
    private String userGroupType;

    @ApiModelProperty(value = "사용여부(Y:사용)")
    private String useYn;

    @ApiModelProperty(value = "회원그룹등급 등급레벨")
    private String groupLevel;

    @ApiModelProperty(value = "회원그룹명 회원등급명")
    private String groupName;

    @ApiModelProperty(value = "쿠폰사용 가능여부(Y:사용가능)")
    private String useCouponYn;

    @ApiModelProperty(value = "적립금 가능여부(Y:사용가능)")
    private String usePointYn;

    @ApiModelProperty(value = "매출액조건 from")
    private String purchaseAmountFrom;

    @ApiModelProperty(value = "매출액조건 to")
    private String purchaseAmountTo;

    @ApiModelProperty(value = "구매건수조건 from")
    private String purchaseCountFrom;

    @ApiModelProperty(value = " 구매건수조건 to")
    private String purchaseCountTo;

    @ApiModelProperty(value = "유지기간(months)")
    private String maintainPeriod;

    @ApiModelProperty(value = "적용시작일")
    private String startDate;

    @ApiModelProperty(value = "상단 등급아이콘(원본 파일명)")
    private String topImageOriginalName;

    @ApiModelProperty(value = "상단 등급아이콘(파일명)")
    private String topImageName;

    @ApiModelProperty(value = "상단 등급아이콘(파일 전체 경로)")
    private String topImagePath;

    @ApiModelProperty(value = "리스트 등급아이콘(원본 파일명)")
    private String listImageOriginalName;

    @ApiModelProperty(value = "리스트 등급아이콘(파일명)")
    private String listImageName;

    @ApiModelProperty(value = "리스트 등급아이콘(파일 전체 경로)")
    private String listImagePath;

    @ApiModelProperty(value = "레벨 갯수")
    private int groupLevelCount;

    @ApiModelProperty(value = "회원등급명 갯수")
    private int groupNameCount;

    @ApiModelProperty(value = "쿠폰 등록 목록", required = false)
    private List<AddItemBenefitDto> addItemCouponList;

    @ApiModelProperty(value = "쿠폰 삭제 목록", required = false)
    private List<AddItemBenefitDto> delItemCouponList;

    @ApiModelProperty(value = "적립금 목록", required = false)
    private List<AddItemBenefitDto> addItemPointList;

    @ApiModelProperty(value = "업로드 파일 정보(json형식)")
    private String addFile;

    @ApiModelProperty(value = "업로드 파일 목록")
    List<FileVo> addFileList;

}


