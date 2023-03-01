package kr.co.pulmuone.v1.api.ezadmin.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import kr.co.pulmuone.v1.api.ezadmin.dto.EZAdminRequestDto;
import kr.co.pulmuone.v1.api.ezadmin.dto.EZAdminResponseDefaultDto;
import kr.co.pulmuone.v1.api.ezadmin.dto.vo.EZAdminEtcInfoVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;


/**
* <PRE>
* Forbiz Korea
* EZAdmin Api
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0		20201218		박승현              최초작성
*
* =======================================================================
* </PRE>
*
*/
@Service
public class EZAdminBizImpl implements EZAdminBiz {

    @Autowired
    EZAdminService ezAdminService;

    /**
     * @Desc EZ Admin 기타정보 조회
     * @param searchType
     * @return EZAdminResponseDefaultDto
     */
    @Override
    public ApiResult<?> getEtcInfo(String searchType){
    	MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

    	//Guide.DTO, VO 로 넘어왔을 경우 DTO, VO >> 치환 Util 사용>> MultiValueMap
//    	EZAdminRequestDto reqDTO = new EZAdminRequestDto();
//    	ObjectMapper objectMapper = new ObjectMapper();
//    	Map<String, String> map = objectMapper.convertValue(reqDTO, new TypeReference<Map<String, String>>(){});
//    	paramMap.setAll(map);

    	//Guide.각 API action 값 입력
    	paramMap.add("action", ApiEnums.EZAdminApiAction.GET_ETC_INFO.getCode());
    	paramMap.add("search_type", searchType);

    	//Guide.각 API 리턴항목에 맞는 Vo 생성 후 처리
    	ApiResult<?> result = ezAdminService.get(paramMap, EZAdminEtcInfoVo.class);

    	//Guide. 각 분기 별 처리 로직은 없는 경우 생략
    	if(result.getCode().equals(ApiResult.success().getCode())) {
    		EZAdminResponseDefaultDto dto = (EZAdminResponseDefaultDto)result.getData();

    		//Guide.각 API 리턴항목에 맞는 Vo 생성 후 처리
    		List<EZAdminEtcInfoVo> etcInfoList = (List<EZAdminEtcInfoVo>)dto.getData();
    		if(CollectionUtils.isNotEmpty(etcInfoList)) dto.setTotal(etcInfoList.size()+"");
    		//처리 로직
    	}else if(result.getCode().equals(ApiResult.fail().getCode())) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.HTTP_STATUS_FAIL)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.RESPONSEBODY_NO_DATA)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.JSON_PARSING_ERROR)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.RESPONSE_FAIL)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.NO_DATA)) {
    		//처리 로직
    	}else {
    		//처리 로직
    	}

        return result;
    }

    /**
     * @Desc EZ Admin 주문 조회
     * @param Guide. 각 API 조회 항목에 맞는 RequestDTO 생성 후 처리
     * @param reqDTO
     * @return EZAdminResponseDefaultDto
     */
    @Override
    public ApiResult<?> getOrderInfo(EZAdminRequestDto reqDTO){

    	MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

    	//Guide.DTO, VO 로 넘어왔을 경우 DTO, VO >> 치환 Util 사용>> MultiValueMap
//    	EZAdminRequestDto reqDTO = new EZAdminRequestDto();
//    	ObjectMapper objectMapper = new ObjectMapper();
//    	Map<String, String> map = objectMapper.convertValue(reqDTO, new TypeReference<Map<String, String>>(){});
//    	paramMap.setAll(map);

    	//Guide.각 API action 값 입력
    	paramMap.add("action", ApiEnums.EZAdminApiAction.GET_ORDER_INFO.getCode());

    	//Guide.각 API 리턴항목에 맞는 Vo 생성 후 처리
    	ApiResult<?> result = ezAdminService.get(paramMap, EZAdminEtcInfoVo.class);

    	//Guide. 각 분기 별 처리 로직은 없는 경우 생략
    	if(result.getCode().equals(ApiResult.success().getCode())) {
    		EZAdminResponseDefaultDto dto = (EZAdminResponseDefaultDto)result.getData();

    		//Guide.각 API 리턴항목에 맞는 Vo 생성 후 처리
    		List<EZAdminEtcInfoVo> etcInfoList = (List<EZAdminEtcInfoVo>)dto.getData();
    		//처리 로직
    	}else if(result.getCode().equals(ApiResult.fail().getCode())) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.HTTP_STATUS_FAIL)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.RESPONSEBODY_NO_DATA)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.JSON_PARSING_ERROR)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.RESPONSE_FAIL)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.NO_DATA)) {
    		//처리 로직
    	}else {
    		//처리 로직
    	}

        return result;
    }

    /**
     * @Desc EZ Admin 답변 입력
     * @param Guide. 각 API 답변 항목에 맞는 RequestDTO 생성 후 처리
     * @param reqDTO
     * @return EZAdminResponseDefaultDto
     */
    @Override
    public ApiResult<?> setAutoCsCyncAnswer(EZAdminRequestDto reqDTO){

    	MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

    	//Guide.DTO, VO 로 넘어왔을 경우 DTO, VO >> 치환 Util 사용>> MultiValueMap
//    	EZAdminRequestDto reqDTO = new EZAdminRequestDto();
//    	ObjectMapper objectMapper = new ObjectMapper();
//    	Map<String, String> map = objectMapper.convertValue(reqDTO, new TypeReference<Map<String, String>>(){});
//    	paramMap.setAll(map);

    	//Guide.각 API action 값 입력
    	paramMap.add("action", ApiEnums.EZAdminApiAction.SET_AUTO_CS_SYNC_DATA.getCode());
		paramMap.add("seq", String.valueOf(reqDTO.getSeq()));
		paramMap.add("answer", reqDTO.getAnswer());

    	//Guide.각 API 리턴항목에 맞는 Vo 생성 후 처리
    	ApiResult<?> result = ezAdminService.set(paramMap);

    	//Guide. 각 분기 별 처리 로직은 없는 경우 생략
    	if(result.getCode().equals(ApiResult.success().getCode())) {
		EZAdminResponseDefaultDto dto = (EZAdminResponseDefaultDto)result.getData();

		//Guide.각 API 리턴항목에 맞는 Vo 생성 후 처리
		List<EZAdminEtcInfoVo> etcInfoList = (List<EZAdminEtcInfoVo>)dto.getData();
    		//처리 로직
    	}else if(result.getCode().equals(ApiResult.fail().getCode())) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.HTTP_STATUS_FAIL)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.RESPONSEBODY_NO_DATA)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.JSON_PARSING_ERROR)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.RESPONSE_FAIL)) {
    		//처리 로직
    	}else if(result.getCode().equals(ApiEnums.Default.NO_DATA)) {
    		//처리 로직
    	}else {
    		//처리 로직
    	}

        return result;
    }

}
