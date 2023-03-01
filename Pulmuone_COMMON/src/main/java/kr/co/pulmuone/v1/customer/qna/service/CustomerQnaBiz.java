package kr.co.pulmuone.v1.customer.qna.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.customer.qna.dto.*;
import kr.co.pulmuone.v1.customer.qna.dto.vo.*;

import java.util.List;

public interface CustomerQnaBiz {

    QnaInfoByUserVo getQnaInfoByUser(QnaInfoByUserRequestDto dto) throws Exception;

    QnaListByUserResponseDto getQnaListByUser(QnaListByUserRequestDto dto) throws Exception;

    ProductQnaListByUserResponseDto getProductQnaListByUser(ProductQnaListByUserRequestDto dto) throws Exception;

    ApiResult<?> putProductQnaSetSecretByUser(Long csQnaId, Long urUserId) throws Exception;

    void putQnaAnswerUserCheckYn(Long csQnaId) throws Exception;

    ProductQnaListByGoodsResponseDto getProductQnaListByGoods(ProductQnaListByGoodsRequestDto dto) throws Exception;

    ApiResult<?> addProductQna(ProductQnaRequestDto dto) throws Exception;

    ProductQnaVo getProductQnaByGoods(Long csQnaId) throws Exception;

    ApiResult<?> putProductQna(ProductQnaVo vo) throws Exception;

    ApiResult<?> addQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) throws Exception;

    ApiResult<?> putQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) throws Exception;

    void addQnaImage(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto);

    void putQnaImage(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto);

    void removeQnaImage(List<OnetooneQnaByUserAttcVo> oldQnaImageList, boolean isAllDeleted,
                        String publicRootStoragePath);

    OnetooneQnaByUserResponseDto getQnaDetailByUser(Long csQnaId, Long urUserId) throws Exception;

    OnetooneQnaOrderInfoByUserResponseDto getOrderInfoPopupByQna(String searchPeriod, Long urUserId) throws Exception;


    // 통합몰문의 목록조회
    ApiResult<?> getQnaList(QnaBosRequestDto qnaBosRequestDto) throws Exception;

    //후기관리 목록조회 엑셀 다운로드
    ExcelDownloadDto qnaExportExcel(QnaBosRequestDto qnaBosRequestDto);

    // 통합몰문의 관리 상세조회
    ApiResult<?> getQnaDetail(QnaBosRequestDto qnaBosRequestDto) throws Exception;

    // 답변진행 상태변경
    ApiResult<?> putQnaAnswerStatus(QnaBosRequestDto qnaBosRequestDto) throws Exception;

    // 문의 답변정보 수정
    ApiResult<?> putQnaInfo(QnaBosRequestDto qnaBosRequestDto) throws Exception;

    //1:1문의 상세 첨부파일 이미지
    ApiResult<?> getImageList(String csQnaId) throws Exception;

    //1:1문의 등록 시 결과 조회
    OnetooneQnaResultVo getOnetooneQnaAddInfo(String urUserId);

    //1:1문의 등록 시 자동메일/SMS 발송
    void getOnetooneQnaAddCompleted(OnetooneQnaResultVo onetooneQnaResultVo);

    //문의 답변등록 시 결과 조회
    QnaBosDetailVo getQnaAnswerInfo(String csQnaId);

    //1:1문의 답변 시 자동메일/SMS 발송
    void getOnetooneQnaAnsweredCompleted(QnaBosDetailVo qnaBosDetailResultVo);

    //상품문의 답변 시 자동메일/SMS 발송
    void getProductQnaAnsweredCompleted(QnaBosDetailVo qnaBosDetailResultVo);


}
