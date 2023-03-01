/*******************************************************************************
/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 기획전상세(등록/수정/삭제)
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.06.28    최윤지       최초생성
 * @
 ******************************************************************************/
'use strict';
var gbBeditable     = false;  // 테스트용임, 반듯이 false로 변경해야 함
var gbPageParam     = '';     // 넘어온 페이지파라미터
var gbExhibitTp     = '';     // 기획전유형
var gbMode          = '';     // 모드(등록/수정/삭제)
var gbEvExhibitId   = '';     // 기획전PK
var gbCsRewardId    = '';     // 고객보상제PK
var gbTodayDe       = '';     // 당일

var gbDelIdArr;     // 삭제 Id 리스트
var gbDelGruopId;   // 삭제상품그룹ID
var gbDetail;       // 상세조회결과 전역변수

var gbGroupIdx = 0;
var gbGroupCnt = 0;
var MAX_GROUP_CNT = 20;


// 그리드내 활성/비활성 처리용 변수
var gbEditableMode  = '';


// ----------------------------------------------------------------------------
// 파일업로드
// ----------------------------------------------------------------------------
var publicStorageUrl = fnGetPublicStorageUrl(); // 이미지 업로드되는 public 저장소 url 경로
var bannerImageUploadMaxLimit = 1024000;  // 배너 이미지 첨부 가능 최대 용량 ( 단위 : byte )
var workindEditorId;                      // 상품 상세 기본 정보와 주요 정보 Editor 중 이미지 첨부를 클릭한 에디터 Id
// 배너이미지
var gbBnrImgPath;        // 이미지1-풀경로
var gbBnrImgOriginNm;    // 이미지1-원본파일명

