/**-----------------------------------------------------------------------------
 * description 		 : 쿠폰목록 조회
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.20		안치열          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var rejectPmCouponId;
// ----------------------------------------------------------------------------
// *팝업용관련
// ----------------------------------------------------------------------------
var gbIsPopup           = false;                          // 팝업여부
var gbIsMenu            = true;                           // 메뉴노출여부

var gbIsPopupViewYn     = true;   // 팝업관련노출 (기본값 숨김 true )
var gbIsPopupHiddenYn   = false;  // 팝업관련숨김 (기본값 노출 false)

// 호출유형
var gbCallType                = '';
// 발급방법
var gbSearchIssueType         = '';
var gbSearchIssueTypeFixYn    = false;
// 승인/발급상태
var gbSearchApprovalStatus    = '';
var gbSearchApprovalStatusYn  = false;

var pageParam     = parent.POP_PARAM["parameter"];  // 넘어온 페이지파라미터
//console.log('# pageParam :: ', JSON.stringify(pageParam));
if (pageParam != undefined && pageParam != null && pageParam != '') {
  var inIsPopup = pageParam.isPopup;

  // 호출유형
  gbCallType                = pageParam.callType;
  // 발급방법
  gbSearchIssueType         = pageParam.searchIssueType;
  gbSearchIssueTypeFixYn    = pageParam.searchIssueTypeFixYn;
  // 승인/발급상태
  gbSearchApprovalStatus    = pageParam.searchApprovalStatus;
  gbSearchApprovalStatusYn  = pageParam.searchApprovalStatusFixYn;

  //console.log('# gbCallType               :: ', gbCallType);
  //console.log('# gbSearchIssueType        :: ', gbSearchIssueType);
  //console.log('# gbSearchIssueTypeFixYn   :: ', gbSearchIssueTypeFixYn);
  //console.log('# gbSearchApprovalStatus   :: ', gbSearchApprovalStatus);
  //console.log('# gbSearchApprovalStatusYn :: ', gbSearchApprovalStatusYn);


  if (inIsPopup == 'true' || inIsPopup == true) {
    // 팝업인 경우
    gbIsPopup   = true;       // 팝업호출임
    gbIsMenu    = false;      // 메뉴노출 안함
    // 노출/숨김
    gbIsPopupViewYn   = false;  // 팝업시 노출
    gbIsPopupHiddenYn = true;   // 팝업시 숨김
    // 저장버튼 노출
    $('#fnBtnChoice').show();
  }
}


$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : gbIsMenu });

		fnPageInfo({
			PG_ID  : 'cpnMgm',
			callback : fnUI
		});


	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		setDefaultDatePicker();

		setTiekctDiv();


		// ------------------------------------------------------------------------
		// 조회 실행 : 파라미터 Set 지연에 따른 조회 처리
		// ------------------------------------------------------------------------
		if (gbIsPopup == true && gbCallType == 'EVENT') {

		  // ----------------------------------------------------------------------
		  // 파라미터 지연Set에 따른 조회 처리
		  // ----------------------------------------------------------------------
		  fnSearchDelay();
		}
		else {
		  // 팝업이 아닌 경우
		  fnSearch();
		}
	}

	let gbIsExistVal;
	let gbIsExistCnt = 0;

	// ==========================================================================
	// # 파라미터 지연Set에 따른 조회 처리
	// ==========================================================================
	function fnSearchDelay() {

	  // 10번까지만 조회 시도
	  if (gbIsExistCnt > 10) {
	    clearTimeout(gbIsExistVal);
	    return false;
	  }

	  setTimeout(function() {
      let searchIssueType       = $('#searchIssueType').val();
      let searchApprovalStatus  = $('#searchApprovalStatus').val();
      if (searchIssueType      != undefined && searchIssueType      != null && searchIssueType != '' &&
          searchApprovalStatus != undefined && searchApprovalStatus != null && searchApprovalStatus != '' ) {
        // --------------------------------------------------------------------
        // 값이 존재할 경우
        // --------------------------------------------------------------------
        //console.log('# 조회한다...[', gbIsExistCnt, ']');
        //console.log('# searchIssueType      [', gbIsExistCnt, '] :: ', searchIssueType);
        //console.log('# searchApprovalStatus [', gbIsExistCnt, '] :: ', searchApprovalStatus);
        fnSearch();
        clearTimeout(gbIsExistVal);
      }
      else {
        // --------------------------------------------------------------------
        // 값이 존재하지 않은 경우 재조회
        // --------------------------------------------------------------------
        //console.log('# 조회안한다[', gbIsExistCnt, ']');
        gbIsExistVal = fnSearchDelay();
        gbIsExistCnt++;
      }
    }, 100);
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnClear,#fnRejectReasonSave').kendoButton();

	}
	function fnSearch(){
		$('#inputForm').formClear(true);
		var data;

		data = $('#searchForm').formSerialize(true);
		//console.log('# fnSearchData :: ', JSON.stringify(data));
		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};
		aGridDs.query( query );
	}

	function setDefaultDatePicker() {

		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})
		$("button[data-id='fnDateBtn7']").attr("fb-btn-active", true);

		// ------------------------------------------------------------------------
		// 이벤트에서 호출한 팝업일 경우 일자 기본값 공백 Set
		// ------------------------------------------------------------------------
		var today = fnGetToday();
		let defaultStartCreateDate = '';
    let defaultEndCreateDate   = '';

    if (gbIsPopup == true && gbCallType == 'EVENT') {
      //console.log('# 팝업+EVENT');
      defaultStartCreateDate = '';
      defaultEndCreateDate   = '';
      // 날짜 버튼 비활성화
      $('.date-controller button').attr("fb-btn-active", false);
    }
    else {
      //console.log('# 쿠폰 리스트');
      defaultStartCreateDate = fnGetDayMinus(today, 90);
      defaultEndCreateDate   = today;
    }

		$("#startCreateDate").val(defaultStartCreateDate);
		$("#endCreateDate").data("kendoDatePicker").value(defaultEndCreateDate);
	}

	function setTiekctDiv(){
		$('#searchIssueTicketTypeName').hide();
		$('#searchIssueTicketTypeDiv').hide();
	}

	function fnClear(){
		$('#searchForm').formClear(true);

		setDefaultDatePicker();

		// ------------------------------------------------------------------------
    // 이벤트에서 호출한 팝업일 경우
    // ------------------------------------------------------------------------
		fnInitPopupCalled();
	}

	function fnNew(){
		fnKendoPopup({
			id     : 'cpnMgmAdd',
			title  : '쿠폰등록/수정',
			src    : '#/cpnMgmAdd',
			param  : { },
			width  : '1400px',
			height : '1400px',
			success: function( id, data ){
				if(data == 'SAVE'){
					fnSearch();
				}
			}
		});

	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	function fnCouponIssueListPopup(pmCouponId){

		fnKendoPopup({
			id     : 'cpnMgmIssueList',
			title  : '쿠폰발급',
			src    : '#/cpnMgmIssueList',
			width  : '1200px',
			height : '100px',
			param  : { "pmCouponId" : pmCouponId},
			success: function( stMenuId, data ){
				fnSearch();
			}
		});
	}


	// 발급내역
	function fnCouponListPopup(pmCouponId, displayCouponName, couponType){

		fnKendoPopup({
			id     : 'cpnMgmList',
			title  : '쿠폰발급내역',
			src    : '#/cpnMgmList',
			width  : '1400px',
			height : '900px',
			param  : { "pmCouponId" : pmCouponId, "displayCouponName" : displayCouponName, "couponType" : couponType},
			success: function( stMenuId, data ){
			}
		});
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

    aGridDs = fnGetPagingDataSource({
        url      : '/admin/pm/cpnMgm/getCpnMgm'
      , pageSize : PAGE_SIZE
    });

    aGridOpt = {
        dataSource  : aGridDs
      , pageable    : {
                        pageSizes: [20, 30, 50]
                      , buttonCount : 5
                      }
      , navigatable : true
      , scrollable  : true
      , columns     : [
          { field : 'chk'                                        , width:'40px'  ,attributes: {style: "text-align:center;" }, editable:function (dataItem) {return false;}, hidden : gbIsPopupViewYn
                                                                 , headerTemplate : "<input type='checkbox' id='couponCheckAll' />"
                                                                 , template       :  function(dataItem) {
                                                                                       return '<input type="checkbox" name="couponCheck" class="couponGridChk" />'
                                                                                     }
          }
        , { field:'no'                 ,title : 'No'           , width:'70px'  ,attributes:{ style:'text-align:center' },template:"<span class='row-number'></span>"}
        , { field:'couponTypeName'     ,title : '종류(발급방법)'  , width:'160px' ,attributes:{ style:'text-align:center' }}
        , { field:'pmCouponId'    	   ,title : '쿠폰번호' 	   , width:'160px' ,attributes:{ style:'text-align:center' }}
        , { field:'displayCouponName'  ,title : '전시쿠폰명'      , width:'200px' ,attributes:{ style:'text-align:left;text-decoration: underline;color:blue;'  }}
        , { field:'bosCouponName'      ,title : '관리자 쿠폰명'    , width:'200px' ,attributes:{ style:'text-align:left;text-decoration: underline;color:blue;'}}
        , { field:'issueDate'          ,title : '발급기간'       , width:'160px' ,attributes:{ style:'text-align:center' }}
        , { field:'validityDate'       ,title : '유효기간'       , width:'160px' ,attributes:{ style:'text-align:center' }}
        , { field:'discountTypeName'   ,title : '할인방식'       , width:'100px' ,attributes:{ style:'text-align:center' }}
        , { field:'discountValue'      ,title : '할인상세'       , width:'200px' ,attributes:{ style:'text-align:center' }}
        , { field:'erpOrganizationName',title : '분담조직'       , width:'200px' ,attributes:{ style:'text-align:center' }, hidden : gbIsPopupViewYn}
        , { field:'erpOrganizationCode', hidden:'true'}
        , { field:'issueCount'         ,title : '발급건수'       , width:'150px' ,attributes:{ style:'text-align:center' }, hidden : gbIsPopupHiddenYn}
        , { field:'useCount'           ,title : '사용건수'       , width:'80px'  ,attributes:{ style:'text-align:center' }, hidden : gbIsPopupHiddenYn}
        , { field:'usePercent'         ,title : '사용율'         , width:'100px' ,attributes:{ style:'text-align:center' }, hidden : gbIsPopupHiddenYn}
        , { field:'approvalStatusName' ,title : '발급상태'       , width:'150px' ,attributes:{ style:'text-align:center' }, hidden : gbIsPopupHiddenYn,
        	template : function(dataItem){
    	    let returnValue;
    	    if(dataItem.ticketCollectYn == 'N' && dataItem.apprStat == 'APPR_STAT.APPROVED'  && dataItem.issueType == 'PAYMENT_TYPE.TICKET'){
    	    	returnValue = dataItem.approvalStatusName + '(미수금)';
    	    }else if(dataItem.ticketCollectYn == 'Y' && dataItem.apprStat == 'APPR_STAT.APPROVED'  && dataItem.issueType == 'PAYMENT_TYPE.TICKET'){
    	    	returnValue = dataItem.approvalStatusName + '(수금 완료)';
    	    }else{
    	    	returnValue = dataItem.approvalStatusName;
    	    }
            return returnValue;
          }}
        , {title : '발급상태 관리'  , width:'200px' ,attributes:{ style:'text-align:left'   }, hidden : gbIsPopupHiddenYn
             , command: [
                          { text    : '승인요청' , className: "btn-point btn-s"
                          , click   : function(e) {
                                        e.preventDefault();
                                        var tr = $(e.target).closest("tr");
                                        var data = this.dataItem(tr);
                                        fnApprAdmin(data.pmCouponId);
                                      }
                          , visible : function(dataItem) {
                                       return (dataItem.apprStat =="APPR_STAT.NONE" || dataItem.apprStat =="APPR_STAT.CANCEL" || dataItem.apprStat =="APPR_STAT.DENIED")
                                      }
                          }
                        , { text    : '요청철회'  , className: "btn-gray btn-s"
                          , click   : function(e) {
                                        e.preventDefault();
                                        var tr = $(e.target).closest("tr");
                                        var data = this.dataItem(tr);
                                        fnCancelRequest(data.pmCouponId);
                                      }
                          , visible : function(dataItem) {
                                        return dataItem.apprStat =="APPR_STAT.REQUEST";
                                      }
                          }
                      , { text    : '발급' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
                        , click   : function(e) {
                                      e.preventDefault();
                                      var tr = $(e.target).closest("tr");
                                      var data = this.dataItem(tr);
                                      fnIssueCoupon(data.pmCouponId);
                                    }
                        , visible : function(dataItem) {
                                      return dataItem.couponMasterStat =="COUPON_MASTER_STAT.STOP";
                                    }
                        }
                      , { text    : '발급중지' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
                        , click   : function(e) {
                                      e.preventDefault();
                                      var tr = $(e.target).closest("tr");
                                      var data = this.dataItem(tr);
                                      fnIssueCouponStop(data.pmCouponId);
                                    }
                        , visible : function(dataItem) {
                                      return dataItem.apprStat =="APPR_STAT.APPROVED" && dataItem.couponMasterStat =="COUPON_MASTER_STAT.APPROVED" && dataItem.approvalStatusName != '발급기간만료';
                                    }
                        }
                      , { text    : '이용권 수금' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
                          , click   : function(e) {
                                        e.preventDefault();
                                        var tr = $(e.target).closest("tr");
                                        var data = this.dataItem(tr);
                                        fnTicketCollect(data.pmCouponId);
                                      }
                          , visible : function(dataItem) {
                                        return dataItem.apprStat =="APPR_STAT.APPROVED" && dataItem.ticketCollectYn == 'N' && dataItem.issueType == 'PAYMENT_TYPE.TICKET';
                                      }
                          }
                        ]
          }
        , {title : '관리'       , width: "340px",attributes:{ style:'text-align:left;'  , class:'forbiz-cell-readonly' }, hidden : gbIsPopupHiddenYn
          , command : [
                        { text    : '복사'    , className: "btn-gray btn-s"
                        , click   : function(e) {
                                      e.preventDefault();
                                      var tr = $(e.target).closest("tr");
                                      var data = this.dataItem(tr);
                                      fnCouponCopy(data.pmCouponId);
                                    }
                        }
                      , { text    : '발급내역' , className: "btn-lightgray btn-s"
                        , click   : function(e) {  e.preventDefault();
                                      var tr = $(e.target).closest("tr");
                                      var data = this.dataItem(tr);
                                      fnCouponListPopup(data.pmCouponId, data.displayCouponName,$("input:radio[name=searchCouponType]:checked").val());
                                    }
                        , visible : function(dataItem) {return dataItem.apprStat =="APPR_STAT.APPROVED"}
                        }
                      , { text    : '쿠폰회수' , className: "btn-red btn-s"
                        , click   : function(e) {
                                      e.preventDefault();
                                      var tr = $(e.target).closest("tr");
                                      var data = this.dataItem(tr);
                                      fnCouponWithDraw(data.pmCouponId);
                                    }
                        , visible : function(dataItem) { return dataItem.apprStat =="APPR_STAT.APPROVED" && dataItem.couponMasterStat != 'COUPON_MASTER_STAT.STOP' && fnIsProgramAuth("WITHDRAW_COUPON")}
                        }
                      , { text    : '삭제' ,className: "btn-red btn-s"
                        , click   : function(e) {
                                      e.preventDefault();
                                      var tr = $(e.target).closest("tr");
                                      var data = this.dataItem(tr);
                                      fnRemoveCoupon(data.pmCouponId);
                                    }
                        , visible : function(dataItem) { return dataItem.buttonStatus =="ADMIN_BUTTON_N" }
                        }
                      , { text    : '이용권내역' , className: "btn-lightgray btn-s"
                        , click   : function(e) {  e.preventDefault();
                                      var tr = $(e.target).closest("tr");
                                      var data = this.dataItem(tr);
                                      fnTicketPopup(data.pmCouponId, data.useType, data.displayCouponName, data.serialNumberTp);
                                    }
                        , visible : function(dataItem) { return dataItem.issueType =="PAYMENT_TYPE.TICKET" && dataItem.apprStat =="APPR_STAT.APPROVED"}
                        }
                      , { text    : '이용권내역다운로드' , className: "btn-lightgray btn-s"
                          , click   : function(e) {  e.preventDefault();
                                        var tr = $(e.target).closest("tr");
                                        var data = this.dataItem(tr);
                                        fnTicketDownloadPopup(data.pmCouponId, data.useType, data.displayCouponName);
                                      }
                          , visible : function(dataItem) { return dataItem.issueType =="PAYMENT_TYPE.TICKET" && dataItem.apprStat =="APPR_STAT.APPROVED" && fnIsProgramAuth("EXCELDOWN")}
                          }
                      ]
  }
        ]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        aGrid.bind("dataBound", function(){
        	var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

        	$('#countTotalSpan').text(aGridDs._total);
        });

		$(aGrid.tbody).on("click", "td", function (e) {

			var row = $(this).closest("tr");
			var rowIdx = $("tr", aGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);
			if(colIdx == 4 || colIdx == 5){
				fnGridClick(e);
			}
		});

		// ------------------------------------------------------------------------
		// 체크박스 이벤트 처리
		// ------------------------------------------------------------------------
    // 전체체크박스
    $("#couponCheckAll").on("click",function(index){
      // 개별체크박스 처리
      if($("#couponCheckAll").prop("checked") == true){
        // 전체체크
        // 개별체크 선택
        $('INPUT[name=couponCheck]').prop("checked", true);
      }
      else{
        // 전체해제
        // 개별체크 선택 해제
        $('INPUT[name=couponCheck]').prop("checked",false);
      }
    });
    // 개별체크박스
    $('#ng-view').on("click","input[name=couponCheck]",function(index){

      const totalCnt    = $("input[name=couponCheck]").length;
      const checkedCnt  = $("input[name=couponCheck]:checked").length;
      // 전체체크박스 처리
      if (totalCnt == checkedCnt) {
        $('#couponCheckAll').prop("checked", true);
      }
      else {
        $('#couponCheckAll').prop("checked", false);
      }
    });

	}

	function fnGridClick(e){
		var dataItem = aGrid.dataItem($(e.target).closest('tr'));
		var sData = $('#searchForm').formSerialize(true);

		fnKendoPopup({
			id     : 'cpnMgmPut',
			title  : '쿠폰등록/수정',
			src    : '#/cpnMgmPut',
			param  : {pmCouponId :dataItem.pmCouponId },
			width  : '1400px',
			height : '1400px',
			success: function( id, data ){
				if(data == 'SAVE'){
					fnSearch();
				}
			}
		});
	}

	//승인요청
	function fnApprovalRequest(pmCouponId, apprSubUserId, apprUserId){

		var url = '/admin/pm/cpnMgm/putCouponStatus'
		var status = 'APPR_STAT.REQUEST';

		fnAjax({
			url     : url,
			params  : {pmCouponId : pmCouponId, apprStat : status, couponMasterStat : 'COUPON_MASTER_STAT.SAVE', apprSubUserId : apprSubUserId, apprUserId : apprUserId },
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});

	}

	// 승인
	function fnCouponApproval(pmCouponId){

		var url = '/admin/pm/cpnMgm/putCouponStatus'
		var status = 'APPR_STAT.APPROVED';

		fnAjax({
			url     : url,
			params  : {pmCouponId : pmCouponId, apprStat : status},
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});

	}

	// 요청철회
	function fnCancelRequest(pmCouponId){

		let params = {};
		params.pmCouponIdList = [];
		params.pmCouponIdList[0] = pmCouponId;

		var url = "/admin/approval/coupon/putCancelRequestApprovalCoupon";

		fnAjax({
			url     : url,
			params  : params,
			contentType : "application/json",
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});

	}

	// 승인반려
	function fnRejectApproval(pmCouponId){
		fnKendoInputPoup({height:"400px" ,width:"500px", title:{ nullMsg :'승인 반려 사유' } });
		rejectPmCouponId = pmCouponId;
	}

	// 승인반려 사유 저장
	function fnRejectReasonSave(){
		var url = '/admin/pm/cpnMgm/putCouponStatus'
		var status = 'APPR_STAT.DENIED';
		var statusComment = $('#statusComment').val();

		fnAjax({
			url     : url,
			params  : {pmCouponId : rejectPmCouponId, apprStat : status, statusComment:statusComment},
			success :
				function( data ){
				fnClose();
				fnSearch();
			},
			isAction : 'batch'
		});

	}

	// 발급
	function fnIssueCoupon(pmCouponId){
		var url = '/admin/pm/cpnMgm/putCouponStatus'
		var status = 'APPR_STAT.APPROVED';
		var masterStatus = 'COUPON_MASTER_STAT.APPROVED';

		fnAjax({
			url     : url,
			params  : {pmCouponId : pmCouponId, apprStat : status, couponMasterStat : masterStatus},
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});

	}

	// 발급중지
	function fnIssueCouponStop(pmCouponId){
		var url = '/admin/pm/cpnMgm/putCouponStatus'
		var status = 'APPR_STAT.APPROVED';
		var masterStatus = 'COUPON_MASTER_STAT.STOP';

		fnAjax({
			url     : url,
			params  : {pmCouponId : pmCouponId, apprStat : status, couponMasterStat : masterStatus},
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});

	}

	//쿠폰복사
	function fnCouponCopy(pmCouponId){

		var couponCopyYn = 'Y';

		fnKendoPopup({
			id     : 'cpnMgmPut',
			title  : '쿠폰등록/수정',
			src    : '#/cpnMgmPut',
			param  : {pmCouponId :pmCouponId, couponCopyYn : couponCopyYn },
			width  : '1300px',
			height : '1400px',
			success: function( id, data ){
				fnSearch();
			}
		});

	}


	function fnCouponWithDraw(pmCouponId){
		fnKendoMessage({message:'미사용 된 쿠폰 전체를 발급취소(회수) 및 발급 중지 처리하겠습니까?', type : "confirm" ,ok :
			function(){
				fnWithDrawAction(pmCouponId);
			}
			, cancel:inputFocus  });
	}

	// 쿠폰 회수
	function fnWithDrawAction(pmCouponId){

		var url = '/admin/pm/cpnMgm/putCouponStatus'
		var status = 'APPR_STAT.APPROVED';
		var masterStatus = 'COUPON_MASTER_STAT.STOP_WITHDRAW';
		fnAjax({
			url     : url,
			params  : {pmCouponId : pmCouponId,  apprStat : status, couponMasterStat : masterStatus},
			success :
				function( data ){

					fnKendoMessage({
						message : '쿠폰회수가 완료 되었습니다.',
						ok      : function(){ fnClose();}
					});
					fnSearch();
				},
				isAction : 'batch'
		});
	}


	// 삭제 확인 Confimrm
	function fnRemoveCoupon(pmCouponId){
		fnKendoMessage({message:'삭제 하시겠습니까?', type : "confirm" ,ok : function(){ fnRemoveCouponConfirm(pmCouponId) } });
	}


	// 쿠폰 삭제
	function fnRemoveCouponConfirm(pmCouponId){

		var url = '/admin/pm/cpnMgm/removeCoupon'

			fnAjax({
				url     : url,
				params  : {pmCouponId : pmCouponId},
				success :
					function( data ){
						fnSearch();
					},
					isAction : 'batch'
			});

	}

	// 이용권 내역 페이지 호출
	function fnTicketPopup(pmCouponId, useType, displayCouponName, serialNumberTp){
//		var map = aGrid.dataItem(aGrid.select());
//		var sData = $('#searchForm').formSerialize(true);
//		var option = new Object();
//
//		option.url = "#/serialNumberList";
//		option.menuId = 807;
//		option.data = {
//				parentId : pmCouponId
//				// 쿠폰의 경우 SERIAL_NUMBER_USE_TYPE.COUPON 포인트의 경우 SERIAL_NUMBER_USE_TYPE.POINT
//				, useType : "SERIAL_NUMBER_USE_TYPE.COUPON"
//				, displayName : displayCouponName
//				};
//
//		$scope.goPage(option);

		fnKendoPopup({
			id     : 'serialNumberList',
			title  : '이용권',
			src    : '#/serialNumberList',
			param  : {parentId : pmCouponId		, useType : "SERIAL_NUMBER_USE_TYPE.COUPON"		, displayName : displayCouponName, serialNumberTp : serialNumberTp},
			width  : '1100px',
			height : '1100px',
			success: function( id, data ){
				if(data == 'SAVE'){
					fnSearch();
				}
			}
		});
	}

	// 이용권 내역 다운로드
	function fnTicketDownloadPopup(pmCouponId, useType, displayCouponName){
		$('#displayName').val(displayCouponName);
		$('#parentId').val(pmCouponId);
		$('#useType').val('SERIAL_NUMBER_USE_TYPE.COUPON');
		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/promotion/serialNumber/ticketNumberExportExcel', data);

	}

	function fnApprAdmin(pmCouponId){
		var param  = {'taskCode' : 'APPR_KIND_TP.COUPON' };
		fnKendoPopup({
			id     : 'approvalManagerSearchPopup',
			title  : '승인관리자 선택',
			src    : '#/approvalManagerSearchPopup',
			param  : param,
			width  : '1600px',
			height : '800px',
			scrollable : "yes",
			success: function( stMenuId, data ){
				if (data.isCompleteProcess != true) {
					return;
				}

				var apprSubUserId;
				var apprUserId
				if(data && !fnIsEmpty(data) && data.authManager2nd){
					var authManager1 = data.authManager1st;
					apprSubUserId = authManager1.apprUserId;
					var authManager2 = data.authManager2nd;
					apprUserId = authManager2.apprUserId;
				}

				fnApprovalRequest(pmCouponId, apprSubUserId, apprUserId);

			}
		});
	}



	// 이용권 수금 완료 여부 확인
	function fnTicketCollect(pmCouponId){

		fnKendoMessage({message:'수금완료로 변경하시겠습니까?', type : "confirm" ,ok : function(){ fnTicketCollectConfirm(pmCouponId) } });

	}

	// 이용권 수금 완료 변경 처리
	function fnTicketCollectConfirm(pmCouponId){
		var url = '/admin/pm/cpnMgm/putTicketCollectStatus'
		var ticketCollectYn = 'Y';
		fnAjax({
			url     : url,
			params  : {pmCouponId : pmCouponId, ticketCollectYn : ticketCollectYn},
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});
	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

	$('#kendoPopup').kendoWindow({
		visible: false,
		modal: true
	});

	/* 발급상태 */
	fnKendoDropDownList({
			id  : 'searchApprovalStatus',
	        url : "/admin/comn/getCouponSearchStatus",
	        params : {"useYn" :"Y"},
	        textField :"NAME",
			valueField : "CODE",
			blank : "전체"
		});

	fnTagMkRadio({
		id: "searchCouponType" ,
		tagId : 'searchCouponType',
		url : "/admin/comn/getCodeList",
		params : {"stCommonCodeMasterCode" : "COUPON_TYPE", "useYn" :"Y"},
		async : false,
		beforeData : [
						{"CODE":"", "NAME":"전체"},
					],
		chkVal: '',
		style : {}
	});

	// 이용권 수금 상태
	fnTagMkRadio({
		id: "searchIssueTicketType" ,
		tagId : 'searchIssueTicketType',
		url : "/admin/comn/getCodeList",
		params : {"stCommonCodeMasterCode" : "TICKET_COLLECT_TYPE", "useYn" :"Y"},
		async : false,
		beforeData : [
						{"CODE":"", "NAME":"전체"},
					],
		chkVal: '',
		style : {}
	});

	/* 발급방법 */
	fnKendoDropDownList({
			id  : 'searchIssueType',
	        url : "/admin/comn/getCodeList",
	        params : {"stCommonCodeMasterCode" : "PAYMENT_TYPE", "useYn" :"Y"},
	        textField :"NAME",
			valueField : "CODE",
			chkVal:gbSearchIssueType,
			blank : "전체"
		});
	$('#searchIssueType').unbind('change').on('change', function(){

		var IssueTypeList =$('#searchIssueType').data('kendoDropDownList');
		if(IssueTypeList._old == 'PAYMENT_TYPE.TICKET'){
			$('#searchIssueTicketTypeName').show();
			$('#searchIssueTicketTypeDiv').show();
		}else{
			$('#searchIssueTicketTypeName').hide();
			$('#searchIssueTicketTypeDiv').hide();
			$('#searchIssueTicketType').val('');
		}

	});






	fnKendoDatePicker({
		id          : 'startCreateDate',
		format      : 'yyyy-MM-dd',
		btnStartId  : "startCreateDate",
    btnEndId    : "endCreateDate",
		defVal      : fnGetDayMinus(fnGetToday(),90),
    defType : 'threeMonth'
	});
	fnKendoDatePicker({
		id          : 'endCreateDate',
		format      : 'yyyy-MM-dd',
		btnStyle    : true,
		btnStartId  : 'startCreateDate',
		btnEndId    : 'endCreateDate',
		defVal      : fnGetToday(),
		defType     : 'threeMonth'
	});

	// 기간조회 검색 타입
	fnKendoDropDownList({
		id    : 'searchDateSelect',
		data  : [
			{"CODE":"CREATE_DATE"		,"NAME":"등록일자"},
			{"CODE":"APPROVED_DATE"		,"NAME":"발급기간"}
		],
		textField :"NAME",
		valueField : "CODE"
	});

	// 검색어 조회 조건
	fnKendoDropDownList({
		id    : 'searchSelect',
		data  : [{"CODE":"SEARCH_SELECT.DISPLAY"	,"NAME":'전시 쿠폰명'}
				,{"CODE":"SEARCH_SELECT.BOS"		,"NAME":'관리자 쿠폰명'}
				,{"CODE":"SEARCH_SELECT.COUPON_ID"	,"NAME":'쿠폰번호'}
				]
	});



	  // ------------------------------------------------------------------------
	  // 팝업/이벤트에서 호출한 경우 처리
	  // ------------------------------------------------------------------------
	  fnInitPopupCalled();
	}


	// ==========================================================================
	// # 팝업/이벤트에서 호출한 경우 처리
	// ==========================================================================
	function fnInitPopupCalled() {
    // 호출유형
    if (gbIsPopup == true && gbCallType == 'EVENT') {
      // 호출유형 : 이벤트
      // 발급방법
      $('#searchIssueType').data('kendoDropDownList').value(gbSearchIssueType);
      // 발급방법 비활성
      if (gbSearchIssueTypeFixYn == true) {
        $('#searchIssueType').data('kendoDropDownList').enable(false);                    // kendoDropDownList
      }
      // 승인/발급상태
      $('#searchApprovalStatus').data('kendoDropDownList').value(gbSearchApprovalStatus);
      // 승인/발급상태 비활성
      if (gbSearchApprovalStatusYn == true) {
        $('#searchApprovalStatus').data('kendoDropDownList').enable(false);                    // kendoDropDownList
      }

      // 신규버튼 숨김
      $('#fnNew').hide();
    }

	}


	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#input1').focus();
	};
		function condiFocus(){
		$('#condition1').focus();
	};


	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnRejectReasonSave = function( ){  fnRejectReasonSave();};


  /** Popup Coupon 등록 */
  $scope.fnBtnPopupChoice = function(){
                              // TODO

                              fnPopupChoice();
                            };

  // --------------------------------------------------------------------------
  // Popup 선택쿠폰 반환
  // --------------------------------------------------------------------------
  function fnPopupChoice() {
    //console.log('# fnPopupChoice Start')

    let params = [];

    let grid = $("#aGrid").data("kendoGrid");
    // 선택된 rows
    var selectRows  = $("#aGrid").find('input[name=couponCheck]:checked').closest('tr');

    if (selectRows != undefined && selectRows != null && selectRows.length > 0) {
      for (var i = 0; i < selectRows.length; i++) {
        var selectedDataItem = grid.dataItem($(selectRows[i]));
        params[i] = selectedDataItem;
      }
      parent.POP_PARAM = params;
      parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    }
    else {
      fnKendoMessage({ message : "선택된 쿠폰이 없습니다." });
      return;
    }
  }

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
