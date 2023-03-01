package kr.co.pulmuone.v1.goods.discount.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.mapper.goods.discount.GoodsDiscountMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.discount.dto.GoodsDiscountUploadRequestDto;
import kr.co.pulmuone.v1.goods.discount.dto.vo.DiscountInfoVo;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountUploadListVo;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDiscountApprVo;
import kr.co.pulmuone.v1.goods.itemprice.service.GoodsItemPriceBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
class GoodsDiscountServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private GoodsDiscountService goodsDiscountService;

    @InjectMocks
    private GoodsDiscountService mockGoodsDiscountService;

    @Mock
    GoodsItemPriceBiz mockItemPriceBiz;

    @Mock
    GoodsDiscountMapper goodsDiscountMapper;
	
	@BeforeEach
	void setUp() {
		preLogin();
	}

    @Test
    void 상품할인_리스트_조회_성공() {
        Long goodsId = 175L;
        String discountTypeCode = "GOODS_DISCOUNT_TP.IMMEDIATE";

        log.info("상품할인_리스트_조회_성공 goodsId :{}, discountTypeCode : {}", goodsId, discountTypeCode);

        List<GoodsDiscountVo> goodsDiscountList = goodsDiscountService.getGoodsDiscountList(goodsId, discountTypeCode);

        assertTrue(CollectionUtils.isNotEmpty(goodsDiscountList));
    }

    @Test
    void 상품할인_리스트_조회_실패() {
        Long goodsId = 176L;
        String discountTypeCode = "GOODS_DISCOUNT_TP.IMMEDIATE";

        log.info("상품할인_리스트_조회_실패 goodsId :{}, discountTypeCode : {}", goodsId, discountTypeCode);

        List<GoodsDiscountVo> goodsDiscountList = goodsDiscountService.getGoodsDiscountList(goodsId, discountTypeCode);

        assertFalse(CollectionUtils.isNotEmpty(goodsDiscountList));
    }

	@Test
	void 상품할인_삭제_성공() throws Exception {
		String goodsId = "90018129";
		Long goodsDiscountApprId = Long.valueOf("1526");
		Long goodsDiscountId = Long.valueOf("4838");
		String discountTypeCode = GoodsEnums.GoodsDiscountType.IMMEDIATE.getCode();
		String goodsType = GoodsEnums.GoodsType.PACKAGE.getCode();
	
		GoodsDiscountVo goodsDiscountVo = new GoodsDiscountVo();
		MessageCommEnum enums = BaseEnums.Default.SUCCESS;
		String today = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
	
		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();					// USER ID
	
		GoodsDiscountApprVo goodsDiscountApprInfo = goodsDiscountService.goodsDiscountApprInfo(goodsDiscountApprId);
		String apprStat = null;
	
		if( goodsDiscountApprInfo != null ) {
			int dateCompare = DateUtil.string2Date(today, "yyyy-MM-dd HH:mm:ss").compareTo(DateUtil.string2Date(goodsDiscountApprInfo.getDiscountStartDt(), "yyyy-MM-dd HH:mm:ss"));
		
			if(dateCompare >= 0){		//시작일이 이미 도래 했다면
				enums = GoodsEnums.GoodsDiscountApprProcStatus.OVER_PERIOD;
			}
			else if (goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())) {		//현재 상품할인 승인 내역이 승인요청 상태라면
				apprStat = ApprovalEnums.ApprovalStatus.CANCEL.getCode();
				enums = GoodsEnums.GoodsDiscountApprProcStatus.DELETE_APPR;
			}
			else if(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())) {		//현재 상품할인 승인 내역이 요청철회 상태라면
				enums = GoodsEnums.GoodsDiscountApprProcStatus.CANCEL_PREVIOUS;
			}
			else if(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())) {		//현재 상품할인 승인 내역이 반려 상태라면
				enums = GoodsEnums.GoodsDiscountApprProcStatus.DENIED_PREVIOUS;
			}
			else if(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
							|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())) {	//현재 상품할인 승인 내역이 승인완료(부), 승인완료 상태라면
				apprStat = ApprovalEnums.ApprovalStatus.DISPOSAL.getCode();
				enums = GoodsEnums.GoodsDiscountApprProcStatus.DELETE_APPR;
			}
		
			if(apprStat != null) {
				goodsDiscountService.insertGoodsDiscountApprStatusHistoryStat(goodsDiscountApprId, apprStat, userId);
				goodsDiscountService.updateGoodsDiscountApprStat(goodsDiscountApprId, apprStat, userId);
			
				if(GoodsEnums.GoodsDiscountType.PACKAGE.getCode().equals(discountTypeCode)) {	//묶음상품 기본할인 설정이라면
					if(goodsDiscountId != null && goodsDiscountId != 0) {
						//삭제할 할인내역의 할인종료일자와 이전 할인ID를 가져온다.
						goodsDiscountVo = goodsDiscountService.getGoodsPackageBaseDiscountUpdateList(goodsDiscountId, discountTypeCode);
					
						if (goodsDiscountVo != null) {
							DiscountInfoVo discountInfoVo = new DiscountInfoVo();
						
							discountInfoVo.setIlGoodsDiscountId(goodsDiscountVo.getGoodsDiscountId());
							discountInfoVo.setDiscountEndDate(goodsDiscountVo.getDiscountEndDateTime());
						
							//이전 할인ID의 할인 종료일자를 삭제할 할인내역의 할인종료일자로 Update
							int putPastGoodsDiscount = goodsDiscountService.putPastGoodsDiscountByBatch(discountInfoVo);
							int putPastGoodsDiscountAppr = goodsDiscountService.putPastGoodsDiscountApprByBatch(discountInfoVo);
						
							if (putPastGoodsDiscount > 0 && putPastGoodsDiscountAppr > 0) {			//업데이트 내역이 있다면
							
								goodsDiscountService.deleteGoodsDiscount(goodsDiscountId, userId);	//해당 행에 대한 삭제 처리 진행
							
								// 묶음 상품 가격정보 저장(프로시져 처리)
								GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();
							
								goodsRegistRequestDto.setIlGoodsId(goodsId);
								goodsRegistRequestDto.setInDebugFlag(true);
								goodsDiscountService.spGoodsPriceUpdateWhenPackageGoodsChanges(goodsRegistRequestDto);
							}
						}
					}
				}
				else {	// 우선할인, 즉시할인등 묶음상품 기본할인 설정외에 할인이라면
					if(goodsDiscountId != null && goodsDiscountId != 0) {
						goodsDiscountService.deleteGoodsDiscount(goodsDiscountId, userId);    //기간에 공백이 생겨도 무관하므로 해당 행에 대한 삭제 처리만 진행
						
						goodsDiscountVo.setGoodsDiscountId(null);
						// 묶음 상품 가격정보 저장(프로시져 처리)
						GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();
					
						goodsRegistRequestDto.setIlGoodsId(goodsId);
						goodsRegistRequestDto.setInDebugFlag(true);
						if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType)) {
							//프로시져 실행 시 RollBack이 되지 않으므로 주석 처리 함
							//goodsDiscountService.spGoodsPriceUpdateWhenPackageGoodsChanges(goodsRegistRequestDto);
						} else {
							//프로시져 실행 시 RollBack이 되지 않으므로 주석 처리 함
							//goodsDiscountService.spGoodsPriceUpdateWhenGoodsDiscountChanges(goodsRegistRequestDto);
						}
					}
				}
			}
		}
		else{
			enums = GoodsEnums.GoodsDiscountApprProcStatus.NONE_APPR;
		}
		
		log.info("enums : " + enums.getMessage());
		
		assertTrue(enums.equals(GoodsEnums.GoodsDiscountApprProcStatus.DELETE_APPR));
	}

	@Test
	void 상품할인_삭제_실패() throws Exception {
		String goodsId = "90018129";
		Long goodsDiscountApprId = Long.valueOf("1527");
		Long goodsDiscountId = Long.valueOf("4840");
		String discountTypeCode = GoodsEnums.GoodsDiscountType.PACKAGE.getCode();
		String goodsType = GoodsEnums.GoodsType.PACKAGE.getCode();
	
		GoodsDiscountVo goodsDiscountVo = new GoodsDiscountVo();
		MessageCommEnum enums = BaseEnums.Default.SUCCESS;
		String today = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
	
		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();					// USER ID
	
		GoodsDiscountApprVo goodsDiscountApprInfo = goodsDiscountService.goodsDiscountApprInfo(goodsDiscountApprId);
		String apprStat = null;
	
		if( goodsDiscountApprInfo != null ) {
			int dateCompare = DateUtil.string2Date(today, "yyyy-MM-dd HH:mm:ss").compareTo(DateUtil.string2Date(goodsDiscountApprInfo.getDiscountStartDt(), "yyyy-MM-dd HH:mm:ss"));
		
			if(dateCompare >= 0){		//시작일이 이미 도래 했다면
				enums = GoodsEnums.GoodsDiscountApprProcStatus.OVER_PERIOD;
			}
			else if (goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())) {		//현재 상품할인 승인 내역이 승인요청 상태라면
				apprStat = ApprovalEnums.ApprovalStatus.CANCEL.getCode();
				enums = GoodsEnums.GoodsDiscountApprProcStatus.DELETE_APPR;
			}
			else if(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())) {		//현재 상품할인 승인 내역이 요청철회 상태라면
				enums = GoodsEnums.GoodsDiscountApprProcStatus.CANCEL_PREVIOUS;
			}
			else if(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())) {		//현재 상품할인 승인 내역이 반려 상태라면
				enums = GoodsEnums.GoodsDiscountApprProcStatus.DENIED_PREVIOUS;
			}
			else if(goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
							|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())) {	//현재 상품할인 승인 내역이 승인완료(부), 승인완료 상태라면
				apprStat = ApprovalEnums.ApprovalStatus.DISPOSAL.getCode();
				enums = GoodsEnums.GoodsDiscountApprProcStatus.DELETE_APPR;
			}
		
			if(apprStat != null) {
				goodsDiscountService.insertGoodsDiscountApprStatusHistoryStat(goodsDiscountApprId, apprStat, userId);
				goodsDiscountService.updateGoodsDiscountApprStat(goodsDiscountApprId, apprStat, userId);
			
				if(GoodsEnums.GoodsDiscountType.PACKAGE.getCode().equals(discountTypeCode)) {	//묶음상품 기본할인 설정이라면
					if(goodsDiscountId != null && goodsDiscountId != 0) {
						//삭제할 할인내역의 할인종료일자와 이전 할인ID를 가져온다.
						goodsDiscountVo = goodsDiscountService.getGoodsPackageBaseDiscountUpdateList(goodsDiscountId, discountTypeCode);
					
						if (goodsDiscountVo != null) {
							DiscountInfoVo discountInfoVo = new DiscountInfoVo();
						
							discountInfoVo.setIlGoodsDiscountId(goodsDiscountVo.getGoodsDiscountId());
							discountInfoVo.setDiscountEndDate(goodsDiscountVo.getDiscountEndDateTime());
						
							//이전 할인ID의 할인 종료일자를 삭제할 할인내역의 할인종료일자로 Update
							int putPastGoodsDiscount = goodsDiscountService.putPastGoodsDiscountByBatch(discountInfoVo);
							int putPastGoodsDiscountAppr = goodsDiscountService.putPastGoodsDiscountApprByBatch(discountInfoVo);
						
							if (putPastGoodsDiscount > 0 && putPastGoodsDiscountAppr > 0) {			//업데이트 내역이 있다면
							
								goodsDiscountService.deleteGoodsDiscount(goodsDiscountId, userId);	//해당 행에 대한 삭제 처리 진행
							
								// 묶음 상품 가격정보 저장(프로시져 처리)
								GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();
							
								goodsRegistRequestDto.setIlGoodsId(goodsId);
								goodsRegistRequestDto.setInDebugFlag(true);
								goodsDiscountService.spGoodsPriceUpdateWhenPackageGoodsChanges(goodsRegistRequestDto);
							}
						}
					}
				}
				else {	// 우선할인, 즉시할인등 묶음상품 기본할인 설정외에 할인이라면
					if(goodsDiscountId != null && goodsDiscountId != 0) {
						goodsDiscountService.deleteGoodsDiscount(goodsDiscountId, userId);    //기간에 공백이 생겨도 무관하므로 해당 행에 대한 삭제 처리만 진행
					
						goodsDiscountVo.setGoodsDiscountId(null);
						// 묶음 상품 가격정보 저장(프로시져 처리)
						GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();
					
						goodsRegistRequestDto.setIlGoodsId(goodsId);
						goodsRegistRequestDto.setInDebugFlag(true);
						if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType)) {
							//프로시져 실행 시 RollBack이 되지 않으므로 주석 처리 함
							//goodsDiscountService.spGoodsPriceUpdateWhenPackageGoodsChanges(goodsRegistRequestDto);
						} else {
							//프로시져 실행 시 RollBack이 되지 않으므로 주석 처리 함
							//goodsDiscountService.spGoodsPriceUpdateWhenGoodsDiscountChanges(goodsRegistRequestDto);
						}
					}
				}
			}
		}
		else{
			enums = GoodsEnums.GoodsDiscountApprProcStatus.NONE_APPR;
		}
	
		log.info("enums : " + enums.getMessage());
	
		assertTrue(!enums.equals(GoodsEnums.GoodsDiscountApprProcStatus.DELETE_APPR));
    }

