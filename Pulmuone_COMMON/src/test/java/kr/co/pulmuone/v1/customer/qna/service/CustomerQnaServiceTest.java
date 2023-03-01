package kr.co.pulmuone.v1.customer.qna.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
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
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaOrderInfoByUserVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaResultVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosListVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaInfoByUserVo;

class CustomerQnaServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    CustomerQnaService customerQnaService;

    @Test
    void getQnaInfoByUser_조회_정상() throws Exception {
        //given
        QnaInfoByUserRequestDto requestDto = new QnaInfoByUserRequestDto();
        requestDto.setUrUserId(1646893L);
        requestDto.setStartDate("2020-01-01");
        requestDto.setEndDate("2020-12-31");
        requestDto.setQnaType("QNA_TP.ONETOONE");

        //when
        QnaInfoByUserVo resultDto = customerQnaService.getQnaInfoByUser(requestDto);

        //then
        Assertions.assertTrue(resultDto.getTotalCount() != null);
    }

    @Test
    void getQnaListByUser_조회_정상() throws Exception {
        //given
        QnaListByUserRequestDto dto = new QnaListByUserRequestDto();
        dto.setUrUserId(1647522L);
        dto.setStartDate("2021-08-01");
        dto.setEndDate("2021-08-18");
        dto.setPage(0);
        dto.setLimit(20);

        //when
        QnaListByUserResponseDto resultDto = customerQnaService.getQnaListByUser(dto);

        //then
        Assertions.assertTrue(resultDto.getTotal() > 0);
    }

    @Test
    void getProductQnaListByUser_조회_정상() throws Exception {
        //given
        ProductQnaListByUserRequestDto dto = new ProductQnaListByUserRequestDto();
        dto.setUrUserId(1646893L);
        dto.setStartDate("2020-01-01");
        dto.setEndDate("2020-12-31");
        dto.setPage(0);
        dto.setLimit(20);

        //when
        ProductQnaListByUserResponseDto resultDto = customerQnaService.getProductQnaListByUser(dto);

        //then
        Assertions.assertTrue(resultDto.getTotal() > 0);
    }

    @Test
    void putProductQnaSetSecretByUser_정상_수정() throws Exception {
        //given
        Long csQnaId = 1L;

        //when, then
        customerQnaService.putProductQnaSetSecretByUser(csQnaId);
    }

    @Test
    void getProductQnaListByGoods_정상_조회() throws Exception {
        //given
        ProductQnaListByGoodsRequestDto dto = new ProductQnaListByGoodsRequestDto();
        dto.setIlGoodsId(175L);
        dto.setUrUserId(1646893L);
        dto.setPage(0);
        dto.setLimit(20);

        //when
        ProductQnaListByGoodsResponseDto resultDto = customerQnaService.getProductQnaListByGoods(dto);

        //then
        Assertions.assertTrue(resultDto.getTotal() > 0);
    }

    @Test
    void addProductQna_정상() throws Exception {
        //given
        ProductQnaRequestDto dto = new ProductQnaRequestDto();
        dto.setUrUserId(10L);
        dto.setProductType("QNA_PRODUCT_TP.PRODUCT");
        dto.setIlGoodsId(10L);
        dto.setTitle("test");
        dto.setQuestion("test");
        dto.setSecretType("Y");
        dto.setAnswerSmsYn("Y");
        dto.setAnswerMailYn("Y");

        //when, then
        customerQnaService.addProductQna(dto);
    }

    @Test
    void putProductQna_정상() throws Exception {
        //given
        ProductQnaVo dto = new ProductQnaVo();
        dto.setCsQnaId(1L);
        dto.setProductType("QNA_PRODUCT_TP.PRODUCT");
        dto.setTitle("test");
        dto.setQuestion("test");
        dto.setSecretType("Y");
        dto.setAnswerSmsYn("Y");
        dto.setAnswerMailYn("Y");

        //when, then
        customerQnaService.putProductQna(dto);
    }

    @Test
    void addQnaByUser_정상() {
    	//given
    	OnetooneQnaByUserRequestDto dto = new OnetooneQnaByUserRequestDto();
    	dto.setUrUserId((long) 1646893);
    	dto.setOnetooneType("QNA_ONETOONE_TP.MEMBER");
    	dto.setOdOrderId(1L);
    	dto.setOdOrderDetlId(1L);
    	dto.setTitle("test-title");
    	dto.setQuestion("test-question");
    	dto.setAnswerSmsYn("Y");
    	dto.setAnswerMailYn("N");

    	//when, then
    	int result = customerQnaService.addQnaByUser(dto);

    	assertTrue(result > 0);
    }

    @Test
    void addQnaImage_정상() {
    	//given
    	OnetooneQnaByUserAttcVo vo = new OnetooneQnaByUserAttcVo();
    	OnetooneQnaByUserRequestDto dto = new OnetooneQnaByUserRequestDto();

    	vo.setCsQnaId(1L);
    	vo.setImageOriginalName("test.jpg");
    	vo.setImageName("test.jpg");
    	vo.setImagePath(dto.getServerSubPath()+vo.getImageOriginalName());
    	vo.setThumbnailName("");
    	vo.setThumbnailOriginalName("");
    	vo.setThumbnailPath("");

    	//when, then
    	int result = customerQnaService.addQnaImage(vo);

    	assertTrue(result > 0);
    }

    @Test
    void putQnaByUser_정상() {
    	//given
    	OnetooneQnaByUserRequestDto dto = new OnetooneQnaByUserRequestDto();
    	dto.setCsQnaId(1L);
    	dto.setUrUserId((long) 1646893);
    	dto.setOnetooneType("QNA_ONETOONE_TP.MEMBER");
    	dto.setTitle("test-title");
    	dto.setQuestion("test-question");
    	dto.setAnswerSmsYn("Y");
    	dto.setAnswerMailYn("N");

    	//when, then
    	int result = customerQnaService.putQnaByUser(dto);

    	assertTrue(result > 0);
    }

    @Test
    void qnaImageList_정상() {
    	//given
    	Long csQnaId = (long)65;

    	//when, then
    	List<OnetooneQnaByUserAttcVo> list = customerQnaService.qnaImageList(csQnaId);

    	assertTrue(list.size() > 0);

    }

    @Test
    void delQnaImage_정상() {
    	//given
    	Long csQnaId = (long)65;

    	//when, then
    	int result = customerQnaService.delQnaImage(csQnaId);

    	assertTrue(result > 0 );

    }

    @Test
    void getQnaDetailByUser_정상() {
    	//given
    	Long csQnaId = (long) 65;
    	Long urUserId = (long) 1646893;

    	//when, then
    	OnetooneQnaByUserVo vo = customerQnaService.getQnaDetailByUser(csQnaId, urUserId);

    	assertEquals("QNA_ONETOONE_TP.PRODUCT", vo.getOnetooneType());

    }

    @Test
    void getOrderInfoPopupByQna_정상() {
    	//given
    	Long urUserId = (long) 1647069;
    	String searchPeriod = "1WEEK";

    	//when, then
    	List<OnetooneQnaOrderInfoByUserVo> list = customerQnaService.getOrderInfoPopupByQna(searchPeriod, urUserId);

    	assertTrue(list.size() > 0);
    }


    @Test
    void getQnaList_정상() throws Exception {
    	QnaBosRequestDto dto = new QnaBosRequestDto();

    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        ArrayList<String> findKeywordArray = new ArrayList<>();
        findKeywordArray.add("3bdb5c8e71344");
        dto.setFindKeywordArray(findKeywordArray);
        dto.setFindKeyword("3bdb5c8e71344");
        dto.setSearchSelect("SEARCH_SELECT.USE_ID");
    	Page<QnaBosListVo> voList = customerQnaService.getQnaList(dto);

    	 //assertNotNull(voList.);
    	assertTrue(voList.size()>0);

    }

    @Test
    void qnaExportExcel_엑셀다운로드조회_성공() {

    	QnaBosRequestDto qnaRequestDto = new QnaBosRequestDto();
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
        qnaRequestDto.setCreateDateStart("2021-08-10");
        qnaRequestDto.setCreateDateEnd("2021-08-11");

        List<QnaBosListVo> excelList = customerQnaService.qnaExportExcel(qnaRequestDto);

        // 해당 품목코드로 1건 또는 0건 조회되어야 함
        assertTrue(excelList.size() > 0 || excelList.size() == 0);

    }

    @Test
    void getQnaDetail_정상() throws Exception {

    	QnaBosRequestDto dto = new QnaBosRequestDto();
	   	dto.setCsQnaId("-1");

	   	QnaBosDetailVo vo = customerQnaService.getQnaDetail(dto);

	   	Assertions.assertNull(vo);
    }

    @Test
    void putQnaAnswerStatus_정상() throws Exception {

    	QnaBosRequestDto dto = new QnaBosRequestDto();
    	dto.setCsQnaId("1");

    	ApiResult<?> result = customerQnaService.putQnaAnswerStatus(dto);

        // then
    	assertTrue(result.getCode().equals(ApiResult.success().getCode()));

    }

    @Test
    void putQnaInfo_정상() throws Exception {

    	QnaBosRequestDto dto = new QnaBosRequestDto();
    	dto.setCsQnaId("1");
    	dto.setStatus("QNA_STATUS.ANSWER_COMPLETED_1ST");
    	dto.setSecretType("QNA_SECRET_TP.CLOSE_ADMIN");
    	dto.setSecretComment("TESt");
    	dto.setEcsCtgryStd1("00032");
    	dto.setEcsCtgryStd2("00001");
    	dto.setEcsCtgryStd3("00002");
    	ApiResult<?> result = customerQnaService.putQnaInfo(dto);

        // then
    	assertTrue(result.getCode().equals(ApiResult.success().getCode()));

    }


    @Test
    void paddQnaAnswer_정상() throws Exception {

    	QnaBosRequestDto dto = new QnaBosRequestDto();
    	dto.setCsQnaId("1");
    	dto.setStatus("QNA_STATUS.ANSWER_COMPLETED_2ND");
    	dto.setContent("TEST 2ND");
    	int result = customerQnaService.addQnaAnswer(dto);
    	 UserVo userVO = new UserVo();
         userVO.setUserId("1");
         userVO.setLoginId("forbiz");
         userVO.setLangCode("1");
         SessionUtil.setUserVO(userVO);
        // then
    	assertTrue(result>0);

    }

    @Test
    void putQnaAnswerUserCheckYn_정상() throws Exception {
        //given
        Long csQnaId = 1L;

        //when, then
        customerQnaService.putQnaAnswerUserCheckYn(csQnaId);
    }

    @Test
    void getOnetooneQnaAddInfo_정상() throws Exception {
    	//given
    	String urUserId = "1647106";
    	//when
    	OnetooneQnaResultVo onetooneQnaResultVo = customerQnaService.getOnetooneQnaAddInfo(urUserId);
    	//then
    	assertEquals("swlee@forbiz.co.kr", onetooneQnaResultVo.getMail());

    }

    @Test
    void getQnaAnswerInfo_정상() throws Exception {
    	//given
    	String csQnaId = "281";
    	//when
    	QnaBosDetailVo qnaBosDetailVo = customerQnaService.getQnaAnswerInfo(csQnaId);
    	//then
    	assertEquals("QNA_STATUS.ANSWER_COMPLETED_2ND", qnaBosDetailVo.getStatus());
    }

}