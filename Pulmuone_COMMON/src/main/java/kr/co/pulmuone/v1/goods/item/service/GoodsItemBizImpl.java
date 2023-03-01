package kr.co.pulmuone.v1.goods.item.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalItemRegistRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalItemRegistResponseDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.approval.auth.service.ApprovalAuthBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalAuthType;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalStatus;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalValidation;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.SaleStatus;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.ItemStatusTp;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.goods.service.GoodsRegistBiz;
import kr.co.pulmuone.v1.goods.item.dto.*;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemApprovalPriceVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemInfoVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemRegistApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemListVo;
import kr.co.pulmuone.v1.policy.excel.service.PolicyExcelTmpltBiz;
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
* 상품품목 BizImpl
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 8.                손진구          최초작성
*  1.1    2020.10.21.                박주형          ( 리팩토리 ) 기존 BOS 에 있던 마스터 품목 리스트 관련 로직 이관
* =======================================================================
 * </PRE>
 */
@Service
public class GoodsItemBizImpl implements GoodsItemBiz {

    @Autowired
    private GoodsItemService goodsItemService;

    @Autowired
	private GoodsItemRegisterService goodsItemRegisterService;

	@Autowired
	private PolicyExcelTmpltBiz policyExcelTmpltBiz;

	@Autowired
	private ApprovalAuthBiz approvalAuthBiz;

	@Autowired
	private GoodsRegistBiz goodsRegistBiz;

	@Autowired
	private GoodsItemModifyBiz goodsItemModifyBiz;

    /**
     * @Desc 상품ID 별 품목상세 조회
     * @param goodsId
     * @return ItemInfoVo
     * @throws Exception
     */
    @Override
    public ItemInfoVo getGoodsIdByItemInfo(Long goodsId) {
        return goodsItemService.getGoodsIdByItemInfo(goodsId);
    }

    /**
     * @Desc 마스터 품목 리스트 조회
     * @param MasterItemListRequestDto : 마스터 품목 리스트 검색 조건 request dto
     * @return MasterItemListResponseDto : 마스터 품목 리스트 검색 결과 response dto
     */
    @Override
    public ApiResult<?> getItemList(MasterItemListRequestDto masterItemListRequestDto) {

        MasterItemListResponseDto result = new MasterItemListResponseDto();

        Page<MasterItemListVo> rows = goodsItemService.getItemList(masterItemListRequestDto);

        result.setTotal(rows.getTotal());
        result.setRows(rows.getResult());

        return ApiResult.success(result);

    }

    /**
     * @Desc 마스터 품목 리스트 엑셀 다운로드 목록 조회
     * @param MasterItemListRequestDto : 마스터 품목 리스트 검색 조건 request dto
     * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     */
    @Override
    public ExcelDownloadDto getItemListExcel(MasterItemListRequestDto masterItemListRequestDto) {

        String excelFileName = "마스터 품목 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

        List<MasterItemListVo> itemList = goodsItemService.getItemListExcel(masterItemListRequestDto);

        //엑셀다운로드 양식을 위한 공통
        ExcelWorkSheetDto firstWorkSheetDto = policyExcelTmpltBiz.getCommonDownloadExcelTmplt(masterItemListRequestDto.getPsExcelTemplateId());

        if (firstWorkSheetDto.getExcelFileName() != null && firstWorkSheetDto.getExcelFileName() != "") {
        	excelFileName = firstWorkSheetDto.getExcelFileName();
        }

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }

