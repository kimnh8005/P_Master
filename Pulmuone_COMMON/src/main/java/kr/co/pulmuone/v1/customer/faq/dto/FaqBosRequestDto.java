package kr.co.pulmuone.v1.customer.faq.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FaqBosRequestDto  extends BaseRequestPageDto{

	@ApiModelProperty(value = "FAQ 분류")
    private String searchFaqSelect;

	@ApiModelProperty(value = "단일조건 검색기준")
	private String searchSelect;

	@ApiModelProperty(value = "복수조건 검색기준")
	private String searchManySelect;

	@ApiModelProperty(value = "단일조건 검색조건")
	private String findKeyword;

	@ApiModelProperty(value = "복수조건 검색조건")
	private String findManyKeyword;

	@ApiModelProperty(value = "단일조건 검색조건")
	private ArrayList<String> findKeywordArray;

	@ApiModelProperty(value = "복수조건 검색조건")
	private ArrayList<String> findManyKeywordArray;

	@ApiModelProperty(value = "등록일 시작")
	private String createDateStart;

	@ApiModelProperty(value = "등록일 종료")
	private String createDateEnd;

	@ApiModelProperty(value = "FAQ ID")
	private String faqId;

	@ApiModelProperty(value = "첨부파일")
    String addFile;

	@ApiModelProperty(value = "첨부파일 List")
	List<FileVo> addFileList;

	@ApiModelProperty(value = "분류")
	private String faqType;

	@ApiModelProperty(value = "FAQ 제목")
    String faqTitle;

	@ApiModelProperty(value = "노출여부")
    String displayYn;

	@ApiModelProperty(value = "FAQ 내용")
    String content;

	@ApiModelProperty(value = "노출순번")
    String viewSort;

}
