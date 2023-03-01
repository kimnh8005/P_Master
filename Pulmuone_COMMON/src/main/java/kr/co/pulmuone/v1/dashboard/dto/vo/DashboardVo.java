package kr.co.pulmuone.v1.dashboard.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DashboardVo {

  // --------------------------------------------------------------------------
  // 사용자 대시보드 정보
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "사용자 대시보드 정보 PK")
  private String urUserDashboardId;

  @ApiModelProperty(value = "회원 PK")
  private String urUserId;

  @ApiModelProperty(value = "대시보드 카드 코드")
  private String dashboardCardCd;

  @ApiModelProperty(value = "X좌표")
  private String xAxis;

  @ApiModelProperty(value = "Y좌표")
  private String yAxis;

  @ApiModelProperty(value = "정렬")
  private int sort;

  @ApiModelProperty(value = "노출여부")
  private String dispYn;

  @ApiModelProperty(value = "등록일")
  private String createDt;

  @ApiModelProperty(value = "수정일")
  private String modifyDt;

  // --------------------------------------------------------------------------
  // 대시보드 설정 정보
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "대시보드 카드 이름")
  private String dashboardCardNm;

  @ApiModelProperty(value = "데이터 요청 URL")
  private String dataUrl;

  @ApiModelProperty(value = "링크 URL")
  private String linkUrl;

  @ApiModelProperty(value = "카드 너비 비율")
  private String cardWidthRatio;

  @ApiModelProperty(value = "사용여부")
  private String useYn;

  // --------------------------------------------------------------------------
  // 주문/매출현황
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "주문합계건수")
  private long orderSumCount;

  @ApiModelProperty(value = "주문합계총액")
  private long orderSumPrice;

  @ApiModelProperty(value = "주문PC건수")
  private long orderPcCount;

  @ApiModelProperty(value = "주문PC총액")
  private long orderPcPrice;

  @ApiModelProperty(value = "주문Mobile건수")
  private long orderMobileCount;

  @ApiModelProperty(value = "주문Mobile총액")
  private long orderMobilePrice;

  @ApiModelProperty(value = "주문MobileApp건수")
  private long orderMobileAppCount;

  @ApiModelProperty(value = "주문MobileApp총액")
  private long orderMobileAppPrice;

  @ApiModelProperty(value = "주문관리자건수")
  private long orderAdminCount;

  @ApiModelProperty(value = "주문관리자총액")
  private long orderAdminPrice;

  @ApiModelProperty(value = "주문외부몰건수")
  private long orderOutCount;

  @ApiModelProperty(value = "주문외부몰총액")
  private long orderOutPrice;


  @ApiModelProperty(value = "결제완료합계건수")
  private long icSumCount;

  @ApiModelProperty(value = "결제완료합계총액")
  private long icSumPrice;

  @ApiModelProperty(value = "결제완료PC건수")
  private long icPcCount;

  @ApiModelProperty(value = "결제완료PC총액")
  private long icPcPrice;

  @ApiModelProperty(value = "결제완료Mobile건수")
  private long icMobileCount;

  @ApiModelProperty(value = "결제완료Mobile총액")
  private long icMobilePrice;

  @ApiModelProperty(value = "결제완료MobileApp건수")
  private long icMobileAppCount;

  @ApiModelProperty(value = "결제완료MobileApp총액")
  private long icMobileAppPrice;

  @ApiModelProperty(value = "결제완료관리자건수")
  private long icAdminCount;

  @ApiModelProperty(value = "결제완료관리자총액")
  private long icAdminPrice;

  @ApiModelProperty(value = "결제완료외부몰건수")
  private long icOutCount;

  @ApiModelProperty(value = "결제완료외부몰총액")
  private long icOutPrice;

  // --------------------------------------------------------------------------
  // 클레임현황
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "취소요청건수")
  private long caCount;

  @ApiModelProperty(value = "반품요청건수")
  private long raCount;

  @ApiModelProperty(value = "환불대기건수")
  private long faCount;

  @ApiModelProperty(value = "반품(회수)완료건수")
  private long rcReturnCount;

  @ApiModelProperty(value = "반품(즉시)완료건수")
  private long rcDirectCount;

  // --------------------------------------------------------------------------
  // 회원가입 현황
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "전체 총 회원 수")
  private long total;

  @ApiModelProperty(value = "전체 신규가입 수")
  private long todayTotal;

  @ApiModelProperty(value = "일반 총 회원 수")
  private long normal;

  @ApiModelProperty(value = "일반 신규가입 수")
  private long todayNormal;

  @ApiModelProperty(value = "임직원 총 회원 수")
  private long employee;

  @ApiModelProperty(value = "임직원 신규가입 수")
  private long todayEmployee;

  // --------------------------------------------------------------------------
  // 고객 문의 현황
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "상품 문의 접수")
  private long product;

  @ApiModelProperty(value = "상품 문의 답변 지연")
  private long productDelay;

  @ApiModelProperty(value = "1:1 문의 접수")
  private long onetoone;

  @ApiModelProperty(value = "1:1 문의 답변 지연")
  private long onetooneDelay;

  @ApiModelProperty(value = "외부몰 문의 접수")
  private long outmall;

  @ApiModelProperty(value = "외부몰 문의 답변 지연")
  private long outmallDelay;

  // --------------------------------------------------------------------------
  // 보상제 처리 현황
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "신청내역")
  private long reward;

  @ApiModelProperty(value = "보상내역")
  private long completedReward;

  @ApiModelProperty(value = "보상액")
  private long rewardPrice;

  // --------------------------------------------------------------------------
  // 부정거래 탐지
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "거래불가카드")
  private long transactionNotVCard;

  @ApiModelProperty(value = "비정상 회원가입")
  private long userJoin;

  @ApiModelProperty(value = "비정상 로그인")
  private long loginFail;

  @ApiModelProperty(value = "비정상주문(결제 횟수)")
  private long orderCount;

  @ApiModelProperty(value = "비정상주문(결제 금액)")
  private long orderPrice;

  @ApiModelProperty(value = "도난분실카드")
  private long stolenLostCard;

  // --------------------------------------------------------------------------
  // 내 승인요청 현황 / 내 승인처리 목록 / 담당자별 승인처리 현황
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "승인상태")
  private String apprStat;

  @ApiModelProperty(value = "담당자 이름 / ID")
  private String apprUser;

  //승인완료
  @ApiModelProperty(value = "승인처리 잔여현황")
  private int totalCnt;

  @ApiModelProperty(value = "품목등록")
  private int itemRegistCnt;

  @ApiModelProperty(value = "품목가격")
  private int itemPriceCnt;

  @ApiModelProperty(value = "상품등록")
  private int goodsRegistCnt;

  @ApiModelProperty(value = "상품할인")
  private int goodsDiscountCnt;

  @ApiModelProperty(value = "균일가")
  private int exhibitSelectCnt;

  @ApiModelProperty(value = "증정행사")
  private int exhibitGiftCnt;

  @ApiModelProperty(value = "쿠폰")
  private int couponCnt;

  @ApiModelProperty(value = "적립금")
  private int pointCnt;

  @ApiModelProperty(value = "CS환불")
  private int csRefundCnt;

  @ApiModelProperty(value = "거래처품목")
  private int itemClientCnt;

  @ApiModelProperty(value = "거래처상품")
  private int goodsClientCnt;


}
