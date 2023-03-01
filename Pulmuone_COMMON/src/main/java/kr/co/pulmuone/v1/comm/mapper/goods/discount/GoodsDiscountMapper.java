package kr.co.pulmuone.v1.comm.mapper.goods.discount;

import java.util.List;

import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDiscountApprVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.discount.dto.GoodsDiscountUploadRequestDto;
import kr.co.pulmuone.v1.goods.discount.dto.vo.DiscountInfoVo;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountUploadListVo;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.DetailSelectGoodsVo;

@Mapper
public interface GoodsDiscountMapper {

    /**
     * @Desc 상품할인 리스트 조회
     * @param goodsId
     * @param discountTypeCode
     * @return List<GoodsDiscountVo>
     */
    List<GoodsDiscountVo> getGoodsDiscountList(@Param("goodsId") Long goodsId, @Param("discountTypeCode") String discountTypeCode);

    /**
     * @Desc 상품할인 삭제
     * @param goodsDiscountId
     * @return int
     */
    int deleteGoodsDiscount(@Param("goodsDiscountId") Long goodsDiscountId);

	/**
	 * @Desc 상품할인 사용안함 처리(삭제 처리)
	 * @param goodsDiscountId
	 * @return int
	 */
	int updateGoodsDiscount(@Param("goodsDiscountId") Long goodsDiscountId, @Param("userId") String userId);

	int putGoodsDiscount(@Param("ilGoodsId") Long ilGoodsId, @Param("discountTypeCode") String discountTypeCode, @Param("discountStartDateTime") String discountStartDateTime);

	/**
	 * @Desc 상품할인승인 승인상태 변경 처리(할인 삭제 처리 시)
	 * @param goodsDiscountApprId
	 * @return int
	 */
	int updateGoodsDiscountApprStat(@Param("goodsDiscountApprId") Long goodsDiscountApprId, @Param("apprStat") String apprStat, @Param("userId") String userId);

	/**
	 * @Desc 상품할인승인히스토리 입력(할인 삭제 처리 시)
	 * @param goodsDiscountApprId
	 * @return int
	 */
	int insertGoodsDiscountApprStatusHistoryStat(@Param("goodsDiscountApprId") Long goodsDiscountApprId, @Param("apprStat") String apprStat, @Param("userId") String userId);

	/**
	 * @Desc 상품 할인 승인 내역 확인, 승인 내역 존재시 요청 자격 확인
	 * @param goodsDiscountApprId
	 * @return int
	 */
	GoodsDiscountApprVo goodsDiscountApprInfo(@Param("goodsDiscountApprId") Long goodsDiscountApprId) throws Exception;

    /**
     * @Desc 묶음상품 개별품목 내역을 삭제
     * @param goodsDiscountId
     * @return int
     */
    int deleteIlGoodsPackageItemFixedDiscountPrice(@Param("goodsDiscountId") Long goodsDiscountId);

    /**
     * @Desc 상품할인 종료일 조회
     * @param ilGoodsId
     * @param discountTypeCode
     * @return string
     */
    String getGoodsDiscountMinEndDt(@Param("ilGoodsId") Long ilGoodsId, @Param("discountStartDate") String discountStartDate);


    /**
     * @Desc 상품할인 등록/수정
     * @param discountInfoVo
     * @return string
     */
//    int addGoodsDiscountByBatch(DiscountInfoVo discountInfoVo);


    /**
     * @Desc 상품할인 기존 데이터 시작일 수정
     * @param discountInfoVo
     * @return string
     */
	int putPastGoodsDiscountByBatch(DiscountInfoVo discountInfoVo);

	/**
	 * @Desc 상품할인승인 기존 데이터 시작일 수정
	 * @param discountInfoVo
	 * @return string
	 */
	int putPastGoodsDiscountApprByBatch(DiscountInfoVo discountInfoVo);


    /**
     * @Desc 상품할인 시작일 이전데이터 목록 조회
     * @param discountInfoVo
     * @return string
     */
	List<DiscountInfoVo> getGoodsDiscountStartDateUnderList(@Param("ilGoodsId") Long ilGoodsId, @Param("discountStartDate") String discountStartDate );


    /**
     * @Desc 상품할인 배치, 상품가격변경처리여부 수정
     * @param discountInfoVo
     * @return string
     */
	int putGoodsBatchChange(@Param("ilGoodsId") Long ilGoodsId);


    /**
     * @Desc 상품등록시 상품할인 정보 등록
     * @param discountInfoVo
     * @return string
     */
    int addGoodsDiscountByAddGoods(DiscountInfoVo discountInfoVo);

    /**
     * @Desc 상품 상세 조회
     * @param GoodsDiscountVo
     * @return DetailSelectGoodsVo
     */
    DetailSelectGoodsVo getGoodsInfo(GoodsDiscountVo paramDto);

