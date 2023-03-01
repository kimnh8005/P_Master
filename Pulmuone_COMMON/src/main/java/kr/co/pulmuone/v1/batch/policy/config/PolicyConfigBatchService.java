package kr.co.pulmuone.v1.batch.policy.config;

import kr.co.pulmuone.v1.comm.mappers.batch.master.policy.PolicyConfigBatchMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Service
public class PolicyConfigBatchService {
    private static final String SERVICE_KEY = "4RmGXs4wsnLZkStUGXTa4Y8ns1w1qvaW2AK43AnDMpsSRhh552gdzffGeL6E02SG3lcRKqvmpw9u%2Bwvmp1OKBQ%3D%3D";

    private final PolicyConfigBatchMapper policyConfigBatchMapper;

    /**
     * 설정정보 조회
     *
     * @param psKey String
     * @return String
     */
    protected String getConfigValue(String psKey) {
        return policyConfigBatchMapper.getConfigValue(psKey);
    }

    /**
     * 공휴일 여부 조회
     *
     * @param nowDate String
     * @return String
     */
    protected String getHolidayYn(String nowDate) {
        return policyConfigBatchMapper.getHolidayYn(nowDate);
    }

    /**
     * 휴일정보 설정
     *
     * @throws IOException
     */
    protected void runSetHoliday() throws IOException {
        // Batch 대상 년월 설정
        LocalDate localDate = LocalDate.now().plusMonths(1);
        String targetYear = String.valueOf(localDate.getYear());
        String targetMonth = localDate.format(DateTimeFormatter.ofPattern("MM"));

        // API 통신 진행
        String apiResult = callHolidayApi(targetYear, targetMonth);

        // API 결과 파싱
        Document doc = Jsoup.parse(apiResult);

        // API 통신 성공일 경우만 다음 작업 진행
        String resultMessage = doc.getElementsByTag("resultMsg").text();
        if (!resultMessage.equals("NORMAL SERVICE.")) {
            return;
        }

        // 기존 휴일정보 삭제
        policyConfigBatchMapper.delHolidayYM(targetYear + targetMonth);

        // 신규 휴일정보 등록
        Elements items = doc.getElementsByTag("item");
        if (items.size() > 0) {
            for (Element item : items) {
//                String holidayName = item.getElementsByTag("dateName").text();    // 휴일명 필요시 활용
                String isHoliday = item.getElementsByTag("isHoliday").text();
                String holidayDate = item.getElementsByTag("locdate").text();

                if (isHoliday.equals("Y")) {
                    String holiday = DateUtil.convertFormatNew(holidayDate, "yyyyMMdd", "yyyy-MM-dd");
                    policyConfigBatchMapper.addHoliday(holiday);
                }
            }
        }
    }

    /**
     * 휴일정보 등록 API 통신
     * https://www.data.go.kr/data/15012690/openapi.do
     *
     * @param year  int
     * @param month int
     * @return String
     * @throws IOException
     */
    private String callHolidayApi(String year, String month) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + SERVICE_KEY); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("solYear", "UTF-8") + "=" + URLEncoder.encode(year, "UTF-8")); /*연*/
        urlBuilder.append("&" + URLEncoder.encode("solMonth", "UTF-8") + "=" + URLEncoder.encode(month, "UTF-8")); /*월*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }

}
