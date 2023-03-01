/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 이벤트참여상세(조회/당첨)
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.01.15    dgyoun        최초생성
 * @
 ******************************************************************************/
'use strict';

var LAST_PAGE = null;   // 최종 페이지 (페이징 기억 관련)
var PAGE_SIZE = 20;

var gbPageParam         = '';     // 넘어온 페이지파라미터
var gbEventTp           = '';     // 이벤트유형
var gbMode              = '';     // 모드(등록/수정/삭제)
var gbEvEventId         = '';     // 이벤트PK
var gbWinnerCnt         = 0;      // 랜덤당첨인원수
var gHandwrittenLotteryTp = '';     // 수기추첨
var gbCheckedWinnerCnt  = 0;      // 체크한당첨자수
var gbEventDrawTp       = '';     // 당첨자설정유형
var gbEventEndYn        = false;  // 이벤트종료여부
var gbLoginId           = '';     // ID조회 검색어

var gbTodayDe           = '';     // 당일
var gbTodayDt           = '';     // 현재일시

var gbDelIdArr;                   // 삭제 Id 리스트
var gbDelGruopId;                 // 삭제상품그룹ID

var gbEvEventJoinId     = '';     // 이벤트참여PK
var gbAdminSecretYn     = '';     // 댓글 차단/차단해제
var fnTemplates         = {};     // 설문리스트 템플릿

var gbFromPage          = '';     // 호출한 페이지 구분

var gbTotalJointCnt     = 0;

var gbGridHeaderViewYn  = false;
var gbTabTp             = 'join';

// ----------------------------------------------------------------------------
// 그리드
// ----------------------------------------------------------------------------
// 참여자목록
var joinGridDs,  joinGridOpt, joinGrid;

// ----------------------------------------------------------------------------
// 당일일자
// ----------------------------------------------------------------------------
var FULL_DATE_FORMAT = 'yyyy-MM-dd';
var FULL_DATE_TIME_FORMAT = 'yyyy-MM-dd HH:mm:ss';
var date = new Date();
//date.setHours(0,0,0,0);
gbTodayDe = date.oFormat(FULL_DATE_FORMAT);
gbTodayDt = date.oFormat(FULL_DATE_TIME_FORMAT);

// ----------------------------------------------------------------------------
// 파일경로
// ----------------------------------------------------------------------------
var publicStorageUrl = fnGetPublicStorageUrl(); // 이미지 업로드되는 public 저장소 url 경로
//console.log('# publicStorageUrl :: ', publicStorageUrl);

