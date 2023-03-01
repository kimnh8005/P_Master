package kr.co.pulmuone.v1.goods.notice.service;

import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.mapper.goods.notice.GoodsNoticeMapper;
import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeDto;
import kr.co.pulmuone.v1.goods.notice.dto.vo.GoodsNoticeVo;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		20201203		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */

@Service
@RequiredArgsConstructor
public class GoodsNoticeService {

	@Autowired
	private final GoodsNoticeMapper goodsNoticeMapper;

    /**
     * 상품공통공지 조회
     *
     * @param ilNoticeId
     * @return GoodsNoticeVo
     */
	@UserMaskingRun
    protected GoodsNoticeVo getGoodsNoticeInfo(String ilNoticeId) {
    	return goodsNoticeMapper.getGoodsNoticeInfo(ilNoticeId);
    }
    /**
     * 상품공통공지 목록 조회
     *
     * @param GoodsNoticeDto
     * @return GoodsNoticeDto
     */
    @UserMaskingRun
    protected GoodsNoticeDto getGoodsNoticeList(GoodsNoticeDto dto) {
    	GoodsNoticeDto result = new GoodsNoticeDto();
    	PageMethod.startPage(dto.getPage(), dto.getPageSize());
    	Page<GoodsNoticeVo> rows = goodsNoticeMapper.getGoodsNoticeList(dto);
		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());
		return result;
    }
    /**
     * 상품공통공지 신규 등록
     *
     * @param GoodsNoticeDto
     * @return ApiResult<?>
     */
    protected MessageCommEnum addGoodsNotice(GoodsNoticeDto dto) {
    	int cnt = goodsNoticeMapper.addGoodsNotice(dto);
    	if(cnt < 1) return BaseEnums.Default.FAIL;
    	return BaseEnums.Default.SUCCESS;
    }
    /**
     * 상품공통공지 수정
     *
     * @param GoodsNoticeDto
     * @return MessageCommEnum
     */
    protected MessageCommEnum putGoodsNotice(GoodsNoticeDto dto) {
    	int cnt = goodsNoticeMapper.putGoodsNotice(dto);
    	if(cnt < 1) return BaseEnums.Default.FAIL;
    	return BaseEnums.Default.SUCCESS;
    }

    /**
     * 상품공통공지 목록 조회 - User
     * @param warehouseGroupCode String
     * @param urWarehouseId Long
     * @return List<GoodsNoticeRequestDto>
     */
    protected List<GoodsNoticeResponseDto> getGoodsNoticeListByUser(String warehouseGroupCode, Long urWarehouseId) {
        List<GoodsNoticeResponseDto> result = new ArrayList<>();
        List<GoodsNoticeVo> voList = goodsNoticeMapper.getGoodsNoticeListByUser();

        for (GoodsNoticeVo vo : voList) {
            // 전체여부 확인
            if (vo.getDispAllYn().equals("Y")) {
                result.add(new GoodsNoticeResponseDto(vo));
                continue;
            }
            // 출고처 그룹 확인
            if (vo.getWarehouseGroup().equals(warehouseGroupCode)) {
                // 출고처 전체의 경우
                if (vo.getUrWarehouseId().equals("0")) {
                    result.add(new GoodsNoticeResponseDto(vo));
                    continue;
                }
                // 출고처 선택의 경우
                if (vo.getUrWarehouseId().equals(String.valueOf(urWarehouseId))) {
                    result.add(new GoodsNoticeResponseDto(vo));
                }
            }
        }

        return result;
    }
}
