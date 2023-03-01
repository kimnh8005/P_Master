package kr.co.pulmuone.v1.comm.util.inicis;

import kr.co.pulmuone.v1.comm.util.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URLEncoder;

@Slf4j
@Component
public class AccountValidation extends RestTemplateUtil {

    @Value("${inicis.account.basic.mid}")
    private String MID;

    public boolean accountNameValidation(String bankCode, String accountNumber, String accountName) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

        String url = "https://iniweb-api.inicis.com/DefaultWebApp/service/acct_cfrm/inicis.jsp";

        ResponseEntity<String> responseEntity = null;

        // 예금주 글자수 10자로 제한(HGRM-10089)
        if(StringUtils.isNotEmpty(accountName)){
            accountName = accountName.replaceAll("\\s", "").toUpperCase();
        }

        try {
            URI uri = URI.create(url + "?" + "banksett="+ bankCode + "&noacct=" + accountNumber + "&nmcomp=" + URLEncoder.encode(accountName, "EUC-KR") + "&mid=" + MID);
            responseEntity = this.post(uri, entity, String.class);
        } catch (Exception e) {
            log.error("=========Inicis siteVerify api===={}", e.getMessage());
        }

        Document doc = Jsoup.parse(responseEntity.getBody());
        String str = doc.text();

        if (str.contains("틀립니다") || str.contains("계좌번호") || str.contains("외부은행코드") || str.contains("확인하여")) {
            return false;
        }

        String[] arr = str.split(" ");

        if(accountName.length() < 10 && !arr[5].equals(accountName)) {
        	return false;
        } else if(accountName.length() > 9 && !accountName.startsWith(arr[5])) {
            return false;
        }

        return true;
    }
}
