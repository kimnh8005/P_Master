package kr.co.pulmuone.v1.goods.discount.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistApprRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDiscountApprVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsRegistBizImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.goods.discount.GoodsDiscountMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.discount.dto.GoodsDisExcelUploadRequestDto;
import kr.co.pulmuone.v1.goods.discount.dto.GoodsDiscountResponseDto;
import kr.co.pulmuone.v1.goods.discount.dto.GoodsDiscountUploadReponseDto;
import kr.co.pulmuone.v1.goods.discount.dto.GoodsDiscountUploadRequestDto;
import kr.co.pulmuone.v1.goods.discount.dto.vo.DiscountInfoVo;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountUploadListVo;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.DetailSelectGoodsVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemInfoVo;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemBiz;


/**
* <PRE>
* Forbiz Korea
* 상품할인 BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 8.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@Service
public class GoodsDiscountBizImpl  implements GoodsDiscountBiz {

    @Autowired
    GoodsDiscountService goodsDiscountService;

    @Autowired
    GoodsItemBiz goodsItemBiz;

	@Autowired
	private GoodsDiscountMapper goodsDiscountMapper;

	@Autowired
	GoodsRegistBizImpl goodsRegistBizImpl;

	/**
	 * @Desc 단품상세정보 및 상품할인 리스트 조회
	 * @param goodsId
	 * @param discountTypeCode
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getItemInfoAndGoodsDiscountList(Long goodsId, String discountTypeCode){
		GoodsDiscountResponseDto goodsDiscountResponseDto = new GoodsDiscountResponseDto();

		ItemInfoVo itemInfo = goodsItemBiz.getGoodsIdByItemInfo(goodsId);
		List<GoodsDiscountVo> goodsDiscountList = goodsDiscountService.getGoodsDiscountList(goodsId, discountTypeCode);

		goodsDiscountResponseDto.setItemInfo(itemInfo);
		goodsDiscountResponseDto.setGoodsDiscountList(goodsDiscountList);

		return ApiResult.success(goodsDiscountResponseDto);
	}

	/**
	 * @Desc 상품할인 삭제
	 * @param goodsDiscountId
	 * @return ApiResult
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> deleteGoodsDiscount(String goodsId, Long goodsDiscountApprId, Long goodsDiscountId, String discountTypeCode, String goodsType) throws Exception{

		GoodsDiscountVo goodsDiscountVo = new GoodsDiscountVo();
		MessageCommEnum enums = BaseEnums.Default.SUCCESS;
		String today = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");

		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();					// USER ID

		GoodsDiscountApprVo goodsDiscountApprInfo = goodsDiscountService.goodsDiscountApprInfo(goodsDiscountApprId);
		String apprStat = null;

		if( goodsDiscountApprInfo != null ) {
			int dateCompare = DateUtil.string2Date(today, "yyyy-MM-dd HH:mm:ss").compareTo(DateUtil.string2Date(goodsDiscountApprInfo.getDiscountStartDt(), "yyyy-MM-dd HH:mm:ss"));

			if(dateCompare >= 0
					&& goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())){		//시작일이 이미 도래 했고 '승인완료'상태라면
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

							if (putPastGoodsDiscount > 0 && putPastGoodsDiscountAppr > 0) {    //업데이트 내역이 있다면

								goodsDiscountService.deleteGoodsDiscount(goodsDiscountId, userId);    //해당 행에 대한 삭제 처리 진행
								//묶음상품 개별품목 내역을 삭제처리 한다.(할인내역 USE_YN ='N'으로 변경하면서 개볆품묵 내역 삭제 처리 안하기로 협의함, 210503 원종한 이사님과 협의)
								//goodsDiscountService.deleteIlGoodsPackageItemFixedDiscountPrice(goodsDiscountId);

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
						//묶음상품 개별품목 내역을 삭제처리 한다.(할인내역 USE_YN ='N'으로 변경하면서 개볆품묵 내역 삭제 처리 안하기로 협의함, 210503 원종한 이사님과 협의)
						//goodsDiscountService.deleteIlGoodsPackageItemFixedDiscountPrice(goodsDiscountId);

						goodsDiscountVo.setGoodsDiscountId(null);
						// 묶음 상품 가격정보 저장(프로시져 처리)
						GoodsRegistRequestDto goodsRegistRequestDto = new GoodsRegistRequestDto();

						goodsRegistRequestDto.setIlGoodsId(goodsId);
						goodsRegistRequestDto.setInDebugFlag(true);
						if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsType)) {
							goodsDiscountService.spGoodsPriceUpdateWhenPackageGoodsChanges(goodsRegistRequestDto);
						} else {
							//this.spGoodsPriceUpdateWhenGoodsDiscountChanges(goodsRegistRequestDto.getIlGoodsId());
							goodsRegistBizImpl.spGoodsPriceUpdateWhenGoodsRegist(goodsRegistRequestDto.getIlGoodsId());
						}
					}
				}
			}
		}
		else{
			enums = GoodsEnums.GoodsDiscountApprProcStatus.NONE_APPR;
		}

		return ApiResult.result(goodsDiscountVo, enums);
	}

    /**
     * 가격조회 ERP 연동 후 상품 할인 데이터 처리
     * @param
     * @return ApiResult<?>
     */
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
//    public ApiResult<?> putGoodsDiscountWithErpIfPriceBatch() throws Exception {
//
//        //배치 데이터 처리  & insert
//        int addDiscountCount = goodsDiscountService.putGoodsDiscountWithErpIfPriceBatch();
//
//        return ApiResult.success(addDiscountCount);
//    }

    /**
     * 상품 등록시 올가ERP 연동 할인가격정보 INSERT 처리
     * @param
     * @return ApiResult<?>
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addGoodsDiscountWithErpIfPriceBatch(String ilGoodsId, String ilItemCode) throws Exception {

        //배치 데이터 처리  & insert
        int addDiscountCount = goodsDiscountService.addGoodsDiscountWithErpIfPriceBatch(ilGoodsId, ilItemCode);

        return ApiResult.success(addDiscountCount);
    }

	@Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addExcelUpload(GoodsDisExcelUploadRequestDto paramDto) throws Exception {
		// TODO Auto-generated method stub

		GoodsDiscountVo resultVo = new GoodsDiscountVo();

		String result = "";//결과

		int failCnt = 0;
		int successCnt = 0;
		boolean logInsertFlag = false;

		// 업로드 내용이 없을 경우
		if(paramDto.getUploadList().size() < 1) {
			result = Integer.toString(0)+"|"+Integer.toString(0)+"|"+Integer.toString(0); //총건수|성공건수|실패건수
			return ApiResult.success(result);
		}

		String logId = "";
		for(GoodsDiscountVo goodsDisVo : paramDto.getUploadList()) {
			// LOG 신규 데이터 생성.
			goodsDisVo.setCreateId(paramDto.getUserVo().getUserId());

			if(!logInsertFlag) {
				goodsDisVo.setUploadTp("IL_DISC_UPLOAD_TP.GOODS_DISC");

				goodsDiscountService.addGoodsDiscountExcelLog(goodsDisVo);
				logInsertFlag = true;
				logId = goodsDisVo.getLogId();
				resultVo.setLogId(goodsDisVo.getLogId());
				resultVo.setCreateId(paramDto.getUserVo().getUserId());
			}


			// 초기 성공여부를 성공으로 셋팅.
			goodsDisVo.setSuccessYn("Y");


			// 값이 없는 경우 실패
			if(null == goodsDisVo.getGoodsId() || null == goodsDisVo.getDiscountStartDate() || null == goodsDisVo.getDiscountEndDate()
				|| null == goodsDisVo.getDiscountTypeCode() || null == goodsDisVo.getDiscountMethodTypeCode() || null == goodsDisVo.getDiscountVal()
				) {
				goodsDisVo.setSuccessYn("N");
				goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.VALUE_EMPTY.getMessage());
			}

			SimpleDateFormat  simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			// 할인 시작날짜 날짜 형식 체크
			try {
				simpleFormat.parse(goodsDisVo.getDiscountStartDate());
			}catch(ParseException e) {
				goodsDisVo.setSuccessYn("N");
				goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.FORMAT_BAD.getMessage());
			}

			// 할인 종료날짜 날짜 형식 체크
			try {
				simpleFormat.parse(goodsDisVo.getDiscountEndDate());
			}catch(ParseException e) {
				goodsDisVo.setSuccessYn("N");
				goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.FORMAT_BAD.getMessage());
			}

			if("Y".equals(goodsDisVo.getSuccessYn())) {
				Date toDate = DateUtil.string2Date(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
				Date startDate = DateUtil.string2Date(goodsDisVo.getDiscountStartDate(), "yyyy-MM-dd HH:mm:ss");
				Date endDate = DateUtil.string2Date(goodsDisVo.getDiscountEndDate(), "yyyy-MM-dd HH:mm:ss");

				int toDateCompare = startDate.compareTo(toDate);

				if(toDateCompare < 0) {
					goodsDisVo.setSuccessYn("N");
					goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.START_DT_BEFORE.getMessage());
				}

				int endDateCompare = endDate.compareTo(startDate);

				if(endDateCompare < 0) {
					goodsDisVo.setSuccessYn("N");
					goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.START_DT_OVER_END_DATE.getMessage());
				}
			}



			try {
				Integer.valueOf(goodsDisVo.getDiscountTypeCode());
			}catch(NumberFormatException e) {
				goodsDisVo.setSuccessYn("N");
				goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.INTEGER_BAD_FORMAT.getMessage());
			}

			try {
				Integer.valueOf(goodsDisVo.getDiscountMethodTypeCode());
			}catch(NumberFormatException e) {
				goodsDisVo.setSuccessYn("N");
				goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.INTEGER_BAD_FORMAT.getMessage());
			}

			try {
				Integer.valueOf(goodsDisVo.getDiscountVal());
			}catch(NumberFormatException e) {
				goodsDisVo.setSuccessYn("N");
				goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.INTEGER_BAD_FORMAT.getMessage());
			}

			// 상품 ID 유무 조회
			DetailSelectGoodsVo goodsInfo = goodsDiscountService.getGoodsInfo(goodsDisVo);
			if(goodsInfo == null) {
				// 상품 코드가 없을경우 실패처리
				goodsDisVo.setSuccessYn("N");
				goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.GOODS_ID_EMPTY.getMessage());
			}

			if("Y".equals(goodsDisVo.getSuccessYn())) {
				// 할인 구분값 1,2 체크
				if(!("1".equals(goodsDisVo.getDiscountTypeCode()) || "2".equals(goodsDisVo.getDiscountTypeCode()))) {
					goodsDisVo.setSuccessYn("N");
					goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.VALUE_BAD.getMessage());
				}

				// 할인 유형 1,2 체크
				if(!("1".equals(goodsDisVo.getDiscountMethodTypeCode()) || "2".equals(goodsDisVo.getDiscountMethodTypeCode()))) {
					goodsDisVo.setSuccessYn("N");
					goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.VALUE_BAD.getMessage());
				}


				// 할인율 형식이 안맞는 경우 - 할인유형 정률할인 경우 할인율이 100이 넘거나 1보다 작으면 실패
				if("1".equals(goodsDisVo.getDiscountMethodTypeCode())) {
					if(Integer.valueOf(goodsDisVo.getDiscountVal()) >= 100 || Integer.valueOf(goodsDisVo.getDiscountVal()) < 1) {
						goodsDisVo.setSuccessYn("N");
						goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.DISCOUNT_OVER_VAL.getMessage());
					}
				}

				// 할인율 형식이 안맞는 경우 - 할인유형 고정가할인 경우 할인율이 한자리인경우 실패
				if("2".equals(goodsDisVo.getDiscountMethodTypeCode())) {
					if(Integer.valueOf(goodsDisVo.getDiscountVal()) < 1) {
						goodsDisVo.setSuccessYn("N");
						goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.DISCOUNT_VAL_NOT_MINUS.getMessage());
					}

					if(goodsDisVo.getDiscountVal().length() == 1) {
						goodsDisVo.setSuccessYn("N");
						goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.DISCOUNT_OVER_VAL.getMessage());
					}

					if(Integer.valueOf(goodsDisVo.getDiscountVal()) > goodsInfo.getRecommendedPrice()) {
						goodsDisVo.setSuccessYn("N");
						goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.PRICE_OVER_VAL.getMessage());
					}
				}

			}

			if("Y".equals(goodsDisVo.getSuccessYn())) {

				// 상품 ID 정보가 있을경우 처리
				if(goodsInfo != null) {
					goodsDisVo.setStandardPrice(goodsInfo.getStandardPrice()); // 승인 요청시 원가 정보
					goodsDisVo.setRecommendedPrice(goodsInfo.getRecommendedPrice()); // 승인 요청시 정상가 정보

					// 묶음 상품일경우 실패처리
					if("GOODS_TYPE.PACKAGE".equals(goodsInfo.getGoodsTp())) {
						goodsDisVo.setSuccessYn("N");
						goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.GOODS_PACKAGE.getMessage());
					}else {
						// discountTypeCode 1= GOODS_DISCOUNT_TP.PRIORITY, 2 = GOODS_DISCOUNT_TP.IMMEDIATE
						if("1".equals(goodsDisVo.getDiscountTypeCode())) {
							goodsDisVo.setDiscountTypeCode("GOODS_DISCOUNT_TP.PRIORITY");
						}else if("2".equals(goodsDisVo.getDiscountTypeCode())) {
							goodsDisVo.setDiscountTypeCode("GOODS_DISCOUNT_TP.IMMEDIATE");
						}

						//엑셀에 있는 날짜 조건이 현재 DB에 존재하는지 체크
						GoodsDiscountVo goodsDisInfo = goodsDiscountService.getGoodsDiscountInfo(goodsDisVo);

						if(goodsDisInfo == null) {

							//상품 할인 승인 내역 확인, 승인 내역 존재시 요청 자격 확인
							GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistBizImpl.goodsDiscountApprInfo(goodsDisVo.getGoodsId().toString(), null, goodsDisVo.getDiscountTypeCode());
							if(goodsDiscountApprInfo != null &&
									   (goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
												|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
									   )) {        //현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
								goodsDisVo.setSuccessYn("N");
								goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.APPR_DUPLICATE.getMessage());
							}
							else {
								// discountMethodTypeCode 1= GOODS_DISCOUNT_METHOD_TP.FIXED_RATE, 2 = GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE
								if ("1".equals(goodsDisVo.getDiscountMethodTypeCode())) {
									goodsDisVo.setDiscountMethodTypeCode("GOODS_DISCOUNT_METHOD_TP.FIXED_RATE");
									goodsDisVo.setDiscountRatio(goodsDisVo.getDiscountVal());
									goodsDisVo.setDiscountSalePrice("0");
								}

								if ("2".equals(goodsDisVo.getDiscountMethodTypeCode())) {
									goodsDisVo.setDiscountMethodTypeCode("GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE");
									goodsDisVo.setDiscountRatio("0");
									goodsDisVo.setDiscountSalePrice(goodsDisVo.getDiscountVal());
								}

								goodsDisVo.setLogId(logId);
								goodsDiscountService.addGoodsDiscountExcelDtlLog(goodsDisVo);

								//할인 승인 내역 저장으로 변경(2021-05-12, 임상건)
								//goodsDiscountService.addGoodsDiscountExcelUpload(goodsDisVo);
								addGoodsDiscountApprExcelUpload(goodsDisVo, paramDto.getGoodsDiscountApproList());
							}
						}else {
							goodsDisVo.setSuccessYn("N");
							goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.GOODS_ID_DISCOUNT_OVERLAP.getMessage());
						}
					}

				}else {

				}
			}


			if("N".equals(goodsDisVo.getSuccessYn())) {
				goodsDisVo.setLogId(logId);
				goodsDisVo.setDiscountSalePrice(goodsDisVo.getDiscountVal());
				goodsDiscountService.addGoodsDiscountExcelDtlLog(goodsDisVo);
			}

			if("Y".equals(goodsDisVo.getSuccessYn())) {
				successCnt++;
			}else {
				failCnt++;
			}

		}

		if(logInsertFlag) {
			resultVo.setSuccessCnt(String.valueOf(successCnt));
			resultVo.setFailCnt(String.valueOf(failCnt));
			goodsDiscountService.putGoodsDiscountExcelLog(resultVo);
		}

		result = Integer.toString(paramDto.getUploadList().size())+"|"+Integer.toString(successCnt)+"|"+Integer.toString(failCnt); //총건수|성공건수|실패건수

		return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> getGoodsDiscountUploadList(GoodsDiscountUploadRequestDto paramDto) {
		// TODO Auto-generated method stub
		GoodsDiscountUploadReponseDto goodsDiscountUploadReponseDto = new GoodsDiscountUploadReponseDto();

		Page<GoodsDiscountUploadListVo> goodsList = goodsDiscountService.getGoodsDiscountUploadList(paramDto);

		goodsDiscountUploadReponseDto.setTotal(goodsList.getTotal());
		goodsDiscountUploadReponseDto.setRows(goodsList.getResult());

        return ApiResult.success(goodsDiscountUploadReponseDto);
	}

	@Override
	public ExcelDownloadDto getGoodsDiscountUploadFailList(GoodsDiscountUploadRequestDto paramDto) {
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		String excelFileName = "상품할인 일괄 업로드 실패 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		/*
		 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
		 * 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
			140, 240, 240, 290, 290, 450, 400
		};

		/*
		 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
		 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
		 * 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = { //
			"center", "center", "center", "center", "center", "center", "center",
		};

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야
		 * 함
		 */
		String[] propertyListOfFirstWorksheet = { //
			"goodsId", "discountStartDt", "discountEndDt", "discountTp", "discountMethodTp", "discountRatio", "msg"
		};

		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
			"상품코드", "할인시작일", "할인종료일", "할인구분", "할인유형", "할인율", "실패사유"
		};

		// 두 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] secondHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
			"예시) 183"
			, "예시) 2020-12-01 06:00:00"
			, "예시) 2020-12-30 00:00:00"
			, "(우선할인=1, 즉시할인=2)\r\n"
					+ "예시)1"
			, "(정률할인=1, 고정가할인=2)\r\n"
					+ "예시)1"
			, "(정률할인 또는 고정가판매가 입력 숫자만가능)\r\n"
					+ "할인유형이 1 인경우 100이상 실패\r\n"
					+ "할인유형이 2 인경우 한자리 경우 실패\r\n"
					+ "문자 입력 실패\r\n"
					+ "예시)20"
			, ""
		};

		// 워크시트 DTO 생성 후 정보 세팅
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();
		// 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼
		firstWorkSheetDto.setHeaderList(1, secondHeaderListOfFirstWorksheet); // 두 번째 헤더 컬럼

		/*
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음 excelData 를 세팅하지 않으면 샘플
		 * 엑셀로 다운로드됨
		 */
		List<GoodsDiscountUploadListVo> goodsPriceList = goodsDiscountService.getGoodsDiscountUploadFailList(paramDto);
		firstWorkSheetDto.setExcelDataList(goodsPriceList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	@Override
	public ApiResult<?> getGoodsDiscountUploadDtlList(GoodsDiscountUploadRequestDto paramDto) {
		// TODO Auto-generated method stub
		GoodsDiscountUploadReponseDto goodsDiscountUploadReponseDto = new GoodsDiscountUploadReponseDto();

		ArrayList<String> goodsCdArray = null;

       	if("single".equals(paramDto.getSearchType()) && !StringUtil.isEmpty(paramDto.getGoodsCodes())) {

       		//화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String ilItemCodeListStr = paramDto.getGoodsCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
            goodsCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
            paramDto.setGoodsCodeArray(goodsCdArray);
       	}

		Page<GoodsDiscountUploadListVo> goodsList = goodsDiscountService.getGoodsDiscountUploadDtlList(paramDto);

		goodsDiscountUploadReponseDto.setTotal(goodsList.getTotal());
		goodsDiscountUploadReponseDto.setRows(goodsList.getResult());

        return ApiResult.success(goodsDiscountUploadReponseDto);
	}

	@Override
	public ExcelDownloadDto createGoodsDiscountUploadDtlListExcel(GoodsDiscountUploadRequestDto paramDto) {
		// TODO Auto-generated method stub
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		String excelFileName = "상품할인 일괄 업로드 상세 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		/*
		 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
		 * 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
				80, 180, 180, 140, 240,
				120, 160, 160, 160, 240,
				240, 120, 120, 120, 120
				};

		/*
		 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
		 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
		 * 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = { //
				"center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center"
				};
		/*
		 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야
		 * 함
		 */
		String[] propertyListOfFirstWorksheet = { //
				"rownum", "itemCd", "itemBarcode", "goodsId", "goodsNm",
				"successYn", "msg", "discountTpCodeNm", "discountMethodTpCodeNm", "discountStartDt",
				"discountEndDt", "standardPriceStr", "recommendedPriceStr", "discountRatioStr", "discountPriceStr"
				};
		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"No", "마스터품목코드", "품목바코드", "상품코드", "상품명",
				"성공여부", "실패사유", "할인구분", "할인유형", "할인 시작일",
				"할인 종료일", "원가", "정상가", "할인율", "판매가"};

		// 워크시트 DTO 생성 후 정보 세팅
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();
		// 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

		/*
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음 excelData 를 세팅하지 않으면 샘플
		 * 엑셀로 다운로드됨
		 */
		List<GoodsDiscountUploadListVo> goodsPriceList = goodsDiscountService.getGoodsDiscountUploadDtlListExcel(paramDto);
		firstWorkSheetDto.setExcelDataList(goodsPriceList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	@Override
	public ApiResult<?> addEmployeeExcelUpload(GoodsDisExcelUploadRequestDto paramDto) throws Exception {
		// TODO Auto-generated method stub
		GoodsDiscountVo resultVo = new GoodsDiscountVo();

		String result = "";// 결과

		int failCnt = 0;
		int successCnt = 0;
		boolean logInsertFlag = false;

		// 업로드 내용이 없을 경우
		if (paramDto.getUploadList().size() < 1) {
			result = Integer.toString(0) + "|" + Integer.toString(0) + "|" + Integer.toString(0); // 총건수|성공건수|실패건수
			return ApiResult.success(result);
		}

		String logId = "";

		for (GoodsDiscountVo goodsDisVo : paramDto.getUploadList()) {
			// LOG 신규 데이터 생성.
			goodsDisVo.setCreateId(paramDto.getUserVo().getUserId());

			if (!logInsertFlag) {
				goodsDisVo.setUploadTp("IL_DISC_UPLOAD_TP.EMPLOYEE_DISC");
				goodsDiscountService.addGoodsDiscountExcelLog(goodsDisVo);
				logInsertFlag = true;
				logId = goodsDisVo.getLogId();
				goodsDisVo.setLogId(goodsDisVo.getLogId());
				resultVo.setLogId(goodsDisVo.getLogId());
				resultVo.setCreateId(paramDto.getUserVo().getUserId());
			}

			// 초기 성공여부를 성공으로 셋팅.
			goodsDisVo.setSuccessYn("Y");

			// 값이 없는 경우 실패
			if (null == goodsDisVo.getGoodsId() || null == goodsDisVo.getDiscountStartDate()
					|| null == goodsDisVo.getDiscountEndDate() || null == goodsDisVo.getDiscountVal()) {
				goodsDisVo.setSuccessYn("N");
				goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.VALUE_EMPTY.getMessage());
			}

			SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			// 할인 시작날짜 날짜 형식 체크
			try {
				simpleFormat.parse(goodsDisVo.getDiscountStartDate());
			} catch (ParseException e) {
				goodsDisVo.setSuccessYn("N");
				goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.FORMAT_BAD.getMessage());
			}

			// 할인 종료날짜 날짜 형식 체크
			try {
				simpleFormat.parse(goodsDisVo.getDiscountEndDate());
			} catch (ParseException e) {
				goodsDisVo.setSuccessYn("N");
				goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.FORMAT_BAD.getMessage());
			}

			if("Y".equals(goodsDisVo.getSuccessYn())) {
				Date toDate = DateUtil.string2Date(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
				Date startDate = DateUtil.string2Date(goodsDisVo.getDiscountStartDate(), "yyyy-MM-dd HH:mm:ss");
				Date endDate = DateUtil.string2Date(goodsDisVo.getDiscountEndDate(), "yyyy-MM-dd HH:mm:ss");

				int toDateCompare = startDate.compareTo(toDate);

				if(toDateCompare < 0) {
					goodsDisVo.setSuccessYn("N");
					goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.START_DT_BEFORE.getMessage());
				}

				int endDateCompare = endDate.compareTo(startDate);

				if(endDateCompare < 0) {
					goodsDisVo.setSuccessYn("N");
					goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.START_DT_OVER_END_DATE.getMessage());
				}

			}

			try {
				Integer.valueOf(goodsDisVo.getDiscountVal());
			}catch(NumberFormatException e) {
				goodsDisVo.setSuccessYn("N");
				goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.INTEGER_BAD_FORMAT.getMessage());
			}

			if ("Y".equals(goodsDisVo.getSuccessYn())) {
				// 할인율 형식이 안맞는 경우 - 할인율이 100이 넘으면 실패
				if (Integer.valueOf(goodsDisVo.getDiscountVal()) >= 100 || Integer.valueOf(goodsDisVo.getDiscountVal()) < 1) {
					goodsDisVo.setSuccessYn("N");
					goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.DISCOUNT_OVER_VAL.getMessage());
				}
			}


			if ("Y".equals(goodsDisVo.getSuccessYn())) {
				// 상품 ID 유무 조회
				DetailSelectGoodsVo goodsInfo = goodsDiscountService.getGoodsInfo(goodsDisVo);

				// 상품 ID 정보가 있을경우 처리
				if (goodsInfo != null) {
					goodsDisVo.setStandardPrice(goodsInfo.getStandardPrice()); // 승인 요청시 원가 정보
					goodsDisVo.setRecommendedPrice(goodsInfo.getRecommendedPrice()); // 승인 요청시 정상가 정보

					// 묶음 상품일경우 실패처리
					if("GOODS_TYPE.PACKAGE".equals(goodsInfo.getGoodsTp())) {
						goodsDisVo.setSuccessYn("N");
						goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.GOODS_PACKAGE.getMessage());
					}else {
						goodsDisVo.setDiscountTypeCode("GOODS_DISCOUNT_TP.EMPLOYEE");

						GoodsDiscountVo goodsDisInfo = goodsDiscountService.getGoodsDiscountInfo(goodsDisVo);

						if (goodsDisInfo == null) {

							//상품 할인 승인 내역 확인, 승인 내역 존재시 요청 자격 확인
							GoodsDiscountApprVo goodsDiscountApprInfo = goodsRegistBizImpl.goodsDiscountApprInfo(goodsDisVo.getGoodsId().toString(), null, goodsDisVo.getDiscountTypeCode());
							if(goodsDiscountApprInfo != null &&
									   (goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
												|| goodsDiscountApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode())
									   )) {        //현재 상품할인 승인 내역이 승인요청/승인완료(부) 상태라면
								goodsDisVo.setSuccessYn("N");
								goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.APPR_DUPLICATE.getMessage());
							}
							else {
								goodsDisVo.setDiscountMethodTypeCode("GOODS_DISCOUNT_METHOD_TP.FIXED_RATE");
								goodsDisVo.setDiscountRatio(goodsDisVo.getDiscountVal());
								goodsDisVo.setDiscountSalePrice("0");
								goodsDisVo.setLogId(logId);

								goodsDiscountService.addGoodsDiscountExcelDtlLog(goodsDisVo);
								//할인 승인 내역 저장으로 변경(2021-05-12, 임상건)
								//goodsDiscountService.addGoodsDiscountExcelUpload(goodsDisVo);
								addGoodsDiscountApprExcelUpload(goodsDisVo, paramDto.getGoodsDiscountApproList());
							}
						} else {
							goodsDisVo.setSuccessYn("N");
							goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.GOODS_ID_DISCOUNT_OVERLAP.getMessage());
						}
					}

				} else {
					// 상품 코드가 없을경우 실패처리
					goodsDisVo.setSuccessYn("N");
					goodsDisVo.setMsg(GoodsEnums.GoodsDiscountExcelUploadErrMsg.GOODS_ID_EMPTY.getMessage());
				}
			}

			if ("N".equals(goodsDisVo.getSuccessYn())) {
				goodsDisVo.setLogId(logId);
				goodsDisVo.setDiscountRatio(goodsDisVo.getDiscountVal());
				goodsDiscountService.addGoodsDiscountExcelDtlLog(goodsDisVo);
			}

			if ("Y".equals(goodsDisVo.getSuccessYn())) {
				successCnt++;
			} else {
				failCnt++;
			}

		}

		if (logInsertFlag) {
			resultVo.setSuccessCnt(String.valueOf(successCnt));
			resultVo.setFailCnt(String.valueOf(failCnt));
			goodsDiscountService.putGoodsDiscountExcelLog(resultVo);
		}

		result = Integer.toString(paramDto.getUploadList().size()) + "|" + Integer.toString(successCnt) + "|"
				+ Integer.toString(failCnt); // 총건수|성공건수|실패건수

		return ApiResult.success(result);

	}

	@Override
	public ExcelDownloadDto createGoodsDiscountEmpUploadFailList(GoodsDiscountUploadRequestDto paramDto) {
		// TODO Auto-generated method stub
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		String excelFileName = "임직원 할인 일괄 업로드 실패 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		/*
		 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
		 * 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
			140, 240, 240, 240, 400
		};

		/*
		 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
		 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
		 * 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = { //
			"center", "center", "center", "center", "center"
		};
		/*
		 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야
		 * 함
		 */

		String[] propertyListOfFirstWorksheet = { //
			"goodsId", "discountStartDt", "discountEndDt", "discountRatio", "msg"
		};
		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
			"상품코드", "할인시작일", "할인종료일", "할인율", "실패사유"
		};

		// 두 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] secondHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
			"예시) 183"
			, "예시) 2020-12-01 06:00:00"
			, "예시) 2020-12-30 00:00:00"
			, "문자 입력 실패\r\n"
					+ "예시)20"
			, ""
		};

		// 워크시트 DTO 생성 후 정보 세팅
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();

		// 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼
		firstWorkSheetDto.setHeaderList(1, secondHeaderListOfFirstWorksheet); // 두 번째 헤더 컬럼

		/*
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음 excelData 를 세팅하지 않으면 샘플
		 * 엑셀로 다운로드됨
		 */
		List<GoodsDiscountUploadListVo> goodsPriceList = goodsDiscountService.getGoodsDiscountEmpUploadFailList(paramDto);
		firstWorkSheetDto.setExcelDataList(goodsPriceList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	@Override
	public ApiResult<?> getGoodsDiscountEmpUploadDtlList(GoodsDiscountUploadRequestDto paramDto) {
		// TODO Auto-generated method stub
		GoodsDiscountUploadReponseDto goodsDiscountUploadReponseDto = new GoodsDiscountUploadReponseDto();

		ArrayList<String> goodsCdArray = null;

       	if("single".equals(paramDto.getSearchType()) && !StringUtil.isEmpty(paramDto.getGoodsCodes())) {

       		//화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String ilItemCodeListStr = paramDto.getGoodsCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
            goodsCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
            paramDto.setGoodsCodeArray(goodsCdArray);
       	}

		Page<GoodsDiscountUploadListVo> goodsList = goodsDiscountService.getGoodsDiscountEmpUploadDtlList(paramDto);

		goodsDiscountUploadReponseDto.setTotal(goodsList.getTotal());
		goodsDiscountUploadReponseDto.setRows(goodsList.getResult());

        return ApiResult.success(goodsDiscountUploadReponseDto);

	}

	@Override
	public ExcelDownloadDto createGoodsDiscountEmpUploadDtlListExcel(GoodsDiscountUploadRequestDto paramDto) {
		// TODO Auto-generated method stub
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		String excelFileName = "임직원 할인 일괄 업로드 상세 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		/*
		 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
		 * 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
				80, 180, 180, 140, 240,
				120, 160, 240, 160, 240,
				120, 120, 120, 120
		};

		/*
		 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
		 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
		 * 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = { //
				"center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center",
				"center", "center", "center", "center"
		};
		/*
		 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야
		 * 함
		 */

		String[] propertyListOfFirstWorksheet = { //
				"rownum", "itemCd", "itemBarcode", "goodsId", "goodsNm",
				"successYn", "msg", "compNm", "discountStartDt", "discountEndDt",
				"standardPriceStr", "recommendedPriceStr", "discountRatioStr", "discountPriceStr"
		};
		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"No", "마스터품목코드", "품목바코드", "상품코드", "상품명",
				"성공여부", "실패사유", "공급업체", "할인 시작일", "할인 종료일",
				"원가", "정상가", "할인율", "판매가"
		};

		// 워크시트 DTO 생성 후 정보 세팅
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();
		// 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

		/*
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음 excelData 를 세팅하지 않으면 샘플
		 * 엑셀로 다운로드됨
		 */
		List<GoodsDiscountUploadListVo> goodsPriceList = goodsDiscountService.getGoodsDiscountEmpUploadDtlListExcel(paramDto);
		firstWorkSheetDto.setExcelDataList(goodsPriceList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	/**
     * @Desc 할인 종료일 업데이트
     * @param goodsDiscountId, discountTypeCode
     * @return ApiResult
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> discountEndDateUpdate(Long goodsDiscountId, String discountEndDate) throws Exception{

    	DiscountInfoVo discountInfoVo = new DiscountInfoVo();

    	if(goodsDiscountId != null) {
			discountInfoVo.setIlGoodsDiscountId(goodsDiscountId);
			discountInfoVo.setDiscountEndDate(discountEndDate);

			goodsDiscountService.putPastGoodsDiscountByBatch(discountInfoVo);		//이전 할인ID의 할인 종료일자를 삭제할 할인내역의 할인종료일자로 Update
			goodsDiscountService.putPastGoodsDiscountApprByBatch(discountInfoVo);	//이전 할인ID의 할인승인 종료일자를 Update
			this.spGoodsPriceUpdateWhenGoodsDiscountChanges(discountInfoVo.getIlGoodsId().toString()); // 할인 변경 후, 가격 계산 프로시저 호출
    	}

    	return ApiResult.success(discountInfoVo);
    }

	@Override
	public ApiResult<?> getGoodsDisEmpUploadList(GoodsDiscountUploadRequestDto paramDto) {
		// TODO Auto-generated method stub
		GoodsDiscountUploadReponseDto goodsDiscountUploadReponseDto = new GoodsDiscountUploadReponseDto();

		Page<GoodsDiscountUploadListVo> goodsList = goodsDiscountService.getGoodsDisEmpUploadList(paramDto);

		goodsDiscountUploadReponseDto.setTotal(goodsList.getTotal());
		goodsDiscountUploadReponseDto.setRows(goodsList.getResult());

        return ApiResult.success(goodsDiscountUploadReponseDto);
	}

	private void addGoodsDiscountApprExcelUpload(GoodsDiscountVo goodsDisVo, List<GoodsRegistApprRequestDto> goodsRegistApprRequestList) throws Exception{
		String today = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");

		GoodsDiscountApprVo goodsDiscountApprVo = new GoodsDiscountApprVo();
		goodsDiscountApprVo.setIlGoodsId(goodsDisVo.getGoodsId().toString());
		goodsDiscountApprVo.setDiscountTp(goodsDisVo.getDiscountTypeCode());
		goodsDiscountApprVo.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
		goodsDiscountApprVo.setDiscountStartDt(goodsDisVo.getDiscountStartDate());
		goodsDiscountApprVo.setDiscountEndDt(goodsDisVo.getDiscountEndDate());
		goodsDiscountApprVo.setDiscountMethodTp(goodsDisVo.getDiscountMethodTypeCode());
		goodsDiscountApprVo.setDiscountSalePrice(Integer.parseInt(goodsDisVo.getDiscountSalePrice()));
		goodsDiscountApprVo.setDiscountRatio(Integer.parseInt(goodsDisVo.getDiscountRatio()));
		goodsDiscountApprVo.setStandardPrice(goodsDisVo.getStandardPrice());
		goodsDiscountApprVo.setRecommendedPrice(goodsDisVo.getRecommendedPrice());

		if(goodsRegistApprRequestList != null && goodsRegistApprRequestList.size() > 0){
			for(GoodsRegistApprRequestDto goodsRegistApprRequestDto : goodsRegistApprRequestList) {	//승인 관리자
				if(goodsRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_1ST.getCode())) {
					goodsDiscountApprVo.setApprSubUserId(goodsRegistApprRequestDto.getApprUserId());
				}

				if(goodsRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_2ND.getCode())) {
					goodsDiscountApprVo.setApprReqUserId(goodsDisVo.getCreateId());
					goodsDiscountApprVo.setApprUserId(goodsRegistApprRequestDto.getApprUserId());
				}
			}
		}
		goodsDiscountApprVo.setApprReqDt(today);
		goodsDiscountApprVo.setCreateId(goodsDisVo.getCreateId());
		goodsDiscountApprVo.setCreateDt(today);

		goodsRegistBizImpl.addGoodsDiscountAppr(goodsDiscountApprVo);

		goodsDiscountApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
		goodsDiscountApprVo.setStatusCmnt("묶음상품 기본 판매가 자동승인");

		goodsRegistBizImpl.addGoodsDiscountApprStatusHistory(goodsDiscountApprVo);
	}

    /**
     * @Desc 상품할인 변경에 의한 가격 계산 프로시저 호출
     * @param goodsDiscountId
     * @return ApiResult
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public void spGoodsPriceUpdateWhenGoodsDiscountChanges(String ilGoodsId) throws Exception {
		goodsDiscountService.spGoodsPriceUpdateWhenGoodsDiscountChanges(ilGoodsId);
	}

	@Override
	public int putGoodsDiscount(Long ilGoodsId, String discountTypeCode, String discountStartDateTime) {
		return goodsDiscountService.putGoodsDiscount(ilGoodsId, discountTypeCode, discountStartDateTime);
	}

}
