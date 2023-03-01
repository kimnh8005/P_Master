package kr.co.pulmuone.bos.user.refund.service;

import kr.co.pulmuone.v1.base.service.UrCommonBiz;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.AddBuyerChangeHististoryParamVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.buyer.dto.GetRefundBankResponseDto;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRefundBosServiceImpl implements UserRefundBosService {

	@Autowired
	private UserBuyerBiz userBuyerBiz;

	@Autowired
	private UrCommonBiz urCommonBiz;

	@Override
	@UserMaskingRun(system = "BOS")
	public GetRefundBankResponseDto getRefundBank(CommonGetRefundBankRequestDto dto) throws Exception {
		GetRefundBankResponseDto result = new GetRefundBankResponseDto();

		result.setRows(userBuyerBiz.getRefundBank(dto));

		return result;
	}

	@Override
	public int putRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {
		int result = userBuyerBiz.putRefundBank(dto);

		//회원정보변경 이력 추가
		if(dto.getChangeLogArray().length() > 0){
			List<Map> insertData = StringUtil.convertJsonToList(dto.getChangeLogArray());
			if(!insertData.isEmpty()){
				AddBuyerChangeHististoryParamVo addBuyerChangeHistoryParamVo = AddBuyerChangeHististoryParamVo.builder()
																			.adminId(dto.getUserVo().getUserId())
																			.urUserId(dto.getUrUserId())
																			.insertData(insertData)
																			.DATABASE_ENCRYPTION_KEY(dto.getDATABASE_ENCRYPTION_KEY())
																			.build();
				urCommonBiz.addBuyerChangeHististory(addBuyerChangeHistoryParamVo);
			}
		}

		return result;
	}

	@Override
	public int addRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {
		int result = userBuyerBiz.addRefundBank(dto);

		if(dto.getChangeLogArray().length() > 0){
			List<Map> insertData = StringUtil.convertJsonToList(dto.getChangeLogArray());
			if(!insertData.isEmpty()){
				AddBuyerChangeHististoryParamVo addBuyerChangeHistoryParamVo = AddBuyerChangeHististoryParamVo.builder()
																			.adminId(dto.getUserVo().getUserId())
																			.urUserId(dto.getUrUserId())
																			.insertData(insertData)
																			.DATABASE_ENCRYPTION_KEY(dto.getDATABASE_ENCRYPTION_KEY())
																			.build();
				urCommonBiz.addBuyerChangeHististory(addBuyerChangeHistoryParamVo);
			}
		}
		return result;
	}

	/**
	 * 계좌 유효 인증 체크
	 * @param bankCode String
	 * @param accountNumber String
	 * @param holderName String
	 * @return ApiResult<?>
	 * @throws Exception Exception
	 */
	@Override
	public ApiResult<?> isValidationBankAccountNumber (String bankCode, String accountNumber, String holderName) throws Exception {
		if (!userBuyerBiz.isValidationBankAccountNumber(bankCode, accountNumber, holderName)) {
			return ApiResult.success(false);
		}
		return ApiResult.success(true);
	}

}
