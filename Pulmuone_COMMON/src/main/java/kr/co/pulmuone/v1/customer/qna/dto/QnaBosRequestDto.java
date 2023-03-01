package kr.co.pulmuone.v1.customer.qna.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaBosRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "공지사항 분류")
    private String searchNoticeSelect;

	@ApiModelProperty(value = "단일조건 검색기준")
	private String searchSelect;

	@ApiModelProperty(value = "복수조건 검색기준")
	private String searchManySelect;

	@ApiModelProperty(value = "상품명검색")
	private String searchGoodsName;

	@ApiModelProperty(value = "단일조건 검색조건")
	private String findKeyword;

	@ApiModelProperty(value = "복수조건 검색조건")
	private String findManyKeyword;

	@ApiModelProperty(value = "단일조건 검색조건")
	private ArrayList<String> findKeywordArray;

	@ApiModelProperty(value = "복수조건 검색조건")
	private ArrayList<String> findManyKeywordArray;

	@ApiModelProperty(value = "답변담당자 검색키워드")
	private String findAnswerKeyword;

	@ApiModelProperty(value = "답변담당자 검색조건")
	private String searchAnswerSelect;

    @ApiModelProperty(value = "고객정보 검색조건")
    private String searchUserSelect;

    @ApiModelProperty(value = "고객정보 검색키워드")
    private String findUserKeyword;

    @ApiModelProperty(value = "처리상태")
    private String qnaStatus;

    @ApiModelProperty(value = "문의유형")
    private String qnaType;

    @ApiModelProperty(value = "처리상태 상세")
    private String status;

    @ApiModelProperty(value = "문의 공개 타입")
    private String secretType;

    @ApiModelProperty(value = "문의 비공개 사유")
    private String secretComment;

    @ApiModelProperty(value = "eCS 대분류")
    private String ecsCtgryStd1;

    @ApiModelProperty(value = "eCS 중분류")
    private String ecsCtgryStd2;

    @ApiModelProperty(value = "eCS 소분류")
    private String ecsCtgryStd3;

    @ApiModelProperty(value = "1차답변")
    private String firstContent;

    @ApiModelProperty(value = "2차답변")
    private String secondContent;

    @ApiModelProperty(value = "답변")
    private String content;

    @ApiModelProperty(value = "처리상태 리스트")
    private List<String> qnaStatusList;

    @ApiModelProperty(value = "문의유형 리스트")
    private List<String> qnaTypeList;

	@ApiModelProperty(value = "등록일 시작")
	private String createDateStart;

	@ApiModelProperty(value = "등록일 종료")
	private String createDateEnd;

	@ApiModelProperty(value = "통합물문의 ID")
	private String csQnaId;

	@ApiModelProperty(value = "상품 문의분류")
    private String goodsType;

	@ApiModelProperty(value = "1:1ansdml 문의분류")
    private String qnaOneType;

	@ApiModelProperty(value = "답변지연여부")
    private String answerDelayView;

	@ApiModelProperty(value = "회원 UserId")
    private String urUserId;

    @ApiModelProperty(value = "탈퇴회원문의 제외")
    private String excludeDropUserView;

	//외부몰 문의관리
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

    @ApiModelProperty(value = "판매채널 리스트")
    private List<String> saleChannelList;

    @ApiModelProperty(value = "문의글 관리번호")
	private String csOutmallQnaSeq;

    @ApiModelProperty(value = "수집몰 주문번호")
	private String collectionMallId;

    @ApiModelProperty(value = "처리불가여부")
	private String procYn;
    // 외부몰 문의관리 END
}
