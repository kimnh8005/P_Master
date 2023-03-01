package kr.co.pulmuone.v1.search.searcher.parser;

import org.apache.commons.lang.StringUtils;

public class KoreanParser {

    /**
     * 토큰을 자음과 모음으로 파싱한다.
     *
     * @param token
     * @return
     */
    public String parse(String token) {
        if (StringUtils.isBlank(token)) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        char[] arrCh = token.toCharArray();
        for(char ch : arrCh) {

            char unicodeIndex = (char)(ch - KoreanConsonantVowel.START_KOREAN_UNICODE);

            if(unicodeIndex >= 0 && unicodeIndex <= 11184) {

                int onsetIndex = unicodeIndex / (28 * 21);
                char onset = KoreanConsonantVowel.UNICODE_ONSET[onsetIndex];

                int nucleusIndex = unicodeIndex % (28 * 21) / 28;
                char nucleus = KoreanConsonantVowel.UNICODE_NUCLEUS[nucleusIndex];

                int codaIndex = unicodeIndex % (28 * 21) % 28;
                char coda = KoreanConsonantVowel.UNICODE_CODA[codaIndex];

                processForKoreanChar(result, onset, nucleus, coda);

            } else {

                processForOther(result, ch);
            }
        }

        return result.toString();
    }


    /**
     * 한글 문자를 처리한다.
     *
     * @param sb
     * @param onset
     * @param nucleus
     * @param coda
     */
    protected void processForKoreanChar(StringBuilder sb, char onset, char nucleus, char coda){
        sb.append(onset).append(nucleus);

        if(coda != KoreanConsonantVowel.UNICODE_CODA_EMPTY) {
            sb.append(coda);
        }
    }


    /**
     * 한글 문자를 제외한 일반 문자를 처리한다.
     *
     * @param sb
     * @param eachToken
     */
    protected void processForOther(StringBuilder sb, char eachToken){
        sb.append(eachToken);
    }

}
