package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 문의글조회 API Request DTO")
public class EZAdminQnaInfoRequestDto {

	@ApiModelProperty(value = "API action")
    private String action;

    @ApiModelProperty(value = "조회일자타입")
    private String date_type;

    @ApiModelProperty(value = "조회시작일")
    private String start_date;

    @ApiModelProperty(value = "조회종료일")
    private String end_date;
    
    @ApiModelProperty(value = "문의글 이지리플 내 상태 (0:답변대기, 1:답변입력, 2:전송실패, 3:전송완료)")
	private String status;

    @ApiModelProperty(value = "문의글 관리번호")
	private int seq;

    @ApiModelProperty(value = "문의글 답변")
	private String cs_answer;

    @ApiModelProperty(value = "조회페이지")
    private int page;

    @ApiModelProperty(value = "조회값")
    private int limit;

    //문의글 답변 reqDto
    @ApiModelProperty(value = "문의글 답변")
	private String answer;

}
