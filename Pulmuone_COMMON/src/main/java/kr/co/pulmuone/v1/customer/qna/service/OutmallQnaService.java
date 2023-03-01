package kr.co.pulmuone.v1.customer.qna.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.mapper.customer.qna.OutmallQnaMapper;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosListVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <PRE>
 * Forbiz Korea
 * Class 의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    2021.07.23   	 최윤지            최초작성
 * =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class OutmallQnaService {

	@Autowired
    OutmallQnaMapper outmallQnaMapper;

    /**
     * 외부몰문의 관리 목록조회
     *
     * @param qnaBosRequestDto
     * @return Page<QnaBosListVo>
     * @throws Exception Exception
     */
	protected Page<QnaBosListVo> getOutmallQnaList(QnaBosRequestDto qnaBosRequestDto) throws Exception {
    	PageMethod.startPage(qnaBosRequestDto.getPage(), qnaBosRequestDto.getPageSize());
        return outmallQnaMapper.getOutmallQnaList(qnaBosRequestDto);
    }

    /**
     * @Desc 통합몰문의관리 리스트 엑셀 다운로드 목록 조회
     * @param qnaBosRequestDto : 후기관리 리스트 검색 조건 request dto
     * @return List<FeedbackBosListVo> : 후기관리 리스트 엑셀 다운로드 목록
     */
    protected List<QnaBosListVo> getOutmallQnaExportExcel(QnaBosRequestDto qnaBosRequestDto) throws Exception {

    	//외부몰 문의관리 목록
    	Page<QnaBosListVo> itemList = outmallQnaMapper.getOutmallQnaList(qnaBosRequestDto);

        // 화면과 동일하게 역순으로 no 지정
        for (int i = itemList.size() - 1; i >= 0; i--) {
            itemList.get(i).setRowNumber(String.valueOf(itemList.size() - i));
        }

        return itemList.getResult();
    }
    /**
     * @Desc 검색키 -> 검색키 리스트 변환
     *       검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
     * @param searchKey
     * @param splitKey
     * @return List<String>
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();

        if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                                       .map(String::trim)
                                       .filter( x -> StringUtils.isNotEmpty(x) )
                                       .collect(Collectors.toList()));
        }

        return searchKeyList;
    }

    /**
     * 외부몰문의관리 상세조회
     * @param qnaBosRequestDto
     * @return QnaBosDetailVo
     * @throws Exception
     */
    protected QnaBosDetailVo getOutmallQnaDetail(QnaBosRequestDto qnaBosRequestDto) throws Exception{
        return outmallQnaMapper.getOutmallQnaDetail(qnaBosRequestDto);
    }

    /**
 	 * 답변진행 상태변경
 	 * @param qnaBosRequestDto
 	 * @return
 	 * @throws Exception
 	 */
 	protected ApiResult<?> putOutmallQnaAnswerStatus(QnaBosRequestDto qnaBosRequestDto) throws Exception {

 		int result = outmallQnaMapper.putOutmallQnaAnswerStatus(qnaBosRequestDto);

        return ApiResult.success(result);
    }

    /**
 	 *  외부몰문의 답변정보 수정
 	 * @param qnaBosRequestDto
 	 * @return
 	 * @throws Exception
 	 */
    protected ApiResult<?> putOutmallQnaInfo(QnaBosRequestDto qnaBosRequestDto) throws Exception {

 		int result = outmallQnaMapper.putOutmallQnaInfo(qnaBosRequestDto);

        return ApiResult.success(result);
    }

    /**
 	 *  외부몰문의 답변정보 등록
 	 * @param qnaBosRequestDto
 	 * @return
 	 * @throws Exception
 	 */
 	protected int addOutmallQnaAnswer(QnaBosRequestDto qnaBosRequestDto) throws Exception {

 		int result = outmallQnaMapper.addOutmallQnaAnswer(qnaBosRequestDto);

        return result;
    }

    /**
	 * @Desc 외부몰문의 답변 등록 시 정보 조회
	 * @param csOutMallQnaId
	 * @return QnaBosDetailVo
	 */
	protected QnaBosDetailVo getOutmallQnaAnswerInfo(String csOutMallQnaId) {
		return outmallQnaMapper.getOutmallQnaAnswerInfo(csOutMallQnaId);
	}

	/**
	 * @Desc 외부몰명 체크박스 리스트
	 * @return GetCodeListResponseDto
	 */
    protected GetCodeListResponseDto getOutmallNameList() {
	     List<GetCodeListResultVo> rows = outmallQnaMapper.getOutmallNameList();

        GetCodeListResponseDto result = new GetCodeListResponseDto();
        result.setRows(rows);

        return result;
    }

    /**
 	 *  외부몰문의 답변 수정
 	 * @param qnaBosRequestDto
 	 * @return
 	 * @throws Exception
 	 */
    protected ApiResult<?> putOutmallQnaAnswer(QnaBosRequestDto qnaBosRequestDto) throws Exception {

 		int result = outmallQnaMapper.putOutmallQnaAnswer(qnaBosRequestDto);

        return ApiResult.success(result);
    }
}
