/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 이벤트리스트 처리
 * @
 * @ 아래 이벤트리스트 공통 처리
 * @  - 일반   이벤트리스트 : eventNormalListController.js, eventNormalList.html
 * @                        : EVENT_TP.NORMAL     - 일반이벤트
 * @                        : EVENT_TP.SURVEY     - 설문이벤트
 * @                        : EVENT_TP.ATTEND     - 스탬프(출석)이벤트
 * @                        : EVENT_TP.MISSION    - 스탬프(미션)이벤트
 * @                        : EVENT_TP.PURCHASE   - 스탬프(구매)이벤트
 * @                        : EVENT_TP.ROULETTE   - 룰렛이벤트
 * @
 * @  - 체험단 이벤트리스트 : eventExprncListController.js, eventExprncList.html
 * @                          EVENT_TP.EXPERIENCE - 체험단이벤트
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2020.12.15    dgyoun        최초생성
 * @
 ******************************************************************************/
'use strict';

var LAST_PAGE = null;   // 최종 페이지 (페이징 기억 관련)
var PAGE_SIZE = 20;
var eventGridDs, eventGridOpt, eventGrid;
var publicStorageUrl = fnGetPublicStorageUrl();

var gbPageParam = '';   // 넘어온 페이지파라미터
var gbMode      = '';
var delEvEventIdArr;    // 삭제 EvEventId 리스트
// var gbEventTp = '';   // 이벤트유형  <- 이벤트유형별 js 페이지에서 선언 및 정의 됨
var gbSearchInfoObj = new Object();   // 조회조건 객체

