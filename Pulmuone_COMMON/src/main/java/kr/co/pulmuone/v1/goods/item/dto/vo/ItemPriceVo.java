package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemPriceVo {

    @ApiModelProperty(value = "품목 가격 ID")
    private int ilItemPriceId;

    @ApiModelProperty(value = "품목 코드")
    private String ilItemCode;

    @ApiModelProperty(value = "수정유형 명")
    private String updateTypeName;

    @ApiModelProperty(value = "가격 적용 시작일 ( yyyy-MM-dd 포맷 )")
    private String priceApplyStartDate;

    @ApiModelProperty(value = "가격 적용 종료일 ( yyyy-MM-dd 포맷 )")
    private String priceApplyEndDate;

    @ApiModelProperty(value = "원가")
    private int standardPrice;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "삭제 가능 여부 : 현재 적용중인 가격인 경우 삭제 불가")
    private boolean canDeleted;

    @ApiModelProperty(value = "등록자 ID")
    private String createId;

    @ApiModelProperty(value = "수정자 ID")
    private String modifyId;

    @ApiModelProperty(value = "마진율")
    private int priceRatio;

    @ApiModelProperty(value = "요청자")
    private String approval1st;

    @ApiModelProperty(value = "1차/최종")
    private String approvalConfirm;

    @ApiModelProperty(value = "승인구분")
    private String approvalTypeName;

    @ApiModelProperty(value = "승인상태")
    private String approvalStatusCode;

    @ApiModelProperty(value = "승인상태 명")
    private String approvalStatusCodeName;

    @ApiModelProperty(value = "요청자")
    private String approvalRequestUserInfo;

    @ApiModelProperty(value = "1차/최종")
    private String approvalAdminUserInfo;

    @ApiModelProperty(value = "품목가격 승인내역 PK")
    private String ilItemPriceApprId;

    @ApiModelProperty(value = "품목가격 반영 PK")
    private String ilItemPriceOrigId;

    @ApiModelProperty(value = "시스템에 의한 업데이트 유무(Y: 시스템)")
    private String systemUpdateYn;

    @ApiModelProperty(value = "관리자에 의한 업데이트 유무(Y: 관리자)")
    private String managerUpdateYn;

}

