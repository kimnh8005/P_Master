/**-----------------------------------------------------------------------------
 * system 			 : APP 설치 단말기 목록
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.26		jg          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'deviceList',
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------
		fnInitButton();	//Initialize Button  ---------------------------------
		fnInitOptionBox();//Initialize Option Box ------------------------------------
		fnInitGrid();	//Initialize Grid ------------------------------------
        fnSearch();
	};

	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$('#fnSearch, #fnClear').kendoButton();
	};

	// 조회
	function fnSearch(){
		if( $('#terminalRegistrationDateStart').val() == "" && $('#terminalRegistrationDateEnd').val() != ""){
			return valueCheck("시작일 또는 종료일을 입력해주세요.", 'terminalRegistrationDateStart');
		}

		if( $('#terminalRegistrationDateStart').val() != "" && $('#terminalRegistrationDateEnd').val() == ""){
			return valueCheck("시작일 또는 종료일을 입력해주세요.", 'terminalRegistrationDateEnd');
		}

		if( $('#terminalRegistrationDateStart').val() > $('#terminalRegistrationDateEnd').val()){
			return valueCheck("시작일을 종료일보다 뒤로 설정할 수 없습니다.", 'terminalRegistrationDateStart');
		}

		var data = $('#searchForm').formSerialize(true);

		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter       : { filters : fnSearchData(data) }
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

    // 조회 초기화
    function fnClear(){
		$('#searchForm').formClear(true);
		$('input:radio[name="deviceType"]').eq(0).prop("checked", true);
	};

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------

	// 그리드 초기화
	function fnInitGrid(){

		aGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/userDevice/getDeviceList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
            dataSource : aGridDs,
            pageable : { pageSizes: [20, 30, 50], buttonCount : 10 },
			navigatable: true,
			columns   : [
			              {field : 'no', title : 'No.', width : '30px', attributes : {style : 'text-align:center'},template:"<span class='row-number'></span>"}
			            , {field : 'userName', title : '회원명', width : '60px', attributes : {style : 'text-align:left'}}
			            , {field : 'userId', title : '회원ID', width : '60px', attributes : {style : 'text-align:left'}}
			            , {field : 'deviceTypeName', title : '기기 타입', width : '80px', attributes : {style : 'text-align:left'}}
				        , {field : 'pushKey', title : '푸시키', width : '150px', attributes : {style : 'text-align:left'}}
				        , {field : 'createDate', title : '등록일', width : '80px', attributes : {style : 'text-align:left'}}
			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
        	var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});
			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });
		//------------------------------- 왼쪽그리드 E -------------------------------
	};

	//-------------------------------  Grid End  -------------------------------

	//-------------------------------  Common Function start -------------------------------

	function fnInitOptionBox(){
	    // 기기타입 radio
	    fnTagMkRadio({
            id    : 'deviceType',
            tagId : 'deviceType',
            url   : '/admin/comn/getCodeList',
            params : {"stCommonCodeMasterCode" : "APP_OS_TYPE", "useYn" :"Y"},
            beforeData : [{"CODE":"", "NAME":"전체"}],
            chkVal: '',
            style : {},
            change : function(e){}
        });

        // 단말등록일 시작일
        fnKendoDatePicker({
            id : 'terminalRegistrationDateStart',
            format : 'yyyy-MM-dd'
        });

        // 단말등록일 종료일
        fnKendoDatePicker({
            id : 'terminalRegistrationDateEnd',
            format : 'yyyy-MM-dd',
            btnStyle : true,
            btnStartId : 'terminalRegistrationDateStart',
            btnEndId : 'terminalRegistrationDateEnd'
        });

        //전체회원 단일조건
		fnKendoDropDownList({
			id    : 'condiType',
			data  : [{ "CODE" : "userName" , "NAME":"회원명"},
				     { "CODE" : "loginId" , "NAME":"회원ID"}
					]
		});
	};

	// 콜백
	function fnBizCallback( id, data ){
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	$scope.fnSearch = function() { fnSearch(); }; /* Common Search */
	$scope.fnClear =function() { fnClear(); }; /* Common Clear */

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END