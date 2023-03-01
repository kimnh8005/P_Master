/*******************************************************************************
/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 DM메일상세(등록/수정/삭제)
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2020.12.07    dgyoun        최초생성
 * @
 ******************************************************************************/
'use strict';

//var PAGE_SIZE = 20;
//var aGridDs, aGridOpt, aGrid;
var gbBeditable     = false;  // 테스트용임, 반듯이 false로 변경해야 함
var gbPageParam     = '';     // 넘어온 페이지파라미터
var gbMode          = '';     // 모드(등록/수정/삭제)
var gbDmMailId   = '';     // DM메일PK
var gbTodayDe       = '';     // 당일

var gbDelIdArr;     // 삭제 Id 리스트
var gbDelGruopId;   // 삭제상품그룹ID
var gbDetail;       // 상세조회결과 전역변수

// ----------------------------------------------------------------------------
// 일반DM메일 - 그룹
// ----------------------------------------------------------------------------
var gbGroupIdx = 0;   // 현재 그룹 마지막 idx
var gbGroupCnt = 0;
var MAX_GROUP_CNT = 20;

// ----------------------------------------------------------------------------
// 증정행사
// ----------------------------------------------------------------------------
var gbIlShippingTmplId      = '';   // 배송정책PK
var gbShippingTmplNm        = '';   // 배송정책명
var gbUndeliverableAreaTp   = '';   // 배송불가지역유형
var gbUndeliverableAreaTpNm = '';   // 배송불가가능여부명
var gbUrWarehouseId         = '';   // 출고처ID
var gbWarehouseNm           = '';   // 출고처명
var gbStoreYn               = '';   // 출고처매정여부
var gbSelectedUrWarehouseId = '';   // 선택한 증정배송조건의 출고처


// ----------------------------------------------------------------------------
// 그리드
// ----------------------------------------------------------------------------
// 골라담기(균일가)-대표상품
var selectTargetGoodsGridDs, selectTargetGoodsGridOpt, selectTargetGoodsGrid;
// 골라담기(균일가)-상품
var selectGoodsGridDs, selectGoodsGridOpt, selectGoodsGrid;
// 골라담기(균일가)-추가상품
var selectAddGoodsGridDs, selectAddGoodsGridOpt, selectAddGoodsGrid;
// 증정행사-상품
var giftGoodsGridDs, giftGoodsGridOpt, giftGoodsGrid;
// 증정행사-적용대상-상품
var giftTargetGoodsGridDs, giftTargetGoodsGridOpt, giftTargetGoodsGrid;
// 증정행사-적용대상-브랜드
var giftTargetBrandGridDs, giftTargetBrandGridOpt, giftTargetBrandGrid;
// 승인
var apprAdminGridDs, apprAdminGridOpt, apprAdminGrid;

// 그리드내 활성/비활성 처리용 변수
var gbEditableMode  = '';
var gGridSalePrice; // 그리드 추가상품 - 판매가 저장용

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

