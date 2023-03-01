/**-----------------------------------------------------------------------------
 * description : 쿠폰지정 조회
 * @ 수정일				수정자			수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.21		안치열			최초생성
 * @
 * -----------------------------------------------------------------------------**/

'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var pageParam = fnGetPageParam();
var csId = pageParam.csId;
var bbsConfigInfo;
var gridTitleArray = new Array();
var searchCnt = 0;
var checkCnt = 0;
var dupCheck = '';
var selectBtnChk = '';
var searchCnt = 0;

$(document).ready(function() {
	fnInitialize();		//Initialize Page Call ---------------------------------

	function fnInitialize(){		//Initialize PageR
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({ PG_ID  : 'cpnMgmIssueList', callback : fnUI });
	}

	function fnUI(){

		fnInitButton();		//Initialize Button  ---------------------------------
		fnInitOptionBox();		//Initialize Option Box ------------------------------------
		fnInitGrid();		//Initialize Grid ------------------------------------

		var option2 = new Object();
		option2 = fnGetPageParam();

//		$('#searchForm').bindingForm({ 'rows':option2 }, 'rows', false);
		$('#fnDel, #fnNotiSet').kendoButton({ enable: false });

		$('#pmCouponId').val(parent.POP_PARAM["parameter"].pmCouponId);

		let orderCreateYn = stringUtil.getString(parent.POP_PARAM['parameter'].orderCreateYn, "N");

		$('#fnSelectChecked').show();
		$('#fnSelectChecked').data('kendoButton').enable(false);
		if (orderCreateYn != "Y"){
			$('#fnSearchResultAll').show();
			$('#fnSearchResultAll').data('kendoButton').enable(false);
		}


//		fnSearch();
	}

	//--------------------------------- Button Start---------------------------------
	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnClear, #fnSelectChecked, #fnSearchResultAll').kendoButton();
		$('#fnSave').kendoButton({ enable: false });
	}

	function fnSearch(){

		searchCnt++;

		if($('#condiValue').val() != ""){
			if($('#searchValue').val() != "" || $('#searchUserGroup').val() != ""|| $('#startCreateDate').val() != "" || $('#endCreateDate').val() != ""){
				return valueCheck("", "아이디 또는 회원명 검색 시 다른 조회항목과 함께 조회할 수 없습니다.", 'condiType');
			}
		}


		var data = $('#searchForm').formSerialize(true);

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

    function valueCheck(key, nullMsg, ID) {
        fnKendoMessage({
            message: fnGetLangData({ key: key, nullMsg: nullMsg }),
            ok: function focusValue() {
                $('#' + ID).focus();
            }
        });

        return false;
    }

	function fnClear(){
		$('#searchForm').formClear(true);

		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})

		if(searchCnt > 1 ){
			searchCnt--;
		}
	}

	// 선택회원 쿠폰 발급
	function fnSelectChecked(){
		fnSelectUser();
	}


	// 검색한 회원 쿠폰 발급
	function fnSearchResultAll(){

		if(searchCnt == 0){
			fnKendoMessage({message:'조회 후 발급 가능합니다.'});
			return;
		}

		if($('#condiValue').val() == '' && $('#searchValue').val() == '' && $('#searchUserGroup').val() == '' && $('#startCreateDate').val() == '' && $('#endCreateDate').val() == ''){
			fnKendoMessage({message:'전체 회원 조회 시 사용할 수 없습니다.'});
			return;
		}


		if(aGridDs._total == 0){
			fnKendoMessage({message:'조회된 회원이 없습니다.'});
			return;
		}

		selectBtnChk = 'Y';

		fnSelectUser();
	}


	// 쿠폰 발급 대상 조회
	function fnSelectUser(){

		var updateArray = new Array();
		var url  = '';
		var data ='';

		if(selectBtnChk != 'Y'){

			url  = '/admin/pm/cpnMgm/putCouponIssueList';

			var selectRows 	= aGrid.tbody.find('input[name=userGridChk]:checked').closest('tr');
			for(var i =0; i< selectRows.length;i++){

				var dataRow = aGrid.dataItem($(selectRows[i]));
				dataRow.no = i+1;
				updateArray.push(dataRow);
			}
			checkCnt = selectRows.length;
			$('#updateData').val(kendo.stringify(updateArray));

			if(checkCnt == 0){
				fnKendoMessage({message:'선택한 회원이 없습니다.'});
				return;
			}
		}else{
			fnOpenLoading()
			url  = '/admin/pm/cpnMgm/putSearchCouponIssueList';

			searchCnt = $('#countTotalSpan').val();
			selectBtnChk = '';

			if(searchCnt == ''){
				fnKendoMessage({message:'조회된 회원이 없습니다.'});
				return;
			}
		}

		data = $('#searchForm').formSerialize(true);


		// 검색실행 초기화
//		searchCnt = 0;
		var cbId = 'update';
		var map = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : url,
			params  : data,
			success :
				function( data ){
					fnCloseLoading()
					fnClose(data);
				},
			isAction : 'update'
		});
	}

	function fnClose(params){
		if(params){
			parent.POP_PARAM = params;
		}
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}


	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
            url: '/admin/pm/cpnMgm/getCpnMgmIssueList',
            pageSize: PAGE_SIZE
        });

		aGridOpt = {
				dataSource: aGridDs
				,  pageable  : {
					pageSizes: [20, 30, 50],
					buttonCount : 10,
					responsive: false
				}
				,navigatable: true
				,scrollable: true
				,height:400
				,columns : [
					{ field : 'chk', headerTemplate : "<input type='checkbox' id='checkBoxAll' />", template : '<input type="checkbox" name="userGridChk" class="userGridChk" />', width:'60px', attributes : { style : "text-align:center;" }}
					,{ field : 'no', title : 'No', width:'100px', attributes : { style : "text-align:center;" }, template:"<span class='row-number'></span>", type:"number"}
					,{ field : 'userName', title : '회원명', width:'100px', attributes : { style : "text-align:center;" }}
					,{ field : 'loginId', title : '아이디', width:'100px', attributes : { style : "text-align:center;" }}
					,{ field : 'email', title : 'Email', width:'160px', attributes : { style : "text-align:center;" }}
					,{ field : 'mobile', title : '휴대폰', width:'150px', attributes : { style : "text-align:center;"}}
					,{ field : 'userCreateDate', title : '회원가입일', width:'170px', attributes : {  style : "text-align:center;"}}
					,{ field : 'lastLoginDate', title : '최근방문일', width:'170px', attributes : { style : "text-align:center;"}}
					,{ field : 'urUserId', hidden:true }
					,{ field : 'no', hidden:true }
					]
				};

		aGrid = $('#aGrid').initializeKendoGrid(aGridOpt).cKendoGrid();

		$("#checkBoxAll").on("click",function(index){
			if($("#checkBoxAll").prop("checked")==true){
				$('INPUT[name=userGridChk]').prop("checked",true);
				$('#fnSelectChecked').data('kendoButton').enable(true);
//				$('#fnSearchResultAll').data('kendoButton').enable(true);

			}else{
				$('INPUT[name=userGridChk]').prop("checked",false);
				$('#fnSelectChecked').data('kendoButton').enable(false);
//				$('#fnSearchResultAll').data('kendoButton').enable(false);
			}
		});

		aGrid.bind("dataBound", function(){

			//$('#countTotalSpan').text(aGridDs._total);
			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

        	$('#countTotalSpan').text(aGridDs._total);
        	$('#countTotalSpan').val(aGridDs._total);
        	if($('#countTotalSpan').val() >0){
    			$('#fnSearchResultAll').data('kendoButton').enable(true);
    		}

			$('input[name=userGridChk]').prop("checked",false);
			$('input[type=checkbox][class=userGridChk]').on("change", function (){
				if($('input[type=checkbox][class=userGridChk]:checked').length > 0){
					$('#fnSelectChecked').data('kendoButton').enable(true);
//					$('#fnSearchResultAll').data('kendoButton').enable(true);
				}
				if($('input[type=checkbox][class=userGridChk]:checked').length == 0){
					$('#fnSelectChecked').data('kendoButton').enable(false);
//					$('#fnSearchResultAll').data('kendoButton').enable(false);
				}
			});
		});
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		fnKendoDropDownList({
			id    : 'searchType',
			data  : [
				{"CODE":"MOBILE"	,"NAME":"휴대폰"},
				{"CODE":"EMAIL"		,"NAME":"Email"}
			],
			textField :"NAME",
			valueField : "CODE"
		});

		fnKendoDropDownList({
			id    : 'searchDateType',
			data  : [
				{"CODE":"CREATE_DATE"	,"NAME":"회원가입일"},
				{"CODE":"LAST_LOGIN_DATE"		,"NAME":"최근방문일"}
			],
			textField :"NAME",
			valueField : "CODE"
		});

    	fnKendoDropDownList({
            id  : "searchUserGroup",
            url : "/admin/comn/getUserGroupList",
            params: {},
            valueField : "groupId",
            textField :"groupName",
            blank : "전체"
        });

		fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd'
		});
		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate',
		    change: function(e){
        	   if ($('#startCreateDate').val() == "" ) {
                   return valueCheck("6495", "시작일을 선택해주세요.", 'endCreateDate');
               }
			}
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------

	//-------------------------------  Common Function start -------------------------------

		/**
		* 콜백합수
		*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#searchForm').bindingForm(data, 'rows', true);
				break;
			case 'delete':
				fnKendoMessage({message : '삭제되었습니다.', ok :fnSearch});
				break;
			case 'update':


				if(data.updateCount == 1){
					fnKendoMessage({message : '발급되었습니다.',
						            ok :function(){
											fnSearch();
											fnClose();
										}});
				}else if(data.updateCount == 0){
					fnKendoMessage({message : data.updateCount + '건 발급되었습니다.',
			            ok :function(){
								fnSearch();
								fnClose();
							}});
				}else if(data.updateCount > 1){
					if(dupCheck == 'Y'){
						fnKendoMessage({message : data.updateCount + '건 발급되었습니다.',
										ok :function(){
												fnSearch();
												fnClose();
											}});
					}else{
						if(selectBtnChk == 'Y'){
							if(data.updateCount == searchCnt){
								fnKendoMessage({message : data.updateCount + '건 발급되었습니다.',
									ok :function(){
										fnSearch();
										fnClose();
									}});
							}else{
								fnKendoMessage({message : data.updateCount +'명'/ + searchCnt +'건 발급되었습니다.',
									ok :function(){
										fnSearch();
										fnClose();
									}});
							}
						}else{
							if(data.updateCount == checkCnt){
								fnKendoMessage({message : data.updateCount + '건 발급되었습니다.',
									ok :function(){
										fnSearch();
										fnClose();
									}});
							}else{
								fnKendoMessage({message : data.updateCount +'명'/ + checkCnt +'건 발급되었습니다.',
									ok :function(){
										fnSearch();
										fnClose();
									}});
							}
						}

					}
				}

				break;
		}
	}
	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ){	fnSearch();};
	/** Common Clear*/
	$scope.fnClear =function( ){	fnClear();};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	/** Common Del*/
	$scope.fnSave = function( ){  	fnSave();};
	/** Common NotiSet*/
	$scope.fnSelectChecked = function(gubun){  fnSelectChecked();};

	$scope.fnSearchResultAll = function(gubun){  fnSearchResultAll();};


	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
