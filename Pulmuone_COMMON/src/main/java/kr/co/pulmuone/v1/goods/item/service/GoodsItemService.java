package kr.co.pulmuone.v1.goods.item.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalItemRegistRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalAuthType;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ItemEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.item.dto.*;
import kr.co.pulmuone.v1.goods.item.dto.vo.*;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <PRE>
* Forbiz Korea
* 상품품목 Service
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 8.                손진구          최초작성
*  1.1    2020.10.21.                박주형          ( 리팩토링 ) 기존 BOS 에 있던 마스터 품목 리스트 관련 로직 이관
* =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class GoodsItemService {

    @Autowired
    private final GoodsItemMapper goodsItemMapper;

    /**
     * @Desc 상품ID 별 품목상세 조회
     * @param goodsId
     * @throws Exception
     * @return ItemInfoVo
     */
    protected ItemInfoVo getGoodsIdByItemInfo(Long goodsId) {
        return goodsItemMapper.getGoodsIdByItemInfo(goodsId);
    }

    /**
     * @Desc 마스터 품목 리스트 조회
     * @param MasterItemListRequestDto : 마스터 품목 리스트 검색 조건 request dto
     * @return Page<MasterItemListVo> : 마스터 품목 리스트 검색 결과 목록 ( 페이지네이션 적용 )
     */
    protected Page<MasterItemListVo> getItemList(MasterItemListRequestDto masterItemListRequestDto) {

        ArrayList<String> ilItemCdArray = new ArrayList<String>();
        ArrayList<String> searchMasterItemTypeArray = null;
        ArrayList<String> storageMethodArray = null;
        String codeStrFlag = "N";
        if (!StringUtil.isEmpty(masterItemListRequestDto.getIlItemCode())) {

            // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String ilItemCodeListStr = masterItemListRequestDto.getIlItemCode().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");

            String[] ilItemCodeListArray = ilItemCodeListStr.split(",+");
            for(int i = 0; i < ilItemCodeListArray.length; i++) {
            	String ilItemCodeSearchVal = ilItemCodeListArray[i];
            	if(ilItemCodeSearchVal.isEmpty()) {
            		continue;
            	}
        		ilItemCdArray.add(ilItemCodeSearchVal);
            }
        }

        // 권한 체크
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        masterItemListRequestDto.setListAuthSupplierId(listAuthSupplierId);
        masterItemListRequestDto.setListAuthWarehouseId(listAuthWarehouseId);

        // 품목코드 or 품목바코드로 조회시에는 ERP 연동여부 외 다른 검색 조건 무시
        if (ilItemCdArray != null && !ilItemCdArray.isEmpty()) {

            MasterItemListRequestDto masterItemListRequestDtoByItemCode = new MasterItemListRequestDto();

            masterItemListRequestDtoByItemCode.setIlItemCode(masterItemListRequestDto.getIlItemCode());
            masterItemListRequestDtoByItemCode.setIlItemCodeStrFlag(codeStrFlag);
            masterItemListRequestDtoByItemCode.setIlItemCodeKind(masterItemListRequestDto.getIlItemCodeKind());
            masterItemListRequestDtoByItemCode.setIlItemCodeArray(ilItemCdArray);
            masterItemListRequestDtoByItemCode.setErpLinkIfYn(masterItemListRequestDto.getErpLinkIfYn()); // ERP 연동여부
            masterItemListRequestDtoByItemCode.setItemStatusTp(masterItemListRequestDto.getItemStatusTp());
            masterItemListRequestDtoByItemCode.setListAuthSupplierId(listAuthSupplierId);
            masterItemListRequestDtoByItemCode.setListAuthWarehouseId(listAuthWarehouseId);

            masterItemListRequestDtoByItemCode.setPage(masterItemListRequestDto.getPage());
            masterItemListRequestDtoByItemCode.setPageSize(masterItemListRequestDto.getPageSize());
            masterItemListRequestDtoByItemCode.setSPage(masterItemListRequestDto.getsPage());
            masterItemListRequestDtoByItemCode.setEPage(masterItemListRequestDto.getePage());

            PageMethod.startPage(masterItemListRequestDtoByItemCode.getPage(), masterItemListRequestDtoByItemCode.getPageSize());

            return goodsItemMapper.getItemList(masterItemListRequestDtoByItemCode);

        }

        if (!StringUtil.isEmpty(masterItemListRequestDto.getMasterItemType())) {
            searchMasterItemTypeArray = StringUtil.getArrayList(masterItemListRequestDto.getMasterItemType().replace(" ", ""));
            masterItemListRequestDto.setMasterItemTypeArray(searchMasterItemTypeArray);
        }

        if (!StringUtil.isEmpty(masterItemListRequestDto.getStorageMethod())) {
            storageMethodArray = StringUtil.getArrayList(masterItemListRequestDto.getStorageMethod().replace(" ", ""));
            masterItemListRequestDto.setStorageMethodArray(storageMethodArray);
        }

        PageMethod.startPage(masterItemListRequestDto.getPage(), masterItemListRequestDto.getPageSize());

        return goodsItemMapper.getItemList(masterItemListRequestDto);

    }

    /**
     * @Desc 마스터 품목 리스트 엑셀 다운로드 목록 조회
     * @param MasterItemListRequestDto : 마스터 품목 리스트 검색 조건 request dto
     * @return List<MasterItemListVo> : 마스터 품목 리스트 엑셀 다운로드 목록
     */
    public List<MasterItemListVo> getItemListExcel(MasterItemListRequestDto masterItemListRequestDto) {

    	ArrayList<String> ilItemCdArray = new ArrayList<String>();
    	String codeStrFlag = "N";
        // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
        if (!StringUtil.isEmpty(masterItemListRequestDto.getIlItemCode())) {

            // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String ilItemCodeListStr = masterItemListRequestDto.getIlItemCode().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
            String[] ilItemCodeListArray = ilItemCodeListStr.split(",+");
            for(int i = 0; i < ilItemCodeListArray.length; i++) {
            	String ilItemCodeSearchVal = ilItemCodeListArray[i];
            	if(ilItemCodeSearchVal.isEmpty()) {
            		continue;
            	}
        		ilItemCdArray.add(ilItemCodeSearchVal);
            }


        }

        // 권한 체크
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        masterItemListRequestDto.setListAuthSupplierId(listAuthSupplierId);
        masterItemListRequestDto.setListAuthWarehouseId(listAuthWarehouseId);

        if (ilItemCdArray != null && !ilItemCdArray.isEmpty()) {
        	// 품목코드 or 품목바코드로 조회시에는 다른 검색 조건 무시

            MasterItemListRequestDto masterItemListRequestDtoByItemCode = new MasterItemListRequestDto();

            masterItemListRequestDtoByItemCode.setIlItemCode(masterItemListRequestDto.getIlItemCode());
            masterItemListRequestDtoByItemCode.setIlItemCodeStrFlag(codeStrFlag);
            masterItemListRequestDtoByItemCode.setIlItemCodeKind(masterItemListRequestDto.getIlItemCodeKind());
            masterItemListRequestDtoByItemCode.setIlItemCodeArray(ilItemCdArray);
            masterItemListRequestDtoByItemCode.setListAuthSupplierId(listAuthSupplierId);
            masterItemListRequestDtoByItemCode.setListAuthWarehouseId(listAuthWarehouseId);

            List<MasterItemListVo> itemList = goodsItemMapper.getItemListExcel(masterItemListRequestDtoByItemCode);

            // 화면과 동일하게 역순으로 no 지정
            for (int i = itemList.size() - 1; i >= 0; i--) {
                itemList.get(i).setRowNumber(String.valueOf(itemList.size() - i));
            }

            return itemList;

        }

        if (!StringUtil.isEmpty(masterItemListRequestDto.getMasterItemType())) {
            ArrayList<String> searchMasterItemTypeArray = StringUtil.getArrayList(masterItemListRequestDto.getMasterItemType().replace(" ", ""));
            masterItemListRequestDto.setMasterItemTypeArray(searchMasterItemTypeArray);
        }

        if (!StringUtil.isEmpty(masterItemListRequestDto.getStorageMethod())) {
            ArrayList<String> storageMethodArray = StringUtil.getArrayList(masterItemListRequestDto.getStorageMethod().replace(" ", ""));
            masterItemListRequestDto.setStorageMethodArray(storageMethodArray);
        }

        List<MasterItemListVo> itemList = goodsItemMapper.getItemListExcel(masterItemListRequestDto);

        // 화면과 동일하게 역순으로 no 지정
        for (int i = itemList.size() - 1; i >= 0; i--) {
            itemList.get(i).setRowNumber(String.valueOf(itemList.size() - i));
        }

        return itemList;

    }

    /**
	 * 품목등록 승인 목록 조회
	 *
	 * @param ApprovalItemRegistRequestDto
	 * @return ItemApprovalResponseDto
	 */
    protected ItemApprovalResponseDto getApprovalItemRegistList(ApprovalItemRegistRequestDto approvalItemRequestDto) {
		List<String> ilItemCdArray = this.getSearchKeyToSearchKeyList(approvalItemRequestDto.getIlItemCode(), Constants.ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS);
		List<String> approvalStatusArray = this.getSearchKeyToSearchKeyList(approvalItemRequestDto.getSearchApprovalStatus(), Constants.ARRAY_SEPARATORS);
		List<String> searchMasterItemTypeArray = this.getSearchKeyToSearchKeyList(approvalItemRequestDto.getMasterItemType(), Constants.ARRAY_SEPARATORS);

		approvalItemRequestDto.setIlItemCodeArray(ilItemCdArray);
		approvalItemRequestDto.setApprovalStatusArray(approvalStatusArray);
		approvalItemRequestDto.setMasterItemTypeArray(searchMasterItemTypeArray);
		approvalItemRequestDto.setApprKindType(ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode());

		PageMethod.startPage(approvalItemRequestDto.getPage(), approvalItemRequestDto.getPageSize());
		Page<ItemApprovalResultVo> resultVoPage = goodsItemMapper.getApprovalItemRegistList(approvalItemRequestDto);

		return ItemApprovalResponseDto.builder()
                                      .total(resultVoPage.getTotal())
                                      .rows(resultVoPage)
                                      .build();
    }

    /**
  	 * 품목등록 상태이력 등록
  	 * @param ApprovalStatusVo
  	 * @return int
  	 */
  	protected int addItemRegistStatusHistory(ApprovalStatusVo history){
  		return goodsItemMapper.addItemRegistStatusHistory(history);
  	}

  	/**
     * 품목등록 승인 요청철회
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putCancelRequestApprovalItemRegist(ApprovalStatusVo approvalVo) throws Exception {
		if(goodsItemMapper.putCancelRequestApprovalItemRegist(approvalVo) > 0
			&& this.addItemRegistStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }
    /**
     * 품목등록 승인처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putApprovalProcessItemRegist(ApprovalStatusVo approvalVo) throws Exception {
		if(goodsItemMapper.putApprovalProcessItemRegist(approvalVo) > 0
			&& this.addItemRegistStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
     * 품목가격 승인 목록 조회
     *
     * @param ApprovalItemRegistRequestDto
     * @return ItemApprovalResponseDto
     */
    protected ItemApprovalResponseDto getApprovalItemPriceList(ApprovalItemRegistRequestDto approvalItemRequestDto) {
		List<String> ilItemCdArray = this.getSearchKeyToSearchKeyList(approvalItemRequestDto.getIlItemCode(), Constants.ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS);
		List<String> approvalStatusArray = this.getSearchKeyToSearchKeyList(approvalItemRequestDto.getSearchApprovalStatus(), Constants.ARRAY_SEPARATORS);
		List<String> searchMasterItemTypeArray = this.getSearchKeyToSearchKeyList(approvalItemRequestDto.getMasterItemType(), Constants.ARRAY_SEPARATORS);

		approvalItemRequestDto.setIlItemCodeArray(ilItemCdArray);
		approvalItemRequestDto.setApprovalStatusArray(approvalStatusArray);
		approvalItemRequestDto.setMasterItemTypeArray(searchMasterItemTypeArray);
		approvalItemRequestDto.setApprKindType(ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode());

		PageMethod.startPage(approvalItemRequestDto.getPage(), approvalItemRequestDto.getPageSize());
		Page<ItemApprovalResultVo> resultVoPage = goodsItemMapper.getApprovalItemPriceList(approvalItemRequestDto);

      	return ItemApprovalResponseDto.builder()
                                      .total(resultVoPage.getTotal())
                                      .rows(resultVoPage)
                                      .build();
    }

    /**
     * 품목가격 상태이력 등록
     * @param ApprovalStatusVo
     * @return int
     */
    protected int addItemPriceStatusHistory(ApprovalStatusVo history){
    	return goodsItemMapper.addItemPriceStatusHistory(history);
    }

    /**
     * 품목가격 승인 요청철회
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putCancelRequestApprovalItemPrice(ApprovalStatusVo approvalVo) throws Exception {
    	if(goodsItemMapper.putCancelRequestApprovalItemPrice(approvalVo) > 0
    			&& this.addItemPriceStatusHistory(approvalVo) > 0 ) {
    		return BaseEnums.Default.SUCCESS;
    	}else {
    		throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
    	}
    }

    /**
     * 품목가격 승인처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putApprovalProcessItemPrice(ApprovalStatusVo approvalVo) throws Exception {
        if(goodsItemMapper.putApprovalProcessItemPrice(approvalVo) > 0
    			&& this.addItemPriceStatusHistory(approvalVo) > 0) {
    		return BaseEnums.Default.SUCCESS;
    	}else {
    		throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
    	}
    }

    /**
     * 품목가격 반영처리
     *
     * @param approvalVo ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum addItemPriceOrigByApproval(ApprovalStatusVo approvalVo) throws Exception {

    	ItemPriceOrigVo apprItemPriceOrig = goodsItemMapper.getApprovedItemPriceOrigInfo(approvalVo); // 승인 가격 정보를 가져온다. 정상가만 반영되는 품목의 경우 현재의 원가를 가져온다.
    	if (apprItemPriceOrig == null) {
            throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
    	}

		apprItemPriceOrig.setManagerUpdateYn("Y"); // 관리자에 의한 가격 변경 Y로 설정
		if (apprItemPriceOrig.getIlItemPriceOriginalId() == 0) { // 승인 가격 시작일에 대한 기존 가격 정보가 없을 경우
			apprItemPriceOrig.setSystemUpdateYn("N"); // Insert시는 시스템에 의한 가격 변경 N으로 설정
    		if (goodsItemMapper.addItemPriceOrigByApproval(apprItemPriceOrig) <= 0) {
                throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
    		}
		}
		else { // 승인 가격 시작일에 대한 기존 가격 정보가 있을때는 update, 이때 시스템에 의한 가격변경 설정값은 이전값을 따른다.
			if (goodsItemMapper.updateItemPriceOrigByItemPriceOrigId(apprItemPriceOrig) <= 0) {
                throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
			}
		}

		// 정상가만 반영하는 품목의 경우, 미래 가격에 대해 정상가를 현행화 해야할 항목에 대해 현행화 한다. S
		List<ItemPriceOrigVo> listItemPriceOrigInfoForApprovalSync = goodsItemMapper.getItemPriceOrigInfoForApprovalSync(apprItemPriceOrig);
		if (listItemPriceOrigInfoForApprovalSync != null) {
			for (ItemPriceOrigVo itemPriceOrigInfoForApprovalSync : listItemPriceOrigInfoForApprovalSync) {
				goodsItemMapper.updateItemPriceOrigByItemPriceOrigId(itemPriceOrigInfoForApprovalSync);
			}
		}
		// 정상가만 반영되는 경우, 미래 가격에 대해 정상가를 현행화 해야할 항목에 대해 현행화 한다. E

    	return BaseEnums.Default.SUCCESS;
    }

    /**
     * 품목가격 삭제처리
     *
     * @param dto ItemPriceDelRequestDto
     * @return MessageCommEnum
     */
    protected MessageCommEnum delItemPriceOrig(ItemPriceDelRequestDto dto) throws Exception {

    	ItemPriceOrigVo itemPriceOrig = goodsItemMapper.selectItemPriceOrigByItemPriceOrigId(dto.getIlItemPriceOrigId()); // 삭제할 가격 정보를 가져온다.
    	if (itemPriceOrig == null) { // 삭제할 가격 정보가 없으면 삭제 불가
            throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
    	}

    	ItemPriceOrigVo prevItemPriceOrig = goodsItemMapper.selectPrevItemPriceOrigByItemPriceOrigId(dto.getIlItemPriceOrigId()); // 이전 가격 정보를 가져온다.
    	if (prevItemPriceOrig == null) { // 이전 가격 정보가 없으면 삭제 불가
            throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
    	}

		UserVo userVo = SessionUtil.getBosUserVO();
		long modifyId = (userVo == null) ? 0 : Long.parseLong(userVo.getUserId());

		itemPriceOrig.setModifyId(modifyId);
		int delCnt = -1;
		if ("Y".equals(itemPriceOrig.getSystemUpdateYn())) { // 삭제할 항목이 시스템 업데이트된 항목이면 정상가/관리자 업데이트 유무만 현행화
			itemPriceOrig.setManagerUpdateYn("N"); // 관리자 업데이트 N로 설정
			itemPriceOrig.setRecommendedPrice(prevItemPriceOrig.getRecommendedPrice()); // 삭제 이전 정상가로 설정
    		delCnt = goodsItemMapper.updateItemPriceOrigByItemPriceOrigId(itemPriceOrig);
		}
		else {
    		delCnt = goodsItemMapper.delItemPriceOrigByItemPriceOrigId(dto.getIlItemPriceOrigId());
		}

    	if(delCnt > 0) {
			// 정상가만 반영하는 품목의 경우, 미래 가격에 대해 정상가를 현행화 해야할 항목에 대해 현행화 한다. S
    		prevItemPriceOrig.setModifyId(modifyId);
//    		List<ItemPriceOrigVo> listItemPriceOrigInfoForDeleteSync = goodsItemMapper.getItemPriceOrigInfoForDeleteSync(prevItemPriceOrig);
    		List<ItemPriceOrigVo> listItemPriceOrigInfoForDeleteSync = goodsItemMapper.getItemPriceOrigInfoForApprovalSync(prevItemPriceOrig);
			if (listItemPriceOrigInfoForDeleteSync != null) {
				for (ItemPriceOrigVo itemPriceOrigInfoForDeleteSync : listItemPriceOrigInfoForDeleteSync) {
					goodsItemMapper.updateItemPriceOrigByItemPriceOrigId(itemPriceOrigInfoForDeleteSync);
				}
			}
			// 정상가만 반영되는 경우, 미래 가격에 대해 정상가를 현행화 해야할 항목에 대해 현행화 한다. E

    		return BaseEnums.Default.SUCCESS;
        }
        else if(delCnt == 0) { // 삭제한 내역이 없을 경우, 성공으로 간주. 예외처리를 하려면 소스 변경.
            return BaseEnums.Default.SUCCESS;
        }
        else {
            throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
        }
    }

    /**
     * 품목가격 승인 요청 처리
     *
     * @param dto ItemPriceApprovalRequestDto
     * @return MessageCommEnum
     */
    protected MessageCommEnum addApprovalItemPrice(ItemPriceApprovalRequestDto dto) throws Exception {
        dto.setApprovalStatus(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
//        if(!dto.getStandardPrice().equals(dto.getStandardPriceChange())){
//            dto.setPriceManageTp(ItemEnums.priceManageType.A.getCode());
//        } else {
//            dto.setPriceManageTp(ItemEnums.priceManageType.R.getCode());
//        }

        // 승인 요청 처리
        if (goodsItemMapper.addItemPriceApprove(dto) > 0) {
            // 승인 요청값 설정
            ApprovalStatusVo approvalVo = ApprovalStatusVo.builder()
                    .taskPk(dto.getIlItemPriceApprId())
                    .apprStat(dto.getApprovalStatus())
                    .apprSubUserId(dto.getApprovalSubUserId())
                    .apprUserId(dto.getApprovalUserId())
                    .build();
            if (this.addItemPriceStatusHistory(approvalVo) > 0) { // 승인상태 이력 저장
                return BaseEnums.Default.SUCCESS;
            }
            else {
    	        throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
            }
        }
	    else {
	        throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
	    }
    }

    /**
     * 품목가격 폐기 처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putDisposalApprovalItemPrice(ApprovalStatusVo approvalVo) throws Exception {
    	if(goodsItemMapper.putDisposalApprovalItemPrice(approvalVo) > 0
    			&& this.addItemPriceStatusHistory(approvalVo) > 0 ) {
    		return BaseEnums.Default.SUCCESS;
    	}else {
    		throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
    	}
    }

    /**
     * 품목가격 반려사유 조회
     *
     * @param ilItemPriceApprId Long
     * @return String
     */
    protected String getApprovalDeniedInfo(Long ilItemPriceApprId) {
        return goodsItemMapper.getApprovalDeniedInfo(ilItemPriceApprId);
    }

    /**
     * @Desc 마스터 품목 리스트 조회
     * @param MasterItemListRequestDto : 마스터 품목 리스트 검색 조건 request dto
     * @return Page<MasterItemListVo> : 마스터 품목 리스트 검색 결과 목록 ( 페이지네이션 적용 )
     */
    protected Page<MasterItemListVo> getItemGoodsPackageList(MasterItemListRequestDto masterItemListRequestDto) {

        PageMethod.startPage(masterItemListRequestDto.getPage(), masterItemListRequestDto.getPageSize());

        return goodsItemMapper.getItemGoodsPackageList(masterItemListRequestDto);

    }

    /**
	 * 품목수정 승인 목록 조회
	 *
	 * @param ApprovalItemRegistRequestDto
	 * @return ItemApprovalResponseDto
	 */
    protected ItemApprovalResponseDto getApprovalItemClientList(ApprovalItemRegistRequestDto approvalItemRequestDto) {
        // 권한 설정
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
		approvalItemRequestDto.setListAuthSupplierId(listAuthSupplierId);
		approvalItemRequestDto.setListAuthWarehouseId(listAuthWarehouseId);

    	List<String> ilItemCdArray = this.getSearchKeyToSearchKeyList(approvalItemRequestDto.getIlItemCode(), Constants.ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS);
		List<String> approvalStatusArray = this.getSearchKeyToSearchKeyList(approvalItemRequestDto.getSearchApprovalStatus(), Constants.ARRAY_SEPARATORS);
		List<String> searchMasterItemTypeArray = this.getSearchKeyToSearchKeyList(approvalItemRequestDto.getMasterItemType(), Constants.ARRAY_SEPARATORS);

		approvalItemRequestDto.setIlItemCodeArray(ilItemCdArray);
		approvalItemRequestDto.setApprovalStatusArray(approvalStatusArray);
		approvalItemRequestDto.setMasterItemTypeArray(searchMasterItemTypeArray);
		approvalItemRequestDto.setApprKindType(ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode());

		PageMethod.startPage(approvalItemRequestDto.getPage(), approvalItemRequestDto.getPageSize());
		Page<ItemApprovalResultVo> resultVoPage = goodsItemMapper.getApprovalItemRegistList(approvalItemRequestDto);

      	return ItemApprovalResponseDto.builder()
                                      .total(resultVoPage.getTotal())
                                      .rows(resultVoPage)
                                      .build();
    }

    /**
     * 품목수정 승인 요청철회
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putCancelRequestApprovalItemClient(ApprovalStatusVo approvalVo) throws Exception {
    	if(goodsItemMapper.putCancelRequestApprovalItemRegist(approvalVo) > 0
    			&& this.addItemRegistStatusHistory(approvalVo) > 0 ) {
    		return BaseEnums.Default.SUCCESS;
    	}else {
    		throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
    	}
    }

    /**
     * 품목수정 승인처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putApprovalProcessItemClient(ApprovalStatusVo approvalVo) throws Exception {
    	if(goodsItemMapper.putApprovalProcessItemRegist(approvalVo) > 0
    			&& this.addItemRegistStatusHistory(approvalVo) > 0 ) {
    		return BaseEnums.Default.SUCCESS;
    	}else {
    		throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
    	}
    }

    protected ItemApprovalPriceVo getApprovalItemPriceChangeNowInfo(ApprovalStatusVo approvalVo) {
        return goodsItemMapper.getApprovalItemPriceChangeNowInfo(approvalVo);
    }

    /**
     * @Desc 검색키 -> 검색키 리스트 변환
     *       검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
     * @param searchKey
     * @param splitKey
     * @return List<String>
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList();

        if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                                       .map(String::trim)
									   .filter( x -> StringUtils.isNotEmpty(x) )
                                       .collect(Collectors.toList()));
        }

        return searchKeyList;
    }

}
