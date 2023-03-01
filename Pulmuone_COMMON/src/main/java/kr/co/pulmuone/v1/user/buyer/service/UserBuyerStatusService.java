package kr.co.pulmuone.v1.user.buyer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.mapper.user.buyer.BuyerStatusMapper;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.BuyerStatusResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerStatusHistoryListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerStopHistoryListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerStopListResultVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


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
 *  1.0    20200623    	  천혜현           최초작성
 *  1.1    20210115        최윤지           자동메일 템플릿 작성
 * =======================================================================
 * </PRE>
 */

@Service
public class UserBuyerStatusService {

	@Autowired
	private BuyerStatusMapper buyerStatusMapper;

	/**
	 * 정지회원 리스트조회
	 * @param getUserStopListRequestDto
	 * @return GetUserStopListResponseDto
	 * @throws Exception
	 */
	protected GetUserStopListResponseDto getBuyerStopList(GetUserStopListRequestDto getUserStopListRequestDto) throws Exception {
		if(StringUtils.isNotEmpty(getUserStopListRequestDto.getCondiValue())) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(getUserStopListRequestDto.getCondiValue(), "\n|,");
			while(st.hasMoreElements()) {
				String object = (String)st.nextElement();
				array.add(object);
			}
			getUserStopListRequestDto.setCondiValueArray(array);
		}

//		int total = buyerStatusMapper.getBuyerStopListCount(getUserStopListRequestDto);	// total
		PageMethod.startPage(getUserStopListRequestDto.getPage(), getUserStopListRequestDto.getPageSize());
		Page<GetBuyerStopListResultVo> rows = buyerStatusMapper.getBuyerStopList(getUserStopListRequestDto);	// rows