    /**
	 * 품목등록 승인 목록 조회
	 *
	 * @param ApprovalItemRegistRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getApprovalItemRegistList(ApprovalItemRegistRequestDto requestDto) {
		return ApiResult.success(goodsItemService.getApprovalItemRegistList(requestDto));
	}

	/**
     * 품목등록 승인 요청철회
     *
     * @param ApprovalItemRegistRequestDto
     * @return ApiResult
     * @throws 	Exception
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putCancelRequestApprovalItemRegist(ApprovalItemRegistRequestDto requestDto) throws Exception {

		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode();

		if(CollectionUtils.isEmpty(requestDto.getIlItemApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilItemApprId : requestDto.getIlItemApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkCancelable(apprKindTp, ilItemApprId);

			if (!ApprovalValidation.CANCELABLE.getCode().equals(apiResult.getCode())) {
				return apiResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
			MessageCommEnum emums = goodsItemService.putCancelRequestApprovalItemRegist(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
				throw new BaseException(emums);
			}

		}

    	return ApiResult.success();
    }

    /**
     * 품목등록 승인처리
     *
     * @param ApprovalItemRegistRequestDto
     * @return ApiResult
     * @throws 	Exception
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putApprovalProcessItemRegist(ApprovalItemRegistRequestDto requestDto) throws Exception {

		String reqApprStat = requestDto.getApprStat();
		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode();

		if(!ApprovalStatus.DENIED.getCode().equals(reqApprStat)
				&& !ApprovalStatus.APPROVED.getCode().equals(reqApprStat)) {
			return ApiResult.result(ApprovalValidation.NONE_REQUEST);
		}

		if(CollectionUtils.isEmpty(requestDto.getIlItemApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilItemApprId : requestDto.getIlItemApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkApprovalProcess(apprKindTp, ilItemApprId);

			if (!ApprovalValidation.APPROVABLE.getCode().equals(apiResult.getCode())) {
				//스킵? 혹은 계속진행? 결정대기중
				return apiResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();

			if(ApprovalStatus.DENIED.getCode().equals(reqApprStat)) {
				approvalVo.setApprStat(reqApprStat);
				approvalVo.setStatusComment(requestDto.getStatusComment());
			}

			MessageCommEnum emums = goodsItemService.putApprovalProcessItemRegist(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
				throw new BaseException(emums);
			}
			else { // 승인처리 성공시
				if (ApprovalStatus.APPROVED.getCode().equals(reqApprStat)) { // 승인완료시 품목 상태를 변경
	  				ItemRegistApprVo itemApprInfo = goodsItemRegisterService.itemApprInfo(null, null, ilItemApprId, apprKindTp);
					goodsItemRegisterService.updateItemRequestStatus(itemApprInfo.getIlItemCd(), ItemStatusTp.REGISTER.getCode());
				}
			}

		}

		return ApiResult.success();
	}


	/**
	 * 품목가격 승인 목록 조회
	 *
	 * @param ApprovalItemRegistRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getApprovalItemPriceList(ApprovalItemRegistRequestDto requestDto) {
		return ApiResult.success(goodsItemService.getApprovalItemPriceList(requestDto));
	}

	/**
	 * 품목가격 승인 요청철회
	 *
	 * @param ApprovalItemRegistRequestDto
	 * @return ApiResult
	 * @throws 	Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putCancelRequestApprovalItemPrice(ApprovalItemRegistRequestDto requestDto) throws Exception {

		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode();

		if(CollectionUtils.isEmpty(requestDto.getIlItemPriceApprIdList())) {
			return ApiResult.fail();
		}

		int failCount = 0;
		StringBuilder failMessageCode = new StringBuilder();
		for(String ilItemPriceApprId : requestDto.getIlItemPriceApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkCancelable(apprKindTp, ilItemPriceApprId);

			if (!ApprovalValidation.CANCELABLE.getCode().equals(apiResult.getCode())) {
				failCount ++;
				failMessageCode.append(",").append(apiResult.getCode());
			}
			else {
				ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
				MessageCommEnum emums = goodsItemService.putCancelRequestApprovalItemPrice(approvalVo);

				if(!BaseEnums.Default.SUCCESS.equals(emums)) {
					throw new BaseException(emums);
				}
			}
		}

		return ApiResult.success(ApprovalItemRegistResponseDto.builder()
				.failCount(failCount)
				.failMessageCode(failMessageCode.toString())
				.build());
	}

	/**
	 * 품목가격 승인 / 반려 처리
	 *
	 * @param ApprovalItemRegistRequestDto
	 * @return ApiResult
	 * @throws 	Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putApprovalProcessItemPrice(ApprovalItemRegistRequestDto requestDto) throws Exception {

		String reqApprStat = requestDto.getApprStat();
		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode();

		if(!ApprovalStatus.DENIED.getCode().equals(reqApprStat)
				&& !ApprovalStatus.APPROVED.getCode().equals(reqApprStat)) {
			return ApiResult.result(ApprovalValidation.NONE_REQUEST);
		}

		if(CollectionUtils.isEmpty(requestDto.getIlItemPriceApprIdList())) {
			return ApiResult.fail();
		}

		int failCount = 0;
		StringBuilder failMessageCode = new StringBuilder();
		for(String ilItemPriceApprId : requestDto.getIlItemPriceApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkApprovalProcess(apprKindTp, ilItemPriceApprId);

			if (!ApprovalValidation.APPROVABLE.getCode().equals(apiResult.getCode())) {
				if(ApprovalStatus.DENIED.getCode().equals(reqApprStat)) {     //반려 케이스
					if(ApprovalEnums.ApprovalValidation.REQUEST_ALREADY_APPROVED.getCode().equals(apiResult.getCode())){
						failCount ++;
						failMessageCode.append(",").append(ApprovalEnums.ApprovalValidation.ALREADY_APPROVED.getCode());
					}else{
						failCount ++;
						failMessageCode.append(",").append(apiResult.getCode());
					}
				} else {
					failCount ++;
					failMessageCode.append(",").append(apiResult.getCode());
				}
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
			if(ApprovalStatus.DENIED.getCode().equals(reqApprStat)) {
				approvalVo.setApprStat(reqApprStat);
				approvalVo.setStatusComment(requestDto.getStatusComment());
			}

			//품목 승인 처리 시점, 가격 정보 매핑
//			ItemApprovalPriceVo approvalPriceVo = goodsItemService.getApprovalItemPriceChangeNowInfo(approvalVo);
//			approvalVo.setStandardPriceChg(approvalPriceVo.getStandardPriceChg());
//			approvalVo.setRecommendedPriceChg(approvalPriceVo.getRecommendedPriceChg());

			MessageCommEnum emums = goodsItemService.putApprovalProcessItemPrice(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
				throw new BaseException(emums);
			}

			// 승인이면서 최종승인자 승인의 경우 - 품목가격 반영
			if(ApprovalStatus.APPROVED.getCode().equals(reqApprStat) && ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(approvalVo.getApprStep())) {
				// 요청값 설정
				String adminId = "0";
				UserVo userVo = SessionUtil.getBosUserVO();
				if (userVo != null) {
					adminId = userVo.getUserId();
				}
				approvalVo.setCreateId(adminId);
				MessageCommEnum addPriceOrigResult = goodsItemService.addItemPriceOrigByApproval(approvalVo);

				if (BaseEnums.Default.SUCCESS.equals(addPriceOrigResult)) {
					goodsRegistBiz.spGoodsPriceUpdateWhenItemPriceChanges(approvalVo.getIlItemCd()); // 품목가격 변경에 따른 상품가격 업데이트 프로시저 호출
				}
				else {
					throw new BaseException(emums);
				}
			}

		}

    	goodsRegistBiz.spPackageGoodsPriceUpdateWhenItemPriceChanges(); // 묶음상품 가격 정보 업데이트 프로시저 호출

    	return ApiResult.success(ApprovalItemRegistResponseDto.builder()
				.failCount(failCount)
				.failMessageCode(failMessageCode.toString())
				.build());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> delItemPriceOrig(ItemPriceDelRequestDto dto) throws Exception {
		// 승인완료된 가격 삭제시 폐기처리 S
		if (dto.getIlItemPriceApprId() != null && !"".equals(dto.getIlItemPriceApprId())) {
			ApprovalItemRegistRequestDto approvalItemRegistRequestDto = new ApprovalItemRegistRequestDto();
			ArrayList<String> listIlItemPriceApprId = new ArrayList<String>();
			listIlItemPriceApprId.add(dto.getIlItemPriceApprId());
			approvalItemRegistRequestDto.setIlItemPriceApprIdList(listIlItemPriceApprId);
			this.putDisposalApprovalItemPrice(approvalItemRegistRequestDto);
		}
		// 승인완료된 가격 삭제시 폐기처리 E

		MessageCommEnum responseEnum = goodsItemService.delItemPriceOrig(dto);
		if (BaseEnums.Default.SUCCESS.equals(responseEnum)) {
			// 상품가격 프로시저 호출
			goodsRegistBiz.spGoodsPriceUpdateWhenItemPriceChanges(dto.getIlItemCd()); // 품목가격 변경에 따른 상품가격 업데이트 프로시저 호출
	    	goodsRegistBiz.spPackageGoodsPriceUpdateWhenItemPriceChanges(); // 묶음상품 가격 정보 업데이트 프로시저 호출
		}
		else {
			throw new BaseException(responseEnum);
		}

		return ApiResult.success();
	}

	/**
	 * 품목가격 폐기 처리
	 *
	 * @param ApprovalItemRegistRequestDto
	 * @return ApiResult
	 * @throws 	Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putDisposalApprovalItemPrice(ApprovalItemRegistRequestDto requestDto) throws Exception {

		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode();

		if(CollectionUtils.isEmpty(requestDto.getIlItemPriceApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilItemPriceApprId : requestDto.getIlItemPriceApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkDisposable(apprKindTp, ilItemPriceApprId);

			if (!ApprovalValidation.DISPOSABLE.getCode().equals(apiResult.getCode())) {
				return apiResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
			MessageCommEnum emums = goodsItemService.putDisposalApprovalItemPrice(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
				throw new BaseException(emums);
			}

		}

		return ApiResult.success();
	}

	@Override
	public String getApprovalDeniedInfo(Long ilItemPriceApprId) {
		return goodsItemService.getApprovalDeniedInfo(ilItemPriceApprId);
	}

	@Override
	public ApiResult<?> getItemGoodsPackageList(MasterItemListRequestDto masterItemListRequestDto) {
		// TODO Auto-generated method stub
		MasterItemListResponseDto result = new MasterItemListResponseDto();

        Page<MasterItemListVo> rows = goodsItemService.getItemGoodsPackageList(masterItemListRequestDto);

        result.setTotal(rows.getTotal());
        result.setRows(rows.getResult());

        return ApiResult.success(result);

	}

	/**
     * 품목 수정 승인 요청 내역 조회
     * @param requestDto
     * @return
     */
    @Override
    public ApiResult<?> getApprovalItemClientList(ApprovalItemRegistRequestDto requestDto) {
        return ApiResult.success(goodsItemService.getApprovalItemClientList(requestDto));
    }

