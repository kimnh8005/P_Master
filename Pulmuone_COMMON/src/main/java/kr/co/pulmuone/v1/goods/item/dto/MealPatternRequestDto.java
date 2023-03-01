package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternDetailListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternGoodsListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealSchedulelListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 패턴 MealPatternRequestDto")
public class MealPatternRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "식단패턴기본정보 코드")
    private String patternCd;

	@ApiModelProperty(value = "식단패턴기본정보 코드 리스트")
    private ArrayList<String> patternCdList;

	@ApiModelProperty(value = "식단패턴 명")
    private String patternNm;

	@ApiModelProperty(value = "식단분류코드 (몰 구분 : MALL_DIV.BABYMEAL, MALL_DIV.EATSLIM)")
    private String mallDiv;

	@ApiModelProperty(value = "식단분류 명 (몰 구분 : MALL_DIV.BABYMEAL, MALL_DIV.EATSLIM)")
    private String mallDivNm;

	@ApiModelProperty(value = "패턴 시작일")
    private String patternStartDt;

	@ApiModelProperty(value = "패턴 종료일")
    private String patternEndDt;

	@ApiModelProperty(value = "등록자 ID")
    private long createId;

	@ApiModelProperty(value = "등록일")
    private String createDt;

	@ApiModelProperty(value = "수정자 ID")
    private long modifyId;

	@ApiModelProperty(value = "수정일")
    private String modifyDt;

	@ApiModelProperty(value = "연결상품 정보 (상품아이디, 상품명)")
    private String ilGoodsIdInfo;

	@ApiModelProperty(value = "식단품목코드")
    private String mealContsCd;
	
	@ApiModelProperty(value = "일괄여부")
    private String bulkUpdateYn;
	
	@ApiModelProperty(value = "연결상품 Vo")
    private List<MealPatternGoodsListVo> patternGoodsList;
	
	@ApiModelProperty(value = "패턴정보 Vo")
    private List<MealPatternDetailListVo> patternDetlList;
	
	@ApiModelProperty(value = "스케쥴정보 Vo")
    private List<MealSchedulelListVo> mealSchedulelList;

	// 식단 스케쥴관리 리스트 검색조건
    @ApiModelProperty(value = "코드검색 검색기준")
	private String searchSelect;

    @ApiModelProperty(value = "코드검색 키워드")
	private String findKeyword;

    @ApiModelProperty(value = "코드검색 키워드 Array")
	private ArrayList<String> findKeywordArray;

	//패턴별 상세 등록/수정
    @ApiModelProperty(value = "식단패턴 상세 ID")
    private long patternDetlId;

    @ApiModelProperty(value = "기존 식단품목코드")
    private String originMealContsCd;

    @ApiModelProperty(value = "패턴상세정보 업데이트 Vo")
    private MealPatternDetailListVo updatePatternData;
    
    @ApiModelProperty(value = "패턴상세정보 업데이트 패턴상세ID 리스트")
    private List<String> updatePatternDetlIdList;

    @ApiModelProperty(value = "기간검색유형")
    private String dateSearchType;

    @ApiModelProperty(value = "기간검색 시작일자")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간검색 종료일자")
    private String dateSearchEnd;
}
