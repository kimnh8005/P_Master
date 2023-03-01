package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExhibitGroupVo {

  public ExhibitGroupVo () {}

  public ExhibitGroupVo (Object userInfo) {
    // 등록자/수정자 Set
    if (userInfo!= null) {
      if (((UserVo)userInfo).getUserId() != null) {
        this.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        this.setCreateId((SessionUtil.getBosUserVO()).getUserId());
      }
      else {
        this.setModifyId("0");
        this.setCreateId("0");
      }
    }
    else {
      this.setModifyId("0");
      this.setCreateId("0");
    }
  }


  @ApiModelProperty(value = "기획전 전시그룹 PK")
  private String evExhibitGroupId;


  @ApiModelProperty(value = "기획전 PK")
  private String evExhibitId;


  @ApiModelProperty(value = "전시그룹명")
  private String groupNm;


  @ApiModelProperty(value = "텍스트 칼라")
  private String textColor;


  @ApiModelProperty(value = "사용여부")
  private String useYn;


  @ApiModelProperty(value = "그룹사용여부")
  private String groupUseYn;


  @ApiModelProperty(value = "전시그룹 배경 유형")
  private String exhibitImgTp;


  @ApiModelProperty(value = "배경 컬러코드")
  private String bgCd;


  @ApiModelProperty(value = "설명")
  private String description;


  @ApiModelProperty(value = "그룹설명")
  private String groupDesc;


  @ApiModelProperty(value = "노출 상품 수")
  private int dispCnt;


  @ApiModelProperty(value = "순서")
  private int groupSort;


  @ApiModelProperty(value = "등록자")
  private String createId;


  @ApiModelProperty(value = "등록일")
  private String createDt;


  @ApiModelProperty(value = "수정자")
  private String modifyId;


  @ApiModelProperty(value = "수정일")
  private String modifyDt;

  // --------------------------------------------------------------------------
  // 상품정보
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "기획전전시그룹상품PK")
  private String evExhibitGroupDetlId;

  @ApiModelProperty(value = "상품순번")
  private int goodsSort;

  @ApiModelProperty(value = "상품PK")
  private String ilGoodsId;

  @ApiModelProperty(value = "상품유형")
  private String goodsTp;

  @ApiModelProperty(value = "상품유형명")
  private String goodsTpNm;

  @ApiModelProperty(value = "상품명")
  private String goodsNm;

  @ApiModelProperty(value = "상품이미지경로")
  private String goodsImagePath;

  @ApiModelProperty(value = "원가")
  private int standardPrice;

  @ApiModelProperty(value = "정상가")
  private int recommendedPrice;

  @ApiModelProperty(value = "판매가")
  private int salePrice;

  @ApiModelProperty(value = "할인유형")
  private String discountTp;

  @ApiModelProperty(value = "할인유형명")
  private String discountTpNm;

  @ApiModelProperty(value = "배송정책 PK")
  private String ilShippingTmplId;

  @ApiModelProperty(value = "배송정책명")
  private String shippingTemplateName;

  @ApiModelProperty(value = "출고처PK")
  private String urWarehouseId;

  @ApiModelProperty(value = "출고처명")
  private String warehouseNm;

  @ApiModelProperty(value = "판매상태")
  private String saleStatus;

  @ApiModelProperty(value = "판매상태명")
  private String saleStatusNm;

  // --------------------------------------------------------------------------
  // 추가항목-상품목록
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "상품리스트String")
  private String groupGoodsListJsonString;


  @ApiModelProperty(value = "상품리스트")
  private List<ExhibitGroupDetlVo> groupGoodsList;


  @ApiModelProperty(value = "전시그룹 배경 유형명")
  private String exhibitImgTpNm;



}
