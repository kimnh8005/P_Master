package kr.co.pulmuone.v1.user.group.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GroupInfoByUserResponseDto {

    @ApiModelProperty(value = "이달의 등급명")
    private String groupName;

    @ApiModelProperty(value = "등급 아이콘")
    private String topImagePath;

    @ApiModelProperty(value = "리스트 이미지")
    private String listImagePath;

    @ApiModelProperty(value = "주문 건수")
    private int orderCount = 0;

    @ApiModelProperty(value = "주문 금액")
    private int orderPrice = 0;

    @ApiModelProperty(value = "등급 산정기간 시작")
    private String calculatePeriodStart;

    @ApiModelProperty(value = "등급 산정기간 종료")
    private String calculatePeriodEnd;

    @ApiModelProperty(value = "예상 등급명")
    private String estimationGroupName;

    @ApiModelProperty(value = "예상 등급 이미지")
    private String estimationTopImagePath;

    @ApiModelProperty(value = "예상 등급 이미지")
    private String estimationListImagePath;

    @ApiModelProperty(value = "")
    private List<GroupListVo> group;

}
