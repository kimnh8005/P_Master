package kr.co.pulmuone.v1.comm.util.asis;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import kr.co.pulmuone.v1.comm.util.RestTemplateUtil;
import kr.co.pulmuone.v1.comm.util.asis.dto.SearchCustomerDeliveryDto;
import kr.co.pulmuone.v1.comm.util.asis.dto.SearchCustomerDeliveryListResponseDto;
import kr.co.pulmuone.v1.comm.util.asis.dto.SearchCustomerRsrvTotalResponseDto;
import kr.co.pulmuone.v1.comm.util.asis.dto.UserInfoCheckResponseDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AsisUserApiUtil extends RestTemplateUtil
{

	@Value("${asis.user.authorizationKey}")
	private String AUTHORIZATION_KEY;

	@Value("${asis.user.userInfoCheck.requestUrl}")
	private String USER_INFO_CHECK_REQUEST_URL;

	@Value("${asis.user.searchCustDelvList.requestUrl}")
	private String SEARCH_CUSTOMER_DELIVERY_LIST_REQUEST_URL;

	@Value("${asis.user.userSearchId.requestUrl}")
	private String USER_SEARCH_ID_REQUEST_URL;

	@Value("${asis.user.searchCustomerRsrvTotal.requestUrl}")
	private String SEARCH_CUSTOMER_RSRV_TOTAL_REQUEST_URL;

	@Value("${asis.user.minusCustomerRsrv.requestUrl}")
	private String MINUS_CUSTOMER_RSRV_REQUEST_URL;

	@Value("${database.encryption.key}")
	private String ENCRYPTION_KEY;

	/**
	 *  [API 01] 통합몰 회원가입시 기존(AS-IS) 사용자 아이디/비밀번호 체크 API
	 */
	public UserInfoCheckResponseDto userInfoCheck(String id, String password)
	{
		UserInfoCheckResponseDto userInfoCheckResponseDto = new UserInfoCheckResponseDto();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization-Key", AUTHORIZATION_KEY);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("USER_ID", id);
		map.add("USER_PWD", pEncrypt(password));
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> responseEntity = null;
		try
		{
			log.info("=========AsisUserApiUtil userInfoCheck USER_INFO_CHECK_REQUEST_URL ===={}", USER_INFO_CHECK_REQUEST_URL);
			responseEntity = this.post(USER_INFO_CHECK_REQUEST_URL, entity, String.class);
		}
		catch (Exception e)
		{
			log.error("=========AsisUserApiUtil userInfoCheck api===={}", e.getMessage());
			userInfoCheckResponseDto.setResultCode("9");
			return userInfoCheckResponseDto;
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonode = null;
		try
		{
			log.info("=========AsisUserApiUtil userInfoCheck api response===={}", responseEntity.getBody());
			jsonode = mapper.readTree(responseEntity.getBody());
		}
		catch (Exception e)
		{
			log.error("=========AsisUserApiUtil userInfoCheck josn===={}", e.getMessage());
			userInfoCheckResponseDto.setResultCode("9");
			return userInfoCheckResponseDto;
		}

		String resultCode = jsonode.get("RESULT_CODE").getValueAsText();
		String resultMessage = jsonode.get("RESULT_MSG").getValueAsText();
		String customerNumber = jsonode.get("CUSTOMER_NUM").getValueAsText();

		userInfoCheckResponseDto.setResultCode(resultCode);
		userInfoCheckResponseDto.setResultMessage(resultMessage);
		userInfoCheckResponseDto.setCustomerNumber(customerNumber);

		return userInfoCheckResponseDto;
	}

	/**
	 *  비밀번호 암호화(통합회원용)
	 */
	protected String pEncrypt(String plaintext)
	{
		String strSRCData = null;
		String strENCData = null;
		strSRCData = plaintext;

		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] bytData = strSRCData.getBytes();
			md.update(bytData);
			byte[] digest = md.digest();

			Encoder encoder = Base64.getEncoder();
			byte[] encodedBytes = encoder.encode(digest);
			strENCData = new String(encodedBytes);
		}
		catch (NoSuchAlgorithmException var8)
		{
			strENCData = null;
			var8.printStackTrace();
		}
		catch (Exception var9)
		{
			strENCData = null;
			var9.printStackTrace();
		}

		return strENCData;
	}

	/**
	 *  비밀번호 암호화(임직원 회원용)
	 */
	protected String pEncryptEmp(String planText)
	{
		String strENCData = null;

		if(planText == null || planText.trim().length() == 0) return "";

		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(planText.getBytes());
			byte byteData[] = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<byteData.length;i++) {
				String hex=Integer.toHexString(0xff & byteData[i]);
				if(hex.length()==1){
					hexString.append('0');
				}
				hexString.append(hex);
			}

			strENCData = hexString.toString();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}

		return strENCData;
	}


	/**
	 *  [API 02] 통합몰 회원가입시 기존 아이디 선점을 위한 아이디 존재여부 확인 API
	 */
	public boolean userSerachId(String id)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization-Key", AUTHORIZATION_KEY);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("USER_ID", id);
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> responseEntity = null;
		try
		{
			log.info("=========AsisUserApiUtil userSerachId USER_SEARCH_ID_REQUEST_URL ===={}", USER_SEARCH_ID_REQUEST_URL);
			responseEntity = this.post(USER_SEARCH_ID_REQUEST_URL, entity, String.class);
		}
		catch (Exception e)
		{
			log.error("=========AsisUserApiUtil userSerachId api===={}", e.getMessage());
			return false;
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonode = null;
		try
		{
			log.info("=========AsisUserApiUtil userSerachId api response===={}", responseEntity.getBody());
			jsonode = mapper.readTree(responseEntity.getBody());
		}
		catch (Exception e)
		{
			log.error("=========AsisUserApiUtil userSerachId josn===={}", e.getMessage());
			return false;
		}

		String resultCode = jsonode.get("RESULT_CODE").getValueAsText();

		if (resultCode.equals("0"))
		{ // 없음
			return false;
		}
		else if (resultCode.equals("1"))
		{ // 존재
			return true;
		}
		else
		{ // 기타 에러는
			return false;
		}
	}


	/**
	 *  [API 03] 통합몰 회원가입시 AS-IS 회원 배송지 정보 조회 API
	 */
	public SearchCustomerDeliveryListResponseDto searchCustomerDeliveryList(String customer_num)
	{
		SearchCustomerDeliveryListResponseDto searchCustomerDeliveryListResponseDto = new SearchCustomerDeliveryListResponseDto();
		List<SearchCustomerDeliveryDto> searchCustomerDeliveryList = new ArrayList<>();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization-Key", AUTHORIZATION_KEY);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("customer_num", customer_num);
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> responseEntity = null;
		try
		{
			log.info("=========AsisUserApiUtil searchCustomerDeliveryList SEARCH_CUSTOMER_DELIVERY_LIST_REQUEST_URL ===={}", SEARCH_CUSTOMER_DELIVERY_LIST_REQUEST_URL);
			responseEntity = this.post(SEARCH_CUSTOMER_DELIVERY_LIST_REQUEST_URL, entity, String.class);
		}
		catch (Exception e)
		{
			log.error("=========AsisUserApiUtil searchCustomerDeliveryList api===={}", e.getMessage());
			return searchCustomerDeliveryListResponseDto;
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonode = null;
		try
		{
			log.info("=========AsisUserApiUtil searchCustomerDeliveryList api response===={}", responseEntity.getBody());
			jsonode = mapper.readTree(responseEntity.getBody());
		}
		catch (Exception e)
		{
			log.error("=========AsisUserApiUtil searchCustomerDeliveryList josn===={}", e.getMessage());
			return searchCustomerDeliveryListResponseDto;
		}

		if(jsonode.get("data").isArray()) {

			for(JsonNode node : jsonode.get("data")) {

				// 개인정보 보호정책으로 인한 데이타 암복호화 로직 추가. 최용호(2020.11.17)
				SearchCustomerDeliveryDto searchCustomerDeliveryDto = new SearchCustomerDeliveryDto();
				searchCustomerDeliveryDto.setShippingNm(StringUtil.decryptStr(ENCRYPTION_KEY, node.get("shippingNm").getValueAsText()));
				searchCustomerDeliveryDto.setReceiverNm(StringUtil.decryptStr(ENCRYPTION_KEY, node.get("receiverNm").getValueAsText()));
				searchCustomerDeliveryDto.setReceiverMo((node.get("receiverMo") == null ? null : StringUtil.decryptStr(ENCRYPTION_KEY, node.get("receiverMo").getValueAsText()).replaceAll("-", "")));
				searchCustomerDeliveryDto.setReceiverTel((node.get("receiverTel") == null ? null : StringUtil.decryptStr(ENCRYPTION_KEY, node.get("receiverTel").getValueAsText()).replaceAll("-", "")));
				searchCustomerDeliveryDto.setBasicYn(node.get("basicYn").getValueAsText());
				searchCustomerDeliveryDto.setReceiverZipCd(node.get("receiverZipCd").getValueAsText());
				searchCustomerDeliveryDto.setReceiverAddr1(StringUtil.decryptStr(ENCRYPTION_KEY, node.get("receiverAddr1").getValueAsText()));
				searchCustomerDeliveryDto.setReceiverAddr2(StringUtil.decryptStr(ENCRYPTION_KEY, node.get("receiverAddr2").getValueAsText()));
				searchCustomerDeliveryDto.setBuildingCd(node.get("buildingCd").getValueAsText());
				searchCustomerDeliveryList.add(searchCustomerDeliveryDto);
			}
			searchCustomerDeliveryListResponseDto.setData(searchCustomerDeliveryList);
		}
		return searchCustomerDeliveryListResponseDto;
	}

	/**
	 *  [API 03-1] 통합몰 회원가입시 AS-IS 회원 기본 배송지 정보 조회 API
	 */
	public SearchCustomerDeliveryDto searchCustomerBasicDelivery(String customer_num) {
		SearchCustomerDeliveryDto searchCustomerBasicDeliveryDto = new SearchCustomerDeliveryDto();

		// AS-IS 회원 배송지 정보 조회 API 호출
		SearchCustomerDeliveryListResponseDto customerDeliveryList =  searchCustomerDeliveryList(customer_num);

		if(CollectionUtils.isNotEmpty(customerDeliveryList.getData())) {
			for(SearchCustomerDeliveryDto deliveryDto : customerDeliveryList.getData()) {
				if("Y".equals(deliveryDto.getBasicYn())) {
					return deliveryDto;
				}
			}
		}

		return searchCustomerBasicDeliveryDto;
	}


	/**
	 *  [API 04] 통합몰 회원가입시 AS-IS  풀무원 적립금 조회 API
	 */
	public SearchCustomerRsrvTotalResponseDto searchCustomerRsrvTotal(String customer_num, String employeeYn, String password)
	{
		SearchCustomerRsrvTotalResponseDto searchCustomerRsrvTotalResponseDto = new SearchCustomerRsrvTotalResponseDto();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization-Key", AUTHORIZATION_KEY);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("customer_num", customer_num);
		map.add("employee_yn", employeeYn);
		map.add("password", pEncryptEmp(password));
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> responseEntity = null;
		try
		{
			log.info("=========AsisUserApiUtil searchCustomerRsrvTotal SEARCH_CUSTOMER_RSRV_TOTAL_REQUEST_URL ===={}", SEARCH_CUSTOMER_RSRV_TOTAL_REQUEST_URL);
			responseEntity = this.post(SEARCH_CUSTOMER_RSRV_TOTAL_REQUEST_URL, entity, String.class);
		}
		catch (Exception e)
		{
			log.error("=========AsisUserApiUtil searchCustomerRsrvTotal api===={}", e.getMessage());
			return searchCustomerRsrvTotalResponseDto;
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonode = null;
		try
		{
			log.info("=========AsisUserApiUtil searchCustomerRsrvTotal api response===={}", responseEntity.getBody());
			jsonode = mapper.readTree(responseEntity.getBody());
		}
		catch (Exception e)
		{
			log.error("=========AsisUserApiUtil searchCustomerRsrvTotal josn===={}", e.getMessage());
			return searchCustomerRsrvTotalResponseDto;
		}

		if(jsonode.get("success").getBooleanValue()) {
			int pulmuoneShopPoint = jsonode.get("data").get("PULSHOP_TOT_REMAIN_PRICE").getValueAsInt();
			int oragPoint = jsonode.get("data").get("ORGA_TOT_REMAIN_PRICE").getValueAsInt();
			searchCustomerRsrvTotalResponseDto.setPulmuoneShopPoint(pulmuoneShopPoint);
			searchCustomerRsrvTotalResponseDto.setOrgaPoint(oragPoint);
		}

		searchCustomerRsrvTotalResponseDto.setSuccess(jsonode.get("success").getBooleanValue());
		searchCustomerRsrvTotalResponseDto.setStatus(jsonode.get("status").getValueAsInt());
		searchCustomerRsrvTotalResponseDto.setCode(jsonode.get("code").getValueAsInt());
		searchCustomerRsrvTotalResponseDto.setMessage(jsonode.get("message").getValueAsText());

		return searchCustomerRsrvTotalResponseDto;
	}



	/**
	 *  [API 05] 통합몰 회원가입시 AS-IS 풀무원 적립금 소멸 API
	 */
	public boolean minusCustomerRsrv(String isOrga, String customer_num, int cust_rsrv_total)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization-Key", AUTHORIZATION_KEY);

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("is_orga", isOrga);
		map.add("customer_num", customer_num);
		map.add("cust_rsrv_enc", StringUtil.encryptStr(ENCRYPTION_KEY, String.valueOf(cust_rsrv_total)));
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
		ResponseEntity<String> responseEntity = null;
		try
		{
			log.info("=========AsisUserApiUtil minusCustomerRsrv MINUS_CUSTOMER_RSRV_REQUEST_URL ===={}", MINUS_CUSTOMER_RSRV_REQUEST_URL);
			responseEntity = this.post(MINUS_CUSTOMER_RSRV_REQUEST_URL, entity, String.class);
		}
		catch (Exception e)
		{
			log.error("=========AsisUserApiUtil minusCustomerRsrv api===={}", e.getMessage());
			return false;
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonode = null;
		try
		{
			log.info("=========AsisUserApiUtil minusCustomerRsrv api response===={}", responseEntity.getBody());
			jsonode = mapper.readTree(responseEntity.getBody());
		}
		catch (Exception e)
		{
			log.error("=========AsisUserApiUtil minusCustomerRsrv josn===={}", e.getMessage());
			return false;
		}

		return jsonode.get("success").getBooleanValue();
	}
}
