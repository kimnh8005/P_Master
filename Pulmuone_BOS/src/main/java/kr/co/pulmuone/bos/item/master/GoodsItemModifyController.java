package kr.co.pulmuone.bos.item.master;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.bos.comm.constant.BosStorageInfoEnum;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType.StorageType;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemModifyRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceHistoryRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemModifyBiz;
import lombok.extern.slf4j.Slf4j;


/**
 * <PRE>
 * Forbiz Korea
 * 마스터 품목 수정 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 11. 05.               박주형         최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@RestController
public class GoodsItemModifyController {

    @Autowired
    private GoodsItemModifyBiz goodsItemModifyBiz;

    /**
     * @Desc 해당 품목코드로 IL_ITEM 테이블의 마스터 품목 정보 조회 API
     *
     * @param ilItemCode : 검색할 품목 코드
     *
     * @return ApiResult<?> : 마스터 품목 정보 조회 ApiResult
     */
    @GetMapping("/admin/item/master/modify/getMasterItem")
    @ApiOperation(value = "마스터 품목 정보 조회 API")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "ilItemCode", value = "품목 코드", required = true, dataType = "string") //
            , @ApiImplicitParam(name = "ilItemApprId", value = "품목 승인 코드", required = false, dataType = "string") //
    })
    public ApiResult<?> getMasterItem( //
            @RequestParam(value = "ilItemCode", required = true) String ilItemCode // 품목코드
            , @RequestParam(value = "ilItemApprId", required = false) String ilItemApprId // 품목 승인 코드
    ) {

        try {

            return goodsItemModifyBiz.getMasterItem(ilItemCode, ilItemApprId);

        } catch (BaseException be) {
            log.error(be.getMessage(), be);
            return ApiResult.result(be.getMessageEnum());

        } catch (Exception e) { // 기타 예외 발생시
            log.error(e.getMessage(), e);
            throw e;

        }

    }

    /**
     * @Desc 해당 품목의 현재/예정 가격정보 목록 조회 API
     *
     * @param ilItemCode : 품목 코드
     *
     * @return ApiResult : 해당 품목의 현재/예정 가격정보 목록 조회 ApiResult
     */
    @GetMapping(value = "/admin/item/master/modify/getItemPriceSchedule")
    @ApiOperation(value = "해당 품목의 현재/예정 가격정보 목록 조회")
    public ApiResult<?> getItemPriceSchedule( //
            @RequestParam(value = "ilItemCode", required = true) String ilItemCode // 품목코드
    ) {

        return goodsItemModifyBiz.getItemPriceSchedule(ilItemCode);

    }

    /**
     * @Desc 해당 품목의 ERP 가격정보 목록 조회
     *
     * @param ilItemCode : 품목 코드
     *
     * @return ApiResult : 해당 품목의 ERP 가격정보 목록 조회  ApiResult
     */
    @GetMapping(value = "/admin/item/master/modify/getItemErpPriceSchedule")
    @ApiOperation(value = "해당 품목의 ERP 가격정보 목록 조회 ")
    public ApiResult<?> getItemErpPriceSchedule( //
            @RequestParam(value = "ilItemCode", required = true) String ilItemCode // 품목코드
    ) {

        return goodsItemModifyBiz.getItemErpPriceSchedule(ilItemCode);

    }

    /**
     * @Desc 해당 품목의 가격정보 전체 이력 조회 API
     *
     * @param ItemPriceHistoryRequestDto : 가격정보 이력 조회 request dto
     *
     * @return ApiResult : 해당 품목의 가격정보 전체 이력 조회 ApiResult
     */
    @PostMapping(value = "/admin/item/master/modify/getItemPriceHistory")
    @ApiOperation(value = "해당 품목의 가격정보 전체 이력 조회")
    public ApiResult<?> getItemPriceHistory( //
            ItemPriceHistoryRequestDto itemPriceHistoryRequestDto // 가격정보 이력 조회 request dto
            , HttpServletRequest request //
    ) throws Exception {

        itemPriceHistoryRequestDto = (ItemPriceHistoryRequestDto) BindUtil.convertRequestToObject(request, ItemPriceHistoryRequestDto.class);
        return goodsItemModifyBiz.getItemPriceHistory(itemPriceHistoryRequestDto);

    }

    /**
     * @Desc 해당 품목의 ERP 행사가격정보 전체 이력 조회 API
     *
     * @param ItemPriceHistoryRequestDto : ERP 행사가격정보 request dto
     *
     * @return ApiResult : 해당 품목의 ERP 행사가격정보 전체 이력 조회 ApiResult
     */
    @PostMapping(value = "/admin/item/master/modify/getItemDiscountPriceHistory")
    @ApiOperation(value = "해당 품목의 가격정보 전체 이력 조회")
    public ApiResult<?> getItemDiscountPriceHistory( //
            ItemPriceHistoryRequestDto itemPriceHistoryRequestDto // 가격정보 이력 조회 request dto
            , HttpServletRequest request //
    ) throws Exception {

        itemPriceHistoryRequestDto = (ItemPriceHistoryRequestDto) BindUtil.convertRequestToObject(request, ItemPriceHistoryRequestDto.class);
        return goodsItemModifyBiz.getItemDiscountPriceHistory(itemPriceHistoryRequestDto);

    }



    /**
     * @Desc 마스터 품목 정보 수정 API
     *
     * @param ItemModifyRequestDto : 마스터 품목 수정 request dto
     *
     * @return ApiResult : 마스터 품목 수정 ApiResult
     * @throws Exception
     */
    @PostMapping("/admin/item/master/modify/modifyItem")
    @ApiOperation(value = "마스터 품목 정보 수정")
    public ApiResult<?> modifyMasterItem( //
            @RequestBody ItemModifyRequestDto itemModifyRequestDto // 마스터 품목 정보 수정 request dto
    ) throws Exception {

        // 품목 이미지 재등록 관련 : BosStorageInfoEnum 에 선언된 public 저장소의 최상위 저장 디렉토리 경로를 dto 에 세팅
        String publicRootStoragePath = BosStorageInfoEnum.getRootStoragePath(StorageType.PUBLIC.name());
        itemModifyRequestDto.setImageRootStoragePath(publicRootStoragePath);

        try {

            return goodsItemModifyBiz.modifyMasterItem(itemModifyRequestDto);

        } catch (BaseException be) {
            log.error(be.getMessage(), be);
            return ApiResult.result(be.getMessageEnum());

        } catch (Exception e) { // 기타 예외 발생시
            log.error(e.getMessage(), e);
            throw e;

        }

    }

    /**
     * 마스터품목 리스트 조회
     *
     * @param MasterItemListRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "마스터품목 리스트 조회")
    @PostMapping(value = "/admin/item/master/getPoTpDetailInfoList")
    public ApiResult<?> getPoTpDetailInfoList(HttpServletRequest request, MasterItemListRequestDto masterItemListRequestDto) throws Exception {

        masterItemListRequestDto = BindUtil.bindDto(request, MasterItemListRequestDto.class);
        return goodsItemModifyBiz.getPoTpDetailInfoList(masterItemListRequestDto);

    }

    @GetMapping("/admin/item/master/modify/refactoringImage")
    @ApiOperation(value = "마스터 품목 이미지 수정")
    public ApiResult<?> refactoringImage( //
    ) throws Exception {

        // 품목 이미지 재등록 관련 : BosStorageInfoEnum 에 선언된 public 저장소의 최상위 저장 디렉토리 경로를 dto 에 세팅
        String publicRootStoragePath = BosStorageInfoEnum.getRootStoragePath(StorageType.PUBLIC.name());
        ItemModifyRequestDto itemModifyRequestDto = new ItemModifyRequestDto();
        itemModifyRequestDto.setImageRootStoragePath(publicRootStoragePath);

        try {
            return goodsItemModifyBiz.refactoringImage(itemModifyRequestDto);
        } catch (BaseException be) {
            log.error(be.getMessage(), be);
            return ApiResult.result(be.getMessageEnum());

        } catch (Exception e) { // 기타 예외 발생시
            log.error(e.getMessage(), e);
            throw e;

        }

    }

}
