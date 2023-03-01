package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.ExhibitUserGroupByUserVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.GiftByUserVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.NormalByUserVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.SelectByUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ExhibitValidationRequestDto {

    @ApiModelProperty(value = "상시진행여부")
    private String alwaysYn;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "WEB PC 전시여부")
    private String displayWebPcYn;

    @ApiModelProperty(value = "WEB Mobile 전시여부")
    private String displayWebMobileYn;

    @ApiModelProperty(value = "APP 전시여부")
    private String displayAppYn;

    @ApiModelProperty(value = "비회원 노출 여부")
    private String displayNonmemberYn;

    @ApiModelProperty(value = "임직원 전용 유형")
    private String evEmployeeType;

    @ApiModelProperty(value = "접근권한 설정 유형")
    private List<ExhibitUserGroupByUserVo> userGroupList;

    @ApiModelProperty(value = "디바이스 유형")
    private String deviceType;

    @ApiModelProperty(value = "유저 상태")
    private String userStatus;

    @ApiModelProperty(value = "유저 등급 PK")
    private Long urGroupId;

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

    public ExhibitValidationRequestDto(){}

    public ExhibitValidationRequestDto(ExhibitRequestDto dto, NormalByUserVo vo) {
        this.alwaysYn = vo.getAlwaysYn();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.displayNonmemberYn = vo.getDisplayNonmemberYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.urUserId = dto.getUrUserId();
    }

    public ExhibitValidationRequestDto(ExhibitRequestDto dto, SelectByUserVo vo) {
        this.alwaysYn = vo.getAlwaysYn();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.displayNonmemberYn = vo.getDisplayNonmemberYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.urUserId = dto.getUrUserId();
    }

    public ExhibitValidationRequestDto(SelectOrderRequestDto dto, SelectByUserVo vo) {
        this.alwaysYn = vo.getAlwaysYn();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.displayNonmemberYn = vo.getDisplayNonmemberYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.urUserId = dto.getUrUserId();
    }

    public ExhibitValidationRequestDto(ExhibitRequestDto dto, GiftByUserVo vo) {
        this.alwaysYn = vo.getAlwaysYn();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.displayWebPcYn = vo.getDisplayWebPcYn();
        this.displayWebMobileYn = vo.getDisplayWebMobileYn();
        this.displayAppYn = vo.getDisplayAppYn();
        this.displayNonmemberYn = vo.getDisplayNonmemberYn();
        this.evEmployeeType = vo.getEvEmployeeType();
        this.userGroupList = vo.getUserGroupList();
        this.deviceType = dto.getDeviceType();
        this.userStatus = dto.getUserStatus();
        this.urGroupId = dto.getUrGroupId();
        this.urUserId = dto.getUrUserId();
    }

}
