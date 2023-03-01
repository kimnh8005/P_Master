package kr.co.pulmuone.v1.search.searcher.parser;

public class KoreanConsonantVowel {
    /**
     * 초성 (19자) : Onset
     * 초성으로 올 수 있는 유니코드들
     * ㄱ ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ
     * ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ
     */
    public static final char[] UNICODE_ONSET = {
            0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143, 0x3145,
            0x3146, 0x3147, 0x3148, 0x3149, 0x314A, 0x314B, 0x314C, 0x314D, 0x314E
    };


    /**
     * 중성 (21자) : Nucleus
     * 중성으로 올 수 있는 유니코드들
     * ㅏ ㅐ ㅑ ㅒ ㅓ ㅔ ㅕ ㅖ ㅗ ㅘ
     * ㅙ ㅚ ㅛ ㅜ ㅝ ㅞ ㅟ ㅠ ㅡ ㅢ
     * ㅣ
     */
    public static final char[] UNICODE_NUCLEUS = {
            0x314F, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158,
            0x3159, 0x315A, 0x315B, 0x315C, 0x315D, 0x315E, 0x315F, 0x3160, 0x3161, 0x3162,
            0x3163
    };


    /**
     * 종성 (28자) : Coda
     * 종성으로 올 수 있는 유니코드들
     *  빈값 ㄱ ㄲ ㄳ ㄴ ㄵ ㄶ ㄷ ㄹ ㄺ
     *  ㄻ ㄼ ㄽ ㄾ ㄿ ㅀ ㅁ ㅂ ㅄ ㅅ
     *  ㅆ ㅇ ㅈ ㅊ ㅋ ㅌ ㅍ ㅎ
     */
    public static final char[] UNICODE_CODA = {
            0x0000, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313A,
            0x313B, 0x313C, 0x313D, 0x313E, 0x313F, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145,
            0x3146, 0x3147, 0x3148, 0x314A, 0x314B, 0x314C, 0x314D, 0x314E
    };



    /**
     * 한글 유니코드의 시작값 (가)<br>
     * 한글 유니코드는 0xAC00로 시작하여 0xD79F로 끝난다.<br>
     * 시작값과 끝값을 벗어난 유니코드는 한글이 아님
     * 시작값 : 0xAC00 가<br>
     * 끝값   : 0xD79F 힟<br>
     */
    public static final char START_KOREAN_UNICODE = 0xAC00;



    /**
     * 종성 빈값 유니코드
     */
    public static final char UNICODE_CODA_EMPTY = 0x0000;


}
