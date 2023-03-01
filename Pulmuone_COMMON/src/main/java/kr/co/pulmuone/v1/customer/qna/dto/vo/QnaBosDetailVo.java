
package kr.co.pulmuone.v1.customer.qna.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingEmail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaBosDetailVo {

	@ApiModelProperty(value = "문의자 이름")
	@UserMaskingUserName
    private String userName;

	@ApiModelProperty(value = "문의자 ID")
	@UserMaskingLoginId
    private String userId;

	@ApiModelProperty(value = "문의자 핸드폰번호")
	@UserMaskingMobile
    private String userMobile;

	@ApiModelProperty(value = "문의자 EMAIL")
	@UserMaskingEmail
    private String userEmail;

	@ApiModelProperty(value = "문의유형")
    private String qnaType;

	@ApiModelProperty(value = "문의유형명")
    private String qnaTypeName;

	@ApiModelProperty(value = "문의분류명")
    private String qnaDivisionName;

	@ApiModelProperty(value = "답변방법 sms")
    private String answerSmsYn;

	@ApiModelProperty(value = "답변방법 email")
    private String answerMailYn;

    @ApiModelProperty(value = "처리상태")
    private String status;

    @ApiModelProperty(value = "처리상태명")
    private String qnaStatusName;

    @ApiModelProperty(value = "문의 등록일")
    private String questionCreateDate;

    @ApiModelProperty(value = "답변처리일")
    private String answeredDate;

    @ApiModelProperty(value = "공개여부")
    private String secretType;

    @ApiModelProperty(value = "답변지연여부")
    private String answerDelayYn;

    @ApiModelProperty(value = "문의상품")
    private String goodsName;

    @ApiModelProperty(value = "문의상품 ID")
    private String ilGoodsId;

    @ApiModelProperty(value = "문의제목")
    private String title;

    @ApiModelProperty(value = "문의내용")
    private String question;

    @ApiModelProperty(value = "1차답변")
    private String firstContent;

    @ApiModelProperty(value = "2차답변")
    private String secondContent;

    @ApiModelProperty(value = "문의 ID")
    private String csQnaId;

    @ApiModelProperty(value = "eCS 대분류")
    private String ecsCtgryStd1;

    @ApiModelProperty(value = "eCS 중분류")
    private String ecsCtgryStd2;

    @ApiModelProperty(value = "eCS 소분류")
    private String ecsCtgryStd3;

    @ApiModelProperty(value = "비공개 사유")
    private String secretComment;

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문상세PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "1차답변 날짜")
    private String firstAnswerDate;

    @ApiModelProperty(value = "2차답변 날짜")
    private String secondAnswerDate;

    @ApiModelProperty(value = "1차답변자정보")
    private String firstUserInfo;

    @ApiModelProperty(value = "2차답변자정보")
    private String secondUserInfo;

    @ApiModelProperty(value = "이미지명")
    private String imageName;

    @ApiModelProperty(value = "이미지경로")
    private String imagePath;

    @ApiModelProperty(value = "상품문의분류")
    private String productType;

    @ApiModelProperty(value = "상품문의분류명")
    private String productTypeName;

    @ApiModelProperty(value = "회원 pk 코드", hidden = true)
    private String urUserId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "상품타입")
    private String goodsTp;

    @ApiModelProperty(value = "담당자 소속팀명")
    private String organizationNm;

    // 외부몰 문의관리 START
    @ApiModelProperty(value = "외부몰문의 ID")
    private String csOutmallQnaId;

    @ApiModelProperty(value = "판매채널 ID")
    private String saleChannelId;

    @ApiModelProperty(value = "판매채널 명")
    private String saleChannelNm;

    @ApiModelProperty(value = "판매처 상품코드")
    private String shopProductId;

    @ApiModelProperty(value = "주문상세번호 pk (외부몰 주문상세번호)")
    private String odOrderDetailId;

    @ApiModelProperty(value = "외부몰문의분류")
    private String outmallType;

    @ApiModelProperty(value = "외부몰문의분류명")
    private String outmallTypeName;

    @ApiModelProperty(value = "외부몰문의관리 > 답변")
    private String outmallQnaAnswerContent;

    @ApiModelProperty(value = "외부몰문의관리 > 답변날짜")
    private String outmallQnaAnswerDate;

    @ApiModelProperty(value = "외부몰 상품상세 페이지 URL")
	private String shopProductUrl;

    @ApiModelProperty(value = "문의글 관리번호")
	private int csOutmallQnaSeq;
    
    @ApiModelProperty(value = "이지리플 내 상태")
	private int easyadminStatus;
     
    @ApiModelProperty(value = "이지리플 I/F 수집일")
	private String easyadminIfDt;

    @ApiModelProperty(value = "수집몰 주문번호")
	private String collectionMallId;

    @ApiModelProperty(value = "처리불가여부")
	private String procYn;
    // 외부몰 문의관리 END

}
