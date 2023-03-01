package kr.co.pulmuone.v1.company.dmmail.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserNameLoginId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DmMailVo {
    @ApiModelProperty(value = "기획전 PK")
    private String dmMailId;

    @ApiModelProperty(value = "DM 구분")
    private String dmMailTemplateTp;

    @ApiModelProperty(value = "DM 구분명")
    private String dmMailTemplateNm;

    @ApiModelProperty(value = "관리자 제목")
    private String title;

    @ApiModelProperty(value = "컨텐츠")
    private String description;

    @ApiModelProperty(value = "메일 컨텐츠")
    private String mailContents;

    @ApiModelProperty(value = "상품전시 여부")
    private String goodsDispYn;

    @ApiModelProperty(value = "발송 예정일")
    private String sendDt;

    @ApiModelProperty(value = "삭제여부")
    private String delYn;

    @ApiModelProperty(value = "등록자")
    @UserMaskingLoginId
    private String createId;

    @ApiModelProperty(value = "등록자")
    @UserMaskingUserNameLoginId
    private String createNm;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "수정자")
    @UserMaskingLoginId
    private String modifyId;

    @ApiModelProperty(value = "수정자명")
    @UserMaskingUserNameLoginId
    private String modifyNm;

    @ApiModelProperty(value = "수정일")
    private String modifyDt;

    @ApiModelProperty(value = "전시그룹")
    private List<DmMailGroupVo> groupInfoList;
}