// ----------------------------------------------------------------------------
// 당일일자
// ----------------------------------------------------------------------------
var FULL_DATE_FORMAT = 'yyyy-MM-dd';
var date = new Date();
//date.setHours(0,0,0,0);
gbTodayDe = date.oFormat(FULL_DATE_FORMAT);

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
    $scope.$emit('fnIsMenu', { flag : 'true' });

    // ------------------------------------------------------------------------
    // 페이지파라미터
    // ------------------------------------------------------------------------
    gbPageParam = fnGetPageParam();
    fnPageInfo({
            PG_ID     : 'rewardMgm'
          , callback  : fnUI
        });

    // ------------------------------------------------------------------------
    // 고객보상제 PK
    // ------------------------------------------------------------------------
    gbCsRewardId = gbPageParam.csRewardId;

    // ------------------------------------------------------------------------
    // 모드
    // ------------------------------------------------------------------------
    gbMode = gbPageParam.mode;

    // ------------------------------------------------------------------------
    // 상단타이틀
    // ------------------------------------------------------------------------
    $('#pageTitleSpan').text('고객보상제 등록/수정');
  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

    // DataSet.js
    importScript('/js/controllers/admin/pm/exhibit/exhibitMgmDataSet.js', function() {

      fnInitButton();           // Initialize Button --------------------------

      fnInitTabArea();          // Initialize Tab -----------------------------

      fnInitOptionBox();        // Initialize Option Box ----------------------

      fnSetDefault();           // 기본설정 -----------------------------------

      fnSearch();               // 조회 ---------------------------------------
    });

  }

  // ==========================================================================
  // # 초기화 - 버튼
  // ==========================================================================
  function fnInitButton() {
    $('#fnBtnGoList, #fnBtnSave').kendoButton();
  }

  // ==========================================================================
  // 상세영역 노출/숨김 설정
  // ==========================================================================
  function fnInitTabArea() {

    $('#normalDiv').removeClass('show');
    $('#rewardPayTpDiv').removeClass('show');
      $('#normalDiv').addClass('show');
      $('#rewardPayTpDiv').addClass('show');
  }

  // ==========================================================================
  // # 초기화 - 옵션박스
  // ==========================================================================
  function fnInitOptionBox() {
    // ------------------------------------------------------------------------
    // 사용여부(R)  - 공통코드 아님
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'useYn'
      , tagId   : 'useYn'
      , data    : [
                    { 'CODE' : 'Y'  , 'NAME' : '예'     }
                  , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                  ]
      , chkVal  : 'Y'
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 고객보상제 신청 기준 - rewardApplyStandard
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id          : 'rewardApplyStandard'
      , tagId       : 'rewardApplyStandard'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'REWARD_APPLY_STANDARD', 'useYn' : 'Y'}
      , async       : false
      , isDupUrl    : 'Y'
      , style       : {}
      , chkVal      : 'REWARD_APPLY_STANDARD.ORDER_NUMBER'
      , change: function(e) {
				const value = e.target.value;
				if( value == 'REWARD_APPLY_STANDARD.ORDER_GOODS' ) {
						$('#normalDiv').show(); //적용대상 상품 div show
					} else {
						$('#normalDiv').hide(); //적용대상 상품 div hide
					}

				if( value == 'REWARD_APPLY_STANDARD.NONE' ) {
						$('#orderApprPeriodTr').hide(); //주문인정기간 tr show
					} else {
						$('#orderApprPeriodTr').show(); //주문인정기간 tr hide
					}

			}
    });

    // ------------------------------------------------------------------------
    // 적용대상 상품 - rewardGoodsTp
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'rewardGoodsTp'
      , tagId   : 'rewardGoodsTp'
      , data    : [
                    { 'CODE' : 'ALL'  , 'NAME' : '전체'     }
                  , { 'CODE' : 'TARGET_GOODS'  , 'NAME' : '지정상품' }
                  ]
      , chkVal  : 'ALL'
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 고객보상제 지급 유형 - rewardPayTp
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id          : 'rewardPayTp'
      , tagId       : 'rewardPayTp'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'REWARD_PAY_TP', 'useYn' : 'Y'}
      , async       : false
      , isDupUrl    : 'Y'
      , style       : {}
      , chkVal      : 'REWARD_PAY_TP.COUPON'
    });

    // ------------------------------------------------------------------------
    // 주문인정기간 - orderApprPeriod
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'orderApprPeriod'
      , tagId       : 'orderApprPeriod'
      , data        : [
                        { 'CODE' : '1'   , 'NAME' : '1개월'       }
                      , { 'CODE' : '2'   , 'NAME' : '2개월'       }
                      , { 'CODE' : '3'   , 'NAME' : '3개월'       }
                      , { 'CODE' : '4'   , 'NAME' : '4개월'       }
                      , { 'CODE' : '5'   , 'NAME' : '5개월'       }
                      , { 'CODE' : '6'   , 'NAME' : '6개월'       }
                      ]
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , chkVal      : '1'
      , style       : {}
    });


    // ------------------------------------------------------------------------
    // 진행기간-시작일자/종료일자
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id        : 'startDe'
      , format    : 'yyyy-MM-dd'
      , change    : fnOnChangeStartDe
      //, defVal    : gbTodayDe
    });
    fnKendoDatePicker({
        id        : 'endDe'
      , format    : 'yyyy-MM-dd'
      //, btnStyle  : true
      , btnStartId: 'startDe'
      , btnEndId  : 'endDe'
      , change    : fnOnChangeEndDe
      //, defVal    : '2999-12-31'
      //, defType   : 'oneWeek'
      , minusCheck: true
      , nextDate  : true
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
    // 기획전상세-PC-에디터
    // ------------------------------------------------------------------------
    fnInitKendoEditor({id : 'detlHtmlPc'});

    // ------------------------------------------------------------------------
    // 기획전상세-Mobile-에디터
    // ------------------------------------------------------------------------
    fnInitKendoEditor({id : 'detlHtmlMo'});

        fnGroupAdd('add', null);

  } // End of fnInitOptionBox

  // //////////////////////////////////// Tmp Start
  // ==========================================================================
  // 제이쿼리 템플릿 초기화 함수 - 접근권한 템플릿 용도
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
  }

  // ==========================================================================
  // # 기본 설정
  // ==========================================================================
  function fnSetDefault(){
    let searchInfo    = sessionStorage.getItem('searchInfo');
    let searchInfoObj = new Object();
    //console.log('# searchInfo     :: ', searchInfo);
    if (searchInfo != null) {
      searchInfoObj = JSON.parse(searchInfo);
      searchInfoObj.isFromDetl = 'Y';
      sessionStorage.setItem('searchInfo', JSON.stringify(searchInfoObj));
    }

    // ------------------------------------------------------------------------
    // 기획전제목 포커스
    // ------------------------------------------------------------------------
    $('#title').focus();
    $('#normalDiv').hide(); // 적용대상상품 div hide
    $('#targetGoodsDiv').hide();

    // 비활성화 (사용여부 제외)
    if(fnNvl(gbCsRewardId) != '') {
      $("#title").prop("disabled", true);
      $("#description").prop("disabled", true);
      $("input[name=rewardApplyStandard]").prop("disabled", true);
      fnGetDatePickerInstance("startDe").enable(false);
      $("#startHour").data("kendoDropDownList").enable( false );
      $("#startMin").data("kendoDropDownList").enable( false );
      fnGetDatePickerInstance("endDe").enable(false);
      $("#endHour").data("kendoDropDownList").enable( false );
      $("#endMin").data("kendoDropDownList").enable( false );
      $("#timeOverCloseYn").prop("disabled", true);
      $("#orderApprPeriod").data("kendoDropDownList").enable( false );
      //$("#detlHtmlPc").prop("disabled", true);
      //$("#detlHtmlMo").prop("disabled", true);
      //$("#rewardNotice").prop("disabled", true);
      $("#btnAddGoodsPopupGoodsGroup1").prop("disabled", true);
      $("input[name=rewardPayTp]").prop("disabled", true);
      //$($('#detlHtmlPc').data().kendoEditor.body).attr('contenteditable', false);
      //$($('#detlHtmlMo').data().kendoEditor.body).attr('contenteditable', false);
    }

  };

  // ##########################################################################
  // # 조회 Start
  // ##########################################################################
  // ==========================================================================
  // # 조회
  // ==========================================================================
  function fnSearch() {
    if (gbMode == 'update' && gbCsRewardId != null && gbCsRewardId != '' && gbCsRewardId != 0 && gbCsRewardId != '0') {
       fnSearchAjax();
   }

  }

  // ==========================================================================
  // # 조회 Ajax
  // ==========================================================================
  function fnSearchAjax() {

    // ----------------------------------------------------------------------
    // 기획전상세조회-기본정보(일반/골라담기/증정행사 공통)
    // ----------------------------------------------------------------------
    fnAjax({
        url       : '/admin/customer/reward/getRewardInfo?csRewardId=' + gbCsRewardId        // 주소줄에서 ID 보기위해 params 사용안함
      , isAction  : 'select'
      , success   : function(data, status, xhr) {
                      // --------------------------------------------------
                      // 성공 Callback 처리
                      // --------------------------------------------------
                      fnBizCallback('select', data, null);
                    }
      , error     : function(xhr, status, strError) {
                      fnKendoMessage({
                       message : xhr.responseText
                      });
                    }
    });

  }

  // ==========================================================================
  // # 조회 - 상세조회
  // ==========================================================================
  function fnSearchDetail(data) {

    // ------------------------------------------------------------------------
    // 상세정보-기본정보 Set (공통)
    // ------------------------------------------------------------------------
    fnSetExhibitDetlInfo(data);
    //적용대상상품
    fnSetGroupInfo(data.rewardTargetGoodsInfo);

  }

  // ==========================================================================
  // # 상세정보-기본정보 Set (공통)
  // ==========================================================================
  function fnSetExhibitDetlInfo(data) {

    var detail = data.rewardDetlInfo;
    // ------------------------------------------------------------------------
    // Binding
    // ------------------------------------------------------------------------
    $('#inputForm').bindingForm( {'rows':detail}, 'rows' );

    // 켄도 에디터 내 스크립트 허용 HGRM-8031
    $("#detlHtmlPc").data("kendoEditor").value(detail.detlHtmlPc);
    $("#detlHtmlMo").data("kendoEditor").value(detail.detlHtmlMobile);

    // ------------------------------------------------------------------------
    // Auto Binding 이외 처리
    // ------------------------------------------------------------------------
    // 보상제 ID
    $('#evExhibitIdSpan').text(detail.csRewardId);

    // 고객보상제 명
    $('#title').val(detail.rewardNm);

    // 고객보상제 설명
    $('#description').text(detail.rewardContent);

    // 최초등록정보
    $('#createSpan').text(detail.createDt + ' / ' + detail.createUserNm + '(' + detail.createUserId + ')');

    // 최근수정내역
    var modifyStr = ' ';

    if (detail.modifyDt != null && detail.modifyDt != '') {
      modifyStr += detail.modifyDt;

      if (detail.modifyUserNm != null && detail.modifyUserNm != '') {
        modifyStr += ' / ';
        modifyStr += detail.modifyUserNm;

        if (detail.modifyUserId != null && detail.modifyUserId != '' && detail.modifyUserId != '0' && detail.modifyUserId != 0) {
          modifyStr += '(';
          modifyStr += detail.modifyUserId;
          modifyStr += ')';
        }
      }
    }
    $('#modifySpan').text(modifyStr);

    if(detail.rewardApplyStandard == 'REWARD_APPLY_STANDARD.ORDER_GOODS') {
      $('#normalDiv').show();
      if(detail.rewardGoodsTp == 'TARGET_GOODS') {
        $('#targetGoodsDiv').show();
      } else {
        $('#targetGoodsDiv').hide();
      }
    } else {
      $('#normalDiv').hide();
      if(detail.rewardApplyStandard == 'REWARD_APPLY_STANDARD.NONE') {
        	$('#orderApprPeriodTr').hide(); //주문인정기간 tr show
      }
    }
    // 진행기간-시작
        var startDtStr = '';
        var startDe    = '';
        var startHour;
        var startMin;
        //startDtStr  = (((detail.startDt).replace(/-/gi, '')).replace(/:/gi, '')).replace(/ /gi, '');
        startDtStr  = ((detail.startDt).replace(/:/gi, '')).replace(/ /gi, '');

        if (startDtStr != null && startDtStr != '') {
          if (startDtStr.length >= 10) {
            startDe = startDtStr.substring(0, 10);
            $('#startDe').val(startDe);

            // ----------------------------------------------------------------
            // 날짜 선후비교를 위한 kendoDatePicker 값 Set
            // ----------------------------------------------------------------
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
        //endDtStr  = (((detail.endDt).replace(/-/gi, '')).replace(/:/gi, '')).replace(/ /gi, '');
        endDtStr  = ((detail.endDt).replace(/:/gi, '')).replace(/ /gi, '');
        if (endDtStr != null && endDtStr != '') {
          if (endDtStr.length >= 10) {
            endDe = endDtStr.substring(0, 10);
            $('#endDe').val(endDe);

            // ----------------------------------------------------------------
            // 날짜 선후비교를 위한 kendoDatePicker 값 Set
            // ----------------------------------------------------------------
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
        // 기간종료후사용자동종료여부
        if (detail.timeOverCloseYn == 'Y') {

          $('#timeOverCloseYn').prop('checked', true);
        }


  }

  // ==========================================================================
  // # 조회 - 일반(그룹) 정보 Set
  // ==========================================================================
  function fnSetGroupInfo(rewardTargetGoodsInfo) {

    var groupData;
    var groupIdx = 0;
    fnSethNormalGroupGoodsList(1, rewardTargetGoodsInfo);

  }

  // # 조회 End
  // ##########################################################################
  // # 조회 - 일반(그룹) - 상품리스트
  // ==========================================================================
  function fnSethNormalGroupGoodsList(groupIdx, evExhibitGroupId) {

    // --------------------------------------------------------------------
    // 1. 그룹상품그리드 초기화
    // --------------------------------------------------------------------
    fnInitGroupGoodsGrid(groupIdx, evExhibitGroupId);

    // --------------------------------------------------------------------
    // 2. 그리드 조회
    // --------------------------------------------------------------------
    let data = $('#inputForm').formSerialize(true);
    $('#groupGoodsGrid'+groupIdx).data('kendoGrid').dataSource.read(data);
  }

  // ##########################################################################
  // # 변경/삭제 Start
  // ##########################################################################
  // ==========================================================================
  // # 삭제 - 상품그룹.상품
  // ==========================================================================
  function fnBtnExhibitGroupDetlDel(evExhibitGroupDetlId, groupId) {

    // 모드
    gbMode      = 'delete.group.detl';    // 삭제.상품그룹.상품
    // 삭제대상
    gbDelIdArr = new Array() ;
    gbDelIdArr.push(evExhibitGroupDetlId);
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
  // ##########################################################################
  // ==========================================================================
  // # 저장
  // ==========================================================================
  function fnSave() {

    var confirmMsg  = '';
    var url         = '';
    var cbId        = gbMode;
    var isAction    = '';
    var rewardDataObj = new Object();   // 반환 obj
    var rewardData    = {};
    var data;

    // ************************************************************************
    // 1. form Set - 기본정보
    // ************************************************************************
    data = $('#inputForm').formSerialize(true);

    // ------------------------------------------------------------------------
    // 1.1. 보상제ID csRewardId
    // ------------------------------------------------------------------------
    if (gbMode == 'insert') {
      data.csRewardId = '';
    }
    else if (gbMode == 'update') {
      data.csRewardId = gbCsRewardId;
    }

    // ------------------------------------------------------------------------
    // @ 3.1.1. 데이터 Set - 고객보상제 기본정보
    // ------------------------------------------------------------------------
    rewardData.csRewardId      = gbCsRewardId; //고객보상제 관리 Pk
    rewardData.useYn           = data.useYn; //사용여부
    rewardData.rewardNm        = data.title; //보상제 명
    rewardData.rewardContent   = data.description; //보상제 설명
    rewardData.rewardPayTp    = data.rewardPayTp; //보상지급유형
    rewardData.rewardApplyStandard    = data.rewardApplyStandard; //보상제 신청 기준
    rewardData.rewardNotice  = data.rewardNotice; //유의사항
    rewardData.detlHtmlPc      = data.detlHtmlPc; // 보상제 상세 PC
    rewardData.detlHtmlMobile  = data.detlHtmlMo; // 보상제 상세 MOBILE
    rewardData.startDt         = data.startDe + '' + data.startHour + '' + data.startMin + '00'; //신청기간 시작일
    rewardData.endDt           = data.endDe   + '' + data.endHour   + '' + data.endMin   + '59'; //신청기간 종료일
    rewardData.orderApprPeriod   = data.orderApprPeriod; // 주문인정기간
    rewardData.timeOverCloseYn   = $('#timeOverCloseYn').prop('checked') == true ? 'Y' : 'N'; // 기간 종료 후 사용 자동종료 여부

    // 고객보상제 신청기준이 해당없음일 경우, 주문인정기간 x
    if(data.rewardApplyStandard == 'REWARD_APPLY_STANDARD.NONE') {
      delete rewardData.orderApprPeriod;
    }
    // 고객보상제 신청기준이 주문상품이고, 지정상품일 시
    if(data.rewardApplyStandard == 'REWARD_APPLY_STANDARD.ORDER_GOODS') {
      if(data.rewardGoodsTp == 'TARGET_GOODS') {
        rewardDataObj = fnSetParamValueSelect(rewardDataObj, data);
        rewardData.rewardTargetGoodsList = rewardDataObj.exhibitSelectGoodsList;
      }
        rewardData.rewardGoodsTp = data.rewardGoodsTp;
    } else {
        rewardData.rewardGoodsTp = '';
    }


    // ************************************************************************
    // 실행
    // ************************************************************************
    if (gbMode == 'insert') {
      // ----------------------------------------------------------------------
      // 저장
      // ----------------------------------------------------------------------
      confirmMsg  = '저장하시겠습니까?';
      url         = '/admin/customer/reward/addReward';
      isAction    = 'insert';
    }
    else if (gbMode == 'update') {
      // ----------------------------------------------------------------------
      // 수정
      // ----------------------------------------------------------------------
      confirmMsg = '수정하시겠습니까?';
      url         = '/admin/customer/reward/putReward';
      isAction    = 'update';
    }
    else {
      fnMessage('', '기능 오류입니다.['+gbMode+']', '');
      return false;
    }
    // ------------------------------------------------------------------------
    // Validation & Ajax Call
    // ------------------------------------------------------------------------
    if(data.rtnValid) {

      // ----------------------------------------------------------------------
      // 2. Ajax Call
      // ----------------------------------------------------------------------
      fnKendoMessage({message:fnGetLangData({key :"",nullMsg : confirmMsg }), type : "confirm", ok :
        function(){
          // 저장 처리 후 선택 콘텐츠레벨 초기화
          //selectedContsLevel = null;

          fnAjax({
              url     : url
            , params  : rewardData
            //, params  : pageData
            , contentType: "application/json"
            , success : function(result){
                          // --------------------------------------------------
                          // 성공 Callback 처리
                          // --------------------------------------------------
                          fnBizCallback(cbId, result, null);
                        }
            , fail    : function(data, resultcode){
                          // --------------------------------------------------
                          // 실패 메시지 처리
                          // --------------------------------------------------
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
                          fnKendoMessage({
                            message : xhr.responseText
                          });
                        }
            , isAction : isAction
          });
        }
      });
    } // End of if(data.rtnValid)
  }

  // ==========================================================================
  // # 등록/수정 데이터 Set
  // ==========================================================================
  function fnSetParamValueSelect(rewardDataObj, data) {
    var selectGoodsList       = new Array();  // 상품리스트
    var selectGoodsGridData;                  // 상품그리드
    var selectGoodsGridArr;                   // 상품그리드

    // ------------------------------------------------------------------------
    // 2. 상품그리드
    // ------------------------------------------------------------------------
    selectGoodsGridData = $('#groupGoodsGrid1').data('kendoGrid');
    selectGoodsGridArr  = selectGoodsGridData.dataSource.data();

    // ------------------------------------------------------------------------
    // 2.1. 상품그리드 - 노출여부 컬럼 Idx 가져오기
    // ------------------------------------------------------------------------
    var selectGoodsDisplayYnNmIdx  = 0;
    var selectGoodsColumns = selectGoodsGridData.options.columns;

    if (selectGoodsColumns.length > 0) {
      for (var k = 0; k < selectGoodsColumns.length; k++) {
        if (selectGoodsColumns[k].field == 'displayYnNm') {
          selectGoodsDisplayYnNmIdx = k;
          break;
        }
      }
    }

    if (selectGoodsGridArr != undefined && selectGoodsGridArr != null && selectGoodsGridArr.length > 0) {

      for (var i = 0; i < selectGoodsGridArr.length; i++) {

        var goodsData = new Object();
        goodsData.ilGoodsId   = selectGoodsGridArr[i].ilGoodsId;
        goodsData.goodsSort   = selectGoodsGridArr[i].goodsSort;
        //goodsData.displayYnNm = $("#selectGoodsGrid").find("tr").eq(i+1).find('td').eq(selectGoodsDisplayYnNmIdx).text();  // 노출여부 화면표시문구

        selectGoodsList.push(goodsData);

      } // End of for (var i = 0; i < selectGoodsGridArr.length; i++)

      rewardDataObj.exhibitSelectGoodsList            = selectGoodsList;

    } // End of if (selectGoodsGridArr != undefined && selectGoodsGridArr != null && selectGoodsGridArr.length > 0)
    return rewardDataObj;
  }

  // # 저장 End
  // ##########################################################################


  // # 삭제 End
  // ##########################################################################

  // ==========================================================================
  // # 콜백함수
  // ==========================================================================
  function fnBizCallback(id, data, removeObj) {

    var gridId;

    switch(id){
      case 'select':
        fnSearchDetail(data);

        break;
      case 'insert':
        // --------------------------------------------------------------------
        // 등록
        // --------------------------------------------------------------------
        // 기획전ID Set
        gbCsRewardId = data.csRewardId;
        // 목록 이동 시 1페이지로 감
        sessionStorage.setItem('lastPage', "1");
        // 모드 Set
        gbMode        = 'update';

        fnKendoMessage({
            message : '등록되었습니다.'
          , ok      : function() {
                        // 수정화면 이동 (자신)
                        fnGoExhibitEdit();
                        // 승인요청 체크박스 언체크
                       // $('input:checkbox[name="approvalRequestYn"]').prop('checked', false);
                      }
        });
        break;
      case 'delete':
        // --------------------------------------------------------------------
        // 삭제
        // --------------------------------------------------------------------
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
      case 'update':
        // --------------------------------------------------------------------
        // 수정
        // --------------------------------------------------------------------
        // 화면 재조회
        fnKendoMessage({
            message : '수정되었습니다.'
          , ok      : function(){
                        // 수정 후 재조회
                        // 재조회
                        fnSearch();
                        // 승인요청 체크박스 언체크
                      //  $('input:checkbox[name="approvalRequestYn"]').prop('checked', false);
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

  // ==========================================================================
  // # 수정화면 이동
  // ==========================================================================
  function fnGoExhibitEdit() {

    // ------------------------------------------------------------------------
    // 수정 화면으로 이동
    // ------------------------------------------------------------------------
    // 링크정보
    let option = {};
    option.url    = '#/rewardMgm';
    // 고객보상제 등록/수정 : 100008059 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
     option.menuId = 1355;
    option.target = '_self';
    option.data = {csRewardId : gbCsRewardId, mode : 'update'};
    // 화면이동
    fnGoPage(option);

    fnSearchAjax();
  }

  // ##########################################################################
  // # 상품그룹 Start
  // ##########################################################################
  // ==========================================================================
  // # 상품그룹-추가
  // ==========================================================================
  function fnGroupAdd(mode, evExhibitGroupId) {
    var groupIdx = gbGroupIdx + 1;
    var groupCnt = gbGroupCnt + 1;

    var $target = $('#group-btnArea');
    var tpl = $('#groupTpl').html();
    // IDX 치환
    tpl = tpl.replace(/{IDX}/g, groupIdx);
    //EV_EXHIBIT_GROUP_ID 치환
    if (evExhibitGroupId != null) {
     tpl = tpl.replace(/{EV_EXHIBIT_GROUP_ID}/g, evExhibitGroupId);
    }

      $(tpl).appendTo($('#addAreaDiv'));

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 객체생성
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     fnTagMkRadio({
        id      : 'rewardGoodsTp'
      , tagId   : 'rewardGoodsTp'
      , data    : [
                    { 'CODE' : 'ALL'  , 'NAME' : '전체'     }
                  , { 'CODE' : 'TARGET_GOODS'  , 'NAME' : '지정상품' }
                  ]
      , chkVal  : 'ALL'
      , style   : {}
      , change: function(e) {
				const value = e.target.value;
				if( value == 'TARGET_GOODS' ) {
						$('#targetGoodsDiv').show(); //적용대상 상품 div show
					} else {
						$('#targetGoodsDiv').hide(); //적용대상 상품 div hide
					}
			}
      //, beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
    });
    //수정 시 비활성화 (적용대상 상품등록)
    fnNvl(gbCsRewardId) != '' ? $('input[name=rewardGoodsTp]').prop('disabled', true) : $('input[name=rewardGoodsTp]').prop('disabled', false);

    fnInitGroupGoodsGrid(groupIdx, evExhibitGroupId);

    gbGroupIdx++;
    gbGroupCnt++;

  }

  // # 상품그룹 End
  // ##########################################################################

  // ##########################################################################
  // # 그리드 Start
  // ##########################################################################
  // --------------------------------------------------------------------------
  // # 그리드-그룹상품 (적용대상 상품등록 )
  // --------------------------------------------------------------------------
  function fnInitGroupGoodsGrid(groupIdx, csRewardId) {
    var callUrl          = '';
    // 페이징없는 그리드
    var groupGoodsGridDs = fnGetDataSource({
      url      : '/admin/customer/reward/getRewardTargetGoodsInfo?csRewardId='+ gbCsRewardId
    });

    var bEditableGrid = gbBeditable;
    bEditableGrid = false;

    var groupGoodsGridOpt = {
          dataSource  : groupGoodsGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , editable    : {
                          confirmation: false
                        }
        , resizable   : true
        , autobind    : false
        , columns     : [
                          { field : 'goodsSort'       , title : '노출순번'      , width:  '60px', attributes : {style : 'text-align:center;'}
                            , editable:function () {
                                                      if(fnNvl(gbCsRewardId) != ''){
                                                        return false;
                                                      } else {
                                                        return true;
                                                      }
                                                    }, editor: sortEditor
                          }
                        , { field : 'goodsTpNm'         , title : '상품유형'      , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        , { field : 'ilGoodsId'       , title : '상품코드'      , width: '120px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        , { field : 'goodsNm'         , title : '상품명'        , width: '300px', attributes : {style : 'text-align:left;'  }, editable:function (dataItem) {return bEditableGrid;}
                                                                                , template  : function(dataItem) {
                                                                                                let imageUrl = dataItem.goodsImagePath ? publicStorageUrl + dataItem.goodsImagePath : '/contents/images/noimg.png';
                                                                                                return '<img src="' + imageUrl + '" width="50" height="50" align="left" /><BR>&nbsp;&nbsp;' + dataItem.goodsNm ;
                                                                                              }
                          }
                        , { field : 'saleStatusNm'    , title : '판매상태'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        , { field : 'management'      , title : '관리'          , width:  '50px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}
                                                      , template  : function(dataItem) {
                                                                      if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
                                                                        if(fnNvl(gbCsRewardId) != '') {
                                                                          return  '<div class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-white btn-s" kind="btnGroupGoodsDel" disabled>제거</button>'
                                                                              + '</div>';
                                                                        } else {
                                                                          return '<div class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-white btn-s" kind="btnGroupGoodsDel">제거</button>'
                                                                              + '</div>';
                                                                        }
                                                                      }
                                                                      else {
                                                                        if(fnNvl(gbCsRewardId) != '') {
                                                                          return  '<div class="btn-area textCenter">'
                                                                              + '<button type="button" name="btnGroupGoodsDel" class="btn-red btn-s" kind="btnGroupGoodsDel" disabled>삭제</button>'
                                                                              + '</div>';
                                                                        } else {
                                                                          return  '<div class="btn-area textCenter">'
                                                                              + '<button type="button" name="btnGroupGoodsDel" class="btn-red btn-s" kind="btnGroupGoodsDel">삭제</button>'
                                                                              + '</div>';
                                                                        }

                                                                      }
                                                        }
                          }
                        ]
    };

    // Grid
    $('#groupGoodsGrid'+groupIdx).initializeKendoGrid( groupGoodsGridOpt ).data('kendoGrid');

    var groupGoodsGrid = $('#groupGoodsGrid'+groupIdx).initializeKendoGrid(groupGoodsGridOpt).data('kendoGrid');

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
        fnBtnExhibitGroupDetlDel(dataItem.evExhibitGroupDetlId, groupIdx);
      }

    });
  }

  // # 그리드 End
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
                //, { name : 'insertImage'        , tooltip : '이미지 URL 첨부' }    // 삭제해도 된다 함(2021.03.03 홍진영L)
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

  // ##########################################################################
  // # 파일업로드 Start
  // ##########################################################################
  // ==========================================================================
  // # 파일업로드-업로드시 사용할 kendoUpload 컴포넌트 초기화
  // ==========================================================================
  function fnInitKendoUpload() {
    var uploadFileTagIdList = ['imgBanner'];

    var selectFunction = function(e) {

      if (e.files && e.files[0]) {
        // 이미지 파일 선택시

        if (bannerImageUploadMaxLimit < e.files[0].size) { // 배너 이미지 업로드 용량 체크
          fnKendoMessage({
            message : '이미지 업로드 허용 최대 용량은 ' + parseInt(bannerImageUploadMaxLimit / 1024) + ' kb 입니다.',
            ok : function(e) {}
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
            message : '허용되지 않는 확장자 입니다.',
            ok : function(e) {}
          });
          return;
        }

        var fileTagId = e.sender.element[0].id;
        let reader = new FileReader();

        reader.onload = function(ele) {
          var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
          var file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

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
    // 에디터에 파일 업로드 고려 안함(이석호M, 2020.11.03)  -> 다시 사용하기로 변경(2021.03.08)
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
        form        : "uploadImageOfEditorForm"
      , fileUrl     : "/fileUpload"
      , method      : 'GET'
      , url         : '/comn/getPublicStorageUrl'
      , storageType : "public"
      , domain      : "cs"
      , params      : formData
      , success     : function(result) {
                        var uploadResult = result['addFile'][0];
                        var serverSubPath = uploadResult['serverSubPath'];
                        var physicalFileName = uploadResult['physicalFileName'];
                        var imageSrcUrl = result['publicStorageUrl'] + serverSubPath + physicalFileName; // 업로드 이미지 url

                        var editor = $('#' + workindEditorId).data("kendoEditor"); // 이미지 첨부할 Editor
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

                        var originalFileName  = '';
                        var fullPath          = '';

                        // ----------------------------------------------------
                        // 업로드파일 정보 Set
                        // ----------------------------------------------------

                        originalFileName = data['addFile'][0].originalFileName;
                        fullPath         = data['addFile'][0].serverSubPath + data['addFile'][0].physicalFileName;

                        if (fileTagId == 'imgBanner') {
                          // 배너-이미지1
                          gbBnrImgPath     = fullPath;             // 이미지1-풀경로
                          gbBnrImgOriginNm = originalFileName;     // 이미지1-원본파일명
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
  // # 파일업로드 End
  // ##########################################################################


  // ##########################################################################
  // # 기타-Start
  // ##########################################################################
  // ==========================================================================
  // # 기타-팝업창닫기
  // ==========================================================================
  function fnClose(){
    var kendoWindow =$('#kendoPopup').data('kendoWindow');
    kendoWindow.close();
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


  // ==========================================================================
  // # 버튼-목록이동
  // ==========================================================================
  function fnGoList() {

    let option = {};

    option.url    = '#/rewardList';
    option.menuId = 1348;
    option.target = '_self';
    // 화면이동
    fnGoPage(option);
  }

  // # 기타-End
  // ##########################################################################


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
      // 고객보상제 - 일반
      // ----------------------------------------------------------------------
      params.goodsCallType                      = 'exhibitNormal';                            // 조회조건 : 일반기획전
      params.saleStatus                         = 'SALE_STATUS.ON_SALE';                      // 판매상태:판매중

      params.columnSalePriceHidden              = false;                                      // 판매가
      params.columnGoodsDisplyYnHidden          = false;                                      // 전시상태
    }

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
  // ------------------------------- Html 버튼 바인딩  Start ------------------
  // ==========================================================================

  /** Common fnBtnSave */
  $scope.fnBtnSave                            = function(){
                                                  if (gbCsRewardId == undefined || gbCsRewardId == null || gbCsRewardId == '' || gbCsRewardId <= 0) {
                                                    gbMode = 'insert';
                                                  }
                                                  else {
                                                    gbMode = 'update';
                                                  }
                                                  fnSave();
                                                };

  /** 상품조회팝업버튼-상품그룹 */
  $scope.fnBtnAddGoodsPopupGoodsGroup         = function(groupIdx){ fnBtnAddGoodsPopupGoodsGroup(groupIdx);};

  /** 목록 */
  $scope.fnBtnGoList                          = function(){
                                                  fnGoList();
                                                };

}); // document ready - END


