package kr.co.pulmuone.v1.order.history.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <PRE>
 * Forbiz Korea
 * 이력 관리 메시지 Util
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 11.    김명진            최초작성
 * =======================================================================
 * </PRE>
 */
public class HistoryMsgUtil {
    public static String getHistoryMsg(String msgCd){
        Map<String, String> histMap =  new HashMap<String, String>();
        histMap.put("HIST_001", "test");
        histMap.put("HIST_002", "test"); // 도착예정일 변경

        // 예외 케이스 추가
        if(!histMap.containsKey(msgCd)) {
        	return "-";
        }
        return histMap.get(msgCd);
    }
}
