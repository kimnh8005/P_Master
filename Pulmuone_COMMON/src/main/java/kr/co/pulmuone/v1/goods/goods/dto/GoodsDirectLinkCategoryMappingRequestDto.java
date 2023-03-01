package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDirectLinkCategoryMappingListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "네이버 직연동 표준 카테고리 Request")
public class GoodsDirectLinkCategoryMappingRequestDto extends BaseRequestPageDto{

    // # 조회조건 -------------------------------------------------------------------------------------------------------
	@ApiModelProperty(value = "단일조건 _ 복수조건 검색")
	private String searchType;

    @ApiModelProperty(value = "표준카테고리 대분류")
	private String categoryStandardDepth1;

    @ApiModelProperty(value = "표준카테고리 중분류")
    private String categoryStandardDepth2;

    @ApiModelProperty(value = "표준카테고리 소분류")
    private String categoryStandardDepth3;

    @ApiModelProperty(value = "표준카테고리 세분류")
    private String categoryStandardDepth4;

    @ApiModelProperty(value = "매핑여부")
    private String mappingYn;


    // # 그리드 ---------------------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "직연동몰")
    private String gearCateType;

    @ApiModelProperty(value = "표준 카테고리 코드")
    private String ilCtgryStdId;

    @ApiModelProperty(value = "풀무원 표준 카테고리")
    private String ilCtgryStdFullName;

    @ApiModelProperty(value = "카테고리 코드")
    private String categoryId;

    @ApiModelProperty(value = "직연동몰 카테고리")
    private String categoryName;

    // # 표준 카테고리 매핑 ----------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "카테고리 코드")
    private String code;

    @ApiModelProperty(value = "카테고리명")
    private String name;

    @ApiModelProperty(value = "카테고리 전체명")
    private String fullCategoryName;

    @ApiModelProperty(value = "마지막 카테고리 여부")
    private String lastYn;

    @ApiModelProperty(value = "depth 구분")
    private int depth;

    @ApiModelProperty(value = "(대)카테고리")
    private String categoryId1;
    private String ctgryNmEx1;

    @ApiModelProperty(value = "(중)카테고리")
    private String categoryId2;
    private String ctgryNmEx2;

    @ApiModelProperty(value = "(소)카테고리")
    private String categoryId3;
    private String ctgryNmEx3;

    @ApiModelProperty(value = "(세)카테고리")
    private String categoryId4;
    private String ctgryNmEx4;

    @ApiModelProperty(value = "표준카테고리ID")
    private String ilCtgryId;

    @ApiModelProperty(value = "등록자")
    private String createId;

    @ApiModelProperty(value = "수정자")
    private String modifyId;




}
