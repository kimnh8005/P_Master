package kr.co.pulmuone.v1.promotion.shoplive.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.mapper.promotion.shoplive.ShopliveMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveInfoRequestDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveInfoResponseDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveRequestDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.vo.ShopliveGoodsVo;
import kr.co.pulmuone.v1.promotion.shoplive.dto.vo.ShopliveInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShopliveEventService {

    @Value("${resource.server.url.mall}")
    private String mallUrl;

    @Value("${resource.server.url.image}")
    private String imageUrl;

    @Value("${shoplive.access_key}")
    private String shopliveAccessKey;

    @Value("${shoplive.secret_key}")
    private String shopliveSecretKey;

    private final ShopliveMapper shopliveMapper;

    private Gson gson = new Gson();

    public ApiResult<?> getShopliveInfo(ShopliveInfoRequestDto dto) throws Exception {
        ShopliveInfoVo shopliveInfoVo = shopliveMapper.getShopliveInfo(dto.getEvShopliveId());
        if(shopliveInfoVo == null) {
            return ApiResult.fail();
        }

        ShopliveInfoResponseDto responseDto = new ShopliveInfoResponseDto(shopliveInfoVo);
        responseDto.setJwtAuthId(getJwtAuthId(dto));
        return ApiResult.success(responseDto);
    }

    private String getJwtAuthId(ShopliveInfoRequestDto dto) {

        if(dto.getUrUserId() == null || dto.getUrUserId() == 0 || StringUtil.isEmpty(Long.toString(dto.getUrUserId()))) {
            return "";
        }

        /*
         * 유효 시간을 12시간으로 설정
         */
        long expiration = System.currentTimeMillis() + 12 * (60 * 60 * 1000);
        // OR
        // long expiration = LocalDateTime.now().plus(12, ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toEpochSecond();

        /*
         * 사용자 아이디
         * 이 아이디를 기준으로 서비스 사용자를 구분하며 최대 255바이트까지 설정할 수 있습니다
         */
        String userId = dto.getLoginId();

        /*
         * 채팅에 노출할 사용자명
         * 금칙어에 해당하는 단어는 별표(*)로 대체되어 표시됩니다
         */
        String name = dto.getUserNm();

        /*
         * 접속자 통계에서 참조할 성별 정보입니다.
         * 남성(m), 여성(f), 기타(n, null, etc.)
         */
        String gender = "f";

        /*
         * 접속자 통계에서 참조할 연령 정보입니다.
         * 임의의 숫자로 입력할 수 있으며 통계에는 5세 단위로 그룹화하여 표시합니다.
         */
        Integer age = 15;

        JwtBuilder builder = Jwts.builder()
                .signWith(SignatureAlgorithm.forName(Constants.SHOPLIVE_ALGORITHM), shopliveSecretKey)
                .setExpiration(new Date(expiration))
                .setIssuedAt(new Date())
                .claim("userId", userId)
                .claim("name", name);
//                .claim("gender", gender)
//                .claim("age", age);

        String jwt = builder.compact();

        log.debug("jwt: " + jwt);

        return jwt;
    }

    private String getJwtAuthHeader() {
        long expiration = System.currentTimeMillis() + 12 * (60 * 60 * 1000);

        JwtBuilder builder = Jwts.builder()
                .signWith(SignatureAlgorithm.forName(Constants.SHOPLIVE_ALGORITHM), shopliveSecretKey)
                .setExpiration(new Date(expiration))
                .setIssuedAt(new Date())
                .claim("accessKey", shopliveAccessKey);

        String jwt = builder.compact();

        log.debug("jwt Auth Header: " + jwt);

        return jwt;
    }

    public void syncShopliveGoodsList() throws Exception {
        /*
            TODO : 상품 조회 후 동기화
         */
        List<ShopliveGoodsVo> syncGoodsList = shopliveMapper.getShopliveGoodsList(); // 동기화할 상품 조회
        List<String> syncGoodsIdList = new ArrayList<>();
        for(ShopliveGoodsVo goodsVo: syncGoodsList) {
            syncGoodsIdList.add(goodsVo.getIlGoodsId());
        }
        String syncResult = postShopliveGoodsSync(syncGoodsList); // 상품 동기화
        Map syncResultMap = gson.fromJson(syncResult, HashMap.class);
        // {results:[], "totalCount":2,"createCount":2,"updateCount":0,"failCount":0}

        String shopliveGoodsResult = getShopliveGoodsList(); // 샵라이브에 등록되어 있는 상품 조회
        // {results:[]}
        Map shopliveGoodsResultMap = gson.fromJson(shopliveGoodsResult, HashMap.class);
//        gson.fromJson(shopliveGoodsResultMap.get("result"));
        List shopliveGoodsList = (List)shopliveGoodsResultMap.get("results");
        List<String> shopliveGoodsSkuList = new ArrayList<>();
        for(Object shopliveGoodsObject: shopliveGoodsList) {
            Map shopliveGoods = (Map)shopliveGoodsObject;
            shopliveGoodsSkuList.add(String.valueOf(shopliveGoods.get("sku").toString()));
        }
        shopliveGoodsSkuList.removeAll(syncGoodsIdList); // 샵라이브에 등록되어 있는 상품목록에서 동기화 목록을 제외
        int delCnt = 0;
        for(String goodsId: shopliveGoodsSkuList) {
            deleteShopliveGoods(goodsId); // 동기화 하지 않는 항목 삭제
            delCnt++;
        }
    }

    /**
     * 샵라이브에 저장되어 있는 방송 정보 조회
     * @return
     * @throws Exception
     */
    public ShopliveInfoVo getRemoteShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        ShopliveHttpClient httpUtil = new ShopliveHttpClient();
        String shopliveInfoString = httpUtil.processGetHTTP("https://api.shoplive.cloud/v1/campaigns/" + shopliveAccessKey + "/" + shopliveRequestDto.getCampaignKey());
        log.info(">>> shopLive getRemoteShopliveInfo : " + shopliveInfoString);
        Map result = gson.fromJson(shopliveInfoString, HashMap.class);
        ShopliveInfoVo infoVo = new ShopliveInfoVo();
        if(StringUtil.isEmpty(result.get("campaignStatus"))) {
            log.info(">>> shopLive campaignStatus Empty..! ");
            return infoVo;
        }
        if("NOT_EXIST".equals(result.get("campaignStatus"))) {
            log.info(">>> shopLive campaignStatus NOT_EXIST..! ");
            return infoVo;
        }
        infoVo.setCampaignStatus(result.get("campaignStatus").toString());
        infoVo.setCampaignKey(result.get("campaignKey").toString());
        infoVo.setTitle(result.get("title").toString());
        if(StringUtil.isNotEmpty(result.get("scheduledAt"))) {
            LocalDateTime startTime = new LocalDateTime(((Double)result.get("scheduledAt")).longValue());
            infoVo.setStartDt(startTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if(StringUtil.isNotEmpty(result.get("scheduledEndAt"))) {
            LocalDateTime endTime = new LocalDateTime(((Double) result.get("scheduledEndAt")).longValue());
            infoVo.setEndDt(endTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
//            infoVo.setEndDt(StringUtil.isNotEmpty(result.get("scheduledEndAt"))?((Double)result.get("scheduledEndAt")).longValue() + "":"");

        return infoVo;
    }

    /**
     * 샵라이브에 저장되어 있는 상품 목록 조회
     * @return
     * @throws Exception
     */
    String getShopliveGoodsList() throws Exception {
        ShopliveHttpClient httpUtil = new ShopliveHttpClient();
        String authResultString = httpUtil.processGetHTTP("https://papi.shoplive.cloud/v1/goods/" + shopliveAccessKey);
        log.debug("Get Result : " + authResultString);
        return authResultString;
    }

    /**
     * 샵라이브에 저장되어 있는 상품 목록 조회
     * @return
     * @throws Exception
     */
    void deleteShopliveGoods(String goodsId) throws Exception {
        ShopliveHttpClient httpUtil = new ShopliveHttpClient();
        String authResultString = httpUtil.processDeleteHttp("https://papi.shoplive.cloud/v1/goods/" + shopliveAccessKey + "/sku?value=" + goodsId);
        log.debug("Delete Result : " + authResultString);
    }

    /**
     * 상품 동기화 호출
     * @param goodsList 동기화할 상품 목록
     * @return
     * @throws Exception
     */
    String postShopliveGoodsSync(List<ShopliveGoodsVo> goodsList) throws Exception {
        ShopliveHttpClient httpUtil = new ShopliveHttpClient();
        String authResultString = httpUtil.processPostHTTP(goodsList, "https://papi.shoplive.cloud/v1/goods/" + shopliveAccessKey + "/sync");
        log.debug("Post Result : " + authResultString);
        return authResultString;
    }


    /**
     * 상품 상태 변경 호출
     * @param goodsInfo 상태를 변경할 상품정보
     * @return
     * @throws Exception
     */
    String putShopliveGoodsSoldOut(ShopliveGoodsVo goodsInfo) throws Exception {
        ShopliveHttpClient httpUtil = new ShopliveHttpClient();
        Map<String, String> request = new HashMap<>();
        request.put("sku", goodsInfo.getIlGoodsId());
        request.put("salesStatus", goodsInfo.getGoodsStatus()); // ON_SALE 판매중, ALMOST_SOLD_OUT 품절 임박, SOLD_OUT 품절
        String authResultString = httpUtil.processPutHTTP(request, "https://papi.shoplive.cloud/v1/goods/" + shopliveAccessKey + "/campaign/" + goodsInfo.getCampaignKey() + "/salesStatus/sku");
        log.debug("Put Result : " + authResultString);
        return authResultString;
    }

    /**
     * 상품 동기화를 위한 파라미터 변환
     * @param goodsVoList
     * @return
     * @throws Exception
     */
    StringRequestEntity makeParam(List<ShopliveGoodsVo> goodsVoList) throws Exception {
        List<Map<String, String>> goodsMapList = new ArrayList<>();
        for(ShopliveGoodsVo goods: goodsVoList) {
            Map<String, String> goodsMap = new HashedMap<>();
            goodsMap.put("name", goods.getGoodsNm());
            goodsMap.put("brand", goods.getDpBrandNm());
            goodsMap.put("url", mallUrl + "/shop/goodsView?goods=" + goods.getIlGoodsId());
            goodsMap.put("sku", goods.getIlGoodsId());
            goodsMap.put("mediaUrl", imageUrl + goods.getGoodsImagePath());
            goodsMap.put("description", goods.getDescription());
            goodsMap.put("originalPrice", String.valueOf(goods.getRecommendedPrice()));
            goodsMap.put("discountedPrice", String.valueOf(goods.getSalePrice()));
            goodsMapList.add(goodsMap);
        }
        JsonArray jsonArray = gson.toJsonTree(goodsMapList).getAsJsonArray();

        return new StringRequestEntity(gson.toJson(jsonArray), "application/json", "UTF-8");
    }

    NameValuePair[] makeParam(Map request) throws Exception {
        int hashSize = request.size();
        Iterator keyset = request.keySet().iterator();
        NameValuePair[] params = new NameValuePair[hashSize];
        String key = "";

        for(int i = 0; i < hashSize; ++i) {
            key = (String)keyset.next();
            params[i] = new NameValuePair(key, String.valueOf(request.get(key)));
        }

        return params;
    }

    /**
     * 샵라이브 서버 통신
     */
    public class ShopliveHttpClient {
        private HttpClient client;
        private GetMethod getMethod;
        private PostMethod postMethod;
        private PutMethod putMethod;
        private DeleteMethod deleteMethod;
        private HostConfiguration hostConf;
        private final int CONNECTION_TIMEOUT = 5000;
        private final int RECEIVE_TIMEOUT = 25000;

        public ShopliveHttpClient() {
        }

        public String processGetHTTP(String actionURL) throws Exception {
            int statusCode = 0;
            String result = null;
            log.info("REQUEST URL  : " + actionURL);

            String var25;
            try {
                URL url = new URL(actionURL);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                this.hostConf = new HostConfiguration();
                this.client = new HttpClient();
                this.client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
                this.client.getHttpConnectionManager().getParams().setSoTimeout(RECEIVE_TIMEOUT);
                this.getMethod = new GetMethod(actionURL);
                this.getMethod.setRequestHeader("Content-Type", "application/json");
                this.getMethod.setRequestHeader("Cache-Control", "no-cache");
                this.getMethod.setRequestHeader("Authorization", "Bearer " + getJwtAuthHeader());
                this.hostConf.setHost(this.getMethod.getURI().getHost(), this.getMethod.getURI().getPort());
//                NameValuePair[] params = this.makeParam(request);
//                this.getMethod.setRequestBody(params);

                try {
                    statusCode = this.client.executeMethod(this.getMethod);
                } catch (Exception var21) {
                    log.error("서버응답 에러 / " + statusCode, var21);
                    System.out.println("서버응답 에러 / " + statusCode);
                    var21.printStackTrace();
                    throw var21;
                }

                Header[] var6 = this.getMethod.getResponseHeaders();
                int var7 = var6.length;

                for (Header header : var6) {
                    log.debug(header.getName() + "=" + header.getValue());
                }

                if (statusCode != 200) {
                    log.error("서버응답 에러 / " + statusCode);
                    throw new HttpException("서버응답 에러 / " + statusCode);
                }

                result = this.getMethod.getResponseBodyAsString();
                log.info("RESPONSE DATA: " + result.trim());
                var25 = result.trim();
            } catch (Exception var22) {
                var22.printStackTrace();
                log.error("서버응답 에러 / " + statusCode, var22);
                log.debug("HTTP 통신에러 발생 / \nREQUEST URL  : " + actionURL + "\nRESPONSE DATA: " + result);
                throw var22;
            } finally {
                try {
                    if (this.getMethod != null) {
                        this.getMethod.releaseConnection();
                    }
                } catch (Exception var20) {
                    this.getMethod = null;
                }

                try {
                    if (this.client != null) {
                        this.client.getHttpConnectionManager().getConnection(this.hostConf).close();
                    }
                } catch (Exception ignored) {
                }

            }

            return var25;
        }

        public String processPostHTTP(List<ShopliveGoodsVo> request, String actionURL) throws Exception {
            int statusCode = 0;
            String result = null;
            log.info("REQUEST URL  : " + actionURL);
            log.info("PARAM: " + request.toString());

            String var25;
            try {
                this.hostConf = new HostConfiguration();
                this.client = new HttpClient();
                this.client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
                this.client.getHttpConnectionManager().getParams().setSoTimeout(RECEIVE_TIMEOUT);
                this.postMethod = new PostMethod(actionURL);
                this.postMethod.setRequestHeader("Content-Type", "application/json");
                this.postMethod.setRequestHeader("Cache-Control", "no-cache");
                this.postMethod.setRequestHeader("Authorization", "Bearer " + getJwtAuthHeader());
                this.hostConf.setHost(this.postMethod.getURI().getHost(), this.postMethod.getURI().getPort());
                this.postMethod.setRequestEntity(makeParam(request));

                try {
                    statusCode = this.client.executeMethod(this.postMethod);
                } catch (Exception var21) {
                    log.error("서버응답 에러 / " + statusCode, var21);
                    System.out.println("서버응답 에러 / " + statusCode);
                    var21.printStackTrace();
                    throw var21;
                }

                Header[] var6 = this.postMethod.getResponseHeaders();
                int var7 = var6.length;

                for (Header header : var6) {
                    log.debug(header.getName() + "=" + header.getValue());
                }

                if (statusCode != 200) {
                    log.error("서버응답 에러 / " + statusCode);
                    throw new HttpException("서버응답 에러 / " + statusCode);
                }

                result = this.postMethod.getResponseBodyAsString();
                log.info("RESPONSE DATA: " + result.trim());
                var25 = result.trim();
            } catch (Exception var22) {
                var22.printStackTrace();
                log.error("서버응답 에러 / " + statusCode, var22);
                log.debug("HTTP 통신에러 발생 / \nREQUEST URL  : " + actionURL + "\nREQUEST PARAM: " + request.toString() + "\nRESPONSE DATA: " + result);
                throw var22;
            } finally {
                try {
                    if (this.postMethod != null) {
                        this.postMethod.releaseConnection();
                    }
                } catch (Exception var20) {
                    this.postMethod = null;
                }

                try {
                    if (this.client != null) {
                        this.client.getHttpConnectionManager().getConnection(this.hostConf).close();
                    }
                } catch (Exception ignored) {
                }

            }

            return var25;
        }

        public String processPutHTTP(Map request, String actionURL) throws Exception {
            int statusCode = 0;
            String result = null;
            log.info("REQUEST URL  : " + actionURL);
            log.info("PARAM: " + request.toString());

            String var25;
            try {
                this.hostConf = new HostConfiguration();
                this.client = new HttpClient();
                this.client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
                this.client.getHttpConnectionManager().getParams().setSoTimeout(RECEIVE_TIMEOUT);
                this.putMethod = new PutMethod(actionURL);
                this.putMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                this.putMethod.setRequestHeader("Cache-Control", "no-cache");
                this.putMethod.setRequestHeader("Authorization", "Bearer " + getJwtAuthHeader());
                this.hostConf.setHost(this.putMethod.getURI().getHost(), this.putMethod.getURI().getPort());
                NameValuePair[] params = makeParam(request);
//                this.putMethod.setRequestEntity(null);
                this.putMethod.setQueryString(params);

                try {
                    statusCode = this.client.executeMethod(this.putMethod);
                } catch (Exception var21) {
                    log.error("서버응답 에러 / " + statusCode, var21);
                    System.out.println("서버응답 에러 / " + statusCode);
                    var21.printStackTrace();
                    throw var21;
                }

                Header[] var6 = this.putMethod.getResponseHeaders();
                int var7 = var6.length;

                for (Header header : var6) {
                    log.debug(header.getName() + "=" + header.getValue());
                }

                if (statusCode != 200) {
                    log.error("서버응답 에러 / " + statusCode);
                    throw new HttpException("서버응답 에러 / " + statusCode);
                }

                result = this.putMethod.getResponseBodyAsString();
                log.info("RESPONSE DATA: " + result.trim());
                var25 = result.trim();
            } catch (Exception var22) {
                var22.printStackTrace();
                log.error("서버응답 에러 / " + statusCode, var22);
                log.debug("HTTP 통신에러 발생 / \nREQUEST URL  : " + actionURL + "\nREQUEST PARAM: " + request.toString() + "\nRESPONSE DATA: " + result);
                throw var22;
            } finally {
                try {
                    if (this.putMethod != null) {
                        this.putMethod.releaseConnection();
                    }
                } catch (Exception var20) {
                    this.postMethod = null;
                }

                try {
                    if (this.client != null) {
                        this.client.getHttpConnectionManager().getConnection(this.hostConf).close();
                    }
                } catch (Exception ignored) {
                }

            }

            return var25;
        }

        public String processDeleteHttp(String actionURL) throws Exception {
            int statusCode = 0;
            String result = null;
            log.info("REQUEST URL  : " + actionURL);

            String var25;
            try {
                this.hostConf = new HostConfiguration();
                this.client = new HttpClient();
                this.client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
                this.client.getHttpConnectionManager().getParams().setSoTimeout(RECEIVE_TIMEOUT);
                this.deleteMethod = new DeleteMethod(actionURL);
                this.deleteMethod.setRequestHeader("Content-Type", "application/json");
                this.deleteMethod.setRequestHeader("Cache-Control", "no-cache");
                this.deleteMethod.setRequestHeader("Authorization", "Bearer " + getJwtAuthHeader());

                this.hostConf.setHost(this.deleteMethod.getURI().getHost(), this.deleteMethod.getURI().getPort());

                try {
                    statusCode = this.client.executeMethod(this.deleteMethod);
                } catch (Exception var21) {
                    log.error("서버응답 에러 / " + statusCode, var21);
                    System.out.println("서버응답 에러 / " + statusCode);
                    var21.printStackTrace();
                    throw var21;
                }

                Header[] var6 = this.deleteMethod.getResponseHeaders();
                int var7 = var6.length;

                for (Header header : var6) {
                    log.debug(header.getName() + "=" + header.getValue());
                }

                if (statusCode != 200) {
                    log.error("서버응답 에러 / " + statusCode);
                    throw new HttpException("서버응답 에러 / " + statusCode);
                }

                result = this.deleteMethod.getResponseBodyAsString();
                log.info("RESPONSE DATA: " + result.trim());
                var25 = result.trim();
            } catch (Exception var22) {
                var22.printStackTrace();
                log.error("서버응답 에러 / " + statusCode, var22);
                log.debug("HTTP 통신에러 발생 / \nREQUEST URL  : " + actionURL + "\nRESPONSE DATA: " + result);
                throw var22;
            } finally {
                try {
                    if (this.deleteMethod != null) {
                        this.deleteMethod.releaseConnection();
                    }
                } catch (Exception var20) {
                    this.deleteMethod = null;
                }

                try {
                    if (this.client != null) {
                        this.client.getHttpConnectionManager().getConnection(this.hostConf).close();
                    }
                } catch (Exception ignored) {
                }

            }

            return var25;
        }
    }
}
