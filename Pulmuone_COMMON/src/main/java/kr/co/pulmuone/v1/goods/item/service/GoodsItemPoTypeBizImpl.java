package kr.co.pulmuone.v1.goods.item.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoTypeVo;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.07.28  박영후           최초작성
 *  1.0    2020.10.27  이성준           리팩토링
 * =======================================================================
 * </PRE>
 */
@Service
public class GoodsItemPoTypeBizImpl implements GoodsItemPoTypeBiz {

	@Autowired
	GoodsItemPoTypeService goodsItemPoTypeService;

	/**
	 * @Desc 발주 유형관리 목록 조회
	 * @param ItemPoTypeListRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getItemPoTypeList(ItemPoTypeListRequestDto itemPoTypeListRequestDto) {
		ItemPoTypeListResponseDto result = new ItemPoTypeListResponseDto();

		ArrayList<String> searchScheduledArray = null;
		ArrayList<String> scheduledArray = new ArrayList<String>();
		ArrayList<String> scheduleNotEq = new ArrayList<String>();

		if (!StringUtil.isEmpty(itemPoTypeListRequestDto.getSearchScheduled())) {
			searchScheduledArray = StringUtil.getArrayList(itemPoTypeListRequestDto.getSearchScheduled().replace(" ", ""));

			if(searchScheduledArray.contains("ALL")) {
				itemPoTypeListRequestDto.setSearchScheduled("");
				searchScheduledArray = null;
				itemPoTypeListRequestDto.setSearchScheduledArray(searchScheduledArray);
			}else {
				scheduledArray.add("SUN");
				scheduledArray.add("MON");
				scheduledArray.add("TUE");
				scheduledArray.add("WED");
				scheduledArray.add("THU");
				scheduledArray.add("FRI");
				scheduledArray.add("SAT");

				itemPoTypeListRequestDto.setSearchScheduledArray(searchScheduledArray);

				for(String scheduleVal : scheduledArray) {
					boolean chk = false;
					for(String searchScheduleVal : itemPoTypeListRequestDto.getSearchScheduledArray()) {
						if(scheduleVal.equals(searchScheduleVal)) {
							chk = true;
							break;
						}
					}

					if(!chk) {
						scheduleNotEq.add(scheduleVal);
					}
				}

				itemPoTypeListRequestDto.setScheduledArray(scheduleNotEq);
			}

		}

		Page<ItemPoTypeVo> rows = goodsItemPoTypeService.getItemPoTypeList(itemPoTypeListRequestDto);

		result.setTotal(rows.getTotal());
		result.setRows(rows.getResult());

		return ApiResult.success(result);
	}

	/**
	 * @Desc  발주 유형관리 상세
	 * @param ilPoTpId 발주유형 PK
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getItemPoType(String ilPoTpId){
		ItemPoTypeResponseDto result = new ItemPoTypeResponseDto();
		ItemPoTypeVo vo = goodsItemPoTypeService.getItemPoType(ilPoTpId);

		result.setRows(vo);

		return ApiResult.success(result);
	}

	/**
	 * @Desc  발주일 상세
	 * @param ilPoTpId 발주유형 PK
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getItemPoDay(String ilPoTpId, String eventStartDtNumber){
		HashMap eventResult = goodsItemPoTypeService.getItemPoDayForEvent(ilPoTpId, eventStartDtNumber);

		ItemPoTypeVo vo = new ItemPoTypeVo();
		String recevingReqDt = ""; //입고요청일
		if (eventResult != null && eventResult.get("RECEVING_REQ_DT") != null)
			recevingReqDt = eventResult.get("RECEVING_REQ_DT").toString();

		String poScheduleDt = ""; //발주예정일
		if (eventResult != null && eventResult.get("PO_SCHEDULE_DT") != null)
			poScheduleDt = eventResult.get("PO_SCHEDULE_DT").toString();

		vo.setRecevingReqDt(recevingReqDt);
        vo.setPoScheduleDt(poScheduleDt);

		ItemPoTypeResponseDto result = new ItemPoTypeResponseDto();
		result.setRows(vo);

		return ApiResult.success(result);
	}

	/**
	 * @Desc   발주 유형관리 추가
	 * @param  ItemPoTypeRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addItemPoType(ItemPoTypeRequestDto itemPoTypeRequestDto) throws Exception {
		ItemPoTypeVo poTypeVo = setDtoToVo(itemPoTypeRequestDto);

		if(goodsItemPoTypeService.duplicateItemPoTypeCount(poTypeVo) > 0) {
			return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
        } else {
        	goodsItemPoTypeService.addItemPoType(itemPoTypeRequestDto);
        }

		return ApiResult.success();
	}

	/**
	 * 발주 유형관리 수정
	 * @param poTypeRequestDto
	 * @return PoTypeResponseDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putItemPoType(ItemPoTypeRequestDto itemPoTypeRequestDto) throws Exception {
		ItemPoTypeVo poTypeVo = setDtoToVo(itemPoTypeRequestDto);

		if(goodsItemPoTypeService.duplicateItemPoTypeCount(poTypeVo) > 0) {
			return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
        } else {
        	goodsItemPoTypeService.putItemPoType(itemPoTypeRequestDto);
        }

		return ApiResult.success();
	}

	/**
	 * 발주 유형관리 삭제
	 * @param ilPoTpId
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> delItemPoType(String ilPoTpId) throws Exception {

		goodsItemPoTypeService.delItemPoType(ilPoTpId);

		return ApiResult.success();
	}

	/**
	 * 발주 유형관리 DTO 를 VO 로 변환
	 * @param poTypeRequestDto
	 * @parm poTypeVo
	 * @return PoTypeVo
	 * @throws Exception
	 */
	private ItemPoTypeVo setDtoToVo(ItemPoTypeRequestDto poTypeRequestDto) throws Exception {
		ItemPoTypeVo poTypeVo = new ItemPoTypeVo();

		if (StringUtils.isNotBlank(poTypeRequestDto.getIlPoTpId()))
			poTypeVo.setIlPoTpId( poTypeRequestDto.getIlPoTpId());
		poTypeVo.setUrSupplierId( poTypeRequestDto.getUrSupplierId());
		poTypeVo.setPoTpNm(poTypeRequestDto.getPoTpNm());
		poTypeVo.setPoType(poTypeRequestDto.getPoTp());

		if (StringUtils.isNotBlank(poTypeRequestDto.getStockPlannedDays()))
			poTypeVo.setStockPlannedDays( Integer.parseInt(poTypeRequestDto.getStockPlannedDays()) );
		poTypeVo.setErpPoType(poTypeRequestDto.getErpPoTp());
		poTypeVo.setUserVo(poTypeRequestDto.getUserVo());

		return poTypeVo;
	}

}
