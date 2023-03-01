package kr.co.pulmuone.v1.api.watson.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import kr.co.pulmuone.v1.comm.util.EcsRestTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.pulmuone.v1.api.ecs.dto.vo.CsEcsCodeVo;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.RestTemplateUtil;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* watson Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 16.    천혜현         최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class WatsonService {

    @Autowired
    EcsRestTemplateUtil restTemplateUtil;

    private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

    @Value("${watson.key}")
    private String key;

    @Value("${watson.url}")
    private String url;


    /**
     * @Desc watson classifier_id 호출 API
     * @param question
     * @param goodsName
     * @return CsEcsCodeVo
     */
    protected CsEcsCodeVo getClassifierIdCall(String question, String goodsName) {
    	CsEcsCodeVo csEcsCode = new CsEcsCodeVo();
    	String apiKey = "apikey:"+key;
    	String encodedKey = new String(Base64.getEncoder().encode(apiKey.getBytes()));

        //헤더 정보 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Accept", "application/json");
        headers.add("Authorization", "Basic " + encodedKey);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplateUtil.getEcs(url, requestEntity);

        //통신 결과 체크
        if(responseEntity.getStatusCode() != HttpStatus.OK) {
        	return csEcsCode;
        }

        String responseBody = responseEntity.getBody();
        JsonNode jsonNode;

        try {
			jsonNode = OBJECT_MAPPER.readTree(responseBody);
			String classifierId = jsonNode.get("classifiers").get(0).get("classifier_id").textValue();

			csEcsCode = counselGubunCall(classifierId,question, goodsName);

		} catch (Exception e) {
			return csEcsCode;
		}

        return csEcsCode;
    }

    /**
     * @Desc watson API 상담분류 호출
     * @param classifierId
     * @param question
     * @param goodsName
     * @return CsEcsCodeVo
     */
    protected CsEcsCodeVo counselGubunCall(String classifierId, String question, String goodsName) {
    	CsEcsCodeVo csEcsCode = new CsEcsCodeVo();
    	String apiKey = "apikey:"+key;
    	String encodedKey = new String(Base64.getEncoder().encode(apiKey.getBytes()));

    	//WATSON API 호출 시 보낼 Data 가공 (문의글 - 엔터, 줄바꿈 제외)
    	String counselDesc1 = question.replace("&","_"); // & 제거
    	String counselDesc2 = counselDesc1.replace("\n", ""); // 행바꿈 제거
    	String counselDesc = counselDesc2.replace("\r",""); // 엔터 제거

    	//text Param으로 보낼 최종 Data (상품명. 문의글)
    	String wData = counselDesc;
    	if(StringUtils.isNotEmpty(goodsName)) {
    		wData = goodsName +"."+ counselDesc;
    	}

    	//헤더 정보 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Accept", "application/json");
        headers.add("Authorization", "Basic " + encodedKey);

        String encodeUrl = "";
        try{
        	encodeUrl = URLEncoder.encode(wData, "UTF-8")
				        			.replaceAll("\\+", "%20")
				                    .replaceAll("\\%21", "!")
				                    .replaceAll("\\%27", "'")
				                    .replaceAll("\\%28", "(")
				                    .replaceAll("\\%29", ")")
				                    .replaceAll("\\%7E", "~")
				                    .replaceAll("\\%3A", ":");
        } catch (UnsupportedEncodingException e){
        	return csEcsCode;
        }

        url +="/"+classifierId + "/classify?text="+encodeUrl;

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplateUtil.getEcs(url, requestEntity);

        //통신 결과 체크
        if(responseEntity.getStatusCode() != HttpStatus.OK) {
        	return csEcsCode;
        }

        String responseBody = responseEntity.getBody();
        JsonNode jsonNode;

        try {
			jsonNode = OBJECT_MAPPER.readTree(responseBody);
			String className = jsonNode.get("classes").get(0).get("class_name").textValue();

			csEcsCode.setHdBcode(className.split("-")[0]);
			csEcsCode.setHdScode(className.split("-")[1]);
			csEcsCode.setClaimGubun(className.split("-")[2]);

		} catch (Exception e) {
			return csEcsCode;
		}

    	return csEcsCode;
    }

}