    /**
     * 품목 수정 승인 요청 취소
     * @param requestDto
     * @return
     * @throws Exception
     */
    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putCancelRequestApprovalItemClient(ApprovalItemRegistRequestDto requestDto) throws Exception {
    	String apprKindTp = ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode();

		if(CollectionUtils.isEmpty(requestDto.getIlItemApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilItemApprId : requestDto.getIlItemApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkCancelable(apprKindTp, ilItemApprId);

			if (!ApprovalValidation.CANCELABLE.getCode().equals(apiResult.getCode())) {
				return apiResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
			MessageCommEnum emums = goodsItemService.putCancelRequestApprovalItemClient(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
				throw new BaseException(emums);
			}

			//상품 상태값 원복
			String ilItemCode = approvalVo.getIlItemCd();
			goodsRegistBiz.updateGoodsSaleStatusToBackByItemAppr(ilItemCode);

		}

        return ApiResult.success();
    }

    /**
     * 거래처 품목수정 승인 폐기처리
     * @param requestDto
     * @return
     * @throws Exception
     */
    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putDisposalApprovalItemClient(ApprovalItemRegistRequestDto requestDto) throws Exception {

    	String apprKindTp = ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode();

		if(CollectionUtils.isEmpty(requestDto.getIlItemApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilItemApprId : requestDto.getIlItemApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkDisposable(apprKindTp, ilItemApprId);

			if (!ApprovalValidation.DISPOSABLE.getCode().equals(apiResult.getCode())) {
				return apiResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
			MessageCommEnum emums = goodsItemService.putApprovalProcessItemClient(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
				throw new BaseException(emums);
			}

		}

        return ApiResult.success();
    }

    /**
     * 품목 수정 승인 요청 처리
     * @param requestDto
     * @return
     * @throws Exception
     */
    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putApprovalProcessItemClient(ApprovalItemRegistRequestDto requestDto) throws Exception {
    	String reqApprStat = requestDto.getApprStat();
		String apprKindTp = ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode();

		if(!ApprovalStatus.DENIED.getCode().equals(reqApprStat)
				&& !ApprovalStatus.APPROVED.getCode().equals(reqApprStat)) {
			return ApiResult.result(ApprovalValidation.NONE_REQUEST);
		}

		if(CollectionUtils.isEmpty(requestDto.getIlItemApprIdList())) {
			return ApiResult.fail();
		}

		for(String ilItemApprId : requestDto.getIlItemApprIdList()) {
			ApiResult<?> apiResult = approvalAuthBiz.checkApprovalProcess(apprKindTp, ilItemApprId);

			if (!ApprovalValidation.APPROVABLE.getCode().equals(apiResult.getCode())) {
				//스킵? 혹은 계속진행? 결정대기중
				return apiResult;
			}

			ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();

			if(ApprovalStatus.DENIED.getCode().equals(reqApprStat)) {
				approvalVo.setApprStat(reqApprStat);
				approvalVo.setStatusComment(requestDto.getStatusComment());
			}

			MessageCommEnum emums = goodsItemService.putApprovalProcessItemClient(approvalVo);

			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
				throw new BaseException(emums);
			}

			if (ApprovalStatus.APPROVED.getCode().equals(approvalVo.getApprStat())
					|| ApprovalStatus.DENIED.getCode().equals(approvalVo.getApprStat())){
				String ilItemCode = approvalVo.getIlItemCd();
				//상품 상태값 변경
				goodsRegistBiz.updateGoodsSaleStatusToBackByItemAppr(ilItemCode);
				//appr 데이터 품목 항목으로 저장
				goodsItemModifyBiz.itemApprAfterProc(approvalVo.getTaskPk(), ilItemCode, apprKindTp);
			}

		}

		return ApiResult.success();
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addApprovalItemPrice(ItemPriceApprovalRequestDto dto) throws Exception {
		// 요청값 설정
		long adminId = 0;
		UserVo userVo = SessionUtil.getBosUserVO();
		if (userVo != null) {
			adminId = Long.parseLong(userVo.getUserId());
		}
		dto.setApprovalRequestUserId(adminId);
		dto.setCreateId(adminId);

		MessageCommEnum responseEnum = goodsItemService.addApprovalItemPrice(dto);

		if(!BaseEnums.Default.SUCCESS.equals(responseEnum)) {
			throw new BaseException(responseEnum);
		}
		return ApiResult.success();
	}

}
