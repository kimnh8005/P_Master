package kr.co.pulmuone.v1.customer.qna.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaByUserAttcVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "1:1 문의 등록/수정 RequestDto")
public class OnetooneQnaByUserRequestDto {

	@ApiModelProperty(value="1:1 문의 분류")
	private String onetooneType;

	@ApiModelProperty(value = "주문번호")
    private Long odOrderId;

    @ApiModelProperty(value = "주문상세번호")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String question;

    @ApiModelProperty(value = "문의 이미지 목록", required = false)
    List<OnetooneQnaByUserAttcVo> image;

    @ApiModelProperty(value = "답변 알람 수신 E-mail")
    private String answerMailYn;

    @ApiModelProperty(value = "답변 알람 수신 SMS")
    private String answerSmsYn;

    @ApiModelProperty(value = "발급회원코드", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "문의 PK")
    private Long csQnaId;

    /*
     * 문의 이미지를 저장할 최상위 저장 디렉토리 경로 ( 리사이징 이미지 포함 )
     * MALL 상의 도메인별 파일 업로드시 저장 경로 정보 MallStorageInfoEnum 을 참조할 수 있는 Service 단에서 값 세팅
     */
    private String publicRootStoragePath;
    private String serverSubPath;

    /*
     * ECS연동 위한 회원 정보
     */
    @ApiModelProperty(value = "회원명", hidden = true)
    private String userName;

    @ApiModelProperty(value = "회원 모바일", hidden = true)
    private String userMobile;

    @ApiModelProperty(value = "회원 이메일", hidden = true)
    private String userEmail;

    /*
     * ECS연동 분류 코드
     */
    @ApiModelProperty(value = "ECS연동 상담대분류", hidden = true)
    private String hdBcode;

    @ApiModelProperty(value = "ECS연동 상담중분류", hidden = true)
    private String hdScode;

    @ApiModelProperty(value = "ECS연동 상담소분류", hidden = true)
    private String claimGubun;
}
