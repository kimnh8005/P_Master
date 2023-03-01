/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 이벤트 등록/수정 - 소스분리 - Template
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.01.28    dgyoun        최초 생성
 * @
 ******************************************************************************/

// 참고메모
/*
// 댓글 분류설정 관련
// 댓글 분류설정 가져오기
const _list = [];

$('.commentCodeValue').each(function(){
    _list.push($(this).text());
})

// 댓글 분류값 뿌려주기
var a = [{'keyword':'1111'}, {'keyword':'2222'}, {'keyword':'3333'}, {'keyword':'4444'}];
fnTemplates.commentTp.render(a);
*/

// ----------------------------------------------------------------------------
// 템플릿관련
// ----------------------------------------------------------------------------
var fnTemplates   = {};             // {}
var gbUserGroupMaps = new Map();    // 접근권한 - 유저등급
var gbCommentCodeMaps = new Map();  // 댓글분류값맵


// ----------------------------------------------------------------------------
// 이벤트 유형별 항목 노출/숨김 설정 : 아래 항목은 숨김 대상
// ----------------------------------------------------------------------------
// [대상목록 전체]
//                                  '#selectStartEndTr'        // 당첨자 선정기간
//                                , '#feedbackStartEndTr'      // 후기 작성기간
//                                , '#commentYnTr'             // 댓글 허용여부
//                                , '#experienceRegTr'         // 체험상품 등록
//                                , '#commentCodeYnTr'         // 댓글 분류설정
//                                , '#eventBenefitTr'          // 당첨자 혜택
//                                , '#stampDefImgTr'           // 스탬프 기본 이미지 / 스탬프 체크 이미지
//                                , '#stampBgImgTr'            // 스탬프 배경 이미지
//                                , '#stampCntTr'              // 스탬프 개수설정
//                                , '#stampOrderTr'            // 스탬프 지급 조건
//                                , '#rouletteStartBtnImgTr'   // 시작 버튼 이미지 / 화살표 이미지
//                                , '#rouletteBgImgTr'         // 룰렛 배경 이미지
//                                , '#rouletteCntTr'           // 룰렛 개수 설정 / 이벤트 제한고객 룰렛 설정
//                                , '#rouletteImgTr'           // 룰렛 이미지
//                                , '#winnerNoticeTr'          // 당첨자 공지사항(url)
//                                , '#stampBenefitCommonDiv'   // 스탬프 템플릿
//                                , '#rouletteBenefitDiv'      // 룰렛 템플릿

