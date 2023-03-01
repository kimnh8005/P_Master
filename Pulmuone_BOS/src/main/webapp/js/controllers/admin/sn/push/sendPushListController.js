/**-----------------------------------------------------------------------------
 * description    : 모바일 푸시발송이력
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.25		jg          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

var pageParam = fnGetPageParam();
var pm_coupon_id = pageParam.pm_coupon_id;
var period_type = pageParam.period_type;

$(document).ready(function() {
	fnInitialize(); // Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});

		fnPageInfo({
			PG_ID : 'sendPushList',
			callback : fnUI
		});

	};

	// 초기 UI 셋팅
	function fnUI() {

	    fnTranslate(); // 다국어 변환--------------------------------------------
		fnInitButton(); // Initialize Button  ---------------------------------
		fnInitGrid(); // Initialize Grid ------------------------------------
		fnInitOptionBox(); //Initialize Option Box ------------------------------------
		fnSearch();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton() {
		$('#fnSearch, #fnClear').kendoButton();

	};

	// 조회
	function fnSearch() {
	    var data;

        if( $('#pushSendDateStart').val() == "" && $('#pushSendDateEnd').val() != ""){
            return valueCheck("시작일 또는 종료일을 입력해주세요.", 'pushSendDateStart');
        }

        if( $('#pushSendDateStart').val() != "" && $('#pushSendDateEnd').val() == ""){
            return valueCheck("시작일 또는 종료일을 입력해주세요.", 'pushSendDateEnd');
        }

		if( $('#pushSendDateStart').val() > $('#pushSendDateEnd').val()){
			return valueCheck("시작일을 종료일보다 뒤로 설정할 수 없습니다.", 'pushSendDateStart');
		}

		data = $('#searchForm').formSerialize(true);

		var query = {
			page : 1,
			pageSize : PAGE_SIZE,
			filterLength : fnSearchData(data).length,
			filter : {
				filters : fnSearchData(data)
			}
		};

		aGridDs.query(query);
	};

	// 값 체크
	function valueCheck(nullMsg, id){
		fnKendoMessage({ message : nullMsg, ok : function focusValue(){
			$('#' + id).focus();
		}});

		return false;
	};

	// 초기화
	function fnClear() {
		$('#searchForm').formClear(true);
		$(".set-btn-type6").attr("fb-btn-active" , false );
	};
	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid() {

		aGridDs = fnGetPagingDataSource({
			url : '/admin/sn/push/getPushSendList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource : aGridDs,
			pageable : {
			    pageSizes: [20, 30, 50],
				buttonCount : 20
			},
			navigatable : true,
			columns : [
						{ field: 'rowNumber'		, title: 'No.'		, width: '80px'	, attributes: { style: 'text-align:center' }, template: function (dataItem){
		            		return fnKendoGridPagenation(aGrid.dataSource,dataItem);
		            	}}
			          , {field : 'advertAndNoticeTypeName', title : '공지타입', width : '80px', attributes : {style : 'text-align:center'}}
			          , {field : 'pushManagementTitle', title : '푸시 관리 제목', width : '140px', attributes : {style : 'text-align:left'}}
			          , {field : 'pushContent', title : '푸시내용', width : '140px', attributes : {style : 'text-align:left'}}
			          , {field : 'deviceTypeName', title : '기기타입', width : '80px', attributes : {style : 'text-align:left'}}
			          , {field : 'pushLink', title : '푸시링크', width : '140px', attributes : {style : 'text-align:left'}}
			          , {field : 'senderId', title : '발송관리자', width : '80px', attributes : {style : 'text-align:center'}}
			          , {field : 'pushSendDate', title : '푸시발송일', width : '80px', attributes : {style : 'text-align:center'}}
			          , {field : 'pushSendResult', title : '푸쉬전송결과<BR>(발송/전체)', width : '60px', attributes : {style : 'text-align:center'}
			          , template : "#=pushSendTotal#/#=pushTotal#"}
			          , { title: '읽음여부' 			,columns: [{ field: 'openTotal'	, title: 'Y'	, width: '60px'	, attributes: { style: 'text-align:center' }}
						, { field: 'closeTotal'	, title: 'N'	, width: '60px'	, attributes: { style: 'text-align:center' }}]
						}
			          ]
		};

		aGrid = $('#aGrid').initializeKendoGrid(aGridOpt).cKendoGrid();
		aGrid.bind("dataBound", function(){
			$("#countTotalSpan").text(aGridDs._total);
		});

	};

	// 그리드 클릭
	function fnGridClick() {

	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {

		// 발송플랫폼유형 라디오
		fnTagMkRadio({
				id    :  "sendGroup",
	            data  : [{ "CODE" : "APP_OS_TYPE.ALL", "NAME":"전체"}
	            		, { "CODE" : "APP_OS_TYPE.IOS", "NAME":"iOS"}
                        , { "CODE" : "APP_OS_TYPE.ANDROID", "NAME":"Android"} ],
				tagId : "sendGroup",
				chkVal: 'APP_OS_TYPE.ALL',
				style : {},
				change : function(e){
					let sendGroupValue = $("#sendGroup").getRadioVal();

					if( sendGroupValue == "APP_OS_TYPE.ANDROID" ){
						$("#pushTitleAndroidView").show();
						$("#pushTitleIosView").hide();
					}else if( sendGroupValue == "APP_OS_TYPE.IOS" ){
						$("#pushTitleAndroidView").hide();
						$("#pushTitleIosView").show();
					}
				}
		});


	    // 푸시발송일 시작일
		fnKendoDatePicker({
			id : 'pushSendDateStart',
			format : 'yyyy-MM-dd',
			defVal : fnGetDayMinus(fnGetToday(),6),
			defType : 'oneWeek'
		});

		// 푸시발송일 종료일
		fnKendoDatePicker({
			id : 'pushSendDateEnd',
			format : 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'pushSendDateStart',
			btnEndId : 'pushSendDateEnd',
			defVal : fnGetToday()
			,defType : 'oneWeek'
			,change: function(e){
        	   if ($('#startStopDate').val() == "" ) {
                   return valueCheck("6495", "시작일을 선택해주세요.", 'endStopDate');
               }
			}
		});

        // 공지타입 셀렉트박스
        fnKendoDropDownList({
            id  : 'advertAndNoticeType',
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "PUSH_SEND_TYPE"},
            blank : "전체"
        });

	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------
	// 콜백
	function fnBizCallback(id, data) {
	};

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	$scope.fnSearch = function() { fnSearch(); }; /* Common Search */
	$scope.fnClear = function() { fnClear(); }; /* Common Clear */

	//------------------------------- Html 버튼 바인딩  End -------------------------------

});
// document ready - END
