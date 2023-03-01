package kr.co.pulmuone.v1.company.dmmail.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailGroupVo;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "DmMailManageRequestDto")
public class DmMailManageRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "DM메일 PK", required = false)
    private String dmMailId;

    @ApiModelProperty(value = "DM 유형")
    private String dmMailTemplateTp;

    @ApiModelProperty(value = "DM메일 전시그룹 PK", required = false)
    private String dmMailGroupId;

    @ApiModelProperty(value = "DM메일 전시그룹 상품 PK", required = false)
    private String dmMailGroupDetlId;

    @ApiModelProperty(value = "제목", required = false)
    private String title;

    // DM메일기본
    @ApiModelProperty(value = "DM메일기본정보", required = false)
    private DmMailVo dmMailInfo;

    @ApiModelProperty(value = "DM메일기본정보JsonString", required = false)
    private String dmMailDataJsonString;

    // 전시그룹
    @ApiModelProperty(value = "DM메일그룹정보", required = false)
    private DmMailGroupVo dmMailGroupInfo;

    @ApiModelProperty(value = "DM메일그룹정보JsonString", required = false)
    private String dmMailGroupInfoJsonString;

    @ApiModelProperty(value = "DM메일IDString", required = false)
    private String dmMailIdListString;

    @ApiModelProperty(value = "DM메일ID목록", required = false)
    private List<String> dmMailIdList;

    // --------------------------------------------------------------------------
    // 등록 관련
    // --------------------------------------------------------------------------
    @ApiModelProperty(value = "그룹리스트String", required = false)
    private String groupListJsonString;

    @ApiModelProperty(value = "그룹리스트", required = false)
    private List<DmMailGroupVo> groupList;

    // --------------------------------------------------------------------------
    // 검색조건
    // --------------------------------------------------------------------------
    @ApiModelProperty(value = "검색 DM 코드", required = false)
    private String searchDmMailId;

    @ApiModelProperty(value = "검색 DM 구분", required = false)
    private String searchTemplateTp;

    @ApiModelProperty(value = "검색 관리자 제목", required = false)
    private String searchTitle;

    @ApiModelProperty(value = "검색구분", required = false)
    private String searchType;

    @ApiModelProperty(value = "키워드", required = false)
    private String searchText;

    @ApiModelProperty(value = "상품번호", required = false)
    private String searchGoodsId;

    @ApiModelProperty(value = "등록자", required = false)
    private String createId;

    @ApiModelProperty(value = "수정자", required = false)
    private String modifyId;

    @ApiModelProperty(value = "시작일자", required = false)
    private String startDt;

    @ApiModelProperty(value = "종료일자", required = false)
    private String endDt;

    // --------------------------------------------------------------------------
    // VO Set
    // --------------------------------------------------------------------------
    // DM메일기본정보 -> DmMailVo 변환
    public void convertDmMailDataObject() throws BaseException {

        try {
            ObjectMapper objMqpper = new ObjectMapper();
            this.dmMailInfo = objMqpper.readValue(dmMailDataJsonString, DmMailVo.class);
        }
        catch (Exception e) {
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_JSON);      // TODO UserEnums에 추가
        }
    }

    // 전시그룹정보 -> DmMailGroupVo 변환
    public void convertDmMailGroupDataObject() throws BaseException {

        try {
            ObjectMapper objMqpper = new ObjectMapper();
            this.dmMailGroupInfo = objMqpper.readValue(dmMailGroupInfoJsonString, DmMailGroupVo.class);
        }
        catch (Exception e) {
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_JSON);      // TODO UserEnums에 추가
        }
    }
}