// ----------------------------------------------------------------------------
// 접근권한 - 유저등급
// ----------------------------------------------------------------------------
var fnTemplates   = {};   // {}
var gbUserGroupMaps = new Map();

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
        PG_ID     : 'dmMailMgm'
      , callback  : fnUI
    });

    // ------------------------------------------------------------------------
    // DM메일PK
    // ------------------------------------------------------------------------
    gbDmMailId = gbPageParam.dmMailId;

    // ------------------------------------------------------------------------
    // 모드
    // ------------------------------------------------------------------------
    gbMode = gbPageParam.mode;

    // ------------------------------------------------------------------------
    // 상단타이틀
    // ------------------------------------------------------------------------
    $('#pageTitleSpan').text('DM 등록/수정');
  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

    // DataSet.js
    importScript('/js/controllers/admin/cs/dmMail/dmMailMgmDataSet.js', function() {

      fnInitButton();           // Initialize Button --------------------------

      fnInitTabArea();          // Initialize Tab -----------------------------

      fnInitOptionBox();        // Initialize Option Box ----------------------

      fnInitGrid();             // Initialize Grid ----------------------------

      fnInitEvent();            // Initialize Event ---------------------------

      fnSetDefault();           // 기본설정 -----------------------------------

      fnSearch();               // 조회 ---------------------------------------
    });

  }

  // ==========================================================================
  // # 초기화 - 버튼
  // ==========================================================================
  function fnInitButton() {
    // $('#fnBtnSetUnitPrice').kendoButton();

    if (gbMode == 'insert') {
      $('#fnBtnSave').text('저장');
      $('#btnDel').css('display','none');
      $('#fnClear').kendoButton();
    } else if (gbMode == 'update') {
      $('#fnBtnSave').text('수정');
      $('#fnPreview').css('display','');
      $('#fnCodeCopy').css('display','');
      $('#btnClear').css('display','none');
    }
  }

  // ==========================================================================
  // # 초기화 - 값
  // ==========================================================================
  function fnClear() {
    $('#inputForm').formClear(true);
    $("#descHtml").data("kendoEditor").value('');
    for(var i = 0; i < gbGroupIdx; i++) {
      fnBtnGroupDel(i+1);
    }
    gbGroupCnt = 0;
    gbGroupIdx = 0;
    fnGroupAdd('add', null);
    fnSetDefault();
  }

  // ==========================================================================
  // 상세영역 노출/숨김 설정
  // ==========================================================================
  function fnInitTabArea() {

    $('#normalDiv').removeClass('show');
    $('#selectDiv').removeClass('show');
    $('#giftDiv').removeClass('show');
    $('#approvalInfoDiv').removeClass('show');
    $('#approvalRequestDiv').removeClass('show');
    $('#approvalManagerDiv').removeClass('show');

    $('#normalDiv').addClass('show');
  }

  // ==========================================================================
  // # 초기화 - 옵션박스
  // ==========================================================================
  function fnInitOptionBox() {


    // ------------------------------------------------------------------------
    // DM메일유형(C)
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'mailTemplateTp'
      , tagId       : 'mailTemplateTp'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'MAIL_TEMPLATE_TP', 'useYn' :'Y'}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      : gbExhibitTp
      //, blank       : '전시범위 전체'
      , style       : {}
    });
    $('#mailTemplateTp').data('kendoDropDownList').enable(true);

    fnTagMkRadio({
      id      : 'goodsDispYn'
      , tagId   : 'goodsDispYn'
      , data    : [
          { 'CODE' : 'Y'  , 'NAME' : '예'     }
        , { 'CODE' : 'N'  , 'NAME' : '아니오' }
      ]
      , chkVal  : 'Y'
      , style   : {}
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
    function fnOnChangeStartDe(e) {
      // fnOnChangeDateTimePicker( e, 'start', 'startDe');
    }

    // ------------------------------------------------------------------------
    // DM메일상세-PC-에디터
    // ------------------------------------------------------------------------
    fnInitKendoEditor({id : 'descHtml'});

    // ------------------------------------------------------------------------
    // 파일업로드-배너이미지
    // ------------------------------------------------------------------------
    fnInitKendoUpload(); // 배너 && 에디터 이미지 업로드시 사용할 kendoUpload 컴포넌트 초기화


    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 기본DM메일
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ------------------------------------------------------------------------
    // 상품그룹1 - 신규인 경우에만 생성
    // ------------------------------------------------------------------------
    // 일반DM메일
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
  // 접근권한 추가 버튼
  // ==========================================================================
  function fnBtnAddUserGroup(e) {
    const $input = $('#userGroup');
    const inputValue = $('#userGroup').data('kendoDropDownList').value();
    const inputText = $('#userMaster').data('kendoDropDownList').text() + "_" + $('#userGroup').data('kendoDropDownList').text();

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
    // 화면 노출
    gbUserGroupMaps.set(inputText, inputValue);
    fnTemplates.commentTp.add({'keyword': inputText });
    //$input.val('').focus();
  }

  // ==========================================================================
  // 접근권한 삭제 버튼
  // ==========================================================================
  function fnRemoveUserGroup(e) {
    e.stopPropagation();
    e.stopImmediatePropagation();
    const $target = $(e.target).closest('.js__remove__userGroupList');
    gbUserGroupMaps.delete($target.find('.userGroupText').text())
    $target.remove();
  }
  // //////////////////////////////////// Tmp END

  // ==========================================================================
  // # 초기화 - 그리드
  // ==========================================================================
  function fnInitGrid() {

    // if (gbExhibitTp == 'EXHIBIT_TP.SELECT') {
    //   fnInitSelectTargetGoodsGrid();  // 골라담기(균일가)-대표상품
    //   fnInitSelectGoodsGrid();        // 골라담기(균일가)-상품
    //   fnInitSelectAddGoodsGrid();     // 골라담기(균일가)-추가상품
    //   fnInitApprGrid();               // 승인정보
    // }
    // else if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
    //   fnInitGiftGoodsGrid();          // 증정행사-상품
    //   fnInitGiftTargetGoodsGrid();    // 증정행사-적용대상-상품
    //   fnInitGiftTargetBrandGrid();    // 증정행사-적용대상-브랜드
    //   fnInitApprGrid();               // 승인정보
    // }
    //}
  }


  // ==========================================================================
  // # 기본 설정
  // ==========================================================================
  function fnSetDefault(){

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
      sessionStorage.setItem('searchInfo', JSON.stringify(searchInfoObj));
    }
    //console.log('# searchInfoObj  :: ', JSON.stringify(searchInfoObj));
    //console.log('# ------------------------------------------------');
    //
    //let searchInfoObj = JSON.parse(searchInfo);
    //if (fnIsEmpty(searchInfoObj) == false) {
    //  console.log('# searchInfoObj.length :: ', searchInfoObj.length);
    //  for (var i = 0; i < searchInfoObj.length; i++) {
    //    let field = searchInfoObj[i].field;
    //    let value = searchInfoObj[i].value;
    //    console.log('# [', field, '] :: [', value, ']');
    //  }
    //}
    //else {
    //  console.log('# searchInfoObj is Empty');
    //}

    // ------------------------------------------------------------------------
    // DM메일제목 포커스
    // ------------------------------------------------------------------------
    $('#title').focus();
  };

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
    $('[data-id="fnDateBtnC"]').on('click', function(){
        $('[data-id="fnDateBtn3"]').mousedown();
    });

    $('input[name="selectedDiv"]').on('change', e => {
      const $tabs = $('.tabs');
      const tabId = $(e.target).data('tab');
      $tabs.removeClass('show');
      $('#' + tabId).addClass('show');
    });

    // ------------------------------------------------------------------------
    // 품목코드 호출 시 품목 상세페이지 새창 호출
    // ------------------------------------------------------------------------
    //$('#itemDetailLink').on('click', function(e){
    //  e.preventDefault();
    //  window.open('#/itemMgmModify?ilItemCode='+ $('#itemCode').val() +'&isErpItemLink=true&masterItemType=MASTER_ITEM_TYPE.COMMON','_blank','width=1800, height=1000, resizable=no, fullscreen=no');
    //});

    // ------------------------------------------------------------------------
    // 파일업로드 - 이미지 이벤트
    // ------------------------------------------------------------------------
    var $removeBtn = $('.fileUpload__removeBtn');

    $removeBtn.on('click', function(e) {
      e.preventDefault();
      e.stopPropagation();

      if(!window.confirm('이미지를 삭제하시겠습니까?')) return;

      var $wrapper = $(this).closest('.fileUpload__imgWrapper');
      var $img = $wrapper.find('img');
      var $message = $(this).closest('.fileUpload-container').find('.fileUpload__message');
      var $title = $wrapper.closest('.fileUpload-container').find('.fileUpload__title');

      $img.attr('src', '');
      $title.text('');
      $wrapper.hide();
      $title.hide();
      $message.show();

      // 배너이미지변수 삭제
      gbBnrImgPath = '';
      gbBnrImgOriginNm = '';
    });

    // ------------------------------------------------------------------------
    // # 입력제한
    // ------------------------------------------------------------------------
    $('.lengthCheck').on('keyup', function (e) {
      // 길이 제한
      //const $titleLength = $(this).parent().find('.currentInput-length');

      const MAX_LENGTH = !!this.maxLength ? this.maxLength : null;
      const _value = $(this).val();
      if (MAX_LENGTH && _value.length > MAX_LENGTH) {
        $(this).val(_value.slice(0, MAX_LENGTH));
      }
      //$titleLength.innerHTML = $(this).val().length;
      // 문자 제한
      if (this.name.startsWith('textColor')) {
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
    // 한글+영문대소문자+특수문자+숫자+공백 입력제한
    // ------------------------------------------------------------------------
    fnInputValidationForHangulAlphabetNumberSpace("title");
    fnInputValidationForHangulAlphabetNumberSpace("description");

    // ------------------------------------------------------------------------
    // 상시진행
    // ------------------------------------------------------------------------
    $('#alwaysYn').on('click', function (e) {
      //let alwaysYn = $('input:checkbox[name=alwaysYn]').is(':checked');
      let alwaysYn = $('input[name=alwaysYn]:checked').val();
      fnSetExhibitAlwaysYn(alwaysYn);
    });

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 일반DM메일
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 골라담기(균일가)
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ------------------------------------------------------------------------
    // 숫자만입력
    // ------------------------------------------------------------------------
    // * 골라담기 균일가
    fnInputValidationForNumber('selectPrice');

    // ------------------------------------------------------------------------
    // 골라담기 기본구매수량 변경 -> 단가 계산
    // ------------------------------------------------------------------------
    $('#defaultBuyCnt').on('change', function (e) {
      fnCalcUnitPrice('change');

      //if ($('#selectPrice').val() != '') {
        // 골라담기 균일가를 입력했을 경우만 메시지 노출
        fnMessage('', '수량변경 시 단가적용 완료 후 해당정보가 반영됩니다.', '');
        return false;
      //}
    });

    // ------------------------------------------------------------------------
    // 골라담기 균일가 입력 -> 단가 계산
    // ------------------------------------------------------------------------
    $('#selectPrice').on('keyup', function (e) {
      let defaultBuyCnt = $('#defaultBuyCnt').val();
      if (fnIsEmpty(defaultBuyCnt) == true) {
        fnMessage('', '골라담기 기본 구매 수량을 선택하세요', '');
        $('#selectPrice').val('');
        return false;
      }

      fnCalcUnitPrice('change');
    });

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 증정행사
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ------------------------------------------------------------------------
    // 숫자만입력
    // ------------------------------------------------------------------------
    fnInputValidationForNumber('giftCnt');
    fnInputValidationForNumber('overPrice');

    // ------------------------------------------------------------------------
    // 증정행사
    // ------------------------------------------------------------------------
    //$('#useYn').on('click', function (e) {
    //  if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
    //    let useYn = $('input[name=useYn]:checked').val();
    //
    //    if (useYn == 'Y') {
    //      // 노출범위설정(디바이스) : 활성
    //      $("input[name=goodsDisplayType]").attr('disabled', false);          // input text
    //      // 파일선택버튼 : 활성
    //      $('#imgBanner').prop('disabled', false);                            // button
    //      // 파일삭제버튼 : 활성
    //      $('#imgBannerDel').prop('disabled', false);                         // button
    //      // 이벤트상세(PC) : 활성
    //      $($('#descHtml').data().kendoEditor.body).attr('contenteditable', true); // kendoEditor
    //      // 이벤트상세(PC) : 활성
    //      $($('#detlHtmlMo').data().kendoEditor.body).attr('contenteditable', true); // kendoEditor
    //    }
    //    else {
    //      // 노출범위설정(디바이스) : 비활성
    //      $("input[name=goodsDisplayType]").attr('disabled', true);           // input text
    //      // 파일선택버튼 : 비활성
    //      $('#imgBanner').prop('disabled', true);                             // button
    //      // 파일삭제버튼 : 비활성
    //      $('#imgBannerDel').prop('disabled', true);                          // button
    //      // 이벤트상세(PC) : 비활성
    //      $($('#descHtml').data().kendoEditor.body).attr('contenteditable', false); // kendoEditor
    //      // 이벤트상세(PC) : 비활성
    //      $($('#detlHtmlMo').data().kendoEditor.body).attr('contenteditable', false); // kendoEditor
    //    }
    //  }
    //});

    // ------------------------------------------------------------------------
    // 증정조건유형 변경 이벤트 (상품별<->장바구니별)
    // ------------------------------------------------------------------------
    $('#giftTp').on('change', function (e) {

      fnChangeGiftTp();
    });

    // ------------------------------------------------------------------------
    // 증정방식 변경 이벤트 (지정<->랜덤<->선택)
    // ------------------------------------------------------------------------
    $('#giftGiveTp').on('change', function (e) {

      // * 증정방식 텍스트 노출/숨김 처리
      fnChangeGiftGiveTp();
    });

    // ------------------------------------------------------------------------
    // 증정배송유형 변경 이벤트 (합배송<->개별배송)
    // ------------------------------------------------------------------------
    $('#giftShippingTp').on('change', function (e) {

      // 증정배송유형 변경에 따른 텍스트/출고처 노출처리
      fnChangeGiftShippingTp();
    });

    // ------------------------------------------------------------------------
    // 출고처 변경 이벤트
    // ------------------------------------------------------------------------
    $('#urWarehouseId').on('change', function (e) {
      //console.log('# 출고처 변경');
      let giftShippingTp = $('#giftShippingTp').val();  // 증정배송유형 (합배송/개별배송)
      let urWarehouseId = $('#urWarehouseId').val();    // 선택한 출고처ID
      //console.log('# 변경될 출고처ID :: ', urWarehouseId);

      if (giftShippingTp == 'GIFT_SHIPPING_TP.COMBINED') {
        // 증정배송유형 : 합배송

        // 증정상품그리드 조회
        var targetGrid    = $('#giftGoodsGrid').data('kendoGrid');
        var targetGridDs  = targetGrid.dataSource;
        var targetGridArr = targetGridDs.data();

        if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0) {

          let confirmMsg = '배송조건 변경 시 등록된 상품정보가 초기화 됩니다 진행 하시겠습니까?';

          fnKendoMessage({
              message : confirmMsg
            , type    : "confirm"
            , ok      : function() {
                          // 출고처 변수 Set
                          gbSelectedUrWarehouseId = urWarehouseId;

                          // --------------------------------------------------
                          // 증정상품 초기화
                          // --------------------------------------------------
                          targetGrid.destroy();
                          $("#giftGoodsGrid").empty();
                          fnInitGiftGoodsGrid();

                          // --------------------------------------------------
                          // 적용대상상품 초기화
                          // --------------------------------------------------
                          var giftTargetGoodsGrid = $('#giftTargetGoodsGrid').data('kendoGrid');
                          var giftTargetGoodsGridDs  = giftTargetGoodsGrid.dataSource;
                          var giftTargetGoodsGridArr = giftTargetGoodsGridDs.data();
                          if (giftTargetGoodsGridArr != undefined && giftTargetGoodsGridArr != null && giftTargetGoodsGridArr.length > 0) {
                            giftTargetGoodsGrid.destroy();
                            $("#giftTargetGoodsGrid").empty();
                            fnInitGiftTargetGoodsGrid();

                          }
                        }
            , cancel  : function() {
                          // 출고처 되돌림
                          $('#urWarehouseId').data('kendoDropDownList').value(gbSelectedUrWarehouseId);
                        }
          });
        }
        else {
          gbSelectedUrWarehouseId = urWarehouseId;
        }
      }
      else {
        // 출고처 변수 Set
        gbSelectedUrWarehouseId = urWarehouseId;
      }
      //console.log('# gbSelectedUrWarehouseId :: ', gbSelectedUrWarehouseId);
    });

    // ------------------------------------------------------------------------
    // 적용대상유형 변경 이벤트 (상품<->브랜드)
    // ------------------------------------------------------------------------
    $('#giftTargetTp').on('change', function (e) {
      // 전시브랜드 노출 제어
      fnChangeGiftTargetTp();
    });

    // ------------------------------------------------------------------------
    // DM메일전시여부 (예<->아니오)
    // ------------------------------------------------------------------------
    $('#exhibitDispYn').on('click', function (e) {

      var exhibitDispYn = $('input[name=exhibitDispYn]:checked').val(); // DM메일전시여부
      fnChangeDispYn(gbExhibitTp, exhibitDispYn);
    });

    // ------------------------------------------------------------------------
    // 승인요청처리
    // ------------------------------------------------------------------------
    $('#approvalRequestYn').on('click', function (e) {
      fnSetApprovalRequestYn();
    });

    // ------------------------------------------------------------------------
    // 적용대상브랜드유형 변경 이벤트 (표준브랜드<->전시브랜드)
    // ------------------------------------------------------------------------
    //$('#giftTargetBrandTp').on('change', function (e) {
    //
    //  if ($('#giftTargetBrandTp').val() == 'GIFT_TARGET_BRAND_TP.STANDARD') {
    //    // --------------------------------------------------------------------
    //    // 표준브랜드
    //    // --------------------------------------------------------------------
    //    //$('#urBrandIdSpan').show();
    //    $('#dpBrandIdSpan').hide();
    //  }
    //  else if ($('#giftTargetBrandTp').val() == 'GIFT_TARGET_BRAND_TP.DISPLAY') {
    //    // --------------------------------------------------------------------
    //    // 전시브랜드
    //    // --------------------------------------------------------------------
    //    //$('#urBrandIdSpan').hide();
    //    $('#dpBrandIdSpan').show();
    //  }
    //  else {
    //    //$('#urBrandIdSpan').hide();
    //    $('#dpBrandIdSpan').hide();
    //  }
    //});

    // ------------------------------------------------------------------------
    // 그리드 숫자 입력 제한 처리 - class로 지정, 판매가 등
    // ------------------------------------------------------------------------
    $('#ng-view').on('input', '.price-input', function(e) {
      let tempValue = $(e.target).val();
      const regexpFilter = /[^0-9]/g;
      if(tempValue.length > 0) {
        if(tempValue.match(regexpFilter)) {
           $(e.target).val(tempValue.replace(regexpFilter, ''));
        }
      }
    });

  } // End of fnInitEvent()

  // ##########################################################################
  // # 조회 Start
  // ##########################################################################
  // ==========================================================================
  // # 조회
  // ==========================================================================
  function fnSearch() {

    if (gbMode == 'update' && gbDmMailId != null && gbDmMailId != '' && gbDmMailId != 0 && gbDmMailId != '0') {
      // ----------------------------------------------------------------------
      // 수정상태로 진입인 경우
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // DM메일상세조회-기본정보(일반/골라담기/증정행사 공통)
      // ----------------------------------------------------------------------
      fnSearchAjax();
    }

  }

  // ==========================================================================
  // # 조회 Ajax
  // ==========================================================================
  function fnSearchAjax() {

    // ----------------------------------------------------------------------
    // DM메일상세조회-기본정보(일반/골라담기/증정행사 공통)
    // ----------------------------------------------------------------------
    fnAjax({
        url       : '/admin/dm/mail/selectDmMailInfo?dmMailId=' + gbDmMailId            // 주소줄에서 ID 보기위해 params 사용안함
      , method    : 'POST'
      , isAction  : 'select'
      , success   : function(data, status, xhr) {
                      // --------------------------------------------------
                      // 성공 Callback 처리
                      // --------------------------------------------------
                      fnBizCallback('select', data, null);
                      //// 이미 등록된 품목코드인 경우 return
                      //if (data['isRegisterdItemCode'] == true) {
                      //    fnKendoMessage({
                      //        type : 'confirm',
                      //        message : '이미 등록된 품목코드입니다.<br>품목 정보를 수정하시겠습니까?',
                      //        ok : function() {
                      //            fnClose(data);
                      //        },
                      //        cancel : function() {
                      //            fnClear();
                      //        }
                      //    });
                      //}
                      //if (!data['erpItemApiList'] || data['erpItemApiList'].length == 0) { // 조회된 데이터 없는 경우 empty list 출력
                      //    erpItemListGridDs.data([]); // empty list 출력
                      //}
                      //if (data['erpItemApiList']) {
                      //    erpItemListGridDs.data(data['erpItemApiList']); // 그리드에 조회된 데이터 출력
                      //}
                    }
      , error     : function(xhr, status, strError) {
                      //fnKendoMessage({
                      //  message : xhr.responseText
                      //});
                    }
    });

  }

  // ==========================================================================
  // # 조회 - DM메일유형별 상세조회
  // ==========================================================================
  function fnSearchDetail(data) {

    if (data == null || data == 'null' || data == '' ||
        data.detail == null || data.detail == 'null' || data.detail == '' ||
        data.detail.dmMailId == null || data.detail.dmMailId == 'null' || data.detail.dmMailId == '') {

      fnMessage('', 'DM메일 기본정보가 존재하지 않습니다.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // 상세정보-기본정보 Set (공통)
    // ------------------------------------------------------------------------
    fnSetDmMailDetlInfo(data);

    // ------------------------------------------------------------------------
    // DM메일유형별 상세정보
    // ------------------------------------------------------------------------
    fnSearchNormal(data);
    // 활성/비활성 제어
    if (gbMode != 'insert') {
      // 모드:등록
      fnSetScreen(data);
    }
  }

  // ==========================================================================
  // # 상세정보-기본정보 Set (공통)
  // ==========================================================================
  function fnSetDmMailDetlInfo(data) {

    var detail = data.detail;
    // ------------------------------------------------------------------------
    // Binding
    // ------------------------------------------------------------------------
    $('#inputForm').bindingForm( {'rows':detail}, 'rows' );

    // 켄도 에디터 내 스크립트 허용 HGRM-8031
    $("#descHtml").data("kendoEditor").value(detail.description);

    // ------------------------------------------------------------------------
    // Auto Binding 이외 처리
    // ------------------------------------------------------------------------
    // DM메일ID
    $('#dmMailIdSpan').text(detail.dmMailId);

    // 최초등록정보
    $('#createSpan').text(detail.createDt + ' / ' + detail.createNm + '(' + detail.createId + ')');

    // 최근수정내역
    var modifyStr = ' ';

    if (detail.modifyDt != null && detail.modifyDt != '') {
      modifyStr += detail.modifyDt;

      if (detail.modifyNm != null && detail.modifyNm != '') {
        modifyStr += ' / ';
        modifyStr += detail.modifyNm;

        if (detail.modifyId != null && detail.modifyId != '' && detail.modifyId != '0' && detail.modifyId != 0) {
          modifyStr += '(';
          modifyStr += detail.modifyId;
          modifyStr += ')';
        }
      }
    }
    $('#modifySpan').text(modifyStr);

    $('#goodsDispYn').val(detail.goodsDispYn);

    // 진행기간-시작
      let startDtStr = ((detail.sendDt).replace(/:/gi, '')).replace(/ /gi, '');
      let startDe    = '';

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
      }
    //let alwaysYn = $('input:checkbox[name=alwaysYn]').is(':checked');
  }

  // ==========================================================================
  // # 조회 - 일반(그룹)
  // ==========================================================================
  function fnSearchNormal(data) {

    // ------------------------------------------------------------------------
    // 일반(그룹) - 그룹리스트조회
    // ------------------------------------------------------------------------
    fnSearchGroupInfoAjax(data);
  }

  // ==========================================================================
  // # 조회 - 그룹정보
  // ==========================================================================
  function fnSearchGroupInfoAjax(data) {

    var dmMailId = data.detail.dmMailId;

    // ------------------------------------------------------------------------
    // 그룹리스트조회
    // ------------------------------------------------------------------------
    fnAjax({
        url       : '/admin/dm/mail/selectDmMailGroupList'
      , method    : 'POST'
      , params    : {
                      'dmMailId' : dmMailId
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
      if(gbGroupCnt < groupIdx) {
        fnGroupAdd('detail', groupData.dmMailGroupId);
      }

      // --------------------------------------------------------------------
      // 2. 그룹별 기본정보 Set
      // --------------------------------------------------------------------
      fnSetNormalGroupBasicInfo(groupIdx, groupData);

      // --------------------------------------------------------------------
      // 3. 그룹별 상품리스트 Set
      // --------------------------------------------------------------------

      fnSethNormalGroupGoodsList(groupIdx, groupData.dmMailGroupId);
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

  // ==========================================================================
  // # 조회 - 일반(그룹) 기본정보
  // ==========================================================================
  function fnSetNormalGroupBasicInfo(groupIdx, groupData) {

    // 상품그룹명
    $('#groupNm'+groupIdx).val(groupData.groupNm);

    $('input:radio[name="dispPriceTp'+groupIdx+'"]:radio[value="'+groupData.dispPriceTp+'"]').attr('checked',true);
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
  function fnSethNormalGroupGoodsList(groupIdx, dmMailGroupId) {

    // --------------------------------------------------------------------
    // 1. 그룹상품그리드 초기화
    // --------------------------------------------------------------------
    fnInitGroupGoodsGrid(groupIdx, dmMailGroupId);

    // --------------------------------------------------------------------
    // 2. 그리드 조회
    // --------------------------------------------------------------------
    let data = $('#inputForm').formSerialize(true);
    $('#groupGoodsGrid'+groupIdx).data('kendoGrid').dataSource.read(data);
  }

  // # 조회 End
  // ##########################################################################


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
  // # 삭제 - 골라담기.상품
  // ==========================================================================
  function fnBtnExhibitSelectGoodsDel(evExhibitSelectGoodsId) {

    // 모드
    gbMode      = 'delete.select.goods';    // 삭제.골라담기.상품
    // 삭제대상
    gbDelIdArr = new Array() ;
    gbDelIdArr.push(evExhibitSelectGoodsId);

    // 제거Obj Set
    var gridId = 'selectGoodsGrid';
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
  // # 삭제 - 골라담기.추가상품
  // ==========================================================================
  function fnBtnExhibitSelectAddGoodsDel(evExhibitSelectAddGoodsId) {

    // 모드
    gbMode      = 'delete.select.add.goods';    // 삭제.골라담기.추가상품
    // 삭제대상
    gbDelIdArr = new Array() ;
    gbDelIdArr.push(evExhibitSelectAddGoodsId);

    // 제거Obj Set
    var gridId = 'selectAddGoodsGrid';
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
  // # 삭제 - 증정행사.상품
  // ==========================================================================
  function fnBtnExhibitGiftGoodsDel(evExhibitGiftGoodsId) {

    // 모드
    gbMode      = 'delete.gift.goods';    // 삭제.증정행사.상품
    // 삭제대상
    gbDelIdArr = new Array() ;
    gbDelIdArr.push(evExhibitGiftGoodsId);

    // 제거Obj Set
    var gridId = 'giftGoodsGrid';
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
  // # 삭제 - 증정행사.적용대상상품.개별삭제
  // ==========================================================================
  function fnBtnExhibitGiftTargetGoodsDel(dataItem) {

    // 모드
    gbMode      = 'delete.gift.target.goods';    // 삭제.증정행사.적용대상상품

    // 삭제대상
    gbDelIdArr = new Array() ;
    gbDelIdArr.push(dataItem.evExhibitGiftTargetGoodsId);

    // 제거Obj Set
    var gridId = 'giftTargetGoodsGrid';
    var grid     = $('#'+gridId).data('kendoGrid');
    //var dataItem      = grid.dataItem(grid.select());
    //var ds          = grid.dataSource;
    var dataObj = new Object();
    dataObj.selectRow         = grid.select();
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
  // # 삭제 - 증정행사.적용대상상품.다중삭제
  // ==========================================================================
  function fnBtnGiftTargetGoodsDelMulti(dataItemArr) {
    //console.log('# 적용대상상품 다중삭제 Start');
    // 모드
    gbMode = 'delete.gift.target.goods';

    // 대상그리드 Ds
    var grid = $('#giftTargetGoodsGrid').data('kendoGrid');
    var ds   = grid.dataSource;

    // 선택된 rows
    var selectRows  = $("#giftTargetGoodsGrid").find('input[name=giftTargetGoodsCheck]:checked').closest('tr');
    // 제거대상 rows (화면에서 제거)
    var removeRowArr   = new Array();
    // 제거대상 정보 (제거대상row, gridId)
    var removeObj      = new Object();

    if (selectRows != undefined && selectRows != null && selectRows.length > 0) {

      // 삭제대상 evExhibitGiftTargetGoodsId List 초기화 : 전역변수
      gbDelIdArr = new Array() ;

      for (var i = 0; i < selectRows.length; i++) {

        var selectedDataItem = grid.dataItem($(selectRows[i]));

        var dataObj = new Object();
        dataObj.selectRow         = selectRows[i];
        dataObj.selectedDataItem  = selectedDataItem;

        // 1. 제거대상 리스트 : 그리드에서 지워야할 제거/삭제 리스트
        removeRowArr.push(dataObj);

        if (selectedDataItem != undefined && selectedDataItem != null && selectedDataItem.tempDataYn != undefined && selectedDataItem.tempDataYn != null && selectedDataItem.tempDataYn == 'Y') {
          // 그리드 임시 로우
          // grid.removeRow($(selectRows[i]));
        }
        else {
          // 그리드 DB 로우
          // 2. DB 삭제대상 Array Set : 전역변수
          gbDelIdArr.push(selectedDataItem.evExhibitGiftTargetGoodsId);
        }

      } // End of for (var i = 0; i < selectRows.length; i++)
      removeObj.removeRowArr  = removeRowArr;
      removeObj.gridId        = 'giftTargetGoodsGrid';

      // 삭제 실행
      fnSaveUpd(removeObj, null);

    } // End of if (selectRows != undefined && selectRows != null && selectRows.length > 0)
    else {
      fnMessage('', '삭제할 대상을 선택해주세요.', '');
      return false;
    }


  }

  // ==========================================================================
  // # 삭제 - 증정행사.적용대상브랜드
  // ==========================================================================
  function fnBtnExhibitGiftTargetBrandDel(evExhibitGiftTargetBrandId) {

    // 모드
    gbMode      = 'delete.gift.target.brand';    // 삭제.증정행사.적용대상브랜드
    // 대상
    gbDelIdArr = new Array() ;
    gbDelIdArr.push(evExhibitGiftTargetBrandId);

    // 제거Obj Set
    var gridId = 'giftTargetBrandGrid';
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
  // # 변경 - 증정행사.대표상품
  // ==========================================================================
  function fnBtnRepGoodsUpd(dataItem) {

    // 모드
    gbMode      = 'update.gift.repGoods';    // 변경.증정행사.대표상품
    // 변경 실행
    fnSaveUpd(null, dataItem);
  }

  // ==========================================================================
  // # 변경/삭제 처리
  // ==========================================================================
  function fnSaveUpd(removeObj, dataItem) {

    var url         = '';
    var inParam     = '';
    var confirmMsg  = '';
    var isExeAjax   = false;

    //if (gbMode == 'delete.group.detl') {
    //  // 삭제.상품그룹.상품
    //  url = '/admin/pm/exhibit/delExhibitGroupDetl';
    //  inParam = {'evExhibitGroupDetlIdListString'  : JSON.stringify(gbDelIdArr)};
    //  confirmMsg  = '<div>삭제하시겠습니까?</div>';
    //  // Ajax실행여부 (DB처리여부)
    //  if (gbDelIdArr != undefined && gbDelIdArr != null && gbDelIdArr.length > 0) {
    //    isExeAjax   = true;
    //  }
    //}
    //else
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
  // ##########################################################################
  // ==========================================================================
  // # 저장
  // ==========================================================================
  function fnSave() {

    var confirmMsg  = '';
    var url         = '';
    var cbId        = gbMode;
    var isAction    = '';
    var param;
    //var dataObj;

    var dmMailDataObj = new Object();   // 반환 obj
    var dmMailData    = new Object();
    var data;

    var resultGroupCheck;               //  상품그룹 Validataion 결과
    var groupList;

    // ************************************************************************
    // 1. form Set - 기본정보
    // ************************************************************************
    data = $('#inputForm').formSerialize(true);

    // ------------------------------------------------------------------------
    // 1.1. DM메일ID
    // ------------------------------------------------------------------------
    if ('insert' == gbMode) {
      data.dmMailId = '';
    }
    else if ('update' == gbMode) {
      data.dmMailId = gbDmMailId;
    }

    // ************************************************************************
    // 2. 기본정보 Validation
    // ************************************************************************
    let resultCheck = fnCheckValidBasic();
    //console.log('# resultCheck :: ', resultCheck);
    if (!resultCheck) {
      return false;
    }

    // ************************************************************************
    // 3. DM메일 기본정보 Set
    // ************************************************************************
    // ------------------------------------------------------------------------
    // @ 3.1.1. 데이터 Set - DM메일 기본정보
    // ------------------------------------------------------------------------
    dmMailData.dmMailId         = gbDmMailId;
    dmMailData.dmMailTemplateTp = data.mailTemplateTp;
    dmMailData.title            = data.title;
    dmMailData.description      = data.descHtml;
    dmMailData.goodsDispYn      = data.goodsDispYn;
    dmMailData.sendDt           = data.startDe;

    dmMailDataObj.dmMailData    = dmMailData;

    // ************************************************************************
    // 4. DM메일유형별 정보
    // ************************************************************************
    // ------------------------------------------------------------------------
    // 4.1. 상세정보 Set
    // ------------------------------------------------------------------------
    // ----------------------------------------------------------------------
    // 일반DM메일 상세 정보
    // ----------------------------------------------------------------------
    dmMailDataObj = fnSetParamValueNormal(dmMailDataObj, data);

    // ----------------------------------------------------------------------
    // Param Set
    // ----------------------------------------------------------------------
    param = {
              "dmMailDataJsonString" : JSON.stringify(dmMailDataObj.dmMailData)
            , "groupListJsonString"   : JSON.stringify(dmMailDataObj.dmMailNormalData.groupList)
            };

    // ----------------------------------------------------------------------
    // 그룹리스트
    // ----------------------------------------------------------------------
    groupList = dmMailDataObj.dmMailNormalData.groupList;

    // ************************************************************************
    // 5. Validation Check - 그리드/그룹정보
    // ************************************************************************
    if('Y' == data.goodsDispYn) {
      let resultCheckDetail = fnCheckValidGrid(data, dmMailDataObj, groupList);
      //console.log('# resultCheckDetail :: ', resultCheckDetail);
      if (resultCheckDetail == false) {
        return false;
      }
    }

    // ************************************************************************
    // 실행
    // ************************************************************************
    if (gbMode == 'insert') {
      // ----------------------------------------------------------------------
      // 저장
      // ----------------------------------------------------------------------
      confirmMsg  = '저장하시겠습니까?';
      url         = '/admin/dm/mail/addDmMail';
      isAction    = 'insert';
    }
    else if (gbMode == 'update') {
      // ----------------------------------------------------------------------
      // 수정
      // ----------------------------------------------------------------------
      confirmMsg = '수정된 정보가 적용되며, HTML이 변경됩니다.<br />진행하시겠습니까?';
      url         = '/admin/dm/mail/putDmMail';
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
            , params  : param
            //, params  : pageData
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
  // # 등록/수정 데이터 Set - 일반DM메일
  // ==========================================================================
  function fnSetParamValueNormal(dmMailDataObj, data) {

    let dmMailNormalData  = {}; // 일반DM메일

    // 그룹 Set
    //exhibitNormalData.groupListJsonString = groupList;
    dmMailNormalData.groupList       = fnSetGroupData(data.goodsDispYn);

    // ------------------------------------------------------------------------
    // 반환 : DM메일데이터 + 일반DM메일데이터
    // ------------------------------------------------------------------------
    dmMailDataObj.dmMailNormalData = dmMailNormalData;
    return dmMailDataObj;
  }

  // ==========================================================================
  // 그룹 데이터 Set - 일반DM메일/증정행사 용
  // ==========================================================================
  function fnSetGroupData(dispYn) {

    var groupList         = new Array();  // 그룹리스트
    var groupData;                        // 그룹정보
    var goodsList;                        // 그룹별 상품리스트
    var groupGridArr;                     // 그룹별 상품그리드
    var orderNo           = 1;

    // ------------------------------------------------------------------------
    // 그룹별 Loop
    // ------------------------------------------------------------------------
    $(".groupTable").each(function(){

      // 그룹별 초기화
      groupData = new Object();
      goodsList = new Array();

      // 그룹정보
      var self = $(this);
      var idx = self.data("groupId");

      // ----------------------------------------------------------------------
      // 필수체크
      // ----------------------------------------------------------------------
      // TODO
      // $('#inputForm input[name=exhibitTp]'        ).prop("required", true);
      // 그룹 row별 그룹사용여부
      // 그룹 row별 상품그룹명 배경 설정
      // 그룹 row별 상품그룹명 배경 설정 = 배경컬러 -> 컬러코드 필수
      // 그룹 row별 전시 상품 수
      // 그룹 row별 그룹 전시 순서
      // 그룹 row별 그리드 1개 이상 존재

      // ----------------------------------------------------------------------
      // 그룹별 기본정보
      // ----------------------------------------------------------------------
      groupData.groupIdx      = idx;
      groupData.groupNm       = $('#groupNm'+idx).val();
      groupData.textColor     = $('#textColor'+idx).val();
      groupData.groupUseYn    = $('input[name=groupUseYn'+idx+']:checked').val();
      groupData.exhibitImgTp  = $('input[name=exhibitImgTp'+idx+']:checked').val();
      groupData.bgCd          = $('#bgCd'+idx).val();
      groupData.groupDesc     = $('#groupDesc'+idx).val();
      groupData.dispCnt       = $('#dispCnt'+idx).val();
      groupData.groupSort     = $('#groupSort'+idx).val();
      groupData.dispPriceTp   = $('input[name=dispPriceTp'+idx+']:checked').val();

      // ----------------------------------------------------------------------
      // 그룹별 그룹상품정보
      // ----------------------------------------------------------------------
      groupGridArr = $('#groupGoodsGrid' + idx).data('kendoGrid').dataSource.data();

      if (groupGridArr != undefined && groupGridArr != null && groupGridArr.length > 0) {

        //var goodsData;

        for (var i = 0; i < groupGridArr.length; i++) {

          var goodsData = new Object();
          goodsData.ilGoodsId = groupGridArr[i].ilGoodsId;
          goodsData.goodsSort = groupGridArr[i].goodsSort;

          goodsList.push(goodsData);

        } // End of for (var i = 0; i < goodsGridArr.length; i++)
        groupData.groupGoodsList = goodsList;
        groupData.groupGoodsListJsonString = JSON.stringify(goodsList);
        //groupData.groupGoodsListJsonString = goodsList;

      } // End of if (goodsGridArr != undefined && goodsGridArr != null && goodsGridArr.length > 0)

      if('N' == dispYn  && (groupData.groupNm == undefined || groupData.groupNm == null || groupData.groupNm == ''
          || groupData.groupSort == undefined || groupData.groupSort == null || groupData.groupSort == ''
          || groupGridArr == undefined || groupGridArr == null || groupGridArr.length == 0)) {

      } else {
        // 상품그룹 add
        groupList.push(groupData);
      }

      orderNo++;
    }); // End of $(".groupTable").each(function(){

    // ------------------------------------------------------------------------
    // 반환 : DM메일데이터 + 골라담기
    // ------------------------------------------------------------------------
    return groupList;
  }

  // # 저장 End
  // ##########################################################################

  // ##########################################################################
  // # 삭제 Start
  // ##########################################################################
  // ==========================================================================
  // # 삭제
  // ==========================================================================
  function fnDelete() {

    gbMode = 'delete';
    let delDmMailIdArr = new Array() ;
    delDmMailIdArr.push(gbDmMailId);

    // 삭제 실행
    const url = '/admin/dm/mail/delDmMail';

    if (delDmMailIdArr == undefined || delDmMailIdArr == null ||
        delDmMailIdArr == '' || delDmMailIdArr.length <= 0) {

      fnMessage('', '삭제대상 DM메일을 확인해주세요.', '');
      return false;
    }

    // ----------------------------------------------------------------------
    // 삭제 Validataion 체크
    // ----------------------------------------------------------------------
    var isCheck = false;

    for (var i = 0; i < delDmMailIdArr.length; i++) {

      isCheck = fnChecDelkValidationExhibit(gbDetail)

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

    let confirmMsg = 'DM이 삭제되며, 복구할 수 없습니다.<br/>진행하시겠습니까?';
    //console.log('#>>>>> JSON.stringify(delEvExhibitIdArr) :: ', JSON.stringify(delEvExhibitIdArr));

    // ----------------------------------------------------------------------
    // 서비스에서의 처리 사항
    // 1. ExhibitManageRequestDto.java 에 String evExhibitIdListString 와 List<String> evExhibitIdListList 선언
    // 2. ExhibitManageController.java 에서 exhibitManageRequestDto.setEvExhibitIdList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getEvExhibitIdListString(), String.class)); 처리
    // ----------------------------------------------------------------------
    let inParam = {"dmMailIdListString"  : JSON.stringify(delDmMailIdArr)};

    fnKendoMessage({message : confirmMsg, type : "confirm" , ok : function(){
      fnAjax({
          url     : url
        , params  : inParam
        , success : function( result ){
          fnBizCallback(gbMode, result);
        }
      , isAction : gbMode
      });

    }});
  }

  // ==========================================================================
  // # DM메일 수정/삭제 Validation Check
  // ==========================================================================
  function fnChecDelkValidationExhibit(detail) {

    var useYn     = '';
    var statusSe  = '';

    // ------------------------------------------------------------------------
    // 상세조회 결과 체크 - DM메일정보, DM메일유형, 사용여부, 진행상태
    // ------------------------------------------------------------------------
    if (detail == undefined || detail == 'undefined' || detail == null || detail == 'null' || detail == '') {
      fnMessage('', 'DM메일 정보가 존재하지 않습니다.', '');
      return false;
    }


    // ------------------------------------------------------------------------
    // DM메일유형별 삭제 Validataion Check 호출
    // ------------------------------------------------------------------------
    // 일반DM메일
    return true;
  }


  // ==========================================================================
  // # DM메일 삭제 Validation Check - 골라담기
  // ==========================================================================
  function fnCheckDelValidationExhibitSelect(detail) {

    // ------------------------------------------------------------------------
    // 상세조회 결과 체크 - 승인상태
    // ------------------------------------------------------------------------
    if (detail.approvalStatus == undefined || detail.approvalStatus == 'undefined' || detail.approvalStatus == null || detail.approvalStatus == 'null' || detail.approvalStatus == '') {
      fnMessage('', '승인상태 정보를 확인하세요.', '');
      return false;
    }

    if (detail.approvalStatus == 'APPR_STAT.NONE') {
      // **********************************************************************
      // 1. 승인상태(승인대기(저장)) - 사용여부
      // **********************************************************************
      if (detail.useYn == 'Y') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.[SAVE-Y]', '');
          return false;
        }
      }
      else if (detail.useYn == 'N') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(미사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.[SAVE-N]', '');
          return false;
        }
      }
      else {
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
    }
    else if (detail.approvalStatus == 'APPR_STAT.REQUEST' || detail.approvalStatus == 'APPR_STAT.SUB_APPROVED') {
      // **********************************************************************
      // 2. 승인상태(승인요청) - 사용여부
      // **********************************************************************
      if (detail.useYn == 'Y') {
        // --------------------------------------------------------------------
        // 승인상태(승인요청) - 사용여부(사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 불가
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else if (detail.useYn == 'N') {
        // --------------------------------------------------------------------
        // 승인상태(승인요청) - 사용여부(미사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 불가
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else {
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
    }
    else if (detail.approvalStatus == 'APPR_STAT.CANCEL') {
      // **********************************************************************
      // 3. 승인상태(요청철회) - 사용여부
      // **********************************************************************
      if (detail.useYn == 'Y') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else if (detail.useYn == 'N') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(미사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else {
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
    }
    else if (detail.approvalStatus == 'APPR_STAT.DENIED') {
      // **********************************************************************
      // 4. 승인상태(승인반려) - 사용여부
      // **********************************************************************
      if (detail.useYn == 'Y') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else if (detail.useYn == 'N') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(미사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else {
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
    }
    else if (detail.approvalStatus == 'APPR_STAT.APPROVED') {
      // **********************************************************************
      // 5. 승인상태(승인완료) - 사용여부
      // **********************************************************************
      if (detail.useYn == 'Y') {
        // --------------------------------------------------------------------
        // 승인상태(승인완료) - 사용여부(사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 불가
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else if (detail.useYn == 'N') {
        // --------------------------------------------------------------------
        // 승인상태(승인완료) - 사용여부(미사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 불가
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else {
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
    }
    else {
      fnMessage('', '유효하지 않은 승인상태입니다.', '');
      return false;
    }
  }

  // ==========================================================================
  // # DM메일 수정/삭제 Validation Check - 증정행사
  // ==========================================================================
  function fnCheckDelValidationExhibitGift(detail) {

    // 골라담기와 동일하여 골라담기 호출
    return fnCheckDelValidationExhibitSelect(detail);
  }


  // # 삭제 End
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
        // --------------------------------------------------------------------
        // 진행상태 전역 변수 Set
        // --------------------------------------------------------------------
        //console.log('# gbEditableMode(a) :: ', gbEditableMode);
//        gbEditableMode      = fnCheckEditableMode(gbExhibitTp, approvalStatus, statusSe);
        //console.log('# gbEditableMode(b) :: ', gbEditableMode);

        // --------------------------------------------------------------------
        // 상시진행여부에 따른 기간/사용자동종료 활성/비활성 처리
        // --------------------------------------------------------------------
        //let alwaysYn = data.detail.alwaysYn;

        // --------------------------------------------------------------------
        // 조회결과 Set
        // --------------------------------------------------------------------
        gbDetail = data.detail;

        // --------------------------------------------------------------------
        // DM메일 유형별 상세정보 조회
        // --------------------------------------------------------------------
        fnSearchDetail(data);

        break;
      case 'insert':
        // --------------------------------------------------------------------
        // 등록
        // --------------------------------------------------------------------
        // DM메일ID Set
        gbDmMailId = data.detail.dmMailId;
        // 목록 이동 시 1페이지로 감
        sessionStorage.setItem('lastPage', "1");
        // 모드 Set
        gbMode        = 'update';

        fnKendoMessage({
            message : '등록되었습니다.'
          , ok      : function() {
                        // 수정화면 이동 (자신)
                        fnGoDmMailEdit();
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
            message : 'DM이 삭제되었습니다.'
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
            message : 'DM이 수정되었습니다.'
          , ok      : function(){
                        // 수정 후 재조회
                        // 재조회
                        fnReSearch();
                        // 승인요청 체크박스 언체크
                        // $('input:checkbox[name="approvalRequestYn"]').prop('checked', false);
                      }
        });

        break;
      case 'update.gift.repGoods':
        // --------------------------------------------------------------------
        // 수정.증정행사.대표상품
        // --------------------------------------------------------------------
        // 모드 변경
        gbMode = 'update';
        // 그리드 재조회
        let giftRepGoodsFormData = $('#inputForm').formSerialize(true);
        $('#giftGoodsGrid').data('kendoGrid').dataSource.read(giftRepGoodsFormData);

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
      case 'delete.select.goods':
        // --------------------------------------------------------------------
        // 삭제.골라담기.상품
        // --------------------------------------------------------------------
        // 모드 변경
        gbMode = 'update';
        // 그리드 재조회
        //let selectGoodsFormData = $('#inputForm').formSerialize(true);
        //$('#selectGoodsGrid').data('kendoGrid').dataSource.read(selectGoodsFormData);
        // 그리드 Row 제거
        gridId = 'selectGoodsGrid';
        fnGridRowRemove(removeObj, gridId);

        break;
      case 'delete.select.add.goods':
        // --------------------------------------------------------------------
        // 삭제.골라담기.추가상품
        // --------------------------------------------------------------------
        // 모드 변경
        gbMode = 'update';
        // 그리드 재조회
        //let selectAddGoodsFormData = $('#inputForm').formSerialize(true);
        //$('#selectAddGoodsGrid').data('kendoGrid').dataSource.read(selectAddGoodsFormData);
        // 그리드 Row 제거
        gridId = 'selectAddGoodsGrid';
        fnGridRowRemove(removeObj, gridId);

        break;
      case 'delete.gift.goods':
        // --------------------------------------------------------------------
        // 삭제.증정행사.상품
        // --------------------------------------------------------------------
        // 모드 변경
        gbMode = 'update';
        // 그리드 재조회
        //let giftGoodsFormData = $('#inputForm').formSerialize(true);
        //$('#giftGoodsGrid').data('kendoGrid').dataSource.read(giftGoodsFormData);
        // 그리드 Row 제거
        gridId = 'giftGoodsGrid';
        fnGridRowRemove(removeObj, gridId);

        break;
      case 'delete.gift.target.goods':
        // --------------------------------------------------------------------
        // 삭제.증정행사.적용대상상품
        // --------------------------------------------------------------------
        // 모드 변경
        gbMode = 'update';
        // 그리드 재조회
        //let giftTargetGoodsFormData = $('#inputForm').formSerialize(true);
        //$('#giftTargetGoodsGrid').data('kendoGrid').dataSource.read(giftTargetGoodsFormData);
        // 그리드 Row 제거
        gridId = 'giftTargetGoodsGrid';
        fnGridRowRemove(removeObj, gridId);

        break;
      case 'delete.gift.target.brand':
        // --------------------------------------------------------------------
        // 삭제.증정행사.적용대상브랜드
        // --------------------------------------------------------------------
        // 모드 변경
        gbMode = 'update';
        // 그리드 재조회
        //let giftTargetBrandFormData = $('#inputForm').formSerialize(true);
        //$('#giftTargetBrandGrid').data('kendoGrid').dataSource.read(giftTargetBrandFormData);
        // 그리드 Row 제거
        gridId = 'giftTargetBrandGrid';
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
  function fnGoDmMailEdit() {

    // ------------------------------------------------------------------------
    // 수정 화면으로 이동
    // ------------------------------------------------------------------------
    // 링크정보
    let option = {};
    option.url    = '#/dmMailMgm';
    // DM메일 등록/수정 : 100008059 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    option.menuId = 1392;
    option.target = '_self';
    option.data = { dmMailId : gbDmMailId, mode : 'update'};
    // 화면이동
    fnGoPage(option);
  }

  // ==========================================================================
  // # 수정 후 재조회
  // ==========================================================================
  function fnReSearch() {
    // ------------------------------------------------------------------------
    // 1. DM메일유형별 초기화
    // ------------------------------------------------------------------------
    // ----------------------------------------------------------------------
    // 1.1. 일반DM메일
    // ----------------------------------------------------------------------
    // 1.1.1. 그룹정보 삭제
    $('[id ^= groupTable]').remove();
    // 1.1.2. 그룹 index 정보 초기화
    gbGroupIdx = 0;
    gbGroupCnt = 0;

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
  function fnGroupAdd(mode, dmMailGroupId) {
      let groupIdx = gbGroupIdx + 1;
      let groupCnt = gbGroupCnt + 1;

    var $target = $('#group-btnArea');
    var tpl = $('#groupTpl').html();
    // IDX 치환
    tpl = tpl.replace(/{IDX}/g, groupIdx);
    // EV_EXHIBIT_GROUP_ID 치환
    //if (evExhibitGroupId != null) {
    tpl = tpl.replace(/{DM_MAIL_GROUP_ID}/g, dmMailGroupId);
    //}

  //$(tpl).insertBefore($target);
    $(tpl).appendTo($('#addAreaDiv'));

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 객체생성
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ------------------------------------------------------------------------
    // 사용여부(R)
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'dispPriceTp' + groupIdx
      , tagId   : 'dispPriceTp' + groupIdx
      , url     : '/admin/comn/getCodeList'
      , params  : {'stCommonCodeMasterCode' : 'DISP_PRICE_TP', 'useYn' :'Y'}
      , async       : false
      , isDupUrl    : 'Y'
      , chkVal  : 'DISP_PRICE_TP.EMPLOYEE_PRICE'
      , style   : {}
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
    fnInitGroupGoodsGrid(groupIdx, dmMailGroupId);

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
    document.location.href = "/contents/excelsample/dmMail/dmMailGroupUploadSample.xlsx"
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
          url       : '/admin/pm/exhibit/selectGoodsInfoList'
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
  function fnBtnGroupCopy(oriGroupIdx, dmMailGroupId) {
    //alert('# evExhibitGroupId :: ' + evExhibitGroupId);

    if (gbGroupCnt >= MAX_GROUP_CNT) {
      fnMessage('', '상품그룹은 ' + MAX_GROUP_CNT + '개까지만 생성할 수 있습니다.', '');
      return false;
    }

    // 그룹 생성
    fnGroupAdd('copy', dmMailGroupId);

    // 그룹 항목값 Copy
    fnCopyGroupValue(oriGroupIdx, dmMailGroupId);
  }

  // ==========================================================================
  // # 상품그룹-항목값Copy
  // ==========================================================================
  function fnCopyGroupValue(oriGroupIdx, dmMailGroupId) {

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
    var exhibitImgTpVal = $('input[name=exhibitImgTp'+oriGroupIdx+']:checked').val();
    $('input:radio[name=exhibitImgTp'+targetGroupIdx+']:input[value="'+exhibitImgTpVal+'"]').prop('checked', true);
    if (exhibitImgTpVal == 'EXHIBIT_IMG_TP.NOT_USE') {
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
    // if (evExhibitGroupId != null) {
    // ------------------------------------------------------------------------
    // DB 조회는 일단 사용하지 않음
    // ------------------------------------------------------------------------
    //let data = $('#inputForm').formSerialize(true);
    //$('#groupGoodsGrid'+targetGroupIdx).data('kendoGrid').dataSource.read(data);
    $('input:radio[name="dispPriceTp'+targetGroupIdx+'"]:radio[value="'+$('input[name=dispPriceTp'+oriGroupIdx+']:checked').val()+'"]').attr('checked',true);


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
  function fnInitGroupGoodsGrid(groupIdx, dmMailGroupId, groupGoodsDataList) {
    var callUrl          = '';
    let groupGoodsGridDs;

    // 페이징없는 그리드
    if(groupGoodsDataList == undefined || groupGoodsDataList == null) {
        groupGoodsGridDs = fnGetDataSource({
            url      : '/admin/dm/mail/selectDmMailGroupGoodsList?dmMailGroupId='+dmMailGroupId
        });
    } else {
        groupGoodsGridDs = groupGoodsDataList;
    }

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
    // $('#groupGoodsGrid'+groupIdx).initializeKendoGrid( groupGoodsGridOpt ).data('kendoGrid');
    //var groupGoodsGrid = $('#groupGoodsGrid'+groupIdx).initializeKendoGrid( groupGoodsGridOpt ).cKendoGrid();

    var groupGoodsGrid = $('#groupGoodsGrid'+groupIdx).initializeKendoGrid(groupGoodsGridOpt).data('kendoGrid');

    // ------------------------------------------------------------------------
    // dataBound 처리
    // ------------------------------------------------------------------------
    groupGoodsGrid.bind('dataBound', function() {

      // ----------------------------------------------------------------------
      // 그리드 내 활성/비활성 처리
      // ----------------------------------------------------------------------
      // if (gbMode == 'update') {
      //   // 수정모드(상세조회)인 경우
      //   if (gbM == 'EXHIBIT_TP.GIFT') {
      //     // 증정행사인 경우만
      //     if (gbEditableMode == 'N') {
      //       // 상품그룹 그리드 삭제버튼
      //       $('[name=btnGroupGoodsDel]').attr('disabled', 'disabled');                   // button
      //     }
      //   }
      // }
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
        fnBtnExhibitGroupDetlDel(dataItem.evExhibitGroupDetlId, groupIdx);
      }

    });
  }

  // --------------------------------------------------------------------------
  // 그리드-골라담기(균일가)-대표상품
  // --------------------------------------------------------------------------
  function fnInitSelectTargetGoodsGrid() {

    var callUrl               = '';

    // 페이징없는 그리드
    selectTargetGoodsGridDs = fnGetDataSource({
      url      : '/admin/pm/exhibit/selectfExhibitSelectInfo?evExhibitId='+gbDmMailId
    });

    var bEditableGrid = gbBeditable;
    bEditableGrid = false;

    selectTargetGoodsGridOpt = {
          dataSource  : selectTargetGoodsGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : false
        , height      : 'auto'
        //, height      : '120'
        , selectable  : true
        , editable    : bEditableGrid
        , resizable   : true
        , autobind    : false
        // 임시 툴바 Start
        //,toolbar      : [
        //                  { name: 'create', text: '신규', className: "btn-point btn-s"}
        //                , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
        //                , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
        //                ]
        // 임시 툴바 End
        , columns     : [
                          { field : 'goodsTpNm'       , title : '상품유형'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'displayYnNm'     , title : '노출여부'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                if (dataItem.resetYn != undefined && dataItem.resetYn != null && dataItem.resetYn == 'Y') {
                                                                                                  // 단가적용 버튼 처리 인 경우
                                                                                                  return dataItem.displayYnNm;
                                                                                                }
                                                                                                else {
                                                                                                  // 단가적용 아닌 경우
                                                                                                  if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
                                                                                                    // 상품추가인 경우
                                                                                                    return '';
                                                                                                  }
                                                                                                  else {
                                                                                                    // 조회 결과 처리인 경우
                                                                                                    var salePrice     = Number(dataItem.salePrice);
                                                                                                    var defaultBuyCnt = Number(dataItem.defaultBuyCnt);
                                                                                                    var selectPrice   = Number($('#selectPrice').val());
                                                                                                    var unitPrice = Math.ceil(selectPrice / defaultBuyCnt);

                                                                                                    // 판매가 > 단가
                                                                                                    if (salePrice > unitPrice) {
                                                                                                      return '노출';
                                                                                                    }
                                                                                                    else {
                                                                                                      return '노출불가';
                                                                                                    }
                                                                                                  }
                                                                                                }
                                                                                              }
                          }
                        , { field : 'ilGoodsId'       , title : '상품코드'      , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        , { field : 'goodsNm'         , title : '상품명'        , width:  'auto', attributes : {style : 'text-align:left;'  }, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                let imageUrl = dataItem.goodsImagePath ? publicStorageUrl + dataItem.goodsImagePath : '/contents/images/noimg.png';
                                                                                                return '<img src="' + imageUrl + '" width="50" height="50" align="left" /><BR>&nbsp;&nbsp;' + dataItem.goodsNm ;
                                                                                              }
                          }
                        , { field : 'warehouseNm'       , title : '출고처명'      , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'shippingTemplateName', title : '배송정책'    , width: '320px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'standardPrice'   , title : '원가'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
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
                        , { field : 'recommendedPrice', title : '정상가'        , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , format: '{0:\#\#,\#}'
                                                                                //, template  : function(dataItem) {
                                                                                //                if (dataItem != undefined && dataItem != null && dataItem.recommendedPrice != undefined && dataItem.recommendedPrice != null && dataItem.recommendedPrice != '') {
                                                                                //                  return String(dataItem.recommendedPrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                                                                                //                }
                                                                                //                else {
                                                                                //                  return '';
                                                                                //                }
                                                                                //              }
                          }
                        , { field : 'salePrice'       , title : '판매가'        , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , format: '{0:\#\#,\#}'
                                                                                //, template  : function(dataItem) {
                                                                                //                if (dataItem != undefined && dataItem != null && dataItem.salePrice != undefined && dataItem.salePrice != null && dataItem.salePrice != '') {
                                                                                //                  return String(dataItem.salePrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                                                                                //                }
                                                                                //                else {
                                                                                //                  return '';
                                                                                //                }
                                                                                //              }
                          }
                        , { field : 'tempDataYn'      , title : '임시데이터'    , width:   '1px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : "ilShippingTmplId",  title : "배송정책ID", hidden : true }
                        , { field : "urWarehouseId", title : "출고처 ID", hidden : true }
                        , { field : 'management'      , title : '관리'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                return  '<div class="btn-area textCenter">'
                                                                                                      + '<button type="button" name="btnAddGoodsPopupSelectTargetGoodsEdit" class="btn-point btn-s" onclick="$scope.fnBtnAddGoodsPopupSelectTargetGoods();">변경</button>'
                                                                                                      + '</div>';
                                                                                              }
                          }
                        ]
        //, rowTemplate : kendo.template($('#rowTemplate').html())
    };

    selectTargetGoodsGrid = $('#selectTargetGoodsGrid').initializeKendoGrid( selectTargetGoodsGridOpt ).data('kendoGrid');
    //$('#selectTargetGoodsGrid').initializeKendoGrid( selectTargetGoodsGridOpt ).cKendoGrid();
    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    selectTargetGoodsGrid.bind('dataBound', function() {
      // 페이징 없음
      //var row_num = selectTargetGoodsGridDs._total;
      var row_num = selectTargetGoodsGridDs.data().length;
      $('#selectTargetGoodsGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      //$('#totalCnt').text(exhibitGridDs._total);

      // ----------------------------------------------------------------------
      // 그리드 내 활성/비활성 처리
      // ----------------------------------------------------------------------
      if (gbMode == 'update') {
        // 수정모드(상세조회)인 경우
        if (gbEditableMode == 'N') {
          // 상품그룹 그리드 변경버튼
          $('[name=btnAddGoodsPopupSelectTargetGoodsEdit]').attr('disabled', 'disabled');                        // button
        }
        else if (gbEditableMode == 'P') {
          // 부분수정가능
          // 상품그룹 그리드 변경버튼
          $('[name=btnAddGoodsPopupSelectTargetGoodsEdit]').attr('disabled', 'disabled');                        // button

        }
      }



    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    //$('#selectTargetGoodsGrid').on("click", "button[kind=btnSelectTargetGoodsDel]", function(e) {
    //  e.preventDefault();
    //  // 골라담기.대표상품 삭제 : 그리드에서만 삭제한다.
    //  fnKendoMessage( {
    //                    message : fnGetLangData({key :'', nullMsg : '<div>그리드에서 제거됩니다.</div><div>제거하시겠습니까?</div>' })
    //                  , type    : 'confirm'
    //                  , ok      : function(){
    //                                let dataItem = selectTargetGoodsGrid.dataItem($(e.currentTarget).closest('tr'));
    //                                selectTargetGoodsGridDs.remove(dataItem);
    //                                //fnMessage('', '제거되었습니다.', '');
    //                              }
    //  });
    //});
  }

  // --------------------------------------------------------------------------
  // 그리드-골라담기(균일가)-상품
  // --------------------------------------------------------------------------
  function fnInitSelectGoodsGrid() {
    var callUrl               = '';

    // 페이징없는 그리드
    selectGoodsGridDs = fnGetDataSource({
      url      : '/admin/pm/exhibit/selectExhibitSelectGoodsList?evExhibitId='+gbDmMailId
    });

    var bEditableGrid = gbBeditable;
    bEditableGrid = false;

    selectGoodsGridOpt = {
          dataSource  : selectGoodsGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : false
        //, height      : 'auto'  // auto 설정일 경우 scrollable false 설정 필요
        , selectable  : true
        //, editable    : "incell"    // 컬럼에디터
        , editable    : {
                          confirmation: false
                        //, confirmDelete: "Yes"
                        }
        , resizable   : true
        , autobind    : false
        // 임시 툴바 Start
        //,toolbar      : [
        //                  { name: 'create', text: '신규', className: "btn-point btn-s"}
        //                , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
        //                , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
        //                ]
        // 임시 툴바 End
        , columns     : [
                          { field : 'goodsSort'       , title : '순번'          , width:  '40px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return true;}, editor: sortEditor}
                        , { field : 'goodsTpNm'       , title : '상품유형'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'displayYnNm'     , title : '노출여부'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                if (dataItem.resetYn != undefined && dataItem.resetYn != null && dataItem.resetYn == 'Y') {
                                                                                                  // 단가적용 버튼 처리 인 경우
                                                                                                  return dataItem.displayYnNm;
                                                                                                }
                                                                                                else {
                                                                                                  // 단가적용 아닌 경우
                                                                                                  if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
                                                                                                    // 상품추가인 경우
                                                                                                    return '';
                                                                                                  }
                                                                                                  else {
                                                                                                    // 조회 결과 처리인 경우
                                                                                                    var salePrice     = Number(dataItem.salePrice);             // 판매가
                                                                                                    var defaultBuyCnt = Number($('#defaultBuyCnt').val());      // 기본구매수량
                                                                                                    var selectPrice   = Number($('#selectPrice').val());        // 골라담기균일가
                                                                                                    var unitPrice     = Math.ceil(selectPrice / defaultBuyCnt); // 골라담기단가

                                                                                                    // --------------------------------------------------------------------------
                                                                                                    // 골다담기상품 노출불가 기준
                                                                                                    // --------------------------------------------------------------------------
                                                                                                    // 1. 판매가가 단가 이하
                                                                                                    // 2. 배송정책이 대표상품의 배송정책과 다를 경우
                                                                                                    // 3. 출고처가 대표상품과 다를 경우 (촐고처는 상품 선택 시 동일한 것만 선택 됨)
                                                                                                    //console.log('# 배송정책 :: [', dataItem.ilShippingTmplId, '][', dataItem.targetIlShippingTmplId, ']');
                                                                                                    if ((salePrice > unitPrice) && (dataItem.ilShippingTmplId == dataItem.targetIlShippingTmplId)) {
                                                                                                      return '노출';
                                                                                                    }
                                                                                                    else {
                                                                                                      return '노출불가';
                                                                                                    }
                                                                                                  }
                                                                                                }
                                                                                              }
                          }
                        , { field : 'ilGoodsId'       , title : '상품코드'      , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        , { field : 'goodsNm'         , title : '상품명'        , width:  'auto', attributes : {style : 'text-align:left;'  }, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                let imageUrl = dataItem.goodsImagePath ? publicStorageUrl + dataItem.goodsImagePath : '/contents/images/noimg.png';
                                                                                                return '<img src="' + imageUrl + '" width="50" height="50" align="left" /><BR>&nbsp;&nbsp;' + dataItem.goodsNm ;
                                                                                              }
                          }
                        , { field : 'warehouseNm'     , title : '출고처명'      , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'shippingTemplateName', title : '배송정책'  , width: '320px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'standardPrice'   , title : '원가'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, format: '{0:\#\#,\#}'}
                        , { field : 'recommendedPrice', title : '정상가'        , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, format: '{0:\#\#,\#}'}
                        , { field : 'salePrice'       , title : '판매가'        , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, format: '{0:\#\#,\#}'}
                        , { field : 'tempDataYn'      , title : '임시데이터'    , width:   '1px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : 'management'      , title : '관리'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
                                                                                                  return  '<div class="btn-area textCenter">'
                                                                                                        + '<button type="button" class="btn-white btn-s" kind="btnSelectGoodsDel">제거</button>'
                                                                                                        + '</div>';
                                                                                                }
                                                                                                else {
                                                                                                  return  '<div class="btn-area textCenter">'
                                                                                                        + '<button type="button" name="btnSelectGoodsDel" class="btn-red btn-s" kind="btnSelectGoodsDel">삭제</button>'
                                                                                                        + '</div>';
                                                                                                }

                                                                                              }
                          }
                        ]
      //, rowTemplate : kendo.template($('#rowTemplate').html())
    };

    selectGoodsGrid = $('#selectGoodsGrid').initializeKendoGrid( selectGoodsGridOpt ).data('kendoGrid');
    //$('#selectGoodsGrid').initializeKendoGrid( selectGoodsGridOpt ).cKendoGrid();

    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    selectGoodsGrid.bind('dataBound', function() {
      // 페이징 없음
      var row_num = selectGoodsGridDs.data().length;
      $('#selectGoodsGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      //$('#totalCnt').text(exhibitGridDs._total);

      // ----------------------------------------------------------------------
      // 그리드 내 활성/비활성 처리
      // ----------------------------------------------------------------------
      if (gbMode == 'update') {
        // 수정모드(상세조회)인 경우
        if (gbEditableMode == 'N') {
          // 상품그룹 그리드 삭제버튼
          $('[name=btnSelectGoodsDel]').attr('disabled', 'disabled');                        // button
        }
      }
    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#selectGoodsGrid').on("click", "button[kind=btnSelectGoodsDel]", function(e) {

      // Grid가 N개 생성되므로 아래와 같이 그리드 가져와야 함
      var grid =  $('#selectGoodsGrid').data('kendoGrid');
      e.preventDefault();
      let dataItem = selectGoodsGrid.dataItem($(e.currentTarget).closest("tr"));

      // 그리드 삭제에서 사용하기 위한 행 선택 설정
      grid.select($(e.currentTarget).closest("tr"));

      if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
        // 임시데이터이므로 그리드에서만 제거
        fnKendoMessage( {
            message : fnGetLangData({key :'', nullMsg : '<div>그리드에서 제거됩니다.</div><div>제거하시겠습니까?</div>' })
          , type    : 'confirm'
          , ok      : function(){
                        selectGoodsGridDs.remove(dataItem);
                        //fnMessage('', '제거되었습니다.', '');
                      }
        });
      }
      else {
        // 골라담기.상품 삭제 호출
        fnBtnExhibitSelectGoodsDel(dataItem.evExhibitSelectGoodsId);
      }

    });
  }

  // --------------------------------------------------------------------------
  // 그리드-골라담기(균일가)-추가상품
  // --------------------------------------------------------------------------
  function fnInitSelectAddGoodsGrid() {
    var callUrl               = '';

    // 페이징없는 그리드
    selectAddGoodsGridDs = fnGetDataSource({
      url      : '/admin/pm/exhibit/selectExhibitSelectAddGoodsList?evExhibitId='+gbDmMailId
    });

    var bEditableGrid = gbBeditable;
    bEditableGrid = false;

    let isEditableSalePrice = true;

    if (gbMode == 'update') {
      // 수정모드(상세조회)인 경우
      if (gbEditableMode == 'N') {
        // 상품그룹 그리드 삭제버튼
        isEditableSalePrice = false;
      }
    }



    selectAddGoodsGridOpt = {
          dataSource  : selectAddGoodsGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : false
        //, height      : 'auto'  // auto 설정일 경우 scrollable false 설정 필요
        , selectable  : true
        , editable    : {
                          confirmation: false
                        //, confirmDelete: "Yes"
                        }
        , resizable   : true
        , autobind    : false
        // 임시 툴바 Start
        //,toolbar      : [
        //                  { name: 'create', text: '신규', className: "btn-point btn-s"}
        //                , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
        //                , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
        //                ]
        // 임시 툴바 End
        , columns     : [
                        // 2021.04.13 - HGRM-7378 : 골라담기 추가상품 노출여부 항목 제거
                        //{ field : 'displayYnNm'     , title : '노출여부'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                        //                                                        , template  : function(dataItem) {
                        //                                                                        if (dataItem.resetYn != undefined && dataItem.resetYn != null && dataItem.resetYn == 'Y') {
                        //                                                                          // 단가적용 버튼 처리 인 경우
                        //                                                                          return dataItem.displayYnNm;
                        //                                                                        }
                        //                                                                        else {
                        //                                                                          // 단가적용 아닌 경우
                        //                                                                          if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
                        //                                                                            // 상품추가인 경우
                        //                                                                            return '';
                        //                                                                          }
                        //                                                                          else {
                        //                                                                            // 조회 결과 처리인 경우
                        //                                                                            var salePrice     = Number(dataItem.salePrice);             // 판매가
                        //                                                                            var defaultBuyCnt = Number($('#defaultBuyCnt').val());      // 기본구매수량
                        //                                                                            var selectPrice   = Number($('#selectPrice').val());        // 골라담기균일가
                        //                                                                            var unitPrice     = Math.ceil(selectPrice / defaultBuyCnt); // 골라담기단가
                        //
                        //                                                                            // --------------------------------------------------------------------------
                        //                                                                            // 골라담기 추가상품 노출불가 기준
                        //                                                                            // --------------------------------------------------------------------------
                        //                                                                            // 1. 판매가가 단가 이하
                        //                                                                            // 2. 출고처가 대표상품과 다를 경우 (촐고처는 상품 선택 시 동일한 것만 선택 됨)
                        //                                                                            //console.log('# 배송정책 :: [', dataItem.ilShippingTmplId, '][', dataItem.targetIlShippingTmplId, ']');
                        //                                                                            // 판매가 > 골라담기단가
                        //                                                                            if (salePrice > unitPrice) {
                        //                                                                              return '노출';
                        //                                                                            }
                        //                                                                            else {
                        //                                                                              return '노출불가';
                        //                                                                            }
                        //                                                                          }
                        //                                                                        }
                        //                                                                      }
                        //  }
                        //,
                          { field : 'ilGoodsId'       , title : '상품코드'      , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        //, { field : 'goodsImagePath'  , title : '이미지경로'    , width: '100px', attributes : {style : 'text-align:left;'}  , editable:function (dataItem) {return false;}}
                        , { field : 'goodsNm'         , title : '상품명'        , width:  'auto', attributes : {style : 'text-align:left;'  }, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                let imageUrl = dataItem.goodsImagePath ? publicStorageUrl + dataItem.goodsImagePath : '/contents/images/noimg.png';
                                                                                                return '<img src="' + imageUrl + '" width="50" height="50" align="left" /><BR>&nbsp;&nbsp;' + dataItem.goodsNm ;
                                                                                              }
                          }
                        , { field : 'warehouseNm'     , title : '출고처명'      , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        //, { field : 'shippingTemplateName', title : '배송정책'  , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'standardPrice'   , title : '원가'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, format: '{0:\#\#,\#}'}
                        , { field : 'recommendedPrice', title : '정상가'        , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, format: '{0:\#\#,\#}'}
                        , { field : 'salePrice'       , title : '판매가'        , width:  '70px', attributes : {style : 'text-align:center;'}
                                                                                , editable:function (dataItem) {
                                                                                    gGridSalePrice = dataItem.salePrice;
                                                                                    return isEditableSalePrice;
                                                                                }, format: '{0:\#\#,\#}'
                                                                                , editor  : function(container, options){
                                                                                              // 숫자만 입력 제한
                                                                                              var $input = $('<input class="price-input salePrice-input" name="' + options.field + '"/>');
                                                                                              $input.appendTo(container);

                                                                                              // ----------------------------------------------------------------------
                                                                                              // 정상가/판매가 가격비교
                                                                                              // ----------------------------------------------------------------------
                                                                                              $("[name='salePrice']").blur(function(e) {
                                                                                                  var grid        =  $('#selectAddGoodsGrid').data('kendoGrid');
                                                                                                  var dataSource  = grid.dataSource;

                                                                                                  let dataItem = grid.dataItem($(e.currentTarget).closest("tr"));
                                                                                                  let recommendedPrice = dataItem.recommendedPrice;
                                                                                                  let salePrice = $(e.target).val();

                                                                                                  if (Number(salePrice) > Number(recommendedPrice)) {
                                                                                                    //console.log('# 가격비교 완료...');
                                                                                                    // 가격 초기화
                                                                                                    // grid.dataItem(grid.select()).set('salePrice', '');
                                                                                                    grid.dataItem(grid.select()).set('salePrice', gGridSalePrice);
                                                                                                    // 선택해제 (기능 확인 중)
                                                                                                    grid.clearSelection();
                                                                                                    // 메시지 노출
                                                                                                    fnMessage('', '판매가는 정상가를 초과할 수 없습니다.', '');
                                                                                                  }
                                                                                              });
                                                                                            }
                                                                                , template: function(dataItem){
                                                                                              // 금액 콤마(,) 적영
                                                                                              return fnNumberWithCommas(dataItem.salePrice);
                                                                                            }
                          }
                        , { field : 'tempDataYn'      , title : '임시데이터'    , width:   '1px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : 'management'      , title : '관리'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
                                                                                                  return  '<div class="btn-area textCenter">'
                                                                                                        + '<button type="button" class="btn-white btn-s" kind="btnSelectAddGoodsDel">제거</button>'
                                                                                                        + '</div>';
                                                                                                }
                                                                                                else {
                                                                                                  return  '<div class="btn-area textCenter">'
                                                                                                          + '<button type="button" name="btnSelectAddGoodsDel" class="btn-red btn-s" kind="btnSelectAddGoodsDel">삭제</button>'
                                                                                                          + '</div>';
                                                                                                }
                                                                                              }
                          }
                        ]
      //, rowTemplate : kendo.template($('#rowTemplate').html())
    };

    selectAddGoodsGrid = $('#selectAddGoodsGrid').initializeKendoGrid( selectAddGoodsGridOpt ).data('kendoGrid');
    //$('#selectAddGoodsGrid').initializeKendoGrid( selectAddGoodsGridOpt ).cKendoGrid();

    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    selectAddGoodsGrid.bind('dataBound', function() {
      // 페이징 없음
      var row_num = selectAddGoodsGridDs.data().length;
      $('#selectAddGoodsGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      //$('#totalCnt').text(exhibitGridDs._total);

      // ----------------------------------------------------------------------
      // 그리드 내 활성/비활성 처리
      // ----------------------------------------------------------------------
      if (gbMode == 'update') {
        // 수정모드(상세조회)인 경우
        if (gbEditableMode == 'N') {
          // 상품그룹 그리드 삭제버튼
          $('[name=btnSelectAddGoodsDel]').attr('disabled', 'disabled');                        // button
        }
      }
    });

    // ------------------------------------------------------------------------
    // 판매가 포커스아웃 이벤트 : 정상가와 판매가 비교
    // ------------------------------------------------------------------------
    //$('#selectAddGoodsGrid').on('focusout', "input[name=salePrice]", function(e) {
    //$('#selectAddGoodsGrid').on('blur', "input[name=salePrice]", function(e) {
    //  console.log(e.target);
    //
    //  e.stopPropagation();
    //
    //  console.log('# 포커스아웃');
    //  var grid =  $('#selectAddGoodsGrid').data('kendoGrid');
    //  e.preventDefault();
    //  let dataItem = selectAddGoodsGrid.dataItem($(e.currentTarget).closest("tr"));
    //  console.log('# dataItem :: ', JSON.stringify(dataItem));
    //
    //  let recommendedPrice = dataItem.recommendedPrice;
    //  console.log('# recommendedPrice :: ', recommendedPrice);
    //  let salePrice = dataItem.salePrice;
    //  console.log('# input salePrice  :: ', salePrice);
    //
    //  if (Number(salePrice) > Number(recommendedPrice)) {
    //    console.log('# 가격비교 완료...');
    //    // 값 지우기
    //    const $target = $(e.target);
    //    $target.val('');
    //    //grid.clearSelection();
    //    fnMessage('', '추가상품 판매가가 정상가보다 높습니다. 입력한 판매가를 확인하세요.', '');
    //    return false;
    //  }
    //});
    // ------------------------------------------------------------------------
    // 판매가 엔터키 이벤트 : 정상가 판매가 비교
    // ------------------------------------------------------------------------
    //$('#selectAddGoodsGrid').on('keydown', "input[name=salePrice]", function(e) {
    //  if (e.keyCode == 13) {
    //    console.log('# 엔터키');
    //    var grid =  $('#selectAddGoodsGrid').data('kendoGrid');
    //    grid.clearSelection();
    //    //$(e.target).trigger('blur');
    //    //return;
    //  }
    //});

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#selectAddGoodsGrid').on('click', 'button[kind=btnSelectAddGoodsDel]', function(e) {

      // Grid가 N개 생성되므로 아래와 같이 그리드 가져와야 함
      var grid =  $('#selectAddGoodsGrid').data('kendoGrid');
      e.preventDefault();
      let dataItem = selectAddGoodsGrid.dataItem($(e.currentTarget).closest("tr"));

      // 그리드 삭제에서 사용하기 위한 행 선택 설정
      grid.select($(e.currentTarget).closest("tr"));

      if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
        // 임시데이터이므로 그리드에서만 제거
        fnKendoMessage( {
            message : fnGetLangData({key :'', nullMsg : '<div>그리드에서 제거됩니다.</div><div>제거하시겠습니까?</div>' })
          , type    : 'confirm'
          , ok      : function(){
                        selectAddGoodsGridDs.remove(dataItem);
                        //fnMessage('', '제거되었습니다.', '');
                      }
        });
      }
      else {
        // 골라담기.추가상품 삭제 호출
        fnBtnExhibitSelectAddGoodsDel(dataItem.evExhibitSelectAddGoodsId);
      }
    });
  }

  // --------------------------------------------------------------------------
  // 그리드-증정행사-상품
  // --------------------------------------------------------------------------
  function fnInitGiftGoodsGrid() {

    var callUrl               = '';

    // 페이징없는 그리드
    giftGoodsGridDs = fnGetDataSource({
      url      : '/admin/pm/exhibit/selectExhibitGiftGoodsList?evExhibitId='+gbDmMailId
    });

    var bEditableGrid = gbBeditable;

    giftGoodsGridOpt = {
          dataSource  : giftGoodsGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : true
        //, height      : 'auto'  // auto 설정일 경우 scrollable false 설정 필요
        , selectable  : true
        , editable    : {
                          confirmation: false
                        //, confirmDelete: "Yes"
                        }
        , resizable   : true
        , autobind    : false
        // 임시 툴바 Start
        //,toolbar      : [
        //                  { name: 'create', text: '신규', className: "btn-point btn-s"}
        //                , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
        //                , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
        //                ]
        // 임시 툴바 End
        , columns     : [
                          { field : 'repGoodsYn'      , title : '대표상품'      , width:  '70px', attributes: {style: "text-align:center;" }, editable:function (dataItem) {return false;}
                                                                                //, headerTemplate : "<input type='checkbox' id='repGoodsYnAll' />"
                                                                                , template :  function(dataItem) {

                                                                                                if (dataItem.repGoodsYn == 'Y') {
                                                                                                  return '대표상품';
                                                                                                }
                                                                                                else {
                                                                                                  if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
                                                                                                    return '';
                                                                                                  }
                                                                                                  else {
                                                                                                    return  '<div class="btn-area textCenter">'
                                                                                                          + '<button type="button" name="btnRepGoodsUpd" class="btn-point btn-s" kind="btnRepGoodsUpd">변경</button>'
                                                                                                          + '</div>';
                                                                                                  }
                                                                                                }

                                                                                                //var checked = '';
                                                                                                //if (dataItem.repGoodsYn == 'Y') {
                                                                                                //  checked = 'checked';
                                                                                                //}
                                                                                                //return '<input type="checkbox" name="repGoodsYn" class="couponGridChk" '+ checked + '/>'
                                                                                              }
                                                                                , locked: true, lockable: false
                          }
                        , { field : 'goodsSort'       , title : '지급순위'      , width:  '60px', attributes : {style : 'text-align:center;'}, editor: fnGiftGridEditorEvent
                                                                                , locked: true
                                                                                //, locked: true, lockable: false
                          }
                        , { field : 'ilGoodsId'       , title : '상품코드'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}
                                                                                , locked: true
                          }
                        , { field : 'goodsNm'         , title : '상품명'        , width: '300px', attributes : {style : 'text-align:left;'  }, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                let imageUrl = dataItem.goodsImagePath ? publicStorageUrl + dataItem.goodsImagePath : '/contents/images/noimg.png';
                                                                                                return '<img src="' + imageUrl + '" width="50" height="50" align="left" /><BR>&nbsp;&nbsp;' + dataItem.goodsNm ;
                                                                                              }
                                                                                , locked: true
                          }
                        , { field : 'giftCnt'         , title : '증정기본수량'  , width:  '90px', attributes : {style : 'text-align:center;'}, editor: fnGiftGridEditorEvent
                                                                                , locked: true
                          }
                        , { field : 'urBrandNm'       , title : '표준브랜드'    , width: '160px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'dpBrandNm'       , title : '전시브랜드'    , width: '160px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'ctgryFullNm'     , title : '카테고리'      , width: '280px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'warehouseNm'     , title : '출고처명'      , width: '140px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                if (dataItem.storeYn == 'N') {
                                                                                                  return dataItem.warehouseNm;
                                                                                                }
                                                                                                else {
                                                                                                  return '';
                                                                                                }
                                                                                              }
                          }
                        , { field : 'shippingTmplNm'  , title : '배송정책'      , width: '180px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                if (dataItem.storeYn == 'N' &&
                                                                                                    dataItem.urWarehouseId != null && dataItem.urWarehouseId != '' &&
                                                                                                    dataItem.urWarehouseId != '0'  && dataItem.urWarehouseId != 0) {
                                                                                                  return dataItem.shippingTmplNm;
                                                                                                }
                                                                                                else {
                                                                                                  return '';
                                                                                                }
                                                                                              }
                          }
                        , { field : 'standardPrice'   , title : '원가'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                if (dataItem != undefined && dataItem != null && dataItem.standardPrice != undefined && dataItem.standardPrice != null && dataItem.standardPrice != '') {
                                                                                                  // TODO replace 확인
                                                                                                  //return (dataItem.standardPrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                                                                                                  return dataItem.standardPrice;
                                                                                                }
                                                                                                else {
                                                                                                  return '';
                                                                                                }
                                                                                              }
                          }
                        , { field : 'recommendedPrice', title : '정상가'        , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                if (dataItem != undefined && dataItem != null && dataItem.recommendedPrice != undefined && dataItem.recommendedPrice != null && dataItem.recommendedPrice != '') {
                                                                                                  // TODO replace 확인
                                                                                                  //return (dataItem.recommendedPrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                                                                                                  return dataItem.recommendedPrice;
                                                                                                }
                                                                                                else {
                                                                                                  return '';
                                                                                                }
                                                                                              }
                          }
                        //, { field : 'salePrice'       , title : '판매가'        , width:  '50px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                        //                                                        , template  : function(dataItem) {
                        //                                                                        return (dataItem.salePrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                        //                                                                      }
                        //  }
                        , { field : 'tempDataYn'      , title : '임시데이터'    , width:   '1px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : "ilShippingTmplId",  title : "배송정책ID", hidden : true }
                        , { field : "urWarehouseId", title : "출고처 ID", hidden : true }
                        , { field : "undeliverableAreaTp", title : "도서산간/제주배송", hidden : true }
                        , { field : 'management'      , title : '관리'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                , template  : function(dataItem) {
                                                                                                if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
                                                                                                  return  '<div class="btn-area textCenter">'
                                                                                                        + '<button type="button" class="btn-white btn-s" kind="btnGiftGoodsDel">제거</button>'
                                                                                                        + '</div>';
                                                                                                }
                                                                                                else {
                                                                                                  return  '<div class="btn-area textCenter">'
                                                                                                        + '<button type="button" name="btnGiftGoodsDel" class="btn-red btn-s" kind="btnGiftGoodsDel">삭제</button>'
                                                                                                        + '</div>';
                                                                                                }
                                                                                              }
                                                                                , lockable: false
                          }
                      ]
      //, rowTemplate : kendo.template($('#rowTemplate').html())
    };
    giftGoodsGrid = $('#giftGoodsGrid').initializeKendoGrid( giftGoodsGridOpt ).data('kendoGrid');
    //$('#giftGoodsGrid').initializeKendoGrid( giftGoodsGridOpt ).cKendoGrid();
    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    giftGoodsGrid.bind('dataBound', function() {
      // 페이징 없음
      var row_num = giftGoodsGridDs.data().length;

      if (row_num > 0) {

        $('#giftGoodsGrid tbody > tr .row-number').each(function(index){
          $(this).html(row_num);
          row_num--;
        });
        // --------------------------------------------------------------------
        // 전체건수 Set
        // --------------------------------------------------------------------
        //$('#totalCnt').text(exhibitGridDs._total);

        // --------------------------------------------------------------------
        // 출고처
        // --------------------------------------------------------------------
        gbUrWarehouseId = giftGoodsGridDs.data()[0].urWarehouseId;
        gbWarehouseNm   = giftGoodsGridDs.data()[0].warehouseNm;
        gbStoreYn       = giftGoodsGridDs.data()[0].storeYn
        $('#warehouseNmSpan').text(gbWarehouseNm);

        // --------------------------------------------------------------------
        // 배송정책 가져오기 - 모든 로우가 동일한 값이어서 첫번째 로우것을 가져옴
        // --------------------------------------------------------------------
        if (gbStoreYn == 'N' &&
            gbUrWarehouseId != null && gbUrWarehouseId != '' &&
            gbUrWarehouseId != '0'  && gbUrWarehouseId != 0) {

          gbIlShippingTmplId      = giftGoodsGridDs.data()[0].ilShippingTmplId;
          gbShippingTmplNm        = giftGoodsGridDs.data()[0].shippingTmplNm;
          $('#shippingTemplateNmSpan').text(gbShippingTmplNm);
        }
        else {
          gbIlShippingTmplId      = '';
          gbShippingTmplNm        = '';
          $('#shippingTemplateNmSpan').text('');
        }

        // --------------------------------------------------------------------
        // 배송불가지역유형(도서산간제주배송) 가져오기 - 모든 로우가 동일한 값이어서 첫번째 로우것을 가져옴
        // --------------------------------------------------------------------
        gbUndeliverableAreaTp   = giftGoodsGridDs.data()[0].undeliverableAreaTp;

        if (gbUndeliverableAreaTp == 'UNDELIVERABLE_AREA_TP.NONE') {
          gbUndeliverableAreaTpNm = '가능';
        }
        else {
          gbUndeliverableAreaTpNm = '불가';
        }
        $('#undeliverableAreaTpNmSpan').text(gbUndeliverableAreaTpNm);

        // --------------------------------------------------------------------
        // * 체크박스-전체선택/해제
        // --------------------------------------------------------------------
        //$("#repGoodsYnAll").on("click",function(index){
        //  // 개별체크박스 처리
        //  if($("#repGoodsYnAll").prop("checked") == true){
        //    // 전체체크
        //    // 개별체크 선택
        //    $('INPUT[name=repGoodsYn]').prop("checked", true);
        //  }
        //  else{
        //    // 전체해제
        //    // 개별체크 선택 해제
        //    $('INPUT[name=repGoodsYn]').prop("checked",false);
        //  }
        //});
        // --------------------------------------------------------------------
        // * 체크박스-개별선택/해제
        // --------------------------------------------------------------------
        //$('#ng-view').on("click","input[name=repGoodsYn]",function(index){
        //
        //  //giftGoodsGrid = $('#giftGoodsGrid').initializeKendoGrid( giftGoodsGridOpt ).data('kendoGrid');
        //  //giftGoodsGrid = $("#giftGoodsGrid").data("kendoGrid");
        //
        //  // ------------------------------------------------------------------
        //  // 싱글선택인 경우
        //  // ------------------------------------------------------------------
        //  let row = $(this).closest("tr");
        //  let rowIdx = $(this).closest("tr").index();
        //  let rowData = giftGoodsGrid.dataItem(row);
        //  let checkedCnt = 0;
        //
        //  $("input[name=repGoodsYn]").each(function(index, item) {
        //    if( rowIdx != index ){
        //      $(this).prop("checked", false);
        //    }
        //  }); // End of $("input[name=repGoodsYn]").each(
        //
        //  // ------------------------------------------------------------------
        //  // 체크된것 해제 시 다시 체크 처리 : 반듯이 한건은 체크
        //  // ------------------------------------------------------------------
        //  if ($(this).is(':checked') == true) {
        //    checkedCnt++;
        //  }
        //  if (checkedCnt <= 0) {
        //    $(this).prop("checked", true);
        //  }
        //
        //  // ------------------------------------------------------------------
        //  // 멀티선택인 경우
        //  // ------------------------------------------------------------------
        //  //const totalCnt    = $("input[name=repGoodsYn]").length;
        //  //const checkedCnt  = $("input[name=repGoodsYn]:checked").length;
        //  //// 전체체크박스 처리
        //  //if (totalCnt == checkedCnt) {
        //  //  $('#repGoodsYnAll').prop("checked", true);
        //  //}
        //  //else {
        //  //  $('#repGoodsYnAll').prop("checked", false);
        //  //}
        //
        //
        //});

        // --------------------------------------------------------------------
        // 그리드 내 활성/비활성 처리
        // --------------------------------------------------------------------
        if (gbEditableMode == 'N' || gbEditableMode == 'P') {
          // 증정상품그리드-대표상품변경
          $('[name=btnRepGoodsUpd]').attr('disabled', 'disabled');                    // button
          // 증정상품그리드-삭제버튼
          $('[name=btnGiftGoodsDel]').attr('disabled', 'disabled');                   // button
        }

      }
    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#giftGoodsGrid').on("click", "button[kind=btnGiftGoodsDel]", function(e) {

      // Grid가 N개 생성되므로 아래와 같이 그리드 가져와야 함
      var grid =  $('#giftGoodsGrid').data('kendoGrid');
      e.preventDefault();
      let dataItem = giftGoodsGrid.dataItem($(e.currentTarget).closest("tr"));

      // 그리드 삭제에서 사용하기 위한 행 선택 설정
      grid.select($(e.currentTarget).closest("tr"));

      // 대표상품 체크
      if (dataItem.repGoodsYn != undefined && dataItem.repGoodsYn != null && dataItem.repGoodsYn == 'Y') {

        fnMessage('', '대표상품은 삭제가 불가능합니다.', '');
        return false;
      }

      if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
        // 임시데이터이므로 그리드에서만 제거
        fnKendoMessage( {
            message : fnGetLangData({key :'', nullMsg : '<div>그리드에서 제거됩니다.</div><div>제거하시겠습니까?</div>' })
          , type    : 'confirm'
          , ok      : function(){
                        giftGoodsGridDs.remove(dataItem);
                        //fnMessage('', '제거되었습니다.', '');
                      }
        });
      }
      else {
        // 증정행사.상품 삭제 호출
        fnBtnExhibitGiftGoodsDel(dataItem.evExhibitGiftGoodsId);
      }

    });

    // ------------------------------------------------------------------------
    // 대표상품 변경 버튼 클릭 : 즉시 처리
    // ------------------------------------------------------------------------
    $('#giftGoodsGrid').on("click", "button[kind=btnRepGoodsUpd]", function(e) {
      e.preventDefault();
      let dataItem = giftGoodsGrid.dataItem($(e.currentTarget).closest("tr"));
      // 대표상품변경처리 호출
      fnBtnRepGoodsUpd(dataItem);
    });

  }

  // --------------------------------------------------------------------------
  // 그리드-증정행사-적용대상-상품
  // --------------------------------------------------------------------------
  function fnInitGiftTargetGoodsGrid() {

    var callUrl               = '';

    // 페이징없는 그리드
    giftTargetGoodsGridDs = fnGetDataSource({
      url      : '/admin/pm/exhibit/selectExhibitGiftTargetGoodsList?evExhibitId='+gbDmMailId
    });

    var bEditableGrid = gbBeditable;

    giftTargetGoodsGridOpt = {
          dataSource  : giftTargetGoodsGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : false
        //, height      : 'auto'  // auto 설정일 경우 scrollable false 설정 필요
        , selectable  : true
        , editable    : {
                          confirmation: false
                        //, confirmDelete: "Yes"
                        }
        , resizable   : true
        , autobind    : false
        // 임시 툴바 Start
        //,toolbar      : [
        //                  { name: 'create', text: '신규', className: "btn-point btn-s"}
        //                , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
        //                , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
        //                ]
        // 임시 툴바 End
        , columns     : [
                          { field : 'chk'                                           , width:  '40px', attributes: {style: "text-align:center;" }, editable:function (dataItem) {return false;}
                                                                                    , headerTemplate : "<input type='checkbox' id='giftTargetGoodsCheckAll' />"
                                                                                    , template :  function(dataItem) {
                                                                                                    return '<input type="checkbox" name="giftTargetGoodsCheck" class="couponGridChk" />'
                                                                                                  }
                          }
                        , { field : 'targetEnableYn'  , title : '대상가능여부'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    if ($('#giftShippingTp').val() == 'GIFT_SHIPPING_TP.COMBINED') {
                                                                                                      if (dataItem.urWarehouseId        == gbUrWarehouseId &&
                                                                                                          dataItem.storeYn              == gbStoreYn &&
                                                                                                          dataItem.ilShippingTmplId     == gbIlShippingTmplId &&
                                                                                                          dataItem.undeliverableAreaTp  == gbUndeliverableAreaTp ) {
                                                                                                        return '가능';
                                                                                                      }
                                                                                                      else {
                                                                                                        return '불가';
                                                                                                      }
                                                                                                    }
                                                                                                    else {
                                                                                                      return '가능';
                                                                                                    }
                                                                                                  }
                          }
                        , { field : 'ilGoodsId'       , title : '상품코드'          , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        , { field : 'goodsNm'         , title : '상품명'            , width: '220px', attributes : {style : 'text-align:left;'  }, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    let imageUrl = dataItem.goodsImagePath ? publicStorageUrl + dataItem.goodsImagePath : '/contents/images/noimg.png';
                                                                                                    return '<img src="' + imageUrl + '" width="50" height="50" align="left" /><BR>&nbsp;&nbsp;' + dataItem.goodsNm ;
                                                                                      }
                          }
                        , { field : 'urBrandNm'       , title : '표준브랜드'        , width: '160px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'dpBrandNm'       , title : '전시브랜드'        , width: '160px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'warehouseNm'     , title : '출고처명'          , width: '140px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    if (dataItem.storeYn == 'N') {
                                                                                                      return dataItem.warehouseNm;
                                                                                                    }
                                                                                                    else {
                                                                                                      return '';
                                                                                                    }
                                                                                                  }
                          }
                        , { field : 'shippingTmplNm'  , title : '배송정책'          , width: '200px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    if (dataItem.storeYn == 'N' &&
                                                                                                        dataItem.urWarehouseId != null && dataItem.urWarehouseId != '' &&
                                                                                                        dataItem.urWarehouseId != '0'  && dataItem.urWarehouseId != 0) {
                                                                                                      return dataItem.shippingTmplNm;
                                                                                                    }
                                                                                                    else {
                                                                                                      return '';
                                                                                                    }
                                                                                                  }
                          }
                        , { field : 'undeliverableAreaTp'
                                                      , title : '도서산간<BR>제주배송' , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    if (dataItem.undeliverableAreaTp == 'UNDELIVERABLE_AREA_TP.NONE') {
                                                                                                      return '가능';
                                                                                                    }
                                                                                                    else {
                                                                                                      return '불가';
                                                                                                    }
                                                                                                  }
                          }
                        , { field : 'tempDataYn'      , title : '임시데이터'        , width:   '1px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : 'management'      , title : '관리'              , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
                                                                                                      //
                                                                                                      return  '<div class="btn-area textCenter">'
                                                                                                            + '<button type="button" class="btn-white btn-s" kind="btnGiftTargetGoodsDel">제거</button>'
                                                                                                            + '</div>';
                                                                                                    }
                                                                                                    else {
                                                                                                      return  '<div class="btn-area textCenter">'
                                                                                                            + '<button type="button" name="btnGiftTargetGoodsDel" class="btn-red btn-s btnGiftTargetGoodsDel" kind="btnGiftTargetGoodsDel">삭제</button>'
                                                                                                            + '</div>';
                                                                                                    }
                                                                                                  }
                          }
                        ]
      //, rowTemplate : kendo.template($('#rowTemplate').html())
    };

    giftTargetGoodsGrid = $('#giftTargetGoodsGrid').initializeKendoGrid( giftTargetGoodsGridOpt ).data('kendoGrid');
    //$('#giftTargetGoodsGrid').initializeKendoGrid( giftTargetGoodsGridOpt ).cKendoGrid();

    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    giftTargetGoodsGrid.bind('dataBound', function() {
      // 페이징 없음
      var row_num = giftTargetGoodsGridDs.data().length;
      $('#giftTargetGoodsGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      //$('#totalCnt').text(exhibitGridDs._total);

      // --------------------------------------------------------------------
      // 그리드 내 활성/비활성 처리
      // --------------------------------------------------------------------
      if (gbEditableMode == 'N') {
        // 적용대상그리드-상품삭제버튼
        $('[name=btnGiftTargetGoodsDel]').attr('disabled', 'disabled');             // button
      }

    });

    // 전체체크박스
    $("#giftTargetGoodsCheckAll").on("click",function(index){
      // 개별체크박스 처리
      if($("#giftTargetGoodsCheckAll").prop("checked") == true){
        // 전체체크
        // 개별체크 선택
        $('INPUT[name=giftTargetGoodsCheck]').prop("checked", true);
        //// 선택삭제 버튼 활성
        //$('#fnBtnContsLv1Del').data('kendoButton').enable(true);
      }
      else{
        // 전체해제
        // 개별체크 선택 해제
        $('INPUT[name=giftTargetGoodsCheck]').prop("checked",false);
        //// 선택삭제 버튼 비활성
        //$('#fnBtnContsLv1Del').data('kendoButton').enable(false);
      }
    });
    // 개별체크박스
    $('#ng-view').on("click","input[name=giftTargetGoodsCheck]",function(index){

      const totalCnt    = $("input[name=giftTargetGoodsCheck]").length;
      const checkedCnt  = $("input[name=giftTargetGoodsCheck]:checked").length;
      // 전체체크박스 처리
      if (totalCnt == checkedCnt) {
        $('#giftTargetGoodsCheckAll').prop("checked", true);
      }
      else {
        $('#giftTargetGoodsCheckAll').prop("checked", false);
      }
      //// 선택삭제 버튼 제어
      //if (checkedCnt > 0) {
      //  $('#fnBtnContsLv1Del').data('kendoButton').enable(true);
      //}
      //else {
      //  // 선택삭제 버튼 비활성
      //  $('#fnBtnContsLv1Del').data('kendoButton').enable(false);
      //}
    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#giftTargetGoodsGrid').on("click", "button[kind=btnGiftTargetGoodsDel]", function(e) {

      // Grid가 N개 생성되므로 아래와 같이 그리드 가져와야 함
      var grid =  $('#giftTargetGoodsGrid').data('kendoGrid');
      e.preventDefault();
      let dataItem = giftTargetGoodsGrid.dataItem($(e.currentTarget).closest("tr"));

      // 그리드 삭제에서 사용하기 위한 행 선택 설정
      grid.select($(e.currentTarget).closest("tr"));

      if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
        // 임시데이터이므로 그리드에서만 제거
        fnKendoMessage( {
            message : fnGetLangData({key :'', nullMsg : '<div>그리드에서 제거됩니다.</div><div>제거하시겠습니까?</div>' })
          , type    : 'confirm'
          , ok      : function(){
                        giftTargetGoodsGridDs.remove(dataItem);
                        //fnMessage('', '제거되었습니다.', '');
                      }
        });
      }
      else {
        // 증정행사.상품 삭제 호출
        fnBtnExhibitGiftTargetGoodsDel(dataItem);
      }
    });
  }

  // --------------------------------------------------------------------------
  // 그리드-증정행사-적용대상-브랜드
  // --------------------------------------------------------------------------
  function fnInitGiftTargetBrandGrid() {

    var callUrl               = '';

    // 페이징없는 그리드
    giftTargetBrandGridDs = fnGetDataSource({
      url      : '/admin/pm/exhibit/selectExhibitGiftTargetBrandList?evExhibitId='+gbDmMailId
    });

    var bEditableGrid = gbBeditable;
    bEditableGrid = false;

    giftTargetBrandGridOpt = {
          dataSource  : giftTargetBrandGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : false
        //, height      : 'auto'  // auto 설정일 경우 scrollable false 설정 필요
        , selectable  : true
        //, editable    : bEditableGrid
        , editable    : {
                          confirmation: false
                        //, confirmDelete: "Yes"
                        }
        , resizable   : true
        , autobind    : false
        // 임시 툴바 Start
        //,toolbar      : [
        //                  { name: 'create', text: '신규', className: "btn-point btn-s"}
        //                , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
        //                , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
        //                ]
        // 임시 툴바 End
        , columns     : [
                          {                                 title : 'No'            , width:  '60px', attributes:{ style:'text-align:center' }
                                                                                    , template: '<span class="row-number"></span>'
                          }
                        , { field : 'brandId'             , title : '브랜드ID'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        , { field : 'giftTargetBrandTpNm' , title : '브랜드구분'    , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'brandNm'             , title : '브랜드명'      , width:  'auto', attributes : {style : 'text-align:left;'  }, editable:function (dataItem) {return false;}}
                        , { field : 'tempDataYn'          , title : '임시데이터'    , width:   '1px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : 'management'          , title : '관리'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
                                                                                                      return  '<div class="btn-area textCenter">'
                                                                                                            + '<button type="button" class="btn-white btn-s" kind="btnGiftTargetBrandDel">제거</button>'
                                                                                                            + '</div>';
                                                                                                    }
                                                                                                    else {
                                                                                                      return  '<div class="btn-area textCenter">'
                                                                                                            + '<button type="button" name="btnGiftTargetBrandDel" class="btn-red btn-s btnGiftTargetBrandDel" kind="btnGiftTargetBrandDel">삭제</button>'
                                                                                                            + '</div>';
                                                                                                    }
                                                                                                  }
                          }
                        ]
      //, rowTemplate : kendo.template($('#rowTemplate').html())
    };

    giftTargetBrandGrid = $('#giftTargetBrandGrid').initializeKendoGrid( giftTargetBrandGridOpt ).data('kendoGrid');
    //$('#giftTargetBrandGrid').initializeKendoGrid( giftTargetBrandGridOpt ).cKendoGrid();

    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    giftTargetBrandGrid.bind('dataBound', function() {
      // 페이징 없음
      var row_num = giftTargetBrandGridDs.data().length;
      $('#giftTargetBrandGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      //$('#totalCnt').text(exhibitGridDs._total);

      // --------------------------------------------------------------------
      // 그리드 내 활성/비활성 처리
      // --------------------------------------------------------------------
      if (gbEditableMode == 'N') {
        // 적용대상그리드-브랜드삭제버튼
        $('[name=btnGiftTargetBrandDel]').attr('disabled', 'disabled');             // button
      }
    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#giftTargetBrandGrid').on("click", "button[kind=btnGiftTargetBrandDel]", function(e) {

      // Grid가 N개 생성되므로 아래와 같이 그리드 가져와야 함
      var grid =  $('#giftTargetBrandGrid').data('kendoGrid');
      e.preventDefault();
      let dataItem = giftTargetBrandGrid.dataItem($(e.currentTarget).closest("tr"));

      // 그리드 삭제에서 사용하기 위한 행 선택 설정
      grid.select($(e.currentTarget).closest("tr"));

      if (dataItem.tempDataYn != undefined && dataItem.tempDataYn != null && dataItem.tempDataYn == 'Y') {
        // 임시데이터이므로 그리드에서만 제거
        fnKendoMessage( {
            message : fnGetLangData({key :'', nullMsg : '<div>그리드에서 제거됩니다.</div><div>제거하시겠습니까?</div>' })
          , type    : 'confirm'
          , ok      : function(){
                        giftTargetBrandGridDs.remove(dataItem);
                        //fnMessage('', '제거되었습니다.', '');
                      }
        });
      }
      else {
        // 증정행사.상품 삭제 호출
        fnBtnExhibitGiftTargetBrandDel(dataItem.evExhibitGiftTargetBrandId);
      }
    });

  }

  // ==========================================================================
  // # 그리드 순번 입력 custom Editor 함수
  // # 그리드 column 옵션에 editor: giftSortEditor로 적용
  // ==========================================================================
  function fnGiftGridEditorEvent(container, options) {

    const input = $("<input/>");
    input.attr("name", options.field);
    input.attr("type", "text");

    // ------------------------------------------------------------------------
    // 입력 컬럼 판단
    // ------------------------------------------------------------------------
    var msgItemNm       = '지급순위를';
    var maxInputLength  = 3;

    if (options.field == 'goodsSort') {
      msgItemNm       = '지급순위를';
      maxInputLength  = 3;
    }
    else if (options.field == 'giftCnt') {
      msgItemNm       = '증정기본수량을';
      maxInputLength  = 2;
    }

    // ------------------------------------------------------------------------
    // 이벤트 바인딩
    // ------------------------------------------------------------------------
    // click 이벤트
    input.on("click", function(e) {
      e.stopPropagation();
      e.stopImmediatePropagation();
    });

    // blur 이벤트
    input.on("blur", function(e) {
      const value = $(this).val().trim();

      if(!value.length) {
        fnKendoMessage({
          message: msgItemNm + ' 입력하세요.',
          ok: function() {
            // 입력 값이 없을 경우 강제로 editCell 실행하여, 다시 input 태그를 생성하고, focus를 맞춥니다.
            giftGoodsGrid.editCell(container);
          }
        });
      }
    })

    // ------------------------------------------------------------------------
    // 입력제한
    // ------------------------------------------------------------------------
    input.on("keyup", function(e) {

      // 숫자만 입력
      const regExp = /[^0-9]/g;
      let value = $(this).val();

      if( value && value.match(regExp) ) {
        value = value.replace(regExp, "");
        $(this).val(value);
      }

      // 입력 길이
      var MAX_INPUT_LENGTH = 3;
      if( value.length > maxInputLength ) {
        value = value.substring(0, maxInputLength);
        $(this).val(value);
      }
    })

    input.appendTo(container);
  }


  // --------------------------------------------------------------------------
  // 그리드-승인
  // --------------------------------------------------------------------------
  function fnInitApprGrid() {

    apprAdminGridDs =  new kendo.data.DataSource();

    apprAdminGridOpt = {
        dataSource  : apprAdminGridDs
      , editable    : false
      , noRecordMsg : '승인관리자를 선택해 주세요.'
      , columns     : [
                        { field   : 'apprTpNm'          , title     : '승인관리자 정보'           , width : '100px' , attributes  : {style : 'text-align:center'}}
                      , { field   : 'adminTypeName'     , title     : '계정<BR>유형'              , width : '100px' , attributes  : {style : 'text-align:center'}}
                      , { field   : 'apprUserNm'        , title     : '관리자<BR>(이름/ID)'       , width : '100px' , attributes  : {style : 'text-align:center'}
                                                        , template  : function(dataItem) {
                                                                        let returnValue;
                                                                        returnValue = dataItem.apprUserName + '/' + dataItem.apprLoginId;
                                                                        return returnValue;
                                                                      }
                        }
                      , { field : 'organizationName'    , title     : '조직/거래처 정보'          , width : '100px' , attributes  : {style : 'text-align:center'}}
                      , { field : 'teamLeaderYn'        , title     : '조직장여부'                , width :  '80px' , attributes  : {style : 'text-align:center'}}
                      , { field : 'userStatusName'      , title     : 'BOS<BR>계정상태'           , width :  '80px' , attributes  : {style : 'text-align:center'}}
                      , { field : 'grantUserInfo'       , title     : '권한위임정보<BR>(이름/ID)' , width : '100px' , attributes  : {style : 'text-align:center'}
                                                        , template  : function(dataItem) {
                                                                        let returnValue;
                                                                        if(dataItem.grantAuthYn == 'Y') {
                                                                          returnValue = dataItem.grantUserName + '/' + dataItem.grantLoginId;
                                                                        }
                                                                        else {
                                                                          returnValue = '';
                                                                        }
                                                                        return returnValue;
                                                                      }
                        }
                      , { field : 'grantAuthPeriod'     , title     : '권한위임기간'            , width : '150px' , attributes : {style : 'text-align:left'}
                                                        , template  : function(dataItem) {
                                                                        let returnValue;
                                                                        if(dataItem.grantAuthYn == 'Y') {
                                                                          returnValue = dataItem.grantAuthStartDt + '~' + dataItem.grantAuthEndDt;
                                                                        }
                                                                        else {
                                                                          returnValue = '';
                                                                        }
                                                                        return returnValue;
                                                                      }
                        }
                      , { field : 'grantUserStatusName' , title     : 'BOS<BR>계정상태' , width : '100px' , attributes : {style : 'text-align:left'}
                                                        , template  : function(dataItem){
                                                                        let returnValue;
                                                                        if(dataItem.grantAuthYn == 'Y') {
                                                                          returnValue = dataItem.grantUserStatusName;
                                                                        }
                                                                        else {
                                                                          returnValue = '';
                                                                        }
                                                                        return returnValue;
                                                                      }
                        }
                      , { field:'addCoverageId', hidden:true}
                      , { field:'includeYn', hidden:true}
                      ]
    };

    apprAdminGrid = $('#apprGrid').initializeKendoGrid(apprAdminGridOpt).cKendoGrid();

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
  // 승인요청처리 이벤트 처리
  // ==========================================================================
  function fnSetApprovalRequestYn() {
    //console.log('# 승인요청 체크 클릭');
    if($("input:checkbox[name=approvalRequestYn]").is(":checked") == true) {
      // 체크 -> 승인관리자 정보 활성화
      //console.log('# 승인관리자 정보 활성');
      //$('#approvalManagerDiv').addClass('show');    // 승인관리자 영역 div
      $('#approvalManagerDiv').show();              // 승인관리자 영역 div
      $('#fnApprovalRequest').show();               // 승인관리자 지정 버튼
      $('#fnApprovalInit').show();                  // 초기화
    }
    else {
      // 해제 -> 승인관리자 정보 비활성화
      //console.log('# 승인관리자 정보 비활성');
      //$('#approvalManagerDiv').removeClass('show'); // 승인관리자 영역 div
      $('#approvalManagerDiv').hide();              // 승인관리자 영역 div
      $('#fnApprovalRequest').hide();               // 승인관리자 영역 버튼
      $('#fnApprovalInit').hide();                  // 초기화
    }
  }

  // ==========================================================================
  // 상품그룹명배경설정에 따른 노출/숨김 처리
  // ==========================================================================
  function fnSetEventBgView(groupIdx) {

    var checkValue = $('input[name=exhibitImgTp'+groupIdx+']:checked').val();

    if (checkValue == 'EXHIBIT_IMG_TP.NOT_USE') {
      //$('#bgCd'+groupIdx).val('');
      $('#bgCdDiv'+groupIdx).hide();
    }
    else {
      $('#bgCdDiv'+groupIdx).show();
    }
  }

  // ==========================================================================
  // 골라담기 - 단위금액 계산
  // ==========================================================================
  function fnCalcUnitPrice(gubn) {

    var defaultBuyCnt = Number($('#defaultBuyCnt').val());  // 골라담기기본구매수량
    var selectPrice   = Number($('#selectPrice').val());    // 골라담기균일가

    if (defaultBuyCnt == undefined || defaultBuyCnt == null || defaultBuyCnt == '' ||
        selectPrice   == undefined || selectPrice   == null || selectPrice == '') {
      // 골라담기기본구매수량, 골라담기균일가 값 없는 경우
      $('#unitPriceSpan').text('');
    }
    else {
      var unitPrice = Math.ceil(selectPrice / defaultBuyCnt);
      var commaUtniPrice = fnNumberWithCommas(String(unitPrice));
      $('#unitPriceSpan').text(commaUtniPrice);
    }

    // 단가적용 버튼 활성화 : 조회 결과 노출시에는 비활성상태 유지
    if (gubn == 'change') {

      if ($('#selectPrice').val() == '') {
        // 값이 비었으면 비활성
        $('#fnBtnSetUnitPrice').data('kendoButton').enable(false);
      }
      else {
        // 값이 있을때만 활성
        $('#fnBtnSetUnitPrice').data('kendoButton').enable(true);
      }
    }
  }

  // ==========================================================================
  // # 이벤트처리 - 증정조건유형 변경
  // ==========================================================================
  function fnChangeGiftTp() {

    if ($('#giftTp').val() == 'GIFT_TP.GOODS') {
      // 상품별
      $('#giftTpCartDiv').hide();
    }
    else if ($('#giftTp').val() == 'GIFT_TP.CART') {
      // 장바구니별
      $('#giftTpCartDiv').show();
    }
  }

  // ==========================================================================
  // # 증정방식 텍스트 노출/숨김 처리
  // ==========================================================================
  function fnChangeGiftGiveTp() {

    if ($('#giftGiveTp').val() == 'GIFT_GIVE_TP.FIXED') {
      // 지정
      $('#giftGiveTextFixedSpan').show();
      $('#giftGiveTextrandomSpan').hide();
      $('#giftGiveTextselectSpan').hide();
    }
    else if ($('#giftGiveTp').val() == 'GIFT_GIVE_TP.RANDOM') {
      // 랜덤
      $('#giftGiveTextFixedSpan').hide();
      $('#giftGiveTextrandomSpan').show();
      $('#giftGiveTextselectSpan').hide();
    }
    else if ($('#giftGiveTp').val() == 'GIFT_GIVE_TP.SELECT') {
      // 선택
      $('#giftGiveTextFixedSpan').hide();
      $('#giftGiveTextrandomSpan').hide();
      $('#giftGiveTextselectSpan').show();
    }
    else {
      // 그외
      $('#giftGiveTextFixedSpan').hide();
      $('#giftGiveTextrandomSpan').hide();
      $('#giftGiveTextselectSpan').hide();
    }
  }

  // ==========================================================================
  // # 증정배송유형 변경에 따른 텍스트/출고처 노출처리
  // ==========================================================================
  function fnChangeGiftShippingTp() {
    if ($('#giftShippingTp').val() == 'GIFT_SHIPPING_TP.COMBINED') {

      // ----------------------------------------------------------------------
      // 합배송
      // ----------------------------------------------------------------------
      // * 증정배송유형 텍스트 노출/숨김 처리
      $('#giftShippingTpCombinedSpan').show();
      $('#giftShippingTpIndividualSpan').hide();
      // * 출고처 - 활성
      $('#urWarehouseId').data('kendoDropDownList').enable(true);

      $('#giftTargetTp').data('kendoDropDownList').value('GIFT_TARGET_TP.GOODS');
      $('#giftTargetTp').data('kendoDropDownList').enable(false);
      fnChangeGiftTargetTp();
    }
    else if ($('#giftShippingTp').val() == 'GIFT_SHIPPING_TP.INDIVIDUAL') {
      // ----------------------------------------------------------------------
      // 개별배송
      // ----------------------------------------------------------------------
      // * 증정배송유형 텍스트 노출/숨김 처리
      $('#giftShippingTpCombinedSpan').hide();
      $('#giftShippingTpIndividualSpan').show();
      // * 출고처 - 비활성
      $('#urWarehouseId').data('kendoDropDownList').value('');
      $('#urWarehouseId').data('kendoDropDownList').enable(false);

      $('#giftTargetTp').data('kendoDropDownList').enable(true);
      fnChangeGiftTargetTp();
    }
    else {
      // ----------------------------------------------------------------------
      // 그외
      // ----------------------------------------------------------------------
      // * 증정배송유형 텍스트 노출/숨김 처리
      $('#giftShippingTpCombinedSpan').hide();
      $('#giftShippingTpIndividualSpan').hide();
      // * 출고처 - 비활성
      $('#urWarehouseId').data('kendoDropDownList').value('');
      $('#urWarehouseId').data('kendoDropDownList').enable(false);
    }
  }

  // ==========================================================================
  // # 적용대상유형에 따른 전시브랜드 노출 제어
  // ==========================================================================
  function fnChangeGiftTargetTp() {

    if ($('#giftTargetTp').val() == 'GIFT_TARGET_TP.GOODS') {
      // ----------------------------------------------------------------------
      // 상품
      // ----------------------------------------------------------------------
      // * 적용대상 상품유형/브랜드유형 노출/숨김 처리
      // 전시브랜드-숨김
      $('#dpBrandIdSpan').hide();
      // 타이틀명
      $('#giftTargetTpTitleNmSpan').text('상품');
      // 적용대상-상품 그리드 노출
      $('#giftTargetGoodsGrid').show();
      // 적용대상-상픔 그리드 선택삭제 버튼 노출
      $('#fnBtnTargetGoodsDel').show();
      // 적용대상-브랜드 그리드 숨김
      $('#giftTargetBrandGrid').hide();
      // 추가버튼
      $('#btnAddGoodsPopupGiftTargetGoods').show();
      $('#btnAddGoodsPopupGiftTargetBrand').hide();
      //$('#giftTargetGoodsTpSpan').show();
      //$('#giftTargetBrandTpSpan').hide();
      // * 표준/전시 브랜드목록 숨김 처리
      //$('#urBrandIdSpan').hide();
    }
    else if ($('#giftTargetTp').val() == 'GIFT_TARGET_TP.BRAND') {
      // ----------------------------------------------------------------------
      // 브랜드
      // ----------------------------------------------------------------------
      // * 적용대상 상품유형/브랜드유형 노출/숨김 처리
      // 전시브랜드- 노출
      $('#dpBrandIdSpan').show();
      // 타이틀명
      $('#giftTargetTpTitleNmSpan').text('브랜드');
      // 적용대상-상품 그리드 숨김
      $('#giftTargetGoodsGrid').hide();
      // 적용대상-상픔 그리드 선택삭제 버튼 숨김
      $('#fnBtnTargetGoodsDel').hide();
      // 적용대상-브랜드 그리드 노출
      $('#giftTargetBrandGrid').show();
      // 추가버튼
      $('#btnAddGoodsPopupGiftTargetGoods').hide();
      $('#btnAddGoodsPopupGiftTargetBrand').show();
      //$('#giftTargetGoodsTpSpan').hide();
      //$('#giftTargetBrandTpSpan').show();
      // * 표준/전시 브랜드목록 노출/숨김 처리
      //if ($('#giftTargetBrandTp').val() == 'GIFT_TARGET_BRAND_TP.STANDARD') {
      //  //$('#urBrandIdSpan').show();
      //  $('#dpBrandIdSpan').hide();
      //}
      //else if ($('#giftTargetBrandTp').val() == 'GIFT_TARGET_BRAND_TP.DISPLAY') {
      //  //$('#urBrandIdSpan').hide();
      //  $('#dpBrandIdSpan').show();
      //}
      //else {
      //  //$('#urBrandIdSpan').hide();
      //  $('#dpBrandIdSpan').hide();
      //}
    }
    else {
      // ----------------------------------------------------------------------
      // 그외
      // ----------------------------------------------------------------------
      // * 적용대상 상품유형/브랜드유형 숨김 처리
      //$('#giftTargetGoodsTpSpan').hide();
      //$('#giftTargetBrandTpSpan').hide();
      // * 표준/전시 브랜드목록 숨김 처리
      //$('#urBrandIdSpan').hide();
      $('#dpBrandIdSpan').hide();
    }
  }

  // ==========================================================================
  // # DM메일전시여부에 따른 노출 제어
  // ==========================================================================
  function fnChangeDispYn(exhibitTp, exhibitDispYn) {

    if (exhibitTp == 'EXHIBIT_TP.GIFT') {
      // DM메일유형 : 증정행사

      if (exhibitDispYn == 'Y') {
        // ----------------------------------------------------------------------
        // 전시여부 : Y
        // ----------------------------------------------------------------------
        // 노출범위설정 활성
        $('input[name=goodsDisplayType]').attr('disabled', false);                  // 체크박스
        // 배너이미지 활성
        $('#imgBanner').attr('disabled', false);                                    // 파일등록 버튼
        $('#imgBannerDel').attr('disabled', false);                                 // 삭제 버튼
        // DM메일상세(PC) 활성
        $($('#descHtml').data().kendoEditor.body).attr('contenteditable', true);  // kendoEditor
        // DM메일상세(Mobile) 활성
        $($('#detlHtmlMo').data().kendoEditor.body).attr('contenteditable', true);  // kendoEditor

        // 상품그룹설정 노출
        $('#giftGroupDiv').show();
      }
      else {
        // ----------------------------------------------------------------------
        // 전시여부 : N
        // ----------------------------------------------------------------------
        // 노출범위설정 비활성
        $('input[name=goodsDisplayType]').attr('disabled', true);                   // 체크박스
        // 배너이미지 비활성
        $('#imgBanner').attr('disabled', true);                                     // 파일등록 버튼
        $('#imgBannerDel').attr('disabled', true);                                  // 삭제 버튼
        // DM메일상세(PC) 비활성
        $($('#descHtml').data().kendoEditor.body).attr('contenteditable', false); // kendoEditor
        // DM메일상세(Mobile) 비활성
        $($('#detlHtmlMo').data().kendoEditor.body).attr('contenteditable', false); // kendoEditor

        // 상품그룹설정 숨김
        $('#giftGroupDiv').hide();
      }
    }

    //if (gbMode == 'insert') {
    //
    //  if (dispYn == 'Y') {
    //    $('#giftGroupDiv').show();
    //  }
    //  else {
    //    $('#giftGroupDiv').hide();
    //  }
    //}
  }

  // ==========================================================================
  // # 버튼-목록이동
  // ==========================================================================
  function fnGoList() {

    let option = {};

    if (gbExhibitTp == 'EXHIBIT_TP.NORMAL') {
      // 일반
      option.url    = '#/exhibitNormalList';
      option.menuId = 956;
    }
    else if (gbExhibitTp == 'EXHIBIT_TP.SELECT') {
      // 골라담기
      option.url    = '#/exhibitSelectList';
      option.menuId = 957;
    }
    else if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      // 증정행사
      option.url    = '#/exhibitGiftList';
      option.menuId = 958;
    }
    //option.url    = '#/exhibitList';
    // DM메일 목록 : 100008053 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    //option.menuId = 956;
    option.target = '_self';
    option.data = {exhibitTp : gbExhibitTp};
    // 화면이동
    fnGoPage(option);
  }

  // ==========================================================================
  // # 버튼-단가적용
  // ==========================================================================
  function fnBtnSetUnitPrice() {

    fnKendoMessage({
        message : fnGetLangData({key :'', nullMsg : '<div>균일가 변경 시, 등록한 상품 중 변경된 골라담기 단가보다 판매가가 낮은 상품은 노출에서 중지됩니다.</div><div>진행하시겠습니까?</div>' })
      , type    : 'confirm'
      , ok      : function(){
                    // 단가적용처리
                    fnSetUnitPrice();
                  }
    });
  }

  // ==========================================================================
  // # 단가적용처리
  // ==========================================================================
  function fnSetUnitPrice() {

    // 기본 구매 수량
    var defaultBuyCnt = Number($('#defaultBuyCnt').val());
    var selectPrice   = Number($('#selectPrice').val());

    // ------------------------------------------------------------------------
    // 골라담기-대표상품 노출여부 변경
    // ------------------------------------------------------------------------
    var selectTargetGoodsGridArr = $('#selectTargetGoodsGrid').data('kendoGrid').dataSource.data();

    if (selectTargetGoodsGridArr != undefined && selectTargetGoodsGridArr != null && selectTargetGoodsGridArr.length > 0) {

      var dataItem;
      var salePrice;
      var unitPrice;

      var displayYnNm   = '';

      for (var i = 0; i < selectTargetGoodsGridArr.length; i++) {

        dataItem = selectTargetGoodsGridArr[i];
        salePrice = Number(dataItem.salePrice);
        unitPrice = Math.ceil(selectPrice / defaultBuyCnt);


        // 판매가 > 단가
        if (salePrice > unitPrice) {
          displayYnNm = '노출';
        }
        else {
          displayYnNm = '노출불가';
        }

        dataItem.set('resetYn'    , 'Y');           // 이거 반듯이 줘야 함
        dataItem.set('displayYnNm', displayYnNm);

      } // End of for (var i = 0; i < selectTargetGoodsGridArr.length; i++)

    } // End of if (selectTargetGoodsGridArr != undefined && selectTargetGoodsGridArr != null && selectTargetGoodsGridArr.length > 0)


    // ------------------------------------------------------------------------
    // 골라담기-상품 노출여부 변경
    // ------------------------------------------------------------------------
    var selectGoodsGridArr = $('#selectGoodsGrid').data('kendoGrid').dataSource.data();

    if (selectGoodsGridArr != undefined && selectGoodsGridArr != null && selectGoodsGridArr.length > 0) {

      var dataItem;
      var salePrice;
      var unitPrice;

      var displayYnNm   = '';

      for (var i = 0; i < selectGoodsGridArr.length; i++) {

        dataItem = selectGoodsGridArr[i];
        salePrice = Number(dataItem.salePrice);
        unitPrice = Math.ceil(selectPrice / defaultBuyCnt);

        // 판매가 > 단가
        if (salePrice > unitPrice) {
          displayYnNm = '노출';
        }
        else {
          displayYnNm = '노출불가';
        }

        dataItem.set('resetYn'    , 'Y');           // 이거 반듯이 줘야 함
        dataItem.set('displayYnNm', displayYnNm);

      } // End of for (var i = 0; i < selectGoodsGridArr.length; i++)

    } // End of if (selectGoodsGridArr != undefined && selectGoodsGridArr != null && selectGoodsGridArr.length > 0)

    // ------------------------------------------------------------------------
    // 골라담기-추가상품 노출여부 변경
    // ------------------------------------------------------------------------
    // 2021.04.13 - HGRM-7378 : 골라담기 추가상품 노출여부 항목 제거
    //var selectAddGoodsGridArr = $('#selectAddGoodsGrid').data('kendoGrid').dataSource.data();
    //
    //if (selectAddGoodsGridArr != undefined && selectAddGoodsGridArr != null && selectAddGoodsGridArr.length > 0) {
    //
    //  var dataItem;
    //  var salePrice;
    //  var unitPrice;
    //
    //  var displayYnNm   = '';
    //
    //  for (var i = 0; i < selectAddGoodsGridArr.length; i++) {
    //
    //    dataItem = selectAddGoodsGridArr[i];
    //    salePrice = Number(dataItem.salePrice);
    //    unitPrice = Math.ceil(selectPrice / defaultBuyCnt);
    //
    //    // 판매가 > 단가
    //    if (salePrice > unitPrice) {
    //      displayYnNm = '노출';
    //    }
    //    else {
    //      displayYnNm = '노출불가';
    //    }
    //
    //    dataItem.set('resetYn'    , 'Y');           // 이거 반듯이 줘야 함
    //    dataItem.set('displayYnNm', displayYnNm);
    //
    //  } // End of for (var i = 0; i < selectAddGoodsGridArr.length; i++)
    //
    //} // End of if (selectAddGoodsGridArr != undefined && selectAddGoodsGridArr != null && selectAddGoodsGridArr.length > 0)

    // ------------------------------------------------------------------------
    // 왜그런지 몰라도 아래 해줘야 균일가/단가가 유지됨
    // ------------------------------------------------------------------------
    $('#selectPrice').val(selectPrice);
    fnCalcUnitPrice('change');

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
      // DM메일 - 일반
      // ----------------------------------------------------------------------
      params.goodsCallType                      = 'exhibitNormal';                            // 조회조건 : 일반DM메일
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

  // ##########################################################################
  // # 기획전 조회 팝업
  // ##########################################################################
  // ==========================================================================
  // # 기획전에 전시된 상품그룹 상품목록 조회
  // ==========================================================================
  function fnBtnGroupAddDmMail() {

    var params = {};

    params.selectType = 'multi'; // 그리드 체크박스 타입 ( single : 하나만 선택됨, multi : 여러개 선택됨 )
    params.goodsType  = 'GOODS_TYPE.NORMAL,GOODS_TYPE.DISPOSAL';  // 상품유형(복수 검색시 , 로 구분)

    params.columnNameHidden                       = false;                                    // 배송정책
    params.columnDpBrandNameHidden                = false;                                    // 전시 브랜드
    params.columnStardardPriceHidden              = false;                                    // 원가
    params.columnRecommendedPriceHidden           = false;                                    // 정상가
    params.columnSaleStatusCodeNameHidden         = false;                                    // 판매상태
    params.columnStorageMethodHidden              = true;                                     // 보관방법

    // ----------------------------------------------------------------------
    // DM메일 - 일반
    // ----------------------------------------------------------------------
    params.goodsCallType                      = 'exhibitNormal';                            // 조회조건 : 일반 기획전
    params.saleStatus                         = 'SALE_STATUS.ON_SALE';                      // 판매상태:판매중

    params.columnSalePriceHidden              = false;                                      // 판매가
    params.columnGoodsDisplyYnHidden          = false;                                      // 전시상태

    fnKendoPopup({
      id          : 'exhibitSearchPopup'
      , title       : '기획전 조회'
      , width       : '1500px'
      , height      : '800px'
      , scrollable  : 'yes'
      // , src         : '#/goodsSearchPopup'
      , src         : '#/exhibitSearchPopup'
      , param       : params
      , success     : function( id, data ) {
        //console.log('# 상품결과 :: ', JSON.stringify(data));
        // ----------------------------------------------------
        // 선택하지 않은 경우 처리
        // ----------------------------------------------------
        if (data == undefined || data == null || data == '' || data.length <= 0) {
          return false;
        }

        if (data.detail == undefined || data.detail == null || data.detail == '' || data.detail <= 0) {
          return false;
        }

        if (data.detail.groupInfoList == undefined || data.detail.groupInfoList == null || data.detail.groupInfoList == '' || data.detail.groupInfoList <= 0) {
          return false;
        }

        const confirmMsg = "<div style='font-size:14px'>기획전 상품목록 적용 시 설정된 상품그룹과 상품은 모두 삭제됩니다.<br/>적용하시겠습니까?</div>";
        fnKendoMessage({message : confirmMsg, type : "confirm" , ok : function() {

//        fnSetGroupInfo(data.detail.groupInfoList);
                // 상품 그룹 초기화 시키기 start
                for(let i = 0; i <= gbGroupIdx; i++) {
                    try {
                        fnBtnGroupDel(i);
                    } catch(e) {
                        console.log(e);
                    }
                }
                gbGroupIdx = 0;
                gbGroupCnt = 0;
                // 상품 그룹 초기화 시키기 end
                let groupList = data.detail.groupInfoList;
                let groupData;
                let groupIdx = 0;

                for (let i = 0; i < groupList.length; i++) {

                    groupIdx = Number(i + 1);
                    groupData = groupList[i];

                    // --------------------------------------------------------------------
                    // 1. HTML 템플릿 생성
                    // --------------------------------------------------------------------
                    // if(gbGroupCnt < groupIdx) {
                    fnGroupAdd('add', null);
                    // }

                    // --------------------------------------------------------------------
                    // 2. 그룹별 기본정보 Set
                    // --------------------------------------------------------------------
                    fnSetNormalGroupBasicInfo(groupIdx, groupData);

                    // --------------------------------------------------------------------
                    // 3. 그룹별 상품리스트 Set
                    // --------------------------------------------------------------------

                    // fnSethNormalGroupGoodsList(groupIdx, groupDatagroupData.groupGoodsList);
                    // --------------------------------------------------------------------
                    // 1. 그룹상품그리드 초기화
                    // --------------------------------------------------------------------
                    fnInitGroupGoodsGrid(groupIdx, groupData.evExhibitGroupId, groupData.groupGoodsList);

                    // --------------------------------------------------------------------
                    // 2. 그리드 조회
                    // --------------------------------------------------------------------
                    let data = $('#inputForm').formSerialize(true);
                    $('#groupGoodsGrid' + groupIdx).data('kendoGrid').dataSource.read(data);
                }

                // 전역변수 Set
                gbGroupIdx = groupIdx;
                gbGroupCnt = groupIdx;
            }
        });
      }
    });
  }

  // ##########################################################################
  // # 상품조회팝업
  // ##########################################################################
  // ==========================================================================
  // # 상품조회-팝업버튼-일반DM메일-상품그룹
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

  function fnPreview() {
    if(gbDmMailId == undefined || gbDmMailId == null || gbDmMailId == '' || gbDmMailId <= 0) {
      fnKendoMessage({ message : '저장 한 뒤에 미리보기가 가능합니다.' });
    } else {
      window.open('/admin/comn/getDmMailPreview?dmMailId='+gbDmMailId, '_blank', 'width=850px, height=1000px');
    }
  }

  function fnCodeCopy() {
    // ----------------------------------------------------------------------
    // DM메일상세조회-기본정보(일반/골라담기/증정행사 공통)
    // ----------------------------------------------------------------------
    fnAjax({
      url       : '/admin/dm/mail/selectDmMailContents?dmMailId=' + gbDmMailId            // 주소줄에서 ID 보기위해 params 사용안함
      , method    : 'POST'
      , isAction  : 'select'
      , success   : function(data, status, xhr) {
        // 복사할 텍스트를 변수에 할당해줍니다.
        const text = data.detail.mailContents;

        const tempElem = document.createElement('textarea');
        tempElem.value = text;
        document.body.appendChild(tempElem);

        tempElem.select();
        document.execCommand('copy');
        document.body.removeChild(tempElem);

        fnKendoMessage({message : 'HTML 소스가 복사 되었습니다'});
      }
      , error     : function(xhr, status, strError) {
        //fnKendoMessage({
        //  message : xhr.responseText
        //});
      }
    });
  }

  // ==========================================================================
  // ------------------------------- Html 버튼 바인딩  Start ------------------
  // ==========================================================================
  ///** Common Search */
  //$scope.fnSearch       = function(){ fnSearch();           };
  ///** Common Clear */
  $scope.fnClear        = function(){ fnClear();            };
  ///** Common Confirm */
  //$scope.fnConfirm      = function(){ fnConfirm();          };
  ///** Common Close */
  //$scope.fnClose        = function(){ fnClose();            };
  ///** Common ShowImage */
  //$scope.fnShowImage    = function(url){ fnShowImage(url);  };

  /** Common fnBtnSave */
  $scope.fnBtnSave                            = function(){

                                                  if (gbDmMailId == undefined || gbDmMailId == null || gbDmMailId == '' || gbDmMailId <= 0) {
                                                    gbMode = 'insert';
                                                  }
                                                  else {
                                                    gbMode = 'update';
                                                  }
                                                  fnSave();
                                                };

  /** 삭제 */
  $scope.fnBtnDelete                          = function() {fnDelete();};

  /** Common fnBtnGiftTargetGoodsDelMulti */
  $scope.fnBtnGiftTargetGoodsDelMulti         = function(){ fnBtnGiftTargetGoodsDelMulti();  };

  /** Common File Upload */
  $scope.fnBtnImage                           = function(imageType) {
                                                  $('#' + imageType).trigger('click');
                                                };
  /** Common 상품그룹추가 */
  $scope.fnBtnGroupAdd                        = function(){ fnBtnGroupAdd();      };

  /** Common 상품그룹추가 */
  $scope.fnBtnGroupAddDmMail                 = function(){ fnBtnGroupAddDmMail();      };

  /** Common 샘플다운로드 */
  $scope.fnSampleFormDownload                 = function() { fnSampleFormDownload();}

  /** Common 그룹별 상품업로드 */
  $scope.fnBtnGroupGoodsUpload                = function(){ fnBtnGroupGoodsExcelUpload();      };

  /** 엑셀 업로드 */
  $scope.fnExcelUpload                        = function(event) { fnExcelUpload(event);}

  /** Common BtnGroupCopy */
  $scope.fnBtnGroupCopy                       = function(groupIdx, dmMailGroupId){ fnBtnGroupCopy(groupIdx, dmMailGroupId);};
  /** Common BtnGroupDel */
  $scope.fnBtnGroupDel                        = function(groupIdx){ fnBtnGroupDel(groupIdx);};

  /** 골라담기-단가적용 */
  $scope.fnBtnSetUnitPrice                    = function(){ fnBtnSetUnitPrice();};

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

  /** 승인-초기화 */
  $scope.fnApprovalInit                       = function(){
                                                  // 그리드 비우기
                                                  $("#apprGrid").data("kendoGrid").destroy();
                                                  $("#apprGrid").empty();
                                                  //그리드 초기화
                                                  fnInitApprGrid();
                                                };

  /** 목록 */
  $scope.fnBtnGoList                          = function(){
                                                  //history.back();
                                                  fnGoList();
                                                };

  $scope.fnPreview                            = function(){ fnPreview(); };
  $scope.fnCodeCopy                           = function(){ fnCodeCopy(); };

}); // document ready - END


