package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventExperienceVo {

  public EventExperienceVo () {}

  public EventExperienceVo (Object userInfo) {
    // 등록자/수정자 Set
    if (userInfo!= null) {
      if (((UserVo)userInfo).getUserId() != null) {

        this.setModifyId(Integer.parseInt((SessionUtil.getBosUserVO()).getUserId()));
        this.setCreateId(Integer.parseInt((SessionUtil.getBosUserVO()).getUserId()));
      }
      else {
        this.setModifyId(0);
        this.setCreateId(0);
      }
    }
    else {
      this.setModifyId(0);
      this.setCreateId(0);
    }
  }


  @ApiModelProperty(value = "체험단 PK")
  private String evEventExperienceId;


  @ApiModelProperty(value = "이벤트  PK")
  private String evEventId;


  @ApiModelProperty(value = "당첨자 선정기간 시작일시")
  private String selectStartDt;


  @ApiModelProperty(value = "당첨자 선정기간 종료일시")
  private String selectEndDt;


  @ApiModelProperty(value = "후기작성 시작일시")
  private String feedbackStartDt;


  @ApiModelProperty(value = "후기작성 종료일시")
  private String feedbackEndDt;


  @ApiModelProperty(value = "당첨자 설정 유형")
  private String eventDrawTp;


  @ApiModelProperty(value = "모집 종료 여부")
  private String recruitCloseYn;


  @ApiModelProperty(value = "선착순 당첨 인원")
  private int firstComeCnt;


  @ApiModelProperty(value = "선착순 마감후 자동종료 여부")
  private String firstComeCloseYn;


  @ApiModelProperty(value = "댓글 분류 설정여부")
  private String commentCodeYn;


  @ApiModelProperty(value = "참여버튼 BG color code")
  private String btnColorCd;


  @ApiModelProperty(value = "쿠폰 PK")
  private String pmCouponId;


  @ApiModelProperty(value = "쿠폰BOS명")
  private String bosCouponNm;


  @ApiModelProperty(value = "쿠폰전시명")
  private String displayCouponNm;

  //@ApiModelProperty(value = "당첨자 공지사항")
  //private String winnerNotice;


  @ApiModelProperty(value = "체험 상품 PK")
  private String ilGoodsId;


  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "상품유형")
  private String goodsTp;

  @ApiModelProperty(value = "상품유형명")
  private String goodsTpNm;

  @ApiModelProperty(value = "상품명")
  private String goodsNm;

  @ApiModelProperty(value = "상품이미지경로")
  private String goodsImagePath;

  @ApiModelProperty(value = "출고처PK")
  private String urWarehouseId;

  @ApiModelProperty(value = "출고처명")
  private String warehouseNm;

  @ApiModelProperty(value = "상품가격PK")
  private String ilGoodsPriceId;

  @ApiModelProperty(value = "원가")
  private int standardPrice;

  @ApiModelProperty(value = "정상가")
  private int recommendedPrice;

  @ApiModelProperty(value = "판매가")
  private int salePrice;


  @ApiModelProperty(value = "등록자")
  private int createId;

  @ApiModelProperty(value = "등록일")
  private String createDt;

  @ApiModelProperty(value = "수정자")
  private int modifyId;

  @ApiModelProperty(value = "수정일")
  private String modifyDt;


  // --------------------------------------------------------------------------
  //
  // --------------------------------------------------------------------------


  // --------------------------------------------------------------------------
  //
  // --------------------------------------------------------------------------


  // --------------------------------------------------------------------------
  //
  // --------------------------------------------------------------------------


  // --------------------------------------------------------------------------
  //
  // --------------------------------------------------------------------------




}
