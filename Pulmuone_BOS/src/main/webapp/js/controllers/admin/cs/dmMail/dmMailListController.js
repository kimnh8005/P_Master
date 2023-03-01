/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 기획전리스트 처리
 * @
 * @ 아래 기획전리스트 공통 처리
 * @  - 일반     기획전리스트 : exhibitNormalListController.js, exhibitNormalList.html
 * @  - 골라담기 기획전리스트 : exhibitSelectListController.js, exhibitSelectList.html
 * @  - 증정행사 기획전리스트 : exhibitGiftListController.js  , exhibitGiftList.html
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2020.12.01    dgyoun        최초생성
 * @
 ******************************************************************************/
'use strict';


var LAST_PAGE = null;   // 최종 페이지 (페이징 기억 관련)
var PAGE_SIZE = 20;
var dmMailGridDs, dmMailGridOpt, dmMailGrid;
var publicStorageUrl = fnGetPublicStorageUrl();

var gbPageParam = '';   // 넘어온 페이지파라미터
var gbMode      = '';
var delDmMailIdArr;  // 삭제 EvExhibitId 리스트
//var gbExhibitTp = '';   // 기획전유형  <- 기획전 유형별 js 페이지에서 선언 및 정의 됨
//var gbGiftGoodsSortYn = 'N';    // 증정행사 대표상품정렬여부

var gbSearchInfoObj = new Object();   // 조회조건 객체

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
        PG_ID     : pageId // 'exhibitList'
      , callback  : fnUI
    });

    // ------------------------------------------------------------------------
    // 페이지파라미터
    // ------------------------------------------------------------------------
    gbPageParam = fnGetPageParam();
    //console.log('# gbPageParam :: ', JSON.stringify(gbPageParam));

    // ------------------------------------------------------------------------
    // 기획전유형 Set
    //   - exhibitNormalListController.js, exhibitSelectListController.js, exhibitGiftListController.js 에서 설정 됨
    // ------------------------------------------------------------------------
    //gbExhibitTp = gbPageParam.exhibitTp;
    //console.log('# gbExhibitTp :: ', gbExhibitTp);
    $('#exhibitTp').val(gbMailTemplateTp);

    // ------------------------------------------------------------------------
    // 상위타이틀
    // ------------------------------------------------------------------------
    if (gbMailTemplateTp != null && gbMailTemplateTp != 'null') {

      if (gbMailTemplateTp == 'EXHIBIT_TP.NORMAL') {
        //$('#pageTitleSpan').text('');
      }
      else if (gbMailTemplateTp == 'EXHIBIT_TP.SELECT') {
        $('#pageTitleSpan').text('골라담기(균일가) 목록');
      }
      else if (gbMailTemplateTp == 'EXHIBIT_TP.GIFT') {
        $('#pageTitleSpan').text('증정행사 기획전 목록');
      }
    }

    // ------------------------------------------------------------------------
    // 조회조건 정보 Set
    // ------------------------------------------------------------------------
    //console.log('# ================================================');
    //console.log('# [리스트] 조회조건 Set');
    //console.log('# ================================================');
    //console.log('# ------------------------------------------------');