		return GetUserStopListResponseDto.builder()
				.total((int) rows.getTotal())
				.rows(rows.getResult())
				.build();
	}

	/**
	 * 정지회원 이력 로그 목록 조회
	 * @param getUserStopHistoryRequestDto
	 * @return GetUserStopHistoryResponseDto
	 * @throws Exception
	 */
	protected GetUserStopHistoryResponseDto getBuyerStopLog(GetUserStopHistoryRequestDto getUserStopHistoryRequestDto) throws Exception {
		GetUserStopHistoryResponseDto result = new GetUserStopHistoryResponseDto();

		result.setRows(buyerStatusMapper.getBuyerStopLog(getUserStopHistoryRequestDto));

		return result;
	}


	/**
	 * 정지회원 이력 리스트조회
	 * @param getUserStopHistoryListRequestDto
	 * @return GetUserStopHistoryListResponseDto
	 * @throws Exception
	 */
	protected GetUserStopHistoryListResponseDto getBuyerStopHistoryList(GetUserStopHistoryListRequestDto getUserStopHistoryListRequestDto) throws Exception {
		if(StringUtils.isNotEmpty(getUserStopHistoryListRequestDto.getCondiValue())) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(getUserStopHistoryListRequestDto.getCondiValue(), "\n|,");
			while(st.hasMoreElements()) {
				String object = (String)st.nextElement();
				array.add(object);
			}
			getUserStopHistoryListRequestDto.setCondiValueArray(array);
		}

		//int total = buyerStatusMapper.getBuyerStopHistoryListCount(getUserStopHistoryListRequestDto);	// total
		PageMethod.startPage(getUserStopHistoryListRequestDto.getPage(), getUserStopHistoryListRequestDto.getPageSize());
		Page<GetBuyerStopHistoryListResultVo> rows = buyerStatusMapper.getBuyerStopHistoryList(getUserStopHistoryListRequestDto);	// rows

		return GetUserStopHistoryListResponseDto.builder()
				.total((int) rows.getTotal())
				.rows(rows.getResult())
				.build();


	}
	/**
	 * 정지회원 이력 리스트조회
	 * @param getBuyerStatusHistoryListRequestDto
	 * @return GetUserStopHistoryListResponseDto
	 * @throws Exception
	 */
	protected GetBuyerStatusHistoryListResponseDto getBuyerStatusHistoryList(GetBuyerStatusHistoryListRequestDto getBuyerStatusHistoryListRequestDto) throws Exception {
		Page<GetBuyerStatusHistoryListResultVo> rows = buyerStatusMapper.getBuyerStatusHistoryList(getBuyerStatusHistoryListRequestDto);	// rows

		return GetBuyerStatusHistoryListResponseDto.builder()
				.rows(rows.getResult())
				.build();
	}

	/**
	 * 회원상태 정지 설정
	 * @param putBuyerStopRequestDto
	 * @return PutBuyerStopResponseDto
	 * @throws Exception
	 */
	protected PutBuyerStopResponseDto putBuyerStop(PutBuyerStopRequestDto putBuyerStopRequestDto) throws Exception {
		PutBuyerStopResponseDto result = new PutBuyerStopResponseDto();

		PutBuyerStopParamDto putBuyerStopParamDto = PutBuyerStopParamDto.builder()
													.urUserId(putBuyerStopRequestDto.getUrUserId())
													.build();
		buyerStatusMapper.putBuyerStop(putBuyerStopParamDto);

		AddBuyerStatusLogParamDto addBuyerStatusLogParamDto = AddBuyerStatusLogParamDto.builder()
															.urUserId(putBuyerStopRequestDto.getUrUserId())
															.reason(putBuyerStopRequestDto.getReason())
															.status("BUYER_STATUS.STOP") // UR_BUYER_STATUS_LOG의 STATUS = BUYER_STATUS.STOP (정지회원)
															.build();
		buyerStatusMapper.addBuyerStatusLog(addBuyerStatusLogParamDto);

		result.setUrUserId(putBuyerStopRequestDto.getUrUserId());

		return result;
	}

	/**
	 * @Desc 회원상태 결과 조회 (정상 <-> 정지)
	 * @param urUserId
	 * @return BuyerStatusResultVo
	 */
	protected BuyerStatusResultVo getBuyerStatusConvertInfo(String urUserId) {
		return buyerStatusMapper.getBuyerStatusConvertInfo(urUserId);
	}

	/**
	 * 회원상태 정상 설정
	 * @param putBuyerNormalRequestDto
	 * @return PutBuyerNormalResponseDto
	 * @throws Exception
	 */
	protected PutBuyerNormalResponseDto putBuyerNormal(PutBuyerNormalRequestDto putBuyerNormalRequestDto) throws Exception {
		PutBuyerNormalResponseDto result = new PutBuyerNormalResponseDto();
		String attachmentPath = "";
		String attachmentOriginName = "";

		PutBuyerNormalParamDto putBuyerNormalParamDto = PutBuyerNormalParamDto.builder()
													  .urUserId(putBuyerNormalRequestDto.getUrUserId())
													  .build();
		buyerStatusMapper.putBuyerNormal(putBuyerNormalParamDto);

		if(putBuyerNormalRequestDto.getAddFileList() != null) {
			attachmentPath = putBuyerNormalRequestDto.getAddFileList().stream()
													.findFirst()
													.map(m -> m.getServerSubPath() +  m.getPhysicalFileName())
													.orElseGet(String::new);
			attachmentOriginName = putBuyerNormalRequestDto.getAddFileList().stream()
															.findFirst()
															.map(m -> m.getOriginalFileName())
															.orElseGet(String::new);
		}

		AddBuyerStatusLogParamDto addBuyerStatusLogParamDto = AddBuyerStatusLogParamDto.builder()
															.urUserId(putBuyerNormalRequestDto.getUrUserId())
															.reason(putBuyerNormalRequestDto.getReason())
															.attachmentPath(attachmentPath)
															.attachmentOriginName(attachmentOriginName)
															.urBuyerStatusLogId(putBuyerNormalRequestDto.getUrBuyerStatusLogId())
															.status("BUYER_STATUS.NORMAL")
															.build();

		buyerStatusMapper.addBuyerStatusLog(addBuyerStatusLogParamDto);

		result.setUrUserId(putBuyerNormalRequestDto.getUrUserId());

		return result;
	}

	/**
	 * 마케팅 정보 수신동의 안내 대상자 조회
	 * @return MarketingInfoDto
	 * @throws Exception
	 */
	protected MarketingInfoDto getMarketingInfo(Long urUserId) throws Exception{
		return buyerStatusMapper.getMarketingInfo(urUserId);
	}

}