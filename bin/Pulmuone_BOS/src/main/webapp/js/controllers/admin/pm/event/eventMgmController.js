/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 이벤트 등록/수정
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2020.01.12    김승우        최초 생성
 * @ 2020.01.26    dgyoun        기능 적용
 * @
 ******************************************************************************/
'use strict';
var ctgryGridDs, ctgryGridOpt, ctgryGrid;

var ilCtgryStd1;
var ilCtgryStd2;
var ilCtgryStd3;
var ilCtgryStd4;

var LAST_PAGE = null; // 최종 페이지 (페이징 기억 관련)
var PAGE_SIZE = 20;
var CUR_SERVER_URL = fnGetServerUrl().mallUrl;

var gbBeditable     = false;  // 테스트용임, 반듯이 false로 변경해야 함
var gbPageParam = ''; // 넘어온 페이지파라미터
var gbMode      = ''; // 모드
var gbEvEventId = ''; // 이벤트PK
var gbEventTp   = ''; // 이벤트유형
var gbStatusSe  = ''; // 진행상태

var gbDelIdArr;     // 삭제 Id 리스트
var gbDelGruopId;   // 삭제상품그룹ID
var gbDetail;       // 상세조회결과 전역변수

// ----------------------------------------------------------------------------
// 파일업로드
//-----------------------------------------------------------------------------
var publicStorageUrl = fnGetPublicStorageUrl();   // 이미지 업로드되는 public 저장소 url 경로
var bannerImageUploadMaxLimit = 5120000;          // 배너 이미지 첨부 가능 최대 용량 ( 단위 : byte )

// [단건]
// 배너이미지 PC
var gbImgBnrPcPath;                 // 배너이미지 Pc - 풀경로
var gbImgBnrPcOriginNm;             // 배너이미지 Pc - 원본파일명
// 배너이미지 Mobile
var gbImgBnrMobilePath;             // 배너이미지 Mobile - 풀경로
var gbImgBnrMobileOriginNm;         // 배너이미지 Mobile - 원본파일명

//스탬프 기본이미지
var gbImgStampDefaultPath;          //  - 풀경로
var gbImgStampDefaultOriginNm;      //  - 원본파일명
// 스탬프 체크이미지
var gbImgStampCheckPath;            //  - 풀경로
var gbImgStampCheckOriginNm;        //  - 원본파일명
// 스탬프 배경이미지
var gbImgStampBgPath;               //  - 풀경로
var gbImgStampBgOriginNm;           //  - 원본파일명

// 룰렛 시작버튼이미지
var gbImgRouletteStartBtnPath;      //  - 풀경로
var gbImgRouletteStartBtnOriginNm;  //  - 원본파일명
// 룰렛 화살표이미지
var gbImgRouletteArrowPath;         //  - 풀경로
var gbImgRouletteArrowOriginNm;     //  - 원본파일명
// 룰렛 배경이미지
var gbImgRouletteBgPath;            //  - 풀경로
var gbImgRouletteBgOriginNm;        //  - 원본파일명
// 룰렛 이미지
var gbImgRoulettePath;              //  - 풀경로
var gbImgRouletteOriginNm;          //  - 원본파일명

// [다건]
//설문 영역 이미지 (다건-다건)
//- 구조 : Map > Map > Map
//- 샘플 : [{1, [{1, {'Path', '설문1의 1번째 Path내용', 'OriginNm', '설문1의 1번째 OriginNm내용'}}, {2, {'Path', '설문1의 1번째 Path내용', 'OriginNm', '설문1의 1번째 OriginNm내용'}}]}, {2, [{1, {'Path', '설문2의 1번째 Path내용', 'OriginNm', '설문1의 2번째 OriginNm내용'}}, {2, {'Path', '설문2의 1번째 Path내용', 'OriginNm', '설문2의 2번째 OriginNm내용'}}]}....]
var gbImgSurveyMap = new Map();

// 스탬프 혜택영역 이미지 (다건) :
//  - 구조 : Map > Map
//  - 샘플 : [{1, [{'Path', '1번째 Path내용'}, {'OriginNm', '1번째 OriginNm내용'}]}, {2, [{'Path', '2번째 Path내용'}, {'OriginNm', '2번째 OriginNm내용'}]}]
var gbImageType = '';
// 스탬프 기본 이미지
var gbImgStampDefaultMap  = new Map();
// 스탬프 체크 이미지
var gbImgStampCheckMap    = new Map();
// 스탬프 아이콘 이미지
var gbImgStampIconMap       = new Map();


// ----------------------------------------------------------------------------
// 템플릿관련
// ----------------------------------------------------------------------------
//var fnTemplates   = {};   // {}
//var surveyList    = null; // []
//var maxRowCount   = 1;

// 설문 idx
var gbSurveyCurrentRowCount = 0;
var gbSurveyCurrentRowIndex = 0;
var SURVEY_MAX_COUNT    = 10;
var gbSurveySubCurrentRowIndexMap = new Map();   // 설문별 ITEM Index (subIndex) [{설문index1, subIdx Last}, {설문index2, subIdx Last}...]
// 스탬프
var gbStampCurrentRowCount = 0;
var gbStampCurrentRowIndex = 0;
//var STAMP_MAX_COUNT      = 10;
// 룰렛
var dgRouletteCurrentRowCount = 0;
var gbRouletteCurrentRowIndex = 0;
//var ROULETTE_MAX_COUNT      = 10;

// ----------------------------------------------------------------------------
// 일반기획전 - 그룹
// ----------------------------------------------------------------------------
var gbGroupIdx = 0;   // 현재 그룹 마지막 idx
var gbGroupCnt = 0;
var MAX_GROUP_CNT = 20;