//    @Test
//    void putGoodsDiscountWithErpIfPriceBatch() throws Exception {
//        Map<String,Object> returnMap = new HashMap<>();
//
//        given(mockItemPriceBiz.getErpIfPriceSrchApi(any())).willReturn(returnMap);
//
//        int n = mockGoodsDiscountService.putGoodsDiscountWithErpIfPriceBatch();
//        assertTrue(n > 0);
//
//        Map<String, String> returnMap = new HashMap<>();
//
//        given(mockItemPriceBiz.getErpIfPriceSrchApi(any())).willReturn(returnMap);
//
//        mockGoodsDiscountService.putGoodsDiscountWithErpIfPriceBatch();
//    }

    @Test
    void 상품할인_일괄업로드테스트() {
        given(goodsDiscountMapper.addGoodsDiscountExcelLog(any())).willReturn(1);
        int n = mockGoodsDiscountService.addGoodsDiscountExcelLog(null);
        assertTrue(n > 0);
    }

    @Test
    void 상품할인일괄업로드조회_테스트() {

        GoodsDiscountUploadRequestDto paramDto = new GoodsDiscountUploadRequestDto();
        Page<GoodsDiscountUploadListVo> result = goodsDiscountService.getGoodsDiscountUploadList(paramDto);

        // then
        assertNotNull(result.getResult());
    }

    @Test
    void 임직원상품할인일괄업로드조회_테스트() {

        GoodsDiscountUploadRequestDto paramDto = new GoodsDiscountUploadRequestDto();
        Page<GoodsDiscountUploadListVo> result = goodsDiscountService.getGoodsDisEmpUploadList(paramDto);

        // then
        assertNotNull(result.getResult());
    }

    @Test
    void 상품할인일괄업로드상세조회_테스트() {

        GoodsDiscountUploadRequestDto paramDto = new GoodsDiscountUploadRequestDto();

        paramDto.setLogId("14");
        paramDto.setSearchType("single");

        Page<GoodsDiscountUploadListVo> result = goodsDiscountService.getGoodsDiscountUploadDtlList(paramDto);

        // then
        assertNotNull(result.getResult());
    }


    @Test
    void 임직원상품할인일괄업로드상세조회_테스트() {

        GoodsDiscountUploadRequestDto paramDto = new GoodsDiscountUploadRequestDto();

        paramDto.setLogId("16");
        paramDto.setSearchType("single");

        Page<GoodsDiscountUploadListVo> result = goodsDiscountService.getGoodsDiscountEmpUploadDtlList(paramDto);
        // then
        assertNotNull(result.getResult());
    }

    @Test
    void 묶음상품_기본할인_이전날짜_업데이트_대상_업데이트() {
        //given
        String discountTypeCode = GoodsEnums.GoodsDiscountType.PACKAGE.getCode();
        long goodsDiscountId = 5028;
        int putPastGoodsDiscount = 0;

        //when
        //삭제할 할인내역의 할인종료일자와 이전 할인ID를 가져온다.
        GoodsDiscountVo goodsDiscountVo = goodsDiscountService.getGoodsPackageBaseDiscountUpdateList(goodsDiscountId, discountTypeCode);

        if (goodsDiscountVo != null) {
            DiscountInfoVo discountInfoVo = new DiscountInfoVo();

            discountInfoVo.setIlGoodsDiscountId(goodsDiscountVo.getGoodsDiscountId());
            discountInfoVo.setDiscountEndDate(goodsDiscountVo.getDiscountEndDate());

            putPastGoodsDiscount = goodsDiscountService.putPastGoodsDiscountByBatch(discountInfoVo);    //이전 할인ID의 할인 종료일자를 삭제할 할인내역의 할인종료일자로 Update
        }

        // then
        assertTrue(putPastGoodsDiscount > 0);
    }

}