var gbEvEventId = ''; // 미리보기(=evEventId)
var CUR_SERVER_URL = fnGetServerUrl().mallUrl;
var gbEventTp = '';

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
        PG_ID     : pageId // 'eventList'
      , callback  : fnUI
    });

    // ------------------------------------------------------------------------
    // 페이지파라미터
    // ------------------------------------------------------------------------
    gbPageParam = fnGetPageParam();
    //console.log('# gbPageParam :: ', JSON.stringify(gbPageParam));

    // ------------------------------------------------------------------------
    // 상위타이틀
    // ------------------------------------------------------------------------
    //if (gbEventTp != null && gbEventTp != 'null') {
    //
    //  if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
    //    $('#pageTitleSpan').text('체험단 이벤트 목록');
    //  }
    //}

    // ------------------------------------------------------------------------
    // 조회조건 정보 Set
    // ------------------------------------------------------------------------
    ////console.log('# ================================================');
    ////console.log('# [리스트] 조회조건 Set');
    ////console.log('# ================================================');
    ////console.log('# ------------------------------------------------');
    //let searchInfo    = sessionStorage.getItem('searchInfo');
    //if (searchInfo != null) {
    //  gbSearchInfoObj = JSON.parse(searchInfo);
    //}
    ////let isFromDetl = gbSearchInfoObj.isFromDetl;
    ////console.log('# searchInfoObj :: ', JSON.stringify(gbSearchInfoObj));
    //console.log('# isFromDetl :: ', isFromDetl);


  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

    fnInitButton();           // Initialize Button ----------------------------

    fnInitGrid();             // Initialize Grid ------------------------------

    fnInitOptionBox();        // Initialize Option Box ------------------------

    fnInitEvent();            // Initialize Event -----------------------------

    fnSetDefaultPre();        // 기본 설정-조회전 -----------------------------

    // 조회조건 Set을 위한 딜레이 설정
    //setTimeout(function() {
      fnSearch();               // 조회 ---------------------------------------
    //}, 1000);   // 모두 처리 안될 경우 시간 늘릴 것


    fnSetDefaultPost();       // 기본 설정-조회후 -----------------------------
  }

  // ==========================================================================
  // # 초기화 - 버튼
  // ==========================================================================
  function fnInitButton() {
    //$('#fnSearch, #fnConfirm, #fnClear, #fnClose, #fnExcelDownload, #fnShowImage').kendoButton();
  }

  // ==========================================================================
  // # 초기화 - 그리드
  // ==========================================================================
  function fnInitGrid() {

    fnInitEventGrid();
  }

  // ==========================================================================
  // # 초기화 - 값
  // ==========================================================================
  function fnClear() {

    $('#searchForm').formClear(true);
    fnSetDefaultPre();
  }

  // ==========================================================================
  // # 체험단이벤트 VS 그외 이벤트 설정
  // ==========================================================================
  function fnInitEventTp() {
    //console.log('# fnInitEventTp Start [', gbEventTp, ']');
    if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
      // 체험단이벤트
      // ----------------------------------------------------------------------
      // 이벤트유형조건 숨김
      // ----------------------------------------------------------------------
      $('#eventTpTr').hide();                           // 숨김
      $('input[name=eventTp]').attr('disabled', true);  // 비활성
    }
    else {
      // 체험단이벤트 이외 이벤트
      // ----------------------------------------------------------------------
      // 이벤트유형조건 노출
      // ----------------------------------------------------------------------
      $('#eventTpTr').show();
      // ----------------------------------------------------------------------
      // 체험단이벤트 제거
      // ----------------------------------------------------------------------
      const $checkbox = $('#eventTp').find('input[value="EVENT_TP.EXPERIENCE"]');
      const $label = $checkbox.closest('label');
      $label.remove();
    }
  }

  // ==========================================================================
  // # 기본 설정 - 조회후
  // ==========================================================================
  function fnSetDefaultPost() {

  }

  // ==========================================================================
  // # 초기화 - 옵션박스
  // ==========================================================================
  function fnInitOptionBox() {

    // ------------------------------------------------------------------------
    // 팝업
    // ------------------------------------------------------------------------
    //$('#kendoPopup').kendoWindow({
    //    visible: false
    //  , modal: true
    //});

    // ------------------------------------------------------------------------
    // 검색구분(S) - 공통코드 아님
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id      : 'searchSe'
      , tagId   : 'searchSe'
      , data    : [
                    {'CODE':'NAME','NAME':'이벤트명'}
                  , {'CODE':'ID'  ,'NAME':'이벤트ID'}
                  ]
      , chkVal  : 'NAME'
    });

    // ------------------------------------------------------------------------
    // 전시여부(R)  - 공통코드 아님
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'dispYn'
      , tagId   : 'dispYn'
      , data    : [
                    { 'CODE' : ''   , 'NAME' : '전체'   }
                  , { 'CODE' : 'Y'  , 'NAME' : '예'     }
                  , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                  ]
      , chkVal  : ''
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 몰구분(C)
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'mallDiv'
      , tagId       : 'mallDiv'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'MALL_DIV', 'useYn' : 'Y'}
      , async       : true
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
      , success     : function() {
                        // 전체선택
                        $('input[name=mallDiv]:checkbox').prop('checked', true);

                        // ----------------------------------------------------
                        // 조회조건 Set : 시간차로 인해 fnSetSearchInfo 에서 처리가 안되는 경우
                        // ----------------------------------------------------
                        if (fnIsEmpty(gbSearchInfoObj.mallDiv) == false) {
                          let mallDivArr = (gbSearchInfoObj.mallDiv).split('∀');
                          if (fnIsEmpty(mallDivArr) == false && mallDivArr.length > 0) {
                            $("input[name=mallDiv]:checkbox").prop("checked", false);
                            for (var i = 0; i < mallDivArr.length; i++) {
                              $('input:checkbox[name="mallDiv"]:input[value="'+mallDivArr[i]+'"]').prop('checked', true);
                            }
                          }
                        }
                      }
    });

    // ------------------------------------------------------------------------
    // 사용여부(R)  - 공통코드 아님
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'useYn'
      , tagId   : 'useYn'
      , data    : [
                    { 'CODE' : ''   , 'NAME' : '전체'   }
                  , { 'CODE' : 'Y'  , 'NAME' : '예'     }
                  , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                  ]
      , chkVal  : ''
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 진행상태구분(C) - 공통코드 아님
    // ------------------------------------------------------------------------

    let statusSeArr = [
                        { 'CODE' : 'ALL'    , 'NAME' : '전체'    }
                      , { 'CODE' : 'BEF'    , 'NAME' : '진행예정'}
                      , { 'CODE' : 'ING'    , 'NAME' : '진행중'  }
                      , { 'CODE' : 'END'    , 'NAME' : '진행종료'}
                      ];

    if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
      statusSeArr = [
                      { 'CODE' : 'ALL'    , 'NAME' : '전체'      }
                    , { 'CODE' : 'BEF'    , 'NAME' : '진행예정'  }
                    , { 'CODE' : 'ING'    , 'NAME' : '진행중'    }
                    , { 'CODE' : 'SEL'    , 'NAME' : '당첨자선정'}
                    , { 'CODE' : 'FED'    , 'NAME' : '후기작성'  }
                    , { 'CODE' : 'END'    , 'NAME' : '진행종료'  }
      ];
    }

    fnTagMkChkBox({
        id        : 'statusSe'
      , tagId     : 'statusSe'
      , data      : statusSeArr
      , chkVal    : ''
      , style     : {}
      //, success     : function() {
      //                  // 전체선택
      //                  $('input[name=statusSe]:checkbox').prop('checked', true);
      //                }
    });
    $('input[name=statusSe]:checkbox').prop('checked', true);

    // ------------------------------------------------------------------------
    // 접근권한설정(회원등급레벨)(C)
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'userMaster'
      , tagId       : 'userMaster'
      , url         : '/admin/comn/getUserMasterCategoryList'
      , textField   : 'groupMasterName'
      , valueField  : 'urGroupMasterId'
      , async       : true
      , isDupUrl    : 'Y'
      , blank       : '회원그룹명 선택'
    });
    //fnTagMkChkBox({
    //    id          : 'evGroupTp'
    //  , tagId       : 'evGroupTp'
    //  , url         : '/admin/comn/getCodeList'
    //  , params      : {'stCommonCodeMasterCode' : 'EV_GROUP_TP', 'useYn' : 'Y'}
    //  , async       : true
    //  , isDupUrl    : 'Y'
    //  , style       : {}
    //  , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
    //});

    // ------------------------------------------------------------------------
    // 노출범위(디바이스)(C)
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'goodsDisplayType'
      , tagId       : 'goodsDisplayType'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'GOODS_DISPLAY_TYPE', 'useYn' : 'Y'}
      , async       : true
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
      , success     : function() {
                        // 전체선택
                        $('input[name=goodsDisplayType]:checkbox').prop('checked', true);

                        // ------------------------------------------------------------------------
                        // 조회조건 Set : 시간차로 인해 fnSetSearchInfo 에서 처리가 안되는 경우
                        // ------------------------------------------------------------------------
                        if (fnIsEmpty(gbSearchInfoObj.goodsDisplayType) == false) {
                          let goodsDisplayTypeArr = (gbSearchInfoObj.goodsDisplayType).split('∀');
                          if (fnIsEmpty(goodsDisplayTypeArr) == false && goodsDisplayTypeArr.length > 0) {
                            $("input[name=goodsDisplayType]:checkbox").prop("checked", false);
                            for (var i = 0; i < goodsDisplayTypeArr.length; i++) {
                              $('input:checkbox[name="goodsDisplayType"]:input[value="'+goodsDisplayTypeArr[i]+'"]').prop('checked', true);
                            }
                          }
                        }
                      }
    });

    // ------------------------------------------------------------------------
    // 임직원전용여부(C)
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'evEmployeeTp'
      , tagId       : 'evEmployeeTp'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'EV_EMPLOYEE_TP', 'useYn' : 'Y'}
      , async       : true
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
      , success     : function() {
                        // 전체선택
                        $('input[name=evEmployeeTp]:checkbox').prop('checked', true);

                        // ------------------------------------------------------------------------
                        // 조회조건 Set : 시간차로 인해 fnSetSearchInfo 에서 처리가 안되는 경우
                        // ------------------------------------------------------------------------
                        if (fnIsEmpty(gbSearchInfoObj.evEmployeeTp) == false) {
                          let evEmployeeTpArr = (gbSearchInfoObj.evEmployeeTp).split('∀');
                          if (fnIsEmpty(evEmployeeTpArr) == false && evEmployeeTpArr.length > 0) {
                            $("input[name=evEmployeeTp]:checkbox").prop("checked", false);
                            for (var i = 0; i < evEmployeeTpArr.length; i++) {
                              $('input:checkbox[name="evEmployeeTp"]:input[value="'+evEmployeeTpArr[i]+'"]').prop('checked', true);
                            }
                          }
                        }
                      }
    });

    // ------------------------------------------------------------------------
    // 이벤트유형(C)
    // ------------------------------------------------------------------------
    if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
      // 체험단이벤트

      fnTagMkChkBox({
          id        : 'eventTp'
        , tagId     : 'eventTp'
        , data      : [
                        { 'CODE' : 'EVENT_TP.EXPERIENCE' , 'NAME' : '체험단이벤트'    }
                      ]
        , chkVal    : ''
        , style     : {}
      });

      // 체험단이벤트 디폴트 체크 처리
      $('input:checkbox[name="eventTp"]:input[value="EVENT_TP.EXPERIENCE"]').prop('checked', true);

      // 전체선택
      $('input[name=eventTp]:checkbox').prop('checked', true);
    }
    else {
      // 이외 이벤트
      fnTagMkChkBox({
          id          : 'eventTp'
        , tagId       : 'eventTp'
        , url         : '/admin/comn/getCodeList'
        , params      : {'stCommonCodeMasterCode' : 'EVENT_TP', 'useYn' : 'Y'}
        , async       : true
        , isDupUrl    : 'Y'
        , style       : {}
        , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
        , success     : function() {
                          // --------------------------------------------------
                          // 체험단이벤트 VS 그외 이벤트 설정
                          // --------------------------------------------------
                          fnInitEventTp();
                          //전체선택
                          $('input[name=eventTp]:checkbox').prop('checked', true);

                          // ------------------------------------------------------------------------
                          // 조회조건 Set : 시간차로 인해 fnSetSearchInfo 에서 처리가 안되는 경우
                          // ------------------------------------------------------------------------
                          if (gbEventTp != 'EVENT_TP.EXPERIENCE') {
                            if (fnIsEmpty(gbSearchInfoObj.eventTp) == false) {
                              let eventTpArr = (gbSearchInfoObj.eventTp).split('∀');
                              if (fnIsEmpty(eventTpArr) == false && eventTpArr.length > 0) {
                                $("input[name=eventTp]:checkbox").prop("checked", false);
                                for (var i = 0; i < eventTpArr.length; i++) {
                                  $('input:checkbox[name="eventTp"]:input[value="'+eventTpArr[i]+'"]').prop('checked', true);
                                }
                              }
                            }
                          }
                        }
      });
    }

    // ------------------------------------------------------------------------
    // 시작일자
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id          : 'startBeginDt'
      , format      : 'yyyy-MM-dd'
      , btnStartId  : 'startBeginDt'
      , btnEndId    : 'startFinishDt'
      , change      : fnOnChangeStartBeginDt
    });
    fnKendoDatePicker({
        id          : 'startFinishDt'
      , format      : 'yyyy-MM-dd'
      , btnStyle    : true
      , btnStartId  : 'startBeginDt'
      , btnEndId    : 'startFinishDt'
      , change      : fnOnChangeStartFinishDt
    });
    function fnOnChangeStartBeginDt(e) {
      fnOnChangeDatePicker(e, 'start', 'startBeginDt', 'startFinishDt');
    }
    function fnOnChangeStartFinishDt(e) {
      fnOnChangeDatePicker(e, 'end'  , 'startBeginDt', 'startFinishDt');
    }

    // ------------------------------------------------------------------------
    // 종료일자
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id          : 'endBeginDt'
      , format      : 'yyyy-MM-dd'
      , btnStartId  : 'endBeginDt'
      , btnEndId    : 'endFinishDt'
      , change      : fnOnChangeEndBeginDt
    });
    fnKendoDatePicker({
        id          : 'endFinishDt'
      , format      : 'yyyy-MM-dd'
      , btnStyle    : true
      , btnStartId  : 'endBeginDt'
      , btnEndId    : 'endFinishDt'
      , change      : fnOnChangeEndFinishDt
    });
    function fnOnChangeEndBeginDt(e) {
      fnOnChangeDatePicker(e, 'start', 'endBeginDt', 'endFinishDt');
    }
    function fnOnChangeEndFinishDt(e) {
      fnOnChangeDatePicker(e, 'end' , 'endBeginDt', 'endFinishDt');
    }

  }

  // ==========================================================================
  // # 초기화 - 이벤트
  // ==========================================================================
  function fnInitEvent() {

    // ------------------------------------------------------------------------
    // 체크박스변경(전체선택관련)
    // ------------------------------------------------------------------------
    fbCheckboxChange();

    // ------------------------------------------------------------------------
    // 검색기간 초기화 버튼 클릭
    // ------------------------------------------------------------------------
    $('[data-id="fnDateBtnC"]').on('click', function(){
      //$('[data-id="fnDateBtn3"]').mousedown();
      //const $controller = $(this).closest('.date-controller');
      //const $buttons = $controller.find('button');
      //$buttons.each(function() {
      //  $(this).attr("fb-btn-active", false);
      //})
      const $controller = $(this).closest('.date-controller');
      fnClearDateController($controller);
    });

    // ------------------------------------------------------------------------
    // 회원마스터 change event
    // ------------------------------------------------------------------------
    $('#userMaster').unbind('change').on('change', function(){
      fnSetUserGroupFilter();
    });

  }

  // ==========================================================================
  // # 시작일자/종료일자 초기화
  // ==========================================================================
  function fnClearDateController($controller) {
    $controller.each(function() {
      const $buttons = $(this).find('button');
      $buttons.attr('fb-btn-active', false);
    });
  }


