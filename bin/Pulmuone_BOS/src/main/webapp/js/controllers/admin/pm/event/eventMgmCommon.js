/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 이벤트 등록/수정 - 소스분리 - OptioinBox 등
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.02.06    dgyoun        최초 생성
 * @
 ******************************************************************************/


// ----------------------------------------------------------------------------
// 코드데이터 중복방지용
// ----------------------------------------------------------------------------
// 설문유형 결과 - fnMakeEventSurveyTp - eventSurveyTp${index}
var gbEventSurveyTpList = new Array();
// 적립금 결과 - fnMakePointList - pmPointId${index}
var gbPmPointIdList     = new Array();  // 콤보 노출 데이터 Array
var gbPmPointIdDataList = new Array();  // 콤보 결과 데이터 Array
// 당첨자혜택 결과 (쿠폰, 적립금, 경품, 응모, 제공안함) - fnMakeEventBenefitTp - eventBenefitTp${index}
var gbEventBenefitTpList = new Array();

//----------------------------------------------------------------------------
//당일일자
//----------------------------------------------------------------------------
var gbTodayDe   = '';   // 당일
var FULL_DATE_FORMAT = 'yyyy-MM-dd';
var date = new Date();
//date.setHours(0,0,0,0);
gbTodayDe = date.oFormat(FULL_DATE_FORMAT);


  // ==========================================================================
  // # 초기화 - 옵션박스
  // ==========================================================================
  function fnInitOptionBox() {
    //console.log('[fnInitOptionBox]');

    // ------------------------------------------------------------------------
    // 팝업
    // ------------------------------------------------------------------------
    $('#kendoPopup').kendoWindow({
      visible: false
      , modal: true
    });

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 기본정보
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ------------------------------------------------------------------------
    // 이벤트유형(C) : [공통]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'eventTp'
      , tagId       : 'eventTp'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'EVENT_TP', 'useYn' :'Y'}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : '' // gbEventTp
      //, blank     : '전시범위 전체'
      , style       : {}
      //, dataBound : function(e) {
      //                console.log('#### aaaaa');
      //                fnSetEventTp(e);
      //              }
      , urlCallback : function(response) {
                        if( response && response.data && response.data.rows ) {
                          fnSetEventTp(response.data.rows);
                        }
                      }
    });
    // 수정화면이면 변경불가
    if (gbMode == 'update') {
      $('#eventTp').data('kendoDropDownList').enable(false);    // 비활성
      //$('#eventTp').data('kendoDropDownList').value(gbEventTp); // 선택
    }

    // ------------------------------------------------------------------------
    // 몰구분(C) : [공통]
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'mallDiv'
      , tagId   : 'mallDiv'
      , url     : '/admin/comn/getCodeList'
      , params  : {'stCommonCodeMasterCode' : 'MALL_DIV', 'useYn' : 'Y'}
      , async   : true
      , isDupUrl: 'Y'
      , chkVal  : 'MALL_DIV.PULMUONE'
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 사용여부(R) : [공통]
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'useYn'
      , tagId   : 'useYn'
      , data    : [
                    { 'CODE' : 'Y'  , 'NAME' : '예'   }
                  , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                  ]
      , chkVal  : 'Y'
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 전시여부(R)  - 공통코드 아님
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'dispYn'
      , tagId   : 'dispYn'
      , data    : [
                    { 'CODE' : 'Y'  , 'NAME' : '예'     }
                  , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                  ]
      , chkVal  : 'Y'
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 노출범위(디바이스)(C) : [공통]
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id        : 'goodsDisplayType'
      , tagId     : 'goodsDisplayType'
      , url       : '/admin/comn/getCodeList'
      , params    : {'stCommonCodeMasterCode' : 'GOODS_DISPLAY_TYPE', 'useYn' : 'Y'}
      , async     : true
      , isDupUrl  : 'Y'
      , style     : {}
      , beforeData: [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
      , success     : function() {
                        // 전체선택
                        $('input[name=goodsDisplayType]:checkbox').prop('checked', true);
                      }
    });

    // ------------------------------------------------------------------------
    // 임직원전용여부(C)
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'evEmployeeTp'
      , tagId   : 'evEmployeeTp'
      , url     : '/admin/comn/getCodeList'
      , params  : {'stCommonCodeMasterCode' : 'EV_EMPLOYEE_TP', 'useYn' : 'Y'}
      , async   : true
      , isDupUrl: 'Y'
      , chkVal  : 'EV_EMPLOYEE_TP.NO_LIMIT'
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 접근권한설정(회원등급)(C) : [공통]
    // ------------------------------------------------------------------------
    // 회원 마스터그룹
    fnKendoDropDownList({
        id          : 'userMaster'
      , tagId       : 'userMaster'
      , url         : '/admin/comn/getUserMasterCategoryList'
      , textField   : 'groupMasterName'
      , valueField  : 'urGroupMasterId'
      , blank       : '회원그룹전체'
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : ''
      //, blank     : '전시범위 전체'
      , style       : {}
    });

    // 회원 등급
    fnKendoDropDownList({
        id          : 'userGroup'
      , tagId       : 'userGroup'
      , url         : '/admin/comn/getUserGroupCategoryList'
      , textField   : 'groupName'
      , valueField  : 'urGroupId'
      , blank       : '회원등급전체'
      , async       : true
      , isDupUrl    : 'Y'
      , cscdId      : 'userMaster'
      , cscdField   : 'urGroupMasterId'
    });

    // 회원마스터그룹/회원등급 사용으로 변경
    //fnTagMkRadio({
    //    id      : 'evGroupTp'
    //  , tagId   : 'evGroupTp'
    //  , url     : '/admin/comn/getCodeList'
    //  , params  : {'stCommonCodeMasterCode' : 'EV_GROUP_TP', 'useYn' : 'Y'}
    //  , async   : true
    //  , isDupUrl: 'Y'
    //  , chkVal  : 'EV_GROUP_TP.NO_LIMIT'
    //  , style   : {}
    //});

    // ------------------------------------------------------------------------
    // 진행기간-시작일자/종료일자 : [공통]
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id      : 'startDe'
      , format  : 'yyyy-MM-dd'
      , change  : fnOnChangeStartDe
      //, defVal  : /gbTodayDe
    });

    fnKendoDatePicker({
        id        : 'endDe'
      , format    : 'yyyy-MM-dd'
      //, btnStyle  : true
      , btnStartId: 'startDe'
      , btnEndId  : 'endDe'
      //, defVal    : '2999-12-31'
      , defType   : 'oneWeek'
      , minusCheck: true
      , nextDate  : true
      , change  : function(e) {
                    // --------------------------------------------------------
                    // 체험단 일자 체크
                    // --------------------------------------------------------
                    if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
                      // 체험단인 경우만 실행
                      // ------------------------------------------------------
                      // 당첨자선정기간 종료일자와 선후 체크
                      // ------------------------------------------------------
                      fnOnChangeDateTimePickerCustom( e, 'start', 'endDe', 'endHour', 'endMin', 'selectEndDe', 'selectEndHour', 'selectEndMin', '진행기간종료일시', '당첨자선정기간종료일시');
                      // ------------------------------------------------------
                      // 지정한 시작일자에 Set
                      // ------------------------------------------------------
                      fnSetStartPickerNextMinute('end', 'selectStart');
                    }
                    // --------------------------------------------------------
                    // 일자 선후체크
                    // --------------------------------------------------------
                    fnOnChangeEndDe(e);
                  }
    });
    fbMakeTimePicker('#startHour', 'start', 'hour', fnOnChangeStartDe);
    fbMakeTimePicker('#startMin' , 'start', 'min' , fnOnChangeStartDe);
    fbMakeTimePicker('#endHour'  , 'end'  , 'hour', fnOnChangeEndDe);
    fbMakeTimePicker('#endMin'   , 'end'  , 'min' , fnOnChangeEndDe);
    // 종료 시/분 기본값 Set
    $('#endHour').data('kendoDropDownList').select(23);
    $('#endMin').data('kendoDropDownList').select(59);
    // 일자 선후 체크
    function fnOnChangeStartDe(e) {
      fnOnChangeDateTimePicker( e, 'start', 'startDe', 'startHour', 'startMin',  'endDe', 'endHour', 'endMin' );
    }
    function fnOnChangeEndDe(e) {
      fnOnChangeDateTimePicker( e, 'end'  , 'startDe', 'startHour', 'startMin',  'endDe', 'endHour', 'endMin' );
    }

    // ------------------------------------------------------------------------
    // 진행기간 종료 시 변경 이벤트
    // ------------------------------------------------------------------------
    $('#endHour').on('change', function (e) {
      console.log('# 진행기간 종료 시 변경 이벤트');

      let endDe = $('#endDe').val();
      // ----------------------------------------------------------------------
      // 지정한 시작일자에 Set
      // ----------------------------------------------------------------------
      if (fnIsEmpty(endDe) == false && endDe.length == 10) {
        fnSetStartPickerNextMinute('end', 'selectStart');
      }
    });

    // ------------------------------------------------------------------------
    // 진행기간 종료 분 변경 이벤트
    // ------------------------------------------------------------------------
    $('#endMin').on('change', function (e) {
      console.log('# 진행기간 종료 분 변경 이벤트');

      let endDe = $('#endDe').val();
      // ----------------------------------------------------------------------
      // 지정한 시작일자에 Set
      // ----------------------------------------------------------------------
      if (fnIsEmpty(endDe) == false && endDe.length == 10) {
        fnSetStartPickerNextMinute('end', 'selectStart');
      }
    });

    // ------------------------------------------------------------------------
    // 사용자동종료(C) : [일반][설문][스탬프(출석)][스탬프(미션)][스탬프(구매)][룰렛]
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id      : 'timeOverCloseYn'
      , tagId   : 'timeOverCloseYn'
      , data    : [
                    { 'CODE' : 'Y'   , 'NAME' : '기간 종료 후 사용 자동 종료'}
                  ]
      , chkVal  : ''
      , style   : {}
    });


    // ------------------------------------------------------------------------
    // 당첨자선정기간-시작일자/종료일자 : [체험단]
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id      : 'selectStartDe'
      , format  : 'yyyy-MM-dd'
      //, defVal  : gbTodayDe
      , change  : function(e) {
                    // --------------------------------------------------------
                    // 일자 선후체크
                    // --------------------------------------------------------
                    //fnOnChangeSelectStartDe(e);
                  }
    });
    fnKendoDatePicker({
        id        : 'selectEndDe'
      , format    : 'yyyy-MM-dd'
      //, btnStyle  : true
      , btnStartId: 'selectStartDe'
      , btnEndId  : 'selectEndDe'
      , change    : function(e) {
                      // ------------------------------------------------------
                      // 당첨자선정기간 종료일자와 선후 체크
                      // ------------------------------------------------------
                      fnOnChangeDateTimePickerCustom( e, 'end', 'endDe', 'endHour', 'endMin', 'selectEndDe', 'selectEndHour', 'selectEndMin', '진행기간종료일시', '당첨자선정기간종료일시');
                      // ------------------------------------------------------
                      // 후기작성기간 종료일자와 선후체크
                      // ------------------------------------------------------
                      fnOnChangeDateTimePickerCustom( e, 'start', 'selectEndDe', 'selectEndHour', 'selectEndMin', 'feedbackEndDe', 'feedbackEndHour', 'feedbackEndMin', '당첨자선정기간종료일시', '후기작성기간종료일시');
                      // ------------------------------------------------------
                      // 지정한 시작일자에 Set
                      // ------------------------------------------------------
                      fnSetStartPickerNextMinute('selectEnd', 'feedbackStart');
                      // ------------------------------------------------------
                      // 일자 선후 체크
                      // ------------------------------------------------------
                      fnOnChangeSelectEndDe(e);
                    }
      //, defVal    : '2999-12-31'
      //, defType   : 'oneWeek'
      , minusCheck: true
      , nextDate  : true
    });
    fbMakeTimePicker('#selectStartHour', 'start', 'hour');
    fbMakeTimePicker('#selectStartMin' , 'start', 'min');
    fbMakeTimePicker('#selectEndHour'  , 'end'  , 'hour');
    fbMakeTimePicker('#selectEndMin'   , 'end'  , 'min');
    // 종료 시/분 기본값 Set
    $('#selectEndHour').data('kendoDropDownList').select(23);
    $('#selectEndMin').data('kendoDropDownList').select(59);
    // 일자 선후 체크
    function fnOnChangeSelectStartDe(e) {
      fnOnChangeDateTimePicker( e, 'start', 'selectStartDe', 'selectStartHour', 'selectStartMin',  'selectEndDe', 'selectEndHour', 'selectEndMin' );
    }
    function fnOnChangeSelectEndDe(e) {
      fnOnChangeDateTimePicker( e, 'end'  , 'selectStartDe', 'selectStartHour', 'selectStartMin',  'selectEndDe', 'selectEndHour', 'selectEndMin' );
    }
    // ------------------------------------------------------------------------
    // 당첨자선정기간 종료 시 변경 이벤트
    // ------------------------------------------------------------------------
    $('#selectEndHour').on('change', function (e) {
      console.log('# 당첨자선정기간 종료 시 변경 이벤트');

      let selectEndDe = $('#selectEndDe').val();
      // ----------------------------------------------------------------------
      // 지정한 시작일자에 Set
      // ----------------------------------------------------------------------
      if (fnIsEmpty(selectEndDe) == false && selectEndDe.length == 10) {
        fnSetStartPickerNextMinute('selectEnd', 'feedbackStart');
      }
    });
    // ------------------------------------------------------------------------
    // 당첨자선정기간 종료 분 변경 이벤트
    // ------------------------------------------------------------------------
    $('#selectEndMin').on('change', function (e) {
      console.log('# 당첨자선정기간 종료 분 변경 이벤트');

      let selectEndDe = $('#selectEndDe').val();
      // ----------------------------------------------------------------------
      // 지정한 시작일자에 Set
      // ----------------------------------------------------------------------
      if (fnIsEmpty(selectEndDe) == false && selectEndDe.length == 10) {
        fnSetStartPickerNextMinute('selectEnd', 'feedbackStart');
      }
    });

    // ------------------------------------------------------------------------
    // 후기작성기간-시작일자/종료일자 : [체험단]
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id      : 'feedbackStartDe'
      , format  : 'yyyy-MM-dd'
      , change  : fnOnChangeFeedbackStartDe
      //, defVal  : gbTodayDe
    });
    fnKendoDatePicker({
        id        : 'feedbackEndDe'
      , format    : 'yyyy-MM-dd'
      //, btnStyle  : true
      , btnStartId: 'feedbackStartDe'
      , btnEndId  : 'feedbackEndDe'
      , change    : function(e) {
                      // ------------------------------------------------------
                      // 후기작성기간 종료일자와 선후체크
                      // ------------------------------------------------------
                      fnOnChangeDateTimePickerCustom( e, 'end', 'selectEndDe', 'selectEndHour', 'selectEndMin', 'feedbackEndDe', 'feedbackEndHour', 'feedbackEndMin', '당첨자선정기간종료일시', '후기작성기간종료일시');
                      // ------------------------------------------------------
                      // 일자선후체크
                      // ------------------------------------------------------
                      fnOnChangeFeedbackEndDe(e);
                    }
      //, defVal    : '2999-12-31'
      //, defType   : 'oneWeek'
      , minusCheck: true
      , nextDate  : true
    });
    fbMakeTimePicker('#feedbackStartHour', 'start', 'hour');
    fbMakeTimePicker('#feedbackStartMin' , 'start', 'min');
    fbMakeTimePicker('#feedbackEndHour'  , 'end'  , 'hour');
    fbMakeTimePicker('#feedbackEndMin'   , 'end'  , 'min');
    // 종료 시/분 기본값 Set
    $('#feedbackEndHour').data('kendoDropDownList').select(23);
    $('#feedbackEndMin').data('kendoDropDownList').select(59);
    // 일자 선후 체크
    function fnOnChangeFeedbackStartDe(e) {
      fnOnChangeDateTimePicker( e, 'start', 'feedbackStartDe', 'feedbackStartHour', 'feedbackStartMin',  'feedbackEndDe', 'feedbackEndHour', 'feedbackEndMin' );
    }
    function fnOnChangeFeedbackEndDe(e) {
      fnOnChangeDateTimePicker( e, 'end'  , 'feedbackStartDe', 'feedbackStartHour', 'feedbackStartMin',  'feedbackEndDe', 'feedbackEndHour', 'feedbackEndMin' );
    }

    // ------------------------------------------------------------------------
    // 사용자동종료(C) : [체험단]
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id      : 'experienceTimeOverCloseYn'
      , tagId   : 'experienceTimeOverCloseYn'
      , data    : [
                    { 'CODE' : 'Y'   , 'NAME' : '기간 종료 후 사용 자동 종료'}
                  ]
      , chkVal  : ''
      , style   : {}
    });


    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 상세정보
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // ------------------------------------------------------------------------
    // 댓글허용여부(R) : [일반]
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'commentYn'
      , tagId   : 'commentYn'
      , data    : [
                    { 'CODE' : 'Y'  , 'NAME' : '예'   }
                  , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                  ]
      , chkVal  : 'Y'
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 임직원참여여부  - [공통]
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'employeeJoinYn'
      , tagId   : 'employeeJoinYn'
      , data    : [
                    { 'CODE' : 'Y'  , 'NAME' : '예'   }
                  , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                  ]
      , chkVal  : 'Y'
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 참여횟수설정유형 : [공통]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id        : 'eventJoinTp'
      , tagId     : 'eventJoinTp'
      , url       : '/admin/comn/getCodeList'
      , params    : {'stCommonCodeMasterCode' : 'EVENT_JOIN_TP', 'useYn' :'Y'}
      , autoBind  : true
      , valueField: 'CODE'
      , textField : 'NAME'
      , async     : true
      , isDupUrl  : 'Y'
      , chkVal    : ''
      , blank     : '선택'
      , style     : {}
    });
    //$('#eventTp').data('kendoDropDownList').value('TODO');


    // ------------------------------------------------------------------------
    // 당첨자설정유형(C) : [공통] - 관리자추첨/즉시당첨/선착순추첨
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'eventDrawTp'
      , tagId   : 'eventDrawTp'
      , url     : '/admin/comn/getCodeList'
      , params  : {'stCommonCodeMasterCode' : 'EVENT_DRAW_TP', 'useYn' : 'Y'}
      , async   : false
      , isDupUrl: 'Y'
      , chkVal  : 'EV_GROUP_TP.NO_LIMIT'
      , style   : {}
      , success : function() {
                    // --------------------------------------------------------
                    // 당첨자설정유형 노출 설정 (관리자추첨/즉시당첨/선착순추첨 중 일부 제거)
                    // --------------------------------------------------------
                    fnSetEventDrawTp(gbEventTp);
                  }
        // 로컬의 comm.tag.min.js > fnTagMkRadio > success 안에 임시로 아래 구현해 놓을 것
        //  if( typeof opt.success  === 'function' ) {
        //    opt.success ();
        //  }
     });

    // ------------------------------------------------------------------------
    // 댓글분류설정여부 : [일반][체험단]
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'commentCodeYn'
      , tagId   : 'commentCodeYn'
      , data    : [
                    { 'CODE' : 'Y'  , 'NAME' : '예'   }
                  , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                  ]
      , chkVal  : 'N'
      , style   : {}
    });


    // ------------------------------------------------------------------------
    // 참여방법 : [일반]
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'normalEventTp'
      , tagId   : 'normalEventTp'
      , url     : '/admin/comn/getCodeList'
      , params  : {'stCommonCodeMasterCode' : 'NORMAL_EVENT_TP', 'useYn' : 'Y'}
      , async   : false
      , isDupUrl: 'Y'
      , chkVal  : 'NORMAL_EVENT_TP.BUTTON'
      , style   : {}
    });


    // ------------------------------------------------------------------------
    // 참여조건 : [일반]
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'joinCondition'
      , tagId   : 'joinCondition'
      , url     : '/admin/comn/getCodeList'
      , params  : {'stCommonCodeMasterCode' : 'JOIN_CONDITION', 'useYn' : 'Y'}
      , async   : false
      , isDupUrl: 'Y'
      , chkVal  : 'JOIN_CONDITION.NO_LIMIT'
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 참여조건 : [일반]
    // ------------------------------------------------------------------------
    fnTagMkRadio({
          id      : 'checkNewUserYn'
        , tagId   : 'checkNewUserYn'
        , data    : [
                { 'CODE' : 'Y'  , 'NAME' : '예'   }
            ,   { 'CODE' : 'N'  , 'NAME' : '아니오' }
        ]
        , chkVal  : 'N'
        , style   : {}
    });

    // ------------------------------------------------------------------------
    // 댓글 분류값 영역 노출/숨김 처리 : fnTagMkRadio이 url 조회가 아니어서 success에 넣지 않음
    // ------------------------------------------------------------------------
    fnEventCommentCodeYn(null);

    // ------------------------------------------------------------------------
    // 당첨자혜택 - 당첨자설정유형(C) : [일반][설문]
    // ------------------------------------------------------------------------
    fnMakeEventBenefitTp('eventBenefitTp', gbEventTp);

    // ------------------------------------------------------------------------
    // 당첨자혜택 - 적립금 : [일반][설문]
    // ------------------------------------------------------------------------
    fnMakePointList('pmPointId');

    // ------------------------------------------------------------------------
    // 당첨자혜택 - 체험단 쿠폰: [체험단]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id        : 'pmCouponCombo'
      , tagId     : 'pmCouponCombo'
      , url       : '/admin/comn/getEventCallCouponInfo'
      //, params    : { 'issueType' : 'PAYMENT_TYPE.AUTO_PAYMENT', 'issueDetailType' : 'AUTO_ISSUE_TYPE.EVENT_AWARD',  'useYn' :'Y'}
      , params    : { 'issueType' : 'PAYMENT_TYPE.AUTO_PAYMENT', 'couponType' : 'COUPON_TYPE.SALEPRICE_APPPOINT', 'issueDetailType' : 'AUTO_ISSUE_TYPE.TESTER_EVENT',  'useYn' :'Y'}
      , autoBind  : true
      , valueField: 'CODE'
      , textField : 'NAME'
      , async     : true
      , isDupUrl  : 'Y'
      , chkVal    : ''
      , blank     : '쿠폰선택'
    });

    // ------------------------------------------------------------------------
    // 스탬프 열구성 : [스탬프(출석)][스탬프(미션)][스탬프(구매)]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id    : 'stampCnt1'
      , tagId : 'stampCnt1'
      , data  : [
                  { CODE: '3', NAME: '3개' }
                , { CODE: '4', NAME: '4개' }
                , { CODE: '5', NAME: '5개' }
                ]
      , chkVal: ''
      , blank : '스탬프 열구성'
    });

    // ------------------------------------------------------------------------
    // 스탬프 총개수 : [스탬프(출석)][스탬프(미션)][스탬프(구매)]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id    : 'stampCnt2'
      , tagId : 'stampCnt2'
      , data  : [
                  { CODE:  1, NAME: '1개' }
                , { CODE:  2, NAME: '2개' }
                , { CODE:  3, NAME: '3개' }
                , { CODE:  4, NAME: '4개' }
                , { CODE:  5, NAME: '5개' }
                , { CODE:  6, NAME: '6개' }
                , { CODE:  7, NAME: '7개' }
                , { CODE:  8, NAME: '8개' }
                , { CODE:  9, NAME: '9개' }
                , { CODE: 10, NAME: '10개' }
                , { CODE: 11, NAME: '11개' }
                , { CODE: 12, NAME: '12개' }
                , { CODE: 13, NAME: '13개' }
                , { CODE: 14, NAME: '14개' }
                , { CODE: 15, NAME: '15개' }
                , { CODE: 16, NAME: '16개' }
                , { CODE: 17, NAME: '17개' }
                , { CODE: 18, NAME: '18개' }
                , { CODE: 19, NAME: '19개' }
                , { CODE: 20, NAME: '20개' }
                , { CODE: 21, NAME: '21개' }
                , { CODE: 22, NAME: '22개' }
                , { CODE: 23, NAME: '23개' }
                , { CODE: 24, NAME: '24개' }
                , { CODE: 25, NAME: '25개' }
                , { CODE: 26, NAME: '26개' }
                , { CODE: 27, NAME: '27개' }
                , { CODE: 28, NAME: '28개' }
                , { CODE: 29, NAME: '29개' }
                , { CODE: 30, NAME: '30개' }
                , { CODE: 31, NAME: '31개' }
                , { CODE: 32, NAME: '32개' }
                , { CODE: 33, NAME: '33개' }
                , { CODE: 34, NAME: '34개' }
                , { CODE: 35, NAME: '35개' }
                , { CODE: 36, NAME: '36개' }
                , { CODE: 37, NAME: '37개' }
                , { CODE: 38, NAME: '38개' }
                , { CODE: 39, NAME: '39개' }
                , { CODE: 40, NAME: '40개' }
                ]
      , chkVal: ''
      , blank : '총 개수 선택'
    });


    // todo 체험단것 위치할 것

    // ------------------------------------------------------------------------
    // 당첨자설정유형(C) - 스탬프계열인 경우 1번 생성
    // ------------------------------------------------------------------------
    // TOTO : 초기 하나 생성하는 부분으로 이동해야 할것 같은데... 확인할 것
    // 스탬프계열 : 첫번째 당첨차유형 동적 생성
    //if (gbEventTp == 'EVENT_TP.ATTEND' || gbEventTp == 'EVENT_TP.MISSION' || gbEventTp == 'EVENT_TP.PURCHASE') {
    //  fnMakeEventBenefitTp('eventBenefitTp1', gbEventTp);
    //}

    // ------------------------------------------------------------------------
    // 룰렉 개수 : [룰렛]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id    : 'rouletteCnt'
      , tagId : 'rouletteCnt'
      , data  : [
                  { CODE: '4', NAME: '4' }
                , { CODE: '6', NAME: '6' }
                , { CODE: '8', NAME: '8' }
                ]
      , chkVal: ''
      , blank : '선택'
    });

    // ------------------------------------------------------------------------
    // 이벤트 제한고객 룰렛설정 개수 : [룰렛]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id    : 'exceptionUserRouletteCnt'
      , tagId : 'exceptionUserRouletteCnt'
      , data  : []        // 룰렛 개수에 따라 설정되므로 초기값 없음
      , chkVal: ''
      , blank : '선택'
    });

    // ------------------------------------------------------------------------
    // 스탬프 지급 조건 유형
    // ------------------------------------------------------------------------
    // 참조 : 테이블 - OD_STATUS.SEARCH_GRP 에 'P' 포함된 것만 조회 함
    //        조회   - orderDetailListController.js > orderCommSearch.js >
    //                 orderOptionUtil > orderStatus 참조
    fnKendoDropDownList({
        id        : 'eventStampOrderTp'
      , tagId     : 'eventStampOrderTp'
      , url       : '/admin/statusMgr/getSearchCode'
      , params    : {'searchGrp' : 'P'}
      , autoBind  : true
      , valueField: 'statusCd'
      , textField : 'statusNm'
      , async     : true
      , isDupUrl  : 'Y'
      , chkVal    : 'IC'
      //, blank     : '선택'
      , style     : {}
    });

    // ------------------------------------------------------------------------
    // 참여조건 주문고객 주문배송유형
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id    : 'goodsDeliveryTp'
        , tagId : 'goodsDeliveryTp'
        , data  : [
            { CODE: 'GOODS_DELIVERY_TYPE.DAWN', NAME: '새벽배송' }
        ]
        , chkVal: ''
        , blank : '주문배송유형 전체'
    });
    // 주문배송유형 전체 코드 노출 시 변경
    // fnKendoDropDownList({
    //     id        : 'goodsDeliveryTp'
    //     , tagId     : 'goodsDeliveryTp'
    //     , url         : '/admin/comn/getCodeList'
    //     , params      : {'stCommonCodeMasterCode' : 'GOODS_DELIVERY_TYPE', 'useYn' :'Y'}
    //     , autoBind    : true
    //     , valueField  : 'CODE'
    //     , textField   : 'NAME'
    //     , async       : true
    //     , isDupUrl    : 'Y'
    //     , chkVal      : '' // gbEventTp
    //     //, blank     : '주문배송유형 전체'
    //     , style       : {}
    // });

    // ------------------------------------------------------------------------
    // 참여조건 주문고객 결제타입
    // ------------------------------------------------------------------------
    // 참조 : 테이블 - OD_STATUS.SEARCH_GRP 에 'P' 포함된 것만 조회 함
    //        조회   - orderDetailListController.js > orderCommSearch.js >
    //                 orderOptionUtil > orderStatus 참조
    fnKendoDropDownList({
        id        : 'normalEventStampOrderTp'
      , tagId     : 'normalEventStampOrderTp'
      , url       : '/admin/statusMgr/getSearchCode'
      , params    : {'searchGrp' : 'P'}
      , autoBind  : true
      , valueField: 'statusCd'
      , textField : 'statusNm'
      , async     : true
      , isDupUrl  : 'Y'
      , chkVal    : 'IC'
      //, blank     : '선택'
      , style     : {}
    });
    //$('#eventStampOrderTp').data('kendoDropDownList').value('TODO');

    // ------------------------------------------------------------------------
    // 파일업로드-배너이미지
    // ------------------------------------------------------------------------
    fnInitKendoUpload(); // 배너 && 에디터 이미지 업로드시 사용할 kendoUpload 컴포넌트 초기화

    // ------------------------------------------------------------------------
    // 기획전상세-PC-에디터
    // ------------------------------------------------------------------------
    fnInitKendoEditor({id : 'detlHtmlPc'});

    // ------------------------------------------------------------------------
    // 기획전상세-Mobile-에디터
    // ------------------------------------------------------------------------
    fnInitKendoEditor({id : 'detlHtmlMo'});

    // ------------------------------------------------------------------------
    // 기획전상세-PC2-에디터
    // ------------------------------------------------------------------------
    fnInitKendoEditor({id : 'detlHtmlPc2'});

    // ------------------------------------------------------------------------
    // 기획전상세-Mobile2-에디터
    // ------------------------------------------------------------------------
    fnInitKendoEditor({id : 'detlHtmlMo2'});

    // ------------------------------------------------------------------------
    // 이벤트 제한고객 룰렛 강제 지정
    // ------------------------------------------------------------------------
    var tooltip = $('#tooltipDiv').kendoTooltip({ // 도움말 toolTip
        filter    : 'span'
      , width     : 250
      , position  : 'center'
      , content   : kendo.template($('#tooltip-template').html())
      , animation : {
                      open  : {
                                effects : 'zoom'
                              , duration : 150
                              }
                    }
    }).data('kendoTooltip');


    // 적용범위 리스트
    fnKendoDropDownList({
      id    : 'goodsCoverageType',
      data  : [{"CODE":"APPLYCOVERAGE.ALL","NAME":'전체'}
          ,{"CODE":"APPLYCOVERAGE.GOODS","NAME":'상품'}
          ,{"CODE":"APPLYCOVERAGE.BRAND","NAME":'전시브랜드'}
          ,{"CODE":"APPLYCOVERAGE.DISPLAY_CATEGORY","NAME":'전시카테고리'}
      ]
    });

      $('#goodsCoverageType').unbind('change').on('change', function(){
          var dropDownList =$('#goodsCoverageType').data('kendoDropDownList');
          var data = dropDownList.value();
          switch(data){

              case "APPLYCOVERAGE.ALL" :
                  $("#goodsIncludeYn").data("kendoDropDownList").enable(false);
                  $("#brandCoverageTypeDiv").hide();
                  $("#categoryCoverageTypeDiv").hide();
                  $("#fnAddCoverageGoods").hide();
                  $("#fnAddCoverageBrand").hide();
                  break;

              case "APPLYCOVERAGE.GOODS" :
                  $("#goodsIncludeYn").data("kendoDropDownList").enable(true);
                  $("#brandCoverageTypeDiv").hide();
                  $("#categoryCoverageTypeDiv").hide();
                  $("#fnAddCoverageGoods").show();
                  $("#fnAddCoverageBrand").hide();
                  break;

              case "APPLYCOVERAGE.BRAND" :
                  $("#goodsIncludeYn").data("kendoDropDownList").enable(true);
                  $("#brandCoverageTypeDiv").show();
                  $("#categoryCoverageTypeDiv").hide();
                  document.getElementById('goodsIncludeYn').style.visibility='visible';
                  $("#fnAddCoverageGoods").hide();
                  $("#fnAddCoverageBrand").show();
                  break;

              case "APPLYCOVERAGE.DISPLAY_CATEGORY" :
                  $("#goodsIncludeYn").data("kendoDropDownList").enable(true);
                  $("#brandCoverageTypeDiv").hide();
                  $("#categoryCoverageTypeDiv").show();
                  $("#fnAddCoverageGoods").hide();
                  $("#fnAddCoverageBrand").hide();

                  $("#ilCtgryStd1").data('kendoDropDownList').value("");
                  $("#ilCtgryStd2").data('kendoDropDownList').value("");
                  $("#ilCtgryStd3").data('kendoDropDownList').value("");
                  $("#ilCtgryStd4").data('kendoDropDownList').value("");
                  break;
          }

      });


      // 조회된 공급업체 ID 로 브랜드 리스트 호출
      fnKendoDropDownList({
          id : 'selectBrandCoverage',
          url : "/admin/ur/brand/searchDisplayBrandList",
          params : {
          },
          textField : "dpBrandName",
          valueField : "dpBrandId",
          blank : "선택해주세요"

      });

      $('#selectBrandCoverage').unbind('change').on('change', function(){
          var shopDropDownList =$('#selectBrandCoverage').data('kendoDropDownList');
          $('#brandName').val(shopDropDownList._oldText);
      });

      // 적용범위 리스트
      fnKendoDropDownList({
          id    : 'goodsCoverageType',
          data  : [{"CODE":"APPLYCOVERAGE.ALL","NAME":'전체'}
              ,{"CODE":"APPLYCOVERAGE.GOODS","NAME":'상품'}
              ,{"CODE":"APPLYCOVERAGE.BRAND","NAME":'전시브랜드'}
              ,{"CODE":"APPLYCOVERAGE.DISPLAY_CATEGORY","NAME":'전시카테고리'}
          ]
      });

      // 적용범위 포함/제외 리스트
      fnKendoDropDownList({
          id    : 'goodsIncludeYn',
          data  : [{"CODE":"Y","NAME":'포함'}
              ,{"CODE":"N","NAME":'제외'}
          ]
      });


      // 적용범위 포함/제외 리스트
      fnKendoDropDownList({
          id    : 'brandIncludeYn',
          data  : [{"CODE":"Y","NAME":'포함'}
              ,{"CODE":"N","NAME":'제외'}
          ]
      });

      // 전시카테고리 시작
      // 전시카테고리 대분류
      fnKendoDropDownList({
          id : "ilCtgryStd1",
          tagId : "ilCtgryStd1",
          url : "/admin/comn/getDropDownCategoryList",
          params : { "depth" : "1", "mallDiv" : "MALL_DIV.PULMUONE","categoryId" : "" },
          textField : "categoryName",
          valueField : "categoryId",
          blank : "대분류",
          async : false
      });

      // 전시카테고리 중분류
      fnKendoDropDownList({
          id : "ilCtgryStd2",
          tagId : "ilCtgryStd2",
          url : "/admin/comn/getDropDownCategoryList",
          textField : "categoryName",
          valueField : "categoryId",
          blank : "중분류",
          async : false,
          cscdId : "ilCtgryStd1",
          cscdField : "categoryId"
      });

      $('#ilCtgryStd1').unbind('change').on('change', function(){
          var ilCtgryStd1DownList =$('#ilCtgryStd1').data('kendoDropDownList');
          ilCtgryStd1 = ilCtgryStd1DownList._oldText;
      });

      $('#ilCtgryStd2').unbind('change').on('change', function(){
          var ilCtgryStd1DownList =$('#ilCtgryStd2').data('kendoDropDownList');
          ilCtgryStd2 = ilCtgryStd1DownList._oldText;
      });

      // 전시카테고리 소분류
      fnKendoDropDownList({
          id : "ilCtgryStd3",
          tagId : "ilCtgryStd3",
          url : "/admin/comn/getDropDownCategoryList",
          textField : "categoryName",
          valueField : "categoryId",
          blank : "소분류",
          async : false,
          cscdId : "ilCtgryStd2",
          cscdField : "categoryId"
      });

      $('#ilCtgryStd3').unbind('change').on('change', function(){
          var ilCtgryStd1DownList =$('#ilCtgryStd3').data('kendoDropDownList');
          ilCtgryStd3 = ilCtgryStd1DownList._oldText;
      });

      // 전시카테고리 세분류
      fnKendoDropDownList({
          id : "ilCtgryStd4",
          tagId : "ilCtgryStd4",
          url : "/admin/comn/getDropDownCategoryList",
          textField : "categoryName",
          valueField : "categoryId",
          blank : "세분류",
          async : false,
          cscdId : "ilCtgryStd3",
          cscdField : "categoryId"
      });

      $('#ilCtgryStd4').unbind('change').on('change', function(){
          var ilCtgryStd1DownList =$('#ilCtgryStd4').data('kendoDropDownList');
          ilCtgryStd4 = ilCtgryStd1DownList._oldText;
      });
      //전시카테고리 끝

      // 적용범위
      $("#goodsCoverageType").data("kendoDropDownList").enable(true);
      $("#goodsIncludeYn").closest(".k-widget").show();
      $("#goodsIncludeYn").data('kendoDropDownList').value("");
      $("#goodsIncludeYn").data("kendoDropDownList").enable(false);
      $("#goodsCoverageDiv").hide();
      $("#categoryCoverageTypeDiv").hide();
      $("#brandCoverageTypeDiv").hide();


  } // End of fnInitOptionBox

  // ==========================================================================
  // # 설문유형 동적생성
  // ==========================================================================
  function fnMakeEventSurveyTp(id, val, status) {
    //console.log('# [fnMakeEventSurveyTp] Start');

    // ------------------------------------------------------------------------
    // 당첨자설정유형(C) - (단일선택/복수선택)
    // ------------------------------------------------------------------------
    if (gbEventSurveyTpList != undefined && gbEventSurveyTpList != null && gbEventSurveyTpList.length > 0) {
      // 기 결과 존재 시
      fnTagMkRadio({
          id      : id
        , tagId   : id
        , data    : gbEventSurveyTpList
        , chkVal  : !!val ? val : 'EVENT_SURVEY_TP.SINGLE'
        , style   : {}
      });
    }
    else {
      // 기 결과 미존재 시
      fnTagMkRadio({
          id      : id
        , tagId   : id
        , url     : '/admin/comn/getCodeList'
        , params  : {'stCommonCodeMasterCode' : 'EVENT_SURVEY_TP', 'useYn' : 'Y'}
        , async   : true
        , isDupUrl: 'Y'
        , chkVal  : !!val ? val : 'EVENT_SURVEY_TP.SINGLE'
        , style   : {}
        , success : function(data) {
                      // ------------------------------------------------------
                      // 결과 재활용 Set
                      // ------------------------------------------------------
                      if (data != undefined && data != null && data != '') {
                        var rows = data.rows;

                        if (rows != undefined && rows != null && rows != '' && rows.length > 0) {
                          gbEventSurveyTpList = new Array();
                          for (var i = 0; i < rows.length; i++) {
                            const dataObje  = {
                                                CODE  : rows[i].CODE
                                              , NAME  : rows[i].NAME
                                              };
                            gbEventSurveyTpList.push(dataObje);
                          }
                        }
                      }
                      //console.log('# @@@@@ gbEventSurveyTpList.length  :: ', gbEventSurveyTpList.length);
                      //console.log('# gbEventSurveyTpList               :: ', JSON.stringify(gbEventSurveyTpList));
                    }
      });
    }
  }

  // ==========================================================================
  // # 당첨자혜택 - 적립금 동적생성
  // ==========================================================================
  function fnMakePointList(id) {
    // ------------------------------------------------------------------------
    // 당첨자혜택 - 적립금
    // ------------------------------------------------------------------------
    if (gbPmPointIdList != undefined && gbPmPointIdList != null && gbPmPointIdList.length > 0) {
      // 기 결과 존재 시
      //console.log('# gbPmPointIdList is Exist :: ', gbPmPointIdList);
      fnKendoDropDownList({
          id    : id
        , tagId : id
        , data  : gbPmPointIdList
        , chkVal: ''
        , blank : '선택'
      });
    }
    else {
      // 기 결과 미존재 시
      fnKendoDropDownList({
          id        : id
        , tagId     : id
        , url       : '/admin/comn/getEventCallPointInfo'
        , params    : { 'pointType' : 'POINT_TYPE.AUTO', 'pointDetailType' : 'POINT_AUTO_ISSUE_TP.EVENT',  'useYn' :'Y'}
        , autoBind  : true
        , valueField: 'CODE'
        , textField : 'NAME'
        , async     : true
        , isDupUrl  : 'Y'
        , chkVal    : ''
        , blank     : '선택'
        , dataBound : function(e) {
                        // ----------------------------------------------------
                        // * fnKendoDropDownList : dataBound, 그외 : success
                        // ----------------------------------------------------
                        // ----------------------------------------------------
                        // 결과 재활용
                        // ----------------------------------------------------
                        const _ds = this.dataSource;
                        const _data = _ds.data();
                        //console.log('# _data.length :: ', _data.length);

                        if (_data != undefined && _data != null && _data.length > 0) {
                          // --------------------------------------------------
                          // JSON 변환
                          // --------------------------------------------------
                          const _clonedData = _data.toJSON().slice();
                          //console.log('# _clonedData :: ', JSON.stringify(_clonedData));
                          // --------------------------------------------------
                          // 콤보 노출 데이터 Array
                          // --------------------------------------------------
                          if (_clonedData != undefined && _clonedData != null && _clonedData.length > 0) {

                            gbPmPointIdList = new Array();

                            for (var i = 0; i < _clonedData.length; i++) {
                              const dataObje  = {
                                                  CODE  : _clonedData[i].CODE
                                                , NAME  : _clonedData[i].NAME
                                                };
                              gbPmPointIdList.push(dataObje);
                            }
                            // ------------------------------------------------
                            // 콤보 결과 데이터 Array
                            // ------------------------------------------------
                            gbPmPointIdDataList = _clonedData;
                            //console.log('# gbPmPointIdDataList :: ', gbPmPointIdDataList);
                            //for (var i = 0; i < gbPmPointIdDataList.length; i++) {
                            //  console.log('# gbPmPointIdDataList[', i, ']       :: ', gbPmPointIdDataList[i]);
                            //  console.log('# gbPmPointIdDataList[', i, '].NAME  :: ', gbPmPointIdDataList[i].NAME);
                            //}
                          }

                        } // End of if (_data != undefined && _data != null && _data.length > 0)

                      } // End of function(e)
      });
    }

  }

  // ==========================================================================
  // # 당첨자혜택 동적생성
  // ==========================================================================
  function fnMakeEventBenefitTp(id, eventTp) {
    //console.log('# fnMakeEventBenefitTp Start [', id, '][', eventTp, ']');
    // ------------------------------------------------------------------------
    // 당첨자설정유형(C) - (쿠폰/적립금/경품/응모/제공안함)
    // ------------------------------------------------------------------------
    if (gbEventBenefitTpList != undefined && gbEventBenefitTpList != null && gbEventBenefitTpList.length > 0) {
      // 기 결과 존재 시
      fnTagMkRadio({
          id      : id
        , tagId   : id
        , data    : gbEventBenefitTpList
        , chkVal  : 'EVENT_BENEFIT_TP.COUPON'
        , style   : {}
      });

      // ----------------------------------------------------------------------
      // 항목제거
      // ----------------------------------------------------------------------
      //if (eventTp == 'EVENT_TP.NORMAL' || eventTp == 'EVENT_TP.SURVEY' || eventTp == 'EVENT_TP.ATTEND') {
      if (eventTp == 'EVENT_TP.NORMAL' || eventTp == 'EVENT_TP.SURVEY') {
        // [일반][설문][스탬프(출석)]
        let $radio = $('#'+id).find('input[value="EVENT_BENEFIT_TP.NONE"]');  // 제공안함
        let $label = $radio.closest('label');
        $label.hide();
      }
      else if (eventTp == 'EVENT_TP.ROULETTE') {
        // [룰렛]
        let $radio = $('#'+id).find('input[value="EVENT_BENEFIT_TP.ENTER"]');  // 응모
        let $label = $radio.closest('label');
        $label.hide();
      } else if (eventTp == 'EVENT_TP.ATTEND') {
        $('#'+id).find('input[value="EVENT_BENEFIT_TP.NONE"]').click();
      }
    }
    else {
      // 기 결과 미존재 시
      fnTagMkRadio({
          id      : id
        , tagId   : id
        , url     : '/admin/comn/getCodeList'
        , params  : {'stCommonCodeMasterCode' : 'EVENT_BENEFIT_TP', 'useYn' : 'Y'}
        , async   : false
        , isDupUrl: 'Y'
        , chkVal  : 'EVENT_BENEFIT_TP.COUPON'
        , style   : {}
        , success : function(data) {
                      // ------------------------------------------------------
                      // 결과 재활용 Set
                      // ------------------------------------------------------
                      if (data != undefined && data != null && data != '') {
                        var rows = data.rows;

                        if (rows != undefined && rows != null && rows != '' && rows.length > 0) {
                          for (var i = 0; i < rows.length; i++) {
                            const dataObje  = {
                                                CODE  : rows[i].CODE
                                              , NAME  : rows[i].NAME
                                              };
                            gbEventBenefitTpList.push(dataObje);
                          }
                        }
                      }
                      //console.log('# @@@@@ gbEventBenefitTpList.length  :: ', gbEventBenefitTpList.length);
                      //console.log('# gbEventBenefitTpList               :: ', JSON.stringify(gbEventBenefitTpList));

                      // ------------------------------------------------------
                      // 항목제거
                      // ------------------------------------------------------
                      //if (eventTp == 'EVENT_TP.NORMAL' || eventTp == 'EVENT_TP.SURVEY' || eventTp == 'EVENT_TP.ATTEND') {
                      if (eventTp == 'EVENT_TP.NORMAL' || eventTp == 'EVENT_TP.SURVEY') {
                        // [일반][설문][스탬프(출석)]
                        let $radio = $('#'+id).find('input[value="EVENT_BENEFIT_TP.NONE"]');  // 제공안함
                        let $label = $radio.closest('label');
                        $label.hide();
                      }
                      else if (eventTp == 'EVENT_TP.ROULETTE') {
                        // [룰렛]
                        let $radio = $('#'+id).find('input[value="EVENT_BENEFIT_TP.ENTER"]');  // 응모
                        let $label = $radio.closest('label');
                        $label.hide();
                      } else if (eventTp == 'EVENT_TP.ATTEND') {
                        $('#'+id).find('input[value="EVENT_BENEFIT_TP.NONE"]').click();
                      }
                    }
      });
    }
  }

  // ==========================================================================
  // # 스탬프(출석) 스탬프번호 콤보 동적생성
  // ==========================================================================
  function fnMakeEventStampNo(id, index, stampTotalCnt) {
    //console.log('# fnMakeEventStampNo Start [', id, '][', index, '][', stampTotalCnt, ']');
    var noList = new Array();
    for (var i = 0; i < stampTotalCnt; i++) {
      const noObje = {
          CODE: i+1+''
        , NAME: i+1+''
      };
      noList.push(noObje);
    }
    // 콤보 생성
    fnKendoDropDownList({
        id      : id
      , tagId   : id
      , data    : noList
      , chkVal  : ''
      , style   : {}
      , blank   : '선택'
    });
    // 초기값 Set
    $('#'+id).data('kendoDropDownList').value(index);

    // ------------------------------------------------------------------------
    // 스탬프번호 변경 이벤트 : 스탬프번호 중복 체크
    // ------------------------------------------------------------------------
    $('#'+id).on('change', function(e) {

      const id  = this.id;
      const val = this.value;
      var   isDup = false;
      //console.log('# this.value :: ', this.value);

      const $stampIdx = $('input[id^="stampIdx"]');
      $stampIdx.each(function() {
        //console.log('# [val][value] :: [', val, '][', this.value, '][', this.id, ']');
        if (id != this.id) {
          if (val != '' && this.value != '') {
            if (val == this.value) {
              isDup = true;
              //console.log('# [val][value] :: [', val, '][', this.value, '][', this.id, ']');
              return false;
            }
          }
        }
      });

      if (isDup == true) {
        fnMessage('', '동일한 이벤트 번호가 있습니다. 다른 번호를 선택하세요.', this.id);
        $('#'+id).data('kendoDropDownList').value('');
        return false;
      }
    });

  }

  // ==========================================================================
  // # 참여횟수 설정
  // ==========================================================================
  function fnInitEventJoinTp(eventTp, eventDrawTp, eventJoinTp) {

    // ------------------------------------------------------------------------
    // 전체목록 만들기
    // ------------------------------------------------------------------------
    //let eventJoinTpArr = $('#eventJoinTp').data('kendoDropDownList').dataSource._data;
    let eventJoinTpArr    = [{'CODE':'EVENT_JOIN_TP.DAY_1','NAME':'일1회'}, {'CODE':'EVENT_JOIN_TP.RANGE_1','NAME':'기간내1회'}, {'CODE':'EVENT_JOIN_TP.NO_LIMIT','NAME':'제한없음'}];
    let delList = [];
    var result = eventJoinTpArr.filter(function(item) {
      return !delList.includes(item.CODE);
    });
    $('#eventJoinTp').data('kendoDropDownList').setDataSource(result);

    // ------------------------------------------------------------------------
    // 이벤트 유형별 구성
    // ------------------------------------------------------------------------
    //console.log('# fnInitEventJoinTp Start [', eventTp, '[', eventDrawTp, ']');
    if (eventTp == 'EVENT_TP.NORMAL')  {
      // ----------------------------------------------------------------------
      // 일반
      // ---------------------------------------------------------------------
      // 즉시당첨 주문고객 참여조건일때 참여횟수 기간내1회로 제한
      var joinConditionVal = $('input[name=joinCondition]:checked').val();
      if (eventDrawTp == 'EVENT_DRAW_TP.AUTO' && joinConditionVal == 'JOIN_CONDITION.ORDER') {
        $('#eventJoinTp').data('kendoDropDownList').value("EVENT_JOIN_TP.RANGE_1"); //참여횟수 기간내1회
        // 비활성
        $('#eventJoinTp').data('kendoDropDownList').enable(false); // kendoDropDownList
      } else {
        $('#eventJoinTp').data('kendoDropDownList').value(""); //참여횟수 기간내1회
        // 비활성
        $('#eventJoinTp').data('kendoDropDownList').enable(true); // kendoDropDownList
      }
    }
    else if (eventTp == 'EVENT_TP.ATTEND')  {
      // ----------------------------------------------------------------------
      // 스탬프(출석)
      // ----------------------------------------------------------------------
      if (fnIsEmpty(eventJoinTp) == true) {
        eventJoinTp = 'EVENT_JOIN_TP.DAY_1';  // 일1회
      }
      $('#eventJoinTp').data('kendoDropDownList').value(eventJoinTp);
      // 비활성
      $('#eventJoinTp').data('kendoDropDownList').enable(false);                    // kendoDropDownList
    }
    else if (eventTp == 'EVENT_TP.MISSION') {
      // ----------------------------------------------------------------------
      // 스탬프(미션) : 일1회/제한없음
      // ----------------------------------------------------------------------
      delList               = ['EVENT_JOIN_TP.RANGE_1'];  // 기간내1회 제외
      //let eventJoinTpArr = $('#eventJoinTp').data('kendoDropDownList').dataSource._data;
      //let eventJoinTpArr    = [{'CODE':'EVENT_JOIN_TP.DAY_1','NAME':'일1회'}, {'CODE':'EVENT_JOIN_TP.RANGE_1','NAME':'기간내1회'}, {'CODE':'EVENT_JOIN_TP.NO_LIMIT','NAME':'제한없음'}];
      var result = eventJoinTpArr.filter(function(item) {
        return !delList.includes(item.CODE);
      });
      $('#eventJoinTp').data('kendoDropDownList').setDataSource(result);
      // 활성
      $('#eventJoinTp').data('kendoDropDownList').enable(true);                    // kendoDropDownList
    }
    else if (eventTp == 'EVENT_TP.PURCHASE') {
      // ----------------------------------------------------------------------
      // 스탬프(구매)
      // ----------------------------------------------------------------------
      if (fnIsEmpty(eventJoinTp) == true) {
        eventJoinTp = 'EVENT_JOIN_TP.RANGE_1';  // 기간내1회
      }
      $('#eventJoinTp').data('kendoDropDownList').value(eventJoinTp);
      // 비활성
      $('#eventJoinTp').data('kendoDropDownList').enable(false);                    // kendoDropDownList
    }
    else if (eventTp == 'EVENT_TP.ROULETTE') {
      // ----------------------------------------------------------------------
      // 룰렛
      // ----------------------------------------------------------------------
      if (fnIsEmpty(eventJoinTp) == true) {
        eventJoinTp = 'EVENT_JOIN_TP.DAY_1';  // 일1회
      }
      $('#eventJoinTp').data('kendoDropDownList').value(eventJoinTp);
      // 비활성
      $('#eventJoinTp').data('kendoDropDownList').enable(false);                    // kendoDropDownList
    }
    else if (eventTp == 'EVENT_TP.EXPERIENCE') {
      // ----------------------------------------------------------------------
      // 체험단
      // ----------------------------------------------------------------------
      if (eventDrawTp == 'EVENT_DRAW_TP.FIRST_COME') {
        // 당첨자설정 : 선착순
        if (fnIsEmpty(eventJoinTp) == true) {
          eventJoinTp = 'EVENT_JOIN_TP.RANGE_1';  // 기간내1회
        }
        $('#eventJoinTp').data('kendoDropDownList').value(eventJoinTp);
        // 비활성
        $('#eventJoinTp').data('kendoDropDownList').enable(false);                  // kendoDropDownList
      }
      else {
        // 당첨자설정 : 선착순 이외
        // 활성
        $('#eventJoinTp').data('kendoDropDownList').enable(true);                   // kendoDropDownList
      }
    }
    else {
      // 활성
      $('#eventJoinTp').data('kendoDropDownList').enable(true);                     // kendoDropDownList
    }
  }


  // ##########################################################################
  // # 파일업로드 Start
  // ##########################################################################
  // ==========================================================================
  // # 파일업로드-업로드시 사용할 kendoUpload 컴포넌트 초기화
  // ==========================================================================
  function fnInitKendoUpload() {
    //console.log('# fnInitKendoUpload Start');
    var uploadFileTagIdList = [ 'imgBannerPc', 'imgBannerMobile', 'imgStampDefault', 'imgStampCheck', 'imgStampBg', 'imgRouletteStartBtn', 'imgRouletteArrow', 'imgRouletteBg', 'imgRoulette'
                              , 'imgSurvey'

                              ];

    var selectFunction = function(e) {

      if (e.files && e.files[0]) {
        // 이미지 파일 선택시
        if (bannerImageUploadMaxLimit < e.files[0].size) { // 배너 이미지 업로드 용량 체크
          fnKendoMessage({
              message : '이미지 업로드 허용 최대 용량은 ' + parseInt(bannerImageUploadMaxLimit / 1024) + ' kb 입니다.'
            , ok : function(e) {}
          });
          return;
        }

        // --------------------------------------------------------------------
        // 확장자 2중 체크 위치
        // --------------------------------------------------------------------
        // var imageExtension = e.files[0]['extension'].toLowerCase();
        // 전역변수에 선언한 허용 확장자와 비교해서 처리
        // itemMgmController.js 의 allowedImageExtensionList 참조

        //  켄도 이미지 업로드 확장자 검사
        if(!validateExtension(e)) {
          fnKendoMessage({
              message : '허용되지 않는 확장자 입니다.'
            , ok : function(e) {}
          });
          return;
        }

        var fileTagId = e.sender.element[0].id;
        let reader = new FileReader();

        reader.onload = function(ele) {
          var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
          var file = e.files[0].rawFile;        // kendoUpload 로 가져온 상품 이미지 file 객체
          //console.log('여기 확인', file);
          fnUploadImage(file, fileTagId);
        };

        reader.readAsDataURL(e.files[0].rawFile);

      } // End of if (e.files && e.files[0])

    } // End of var selectFunction = function(e)

    for (var i = 0; i < uploadFileTagIdList.length; i++) {

      fnKendoUpload({
          id : uploadFileTagIdList[i]
        , select : selectFunction
      });
    } // End of for (var i = 0; i < uploadFileTagIdList.length; i++)

    // ------------------------------------------------------------------------
    // 에디터 파일 업로드 - Editor 의 이미지 첨부 File Tag 를 kendoUpload 로 초기화
    // ------------------------------------------------------------------------
    // 에디터에 파일 업로드 고려 안함(이석호M, 2020.11.03) -> 다시 사용하기로 변경(2021.03.08)
    fnKendoUpload({
        id      : 'uploadImageOfEditor'
      , select  : function(e) {
                    if (e.files && e.files[0]) { // 이미지 파일 선택시
                      // bannerImageUploadMaxLimit : 이미지 업로드 / 첨부 최대 용량 ( 단위 : byte )
                      if (bannerImageUploadMaxLimit < e.files[0].size) { // 용량 체크
                        fnKendoMessage({
                          message : '이미지 업로드 허용 최대 용량은 ' + parseInt(bannerImageUploadMaxLimit / 1048576) + ' MB 입니다.',
                          ok : function(e) {}
                        });
                        return;
                      }
                      let reader = new FileReader();
                      reader.onload = function(ele) {
                        fnUploadImageOfEditor(); // 선택한 이미지 파일 업로드 함수 호출
                      };
                      reader.readAsDataURL(e.files[0].rawFile);
                    }
                  }
    });
  }

  // ==========================================================================
  // # 켄도에디터 파일업로드 처리
  // ==========================================================================
  function fnUploadImageOfEditor(opt) { // Editor 에 첨부된 이미지 Upload 후 반환된 url 을 Editor 내에 img 태그로 추가

    var formData = $('#uploadImageOfEditorForm').formSerialize(true);

    fnAjaxSubmit({
        form        : 'uploadImageOfEditorForm'
      , fileUrl     : '/fileUpload'
      , method      : 'GET'
      , url         : '/comn/getPublicStorageUrl'
      , storageType : 'public'
      , domain      : 'cs'
      , params      : formData
      , success     : function(result) {
                        var uploadResult = result['addFile'][0];
                        var serverSubPath = uploadResult['serverSubPath'];
                        var physicalFileName = uploadResult['physicalFileName'];
                        var imageSrcUrl = result['publicStorageUrl'] + serverSubPath + physicalFileName; // 업로드 이미지 url

                        var editor = $('#' + workindEditorId).data('kendoEditor'); // 이미지 첨부할 Editor
                        editor.exec('inserthtml', {
                          value : '<img src="' + imageSrcUrl + '"/>'
                        });

                      }
      , isAction    : 'insert'
    });
  }

  // ==========================================================================
  // # 파일업로드-validateExtension
  // ==========================================================================
  function validateExtension(e) {

    var allowedExt = '';
    var ext = e.files[0].extension;
    var $el = e.sender.element;

    if( !$el.length ) return;

    if( $el[0].accept && $el[0].accept.length ) {
      // 공백 제거
      allowedExt = $el[0].accept.replace(/\s/g, '');
      allowedExt = allowedExt.split(',');
    } else {
      allowedExt = allowedImageExtensionList;
    }
    return allowedExt.includes(ext.toLowerCase());
  };

  // ==========================================================================
  // # 파일업로드-처리
  // ==========================================================================
  // NOTE 파일 업로드 이벤트
  function fnUploadImage(file, fileTagId) {
    //console.log('# fnUploadImage Start [', file, '][', fileTagId, ']');
    var formData = new FormData();
    formData.append('bannerImage', file);
    formData.append('storageType', 'public'); // storageType 지정
    formData.append('domain', 'dp');          // domain 지정 - 전시
    // 관련 Class : BosStorageInfoEnum, BosDomainPrefixEnum

    $.ajax({
        url         : '/comn/fileUpload'
      , data        : formData
      , type        : 'POST'
      , contentType : false
      , processData : false
      , async       : false
      , success     : function(data) {
                        data = data.data;

                        //console.log('# fnUploadImage sucess data :: ', JSON.stringify(data));
                        var originalFileName  = '';
                        var fullPath          = '';
                        // ----------------------------------------------------
                        // 업로드파일 정보 Set
                        // ----------------------------------------------------
                        originalFileName = data['addFile'][0].originalFileName;
                        fullPath         = data['addFile'][0].serverSubPath + data['addFile'][0].physicalFileName;

                        if (fileTagId == 'imgBannerPc') {
                          //  배너이미지 Pc
                          gbImgBnrPcPath                  = fullPath;               // 풀경로
                          gbImgBnrPcOriginNm              = originalFileName;       // 원본파일명
                        }
                        else if (fileTagId == 'imgBannerMobile') {
                          //  배너이미지 Mobile
                          gbImgBnrMobilePath              = fullPath;               // 풀경로
                          gbImgBnrMobileOriginNm          = originalFileName;       // 원본파일명
                        }
                        else if (fileTagId == 'imgStampDefault') {
                          //  스탬프 기본이미지
                          gbImgStampDefaultPath           = fullPath;               // 풀경로
                          gbImgStampDefaultOriginNm       = originalFileName;       // 원본파일명
                        }
                        else if (fileTagId == 'imgStampCheck') {
                          //  스탬프 체크이미지
                          gbImgStampCheckPath             = fullPath;               // 풀경로
                          gbImgStampCheckOriginNm         = originalFileName;       // 원본파일명
                        }
                        else if (fileTagId == 'imgStampBg') {
                          //  스탬프 배경이미지
                          gbImgStampBgPath                = fullPath;               // 풀경로
                          gbImgStampBgOriginNm            = originalFileName;       // 원본파일명
                        }
                        else if (fileTagId == 'imgRouletteStartBtn') {
                          //  룰렛 시작버튼이미지
                          gbImgRouletteStartBtnPath       = fullPath;               // 풀경로
                          gbImgRouletteStartBtnOriginNm   = originalFileName;       // 원본파일명
                        }
                        else if (fileTagId == 'imgRouletteArrow') {
                          //  룰렛 화살표이미지
                          gbImgRouletteArrowPath          = fullPath;               // 풀경로
                          gbImgRouletteArrowOriginNm      = originalFileName;       // 원본파일명
                        }
                        else if (fileTagId == 'imgRouletteBg') {
                          //  룰렛 배경이미지
                          gbImgRouletteBgPath             = fullPath;               // 풀경로
                          gbImgRouletteBgOriginNm         = originalFileName;       // 원본파일명
                        }
                        else if (fileTagId == 'imgRoulette') {
                          //  룰렛 이미지
                          gbImgRoulettePath               = fullPath;               // 풀경로
                          gbImgRouletteOriginNm           = originalFileName;       // 원본파일명
                        }

                        // ----------------------------------------------------
                        // 업로드파일 노출
                        // ----------------------------------------------------
                        var imageUrl = publicStorageUrl + data['addFile'][0].serverSubPath + data['addFile'][0].physicalFileName;
                        $('#'+ fileTagId +'View').attr('src', imageUrl);
                        $('#'+ fileTagId +'View').closest('.fileUpload__imgWrapper').show();

                        // ----------------------------------------------------
                        // 업로드파일 제목 노출
                        // ----------------------------------------------------
                        var $title = $('#'+ fileTagId +'View').closest('.fileUpload-container').find('.fileUpload__title');
                        var $message = $('#'+ fileTagId +'View').closest('.fileUpload-container').find('.fileUpload__message');
                        $title.text(originalFileName);
                        $message.hide();
                        $title.show();
                      }
    });
  }

  // ==========================================================================
  // # 파일업로드-처리 : Kendo 미사용(다건, No id)
  // ==========================================================================
  // NOTE 파일 업로드 이벤트
  function fnUploadImageNoId(file, fileEl) {
    //console.log('# fnUploadImageNoId Start [', gbImageType, '][', fileEl, ']');
    //console.log('# fnUploadImageNoId Start [', file, '][', fileEl, ']');

    var formData = new FormData();
    formData.append('bannerImage', file);
    formData.append('storageType', 'public'); // storageType 지정
    formData.append('domain', 'dp');          // domain 지정 - 전시
    // 관련 Class : BosStorageInfoEnum, BosDomainPrefixEnum

    $.ajax({
        url         : '/comn/fileUpload'
      , data        : formData
      , type        : 'POST'
      , contentType : false
      , processData : false
      , async       : false
      , success     : function(data) {
                        data = data.data;
                        //console.log('# fnUploadImage sucess data :: ', JSON.stringify(data));

                        let originalFileName  = '';
                        let fullPath          = '';
                        let $fileEl               = $(fileEl);

                        // ----------------------------------------------------
                        // 업로드파일 정보 Set
                        // ----------------------------------------------------
                        originalFileName = data['addFile'][0].originalFileName;
                        fullPath         = data['addFile'][0].serverSubPath + data['addFile'][0].physicalFileName;
                        //console.log('# originalFileName :: ', originalFileName);
                        //console.log('# fullPath         :: ', fullPath);

                        // ----------------------------------------------------
                        // 업로드파일 노출
                        // ----------------------------------------------------
                        let imageUrl = publicStorageUrl + data['addFile'][0].serverSubPath + data['addFile'][0].physicalFileName;
                        let $img = $fileEl.closest('.fileUpload-container').find('.imgView');

                        // 사이즈 축소
                        $img.closest('.fileUpload__img').css({
                          height: 100 +'px',
                        });

                        $img.attr('src', imageUrl);
                        $img.closest('.fileUpload__imgWrapper').show();

                        // ----------------------------------------------------
                        // 업로드파일 제목 노출
                        // ----------------------------------------------------
                        let $title = $fileEl.closest('.fileUpload-container').find('.fileUpload__title');
                        let $message = $fileEl.closest('.fileUpload-container').find('.fileUpload__message');
                        $title.text(originalFileName);
                        $message.hide();
                        $title.show();

                        // ----------------------------------------------------
                        // 파일업로드 정보 저장
                        // ----------------------------------------------------
                        let imgMap = new Map();
                        imgMap.set('Path'     , fullPath);
                        imgMap.set('OriginNm' , originalFileName);

                        if (gbImageType.startsWith('imgSurvey')) {
                          // --------------------------------------------------
                          // 설문 아이템 이미지
                          // --------------------------------------------------
                          //console.log('# gbImgSurveyMap.size(bef) :: ', gbImgSurveyMap.size);
                          let imageTypeArr = gbImageType.split('imgSurvey');
                          let indexArr = imageTypeArr[1].split('_');
                          let index    = indexArr[0];
                          let subIndex = indexArr[1];
                          //console.log('# [index][subIndex] :: [', index, '][', subIndex, ']');

                          if (gbImgSurveyMap.get(index+'') == undefined || gbImgSurveyMap.get(index+'') == null) {
                            // ------------------------------------------------
                            // 설문이미지그룹 인덱스 미존재
                            // ------------------------------------------------
                            // subIndex
                            let imgSurveyMap = new Map();
                            imgSurveyMap.set(subIndex+'', imgMap);
                            // index
                            gbImgSurveyMap.set(index+'', imgSurveyMap);
                          }
                          else {
                            // ------------------------------------------------
                            // 설문인덱스그룹 존재
                            // ------------------------------------------------
                            gbImgSurveyMap.get(index+'').set(subIndex+'', imgMap);
                            //let imgSurveyMap = gbImgSurveyMap.get(index+'').get();
                            //imgSurveyMap.set(subIndex+'', imgMap);
                          }
                          // --------------------------------------------------
                          // 임시 로그 Start
                          //let surveyImgTotalCnt = 0;
                          //if (gbImgSurveyMap != undefined && gbImgSurveyMap != null && gbImgSurveyMap.size > 0) {
                          //  console.log('# 설문이미지그룹 개수 :: ', gbImgSurveyMap.size);
                          //  for (let [key, vlaue] of gbImgSurveyMap) {
                          //    if (vlaue != undefined && vlaue != null && vlaue.size > 0) {
                          //      console.log('# [', key, ']별 아이템이미지 개수 :: ', vlaue.size);
                          //      for (let [subKey, subValue] of vlaue) {
                          //        console.log('# imgInfo[', key, '][', subKey, '] :: ', JSON.stringify(Array.from(subValue)));
                          //        surveyImgTotalCnt++;
                          //      }
                          //    }
                          //  }
                          //}
                          //console.log('# surveyImgTotalCnt :: ', surveyImgTotalCnt);
                          // 임시 로그 End
                          // --------------------------------------------------
                        }
                        if (gbImageType.startsWith('imgStampDefault')) {
                          // --------------------------------------------------
                          // 스탬프 기본 이미지
                          // --------------------------------------------------
                          let imageTypeArr = gbImageType.split('imgStampDefault');
                          gbImgStampDefaultMap.set(imageTypeArr[1]+'', imgMap);
                          //console.log('# gbImgStampDefaultMap(After) :: ', gbImgStampDefaultMap);
                        }
                        else if (gbImageType.startsWith('imgStampCheck')) {
                          // --------------------------------------------------
                          // 스탬프 체크 이미지
                          // --------------------------------------------------
                          let imageTypeArr = gbImageType.split('imgStampCheck');
                          gbImgStampCheckMap.set(imageTypeArr[1]+'', imgMap);
                        }
                        else if (gbImageType.startsWith('imgStampIcon')) {
                          // --------------------------------------------------
                          // 스탬프 아이콘 이미지
                          // --------------------------------------------------
                          let imageTypeArr = gbImageType.split('imgStampIcon');
                          gbImgStampIconMap.set(imageTypeArr[1]+'', imgMap);
                        }

                        // 파일구분자 초기화
                        gbImageType = '';
                      }
    });
  }

  // ==========================================================================
  // # 동적 파일 업로드업로드 핸들러 : Kendo 미사용 : [설문][스탬프(출석)][스탬프(미션)][스탬프(구매)]
  // ==========================================================================
  function fileSelectFunction(e) {
    //console.log('# fileSelectFunction Start');
    //console.log('# e :: ', e);

    const self = e.target;
    const _files = this.files;

    if (this.files && this.files[0]) {
      // 이미지 파일 선택시
      if (bannerImageUploadMaxLimit < this.files[0].size) { // 배너 이미지 업로드 용량 체크
        fnKendoMessage({
            message : '이미지 업로드 허용 최대 용량은 ' + parseInt(bannerImageUploadMaxLimit / 1024) + ' kb 입니다.'
          , ok : function(e) {}
        });
        return;
      }

      var isValidate = checkFileExt(self, _files);
      //console.log('isValidate', isValidate);

      if(!isValidate) {
        fnKendoMessage({
            message : '허용되지 않는 확장자 입니다.'
          , ok : function(e) {}
        });
        return;
      }

      let reader = new FileReader();

      reader.onload = function(ele) {
        var itemImageScr = ele.target.result; // 이미지 url
        var file = _files[0];                 // 이미지 file 객체
        fnUploadImageNoId(file, self);
      };

      reader.readAsDataURL(this.files[0]);

    } // End of if (this.files && this.files[0])

  }

  // ==========================================================================
  // # 파일업로드 체크
  // ==========================================================================
  function checkFileExt(el, files) {

    // ------------------------------------------------------------------------
    // 1. 확장자
    // ------------------------------------------------------------------------
    let accepts = el.accept; //".jpg .png .jpeg"

    if (!accepts) return true;

    // ------------------------------------------------------------------------
    // 2. .제거
    // ------------------------------------------------------------------------
    accepts = accepts.split(',').map(function (ext) {
        return ext.replace('.', '').trim();
    }) //['jpg', 'png', 'jpeg']

    // ------------------------------------------------------------------------
    // 3.
    // ------------------------------------------------------------------------
    var ext = files[0].name.match(/\.([^\.]+)$/)[1]; //png, jpg..

    // ------------------------------------------------------------------------
    // 4.
    // ------------------------------------------------------------------------
    if (!accepts.includes(ext)) return false;

    //5.
    return true;
  }
  // # 파일업로드 End
  // ##########################################################################

  // ##########################################################################
  // # 에디터 Start
  // ##########################################################################
  // ==========================================================================
  // # 에디터-초기화
  // ==========================================================================
  function fnInitKendoEditor(opt) {

    if  ( $('#' + opt.id).data('kendoEditor') ) { // 기존에 이미 Editor 로 생성되어 있는 경우 초기화

      $('#' + opt.id + 'Div').html('');  // 해당 editor TextArea 를 가지고 있는 td 내의 html 을 강제 초기화
      var textAreaHtml = '<textarea class="comm-textarea" id="' + opt.id + '" name="' + opt.id + '" style="height:100px;"></textarea>';
      $('#' + opt.id + 'Div').append(textAreaHtml);  // 새로운 editor TextArea 를 추가
    }

    $('#' + opt.id).kendoEditor({
        tools : [ { name : 'viewHtml'           , tooltip : 'HTML 소스보기'   }
                , { name : 'bold'               , tooltip : '진하게'          }
                , { name : 'italic'             , tooltip : '이탤릭'          }
                , { name : 'underline'          , tooltip : '밑줄'            }
                , { name : 'strikethrough'      , tooltip : '취소선'          }
                  //------------------- 구분선 ----------------------
                , { name : 'foreColor'          , tooltip : '글자색상'        }
                , { name : 'backColor'          , tooltip : '배경색상'        }
                  //------------------- 구분선 ----------------------
                , { name : 'justifyLeft'        , tooltip : '왼쪽 정렬'       }
                , { name : 'justifyCenter'      , tooltip : '가운데 정렬'     }
                , { name : 'justifyRight'       , tooltip : '오른쪽 정렬'     }
                , { name : 'justifyFull'        , tooltip : '양쪽 맞춤'       }
                  //------------------- 구분선 ----------------------
                , { name : 'insertUnorderedList', tooltip : '글머리기호'      }
                , { name : 'insertOrderedList'  , tooltip : '번호매기기'      }
                , { name : 'indent'             , tooltip : '들여쓰기'        }
                , { name : 'outdent'            , tooltip : '내어쓰기'        }
                  //------------------- 구분선 ----------------------
                , { name : 'createLink'         , tooltip : '하이퍼링크 연결' }
                , { name : 'unlink'             , tooltip : '하이퍼링크 제거' }
                //, { name : 'insertImage'        , tooltip : '이미지 URL 첨부' }  // 삭제해도 된다 함(2021.03.03 홍진영L)
                , { name : 'file-image'         , tooltip : '이미지 파일 첨부'
                                                , exec :  function(e) {
                                                            e.preventDefault();
                                                            workindEditorId = opt.id; // 이미지 첨부할 Editor 의 id 를 전역변수에 저장
                                                            $('#uploadImageOfEditor').trigger('click'); // 파일 input Tag 클릭 이벤트 호출
                                                          }
                  }
                  //------------------- 구분선 ----------------------
                , { name : 'subscript'          , tooltip : '아래 첨자'       }
                , { name : 'superscript'        , tooltip : '위 첨자'         }
                  //------------------- 구분선 ----------------------
                , { name : 'tableWizard'    , tooltip : '표 수정' }
                , { name : 'createTable'        , tooltip : '표 만들기'       }
                , { name : 'addRowAbove'        , tooltip : '위 행 추가'      }
                , { name : 'addRowBelow'        , tooltip : '아래 행 추가'    }
                , { name : 'addColumnLeft'      , tooltip : '왼쪽 열 추가'    }
                , { name : 'addColumnRight'     , tooltip : '오른쪽 열 추가'  }
                , { name : 'deleteRow'          , tooltip : '행 삭제'         }
                , { name : 'deleteColumn'       , tooltip : '열 삭제'         }
                , { name : 'mergeCellsHorizontally'   , tooltip : '수평으로 셀 병합' }
                , { name : 'mergeCellsVertically'   , tooltip : '수직으로 셀 병합' }
                , { name : 'splitCellHorizontally'   , tooltip : '수평으로 셀 분할' }
                , { name : 'splitCellVertically'   , tooltip : '수직으로 셀 분할' }
                  //------------------- 구분선 ----------------------
                , 'formatting'
                , 'fontName'

                , { name  : 'fontSize'
                  , items : [ { text :  '8px', value :  '8px' }
                            , { text :  '9px', value :  '9px' }
                            , { text : '10px', value : '10px' }
                            , { text : '11px', value : '11px' }
                            , { text : '12px', value : '12px' }
                            , { text : '13px', value : '13px' }
                            , { text : '14px', value : '14px' }
                            , { text : '16px', value : '16px' }
                            , { text : '18px', value : '18px' }
                            , { text : '20px', value : '20px' }
                            , { text : '22px', value : '22px' }
                            , { text : '24px', value : '24px' }
                            , { text : '26px', value : '26px' }
                            , { text : '28px', value : '28px' }
                            , { text : '36px', value : '36px' }
                            , { text : '48px', value : '48px' }
                            , { text : '72px', value : '72px' }
                            ]
                  }
                ]
              , messages  : { formatting          : '포맷'
                            , formatBlock         : '포맷을 선택하세요.'
                            , fontNameInherit     : '폰트'
                            , fontName            : '글자 폰트를 선택하세요.'
                            , fontSizeInherit     : '글자크기'
                            , fontSize            : '글자 크기를 선택하세요.'
                            , print               : '출력'
                            , imageWebAddress     : '웹 주소'
                            , imageAltText        : '대체 문구'
                            , fileWebAddress      : '웹 주소'
                            , fileTitle           : '링크 문구'
                            , linkWebAddress      : '웹 주소'
                            , linkText            : '선택 문구'
                            , linkToolTip         : '풍선 도움말'
                            , linkOpenInNewWindow : '새 창에서 열기'
                            , dialogInsert        : '적용'
                            , dialogUpdate        : 'Update'
                            , dialogCancel        : '닫기'
                            }
              , serialization : {
                  // 켄도 에디터 내 스크립트 허용 HGRM-8031
                  scripts: true,
                  // 켄도 에디터 줄 바꿈 <br />태그로 변경 금지 HGRM-8112
                  custom: function(html) {
                      return html.replace(/\n/gi, "");
                  },
                }
              , encoded: false,
    });

    $('<br/>').insertAfter($('.k-i-create-table').closest('li'));
  }
  // # 에디터 End
  // ##########################################################################

  // ==========================================================================
  // # 입직원참여여부 처리
  // ==========================================================================
  function fnEventEvEmployeeTp() {
    let evEmployeeTp  = $('input[name=evEmployeeTp]:checked').val();
    if (evEmployeeTp == 'EV_EMPLOYEE_TP.EMPLOYEE_ONLY') {
    
      // 임직원전용 선택 시
      // 임직원참여여부  'Y' Set
      $('input:radio[name="employeeJoinYn"]:radio[value="Y"]').prop('checked',true);
      // 임직원참여여부 비활성
      $('input[name=employeeJoinYn]').attr('disabled', true);
    }
    else if (evEmployeeTp == 'EV_EMPLOYEE_TP.EMPLOYEE_EXCEPT') {
      // 임직원제외 선택 시
      // 임직원참여여부  'N' Set
      $('input:radio[name="employeeJoinYn"]:radio[value="N"]').prop('checked',true);
      // 임직원참여여부 비활성

      $('input[name=employeeJoinYn]').attr('disabled', true);
    }
    else {
      // 임직원참여여부 활활성
      $('input[name=employeeJoinYn]').attr('disabled', false);
    }
  }

  // ==========================================================================
  // # 이벤트유형 노출 설정
  // ==========================================================================
  function fnSetEventTp(data) {

    //console.log('# fnSetEventTp Start [', gbEventTp, ']');
    var delList;
    var experienceDellList    = ['EVENT_TP.NORMAL', 'EVENT_TP.SURVEY', 'EVENT_TP.ATTEND', 'EVENT_TP.MISSION', 'EVENT_TP.PURCHASE', 'EVENT_TP.ROULETTE'];
    var nonExperienceDelList  = ['EVENT_TP.EXPERIENCE'];

    if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
      delList = experienceDellList;
    }
    else {
      delList = nonExperienceDelList;
    }

    var result = data.filter(function(item) {
      return !delList.includes(item.CODE);
    });

    $('#eventTp').data('kendoDropDownList').setDataSource(result);
  }
  // --------------------------------------------------------------------------
  // * dataBound 사용인 경우
  // --------------------------------------------------------------------------
  //function fnSetEventTp(e) {
  //
  //  console.log('# fnSetEventTp Start [', gbEventTp, ']');
  //  var delList;
  //  var experienceDellList    = ['EVENT_TP.NORMAL', 'EVENT_TP.SURVEY', 'EVENT_TP.ATTEND', 'EVENT_TP.MISSION', 'EVENT_TP.PURCHASE', 'EVENT_TP.ROULETTE'];
  //  var nonExperienceDelList  = ['EVENT_TP.EXPERIENCE'];
  //
  //  if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
  //    delList = experienceDellList;
  //  }
  //  else {
  //    delList = nonExperienceDelList;
  //  }
  //
  //  var eventTpSelect = $('#eventTp').data('kendoDropDownList');
  //  var originData    = eventTpSelect.dataSource.data();
  //  var dataLength    = originData.length;
  //  //console.log('# dataLength :: ', dataLength);
  //  for (var i = 0; i < dataLength; i++) {
  //    var item = originData[i];
  //
  //    if (item != undefined && item != null) {
  //      //console.log('# [', i, '] :: ', JSON.stringify(item));
  //      //console.log('# item.value :: ', item.value);
  //
  //      if (delList != undefined && delList != null && delList.length > 0) {
  //        for (var j = 0; j < delList.length; j++) {
  //          if (item.CODE == delList[j]){ // Here 'value' is 'dataValueField' field
  //            eventTpSelect.dataSource.remove(item);
  //            break;
  //          }
  //        }
  //      }
  //    }
  //  }
  //}


  // ==========================================================================
  // # 당첨자설정유형 노출 설정
  // ==========================================================================
  function fnSetEventDrawTp(eventTp) {
    //console.log('# fnSetEventDrawTp Start [', eventTp, ']');

    // ------------------------------------------------------------------------
    // 전체 노출
    // ------------------------------------------------------------------------
    $('#eventDrawTp label').show();
    //console.log('# 변경전 체크값(1) :: ', $('input[name=eventDrawTp]:checked').val());

    // ------------------------------------------------------------------------
    // 당첨자설정유형 노출 설정
    // ------------------------------------------------------------------------
    // 이벤트유형별 항목제거 및 기본값설정
    if (eventTp == 'EVENT_TP.NORMAL') {
      // ----------------------------------------------------------------------
      // * 일반 : 관리자 추첨/즉시 당첨
      // ----------------------------------------------------------------------
      // 항목제거
      let $radio = $('#eventDrawTp').find('input[value="EVENT_DRAW_TP.FIRST_COME"]');
      let $label = $radio.closest('label');
      $label.hide();
      // 기본값
      $('input:radio[name="eventDrawTp"]:radio[value="EVENT_DRAW_TP.ADMIN"]').prop('checked',true);
    }
    else if (eventTp == 'EVENT_TP.SURVEY') {
      // ----------------------------------------------------------------------
      // * 설문 : 관리자 추첨/즉시 당첨
      // ----------------------------------------------------------------------
      // 항목제거
      let $radio = $('#eventDrawTp').find('input[value="EVENT_DRAW_TP.FIRST_COME"]');
      let $label = $radio.closest('label');
      $label.hide();
      // 기본값
      $('input:radio[name="eventDrawTp"]:radio[value="EVENT_DRAW_TP.ADMIN"]').prop('checked',true);
    }
    else if (eventTp == 'EVENT_TP.ATTEND') {
      // ----------------------------------------------------------------------
      // * 스탬프(출석) : 즉시당첨
      // ----------------------------------------------------------------------
      // 항목제거
      let $radio = $('#eventDrawTp').find('input[value="EVENT_DRAW_TP.ADMIN"]');
      let $label = $radio.closest('label');
      $label.hide();
      $radio = $('#eventDrawTp').find('input[value="EVENT_DRAW_TP.FIRST_COME"]');
      $label = $radio.closest('label');
      $label.hide();
      // 기본값
      $('input:radio[name="eventDrawTp"]:radio[value="EVENT_DRAW_TP.AUTO"]').prop('checked',true);
      // 당첨혜택 테이블에 클래스 추가
      $('#stampBenefitCommonDiv').addClass('stampBenefitDiv--attend');
    }
    else if (eventTp == 'EVENT_TP.MISSION') {
      // ----------------------------------------------------------------------
      // * 스탬프(미션) : 즉시당첨
      // ----------------------------------------------------------------------
      // 항목제거
      let $radio = $('#eventDrawTp').find('input[value="EVENT_DRAW_TP.ADMIN"]');
      let $label = $radio.closest('label');
      $label.hide();
      $radio = $('#eventDrawTp').find('input[value="EVENT_DRAW_TP.FIRST_COME"]');
      $label = $radio.closest('label');
      $label.hide();
      // 기본값
      $('input:radio[name="eventDrawTp"]:radio[value="EVENT_DRAW_TP.AUTO"]').prop('checked',true);
      // 당첨혜택 테이블에 클래스 추가
      $('#stampBenefitCommonDiv').addClass('stampBenefitDiv--mission');
    }
    else if (eventTp == 'EVENT_TP.PURCHASE') {
      // ----------------------------------------------------------------------
      // * 스탬프(구매) : 관리자추첨
      // ----------------------------------------------------------------------
      // 항목제거
      let $radio = $('#eventDrawTp').find('input[value="EVENT_DRAW_TP.AUTO"]');
      let $label = $radio.closest('label');
      $label.hide();
      $radio = $('#eventDrawTp').find('input[value="EVENT_DRAW_TP.FIRST_COME"]');
      $label = $radio.closest('label');
      $label.hide();
      // 기본값
      $('input:radio[name="eventDrawTp"]:radio[value="EVENT_DRAW_TP.ADMIN"]').prop('checked',true);
      // 당첨혜택 테이블에 클래스 추가
      $('#stampBenefitCommonDiv').addClass('stampBenefitDiv--purchase');
    }
    else if (eventTp == 'EVENT_TP.ROULETTE') {
      // ----------------------------------------------------------------------
      // * 룰렛 : 즉시당첨
      // ----------------------------------------------------------------------
      // 항목제거
      let $radio = $('#eventDrawTp').find('input[value="EVENT_DRAW_TP.ADMIN"]');
      let $label = $radio.closest('label');
      $label.hide();
      $radio = $('#eventDrawTp').find('input[value="EVENT_DRAW_TP.FIRST_COME"]');
      $label = $radio.closest('label');
      $label.hide();
      // 기본값
      $('input:radio[name="eventDrawTp"]:radio[value="EVENT_DRAW_TP.AUTO"]').prop('checked',true);
      $('#immediatleyWinRouletteDiv').show();
      $('#immediatleyWinDiv').hide();
    }
    else if (eventTp == 'EVENT_TP.EXPERIENCE') {
      // ----------------------------------------------------------------------
      // * 체험단 : 관리자 추첨/즉시당첨
      // ----------------------------------------------------------------------
      // 항목제거
      let $radio = $('#eventDrawTp').find('input[value="EVENT_DRAW_TP.AUTO"]');
      let $label = $radio.closest('label');
      $label.hide();
      // 기본값
      $('input:radio[name="eventDrawTp"]:radio[value="EVENT_DRAW_TP.ADMIN"]').prop('checked',true);
    }
    else {
      // 기타
      $('input:radio[name="eventDrawTp"]:radio[value="EVENT_DRAW_TP.ADMIN"]').prop('checked',true);
    }

  }

  // ==========================================================================
  // # 적립금선택 - 적립금콤보
  // ==========================================================================
  function fnAddPoint(inGridId, index, inTagId) {
    //console.log('# fnAddPoint Start [', inGridId, '],[', index, '],[', inTagId, ']');

    var pmPointId  = $('#'+inTagId).data('kendoDropDownList').value();
    var pmPointNm  = $('#'+inTagId).data('kendoDropDownList').text();
    //console.log('# pmPointId :: ', pmPointId);
    //console.log('# pmPointNm :: ', pmPointNm);

    var bIsDupGoodsId;
    var gridObj;
    var addIdx = 0;

    // ------------------------------------------------------------------------
    // 1. 선택 체크
    // ------------------------------------------------------------------------
    if (fnIsEmpty(pmPointId) == true || pmPointId == '0') {
      fnMessage('', '적립금을 선택해주세요.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // 2. 현재 그리드 초기화 - 1건만 등록되어야 하므로 초기화
    // ------------------------------------------------------------------------
    if ($('#'+inGridId).data('kendoGrid') != undefined && $('#'+inGridId).data('kendoGrid') != null) {
      $('#'+inGridId).data('kendoGrid').destroy();
      $('#'+inGridId).empty();
    }
    if (inGridId == 'benefitPointGrid') {
        if(gbEventTp == "EVENT_TP.SURVEY"){
            fnInitBenefitPointGrid(true);
        }else{
            fnInitBenefitPointGrid();
        }

    }
    else {
      fnInitBenefitPointDynamicGrid(inGridId, index, gbEvEventId);
    }



    // ------------------------------------------------------------------------
    // 3.1. 적립금 추가 정보 조회 (콤보 조회 시 적립금추가정보 Set 되어있음
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // DataSource
    // ------------------------------------------------------------------------
    var targetGrid    = $('#'+inGridId).data('kendoGrid');
    var targetGridDs  = targetGrid.dataSource;
    var targetGridArr = targetGridDs.data();

    var discountTpNm        = '';
    var discountDetl        = '';
    var erpOrganizationCode = '';
    var urErpOrganizationNm = '';

    if (gbPmPointIdDataList != undefined && gbPmPointIdDataList != null && gbPmPointIdDataList.length > 0) {
      for (var i = 0; i < gbPmPointIdDataList.length; i++) {
        if (pmPointId == gbPmPointIdDataList[i].CODE) {
          //console.log('# 포인트정보 :: ', JSON.stringify(gbPmPointIdDataList[i]));
          discountTpNm        = gbPmPointIdDataList[i].pointTypeName;       // 적립구분명
          discountDetl        = gbPmPointIdDataList[i].issueValue;          // 적립금
          erpOrganizationCode = gbPmPointIdDataList[i].erpOrganizationCode; // 분담조직코드
          urErpOrganizationNm = gbPmPointIdDataList[i].erpOrganizationName; // 분담조직명
          break;
        }
      }
    }

    // ------------------------------------------------------------------------
    // 3.2. 적립금정보 Set
    // ------------------------------------------------------------------------
    gridObj = new Object();
    gridObj.pmPointId             = pmPointId;            // 적립금ID
    gridObj.displayPointNm        = pmPointNm;            // 적립금명
    gridObj.discountTpNm          = discountTpNm;         // 적립구분명
    gridObj.discountDetl          = discountDetl;         // 적립금
    gridObj.erpOrganizationCode   = erpOrganizationCode;  // 분담조직코드
    gridObj.urErpOrganizationNm   = urErpOrganizationNm;  // 분담조직명
    // 1건만 가능하므로 0 Set
    targetGridDs.insert(0, gridObj);

    // ------------------------------------------------------------------------
    // 3.3. 적립금 콤보 초기값 Set
    // ------------------------------------------------------------------------
    $('#'+inTagId).data('kendoDropDownList').value('');
  }

  // ==========================================================================
  // # 쿠폰선택 - 쿠폰콤보
  // ==========================================================================
  function fnAddCoupon() {

    var pmCouponId = $('#pmCouponCombo').data('kendoDropDownList').value();
    var pmCouponNm = $('#pmCouponCombo').data('kendoDropDownList').text();

    $('#pmCouponId').val(pmCouponId);
    $('#pmCouponNm').text(pmCouponNm);
    $("#pmCouponCombo").data("kendoDropDownList").value('');
  }

  // ==========================================================================
  // # 쿠폰조회-팝업오픈
  // ==========================================================================
  function fnAddCouponPopup(inGridId) {
    //console.log('# fnAddCouponPopup Start [', inGridId, ']');
    var params = {};
    //params.selectType = 'single';                                 // 그리드 체크박스 타입 ( single : 하나만 선택됨, multi : 여러개 선택됨 )
    //params.goodsType = 'GOODS_TYPE.NORMAL, GOODS_TYPE.DISPOSAL';  // 상품유형(복수 검색시 , 로 구분)
    //params.supplierId = viewModel.ilGoodsDetail.urSupplierId;
    //params.warehouseId = viewModel.ilGoodsDetail.urWarehouseId;
    params.isPopup                    = true;                                 // 팝업여부
    params.callType                   = 'EVENT';                              // 호출유형 : 이벤트
    params.searchIssueType            = 'PAYMENT_TYPE.AUTO_PAYMENT';          // 발급방법 : 자동발급
    params.searchIssueTypeFixYn       = true;                                 // 발급방법 : 고정
    params.searchApprovalStatus       = 'APPR_STAT.APPROVED';                 // 승인/발급상태 : 발급
    params.searchApprovalStatusFixYn  = true;                                 // 승인/발급상태 : 고정

    fnKendoPopup({
        id          : 'couponSearchPopup'
      , title       : '쿠폰조회'
      //, width       : '1200px'
      , height      : '800px'
      , scrollable  : 'yes'
      , src         : '#/cpnMgm'
      , param       : params
      , success     : function( id, data ) {

                        var selectedCnt = data.length;
                        //console.log('# selectedCnt :: ', selectedCnt);
                        //console.log('# data :: ', JSON.stringify(data));

                        // ----------------------------------------------------
                        // 메인의 당첨자혜택 : [일반][설문]
                        // ----------------------------------------------------
                        var bIsDupGoodsId;
                        var gridObj;
                        var addIdx = 0;

                        var dupArr = new Array();
                        var dupObj;
                        var dupCnt = 0;

                        // ----------------------------------------------------
                        // 1. 1개 선택 체크
                        // ----------------------------------------------------
                        //if (data.length > 1) {
                        //
                        //  fnMessage('', '체험단 상품은 1개만 선택 가능합니다.', '');
                        //  return false;
                        //}

                        // ----------------------------------------------------
                        // 2. 현재 그리드 초기화
                        // ----------------------------------------------------
                        //fnInitBenefitCouponGrid();

                        // ----------------------------------------------------
                        // 4. 상품 등록
                        // ----------------------------------------------------
                        if (data.length > 0) {

                          // --------------------------------------------------
                          // DataSource
                          // --------------------------------------------------
                          var targetGrid    = $('#'+inGridId).data('kendoGrid');
                          var targetGridDs  = targetGrid.dataSource;
                          var targetGridArr = targetGridDs.data();
                          //console.log('# targetGridArr.length   :: ', targetGridArr.length);
                          if (targetGridArr.length > 0) {
                            //console.log('# targetGridArr :: ', JSON.stringify(targetGridArr));
                          }

                          for (var i = 0;  i < data.length; i++) {

                            bIsDupGoodsId = false;

                            //console.log('# add dataItem[', i, '] :: ', JSON.stringify(data[i]));
                            // ------------------------------------------------
                            // 중복체크
                            // ------------------------------------------------
                            for (var t = 0; t < targetGridArr.length; t++) {

                              var targetPmCouponId  = targetGridArr[t].pmCouponId+'';
                              var addPmCouponId     = data[i].pmCouponId+'';

                              if (targetPmCouponId == addPmCouponId) {
                                bIsDupGoodsId = true;
                              }

                              //console.log('# [', i, '][', t, ']:[target][add][isDup] :: [',targetPmCouponId, '][', targetPmCouponId, '][', bIsDupGoodsId, ']');
                            }
                            // 중복이 존재하면 continue
                            if (bIsDupGoodsId == true) {

                              dupObj = new Object();
                              dupObj.pmCouponId       = data[i].pmCouponId;           // 쿠폰PK
                              dupObj.displayCouponNm  = data[i].displayCouponName;    // 전시쿠폰명
                              dupObj.bosCouponNm      = data[i].bosCouponName;        // BOS쿠폰명
                              dupArr.push(dupObj);
                              dupCnt++;
                              continue;
                            }

                            // ------------------------------------------------
                            // 쿠폰추가
                            // ------------------------------------------------
                            //console.log('# add Start [', i, '][', t, ']');
                            gridObj = new Object();
                            gridObj.pmCouponId            = data[i].pmCouponId;               // 전시쿠폰명
                            gridObj.displayCouponNm       = data[i].displayCouponName;        // 전시쿠폰명
                            gridObj.bosCouponNm           = data[i].bosCouponName;            // BOS쿠폰명
                            gridObj.discountTpNm          = data[i].discountTypeName;         // 할인방식
                            gridObj.discountDetl          = data[i].discountValue;            // 할인상세
                            gridObj.validityConts         = data[i].validityDate;             // 유효기간
                            gridObj.couponCnt             = 1;                                // 지급수량(기본값:1)
                            gridObj.erpOrganizationCode   = data[i].erpOrganizationCode;      // 분담조직코드
                            gridObj.urErpOrganizationNm   = data[i].erpOrganizationName;      // 분담조직
                            gridObj.validityType          = data[i].validityTp;             // 유효기간타입 ( 기간설정, 유효일 )
                            gridObj.issueDt               = data[i].issueDate;                  // 발급기간

                            targetGridDs.insert(addIdx, gridObj);
                            addIdx++;

                            // ------------------------------------------------
                            // 정렬 - 1건이라 정렬 불필요
                            // ------------------------------------------------
                            //targetGridDs.sort({
                            //    field : 'goodsSort'
                            //  , dir   : 'asc'
                            //});

                          } // End of for (var i = 0;  i < data.length; i++)

                          // --------------------------------------------------
                          // 중복 메시지 작성
                          // --------------------------------------------------
                          //console.log('# dupCnt :: ', dupCnt);
                          var dupCouponInfoStr = '';
                          var addedCnt = selectedCnt - dupCnt;

                          //console.log('# selectedCnt :: ', selectedCnt);
                          //console.log('# dupCnt      :: ', dupCnt);
                          //console.log('# addedCnt    :: ', addedCnt);

                          if (addedCnt <= 0) {
                            // 등록건 없는 경우
                            fnMessage('', '중복건을 제외한 추가된 쿠폰이 없습니다.', '');
                          }
                          else {
                            // 등록건 존재하는 경우
                            if (dupCnt > 0) {
                              var msg = '';

                              if (addedCnt > 0) {
                                msg = '<div>중복건을 제외하고 '+addedCnt+'건의 쿠폰이 추가되었습니다.</div>';
                              }
                              else {
                                msg = '<div>추가된 쿠폰이 없습니다.</div>';
                              }

                              if (dupArr != undefined && dupArr != null && dupArr.length > 0) {
                                for (var i = 0; i < dupArr.length; i++) {
                                  dupCouponInfoStr += '<div>['+dupArr[i].pmCouponId+'] '+dupArr[i].displayCouponNm+'</div>';
                                }
                              }
                              var dupMsg = msg
                                         + '<div>중복건 : '+dupCnt+'건</div>'
                                         + '<div>&nbsp;</div>'
                                         + dupCouponInfoStr;
                              fnMessage('', dupMsg, '');
                            }
                            else {
                              fnMessage('', addedCnt + '건의 쿠폰이 추가되었습니다.', '');
                            }
                          }
                        } // End of if (data.length > 0)

                      } // End of function( id, data )
    });
  }

  // ==========================================================================
  // # 상품조회-팝업오픈
  // ==========================================================================
  function fnBtnAddGoodsExperiencePopup(inGridId) {

    var params = {};
    params.selectType = 'single';                                 // 그리드 체크박스 타입 ( single : 하나만 선택됨, multi : 여러개 선택됨 )

    // 이전요건
    //params.goodsType = 'GOODS_TYPE.NORMAL, GOODS_TYPE.DISPOSAL';  // 상품유형(복수 검색시 , 로 구분)
    ////params.supplierId = viewModel.ilGoodsDetail.urSupplierId;
    ////params.warehouseId = viewModel.ilGoodsDetail.urWarehouseId;

    // 일반기획전 상품그룹별 상품조회와 동일한 팝업 호출 (2021.03.13 적용)
    //params.goodsCallType                      = 'exhibitNormal';                            // 조회조건 : 일반기획전
    //params.saleStatus                         = 'SALE_STATUS.ON_SALE';                      // 판매상태:판매중
    //params.columnSalePriceHidden              = false;                                      // 판매가
    //params.columnGoodsDisplyYnHidden          = false;                                      // 전시상태
    // ------------------------------------------------------------------------
    // 체험상품조회 조회 조건 변경에 따른 호출 파라메터 변경 (2021.03.30, HGRM-6953)
    //  - 상품 유형 : 일반, 폐기임박 만 노출
    //  - 증정품이 등록된 상품은 조회되지 않아야 함
    // ------------------------------------------------------------------------
    params.goodsCallType                      = 'experienceEvent';                          // 조회조건 : 체험단이벤트
    params.saleStatus                         = 'SALE_STATUS.ON_SALE';                      // 판매상태:판매중
    params.columnSalePriceHidden              = false;                                      // 판매가
    params.columnGoodsDisplyYnHidden          = false;                                      // 전시상태

    fnKendoPopup({
        id          : 'goodsSearchPopup'
      , title       : '체험상품조회'
      , width       : '1700px'
      , height      : '800px'
      , scrollable  : 'yes'
      , src         : '#/goodsSearchPopup'
      , param       : params
      , success     : function( id, data ) {
                        //console.log('# 상품결과 :: ', JSON.stringify(data));

                        if (inGridId == 'experienceGrid') {
                          // --------------------------------------------------
                          // 체험상품
                          // --------------------------------------------------
                          var bIsDupGoodsId;
                          var gridObj;
                          var addIdx = 0;
                          //console.log('# 상품추가 data,length :: ', data.length);
                          //console.log('# 상품추가 data :: ', JSON.stringify(data));

                          // --------------------------------------------------
                          // 팝업 취소, 닫기 등
                          // --------------------------------------------------
                          if (data == undefined || data == null || data.length == undefined) {
                            //fnMessage('', '상품이 선택되지 않았습니다.', '');
                            return false;
                          }

                          // --------------------------------------------------
                          // 1. 1개 선택 체크
                          // --------------------------------------------------
                          if (data.length > 1) {

                            fnMessage('', '체험단 상품은 1개만 선택 가능합니다.', '');
                            return false;
                          }

                          // --------------------------------------------------
                          // 2. 현재 그리드 초기화 - 1건만 등록되어야 하므로 초기화
                          // --------------------------------------------------
                          fnInitExperienceGrid();

                          // --------------------------------------------------
                          // 3. 상품 등록 - for문이지만 1건만 들어옴
                          // --------------------------------------------------
                          if (data.length > 0) {

                            // ------------------------------------------------
                            // DataSource
                            // ------------------------------------------------
                            var targetGrid    = $('#'+inGridId).data('kendoGrid');
                            var targetGridDs  = targetGrid.dataSource;
                            var targetGridArr = targetGridDs.data();

                            for (var i = 0;  i < data.length; i++) {

                              bIsDupGoodsId = false;

                              gridObj = new Object();
                              gridObj.goodsTpNm             = data[i].goodsTypeName;            // 상품유형명
                              gridObj.ilGoodsId             = data[i].goodsId;                  // 상품코드
                              gridObj.goodsNm               = data[i].goodsName;                // 상품명
                              gridObj.goodsImagePath        = data[i].itemImagePath;            // 상품이미지경로
                              gridObj.warehouseNm           = data[i].warehouseName;            // 출고처명
                              gridObj.standardPrice         = data[i].standardPrice + '';       // 원가
                              gridObj.recommendedPrice      = data[i].recommendedPrice + '';    // 정상가
                              gridObj.salePrice             = data[i].salePrice + '';           // 판매가
                              gridObj.shippingTemplateName  = data[i].name;                     // 배송정책명
                              targetGridDs.insert(addIdx, gridObj);
                              addIdx++;

                              // ------------------------------------------------
                              // 정렬 - 1건이라 정렬 불필요
                              // ------------------------------------------------
                              //targetGridDs.sort({
                              //    field : 'goodsSort'
                              //  , dir   : 'asc'
                              //});

                            } // End of for (var i = 0;  i < data.length; i++)

                        } // End of

                        } // End of if (inGridId == 'experienceGrid')

                      } // End of function( id, data )
    });
  }

  // ==========================================================================
  // # 이미지노출
  // ==========================================================================
  function fnSetImgView(path, originNm, viewId) {

    // 배너이미지
    if (fnIsEmpty(path) == false) {
      // 이미지
      $('#'+viewId).closest('.fileUpload__imgWrapper').show();
      $('#'+viewId).attr('src', publicStorageUrl + path);
      // 이미지명
      var imgFileNm = '';
      var arrStr = (path).split('/');
      if (arrStr != null && arrStr.length > 0) {
        imgFileNm = arrStr[arrStr.length - 1];
      }
      var $container = $('#'+viewId).closest('.fileUpload-container');
      $container.find('.fileUpload__title').show().text(imgFileNm);
      $container.find('.fileUpload__message').hide();
    }
    else {
      $('#'+viewId).attr('src', '');
    }
  }

  // ==========================================================================
  // # 오류메시지처리
  // ==========================================================================
  function fnMessage(key, nullMsg, ID, option) {
    fnKendoMessage({
        message : fnGetLangData({ key : key, nullMsg : nullMsg})
      , ok      : function() {
                    if (fnIsEmpty(ID) == false) {
                      if (option == 'kendoDropDownList') {
                        // kendoDropDownList 이면
                        $('#'+ID).data('kendoDropDownList').focus();
                      }
                      else if (option == 'kendoEditor') {
                        // kendoEditor 이면
                        $('#'+ID).data('kendoEditor').focus();
                      }
                      else {
                        $('#'+ID).focus();
                      }
                    }
                  }
    });
  }



  // ==========================================================================
  // 1분후 날짜 Set
  // ==========================================================================
  function fnSetStartPickerNextMinute(thisId, targetId) {
    //console.log('# thisId, targetId :: [', thisId, '][', targetId);

    let thisDe   = $('#'+thisId+'De').val();
    let thisHour = $('#'+thisId+'Hour').val();
    let thisMin  = $('#'+thisId+'Min').val();
    let thisDateStr = thisDe + ' ' + thisHour + ':' + thisMin;
    let inDate = new Date(thisDateStr);

    // ------------------------------------------------------------------------
    // 1분 더하기
    // ------------------------------------------------------------------------
    let targetDateNew = inDate.setMinutes(inDate.getMinutes()+1);
    let targetDate = new Date(targetDateNew);
    //console.log('# targetDate :: ', targetDate);
    let targetDateStr = targetDate.oFormat('yyyy-MM-dd HH:mm');
    //console.log('# targetDateStr   :: ', targetDateStr);

    let targetDe    = targetDateStr.substring(0, 10);
    let targetHour  = targetDateStr.substring(11, 13);
    let iTargetHour = Number(targetHour);
    let targetMin   = targetDateStr.substring(14, 16);
    let iTargetMin  = Number(targetMin);
    //console.log('# targetDe   :: ', targetDe);
    //console.log('# targetHour :: ', targetHour);
    //console.log('# targetMin  :: ', targetMin);

    // ------------------------------------------------------------------------
    // targetId에 일자/시/분 Set
    // ------------------------------------------------------------------------
    $('#'+targetId+'De').data('kendoDatePicker').value(targetDe);
    $('#'+targetId+'Hour').data('kendoDropDownList').select(iTargetHour);
    $('#'+targetId+'Min').data('kendoDropDownList').select(iTargetMin);

    // 1분 더하기 테스트
    //  //let selectStartDate = new Date('yyyy-MM-dd HH:mm');
    //  let selectStartDateNew = endDate.setMinutes(endDate.getMinutes()+1);
    //  let selectStartDate    = new Date(selectStartDateNew);
    //  console.log('# selectStartDate :: ', selectStartDate);
    //  let selectStartDateStr = selectStartDate.oFormat('yyyy-MM-dd HH:mm');
    //  console.log('# selectStartDateStr :: ', selectStartDateStr);
    //  //let selectStartYear        = selectStartDate.getFullYear();
    //  //let selectStartMonth       = selectStartDate.getMonth();
    //  //let selectStartDay         = selectStartDate.getDate();
    //  //let selectStartHourIndex = selectStartDate.getHours();
    //  //let selectStartMinIndex  = selectStartDate.getMinutes();
    //  //console.log('# selectStartYear      :: ', selectStartYear);
    //  //console.log('# selectStartMonth     :: ', selectStartMonth);
    //  //console.log('# selectStartDay       :: ', selectStartDay);
    //  //console.log('# selectStartHourIndex :: ', selectStartHourIndex);
    //  //console.log('# selectStartMinIndex  :: ', selectStartMinIndex);


  }

  /**
   * 날짜 + 시간
   * @param {*} e : 켄도데이트피커 change 이벤트 전달 인자
   * @param {string} type : 'start' , 'end'
   * @param {string} sId  : 시작일 데이트피커 아이디
   * @param {string} sHour : 시작 시간 타임피커 아이디
   * @param {string} sMin : 시작 분 타임피커 아이디
   * @param {string} eId : 종료일 데이트피커 아이디
   * @param {string} eHour : 종료 시간 타임피커 아이디
   * @param {string} eMin : 종료 분 타임피커 아이디
   */
  function fnOnChangeDateTimePickerCustom( e, type, sId, sHour, sMin, eId, eHour, eMin, startTitle, endTitle) {
    var self = e.sender.element;
    var role = self.data('role');

    var sd = $('#' + sId).data('kendoDatePicker');
    var sh = $('#' + sHour).data('kendoDropDownList');
    var sm = $('#' + sMin).data('kendoDropDownList');

    var ed = $('#' + eId).data('kendoDatePicker');
    var eh = $('#' + eHour).data('kendoDropDownList');
    var em = $('#' + eMin).data('kendoDropDownList');

    var sDate = sd.value();
    var eDate   = ed.value();
    var isValid = true;

    if( sDate && eDate ) {
      sDate.setHours(sh.value());
      sDate.setMinutes(sm.value());

      eDate.setHours(eh.value());
      eDate.setMinutes(em.value());

      isValid = fnValidateDatePicker(sDate, eDate);
    }

    if( !isValid ) {
      if( type === 'start' ) {
        alert(startTitle+'는 '+endTitle+'를 초과할 수 없습니다.');
      } else {
        alert(endTitle+'는 '+startTitle+'보다 빠를수 없습니다.');
      }

      if( role === 'datepicker' ) {
        self.data('kendoDatePicker').value('');
      } else  if( role === 'dropdownlist' ) {
        if( type === 'start' ) {
          self.data('kendoDropDownList').select(0);
        } else {
          self.data('kendoDropDownList').select(e.sender.dataSource.options.data.length - 1);
        }
      }
      return;
    }
    fnClearDateController(eId);
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 등록 임시 작업 영역 Start : eventMgmDataSet.js 로 이동 할 것

  // 등록 임시 작업 영역 End : eventMgmDataSet.js 로 이동 할 것
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 상세 임시 작업 영역 Start : eventMgmDataSet.js 로 이동 할 것


  // ==========================================================================
  // # 조회데이터Set - 룰렛이벤트
  // ==========================================================================






  // 상세 임시 작업 영역 End : eventMgmDataSet.js 로 이동 할 것
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