//    let searchInfo    = sessionStorage.getItem('searchInfo');
//    if (searchInfo != null) {
//      gbSearchInfoObj = JSON.parse(searchInfo);
//    }
    //let isFromDetl = gbSearchInfoObj.isFromDetl;
    //console.log('# searchInfoObj :: ', JSON.stringify(gbSearchInfoObj));
    //console.log('# isFromDetl :: ', isFromDetl);

  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

    fnInitButton();           // Initialize Button ----------------------------

    fnInitGrid();             // Initialize Grid ------------------------------

    fnInitOptionBox();        // Initialize Option Box ------------------------

    // fnInitEvent();            // Initialize Event -----------------------------

    // fnSetDefaultPre();           // 기본설정 ----------------------------------

    //조회조건 Set을 위한 딜레이 설정
    //setTimeout(function() {
      fnSearch();               // 조회 ---------------------------------------
    //}, 1000);   // 모두 처리 안될 경우 시간 늘릴 것
  }

  // ==========================================================================
  // # 초기화 - 버튼
  // ==========================================================================
  function fnInitButton() {
    $('#fnSearch, #fnClear').kendoButton();
  }

  // ==========================================================================
  // # 초기화 - 값
  // ==========================================================================
  function fnClear() {

    $('#searchForm').formClear(true);
    fnSetDefaultPre();
    // 기획전유형 Set
    $('#exhibitTp').val(gbMailTemplateTp);
  }

  // ==========================================================================
  // # 초기화 - 옵션박스
  // ==========================================================================
  function fnInitOptionBox() {

    // ------------------------------------------------------------------------
    // 담당자구분
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
      id        : 'searchTemplateTp'
      , tagId     : 'searchTemplateTp'
      , url       : '/admin/comn/getCodeList'
      , params    : { 'stCommonCodeMasterCode' : 'MAIL_TEMPLATE_TP', 'useYn' : 'Y' }
      , chkVal    : ""
      , blank     : "전체"
      , async     : false
    });

    // ------------------------------------------------------------------------
    // 담당자구분
    // ------------------------------------------------------------------------
   fnKendoDropDownList({
       id          : 'searchType'
     , tagId       : 'searchType'
     , data        : [
                       { 'CODE' : 'ALL'   , 'NAME' : '전체'     }
                     , { 'CODE' : 'NAME'  , 'NAME' : '담당자명'  }
                     , { 'CODE' : 'ID'    , 'NAME' : '담당자ID'  }
                     ]
     , valueField  : 'CODE'
     , textField   : 'NAME'
     , chkVal      : '0'
     , style       : {}
           /*blank : '선택',*/
   });

    // ------------------------------------------------------------------------
    // 진행기간-시작일자
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id          : 'startDt'
      , format      : 'yyyy-MM-dd'
      , btnStartId  : 'startDt'
      , btnEndId    : 'endDt'
      , defVal      : fnGetDayMinus(fnGetToday(),30)
      , defType     : ''
      , change      : function() {
          fnOnChangeDatePicker(e, 'start', 'startDt', 'endDt');
      }
    });
    fnKendoDatePicker({
        id          : 'endDt'
      , format      : 'yyyy-MM-dd'
      , btnStyle    : true
      , btnStartId  : 'startDt'
      , btnEndId    : 'endDt'
      , defType     : ''
      , change      : function() {
          fnOnChangeDatePicker(e, 'end', 'startDt', 'endDt');
      }
    });
  }

  // ==========================================================================
  // # 초기화 - 이벤트
  // ==========================================================================
  function fnInitEvent() {

  }

  // ==========================================================================
  // # 기본 설정
  // ==========================================================================
  function fnSetDefaultPre(){

    if (fnIsEmpty(gbSearchInfoObj.isFromDetl) == false && gbSearchInfoObj.isFromDetl == 'Y') {
      // ----------------------------------------------------------------------
      // 상세에서 넘어온 경우 : 조회조건 Set
      // ----------------------------------------------------------------------
      fnSetSearchInfo(gbSearchInfoObj);
    }
    else {
      $('#startDt').val(fnGetDayMinus(fnGetToday(),30));
    }

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
      // 몰구분 : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.mallDiv) == false) {
        let mallDivArr = (searchInfoObj.mallDiv).split('∀');
        if (fnIsEmpty(mallDivArr) == false && mallDivArr.length > 0) {
          $('input[name=mallDiv]:checkbox').prop('checked', false);
          for (var i = 0; i < mallDivArr.length; i++) {
            $('input:checkbox[name="mallDiv"]:input[value="'+mallDivArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 골라담기/증정행사 기획전인 경우
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      if (gbMailTemplateTp == 'EXHIBIT_TP.SELECT' || gbMailTemplateTp == 'EXHIBIT_TP.GIFT') {
        // --------------------------------------------------------------------
        // 담당자ID : 콤보
        // --------------------------------------------------------------------
        $('#managerId').data('kendoDropDownList').value(searchInfoObj.managerId);
        // --------------------------------------------------------------------
        // 승인상태 : 체크
        // --------------------------------------------------------------------
        if (fnIsEmpty(searchInfoObj.approvalStatus) == false) {
          let approvalStatusArr = (searchInfoObj.approvalStatus).split('∀');
          if (fnIsEmpty(approvalStatusArr) == false && approvalStatusArr.length > 0) {
            $('input[name=approvalStatus]:checkbox').prop('checked', false);
            for (var i = 0; i < approvalStatusArr.length; i++) {
              $('input:checkbox[name="approvalStatus"]:input[value="'+approvalStatusArr[i]+'"]').prop('checked', true);
            }
          }
        }
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 증정행사 기획전인 경우
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        if (gbMailTemplateTp == 'EXHIBIT_TP.GIFT') {
          // ------------------------------------------------------------------
          // 증정방식 : 체크
          // ------------------------------------------------------------------
          if (fnIsEmpty(searchInfoObj.giftGiveTp) == false) {
            let giftGiveTpArr = (searchInfoObj.giftGiveTp).split('∀');
            if (fnIsEmpty(giftGiveTpArr) == false && giftGiveTpArr.length > 0) {
              $('input[name=giftGiveTp]:checkbox').prop('checked', false);
              for (var i = 0; i < giftGiveTpArr.length; i++) {
                $('input:checkbox[name="giftGiveTp"]:input[value="'+giftGiveTpArr[i]+'"]').prop('checked', true);
              }
            }
          }
          // ------------------------------------------------------------------
          // 증정품 배송 조건 : 라디오
          // ------------------------------------------------------------------
          $('input:radio[name="giftShippingTp"]:radio[value="'+searchInfoObj.giftShippingTp+'"]').prop('checked', true);
          // ------------------------------------------------------------------
          // 증정 조건 : 라디오
          // ------------------------------------------------------------------
          $('input:radio[name="giftTp"]:radio[value="'+searchInfoObj.giftTp+'"]').prop('checked', true);
          // ------------------------------------------------------------------
          // 증정 범위 : 라디오
          // ------------------------------------------------------------------
          $('input:radio[name="giftRangeTp"]:radio[value="'+searchInfoObj.giftRangeTp+'"]').prop('checked', true);
        }
      }
      // ----------------------------------------------------------------------
      // 진행상태 : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.statusYnSe) == false) {
        let statusYnSeArr = (searchInfoObj.statusYnSe).split('∀');
        if (fnIsEmpty(statusYnSeArr) == false && statusYnSeArr.length > 0) {
          $('input[name=statusYnSe]:checkbox').prop('checked', false);
          for (var i = 0; i < statusYnSeArr.length; i++) {
            $('input:checkbox[name="statusYnSe"]:input[value="'+statusYnSeArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 사용여부 : 라디오
      // ----------------------------------------------------------------------
      $('input:radio[name="useYn"]:radio[value="'+searchInfoObj.useYn+'"]').prop('checked', true);
      // ----------------------------------------------------------------------
      // 노출범위(디바이스) : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.goodsDisplayType) == false) {
        let goodsDisplayTypeArr = (searchInfoObj.goodsDisplayType).split('∀');
        if (fnIsEmpty(goodsDisplayTypeArr) == false && goodsDisplayTypeArr.length > 0) {
          $('input[name=goodsDisplayType]:checkbox').prop('checked', false);
          for (var i = 0; i < goodsDisplayTypeArr.length; i++) {
            $('input:checkbox[name="goodsDisplayType"]:input[value="'+goodsDisplayTypeArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 비회원 접근권한 : 라디오
      // ----------------------------------------------------------------------
      $('input:radio[name="dispNonmemberYn"]:radio[value="'+searchInfoObj.dispNonmemberYn+'"]').prop('checked', true);
      // ----------------------------------------------------------------------
      // 임직원 전용 여부 : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.evEmployeeTp) == false) {
        let evEmployeeTpArr = (searchInfoObj.evEmployeeTp).split('∀');
        if (fnIsEmpty(evEmployeeTpArr) == false && evEmployeeTpArr.length > 0) {
          $('input[name=evEmployeeTp]:checkbox').prop('checked', false);
          for (var i = 0; i < evEmployeeTpArr.length; i++) {
            $('input:checkbox[name="evEmployeeTp"]:input[value="'+evEmployeeTpArr[i]+'"]').prop('checked', true);
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
      $('input[name=userGroupFilter]').eq(0).prop('checked', false).trigger('change');
      if (fnIsEmpty(searchInfoObj.userGroupFilter) == false) {
        let userGroupFilterArr = (searchInfoObj.userGroupFilter).split('∀');
        if (fnIsEmpty(userGroupFilterArr) == false && userGroupFilterArr.length > 0) {
          $('input[name=userGroupFilter]:checkbox').prop('checked', false);
          for (var i = 0; i < userGroupFilterArr.length; i++) {
            $('input:checkbox[name="userGroupFilter"]:input[value="'+userGroupFilterArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 진행기간-시작일자
      // ----------------------------------------------------------------------
      $('#startDt').val(searchInfoObj.startDt);
      // ----------------------------------------------------------------------
      // 진행기간-종료일자
      // ----------------------------------------------------------------------
      $('#endDt').val(searchInfoObj.endDt);
    }

  }


  // ==========================================================================
  // # 조회
  // ==========================================================================
  function fnSearch() {
    //console.log('# fnSearch Start');

    // 기획전 리스트 조회
    $('#inputForm').formClear(false);
    var data;
    data = $('#searchForm').formSerialize(true);


    // ------------------------------------------------------------------------
    // 조회조건 Set
    // ------------------------------------------------------------------------
    let searchInfoObj = new Object();
    let searchData = JSON.stringify(fnSearchData(data));
    let searchDataObj = JSON.parse(searchData);
    //console.log('# searchDataObj :: ', JSON.stringify(searchDataObj));
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
    //sessionStorage.setItem('searchInfo', JSON.stringify(fnSearchData(data)));

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
    dmMailGridDs.query(query);
  }

  // ==========================================================================
  // # 삭제 버튼 클릭
  // ==========================================================================
  function fnBtnDmMailGridDel(dmMailId) {

    gbMode = 'delete';
    delDmMailIdArr = new Array() ;
    delDmMailIdArr.push(dmMailId);

    // 삭제 실행
    fnSave();
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
      url = '/admin/dm/mail/delDmMail';

      if (delDmMailIdArr == undefined || delDmMailIdArr == null ||
          delDmMailIdArr == '' || delDmMailIdArr.length <= 0) {

        fnMessage('', '삭제대상 기획전을 확인해주세요.', '');
        return false;
      }

      // ----------------------------------------------------------------------
      // 삭제 Validataion 체크
      // ----------------------------------------------------------------------
      var isCheck = false;

      for (var i = 0; i < delDmMailIdArr.length; i++) {

        isCheck = fnCheckValidationEdit(delDmMailIdArr[i]);

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

      confirmMsg = 'DM이 삭제되며, 복구할 수 없습니다.<br/>진행하시겠습니까?';
      //console.log('#>>>>> JSON.stringify(delEvExhibitIdArr) :: ', JSON.stringify(delEvExhibitIdArr));

      // ----------------------------------------------------------------------
      // 서비스에서의 처리 사항
      // 1. ExhibitManageRequestDto.java 에 String evExhibitIdListString 와 List<String> evExhibitIdListList 선언
      // 2. ExhibitManageController.java 에서 exhibitManageRequestDto.setEvExhibitIdList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getEvExhibitIdListString(), String.class)); 처리
      // ----------------------------------------------------------------------
      inParam = {'dmMailIdListString'  : JSON.stringify(delDmMailIdArr)};

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
  // # 기획전 삭제 Validation Check
  // ==========================================================================
  function fnCheckValidationEdit(inDmMailId) {

    var isCheck = false;
    // ------------------------------------------------------------------------
    // 상세조회
    // ------------------------------------------------------------------------
    fnAjax({
        url       : '/admin/dm/mail/selectDmMailInfo?dmMailId=' + inDmMailId            // 주소줄에서 ID 보기위해 params 사용안함
      , method    : 'POST'
      , isAction  : 'select'
      , async     : false     // 결과에 의해 진행을 멈춰야 하므로 false 처리
      , success   : function(data, status, xhr) {
                      //console.log('# success data   :: ', JSON.stringify(data));
                      //console.log('# success status :: ', JSON.stringify(status));
                      //console.log('# success xhr    :: ', JSON.stringify(xhr));
                      // ------------------------------------------------------
                      // 상세조회 결과로 삭제 Validation 체크
                      // ------------------------------------------------------
                      isCheck = fnChecDelkValidationDmMail(data.detail);
                    }
      , error     : function(xhr, status, strError) {
                      //console.log('# error xhr      :: ', JSON.stringify(xhr));
                      //console.log('# error status   :: ', JSON.stringify(status));
                      //console.log('# error strError :: ', JSON.stringify(strError));
                      fnMessage('', '기획전 정보를 확인하세요.', '');
                      return false;
                    }
    });

    return isCheck;
  }

  // ==========================================================================
  // # 기획전 수정/삭제 Validation Check
  // ==========================================================================
  function fnChecDelkValidationDmMail(detail) {

    var useYn     = '';
    var statusSe  = '';

    // ------------------------------------------------------------------------
    // 상세조회 결과 체크 - 기획전정보, 기획전유형, 사용여부, 진행상태
    // ------------------------------------------------------------------------
    if (detail == undefined || detail == 'undefined' || detail == null || detail == 'null' || detail == '') {
      fnMessage('', '기획전 정보가 존재하지 않습니다.', '');
      return false;
    }


    // ------------------------------------------------------------------------
    // 기획전유형별 삭제 Validataion Check 호출
    // ------------------------------------------------------------------------
    // 일반기획전
    return true;
  }


  // ==========================================================================
  // # 기획전 삭제 Validation Check - 골라담기
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
  // # 기획전 수정/삭제 Validation Check - 증정행사
  // ==========================================================================
  function fnCheckDelValidationExhibitGift(detail) {

    // 골라담기와 동일하여 골라담기 호출
    return fnCheckDelValidationExhibitSelect(detail);
  }

  // ------------------------------- Grid Start -------------------------------
  // ==========================================================================
  // # 그리드
  // ==========================================================================
  function fnInitGrid(){

    // ------------------------------------------------------------------------
    // 최종 페이지 관련 (페이징 기억 관련)
    // ------------------------------------------------------------------------
    var lastPage = sessionStorage.getItem('lastPage');
    LAST_PAGE = lastPage ? JSON.parse(lastPage) : null;

    // ------------------------------------------------------------------------
    // 컬럼 노출/숨김 설정
    // ------------------------------------------------------------------------
    var bStatusManageHiddenYn = true;   // 상태관리 컬럼 숨김여부
    var bGiftColHiddenYn      = true;   // 증정행사 관련 컬럼 숨김여부
    var userNmTitle           = '';     // 담당자/승인요청자 타이틀 이름

    userNmTitle           = '등록/수정자';

    // ------------------------------------------------------------------------
    // 가로스크롤
    // ------------------------------------------------------------------------
    var bScrollable = false;
    var bLocked     = false;
    var bLockable   = true;

    // ------------------------------------------------------------------------
    // 그리드 호출
    // ------------------------------------------------------------------------
    dmMailGridDs = fnGetPagingDataSource({
        url      : '/admin/dm/mail/selectDmMailList'
      , pageSize : PAGE_SIZE
    });

    dmMailGridOpt  = {
        dataSource  : dmMailGridDs
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
                      , { field:'dmMailId'     , title: 'DM 코드'     , width: 60 , attributes:{ style:'text-align:center' }, locked: bLocked}
                      , { field:'dmMailTemplateNm'     , title: 'DM 구분'   , width:120 , attributes:{ style:'text-align:center' }, locked: bLocked}
                      , { field:'title'           , title: '관리자 제목'   , width:200 , attributes:{ style:'text-align:left'   }, locked: bLocked}
                      , { field:'sendDt'         , title: '발송 예정일'     , width:140 , attributes:{ style:'text-align:center' } }
                      , { field:'management'      , title: '관리'         , width:140 , attributes: {style:'text-align:center;'}
                        , template: function(dataItem) {
                            let delDisableStr = '';
                            let btnStr = '<div id="pageMgrButtonArea" class="textCenter">';
                            //console.log('# 삭제가능여부 :: ', delAbleYn);
                            btnStr += '<button type="button" class="btn-blue btn-s" kind="btnDmMailGridPreview">미리보기</button>';
                            if(fnIsProgramAuth("SAVE")) {
                              btnStr += '&nbsp;'
                                  +'<button type="button" class="btn-gray btn-s" kind="btnDmMailGridEdit">수정 </button>';
                            }
                            if(fnIsProgramAuth("DELETE")) {
                              btnStr +='&nbsp;'
                                  + '<button type="button" name="btnExhibitDel" class="btn-red btn-s"  kind="btnDmMailGridDel" '+delDisableStr+'>삭제 </button>';
                            }
                            btnStr += '</div>';

                          return btnStr;
                        }
                        , lockable: bLockable
                      }
                      , { field:'userNm'          , title: userNmTitle    , width:120 , attributes:{ style:'text-align:center' }
                                                                        , template: function(dataItem) {
                                                                                  if(dataItem.modifyId == '') {
                                                                                    return dataItem.createNm + '(' + dataItem.createId+')';
                                                                                  } else {
                                                                                    return dataItem.modifyNm + '(' + dataItem.modifyId+')';
                                                                                  }
                                                                                }
                        }
                      ]
    };

    dmMailGrid = $('#dmMailGrid').initializeKendoGrid( dmMailGridOpt ).cKendoGrid();

    //// ------------------------------------------------------------------------
    //// 그리드 클릭 이벤트
    //// ------------------------------------------------------------------------
    //$('#dmMailGrid').on('click', 'tbody>tr', function () {
    //  fnGridClick();
    //});

    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    dmMailGrid.bind('dataBound', function(e) {
      var row_num = dmMailGridDs._total - ((dmMailGridDs._page - 1) * dmMailGridDs._pageSize);
      $('#dmMailGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      $('#totalCnt').text(dmMailGridDs._total);

      // ----------------------------------------------------------------------
      // 세션 lastPage 삭제(페이징 기억 관련)
      // ----------------------------------------------------------------------
      if( sessionStorage.getItem('lastPage') ) {
        delete sessionStorage.lastPage;
      }

    });

    // ------------------------------------------------------------------------
    // 미리보기 버튼 클릭
    // ------------------------------------------------------------------------
    $('#dmMailGrid').on('click', 'button[kind=btnDmMailGridPreview]', function(e) {
      e.preventDefault();
      let dataItem = dmMailGrid.dataItem($(e.currentTarget).closest('tr'));

      window.open('/admin/comn/getDmMailPreview?dmMailId='+dataItem.dmMailId, '_blank', 'width=850px, height=1000px');      //let param = {};
    });

    // ------------------------------------------------------------------------
    // 수정 버튼 클릭
    // ------------------------------------------------------------------------
    $('#dmMailGrid').on('click', 'button[kind=btnDmMailGridEdit]', function(e) {
      e.preventDefault();

      // ----------------------------------------------------------------------
      // 세션 lastPage 삭제(페이징 기억 관련)
      // ----------------------------------------------------------------------
      var curPage = dmMailGridDs._page;
      sessionStorage.setItem('lastPage', JSON.stringify(curPage));

      let dataItem = dmMailGrid.dataItem($(e.currentTarget).closest('tr'));

      if (dataItem != null && dataItem != 'null' && dataItem != '') {
        //console.log('# dataItem :: ', JSON.stringify(dataItem));
      }
      // 참고 : 아래의 경우는 버튼 클릭시 값을 가져오지 못함, 로우를 선택해야 함
      //let dataItem2 = dmMailGrid.dataItem(dmMailGrid.select());
      //console.log('# btnInventoryEdit.dataItem :: ' + JSON.stringify(dataItem));
      //fnBtnInventoryEdit(dataItem);

      // 수정 버튼 클릭
      fnBtnDmMailEdit(dataItem);
    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#dmMailGrid').on('click', 'button[kind=btnDmMailGridDel]', function(e) {
      e.preventDefault();
      let dataItem = dmMailGrid.dataItem($(e.currentTarget).closest('tr'));
      //let param = {};
      //param.dpPageId      = dataItem.dpPageId;
      //param.dpInventoryId = dataItem.dpInventoryId;
      //console.log('# btnInventoryDel.dataItem :: ' + JSON.stringify(dataItem));
      //fnBtnInventoryDel(dataItem.dpInventoryId);
      //fnBtnExhibitDel(dataItem);    // -> fnExhibitDel 에서 삭제 처리
      fnBtnDmMailGridDel(dataItem.dmMailId)
    });

  }
  // ------------------------------- Grid End ---------------------------------

  // ==========================================================================
  // # 삭제가능여부
  // ==========================================================================
  function fnExhibitDelYn(exhibitTp, statusSe, approvalStatus) {
    //console.log('# ------------------------------');
    //console.log('# exhibitTp      :: ', exhibitTp);
    //console.log('# statusSe       :: ', statusSe);
    //console.log('# approvalStatus :: ', approvalStatus);
    let delAbleYn;

    if (exhibitTp == 'EXHIBIT_TP.NORMAL') {
      // ----------------------------------------------------------------------
      // 일반기획전
      // ----------------------------------------------------------------------
      if (statusSe != undefined && statusSe != null && statusSe == 'BEF') {
        // 진행상태 : 진행예정인
        delAbleYn = true;
      }
      else {
        // 진행상태 : 진행중/진행완료
        delAbleYn = false;
      }
    }
    else {
      // ----------------------------------------------------------------------
      // 골라담기/증정행사
      // ----------------------------------------------------------------------
      if ( approvalStatus == 'APPR_STAT.NONE' || approvalStatus == 'APPR_STAT.CANCEL' || approvalStatus == 'APPR_STAT.DENIED' ||
          (approvalStatus == 'APPR_STAT.APPROVED' && (statusSe == 'BEF' || statusSe == 'ING')) ){
        // 승인상태:승인대기/요청철회/승인반려/(승인완료 중 진행예정/진행중) : 수정가능
        delAbleYn = true;
      }
      else {
        delAbleYn = false;
      }
      // ----------------------------------------------------------------------
      // 삭제버튼
      // ----------------------------------------------------------------------
      if (approvalStatus == 'APPR_STAT.NONE' || approvalStatus == 'APPR_STAT.CANCEL' || approvalStatus == 'APPR_STAT.DENIED' ) {
        // 승인상태 : 승인대기/요청철회/승인반려 : 삭제 가능
        // 진행상태가 진행예정인 경우
        delAbleYn = true;
      }
      else {
        // 승인상태 : 승인요청/승인완료(부)/승인완료
        delAbleYn = false;
      }
    }
    return delAbleYn;
  }



  // ==========================================================================
  // 신규등록 버튼 클릭
  // ==========================================================================
  function fnBtnDmMailNew() {

    // ----------------------------------------------------------------------
    // 세션 lastPage 삭제(페이징 기억 관련)
    // ----------------------------------------------------------------------
    var curPage = dmMailGridDs._page;
    sessionStorage.setItem('lastPage', JSON.stringify(curPage));

    // 링크정보
    let option = {};

    //option.url    = '#/exhibitMgm?exhibitTp='+gbExhibitTp;
    // if (pageId == 'exhibitSelectList') {
        option.url    = '#/dmMailMgm';
    // } else {
    //     option.url    = '#/exhibitMgm';
    // }
    // 기획전 등록/수정 : 100008059 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    option.menuId = 1392;
    //option.menuId = 100008059;
    option.target = '_blank';
    option.data = { exhibitTp : gbMailTemplateTp, mode : 'insert' };
    // 화면이동
    fnGoNewPage(option);  // 새페이지(탭)
//    fnGoPage(option);
  }

  // ==========================================================================
  // 승인 담당자 지정
  // ==========================================================================
  function fnSetManagerByTaskAppr(dataItem){
    var param = '';
    if (gbMailTemplateTp == 'EXHIBIT_TP.SELECT') {
      param  = {'taskCode' : 'APPR_KIND_TP.EXHIBIT_SELECT' };
    }
    else if (gbMailTemplateTp == 'EXHIBIT_TP.GIFT') {
      param  = {'taskCode' : 'APPR_KIND_TP.EXHIBIT_GIFT' };
    }

    fnKendoPopup({
        id          : 'approvalManagerSearchPopup'
      , title       : '승인관리자 선택'
      , src         : '#/approvalManagerSearchPopup'
      , param       : param
      , width       : '1600px'
      , height      : '800px'
      , scrollable  : 'yes'
      , success     : function(stMenuId, data) {
                        if(data && !fnIsEmpty(data) && data.authManager2nd) {
                          if(data.authManager1st != undefined) {
                            dataItem.approvalSubUserId = data.authManager1st.apprUserId;
                          }
                          dataItem.approvalUserId = data.authManager2nd.apprUserId;

                          fnApprovalRequest(dataItem);
                        }
                      }
    });
  }

  // ==========================================================================
  // 승인 요청
  // ==========================================================================
  function fnApprovalRequest(dataItem){
    var confirmMsg = '승인요청을 처리하시겠습니까?';

    // ------------------------------------------------------------------------
    // Param Set
    // ------------------------------------------------------------------------
    fnKendoMessage({
        message : fnGetLangData({key :'', nullMsg : confirmMsg })
      , type    : 'confirm'
      , ok      : function() {
                    fnAjax({
                        url       : '/admin/pm/exhibit/putApprovalRequestExhibit'
                      , params    : { 'exhibitDataJsonString' : JSON.stringify(dataItem) }
                      , success   : function( data ) {
                                      fnKendoMessage({
                                          message : '저장이 완료되었습니다.'
                                        , ok      : function() {
                                                      fnSearch();
                                                    }
                                      });
                                    }
                      , error     : function(xhr, status, strError) {
                                      fnKendoMessage({ message : xhr.responseText });
                                    }
                      , isAction  : "update"
                    });
                  }
    });
  }

  // ==========================================================================
  // 요청 철회
  // ==========================================================================
    function fnCancelRequest(params){
      if( fnIsEmpty(params) || fnIsEmpty(params.evExhibitIdList) || params.evExhibitIdList.length < 1) {
        fnKendoMessage({ message : '선택된 기획전이 없습니다.' });
        return;
      }
      params.exhibitTp = gbMailTemplateTp;

      var confirmMsg = '승인요청을 철회하시겠습니까?';

      // ----------------------------------------------------------------------
      // Param Set
      // ----------------------------------------------------------------------
      fnKendoMessage({
          message : fnGetLangData({key :'', nullMsg : confirmMsg })
        , type    : 'confirm'
        , ok      : function() {
                      fnAjax({
                          url         : '/admin/approval/exhibit/putCancelRequestApprovalExhibit'
                        , params      : params
                        , contentType : 'application/json'
                        , success     : function( data ) {
                                          fnKendoMessage({
                                              message : '저장이 완료되었습니다.'
                                            , ok      : function(){
                                                          fnSearch();
                                                        }
                                          });
                                        }
                        , error       : function(xhr, status, strError) {
                                          fnKendoMessage({ message : xhr.responseText });
                                        }
                        , isAction    : 'update'
                      });
                    }
      });
    };

  // ==========================================================================
  // 수정 버튼 클릭
  // ==========================================================================
  function fnBtnDmMailEdit(dataItem) {

    // ------------------------------------------------------------------------
    // 수정 화면으로 이동
    // ------------------------------------------------------------------------
    // 링크정보
    let option = {};

    option.url    = '#/dmMailMgm';
    // 기획전 등록/수정 : 100008059 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    option.menuId = 1392;
    //option.menuId = 100008059;
    option.target = '_blank';
    option.data = { exhibitTp : gbMailTemplateTp, dmMailId : dataItem.dmMailId, mode : 'update'};
    // 화면이동
    fnGoNewPage(option);  // 새페이지(탭)
    //fnGoPage(option);

    // 등록/수정/상세 화면으로 이동(exhibitMgm)
    // 새탭으로 열기
    //window.open('#/exhibitMgm?exhibitTp='+gbExhibitTp+'&evExhibitId='+dataItem.evExhibitId+'&mode=update', '_blank');
    //window.open('#/exhibitList?exhibitTp='+gbExhibitTp+'&evExhibitId='+dataItem.evExhibitId+'&mode=update', '_blank');
    //새창으로 열기
    // window.open('#/exhibitMgm?exhibitTp='+gbExhibitTp+'&evExhibitId='+dataItem.evExhibitId+'&mode=update', '_blank','width=1200, height=1000, resizable=no, fullscreen=no');
  }

  // ==========================================================================
  // 후기관리 상세화면 호출
  // ==========================================================================
  //function fnGridClick() {
  //
  //  var aMap = dmMailGrid.dataItem(dmMailGrid.select());
  //  //console.log('# aMap :: ', JSON.stringify(aMap))
  //
  //  //alert('# 새창열기 준비 중 :: ' + aMap.evExhibitId);
  //  //var aMap = dmMailGrid.dataItem(dmMailGrid.select());
  //  //fnAjax({
  //  //  url     : '/admin/customer/feedback/getDetailFeedback',
  //  //  params  : {feedbackId : aMap.feedbackId},
  //  //  success :
  //  //    function( data ){
  //  //      fnBizCallback('select',data);
  //  //    },
  //  //  isAction : 'select'
  //  //});
  //
  //  // 새창열기 샘플
  //  //$('#itemDetailLink').on('click', function(e){
  //  //  e.preventDefault();
  //  //  window.open("#/itemMgmModify?ilItemCode="+ $('#itemCode').val() +"&isErpItemLink=true&masterItemType=MASTER_ITEM_TYPE.COMMON","_blank","width=1800, height=1000, resizable=no, fullscreen=no");
  //};

  //// ==========================================================================
  //// # 저장
  //// ==========================================================================
  //function fnConfirm(psShippingPattern){
  //  fnKendoMessage({message:'수정된 정보를 저장하시겠습니까?', type : "confirm" ,ok : function(){ fnSave() } });
  //
  //}

  //// ==========================================================================
  //// # 저장
  //// ==========================================================================
  //function fnSave(){
  //
  //  var url = '/admin/customer/feedback/putFeedbackInfo';
  //  var data = $('#inputForm').formSerialize(true);
  //
  //  if(data.adminExcellentYnCheck == 'on'){
  //    data.adminExcellentYnCheck = 'Y';
  //  }
  //
  //  if( data.rtnValid ){
  //    fnAjax({
  //      url     : url,
  //      params  : data,
  //      success :
  //        function( data ){
  //          fnBizCallback('confirm', data);
  //        },
  //        isAction : 'batch'
  //      });
  //  }
  //}
  //

  // ==========================================================================
  // # 접근권한 - 체크박스 설정
  // ==========================================================================
  function fnSetUserGroupFilter(){
    // 기존 체크박스 삭제
    $('[id=userGroupFilter]').children().each(function(){
        $(this).remove();
    });

    if($('#userMaster').data('kendoDropDownList').value() == ''){
        return;
    }

    // 복수조건.외부몰 체크박스 그룹 ALL
    fnTagMkChkBox({
        id          : 'userGroupFilter'
      , url         : '/admin/comn/getUserGroupCategoryList'
      , params      : {'urGroupMasterId' : $('#userMaster').data('kendoDropDownList').value()}
      , tagId       : 'userGroupFilter'
      , async       : false
      , chkVal      : ''
      , style       : {}
      , textField   : 'groupName'
      , valueField  : 'urGroupId'
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
    });

    $('input[name=userGroupFilter]').eq(0).prop('checked', true).trigger('change');
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
            message : 'DM이 삭제되었습니다.'
          , ok      : function(){
                        // 삭제 후 재조회
                        fnSearch();
                      }
        });

        break;

      //case 'select':
      //  fnKendoInputPoup({height:'auto' ,width:'800px',title:{ nullMsg : '후기 상세 정보'} });
      //  $('#kendoPopup').scrollTop(0);
      //  $('#inputForm').bindingForm(data, 'row', true);
      //  if(data.row.evEventId != null){
      //    $('#adminExcellentYnDiv').show();
      //    $('#adminExcellentYn').show();
      //    $('#adminExcellentView').show();
      //  }else{
      //    $('#adminExcellentYnDiv').hide();
      //    $('#adminExcellentYn').hide();
      //    $('#adminExcellentView').hide();
      //  }
      //  // 베스트후기(자동)
      //  if(data.row.bestCnt > 10){
      //    $('#bestCntYn').val('예');
      //  }else{
      //    $('#bestCntYn').val('아니오');
      //  }
      //  // 베스트후기(관리자)
      //  if(data.row.adminBestYn == 'Y'){
      //    $('#adminBestYn_0').prop('checked',true);
      //  }else{
      //    $('#adminBestYn_1').prop('checked',true);
      //  }
      //  // 공개설정
      //  if(data.row.displayYn == 'Y'){
      //    $('#popupDisplayYn_0').prop('checked',true);
      //  }else{
      //    $('#popupDisplayYn_1').prop('checked',true);
      //  }
      //  // 우수후기선정 체크시 표시처리
      //  if(data.row.adminExcellentYn == 'Y'){
      //    $('#adminExcellentYnCheckDiv').hide();
      //    $('#adminExcellentYnView').show();
      //    $('#adminExcellentYnView').val('우수후기');
      //    $('#adminExcellentYn').val('우수후기');
      //  }else{
      //    $('#adminExcellentYnCheckDiv').show();
      //    $('#adminExcellentYnView').hide();
      //  }
      //
      //  fnAjax({
      //    url    : '/admin/customer/feedback/getImageList',
      //    params  : {feedbackId : data.row.feedbackId},
      //    isAction : 'select',
      //    success  :
      //      function( data ){
      //        var imageHtml = '';
      //        var testurl = 'http://localhost:8280/pulmuone/public/BOS/ur/test/2020/10/29/9F20E0D4B3274952A727.png';
      //        if(data.rows.length>0){
      //          imageHtml += '      <div style="position:relative; display: flex; flex-direction: row; flex-wrap: wrap; justify-content: left;">';
      //          for(var i=0; i < data.rows.length; i++){
      //
      //            var imageUrl = publicStorageUrl + data.rows[i].imageName;
      //            imageHtml += '      <span style="width:150px; height:150px; margin-bottom:10px; margin-right:10px; border-color:#a9a9a9; border-style:solid; border-width:1px">';
      //            imageHtml += '          <img src="'+ imageUrl +'" style="max-width: 100%; max-height: 100%;" onclick="$scope.fnShowImage(\''+imageUrl+'\')">';
      //            imageHtml += '      </span>';
      //          }
      //          imageHtml += '      </div>';
      //
      //          $('#imageContent').html(imageHtml);
      //        }
      //        else{
      //          $('#imageContent').html("");
      //        }
      //      }
      //  });
      //
      //  break;


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


  // ?? 이벤트
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
  $scope.fnBtnDmMailNew   = function( )   { fnBtnDmMailNew();  };
  /** Common Clear */
  $scope.fnClear          = function()    { fnClear();          };
  /** Common Confirm */
  $scope.fnConfirm        = function()    { fnConfirm();        };
  /** Common Close */
  $scope.fnClose          = function( )   { fnClose();          };
  /** Common ShowImage */
  $scope.fnShowImage      = function(url) { fnShowImage(url);   };


}); // document ready - END
