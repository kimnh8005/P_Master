package kr.co.pulmuone.v1.comm.mapper.goods.item;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.goods.discount.dto.GoodsDiscountUploadRequestDto;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountUploadListVo;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoRequestVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoTypeVo;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersVo;

@Mapper
public interface GoodsItemPoRequestMapper {

	/**
	 * @Desc 행사 발부관리 조회
	 * @param ItemPoRequestDto
	 * @return List<ItemPoRequestVo>
	 */
	Page<ItemPoRequestVo> getPoRequestList(ItemPoRequestDto paramDto);

	/**
	 * @Desc  행사 발부관리 추가
	 * @param ItemPoRequestDto
	 * @return int
	 */
	int addItemPoRequest(ItemPoRequestDto paramDto);

	/**
	 * @Desc  행사 발주관리상세
	 * @param ilPoEventId 발주유형 PK
	 * @return ItemPoRequestVo
	 */
	ItemPoRequestVo getPoRequest(String ilPoEventId);

	/**
	 * @Desc  행사 발부관리 수정
	 * @param ItemPoRequestDto
	 * @return int
	 */
	int putPoRequest(ItemPoRequestDto paramDto);

	/**
	 * @Desc  행사 발부관리 삭제
	 * @param ItemPoRequestDto
	 * @return int
	 */
	int delPoRequest(ItemPoRequestDto paramDto);

	GoodsSearchVo getGoodsIdInfo(String ilGoodsId);

	SellersVo getSellersInfo(String omSellersId);

	/**
     * @Desc 행사 발주 엑셀 업로드 로그
     * @param ItemPoRequestVo
     * @return string
     */
	int addPoEventUploadLog(ItemPoRequestVo paramDto);

	/**
     * @Desc 행사 발주 엑셀 업로드 상세 로그
     * @param ItemPoRequestVo
     * @return string
     */
	int addPoEventUploadDtlLog(ItemPoRequestVo paramDto);

	/**
	 * @Desc  행사 발부관리 추가
	 * @param ItemPoRequestVo
	 * @return int
	 */
	int addItemPoRequestUpload(ItemPoRequestVo paramDto);

	/**
	 * @Desc  행사 발주 엑셀 업로드 로그 수정
	 * @param ItemPoRequestDto
	 * @return int
	 */
	int putPoEventUploadLog(ItemPoRequestDto paramDto);

	/**
	 * @Desc 행사 발부관리 업로드 조회
	 * @param ItemPoRequestDto
	 * @return List<ItemPoRequestVo>
	 */
	Page<ItemPoRequestVo> getPoRequestUploadList(ItemPoRequestDto paramDto);

    /**
     * @Desc 행사 발주 업로드 실패 내역 - 엑셀다운로드
     * @param ItemPoRequestDto
     * @return List<ItemPoRequestVo>
     */
    List<ItemPoRequestVo> getPoRequestUploadFailList(ItemPoRequestDto paramDto);

}
