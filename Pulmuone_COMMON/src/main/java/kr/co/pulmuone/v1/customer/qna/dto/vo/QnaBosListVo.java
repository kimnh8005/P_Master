package kr.co.pulmuone.v1.customer.qna.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaBosListVo {

    @ApiModelProperty(value = "통합몰문의 ID")
    private String csQnaId;

	@ApiModelProperty(value = "문의유형")
    private String qnaType;

	@ApiModelProperty(value = "문의유형명")
    private String qnaTypeName;

	@ApiModelProperty(value = "분류")
    private String qnaDivision;

	@ApiModelProperty(value = "분류명")
    private String qnaDivisionName;

	@ApiModelProperty(value = "문의제목")
    private String qnaTitle;

	@ApiModelProperty(value = "문의내용")
    private String qnaQuestion;

	@ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "회원명")
    @UserMaskingUserName
    private String userName;

    @ApiModelProperty(value = "회원 ID")
    @UserMaskingLoginId
    private String userId;

	@ApiModelProperty(value = "처리상태")
    private String status;

	@ApiModelProperty(value = "처리상태명")
    private String qnaStatusName;

	@ApiModelProperty(value = "등록일자")
	private String createDate;

    @ApiModelProperty(value = "처리담당자 ID")
    private String answerUserId;

    @ApiModelProperty(value = "처리담당자 명")
    private String answerUserName;

    @ApiModelProperty(value = "처리날짜")
    private String answerDate;

    @ApiModelProperty(value = "지연여부")
    private String delayYn;

    @ApiModelProperty(value = "순번")
    private String rowNumber;

    @ApiModelProperty(value = "상품ID")
    private String ilGoodsId;

    @ApiModelProperty(value = "상품 타입")
    private String goodsTp;

    //외부몰 문의관리
    @ApiModelProperty(value = "외부몰문의 ID")
    private String csOutmallQnaId;

    @ApiModelProperty(value = "판매채널 ID")
    private String saleChannelId;

    @ApiModelProperty(value = "판매채널 명")
    private String saleChannelNm;

    @ApiModelProperty(value = "판매처 상품코드")
    private String shopProductId;

    @ApiModelProperty(value = "주문번호 pk (외부몰 주문번호)")
    private String odOrderId;

    @ApiModelProperty(value = "주문상세번호 pk (외부몰 주문상세번호)")
    private String odOrderDetailId;

    @ApiModelProperty(value = "외부몰 문의유형명")
    private String outmallTypeName;

    @ApiModelProperty(value = "문의일시")
	private String regDate;

    @ApiModelProperty(value = "외부몰 상품상세 페이지 URL")
	private String shopProductUrl;


    @ApiModelProperty(value = "답변내용(1차)")
    private String firstAnswer;

    @ApiModelProperty(value = "답변내용(2차)")
    private String secondAnswer;

    @ApiModelProperty(value = "이메일")
    private String userMail;

    @ApiModelProperty(value = "휴대폰번호")
    private String userMobile;

    @ApiModelProperty(value = "답변방법 SMS")
    private String answerSmsYn;

    @ApiModelProperty(value = "답변방법 MAIL")
    private String answerMailYn;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "1차답변 담당자 ID")
    private String firstAnswerUserId;

    @ApiModelProperty(value = "2차답변 담당자 ID")
    private String secondAnswerUserId;

    @ApiModelProperty(value = "1차답변 담당자 명")
    private String firstAnswerUserNm;

    @ApiModelProperty(value = "2차답변 담당자 명")
    private String secondAnswerUserNm;

    @ApiModelProperty(value = "1차답변일")
    private String firstAnswerDt;

    @ApiModelProperty(value = "2차답변일")
    private String secondAnswerDt;

    @ApiModelProperty(value = "eCS 대분류")
    private String hdBcodeNm;

    @ApiModelProperty(value = "eCs 중분류")
    private String hdScodeNm;

    @ApiModelProperty(value = "eCs 소분류")
    private String claimGubunNm;

    @ApiModelProperty(value = "이지리플 내 상태코드")
	private int easyadminStatus;

    @ApiModelProperty(value = "이지리플 내 상태명")
	private String easyadminStatusText;

    @ApiModelProperty(value = "이지리플 I/F 수집일")
	private String easyadminIfDt;

    @ApiModelProperty(value = "수집몰 주문번호")
	private String collectionMallId;

    @ApiModelProperty(value = "처리불가여부")
	private String procYn;

    @ApiModelProperty(value = "처리불가여부명")
	private String procYnText;

    @ApiModelProperty(value = "탈퇴회원 여부")
    private String dropUserYn;
}
