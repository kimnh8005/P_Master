package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.item.dto.ItemModifyRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceHistoryRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPriceVo;

import java.util.List;

public interface GoodsItemModifyBiz {

    /*
     * 해당 마스터 품목 정보 조회 로직 Start
     */

    // 해당 품목코드로 IL_ITEM 테이블의 마스터 품목 정보 조회
    public ApiResult<?> getMasterItem(String ilItemCode, String ilItemApprId) throws BaseException;

    /*
     * 해당 마스터 품목 정보 조회 로직 End
     */

    /*
     * 해당 품목의 가격정보 조회 로직 Start
     */

    // 해당 품목의 현재/예정 가격정보 목록 조회 : 과거 이력은 제외
    public ApiResult<?> getItemPriceSchedule(String ilItemCode);

    // 해당 품목의 가격정보 전체 이력 목록 조회 : 페이지네이션 적용
    public ApiResult<?> getItemPriceHistory(ItemPriceHistoryRequestDto itemPriceHistoryRequestDto);

    // 해당 품목의 ERP 가격정보 목록 조회
    public ApiResult<?> getItemErpPriceSchedule(String ilItemCode);

    // 해당 품목의 Discount 가격정보 전체 이력 목록 조회 : 페이지네이션 적용
    public ApiResult<?> getItemDiscountPriceHistory(ItemPriceHistoryRequestDto itemPriceHistoryRequestDto);

    /*
     * 해당 품목의 가격정보 조회 로직 End
     */

    /*
     * 마스터 품목 정보 수정 관련 로직 Start
     */

    // 마스터 품목 정보 수정
    public ApiResult<?> modifyMasterItem(ItemModifyRequestDto itemModifyRequestDto) throws Exception;

    public ApiResult<?> refactoringImage(ItemModifyRequestDto itemModifyRequestDto) throws Exception;

    /*
     * 마스터 품목 정보 수정 관련 로직 End
     */
    ApiResult<?> getPoTpDetailInfoList(MasterItemListRequestDto masterItemListRequestDto);

	//품목 승인 이후에 수정된 데이터 처리
	MessageCommEnum itemApprAfterProc(String ilItemApprId, String ilItemCd, String apprKindTp) throws Exception;

    int deleteItemPriceOrigin(String ilItemCode, String priceApplyStartDate) throws Exception;

    List<ItemPriceVo> getItemPriceListByDate(String ilItemCode, String targetDate);

}
