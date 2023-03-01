package kr.co.pulmuone.v1.user.group.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupBenefitVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GroupListVo {

    @ApiModelProperty(value = "그룹 PK")
    private Long urGroupId;

    @ApiModelProperty(value = "그룹명")
    private String groupName;

    @ApiModelProperty(value = "등급 이미지")
    private String topImagePath;

    @ApiModelProperty(value = "리스트 이미지")
    private String listImagePath;

    @ApiModelProperty(value = "매출액조건 from")
    private int purchaseAmountFrom;

    @ApiModelProperty(value = "매출액조건 to")
    private String purchaseAmountTo;

    @ApiModelProperty(value = "구매건수조건 from")
    private int purchaseCountFrom;

    @ApiModelProperty(value = "구매건수조건 to")
    private String purchaseCountTo;

    @ApiModelProperty(value = "혜택")
    List<GroupBenefitVo> benefit;
}