//각 타입 별 display: none처리할 행 id 리스트
var ignoreList = {
          'EVENT_TP.ALL'        : [
                                    '#selectStartEndTr'        // 당첨자 선정기간
                                  , '#feedbackStartEndTr'      // 후기 작성기간
//                                  , '#commentYnTr'             // 댓글 허용여부
                                  , '#experienceRegTr'         // 체험상품 등록
                                  , '#commentCodeYnTr'         // 댓글 분류설정
                                  , '#employeeJoinYnTr'        // 임직원참여여부**
                                  , '#eventDrawTpTr'           // 당첨자설정**
                                  , '#eventBenefitTr'          // 당첨자 혜택
                                  , '#normalEventTpTr'         // 참여방법
                                  , '#eventDrawTpTr'           // 당첨자 설정
                                  , '#joinConditionTr'         // 참여조건
                                  , '#goodsCoverageDiv'        // 적용범위
                                  , '#winnerInforTr'           // 당첨자 안내**
                                  , '#stampDefImgTr'           // 스탬프 기본 이미지 / 스탬프 체크 이미지
                                  , '#stampBgImgTr'            // 스탬프 배경 이미지
                                  , '#stampCntTr'              // 스탬프 개수설정
                                  , '#stampOrderTr'            // 스탬프 지급 조건
                                  , '#rouletteStartBtnImgTr'   // 시작 버튼 이미지 / 화살표 이미지
                                  , '#rouletteBgImgTr'         // 룰렛 배경 이미지
                                  , '#rouletteCntTr'           // 룰렛 개수 설정 / 이벤트 제한고객 룰렛 설정
                                  , '#rouletteImgTr'           // 룰렛 이미지
                                  , '#winnerNoticeTr'          // 당첨자 공지사항(url)
                                  , '#stampBenefitCommonDiv'   // 스탬프 템플릿
                                  , '#rouletteBenefitDiv'      // 룰렛 템플릿
                                  ]
        // --------------------------------------------------------------------
        // 일반 - 댓글허용(Y)
        // --------------------------------------------------------------------
        , 'EVENT_TP.NORMAL'     : [
                                    '#selectStartEndTr'        // 당첨자 선정기간
                                  , '#feedbackStartEndTr'      // 후기 작성기간
                                  //, '#commentYnTr'             // 댓글 허용여부
                                  , '#experienceRegTr'         // 체험상품 등록
//                                  , '#commentCodeYnTr'         // 댓글 분류설정
                                  //, '#eventBenefitTr'          // 당첨자 혜택
//                                  , '#normalEventTpTr'         // 참여방법 / 당첨자 설정
//                                  , '#eventDrawTpTr'           // 당첨자 설정
//                                  , '#joinConditionTr'         // 참여조건
                                  , '#stampDefImgTr'           // 스탬프 기본 이미지 / 스탬프 체크 이미지
                                  , '#stampBgImgTr'            // 스탬프 배경 이미지
                                  , '#stampCntTr'              // 스탬프 개수설정
                                  , '#stampOrderTr'            // 스탬프 지급 조건
                                  , '#rouletteStartBtnImgTr'   // 시작 버튼 이미지 / 화살표 이미지
                                  , '#rouletteBgImgTr'         // 룰렛 배경 이미지
                                  , '#rouletteCntTr'           // 룰렛 개수 설정 / 이벤트 제한고객 룰렛 설정
                                  , '#rouletteImgTr'           // 룰렛 이미지
                                  //, '#winnerNoticeTr'          // 당첨자 공지사항(url)
                                  , '#stampBenefitCommonDiv'   // 스탬프 템플릿
                                  , '#rouletteBenefitDiv'      // 룰렛 템플릿
                                  , '#immediatleyWinRouletteDiv' // 룰렛 예상참여자수
                                  ]
        // --------------------------------------------------------------------
        // 일반 - 댓글허용(N)
        // --------------------------------------------------------------------
        , 'EVENT_TP.NORMAL.NO'  : [
                                    '#selectStartEndTr'        // 당첨자 선정기간
                                  , '#feedbackStartEndTr'      // 후기 작성기간
                                  //, '#commentYnTr'             // 댓글 허용여부
                                  , '#experienceRegTr'         // 체험상품 등록
                                  , '#commentCodeYnTr'         // 댓글 분류설정*
                                  , '#employeeJoinYnTr'        // 임직원참여여부**
                                  , '#eventDrawTpTr'           // 당첨자설정**
                                  , '#eventBenefitTr'          // 당첨자 혜택*
                                  , '#winnerInforTr'           // 당첨자 안내**
//                                  , '#normalEventTpTr'         // 참여방법
                                  , '#eventDrawTpTr'           // 당첨자 설정
                                  , '#joinConditionTr'         // 참여조건
                                  , '#goodsCoverageDiv'        // 적용범위
                                  , '#stampDefImgTr'           // 스탬프 기본 이미지 / 스탬프 체크 이미지
                                  , '#stampBgImgTr'            // 스탬프 배경 이미지
                                  , '#stampCntTr'              // 스탬프 개수설정
                                  , '#stampOrderTr'            // 스탬프 지급 조건
                                  , '#rouletteStartBtnImgTr'   // 시작 버튼 이미지 / 화살표 이미지
                                  , '#rouletteBgImgTr'         // 룰렛 배경 이미지
                                  , '#rouletteCntTr'           // 룰렛 개수 설정 / 이벤트 제한고객 룰렛 설정
                                  , '#rouletteImgTr'           // 룰렛 이미지
                                  //, '#winnerNoticeTr'          // 당첨자 공지사항(url)
                                  , '#stampBenefitCommonDiv'   // 스탬프 템플릿
                                  , '#rouletteBenefitDiv'      // 룰렛 템플릿
                                  , '#immediatleyWinRouletteDiv' // 룰렛 예상참여자수
                                  ]

        // --------------------------------------------------------------------
        // 설문
        // --------------------------------------------------------------------
        , 'EVENT_TP.SURVEY'     : [
                                    '#selectStartEndTr'        // 당첨자 선정기간
                                  , '#feedbackStartEndTr'      // 후기 작성기간
//                                  , '#commentYnTr'             // 댓글 허용여부
                                  , '#experienceRegTr'         // 체험상품 등록
                                  , '#commentCodeYnTr'         // 댓글 분류설정
                                  //, '#eventBenefitTr'          // 당첨자 혜택
                                  , '#normalEventTpTr'         // 참여방법
                                 // , '#eventDrawTpTr'           // 당첨자 설정
                                  , '#joinConditionTr'         // 참여조건
                                  , '#goodsCoverageDiv'        // 적용범위
                                  , '#stampDefImgTr'           // 스탬프 기본 이미지 / 스탬프 체크 이미지
                                  , '#stampBgImgTr'            // 스탬프 배경 이미지
                                  , '#stampCntTr'              // 스탬프 개수설정
                                  , '#stampOrderTr'            // 스탬프 지급 조건
                                  , '#rouletteStartBtnImgTr'   // 시작 버튼 이미지 / 화살표 이미지
                                  , '#rouletteBgImgTr'         // 룰렛 배경 이미지
                                  , '#rouletteCntTr'           // 룰렛 개수 설정 / 이벤트 제한고객 룰렛 설정
                                  , '#rouletteImgTr'           // 룰렛 이미지
                                  //, '#winnerNoticeTr'          // 당첨자 공지사항(url)
                                  , '#stampBenefitCommonDiv'   // 스탬프 템플릿
                                  , '#rouletteBenefitDiv'      // 룰렛 템플릿
                                  , '#immediatleyWinRouletteDiv' // 룰렛 예상참여자수
                                  ]
        // --------------------------------------------------------------------
        // 스탬프(출석)
        // --------------------------------------------------------------------
        , 'EVENT_TP.ATTEND'     : [
                                    '#selectStartEndTr'        // 당첨자 선정기간
                                  , '#feedbackStartEndTr'      // 후기 작성기간
//                                  , '#commentYnTr'             // 댓글 허용여부
                                  , '#experienceRegTr'         // 체험상품 등록
                                  , '#commentCodeYnTr'         // 댓글 분류설정
                                  , '#eventBenefitTr'          // 당첨자 혜택
                                  , '#normalEventTpTr'         // 참여방법
                                  , '#eventDrawTpTr'           // 당첨자 설정
                                  , '#joinConditionTr'         // 참여조건
                                  , '#goodsCoverageDiv'        // 적용범위
                                  //, '#stampDefImgTr'           // 스탬프 기본 이미지 / 스탬프 체크 이미지
                                  //, '#stampBgImgTr'            // 스탬프 배경 이미지
                                  //, '#stampCntTr'              // 스탬프 개수설정
                                  , '#stampOrderTr'            // 스탬프 지급 조건
                                  , '#rouletteStartBtnImgTr'   // 시작 버튼 이미지 / 화살표 이미지
                                  , '#rouletteBgImgTr'         // 룰렛 배경 이미지
                                  , '#rouletteCntTr'           // 룰렛 개수 설정 / 이벤트 제한고객 룰렛 설정
                                  , '#rouletteImgTr'           // 룰렛 이미지
                                  //, '#winnerNoticeTr'          // 당첨자 공지사항(url)
                                  //, '#stampBenefitCommonDiv'   // 스탬프 템플릿
                                  , '#rouletteBenefitDiv'      // 룰렛 템플릿
                                  , '#immediatleyWinRouletteDiv' // 룰렛 예상참여자수
                                  ]
        // --------------------------------------------------------------------
        // 스탬프(미션)
        // --------------------------------------------------------------------
        , 'EVENT_TP.MISSION'    : [
                                    '#selectStartEndTr'        // 당첨자 선정기간
                                  , '#feedbackStartEndTr'      // 후기 작성기간
//                                  , '#commentYnTr'             // 댓글 허용여부
                                  , '#experienceRegTr'         // 체험상품 등록
                                  , '#commentCodeYnTr'         // 댓글 분류설정
                                  , '#eventBenefitTr'          // 당첨자 혜택
                                  , '#normalEventTpTr'         // 참여방법
                                  , '#eventDrawTpTr'           // 당첨자 설정
                                  , '#joinConditionTr'         // 참여조건
                                  , '#goodsCoverageDiv'        // 적용범위
                                  //, '#stampDefImgTr'           // 스탬프 기본 이미지 / 스탬프 체크 이미지
                                  //, '#stampBgImgTr'            // 스탬프 배경 이미지
                                  //, '#stampCntTr'              // 스탬프 개수설정
                                  , '#stampOrderTr'            // 스탬프 지급 조건
                                  , '#rouletteStartBtnImgTr'   // 시작 버튼 이미지 / 화살표 이미지
                                  , '#rouletteBgImgTr'         // 룰렛 배경 이미지
                                  , '#rouletteCntTr'           // 룰렛 개수 설정 / 이벤트 제한고객 룰렛 설정
                                  , '#rouletteImgTr'           // 룰렛 이미지
                                  //, '#winnerNoticeTr'          // 당첨자 공지사항(url)
                                  //, '#stampBenefitCommonDiv'   // 스탬프 템플릿
                                  , '#rouletteBenefitDiv'      // 룰렛 템플릿
                                  , '#immediatleyWinRouletteDiv' // 룰렛 예상참여자수
                                  ]
        // --------------------------------------------------------------------
        // 스탬프(구매)
        // --------------------------------------------------------------------
        , 'EVENT_TP.PURCHASE'   : [
                                    '#selectStartEndTr'        // 당첨자 선정기간
                                  , '#feedbackStartEndTr'      // 후기 작성기간
//                                  , '#commentYnTr'             // 댓글 허용여부
                                  , '#experienceRegTr'         // 체험상품 등록
                                  , '#commentCodeYnTr'         // 댓글 분류설정
                                  , '#eventBenefitTr'          // 당첨자 혜택
                                  , '#normalEventTpTr'         // 참여방법
                                  , '#eventDrawTpTr'           // 당첨자 설정
                                  , '#joinConditionTr'         // 참여조건
                                  , '#goodsCoverageDiv'        // 적용범위
                                  //, '#stampDefImgTr'           // 스탬프 기본 이미지 / 스탬프 체크 이미지
                                  //, '#stampBgImgTr'            // 스탬프 배경 이미지
                                  //, '#stampCntTr'              // 스탬프 개수설정
                                  //, '#stampOrderTr'            // 스탬프 지급 조건
                                  , '#rouletteStartBtnImgTr'   // 시작 버튼 이미지 / 화살표 이미지
                                  , '#rouletteBgImgTr'         // 룰렛 배경 이미지
                                  , '#rouletteCntTr'           // 룰렛 개수 설정 / 이벤트 제한고객 룰렛 설정
                                  , '#rouletteImgTr'           // 룰렛 이미지
                                  //, '#winnerNoticeTr'          // 당첨자 공지사항(url)
                                  //, '#stampBenefitCommonDiv'   // 스탬프 템플릿
                                  , '#rouletteBenefitDiv'      // 룰렛 템플릿
                                  , '#immediatleyWinRouletteDiv' // 룰렛 예상참여자수
                                  ]
        // --------------------------------------------------------------------
        // 룰렛
        // --------------------------------------------------------------------
        , 'EVENT_TP.ROULETTE'   : [
                                    '#selectStartEndTr'        // 당첨자 선정기간
                                  , '#feedbackStartEndTr'      // 후기 작성기간
//                                  , '#commentYnTr'             // 댓글 허용여부
                                  , '#experienceRegTr'         // 체험상품 등록
                                  , '#commentCodeYnTr'         // 댓글 분류설정
                                  , '#eventBenefitTr'          // 당첨자 혜택
                                  , '#normalEventTpTr'         // 참여방법
//                                  , '#eventDrawTpTr'           // 당첨자 설정
                                  , '#joinConditionTr'         // 참여조건
                                  , '#goodsCoverageDiv'        // 적용범위
                                  , '#stampDefImgTr'           // 스탬프 기본 이미지 / 스탬프 체크 이미지
                                  , '#stampBgImgTr'            // 스탬프 배경 이미지
                                  , '#stampCntTr'              // 스탬프 개수설정
                                  , '#stampOrderTr'            // 스탬프 지급 조건
                                  //, '#rouletteStartBtnImgTr'   // 시작 버튼 이미지 / 화살표 이미지
                                  //, '#rouletteBgImgTr'         // 룰렛 배경 이미지
                                  //, '#rouletteCntTr'           // 룰렛 개수 설정 / 이벤트 제한고객 룰렛 설정
                                  //, '#rouletteImgTr'           // 룰렛 이미지
                                  //, '#winnerNoticeTr'          // 당첨자 공지사항(url)
                                  , '#stampBenefitCommonDiv'   // 스탬프 템플릿
                                  //, '#rouletteBenefitDiv'      // 룰렛 템플릿
                                  ]
        // --------------------------------------------------------------------
        // 체험단
        // --------------------------------------------------------------------
        , 'EVENT_TP.EXPERIENCE' : [
                                  //  '#selectStartEndTr'        // 당첨자 선정기간
                                  //, '#fe/edbackStartEndTr'      // 후기 작성기간
//                                    '#commentYnTr'             // 댓글 허용여부
                                  //, '#experienceRegTr'         // 체험상품 등록
                                  //, '#commentCodeYnTr'         // 댓글 분류설정
                                  //, '#eventBenefitTr'          // 당첨자 혜택
                                   '#normalEventTpTr'         // 참여방법
                                  , '#eventDrawTpTr'           // 당첨자 설정
                                  , '#joinConditionTr'         // 참여조건
                                  , '#goodsCoverageDiv'        // 적용범위
                                  , '#stampDefImgTr'           // 스탬프 기본 이미지 / 스탬프 체크 이미지
                                  , '#stampBgImgTr'            // 스탬프 배경 이미지
                                  , '#stampCntTr'              // 스탬프 개수설정
                                  , '#stampOrderTr'            // 스탬프 지급 조건
                                  , '#rouletteStartBtnImgTr'   // 시작 버튼 이미지 / 화살표 이미지
                                  , '#rouletteBgImgTr'         // 룰렛 배경 이미지
                                  , '#rouletteCntTr'           // 룰렛 개수 설정 / 이벤트 제한고객 룰렛 설정
                                  , '#rouletteImgTr'           // 룰렛 이미지
                                  //, '#winnerNoticeTr'          // 당첨자 공지사항(url)
                                  , '#stampBenefitCommonDiv'   // 스탬프 템플릿
                                  , '#rouletteBenefitDiv'      // 룰렛 템플릿
                                  , '#immediatleyWinRouletteDiv' // 룰렛 예상참여자수
                                  ]
};

  // ==========================================================================
  // # 화면 항목 노출/숨김 처리
  // ==========================================================================
  function fnDisplayScreen(eventTp) {
    //console.log('# fnDisplayScreen Start [', eventTp, ']');
    //const eventTp = gbPageParam.eventTp ? gbPageParam.eventTp : 'EVENT_TP.NORMAL';

    // ------------------------------------------------------------------------
    // 모든 항목 show
    // ------------------------------------------------------------------------
    const allRowList = ignoreList['EVENT_TP.ALL'].join(',');
    const $allRows = $(allRowList);
    $allRows.show().toggleDisableInTag(false);

    // ------------------------------------------------------------------------
    // 항목 숨김 Strat
    //console.log('# eventTp :: ', eventTp);
    const ignoreRowList = ignoreList[eventTp].join(',');
    //console.log('# ignoreRowList :: ', JSON.stringify(ignoreRowList));
    // display:none, disabled처리할 테이블 행
    const $rows = $(ignoreRowList);
    // disabled처리
    $rows.hide().toggleDisableInTag(true);
    // 항목 숨김 End
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // 기 생성된 템플릿 모두 삭제 : [설문][스탬프][룰렛]
    // ------------------------------------------------------------------------
    $('#surveyDiv tbody').empty();
    $('#stampBenefitCommonTbody').empty();
    $('#rouletteBenefitTbody').empty();
    // 템플릿 index 정보 초기화
    // 설문 idx
    gbSurveyCurrentRowCount = 0;
    gbSurveyCurrentRowIndex = 0;
    // 스탬프
    gbStampCurrentRowCount = 0;
    gbStampCurrentRowIndex = 0;
    // 룰렛
    dgRouletteCurrentRowCount = 0;
    gbRouletteCurrentRowIndex = 0;

    // ------------------------------------------------------------------------
    // 신규가입자 한정 여부 노출/숨김 : 일반 이벤트에만 우선 적용 ( 추후에 모든 이벤트에 적용할 때 삭제 )
    // ------------------------------------------------------------------------
    if (gbEventTp == 'EVENT_TP.NORMAL') {
        $('#checkNewUserTr').show();
        if($("#checkNewUserYn").getRadioVal() == "N") {
            $("#checkNewUserNoti").hide();
        }else{
            $("#checkNewUserNoti").show();
        }
    }else{
        $('#checkNewUserTr').hide();
    }


    // ------------------------------------------------------------------------
    // 기간 종료 후 사용 자동 종료 노출/숨김 : [체험단]/[그외]
    // ------------------------------------------------------------------------
    if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
      $('#timeOverCloseYn').hide();
      $('#experienceTimeOverCloseYn').show();
    }
    else {
      $('#timeOverCloseYn').show();
      $('#experienceTimeOverCloseYn').hide();
    }

    // ------------------------------------------------------------------------
    // 참여버튼 BG 컬러코드
    // ------------------------------------------------------------------------
    if (eventTp == 'EVENT_TP.NORMAL' || eventTp == 'EVENT_TP.ROULETTE') {
      // 숨김
      $("#btnColorCdThDiv").hide('');
      $("#btnColorCdTdDiv").hide('');
      $("#commentCodeYnTr").hide();
    }
    else {
      // 노출
      $("#btnColorCdThDiv").show('');
      $("#btnColorCdTdDiv").show('');
    }

    // ----------------------------------------------------------------------
    // 당첨자혜택(메인) 노출 : [일반][설문][체험단]
    // ----------------------------------------------------------------------
    if (eventTp == 'EVENT_TP.NORMAL' || eventTp == 'EVENT_TP.SURVEY' || eventTp == 'EVENT_TP.EXPERIENCE') {
      $('.js__benefits input[type="radio"]').eq(0).attr('checked', true);
      $('[data-parent="EVENT_BENEFIT_TP.COUPON"]').addClass('show');

      if (eventTp == 'EVENT_TP.NORMAL' || eventTp == 'EVENT_TP.SURVEY') {
        // [일반][설문]
        $('#eventBenefitTpNormalDiv').show();
        $('#eventBenefitTpExperienceDiv').hide();

      }
      else {
        // [체험단]
        $('#eventBenefitTpNormalDiv').hide();
        $('#eventBenefitTpExperienceDiv').show();
      }
    }

    // ------------------------------------------------------------------------
    // @ 설문 템플릿 초기 생성
    // ------------------------------------------------------------------------
    if (eventTp == 'EVENT_TP.SURVEY'){
      // 설문영역 노출
      $('#surveyDiv').show();
      // 설문템플릿 초기 추가
      if (gbMode == 'insert') {
        fnTmplAddSurveyEmpty();
      }
    }
    else {
      // 설문영역 숨김
      $('#surveyDiv').hide();
    }

    // ------------------------------------------------------------------------
    // @ 스탬프 템플릿 초기 생성
    // ------------------------------------------------------------------------
    if (eventTp == 'EVENT_TP.ATTEND' || eventTp == 'EVENT_TP.MISSION' || eventTp == 'EVENT_TP.PURCHASE'){

      // ----------------------------------------------------------------------
      // 스탬프 종류별 항목 설정
      // ----------------------------------------------------------------------
      const stampDiv = $('#stampBenefitCommonDiv')[0];
      stampDiv.className = 'toggleItem';

      if( eventTp == 'EVENT_TP.ATTEND' ) {
        stampDiv.classList.add('stampBenefitDiv--attend');
      }
      else if( eventTp == 'EVENT_TP.MISSION' ) {
        stampDiv.classList.add('stampBenefitDiv--mission');
      }
      else if( eventTp == 'EVENT_TP.PURCHASE' ) {
        stampDiv.classList.add('stampBenefitDiv--purchase');
      }

      // ----------------------------------------------------------------------
      // 템플릿 추가 : 스탬프 개수에 의해 성생되므로 최초 생성 없음
      // ----------------------------------------------------------------------
      // TODO 임시로 최초 생성 테스트
      //if (eventTp == 'EVENT_TP.ATTEND') {
      //  fnTmplAddStamp(eventTp, 1);
      //}
    }

    // ------------------------------------------------------------------------
    // @ 룰렛 템플릿 초기 생성 : 룰렛 개수 설정 시 생성되므로 최초 생성 없음
    // ------------------------------------------------------------------------
    // TODO 임시로 최초 생성 테스트
    //if (eventTp == 'EVENT_TP.ROULETTE'){
    //  fnTmplAddRoulette();
    //}

  }



  // ##########################################################################
  // # 템플릿 Function Start

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 템플릿 공통 Start
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // ==========================================================================
  // # 당첨혜택 설정 테이블에 빈 템플릿 추가
  //  @param {object} target   : 템플릿을 추가할 DOM 엘리먼트
  //  @param {string} template : 추가 할 템플릿
  //  @param {number} rowIndex : 현재 행 번호
  // ==========================================================================
  function fnTmplAddEmptyTemplate(target, template, rowIndex) {
    //console.log('# fnTmplAddEmptyTemplate Start');

    // ------------------------------------------------------------------------
    // 설문별 보기 개수 체크 : 10개 제한
    // ------------------------------------------------------------------------
    const $items = target.find('.surveyItem');
    var _length = 0;
    if( $items.length > 0 ) {
      _length = $items.length;
    }

    if (_length >= 10 && !template.includes('surveyTmplTr')) {
      fnMessage('', '보기는 최대 10개까지 가능합니다.', '');
      return false;
    }

    let subIndex; // subIndex

    // ------------------------------------------------------------------------
    // 설문인 경우 subIndex Set
    // ------------------------------------------------------------------------
    if (gbEventTp == 'EVENT_TP.SURVEY') {
      if (gbSurveySubCurrentRowIndexMap != undefined && gbSurveySubCurrentRowIndexMap != null && gbSurveySubCurrentRowIndexMap.size > 0) {
        subIndex = gbSurveySubCurrentRowIndexMap.get(rowIndex+'');

        if (fnIsEmpty(subIndex) == true || subIndex == 0) {
          subIndex = 1;
        }
        else {
          subIndex += 1;
        }
        gbSurveySubCurrentRowIndexMap.set(rowIndex+'', subIndex);
      }
    }

    if (rowIndex) {
      template = template.replace(/\${index}/gi, rowIndex);
      // subIndex 존재할 경우 치환
      if (fnIsEmpty(subIndex) == false && subIndex > 0) {
        template = template.replace(/\${subIndex}/gi, subIndex);
      }
      // _${subIndex} 가 남은 경우 제거 (안전장치)
      template = template.replace(/\_${subIndex}/gi, '');
    }
    target.append($(template));
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 템플릿 - 접근권한(회원등급)
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // ==========================================================================
  // # 접근권한 추가 버튼
  // ==========================================================================
  function fnBtnAddUserGroup(e) {
    //console.log('# fnBtnAddUserGroup Start');
    const $input = $('#userGroup');
    const inputValue  = $('#userGroup').data('kendoDropDownList').value();
    const inputText   = $('#userMaster').data('kendoDropDownList').text() + '_' + $('#userGroup').data('kendoDropDownList').text();

    // ------------------------------------------------------------------------
    // 접근권한 입력 체크
    // ------------------------------------------------------------------------
    if (!inputValue.length) {
      fnMessage('', '접근권한 설정 회원등급을 선택해주세요.', 'inputValue');
      return false;
    }

    // ------------------------------------------------------------------------
    // 접근권한 중복 체크
    // ------------------------------------------------------------------------
    var isDup = false;

    $('.userGroupText').each(function(){
      if (inputText == $(this).text()) {
        isDup = true;
        return false;
      }
    });

    if (isDup == true) {
      fnMessage('', '중복된 회원등급 입니다. 회원등급을 확인하세요.', 'inputValue');
      return false;
    }

    // 화면 노출 (화면에 선택 내용 추가)
    //gbUserGroupMaps.set(inputText, inputValue);
    //fnTemplates.userGroupTp.add({'keyword': inputText });
    fnAddUserGroup(inputText, inputValue);
    // 회원등급 '회원등릅선택' 선택
    //$('#userGroup').data('kendoDropDownList').value('');
    //$input.val('').focus();
  }

  // ==========================================================================
  // # 접근권한 추가
  // ==========================================================================
  function fnAddUserGroup(inputText, inputValue) {

    gbUserGroupMaps.set(inputText, inputValue);
    fnTemplates.userGroupTp.add({'keyword': inputText });
  }

  // ==========================================================================
  // # 접근권한 삭제 버튼
  // ==========================================================================
  function fnRemoveUserGroup(e) {
    e.stopPropagation();
    e.stopImmediatePropagation();
    const $target = $(e.target).closest('.js__remove__userGroupList');

    fnKendoMessage({
        type    : 'confirm'
      , message : '제거하시겠습니까?'
      , ok      : function() {
                    gbUserGroupMaps.delete($target.find('.userGroupText').text())
                    $target.remove();
                  }
      , cancel  : function() {}
    });
    //if (!window.confirm('제거하시겠습니까?')) {
    //  return;
    //}
    //gbUserGroupMaps.delete($target.find('.userGroupText').text())
    //$target.remove();
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 템플릿 - 설문
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // ==========================================================================
  // # 템플릿 - 설문 - 템플릿 조회 추가
  // ==========================================================================
  function fnTmplAddSurveySearch(itemCnt) {
    //console.log('# [fnTmplAddSurveySearch] Start');
    //e.stopPropagation();
    // ------------------------------------------------------------------------
    // Count/Incex Set
    // ------------------------------------------------------------------------
    gbSurveyCurrentRowCount += 1;
    gbSurveyCurrentRowIndex += 1;
    // ------------------------------------------------------------------------
    // 설문 템플릿 추가
    // ------------------------------------------------------------------------
    const $target = $('#surveyDiv tbody');
    const template = $('#emptySurveyTmpl').html();
    fnTmplAddEmptyTemplate($target, template, gbSurveyCurrentRowIndex);
    // 설문인 경우 subIndex Set
    if (gbEventTp == 'EVENT_TP.SURVEY') {
      gbSurveySubCurrentRowIndexMap.set(gbSurveyCurrentRowIndex+'', 1);
    }

    // ------------------------------------------------------------------------
    // 설문 아이템 템플릿 기본 추가 (1번째 이후 4건)
    // ------------------------------------------------------------------------
    const $tr = $('#surveyDiv table tr#surveyTmplTr_'+gbSurveyCurrentRowIndex);
    const $targetItem = $tr.find('.surveyList');
    const templateItem = $('#emptySurveyItemTpl').html();
    //$target.find('.surveyItem').length;
    for (var i = 0; i < (itemCnt-1); i++) {
      fnTmplAddEmptyTemplate($targetItem, templateItem, gbSurveyCurrentRowIndex);
    }
    // ------------------------------------------------------------------------
    // 설문유형 라디오 생성
    // ------------------------------------------------------------------------
    //const eventSurveyTp = 'eventSurveyTp' + gbSurveyCurrentRowIndex;
    //fnMakeEventSurveyTp(eventSurveyTp);
    // ------------------------------------------------------------------------
    // 노출제목 No 갱신
    // ------------------------------------------------------------------------
    fnChangeSurveyViewIdxNum();
  }

  // ==========================================================================
  // # 템플릿 - 설문 - 템플릿 신규 추가
  // ==========================================================================
  function fnTmplAddSurveyEmpty() {
    //console.log('# [fnTmplAddSurveyEmpty] Start');
    //e.stopPropagation();
    // ------------------------------------------------------------------------
    // Count/Incex Set
    // ------------------------------------------------------------------------
    gbSurveyCurrentRowCount += 1;
    gbSurveyCurrentRowIndex += 1;
    // ------------------------------------------------------------------------
    // 설문 템플릿 추가
    // ------------------------------------------------------------------------
    const $target = $('#surveyDiv tbody');
    const template = $('#emptySurveyTmpl').html();
    fnTmplAddEmptyTemplate($target, template, gbSurveyCurrentRowIndex);
    // 설문인 경우 subIndex Set
    if (gbEventTp == 'EVENT_TP.SURVEY') {
      gbSurveySubCurrentRowIndexMap.set(gbSurveyCurrentRowIndex+'', 1);
    }

    // ------------------------------------------------------------------------
    // 설문 아이템 템플릿 기본 추가 (1번째 이후 4건)
    // ------------------------------------------------------------------------
    const $tr = $('#surveyDiv table tr#surveyTmplTr_'+gbSurveyCurrentRowIndex);
    const $targetItem = $tr.find('.surveyList');
    const templateItem = $('#emptySurveyItemTpl').html();
    //$target.find('.surveyItem').length;
    for (var i = 0; i < 4; i++) {
      fnTmplAddEmptyTemplate($targetItem, templateItem, gbSurveyCurrentRowIndex);
    }
    // ------------------------------------------------------------------------
    // 설문유형 라디오 생성
    // ------------------------------------------------------------------------
    const eventSurveyTp = 'eventSurveyTp' + gbSurveyCurrentRowIndex;
    if ($('input[name='+eventSurveyTp+']') == undefined || $('input[name='+eventSurveyTp+']') == null || $('input[name='+eventSurveyTp+']').length <= 0) {
      fnMakeEventSurveyTp(eventSurveyTp);
    }
    // ------------------------------------------------------------------------
    // 노출제목 No 갱신
    // ------------------------------------------------------------------------
    fnChangeSurveyViewIdxNum();
  }

  // ==========================================================================
  // # 템플릿 - 설문 템플릿 삭제
  // ==========================================================================
  function fnEventRemoveSurvey(e) {
    e.stopPropagation();
    const $this = $(this);

    // 대상 TR id
    const $tr     = $this.closest('tr');
    const $trId   =  $tr.attr('id');
    //console.log('# $trId  :: ', $trId);
    let trIdArr = $trId.split('_');
    let index   = trIdArr[1];
    //console.log('# index :: ', index);

    fnKendoMessage({
        type    : 'confirm'
      , message : '삭제하시겠습니까?'
      , ok      : function() {
                    // Count/Incex Set (Idex는 계속 증가되므로 변경 없음)
                    gbSurveyCurrentRowCount = gbSurveyCurrentRowCount - 1;
                    // 템플릿 삭제
                    const $tbody = $this.closest('tbody');
                    const $target = $this.closest('tr');
                    $target.remove();
                    // 설문별 ITEM Index (subIndex) 삭제
                    gbSurveySubCurrentRowIndexMap.delete(index+'');
                    // 노출제목 No 갱신
                    fnChangeSurveyViewIdxNum();
                  }
      , cancel  : function() {}
    });
  }

  // ==========================================================================
  // # 템플릿 - 설문 - 노출번호 갱신(노출용)
  // ==========================================================================
  function fnChangeSurveyViewIdxNum() {
    //console.log('# fnChangeSurveyViewIdxNum Start');
    var surveyViewIdxSpanList = $('[id ^= surveyViewIdxSpan]');
    //console.log('# surveyViewIdxSpanList :: ', JSON.stringify(surveyViewIdxSpanList));
    for (var i = 0; i < surveyViewIdxSpanList.length; i++) {
      //console.log('# surveyViewIdxSpanList[', i, '] :: ', JSON.stringify(surveyViewIdxSpanList[i]));
      var viewIdxSpanId = surveyViewIdxSpanList[i].id;
      $('#'+viewIdxSpanId).text(i+1);
    }
  }

  // ==========================================================================
  // # 템플릿 - 설문 - 보기 추가
  // ==========================================================================
  function fnEventAddSurveyItem(e) {
    //console.log('# fnEventAddSurveyItem Start');
    e.stopPropagation();
    const $this = $(this);
    const $target = $this.closest('.surveyList');
    //console.log('# $target :: ', $target);
    const template = $('#emptySurveyItemTpl').html();
    let trId = $this.closest('tr').attr('id');
    //console.log('# trId :: ', trId);
    let trIdArr = trId.split('surveyTmplTr_');
    let trIndex = trIdArr[1];

    fnTmplAddEmptyTemplate($target, template, trIndex);
  }


  // ==========================================================================
  // # 템플릿 - 설문 - 보기 삭제
  // ==========================================================================
  function fnEventRemoveSurveyItem(e) {
    const $item = $(e.target).closest('.surveyItem');

    fnKendoMessage({
        type    : 'confirm'
      , message : '삭제하시겠습니까?'
      , ok      : function() {
                    $item.remove();
                  }
      , cancel  : function() {}
    });
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 템플릿 - 스탬프
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // ==========================================================================
  // # 템플릿 - 스탬프 - 템플릿 추가
  // ==========================================================================
  function fnTmplAddStamp(eventTp, STAMP_MAX_COUNT) {
    //e.stopPropagation();
    //console.log('# fnTmplAddStamp Start [eventTp][STAMP_MAX_COUNT] :: [', eventTp, '][', STAMP_MAX_COUNT, ']');
    //console.log('# gbStampCurrentRowIndex :: ', gbStampCurrentRowIndex);
    //console.log('##### stampCnt2 :: ', $('#stampCnt2').val());

    // 현재개수
    //const previousRowCount = gbStampCurrentRowCount;
    //console.log('# gbStampCurrentRowCount :: ', gbStampCurrentRowCount);
    //console.log('# STAMP_MAX_COUNT        :: ', STAMP_MAX_COUNT);
    // 스탬프 개수 체크
    if (STAMP_MAX_COUNT > 0) {
      if (gbStampCurrentRowCount >= STAMP_MAX_COUNT) {
        fnMessage('', '스탬프는 최대 ' + STAMP_MAX_COUNT + '개까지 등록 가능합니다.', '');
        return false;
      }
    }
    else {
      fnMessage('', '스탬프 개수를 설정해주세요.', 'stampCnt2');
      return false;
    }

    // Count/Incex Set
    gbStampCurrentRowCount += 1;
    gbStampCurrentRowIndex += 1;
    // ------------------------------------------------------------------------
    // 템플릿 추가
    // ------------------------------------------------------------------------
    const $target = $('#stampBenefitCommonTbody');
    var template = $('#emptystampCommonTpl').html();
    fnTmplAddEmptyTemplate($target, template, gbStampCurrentRowIndex);

    // ------------------------------------------------------------------------
    // 템프릿 - 스탬프번호 라디오 생성
    // ------------------------------------------------------------------------
    if (gbEventTp == 'EVENT_TP.ATTEND') {
      const stampIdxId = 'stampIdx' + gbStampCurrentRowIndex;
      fnMakeEventStampNo(stampIdxId, gbStampCurrentRowIndex, STAMP_MAX_COUNT);
    }

    // ------------------------------------------------------------------------
    // 템프릿 - 당첨혜택 라디오 버튼 생성
    // ------------------------------------------------------------------------
    //console.log('# 템프릿 - 당첨혜택 호출');
    const benefitTpId = 'eventBenefitTp' + gbStampCurrentRowIndex;
    fnMakeEventBenefitTp(benefitTpId, eventTp);

    // ------------------------------------------------------------------------
    // 템프릿 - 스탬프 당첨혜택 쿠폰 그리드 초기화(생성)
    // ------------------------------------------------------------------------
    var couponGridId = 'benefitCouponDynamicGrid' + gbStampCurrentRowIndex;
    var gbIsPopupHiddenYn = true;
    if ($('#'+couponGridId).data('kendoGrid') != undefined ) {
      $('#'+couponGridId).data("kendoGrid").destroy();
      $('#'+couponGridId).empty();
    }
    else {
      fnInitBenefitCouponDynamicGrid(couponGridId, gbStampCurrentRowIndex, gbEvEventId, gbIsPopupHiddenYn);
    }

    // ------------------------------------------------------------------------
    // 템프릿 - 스탬프 당첨혜택 적립금 그리드 초기화(생성)
    // ------------------------------------------------------------------------
    var pointGridId = 'benefitPointDynamicGrid' + gbStampCurrentRowIndex;
    if ($('#'+pointGridId).data('kendoGrid') != undefined ) {
      $('#'+pointGridId).data("kendoGrid").destroy();
      $('#'+pointGridId).empty();
    }
    else {
      fnInitBenefitPointDynamicGrid(pointGridId, gbStampCurrentRowIndex, gbEvEventId);
    }

    // ------------------------------------------------------------------------
    // 템프릿 - 스탬프 당첨혜택 적립금 리스트 생성
    // ------------------------------------------------------------------------
    fnMakePointList('pmPointId'+gbStampCurrentRowIndex);

    // ------------------------------------------------------------------------
    // 템프릿 - 노출제목 No 갱신
    // ------------------------------------------------------------------------
    fnChangeStampViewIdxNum();
  }

  // ==========================================================================
  // # 템플릿 - 스탬프 - 혜택구간 템플릿 삭제
  // ==========================================================================
  function fnEventRemoveStampAttendance(e) {
    e.stopPropagation();
    const $target = $(e.target).closest('tr');
    const trId    = $target.attr('id');

    // 임시
    fnDeleteStampImg(trId);

    fnKendoMessage({
        type    : 'confirm'
      , message : '삭제하시겠습니까?'
      , ok      : function() {
                    // Count/Incex Set (Idex는 계속 증가되므로 변경 없음)
                    gbStampCurrentRowCount = gbStampCurrentRowCount - 1;
                    // 템플릿 삭제
                    $target.remove();
                    // 노출제목 No 갱신
                    fnChangeStampViewIdxNum();
                    // 이미지파일정보 제거
                    fnDeleteStampImg(trId);
                  }
      , cancel  : function() {}
    });
  }

  // ==========================================================================
  // # 템플릿 - 스탬프 - 노출번호 갱신(노출용)
  // ==========================================================================
  function fnChangeStampViewIdxNum() {
    //console.log('# fnChangeStampViewIdxNum Start');
    var stampViewIdxSpanList = $('[id ^= stampViewIdxSpan]');
    //console.log('# stampViewIdxSpanList :: ', JSON.stringify(stampViewIdxSpanList));
    for (var i = 0; i < stampViewIdxSpanList.length; i++) {
      //console.log('# stampViewIdxSpanList[', i, '] :: ', JSON.stringify(stampViewIdxSpanList[i]));
      var viewIdxSpanId = stampViewIdxSpanList[i].id;
      $('#'+viewIdxSpanId).text(i+1);
    }
  }

  // ==========================================================================
  // # 템플릿 - 스탬프 - 스탬프이미지 정보 제거
  // ==========================================================================
  function fnDeleteStampImg(trId) {
    //console.log('# fnDeleteStampImg Start [', trId, '][', gbEventTp, ']');
    let trIdArr = trId.split('stampTmplTr_');
    let trIdx   = trIdArr[1];
    //console.log('# trIdx :: ', trIdx);

    // 혜택영역 스탬프 기본 이미지 제거
    if (gbImgStampDefaultMap != undefined && gbImgStampDefaultMap != null) {
      gbImgStampDefaultMap.delete(trIdx);
    }

    // 혜택영역 스탬프 체크 이미지 제거
    if (gbImgStampCheckMap != undefined && gbImgStampCheckMap != null) {
      gbImgStampCheckMap.delete(trIdx);
    }

    // 혜택영역 스탬프 아이콘 이미지 제거
    if (gbEventTp == 'EVENTP_TP.MISSION') {
      // 미션인 경우만
      if (gbImgStampIconMap != undefined && gbImgStampCheckMap != null) {
        gbImgStampIconMap.delete(trIdx);
      }
    }
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 템플릿 - 룰렛
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // ==========================================================================
  // # 템플릿 - 룰렛 - 템플릿 추가
  // ==========================================================================
  function fnTmplAddRoulette(eventTp, ROULETTE_MAX_COUNT) {
    //console.log('# fnTmplAddRoulette Start [eventTp][ROULETTE_MAX_COUNT] :: [', eventTp, '][', ROULETTE_MAX_COUNT, ']');
    //e.stopPropagation();

    // 룰렛 개수 체크
    if (ROULETTE_MAX_COUNT > 0) {
      if (dgRouletteCurrentRowCount >= ROULETTE_MAX_COUNT) {
        fnMessage('', '룰렛은 최대 ' + ROULETTE_MAX_COUNT + '개까지 등록 가능합니다.', '');
        return false;
      }
    }
    else {
      fnMessage('', '룰렛 개수를 설정해주세요.', 'rouletteCnt');
      return false;
    }

    // Count/Incex Set
    dgRouletteCurrentRowCount += 1;
    gbRouletteCurrentRowIndex += 1;
    // ------------------------------------------------------------------------
    // 템플릿 추가
    // ------------------------------------------------------------------------
    const $target = $('#rouletteBenefitTbody');
    var template = $('#emptyRouletteTpl').html();
    fnTmplAddEmptyTemplate($target, template, gbRouletteCurrentRowIndex);

    // ------------------------------------------------------------------------
    // 템플릿 - 당첨혜택 라디오 버튼 생성
    // ------------------------------------------------------------------------
    const benefitTpId = 'eventBenefitTp' + gbRouletteCurrentRowIndex;
    fnMakeEventBenefitTp(benefitTpId, eventTp);

    // ------------------------------------------------------------------------
    // 템프릿 - 룰렛 당첨혜택 쿠폰 그리드 초기화(생성)
    // ------------------------------------------------------------------------
    var couponGridId = 'benefitCouponDynamicGrid' + gbRouletteCurrentRowIndex;
    var gbIsPopupHiddenYn = true;
    if ($('#'+couponGridId).data('kendoGrid') != undefined ) {
      $('#'+couponGridId).data("kendoGrid").destroy();
      $('#'+couponGridId).empty();
    }
    else {
      fnInitBenefitCouponDynamicGrid(couponGridId, gbRouletteCurrentRowIndex, gbEvEventId, gbIsPopupHiddenYn);
    }

    // ------------------------------------------------------------------------
    // 템프릿 - 룰렛 당첨혜택 적립금 그리드 초기화(생성)
    // ------------------------------------------------------------------------
    var pointGridId = 'benefitPointDynamicGrid' + gbRouletteCurrentRowIndex;
    if ($('#'+pointGridId).data('kendoGrid') != undefined ) {
      $('#'+pointGridId).data("kendoGrid").destroy();
      $('#'+pointGridId).empty();
    }
    else {
      fnInitBenefitPointDynamicGrid(pointGridId, gbRouletteCurrentRowIndex, gbEvEventId);
    }

    // ------------------------------------------------------------------------
    // 템프릿 - 룰렛 당첨혜택 적립금 리스트 생성
    // ------------------------------------------------------------------------
    fnMakePointList('pmPointId'+gbRouletteCurrentRowIndex);

    // ------------------------------------------------------------------------
    // 숫자만입력
    // ------------------------------------------------------------------------
    // * 당첨확률
//    fnInputValidationForNumber('awardRt'+gbRouletteCurrentRowIndex);
    fnInputValidationByRegexp('awardRt'+gbRouletteCurrentRowIndex, /[^0-9.]/g);
    // * 당첨인원
    fnInputValidationForNumber('awardMaxCnt'+gbRouletteCurrentRowIndex);

    // .(마침표)가 양끝에 있는 케이스를 처리, 입력값을 Slider에 적용
    $('#awardRt' +gbRouletteCurrentRowIndex).off('blur').on('blur',function(e){
        var value = $(this).val();
        var regExp = /^\.|\.$/;
        if(regExp.test(this.value)){
            $(this).val(value.replace('.',''));
        }
    });

    // 소수점 둘째자리까지의 실수만 입력 허용
    $('#awardRt' +gbRouletteCurrentRowIndex).off('input').on('input',function(e){
        var value = $(this).val();
        var regExp = /^\d*.?\d{0,3}$/;
        if(!regExp.test(this.value)){
            $(this).val(value.substring(0,value.length-1));
        }
    });

  }

  // # 템플릿 Function End
  // ##########################################################################


  // ==========================================================================
  // # 댓글 분류설정 추가버튼 - 추가 : [일반]
  // ==========================================================================
  function fnEventBtnAddCommentCode(e) {
    //console.log('# fnEventBtnAddCommentCode Start');
    const $input = $('#inputValue');
    const inputValue = $input.val().trim();
    //const $target = $('#commentTpList');

    // ------------------------------------------------------------------------
    // 분류값 입력 체크
    // ------------------------------------------------------------------------
    if (!inputValue.length) {
      // TODO fnM
      fnMessage('', '분류 값을 입력해주세요.', 'inputValue');
      //$input.focus();
      return false;
    }

    // ------------------------------------------------------------------------
    // 분류값 중복 체크
    // ------------------------------------------------------------------------
    var isDup = false;
    //const commentCodeValue = [];

    $('.commentCodeValue').each(function(){
      if (inputValue == $(this).text()) {
        isDup = true;
        return false;
      }
      //console.log('# 기 등록값 :: ', $(this).text());
      //commentCodeValue.push($(this).text());
    });

    if (isDup == true) {
      fnMessage('', '중복된 분류값입니다. 분류값을 확인하세요.', 'inputValue');
      return false;
    }
    // 데이터 add
    //gbCommentCodeMaps.set(inputValue, inputValue);
    //// 노출 추가
    //fnTemplates.commentTp.add({'keyword': inputValue });
    fnEventAddCommentCode(inputValue);

    $input.val('').focus();
    //console.log('# after add :: ', Array.from(gbCommentCodeMaps.values()));
  }

  // ==========================================================================
  // # # 댓글 분류설정 추가 : [일반]
  // ==========================================================================
  function fnEventAddCommentCode(inputValue) {

    gbCommentCodeMaps.set(inputValue, inputValue);
    // 노출 추가
    fnTemplates.commentTp.add({'keyword': inputValue });
  }

  // ==========================================================================
  // # 댓글 분류설정 - 제거 : [일반]
  // ==========================================================================
  function fnEventRemoveCommentCode(e) {
    //console.log('# fnEventRemoveCommentCode Start');
    e.stopPropagation();
    e.stopImmediatePropagation();
    const $target = $(e.target).closest('.eventMgm__commentTpListItem');

    //

    fnKendoMessage({
        type    : 'confirm'
      , message : '삭제하시겠습니까?'
      , ok      : function() {
                    // 데이터 del
                    var delValue = $target.find('.commentCodeValue').text();
                    gbCommentCodeMaps.delete(delValue);
                    // 노출 제거
                    $target.remove();
                    //console.log('# after del :: ', Array.from(gbCommentCodeMaps.values()));
                  }
      , cancel  : function() {}
    });
  }


  // ==========================================================================
  // # 댓글 분류설정 노출 제어
  // ==========================================================================
  function fnEventCommentCodeYn(e) {

    var commentCodeYn = $('input[name=commentCodeYn]:checked').val();
    //console.log('# commentCodeYn :: ', commentCodeYn);

    if (commentCodeYn == 'Y') {
      // 분류추가 영역 노출
      $('#commentCodeValueDiv').show();
      $('#commentCodeValueListDiv').show();
    }
    else {
      // 분류추가 영역 숨김
      $('#commentCodeValueDiv').hide();
      $('#commentCodeValueListDiv').hide();
    }
  }

  // ==========================================================================
  // # 댓글 분류설정 영역(Tr) 노출제어
  // ==========================================================================
  function fnEventCommentCodeYnTr(eventTp, eventDrawTp) {
    //console.log('# eventTp     :: ', eventTp);
    //console.log('# eventDrawTp :: ', eventDrawTp);
    if (eventTp == 'EVENT_TP.EXPERIENCE') {
      // 이벤트유형 : 체험단
      //console.log('# eventDrawTp :: ', eventDrawTp);
      if (eventDrawTp == 'EVENT_DRAW_TP.FIRST_COME') {
        // 당첨자설정 : 선착순
        $('#commentCodeYnTr').hide();
        //console.log('# 댓글 분류설정 영역(Tr) 숨김');
      }
      else {
        $('#commentCodeYnTr').show();
        //console.log('# 댓글 분류설정 영역(Tr) 노출');
      }
    }
  }

  // ==========================================================================
  // # 당첨자 혜택 라디오 클릭 이벤트
  //   - js__benefits 클래스르 가진 당첨자 혜택 영역 안에 있는 라디오 태그 이벤트 핸들러
  // ==========================================================================
  function fnEventChangeEventBenefitTp(e) {
    //console.log('# fnEventChangeEventBenefitTp Start');
    e.stopPropagation();
    e.stopImmediatePropagation();
    const $this = $(this);
    const $benefits = $this.closest('.js__benefits');
    const id = $(this).val();
    //console.log('# >>> id :: ', id);
    // 노출처리
    const $allContents = $benefits.find('[data-parent]');
    const $content = $benefits.find('[data-parent="' + id + '"]');
    $allContents.removeClass('show');
    $content.addClass('show');

    // ------------------------------------------------------------------------
    // 당첨자설정에 따른 경품/응모 노출 처리
    // ------------------------------------------------------------------------
    let eventDrawTp = $('input[name=eventDrawTp]:checked').val(); // 당첨자설정(관리자추첨/즉시당첨 등)
    fnBenefitItemGiftEnterView(eventDrawTp);
  }

  // ==========================================================================
  // # 당첨자설정에 따른 경품/응모 노출 처리
  // ==========================================================================
  function fnBenefitItemGiftEnterView(eventDrawTp) {
    //let eventDrawTp = $('input[name=eventDrawTp]:checked').val(); // 당첨자설정(관리자추첨/즉시당첨 등)
    //console.log('# gbEventTp   :: ', gbEventTp);
    //console.log('# eventDrawTp :: ', eventDrawTp);
    let eventBenefitTp = $('input[name=eventBenefitTp]:checked').val();   // 선택된 이벤트혜택유형(경품/응모)

    if(gbEventTp == 'EVENT_TP.NORMAL') {
      // 이벤트유형 : 일반
      if (eventDrawTp == 'EVENT_DRAW_TP.ADMIN') {
        // 당첨자설정 : 관리자추첨
        $('#benefitItemGiftDiv').hide();
        $('#benefitItemEnterDiv').hide();
      }
      else if (eventDrawTp == 'EVENT_DRAW_TP.AUTO') {
        // 당첨자설정 : 즉시당첨
        if (eventBenefitTp == 'EVENT_BENEFIT_TP.GIFT') {
          // 이벤트혜택 : 경품
          $('#benefitItemGiftDiv').show();
          $('#benefitItemEnterDiv').hide();
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.ENTER') {
          // 이벤트혜택 : 응모
          $('#benefitItemGiftDiv').hide();
          $('#benefitItemEnterDiv').show();
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.POINT') {
          // 이벤트혜택 : 적립금
          $('#benefitItemGiftDiv').hide();
          $('#benefitItemEnterDiv').hide();
        }
      }
    }
    else if(gbEventTp == 'EVENT_TP.SURVEY') {
      // 이벤트유형 : 설문
      if (eventDrawTp == 'EVENT_DRAW_TP.ADMIN') {

        $('#benefitItemGiftDiv').hide();
        $('#benefitItemEnterDiv').hide();
      }
      else if (eventDrawTp == 'EVENT_DRAW_TP.AUTO') {
        // 당첨자설정 : 즉시(EVENT_DRAW_TP.AUTO)
        if (eventBenefitTp == 'EVENT_BENEFIT_TP.GIFT') {
          // 이벤트혜택 : 경품
          $('#benefitItemGiftDiv').show();
          $('#benefitItemEnterDiv').hide();
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.ENTER') {
          // 이벤트혜택 : 응모
          $('#benefitItemGiftDiv').hide();
          $('#benefitItemEnterDiv').show();
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.POINT') {
          // 이벤트혜택 : 적립금
          $('#benefitItemGiftDiv').hide();
          $('#benefitItemEnterDiv').hide();
        }
      }
    }
  }


  // ==========================================================================
  // # 당첨자혜택 별 노출 처리 - 조회처리용
  // ==========================================================================
  function fnChangeEventBenefitTp(tagId, eventBenefitTp) {
    //console.log('# fnChangeEventBenefitTp eventBenefitTp :: ', eventBenefitTp);
    const $benefits = $('#'+tagId).closest('.js__benefits');
    const $allContents = $benefits.find('[data-parent]');
    const $content = $benefits.find('[data-parent="' + eventBenefitTp + '"]');
    $allContents.removeClass('show');
    $content.addClass('show');
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 순서 정리 대상 Start

  // ==========================================================================
  // # 당첨자수 입력 노출 제어
  // ==========================================================================
  function fnEventChangeSetWinnerTp(e) {
    //console.log('# fnEventChangeSetWinnerTp Start');
    //console.log('# this :: ', this);
    //console.log('# e :: ', e);
    //console.log('# eventDrawTp :: ', $('input[name=eventDrawTp]:checked').val());
    var value = $('input[name=eventDrawTp]:checked').val();
    var normalEventTpValue = $('input[name=normalEventTp]:checked').val();
    //const $this = $(this);
    //const value = $this.val();
    //console.log('# 당첨자 설정 :: ', value);

    // 선착순 당첨일 경우
    if (value === 'EVENT_DRAW_TP.FIRST_COME') {
      $('#firstWinnerTpDiv').show();
      $('#immediatleyWinDiv').hide();
    }
    else {
      $('#firstWinnerTpDiv').hide();
      // 참여방법 : 응모버튼 && 당첨자 설정 : 즉시당첨 경우 당첨확률,예상참여자수 표시
      if( normalEventTpValue == 'NORMAL_EVENT_TP.BUTTON' && value == 'EVENT_DRAW_TP.AUTO'){
        $('#immediatleyWinDiv').show();
      }
    }
  }

  // ==========================================================================
  // # 이벤트처리 - 스탬프총개수 변경
  // ==========================================================================
  function fnEventChangeStampTotal(e) {
    //console.log('# fnEventChangeStampTotal Start');

    // 컨펌 메시지
    var confirmMsg = '';
    // 변경전 스탬프개수
    var befStampTotalCnt = gbStampCurrentRowCount;
    //console.log('# befStampTotalCnt :: ', befStampTotalCnt);

    const $e = e;

    // ------------------------------------------------------------------------
    // 스탬프 개수 설정 변경 시 초기화 처리
    // ------------------------------------------------------------------------
    var innerFunction = function() {
      // ----------------------------------------------------------------------
      // 기존 스탬프 영역 초기화
      // ----------------------------------------------------------------------
      $('#stampBenefitCommonTbody').empty();
      // Count/Incex Set
      gbStampCurrentRowCount = 0;
      gbStampCurrentRowIndex = 0;

      // ----------------------------------------------------------------------
      // 스탬프 템플릿 생성
      // ----------------------------------------------------------------------
      var rowCount = $e.dataItem.CODE;
      //console.log('# ### rowCount :: ', rowCount);

      for (var i = 0; i < rowCount; i++) {
        // 스탬프 템플릿 생성
        fnTmplAddStamp(gbEventTp, rowCount);
      }

      // ----------------------------------------------------------------------
      // 스탬프 혜택영역 이미지 Array 초기화
      // ----------------------------------------------------------------------
      // 스탬프 기본 이미지
      gbImgStampDefaultMap  = new Map();
      // 스탬프 체크 이미지
      gbImgStampCheckMap    = new Map();
      // 스탬프 아이콘 이미지
      gbImgStampIconMap     = new Map();

    };

    if (befStampTotalCnt > 0) {
      // ----------------------------------------------------------------------
      // 화면에 생성된 것이 있는 경우 메시지 노출
      // ----------------------------------------------------------------------

      // 템플릿 생성
      fnKendoMessage({
          type    : 'confirm'
        , message : '스탬프 정보가 모두 초기화 됩니다. 초기화 하시겠습니까?'
        , ok      : function() {
                      innerFunction();
                    }
        , cancel  : function() {
                      // 원래 값으로 Set
                      $('#stampCnt2').data('kendoDropDownList').value(befStampTotalCnt);
                    }
      });
    }
    else {
      // ----------------------------------------------------------------------
      // 화면에 생성된 것이 없는 경우
      // ----------------------------------------------------------------------
      innerFunction();
    }

  }


  // ==========================================================================
  // # 이벤트처리 - 룰렛 개수 설정 변경
  // ==========================================================================
  function fnEventChangeRouletteCnt(e) {
    //console.log('# fnEventChangeRouletteCnt Start');

    // 변경전 룰렛개수
    var befRouletteTotalCnt = dgRouletteCurrentRowCount;
    //console.log('# befRouletteTotalCnt :: ', befRouletteTotalCnt);

    const $e = e;

    var innerFunction = function() {
      // ----------------------------------------------------------------------
      // 기존 룰렛 영역 초기화
      // ----------------------------------------------------------------------
      $('#rouletteBenefitTbody').empty();
      // Count/Incex Set
      dgRouletteCurrentRowCount = 0;
      gbRouletteCurrentRowIndex = 0;

      // ----------------------------------------------------------------------
      // 룰렛 템플릿 생성
      // ----------------------------------------------------------------------
      var rowCount = $e.dataItem.CODE;
      //console.log('# ### rowCount :: ', rowCount);

      for (var i = 0; i < rowCount; i++) {
        // 룰렛 템플릿 생성
        fnTmplAddRoulette(gbEventTp, rowCount);
      }
    };

    if (befRouletteTotalCnt > 0) {
      // ----------------------------------------------------------------------
      // 화면에 생성된 것이 있는 경우 메시지 노출
      // ----------------------------------------------------------------------

      // 템플릿 생성
      fnKendoMessage({
          type    : 'confirm'
        , message : '룰렛 정보가 모두 초기화 됩니다. 초기화 하시겠습니까?'
        , ok      : function() {
                      innerFunction();
                      // 이벤트 제한고객 룰렛설정 초기화
                      $('#exceptionUserRouletteCnt').data('kendoDropDownList').value('');
                    }
        , cancel  : function() {
                      // 원래 값으로 Set
                      $('#rouletteCnt').data('kendoDropDownList').value(befRouletteTotalCnt);
                    }
      });
    }
    else {
      // ----------------------------------------------------------------------
      // 화면에 생성된 것이 없는 경우
      // ----------------------------------------------------------------------
      innerFunction();
    }

  }

  // ==========================================================================
  // # 룰렛 개수 변경 이벤트
  // ==========================================================================
  function fnEventRouletteCnt(e) {

    var rouletteCnt = $('#rouletteCnt').val();
    fnSetExceptionUserRouletteCnt(rouletteCnt);
  }

  // ==========================================================================
  // # 이벤트 제한고객 룰렛설정 생성
  // ==========================================================================
  function fnSetExceptionUserRouletteCnt(rouletteCnt) {
    //console.log('# rouletteCnt :: ', rouletteCnt);
    var exceptionUserRouletteArray = new Array();
    for (var i = 0; i < rouletteCnt; i++) {
      const exceptionUserRouletteObj = {
          CODE: i+1+''
        , NAME: i+1+''
      };
      exceptionUserRouletteArray.push(exceptionUserRouletteObj);
    }
    //console.log('# exceptionUserRouletteArray :: ', JSON.stringify(exceptionUserRouletteArray));
    $('#exceptionUserRouletteCnt').data('kendoDropDownList').setDataSource({
      data: exceptionUserRouletteArray
    });
  }

  // ==========================================================================
  // # 당첨자 혜택 초기화
  // ==========================================================================
  function initBenefitItems() {
    // $('[data-parent]').hide();
    // $('[data-parent="EVENT_BENEFIT_TP.COUPON"]').show();

    $('[data-parent]').removeClass('show');
    $('[data-parent="EVENT_BENEFIT_TP.COUPON"]').addClass('show');
  }

  // ==========================================================================
  // # JQuery 템플릿 초기화 함수
  //  @param {object} el : 템플릿이 추가될 타겟, 제이쿼리 엘리먼트
  //  @param {string} template : 데이터 바인딩 시 사용할 제이쿼리 템플릿, html 태그 또는 제이쿼리 엘리먼트
  // ==========================================================================
  function createTemplateItem({ el, template }) {
    if (!el || !template) {
      console.warn('error', el, template);
      return {};
    }

    const $el = $(el);
    const _template = $(template).html();

    return {
        el      : $el
      , template: $.template(_template)
      , render  : function (data, callback) {
                    const tmpl = $.tmpl(this.template, data);
                    this.el.empty();
                    tmpl.appendTo(this.el);

                    if (typeof callback === 'function') {
                      callback.call(this, data);
                    }
                  }
      , add     : function (data, callback) {
                    const tmpl = $.tmpl(this.template, data);
                    tmpl.appendTo(this.el);

                    if (typeof callback === 'function') {
                      callback.call(this, data);
                    }
                  }
    };

    function isJqueryElement(obj) {
      return obj instanceof jQuery && isHTMLElement(obj[0]);
    }

    function isHTMLElement(obj) {
      try {
        return obj instanceof HTMLElement;
      }
      catch (e) {
        return (
          typeof obj                === 'object' &&
          obj.nodeType              === 1 &&
          typeof obj.style          === 'object' &&
          typeof obj.ownerDocument  === 'object'
        );
      }
    }
  }

  // 순서 정리 대상 End
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // # 화면제어 functions End
  // ##########################################################################


  // ##########################################################################
  // # 공통 Func. Start
  // eventMgmCommon.js 에 위치
  // # 공통 Func. End
  // ##########################################################################