    /**
     * @Desc 상품할인 정보 조회
     * @param GoodsDiscountVo
     * @return DetailSelectGoodsVo
     */
    GoodsDiscountVo getGoodsDiscountInfo(GoodsDiscountVo paramDto);

    /**
     * @Desc 상품 할인 엑셀 업로드 등록
     * @param GoodsDiscountVo
     * @return string
     */
	int addGoodsDiscountExcelUpload(GoodsDiscountVo paramDto);

	/**
     * @Desc 상품 할인 엑셀 로그 등록
     * @param GoodsDiscountVo
     * @return string
     */
	int addGoodsDiscountExcelLog(GoodsDiscountVo paramDto);

	/**
     * @Desc 상품 할인 엑셀 상세 로그 등록
     * @param GoodsDiscountVo
     * @return string
     */
	int addGoodsDiscountExcelDtlLog(GoodsDiscountVo paramDto);

	/**
     * @Desc 상품 할인 엑셀 상세 로그 등록
     * @param GoodsDiscountVo
     * @return string
     */
	int putGoodsDiscountExcelLog(GoodsDiscountVo paramDto);

	/**
     * @Desc 상품 할인 일괄 업로드 내역 조회
     * @param GoodsDiscountUploadRequestDto
     * @return Page<GoodsDiscountUploadListVo>
     */
    Page<GoodsDiscountUploadListVo> getGoodsDiscountUploadList(GoodsDiscountUploadRequestDto paramDto);

    /**
     * @Desc 상품 할인 일괄 업로드 실패 내역 - 엑셀다운로드
     * @param GoodsDiscountUploadRequestDto
     * @return List<GoodsDiscountUploadListVo>
     */
    List<GoodsDiscountUploadListVo> getGoodsDiscountUploadFailList(GoodsDiscountUploadRequestDto paramDto);

    /**
     * @Desc 상품 할인 일괄 업로드 내역 상세 조회
     * @param GoodsDiscountUploadRequestDto
     * @return Page<GoodsDiscountUploadListVo>
     */
    Page<GoodsDiscountUploadListVo> getGoodsDiscountUploadDtlList(GoodsDiscountUploadRequestDto paramDto);

    /**
     * @Desc 상품 할인 일괄 업로드 내역 상세 조회 - 엑셀 다운로드
     * @param GoodsDiscountUploadRequestDto
     * @return Page<GoodsDiscountUploadListVo>
     */
    List<GoodsDiscountUploadListVo> getGoodsDiscountUploadDtlListExcel(GoodsDiscountUploadRequestDto paramDto);

    /**
     * @Desc 임직원 상품 할인 일괄 업로드 실패 내역 - 엑셀다운로드
     * @param GoodsDiscountUploadRequestDto
     * @return List<GoodsDiscountUploadListVo>
     */
    List<GoodsDiscountUploadListVo> getGoodsDiscountEmpUploadFailList(GoodsDiscountUploadRequestDto paramDto);

    /**
     * @Desc 임직원 상품 할인 일괄 업로드 상세 조회
     * @param GoodsDiscountUploadRequestDto
     * @return Page<GoodsDiscountUploadListVo>
     */
    Page<GoodsDiscountUploadListVo> getGoodsDiscountEmpUploadDtlList(GoodsDiscountUploadRequestDto paramDto);

    /**
     * @Desc 임직원 할인 일괄 업로드 내역 상세 조회 - 엑셀 다운로드
     * @param GoodsDiscountUploadRequestDto
     * @return Page<GoodsDiscountUploadListVo>
     */
    List<GoodsDiscountUploadListVo> getGoodsDiscountEmpUploadDtlListExcel(GoodsDiscountUploadRequestDto paramDto);

    /**
     * @Desc 묶음상품 기본할인 삭제 전 이전날짜 UPDATE 대상 찾기
     * @param goodsId
     * @param discountTypeCode
     * @return GoodsDiscountVo
     */
    GoodsDiscountVo getGoodsPackageBaseDiscountUpdateList(@Param("goodsDiscountId") Long goodsDiscountId, @Param("discountTypeCode") String discountTypeCode);


    /**
     * @Desc 묶음 상품 가격 프로시져
     * @param goodsRegistRequestDto
     * @return int
     */
    int spGoodsPriceUpdateWhenPackageGoodsChanges(GoodsRegistRequestDto goodsRegistRequestDto);

    /**
     * @Desc 상품 할인 변경에 의한 일반 상품 가격 프로시져
     * @param String
     * @param boolean
     * @return void
     */
	void spGoodsPriceUpdateWhenGoodsDiscountChanges(@Param("ilGoodsId") String ilItemCode, @Param("inDebugFlag") boolean inDebugFlag);

}