$(document).ready(function() {

  // ==========================================================================
  // # Initialize Page Call
  // ==========================================================================
  fnInitialize();

  // ==========================================================================
  // # Initialize PageR
  // ==========================================================================
  function fnInitialize() {

    // ------------------------------------------------------------------------
    // 화면기본설정
    // ------------------------------------------------------------------------
    $scope.$emit('fnIsMenu', { flag : 'true' });

    fnPageInfo({
        PG_ID     : 'eventJoin'
      , callback  : fnUI
    });

    // ------------------------------------------------------------------------
    // 페이지파라미터
    // ------------------------------------------------------------------------
    gbPageParam = fnGetPageParam();

    // ------------------------------------------------------------------------
    // 이벤트유형 Set
    // ------------------------------------------------------------------------
    gbEventTp = gbPageParam.eventTp;
    //$('#eventTp').val(gbEventTp);

    // ------------------------------------------------------------------------
    // 이벤트PK
    // ------------------------------------------------------------------------
    gbEvEventId = gbPageParam.evEventId;

    // ------------------------------------------------------------------------
    // 모드
    // ------------------------------------------------------------------------
    gbMode = gbPageParam.mode;

    // ------------------------------------------------------------------------
    // 호출한 페이지
    // ------------------------------------------------------------------------
    //gbFromPage = gbPageParam.fromPage;    // 사용안함

    // ------------------------------------------------------------------------
    // 상단타이틀
    // ------------------------------------------------------------------------
    if (gbEventTp != null && gbEventTp != 'null') {

      if (gbEventTp == 'EVENT_TP.NORMAL') {
        $('#pageTitleSpan').text('일반이벤트 참여정보 상세');
      }
      else if (gbEventTp == 'EVENT_TP.SURVEY') {
        $('#pageTitleSpan').text('설문이벤트 참여정보 상세');
      }
      else if (gbEventTp == 'EVENT_TP.ATTEND') {
        $('#pageTitleSpan').text('스탬프(출석) 참여정보 상세');
      }
      else if (gbEventTp == 'EVENT_TP.MISSION') {
        $('#pageTitleSpan').text('스탬프(미션) 참여정보 상세');
      }
      else if (gbEventTp == 'EVENT_TP.PURCHASE') {
        $('#pageTitleSpan').text('스탬프(구매) 참여정보 상세');
      }
      else if (gbEventTp == 'EVENT_TP.ROULETTE') {
        $('#pageTitleSpan').text('룰렛이벤트 참여정보 상세');
      }
      else if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
        $('#pageTitleSpan').text('체험단이벤트 참여정보 상세');
      }
    }
  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

    fnInitButton();           // Initialize Button ----------------------------

    fnInitOptionBox();        // Initialize Option Box ------------------------

    fnSetDefaultPre();        // 기본설정 - 조회전 ----------------------------

    fnInitGrid();             // Initialize Grid ------------------------------

    fnInitEvent();            // Initialize Event -----------------------------

    fnSearch();               // 조회 -----------------------------------------

    //fnSetDefaultPost();       // 기본설정 - 조회후 ----------------------------

  }

  // ==========================================================================
  // # 초기화 - 버튼
  // ==========================================================================
  function fnInitButton() {
    //$('#fnBtnSetUnitPrice').kendoButton();
    $('#fnBtnWinnerSelect').kendoButton();

    // 하단 버튼명
    if (gbFromPage == 'DETL') {
      // 상세에서 클릭
      $('#fnOk').text('이벤트 상세');
    }
    else {
      // 리스트에서 클릭
      $('#fnOk').text('이벤트 목록');
    }
  }

  // ==========================================================================
  // # 초기화 - 그리드
  // ==========================================================================
  function fnInitGrid() {

    fnInitJoinGrid('', '');
  }

  // ==========================================================================
  // # 초기화 - 값
  // ==========================================================================
  function fnClear() {

    //$('#inputForm').formClear(true);
    fnSetDefaultPre();
  }

  // ==========================================================================
  // # 초기화 - 랜덤 당첨자선택 혜택
  // ==========================================================================
  function fnRandomBenefit() {
    $('input:radio[name=randomBenefitTp]:input[value="RANDOM_BENEFIT_TP.SINGLE"]').prop('checked', true);

    // 단일 혜택
    $('#benefit-single').show();
    $('#benefit-differential').hide();

  }

  // ==========================================================================
  // # 초기화 - 옵션박스
  // ==========================================================================
  function fnInitOptionBox() {

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 기본정보
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ------------------------------------------------------------------------
    // 팝업
    // ------------------------------------------------------------------------
    $('#kendoPopup').kendoWindow({
        visible: false
      , modal: true
    });


    // ------------------------------------------------------------------------
    // 임직원제외 - 공통코드 아님 (EV_EMPLOYEE_TP 참조해서 조건 걸아야 함)
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id        : 'employeeExceptYn'
      , tagId     : 'employeeExceptYn'
      , data      : [
                      { 'CODE' : 'Y'   , 'NAME' : '임직원 제외'}
                    ]
      , chkVal    : ''
      , style     : {}
    });

    // ------------------------------------------------------------------------
    // 이벤트참여제한고객제외 - 공통코드 아님 (UR_BUYER.EVENT_JOIN_YN)
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id        : 'eventJoinYn'
      , tagId     : 'eventJoinYn'
      , data      : [
                      { 'CODE' : 'Y'   , 'NAME' : '이벤트 참여제한 고객 제외'}
                    ]
      , chkVal    : ''
      , style     : {}
    });

    // ------------------------------------------------------------------------
    // 당첨자선택방식유형 - 공통코드 아님
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'winnerSelectTp'
      , tagId       : 'goodsBuyLimitCnt'
      , data        : [
                        { 'CODE' : 'DIRECT'   , 'NAME' : '당첨자 직접선택'}
                      , { 'CODE' : 'RANDOM'   , 'NAME' : '당첨자 랜덤선정'}
                      ]
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , chkVal      : 'DIRECT'
      , style       : {}
            /*blank : '선택',*/
    });

    // ------------------------------------------------------------------------
    // 랜덤 당첨 혜택 구분
    // ------------------------------------------------------------------------
    fnTagMkRadio({
      id      : 'randomBenefitTp'
      , tagId   : 'randomBenefitTp'
      , url     : '/admin/comn/getCodeList'
      , params  : {'stCommonCodeMasterCode' : 'RANDOM_BENEFIT_TP', 'useYn' : 'Y'}
      , async   : true
      , isDupUrl: 'Y'
      , chkVal  : 'RANDOM_BENEFIT_TP.SINGLE'
      , style   : {}
    });

  } // End of fnInitOptionBox

  // ==========================================================================
  // # 기본 설정 - 조회전
  // ==========================================================================
  function fnSetDefaultPre() {

    // ------------------------------------------------------------------------
    // 설문 영역 노출/숨김
    // ------------------------------------------------------------------------
    if (gbEventTp == 'EVENT_TP.SURVEY') {
      $('#surveyDiv').show();
    }
    else {
      $('#surveyDiv').hide();
    }

    // 임직원제외 기본값 체크
    $('INPUT[name=employeeExceptYn]').prop('checked', true);
    // 이벤트참여제한고객제외 기본값 체크
    $('INPUT[name=eventJoinYn]').prop('checked', true);
  };

  // ==========================================================================
  // # 초기화 - 설문 리스트 템플릿
  // ==========================================================================
  function fnInitTemplates() {
    fnTemplates.survey =  createTemplateItem({
        el      : '#surveyList'
      , template: '#surveyListTpl'
    })
  }

  // ==========================================================================
  // # 초기화 - 설문참여 리스트 템플릿
  // ==========================================================================
  function fnInitTemplatesSurveyJoin() {
    fnTemplates.surveyJoin =  createTemplateItem({
      el      : '#surveyJoinList'
    , template: '#surveyJoinListTpl'
    })
  }

  // ==========================================================================
  // # 초기화 - 이벤트
  // ==========================================================================
  function fnInitEvent() {

    //$('#giftTargetBrandTp').hide();

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 기본정보
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ------------------------------------------------------------------------
    // 체크박스변경(전체선택관련)
    // ------------------------------------------------------------------------
    fbCheckboxChange();

    // ------------------------------------------------------------------------
    // 검색기간 초기화 버튼 클릭
    // ------------------------------------------------------------------------
    //$('[data-id="fnDateBtnC"]').on('click', function() {
    //    $('[data-id="fnDateBtn3"]').mousedown();
    //});

    //$('input[name="selectedDiv"]').on('change', e => {
    //  const $tabs = $('.tabs');
    //  const tabId = $(e.target).data('tab');
    //  $tabs.removeClass('show');
    //  $('#' + tabId).addClass('show');
    //});

    // ------------------------------------------------------------------------
    // 탭 변경 이벤트
    // ------------------------------------------------------------------------
    $('.labelTabs input').on('change', function() {
     //console.log('# 탭 Change');
     const value = $(this).val();
     //console.log(value);
     //console.log('# tab value :: ', value);

     // 그리드 재조회
     fnSearchReload(value);

     // 탭 변경 시 ID조회 검색어 초기화
     gbLoginId = '';

    });

    // ------------------------------------------------------------------------
    // 랜덤 당첨 선택 레이어팝업 혜택 구분 변경 시
    // ------------------------------------------------------------------------
    $('#randomBenefitTp').on('change', function(e) {
      var randomBenefitTp  = $('input[name=randomBenefitTp]:checked').val();

      if(randomBenefitTp == 'RANDOM_BENEFIT_TP.SINGLE') {
        // 단일 혜택
        $('#benefit-single').show();
        $('#benefit-differential').hide();
        $('#winnerCnt').val('');
      } else {
        // 차등 혜택
        $('#benefit-differential').show();
        $('#benefit-single').hide();

        $('#benefit-differential').empty();

        var addRow = "";
        addRow += "<div class=\"complex-condition\">";
        addRow += " <input type=\"text\" id=\"benefitName1\" name=\"benefitName\" class=\"comm-input lengthCheck\" maxlength=\"50\" style=\"width:50%;\" value=\"\" placeholder=\"당첨혜택명\" required>";
        addRow += " <input type=\"text\" id=\"winnerCnt1\" name=\"winnerCnt\" class=\"comm-input lengthCheck\" maxlength=\"3\" style=\"width:25%;margin-right:5px;margin-left:5px;\" value=\"\" placeholder=\"숫자만 입력\" required>";
        addRow += " <button type='button' class='btn-point btn-m' style=\"min-width:47px;\" onclick=\"$scope.fnBenefitController('add',this);\">+</button>";
        addRow += "	</td>";
        addRow += "</div>";
        $("#benefit-differential").append(addRow);
      }

    });

    // ------------------------------------------------------------------------
    // 임직원 제외 체크박스 클릭 이벤트
    // ------------------------------------------------------------------------
    $('#employeeExceptYn').on('click',function(index) {

      fnEventCheck();
    });

    // ------------------------------------------------------------------------
    // 이벤트 참여제한 고객 제외 체크박스 클릭 이벤트
    // ------------------------------------------------------------------------
    $('#eventJoinYn').on('click',function(index) {

      fnEventCheck();
    });

    // ------------------------------------------------------------------------
    // 숫자만입력
    // ------------------------------------------------------------------------
    // * 랜덤선정 당첨인원수
    fnInputValidationForNumber('winnerCnt');

    // ------------------------------------------------------------------------
    // 참여자목록/당첨자목록 탭(라디오) 선택
    // ------------------------------------------------------------------------
    //$('#tab').on('change',function(index) {
    $('input:radio[name=tab]').on('click',function(index) {
      //console.log('# 탭 선택 :: ', $('input[name=tab]:checked').val());
    });


  } // End of fnInitEvent()

  // --------------------------------------------------------------------------
  // # 랜덤 당첨자 선택 차등 혜택 정보 행 추가 삭
  // --------------------------------------------------------------------------
  function fnBenefitController(action, object) {
    if (action == "add") {
      var idSeq = $("input[name=benefitName]").length+1;

      var addRow = "";
      addRow += "<div class=\"complex-condition\">";
      addRow += " <input type=\"text\" id=\"benefitName" + idSeq + "\" name=\"benefitName\" class=\"comm-input lengthCheck\" maxlength=\"50\" style=\"width:50%;\" value=\"\" placeholder=\"당첨혜택명\" required>";
      addRow += " <input type=\"text\" id=\"winnerCnt" + idSeq + "\" name=\"winnerCnt\" class=\"comm-input lengthCheck\" maxlength=\"3\" style=\"width:25%;margin-right:5px;margin-left:5px;\" value=\"\" placeholder=\"숫자만 입력\" required>";
      addRow += " <button type='button' class='btn-gray btn-m' style=\"min-width:50px;\" onclick=\"$scope.fnBenefitController('del',this);\">-</button>";
      addRow += "	</td>";
      addRow += "</div>";
      $("#benefit-differential").append(addRow);
    } else if (action == "del") {
      $(object).parent().remove();
    }
  }

  // --------------------------------------------------------------------------
  // # 임직원제외/이벤트참여제한고객제외 이벤트 처리
  // --------------------------------------------------------------------------
  function fnEventCheck() {

    // 임직원제외 체크여부
    var checkEmployeeYn = '';
    // 이벤트참여제한고객제외 체크여부
    var checkEventJoinYn = '';
    // 임직원제외 체크 여부
    if($('input:checkbox[name=employeeExceptYn]').is(':checked') == true) {
      checkEmployeeYn = 'Y';
    }
    else {
      checkEmployeeYn = 'N';
    }
    // 이벤트참여제한고객제외 체크여부
    if($('input:checkbox[name=eventJoinYn]').is(':checked') == true) {
      checkEventJoinYn = 'Y';
    }
    else {
      checkEventJoinYn = 'N';
    }
    //console.log('# checkEmployeeYn  :: ', checkEmployeeYn);
    //console.log('# checkEventJoinYn :: ', checkEventJoinYn);

    // 그리드 Array
    var joinGridArr  = $('#joinGrid').data('kendoGrid').dataSource.data();

    if (joinGridArr != undefined && joinGridArr != null && joinGridArr.length > 0) {

      var eachWinnerYn      = '';   // 그리드.아이템.당첨여부
      var eachWinnerExistYn = '';   // 그리드.아이템.당첨존재여부
      var eachEmployeeYn    = '';   // 그리드.아이템.임직원여부
      var eachEventJoinYn   = '';   // 그리드.아이템.이벤트참여제한여부

      for (var i = 0; i < joinGridArr.length; i++) {
        //console.log('# joinGridArr[', i, '] :: ', JSON.stringify(joinGridArr[i]));
        eachWinnerYn      = joinGridArr[i].winnerYn;        // 그리드.아이템.당첨여부
        eachWinnerExistYn = joinGridArr[i].winnerExistYn;   // 그리드.아이템.당첨존재여부
        eachEmployeeYn    = joinGridArr[i].employeeYn;      // 그리드.아이템.임직원제외여부
        eachEventJoinYn   = joinGridArr[i].eventJoinYn;     // 그리드.아이템.이벤트참여제한여부
        //console.log('# [', i, '] eachEmployeeYn  :: ', eachEmployeeYn);
        //console.log('# [', i, '] eachEventJoinYn :: ', eachEventJoinYn);
        //console.log('# --------------------------------------------------');
        //console.log('# [', i, '] eachWinnerYn       :: ', eachWinnerYn);
        //console.log('# [', i, '] eachWinnerExistYn  :: ', eachWinnerExistYn);
        //console.log('# [', i, '] eachEmployeeYn     :: ', eachEmployeeYn);
        //console.log('# [', i, '] eachEventJoinYn    :: ', eachEventJoinYn);
        //if (eachWinnerYn == 'Y' || eachWinnerExistYn == 'Y') {
        if (eachWinnerYn == 'Y') {
          // 당첨고객이면 (당첨 또는 당첨존재) -> 그대로 유지, 그리드 조회 시 체크 && 비활성 처리됨
        }
        else {
          // 당첨고객이 아니면
          let $tr = $('#joinGrid tbody').find('tr').eq(i);
          //console.log($tr);
          let $checkbox = $tr.find('input[name="joinCheck"]');
          let isDisabledEmployeeYn;   // 비활성여부-임직원
          let isDisabledEventJoinYn;  // 비활성여부-이벤트제한

          // ------------------------------------------------------------------
          // 임직원제외 체크여부 처리
          // ------------------------------------------------------------------
          if (checkEmployeeYn == 'Y') {
            // ----------------------------------------------------------------
            // 임직원제외 체크이면
            // ----------------------------------------------------------------
            if (eachEmployeeYn == 'Y') {
              // 그리드.아이템.임직원제외여부 : Y
              // 체크박스 해제(체크 했을수도 있으므로)
              $checkbox.prop('checked', false);
              // 체크박스 비활성
              //$checkbox.attr('disabled', true);
              // 비활성여부 : 비활성
              isDisabledEmployeeYn = true;
            }
            else {
              // 그리드.아이템.임직원제외여부 : N
              // 체크박스 활성
              //$checkbox.attr('disabled', false);
              // 비활성여부 : 활성
              isDisabledEmployeeYn = false;
            }
          }
          else {
            // ----------------------------------------------------------------
            // 임직원제외 체크가 아니면
            // ----------------------------------------------------------------
            // 체크박스 활성
            //$checkbox.attr('disabled', false);
            // 비활성여부 : 활성
            isDisabledEmployeeYn = false;
          }
          // ------------------------------------------------------------------
          // 이벤트참여제한 체크여부 처리
          // ------------------------------------------------------------------
          if (checkEventJoinYn == 'Y') {
            // ----------------------------------------------------------------
            // 이벤트참여제한 체크이면
            // ----------------------------------------------------------------
            if (eachEventJoinYn == 'Y') {
              // 그리드.아이템.이벤트참여제한 : Y
              // 체크박스 해제 (체크 했을수도 있으므로)
              $checkbox.prop('checked', false);
              // 체크박스 비활성
              //$checkbox.attr('disabled', true);
              // 비활성여부 : 비활성
              isDisabledEventJoinYn = true;
            }
            else {
              // 그리드.아이템.임직원제외여부 : N
              // 체크박스 활성
              //$checkbox.attr('disabled', false);
              // 비활성여부 : 활성
              isDisabledEventJoinYn = false;
            }
          }
          else {
            // ----------------------------------------------------------------
            // 임직원제외 체크가 아니면
            // ----------------------------------------------------------------
            // 체크박스 활성
            //$checkbox.attr('disabled', false);
            // 비활성여부 : 활성
            isDisabledEventJoinYn = false;
          }
          //console.log('# [', i, '][', checkEmployeeYn, '][', eachEmployeeYn, ']/[', checkEventJoinYn, '][', eachEventJoinYn, '] :: ', isDisabled);
          // 체크박스 활성/비활성
          if (isDisabledEmployeeYn == true || isDisabledEventJoinYn == true) {
            // 임직원제외 또는 이벤트참여제한 체크인 경우
            // 체크박스 비활성
            $checkbox.attr('disabled', true);
          }
          else {
            // 체크박스 활성
            $checkbox.attr('disabled', false);
          }

          //if (checkEmployeeYn == 'Y' || checkEventJoinYn == 'Y') {
          //  // ----------------------------------------------------------------
          //  // 임직원제외 or 이벤트참여제한여부 체크이면
          //  // ----------------------------------------------------------------
          //  // 체크박스 해제
          //  $checkbox.prop('checked', false);
          //  // 체크박스 비활성
          //  $checkbox.attr('disabled', true);
          //}
          //else {
          //  // ----------------------------------------------------------------
          //  // 제외조건이 없으면
          //  // ----------------------------------------------------------------
          //  // 체크박스 활성
          //  $checkbox.attr('disabled', false);
          //  //// 모든 체크박스 활성
          //  //$('INPUT[name=joinCheck]').each(function() {
          //  //  const $this = $(this);
          //  //  //if(!$this.attr('disabled')) {
          //  //    //
          //  //    $this.prop("disabled", false);
          //  //  //}
          //  //});
          //}

        } // End of if (eachWinnerYn == 'Y') else

      } // End of for (var i = 0; i < joinGridArr.length; i++)

    } // End of if (joinGridArr != undefined && joinGridArr != null && joinGridArr.length > 0)
  }


  // ##########################################################################
  // # 조회 Start
  // ##########################################################################
  // ==========================================================================
  // # 조회
  // ==========================================================================
  function fnSearch() {
    //console.log('# fnSearch :: [', gbEvEventId, '][', gbMode, ']');
    if (gbEvEventId != null && gbEvEventId != '' && gbEvEventId != 0 && gbEvEventId != '0') {

      if (gbMode == 'select') {
        // --------------------------------------------------------------------
        // 이벤트상세조회-기본정보 + 참여자/당첨자 리스트
        // --------------------------------------------------------------------
        var url = '/admin/pm/event/selectEventInfo?evEventId=' + gbEvEventId;
      }
      else if (gbMode == 'select.survey') {
        // --------------------------------------------------------------------
        // 설문리스트조회
        // --------------------------------------------------------------------
        var url = '/admin/pm/event/selectEventJoinSurveyList?evEventId=' + gbEvEventId;
      }
      // Ajax 호출
      fnSearchAjax(url, null);
    }
    else {
      fnMessage('', '이벤트ID정보를 확인해주세요.', '');
      return false;
    }
  }

  // ==========================================================================
  // # 조회-설문통계조회
  // ==========================================================================
  function fnSearchSurvey() {
    //console.log('# fnSearch :: [', gbEvEventId, '][', gbMode, ']');
    if (gbEvEventId != null && gbEvEventId != '' && gbEvEventId != 0 && gbEvEventId != '0') {

      // ----------------------------------------------------------------------
      // 설문리스트조회
      // ----------------------------------------------------------------------
      gbMode = 'select.survey';
      var url = '/admin/pm/event/selectEventJoinSurveyList?evEventId=' + gbEvEventId;
      // Ajax 호출
      fnSearchAjax(url, null);
    }
    else {
      fnMessage('', '이벤트ID정보를 확인해주세요.', '');
      return false;
    }

  }

  // ==========================================================================
  // # 재조회
  // ==========================================================================
  function fnSearchReload(tabTp) {

    gbTabTp = tabTp;

    var data = new Object();
    data.evEventId = gbEvEventId;

    if (tabTp == 'join') {
      // ---------------------------------------------------------------------
      // 참여자 목록
      // ---------------------------------------------------------------------
      // 체크, 버튼영역 노출
      if (gbGridHeaderViewYn == true) {
        $('#gridHeader').show();
      }
      else {
        $('#gridHeader').hide();
      }
      // 참여자 목록 조회
      $('#joinGrid').data('kendoGrid').destroy();
      $('#joinGrid').empty();
      fnInitJoinGrid('', '');
      fnSearchJoinList(data);
    }
    else {
      // ---------------------------------------------------------------------
      // 당첨자 목록
      // ---------------------------------------------------------------------
      // 체크, 버튼영역 숨김
      $('#gridHeader').hide();
      // 당첨자 목록 조회
      $('#joinGrid').data('kendoGrid').destroy();
      $('#joinGrid').empty();
      fnInitJoinGrid('Y', '');
      fnSearchJoinList(data);
    }
  }

  // ==========================================================================
  // # 조회 - 그리드
  // ==========================================================================
  function fnSearchJoinList(data) {
    //console.log('# fnSearchJoinList Start');

    // 이벤트참여자 목록조회 : form에서 사용하는 항목 없을 경우 주석
    //$('#inputForm').formClear(false);
    //var data = $('#inputForm').formSerialize(true);
    //console.log('# LAST_PAGE :: ', LAST_PAGE);
    var curPage = LAST_PAGE ? LAST_PAGE : 1;

    var query = {
        page          : curPage
      , pageSize      : PAGE_SIZE
      // param은 안되나?      , param         : { evEventId : gbEvEventId,  winnerYn : winnerYn, $('#loginId').val()}
      //, filterLength  : fnSearchData(data).length
      //, filter        : {
      //                    filters : fnSearchData(data)
      //                  }
    };
    joinGridDs.query(query);

  }

  // ==========================================================================
  // # 조회 Ajax
  // ==========================================================================
  function fnSearchAjax(url, params) {

    // ----------------------------------------------------------------------
    // 이벤트상세조회-기본정보(일반/골라담기/증정행사 공통)
    // ----------------------------------------------------------------------
    fnAjax({
        //url       : '/admin/pm/event/selectEventInfo?evEventId=' + gbEvEventId            // 주소줄에서 ID 보기위해 params 사용안함
        url       : url            // 주소줄에서 ID 보기위해 params 사용안함
      , method    : 'POST'
      , params    : params
      //, params    : {
      //                'evEventId' : gbEvEventId
      //              }
      , isAction  : 'select'
      //, async     : false
      , success   : function(data, status, xhr) {
                      // --------------------------------------------------
                      // 성공 Callback 처리
                      // --------------------------------------------------
                      //console.log('# [', gbMode, '] :: ', JSON.stringify(data));
                      fnBizCallback(gbMode, data, null);

                    }
      , error     : function(xhr, status, strError) {
                      //fnKendoMessage({
                      //  message : xhr.responseText
                      //});
                    }
    });

  }



  // ==========================================================================
  // # 저장
  // ==========================================================================
  function fnSave(checkWinnerArr) {
    //console.log('# fnSave Start [', gbMode, ']');

    var url         = '';
    var cbId        = gbMode;
    var isAction    = '';
    var param;
    var data;
    var confirmMsg  = '';

    if (gbMode == 'winner.direct') {
      // ----------------------------------------------------------------------
      // 당첨자.직접선택
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // Validation
      // ----------------------------------------------------------------------
      if (checkWinnerArr == undefined || checkWinnerArr == null || checkWinnerArr == '' || checkWinnerArr.length <= 0) {

        fnMessage('', '참여자를 선택해주세요.', '');
        return false;
      }

      let employeeExceptYn  = 'N';
      let eventJoinYn       = 'N';

      if ($('input:checkbox[name=employeeExceptYn]').is(':checked') == true) {
        employeeExceptYn  = 'Y';
      }
      if ($('input:checkbox[name=eventJoinYn]').is(':checked') == true) {
        eventJoinYn  = 'Y';
      }

      // 선택수
      gbCheckedWinnerCnt = checkWinnerArr.length;

      url         = '/admin/pm/event/putWinnerLottery';
      param       = {
                      'evEventId'       : gbEvEventId
                    , 'winnerSelectTp'  : $('#winnerSelectTp').val()
                    , 'employeeExceptYn': employeeExceptYn
                    , 'handwrittenLotteryTp' : 'HANDWRITTEN_LOTTERY_TP.DIRECT_LOTTERY'
                    , 'eventJoinYn'     : eventJoinYn
                    , 'winnerEventJoinListJsonString' : JSON.stringify(checkWinnerArr)
                    };
      isAction    = 'update';
      confirmMsg  = '선택하신 ' + gbCheckedWinnerCnt + '건을 당첨자로 선정하시겠습니까?';

    }
    else if (gbMode == 'winner.random') {
      // ----------------------------------------------------------------------
      // 당첨자.랜덤
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // Validation 단일 혜택
      // ----------------------------------------------------------------------
      if ((gHandwrittenLotteryTp == 'HANDWRITTEN_LOTTERY_TP.RANDOM_SINGLE') && (gbWinnerCnt == undefined || gbWinnerCnt == null || gbWinnerCnt == '' || gbWinnerCnt <= 0)) {

        fnMessage('', '당첨인원을 입력해주세요.', '');
        return false;
      }
      ;
      // 당첨처리
      url         = '/admin/pm/event/putWinnerLottery';
      param       = {
                      'evEventId'       : gbEvEventId
                    , 'winnerCnt'       : gbWinnerCnt
                    , 'winnerSelectTp'  : $('#winnerSelectTp').val()
                    , 'handwrittenLotteryTp' : gHandwrittenLotteryTp
                    , "randomBenefitType" : $('input[name=randomBenefitTp]:checked').val()
                    , 'winnerEventJoinListJsonString' : JSON.stringify(checkWinnerArr)
                    };
      isAction    = 'update';

      if(gHandwrittenLotteryTp == 'HANDWRITTEN_LOTTERY_TP.RANDOM_SINGLE') {
        confirmMsg  = '단일 혜택으로 총 ' + gbWinnerCnt + '건을 랜덤추첨 하시겠습니까?';
      } else {
        confirmMsg  = '차등 혜택으로 랜덤추첨 하시겠습니까?';
      }
    }
    else if (gbMode  == 'update.feedback') {
      // ----------------------------------------------------------------------
      // 댓글 차단/차단해제
      // ----------------------------------------------------------------------
      // ----------------------------------------------------------------------
      // Validation
      // ----------------------------------------------------------------------
      if (gbEvEventJoinId == undefined || gbEvEventJoinId == null || gbEvEventJoinId == '') {
        fnMessage('', '이벤트참여 정보를 확인하세요..', '');
        return false;
      }
      if (gbAdminSecretYn == undefined || gbAdminSecretYn == null || gbAdminSecretYn == '') {
        fnMessage('', '댓글 차단 정보를 확인하세요..', '');
        return false;
      }

      var chngeAdminSecretYn = '';    // 변경하려는 관리자비공개여부 값

      if (gbAdminSecretYn == 'Y') {
        // 현재 관리자비공개여부가 Y 이면
        chngeAdminSecretYn = 'N';
        confirmMsg = '댓글을 차단해제 하시겠습니까?';
      }
      else {
        chngeAdminSecretYn = 'Y';
        confirmMsg = '댓글을 차단 하시겠습니까?';
      }

      // 댓글 차단/차단해제
      url         = '/admin/pm/event/putAdminSecretYn';
      param       = {
                        'evEventId'       : gbEvEventId
                      , 'evEventJoinId'   : gbEvEventJoinId
                      , 'adminSecretYn'   : chngeAdminSecretYn
                      };
      isAction    = 'update';

    }
    else {
      fnMessage('', '실행 오류입니다. 다시 시도해 주십시오.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // 1. form Set - 기본정보
    // ------------------------------------------------------------------------
    data = $('#inputForm').formSerialize(true);

    //console.log('# data.rtnValid :: ', data.rtnValid);
    // ------------------------------------------------------------------------
    // Validation & Ajax Call
    // ------------------------------------------------------------------------
    if(data.rtnValid) {

      // ----------------------------------------------------------------------
      // 2. Ajax Call
      // ----------------------------------------------------------------------
      fnKendoMessage({
          message : fnGetLangData({key :'', nullMsg : confirmMsg })
        , type    : 'confirm'
        , ok      : function() {
                      // 저장 처리 후 선택 콘텐츠레벨 초기화
                      //selectedContsLevel = null;
                      //console.log('# Ajax 1111');
                      fnAjax({
                          url     : url
                        , params  : param
                        //, params  : pageData
                        , success : function(result) {
                                      // --------------------------------------------------
                                      // 성공 Callback 처리
                                      // --------------------------------------------------
                                      //console.log('# Ajax success');
                                      fnBizCallback(cbId, result, null);
                                    }
                        , fail    : function(data, resultcode) {
                                      // --------------------------------------------------
                                      // 실패 메시지 처리
                                      // --------------------------------------------------
                                      //console.log('# Ajax fail');
                                      resultcode.code;
                                      resultcode.message;
                                      resultcode.messageEnum;

                                      fnKendoMessage({
                                          message : resultcode.message
                                        , ok      : function(e) {
                                                      //fnBizCallback('fail', data, null);
                                                    }
                                      });
                                    }
                        , error   : function(xhr, status, strError) {
                                      //console.log('# Ajax error');
                                      fnKendoMessage({
                                        message : xhr.responseText
                                      });
                                    }
                        , isAction : isAction
                      });
                    }
      });

    }

  }

  // ==========================================================================
  // # 댓글 차단/차단해제 버튼
  // ==========================================================================
  function fnBtnFeedbackBlockEdit(dataItem) {

    // 모드 : 댓글 차단/차단해제
    gbMode = 'update.feedback';

    gbEvEventJoinId = dataItem.evEventJoinId; // 클릭한 이벤트참여PK
    gbAdminSecretYn = dataItem.adminSecretYn; // 클릭한 현재의 관리자비공개여부

    // 저장 실행
    fnSave('');
  }


  // ==========================================================================
  // # 당첨자선택버튼
  // ==========================================================================
  function fnBtnWinnerSelect() {
    //console.log('# fnBtnWinnerSelect Start');
    //console.log('# winnerSelectTp :: ', $('#winnerSelectTp').val());
    //console.log('# joinGridDs._page :: ', joinGridDs._page);

    // 당첨 처리 후 기존 페이지 조회
    if ($('#winnerSelectTp').val() == 'DIRECT') {
      // 직접선택이면
      LAST_PAGE = joinGridDs._page;
    }

    var checkWinnerArr = new Array();   // int Array
    var checkWinnerObj = null;          // EventJoinVo 대응

    if ($('#winnerSelectTp').val() == 'DIRECT') {
      // ----------------------------------------------------------------------
      // 직접선택
      // ----------------------------------------------------------------------
      gbMode = 'winner.direct';

      // 체크수 체크 (기 당첨건은 제외)
      var checkCnt = 0;

      var joinGridArr  = $('#joinGrid').data('kendoGrid').dataSource.data();

      if (joinGridArr != undefined && joinGridArr != null && joinGridArr.length > 0) {

        var eachWinnerYn    = '';   // 그리드.아이템.당첨여부
        var eachEmployeeYn  = '';   // 그리드.아이템.임직원여부
        var eachEventJoinYn = '';   // 그리드.아이템.이벤트참여제한여부

        for (var i = 0; i < joinGridArr.length; i++) {

          const $tr = $('#joinGrid tbody').find('tr').eq(i);
          //console.log($tr);
          const $checkbox = $tr.find('input[name="joinCheck"]');
          //console.log('# joinGridArr[i] :: ', JSON.stringify(joinGridArr[i]));

          if (joinGridArr[i].winnerYn != 'Y' && joinGridArr[i].winnerExistYn != 'Y') {
            // 당첨이 아닌 경우
            if($checkbox.is(':checked') == true) {
              // 체크상태인 경우
              checkCnt++;
              checkWinnerObj = new Object();
              checkWinnerObj.evEventJoinId  = joinGridArr[i].evEventJoinId;     // 이벤트참여PK
              checkWinnerObj.evEventId      = joinGridArr[i].evEventId;         // 이벤트PK
              checkWinnerObj.loginId        = joinGridArr[i].loginId;           // 로그인ID
              checkWinnerObj.urUserId       = joinGridArr[i].urUserId;          // 사용자PK
              checkWinnerObj.eventBenefitTp = joinGridArr[i].eventBenefitTp;    // 이벤트혜택유형(쿠폰/적립금/경품/응모/혜택없음)
              checkWinnerObj.eventTp        = joinGridArr[i].eventTp;           // 이벤트유형(일반/설문/스탬프...)
              checkWinnerObj.reqConts       = joinGridArr[i].reqConts;          // 신청내용
              checkWinnerObj.stampCnt       = joinGridArr[i].stampCnt;          // 스탬프개수(스탬프번호)
              checkWinnerObj.pmPointId      = joinGridArr[i].pmPointId;         // 포인트ID
              //checkWinnerObj.pintNm         = joinGridArr[i].pointNm;           // 포인트명
              //checkWinnerObj.pintTp         = joinGridArr[i].pointTp;           // 포인트유형

              checkWinnerArr.push(checkWinnerObj);

            } // End of if($checkbox.is(':checked') == true)

          } // End of if (joinGridArr[i].winnerYn != 'Y')

        } // End of for (var i = 0; i < joinGridArr.length; i++)

      } // End of if (joinGridArr != undefined && joinGridArr != null && joinGridArr.length > 0)

      //console.log('# checkCnt :: ', checkCnt);
      if (checkCnt <= 0) {
        fnMessage('', '참여자를 선택해주세요.', '');
        return false;
      }
      //console.log('# checkWinnerArr.Json :: ', JSON.stringify(checkWinnerArr));

      gHandwrittenLotteryTp = 'HANDWRITTEN_LOTTERY_TP.DIRECT_LOTTERY';

      // ----------------------------------------------------------------------
      // 당첨처리 호출
      // ----------------------------------------------------------------------
      fnSave(checkWinnerArr);
    }
    else {
      // ----------------------------------------------------------------------
      // 랜덤선택
      // ----------------------------------------------------------------------
      gbMode = 'winner.random';

      // 당첨자수 입력 레이어 팝업 오픈
      document.documentElement.scrollTop = 0;
      document.documentElement.scrollTop = 0;
      // 당첨인원 초기화
      $('#winnerCnt').val('');
      // 팝업노출
      $('#winnerPopup').show();
      //$('#inputForm').formClear(true);
      fnKendoInputPoup({height:"auto" ,width:"500px", title:{ nullMsg :'당첨자 랜덤 선택' } } );

      fnRandomBenefit();
    }

  }

  // ==========================================================================
  // # 당첨자등록(Popup)버튼
  // ==========================================================================
  function fnBtnWinnerSave() {
    var randomBenefitTp  = $('input[name=randomBenefitTp]:checked').val();

    if(randomBenefitTp == 'RANDOM_BENEFIT_TP.SINGLE') {
      // 랜덤 당첨 단일 혜택 처리
      fnRandomBenefitSingleSave();
    } else {
      // 랜덤 당첨 차등 혜택 처리
      fnRandomBenefitDefferentialSave();
    }
  }

  // ==========================================================================
  // # 랜덤 당첨 단일 혜택 처리
  // ==========================================================================
  function fnRandomBenefitSingleSave() {
    //console.log('# fnBtnWinnerSave Start [', $('#winnerCnt').val(), ']');

    let winnerCnt = $('#winnerCnt').val();
    let iWinnerCnt = 0;

    if (winnerCnt == undefined || winnerCnt == null || winnerCnt == '') {
      fnMessage('', '당첨인원을 입력해주세요.', '');
      return false;
    }
    else {
      iWinnerCnt = Number(winnerCnt);
      if (iWinnerCnt > gbTotalJointCnt) {
        fnMessage('', '당첨자는 참여자를 초과할 수 없습니다.', '');
        return false;
      }

      if (iWinnerCnt <= 0) {
        fnMessage('', '당첨인원을 확인해주세요.', '');
        return false;
      }
    }

    gbWinnerCnt = iWinnerCnt;

    gHandwrittenLotteryTp = 'HANDWRITTEN_LOTTERY_TP.RANDOM_SINGLE';

    // 팝업 닫기
    fnPopupClose();

    // ------------------------------------------------------------------------
    // 당첨처리 호출
    // ------------------------------------------------------------------------
    fnSave(null);
  }

  // ==========================================================================
  // # 랜덤 당첨 차등 혜택 처리
  // ==========================================================================
  function fnRandomBenefitDefferentialSave() {

    var checkWinnerArr = new Array();   // int Array
    var checkWinnerObj = null;          // EventJoinVo 대응

    var loopCnt = $("input[name=benefitName]").length;

    if(loopCnt==0) {
      fnMessage('', '차등 혜택 정보가 존재하지 않습니다.', '');
      return false;
    }

    for (var i=1; i<=loopCnt; i++) {
      var benefitName = $.trim($("#benefitName"+i).val());
      var winnerCnt = $.trim($("#winnerCnt"+i).val());

      if(benefitName == '') {
        fnMessage('', '당첨 혜택명을 입력해주세요.', '');
        return false;
      }

      if(winnerCnt == '') {
        fnMessage('', '당첨인원을 입력해주세요.', '');
        return false;
      }

      if(winnerCnt <= 0) {
        fnMessage('', '당첨인원을 확인해주세요.', '');
        return false;
      }

      if(!checkNum($("#winnerCnt"+i))) {
        fnMessage('', '당첨인원은 숫자만 입력해주세요.', '');
        return false;
      }

      checkWinnerObj = new Object();
      checkWinnerObj.randomBenefitName = benefitName;
      checkWinnerObj.winnerCnt = winnerCnt;

      checkWinnerArr.push(checkWinnerObj);
    }

    gHandwrittenLotteryTp = 'HANDWRITTEN_LOTTERY_TP.RANDOM_DIFFERENTIAL';

    // 팝업 닫기
    fnPopupClose();

    // ------------------------------------------------------------------------
    // 당첨처리 호출
    // ------------------------------------------------------------------------
    fnSave(checkWinnerArr);
  }

  // ==========================================================================
  // # 설문참여내용 Popup Open
  // ==========================================================================
  function fnSearchSurveyJoin(dataItem) {

    // 설문참여 Popup 오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    // 당첨인원 초기화
    //$('#winnerCnt').val('');

    // ------------------------------------------------------------------------
    // 설문참여리스트조회
    // ------------------------------------------------------------------------
    gbMode = 'select.survey.join';
    var url = '/admin/pm/event/selectEventJoinSurveyItemJoinList?evEventId=' + gbEvEventId;
    var params  = {'evEventJoinId' : dataItem.evEventJoinId, 'evEventSurveyQuestionId' : dataItem.evEventSurveyQuestionId };
    // Ajax 호출
    fnSearchAjax(url, params);
  }

  // ==========================================================================
  // # 콜백함수
  // ==========================================================================
  function fnBizCallback(id, data, removeObj) {
    //console.log('# fnBizCallback Start [', id, ']');
    //console.log('# data.json :: ', JSON.stringify(data));
    var gridId;

    switch(id) {
      case 'select':
        // --------------------------------------------------------------------
        // 조회
        // --------------------------------------------------------------------
        // console.log('# data :: ', JSON.stringify(data));
        if (data == null || data == 'null' || data == '' ||
            data.eventInfo == null || data.eventInfo == 'null' || data.eventInfo == '' ||
            data.eventInfo.evEventId == null || data.eventInfo.evEventId == 'null' || data.eventInfo.evEventId == '' || data.eventInfo.evEventId == 0) {

          fnMessage('', '이벤트 기본정보가 존재하지 않습니다.', '');
          return false;
        }
        // console.log('# eventInfo :: ', JSON.stringify(data.eventInfo));

        var eventInfo = data.eventInfo;

        // --------------------------------------------------------------------
        // 이벤트 기본정보 Set
        // --------------------------------------------------------------------
        // 이벤트유형명
        $('#eventTpNmSpan').text(eventInfo.eventTpNm);
        // 이벤트명
        $('#eventNmSpan').text(eventInfo.title);
        // 당첨자설정명
        $('#eventDrawNmSpan').text(eventInfo.eventDrawTpNm);
        // 진행기간-시작일자
        $('#startDtSpan').text(eventInfo.startDt);
        // 진행기간-종료일자
        $('#endDtSpan').text(eventInfo.endDt);

        // --------------------------------------------------------------------
        // 당첨자설정유형 Set
        // --------------------------------------------------------------------
        gbEventDrawTp = eventInfo.eventDrawTp;

        // --------------------------------------------------------------------
        // 이벤트종료여부
        // --------------------------------------------------------------------
        //console.log('# gbTodayDt       :: ', gbTodayDt);
        //console.log('# eventInfo.endDt :: ', eventInfo.endDt);

        if (eventInfo.endDt == undefined || eventInfo.endDt == null || eventInfo.endDt == '') {
          // 이벤트 종료일자 없는 경우 -> 진행중
          gbEventEndYn = false; // 이벤트진행중
        }
        else {
          // 이벤트 종료일자 있는 경우 -> 현재시각과 비교
          let iGbTodayDt  = Number(gbTodayDt.replace(/[^0-9]/g, ''));
          let iEndDt      = Number((eventInfo.endDt).replace(/[^0-9]/g, ''));
          //console.log('# iGbTodayDt :: ', iGbTodayDt);
          //console.log('# iEndDt     :: ', iEndDt);
          if (iGbTodayDt > iEndDt) {
            //console.log('# 종료');
            gbEventEndYn = true; // 이벤트종료
          }
          else {
            //console.log('# 종료아님');
            gbEventEndYn = false; // 이벤트진행중
          }
        }
        //console.log('# >>>>> gbEventEndYn :: ', gbEventEndYn);

        // --------------------------------------------------------------------
        // 참여자목록 조회
        // --------------------------------------------------------------------
        fnSearchJoinList(data);

        // --------------------------------------------------------------------
        // 설문 템플릿 초기화
        // --------------------------------------------------------------------
        fnInitTemplates();

        // --------------------------------------------------------------------
        // 설문통계조회
        // --------------------------------------------------------------------
        fnSearchSurvey();

        break;

      case 'select.survey':
        // --------------------------------------------------------------------
        // 조회-설문통계
        // --------------------------------------------------------------------
        //console.log('# data.rows :: ', JSON.stringify(data.rows));

        // 설문 랜더링
        fnTemplates.survey.render(data.rows);

        break;

      case 'select.survey.join':
        // --------------------------------------------------------------------
        // 조회-설문참여
        // --------------------------------------------------------------------
        //console.log('# data.rows :: ', JSON.stringify(data.rows));

        // 템플릿초기화
        fnInitTemplatesSurveyJoin();

        // 설문참여 랜더링
        fnTemplates.surveyJoin.render(data.rows);

        // --------------------------------------------------------------------
        // 팝업노출
        // --------------------------------------------------------------------
        $('#surveyJoinPopup').show();
        //$('#inputSurveyForm').formClear(true);
        fnKendoInputPoup({height:"auto" ,width:"600px", title:{ nullMsg :'신청내용 상세' } } );

        break;
      case 'winner.direct':
        // --------------------------------------------------------------------
        // 당첨자.직접선택
        // --------------------------------------------------------------------
        var total = data.total;   // 당첨처리건수
        var confirmMsg   = '';

        if (total > 0) {
          // 당첨 처리건 존재
          //confirmMsg = '<div>중복을 제외한 <font color="#FF1A1A">' + total + '</font>명의 당첨자 선정이 완료되었습니다.</div>';
          confirmMsg = '<div><font color="#FF1A1A">' + total + '</font>건의 당첨자 선정이 완료되었습니다.</div>';

          fnKendoMessage({
              message : confirmMsg
            , ok: function() {
                    // 참여자 재조회
                    fnSearchReload('join');
                  }
          });
        }
        else {
          // 당첨 처리건 미존재
          confirmMsg = '<div>당첨 처리건이 없습니다.</div><div>참여자를 다시 확인해주세요.</div>';

          fnKendoMessage({
              message : confirmMsg
            , ok: function() {}
          });
        }

        break;

      case 'winner.random':
        // --------------------------------------------------------------------
        // 당첨자.랜덤선정
        // --------------------------------------------------------------------
        var total = data.total;
        var confirmMsg   = '';

        if (total > 0) {
          // 당첨 처리건 존재
          confirmMsg = '<div><font color="#FF1A1A">' + total + '</font>건의 랜덤추첨이 완료되었습니다.</div><div>당첨자 리스트를 확인해주세요.</div>';

          // 당첨자 조회
          fnSearchReload('winner');
          // 당첨자목록 탭 선택
          $('input:radio[name=tab]:input[value="winner"]').attr("checked", true);

          fnMessage('',  confirmMsg, '');

          //fnKendoMessage({
          //    message : confirmMsg
          //  , ok: function() {
          //          // 당첨자 조회
          //          fnSearchReload('winner');
          //          // 당첨자목록 탭 선택
          //          $('input:radio[name=tab]:input[value="winner"]').attr("checked", true);
          //        }
          //});

        }
        else {
          // 당첨 처리건 미존재
          confirmMsg = '<div>랜덤추첨 처리건이 없습니다.</div><div>참여자 리스트를 확인해주세요.</div>';

          fnKendoMessage({
              message : confirmMsg
            , ok: function() {}
          });
        }

        break;

      case 'update.feedback':
        // --------------------------------------------------------------------
        // 댓글 차단/차단해제
        // --------------------------------------------------------------------
        var total = data.total;
        var confirmMsg   = '';

        if (total > 0) {
          // 성공

          if (gbAdminSecretYn == 'Y') {
            // 차단 -> 차단해제
            confirmMsg = '<div>댓글 차단이 해제되었습니다.</div>';
          }
          else {
            // 차단해제 -> 차단
            confirmMsg = '<div>댓글 차단 되었습니다.</div>';
          }

          fnKendoMessage({
              message : confirmMsg
            , ok: function() {
                    // 재조회
                    var tabId = $('input[name=tab]:checked').val();
                    fnSearchReload(tabId);
                  }
          });
        }
        else {
          // 실패
          if (gbAdminSecretYn == 'Y') {
            // 차단 -> 차단해제
            confirmMsg = '<div>댓글 차단이 실패했습니다.</div>';
          }
          else {
            // 차단해제 -> 차단
            confirmMsg = '<div>댓글 차단해제가 실패했습니다.</div>';
          }

          fnKendoMessage({
              message : confirmMsg
            , ok: function() {}
          });
        }

      default :
    }
  }



  // ##########################################################################
  // # 그리드 Start
  // ##########################################################################

  // ------------------------------- Grid Start -------------------------------
  // ==========================================================================
  // # 그리드 - 참여자/당첨자
  // ==========================================================================
  function fnInitJoinGrid(winnerYn, loginId) {

    // ------------------------------------------------------------------------
    // 최종 페이지 관련 (페이징 기억 관련)
    // ------------------------------------------------------------------------
    var lastPage = sessionStorage.getItem('lastPage');
    LAST_PAGE = lastPage ? JSON.parse(lastPage) : null;

    var isCheckboxHidden = false;

    if (winnerYn == undefined || winnerYn == null || winnerYn == '') {
      // 참여자 조회
    }
    else {
      // 당첨자 조회
      isCheckboxHidden = true;
    }
    //console.log('# winnerYn         :: ', winnerYn);
    //console.log('# isCheckboxHidden :: ', isCheckboxHidden);

    // ------------------------------------------------------------------------
    // 그리드 호출
    // ------------------------------------------------------------------------
    joinGridDs = fnGetPagingDataSource({
        url      : '/admin/pm/event/selectEventJoinList'
      , param    : { evEventId : gbEvEventId,  winnerYn : winnerYn, loginId : loginId}
      , pageSize : PAGE_SIZE
    });

    joinGridOpt  = {
        dataSource  : joinGridDs
      , noRecordMsg : '검색된 목록이 없습니다.'
      , pageable    : {
                        //pageSizes   : [20, 30, 50],
                        buttonCount : 10      // 페이징 네이게이션 개수
                      }
      , navigatable : true
      , scrollable  : false
      , selectable  : true
      , editable    : {
                        confirmation: false     // 켄도 alert 제거
                        //, confirmDelete: "Yes"
                      }
      , resizable   : true
      , autobind    : false
      , columns     : [
                        { field : 'chk'                                   , width: 40 , attributes: {style: "text-align:center;" }, editable:function (dataItem) {return false;}, hidden:isCheckboxHidden
                                                                          , headerTemplate : '<input type="checkbox" id="joinCheckAll" />'
                                                                          , template  : function(dataItem) {
                                                                                          var isDisabled = '';
                                                                                          var isChecked  = '';
                                                                                          //if (dataItem.winnerYn == 'Y' || dataItem.winnerExistYn == 'Y') {
                                                                                          if (dataItem.winnerYn == 'Y') {
                                                                                            // ------------------------------------------------------------------
                                                                                            // 당첨인경우 : 이벤트참여ID 기준으로만 활성 비활성 처리
                                                                                            // ------------------------------------------------------------------
                                                                                            isChecked   = 'checked';
                                                                                            isDisabled  = 'disabled';
                                                                                          }
                                                                                          else {
                                                                                            // ------------------------------------------------------------------
                                                                                            // 당첨이 아닌 경우
                                                                                            // ------------------------------------------------------------------
                                                                                            // 임직원제외 체크여부
                                                                                            var checkEmployeeYn = '';
                                                                                            // 이벤트참여제한고객제외 체크여부
                                                                                            var checkEventJoinYn = '';
                                                                                            // 임직원제외 체크 여부
                                                                                            if($('input:checkbox[name=employeeExceptYn]').is(':checked') == true) {
                                                                                              checkEmployeeYn = 'Y';
                                                                                            }
                                                                                            else {
                                                                                              checkEmployeeYn = 'N';
                                                                                            }
                                                                                            // 이벤트참여제한고객제외 체크여부
                                                                                            if($('input:checkbox[name=eventJoinYn]').is(':checked') == true) {
                                                                                              checkEventJoinYn = 'Y';
                                                                                            }
                                                                                            else {
                                                                                              checkEventJoinYn = 'N';
                                                                                            }

                                                                                            let isDisabledEmployeeYn;   // 비활성여부-임직원
                                                                                            let isDisabledEventJoinYn;  // 비활성여부-이벤트제한

                                                                                            // ------------------------------------------------------------------
                                                                                            // 임직원제외 체크여부 처리
                                                                                            // ------------------------------------------------------------------
                                                                                            if (checkEmployeeYn == 'Y') {
                                                                                              // ----------------------------------------------------------------
                                                                                              // 임직원제외 체크이면
                                                                                              // ----------------------------------------------------------------
                                                                                              if (dataItem.employeeYn == 'Y') {
                                                                                                // 그리드.아이템.임직원제외여부 : Y : 비활성
                                                                                                isDisabledEmployeeYn = true;
                                                                                              }
                                                                                              else {
                                                                                                // 그리드.아이템.임직원제외여부 : N : 활성
                                                                                                isDisabledEmployeeYn = false;
                                                                                              }
                                                                                            }
                                                                                            else {
                                                                                              // ----------------------------------------------------------------
                                                                                              // 임직원제외 체크가 아니면
                                                                                              // ----------------------------------------------------------------
                                                                                              // 체크박스 활성
                                                                                              isDisabledEmployeeYn = false;
                                                                                            }
                                                                                            // ------------------------------------------------------------------
                                                                                            // 이벤트참여제한 체크여부 처리
                                                                                            // ------------------------------------------------------------------
                                                                                            if (checkEventJoinYn == 'Y') {
                                                                                              // ----------------------------------------------------------------
                                                                                              // 이벤트참여제한 체크이면
                                                                                              // ----------------------------------------------------------------
                                                                                              if (dataItem.eventJoinYn == 'Y') {
                                                                                                // 그리드.아이템.이벤트참여제한 : Y : 비활성
                                                                                                isDisabledEventJoinYn = true;
                                                                                              }
                                                                                              else {
                                                                                                // 그리드.아이템.임직원제외여부 : N : 활성
                                                                                                isDisabledEventJoinYn = false;
                                                                                              }
                                                                                            }
                                                                                            else {
                                                                                              // ----------------------------------------------------------------
                                                                                              // 임직원제외 체크가 아니면
                                                                                              // ----------------------------------------------------------------
                                                                                              // 체크박스 활성
                                                                                              isDisabledEventJoinYn = false;
                                                                                            }
                                                                                            if (isDisabledEmployeeYn == true || isDisabledEventJoinYn == true) {
                                                                                              // 임직원제외 또는 이벤트참여제한 체크인 경우
                                                                                              // 체크박스 비활성
                                                                                              isDisabled  = 'disabled';
                                                                                            }
                                                                                            else {
                                                                                              // 체크박스 활성
                                                                                              isDisabled  = '';
                                                                                            }
                                                                                          }
                                                                                          return '<input type="checkbox" name="joinCheck" class="couponGridChk" ' + isDisabled + ' ' + '' + isChecked + ' />'
                                                                                        }
                        }
                      , { field:'loginId'         , title: 'ID'           , width:100 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}
                                                                          , headerTemplate  : function() {
                                                                                                var tmpTag  = '<div class="complex-condition">'
                                                                                                            + '<input type="text" id="loginId" name="loginId" class="comm-input" style="width:95%;" placeholder="ID 조회">'
                                                                                                            + '</div>'
                                                                                                            ;
                                                                                                return tmpTag;
                                                                                              }
                        }
                      , { field:'userNm'          , title: '이름'         , width: 60 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}}
                      , { field:'groupNm'         , title: '회원등급'     , width: 60 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}}
                      , { field:'employeeYnNm'    , title: '임직원여부'   , width: 60 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}}
                      , { field:'eventJoinYn'     , title: '참여제한'     , width: 60 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}}
                      , { field:'regDt'           , title: '가입일'       , width:140 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}
                                                                          //, template  : function(dataItem) {
                                                                          //                if (dataItem.regDt != undefined && dataItem.regDt != null && dataItem.regDt != '') {
                                                                          //                  if (dataItem.regDt.length >= 10) {
                                                                          //                    return (dataItem.regDt).substring(0, 10);
                                                                          //                  }
                                                                          //                  else {
                                                                          //                    return dataItem.regDt;
                                                                          //                  }
                                                                          //                }
                                                                          //                else {
                                                                          //                  return dataItem.regDt;
                                                                          //                }
                                                                          //              }
                        }
                      , { field:'mail'            , title: '이메일'       , width:140 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}}
                      , { field:'mobile'          , title: '휴대폰'       , width:100 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}}
                      , { field:'reqDt'           , title: '신청일'       , width: 80 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}
                                                                          , template  : function(dataItem) {
                                                                                          if (dataItem.reqDt != undefined && dataItem.reqDt != null && dataItem.reqDt != '') {
                                                                                            if (dataItem.reqDt.length >= 10) {
                                                                                              return (dataItem.reqDt).substring(0, 10);
                                                                                            }
                                                                                            else {
                                                                                              return dataItem.reqDt;
                                                                                            }
                                                                                          }
                                                                                          else {
                                                                                            return dataItem.reqDt;
                                                                                          }
                                                                                        }
                        }
                      , { field:'reqConts'        , title: '신청내용'     , width:120 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}
                                                                          , template  : function(dataItem) {
                                                                                          if (dataItem.eventTp == 'EVENT_TP.SURVEY') {
                                                                                            return '<div id="pageMgrButtonArea" class="textCenter">'
                                                                                                 + '<button type="button" class="btn-lightgray btn-s" kind="fnBtnSurveyPopup" style="width:100px;">상세보기</button>'
                                                                                                 + '</div>';
                                                                                          }
                                                                                          else {
                                                                                            // 설문이외
                                                                                            if (dataItem.reqConts == undefined || dataItem.reqConts == null) {
                                                                                              return '';
                                                                                            }
                                                                                            else {
                                                                                              return dataItem.reqConts;
                                                                                            }
                                                                                          }
                                                                                        }
                        }
                      , { field:'winnerYn'        , title: '당첨여부'     , width: 50 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}}
                      , { field:'handwrittenLotteryName'        , title: '당첨방법'     , width: 50 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}}
                      //, { field:'winnerExistYn'   , title: '당첨존재여부' , width:  0 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}, hidden:true}
                      , { field:'winnerConts'     , title: '당첨내용'     , width:120 , attributes:{ style:'text-align:center' }, editable:function (dataItem) {return false;}
                                                                          , template  : function(dataItem) {
                                                                                          if (dataItem.winnerYn == 'Y') {
                                                                                            if (dataItem.winnerConts == undefined || dataItem.winnerConts == null) {
                                                                                              return '';
                                                                                            }
                                                                                            else {
                                                                                              return dataItem.winnerConts;
                                                                                            }
                                                                                          }
                                                                                          else {
                                                                                            return '';
                                                                                          }
                                                                                        }
                        }
                      , { field:'management'      , title: '관리'         , width:100 , attributes: {style:'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                          , template: function(dataItem) {
                                                                                        var btnDisplayYn = false;

                                                                                        if (dataItem.eventTp == 'EVENT_TP.NORMAL') {
                                                                                          // 일반         : 노출
                                                                                          btnDisplayYn = true;
                                                                                        }
                                                                                        else if (dataItem.eventTp == 'EVENT_TP.SURVEY') {
                                                                                          // 설문         : 노출
                                                                                          btnDisplayYn = false;
                                                                                        }
                                                                                        else if (dataItem.eventTp == 'EVENT_TP.ATTEND') {
                                                                                          // 스탬프(출석) : 미노출
                                                                                          btnDisplayYn = false;
                                                                                        }
                                                                                        else if (dataItem.eventTp == 'EVENT_TP.MISSION') {
                                                                                          // 스탬프(미션) : 미노출
                                                                                          btnDisplayYn = false;
                                                                                        }
                                                                                        else if (dataItem.eventTp == 'EVENT_TP.PURCHASE') {
                                                                                          // 스탬프(구매) : 노출
                                                                                          btnDisplayYn = false;
                                                                                        }
                                                                                        else if (dataItem.eventTp == 'EVENT_TP.ROULETTE') {
                                                                                          // 룰렛         : 미노출
                                                                                          btnDisplayYn = false;
                                                                                        }
                                                                                        else if (dataItem.eventTp == 'EVENT_TP.EXPERIENCE') {
                                                                                             // 체험단
                                                                                            if(dataItem.eventDrawTp == 'EVENT_DRAW_TP.FIRST_COME'){
                                                                                                btnDisplayYn = false;
                                                                                            }else{
                                                                                                btnDisplayYn = true;
                                                                                            }
                                                                                        }

                                                                                        if (btnDisplayYn == true && dataItem.normalEventTp != 'NORMAL_EVENT_TP.BUTTON') {               // 참여방법이 '응모버튼'인 경우 댓글차단 버튼 비표시 처리
                                                                                          // 버튼 노출
                                                                                          var btnTitle = '';
                                                                                          var btnColorClass = '';

                                                                                          if (dataItem.adminSecretYn == undefined || dataItem.adminSecretYn == null || dataItem.adminSecretYn == '') {
                                                                                            btnTitle = '댓글 차단';
                                                                                            btnColorClass = 'btn-red';
                                                                                          }
                                                                                          else {
                                                                                            if (dataItem.adminSecretYn == 'Y') {
                                                                                              btnTitle = '댓글 차단해제';
                                                                                              btnColorClass = 'btn-point';
                                                                                            }
                                                                                            else {
                                                                                              btnTitle = '댓글 차단';
                                                                                              btnColorClass = 'btn-red';
                                                                                            }
                                                                                          }

                                                                                          return  '<div id="pageMgrButtonArea" class="textCenter">'
                                                                                                + '<button type="button" class="'+btnColorClass+' btn-s" kind="btnFeedbackBlockEdit" style="width:100px;">' + btnTitle + '</button>'
                                                                                                + '</div>';
                                                                                        }
                                                                                        else {
                                                                                          // 버튼 미노출
                                                                                          return '';
                                                                                        }
                                                                                      }
                        }
                      ]
    };

    joinGrid = $('#joinGrid').initializeKendoGrid( joinGridOpt ).cKendoGrid();

    //// ------------------------------------------------------------------------
    //// 그리드 클릭 이벤트
    //// ------------------------------------------------------------------------
    //$('#joinGrid').on("click", "tbody>tr", function () {
    //  fnGridClick();
    //});

    // ------------------------------------------------------------------------
    // 그리드 조회 후 실행
    // ------------------------------------------------------------------------
    joinGrid.bind('dataBound', function(e) {


      //var joinGridArr  = $('#joinGrid').data('kendoGrid').dataSource.data();
      //var joinGridArr  = $('#joinGrid').data('kendoGrid').dataSource.data();
      //console.log('# joinGridArr.length>>>> :: ', joinGridArr.length);

      // ----------------------------------------------------------------------
      // ID조회 입력 허용
      // ----------------------------------------------------------------------
      e.sender.thead.on("mousedown", "input", function(e) {
        e.stopPropagation();
      });

      // ----------------------------------------------------------------------
      // NO 항목 및 전체건수 Set
      // ----------------------------------------------------------------------
      var row_num = joinGridDs._total - ((joinGridDs._page - 1) * joinGridDs._pageSize);
      $('#joinGrid tbody > tr .row-number').each(function(index) {
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      $('#totalCnt').text(joinGridDs._total);

      gbTotalJointCnt = joinGridDs._total;
      //console.log('# joinGridDs._total :: ', gbTotalJointCnt);

      //if (gbWinnerYn == 'Y') {
      //  // 당첨자
      //  $('#winnerTotalCntSpan').text(joinGridDs._total);
      //}
      //else {
      //  // 참여자
      //  $('#joinTotalCntSpan').text(joinGridDs._total);
      //}
      //console.log('# gbEventDrawTp :: ', gbEventDrawTp);
      // ----------------------------------------------------------------------
      // 당첨자설정유형이 관리자추첨이 아닌경우 처리
      // ----------------------------------------------------------------------
      var lotteryAbleYn = false; // 추첨가능여부

      if (gbEventDrawTp == 'EVENT_DRAW_TP.ADMIN') {
        // --------------------------------------------------------------------
        // 당첨자설정유형이 관리자추첨
        // --------------------------------------------------------------------
        lotteryAbleYn = true;

        // 이벤트종료여부 체크
        if (gbEventEndYn != true) {
          // 이벤트종료
          lotteryAbleYn = false;
        }
      }
      else {
        // --------------------------------------------------------------------
        // 당첨자설정유형이 관리자추첨 이외
        // --------------------------------------------------------------------
        lotteryAbleYn = false;
      }

      //console.log('# 종료여부      :: ', gbEventEndYn);
      //console.log('# 추첨가능 여부 :: ', lotteryAbleYn);
      if (lotteryAbleYn == true) {
        // --------------------------------------------------------------------
        // 추첨 가능
        // --------------------------------------------------------------------
        // --------------------------------------------------------------------
        // 그리드 헤더영역 노출
        // --------------------------------------------------------------------
        gbGridHeaderViewYn = true;
        if (gbTabTp == 'join') {
          $('#gridHeader').show();
        }
        else {
          $('#gridHeader').hide();
        }
      }
      else {
        // --------------------------------------------------------------------
        // 추첨 불가능
        // --------------------------------------------------------------------

        // --------------------------------------------------------------------
        // 그리드 헤더영역 숨김
        // --------------------------------------------------------------------
        gbGridHeaderViewYn = false;
        $('#gridHeader').hide();

        // --------------------------------------------------------------------
        // 당첨자선택 비활성
        // --------------------------------------------------------------------
        $('#fnBtnWinnerSelect').data('kendoButton').enable(false);

        // --------------------------------------------------------------------
        // 전체체크 언체크
        // --------------------------------------------------------------------
        $('#joinCheckAll').prop("checked" , false);
        $('#joinCheckAll').prop("disabled", true);

        // --------------------------------------------------------------------
        // 체크박스 언체크 + 비활성
        // --------------------------------------------------------------------
        var joinGridArr  = $('#joinGrid').data('kendoGrid').dataSource.data();

        if (joinGridArr != undefined && joinGridArr != null && joinGridArr.length > 0) {

          for (var i = 0; i < joinGridArr.length; i++) {
            const $tr = $('#joinGrid tbody').find('tr').eq(i);
            //console.log($tr);
            const $checkbox = $tr.find('input[name="joinCheck"]');
            // 체크박스 해제
            $checkbox.prop('checked' , false);
            // 체크박스 비활성
            $checkbox.attr('disabled', true);
          }
        }
      }

      // ----------------------------------------------------------------------
      // ID조회 검색어 유지
      // ----------------------------------------------------------------------
      $('#loginId').val(gbLoginId);

    });

    // ------------------------------------------------------------------------
    // 전체체크박스
    // ------------------------------------------------------------------------
    $("#joinCheckAll").on("click",function(index) {
      // 개별체크박스 처리
      if($("#joinCheckAll").prop("checked") == true) {
        // 전체체크
        // 개별체크 선택
        $('INPUT[name=joinCheck]').each(function() {
          const $this = $(this);
          if(!$this.attr('disabled')) {
            // 활성화 된것만 체크 처리
            $this.prop("checked", true);
          }
        });
      }
      else{
        // 전체해제
        // 개별체크 선택 해제
        $('INPUT[name=joinCheck]').each(function() {
          const $this = $(this);
          if(!$this.attr('disabled')) {
            // 활성화 된것만 체크해제 처리
            $this.prop("checked", false);
          }
        });
      }
    });
    // ------------------------------------------------------------------------
    // 개별체크박스
    // ------------------------------------------------------------------------
    $('#ng-view').on("click","input[name=joinCheck]",function(index) {

      const totalCnt    = $("input[name=joinCheck]").length;
      const checkedCnt  = $("input[name=joinCheck]:checked").length;
      // 전체체크박스 처리
      if (totalCnt == checkedCnt) {
        $('#joinCheckAll').prop("checked", true);
      }
      else {
        $('#joinCheckAll').prop("checked", false);
      }
    });

    // ------------------------------------------------------------------------
    // 댓글 차단/해제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#joinGrid').on("click", 'button[kind=btnFeedbackBlockEdit]', function(e) {
      e.preventDefault();

      // ----------------------------------------------------------------------
      // 세션 lastPage 삭제(페이징 기억 관련)
      // ----------------------------------------------------------------------
      var curPage = joinGridDs._page;
      sessionStorage.setItem('lastPage', JSON.stringify(curPage));

      let dataItem = joinGrid.dataItem($(e.currentTarget).closest('tr'));

      if (dataItem != null && dataItem != 'null' && dataItem != '') {
        //console.log('# dataItem :: ', JSON.stringify(dataItem));
        //console.log('# evEventId      :: ', JSON.stringify(dataItem.evEventId));
        //console.log('# evEventJoinId  :: ', JSON.stringify(dataItem.evEventJoinId));
        //console.log('# adminSecretYn  :: ', JSON.stringify(dataItem.adminSecretYn));
      }
      // 참고 : 아래의 경우는 버튼 클릭시 값을 가져오지 못함, 로우를 선택해야 함
      //let dataItem2 = joinGrid.dataItem(joinGrid.select());
      //console.log('# btnInventoryEdit.dataItem :: ' + JSON.stringify(dataItem));
      //fnBtnInventoryEdit(dataItem);

      // 댓글 차단/해제 버튼 클릭
      fnBtnFeedbackBlockEdit(dataItem);
    });

    // ------------------------------------------------------------------------
    // 설문 팝업 오픈 버튼 클릭
    // ------------------------------------------------------------------------
    $('#joinGrid').on("click", 'button[kind=fnBtnSurveyPopup]', function(e) {
      e.preventDefault();

      // ----------------------------------------------------------------------
      // 세션 lastPage 삭제(페이징 기억 관련)
      // ----------------------------------------------------------------------
      var curPage = joinGridDs._page;
      sessionStorage.setItem('lastPage', JSON.stringify(curPage));

      let dataItem = joinGrid.dataItem($(e.currentTarget).closest('tr'));

      if (dataItem != null && dataItem != 'null' && dataItem != '') {
        //console.log('# dataItem :: ', JSON.stringify(dataItem));
        //console.log('# evEventId      :: ', JSON.stringify(dataItem.evEventId));
        //console.log('# evEventJoinId  :: ', JSON.stringify(dataItem.evEventJoinId));
        //console.log('# adminSecretYn  :: ', JSON.stringify(dataItem.adminSecretYn));
      }
      // 설문참여조회(팝업용)
      fnSearchSurveyJoin(dataItem);
    });

    // ------------------------------------------------------------------------
    // ID 조회 엔터 이벤트
    // ------------------------------------------------------------------------
    $("input[name=loginId]").keydown(function (e) {
      e.stopPropagation();  // 포커스 가기위해 필요
      if(e.keyCode == 13){
        // 키가 13이면 실행 (엔터는 13)
        // 입력ID
        gbLoginId = $('#loginId').val();
        // 포커스
        $('#loginId').focus();
        //$(this).focus();

        // 참여자 목록 조회
        var data = new Object();
        data.evEventId = gbEvEventId;
        $('#joinGrid').data('kendoGrid').destroy();
        $('#joinGrid').empty();

        var winnerYn = '';
        var tabId = $('input[name=tab]:checked').val();
        if (tabId == 'join') {
          winnerYn = '';
        }
        else if (tabId == 'winner') {
          winnerYn = 'Y';
        }
        fnInitJoinGrid(winnerYn, gbLoginId);
        fnSearchJoinList(data);
      }
    });

  }
  // ------------------------------- Grid End ---------------------------------

  // # 그리드 End
  // ##########################################################################

  // ==========================================================================
  // 제이쿼리 템플릿 초기화 함수
  // ==========================================================================
  /**
   * 제이쿼리 템플릿 초기화 함수
   * @param {object} el : 템플릿이 추가될 타겟, 제이쿼리 엘리먼트
   * @param {string} template : 데이터 바인딩 시 사용할 제이쿼리 템플릿, html 태그 또는 제이쿼리 엘리먼트
   */
  function createTemplateItem({ el, template }) {
      if (!el || !template) {
          console.warn('error', el, template);
          return {};
      }

      const $el = $(el);
      const _template = $(template).html();

      return {
          el: $el,
          template: $.template(_template),
          render: function (data, callback) {
              const tmpl = $.tmpl(this.template, data);
              this.el.empty();
              tmpl.appendTo(this.el);

              if (typeof callback === 'function') {
                  callback.call(this, data);
              }
          },
          add: function (data, callback) {
              const tmpl = $.tmpl(this.template, data);
              tmpl.appendTo(this.el);

              if (typeof callback === 'function') {
                  callback.call(this, data);
              }
          },
      };
  }

  // ##########################################################################
  // # 기타-Start
  // ##########################################################################
  // ==========================================================================
  // # 기타-팝업창닫기
  // ==========================================================================
  function fnClose() {
    var kendoWindow =$('#kendoPopup').data('kendoWindow');
    kendoWindow.close();
  }

  // ==========================================================================
  // # 기타-오류메시지처리
  // ==========================================================================
  function fnMessage(key, nullMsg, ID) {
    fnKendoMessage({
        message : fnGetLangData({ key : key, nullMsg : nullMsg})
      , ok      : function() {
                    if (ID != null && ID != '') {

                      if (ID == 'detlHtmlPc' || ID == 'detlHtmlMo') {
                        var editor = $("#"+ID).data("kendoEditor");
                        editor.focus();
                      }
                      else {
                        $('#'+ID).focus();
                      }
                    }
                  }
    });
  }

  // ==========================================================================
  // # 버튼-상세이동
  // ==========================================================================
  function fnBtnGoEdit() {

    //history.back();
    let option = {};
    option.url    = '#/eventMgm';
    // 이벤트 등록/수정 : 1093 / 130000112 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    option.menuId = 1093;
    option.target = '_blank';
    option.data = {eventTp : gbEventTp, evEventId : gbEvEventId, mode : 'update'};
    // 화면이동
    fnGoNewPage(option);  // 새페이지(탭)
    //fnGoPage(option);
  }

  // ==========================================================================
  // # 버튼-목록이동
  // ==========================================================================
  function fnBtnGoList() {

    //history.back();
    let option = {};

    if (gbFromPage == 'DETL') {
      // 상세에서 호출
      option.url    = '#/eventMgm';
    }
    else {
      // 리스트에서 호출
      if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
        // 체험단
        option.url    = '#/eventExprncList';
      }
      else {
        // 체험단이외
        option.url    = '#/eventNormalList';
      }
    }

    // 이벤트 목록 : 130000110 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    option.menuId = 1090;
    option.target = '_self';
    option.data = {eventTp : gbEventTp};
    // 화면이동
    fnGoPage(option);
  }

  // ==========================================================================
  // # 버튼-팝업-닫기
  // ==========================================================================
  function fnPopupClose() {

    var kendoWindow =$('#kendoPopup').data('kendoWindow');
    kendoWindow.close();

  }

  // # 기타-End
  // ##########################################################################

  // ==========================================================================
  // # 버튼-엑셀다운로드-참여자목록/당첨자목록
  // ==========================================================================
  function fnBtnExcelDownJoinList() {
    fnExcelDownJoinList('JoinList', null);
  }

  // ==========================================================================
  // # 버튼-엑셀다운로드-직접입력목록
  // ==========================================================================
  function fnBtnExcelDownDirectList(evEventSurveyQuestionId) {
    fnExcelDownJoinList('DirectList', evEventSurveyQuestionId);
  }

  // ==========================================================================
  // # 엑셀다운로드-참여자목록/당첨자목록
  // ==========================================================================
  function fnExcelDownJoinList(gubn, keyVal) {
    //console.log('# fnExcelDownJoinList Start [', gubn, '][', keyVal, ']');

    // 세션 체크
    if (PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined') {
      //console.log('# PG_SESSION :: ' + JSON.stringify(PG_SESSION));
      //location.href = "/layout.html#/goodsMgm?ilGoodsId="+data.ilGoodsId;
      fnKendoMessage({ message : "세션이 종료되었습니다. \n다시 로그인 해 주십시오.", ok : function () {
        location.href = "/admVerify.html";
      }});
      return false;
    }
    //console.log('# PG_SESSION :: ', JSON.stringify(PG_SESSION));

    var winnerYn    = '';         // 당첨여부
    var excelTitle  = '';         // 엑셀타이틀명(메시지용)
    var url         = '';         // 호출URL
    var inParam = new Object();   // 파함

    if (gubn == 'JoinList') {
      // ----------------------------------------------------------------------
      // 참여/당첨 목록
      // ----------------------------------------------------------------------

      if ($('input[name=tab]:checked').val() == 'join') {
        // 참여자
        winnerYn = '';
        excelTitle = '참여자목록';
      }
      else {
        // 당첨자
        excelTitle = '당첨자목록';
        winnerYn = 'Y';
      }
      // 파람
      inParam.evEventId = gbEvEventId;
      inParam.winnerYn  = winnerYn;
      // 호출
      url = '/admin/pm/event/getExportExcelEventJoinList';
    }
    else {
      // ----------------------------------------------------------------------
      // 직접입력 목록
      // ----------------------------------------------------------------------
      excelTitle = '직접입력목록';
      // 파람
      inParam.evEventSurveyQuestionId = keyVal;
      // 호출
      url = '/admin/pm/event/getExportExcelEventJoinDirectJoinList';
    }

    // ------------------------------------------------------------------------
    // 1. 세션체크
    // ------------------------------------------------------------------------
    fnAjax({
      url       :'/system/getSessionCheck'
    , method    : 'POST'
    //, success   : function(data, status, xhr) {
    , success   : function(data) {
                    if(data.session){
                      // ------------------------------------------------------
                      // 2. 엑셀다운로드실행
                      // ------------------------------------------------------
                      var confirmMsg = excelTitle + '을 다운로드 하시겠습니까?';

                      fnKendoMessage({
                          message : fnGetLangData({key :"", nullMsg : confirmMsg })
                        , type    : "confirm"
                        , ok      : function() {
                                      if (gubn == 'JoinList') {
                                        // ------------------------------------
                                        // 참여자/당첨자 조회인 경우 엑셀다운로드 이력 등록
                                        // ------------------------------------
                                        fnExcelDownloadForLog(url, inParam);
                                      }
                                      else {
                                        // 바로 엑셀다운로드
                                        fnExcelDownload(url, inParam);
                                      }
                                    }
                        });
                    }
                    else {
                      fnKendoMessage({ message : "세션이 종료되었습니다. \n다시 로그인 해 주십시오.", ok : function () {
                        location.href = "/admVerify.html";
                      }});
                      return false;
                    }
                  }
    });

  }

  // ==========================================================================
  // # 엑셀다운로드 로그 등록
  // ==========================================================================
  function fnExcelDownloadForLog(url, inParam) {

    fnKendoPopup({
        id      : 'excelDownloadReasonPopup'
      , title   : '엑셀 다운로드 사유'
      , src     : '#/excelDownloadReasonPopup'
      , param   : {excelDownloadType : 'EXCEL_DOWN_TP.EVENT_PARTICIPANT'}
      , width   : '700px'
      , height  : '300px'
      , success : function(id, data){
                    if(data == 'EXCEL_DOWN_TP.EVENT_PARTICIPANT'){
                      fnExcelDownload(url, inParam);
                    }
                  }
    });
  }

  // ==========================================================================
  // ------------------------------- Html 버튼 바인딩  Start ------------------
  // ==========================================================================

  /** 당첨자선택 */
  $scope.fnBtnWinnerSelect    = function() { fnBtnWinnerSelect();};
  /** 당첨자등록(Popup) */
  $scope.fnBtnWinnerSave      = function() { fnBtnWinnerSave();};
  /** 설문참여조회Popup Open */
  $scope.fnBtnSurveyPopupOpen = function() { fnSearchSurveyJoin();};
  /** 팝업닫기(Popup) */
  $scope.fnPopupClose         = function() { fnPopupClose();};

  /** 참여목록/당첨목록 엑셀다운로드 */
  $scope.fnBtnExcelDownJoinList   = function() { fnBtnExcelDownJoinList();};
  /** 직접입력목록 엑셀다운로드 */
  $scope.fnBtnExcelDownDirectList = function(evEventSurveyQuestionId) { fnBtnExcelDownDirectList(evEventSurveyQuestionId);};
  /** 상세화면이동 */
  $scope.fnBtnGoEdit          = function() { fnBtnGoEdit();};
  /** 목록이동 */
  $scope.fnBtnGoList          = function() { fnBtnGoList();};

  /** 이미지URL 추가 */
  $scope.fnImgUrlPath         = function(imgPath) {
                                  //console.log('# imgPath :: ', imgPath);
                                  return publicStorageUrl + imgPath;
                                };

  /** 이미지 에러 핸들링 */
  $scope.handleImgError     = function(img) {
                                //console.log('# img :: ', img);
                                const imgSrc = '/contents/images/common/add-image-noti.png';
                                //const $parent = img.closest('.survey__image');
                                //$parent.style.display = 'none';
                                img.src = imgSrc;
                                img.style.border = '1px solid #ddd';
                              };

  /** 랜덤 당첨자 선택 차등 혜택 추가 삭제 */
  $scope.fnBenefitController = function(action, object) { fnBenefitController(action, object);};

}); // document ready - END


