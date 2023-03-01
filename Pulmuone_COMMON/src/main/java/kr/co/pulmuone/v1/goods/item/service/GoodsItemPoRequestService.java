package kr.co.pulmuone.v1.goods.item.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemPoRequestMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoRequestVo;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersVo;
import lombok.RequiredArgsConstructor;

/**
 * dto, vo import 하기
 * <PRE>
 * Forbiz Korea
 * 행사 발주 관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.01.21  정형진
 * =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class GoodsItemPoRequestService {

	@Autowired
    private final GoodsItemPoRequestMapper goodsItemPoRequestMapper;

	/**
	 * @Desc 행사발주 요청 목록 조회
	 * @param ItemPoRequestDto
	 * @return List<ItemPoRequestVo>
	 */
	@UserMaskingRun
	protected Page<ItemPoRequestVo> getPoRequestList(ItemPoRequestDto paramDto){
		ArrayList<String> ilItemCdArray = null;

        // 권한 체크
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        paramDto.setListAuthSupplierId(listAuthSupplierId);
        paramDto.setListAuthWarehouseId(listAuthWarehouseId);

		if (!StringUtil.isEmpty(paramDto.getSearchItemCd())) {

            // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String ilItemCodeListStr = paramDto.getSearchItemCd().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
            ilItemCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
            paramDto.setSearchItemCdArray(ilItemCdArray);
        }

		PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());
		return goodsItemPoRequestMapper.getPoRequestList(paramDto);
	}

	/**
	 * @Desc  행사발주요청 추가
	 * @param ItemPoTypeRequestDto
	 * @return ItemPoRequestVo
	 */
	protected int addItemPoRequest(ItemPoRequestDto paramDto) {
		return goodsItemPoRequestMapper.addItemPoRequest(paramDto);
	}

	/**
	 * @Desc  행사발주요청 상세
	 * @param ilPoTpId 발주유형 PK
	 * @return ItemPoTypeVo
	 */
	@UserMaskingRun
	protected ItemPoRequestVo getPoRequest(String ilPoEventId) {
		return goodsItemPoRequestMapper.getPoRequest(ilPoEventId);
	}

	/**
	 * @Desc  행사발주요청 추가
	 * @param ItemPoTypeRequestDto
	 * @return ItemPoRequestVo
	 */
	protected int putPoRequest(ItemPoRequestDto paramDto) {
		return goodsItemPoRequestMapper.putPoRequest(paramDto);
	}

	/**
	 * @Desc  행사발주요청 삭제
	 * @param ItemPoTypeRequestDto
	 * @return ItemPoRequestVo
	 */
	protected int delPoRequest(ItemPoRequestDto paramDto) {
		return goodsItemPoRequestMapper.delPoRequest(paramDto);
	}

	/**
	 * @Desc  행사발주요청 상세
	 * @param ilPoTpId 발주유형 PK
	 * @return ItemPoTypeVo
	 */
	protected GoodsSearchVo getGoodsIdInfo(String ilGoodsId) {
		return goodsItemPoRequestMapper.getGoodsIdInfo(ilGoodsId);
	}

	/**
	 * @Desc  판매처 정보 조회
	 * @param ilPoTpId 발주유형 PK
	 * @return ItemPoTypeVo
	 */
	protected SellersVo getSellersInfo(String omSellersId) {
		return goodsItemPoRequestMapper.getSellersInfo(omSellersId);
	}

	/**
	* 행사발주요청 엑셀 업로드 로그 등록
	* @param
	* @return int
	*/
	@Transactional(rollbackFor = Exception.class)
	protected int addPoEventUploadLog(ItemPoRequestVo paramDto) {
		int logId = 0;
		logId = goodsItemPoRequestMapper.addPoEventUploadLog(paramDto);
		return logId;
	}



	/**
	* 행사발주요청 엑셀 업로드 상세 로그 등록
	* @param
	* @return int
	*/
	@Transactional(rollbackFor = Exception.class)
	protected int addPoEventUploadDtlLog(ItemPoRequestVo paramDto) {
		int result = 0;
		result = goodsItemPoRequestMapper.addPoEventUploadDtlLog(paramDto);
		return result;
	}

	/**
	 * @Desc  행사발주요청 추가
	 * @param ItemPoTypeRequestDto
	 * @return ItemPoRequestVo
	 */
	protected int addItemPoRequestUpload(ItemPoRequestVo paramDto) {
		return goodsItemPoRequestMapper.addItemPoRequestUpload(paramDto);
	}

	/**
	 * @Desc  행사발주요청 엑셀 업로드 로그 수정
	 * @param ItemPoTypeRequestDto
	 * @return ItemPoRequestVo
	 */
	protected int putPoEventUploadLog(ItemPoRequestDto paramDto) {
		return goodsItemPoRequestMapper.putPoEventUploadLog(paramDto);
	}

	/**
	 * @Desc 행사발주 엑셀 업로드 조회
	 * @param ItemPoRequestDto
	 * @return List<ItemPoRequestVo>
	 */
	protected Page<ItemPoRequestVo> getPoRequestUploadList(ItemPoRequestDto paramDto){
		PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());
		return goodsItemPoRequestMapper.getPoRequestUploadList(paramDto);
	}

	/**
     * @Desc 행사 발주 실패내역 - 엑셀다운로드
     * @param ItemPoRequestDto
     * @return List<ItemPoRequestVo>
     */
    protected List<ItemPoRequestVo> getPoRequestUploadFailList(ItemPoRequestDto paramDto) {
        return goodsItemPoRequestMapper.getPoRequestUploadFailList(paramDto);
    }

}
