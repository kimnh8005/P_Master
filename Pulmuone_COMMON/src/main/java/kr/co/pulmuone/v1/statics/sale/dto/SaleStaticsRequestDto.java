package kr.co.pulmuone.v1.statics.sale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

//@Slf4j
@Getter
@Setter
@ToString
@ApiModel(description = "SaleStaticsRequestDto")
public class SaleStaticsRequestDto extends BaseRequestPageDto {

  @ApiModelProperty(value = "검색기준유형", required = false)
  private String searchTp;

  @ApiModelProperty(value = "VAT포함여부", required = false)
  private String taxYn;

  @ApiModelProperty(value = "기준시작일시", required = false)
  private String startDt;

  @ApiModelProperty(value = "기준시작일자", required = false)
  private String startDe;

  @ApiModelProperty(value = "기준종료일시", required = false)
  private String endDt;

  @ApiModelProperty(value = "기준종료일자", required = false)
  private String endDe;

  @ApiModelProperty(value = "기준시작시간", required = false)
  private String searchStHour;

  @ApiModelProperty(value = "기준종료시간", required = false)
  private String searchEdHour;

  @ApiModelProperty(value = "대비기간시작일시", required = false)
  private String contrastStartDt;

  @ApiModelProperty(value = "대비기간시작일자", required = false)
  private String contrastStartDe;

  @ApiModelProperty(value = "대비기간종료일시", required = false)
  private String contrastEndDt;

  @ApiModelProperty(value = "대비기간종료일자", required = false)
  private String contrastEndDe;

  @ApiModelProperty(value = "상품코드별합산여부", required = false)
  private String goodsSumYn;

  @ApiModelProperty(value = "VAT포함여부", required = false)
  private String vatSumYn;

  @ApiModelProperty(value = "코드유형", required = false)
  private String dpGoodsSearchTp;

  @ApiModelProperty(value = "공급업체ID", required = false)
  private String urSupplierId;

  @ApiModelProperty(value = "출고처그룹코드", required = false)
  private String urWarehouseGrpCd;

  @ApiModelProperty(value = "출고처ID", required = false)
  private String urWarehouseId;

  @ApiModelProperty(value = "판매처그룹ID", required = false)
  private String sellersGroupCd;

  @ApiModelProperty(value = "판매처ID", required = false)
  private String omSellersId;

  @ApiModelProperty(value = "브랜드유형ID", required = false)
  private String brandTp;

  @ApiModelProperty(value = "표준브랜드ID", required = false)
  private String urBrandId;

  @ApiModelProperty(value = "전시브랜드ID", required = false)
  private String dpBrandId;

  @ApiModelProperty(value = "판매채널유형ID", required = false)
  private String agentTypeCd;

  @ApiModelProperty(value = "판매채널유형ID리스트", required = false)
  private List<String> agentTypeCdList;

  @ApiModelProperty(value = "회원유형ID", required = false)
  private String buyerTypeCd;

  @ApiModelProperty(value = "회원유형ID리스트", required = false)
  private List<String> buyerTypeCdList;

  @ApiModelProperty(value = "보관온도유형ID", required = false)
  private String storageMethodTp;

  @ApiModelProperty(value = "카테고리분류", required = false)
  private String ctgryTp;

  @ApiModelProperty(value = "표준카테고리대분류ID", required = false)
  private String ctgryStdIdDepth1;

  @ApiModelProperty(value = "표준카테고리중분류ID", required = false)
  private String ctgryStdIdDepth2;

  @ApiModelProperty(value = "표준카테고리소분류ID", required = false)
  private String ctgryStdIdDepth3;

  @ApiModelProperty(value = "표준카테고리세분류ID", required = false)
  private String ctgryStdIdDepth4;

  @ApiModelProperty(value = "카테고리대분류ID", required = false)
  private String ctgryIdDepth1;

  @ApiModelProperty(value = "카테고리중분류ID", required = false)
  private String ctgryIdDepth2;

  @ApiModelProperty(value = "카테고리소분류ID", required = false)
  private String ctgryIdDepth3;

  @ApiModelProperty(value = "카테고리세분류ID", required = false)
  private String ctgryIdDepth4;

  @ApiModelProperty(value = "상품유형ID", required = false)
  private String goodsTpCd;

  @ApiModelProperty(value = "상품명", required = false)
  private String goodsNm;

  @ApiModelProperty(value = "상품유형ID리스트", required = false)
  private List<String> goodsTpCdList;

  @ApiModelProperty(value = "조회유형(단일조건/복수조건)", required = false)
  private String selectConditionType;

  @ApiModelProperty(value = "검색코드유형", required = false)
  private String goodsSearchTp;

  @ApiModelProperty(value = "키워드", required = false)
  private String keyword;

  @ApiModelProperty(value = "키워드리스트", required = false)
  private List<String> keywordList;

  @ApiModelProperty(value = "조회조건정보", required = false)
  private String searchInfo;

  @ApiModelProperty(value = "표준 푸드머스 보기", required = false)
  private String urFoodmusYn;

  @ApiModelProperty(value = "전시 푸드머스 보기", required = false)
  private String dpFoodmusYn;

  @ApiModelProperty(value = "브랜드 푸드머스 보기", required = false)
  private String foodmusYn;

}
