package kr.co.pulmuone.v1.customer.qna.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;

public interface OutmallQnaBiz {
    // 외부몰 문의관리 > 목록 조회
    ApiResult<?> getOutmallQnaList(QnaBosRequestDto qnaBosRequestDto) throws Exception;
    //외부몰 문의관리 리스트 엑셀 다운로드 목록 조회
    ExcelDownloadDto getOutmallQnaExportExcel(QnaBosRequestDto qnaBosRequestDto) throws Exception;
    //외부몰 문의관리 > 상세조회
    ApiResult<?> getOutmallQnaDetail(QnaBosRequestDto qnaBosRequestDto) throws Exception;
    //외부몰 문의관리 답변진행 상태변경
    ApiResult<?> putOutmallQnaAnswerStatus(QnaBosRequestDto qnaBosRequestDto) throws Exception;
    // 외부몰문의 답변정보 수정
    ApiResult<?> putOutmallQnaInfo(QnaBosRequestDto qnaBosRequestDto) throws Exception;
    //외부몰문의 답변 시 정보 조회
    QnaBosDetailVo getOutmallQnaAnswerInfo(String csOutMallQnaId);
    //외부몰명 체크박스 리스트
    ApiResult<?> getOutmallNameList();
}
