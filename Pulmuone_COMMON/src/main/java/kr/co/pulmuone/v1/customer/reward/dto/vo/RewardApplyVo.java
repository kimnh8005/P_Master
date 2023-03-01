package kr.co.pulmuone.v1.customer.reward.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingEmail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromMypageRewardVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RewardApplyVo {

    @ApiModelProperty(value = "보상제명")
    private String rewardName;

    @ApiModelProperty(value = "유의사항")
    private String rewardNotice;

    @ApiModelProperty(value = "신청기준")
    private String rewardApplyStandard;

    @ApiModelProperty(value = "적용대상상품 유형")
    private String rewardGoodsType;

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문 상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "배송예정일")
    private String deliveryDate;

    @ApiModelProperty(value = "배송유형 코드")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "주문 배송비")
    private String odShippingPriceId;

    @ApiModelProperty(value = "상품유형")
    private String packType;

    @ApiModelProperty(value = "타이틀")
    private String packTitle;

    @ApiModelProperty(value = "이미지경로")
    private String thumbnailPath;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "주문상품건수(현재상품제외)")
    private Integer orderGoodsCount;

    @ApiModelProperty(value = "신청사유")
    private String rewardApplyContent;

    @ApiModelProperty(value = "답변알림 이메일")
    private String answerMailYn;

    @ApiModelProperty(value = "답변알림 SMS")
    private String answerSmsYn;

    @ApiModelProperty(value = "처리상태")
    private String rewardApplyStatus;

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

    @ApiModelProperty(value = "요청 유저 PK")
    private Long requestUrUserId;

    @ApiModelProperty(value = "파일정보")
    private List<RewardAttcVo> file;

    @ApiModelProperty(value = "보상제 신청관리 ID")
    private String csRewardApplyId;

    @ApiModelProperty(value = "보상제 ID")
    private Long csRewardId;

    @ApiModelProperty(value = "보상제명")
    private String rewardNm;

    @ApiModelProperty(value = "신청대상 주문번호")
    private String qnaDivision;

    @ApiModelProperty(value = "신청대상 상품명")
    private String qnaDivisionName;

    @ApiModelProperty(value = "신청대상 상품 ID")
    private String qnaTitle;

    @ApiModelProperty(value = "회원명")
    @UserMaskingUserName
    private String userName;

    @ApiModelProperty(value = "회원명")
    private String noMaskUserName;

    @ApiModelProperty(value = "회원 ID")
    @UserMaskingLoginId
    private String loginId;

    @ApiModelProperty(value = "처리지연여부")
    private String delayYn;

    @ApiModelProperty(value = "신청일")
    private String createDate;

    @ApiModelProperty(value = "처리날짜")
    private String modifyDate;

    @ApiModelProperty(value = "처리상태명")
    private String rewardApplyStatusName;

    @ApiModelProperty(value = "처리담당자 ID")
    private String modifyId;

    @ApiModelProperty(value = "처리담당자 명")
    private String modifyUserName;

    @ApiModelProperty(value = "지급유형")
    private String rewardPayTp;

    @ApiModelProperty(value = "지급유형명")
    private String rewardPayTpName;

    @ApiModelProperty(value = "지습상세")
    private String rewardPayDetl;

    @ApiModelProperty(value = "이미지명")
    private String imageName;

    @ApiModelProperty(value = "이미지경로")
    private String imagePath;

    @ApiModelProperty(value = "보상구분명")
    private String rewardApplyStandardName;

    @ApiModelProperty(value = "휴대폰번호")
    @UserMaskingMobile
    private String mobile;

    @ApiModelProperty(value = "email")
    @UserMaskingEmail
    private String mail;

    @ApiModelProperty(value = "휴대폰번호")
    private String mobileSend;

    @ApiModelProperty(value = "email")
    private String mailSend;

    @ApiModelProperty(value = "처리사유")
    private String answer;

    @ApiModelProperty(value = "관리자메모")
    private String adminCmt;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "상품 타입")
    private String goodsTp;

    @ApiModelProperty(value = "보상신청대상")
    private String productInfo;

    @ApiModelProperty(value = "신청대상")
    private String rewardExcel;

    public void setOrderInfo(OrderInfoFromMypageRewardVo vo) {
        this.odid = vo.getOdid();
        this.packType = vo.getPackType();
        this.packTitle = vo.getPackTitle();
        this.thumbnailPath = vo.getThumbnailPath();
        this.ilGoodsId = vo.getIlGoodsId();
        this.goodsName = vo.getGoodsName();
    }
}
