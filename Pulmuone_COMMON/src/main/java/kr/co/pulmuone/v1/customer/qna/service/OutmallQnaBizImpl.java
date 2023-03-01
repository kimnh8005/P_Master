package kr.co.pulmuone.v1.customer.qna.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.api.ecs.dto.vo.QnaToEcsVo;
import kr.co.pulmuone.v1.api.ecs.service.EcsBiz;
import kr.co.pulmuone.v1.api.ezadmin.dto.EZAdminRequestDto;
import kr.co.pulmuone.v1.api.ezadmin.service.EZAdminBiz;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CustomerEnums;
import kr.co.pulmuone.v1.comm.enums.QnaEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosDetailResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosListVo;
import kr.co.pulmuone.v1.policy.bbs.service.PolicyBbsBannedWordBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
@Slf4j
public class OutmallQnaBizImpl implements OutmallQnaBiz {

    @Autowired
    private OutmallQnaService outmallQnaService;

    @Autowired
    private EZAdminBiz ezAdminBiz;

    @Autowired
    private PolicyBbsBannedWordBiz policyBbsBannedWordBiz;

    @Autowired
    private EcsBiz ecsBiz;

    /**
     * 외부몰 문의 관리 목록조회
     *
     * @throws Exception
     */
    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ApiResult<?> getOutmallQnaList(QnaBosRequestDto qnaBosRequestDto) throws Exception{
        QnaBosResponseDto result = new QnaBosResponseDto();

        if (!qnaBosRequestDto.getFindKeyword().isEmpty()) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(qnaBosRequestDto.getFindKeyword(), "\n|,");
            while (st.hasMoreElements()) {
                String object = (String) st.nextElement();
                array.add(object);
            }
            qnaBosRequestDto.setFindKeywordArray(array);
        }

        qnaBosRequestDto.setQnaTypeList(outmallQnaService.getSearchKeyToSearchKeyList(qnaBosRequestDto.getQnaType(), Constants.ARRAY_SEPARATORS)); // 검색어

        qnaBosRequestDto.setQnaStatusList(outmallQnaService.getSearchKeyToSearchKeyList(qnaBosRequestDto.getQnaStatus(), Constants.ARRAY_SEPARATORS)); // 검색어

        qnaBosRequestDto.setSaleChannelList(outmallQnaService.getSearchKeyToSearchKeyList(qnaBosRequestDto.getSaleChannelId(), Constants.ARRAY_SEPARATORS)); // 검색어

        Page<QnaBosListVo> voList = outmallQnaService.getOutmallQnaList(qnaBosRequestDto);

        result.setTotal(voList.getTotal());
        result.setRows(voList.getResult());

