package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto;

import java.util.List;

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
@ApiModel(description = "이지어드민 API Response Default")
public class EZAdminResponseDefaultDto{

	/* 응답 데이터 */
    @ApiModelProperty(value = "에러여부")
    private String error;

    @ApiModelProperty(value = "메세지")
    private String msg;

    private List<?> data;

    public EZAdminResponseDefaultDto (List<?> list) {
    	this.data = list;
    }

    @ApiModelProperty(value = "API DATA")
    private String responseData;

    /* 요청 데이터 */
    @ApiModelProperty(value = "API 종류")
    private String action;

    @ApiModelProperty(value = "요청 데이터")
    private String map;

    @ApiModelProperty(value = "요청 시작 일자")
    private String reqStartDate;

    @ApiModelProperty(value = "요청 종료 일자")
    private String reqEndDate;

    /* 주문조회 API */
    @ApiModelProperty(value = "총건수")
    private String total;

    @ApiModelProperty(value = "총 합포단위수")
    private String pack_cnt;

    @ApiModelProperty(value = "총 상품수량")
    private String product_sum;

    @ApiModelProperty(value = "페이지")
    private String page;

    @ApiModelProperty(value = "조회값")
    private String limit;

    @ApiModelProperty(value = "이지어드민정보 PK")
    private Long ifEasyadminInfoId;

    @ApiModelProperty(value = "배치 연동 상태")
    private String syncCd;

    @ApiModelProperty(value = "처리 상태")
    private String processCd;

    @ApiModelProperty(value = "이지어드민배치타입")
    private String easyadminBatchTp;

    @ApiModelProperty(value = "이지어드민 API 호출 정보 PK")
    private Long ifEasyadminApiInfoId;

    /* 문의글조회 API */
    @ApiModelProperty(value = "이지어드민 문의글 정보 PK")
    private Long csOutmallQnaEasyadminInfoId;

    @ApiModelProperty(value = "이지어드민 요청데이터 정보 PK")
    private Long ifEasyadminInfoReqDataId;

    @ApiModelProperty(value = "API 수집상태 I: 수집중, S: 수집성공, F:수집실패")
    private String collectCd;

}
