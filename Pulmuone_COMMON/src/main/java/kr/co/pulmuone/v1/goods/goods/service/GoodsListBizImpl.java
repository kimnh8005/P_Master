package kr.co.pulmuone.v1.goods.goods.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsPriceVo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalGoodsRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.approval.auth.service.ApprovalAuthBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalAuthType;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalStatus;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalValidation;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsVo;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemModifyBiz;
import kr.co.pulmuone.v1.policy.excel.service.PolicyExcelTmpltBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * Forbiz Korea
 * 상품리스트 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 10. 05.                손진구          최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class GoodsListBizImpl  implements GoodsListBiz {

	@Autowired
	GoodsListService goodsListService;

	@Autowired
	private PolicyExcelTmpltBiz policyExcelTmpltBiz;

	@Autowired
	private ApprovalAuthBiz approvalAuthBiz;

    @Autowired
    private GoodsRegistBiz goodsRegistBiz;

    /**
     * @Desc 상품 목록 조회
     * @param goodsListRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getGoodsList(GoodsListRequestDto goodsListRequestDto){
        GoodsListResponseDto goodsListResponseDto = new GoodsListResponseDto();
        ArrayList<String> ilItemCdArray = new ArrayList<String>();
        String codeStrFlag = "N";
        String ilItemCodeListStr = "";

//        if (!StringUtil.isEmpty(goodsListRequestDto.getFindKeyword())) {
//
//            // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
//            String ilItemCodeListStr = goodsListRequestDto.getFindKeyword().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
//
//            String regExp = "^[0-9]+$";
////            ilItemCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
//            String[] ilItemCodeListArray = ilItemCodeListStr.split(",+");
//            for(int i = 0; i < ilItemCodeListArray.length; i++) {
//            	String ilItemCodeSearchVal = ilItemCodeListArray[i];
//            	if(ilItemCodeSearchVal.isEmpty()) {
//            		continue;
//            	}
//            	if(ilItemCodeSearchVal.matches(regExp)) {
//            		ilItemCdArray.add(String.valueOf(Long.valueOf(ilItemCodeSearchVal)));
//            	}else {
//            		codeStrFlag = "Y";
//            		ilItemCdArray.add(ilItemCodeSearchVal);
//            	}
//            }
//        }

		// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
		if (!StringUtil.isEmpty(goodsListRequestDto.getFindKeyword())) {
			ilItemCodeListStr = goodsListRequestDto.getFindKeyword().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
		}
		else if (!StringUtil.isEmpty(goodsListRequestDto.getFindKeywordOnMulti())) {
			ilItemCodeListStr = goodsListRequestDto.getFindKeywordOnMulti().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
		}

        if (!StringUtil.isEmpty(ilItemCodeListStr)) {
			String[] ilItemCodeListArray = ilItemCodeListStr.split(",+");
			for(int i = 0; i < ilItemCodeListArray.length; i++) {
				String ilItemCodeSearchVal = ilItemCodeListArray[i];
				if(ilItemCodeSearchVal.isEmpty()) {
					continue;
				}
				ilItemCdArray.add(ilItemCodeSearchVal);
			}
		}

        if (!StringUtil.isEmpty(goodsListRequestDto.getFindKeyword())) {
            goodsListRequestDto.setFindKeywordList(ilItemCdArray); // 검색어
            goodsListRequestDto.setFindKeywordOnMultiList(new ArrayList<String>()); // 검색어
        }
        else if (!StringUtil.isEmpty(goodsListRequestDto.getFindKeywordOnMulti())) {
            goodsListRequestDto.setFindKeywordList(new ArrayList<String>()); // 검색어
            goodsListRequestDto.setFindKeywordOnMultiList(ilItemCdArray); // 검색어
        }
        goodsListRequestDto.setFindKeywordStrFlag(codeStrFlag);
        goodsListRequestDto.setGoodsTypeList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS)); // 상품유형
        goodsListRequestDto.setSaleTypeList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getSaleType(), Constants.ARRAY_SEPARATORS)); // 판매유형
        goodsListRequestDto.setGoodsDeliveryTypeList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getGoodsDeliveryType(), Constants.ARRAY_SEPARATORS)); // 배송유형
        goodsListRequestDto.setSaleStatusList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getSaleStatus(), Constants.ARRAY_SEPARATORS)); // 판매상태
        goodsListRequestDto.setStorageMethodTypeList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getStorageMethodType(), Constants.ARRAY_SEPARATORS)); // 보관방법
        goodsListRequestDto.setGoodsOutMallSaleStatList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getGoodsOutMallSaleStat(), Constants.ARRAY_SEPARATORS)); // 외부몰 판매상태
        goodsListRequestDto.setStockStatusList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getStockStatus(), Constants.ARRAY_SEPARATORS)); // 재고운영형태

        Page<GoodsVo> goodsList = goodsListService.getGoodsList(goodsListRequestDto);

        goodsListResponseDto.setTotal(goodsList.getTotal());
        goodsListResponseDto.setRows(goodsList.getResult());

        return ApiResult.success(goodsListResponseDto);
    }

    /**
     * @Desc 상품목록 판매상태 변경
     * @param goodsListRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putGoodsListSaleStatusChange(GoodsListRequestDto goodsListRequestDto) throws Exception{
        long failedCount = 0;
        List<GoodsVo> failedList = new ArrayList<GoodsVo>();

        for(Long goodsId : goodsListRequestDto.getGoodsIdList()) {
            GoodsVo goodsVo = new GoodsVo();
            goodsVo.setSaleStatusCode(goodsListRequestDto.getGridSaleStatus());
            goodsVo.setGoodsId(goodsId);
            goodsVo.setCreateId(goodsListRequestDto.getUserVo().getUserId());

            GoodsRegistRequestDto beforeData = goodsListService.getGoodsSaleStatusInfo(goodsVo);

            // 해당 상품이 존재하지 않으면 skip
            if (beforeData == null) {
            	failedCount++;
            	failedList.add(goodsVo);
            	continue;
            }

            // 저장(승인중) 및 영구판매중지 상태인 경우 처리하지 않는다.
            if ("SALE_STATUS.SAVE".equals(beforeData.getSaleStatus()) || "SALE_STATUS.STOP_PERMANENT_SALE".equals(beforeData.getSaleStatus())) {
            	failedCount++;
            	failedList.add(goodsVo);
            	continue;
            }

            // 품절(시스템) 상태의 경우 판매대기/판매중 상태로 변경할 수 없다.
            if (
            	("SALE_STATUS.WAIT".equals(goodsListRequestDto.getGridSaleStatus()) || "SALE_STATUS.ON_SALE".equals(goodsListRequestDto.getGridSaleStatus()))
            	&& "SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM".equals(beforeData.getSaleStatus())
            ) {
            	failedCount++;
            	failedList.add(goodsVo);
            	continue;
            }

            // 이전 상태와 변경요청 상태가 동일할 경우 처리하지 않는다.
            if (beforeData.getSaleStatus().equals(goodsListRequestDto.getGridSaleStatus())) {
            	failedCount++;
            	failedList.add(goodsVo);
            	continue;
            }

            // 거래처 품목 승인중인 경우 처리하지 않는다.
            if ("APPR_STAT.NONE".equals(beforeData.getItemClientApprStat())
            		|| "APPR_STAT.REQUEST".equals(beforeData.getItemClientApprStat())
            		|| "APPR_STAT.SUB_APPROVED".equals(beforeData.getItemClientApprStat())
            		) {
            	failedCount++;
            	failedList.add(goodsVo);
            	continue;
            }

            // 거래처 상품 승인중인 경우 처리하지 않는다.
            if ("APPR_STAT.NONE".equals(beforeData.getGoodsClientApprStat())
            		|| "APPR_STAT.REQUEST".equals(beforeData.getGoodsClientApprStat())
            		|| "APPR_STAT.SUB_APPROVED".equals(beforeData.getGoodsClientApprStat())
            		) {
            	failedCount++;
            	failedList.add(goodsVo);
            	continue;
            }

            goodsListService.putGoodsSaleStatusChange(goodsVo);

            // 변경 로그 저장
            GoodsRegistRequestDto afetrData = (GoodsRegistRequestDto)beforeData.clone();
            afetrData.setSaleStatus(goodsListRequestDto.getGridSaleStatus());
            String createDate = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
            Map<String, String> beforeGoodsData = BeanUtils.describe(beforeData);
            Map<String, String> afterGoodsData = BeanUtils.describe(afetrData);
            goodsRegistBiz.addGoodsMasterChangeLog("UPDATE", afetrData.getIlGoodsId(), goodsListRequestDto.getUserVo().getUserId(), beforeGoodsData, afterGoodsData, createDate);
        }

        // 상품상태 변경 실패 list 저장
        GoodsListResponseDto goodsListResponseDto = new GoodsListResponseDto();
    	goodsListResponseDto.setTotal(failedCount);
        if (failedCount > 0) {
        	goodsListResponseDto.setRows(failedList);
        }

        return ApiResult.success(goodsListResponseDto);
    }

	@Override
	public ExcelDownloadDto getGoodsListExcel(GoodsListRequestDto goodsListRequestDto) {
		// TODO Auto-generated method stub
		String excelFileName = "상품 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		ArrayList<String> ilItemCdArray = new ArrayList<String>();
		String codeStrFlag = "N";
        String ilItemCodeListStr = "";

//		 if (!StringUtil.isEmpty(goodsListRequestDto.getFindKeyword())) {
//			 // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
//	         String ilItemCodeListStr = goodsListRequestDto.getFindKeyword().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
//
//	         String regExp = "^[0-9]+$";
////	            ilItemCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
//	         String[] ilItemCodeListArray = ilItemCodeListStr.split(",+");
//	         for(int i = 0; i < ilItemCodeListArray.length; i++) {
//	        	 String ilItemCodeSearchVal = ilItemCodeListArray[i];
//	        	 if(ilItemCodeSearchVal.isEmpty()) {
//	        		 continue;
//	        	 }
//	        	 if(ilItemCodeSearchVal.matches(regExp)) {
//	        		 ilItemCdArray.add(String.valueOf(Long.valueOf(ilItemCodeSearchVal)));
//	        	 }else {
//	        		 codeStrFlag = "Y";
//	        		 ilItemCdArray.add(ilItemCodeSearchVal);
//	        	 }
//	         }
//		 }

		// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
		if (!StringUtil.isEmpty(goodsListRequestDto.getFindKeyword())) {
			ilItemCodeListStr = goodsListRequestDto.getFindKeyword().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
		}
		else if (!StringUtil.isEmpty(goodsListRequestDto.getFindKeywordOnMulti())) {
			ilItemCodeListStr = goodsListRequestDto.getFindKeywordOnMulti().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
		}

        if (!StringUtil.isEmpty(ilItemCodeListStr)) {
			String[] ilItemCodeListArray = ilItemCodeListStr.split(",+");
			for(int i = 0; i < ilItemCodeListArray.length; i++) {
				String ilItemCodeSearchVal = ilItemCodeListArray[i];
				if(ilItemCodeSearchVal.isEmpty()) {
					continue;
				}
				ilItemCdArray.add(ilItemCodeSearchVal);
			}
		}

        if (!StringUtil.isEmpty(goodsListRequestDto.getFindKeyword())) {
            goodsListRequestDto.setFindKeywordList(ilItemCdArray); // 검색어
            goodsListRequestDto.setFindKeywordOnMultiList(new ArrayList<String>()); // 검색어
        }
        else if (!StringUtil.isEmpty(goodsListRequestDto.getFindKeywordOnMulti())) {
            goodsListRequestDto.setFindKeywordList(new ArrayList<String>()); // 검색어
            goodsListRequestDto.setFindKeywordOnMultiList(ilItemCdArray); // 검색어
        }
		goodsListRequestDto.setFindKeywordStrFlag(codeStrFlag);
		goodsListRequestDto.setGoodsTypeList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS)); // 상품유형
		goodsListRequestDto.setSaleTypeList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getSaleType(), Constants.ARRAY_SEPARATORS)); // 판매유형
		goodsListRequestDto.setGoodsDeliveryTypeList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getGoodsDeliveryType(), Constants.ARRAY_SEPARATORS)); // 배송유형
		goodsListRequestDto.setSaleStatusList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getSaleStatus(), Constants.ARRAY_SEPARATORS)); // 판매상태
		goodsListRequestDto.setStorageMethodTypeList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getStorageMethodType(), Constants.ARRAY_SEPARATORS)); // 보관방법
		goodsListRequestDto.setGoodsOutMallSaleStatList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getGoodsOutMallSaleStat(), Constants.ARRAY_SEPARATORS)); // 외부몰 판매상태
		goodsListRequestDto.setStockStatusList(goodsListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getStockStatus(), Constants.ARRAY_SEPARATORS)); // 재고운영형태


		List<GoodsVo> goodsList = goodsListService.getGoodsListExcel(goodsListRequestDto);

		//엑셀다운로드 양식을 위한 공통
		ExcelWorkSheetDto firstWorkSheetDto = policyExcelTmpltBiz.getCommonDownloadExcelTmplt(goodsListRequestDto.getPsExcelTemplateId());
		firstWorkSheetDto.setExcelDataList(goodsList);

		if (firstWorkSheetDto.getExcelFileName() != null && firstWorkSheetDto.getExcelFileName() != "") {
			excelFileName = firstWorkSheetDto.getExcelFileName();
		}

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();
		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}


	/**
	 * @Desc 상품등록 승인 목록 조회
	 * @param ApprovalGoodsRegistRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getApprovalGoodsRegistList(ApprovalGoodsRequestDto approvalGoodsRequestDto){
		return ApiResult.success(goodsListService.getApprovalGoodsRegistList(approvalGoodsRequestDto));
	}


	/**
	 * @Desc 상품등록 승인 요청철회
	 * @param ApprovalGoodsRegistRequestDto
	 * @return ApiResult
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putCancelRequestApprovalGoodsRegist(ApprovalGoodsRequestDto requestDto) throws Exception {

		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode();

		if(CollectionUtils.isEmpty(requestDto.getIlGoodsApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilGoodsApprId : requestDto.getIlGoodsApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkCancelable(ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode(), ilGoodsApprId);

			if(!apiResult.getCode().equals(ApprovalValidation.CANCELABLE.getCode())) {
				return apiResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
			MessageCommEnum emums = goodsListService.putCancelRequestApprovalGoodsRegist(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
				throw new BaseException(emums);
			}

			MessageCommEnum goodsStataEmums = goodsRegistBiz.goodsApprAfterProc(approvalVo.getTaskPk(), apprKindTp);

			if(!BaseEnums.Default.SUCCESS.equals(goodsStataEmums)) {
				throw new BaseException(goodsStataEmums);
			}

		}

		return ApiResult.success();
	}

	/**
	 * @Desc 상품등록 승인처리
	 * @param ApprovalGoodsRegistRequestDto
	 * @return ApiResult
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putApprovalProcessGoodsRegist(ApprovalGoodsRequestDto requestDto) throws Exception {

		String reqApprStat = requestDto.getApprStat();
		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode();

		if(!ApprovalStatus.DENIED.getCode().equals(reqApprStat)
				&& !ApprovalStatus.APPROVED.getCode().equals(reqApprStat)) {
			return ApiResult.result(ApprovalValidation.NONE_REQUEST);
		}

		if(CollectionUtils.isEmpty(requestDto.getIlGoodsApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilGoodsApprId : requestDto.getIlGoodsApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkApprovalProcess(apprKindTp, ilGoodsApprId);
			ApiResult<?> dupResult = approvalAuthBiz.checkDuplicateApproval(apprKindTp, ilGoodsApprId);

			if(!apiResult.getCode().equals(ApprovalValidation.APPROVABLE.getCode())) {
				//스킵? 혹은 계속진행? 결정대기중
				return apiResult;
			}

			//품목 상품 가격 관련 중복 승인 체크
			if(!BaseEnums.Default.SUCCESS.equals(dupResult.getMessageEnum())) {
				//스킵? 혹은 계속진행? 결정대기중
				return dupResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
			if(ApprovalStatus.DENIED.getCode().equals(reqApprStat)) {
				approvalVo.setApprStat(reqApprStat);
				approvalVo.setStatusComment(requestDto.getStatusComment());
			}

			MessageCommEnum emums = goodsListService.putApprovalProcessGoodsRegist(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
				throw new BaseException(emums);
			}

			if (ApprovalStatus.APPROVED.getCode().equals(approvalVo.getApprStat())
					|| ApprovalStatus.DENIED.getCode().equals(approvalVo.getApprStat())){
				MessageCommEnum goodsStataEmums = goodsRegistBiz.goodsApprAfterProc(approvalVo.getTaskPk(), apprKindTp);

				if(!BaseEnums.Default.SUCCESS.equals(goodsStataEmums)) {
					throw new BaseException(goodsStataEmums);
				}
			}
		}

		return ApiResult.success();
	}

	/**
	 * @Desc 상품 수정 승인 목록 조회
	 * @param ApprovalGoodsRegistRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getApprovalGoodsClientList(ApprovalGoodsRequestDto approvalGoodsRequestDto){
		return ApiResult.success(goodsListService.getApprovalGoodsClientList(approvalGoodsRequestDto));
	}

	/**
	 * @Desc 상품 수정 승인 요청철회
	 * @param ApprovalGoodsRegistRequestDto
	 * @return ApiResult
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putCancelRequestApprovalGoodsClient(ApprovalGoodsRequestDto requestDto) throws Exception {

		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode();

		if(CollectionUtils.isEmpty(requestDto.getIlGoodsApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilGoodsApprId : requestDto.getIlGoodsApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkCancelable(apprKindTp, ilGoodsApprId);

			if(!ApprovalValidation.CANCELABLE.getCode().equals(apiResult.getCode())) {
				return apiResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
			MessageCommEnum apprEmums = goodsListService.putCancelRequestApprovalGoodsRegist(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(apprEmums)) {
				throw new BaseException(apprEmums);
			}

			MessageCommEnum goodsStataEmums = goodsRegistBiz.goodsApprAfterProc(approvalVo.getTaskPk(), apprKindTp);

			if(!BaseEnums.Default.SUCCESS.equals(goodsStataEmums)) {
				throw new BaseException(apprEmums);
			}

		}

		return ApiResult.success();
	}

    /**
     * 거래처 상품수정 승인 폐기처리
     * @param requestDto
     * @return
     * @throws Exception
     */
    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putDisposalApprovalGoodsClient(ApprovalGoodsRequestDto requestDto) throws Exception {

    	String apprKindTp = ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode();

		if(CollectionUtils.isEmpty(requestDto.getIlGoodsApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilGoodsApprId : requestDto.getIlGoodsApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkDisposable(apprKindTp, ilGoodsApprId);

			if (!ApprovalValidation.DISPOSABLE.getCode().equals(apiResult.getCode())) {
				return apiResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
			MessageCommEnum emums = goodsListService.putApprovalProcessGoodsRegist(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
				throw new BaseException(emums);
			}

		}

        return ApiResult.success();
    }

    /**
	 * @Desc 상품 수정 승인처리
	 * @param ApprovalGoodsRegistRequestDto
	 * @return ApiResult
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putApprovalProcessGoodsClient(ApprovalGoodsRequestDto requestDto) throws Exception {

		String reqApprStat = requestDto.getApprStat();
		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode();

		if(CollectionUtils.isEmpty(requestDto.getIlGoodsApprIdList())) {
			return ApiResult.fail();
		}

		if(!ApprovalStatus.DENIED.getCode().equals(reqApprStat)
				&& !ApprovalStatus.APPROVED.getCode().equals(reqApprStat)) {
			return ApiResult.result(ApprovalValidation.NONE_REQUEST);
		}

		for(String ilGoodsApprId : requestDto.getIlGoodsApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkApprovalProcess(apprKindTp, ilGoodsApprId);
			ApiResult<?> dupResult = approvalAuthBiz.checkDuplicateApproval(apprKindTp, ilGoodsApprId);

			if(!ApprovalValidation.APPROVABLE.getCode().equals(apiResult.getCode())) {
				//스킵? 혹은 계속진행? 결정대기중
				return apiResult;
			}

			//품목 상품 가격 관련 중복 승인 체크
			if(!BaseEnums.Default.SUCCESS.equals(dupResult.getMessageEnum())) {
				//스킵? 혹은 계속진행? 결정대기중
				return dupResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo) apiResult.getData();
			if (ApprovalStatus.DENIED.getCode().equals(reqApprStat)) {
				approvalVo.setApprStat(reqApprStat);
				approvalVo.setStatusComment(requestDto.getStatusComment());
			}

			MessageCommEnum apprEmums = goodsListService.putApprovalProcessGoodsRegist(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(apprEmums)) {
				throw new BaseException(apprEmums);
			}

			if (ApprovalStatus.APPROVED.getCode().equals(approvalVo.getApprStat())
					|| ApprovalStatus.DENIED.getCode().equals(approvalVo.getApprStat())){
				MessageCommEnum goodsStataEmums = goodsRegistBiz.goodsApprAfterProc(approvalVo.getTaskPk(), apprKindTp);

				if(!BaseEnums.Default.SUCCESS.equals(goodsStataEmums)) {
					throw new BaseException(apprEmums);
				}
			}

		}

		return ApiResult.success();
	}

	/**
	 * @Desc 상품 수정관련 상품 상세내용 조회
	 * @param ilGoodsApprId
	 * @return ApiResult
	 */
	@Override
	public ApiResult getGoodsClientDetail(String ilGoodsApprId) {
		return goodsListService.getApprovalGoodsDetailClient(ilGoodsApprId);
	}


	/**
	 * @Desc 상품 할인 승인 목록 조회
	 * @param approvalGoodsRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getApprovalGoodsDiscountList(ApprovalGoodsRequestDto approvalGoodsRequestDto) {
		return ApiResult.success(goodsListService.getApprovalGoodsDiscountList(approvalGoodsRequestDto));
	}

	/**
	 * @Desc 상품 할인 승인 요청철회
	 * @param approvalGoodsRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putCancelRequestApprovalGoodsDiscount(ApprovalGoodsRequestDto requestDto) throws Exception {
		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode();

		if(CollectionUtils.isEmpty(requestDto.getIlGoodsApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilGoodsApprId : requestDto.getIlGoodsApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkCancelable(apprKindTp, ilGoodsApprId);

			if(!ApprovalValidation.CANCELABLE.getCode().equals(apiResult.getCode())) {
				return apiResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
			MessageCommEnum apprEmums = goodsListService.putCancelRequestApprovalGoodsDiscount(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(apprEmums)) {
				throw new BaseException(apprEmums);
			}

			//TODO : 상품 할인 후처리 진행

		}

		return ApiResult.success();
	}

	/**
	 * @Desc 상품 할인 승인처리
	 * @param approvalGoodsRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putApprovalProcessGoodsDiscount(ApprovalGoodsRequestDto requestDto) throws Exception {

		String reqApprStat = requestDto.getApprStat();
        String apprKindTp = ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode();

        if(CollectionUtils.isEmpty(requestDto.getIlGoodsApprIdList())) {
			return ApiResult.fail();
		}

		if(!ApprovalStatus.DENIED.getCode().equals(reqApprStat)
				&& !ApprovalStatus.APPROVED.getCode().equals(reqApprStat)) {
			return ApiResult.result(ApprovalValidation.NONE_REQUEST);
		}

		for(String ilGoodsApprId : requestDto.getIlGoodsApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkApprovalProcess(apprKindTp, ilGoodsApprId);
			ApiResult<?> dupResult = approvalAuthBiz.checkDuplicateApproval(apprKindTp, ilGoodsApprId);

			if(!ApprovalValidation.APPROVABLE.getCode().equals(apiResult.getCode())) {
				//스킵? 혹은 계속진행? 결정대기중
				return apiResult;
			}

			//품목 상품 가격 관련 중복 승인 체크
			if(!BaseEnums.Default.SUCCESS.equals(dupResult.getMessageEnum())) {
				//스킵? 혹은 계속진행? 결정대기중
				return dupResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo) apiResult.getData();
			if (ApprovalStatus.DENIED.getCode().equals(reqApprStat)) {
				approvalVo.setApprStat(reqApprStat);
				approvalVo.setStatusComment(requestDto.getStatusComment());
			}
			// APPR 철회, 반려, 완료이면 현재 원가 정상가 저장
//			if (ApprovalStatus.CANCEL.getCode().equals(reqApprStat)
//					|| ApprovalStatus.APPROVED.getCode().equals(reqApprStat)
//					|| ApprovalStatus.DENIED.getCode().equals(reqApprStat)){
//				GoodsPriceVo currentGoodsPrice = goodsListService.getCurrentGoodsPriceByGoodsDiscountApprId(ilGoodsApprId);
//				if (currentGoodsPrice == null) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
//
//				approvalVo.setRecommendedPriceChg(currentGoodsPrice.getRecommendedPrice());
//				approvalVo.setStandardPriceChg(currentGoodsPrice.getStandardPrice());
//			}

			MessageCommEnum apprEmums = goodsListService.putApprovalProcessGoodsDiscount(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(apprEmums)) {
				throw new BaseException(apprEmums);
			}

			//TODO : 상품 할인 후처리 진행
			if (ApprovalStatus.APPROVED.getCode().equals(approvalVo.getApprStat())){
				MessageCommEnum goodsStataEmums = goodsRegistBiz.goodsDiscountApprAfterProc(approvalVo.getTaskPk(), approvalVo.getDiscountTp());

				if(!BaseEnums.Default.SUCCESS.equals(goodsStataEmums)) {
					throw new BaseException(goodsStataEmums);
				}
			}
		}

		return ApiResult.success();
	}

	/**
	 * @Desc 상품할인 승인 폐기처리
	 * @param approvalGoodsRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putDisposalApprovalGoodsDiscount(ApprovalGoodsRequestDto requestDto) throws Exception {

		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode();

		if(CollectionUtils.isEmpty(requestDto.getIlGoodsApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilGoodsApprId : requestDto.getIlGoodsApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkDisposable(apprKindTp, ilGoodsApprId);

			if(!ApprovalValidation.DISPOSABLE.getCode().equals(apiResult.getCode())) {
				return apiResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
			MessageCommEnum apprEmums = goodsListService.putDisposalApprovalGoodsDiscount(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(apprEmums)) {
				throw new BaseException(apprEmums);
			}

		}

		return ApiResult.success();
	}
}

