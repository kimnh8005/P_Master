package kr.co.pulmuone.v1.dashboard.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.dashboard.dto.vo.DashboardVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "DashboardResponseDto")
public class DashboardResponseDto {

  @ApiModelProperty(value = "결과코드")
  private String resultCode;

  @ApiModelProperty(value = "결과메시지")
  private String resultMessage;

  @ApiModelProperty(value = "리스트전체건수")
  private long total;

  @ApiModelProperty(value = "리스트")
  private List<?> rows;

  @ApiModelProperty(value = "주문/매출현황")
  private DashboardVo orderSaleStatics;

  @ApiModelProperty(value = "클레임현황")
  private DashboardVo claimStatics;

  @ApiModelProperty(value = "회원가입 현황")
  private DashboardVo signUpStatics;

  @ApiModelProperty(value = "고객 문의 현황")
  private DashboardVo customerQnaStatics;

  @ApiModelProperty(value = "보상제 처리 현황")
  private DashboardVo rewardApplyStatics;

  @ApiModelProperty(value = "부정거래 탐지")
  private DashboardVo illegalDetectStatics;
  
  @ApiModelProperty(value = "내 승인요청 현황")
  private List<DashboardVo> myApprovalRequestStatics;
  
  @ApiModelProperty(value = "내 승인처리 목록")
  private DashboardVo myApprovalAcceptStatics;
  
  @ApiModelProperty(value = "담당자별 승인처리 현황")
  private List<DashboardVo> totApprovalAcceptStatics;

}
