package kr.co.pulmuone.v1.customer.qna.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.QnaEnums;
import kr.co.pulmuone.v1.comm.mapper.customer.qna.CustomerQnaMapper;
import kr.co.pulmuone.v1.customer.qna.dto.OnetooneQnaByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByGoodsResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByUserResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaInfoByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaListByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaListByUserResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaByUserAttcVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaByUserVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaOrderDetailInfoByUserVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaOrderInfoByUserVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaResultVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaListByGoodsVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaListByUserVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaAnswerByCsQnaIdVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaAttcByCsQnaIdVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosListVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaInfoByUserVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaListByUserVo;
import lombok.RequiredArgsConstructor;

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
 * 1.0    20200918   	 이원호            최초작성
 * =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class CustomerQnaService {

	@Autowired
    CustomerQnaMapper customerQnaMapper;

    /**
     * 1:1문의 현황 조회
     *
     * @param dto QnaInfoByUserRequestDto
     * @return QnaInfoByUserVo
     * @throws Exception Exception
     */
    public QnaInfoByUserVo getQnaInfoByUser(QnaInfoByUserRequestDto dto) throws Exception {
        QnaInfoByUserVo result = customerQnaMapper.getQnaInfoByUser(dto);
        if(result == null){
            result = new QnaInfoByUserVo();
            result.setTotalCount("0");
            result.setReceptionCount("0");
            result.setAnswerCheckingCount("0");
            result.setAnswerCompletedCount("0");
        }

        return result;
    }

    /**
     * 1:1문의 내역 조회
     *
     * @param dto QnaListByUserRequestDto
     * @return QnaListByUserResponseDto
     * @throws Exception Exception
     */
    public QnaListByUserResponseDto getQnaListByUser(QnaListByUserRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<QnaListByUserVo> rows = customerQnaMapper.getQnaListByUser(dto);
        List<QnaListByUserVo> list = rows.getResult();
        for (QnaListByUserVo vo : list) {
            vo.setImage(getQnaAttcByCsQnaId(vo.getCsQnaId()));
            vo.setAnswer(getQnaAnswerByCsQnaId(vo.getCsQnaId()));
        }

        return QnaListByUserResponseDto.builder()
                .total((int) rows.getTotal())
                .qnaList(list)
                .build();
    }

    /**
     * 1:1문의 이미지 조회 by csQnaId
     *
     * @param csQnaId Long
     * @return QnaAttcByCsQnaIdVo
     * @throws Exception Exception
     */
    private List<QnaAttcByCsQnaIdVo> getQnaAttcByCsQnaId(Long csQnaId) throws Exception {
        return customerQnaMapper.getQnaAttcByCsQnaId(csQnaId);
    }

    /**
     * 1:1문의 답변 조회 by csQnaId
     *
     * @param csQnaId Long
     * @return QnaAnswerByCsQnaIdVo
     * @throws Exception Exception
     */
    private List<QnaAnswerByCsQnaIdVo> getQnaAnswerByCsQnaId(Long csQnaId) throws Exception {
        return customerQnaMapper.getQnaAnswerByCsQnaId(csQnaId);
    }

    /**
     * 1:1문의 답변 - 유저 확인여부 반영
     *
     * @param csQnaId Long
     * @throws Exception Exception
     */
    protected void putQnaAnswerUserCheckYn(Long csQnaId) throws Exception {
        customerQnaMapper.putQnaAnswerUserCheckYn(csQnaId);
    }

    /**
     * 상품문의 내역 목록 조회
     *
     * @param dto ProductQnaListByUserRequestDto
     * @return ProductQnaListByUserResponseDto
     * @throws Exception Exception
     */
    public ProductQnaListByUserResponseDto getProductQnaListByUser(ProductQnaListByUserRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<ProductQnaListByUserVo> rows = customerQnaMapper.getProductQnaListByUser(dto);

        List<ProductQnaListByUserVo> list = rows.getResult();
        for (ProductQnaListByUserVo vo : list) {
            vo.setAnswer(getQnaAnswerByCsQnaId(vo.getCsQnaId()));
        }

        return ProductQnaListByUserResponseDto.builder().total((int) rows.getTotal()).list(list).build();
    }

    /**
     * 문의 비공개처리 - 고객
     *
     * @param csQnaId Long
     * @throws Exception Exception
     */
    public void putProductQnaSetSecretByUser(Long csQnaId) throws Exception {
        customerQnaMapper.putProductQnaSetSecretByUser(csQnaId);
    }

    /**
     * 상품상세 - 상품문의 목록 조회
     *
     * @param dto ProductQnaListByGoodsRequestDto
     * @return ProductQnaListByGoodsResponseDto
     * @throws Exception Exception
     */
    public ProductQnaListByGoodsResponseDto getProductQnaListByGoods(ProductQnaListByGoodsRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<ProductQnaListByGoodsVo> rows = customerQnaMapper.getProductQnaListByGoods(dto);

        List<ProductQnaListByGoodsVo> list = rows.getResult();
        for (ProductQnaListByGoodsVo vo : list) {
            vo.setAnswer(getQnaAnswerByCsQnaId(vo.getCsQnaId()));
        }

        return ProductQnaListByGoodsResponseDto.builder()
                .total((int) rows.getTotal())
                .rows(list)
                .build();
    }

    /**
     * 상품 문의 등록 - 상품 상세
     *
     * @param dto ProductQnaRequestDto
     * @throws Exception Exception
     */
    public ProductQnaListByGoodsVo addProductQna(ProductQnaRequestDto dto) throws Exception {
        if (dto.getSecretType().equals("Y")) {
            dto.setSecretType(QnaEnums.QnaSecretType.CLOSE_CUSTOMER.getCode());
        } else {
            dto.setSecretType(QnaEnums.QnaSecretType.OPEN.getCode());
        }

        customerQnaMapper.addProductQna(dto);
        return customerQnaMapper.getProductQnaByCsQnaId(dto.getCsQnaId(), dto.getUrUserId());
    }

    /**
     * 상품 문의 상세 조회 - 상품 상세
     *
     * @param csQnaId Long
     * @return ProductQnaVo
     * @throws Exception Exception
     */
    public ProductQnaVo getProductQnaByGoods(Long csQnaId) throws Exception {
        return customerQnaMapper.getProductQnaByGoods(csQnaId);
    }

    /**
     * 상품 문의 수정 - 상품 상세
     *
     * @param vo ProductQnaVo
     * @throws Exception Exception
     */
    public void putProductQna(ProductQnaVo vo) throws Exception {
        if (vo.getSecretType().equals("Y")) {
            vo.setSecretType(QnaEnums.QnaSecretType.CLOSE_CUSTOMER.getCode());
        } else {
            vo.setSecretType(QnaEnums.QnaSecretType.OPEN.getCode());
        }

        customerQnaMapper.putProductQna(vo);
    }

	/**
	 * @Desc 1:1 문의 등록
	 * @param onetooneQnaByUserRequestDto
	 */
	protected int addQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) {

		// 문의 Insert(CS_QNA)
		return customerQnaMapper.addQnaByUser(onetooneQnaByUserRequestDto);

	}

	/**
	 * @Desc 1:1 문의 이미지 등록
	 * @param onetooneQnaByUserAttcVo
	 * @return int
	 */
	protected int addQnaImage(OnetooneQnaByUserAttcVo onetooneQnaByUserAttcVo) {

		return customerQnaMapper.addQnaImage(onetooneQnaByUserAttcVo);

	}

	/**
	 * @Desc 1:1 문의 수정
	 * @param onetooneQnaByUserRequestDto
	 * @return
	 */
	protected int putQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) {

		// 문의 Insert(CS_QNA)
		return customerQnaMapper.putQnaByUser(onetooneQnaByUserRequestDto);

	}

	/**
	 * @Desc 1:1 문의 이미지 조회
	 * @param csQnaId
	 * @return List<OnetooneQnaByUserAttcVo>
	 */
	protected List<OnetooneQnaByUserAttcVo> qnaImageList(Long csQnaId) {

		return customerQnaMapper.qnaImageList(csQnaId);
	}

	/**
	 * @Desc 1:1 문의 이미지 삭제
	 * @param csQnaId
	 * @return int
	 */
	protected int delQnaImage(Long csQnaId) {
		return customerQnaMapper.delQnaImage(csQnaId);

	}

	/**
	 * @Desc 1:1 문의 상세조회
	 * @param csQnaId
	 * @param urUserId
	 * @return OnetooneQnaByUserVo
	 */
	protected OnetooneQnaByUserVo getQnaDetailByUser(Long csQnaId, Long urUserId) {
		return customerQnaMapper.getQnaDetailByUser(csQnaId, urUserId);
	}

	/**
	 * @Desc 1:1 문의 주문조회 팝업조회 (상세 제외)
	 * @param searchPeriod
	 * @return List<OnetooneQnaOrderInfoByUserVo>
	 */
	protected List<OnetooneQnaOrderInfoByUserVo> getOrderInfoPopupByQna(String searchPeriod, Long urUserId) {

		return customerQnaMapper.getOrderInfoPopupByQna(searchPeriod, urUserId);
	}

	/**
	 * @Desc 1:1 문의 주문조회 팝업조회 (상세 포함)
	 * @return OnetooneQnaOrderInfoByUserVo
	 */
	protected List<OnetooneQnaOrderDetailInfoByUserVo> getOrderDetlInfoPopupByQna(Long odOrderId) {

		return customerQnaMapper.getOrderDetlInfoPopupByQna(odOrderId);
	}

    /**
     * 통합몰문의 관리 목록조회
     *
     * @param QnaBosRequestDto
     * @return Page<QnaBosListVo>
     * @throws Exception Exception
     */
    protected Page<QnaBosListVo> getQnaList(QnaBosRequestDto qnaBosRequestDto) throws Exception {
    	PageMethod.startPage(qnaBosRequestDto.getPage(), qnaBosRequestDto.getPageSize());
        return customerQnaMapper.getQnaList(qnaBosRequestDto);
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
     * @Desc 통합몰문의관리 리스트 엑셀 다운로드 목록 조회
     * @param FeedbackBosRequestDto : 후기관리 리스트 검색 조건 request dto
     * @return List<FeedbackBosListVo> : 후기관리 리스트 엑셀 다운로드 목록
     */
    public List<QnaBosListVo> qnaExportExcel(QnaBosRequestDto qnaBosRequestDto) {


        List<QnaBosListVo> itemList = customerQnaMapper.qnaExportExcel(qnaBosRequestDto);

        // 화면과 동일하게 역순으로 no 지정
        for (int i = itemList.size() - 1; i >= 0; i--) {
            itemList.get(i).setRowNumber(String.valueOf(itemList.size() - i));
        }

        return itemList;
    }


    /**
     * 통합몰문의관리 상세조회
     * @param QnaBosRequestDto
     * @return QnaBosDetailVo
     * @throws Exception
     */
    protected QnaBosDetailVo getQnaDetail(QnaBosRequestDto qnaBosRequestDto)throws Exception {
    	return customerQnaMapper.getQnaDetail(qnaBosRequestDto);
    }

 	/**
 	 * 답변진행 상태변경
 	 * @param dto
 	 * @return
 	 * @throws Exception
 	 */
 	protected ApiResult<?> putQnaAnswerStatus(QnaBosRequestDto qnaBosRequestDto) throws Exception {

 		int result = customerQnaMapper.putQnaAnswerStatus(qnaBosRequestDto);

        return ApiResult.success(result);
    }


 	/**
 	 *  문의 답변정보 수정
 	 * @param dto
 	 * @return
 	 * @throws Exception
 	 */
 	protected ApiResult<?> putQnaInfo(QnaBosRequestDto qnaBosRequestDto) throws Exception {

 		int result = customerQnaMapper.putQnaInfo(qnaBosRequestDto);

        return ApiResult.success(result);
    }

 	/**
 	 *  문의 답변정보 등록
 	 * @param dto
 	 * @return
 	 * @throws Exception
 	 */
 	protected int addQnaAnswer(QnaBosRequestDto qnaBosRequestDto) throws Exception {

 		int result = customerQnaMapper.addQnaAnswer(qnaBosRequestDto);

        return result;
    }


	/**
	* @Desc 1:1문의 상세 첨부파일 이미지
	* @param String
	* @return List<QnaBosDetailVo>
	*/
	protected List<QnaBosDetailVo> getImageList(String csQnaId) {
		return customerQnaMapper.getImageList(csQnaId);
	}

	/**
	 * @Desc 1:1문의 등록 시 결과 조회
	 * @param urUserId
	 * @return OnetooneQnaResultVo
	 */
	protected OnetooneQnaResultVo getOnetooneQnaAddInfo(String urUserId) {
		return customerQnaMapper.getOnetooneQnaAddInfo(urUserId);
	}

	/**
	 * @Desc 문의 답변 등록 시 정보 조회
	 * @param csQnaId
	 * @return QnaBosDetailVo
	 */
	protected QnaBosDetailVo getQnaAnswerInfo(String csQnaId) {
		return customerQnaMapper.getQnaAnswerInfo(csQnaId);
	}



}