        return ApiResult.success(result);
    }

    /**
     * @param qnaBosRequestDto : 외부몰 문의관리 리스트 검색 조건 request dto
     * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     * @Desc 외부몰문의 리스트 엑셀 다운로드 목록 조회
     */
    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ExcelDownloadDto getOutmallQnaExportExcel(QnaBosRequestDto qnaBosRequestDto) throws Exception {

        String excelFileName = "외부몰문의관리 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                70, 200, 200, 400, 400, 200, 150, 200, 200, 150, 150, 150, 200, 200, 150};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center", "left", "center", "center", "center", "center", "center", "center", "center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "rowNumber", "saleChannelNm", "shopProductId", "odOrderId", "odOrderDetailId", "collectionMallId", "outmallTypeName", "qnaTitle", "easyadminStatusText", "qnaStatusName", "procYnText", "delayYn", "createDate", "answerDate", "answerUserName"};

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "No", "판매채널", "판매처상품코드", "외부몰주문번호", "외부몰주문상세번호", "수집몰 주문번호", "문의분류", "문의제목", "이지리플 상태", "처리상태", "처리불가여부", "답변지연여부", "등록일자", "처리날짜", "답변담당자"};

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼


        if (!qnaBosRequestDto.getFindKeyword().isEmpty()) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(qnaBosRequestDto.getFindKeyword(), "\n|,");
            while (st.hasMoreElements()) {
                String object = (String) st.nextElement();
                array.add(object);
            }
            qnaBosRequestDto.setFindKeywordArray(array);
        }

        qnaBosRequestDto.setQnaTypeList(outmallQnaService.getSearchKeyToSearchKeyList(qnaBosRequestDto.getQnaType(), Constants.ARRAY_SEPARATORS)); // 검색어

        qnaBosRequestDto.setQnaStatusList(outmallQnaService.getSearchKeyToSearchKeyList(qnaBosRequestDto.getQnaStatus(), Constants.ARRAY_SEPARATORS)); // 검색어

        qnaBosRequestDto.setSaleChannelList(outmallQnaService.getSearchKeyToSearchKeyList(qnaBosRequestDto.getSaleChannelId(), Constants.ARRAY_SEPARATORS)); // 검색어

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<QnaBosListVo> itemList = outmallQnaService.getOutmallQnaExportExcel(qnaBosRequestDto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }
    /**
     * 외부몰문의 관리 상세조회
     *
     * @throws Exception
     */
    @Override
    @UserMaskingRun(system = "BOS")
    public ApiResult<?> getOutmallQnaDetail(QnaBosRequestDto qnaBosRequestDto) throws Exception{
        QnaBosDetailResponseDto result = new QnaBosDetailResponseDto();
        QnaBosDetailVo vo = new QnaBosDetailVo();
        vo = outmallQnaService.getOutmallQnaDetail(qnaBosRequestDto);

        UserVo userVo = qnaBosRequestDto.getUserVo();
        String answer = "";
        /*
        answer = CustomerEnums.AnswerTempType.ANSWER.getMessage() + " " + vo.getOrganizationNm() + " "
        		+ qnaBosRequestDto.getUserVo().getLoginName() + CustomerEnums.AnswerTempType.ANSWER_CMT.getMessage();
        */
        answer = CustomerEnums.AnswerTempType.ANSWER.getMessage();
        if (vo.getOutmallQnaAnswerContent() == null || vo.getOutmallQnaAnswerContent().isEmpty()) {
            vo.setOutmallQnaAnswerContent(answer);
        }
        result.setRow(vo);

        return ApiResult.success(result);
    }

    /**
     * 답변진행 상태변경
     *
     * @throws Exception
     */
    @Override
    public ApiResult<?> putOutmallQnaAnswerStatus(QnaBosRequestDto qnaBosRequestDto) throws Exception {
        return outmallQnaService.putOutmallQnaAnswerStatus(qnaBosRequestDto);
    }

    /**
     * 외부몰문의 답변정보 수정
     *
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
    public ApiResult<?> putOutmallQnaInfo(QnaBosRequestDto qnaBosRequestDto) throws Exception {

        QnaBosDetailVo vo = new QnaBosDetailVo();

        String prevStatus = qnaBosRequestDto.getStatus();

        // 답변대기 상태
        if (qnaBosRequestDto.getStatus().equals(QnaEnums.QnaStatus.ANSWER_CHECKING.getCode()) && !qnaBosRequestDto.getOutmallQnaAnswerContent().isEmpty()) {

            qnaBosRequestDto.setStatus(QnaEnums.QnaStatus.ANSWER_COMPLETED.getCode());  //답변완료 상태변경

            vo = outmallQnaService.getOutmallQnaDetail(qnaBosRequestDto);
            if (vo.getStatus().equals(QnaEnums.QnaStatus.ANSWER_COMPLETED.getCode())) {  //답변완료 상태 확인: 이미 처리된 경우
                return ApiResult.result(QnaEnums.PutQnaValidation.ALREADY_CHECK);
            }

            if (!qnaBosRequestDto.getOutmallQnaAnswerContent().isEmpty()) {
                qnaBosRequestDto.setContent(policyBbsBannedWordBiz.filterSpamWord(qnaBosRequestDto.getOutmallQnaAnswerContent(), BaseEnums.EnumSiteType.BOS));
                //새로운 답변인 경우
                if(vo.getOutmallQnaAnswerContent() == null || vo.getOutmallQnaAnswerContent().isEmpty()) {
                    outmallQnaService.addOutmallQnaAnswer(qnaBosRequestDto);
                }else { //기존 답변이 있을 경우
                    outmallQnaService.putOutmallQnaAnswer(qnaBosRequestDto);
                }
            }
        }

        // 공개여부, eCS분류, 처리상태 수정
        ApiResult<?> result = outmallQnaService.putOutmallQnaInfo(qnaBosRequestDto);

        // 문의 답변 등록/수정 후 정보조회
        QnaBosDetailVo resultVo = outmallQnaService.getOutmallQnaDetail(qnaBosRequestDto);

        // ECS 답변 등록/수정
        //ECS연동 문의 등록 파라미터 세팅 (추후수정예정)
        String userMobile = "010-0000-0000";
        String customerEmail = " ";
        String customerName = " ";
        String customerPhonearea = userMobile.split("-")[0];
        String customerPhonefirst = userMobile.split("-")[1];
        String customerPhonesecond = userMobile.split("-")[2];

        String boardSeq = ecsBiz.getEcsBoardSeq(QnaEnums.QnaType.OUTMALL.getCode(), null, Long.valueOf(resultVo.getCsOutmallQnaSeq()), null);
        String counselDesc = StringUtil.htmlSingToText("제목:" + resultVo.getTitle() + "\n내용:" + resultVo.getQuestion());
        String reply = StringUtil.htmlSingToText("내용:" + resultVo.getOutmallQnaAnswerContent() + "\n답변자:"+ qnaBosRequestDto.getUserVo().getUserId());

        QnaToEcsVo qnaToEcsVo = QnaToEcsVo.builder()
                .receiptRoot(Constants.RECEIPT_ROOT_ESHOP_HP)
                .boardDiv("외부몰문의")
                .boardSeq(boardSeq)
                .customerNum(qnaBosRequestDto.getUserVo().getUserId())
                .customerName(customerName)
                .customerPhonearea(customerPhonearea)
                .customerPhonefirst(customerPhonefirst)
                .customerPhonesecond(customerPhonesecond)
                .customerEmail(customerEmail)
                .hdBcode(resultVo.getEcsCtgryStd1())
                .hdScode(resultVo.getEcsCtgryStd2())
                .claimGubun(resultVo.getEcsCtgryStd3())
                .counselDesc(counselDesc)
                .reply(reply)
                .counseler(qnaBosRequestDto.getUserVo().getUserId())
                .secCode(Constants.SEC_CODE)
                .build();

        //ECS 연동 외부몰문의 답변 등록/수정
        try {
            //새로운 답변인 경우
            if(prevStatus.equals(QnaEnums.QnaStatus.ANSWER_CHECKING.getCode()) && !qnaBosRequestDto.getOutmallQnaAnswerContent().isEmpty()) {
                ecsBiz.addQnaAnswerToEcs(qnaToEcsVo);
            } else{// 답변완료인 경우
                ecsBiz.putQnaToEcs(qnaToEcsVo);
            }

            // 이지어드민 답변입력
            EZAdminRequestDto reqDTO = new EZAdminRequestDto();
            reqDTO.setSeq(resultVo.getCsOutmallQnaSeq());
            reqDTO.setAnswer(resultVo.getOutmallQnaAnswerContent());

            ezAdminBiz.setAutoCsCyncAnswer(reqDTO);

        } catch (Exception e) {
            return ApiResult.result(QnaEnums.AddQnaValidation.INTERFACE_ERROR);
        }


        return result;
    }

    /**
     * @param csOutMallQnaId
     * @return QnaBosDetailVo
     * @Desc 외부몰문의 답변 등록 후 정보 조회
     */
    @Override
    public QnaBosDetailVo getOutmallQnaAnswerInfo(String csOutMallQnaId) {
        return outmallQnaService.getOutmallQnaAnswerInfo(csOutMallQnaId);
    }

    /**
     * @return QnaBosDetailVo
     * @Desc 외부몰문의 답변 등록 후 정보 조회
     */
    @Override
    public ApiResult<?> getOutmallNameList() {
       return ApiResult.success(outmallQnaService.getOutmallNameList());
    }
}
