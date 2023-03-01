package kr.co.pulmuone.v1.dashboard.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.dashboard.dto.vo.DashboardVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

//@Slf4j
@Getter
@Setter
@ToString
@ApiModel(description = "DashboardRequestDto")
public class DashboardRequestDto extends BaseRequestPageDto {

  @ApiModelProperty(value = "회원PK", required = false)
  private String urUserId;

  @ApiModelProperty(value = "대시보드리스트", required = false)
  List<DashboardVo> dashboardList;

  @ApiModelProperty(value = "몰구분", required = false)         // 공통코드(MALL_TP) : 전체/통합몰(MALL_TP.MALL)/외부몰(MALL_TP.OUTSOURCE)
  private String mallTp;

  @ApiModelProperty(value = "조회기간구분", required = false)   // 조회기간구분(SEARCH_PERIOD_SE) : 오늘(TD), 1시간(1H), 3시간(3H), 6시간(6H), 12시간(12H), 1일(1D)
  private String searchPeriodSe;

  @ApiModelProperty(value = "조회기간기준")
  private LocalDateTime searchPeriodFrom;

  @ApiModelProperty(value = "사용자 대시보드 정보 PK")
  private String urUserDashboardId;

}
