package kr.co.pulmuone.v1.user.group.dto.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.group.dto.AddItemBenefitDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "회원그룹 Result Vo")
public class UserGroupResultVo {
    // List
    @ApiModelProperty(value = "SEQ")
    private long urGroupId;

    @ApiModelProperty(value = "기본등급")
    private String defaultYn;

    @ApiModelProperty(value = "회원등급명")
    private String groupName;

    @ApiModelProperty(value = "등급레벨 유형 이름")
    private String groupLevelTypeName;

    @ApiModelProperty(value = "매출액조건 from")
    private Long purchaseAmountFrom;

    @ApiModelProperty(value = "매출액조건 to")
    private Long purchaseAmountTo;

    @ApiModelProperty(value = "구매건수조건 from")
    private int purchaseCountFrom;

    @ApiModelProperty(value = " 구매건수조건 to")
    private int purchaseCountTo;

    // Group Popup
    @ApiModelProperty(value = "등급레벨 유형")
    private String groupLevelType;

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

    // Group Benefit
    @ApiModelProperty(value = "쿠폰 목록")
    private List<AddItemBenefitDto> addItemCouponList;

    @ApiModelProperty(value = "적립금 목록")
    private List<AddItemBenefitDto> addItemPointList;

}