package kr.co.pulmuone.v1.base.dto.vo;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원그룹 조회 Result")
public class GetUserGroupListResultVo {

    private String groupId;

    private String urGroupMasterId;

    private String groupName;

    private String purchaseAmountFrom;

    private String purchaseCountFrom;

    private String calculatePeriod;

    private String defaultYn;

}