$(document).ready(function() {

  // ==========================================================================
  // # Initialize Page Call
  // ==========================================================================
  fnInitialize();

  // sheetJs 스크립트 추가
  let myScript = document.createElement("script");
  myScript.setAttribute("src", "js/lib/sheetJs/xlsx.full.min.js");
  document.body.appendChild(myScript);

  // ==========================================================================
  // # Initialize PageR
  // ==========================================================================
  function fnInitialize() {
    // ------------------------------------------------------------------------
    // 화면기본설정
    // ------------------------------------------------------------------------
    $scope.$emit('fnIsMenu', { flag: 'true' });

    fnPageInfo({
        PG_ID   : 'eventMgm'
      , callback  : fnUI
    });

    // ------------------------------------------------------------------------
    // 페이지파라미터
    // ------------------------------------------------------------------------
    gbPageParam = fnGetPageParam();
    //console.log('# gbPageParam :: ', JSON.stringify(gbPageParam));

    // ------------------------------------------------------------------------
    // 이벤트유형 Set
    // ------------------------------------------------------------------------
    gbEventTp = gbPageParam.eventTp;
    if(fnIsEmpty(gbEventTp) == true) {
      gbEventTp = 'EVENT_TP.NORMAL';
    }
    // ------------------------------------------------------------------------
    // 이벤트PK
    // ------------------------------------------------------------------------
    gbEvEventId = gbPageParam.evEventId;
    // ------------------------------------------------------------------------
    // 모드
    // ------------------------------------------------------------------------
    gbMode = gbPageParam.mode;
    // 모드값이 없으면 기본값 insert
    if(fnIsEmpty(gbMode) == true) {
      gbMode = 'insert';
    }
    // 이벤트ID가 없으면 기본값 iInsert
    if(fnIsEmpty(gbEvEventId) == true || gbEvEventId == '0' || gbEvEventId <= 0) {
      gbMode = 'insert';
    }

    // ------------------------------------------------------------------------
    // 상위타이틀
    // ------------------------------------------------------------------------
    //if (gbEventTp != null && gbEventTp != 'null') {
    //
    //  if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
    //    $('#pageTitleSpan').text('체험단 이벤트 등록/수정');
    //  }
    //}

  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

    // OptionBox js
    importScript('/js/controllers/admin/pm/event/eventMgmCommon.js', function() {
      // 그리드 Js
      importScript('/js/controllers/admin/pm/event/eventMgmGrid.js', function() {
        // 공통 js
        importScript('/js/controllers/admin/pm/event/eventMgmTemplate.js', function() {
          // 등록/수정/상세 DataSet js
          importScript('/js/controllers/admin/pm/event/eventMgmDataSet.js', function() {

            fnInitButton();           // Initialize Button --------------------

            fnInitOptionBox();        // Initialize Option Box ---------------- // eventMgmCommon.js 에 위치

            fnInitTemplates();        // 템플릿 설정 --------------------------

            fnInitEvent();            // 이벤트 설정 --------------------------

            fnInitPre();              // 기본설정 - 조회전 --------------------

            fnInitScreen();           // 화면노출설정 -------------------------

            fnInitGrid();             // Grid ---------------------------------

            fnSearch();               // 조회 ---------------------------------

            fnCoverageInit();         // 적용범위

            //fnInitPost();             // 기본설정 - 조회후 ------------------

          }); // End of importScript('/js/controllers/admin/pm/event/eventMgmDataSet.js', function()

        }); // End of importScript('/js/controllers/admin/pm/event/eventMgmTemplate.js', function()

      }); // End of importScript('/js/controllers/admin/pm/event/eventMgmGrid.js', function()

    }); // End of importScript('/js/controllers/admin/pm/event/eventMgmCommon.js', function()

  }

  // ==========================================================================
  // # 초기화 - 버튼
  // ==========================================================================
  function fnInitButton() {
    //console.log('fnInitButton start');
    //$('#fnBtnSave, #fnBtnGoList, #fnDelete').kendoButton();

    if (gbMode == 'insert') {
      // ----------------------------------------------------------------------
      // 등록
      // ----------------------------------------------------------------------
      $('#fnBtnDelete').hide();                           // 삭제버튼:숨김
      $('#btnEventJoin').attr('disabled', true);          // 참여/당첨버튼:비활성
      $('#btnEventPreview').attr('disabled', true);       // 이벤트 미리보기 버튼:비활성
    }
    else {
      // ----------------------------------------------------------------------
      // 수정
      // ----------------------------------------------------------------------
      $('#fnBtnDelete').show();                           // 삭제버튼:노출
      $('#btnEventJoin').attr('disabled', false);         // 참여/당첨버튼:활성
      $('#btnEventPreview').attr('disabled', false);       // 이벤트 미리보기 버튼:활성
    }

  }

  // ==========================================================================
  // # 초기화 - 옵션박스
  // ==========================================================================
  // eventMgmCommon.js 에 위치

  // ==========================================================================
  // # 템플릿 초기화
  // ==========================================================================
  function fnInitTemplates() {
    //console.log('# fnInitTemplates Start');

    // 접근 권한 설정 템플릿 초기화
    fnTemplates.userGroupTp = createTemplateItem({
        el      : '#userGroupList'
      , template: '#userGroupTpl'
    });

    // 댓글 분류 설정 템플릿 초기화 : [일반][체험단]
    fnTemplates.commentTp = createTemplateItem({
        el      : '#commentTpList'
      , template: '#commentTpTpl'
    });

    // 설문 템플릿 초기화 : [설문]
    fnTemplates.survey = createTemplateItem({
        el      : '#surveyDiv tbody'
      , template: '#surveyTmpl'
    });

    // 스탬프(미션) 템플릿 초기화 : [스탬프(미션)]
    fnTemplates.stampCommon = createTemplateItem({
        el      : '#stampBenefitCommonTbody'
      , template: '#stampCommonTpl'
    });

    // 룰렛 템플릿 초기화 : [룰렛]
    fnTemplates.roulette = createTemplateItem({
        el      : '#rouletteBenefitTbody'
      , template: '#rouletteTpl'
    });

    // 일반 이벤트 -> 응모버튼 : 신규가입자 한정 여부 체크 템플릿
    fnTemplates.roulette = createTemplateItem({
        el      : '#checkNewUserTplList'
      , template: '#checkNewUserTpl'
    });
  }


  // ==========================================================================
  // # 초기화 - 이벤트
  // ==========================================================================
  function fnInitEvent() {
    //console.log('fnInitEvent Start');

    // document
    const $document = $(document);

    //  // ------------------------------------------------------------------------
    //  // FIXME 제이쿼리 tmpl 테스트 : 추후 삭제
    //  // ------------------------------------------------------------------------
    //  $('#testBinding').on('click', function (e) {
    //    // fnTemplates.survey.render({ list: [{}] }); //빈 데이터
    //    fnTemplates.survey.render({ list: surveyList });
    //    const rowCount = surveyList ? surveyList.length : 0;
    //
    //    setCurrentRowCount(rowCount);
    //    setMaxRowCount(rowCount);
    //  });


    // ------------------------------------------------------------------------
    // # 입력제한
    // ------------------------------------------------------------------------
    $('.lengthCheck').on('keyup', function (e) {
      //const $titleLength = $(this).parent().find('.currentInput-length');
      // ----------------------------------------------------------------------
      // 길이 제한 : class 에 lengthCheck 추가, maxLength= 설정
      // ----------------------------------------------------------------------
      const MAX_LENGTH = !!this.maxLength ? this.maxLength : null;
      const _value = $(this).val();
      if (MAX_LENGTH && _value.length > MAX_LENGTH) {
        $(this).val(_value.slice(0, MAX_LENGTH));
      }
      //$titleLength.innerHTML = $(this).val().length;
      // 문자 제한
      if (this.name.startsWith('btnColorCd')) {
        const regExp = /[^0-9a-fA-F\#]/gi;
        const _value = $(this).val();

        if (_value.match(regExp)) {
          $(this).val(_value.replace(regExp, ''));
        }
      }
      //if($titleLength.length) {
      //   $titleLength.text($(this).val().length);
      //}
    });

    // ------------------------------------------------------------------------
    // 체크박스변경(전체선택관련)
    // ------------------------------------------------------------------------
    fbCheckboxChange();

    // ------------------------------------------------------------------------
    // 숫자만입력
    // ------------------------------------------------------------------------
    // * 스탬프 지급조건 금액
    fnInputValidationForNumber('orderPrice');
    // * 체험단 당천자설정 선착순당첨 인원
    fnInputValidationForNumber('firstComeCnt');
    // * 쿠폰그리드 지급수량
    fnInputValidationForNumber('couponCount');
    // * 쿠폰그리드 총 당첨 수량
    fnInputValidationForNumber('couponTotalCount');
    // * 적립금그리드 총 당첨 수량
    fnInputValidationForNumber('totalWinCount');
    // * 경품 총 당첨 수량
    fnInputValidationForNumber('totalWinCntGift');
    // 당첨자 설정 : 당첨확률
    fnInputValidationByRegexp("awardRt", /[^0-9.]/g);
    // 당첨자 설정 : 예상참여자수
    fnInputValidationByRegexp('expectJoinUserCnt', /[^0-9]/g);
    // 당첨자 설정 : 예상참여자수
    fnInputValidationByRegexp('expectJoinUserCntRoulette', /[^0-9]/g);
    // 참여조건 : 최소 주문건수
    fnInputValidationForNumber('orderCnt');
    // 참여조건 : 최수구매 금액
    fnInputValidationForNumber('normalOrderPrice');
    // * 댓글분류 제한 (한글/영문대소문자/숫자/공백)
    fnInputValidationForAlphabetHangulNumberSpace('inputValue');
    // * Style ID (영문/숫자)
    fnInputValidationByRegexp('styleId', /[^a-zA-Z0-9]/g);

    // .(마침표)가 양끝에 있는 케이스를 처리, 입력값을 Slider에 적용
    $('#awardRt').off('blur').on('blur',function(e){
        var value = $(this).val();
        var regExp = /^\.|\.$/;
        if(regExp.test(this.value)){
            $(this).val(value.replace('.',''));
        }
    });

    // 소수점 둘째자리까지의 실수만 입력 허용
    $('#awardRt').off('input').on('input',function(e){
        var value = $(this).val();
        var regExp = /^\d*.?\d{0,3}$/;
        if(!regExp.test(this.value)){
            $(this).val(value.substring(0,value.length-1));
        }
    });

    // ------------------------------------------------------------------------
    // 신규가입자 한정 여부 이벤트
    // ------------------------------------------------------------------------
    $("#checkNewUserYn").on("change", function (e) {
        if($("#checkNewUserYn").getRadioVal() == "N") {
            $("#checkNewUserNoti").hide();
        }else{
            $("#checkNewUserNoti").show();
        }
    });


    // ----------------------------------------------------------$($--------------
    // 이벤트유형 콤보변경 이벤트
    // ------------------------------------------------------------------------
    $('#eventTp').on('change', function (e) {

      // 변경전 이벤트유형
      var befEventTp = gbEventTp;

      fnKendoMessage({
          type    : 'confirm'
        , message : '화면의 모든 내용이 초기화됩니다.'
        , ok      : function() {
                      // 이벤트유형 Set
                      gbEventTp = $('#eventTp').val();
                      //console.log('# changed gbEventTp :: ', gbEventTp);

                      // ------------------------------------------------------
                      // 폼 초기화
                      // ------------------------------------------------------
                      $('#inputForm').formClear(true);

                      // 화면초기화(기본값 및 항목노출)
                      fnInitScreen();
                    }
        , cancel  : function() {
                      //console.log('# befEventTp :: ', befEventTp);
                      // 원래 값으로 Set
                      $('#eventTp').data('kendoDropDownList').value(befEventTp);
                    }
      });

    });

    // ------------------------------------------------------------------------
    // 임직원전용여부 선택 이벤트
    // ------------------------------------------------------------------------
    $('#evEmployeeTp').on('click', function(e){
      fnEventEvEmployeeTp();
    });

    // ------------------------------------------------------------------------
    // 접근권한설정(회원등급)(C) : [공통]
    // ------------------------------------------------------------------------
    // 접근 권한 설정 추가 버튼 클릭 이벤트
    $('#btnAddUserGroup').on('click', function(e) {
      //console.log('# 회원등급 추가 Click');
      e.stopPropagation();
      e.stopImmediatePropagation();
      fnBtnAddUserGroup(e);
    });

    // 접근 권한 설정 삭제 버튼 클릭 이벤트
    $document.on('click', '.js__remove__userGroupBtn', fnRemoveUserGroup);

    // ------------------------------------------------------------------------
    // 당첨자혜택 선택 이벤트
    // ------------------------------------------------------------------------
    //$('#eventDrawTp').on('click', function(e) {
    //  let eventBenefitTp = $('input[name=eventBenefitTp]:checked').val()
    //  if (eventBenefitTp == 'EVENT_BENEFIT_TP.GIFT' || eventBenefitTp == 'EVENT_BENEFIT_TP.ENTER') {
    //    // 당첨자혜택유형이 경품/응모인 경우만
    //    let eventDrawTp = $('input[name=eventDrawTp]:checked').val(); // 당첨자설정(관리자추첨/즉시당첨 등)
    //    fnBenefitItemGiftEnterView(eventDrawTp);
    //  }
    //});

    // ------------------------------------------------------------------------
    // 당첨자 설정 변경 이벤트
    // ------------------------------------------------------------------------
    $('#setWinnerTp input[type="radio"]').on('change', fnEventChangeSetWinnerTp);

    // ------------------------------------------------------------------------
    // 댓글 허용여부 라디오선택 이벤트 : [일반]
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // 참여방법 라디오선택 이벤트 : [일반]
    // ------------------------------------------------------------------------
//    $('#normalEventTp input[type="radio"]').on('change', function(e) {
    $('#normalEventTp').on('change', function(e) {
      // 댓글 허용여부에 따른 하위 상세정보 항목 노출 설정
//      var commentYnVal = $('input[name=commentYn]:checked').val();
      // 참여방법에 따른 하위 상세정보 항목 노출 설정
      var normalEventVal = $('input[name=normalEventTp]:checked').val();

      if (normalEventVal != 'NORMAL_EVENT_TP.NONE') {
        // 댓글허용여부 : Y
//        fnDisplayScreen(gbEventTp);
        // 참여방법 : 응모버튼(BUTTON), 댓글응모(COMMENT)
        fnDisplayScreen(gbEventTp);


        // 참여방법 : 응모버튼 ( 신규가입자 한정 여부 )
        if(normalEventVal == 'NORMAL_EVENT_TP.BUTTON') {
            $('#checkNewUserTr').show();
        } else {
            $('#checkNewUserTr').hide();
        }

        // 참여방법 : 댓글응모(COMMENT)
        if(normalEventVal == 'NORMAL_EVENT_TP.COMMENT'){
            $('#commentCodeYnTr').show();
//            $('#commentCodeYn').attr('disabled', false);          // 참여/당첨버튼:비활성
        }else{
            $('#commentCodeYnTr').hide();
        }

        // 참여방법에 따른 하위 상세정보 항목 노출 설정
        var joinConditionVal = $('input[name=joinCondition]:checked').val();
        // 참여조건 : 주문고객
        if(joinConditionVal == 'JOIN_CONDITION.ORDER'){
          // 적용범위
          $('#goodsCoverageDiv').show();
        } else {
          $('#goodsCoverageDiv').hide();
        }

        // 참여횟수 설정(ID당) 셀렉트박스 값 설정
        let eventDrawTp = $('input[name=eventDrawTp]:checked').val();
        fnInitEventJoinTp(gbEventTp, eventDrawTp, '');

      }
      else {
        // 댓글허용여부 : N
//        fnDisplayScreen(gbEventTp+'.NO');
        // 참여방법 : 응모없음(NORMAL_EVENT_TP.NONE)
        fnDisplayScreen(gbEventTp+'.NO');

        // 적용범위
        $('#goodsCoverageDiv').hide();

      }

      // 입직원참여여부 처리
      fnEventEvEmployeeTp();
    });

    // ------------------------------------------------------------------------
    // 참여조건 라디오선택 이벤트
    // ------------------------------------------------------------------------
    $('#joinCondition').on('change', function(e) {
      // 참여방법에 따른 하위 상세정보 항목 노출 설정
      var joinConditionVal = $('input[name=joinCondition]:checked').val();

      // 참여횟수 설정(ID당) 셀렉트박스 값 설정
      let eventDrawTp = $('input[name=eventDrawTp]:checked').val();
      fnInitEventJoinTp(gbEventTp, eventDrawTp, '');

      // 참여조건 : 주문고객
      if(joinConditionVal == 'JOIN_CONDITION.ORDER'){
        $('#joinConditionDiv').show();

        // 적용범위
        $('#goodsCoverageDiv').show();
        $("#goodsCoverageType").data("kendoDropDownList").enable(true);

        $("#goodsIncludeYn").closest(".k-widget").show();
        $("#goodsIncludeYn").data('kendoDropDownList').value("Y");
        $("#goodsIncludeYn").data("kendoDropDownList").enable(false);

        //$('#ctgryGrid').gridClear(true);

      }else{
        $('#joinConditionDiv').hide();

        // 적용범위
        $('#goodsCoverageDiv').hide();
        $('#ctgryGrid').gridClear(true);
        $('#categoryCoverageTypeDiv').hide();
        $("#brandCoverageTypeDiv").hide();
      }
    });

    // ------------------------------------------------------------------------
    // 댓글 분류설정 라디오선택 이벤트
    // ------------------------------------------------------------------------
    $('#commentCodeYn').on('change', function(e) {
      e.stopPropagation();
      e.stopImmediatePropagation();
      // 분류설정 분류값 노출제어
      fnEventCommentCodeYn(e);
    });

    // ------------------------------------------------------------------------
    // 댓글 분류추가 버튼 클릭 이벤트
    // ------------------------------------------------------------------------
    $('#btnAddCommentCode').on('click', function(e) {
      e.stopPropagation();
      e.stopImmediatePropagation();
      fnEventBtnAddCommentCode(e);
    });

    // ------------------------------------------------------------------------
    // 댓글 분류삭제 버튼 클릭 이벤트
    // ------------------------------------------------------------------------
    $document.on('click', '.js__remove__commentTpBtn', fnEventRemoveCommentCode);

    // ------------------------------------------------------------------------
    // 당첨자 혜택 라디오 클릭 변경 이벤트 : function 방식 안됨
    // ------------------------------------------------------------------------
    $document.on('change', '.js__benefits input[type="radio"]', fnEventChangeEventBenefitTp);

    // ------------------------------------------------------------------------
    // 설문 - 보기 - 이미지파일 업로드 (kendo 미사용) : [설문]
    // ------------------------------------------------------------------------
    $('#ng-view').on('change', '.surveyItem input[type="file"]', fileSelectFunction);

    // ------------------------------------------------------------------------
    // 설문 > 설문 테이블 행 추가 이벤트
    // ------------------------------------------------------------------------
    $('#fnBtnAddSurvey').on('click', function(e) {
      e.stopPropagation();
      fnTmplAddSurveyEmpty();
    });

    // ------------------------------------------------------------------------
    // 설문 > 설문 테이블 삭제 이벤트
    // ------------------------------------------------------------------------
    $document.on('click', '.js__remove__survey', fnEventRemoveSurvey);

    // ------------------------------------------------------------------------
    // 설문 > 설문 보기 추가 버튼 이벤트
    // ------------------------------------------------------------------------
    $('#surveyDiv').on('click', '.js__add__surveyItem', fnEventAddSurveyItem);
    // 아래는 중복 이벤트 발생 이슈 있음
    //$document.on('click', '.js__add__surveyItem', fnEventAddSurveyItem);

    // ------------------------------------------------------------------------
    // 설문 > 설문 보기 삭제 버튼 이벤트
    // ------------------------------------------------------------------------
    $document.on('click', '.js__remove__surveyItem', fnEventRemoveSurveyItem);

    // ------------------------------------------------------------------------
    // 설문 아이템 직접입력 체크박스 클릭
    // ------------------------------------------------------------------------
    $document.on('click', '.js__click__directInputYn', function(e) {
      console.log('# 직접입력 클릭...');
      const $this = $(this);
      const $id   = $this.attr('id');
      console.log('# $id :: ', $id);
      let indexKey = '';
      let indexKeyArr = $id.split('directInputYn');
      if (indexKeyArr != undefined && indexKeyArr != null && indexKeyArr.length >= 2) {
        indexKey = indexKeyArr[1];
      }
      let itemId = 'item' + indexKey;
      console.log('# itemId :: ', itemId);
      let itemVal = $('#'+itemId).val();
      console.log('# itemVal :: ', itemVal);

    //  const $idArr = $id.split('directInputYn');
    //  console.log('# $idArr[1] :: ', $idArr[1]);
    //  const $indexArr = $idArr[1].split('_');
    //  let index       =  $indexArr[0];
    //  let subIndex    =  $indexArr[1];
    //  console.log('# index     :: ', index);
    //  console.log('# subIndex  :: ', subIndex);
    });

    // ------------------------------------------------------------------------
    // 스탬프 총 개수 변경 이벤트
    // ------------------------------------------------------------------------
    $('#stampCnt2').data('kendoDropDownList').bind('select', function (e) {

      fnEventChangeStampTotal(e);
    });

    // ------------------------------------------------------------------------
    // 스탬프 - 당첨혜택설정 - 이미지파일 업로드 (kendo 미사용) : [스탬프(출석)][스탬프(미션)][스탬프(구매)]
    // ------------------------------------------------------------------------
    $('#ng-view').on('change', '.stampItem input[type="file"]', fileSelectFunction);

    // ------------------------------------------------------------------------
    // 스탬프(출석) : 당첨혜택 구간설정 > 혜택구간 테이블 행 추가 이벤트
    // ------------------------------------------------------------------------
    $('#fnBtnAddStampBenefitRow').on('click', function(e) {
      e.stopPropagation();
      fnTmplAddStamp(gbEventTp, $('#stampCnt2').val());
    });

    // ------------------------------------------------------------------------
    // 스탬프(출석) : 당첨혜택 구간설정 > 혜택구간 테이블 행 삭제 이벤트
    // ------------------------------------------------------------------------
    $document.on('click', '.js__remove__stampAttend', fnEventRemoveStampAttendance);

    // ------------------------------------------------------------------------
    // 룰렛 개수 설정 변경 이벤트
    // ------------------------------------------------------------------------
    $('#rouletteCnt').data('kendoDropDownList').bind('select', function (e) {
      fnEventChangeRouletteCnt(e);
    });

    // ------------------------------------------------------------------------
    // 룰렛 개수 변경 이벤트
    // ------------------------------------------------------------------------
    $('#rouletteCnt').on('change', function(e) {
      e.stopPropagation();
      e.stopImmediatePropagation();
      fnEventRouletteCnt(e);
    });

    // ------------------------------------------------------------------------
    // 체험단 - 당첨자설정 변경 이벤트 (관리자추첨, 즉시당첨, 선착순당첨)
    // ------------------------------------------------------------------------
    $('#eventDrawTp').on('click', function(e) {

      e.stopPropagation();
      e.stopImmediatePropagation();
      // 당첨자수 입력 노출 제어
      fnEventChangeSetWinnerTp(e);
      // 댓글분류설정 TR영역 노출제어
      let eventDrawTp = $('input[name=eventDrawTp]:checked').val();
      fnEventCommentCodeYnTr(gbEventTp, eventDrawTp);

      let eventBenefitTp = $('input[name=eventBenefitTp]:checked').val(); // 당첨자설정(관리자추첨/즉시당첨 등)
      if (eventBenefitTp == 'EVENT_BENEFIT_TP.GIFT' || eventBenefitTp == 'EVENT_BENEFIT_TP.ENTER') {
        // 당첨자혜택유형이 경품/응모인 경우만
        fnBenefitItemGiftEnterView(eventDrawTp);
      }
      // 참여횟수 설정(ID당) 셀렉트박스 값 설정
      fnInitEventJoinTp(gbEventTp, eventDrawTp, '');

      var normalEventTpValue = $('input[name=normalEventTp]:checked').val();

      if(normalEventTpValue != 'NORMAL_EVENT_TP.NONE' && eventDrawTp == 'EVENT_DRAW_TP.AUTO'){
          $('#immediatleyWinDiv').show();
      }else{
          $('#immediatleyWinDiv').hide();
          $('#awardRt').val(null);
          $('#expectJoinUserCnt').val(null);
      }

    });

    // ------------------------------------------------------------------------
    // 파일삭제 - 이미지 이벤트
    // ------------------------------------------------------------------------
    $('#ng-view').on('click', '.fileUpload__removeBtn' , function(e) {
      e.preventDefault();
      e.stopPropagation();

      const $this = $(this);
      const $container = $this.closest('.fileUpload-container');
      //console.log('# $container :: ', $container);

      // ----------------------------------------------------------------------
      // 첨부 삭제가능 체크
      // ----------------------------------------------------------------------
      var $wrapper  = $($container[0]).find('.fileUpload__imgWrapper');
      var $img      = $wrapper.find('img');
      var removeId  = $img.attr('id');
      //console.log('# removeId :: ', removeId);

      if (gbMode != 'insert') {
        if (gbStatusSe == 'ING' || gbStatusSe == 'END') {
          if (removeId != 'imgBannerPcView' && removeId != 'imgBannerMobileView') {
            console.log('# 진행예정인 경우만 수정이 가능합니다.');
            //fnMessage('', '진행예정인 경우만 수정이 가능합니다.[', removeId, ']', '');
            return false;
          }
        }
      }

      fnKendoMessage({
          type    : 'confirm'
        , message : '삭제하시겠습니까?'
        , ok      : function() {
                      fnRemoveAllImagesContainer($container[0]);
                    }
        , cancel  : function() {}
      });
    });

  } // End of fnInitEvent

  // ==========================================================================
  // # 초기화 - 화면초기화(기본값 및 항목노출)
  // ==========================================================================
  function fnInitScreen() {
    //console.log('# fnInitScreen Start');

    // ------------------------------------------------------------------------
    // 폼 초기화
    // ------------------------------------------------------------------------
    //$('#inputForm').formClear(true);

    // 폼 초기화에 따른 날짜 기본값 Set
    //$('#startDe').data('kendoDatePicker').value(gbTodayDe);
    //$('#endDe').data('kendoDatePicker').value('2999-12-31');
    //$('#endHour').data('kendoDropDownList').select(23);
    //$('#endMin').data('kendoDropDownList').select(59);
    // 노출범위설정 전체 선택
    $("input[name=goodsDisplayType]:checkbox").prop("checked", true);
    // 기간 종료 후 사용 자동종료
    $("input[name=timeOverCloseYn]:checkbox").prop("checked", true);
    // 기간 종료 후 사용 자동종료(체험단 이벤트)
    $("input[name=experienceTimeOverCloseYn]:checkbox").prop("checked", true);

    // 이벤트분류 Set (폼 초기화로 인해 날라가므로 다시 Set)
    $("#eventTp").data("kendoDropDownList").value(gbEventTp);

    // 이벤트상세
    $("#detlHtmlPc").data("kendoEditor").value('');
    $("#detlHtmlMo").data("kendoEditor").value('');
    $("#detlHtmlPc2").data("kendoEditor").value('');
    $("#detlHtmlMo2").data("kendoEditor").value('');

    // ------------------------------------------------------------------------
    // 추가 기본값 설정
    // ------------------------------------------------------------------------
    // 댓글분류설정 기본값 N 설정
    $('input:radio[name="commentCodeYn"]:input[value="N"]').prop("checked", true);

    // 신규가입자 한정 여부 기본값 N설정
    $('input:radio[name="checkNewUserYn"]:input[value="N"]').prop("checked", true);

    // ------------------------------------------------------------------------
    // 화면 노출/숨김 처리
    // ------------------------------------------------------------------------
    fnDisplayScreen(gbEventTp);

    // ------------------------------------------------------------------------
    // 참여횟수 설정(ID당)
    // ------------------------------------------------------------------------
    let eventDrawTp = $('input[name=eventDrawTp]:checked').val(); // 당첨자설정(관리자추첨/즉시당첨 등)
    fnInitEventJoinTp(gbEventTp, eventDrawTp, '');

    // ------------------------------------------------------------------------
    // 당첨자설정유형 이벤트유형별 노출/숨김 처리
    // ------------------------------------------------------------------------
    fnSetEventDrawTp(gbEventTp);

    // ------------------------------------------------------------------------
    // 이미지 변수 초기화
    // ------------------------------------------------------------------------
    // [단건]
    // 배너이미지 PC
    gbImgBnrPcPath                = '';       // 배너이미지 Pc - 풀경로
    gbImgBnrPcOriginNm            = '';       // 배너이미지 Pc - 원본파일명
    // 배너이미지 Mobile
    gbImgBnrMobilePath            = '';       // 배너이미지 Mobile - 풀경로
    gbImgBnrMobileOriginNm        = '';       // 배너이미지 Mobile - 원본파일명

    //스탬프 기본이미지
    gbImgStampDefaultPath         = '';       //  - 풀경로
    gbImgStampDefaultOriginNm     = '';       //  - 원본파일명
    // 스탬프 체크이미지
    gbImgStampCheckPath           = '';       //  - 풀경로
    gbImgStampCheckOriginNm       = '';       //  - 원본파일명
    // 스탬프 배경이미지
    gbImgStampBgPath              = '';       //  - 풀경로
    gbImgStampBgOriginNm          = '';       //  - 원본파일명

    // 룰렛 시작버튼이미지
    gbImgRouletteStartBtnPath     = '';       //  - 풀경로
    gbImgRouletteStartBtnOriginNm = '';       //  - 원본파일명
    // 룰렛 화살표이미지
    gbImgRouletteArrowPath        = '';       //  - 풀경로
    gbImgRouletteArrowOriginNm    = '';       //  - 원본파일명
    // 룰렛 배경이미지
    gbImgRouletteBgPath           = '';       //  - 풀경로
    gbImgRouletteBgOriginNm       = '';       //  - 원본파일명
    // 룰렛 이미지
    gbImgRoulettePath             = '';       //  - 풀경로
    gbImgRouletteOriginNm         = '';       //  - 원본파일명

    // [다건]
    // 설문 영역 이미지
    gbImgSurveyMap = new Map();
    // 스탬프 혜택영역 이미지 (다건)
    gbImageType = '';
    // 스탬프 기본 이미지
    gbImgStampDefaultMap  = new Map();
    // 스탬프 체크 이미지
    gbImgStampCheckMap    = new Map();
    // 스탬프 아이콘 이미지
    gbImgStampIconMap       = new Map();


    // ------------------------------------------------------------------------
    // 이미지 초기화 : View, 변수
    // ------------------------------------------------------------------------
    $('.fileUpload-container').each(function() {
      fnRemoveAllImagesContainer(this);
    });

    // ------------------------------------------------------------------------
    // 사용자그룹 초기화
    // ------------------------------------------------------------------------
    $('.eventMgm__commentTpList').find('.js__remove__userGroupList').each(function(){
      fnRemoveAllUserGroup(this);
    });

    // ------------------------------------------------------------------------
    // 그리드 초기화
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // 쿠폰그리드 초기화
    // ------------------------------------------------------------------------
    if (benefitCouponGrid != undefined && benefitCouponGrid != null) {
      benefitCouponGrid.destroy();
      $("#benefitCouponGrid").empty();
    }

    if(gbEventTp == 'EVENT_TP.SURVEY'){
        fnInitBenefitCouponGrid(true);
    }else{
        fnInitBenefitCouponGrid();
    }

    // ------------------------------------------------------------------------
    // 적립금그리드 초기화
    // ------------------------------------------------------------------------
    if (benefitPointGrid != undefined && benefitPointGrid != null) {
      benefitPointGrid.destroy();
      $("#benefitPointGrid").empty();
    }

    if(gbEventTp == 'EVENT_TP.SURVEY'){
        fnInitBenefitPointGrid(true);
    }else{
        fnInitBenefitPointGrid();
    }
    // ------------------------------------------------------------------------
    // 체험단상품그리드 초기화
    // ------------------------------------------------------------------------
    if (experienceGrid != undefined && experienceGrid != null) {
      experienceGrid.destroy();
      $("#experienceGrid").empty();
    }
    fnInitExperienceGrid();

    // ------------------------------------------------------------------------
    // * 동적 그리드
    // ------------------------------------------------------------------------
    // TODO 쿠론그리드(n) 초기화 등등....


  } // End of fnInitScreen

  // ==========================================================================
  // # 이미지 삭제 : View, 변수
  // ==========================================================================
  function fnRemoveAllImagesContainer(container) {
    //console.log('# fnRemoveAllImagesContainer Start ');
    var $container = $(container);
    var $wrapper  = $container.find('.fileUpload__imgWrapper');
    var $img      = $wrapper.find('img');
    var $message  = $container.find('.fileUpload__message');
    var $title    = $container.find('.fileUpload__title');
    var removeId  = $img.attr('id');
    //console.log('# $img :: ', $img);
    //console.log('# $message :: ', $message);
    //console.log('# $title :: ', $title);
    //console.log('# removeId', removeId);

    // ------------------------------------------------------------------------
    // 이미지 View 삭제
    // ------------------------------------------------------------------------
    $img.attr('src', '');
    $title.text('');
    $wrapper.hide();
    $title.hide();
    $message.show();

    // ------------------------------------------------------------------------
    // 이미지 변수 삭제
    // ------------------------------------------------------------------------
    fnRemoveImageInfo(removeId);
  }

  // ==========================================================================
  // # 접근권한 선택영역 삭제
  // ==========================================================================
  function fnRemoveAllUserGroup(listItem) {
    var $listItem = $(listItem);

    $listItem.closest('.js__remove__userGroupList__item').remove();
    // ------------------------------------------------------------------------
    // 접근권한 변수 삭제
    // ------------------------------------------------------------------------
    gbUserGroupMaps = new Map();
  }

  // ==========================================================================
  // # 초기화 - 화면초기화(기본값 및 항목노출)
  // ==========================================================================
  function fnRemoveImageInfo(removeId) {
    //console.log('# fnRemoveImageInfo Start [', removeId, ']');
    if (removeId == 'imgBannerPcView') {
      //  배너이미지 Pc
      gbImgBnrPcPath                  = '';       // 풀경로
      gbImgBnrPcOriginNm              = '';       // 원본파일명
    }
    else if (removeId == 'imgBannerMobileView') {
      //  배너이미지 Mobile
      gbImgBnrMobilePath              = '';       // 풀경로
      gbImgBnrMobileOriginNm          = '';       // 원본파일명
    }
    else if (removeId == 'imgStampDefaultView') {
      //  스탬프 기본이미지
      gbImgStampDefaultPath           = '';       // 풀경로
      gbImgStampDefaultOriginNm       = '';       // 원본파일명
    }
    else if (removeId == 'imgStampCheckView') {
      //  스탬프 체크이미지
      gbImgStampCheckPath             = '';       // 풀경로
      gbImgStampCheckOriginNm         = '';       // 원본파일명
    }
    else if (removeId == 'imgStampBgView') {
      //  스탬프 배경이미지
      gbImgStampBgPath                = '';       // 풀경로
      gbImgStampBgOriginNm            = '';       // 원본파일명
    }
// TODO 아미지 ID 변경할 것
    else if (removeId == 'imgTODOView') {
      //  룰렛 시작버튼이미지
      gbImgRouletteStartBtnPath       = '';       // 풀경로
      gbImgRouletteStartBtnOriginNm   = '';       // 원본파일명
    }
    else if (removeId == 'imgTODOView') {
      //  룰렛 화살표이미지
      gbImgRouletteArrowPath          = '';       // 풀경로
      gbImgRouletteArrowOriginNm      = '';       // 원본파일명
    }
    else if (removeId == 'imgTODOView') {
      //  룰렛 배경이미지
      gbImgRouletteBgPath             = '';       // 풀경로
      gbImgRouletteBgOriginNm         = '';       // 원본파일명
    }
    else if (removeId == 'imgTODOView') {
      //  룰렛 이미지
      gbImgRoulettePath               = '';       // 풀경로
      gbImgRouletteOriginNm           = '';       // 원본파일명
    }
    else {
      // ----------------------------------------------------------------------
      // 동적이미지
      // ----------------------------------------------------------------------
      //console.log('# removeId :: ', removeId);
      if (fnIsEmpty(removeId) == true) {
        return;
      }

      if (removeId.startsWith('imgSurvey')) {
        // --------------------------------------------------------------------
        // 설문 아이템 이미지
        // --------------------------------------------------------------------
        let imgInfoArr  = removeId.split('imgSurveyView');
        let indexArr    = imgInfoArr[1].split('_');
        let indexKey    = indexArr[0];
        let subIndexKey = indexArr[1];
        //console.log('# [indexKey][subIndexKey] :: [', indexKey, '][', subIndexKey, ']');

        if (gbImgSurveyMap != undefined && gbImgSurveyMap != null) {
          if (gbImgSurveyMap.get(indexKey+'') != undefined && gbImgSurveyMap.get(indexKey+'') != null) {
            gbImgSurveyMap.get(indexKey+'').delete(subIndexKey+'');
          }
        }
        //  -------------------------------------------------------------------
        // 임시 로그 Start
        // --------------------------------------------------------------------
        let surveyImgTotalCnt = 0;
        if (gbImgSurveyMap != undefined && gbImgSurveyMap != null && gbImgSurveyMap.size > 0) {
          //console.log('# 설문이미지그룹 개수 :: ', gbImgSurveyMap.size);
          for (let [key, vlaue] of gbImgSurveyMap) {
            if (vlaue != undefined && vlaue != null && vlaue.size > 0) {
              //console.log('# [', key, ']별 아이템이미지 개수 :: ', vlaue.size);
              for (let [subKey, subValue] of vlaue) {
                //console.log('# imgInfo[', key, '][', subKey, '] :: ', JSON.stringify(Array.from(subValue)));
                surveyImgTotalCnt++;
              }
            }
          }
        }
        //console.log('# surveyImgTotalCnt :: ', surveyImgTotalCnt);
        // 임시 로그 End
      }
      else if (removeId.startsWith('imgStampDefault')) {
        // --------------------------------------------------------------------
        // 당첨혜택영역 스탬프 기본 이미지
        // --------------------------------------------------------------------
        let imgInfoArr  = removeId.split('imgStampDefaultView');
        let indexKey    = imgInfoArr[1];

        if (gbImgStampDefaultMap != undefined && gbImgStampDefaultMap != null) {
          gbImgStampDefaultMap.delete(indexKey+'');
        }
      }
      else if (removeId.startsWith('imgStampCheck')) {
        // --------------------------------------------------------------------
        // 당첨혜택영역 스탬프 체크 이미지
        // --------------------------------------------------------------------
        let imgInfoArr  = removeId.split('imgStampCheckView');
        let indexKey    = imgInfoArr[1];

        if (gbImgStampCheckMap != undefined && gbImgStampCheckMap != null) {
          gbImgStampCheckMap.delete(indexKey+'');
        }
      }
      else if (removeId.startsWith('imgStampIcon')) {
        // --------------------------------------------------------------------
        // 당첨혜택영역 스탬프 아이콘 이미지
        // --------------------------------------------------------------------
        let imgInfoArr  = removeId.split('imgStampIconView');
        let indexKey    = imgInfoArr[1];

        if (gbImgStampIconMap != undefined && gbImgStampIconMap != null) {
          gbImgStampIconMap.delete(indexKey+'');
        }
      }
    }
  }



  // ==========================================================================
  // # 초기화 - 기본 설정 - 조회 전
  // ==========================================================================
  function fnInitPre() {
    //console.log('# fnInitPre Start');

    // ------------------------------------------------------------------------
    // 조회조건
    // ------------------------------------------------------------------------
    //console.log('# ================================================');
    //console.log('# [상세] 조회조건 Set');
    //console.log('# ================================================');
    //console.log('# ------------------------------------------------');
    let searchInfo    = sessionStorage.getItem('searchInfo');
    let searchInfoObj = new Object();
    //console.log('# searchInfo     :: ', searchInfo);
    if (searchInfo != null) {
      searchInfoObj = JSON.parse(searchInfo);
      searchInfoObj.isFromDetl = 'Y';
      //console.log('# searchInfoObj     :: ', JSON.stringify(searchInfoObj));
      sessionStorage.setItem('searchInfo', JSON.stringify(searchInfoObj));
    }
  }

  // ==========================================================================
  // # 초기화 - 그리드
  // ==========================================================================
  function fnInitGrid() {
    //console.log('# fnInitGrid Start');

    // ------------------------------------------------------------------------
    // 상세정보 당첨자 혜택 쿠폰 그리드 : [일반][설문][체험단]
    // ------------------------------------------------------------------------
    // 당첨자 혜택 - 쿠폰그리드
    fnInitBenefitCouponGrid();
    // 당첨자 혜택 - 적립금그리드
    fnInitBenefitPointGrid();
    // 체험단 - 체험상품그리드
    fnInitExperienceGrid();

  }

  // ==========================================================================
  // # 초기화 - 기본 설정 - 조회 후
  // ==========================================================================
  function fnInitPost() {
    //console.log('# fnInitPost Start');
  }

  // ==========================================================================
  // # 초기화 - 값
  // ==========================================================================
  function fnClear() {
    //console.log('[fnClear]');

    // 화면초기화(기본값 및 항목노출)
    fnInitScreen();
  }


  // ##########################################################################
  // # 조회 Start
  // ==========================================================================
  // # 조회
  // ==========================================================================
  function fnSearch() {
    //console.log('# fnSearch Start');
    if (gbMode == 'update' && gbEvEventId != null && gbEvEventId != '' && gbEvEventId != 0 && gbEvEventId != '0') {
      fnAjaxSearch();
    }

    // ------------------------------------------------------------------------
    // 당첨자선정일자/후기작성일자 시작 일/시/분 비활성 처리
    //  - kendo 로딩 지연으로 인해 화면 최뒷단에서 실행 해야 함
    // ------------------------------------------------------------------------
    $('#selectStartDe').data('kendoDatePicker').enable(false);        // kendoDatePicker
    $('#selectStartHour').data('kendoDropDownList').enable(false);    // kendoDropDownList
    $('#selectStartMin').data('kendoDropDownList').enable(false);     // kendoDropDownList
    $('#feedbackStartDe').data('kendoDatePicker').enable(false);      // kendoDatePicker
    $('#feedbackStartHour').data('kendoDropDownList').enable(false);  // kendoDropDownList
    $('#feedbackStartMin').data('kendoDropDownList').enable(false);   // kendoDropDownList

  }

  // # 조회 End
  // ##########################################################################

  // ##########################################################################
  // # 변경/삭제 Start
  // ##########################################################################
  // ==========================================================================
  // # 삭제 - 상품그룹.상품
  // ==========================================================================
  function fnBtnEventGroupDetlDel(evEventGroupDetlId, groupId) {

    // 모드
    gbMode      = 'delete.group.detl';    // 삭제.상품그룹.상품
    // 삭제대상
    gbDelIdArr = new Array() ;
    gbDelIdArr.push(evEventGroupDetlId);
    // 그룹ID
    gbDelGruopId = groupId;

    // 제거Obj Set
    var gridId = 'groupGoodsGrid'+gbDelGruopId;
    var grid     = $('#'+gridId).data('kendoGrid');
    //var dataItem      = grid.dataItem(grid.select());
    //var ds          = grid.dataSource;
    var dataObj = new Object();
    dataObj.selectRow        = grid.select();
    dataObj.selectedDataItem  = grid.dataItem(grid.select());
    var removeRowArr   = new Array();
    removeRowArr.push(dataObj);
    var removeObj      = new Object();
    removeObj.removeRowArr  = removeRowArr;
    removeObj.gridId        = gridId;

    // 삭제 실행
    fnSaveUpd(removeObj, null);
  }

  // ==========================================================================
  // # 변경/삭제 처리
  // ==========================================================================
  function fnSaveUpd(removeObj, dataItem) {

    var url         = '';
    var inParam     = '';
    var confirmMsg  = '';
    var isExeAjax   = false;

    if (gbMode == 'delete.select.goods') {
      // 삭제.골라담기.상품
      url = '/admin/pm/exhibit/delExhibitSelectGoods';
      inParam = {'evExhibitSelectGoodsIdListString'  : JSON.stringify(gbDelIdArr)};
      confirmMsg  = '<div>삭제하시겠습니까?</div>';
      // Ajax실행여부 (DB처리여부)
      if (gbDelIdArr != undefined && gbDelIdArr != null && gbDelIdArr.length > 0) {
        isExeAjax   = true;
      }
    }
    else if (gbMode == 'delete.select.add.goods') {
      // 삭제.골라담기.추가상품
      url = '/admin/pm/exhibit/delExhibitSelectAddGoods';
      inParam = {'evExhibitSelectAddGoodsIdListString'  : JSON.stringify(gbDelIdArr)};
      confirmMsg  = '<div>삭제하시겠습니까?</div>';
      // Ajax실행여부 (DB처리여부)
      if (gbDelIdArr != undefined && gbDelIdArr != null && gbDelIdArr.length > 0) {
        isExeAjax   = true;
      }
    }
    else if (gbMode == 'delete.gift.goods') {
      // 삭제.증정행사.상품
      url = '/admin/pm/exhibit/delExhibitGiftGoods';
      inParam = {'evExhibitGiftGoodsIdListString'  : JSON.stringify(gbDelIdArr)};
      confirmMsg  = '<div>삭제하시겠습니까?</div>';
      // Ajax실행여부 (DB처리여부)
      if (gbDelIdArr != undefined && gbDelIdArr != null && gbDelIdArr.length > 0) {
        isExeAjax   = true;
      }
    }
    else if (gbMode == 'delete.gift.target.goods') {
      // 삭제.증정행사.적용대상상품

      url = '/admin/pm/exhibit/delExhibitGiftTargetGoods';
      inParam = {'evExhibitGiftTargetGoodsIdListString'  : JSON.stringify(gbDelIdArr)};
      confirmMsg  = '<div>삭제하시겠습니까?</div>';
      // Ajax실행여부 (DB처리여부)
      if (gbDelIdArr != undefined && gbDelIdArr != null && gbDelIdArr.length > 0) {
        isExeAjax   = true;
      }
    }
    else if (gbMode == 'delete.gift.target.brand') {
      // 삭제.증정행사.적용대상브랜드
      url = '/admin/pm/exhibit/delExhibitGiftTargetBrand';
      inParam = {'evExhibitGiftTargetBrandIdListString'  : JSON.stringify(gbDelIdArr)};
      confirmMsg  = '<div>삭제하시겠습니까?</div>';
      // Ajax실행여부 (DB처리여부)
      if (gbDelIdArr != undefined && gbDelIdArr != null && gbDelIdArr.length > 0) {
        isExeAjax   = true;
      }
    }
    else if (gbMode == 'update.gift.repGoods') {
      // 변경.증정행사.대표상품

      url = '/admin/pm/exhibit/putExhibitGiftRepGoods';
      inParam = {'evExhibitId'  : dataItem.evExhibitId, 'evExhibitGiftGoodsId' : dataItem.evExhibitGiftGoodsId};
      confirmMsg  = '<div>대표상품을 변경하시겠습니까?</div>';
      // Ajax실행여부 (DB처리여부)
      isExeAjax   = true;
    }

    // ------------------------------------------------------------------------
    // 변경/삭제 Ajax 호출
    // ------------------------------------------------------------------------
    if (isExeAjax == true) {
      fnKendoMessage({message : confirmMsg, type : "confirm" , ok : function(){
          fnAjax({
            url     : url
            , params  : inParam
            , success : function( result ){
              fnBizCallback(gbMode, result, removeObj);
            }
            , isAction  : gbMode
          });

        }});
    }
    else {
      // DB작업 없이 그리드에서만 제거하는 경우
      confirmMsg  = '<div>삭제하시겠습니까?</div>';
      fnKendoMessage({message : confirmMsg, type : "confirm" , ok : function(){
          fnBizCallback(gbMode, null, removeObj);
        }});
    }

  }

  // ##########################################################################
  // # 저장 Start
  // ==========================================================================
  // # 저장처리
  // ==========================================================================
  function fnSave() {
    //console.log('# fnSave Start');
    // 이벤트유형별 처리는 일반이벤트 처리 만든 후 작업 예정
    var url         = '';
    var confirmMsg  = '';

    // ------------------------------------------------------------------------
    // form Set
    // ------------------------------------------------------------------------
    var data = $('#inputForm').formSerialize(true);
    var paramData = new Object();

    var resultGroupCheck;               //  상품그룹 Validataion 결과
    var groupList;

    // ------------------------------------------------------------------------
    // 1.1. 기획전ID
    // ------------------------------------------------------------------------
    if (gbMode == 'insert') {
      data.evEventId = '';
    }
    else if (gbMode == 'update') {
      data.evEventId = gbEvEventId;
    }

    if(data.rtnValid) {

      // ----------------------------------------------------------------------
      // Validation Check
      // ----------------------------------------------------------------------
      if (fnCheckValidatiaon() == false) {
        // 체크 실패인 경우 중단
        return false;
      }

      // ----------------------------------------------------------------------
      // 이벤트정보 - 기본정보
      // ----------------------------------------------------------------------
      var eventInfoObj = new Object();
      eventInfoObj.eventTp          = data.eventTp;           // 이벤트유형
      eventInfoObj.mallDiv          = data.mallDiv;           // 몰구분
      eventInfoObj.useYn            = data.useYn;             // 사용여부
      eventInfoObj.dispYn           = data.dispYn;            // 전시여부
      eventInfoObj.delYn            = 'N';                    // 삭제여부 TODO 삭제검토, 서버에서 Set 하는것 검토
      eventInfoObj.title            = data.title;             // 이벤트제목
      eventInfoObj.description      = data.description;       // 이벤트설명
      // 노출범위(디바이스)
      if (data.goodsDisplayType.indexOf('GOODS_DISPLAY_TYPE.WEB_PC') != -1) {
        eventInfoObj.dispWebPcYn = 'Y';
      }
      else {
        eventInfoObj.dispWebPcYn = 'N';
      }
      if (data.goodsDisplayType.indexOf('GOODS_DISPLAY_TYPE.WEB_MOBILE') != -1) {
        eventInfoObj.dispWebMobileYn = 'Y';
      }
      else {
        eventInfoObj.dispWebMobileYn = 'N';
      }
      if (data.goodsDisplayType.indexOf('GOODS_DISPLAY_TYPE.APP') != -1) {
        eventInfoObj.dispAppYn = 'Y';
      }
      else {
        eventInfoObj.dispAppYn = 'N';
      }
      eventInfoObj.evEmployeeTp     = data.evEmployeeTp;      // 임직원전용여부
      eventInfoObj.startDt          = data.startDe + '' + data.startHour + '' + data.startMin + '00'; // 시작일시
      eventInfoObj.endDt            = data.endDe   + '' + data.endHour   + '' + data.endMin   + '59'; // 종료일시
      eventInfoObj.timeOverCloseYn  = data.timeOverCloseYn == 'Y' ? 'Y' : 'N';   // 자동종료여부
      eventInfoObj.bnrImgPathPc     = gbImgBnrPcPath;         // 배너이미지경로PC
      eventInfoObj.bnrImgOriginNmPc = gbImgBnrPcOriginNm;     // 배너이미지원본파일명PC
      eventInfoObj.bnrImgPathMo     = gbImgBnrMobilePath;     // 배너이미지경로Mobile
      eventInfoObj.bnrImgOriginNmMo = gbImgBnrMobileOriginNm; // 배너이미지원본파일명Mobile
      eventInfoObj.styleId          = data.styleId;           // Style ID
      eventInfoObj.detlHtmlPc       = data.detlHtmlPc;        // 이벤트상세Pc
      eventInfoObj.detlHtmlMo       = data.detlHtmlMo;        // 이벤트상세Mobile
      eventInfoObj.detlHtmlPc2      = data.detlHtmlPc2;        // 이벤트상세2Pc
      eventInfoObj.detlHtmlMo2      = data.detlHtmlMo2;        // 이벤트상세2Mobile

      // ----------------------------------------------------------------------
      // 수정인 경우 추가 Set
      // ----------------------------------------------------------------------
      if (gbMode == 'update') {
        eventInfoObj.evEventId      = gbEvEventId;            // 이벤트ID
        eventInfoObj.statusSe       = gbStatusSe;             // 진생상태
      }

      // @@@@@
      paramData.eventInfo = eventInfoObj;

      // ----------------------------------------------------------------------
      // 접근권한설정(회원등급)
      // ----------------------------------------------------------------------
      var urGroupIdList = Array.from(gbUserGroupMaps.values());
      //eventInfoObj.urGroupIdList = Array.from(gbUserGroupMaps.values());
      var userGroupList = new Array();

      if (urGroupIdList != undefined && urGroupIdList != null && urGroupIdList.length > 0) {

        for (var i = 0; i < urGroupIdList.length; i++) {
          var groupObj = new Object();
          groupObj.urGroupId = urGroupIdList[i];
          userGroupList.push(groupObj);
        }
      }
      // @@@@@
      paramData.userGroupList = userGroupList;

      // ----------------------------------------------------------------------
      // 이벤트상세
      // ----------------------------------------------------------------------
      if (gbEventTp == 'EVENT_TP.NORMAL') {
        // --------------------------------------------------------------------
        // 일반이벤트
        // --------------------------------------------------------------------
        paramData = fnParamDataSetNormal(paramData, data);

        // ----------------------------------------------------------------------
        // 그룹리스트
        // ----------------------------------------------------------------------
        groupList = paramData.groupList;
      }
      else if (gbEventTp == 'EVENT_TP.SURVEY') {
        // --------------------------------------------------------------------
        // 설문이벤트
        // --------------------------------------------------------------------
        paramData = fnParamDataSetSurvey(paramData, data);
        // 스탬프 당첨혜택리스트
        paramData.eventSurveyQuestionList = gbValidData.eventSurveyQuestionList;

        // ----------------------------------------------------------------------
        // 그룹리스트
        // ----------------------------------------------------------------------
        groupList = paramData.groupList;
      }
      else if (gbEventTp == 'EVENT_TP.ATTEND' || gbEventTp == 'EVENT_TP.MISSION' || gbEventTp == 'EVENT_TP.PURCHASE') {
        // --------------------------------------------------------------------
        // 스탬프(출석)/스탬프(미션)/스탬프(구매)
        // --------------------------------------------------------------------
        paramData = fnParamDataSetStamp(paramData, data);
        // 스탬프 당첨혜택리스트
        paramData.eventStampDetlList = gbValidData.eventStampDetlList;

        // ----------------------------------------------------------------------
        // 그룹리스트
        // ----------------------------------------------------------------------
        groupList = paramData.groupList;
      }
      else if (gbEventTp == 'EVENT_TP.ROULETTE') {
        // --------------------------------------------------------------------
        // 룰렛이벤트
        // --------------------------------------------------------------------
        paramData = fnParamDataSetRoulette(paramData, data);
        // 룰렛 아이템리스트
        paramData.eventRouletteItemList = gbValidData.eventRouletteItemList;

        // ----------------------------------------------------------------------
        // 그룹리스트
        // ----------------------------------------------------------------------
        groupList = paramData.groupList;
      }
      else if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
        // 체험단이벤트
        paramData = fnParamDataSetExperience(paramData, data);
      }
      //주석console.log('# paramData Result :: ', JSON.stringify(paramData));
      //console.log('# paramData :: ', JSON.stringify(paramData));

      // ************************************************************************
      // 5. Validation Check - 그리드/그룹정보
      // ************************************************************************
      let resultCheckDetail = fnCheckValidGrid(data, paramData, groupList);
      //console.log('# resultCheckDetail :: ', resultCheckDetail);
      if (resultCheckDetail == false) {
        return false;
      }

      // ----------------------------------------------------------------------
      // Ajax Call
      // ----------------------------------------------------------------------
      //console.log('# data.rtnValid :: ', data.rtnValid);


      if (gbMode == 'insert') {
        url         = '/admin/pm/event/addEvent';
        confirmMsg  = '저장하시겠습니까?';
      }
      else if (gbMode == 'update') {
        url         = '/admin/pm/event/putEvent';
        confirmMsg  = '수정하시겠습니까?';
      }

      fnKendoMessage({
          message : fnGetLangData({key : '', nullMsg : confirmMsg })
        , type    : 'confirm'
        , ok      : function(){
                      // 저장 처리 후 선택 콘텐츠레벨 초기화
                      //selectedContsLevel = null;
                      fnAjaxSave(gbMode, url, paramData);

                    }
      });

    }
  }

  // ==========================================================================
  // # 조회 - 일반(그룹) 기본정보
  // ==========================================================================
  function fnSetNormalGroupBasicInfo(groupIdx, groupData) {

    // 상품그룹명
    $('#groupNm'+groupIdx).val(groupData.groupNm);
    // Text Color
    $('#textColor'+groupIdx).val(groupData.textColor);
    // 그룹사용여부
    $('input:radio[name="groupUseYn'+groupIdx+'"]:radio[value="'+groupData.groupUseYn+'"]').attr('checked',true);
    // 상품그룹명배경설정
    $('input:radio[name="exhibitImgTp'+groupIdx+'"]:radio[value="'+groupData.exhibitImgTp+'"]').attr('checked',true);
    // 상품그룹명배경설정에 따른 노출/숨김 처리
    fnSetEventBgView(groupIdx);

    // 상품그룹명 컬러코드
    $('#bgCd'+groupIdx).val(groupData.bgCd);
    if (groupData.exhibitImgTp == 'EXHIBIT_IMG_TP.BG') {
      $('#bgCdDiv'+groupIdx).show();
    }
    else {
      $('#bgCdDiv'+groupIdx).hide();
    }

    // 상품그룹설명
    $('#groupDesc'+groupIdx).val(groupData.description);
    // 전시상품수
    $('#dispCnt'+groupIdx).val(groupData.dispCnt);
    // 그룹전시순서
    $('#groupSort'+groupIdx).val(groupData.groupSort);

  }

  // ==========================================================================
  // # 조회 - 일반(그룹) - 상품리스트
  // ==========================================================================
  function fnSethNormalGroupGoodsList(groupIdx, evEventGroupId) {

    // --------------------------------------------------------------------
    // 1. 그룹상품그리드 초기화
    // --------------------------------------------------------------------
    fnInitGroupGoodsGrid(groupIdx, evEventGroupId);

    // --------------------------------------------------------------------
    // 2. 그리드 조회
    // --------------------------------------------------------------------
    let data = $('#inputForm').formSerialize(true);
    $('#groupGoodsGrid'+groupIdx).data('kendoGrid').dataSource.read(data);
  }

  // ==========================================================================
  // # 저장 - 당첨자공지사항
  // ==========================================================================
  function fnSaveWinnerNotice() {

    let winnerNotice = $('#winnerNoticeConts').val();
    //console.log('# winnerNotice :: ', winnerNotice );

    // ------------------------------------------------------------------------
    // paramData Set
    // ------------------------------------------------------------------------
    let paramData = new Object();
    let eventInfoObj = new Object();
    eventInfoObj.evEventId    = gbEvEventId;    // 이벤트ID
    eventInfoObj.winnerNotice = winnerNotice;   // 당첨자공지사항
    paramData.eventInfo       = eventInfoObj;

    // ------------------------------------------------------------------------
    // Ajax Call
    // ------------------------------------------------------------------------
    let url         = '/admin/pm/event/putEventWinnerNotice';
    let confirmMsg  = '저장하시겠습니까?';
    //console.log('# paramData :: ', JSON.stringify(paramData));

    fnKendoMessage({
        message : fnGetLangData({key : '', nullMsg : confirmMsg })
      , type    : 'confirm'
      , ok      : function(){

                    gbMode = 'update.winnerNotice';
                    fnAjaxSave(gbMode, url, paramData);

                  }
    });

  }
  // # 저장 End
  // ##########################################################################

  // ==========================================================================
  // # 삭제처리
  // ==========================================================================
  function fnDelete() {
    //console.log('# fnDelete Start');

    var delEvEventIdArr = new Array() ;
    delEvEventIdArr.push(gbEvEventId);

    // ------------------------------------------------------------------------
    // 삭제가능여부 체크
    // ------------------------------------------------------------------------
    if (gbStatusSe != 'BEF') {
      // 진행상태가 진행예정이 아니면 삭제 불가
      // 체험단 진행상태 기준은 종료일자가 후기작성종료일로 Query에서 적용되어 있음

      fnMessage('', '삭제가 불가한 게시물입니다', '');
      gbMode = 'update';
      return false;
    }

    // ----------------------------------------------------------------------
    // 삭제 Validataion 체크
    // ----------------------------------------------------------------------
    var isCheck = false;

    for (var i = 0; i <delEvEventIdArr.length; i++) {

      isCheck = fnChecDelkValidationEvent(gbDetail)

      if (isCheck == false) {
        break;
      }
    }
    //console.log('# isCheck :: ', isCheck);
    if (isCheck == false) {
      return false;
    }

    // ------------------------------------------------------------------------
    // Ajax Call
    // ------------------------------------------------------------------------
    var url         = '/admin/pm/event/delEvent';
    var confirmMsg  = '삭제하시겠습니까?';

    var inParam = {"evEventIdListString"  : JSON.stringify(delEvEventIdArr)};

    fnKendoMessage({message : confirmMsg, type : "confirm" , ok : function(){

      gbMode = 'delete';
      fnAjax({
          url     : url
        , params  : inParam
        , success : function( result ){
                      fnBizCallback(gbMode, result);
                    }
        , error   : function(xhr, status, strError) {
                      gbMode = 'update';
                    }
        , isAction : gbMode
      });

    }});
  }

  // ==========================================================================
  // # 기획전 수정/삭제 Validation Check
  // ==========================================================================
  function fnChecDelkValidationEvent(detail) {

    var useYn     = '';
    var statusSe  = '';

    // ------------------------------------------------------------------------
    // 상세조회 결과 체크 - 기획전정보, 기획전유형, 사용여부, 진행상태
    // ------------------------------------------------------------------------
    if (detail == undefined || detail == 'undefined' || detail == null || detail == 'null' || detail == '') {
      fnMessage('', '이벤트 정보가 존재하지 않습니다.', '');
      return false;
    }

    if (detail.exhibitTp == undefined || detail.exhibitTp == 'undefined' || detail.exhibitTp == null || detail.exhibitTp == 'null' || detail.exhibitTp == '') {
      fnMessage('', '이벤트 유형 정보가 존재하지 않습니다.', '');
      return false;
    }

    if (detail.useYn == undefined || detail.useYn == 'undefined' || detail.useYn == null || detail.useYn == 'null' || detail.useYn == '') {
      fnMessage('', '사용여부 정보를 확인하세요.', '');
      return false;
    }
    if (detail.statusSe == undefined || detail.statusSe == 'undefined' || detail.statusSe == null || detail.statusSe == 'null' || detail.statusSe == '') {
      fnMessage('', '진행상태 정보를 확인하세요.', '');
      return false;
    }
  }

  // ------------------------------------------------------------------------
  // 이벤트 미리보기 링크
  // ------------------------------------------------------------------------
  function fnBtnEventPreview() {

      var mallUrl = CUR_SERVER_URL + "/events/";

      var eventPreviewParam = gbEvEventId; // +"&preview=Y"; SPMO-815 내용으로 인해 제외

      switch(gbEventTp) {
          // 일반 이벤트
          case "EVENT_TP.NORMAL" :
              mallUrl += "reply?event="+eventPreviewParam
              break;
          // 설문 이벤트
          case "EVENT_TP.SURVEY" :
              mallUrl += "survey?event="+eventPreviewParam;
              break
          // 스탬프(출석, 미션, 구매)
          case "EVENT_TP.ATTEND" : case "EVENT_TP.MISSION" : case "EVENT_TP.PURCHASE" :
              mallUrl += "stamp?event="+eventPreviewParam;
              break;

          // 룰렛 이벤트
          case "EVENT_TP.ROULETTE" :
              mallUrl += "roulette?event="+eventPreviewParam;
              break;

          // 체험단 이벤트
          case "EVENT_TP.EXPERIENCE":
              mallUrl += "experience?event="+eventPreviewParam;
              break;

          default :
              break;
      }

      window.open(mallUrl);
  }


  // ##########################################################################
  // # Ajax Call Start
  // ==========================================================================
  // # fnAjaxSearch
  // ==========================================================================
  function fnAjaxSearch() {
    //console.log('# fnAjaxSearch Start [', gbEvEventId, ']');
    fnAjax({
        url       : '/admin/pm/event/selectEventInfo?evEventId=' + gbEvEventId      // 주소줄에서 ID 보기위해 params 사용안함
      , method    : 'POST'
      , isAction  : 'select'
      , success   : function (data, status, xhr) {
                      //주석console.log('# fnAjaxSearch :: ', JSON.stringify(data));
                      fnBizCallback('select', data, null);
                    }
      , error     : function (xhr, status, strError) {

                    }
    });
  }

  // ==========================================================================
  // # fnAjaxSave
  // ==========================================================================
  function fnAjaxSave(isAction, url, paramData) {
    //console.log('# fnAjaxSave Start [', gbMode, '][', url, '][', JSON.stringify(paramData), ']');
    // ----------------------------------------------------------------------
    // 기획전상세조회-기본정보(일반/골라담기/증정행사 공통)
    // ----------------------------------------------------------------------
    fnAjax({
        url         : url
      //, method      : 'POST'
      , params      : paramData
      , contentType : 'application/json'
                      //{
                      //  'eventInfo' : testObj
                      //}
      , isAction    : isAction
      //, async   : false
      , success     : function(data, status, xhr) {
                        // --------------------------------------------------
                        // 성공 Callback 처리
                        // --------------------------------------------------
                        fnBizCallback(isAction, data, null);
                      }
      , error     : function(xhr, status, strError) {
                      //fnKendoMessage({
                      //  message : xhr.responseText
                      //});
                    }
    });
  }
  // # Ajax Call End
  // ##########################################################################

  // ==========================================================================
  // # 콜백함수
  // ==========================================================================
  function fnBizCallback(id, data, removeObj) {

    var gridId;

    switch(id){
      case 'select':
        // --------------------------------------------------------------------
        // 조회
        // --------------------------------------------------------------------
        //console.log('# 조회결과 Start');
        fnSetSearchData(data);

        // --------------------------------------------------------------------
        // 조회결과 Set
        // --------------------------------------------------------------------
        gbDetail = data.detail;

        break;
      case 'insert':
        // --------------------------------------------------------------------
        // 등록
        // --------------------------------------------------------------------
        // 이벤트ID Set
        gbEvEventId = data.evEventId;
        // 이벤트유형 Set
        gbEventTp   = data.eventTp;
        // 모드 Set
        gbMode        = 'update';
        // 목록 이동 시 1페이지로 감
        //sessionStorage.setItem('lastPage', "1");

        fnKendoMessage({
            message : '저장이 완료되었습니다.'
          , ok      : function(){
                        // ----------------------------------------------------
                        // 수정화면 이동
                        // ----------------------------------------------------
                        fnGoEventEdit();
                        // ----------------------------------------------------
                        // 목록이동
                        // ----------------------------------------------------
                        //fnGoList();
                        // * 수정화면 이동 (자신)
                        //fnGoEventEdit();
                        // * 재조회
                        //fnSearch();
                        // * 수정화면 이동 (실제로는 리로드)
                        //let option = {};
                        //option.url    = '#/eventMgm';
                        //// 이벤트 등록/수정 : 1093 / 130000112 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
                        //option.menuId = 1090;
                        ////option.menuId = 130000112;
                        ////option.target = '_blank';
                        //option.target = '_self';
                        //option.data = { eventTp : gbEventTp, evEventId : gbEvEventId, mode : 'update'};
                        //// 화면이동
                        //fnGoPage(option);
                      }
        });
        break;
      case 'update':
        // --------------------------------------------------------------------
        // 수정
        // --------------------------------------------------------------------
        // 화면 재조회
        fnKendoMessage({
            message : '수정이 완료되었습니다.'
          , ok      : function(){
                        // 수정 후 재조회
                        $("#searchForm").formClear(true);
                        //fnSearch();
                        fnReSearch();
                      }
        });

        break;
      case 'update.winnerNotice':
        // --------------------------------------------------------------------
        // 수정-당첨자공지사항
        // --------------------------------------------------------------------
        //console.log('# 당첨자공지사항 처리 결과 :: ', JSON.stringify(data));
        gbMode = 'update';

        // --------------------------------------------------------------------
        // 변경값 Set
        // --------------------------------------------------------------------
        // 당첨자공지사항
        $('#winnerNotice').val(data.eventInfo.winnerNotice);

        // 당첨자공지사항 등록일시
        if (fnIsEmpty(data.eventInfo.winnerInforDt) == false) {
          // 데이터가 있는 경우만 노출
          $('#winnerInforDtDiv').show();
          $('#winnerInforDtSpan').text(data.eventInfo.winnerInforDt);
        }
        else {
          // 데이터 없는 경우 숨김
          $('#winnerInforDtDiv').hide();
        }

        // --------------------------------------------------------------------
        // 팝업닫기
        // --------------------------------------------------------------------
        var kendoWindow =$('#kendoPopup').data('kendoWindow');
        kendoWindow.close();

        // 화면 재조회
        fnKendoMessage({
          message : '등록이 완료되었습니다.'
            , ok      : function(){
              // 수정 후 재조회
              //$("#searchForm").formClear(true);
              //fnSearch();
            }
        });

        break;
      case 'delete':
        // --------------------------------------------------------------------
        // 삭제
        // --------------------------------------------------------------------
        gbMode = 'update';
        // 화면 재조회
        fnKendoMessage({
          message : '삭제가 완료되었습니다.'
            , ok      : function(){
                          // 리스트로 이동
                          //fnGoList();
                          // 현재 탭(윈도우) 닫기
                          window.close();
                        }
        });
        break;

      case 'delete.group.detl':
        // --------------------------------------------------------------------
        // 삭제.상품그룹.상품
        // --------------------------------------------------------------------
        // 모드 변경
        gbMode = 'update';
        // 그리드 재조회
        //let groupGoodsFormData = $('#inputForm').formSerialize(true);
        //$('#groupGoodsGrid'+gbDelGruopId).data('kendoGrid').dataSource.read(groupGoodsFormData);
        // 그리드 Row 제거
        gridId = 'groupGoodsGrid'+gbDelGruopId;
        fnGridRowRemove(removeObj, gridId);

        break;
    }

  }

  // ==========================================================================
  // # 그리드 Row 제거
  // ==========================================================================
  function fnGridRowRemove(removeObj, gridId) {

    // 그리드 Row 제거
    if (removeObj != undefined && removeObj != null && removeObj != '') {

      if (removeObj.removeRowArr != undefined && removeObj.removeRowArr != null && removeObj.removeRowArr.length > 0) {

        var removeRowArr  = removeObj.removeRowArr;
        var grid          = $('#'+gridId).data('kendoGrid');
        var ds            = grid.dataSource;

        for (var i = 0; i < removeObj.removeRowArr.length; i++) {

          var selectRow = removeObj.removeRowArr[i].selectRow;
          grid.removeRow($(selectRow));

        } // End of for (var i = 0; i < removeObj.removeRowArr.length; i++)

        if (gbDelIdArr != undefined && gbDelIdArr != null && gbDelIdArr.length > 0) {
          //fnKendoMessage({
          //    message : '삭제되었습니다.'
          //  , ok      : function(){
          //
          //              }
          //});
        }

      } // End of if (removeObj.removeRowArr != undefined && removeObj.removeRowArr != null && removeObj.removeRowArr.length > 0)

    } // End of if (removeObj != undefined && removeObj != null && removeObj != '')

  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 상세조회 DataSet Start
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // ==========================================================================
  // # 조회데이터Set - 공통
  // ==========================================================================
  function fnSetSearchData(data) {
    //console.log('# fnSetSearchData Start');
    var eventInfo;
    //console.log('# data :: ', JSON.stringify(data));
    // ------------------------------------------------------------------------
    // 참여횟수설정 (ID당)
    // ------------------------------------------------------------------------
    //console.log('# data.eventInfo.eventTp     :: ', data.eventInfo.eventTp);
    //console.log('# data.eventInfo.eventDrawTp :: ', data.eventInfo.eventDrawTp);
    //console.log('# data.eventInfo.eventJoinTp :: ', data.eventInfo.eventJoinTp);
    fnInitEventJoinTp(data.eventInfo.eventTp, data.eventInfo.eventDrawTp, data.eventInfo.eventJoinTp);

    // ------------------------------------------------------------------------
    // 당첨자 공지사항(url입력) 일자
    // ------------------------------------------------------------------------
    if (data.eventInfo.winnerInforDt != undefined && data.eventInfo.winnerInforDt != null && data.eventInfo.winnerInforDt != '') {
      // 데이터가 있는 경우만 노출
      $('#winnerInforDtDiv').show();
      $('#winnerInforDtSpan').text(data.eventInfo.winnerInforDt);
    }
    else {
      // 데이터 없는 경우 숨김
      $('#winnerInforDtDiv').hide();
    }

    // ------------------------------------------------------------------------
    // 적용범위
    // ------------------------------------------------------------------------
    fnCoverageApprInit();

    if(data.coverageList != null){
      for(var i =0;i<data.coverageList.length ; i++){
        var obj = new Object();
        var categoryName;
        if(data.coverageList[i].coverageType == 'APPLYCOVERAGE.WAREHOUSE'){
          categoryName = '출고처';
          obj["coverageName"] = '출고처 코드 : ' + data.coverageList[i].coverageId + '</br>' + '출고처 명 : ' + data.coverageList[i].coverageName;
        }else if(data.coverageList[i].coverageType == 'APPLYCOVERAGE.BRAND'){
          categoryName = '전시 브랜드';
          obj["coverageName"] = '전시브랜드 코드 : ' + data.coverageList[i].coverageId + "</br>" + '전시브랜드명 : ' + data.coverageList[i].coverageName;
        }else if(data.coverageList[i].coverageType == 'APPLYCOVERAGE.DISPLAY_CATEGORY'){
          categoryName = '전시 카테고리';
          obj["coverageName"] = data.coverageList[i].ilCategoryName;
        }else{
          categoryName = '상품';
          obj["coverageName"] = '상품코드 : ' + data.coverageList[i].coverageId + "</br>" + '판매상태 : ' + data.coverageList[i].saleStatusCodeName + ' / 전시상태 : ' + data.coverageList[i].goodsDisplayYn + "</br>" + '상품명 : ' + data.coverageList[i].coverageName;
        }
        obj["categoryType"] = categoryName;
        obj["includeYnName"] = data.coverageList[i].includeYn == 'Y' ? '포함' : '제외';
        obj["includeYn"] = data.coverageList[i].includeYn;
        obj["coverageId"] = data.coverageList[i].coverageId;
        obj["coverageType"] = data.coverageList[i].coverageType;

        ctgryGridDs.add(obj);
      }
    }

    // ------------------------------------------------------------------------
    // 이벤트기본정보/이벤트상세정보 Binding
    // ------------------------------------------------------------------------
    if (data.eventInfo.eventTp == 'EVENT_TP.NORMAL') {
      // ----------------------------------------------------------------------
      // 일반이벤트
      // ----------------------------------------------------------------------
      eventInfo = fnSetSearchDataNormal(data);
    }
    else if (data.eventInfo.eventTp == 'EVENT_TP.SURVEY') {
      // ----------------------------------------------------------------------
      // 설문이벤트
      // ----------------------------------------------------------------------
      eventInfo = fnSetSearchDataSurvey(data);
    }
    else if (data.eventInfo.eventTp == 'EVENT_TP.ATTEND' || data.eventInfo.eventTp == 'EVENT_TP.MISSION' || data.eventInfo.eventTp == 'EVENT_TP.PURCHASE') {
      // ----------------------------------------------------------------------
      // 스탬프(출석)/스탬프(미션)/스탬프(구매)
      // ----------------------------------------------------------------------
      eventInfo = fnSetSearchDataStamp(data);
    }
    else if (data.eventInfo.eventTp == 'EVENT_TP.ROULETTE') {
      // ----------------------------------------------------------------------
      // 룰렛이벤트
      // ----------------------------------------------------------------------
      eventInfo = fnSetSearchDataRoulette(data);
    }
    else if (data.eventInfo.eventTp == 'EVENT_TP.EXPERIENCE') {
      // ----------------------------------------------------------------------
      // 체험단이벤트
      // ----------------------------------------------------------------------
      eventInfo = fnSetSearchDataExperience(data);
    }
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 조회결과 bind
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    $('#inputForm').bindingForm( {'rows':eventInfo}, 'rows' );

    // 켄도 에디터 내 스크립트 허용 HGRM-8031
    $("#detlHtmlPc").data("kendoEditor").value(eventInfo.detlHtmlPc);
    $("#detlHtmlMo").data("kendoEditor").value(eventInfo.detlHtmlMo);
    $("#detlHtmlPc2").data("kendoEditor").value(eventInfo.detlHtmlPc2);
    $("#detlHtmlMo2").data("kendoEditor").value(eventInfo.detlHtmlMo2);

    // ------------------------------------------------------------------------
    // 이벤트키정보 Set
    // ------------------------------------------------------------------------
    gbEvEventId = eventInfo.evEventId;
    gbEventTp   = eventInfo.eventTp;
    gbStatusSe  = eventInfo.statusSe;

    // ------------------------------------------------------------------------
    // 이벤트ID 노출
    // ------------------------------------------------------------------------
    $('#evEventIdSpan').text(eventInfo.evEventId);

    // ------------------------------------------------------------------------
    // 최초등록정보/최근수정내역
    // ------------------------------------------------------------------------
    // 최초등록정보
    $('#createSpan').text(eventInfo.createDt + ' / ' + eventInfo.userNm + '(' + eventInfo.createLoginId + ')');

    // 최근수정내역
    var modifyStr = ' ';

    if (eventInfo.modifyDt != null && eventInfo.modifyDt != '') {
      modifyStr += eventInfo.modifyDt;

      if (eventInfo.modifyNm != null && eventInfo.modifyNm != '') {
        modifyStr += ' / ';
        modifyStr += eventInfo.modifyNm;

        if (eventInfo.modifyLoginId != null && eventInfo.modifyLoginId != '' && eventInfo.modifyLoginId != '0' && eventInfo.modifyLoginId != 0) {
          modifyStr += '(';
          modifyStr += eventInfo.modifyLoginId;
          modifyStr += ')';
        }
      }
    }
    $('#modifySpan').text(modifyStr);

    // ------------------------------------------------------------------------
    // 노출범위(디바이스)
    // ------------------------------------------------------------------------
    if (eventInfo.dispWebPcYn == 'Y' && eventInfo.dispWebMobileYn == 'Y' && eventInfo.dispAppYn == 'Y') {
      $('input:checkbox[name="goodsDisplayType"]:input[value="ALL"]').prop('checked', true);
    }
    else {
      $('input:checkbox[name="goodsDisplayType"]:input[value="ALL"]').prop('checked', false);
    }
    if (eventInfo.dispWebPcYn == 'Y') {
      $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.WEB_PC"]').prop('checked', true);
    }
    else {
      $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.WEB_PC"]').prop('checked', false);
    }
    if (eventInfo.dispWebMobileYn == 'Y') {
      $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.WEB_MOBILE"]').prop('checked', true);
    }
    else {
      $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.WEB_MOBILE"]').prop('checked', false);
    }
    if (eventInfo.dispAppYn == 'Y') {
      $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.APP"]').prop('checked', true);
    }
    else {
      $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.APP"]').prop('checked', false);
    }

    // ------------------------------------------------------------------------
    // 접근권한설정
    // ------------------------------------------------------------------------
    // 접근권한맵 초기화 : 수정 후 재조회 시 중복방지
    gbUserGroupMaps.clear();
    $('.js__remove__userGroupList').each(function(){
      $(this).remove();
    });

    var userGroupList = data.userGroupList;

    if (userGroupList != undefined && userGroupList != null && userGroupList.length > 0) {
      for (var i = 0; i < userGroupList.length; i++) {
        var inputValue = userGroupList[i].urGroupId;
        var inputText  = userGroupList[i].groupMasterName + '_' + userGroupList[i].groupName;
        fnAddUserGroup(inputText, inputValue);
      }
    }

    // ------------------------------------------------------------------------
    // 진행기간
    // ------------------------------------------------------------------------
    // 진행기간-시작
    var startDtStr = '';
    var startDe    = '';
    var startHour;
    var startMin;
    //startDtStr  = (((eventInfo.startDt).replace(/-/gi, '')).replace(/:/gi, '')).replace(/ /gi, '');
    startDtStr  = ((eventInfo.startDt).replace(/:/gi, '')).replace(/ /gi, '');

    if (startDtStr != null && startDtStr != '') {
      if (startDtStr.length >= 10) {
        startDe = startDtStr.substring(0, 10);
        $('#startDe').val(startDe);
        // --------------------------------------------------------------------
        // 날짜 선후비교를 위한 kendoDatePicker 값 Set
        // --------------------------------------------------------------------
        const $datePicker = $('#startDe').data('kendoDatePicker');
        if( $datePicker ) {
          $datePicker.value(startDe);
        }
      }
      if (startDtStr.length >= 12) {
        startHour = Number(startDtStr.substring(10, 12));
        $('#startHour').data('kendoDropDownList').select(startHour);
      }
      if (startDtStr.length >= 14) {
        startMin = Number(startDtStr.substring(12, 14));
        $('#startMin').data('kendoDropDownList').select(startMin);
      }
    }
    // 진행기간-종료
    var endDtStr = '';
    var endDe    = '';
    var endHour;
    var endMin;
    //endDtStr  = (((eventInfo.endDt).replace(/-/gi, '')).replace(/:/gi, '')).replace(/ /gi, '');
    endDtStr  = ((eventInfo.endDt).replace(/:/gi, '')).replace(/ /gi, '');
    if (endDtStr != null && endDtStr != '') {
      if (endDtStr.length >= 10) {
        endDe = endDtStr.substring(0, 10);
        $('#endDe').val(endDe);
        // --------------------------------------------------------------------
        // 날짜 선후비교를 위한 kendoDatePicker 값 Set
        // --------------------------------------------------------------------
        const $datePicker = $('#endDe').data('kendoDatePicker');
        if( $datePicker ) {
          $datePicker.value(endDe);
        }
      }
      if (endDtStr.length >= 12) {
        endHour = Number(endDtStr.substring(10, 12));
        $('#endHour').data('kendoDropDownList').select(endHour);
      }
      if (endDtStr.length >= 14) {
        endMin = Number(endDtStr.substring(12, 14));
        $('#endMin').data('kendoDropDownList').select(endMin);
      }


    }

    // ------------------------------------------------------------------------
    // 배너이미지(PC)
    // ------------------------------------------------------------------------
    let path      = eventInfo.bnrImgPathPc;
    let originNm  = eventInfo.bnrImgOriginNmPc;
    let viewId    = 'imgBannerPcView';
    // 전역변수에 Set
    gbImgBnrPcPath      = path;
    gbImgBnrPcOriginNm  = originNm;
    // 이미지 노출
    if (path != undefined && path != null && path != '') {
      fnSetImgView(path, originNm, viewId);
    }

    // ------------------------------------------------------------------------
    // 배너이미지(Mobile)
    // ------------------------------------------------------------------------
    path      = eventInfo.bnrImgPathMo;
    originNm  = eventInfo.bnrImgOriginNmMo;
    viewId    = 'imgBannerMobileView';
    // 전역변수에 Set
    gbImgBnrMobilePath      = path;
    gbImgBnrMobileOriginNm  = originNm;
    // 이미지 노출
    if (path != undefined && path != null && path != '') {
      fnSetImgView(path, originNm, viewId);
    }

    // ------------------------------------------------------------------------
    // 기본정보 비활성처리
    // ------------------------------------------------------------------------
    // 진행기간 (진행중 인 경우 수정 불가)
    if (eventInfo.statusSe == 'ING') {
      $('#startDe').data('kendoDatePicker').enable(false);      // kendoDatePicker
      $('#startHour').data('kendoDropDownList').enable(false);  // kendoDropDownList
      $('#startMin').data('kendoDropDownList').enable(false);   // kendoDropDownList
      $('#endDe').data('kendoDatePicker').enable(false);        // kendoDatePicker
      $('#endHour').data('kendoDropDownList').enable(false);    // kendoDropDownList
      $('#endMin').data('kendoDropDownList').enable(false);     // kendoDropDownList
    }

    // ------------------------------------------------------------------------
    // 저장 버튼
    // ------------------------------------------------------------------------
    if (eventInfo.statusSe == 'END') {
      // 진행종료 : 저장 불가
      $('#fnBtnSave').prop('disabled', true);
      // 당첨자공지사항 입력 비활성
      $('#winnerNotice').attr('disabled', 'disabled');
      // 당첨자공지사항 등록 버튼 : 노출
      $('#btnWinnerNoticePopup').show();
    }
    else {
      // 진행예정/진행중 : 저장가능
      $('#fnBtnSave').prop('disabled', false);
      // 당첨자공지사항 입력 활성
      $('#winnerNotice').removeAttr('disabled', 'disabled');
      // 당첨자공지사항 등록 버튼 : 숨김
      $('#btnWinnerNoticePopup').hide();
    }

    //console.log('# eventInfo.statusSe :: ', eventInfo.statusSe);
    // ------------------------------------------------------------------------
    // 삭제 버튼 - 진행중/종료는 삭제 불가
    // ------------------------------------------------------------------------
    if (eventInfo.statusSe == 'BEF') {
      // 진행예정 : 삭제 가능
      $('#fnBtnDelete').prop('disabled', false);    // 활성
    }
    else {
      // 진행중/진행종료 : 삭제불가
      $('#fnBtnDelete').prop('disabled', true);     // 비활성
    }

    // ------------------------------------------------------------------------
    // 기본정보 비활성 처리
    // ------------------------------------------------------------------------
    if (eventInfo.statusSe == 'END') {
      // 진행상태 : 종료
      // 몰구분
      $('input[name=mallDiv]').attr('disabled', true);                      // radio
      // 사용여부
      $('input[name=useYn]').attr('disabled', true);                        // radio
      // 전시여부
      $('input[name=dispYn]').attr('disabled', false);                       // radio
      // 이벤트제목
      $("input[name=title]").attr('disabled', true);                        // input text
      // 이벤트설명
      $('#description').attr('disabled', 'disabled');                       // textarea
      // 노출범위설정(디바이스)
      $('input[name=goodsDisplayType]').attr('disabled', true);             // checkbox
      // 임직원전용여부
      $('input[name=evEmployeeTp]').attr('disabled', true);                 // radio
      // 접근권한설정-그룹
      $('#userMaster').data('kendoDropDownList').enable(false);             // kendoDropDownList
      // 접근권한설정-등급
      $('#userGroup').data('kendoDropDownList').enable(false);              // kendoDropDownList
      // 접근권한설정-추가버튼
      $('#btnAddUserGroup').prop('disabled', true);                         // button
      // 접근권한설정-삭제
      $('.js__remove__userGroupBtn').prop('disabled', true);                // button
      // 기간종료 후 자동종료
      $('input[name=timeOverCloseYn]').attr('disabled', false);              // checkbox
      // 기간종료 후 자동종료 - 체험단용
      $('input[name=experienceTimeOverCloseYn]').attr('disabled', true);    // checkbox
      // 배너이미지(PC)-파일선택
      $('#imgBannerPc').attr('disabled', true);                             // button
      // 배너이미지(PC)-파일삭제
      $('#btnImgBannerPc').attr('disabled', true);                          // button
      // 배너이미지(Mobile)-파일선택
      $('#imgBannerMobile').attr('disabled', true);                         // button
      // 배너이미지(Mobile)-파일삭제
      $('#btnImgBannerMobile').attr('disabled', true);                      // button
      // 이벤트상세(PC)
      $($('#detlHtmlPc').data().kendoEditor.body).attr('contenteditable', false); // kendoEditor
      // 이벤트상세(Mobile)
      $($('#detlHtmlMo').data().kendoEditor.body).attr('contenteditable', false); // kendoEditor
      // 이벤트상세(PC)2
      $($('#detlHtmlPc2').data().kendoEditor.body).attr('contenteditable', false); // kendoEditor
      // 이벤트상세(Mobile)2
      $($('#detlHtmlMo2').data().kendoEditor.body).attr('contenteditable', false); // kendoEditor

      // 진행기간
      $('#startDe').data('kendoDatePicker').enable(false);      // kendoDatePicker
      $('#startHour').data('kendoDropDownList').enable(false);  // kendoDropDownList
      $('#startMin').data('kendoDropDownList').enable(false);   // kendoDropDownList
      $('#endDe').data('kendoDatePicker').enable(false);        // kendoDatePicker
      $('#endHour').data('kendoDropDownList').enable(false);    // kendoDropDownList
      $('#endMin').data('kendoDropDownList').enable(false);     // kendoDropDownList

      // 당첨자 안내
      $('#winnerInfor').attr('disabled',false);

      // 저장버튼 활성화
      $('#fnBtnSave').prop('disabled',false);

    }

    //if (data.eventInfo.eventTp == 'EVENT_TP.SURVEY') {
    //  let $eventSurveyTp = $('input[name^="eventSurveyTp"]');
    //  $eventSurveyTp.each(function() {
    //    console.log('# this.name :: ', this.name);
    //    $('input[name='+this.name+']').attr('disabled', true);      // radio
    //  });
    //}

    // ------------------------------------------------------------------------
    // 일반(그룹) - 그룹리스트조회
    // ------------------------------------------------------------------------
    fnSearchGroupInfoAjax(data);

  } // End of function fnSetSearchData(data)

  // ==========================================================================
  // # 조회 - 그룹정보
  // ==========================================================================
  function fnSearchGroupInfoAjax(data) {

    // ------------------------------------------------------------------------
    // 그룹리스트조회
    // ------------------------------------------------------------------------
    fnAjax({
      url       : '/admin/pm/event/selectfEventGroupList'
      , method    : 'POST'
      , params    : {
        'evEventId' : gbEvEventId
      }
      , isAction  : 'select'
      //, async     : false
      , success   : function(data, status, xhr) {

        var groupSize = 0;
        if (data.rows != null && data.rows.length > 0) {
          groupSize = data.rows.length;

          fnSetGroupInfo(data.rows);

        }

      }
      , error     : function(xhr, status, strError) {
        fnMessage('', xhr.responseText, '');
      }
    });

    // Loop
    // 일반(그룹) - 그룹상품리스트조회

  }

  // ==========================================================================
  // # 조회 - 일반(그룹) 정보 Set
  // ==========================================================================
  function fnSetGroupInfo(groupList) {

    var groupData;
    var groupIdx = 0;

    for (var i = 0; i < groupList.length; i++) {

      groupIdx = Number(i+1);
      groupData = groupList[i];

      // --------------------------------------------------------------------
      // 1. HTML 템플릿 생성
      // --------------------------------------------------------------------
      fnGroupAdd('detail', groupData.evEventGroupId);

      // --------------------------------------------------------------------
      // 2. 그룹별 기본정보 Set
      // --------------------------------------------------------------------
      fnSetNormalGroupBasicInfo(groupIdx, groupData);

      // --------------------------------------------------------------------
      // 3. 그룹별 상품리스트 Set
      // --------------------------------------------------------------------
      fnSethNormalGroupGoodsList(groupIdx, groupData.evEventGroupId);
    }

    // 전역변수 Set
    gbGroupIdx = groupIdx;
    gbGroupCnt = groupIdx;

    // 그룹 미존재 인 경우 빈 그룹 생성
    if (gbGroupCnt <= 0) {
      // 그룹이 미생성된 상태라면 생성
      fnGroupAdd('add', null);
    }
  }

  // ##########################################################################
  // ------------------------------- Grid Start -------------------------------
  // ==========================================================================
  // # 그리드
  // ==========================================================================
  // eventMgmGrid.js 에 위치

  // ##########################################################################
  // # 파일업로드 Start
  // ##########################################################################
  // eventMgmCommon.js 에 위치

  // ##########################################################################
  // # 에디터 Start
  // ##########################################################################
  // eventMgmCommon.js 에 위치

  // ==========================================================================
  // 목록 버튼 클릭
  // ==========================================================================
  function fnGoList() {

    // ------------------------------------------------------------------------
    // 세션 lastPage 삭제(페이징 기억 관련)
    // ------------------------------------------------------------------------
    // TODO 페이징 적용할 것
    //var curPage = eventGridDs._page;
    //sessionStorage.setItem('lastPage', JSON.stringify(curPage));
    // 링크정보
    let option = {};
    if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
      // 체험단이벤트 목록 : 1091 / 130000111 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
      option.url    = '#/eventExprncList';
      option.menuId = 1091;
    }
    else {
      // 이벤트 목록 : 1090 / 130000110 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
      option.url    = '#/eventNormalList';
      option.menuId = 1090;
    }
    option.target = '_blank';
    fnGoNewPage(option);  // 새페이지(탭)

    ////option.target = '_blank';
    //option.target = '_self';
    //option.data = { mode : 'insert'};
    //// 화면이동
    //fnGoPage(option);
  }

  // ==========================================================================
  // # 수정화면 이동
  // ==========================================================================
  function fnGoEventEdit() {

    // ------------------------------------------------------------------------
    // 수정 화면으로 이동
    // ------------------------------------------------------------------------
    // 링크정보
    let option = {};
    option.url    = '#/eventMgm';
    // 이벤트 등록/수정 : 130000112 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    option.menuId = 1093;
    option.target = '_self';
    option.data = { eventTp : gbEventTp, evEventId : gbEvEventId, mode : 'update'};
    // 화면이동 : 리로딩이 목적이므로 _self 이고 새창 아님
    fnGoPage(option);
  }

  // ==========================================================================
  // 참여정보 버튼 클릭
  // ==========================================================================
  function fnBtnEventJoin() {

    // ------------------------------------------------------------------------
    // 참여정보 화면으로 이동
    // ------------------------------------------------------------------------
    // 링크정보
    let option = {};

    option.url    = '#/eventJoin';
    // 이벤트 등록/수정 : 1103 / 130000113 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    option.menuId = 1103;
    //option.menuId = 130000113;
    option.target = '_blank';
    //option.target = '_self';
    option.data = { eventTp : gbEventTp, evEventId : gbEvEventId, mode : 'select', fromPage : 'DETL'};
    // 화면이동
    fnGoNewPage(option);  // 새페이지(탭)
    //fnGoPage(option);
    // 등록/수정/상세 화면으로 이동(eventMgm)
    // 새탭으로 열기
    //window.open('#/eventMgm?eventTp='+gbEventTp+'&evEventId='+dataItem.evEventId+'&mode=update', '_blank');
    //window.open('#/exhibitList?eventTp='+gbEventTp+'&evEventId='+dataItem.evEventId+'&mode=update', '_blank');
    //새창으로 열기
    // window.open('#/eventMgm?eventTp='+gbEventTp+'&evEventId='+dataItem.evEventId+'&mode=update', '_blank','width=1200, height=1000, resizable=no, fullscreen=no');
  }

  // ==========================================================================
  // # 당첨자 공지사항 팝업
  // ==========================================================================
  function fnOpenPopupWinnerNotice() {

    // 레이어 팝업 오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    // 당첨인원 초기화
    $('#winnerNoticePopup').val('');
    // 팝업노출
    $('#winnerNoticePopup').show();
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:"auto" ,width:"900px", title:{ nullMsg :'당첨자 공지사항' } } );
  }

  // ==========================================================================
  // # 수정 후 재조회
  // ==========================================================================
  function fnReSearch() {

    // 1.1.1. 그룹정보 삭제
    $('[id ^= groupTable]').remove();
    // 1.1.2. 그룹 index 정보 초기화
    gbGroupIdx = 0;
    gbGroupCnt = 0;

    // 1.4. 접근권한 초기화
    gbUserGroupMaps.clear();
    $('.eventMgm__commentTpListItem').each(function(){
      $(this).remove();
    });

    // ------------------------------------------------------------------------
    // 2. 재조회
    // ------------------------------------------------------------------------
    fnSearch();
  }

  // ##########################################################################
  // # 상품그룹 Start
  // ##########################################################################
  // ==========================================================================
  // # 상품그룹-추가
  // ==========================================================================
  function fnGroupAdd(mode, evEventGroupId) {

    var groupIdx = gbGroupIdx + 1;
    var groupCnt = gbGroupCnt + 1;

    var $target = $('#group-btnArea');
    var tpl = $('#groupTpl').html();
    // IDX 치환
    tpl = tpl.replace(/{IDX}/g, groupIdx);
    // EV_EVENT_GROUP_ID 치환
    //if (evEventGroupId != null) {
    tpl = tpl.replace(/{EV_EVENT_GROUP_ID}/g, evEventGroupId);
    //}

    //$(tpl).insertBefore($target);
    if (gbEventTp == 'EVENT_TP.NORMAL' ||
        gbEventTp == 'EVENT_TP.ATTEND' ||
        gbEventTp == 'EVENT_TP.MISSION' ||
        gbEventTp == 'EVENT_TP.PURCHASE' ||
        gbEventTp == 'EVENT_TP.ROULETTE' ||
        gbEventTp == 'EVENT_TP.SURVEY'
  ) {
      $(tpl).appendTo($('#addAreaDiv'));
    }
    else if (gbEventTp == 'EVENT_TP.GIFT') {
      $(tpl).appendTo($('#addAreaGiftDiv'));
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 객체생성
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ------------------------------------------------------------------------
    // 사용여부(R)  - 공통코드 아님
    // ------------------------------------------------------------------------
    fnTagMkRadio({
      id      : 'groupUseYn' + groupIdx
      , tagId   : 'groupUseYn' + groupIdx
      , data    : [
        { 'CODE' : 'Y'  , 'NAME' : '예'     }
        , { 'CODE' : 'N'  , 'NAME' : '아니오' }
      ]
      , chkVal  : 'Y'
      , style   : {}
    });
    // ------------------------------------------------------------------------
    // 전시그룹배경유형(R)
    // ------------------------------------------------------------------------
    fnTagMkRadio({
      id      : 'eventImgTp' + groupIdx
      , tagId   : 'eventImgTp' + groupIdx
      , data    : [
        { 'CODE' : 'EVENT_IMG_TP.NOT_USE'  , 'NAME' : '사용안함'     }
        , { 'CODE' : 'EVENT_IMG_TP.BG'  , 'NAME' : '배경컬러' }
      ]
      , chkVal  : 'EVENT_IMG_TP.NOT_USE'
      , style   : {}
    });
    // 배경컬러 노출설정
    $('#bgCd' + groupIdx).val('');
    $('#bgCdDiv' + groupIdx).hide();

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 이벤트
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // * 상품그룹명배경설정
    $('#eventImgTp'+groupIdx).off().on('click', function(e) {

      fnSetEventBgView(groupIdx);
    });

    // * 입력제한
    $('#bgCd'+groupIdx).off().on('keyup', function (e) {
      // 문자 제한

      const regExp = /[^0-9a-fA-F\#]/gi;
      const _value = $(this).val();

      if (_value.match(regExp)) {
        $(this).val(_value.replace(regExp, ''));
      }
    });


    // * 전시상품수 숫자만입력 이벤트
    fnInputValidationForNumber('dispCnt'+groupIdx);

    // * 그룹전시순서 숫자만입력 이벤트
    fnInputValidationForNumber('groupSort'+groupIdx);

    // ------------------------------------------------------------------------
    // 그룹상품리스트 그리드 생성
    // ------------------------------------------------------------------------
    fnInitGroupGoodsGrid(groupIdx, evEventGroupId);

    // ------------------------------------------------------------------------
    // 상품그룹번호갱신(노출용)
    // ------------------------------------------------------------------------
    fnChangeGroupViewIdxNum();

    // ------------------------------------------------------------------------
    // 그룹정보 업데이트
    // ------------------------------------------------------------------------
    gbGroupIdx++;
    gbGroupCnt++;

  }

  // ==========================================================================
  // 상품그룹명배경설정에 따른 노출/숨김 처리
  // ==========================================================================
  function fnSetEventBgView(groupIdx) {

    var checkValue = $('input[name=eventImgTp'+groupIdx+']:checked').val();

    if (checkValue == 'EVENT_IMG_TP.NOT_USE') {
      //$('#bgCd'+groupIdx).val('');
      $('#bgCdDiv'+groupIdx).hide();
    }
    else {
      $('#bgCdDiv'+groupIdx).show();
    }
  }

  // ==========================================================================
  // # 상품그룹-추가버튼
  // ==========================================================================
  function fnBtnGroupAdd() {
    if (gbGroupCnt >= MAX_GROUP_CNT) {
      fnMessage('', '상품그룹은 ' + MAX_GROUP_CNT + '개까지만 생성할 수 있습니다.', '');
      return false;
    }
    fnGroupAdd('add', null);
  }

  // ==========================================================================
  // # 상품그룹-상품업로드 샘플 다운로드
  // ==========================================================================
  function fnSampleFormDownload() {
    document.location.href = "/contents/excelsample/event/eventGroupUploadSample.xlsx"
  }

  // ==========================================================================
  // # 상품그룹-그룹별상품업로드 - 파일선택
  // ==========================================================================
  function fnBtnGroupGoodsExcelUpload(){
    $("#uploadUserFile").trigger("click");
  }

  // ==========================================================================
  // # 상품그룹-그룹별상품업로드 - 엑셀파일 읽기
  // ==========================================================================
  function fnExcelUpload(event){
    // Excel Data => Javascript Object 로 변환
    var input = event.target;
    var reader = new FileReader();

    var fileName = event.target.files[0].name;

    reader.onload = function() {
      var fileData = reader.result;
      var wb = XLSX.read(fileData, {
        type : 'binary'
      });

      wb.SheetNames.forEach(function(sheetName) {
        var excelData = XLSX.utils.sheet_to_json(wb.Sheets[sheetName]);
        fnBtnGroupGoodsUpload(excelData);
      })
    };

    reader.readAsBinaryString(input.files[0]);
  }

  // ==========================================================================
  // # 상품그룹-그룹별상품업로드 - 처리
  // ==========================================================================

  var excelSuccessCount = 0;
  function fnBtnGroupGoodsUpload(excelData) {
    // 첫번째 데이터 삭제
    excelData.splice(0, 1);

    var excelTotalCount = excelData.length;
    excelSuccessCount = 0;

    // 그룹정보 검색
    let groupObjList = Array.from(new Set(excelData.map((v) => v.그룹구분인덱스)));
    for (var groupIdx of groupObjList){
      if (gbGroupCnt >= MAX_GROUP_CNT) {
        fnMessage('', '상품그룹은 ' + MAX_GROUP_CNT + '개까지만 생성할 수 있습니다.', '');
        return false;
      }

      // 그룹 생성
      if (gbGroupCnt < groupIdx) {
        fnGroupAdd("add", null);
      }

      // 상품정보 조회
      let searchGoodsList = excelData.filter(v => v.그룹구분인덱스 == groupIdx)
          .filter(v => (/^[0-9]+$/).test(v.그룹구분인덱스))
          .filter(v => (/^[0-9]+$/).test(v.노출순번))
          .map(v => ({GROUP_SORT : v.노출순번, IL_GOODS_ID : v.상품코드}));
      if(searchGoodsList.length > 0){
        fnSelectGoodsInfoList(searchGoodsList, groupIdx);
      }
    }
    var excelFailCount = excelTotalCount - excelSuccessCount;
    fnKendoMessage({message : "총 "+excelTotalCount+"건</br>"+"정상 "+excelSuccessCount+"건 / "+"실패 "+excelFailCount+"건", ok : ""});
    $("#uploadUserFile").val("");
  }

  // ==========================================================================
  // # 상품그룹-그룹별상품업로드 - 상품정보 조회
  // ==========================================================================
  function fnSelectGoodsInfoList(param, groupIdx) {
    fnAjax({
      url       : '/admin/pm/event/selectGoodsInfoList'
      , method    : 'POST'
      , params    : { 'ilGoodsIdListString' : JSON.stringify(param.map(v => v.IL_GOODS_ID)) }
      , isAction  : 'select'
      , async     : false
      , success   : function(data, status, xhr) {
        // --------------------------------------------------
        // 성공 Callback 처리
        // --------------------------------------------------
        let goodsObjList = data.rows;
        for(let goodsObj of goodsObjList){
          goodsObj.goodsSort = param.filter(v => v.IL_GOODS_ID == goodsObj.goodsId)
              .map(v => v.GROUP_SORT)[0];
        }
        excelSuccessCount += data.total;

        //상품 등록
        fnSetGroupGoodsGrid(null, goodsObjList, groupIdx);
      }
      , error     : function(xhr, status, strError) {
        //fnKendoMessage({
        //  message : xhr.responseText
        //});
      }
    });
  }

  // ==========================================================================
  // # 상품그룹-복제버튼
  // ==========================================================================
  function fnBtnGroupCopy(oriGroupIdx, evEventGroupId) {
    //alert('# evEventGroupId :: ' + evEventGroupId);

    if (gbGroupCnt >= MAX_GROUP_CNT) {
      fnMessage('', '상품그룹은 ' + MAX_GROUP_CNT + '개까지만 생성할 수 있습니다.', '');
      return false;
    }

    // 그룹 생성
    fnGroupAdd('copy', evEventGroupId);

    // 그룹 항목값 Copy
    fnCopyGroupValue(oriGroupIdx, evEventGroupId);
  }

  // ==========================================================================
  // # 상품그룹-항목값Copy
  // ==========================================================================
  function fnCopyGroupValue(oriGroupIdx, evEventGroupId) {

    var targetGroupIdx = gbGroupIdx;

    // ------------------------------------------------------------------------
    // 상품그룹명(I)
    // ------------------------------------------------------------------------
    $('#groupNm'+targetGroupIdx).val($('#groupNm'+oriGroupIdx).val());
    // ------------------------------------------------------------------------
    // Text Color
    // ------------------------------------------------------------------------
    $('#textColor'+targetGroupIdx).val($('#textColor'+oriGroupIdx).val());
    // ------------------------------------------------------------------------
    // 그룹사용여부(R)
    // ------------------------------------------------------------------------
    var groupUseYnVal = $('input[name=groupUseYn'+oriGroupIdx+']:checked').val();
    $('input:radio[name=groupUseYn'+targetGroupIdx+']:input[value='+groupUseYnVal+']').prop('checked', true);
    // ------------------------------------------------------------------------
    // 상품그룹명배경설정
    // ------------------------------------------------------------------------
    var eventImgTpVal = $('input[name=eventImgTp'+oriGroupIdx+']:checked').val();
    $('input:radio[name=eventImgTp'+targetGroupIdx+']:input[value="'+eventImgTpVal+'"]').prop('checked', true);
    if (eventImgTpVal == 'EVENT_IMG_TP.NOT_USE') {
      $('#bgCdDiv'+targetGroupIdx).hide();
      //
      $('#bgCd'+targetGroupIdx).val('');
    }
    else {
      $('#bgCdDiv'+targetGroupIdx).show();
      // 상품그룹배경색상
      $('#bgCd'+targetGroupIdx).val($('#bgCd'+oriGroupIdx).val());
    }
    // ------------------------------------------------------------------------
    // 상품그룹설명
    // ------------------------------------------------------------------------
    $('#groupDesc'+targetGroupIdx).val($('#groupDesc'+oriGroupIdx).val());
    // ------------------------------------------------------------------------
    // 전시상품수
    // ------------------------------------------------------------------------
    $('#dispCnt'+targetGroupIdx).val($('#dispCnt'+oriGroupIdx).val());
    // ------------------------------------------------------------------------
    // 그룹전시순서
    // ------------------------------------------------------------------------
    $('#groupSort'+targetGroupIdx).val($('#groupSort'+oriGroupIdx).val());
    // ------------------------------------------------------------------------
    // 상품목록
    // ------------------------------------------------------------------------
    // if (evEventGroupId != null) {
    // ------------------------------------------------------------------------
    // DB 조회는 일단 사용하지 않음
    // ------------------------------------------------------------------------
    //let data = $('#inputForm').formSerialize(true);
    //$('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource.read(data);

    // ------------------------------------------------------------------------
    // 화면에서 복제
    // ------------------------------------------------------------------------
    // 원본 그리드
    var oriGroupGridArr = $('#groupGoodsGrid'+oriGroupIdx).data('kendoGrid').dataSource.data();

    if (oriGroupGridArr != undefined && oriGroupGridArr != null && oriGroupGridArr.length > 0) {

      // Target Ds
      var targetGroupGridDs = $('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource;
      var targetGroupGridArr;

      var len = oriGroupGridArr.length;
      var startIdx = len - 1;

      for (var i = startIdx; i >= 0; i--) {
        // row 생성 - 상단에 생성 됨
        targetGroupGridDs.add();
        //targetGroupGridDs.add(oriGroupGridArr.slice()[i]);
        // 값 복제
        $('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource.data()[0].set('goodsSort'        , oriGroupGridArr[i].goodsSort);
        $('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource.data()[0].set('goodsTpNm'        , oriGroupGridArr[i].goodsTpNm);
        $('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource.data()[0].set('ilGoodsId'        , oriGroupGridArr[i].ilGoodsId);
        $('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource.data()[0].set('goodsNm'          , oriGroupGridArr[i].goodsNm);
        $('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource.data()[0].set('standardPrice'    , oriGroupGridArr[i].standardPrice);
        $('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource.data()[0].set('recommendedPrice' , oriGroupGridArr[i].recommendedPrice);
        $('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource.data()[0].set('salePrice'        , oriGroupGridArr[i].salePrice);
        $('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource.data()[0].set('goodsImagePath'   , oriGroupGridArr[i].goodsImagePath);
        $('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource.data()[0].set('tempDataYn'       , 'Y');
      }

    } // End of if (oriGroupGridArr != undefined && oriGroupGridArr != null && oriGroupGridArr.length > 0)
    //}

  }

  // ==========================================================================
  // # 상품그룹-삭제버튼
  // ==========================================================================
  function fnBtnGroupDel(groupIndex) {

    $('#groupTable'+groupIndex).remove();
    gbGroupCnt--;

    // 상품그룹번호갱신(노출용)
    fnChangeGroupViewIdxNum();
  }

  // ==========================================================================
  // # 상품그룹-번호갱신(노출용)
  // ==========================================================================
  function fnChangeGroupViewIdxNum() {

    var viewIdxSpanList = $('[id ^= viewIdxSpan]');

    for (var i = 0; i < viewIdxSpanList.length; i++) {

      var viewIdxSpanId = viewIdxSpanList[i].id;
      $('#'+viewIdxSpanId).text(i+1);
    }

  }
  // # 상품그룹 End
  // ##########################################################################


  // ##########################################################################
  // # 그리드 Start
  // ##########################################################################
  // --------------------------------------------------------------------------
  // # 그리드-그룹상품
  // --------------------------------------------------------------------------
  function fnInitGroupGoodsGrid(groupIdx, evEventGroupId) {
    var callUrl          = '';

    // 페이징없는 그리드
    var groupGoodsGridDs = fnGetDataSource({
      url      : '/admin/pm/event/selectfEventGroupGoodsList?evEventGroupId='+evEventGroupId
    });

    var bEditableGrid = gbBeditable;
    bEditableGrid = false;

    var groupGoodsGridOpt = {
      dataSource  : groupGoodsGridDs
      , noRecordMsg : '검색된 목록이 없습니다.'
      , navigatable : true
      , scrollable  : true
      //, height      : '620'
      , selectable  : true
      , editable    : {
        confirmation: false
        //, confirmDelete: "Yes"
      }
      , resizable   : true
      , autobind    : false
      //, sortable    : {mode : "multiple"}   // default : "single"
      // 임시 툴바 Start
      //        ,toolbar      : [
      //                          { name: 'create', text: '신규', className: "btn-point btn-s"}
      //                        , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
      //                        , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
      //                        ]
      // 임시 툴바 End
      , columns     : [
        { field : 'goodsSort'       , title : '노출순번'      , width:  '60px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return true;}, editor: sortEditor}
        , { field : 'goodsTpNm'       , title : '상품유형'      , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
        , { field : 'ilGoodsId'       , title : '상품코드'      , width: '120px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
        , { field : 'goodsNm'         , title : '상품명'        , width: '300px', attributes : {style : 'text-align:left;'  }, editable:function (dataItem) {return bEditableGrid;}
          , template  : function(dataItem) {
            let imageUrl = dataItem.goodsImagePath ? publicStorageUrl + dataItem.goodsImagePath : '/contents/images/noimg.png';
            return '<img src="' + imageUrl + '" width="50" height="50" align="left" /><BR>&nbsp;&nbsp;' + dataItem.goodsNm ;
          }
        }
        , { field : 'standardPrice'   , title : '원가'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}
          , format: '{0:\#\#,\#}'
          //, template  : function(dataItem) {
          //                if (dataItem != undefined && dataItem != null && dataItem.standardPrice != undefined && dataItem.standardPrice != null && dataItem.standardPrice != '') {
          //                  return String(dataItem.standardPrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
          //                }
          //                else {
          //                  return '';
          //                }
          //              }
        }
        , { field : 'recommendedPrice', title : '정상가'        , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}, format: '{0:\#\#,\#}'}
        , { field : 'discountTpNm'    , title : '할인유형'      , width: '100px', attributes : {style : 'text-align:center;'}}
        , { field : 'salePrice'       , title : '판매가'        , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}, format: '{0:\#\#,\#}'}
        , { field : 'warehouseNm'     , title : '출고처명'      , width: '120px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
        , { field : 'saleStatusNm'    , title : '판매상태'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
        , { field : 'tempDataYn'      , title : '임시데이터'    , width:   '1px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;        }, hidden : true}
        , { field : 'management'      , title : '관리'          , width:  '50px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}
          , template  : function(dataItem) {
            if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
              return  '<div class="btn-area textCenter">'
                  + '<button type="button" class="btn-white btn-s" kind="btnGroupGoodsDel">제거</button>'
                  + '</div>';
            }
            else {
              return  '<div class="btn-area textCenter">'
                  + '<button type="button" name="btnGroupGoodsDel" class="btn-red btn-s" kind="btnGroupGoodsDel">삭제</button>'
                  + '</div>';
            }
          }
        }
      ]
      //, rowTemplate : kendo.template($('#rowTemplate').html())
    };

    // Grid
    $('#groupGoodsGrid'+groupIdx).initializeKendoGrid( groupGoodsGridOpt ).data('kendoGrid');
    //var groupGoodsGrid = $('#groupGoodsGrid'+groupIdx).initializeKendoGrid( groupGoodsGridOpt ).cKendoGrid();

    var groupGoodsGrid = $('#groupGoodsGrid'+groupIdx).initializeKendoGrid(groupGoodsGridOpt).data('kendoGrid');

    // ------------------------------------------------------------------------
    // dataBound 처리
    // ------------------------------------------------------------------------
    groupGoodsGrid.bind('dataBound', function() {

    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#groupGoodsGrid'+groupIdx).on("click", "button[kind=btnGroupGoodsDel]", function(e) {

      // Grid가 N개 생성되므로 아래와 같이 그리드 가져와야 함
      var grid =  $('#groupGoodsGrid'+groupIdx).data('kendoGrid');
      e.preventDefault();
      let dataItem = grid.dataItem($(e.currentTarget).closest("tr"));

      // 그리드 삭제에서 사용하기 위한 행 선택 설정
      grid.select($(e.currentTarget).closest("tr"));

      if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {

        // 임시데이터이므로 그리드에서만 제거
        fnKendoMessage( {
          message : fnGetLangData({key :'', nullMsg : '<div>삭제하시겠습니까?</div>' })
          , type    : 'confirm'
          , ok      : function(){
            // 가변 그리드이므로 매번 let dataSource 를 읽음
            let dataSource =  $('#groupGoodsGrid'+groupIdx).data('kendoGrid').dataSource;
            dataSource.remove(dataItem);
            //fnMessage('', '제거되었습니다.', '');
          }
        });
      }
      else {
        // 상품그룹.상품 삭제 호출
        fnBtnEventGroupDetlDel(dataItem.evEventGroupDetlId, groupIdx);
      }

    });
  }


  // ==========================================================================
  // # 그리드 순번 입력 custom Editor 함수
  // # 그리드 column 옵션에 editor: sortEditor로 적용
  // ==========================================================================
  function sortEditor(container, options) {

    const input = $("<input/>");
    input.attr("name", options.field);
    input.attr("type", "text");

    input.on("click", function(e) {
      e.stopPropagation();
      e.stopImmediatePropagation();
    });

    // blur 이벤트 바인딩
    input.on("blur", function(e) {
      const value = $(this).val().trim();

      if(!value.length) {
        fnKendoMessage({
          message: "순번을 입력해주세요.",
          ok: function() {
            // 입력 값이 없을 경우 강제로 editCell 실행하여, 다시 input 태그를 생성하고, focus를 맞춥니다.
            selectGoodsGrid.editCell(container);
          }
        });
      }
    })

    input.on("keyup", function(e) {
      const regExp = /[^0-9]/g;
      let value = $(this).val();

      if( value && value.match(regExp) ) {
        value = value.replace(regExp, "");
        $(this).val(value);
      }

      if( value.length > 3 ) {
        value = value.substring(0, 3);
        $(this).val(value);
      }
    })

    input.appendTo(container);
  }

  // ##########################################################################
  // # 상품조회팝업
  // ##########################################################################
  // ==========================================================================
  // # 상품조회-팝업버튼-일반기획전-상품그룹
  // ==========================================================================
  function fnBtnAddGoodsPopupGoodsGroup(groupIdx) {

    // ------------------------------------------------------------------------
    // 상품그룹 처리
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // 상품조회 팝업 오픈 호출
    // ------------------------------------------------------------------------
    fnAddGoodsPopup('groupGoodsGrid', groupIdx);
  }

  // ==========================================================================
  // # 상품조회-팝업버튼-골라담기-대표상품
  // ==========================================================================
  function fnBtnAddGoodsPopupSelectTargetGoods() {

    // ------------------------------------------------------------------------
    // 채크
    // ------------------------------------------------------------------------
    // 골라담기 균일가 입력 체크
    var tmpSalectPrice = $('#selectPrice').val();
    var tmpNumSalectPrice = Number(tmpSalectPrice);

    if (tmpSalectPrice    == undefined || tmpSalectPrice    == null || tmpSalectPrice    == '' || tmpSalectPrice == '0' ||
        tmpNumSalectPrice == undefined || tmpNumSalectPrice == null || tmpNumSalectPrice == '' || tmpNumSalectPrice == 0) {
      fnMessage('', '골라담기 균일가를 확인해주세요.', 'selectPrice');
      return false;
    }

    // ------------------------------------------------------------------------
    // 상품조회 팝업 오픈 호출
    // ------------------------------------------------------------------------
    fnAddGoodsPopup('selectTargetGoodsGrid', null);
  }

  // ==========================================================================
  // # 상품조회-팝업버튼-골라담기-상품
  // ==========================================================================
  function fnBtnAddGoodsPopupSelectGoods() {

    // ------------------------------------------------------------------------
    // 체크
    // ------------------------------------------------------------------------
    // 대표상품 존재여부 체크
    var selectTargetGoodsGridArr = $('#selectTargetGoodsGrid').data('kendoGrid').dataSource.data();

    if (selectTargetGoodsGridArr == undefined || selectTargetGoodsGridArr == null || selectTargetGoodsGridArr.length <= 0) {
      fnMessage('', '대표상품을 설정해주세요.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // 상품조회 팝업 오픈 호출
    // ------------------------------------------------------------------------
    fnAddGoodsPopup('selectGoodsGrid', null);
  }

  // ==========================================================================
  // # 상품조회-팝업버튼-골라담기-추가상품
  // ==========================================================================
  function fnBtnAddGoodsPopupSelectAddGoods() {

    // ------------------------------------------------------------------------
    // 체크
    // ------------------------------------------------------------------------
    // 대표상품 존재여부 체크
    var selectTargetGoodsGridArr = $('#selectTargetGoodsGrid').data('kendoGrid').dataSource.data();
    //console.log('# 대표상품 데이터 :: ', JSON.stringify(selectTargetGoodsGridArr[0]));

    if (selectTargetGoodsGridArr == undefined || selectTargetGoodsGridArr == null || selectTargetGoodsGridArr.length <= 0) {
      fnMessage('', '대표상품을 설정해주세요.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // 상품조회 팝업 오픈 호출
    // ------------------------------------------------------------------------
    fnAddGoodsPopup('selectAddGoodsGrid', null);
  }

  // ==========================================================================
  // # 상품조회-팝업버튼-증정행사-상품
  // ==========================================================================
  function fnBtnAddGoodsPopupGiftGoods() {

    // ------------------------------------------------------------------------
    // 체크
    // ------------------------------------------------------------------------
    // TODO

    // ------------------------------------------------------------------------
    // 상품조회 팝업 오픈 호출
    // ------------------------------------------------------------------------
    fnAddGoodsPopup('giftGoodsGrid', null);
  }

  // ==========================================================================
  // # 상품조회-팝업버튼-증정행사-적용대상상품
  // ==========================================================================
  function fnBtnAddGoodsPopupGiftTargetGoods() {

    // ------------------------------------------------------------------------
    // 체크
    // ------------------------------------------------------------------------
    // TODO

    // ------------------------------------------------------------------------
    // 상품조회 팝업 오픈 호출
    // ------------------------------------------------------------------------
    fnAddGoodsPopup('giftTargetGoodsGrid', null);
  }

  // ==========================================================================
  // # 상품조회-팝업버튼-증정행사-적용대상브랜드
  // ==========================================================================
  function fnBtnAddGoodsPopupGiftTargetBrand() {

    // ------------------------------------------------------------------------
    // 체크
    // ------------------------------------------------------------------------
    // 선택 체크
    var dpBrandId = $('#dpBrandId').data().kendoDropDownList.value();
    if (dpBrandId== undefined ||
        dpBrandId == null ||
        dpBrandId == '') {
      fnMessage('', '브랜드를 선택해주세요.', '');
      return false;
    }

    var gridObj;
    var bIsDupGoodsId;

    // ------------------------------------------------------------------------
    // 1. 현재 그리드 조회
    // ------------------------------------------------------------------------
    var oriCnt = 0;
    // DataSource
    var gridDs = $('#giftTargetBrandGrid').data('kendoGrid').dataSource;
    var gridArr = gridDs.data();

    if (gridArr != undefined && gridArr != null && gridArr.length > 0) {
      oriCnt = gridArr.length;

      if (oriCnt > 0) {

        for (var j = 0; j < gridArr.length; j++) {

          if (Number(gridArr[j].brandId) == dpBrandId) {
            // 동일한것이 존재하면 skip
            bIsDupGoodsId = true;
            break;
          }
        }

        if (bIsDupGoodsId == true) {
          return false;
        }
      } // End of if (oriCnt > 0)
    }

    gridObj = new Object();
    gridObj.brandId             = dpBrandId;
    gridObj.giftTargetBrandTp   = 'GIFT_TARGET_BRAND_TP.DISPLAY';  // 전시브랜드
    gridObj.giftTargetBrandTpNm = '전시브랜드';                    // 전시브랜드
    gridObj.brandNm             = $('#dpBrandId').data().kendoDropDownList.text();
    gridObj.tempDataYn          = 'Y';    // 저장 전에 추가된 로우 여부
    gridDs.insert(oriCnt, gridObj);

  }

  // ##########################################################################
  // # 상품조회
  // ##########################################################################
  // ==========================================================================
  // # 상품조회-팝업오픈
  // ==========================================================================
  function fnAddGoodsPopup(inGridId, groupIdx) {

    var params = {};
    params.selectType = 'multi'; // 그리드 체크박스 타입 ( single : 하나만 선택됨, multi : 여러개 선택됨 )
    params.goodsType  = 'GOODS_TYPE.NORMAL,GOODS_TYPE.DISPOSAL';  // 상품유형(복수 검색시 , 로 구분)

    params.columnNameHidden                       = false;                                    // 배송정책
    params.columnDpBrandNameHidden                = false;                                    // 전시 브랜드
    params.columnStardardPriceHidden              = false;                                    // 원가
    params.columnRecommendedPriceHidden           = false;                                    // 정상가
    params.columnSaleStatusCodeNameHidden         = false;                                    // 판매상태
    params.columnStorageMethodHidden              = true;                                     // 보관방법

    if (inGridId == 'groupGoodsGrid') {
      // ----------------------------------------------------------------------
      // 기획전 - 일반
      // ----------------------------------------------------------------------
      params.goodsCallType                      = 'exhibitNormal';                            // 조회조건 : 일반기획전
      params.saleStatus                         = 'SALE_STATUS.ON_SALE';                      // 판매상태:판매중

      params.columnSalePriceHidden              = false;                                      // 판매가
      params.columnGoodsDisplyYnHidden          = false;                                      // 전시상태
    }
    else if (inGridId == 'selectTargetGoodsGrid') {
      // ----------------------------------------------------------------------
      // 골라담기-대표상품
      // ----------------------------------------------------------------------
      params.goodsCallType                      = 'exhibitSelectTargetGoods';                 // 팝업상품조회유형 : 골라담기-대표상품
      params.saleStatus                         = 'SALE_STATUS.ON_SALE';                      // 판매상태:판매중
      params.goodsType                          = 'GOODS_TYPE.NORMAL,GOODS_TYPE.DISPOSAL';    // 체크할 상품유형(복수 검색시 , 로 구분) : 일반/폐기임박
      params.selectType                         = 'single';                                   // 선택유형:단일선택

      params.columnSalePriceHidden              = false;                                      // 판매가
      params.columnAreaShippingDeliveryYnHidden = false;                                      // 도서산간/제주배송
      params.columnGoodsDisplyYnHidden          = false;                                      // 전시상태
      params.selectMultiRowYn                   = false;                                      // 다중선택여부
    }
    else if (inGridId == 'selectGoodsGrid') {
      // ----------------------------------------------------------------------
      // 골라담기-상품
      // ----------------------------------------------------------------------
      params.goodsCallType                      = 'exhibitSelectGoods';                       // 팝업상품조회유형 : 골라담기-상품
      params.saleStatus                         = 'SALE_STATUS.ON_SALE';                      // 판매상태:판매중
      params.goodsType                          = 'GOODS_TYPE.NORMAL,GOODS_TYPE.DISPOSAL';    // 체크할 상품유형(복수 검색시 , 로 구분) : 일반/폐기임박

      params.columnSalePriceHidden              = false;                                      // 판매가
      params.columnAreaShippingDeliveryYnHidden = false;                                      // 도서산간/제주배송
      params.columnGoodsDisplyYnHidden          = false;                                      // 전시상태
      // 골라담기-대표상품의 출고처
      let targetGrid    = $('#selectTargetGoodsGrid').data('kendoGrid');
      let targetGridDs  = targetGrid.dataSource;
      let targetGridArr = targetGridDs.data();

      if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length <= 0) {
        fnMessage('', '대표상품을 확인하세요.', '');
        return false;
      }
      let urWarehouseId = targetGridArr[0].urWarehouseId;
      params.warehouseId                        = urWarehouseId;                              // 출고처ID
      params.warehouseIdDisabled                = true;                                       // 출고처 조건고정
      params.undeliverableAreaTp                = targetGridArr[0].undeliverableAreaTp;       // 배송불가지역(공통코드, 도서산간/제주배송)
      //console.log('# params :: ', JSON.stringify(params));
    }
    else if (inGridId == 'selectAddGoodsGrid') {
      // ----------------------------------------------------------------------
      // 골라담기-추가상품
      // ----------------------------------------------------------------------
      params.goodsCallType                      = 'addGoods';                                 // 팝업상품조회유형 : 추가상품
      params.saleStatus                         = 'SALE_STATUS.ON_SALE';                      // 판매상태:판매중
      params.goodsType                          = 'GOODS_TYPE.ADDITIONAL';                    // 체크할 상품유형(복수 검색시 , 로 구분) : 추가

      params.columnAreaShippingDeliveryYnHidden = false;                                      // 도서산간/제주배송
      params.isGoodsTypes                       = false                                       // 조회조건 : 상품유형,전시여부 숨김처리
      // 골라담기-대표상품의 출고처
      let targetGrid    = $('#selectTargetGoodsGrid').data('kendoGrid');
      let targetGridDs  = targetGrid.dataSource;
      let targetGridArr = targetGridDs.data();

      if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length <= 0) {
        fnMessage('', '대표상품을 확인하세요.', '');
        return false;
      }
      // 추가조건 (대표상품의 출고처/합배송/배송불가지역 조건이 동일해야 함)
      params.warehouseId                        = targetGridArr[0].urWarehouseId;             // 출고처ID
      params.warehouseIdDisabled                = true;                                       // 출고처 조건고정
      //params.ilShippingTmplId                   = targetGridArr[0].ilShippingTmplId;        // 배송정책ID (제외, 2021.03.09 이석호M)
      // 합배송은 상품팝업에서 설정
      //params.undeliverableAreaTp                = targetGridArr[0].undeliverableAreaTp;       // 배송불가지역(공통코드, 도서산간/제주배송)
      //console.log('# params :: ', JSON.stringify(params));
    }
    else if (inGridId == 'giftGoodsGrid') {
      // ----------------------------------------------------------------------
      // 증정행사-상품
      // ----------------------------------------------------------------------
      params.goodsCallType                      = 'giftGoods';                                // 팝업상품조회유형 : 증정행사-상품
      params.saleStatus                         = 'SALE_STATUS.ON_SALE';                      // 판매상태:판매중
      params.goodsType                          = 'GOODS_TYPE.GIFT, GOODS_TYPE.GIFT_FOOD_MARKETING';                          // 체크할 상품유형(복수 검색시 , 로 구분) : 증정
      // 지급방식이 지정일 경우 1개 선택
      let giftGiveTp = $('#giftGiveTp').val();
      if (giftGiveTp == 'GIFT_GIVE_TP.FIXED') {
        params.selectType                         = 'single';                                 // 선택유형:단일선택
      }


      if ($('#giftShippingTp').val() == 'GIFT_SHIPPING_TP.COMBINED') {
        // 합배송인 경우
        if ($('#urWarehouseId').val() == undefined || $('#urWarehouseId').val() == null || $('#urWarehouseId').val() == '') {
          fnMessage('', '출고처를 선택해주세요.', 'urWarehouseId');
          return false;
        }
        params.warehouseId                        = $('#urWarehouseId').val();                // 출고처ID
        params.warehouseIdDisabled                - true;                                     // 출고처 조건고정
      }
      params.columnSalePriceHidden              = false;                                      // 판매가
      params.columnAreaShippingDeliveryYnHidden = false;                                      // 도서산간/제주배송
      params.columnGoodsDisplyYnHidden          = false;                                      // 전시상태
    }
    else if (inGridId == 'giftTargetGoodsGrid') {
      // ----------------------------------------------------------------------
      // 증정행사-적용대상상품
      // ----------------------------------------------------------------------
      params.goodsCallType                      = 'basePackageGoods';                         // 팝업상품조회유형 : 증정행사-적용대상상품
      params.saleStatus                         = 'SALE_STATUS.ON_SALE';                      // 판매상태:판매중
      params.goodsType                          = 'GOODS_TYPE.NORMAL,GOODS_TYPE.DISPOSAL';    // 체크할 상품유형(복수 검색시 , 로 구분) : 일반/폐기임박
      //params.goodsCallType                      = 'basePackageGoods';                       // 조회조건 : 일반, 폐기임박

      if ($('#giftShippingTp').val() == 'GIFT_SHIPPING_TP.COMBINED') {
        // 합배송인 경우
        if ($('#urWarehouseId').val() == undefined || $('#urWarehouseId').val() == null || $('#urWarehouseId').val() == '') {
          fnMessage('', '출고처를 선택해주세요.', 'urWarehouseId');
          return false;
        }
        params.warehouseId                        = $('#urWarehouseId').val();                // 출고처ID
        params.warehouseIdDisabled                - true;                                     // 출고처 조건고정
      }
      params.columnSalePriceHidden              = false;                                      // 판매가
      params.columnAreaShippingDeliveryYnHidden = false;                                      // 도서산간/제주배송
      params.columnGoodsDisplyYnHidden          = false;                                      // 전시상태
    }

    //params.supplierId = viewModel.ilGoodsDetail.urSupplierId;
    //params.warehouseId = viewModel.ilGoodsDetail.urWarehouseId;

    fnKendoPopup({
      id          : 'goodsSearchPopup'
      , title       : '상품조회팝업'
      , width       : '1700px'
      , height      : '800px'
      , scrollable  : 'yes'
      , src         : '#/goodsSearchPopup'
      , param       : params
      , success     : function( id, data ) {
        //console.log('# 상품결과 :: ', JSON.stringify(data));
        // ----------------------------------------------------
        // 선택하지 않은 경우 처리
        // ----------------------------------------------------
        if (data == undefined || data == null || data == '' || data.length <= 0) {
          return false;
        }
        if (data.length == undefined || data.length == null || data.length == '') {
          return false;
        }
        if (data.length > 0) {
          if (data[0].goodsId == undefined || data[0].goodsId == null || data[0].goodsId == '' || data[0].goodsId <= 0) {
            return false;
          }
        }

        if (inGridId == 'groupGoodsGrid') {
          // --------------------------------------------------
          // 상품그룹
          // --------------------------------------------------
          fnSetGroupGoodsGrid(id, data, groupIdx);
        }
        else if (inGridId == 'selectTargetGoodsGrid') {
          // --------------------------------------------------
          // 골라담기-대표상품
          // --------------------------------------------------
          // 기존 골라담기-대표상품 그리드
          let targetGrid    = $('#selectTargetGoodsGrid').data('kendoGrid');
          let targetGridDs  = targetGrid.dataSource;
          let targetGridArr = targetGridDs.data();

          if (targetGridArr != undefined && targetGridArr != null && targetGridArr != '' && targetGridArr.length > 0) {
            // 기존에 존재하면 메시지 노출
            fnKendoMessage({
              message : fnGetLangData({key :'', nullMsg : '<div>대표상품 변경 시 골라담기/추가 상품이 초기화 됩니다.</div><div>진행하시겠습니까?</div>' })
              , type    : 'confirm'
              , ok      : function(){
                // 그리드팝업조회결과처리-골라담기-대표상품
                fnSetSelectTargetGoodsGrid(id, data);
              }
            });
          }
          else {
            // 그리드팝업조회결과처리-골라담기-대표상품
            fnSetSelectTargetGoodsGrid(id, data);
          }
        }
        else if (inGridId == 'selectGoodsGrid') {
          // --------------------------------------------------
          // 골라담기-상품
          // --------------------------------------------------
          fnSetSelectGoodsGrid(id, data);
          // 메시지
          fnMessage('', '상품추가가 완료되었습니다. (중복 추가상품 제외)', '');
        }
        else if (inGridId == 'selectAddGoodsGrid') {
          // --------------------------------------------------
          // 골라담기-추가상품
          // --------------------------------------------------
          fnSetSelectAddGoodsGrid(id, data);
        }
        else if (inGridId == 'giftGoodsGrid') {
          // --------------------------------------------------
          // 증정행사-상품
          // --------------------------------------------------
          fnSetGiftGoodsGrid(id, data);
        }
        else if (inGridId == 'giftTargetGoodsGrid') {
          // --------------------------------------------------
          // 증정행사-적용대상상품
          // --------------------------------------------------
          fnSetGiftTargetGoodsGrid(id, data);
        }
        else if (inGridId == 'giftTargetBrandGrid') {
          // --------------------------------------------------
          // 증정행사-적용대상브랜드
          // --------------------------------------------------
          //fnSetGiftTargetBrandGrid(id, data);
        }
      }
    });
  }

  // ==========================================================================
  // # 그리드팝업조회결과처리-상품그룹
  // ==========================================================================
  function fnSetGroupGoodsGrid(id, data, groupIdx) {

    // ************************************************************************
    // TODO 임시 상품 세팅 Start
    // TODO 로직변경
    var gridObj;

    // ------------------------------------------------------------------------
    // 1. 현재 그리드 조회
    // ------------------------------------------------------------------------
    var oriCnt = 0;
    // DataSource
    var gridDs = $('#groupGoodsGrid'+groupIdx).data('kendoGrid').dataSource;
    var gridArr = gridDs.data();

    if (gridArr != undefined && gridArr != null && gridArr.length > 0) {
      oriCnt = gridArr.length;
    }

    if (data != null && data != '' && data.length > 0) {

      var addIdx = 0;
      var bIsDupGoodsId;

      for (var i = 0;  i < data.length; i++) {

        bIsDupGoodsId = false;

        // --------------------------------------------------------------------
        // 2. 그리드 추가 체크
        // --------------------------------------------------------------------
        // --------------------------------------------------------------------
        // 2.1. 중복상품 제거
        // --------------------------------------------------------------------
        if (oriCnt > 0) {

          for (var j = 0; j < gridArr.length; j++) {

            if (Number(gridArr[j].ilGoodsId) == data[i].goodsId) {
              // 동일한것이 존재하면 skip
              bIsDupGoodsId = true;
              break;
            }
          }

          if (bIsDupGoodsId == true) {
            continue;
          }

        } // End of if (oriCnt > 0)

        // --------------------------------------------------------------------
        // 2.2. TODO 다른 등록 제한 체크 추가할 것
        // --------------------------------------------------------------------

        // --------------------------------------------------------------------
        // 3. 상품ID 등록
        // --------------------------------------------------------------------
        gridObj = new Object();
        gridObj.goodsSort           = (data[i].goodsSort != null ? data[i].goodsSort : 999);  // (addIdx+1)+oriCnt;
        gridObj.goodsTpNm           = data[i].goodsTypeName;
        gridObj.ilGoodsId           = data[i].goodsId;
        gridObj.goodsImagePath      = data[i].itemImagePath;
        gridObj.goodsNm             = data[i].goodsName;
        gridObj.standardPrice       = data[i].standardPrice + '';
        gridObj.recommendedPrice    = data[i].recommendedPrice + '';
        gridObj.salePrice           = data[i].salePrice + '';
        gridObj.discountTpNm        = data[i].discountTpName + '';
        gridObj.undeliverableAreaTp = data[i].undeliverableAreaTp;
        gridObj.urWarehouseId       = data[i].warehouseId;          // 출고처
        gridObj.warehouseNm         = data[i].warehouseName;        // 출고처명
        gridObj.saleStatus          = data[i].saleStatusCode;       // 판매상태
        gridObj.saleStatusNm        = data[i].saleStatusCodeName;   // 판매상태명
        gridObj.tempDataYn          = 'Y';                          // 저장 전에 추가된 로우 여부
        gridDs.insert(addIdx, gridObj);
        addIdx++;

      } // End of for (var i = 0;  i < data.length; i++)

      // ----------------------------------------------------------------------
      // 정렬
      // ----------------------------------------------------------------------
      gridDs.sort(
          [
            {field : 'goodsSort', dir   : 'asc'}
            , {field : 'ilGoodsId', dir   : 'desc'}
          ]
      );

    }  // End of if (data != null && data != '' && data.length > 0)

    // TODO 임시 상품 세팅 End
    // ************************************************************************

//if (false) {
//                         if (data.length > 0) {
//                           var goodsIds = [];
//
//                           for (var i = 0; i < data.length; i++) {
//                             goodsIds.push(data[i].goodsId);
//                           }
//                           // --------------------------------------------------
//                           // 선택한 상품의 상품그룹 리스트 조회
//                           // --------------------------------------------------
//                           // TODO 조회 쿼리 만들 것
//                           var url  = '/admin/goods/regist/getGoodsList';
//                           var cbId = 'goodsList';
//
//                           var data = { ilGoodsIds : goodsIds.join(",") }
//
//                           fnAjax({
//                               url       : url
//                             , params    : data
//                             , async     : false
//                             , success   : function( data ) {
//                                             // --------------------------------
//                                             // 그리드에 추가할 것
//                                             // --------------------------------
//                                             // TODO goodsPackageGoodsMappingGridDs.insert(i, data.goodsPackageGoodsMappingList[i]);
//                                             var dataRows = data.rows;
//
//                                             for (var i in dataRows) {
//                                               aGridDs.add(dataRows[i]);
//                                             }
//                                           }
//                             , isAction  : 'batch'
//                           });
//                         }
//
//}

  }


  function fnCoverageInit(){
    ctgryGridDs =  new kendo.data.DataSource();

    ctgryGridOpt = {
      dataSource : ctgryGridDs,
      editable : false,
      filterble : true,
      columns : [
        {
          field : 'categoryType',
          title : '구분',
          width : '15%',
          attributes : {
            style : 'text-align:center'
          }
        },
        {
          field : 'coverageName',
          encoded: false,
          title : '항목',
          width : '45%',
          attributes : {
            style : 'text-align:left'
          }
        }, {
          field : 'includeYnName',
          title : '포함/제외',
          width : '20%',
          attributes : {
            style : 'text-align:center'
          }
        }, {
          command : {text: '해제',click:fnDelCoverage} ,
          title : '관리',
          width : '20%',
          attributes : {
            style : 'text-align:center',
            class : 'forbiz-cell-readonly'
          }
        },
        { field:'addCoverageId', hidden:true},
        { field:'includeYn', hidden:true}
      ]
    };

    ctgryGrid = $('#ctgryGrid').initializeKendoGrid(ctgryGridOpt).cKendoGrid();

    $("#goodsCoverageDiv").hide();
  }

  function fnCoverageApprInit(){
    ctgryGridDs =  new kendo.data.DataSource();

    ctgryGridOpt = {
      dataSource : ctgryGridDs,
      editable : false,
      columns : [
        {
          field : 'categoryType',
          title : '구분',
          width : '15%',
          attributes : {
            style : 'text-align:center'
          }
        },
        {
          field : 'coverageName',
          encoded: false,
          title : '항목',
          width : '45%',
          attributes : {
            style : 'text-align:left'
          }
        }, {
          field : 'includeYnName',
          title : '포함/제외',
          width : '20%',
          attributes : {
            style : 'text-align:center'
          }
        },
        { field:'addCoverageId', hidden:true},
        { field:'includeYn', hidden:true},
        { field:'apprStat', hidden:true}
      ]
    };

    ctgryGrid = $('#ctgryGrid').initializeKendoGrid(ctgryGridOpt).cKendoGrid();


  }

  function fnDelCoverage(e){
    e.preventDefault();
    var dataItem = ctgryGrid.dataItem($(e.currentTarget).closest('tr'));
    ctgryGridDs.remove(dataItem);
    return;
  }

  // 상품 추가 - 상품검색 팝업
  function fnAddCoverage(  ){
    var obj = new Object();

    var goodsCoverageType = $('#goodsCoverageType').val();

    if(goodsCoverageType == 'APPLYCOVERAGE.GOODS'){  //쿠폰종류 : 상품, 장바구니

      var params = {};
      params.goodsType = "GOODS_TYPE.NORMAL,GOODS_TYPE.DISPOSAL";	// 상품유형(복수 검색시 , 로 구분)

      params.selectType = "multi";
      params.searchChoiceBtn = "Y";
      params.columnNameHidden = false;
      params.columnAreaShippingDeliveryYnHidden = false;
      params.columnDpBrandNameHidden = false;
      params.columnStardardPriceHidden = false;
      params.columnRecommendedPriceHidden = false;
      params.columnSalePriceHidden = false;
      params.columnSaleStatusCodeNameHidden = false;
      params.columnGoodsDisplyYnHidden = false;



      fnKendoPopup({
        id         : "goodsSearchPopup",
        title      : "상품조회",  // 해당되는 Title 명 작성
        width      : "1900px",
        height     : "800px",
        scrollable : "yes",
        src        : "#/goodsSearchPopup",
        param      : params,
        success    : function( id, data ){

          if(data[0] != undefined){

            var dataCheck = $("#ctgryGrid").data("kendoGrid").dataSource._data;
            var dataCheckSize = dataCheck.length;
            if(dataCheck.length > 0 ){

              var dataCheckAarray =[];
              for(var i=0;i<dataCheck.length ;i++){
                dataCheckAarray.push(dataCheck[i].coverageId);
              }

              var goodsDataCheckArray =[];
              for(var j=0 ; j<data.length; j++){
                goodsDataCheckArray.push(data[j].goodsId);
              }
              var sum = dataCheckAarray.concat(goodsDataCheckArray);
              var intersec = sum.filter((item, index) => sum.indexOf(item) !== index);

              for(var k=0 ; k<data.length; k++){
                var item = data[k].goodsId;

                if(!intersec.includes(item)){
                  var goodsDisplayType;
                  if( data[k].goodsDisplayYn == 'Y'){
                    goodsDisplayType = '가능';
                  }else{
                    goodsDisplayType = '불가능';
                  }
                  obj["categoryType"] = '상품';
                  obj["coverageName"] = '상품코드 : ' + data[k].goodsId + "</br>" + '판매상태 : ' + data[k].saleStatusCodeName + ' / 전시상태 : ' + goodsDisplayType  + "</br>" + '상품명 : ' + data[k].goodsName;
                  obj["includeYnName"] = $('#goodsIncludeYn').val() == 'Y' ? '포함' : '제외';
                  obj["includeYn"] = $('#goodsIncludeYn').val();
                  obj["coverageId"] = data[k].goodsId;
                  obj["coverageType"] = 'APPLYCOVERAGE.GOODS';
                  ctgryGridDs.add(obj);
                }
              }

            }else{
              for(var k=0;k<data.length;k++){
                var goodsDisplayType;
                if( data[k].goodsDisplayYn == 'Y'){
                  goodsDisplayType = '가능';
                }else{
                  goodsDisplayType = '불가능';
                }
                obj["categoryType"] = '상품';
                obj["coverageName"] = '상품코드 : ' + data[k].goodsId + "</br>" + '판매상태 : ' + data[k].saleStatusCodeName + ' / 전시상태 : ' + goodsDisplayType  +"</br>" + '상품명 : ' + data[k].goodsName;
                obj["includeYnName"] = $('#goodsIncludeYn').val() == 'Y' ? '포함' : '제외';
                obj["includeYn"] = $('#goodsIncludeYn').val();
                obj["coverageId"] = data[k].goodsId;
                obj["coverageType"] = 'APPLYCOVERAGE.GOODS';
                ctgryGridDs.add(obj);
              }
            }

          }
        }
      });

    }else if(goodsCoverageType == 'APPLYCOVERAGE.BRAND'){


      var dataCheck =$("#ctgryGrid").data("kendoGrid").dataSource._data;

      if($('#selectBrandCoverage').val() == ''){
        fnKendoMessage({message : '전시브랜드가 선택되지 않았습니다.'});
        return;
      }

      for(var i=0; i<dataCheck.length; i++){
        if(dataCheck[i].coverageId == $('#selectBrandCoverage').val()){
          fnKendoMessage({message : '이미 존재하는 브랜드입니다.'});
          return;
        }

      }

      //쿠폰종류 : 상품
      obj["categoryType"] = '전시브랜드';
      obj["coverageName"] = '전시브랜드 코드 : ' + $('#selectBrandCoverage').val() + "</br>" + '전시브랜드명 : ' + $('#brandName').val();
      obj["includeYnName"] = $('#goodsIncludeYn').val() == 'Y' ? '포함' : '제외';
      obj["includeYn"] = $('#goodsIncludeYn').val();
      obj["coverageId"] = $('#selectBrandCoverage').val();
      obj["coverageType"] = 'APPLYCOVERAGE.BRAND';
      ctgryGridDs.add(obj);

      // 전시 브랜드 선택 초기화
      $("#selectBrandCoverage").data('kendoDropDownList').value("");

    }else{
      var coverageName;
      var coverageId;
      if(ilCtgryStd4 != null){
        coverageName = '대분류 : ' + ilCtgryStd1 + ' / 중분류 :' + ilCtgryStd2 + ' / 소분류 : ' + ilCtgryStd3 + ' / </br>' + ' 세분류 :' +ilCtgryStd4;
        coverageId = $('#ilCtgryStd4').val();
      }else if(ilCtgryStd3 != null){
        coverageName =  '대분류 : ' + ilCtgryStd1 + ' / 중분류 :' + ilCtgryStd2 + ' / 소분류 : ' + ilCtgryStd3;
        coverageId = $('#ilCtgryStd3').val();
      }else if(ilCtgryStd2 != null){
        coverageName = '대분류 : ' + ilCtgryStd1 + ' / 중분류 :' + ilCtgryStd2 ;
        coverageId = $('#ilCtgryStd2').val();
      }else{
        coverageName = '대분류 : ' + ilCtgryStd1;
        coverageId = $('#ilCtgryStd1').val();
      }

      if(coverageId == ''){
        fnKendoMessage({message : '분류선택 후 추가해주세요.'});
        return;
      }

      var dataCheck =$("#ctgryGrid").data("kendoGrid").dataSource._data;
      for(var i=0; i<dataCheck.length; i++){
        if(dataCheck[i].coverageId == coverageId){
          fnKendoMessage({message : '이미 존재하는 카테고리입니다.'});
          return;
        }

      }

      //쿠폰종류 : 상품
      obj["categoryType"] = '전시카테고리';
      obj["coverageName"] = '전시카테고리 코드 : ' + coverageId + "</br>"+ coverageName;
      obj["includeYnName"] = $('#goodsIncludeYn').val() == 'Y' ? '포함' : '제외';
      obj["includeYn"] = $('#goodsIncludeYn').val();
      obj["coverageId"] = coverageId;
      obj["coverageType"] = 'APPLYCOVERAGE.DISPLAY_CATEGORY';
      ctgryGridDs.add(obj);

      ilCtgryStd1 = null;
      ilCtgryStd2 = null;
      ilCtgryStd3 = null;
      ilCtgryStd4 = null;

      $("#ilCtgryStd1").data('kendoDropDownList').value("");
      $("#ilCtgryStd2").data('kendoDropDownList').value("");
      $("#ilCtgryStd3").data('kendoDropDownList').value("");
      $("#ilCtgryStd4").data('kendoDropDownList').value("");

    }


  }

  // ==========================================================================
  // ------------------------------- Html 버튼 바인딩  Start ------------------
  // ==========================================================================
  ///** Common Search */
  //$scope.fnSearch             = function () {fnSearch();};

  /** 저장 */
  $scope.fnBtnSave            = function () {fnSave();};

  /** 당첨자공지사항 팝업 오픈 */
  $scope.fnBtnWinnerNoticePopup = function () {fnOpenPopupWinnerNotice();};

  /** 당첨자공지사항 등록 */
  $scope.fnBtnWinnerNotice    = function () {fnSaveWinnerNotice();};

  /** 목록가기 */
  $scope.fnBtnGoList          = function () {fnGoList();};

  /** 참여목록가기 */
  $scope.fnBtnEventJoin       = function () {fnBtnEventJoin();};

  /** 삭제 */
  $scope.fnBtnDelete          = function () {fnDelete();};

  /** 이벤트 미리보기 */
  $scope.fnBtnEventPreview      = function () {fnBtnEventPreview();};

  /** 적용 범위 */
  $scope.fnAddCoverage = function(){fnAddCoverage();};

  ///** Common New */
  //$scope.fnBtnExhibitNew      = function () {fnBtnExhibitNew();};

  ///** Common Clear */
  //$scope.fnClear              = function () {fnClear();};

  ///** Common Confirm */
  //$scope.fnConfirm            = function () {fnConfirm();};

  ///** Common Close */
  //$scope.fnClose              = function () {fnClose();};

  ///** Common ShowImage */
  //$scope.fnShowImage          = function (url) {fnShowImage(url);};

  /** 적립금선택 : [일반][설문][체험단] */
  $scope.fnBtnAddPoint        = function (gridId, index, tagId) {fnAddPoint(gridId, index, tagId);};

  /** 쿠폰선택 : [체험단] */
  $scope.fnBtnAddCoupon       = function () {fnAddCoupon();};

  /** 쿠폰조회팝업 : [일반][설문][체험단] */
  $scope.fnBtnAddCouponPopup  = function (gridId) {fnAddCouponPopup(gridId);};

  /** 상품조회팝업 : [체험단] */
  $scope.fnBtnAddGoodsPopup   = function (gridId) {fnBtnAddGoodsExperiencePopup(gridId);};
  /** 상품조회팝업버튼-상품그룹 */
  $scope.fnBtnAddGoodsPopupGoodsGroup         = function(groupIdx){ fnBtnAddGoodsPopupGoodsGroup(groupIdx);};
  /** 상품조회팝업버튼-골라담기-대표상품 */
  $scope.fnBtnAddGoodsPopupSelectTargetGoods  = function(){ fnBtnAddGoodsPopupSelectTargetGoods();};
  /** 상품조회팝업버튼-골라담기-상품 */
  $scope.fnBtnAddGoodsPopupSelectGoods        = function(){ fnBtnAddGoodsPopupSelectGoods();};
  /** 상품조회팝업버튼-골라담기-추가상품 */
  $scope.fnBtnAddGoodsPopupSelectAddGoods     = function(){ fnBtnAddGoodsPopupSelectAddGoods();};
  /** 상품조회팝업버튼-증정상품-상품 */
  $scope.fnBtnAddGoodsPopupGiftGoods          = function(){ fnBtnAddGoodsPopupGiftGoods();};
  /** 상품조회팝업버튼-증정상품-적용대상상품 */
  $scope.fnBtnAddGoodsPopupGiftTargetGoods    = function(){ fnBtnAddGoodsPopupGiftTargetGoods();};
  /** 상품조회팝업버튼-증정상품-적용대상브랜드 */
  $scope.fnBtnAddGoodsPopupGiftTargetBrand    = function(){ fnBtnAddGoodsPopupGiftTargetBrand();};

  /** Common 상품그룹추가 */
  $scope.fnBtnGroupAdd                        = function(){ fnBtnGroupAdd();      };

  /** Common 샘플다운로드 */
  $scope.fnSampleFormDownload                 = function() { fnSampleFormDownload();}

  /** Common 그룹별 상품업로드 */
  $scope.fnBtnGroupGoodsUpload                = function(){ fnBtnGroupGoodsExcelUpload();      };

  /** 엑셀 업로드 */
  $scope.fnExcelUpload                        = function(event) { fnExcelUpload(event);}

  /** Common BtnGroupCopy */
  $scope.fnBtnGroupCopy                       = function(groupIdx, evEventGroupId){ fnBtnGroupCopy(groupIdx, evEventGroupId);};
  /** Common BtnGroupDel */
  $scope.fnBtnGroupDel                        = function(groupIdx){ fnBtnGroupDel(groupIdx);};

  /** Common File Upload */
  $scope.fnBtnImage           = function(imageType, gubn) {
                                  //console.log('# fnBtnImage Click :: ', imageType);
                                  // ------------------------------------------
                                  // 첨부 수정가능 체크
                                  // ------------------------------------------
                                  if (gbMode != 'insert') {
                                    if (gbStatusSe == 'ING' || gbStatusSe == 'END') {
                                      if (imageType != 'imgBannerPc' && imageType != 'imgBannerMobile') {
                                        console.log('# 진행예정인 경우만 수정이 가능합니다.');
                                        //fnMessage('', '진행예정인 경우만 수정이 가능합니다.', '');
                                        return false;
                                      }
                                    }
                                  }

                                  //if (imageType.startsWith('imgSurvey')) {
                                  if (gubn == 'NonKendo') {
                                    // ----------------------------------------
                                    // Kendo 미사용 버전 (No id, 동일 name의 다건 이미지)
                                    //  - 핸들러         : fileSelectFunction
                                    //  - 업로드 및 처리 : fnUploadImageNoId
                                    // ----------------------------------------
                                    //console.log('# fnBtnImage(NonKendo)');
                                    //imageType == 'imgStampDefault' ||
                                    //imageType == 'imgStampCheck'   ||
                                    //imageType == 'imgStampBg'
                                    //let imageTypeArr = imageType.split('imgSurvey');
                                    //let index = imageTypeArr[1];
                                    //console.log('# index :: ', index);
                                    const $target = $(event.target);
                                    //const $tr     = $target.closest('tr');
                                    //const $trId   =  $tr.attr('id');
                                    ////console.log('# $trId  :: ', $trId);
                                    //let trIdArr = $trId.split('_');
                                    //let index   = trIdArr[1];
                                    ////console.log('# index  :: ', index);
                                    ////console.log('# imageType  :: ', imageType);
                                    //let imageTypeArr = imageType.split('imgSurvey');
                                    ////console.log('# imageTypeArr[1] :: ', imageTypeArr[1]);
                                    //let indexArr = imageTypeArr[1].split('_');
                                    ////let mainIndex    = indexArr[0];
                                    //let subIndex = indexArr[1];
                                    //console.log('# mainIndex:: ', mainIndex);
                                    //console.log('# subIndex :: ', subIndex);
                                    //const rowIndex = $tr.data('rowId');
                                    //console.log('# rowIndex :: ', rowIndex);

                                    const $fileEl = $target.closest('.fileUpload-container').find('[type="file"]');
                                    $fileEl.trigger('click');
                                  }
                                  else {
                                    // ----------------------------------------
                                    // Kendo 사용 버전 : 단건 이미지
                                    //   - 초기화            : fnInitKendoUpload
                                    //   - 업로드 및 후처리  : fnUploadImage
                                    // ----------------------------------------
                                    //console.log('# fnBtnImage(kendo) 다건');
                                    $('#' + imageType).trigger('click');
                                  }

                                  // ------------------------------------------
                                  // 이미지구분자 Set
                                  // ------------------------------------------
                                  gbImageType = imageType;
                                  //console.log('# gbImageType :: ', gbImageType);
                                };

}); // document ready - END