//  function fnInitDatePicker(obj) {
//    const $controller = $(obj).closest('.date-controller');
//    const $buttons = $controller.find('button');
//    $buttons.each(function() {
//      $(obj).attr("fb-btn-active", false);
//    })
//  }

  // ==========================================================================
  // # 접근권한 - 체크박스 설정
  // ==========================================================================
  function fnSetUserGroupFilter(){
    // 기존 체크박스 삭제
    $('[id=userGroupFilter]').children().each(function(){
        $(this).remove();
    });

    var beforeDataVal = [{ 'CODE' : 'ALL', 'NAME' : '전체' }];

    if ($('#userMaster').val() == undefined || $('#userMaster').val() == null || $('#userMaster').val() == '') {
      beforeDataVal = null;
    }

    // 복수조건.외부몰 체크박스 그룹 ALL
    fnTagMkChkBox({
        id          : 'userGroupFilter'
      , tagId       : 'userGroupFilter'
      , url         : '/admin/comn/getUserGroupCategoryList'
      , params      : {'urGroupMasterId' : $('#userMaster').data('kendoDropDownList').value()}
      , valueField  : 'urGroupId'
      , textField   : 'groupName'
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : ''
      , style       : {}
      , beforeData  : beforeDataVal
      , success     : function() {
                        // 전체선택
                        $('input[name=userGroupFilter]:checkbox').prop('checked', true);
                      }
    });

    $('input[name=userGroupFilter]').eq(0).prop('checked', true).trigger('change');
  }


  // ==========================================================================
  // # 기본 설정 - 조회전
  // ==========================================================================
  function fnSetDefaultPre() {

    if (fnIsEmpty(gbPageParam.evEventId) == false) {
        // ----------------------------------------------------------------------
        // 검색구분 : 콤보
        // ----------------------------------------------------------------------
        $('#searchSe').data('kendoDropDownList').value("ID");
        // ----------------------------------------------------------------------
        // 검색어 : Text
        // ----------------------------------------------------------------------
        $('#keyWord').val(gbPageParam.evEventId);
    }

    // ------------------------------------------------------------------------
    // 조회조건 처리
    // ------------------------------------------------------------------------
    if (fnIsEmpty(gbSearchInfoObj.isFromDetl) == false && gbSearchInfoObj.isFromDetl == 'Y') {
      // ----------------------------------------------------------------------
      // 상세에서 넘어온 경우 : 조회조건 Set
      // ----------------------------------------------------------------------
      fnSetSearchInfo(gbSearchInfoObj);
    }
    else {
      // ----------------------------------------------------------------------
      // 리스트 신규 진입인 경우 : 조회조건 초기화
      // ----------------------------------------------------------------------
      //console.log('# 신규접속');
      // ----------------------------------------------------------------------
      // 조회조건 초기값 설정 (전체선택)
      // ----------------------------------------------------------------------
      // 몰구분
      $('input[name=mallDiv]:checkbox').prop('checked', true);
      // 진행상태
      $('input[name=statusSe]:checkbox').prop('checked', true);
      // 접근권한설정
      $('#userMaster').data('kendoDropDownList').select(0);
      fnSetUserGroupFilter();
      // 노출범위
      $('input[name=goodsDisplayType]:checkbox').prop('checked', true);
      // 임직원전용
      $('input[name=evEmployeeTp]:checkbox').prop('checked', true);
      // 이벤트유형
      $('input[name=eventTp]:checkbox').prop('checked', true);

      // 시작일자/종료일자
      fnClearDateController($('.date-controller'));
    }

    // ------------------------------------------------------------------------
    // 체험단이벤트 VS 그외 이벤트 설정
    // ------------------------------------------------------------------------
    fnInitEventTp();

  }

  // ==========================================================================
  // # 조회조건 Set
  // ==========================================================================
  function fnSetSearchInfo(searchInfoObj) {

    if (fnIsEmpty(searchInfoObj) == false) {

      // ----------------------------------------------------------------------
      // 검색구분 : 콤보
      // ----------------------------------------------------------------------
      $('#searchSe').data('kendoDropDownList').value(searchInfoObj.searchSe);
      // ----------------------------------------------------------------------
      // 검색어 : Text
      // ----------------------------------------------------------------------
      $('#keyWord').val(searchInfoObj.keyWord);
      // ----------------------------------------------------------------------
      // 전시여부 : 라디오
      // ----------------------------------------------------------------------
      $('input:radio[name="dispYn"]:radio[value="'+searchInfoObj.dispYn+'"]').prop('checked', true);
      // ----------------------------------------------------------------------
      // 몰구분 : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.mallDiv) == false) {
        let mallDivArr = (searchInfoObj.mallDiv).split('∀');
        if (fnIsEmpty(mallDivArr) == false && mallDivArr.length > 0) {
          $("input[name=mallDiv]:checkbox").prop("checked", false);
          for (var i = 0; i < mallDivArr.length; i++) {
            $('input:checkbox[name="mallDiv"]:input[value="'+mallDivArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 사용여부 : 라디오
      // ----------------------------------------------------------------------
      $('input:radio[name="useYn"]:radio[value="'+searchInfoObj.useYn+'"]').prop('checked', true);
      // ----------------------------------------------------------------------
      // 진행상태 : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.statusSe) == false) {
        let statusSeArr = (searchInfoObj.statusSe).split('∀');
        if (fnIsEmpty(statusSeArr) == false && statusSeArr.length > 0) {
          $("input[name=statusSe]:checkbox").prop("checked", false);
          for (var i = 0; i < statusSeArr.length; i++) {
            $('input:checkbox[name="statusSe"]:input[value="'+statusSeArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 접근권한설정(회원등급레벨) : 콤보+체크
      // ----------------------------------------------------------------------
      // 접근권한그룹
      $('#userMaster').data('kendoDropDownList').value(searchInfoObj.userMaster);
      // 회원등급레벨
      fnSetUserGroupFilter();
      $("input[name=userGroupFilter]").eq(0).prop("checked", false).trigger("change");
      if (fnIsEmpty(searchInfoObj.userGroupFilter) == false) {
        let userGroupFilterArr = (searchInfoObj.userGroupFilter).split('∀');
        if (fnIsEmpty(userGroupFilterArr) == false && userGroupFilterArr.length > 0) {
          $("input[name=userGroupFilter]:checkbox").prop("checked", false);
          for (var i = 0; i < userGroupFilterArr.length; i++) {
            $('input:checkbox[name="userGroupFilter"]:input[value="'+userGroupFilterArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 노출범위(디바이스) : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.goodsDisplayType) == false) {
        let goodsDisplayTypeArr = (searchInfoObj.goodsDisplayType).split('∀');
        if (fnIsEmpty(goodsDisplayTypeArr) == false && goodsDisplayTypeArr.length > 0) {
          $("input[name=goodsDisplayType]:checkbox").prop("checked", false);
          for (var i = 0; i < goodsDisplayTypeArr.length; i++) {
            $('input:checkbox[name="goodsDisplayType"]:input[value="'+goodsDisplayTypeArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 임직원 전용 여부 : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.evEmployeeTp) == false) {
        let evEmployeeTpArr = (searchInfoObj.evEmployeeTp).split('∀');
        if (fnIsEmpty(evEmployeeTpArr) == false && evEmployeeTpArr.length > 0) {
          $("input[name=evEmployeeTp]:checkbox").prop("checked", false);
          for (var i = 0; i < evEmployeeTpArr.length; i++) {
            $('input:checkbox[name="evEmployeeTp"]:input[value="'+evEmployeeTpArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 이벤트유형 : 체크
      // ----------------------------------------------------------------------
      if (gbEventTp != 'EVENT_TP.EXPERIENCE') {
        if (fnIsEmpty(searchInfoObj.eventTp) == false) {
          let eventTpArr = (searchInfoObj.eventTp).split('∀');
          if (fnIsEmpty(eventTpArr) == false && eventTpArr.length > 0) {
            $("input[name=eventTp]:checkbox").prop("checked", false);
            for (var i = 0; i < eventTpArr.length; i++) {
              $('input:checkbox[name="eventTp"]:input[value="'+eventTpArr[i]+'"]').prop('checked', true);
            }
          }
        }
      }
      // ----------------------------------------------------------------------
      // 시작일자(From~To)
      // ----------------------------------------------------------------------
      $('#startBeginDt').val(searchInfoObj.startBeginDt);
      $('#startFinishDt').val(searchInfoObj.startFinishDt);
      // ----------------------------------------------------------------------
      // 종료일자(From~To)
      // ----------------------------------------------------------------------
      $('#endBeginDt').val(searchInfoObj.endBeginDt);
      $('#endFinishDt').val(searchInfoObj.endFinishDt);

    }
  }

  // ==========================================================================
  // # 조회
  // ==========================================================================
  function fnSearch() {
    //console.log('# fnSearch Start');

    // ------------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------------
    if ($('#searchSe').val() == 'NAME') {

      var keyword = $('#keyWord').val();

      if (keyword != null && keyword != 'null' && keyword != '') {
        keyword = $.trim(keyword);

        if (keyword.length < 2) {
          fnMessage('', '이벤트명을 두글자 이상 입력하세요.', '');
          return false;
        }
      }
    }

    // 이벤트 리스트 조회
    $('#inputForm').formClear(false);
    var data;
    data = $('#searchForm').formSerialize(true);

    // ------------------------------------------------------------------------
    // 조회조건 Set
    // ------------------------------------------------------------------------
    let searchInfoObj = new Object();
    let searchData = JSON.stringify(fnSearchData(data));
    let searchDataObj = JSON.parse(searchData);
    if (fnIsEmpty(searchDataObj) == false) {
      for (var i = 0; i < searchDataObj.length; i++) {
        let field = searchDataObj[i].field;
        let value = searchDataObj[i].value;
        //console.log('# [', field, '] :: [', value, ']');
        searchInfoObj[field] = value;
      }
      //console.log('# searchInfoObj[1] :: ', JSON.stringify(searchInfoObj));
      searchInfoObj.isFromDetl = '';
    }
    //console.log('# searchInfoObj[2] :: ', JSON.stringify(searchInfoObj));
    sessionStorage.setItem('searchInfo', JSON.stringify(searchInfoObj));

    // ------------------------------------------------------------------------
    // 최종페이지 정보 Set (페이징 기억 관련)
    // ------------------------------------------------------------------------
    var curPage = LAST_PAGE ? LAST_PAGE : 1;

    var query = {
        page          : curPage
      , pageSize      : PAGE_SIZE
      , filterLength  : fnSearchData(data).length
      , filter        : {
                          filters : fnSearchData(data)
                        }
    };
    eventGridDs.query(query);
  }

  // ==========================================================================
  // # 삭제 버튼 클릭
  // ==========================================================================
  function fnBtnEventGridDel(evEventId) {

    gbMode = 'delete';
    delEvEventIdArr = new Array() ;
    delEvEventIdArr.push(evEventId);

    // 삭제 실행
    fnSave();
  }

  // ==========================================================================
  // # 미리보기 버튼 클릭
  // ==========================================================================
  function fnBtnEventGridPre(evEventId) {

    gbMode = 'preview';
    var mallUrl = CUR_SERVER_URL + "/events/";
    var eventPreviewParam = gbEvEventId; // +"&preview=Y"; SPMO-815 내용으로 인해 제외

    switch(gbEventTp) {
        // 일반 이벤트
      case "EVENT_TP.NORMAL" :
        mallUrl += "reply?event="+eventPreviewParam;
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

  // ==========================================================================
  // # 저장처리
  // ==========================================================================
  function fnSave() {

    var url = '';
    var inParam;
    var confirmMsg = '';

    if (gbMode == 'insert') {
      // ----------------------------------------------------------------------
      // 등록
      // ----------------------------------------------------------------------
      confirmMsg = '등록하시겠습니까?';

    }
    else if (gbMode == 'update') {
      // ----------------------------------------------------------------------
      // 수정
      // ----------------------------------------------------------------------

    }
    else if (gbMode == 'delete') {
      // ----------------------------------------------------------------------
      // 삭제
      // ----------------------------------------------------------------------
      url = '/admin/pm/event/delEvent';

      if (delEvEventIdArr == undefined || delEvEventIdArr == null ||
          delEvEventIdArr == '' || delEvEventIdArr.length <= 0) {

        fnMessage('', '삭제대상 이벤트를 확인해주세요.', '');
        return false;
      }

      // ----------------------------------------------------------------------
      // 삭제 Validataion 체크
      // ----------------------------------------------------------------------
      var isCheck = false;

      for (var i = 0; i <delEvEventIdArr.length; i++) {
        isCheck = fnCheckValidationEdit(delEvEventIdArr[i]);
        if (isCheck == false) {
          break;
        }
      }
      //console.log('# isCheck :: ', isCheck);

      if (isCheck == false) {
        return false;
      }

      // ----------------------------------------------------------------------
      // 삭제 실행
      // ----------------------------------------------------------------------
      confirmMsg = '삭제하시겠습니까?';
      //console.log('#>>>>> JSON.stringify(delEvEventIdArr) :: ', JSON.stringify(delEvEventIdArr));

      // ----------------------------------------------------------------------
      // 서비스에서의 처리 사항
      // 1. EventManageRequestDto.java 에 String evEventIdListString 와 List<String> evEventIdListList 선언
      // 2. EventManageController.java 에서 eventManageRequestDto.setEvEventIdList(BindUtil.convertJsonArrayToDtoList(eventManageRequestDto.getEvEventIdListString(), String.class)); 처리
      // ----------------------------------------------------------------------
      inParam = {'evEventIdListString'  : JSON.stringify(delEvEventIdArr)};

      fnKendoMessage({message : confirmMsg, type : 'confirm' , ok : function(){
        fnAjax({
            url     : url
          , params  : inParam
          , success : function( result ){
            fnBizCallback(gbMode, result);
          }
        , isAction : gbMode
        });

      }});

    } // End of else if (gbMode == 'delete')

  }

  // ==========================================================================
  // # 이벤트 삭제 Validation Check
  // ==========================================================================
  function fnCheckValidationEdit(inEvEventId) {

    var isCheck = false;
    // ------------------------------------------------------------------------
    // 상세조회
    // ------------------------------------------------------------------------
    fnAjax({
        url       : '/admin/pm/event/selectEventInfo?evEventId=' + inEvEventId            // 주소줄에서 ID 보기위해 params 사용안함
      , method    : 'POST'
      , isAction  : 'select'
      , async     : false     // 결과에 의해 진행을 멈춰야 하므로 false 처리
      , success   : function(data, status, xhr) {
                      //console.log('# success data   :: ', JSON.stringify(data));
                      //console.log('# success data   :: ', JSON.stringify(data.eventInfo));
                      //console.log('# success status :: ', JSON.stringify(status));
                      //console.log('# success xhr    :: ', JSON.stringify(xhr));
                      // ------------------------------------------------------
                      // 상세조회 결과로 삭제 Validation 체크
                      // ------------------------------------------------------
                      isCheck = fnChecDelkValidationEvent(data.eventInfo);
                    }
      , error     : function(xhr, status, strError) {
                      //console.log('# error xhr      :: ', JSON.stringify(xhr));
                      //console.log('# error status   :: ', JSON.stringify(status));
                      //console.log('# error strError :: ', JSON.stringify(strError));
                      fnMessage('', '이벤트 정보를 확인하세요.', '');
                      return false;
                    }
    });

    return isCheck;
  }

  // ==========================================================================
  // # 이벤트 수정/삭제 Validation Check
  // ==========================================================================
  function fnChecDelkValidationEvent(detail) {

    var useYn     = '';
    var statusSe  = '';

    // ------------------------------------------------------------------------
    // 상세조회 결과 체크 - 이벤트정보, 이벤트유형, 사용여부, 진행상태
    // ------------------------------------------------------------------------
    if (detail == undefined || detail == 'undefined' || detail == null || detail == 'null' || detail == '') {
      fnMessage('', '이벤트 정보가 존재하지 않습니다.', '');
      return false;
    }

    if (detail.eventTp == undefined || detail.eventTp == 'undefined' || detail.eventTp == null || detail.eventTp == 'null' || detail.eventTp == '') {
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

    // ------------------------------------------------------------------------
    // 이벤트유형별 수정/삭제 Validataion Check 호출
    // ------------------------------------------------------------------------
    if (detail.eventTp == 'EVENT_TP.NORMAL') {
      // 일반이벤트
      return fnCheckDelValidationEventNormal(detail);
    }
    else if (detail.eventTp == 'EVENT_TP.SURVEY') {
      // 설문이벤트
      return fnCheckDelValidationEventNormal(detail);
    }
    else if (detail.eventTp == 'EVENT_TP.ATTEND') {
      // 스탬프(출석)
      return fnCheckDelValidationEventNormal(detail);
    }
    else if (detail.eventTp == 'EVENT_TP.MISSION') {
      // 스탬프(미션)
      return fnCheckDelValidationEventNormal(detail);
    }
    else if (detail.eventTp == 'EVENT_TP.PURCHASE') {
      // 스탬프(구매)
      return fnCheckDelValidationEventNormal(detail);
    }
    else if (detail.eventTp == 'EVENT_TP.ROULETTE') {
      // 룰렛이벤트
      return fnCheckDelValidationEventNormal(detail);
    }
    else if (detail.eventTp == 'EVENT_TP.EXPERIENCE') {
      // 체험단이벤트
      return fnCheckDelValidationEventExperience(detail);
    }
    else {
      fnMessage('', '이벤트 유형을 확인하세요.', '');
      return false;
    }

  }

  // ==========================================================================
  // # 이벤트 수선/삭제 Validation Check - 일반/설문/스탬프(출석)/스탬프(미션)/스탬프(구매)/룰렛 공통
  // ==========================================================================
  function fnCheckDelValidationEventNormal(detail) {
    // console.log('# detail :: ', JSON.stringify(detail));
    // ------------------------------------------------------------------------
    // 수선/삭제 - 사용여부
    // ------------------------------------------------------------------------
    if (detail.useYn == 'Y') {

      // ----------------------------------------------------------------------
      // 수선/삭제 - 사용여부(Y) - 진행상태
      // ----------------------------------------------------------------------
      if(detail.statusSe == 'BEF') {
        // 진행예정 : 수정/삭제 가능
      }
      else if(detail.statusSe == 'ING') {
        // 진행중

        if (gbMode == 'update') {
          // 수정 : 가능
        }
        else if (gbMode == 'delete') {
          // 삭제 : 불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          gbMode = '';
          return false;
        }
      }
      else if(detail.statusSe == 'END') {
        // 진행종료 : 수정/삭제 불가
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        gbMode = '';
        return false;
      }
      else {
        fnMessage('', '진행상태를 확인하세요.', '');
        gbMode = '';
        return false;
      }
    }
    else if (detail.useYn == 'N') {

      // ----------------------------------------------------------------------
      // 삭제 - 사용여부(N) - 진행상태
      // ----------------------------------------------------------------------
      if(detail.statusSe == 'BEF') {
        // 진행예정 : 수정/삭제 가능
      }
      else if(detail.statusSe == 'ING') {
        // 진행중

        if (gbMode == 'update') {
          // 수정 : 가능
        }
        else if (gbMode == 'delete') {
          // 삭제 : 불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          gbMode = '';
          return false;
        }
      }
      else if(detail.statusSe == 'END') {
        // 진행종료 : 수정/삭제 불가
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        gbMode = '';
        return false;
      }
      else {
        fnMessage('', '진행상태를 확인하세요.', '');
        return false;
      }
    }
    else {
      fnMessage('', '삭제가 불가한 게시물입니다.', '');
      gbMode = '';
      return false;
    }
  }

  // ==========================================================================
  // # 이벤트 삭제 Validation Check - 체험단
  // ==========================================================================
  function fnCheckDelValidationEventExperience(detail) {
    // ------------------------------------------------------------------------
    // 수선/삭제 - 사용여부
    // ------------------------------------------------------------------------
    if (detail.useYn == 'Y') {

      // ----------------------------------------------------------------------
      // 수선/삭제 - 사용여부(Y) - 진행상태
      // ----------------------------------------------------------------------
      if(detail.statusSe == 'BEF') {
        // 진행예정 : 수정/삭제 가능
      }
      else if(detail.statusSe == 'ING') {
        // 진행중

        if (gbMode == 'update') {
          // 수정 : 가능
        }
        else if (gbMode == 'delete') {
          // 삭제 : 불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          gbMode = '';
          return false;
        }
      }
      else if(detail.statusSe == 'END') {
        // 진행종료 : 수정/삭제 불가
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        gbMode = '';
        return false;
      }
      else {
        fnMessage('', '진행상태를 확인하세요.', '');
        gbMode = '';
        return false;
      }
    }
    else if (detail.useYn == 'N') {

      // ----------------------------------------------------------------------
      // 삭제 - 사용여부(N) - 진행상태
      // ----------------------------------------------------------------------
      if(detail.statusSe == 'BEF') {
        // 진행예정 : 수정/삭제 가능
      }
      else if(detail.statusSe == 'ING') {
        // 진행중

        if (gbMode == 'update') {
          // 수정 : 가능
        }
        else if (gbMode == 'delete') {
          // 삭제 : 불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          gbMode = '';
          return false;
        }
      }
      else if(detail.statusSe == 'END') {
        // 진행종료 : 수정/삭제 불가
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        gbMode = '';
        return false;
      }
      else {
        fnMessage('', '진행상태를 확인하세요.', '');
        gbMode = '';
        return false;
      }
    }
    else {
      fnMessage('', '삭제가 불가한 게시물입니다.', '');
      return false;
    }

  }




  // ------------------------------- Grid Start -------------------------------
  // ==========================================================================
  // # 그리드
  // ==========================================================================
  function fnInitEventGrid(){

    // ------------------------------------------------------------------------
    // 최종 페이지 관련 (페이징 기억 관련)
    // ------------------------------------------------------------------------
    var lastPage = sessionStorage.getItem('lastPage');
    LAST_PAGE = lastPage ? JSON.parse(lastPage) : null;

    // ------------------------------------------------------------------------
    // 가로스크롤
    // ------------------------------------------------------------------------
    var bScrollable = false;
    var bLocked     = false;
    var bLockable   = true;

    // ------------------------------------------------------------------------
    // 그리드 호출
    // ------------------------------------------------------------------------
    eventGridDs = fnGetPagingDataSource({
        url      : '/admin/pm/event/selectEventList'
      //, param    : {evEventCommentCodeId : '9999887'}
      , pageSize : PAGE_SIZE
    });

    eventGridOpt  = {
        dataSource  : eventGridDs
      , pageable    : {
                        pageSizes   : [20, 30, 50]
                      , buttonCount : 10
                      }
      , navigatable : true
      , scrollable  : bScrollable
      , columns     : [
                        {                           title: 'No'           , width: 50 , attributes:{ style:'text-align:center' }
                                                                          , template: '<span class="row-number"></span>'
                                                                          , locked: bLocked, lockable:bLockable
                        }
                      , { field:'eventTpNm'       , title: '이벤트유형'   , width:60 , attributes:{ style:'text-align:center' }, locked: bLocked}
                      , { field:'evEventId'       , title: '이벤트ID'     , width: 50 , attributes:{ style:'text-align:center' }, locked: bLocked}
                      , { field:'mallDivNm'       , title: '몰구분'       , width: 60 , attributes:{ style:'text-align:center' }, locked: bLocked}
                      , { field:'title'           , title: '이벤트명'     , width:200 , attributes:{ style:'text-align:left'   }, locked: bLocked}
                      , { field:'startDt'         , title: '시작일자'     , width:140 , attributes:{ style:'text-align:center' }}
                      , { field:'endDt'           , title: '종료일자'     , width:140 , attributes:{ style:'text-align:center' }}
                      , { field:'device'          , title: '디바이스'     , width:140 , attributes:{ style:'text-align:center' }
                                                                          , template: function(dataItem) {
                                                                                        var rtnStr = '';
                                                                                        var yesCnt = 0;
                                                                                        var sepStr = '';

                                                                                        // PC
                                                                                        if (dataItem.dispWebPcYn == 'Y') {
                                                                                          rtnStr = dataItem.dispWebPcYnNm;
                                                                                          yesCnt++;
                                                                                        }
                                                                                        // Mobile
                                                                                        if (dataItem.dispWebMobileYn == 'Y') {

                                                                                          if (yesCnt > 0) {
                                                                                            sepStr = '/';
                                                                                          }

                                                                                          rtnStr += (sepStr + dataItem.dispWebMobileYnNm);
                                                                                          yesCnt++;
                                                                                        }
                                                                                        // APP
                                                                                        if (dataItem.dispAppYn == 'Y') {

                                                                                          if (yesCnt > 0) {
                                                                                            sepStr = '/';
                                                                                          }

                                                                                          rtnStr += (sepStr + dataItem.dispAppYnNm);
                                                                                        }

                                                                                        if (yesCnt == 3) {
                                                                                          rtnStr = '전체';
                                                                                        }
                                                                                        return  rtnStr;
                                                                                      }
                        }
                      , { field:'evEmployeeTpNm'  , title: '회원유형'     , width: 80 , attributes:{ style:'text-align:center' }}
                      //, { field:'evGroupTpNm'     , title: '회원등급'     , width:120 , attributes:{ style:'text-align:center' }
                      //                                                    , template: function(dataItem) {
                      //                                                                  var userGroupList = dataItem.userGroupList;
                      //                                                                  var result = "";
                      //                                                                  if(userGroupList == null){
                      //                                                                      return result;
                      //                                                                  }
                      //                                                                  for(var userGroup of userGroupList){
                      //                                                                      result += "[" + userGroup.groupMasterName + '-' + userGroup.groupName + "]<BR> ";
                      //                                                                  }
                      //                                                                  return result;
                      //                                                                }
                      //  }
                      //, { field:'evEmployeeTpNm'  , title: '임직원전용'   , width: 60 , attributes:{ style:'text-align:center' }}
                      , { field:'statusNm'        , title: '진행상태'     , width: 60 , attributes:{ style:'text-align:center' }}
                      , { field:'useYn'           , title: '사용여부'     , width: 50 , attributes:{ style:'text-align:center' }
                                                                          , template: function(dataItem) {
                                                                                        if (dataItem.useYn == 'Y') {
                                                                                          return "예";
                                                                                        }
                                                                                        else {
                                                                                          return "아니오";
                                                                                        }
                                                                                      }
                        }
                      , { field:'dispYn'          , title: '목록노출'     , width: 50 , attributes:{ style:'text-align:center' }
                                                                          , template: function(dataItem) {
                                                                                        if (dataItem.dispYn == 'Y') {
                                                                                          return "예";
                                                                                        }
                                                                                        else {
                                                                                          return "아니오";
                                                                                        }
                                                                                      }
                        }
                      , { field:'userNm'          , title: '담당자'       , width: 80 , attributes:{ style:'text-align:center' }}
                      , { field:'management'      , title: '관리'         , width:200 , attributes: {style:'text-align:center;'}
                                                                          , template: function(dataItem) {
                                                                                        var btnStr =  '<div id="pageMgrButtonArea" class="textCenter">';

                                                                                        if(fnIsProgramAuth("SAVE")) {
                                                                                          btnStr += '<button type="button" class="btn-gray btn-s" kind="btnEventGridEdit">수정</button>';
                                                                                        }
                                                                                        if(fnIsProgramAuth("DELETE")) {
                                                                                          btnStr +='&nbsp;'
                                                                                              + '<button type="button" class="btn-red btn-s"  kind="btnEventGridDel" >삭제</button>';
                                                                                        }

                                                                                        btnStr +='&nbsp;'
                                                                                            + '<button type="button" class="btn-white btn-s"  kind="btnEventGridPre" >미리보기</button>';

                                                                                        if(fnIsProgramAuth("VIEW_EVENT_DETAIL")) {
                                                                                          btnStr +='&nbsp;'
                                                                                              + '<button type="button" class="btn-lightgray btn-s"  kind="btnEventGridJoin" >참여정보</button>';
                                                                                        }
                                                                                        btnStr += '</div>';

                                                                                        return  btnStr;
                                                                                      }
                                                                          , lockable: bLockable
                        }
                      ]
    };

    eventGrid = $('#eventGrid').initializeKendoGrid( eventGridOpt ).cKendoGrid();

    //// ------------------------------------------------------------------------
    //// 그리드 클릭 이벤트
    //// ------------------------------------------------------------------------
    //$('#eventGrid').on('click', 'tbody>tr', function () {
    //  fnGridClick();
    //});

    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    eventGrid.bind('dataBound', function() {
      var row_num = eventGridDs._total - ((eventGridDs._page - 1) * eventGridDs._pageSize);
      $('#eventGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      $('#totalCnt').text(eventGridDs._total);

      // ----------------------------------------------------------------------
      // 세션 lastPage 삭제(페이징 기억 관련)
      // ----------------------------------------------------------------------
      if( sessionStorage.getItem('lastPage') ) {
        delete sessionStorage.lastPage;
      }

    });

    // ------------------------------------------------------------------------
    // 수정 버튼 클릭
    // ------------------------------------------------------------------------
    $('#eventGrid').on('click', 'button[kind=btnEventGridEdit]', function(e) {
      e.preventDefault();

      // ----------------------------------------------------------------------
      // 세션 lastPage 삭제(페이징 기억 관련)
      // ----------------------------------------------------------------------
      var curPage = eventGridDs._page;
      sessionStorage.setItem('lastPage', JSON.stringify(curPage));

      let dataItem = eventGrid.dataItem($(e.currentTarget).closest('tr'));

      if (dataItem != null && dataItem != 'null' && dataItem != '') {
        //console.log('# dataItem :: ', JSON.stringify(dataItem));
      }
      // 참고 : 아래의 경우는 버튼 클릭시 값을 가져오지 못함, 로우를 선택해야 함
      //let dataItem2 = eventGrid.dataItem(eventGrid.select());
      //console.log('# btnInventoryEdit.dataItem :: ' + JSON.stringify(dataItem));
      //fnBtnInventoryEdit(dataItem);

      // 수정 버튼 클릭
      fnBtnEventEdit(dataItem);
    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#eventGrid').on('click', 'button[kind=btnEventGridDel]', function(e) {
      e.preventDefault();
      let dataItem = eventGrid.dataItem($(e.currentTarget).closest('tr'));

      //console.log('# 사용여부 :: ', dataItem.useYn);
      //console.log('# 진행구분 :: ', dataItem.statusSe);
      //let param = {};
      //param.dpPageId      = dataItem.dpPageId;
      //param.dpInventoryId = dataItem.dpInventoryId;
      //console.log('# btnInventoryDel.dataItem :: ' + JSON.stringify(dataItem));
      //fnBtnInventoryDel(dataItem.dpInventoryId);
      //fnBtnEventDel(dataItem);    // -> fnEventDel 에서 삭제 처리
      fnBtnEventGridDel(dataItem.evEventId);
    });

    // ------------------------------------------------------------------------
    // 미리보기 버튼 클릭
    // ------------------------------------------------------------------------
    $('#eventGrid').on('click', 'button[kind=btnEventGridPre]', function(e) {

      e.preventDefault();
      let dataItem = eventGrid.dataItem($(e.currentTarget).closest('tr'));
      gbEvEventId = dataItem.evEventId;
      gbEventTp = dataItem.eventTp;
      fnBtnEventGridPre(dataItem.evEventId);
    });


    // ------------------------------------------------------------------------
    // 참여정보 버튼 클릭
    // ------------------------------------------------------------------------
    $('#eventGrid').on('click', 'button[kind=btnEventGridJoin]', function(e) {
      e.preventDefault();

      // ----------------------------------------------------------------------
      // 세션 lastPage 삭제(페이징 기억 관련)
      // ----------------------------------------------------------------------
      var curPage = eventGridDs._page;
      sessionStorage.setItem('lastPage', JSON.stringify(curPage));

      let dataItem = eventGrid.dataItem($(e.currentTarget).closest('tr'));

      if (dataItem != null && dataItem != 'null' && dataItem != '') {
        //console.log('# dataItem :: ', JSON.stringify(dataItem));
      }
      // 참고 : 아래의 경우는 버튼 클릭시 값을 가져오지 못함, 로우를 선택해야 함
      //let dataItem2 = eventGrid.dataItem(eventGrid.select());
      //console.log('# btnInventoryEdit.dataItem :: ' + JSON.stringify(dataItem));
      //fnBtnInventoryEdit(dataItem);

      // 참여정보 버튼 클릭
      fnBtnEventJoin(dataItem);
    });

  }
  // ------------------------------- Grid End ---------------------------------

  // ==========================================================================
  // 신규등록 버튼 클릭
  // ==========================================================================
  function fnEventNew() {

    // ----------------------------------------------------------------------
    // 세션 lastPage 삭제(페이징 기억 관련)
    // ----------------------------------------------------------------------
    var curPage = eventGridDs._page;
    sessionStorage.setItem('lastPage', JSON.stringify(curPage));

    // 링크정보
    let option = {};

    option.url    = '#/eventMgm';
    // 이벤트 등록/수정 : 1090 / 130000110 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    option.menuId = 1090;
    //option.menuId = 130000110;
    option.target = '_blank';
    // 체험단인 경우 이벤트유형 Param Set
    if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
      option.data = { eventTp : gbEventTp };
    }
    //option.data = { mode : 'insert', eventTp : gbEventTp };
    // 화면이동
    fnGoNewPage(option);  // 새페이지(탭)
    //fnGoPage(option);
  }

  // ==========================================================================
  // 수정 버튼 클릭
  // ==========================================================================
  function fnBtnEventEdit(dataItem) {
    // ------------------------------------------------------------------------
    // 수정 화면으로 이동
    // ------------------------------------------------------------------------
    // 링크정보
    let option = {};

    option.url    = '#/eventMgm';
    option.menuId = 1093;
    option.target = '_blank';
    option.data   = { eventTp : dataItem.eventTp, evEventId : dataItem.evEventId, mode : 'update'};
    fnGoNewPage(option);  // 새페이지(탭)

    //option.url    = '#/eventMgm';
    //// 이벤트 등록/수정 : 1093 / 130000112 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    //option.menuId = 1093;
    ////option.menuId = 130000112;
    ////option.target = '_blank';
    //option.target = '_self';
    //option.data = { eventTp : dataItem.eventTp, evEventId : dataItem.evEventId, mode : 'update'};
    //// 화면이동
    //fnGoPage(option);
    // 등록/수정/상세 화면으로 이동(eventMgm)
    // 새탭으로 열기
    //window.open('#/eventMgm?eventTp='+gbEventTp+'&evEventId='+dataItem.evEventId+'&mode=update', '_blank');
    //window.open('#/eventList?eventTp='+gbEventTp+'&evEventId='+dataItem.evEventId+'&mode=update', '_blank');
    //새창으로 열기
    // window.open('#/eventMgm?eventTp='+gbEventTp+'&evEventId='+dataItem.evEventId+'&mode=update', '_blank','width=1200, height=1000, resizable=no, fullscreen=no');
  }

  // ==========================================================================
  // 참여정보 버튼 클릭
  // ==========================================================================
  function fnBtnEventJoin(dataItem) {

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
    option.data = { eventTp : dataItem.eventTp, evEventId : dataItem.evEventId, mode : 'select'};
    // 화면이동
    fnGoNewPage(option);  // 새페이지(탭)

    //fnGoPage(option);
    // 등록/수정/상세 화면으로 이동(eventMgm)
    // 새탭으로 열기
    //window.open('#/eventMgm?eventTp='+gbEventTp+'&evEventId='+dataItem.evEventId+'&mode=update', '_blank');
    //window.open('#/eventList?eventTp='+gbEventTp+'&evEventId='+dataItem.evEventId+'&mode=update', '_blank');
    //새창으로 열기
    // window.open('#/eventMgm?eventTp='+gbEventTp+'&evEventId='+dataItem.evEventId+'&mode=update', '_blank','width=1200, height=1000, resizable=no, fullscreen=no');
  }

  // ==========================================================================
  // # 콜백함수
  // ==========================================================================
  function fnBizCallback( id, data ){

    switch(id){
      case 'insert' :
        // --------------------------------------------------------------------
        // 등록
        // --------------------------------------------------------------------

        break;
      case 'update' :
        // --------------------------------------------------------------------
        // 수정
        // --------------------------------------------------------------------

        break;
      case 'delete' :
        // --------------------------------------------------------------------
        // 삭제
        // --------------------------------------------------------------------
        //alert('삭제 callback Start');
        fnKendoMessage({
            message : '삭제되었습니다.'
          , ok      : function(){
                        // 삭제 후 재조회
                        fnSearch();
                      }
        });

        break;
      case 'confirm':
        fnKendoMessage({
            message : '저장되었습니다.'
          , ok      : function(){
                        fnSearch();
                        fnClose();
                      }
        });
    }
  }

  // ==========================================================================
  // # 팝업창 닫기
  // ==========================================================================
  function fnClose(){
    var kendoWindow =$('#kendoPopup').data('kendoWindow');
    kendoWindow.close();
  }

  // ==========================================================================
  // # ?? 이벤트
  // ==========================================================================
  $('#clear').click(function(){
    $('.resultingarticles').empty();
    $('#searchbox').val('');
  });

  // ==========================================================================
  // # 오류메시지처리
  // ==========================================================================
  function fnMessage(key, nullMsg, ID) {
    fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
      $('#'+ID).focus();
    }});

    return false;
  }

  // ==========================================================================
  // ------------------------------- Html 버튼 바인딩  Start ------------------
  // ==========================================================================
  /** Common Search */
  $scope.fnSearch         = function( )   { fnSearch();         };
  /** Common New */
  $scope.fnBtnEventNew    = function( )   { fnEventNew();  };
  /** Common Clear */
  $scope.fnClear          = function()    { fnClear();          };
  /** Common Confirm */
  $scope.fnConfirm        = function()    { fnConfirm();        };
  /** Common Close */
  $scope.fnClose          = function( )   { fnClose();          };
  /** Common ShowImage */
  $scope.fnShowImage      = function(url) { fnShowImage(url);   };


}); // document ready - END
