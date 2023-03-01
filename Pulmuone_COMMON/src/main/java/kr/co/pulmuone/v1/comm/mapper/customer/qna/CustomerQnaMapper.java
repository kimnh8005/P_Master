package kr.co.pulmuone.v1.comm.mapper.customer.qna;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.customer.qna.dto.OnetooneQnaByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaInfoByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaListByUserRequestDto;
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

@Mapper
public interface CustomerQnaMapper {

    QnaInfoByUserVo getQnaInfoByUser(QnaInfoByUserRequestDto dto) throws Exception;

    Page<QnaListByUserVo> getQnaListByUser(QnaListByUserRequestDto dto) throws Exception;

    List<QnaAttcByCsQnaIdVo> getQnaAttcByCsQnaId(Long csQnaId) throws Exception;

    List<QnaAnswerByCsQnaIdVo> getQnaAnswerByCsQnaId(Long csQnaId) throws Exception;

	void putQnaAnswerUserCheckYn(Long csQnaId) throws Exception;

    Page<ProductQnaListByUserVo> getProductQnaListByUser(ProductQnaListByUserRequestDto dto) throws Exception;

    void putProductQnaSetSecretByUser(Long csQnaId) throws Exception;

    Page<ProductQnaListByGoodsVo> getProductQnaListByGoods(ProductQnaListByGoodsRequestDto dto) throws Exception;

    ProductQnaListByGoodsVo getProductQnaByCsQnaId(@Param("csQnaId") Long csQnaId, @Param("urUserId") Long urUserId ) throws Exception;

    void addProductQna(ProductQnaRequestDto dto) throws Exception;

    ProductQnaVo getProductQnaByGoods(Long csQnaId) throws Exception;

    void putProductQna(ProductQnaVo vo) throws Exception;

	int addQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto);

	int addQnaImage(OnetooneQnaByUserAttcVo onetooneQnaByUserAttcVo);

	int putQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto);

	List<OnetooneQnaByUserAttcVo> qnaImageList(Long csQnaId);

	int delQnaImage(Long csQnaId);

	OnetooneQnaByUserVo getQnaDetailByUser(@Param("csQnaId") Long csQnaId , @Param("urUserId") Long urUserId);

	List<OnetooneQnaOrderInfoByUserVo> getOrderInfoPopupByQna(@Param("searchPeriod") String searchPeriod,  @Param("urUserId") Long urUserId);

	List<OnetooneQnaOrderDetailInfoByUserVo> getOrderDetlInfoPopupByQna(@Param("odOrderId") Long odOrderId);

	List<HashMap<String,String>> getEcsHdBcodeList();

	List<HashMap<String,String>> getEcsHdScodeList(String hdBcode);

	List<HashMap<String,String>> getEcsClaimGubunList(@Param("hdBcode")String hdBcode, @Param("hdScode")String hdScode);

	Page<QnaBosListVo> getQnaList(QnaBosRequestDto qnaBosRequestDto) throws Exception;

	List<QnaBosListVo> qnaExportExcel(QnaBosRequestDto qnaBosRequestDto);

	QnaBosDetailVo getQnaDetail (QnaBosRequestDto qnaBosRequestDto) throws Exception;

	int putQnaAnswerStatus(QnaBosRequestDto qnaBosRequestDto);

	int putQnaInfo(QnaBosRequestDto qnaBosRequestDto);

	int addQnaAnswer(QnaBosRequestDto qnaBosRequestDto);

    List<QnaBosDetailVo> getImageList(String csQnaId);

	OnetooneQnaResultVo getOnetooneQnaAddInfo(String urUserId);

	QnaBosDetailVo getQnaAnswerInfo(String csQnaId);



}
