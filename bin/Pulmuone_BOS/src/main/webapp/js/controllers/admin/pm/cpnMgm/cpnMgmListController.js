/**-----------------------------------------------------------------------------
 * description 		 : 쿠폰발급 내역 조회
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.21		안치열			최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

var pageParam = fnGetPageParam();
var pmCouponId;
var displayCouponName;
var couponType;

$(document).ready(function() {
	fnInitialize();
	//Initialize Page Call ---------------------------------

	pmCouponId = parent.POP_PARAM["parameter"].pmCouponId;
	displayCouponName = parent.POP_PARAM["parameter"].displayCouponName;
	couponType = parent.POP_PARAM["parameter"].couponType;

	//Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : false
		});

		fnPageInfo({
			PG_ID : 'cpnMgmList',
			callback : fnUI
		});

	}

	function fnUI() {

		fnInitButton();
		//Initialize Button  ---------------------------------

		fnInitGrid();
		//Initialize Grid ------------------------------------

		fnInitOptionBox();
		//Initialize Option Box ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton() {
		$('#fnSearch, #fnClear, #fnList, #fnCancelDeposit, #fnExcelExport').kendoButton();

	}

	function fnSearch() {

		var data;
		data = $('#searchForm').formSerialize(true);
		//console.log(data);
		var query = {
			page : 1,
			pageSize : PAGE_SIZE,
			filterLength : fnSearchData(data).length,
			filter : {
				filters : fnSearchData(data)
			}
		};
		aGridDs.query(query);
	}

	function fnClear() {
		$('#searchForm').formClear(true);
		$('#aGrid').gridClear(true);
		$('#pmCouponId').val(pmCouponId);
	}


	function fnCancelDeposit(){
		fnKendoMessage({message:'미사용 쿠폰을 발급 취소하시겠습니까?', type : "confirm" , ok : function(){ fnPutDeposit() }});

	}


	function fnPutDeposit(){
		var selectRows 	= aGrid.tbody.find('input[name=couponGridChk]:checked').closest('tr');
		var depositArray = new Array();

		for(var i =0; i< selectRows.length;i++){
			var dataRow = aGrid.dataItem($(selectRows[i]));
			depositArray.push(dataRow);
		}


		$('#updateData').val(kendo.stringify(depositArray));

		var url  = '/admin/pm/cpnMgm/putCancelDepositList';
		var cbId = 'update';
		fnAjax({
			url     : url,
			params  :  {updateData :$('#updateData').val()},
			success :
				function( data ){
					fnBizCallback(cbId, data);
				},
				isAction : 'update'
		});

	}

	function fnExcelExport(){

		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/pm/cpnMgm/issueListExportExcel', data);

	}

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid() {
		$('#pmCouponId').val(pmCouponId);
		$('#displayCouponName').val(displayCouponName);
		var data = $('#searchForm').formSerialize(true);

		aGridDs = fnGetPagingDataSource({
			url: '/admin/pm/cpnMgm/getCpnMgmList',
			//data  : data,
			pageSize : PAGE_SIZE
		});


		aGridOpt = {
				dataSource : aGridDs
				,  pageable  : {
					pageSizes: [5, 20, 30, 50],
					buttonCount : 5
				}
				,navigatable: true
				//,height:550
				,columns : [
					{ field : 'chk', headerTemplate : "<input type='checkbox' id='checkBoxAll' />", template : function(dataItem) { return dataItem.couponUseDate == null && dataItem.status != 'COUPON_STATUS.CANCEL'  ? '<input type="checkbox" name="couponGridChk" class="couponGridChk" />' : '' }, width:'60px', attributes : { style : "text-align:center;" }}
					,{ field : 'no', title : 'No', width:'80px', attributes : { style : "text-align:center;" }, template:"<span class='row-number'></span>"}
					,{ field : 'bosCouponName', title : '쿠폰명', width:'150px', attributes : { style : "text-align:center;" }}
					,{ field : 'discountValue', title : '할인', width:'180px', format: "{0:n0}", attributes : {  style : "text-align:center;" }}
					,{ field : 'validityPeroid', title : '유효기간', width:'200px', attributes : { style : "text-align:center;" }}
					,{ field : 'issueDate', title : '발급일자', width:'150px', attributes : {  style : "text-align:center;"}}
					,{ field : 'remainingPeriod', title : '잔여기간', width:'120px', attributes : { style : "text-align:center;"}}
					,{ field : 'couponUseDate', title : '사용일자', width:'120px', attributes : {  style : "text-align:center;"}}
					,{ field : 'couponUseTime', title : '사용시각', width:'120px', attributes : {  style : "text-align:center;"}}
					,{ field : 'userInfo', title : '회원정보(ID)', width:'150px', attributes : { style : "text-align:center;"},
						template : function(dataItem){
                   	     let returnValue;

                   	     	if(dataItem.userStatus == 0){
                   	     		returnValue = '탈퇴회원(' + dataItem.loginId + ')';
                   	     	}else{
                   	     		returnValue = dataItem.userNm + '(' + dataItem.loginId + ')';
                   	     	}

                            return returnValue;
				          }
					}
					,{ field : 'statusComment', title : '사유', width:'200px', attributes : {  style : "text-align:center;"}}
					,{ field : 'pmCouponIssueId', hidden:true }
					]
				};

		// 발급내역 화면에서 판매가 지정으로 조회한 경우
		if(couponType == "COUPON_TYPE.SALEPRICE_APPPOINT"){
			let addColumn = {field : 'goodsNm', title : '쿠폰 적용 상품명', width : '300px', attributes : { style : "text-align:center;"},
				template : function(dataItem){
					let goodsNm = dataItem['goodsNm'];

					if(goodsNm) {
						if(goodsNm.length > 30){
							goodsNm = goodsNm.substr(0,30) + "...";
						}
					}

					if(!goodsNm) goodsNm = "";

					return goodsNm ;
				}
			};
			aGridOpt.columns.splice(7,0,addColumn);
		}

		aGrid = $('#aGrid').initializeKendoGrid(aGridOpt).cKendoGrid();

		aGrid.bind("dataBound", function(){
	        	var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
				$("#aGrid tbody > tr .row-number").each(function(index){
					$(this).html(row_num);
					row_num--;
				});

	        	$('#totalCnt').text(aGridDs._total);
	        });

		$("#checkBoxAll").on("click",function(index){
			if($("#checkBoxAll").prop("checked")==true){
				$('INPUT[name=couponGridChk]').prop("checked",true);
				$('#fnCancelDeposit').data('kendoButton').enable(true);

			}else{
				$('INPUT[name=couponGridChk]').prop("checked",false);
				$('#fnCancelDeposit').data('kendoButton').enable(false);
			}
		});

		aGrid.bind("dataBound", function(){
			$('#countTotalSpan').text(aGridDs._total);

			$('INPUT[name=couponGridChk]').prop("checked",false);
			$('input[type=checkbox][class=couponGridChk]').on("change", function (){
				if($('input[type=checkbox][class=couponGridChk]:checked').length > 0){
					$('#fnCancelDeposit').data('kendoButton').enable(true);
				}
				if($('input[type=checkbox][class=couponGridChk]:checked').length == 0){
					$('#fnCancelDeposit').data('kendoButton').enable(false);
				}
			});
		});


		$('#aGrid').on('change', ':checkbox', function(e) {
			var data = aGridDs.getByUid($(e.target).closest('tr').data('uid'));
			if ($(this).is(':checked')) {
				data.CHECK_YN = 'Y';
			} else {
				data.CHECK_YN = 'N';
			}
			data.dirty = true;
			//변경여부
		});

	}

	function fnGridClick() {

	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox() {


		fnKendoDropDownList({
			id    : 'searchDateType',
			data  : [
				{"CODE":"ISSUE_DATE"	,"NAME":"발급일자"},
				{"CODE":"VALIDITY_PERIOD"		,"NAME":"유효기간"},
				{"CODE":"USE_DATE"		,"NAME":"사용일자"}
			],
			textField :"NAME",
			valueField : "CODE"
		});

		fnTagMkRadio({
			id    :  'couponUseYn',
			tagId : 'couponUseYn',
			chkVal: '',
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "COUPON_STATUS.NOTUSE"	, "NAME":'발급완료' },
						{ "CODE" : "COUPON_STATUS.USE"	, "NAME":'사용완료' },
						{ "CODE" : "COUPON_STATUS.EXPIRATION"	, "NAME":'기간만료' },
						{ "CODE" : "COUPON_STATUS.CANCEL"	, "NAME":'관리자회수' }
					],
			style : {}
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
	function fnBizCallback(id, data) {
		switch(id) {
		case 'update':

			fnKendoMessage({
				message : '쿠폰이 회수 되었습니다.',
				ok      : function(){ fnSearch();}
			});

			break;

		}
	}

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function() {
		fnSearch();
	};
	/** Common Clear*/
	$scope.fnClear = function() {
		fnClear();
	};

	$scope.fnCancelDeposit = function() {fnCancelDeposit(); };

	/** Common List*/
	$scope.fnList = function() {
		fnList();
	};

	$scope.fnExcelExport = function() {
		fnExcelExport();
	};



	//------------------------------- Html 버튼 바인딩  End -------------------------------

});
// document ready - END

