/**-----------------------------------------------------------------------------
 * description 		 : 적립금 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.12		안치열          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var detailGridDs, detailGridOpt, detailGrid;
var detailGridPvDs, detailGridPvOpt, detailGridPv;

var viewYn = false;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------


	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'pointHistoryList',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();

		fnPointDetailHistoryInit();

		setDefaultDatePicker();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear, #fnExcel').kendoButton();
	}

	function fnSearch(){

		var data;
		data = $('#searchForm').formSerialize(true);
		var query = {
                page         : 1
              , pageSize     : PAGE_SIZE
              , filterLength : fnSearchData(data).length
              , filter :  {
                    filters : fnSearchData(data)
                }
		};
		aGridDs.query( query );

	}

	function numberFormat(inputNumber) {
	   return inputNumber.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}

	function fnClear() {
		$('#searchForm').formClear(true);
		//$('#aGrid').gridClear(true);
		setDefaultDatePicker();
	}

	function setDefaultDatePicker() {
		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})

		$("button[data-id='fnDateBtn3']").attr("fb-btn-active", true);

		var today = fnGetToday();

		$("#startDate").data("kendoDatePicker").value(fnGetDayMinus(today, 6));
		$("#endDate").data("kendoDatePicker").value(today);
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


	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------



	function fnInitGrid(){


		aGridDs = fnGetPagingDataSource({
			url		: '/admin/promotion/pointHistory/getPointHistoryList',
			pageSize: PAGE_SIZE,
			requestEnd : function(e){
			    if(e.response.data.totalIssuePoint != null){
                    $('#totalIssuePoint').text(numberFormat(e.response.data.totalIssuePoint) + '원');
                }else{
                    $('#totalIssuePoint').text('0원');
                }
                if(e.response.data.totalUsePoint != null){
                    $('#totalUsePoint').text(numberFormat(e.response.data.totalUsePoint) + '원');
                }else{
                    $('#totalUsePoint').text('0원');
                }
                if(e.response.data.totalExpirationPoint != null){
                   $('#totalExpirationPoint').text(numberFormat(e.response.data.totalExpirationPoint) + '원');
                }else{
                    $('#totalExpirationPoint').text('0원');
                }
                if(e.response.data.totalMonthExpirationPoint != null){
                    $('#totalMonthExpirationPoint').text(numberFormat(e.response.data.totalMonthExpirationPoint) + '원');
                }else{
                    $('#totalMonthExpirationPoint').text('0원');
                }
			}
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,scrollable: true
//			,height:400
			,columns   : [
				{ field:'no'		,title : 'No'				, width:'50px',attributes:{ style:'text-align:center' }, template:"<span class='row-number'></span>"}
				,{ field:'pmPointUsedId'		,hidden:true}
				,{ field:'urUserName'		,title : '회원명'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'urUserId'		,title : '회원ID'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'paymentTypeName'		,title : '구분'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'pointTypeName'		,title : '적립금 설정'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'pmPointId'		,title : '적립금번호'				, width:'120px',attributes:{ style:'text-align:center' }}
				,{ field:'pointName'		,title : '적립금명'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'amount'		,title : '내역'				, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						let returnValue;
						if(dataItem.paymentType == 'POINT_PAYMENT_TP.PROVISION'){
							if(dataItem.validityDay == ''){
								returnValue = numberFormat(dataItem.amount) + '원';
							}else{
								returnValue = numberFormat(dataItem.amount) + "원 (" + dataItem.validityDay + "일)";
							}
						}else{
							returnValue = numberFormat(dataItem.amount) + '원';
						}
						return returnValue;
					}
				}
				,{ field:'odid'		,title : '주문번호'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'pointDetailType'		,hidden:true}
				,{ field:'pointDetailTypeName'		,title : '상세구분'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '지급/차감 일자'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'calculateDay'		,title : '유효기간'				, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						let returnValue;
						if(dataItem.detailCount > 1){
							returnValue = '<div id="pageMgrButtonArea" class="btn-area textCenter">'
			                            + '<button type="button" class="btn-gray btn-s" kind="viewHistory">상세보기</button>'
			                            + '</div>';
						}else{
							returnValue = dataItem.expirationDt;
							//returnValue = '';
						}
						return returnValue;
					}
				}
                ,{ field:'organizationNm'	,title : '분담조직'			, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'pointUsedMsg'		,title : '사유'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'createId'		,title : '현업 ID'				, width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'createNm'		,title : '현업 이름'				, width:'100px',attributes:{ style:'text-align:center' }}
			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function(){

	        	var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
				$("#aGrid tbody > tr .row-number").each(function(index){
					$(this).html(row_num);
					row_num--;
				});

	        	$('#totalCnt').text(aGridDs._total);
	        });

		   $('#aGrid').on("click", "button[kind=viewHistory]", function(e) {
			      e.preventDefault();

			      let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));

			      fnBtnViewHistory(dataItem.pmPointUsedId, dataItem.paymentTypeName, dataItem.pointDetailTypeName, dataItem.amount);

			    });

	}


	function fnPointDetailHistoryInit(){
	    $("#kendoPopup").kendoWindow({
           width : 450,
           height : 500,
           title : "적립금 상세 내역",
           visible : false,
           modal : true
       });

		detailGridDs =  new kendo.data.DataSource();

		detailGridOpt = {
	            dataSource : detailGridDs,
	            editable : false,
	            columns : [ {field : 'pmPointUsedDetlId'	, title : '적립 ID'	, width : '60px', attributes : {style : 'text-align:center'}},
	            	{field : 'createDate'	, title : '등록일'	, width : '60px', attributes : {style : 'text-align:center'}},
	            	{field : 'paymentTypeName'	, title : '구분'	, width : '60px', attributes : {style : 'text-align:center'}},
	            	{field : 'pointDetailTypeName'	, title : '상세'	, width : '60px', attributes : {style : 'text-align:center'}},
	            	{field : 'provisionAmount'	, title : '내역'	, width : '60px', attributes : {style : 'text-align:center'},
	            		template : function(dataItem){
							let returnValue;
								returnValue = numberFormat(dataItem.provisionAmount) + '원';
							return returnValue;
						}
						//, hidden : viewYn
	            	},
	            	{field : 'usePoint'	, title : '사용금액'	, width : '60px', attributes : {style : 'text-align:center'},
	            		template : function(dataItem){
							let returnValue;
								returnValue = numberFormat(dataItem.usePoint) + '원';
							return returnValue;
						}
	            		},
	            	{field : 'expirationDate'	, title : '유효기간'	, width : '60px', attributes : {style : 'text-align:center'}},
	            	{field : 'comment'	, title : '비고'	, width : '60px', attributes : {style : 'text-align:center'}}
	            ]
	        };

        if(viewYn == true){
            detailGridPvDs =  new kendo.data.DataSource();

            detailGridPvOpt = {
                dataSource : detailGridPvDs,
                editable : false,
                columns : [ {field : 'pmPointUsedDetlId'	, title : '적립 ID'	, width : '60px', attributes : {style : 'text-align:center'}},
                    {field : 'createDate'	, title : '등록일'	, width : '60px', attributes : {style : 'text-align:center'}},
                    {field : 'paymentTypeName'	, title : '구분'	, width : '60px', attributes : {style : 'text-align:center'}},
                    {field : 'pointDetailTypeName'	, title : '상세'	, width : '60px', attributes : {style : 'text-align:center'}},
                    {field : 'usePoint'	, title : '사용금액'	, width : '60px', attributes : {style : 'text-align:center'},
                        template : function(dataItem){
                            let returnValue;
                                returnValue = numberFormat(dataItem.usePoint) + '원';
                            return returnValue;
                        }
                    },
                    {field : 'expirationDate'	, title : '유효기간'	, width : '60px', attributes : {style : 'text-align:center'}},
                    {field : 'comment'	, title : '비고'	, width : '60px', attributes : {style : 'text-align:center'}}
                ]
            };

            detailGridPv= $('#detailGridPv').initializeKendoGrid(detailGridPvOpt).cKendoGrid();
        }else{
            detailGrid= $('#detailGrid').initializeKendoGrid(detailGridOpt).cKendoGrid();
        }
	}

	function fnBtnViewHistory(pmPointUsedId, paymentTypeName, pointDetailTypeName, amount){
		//var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/promotion/pointHistory/getPointDetailHistory',
			params  : {pmPointUsedId: pmPointUsedId},
			success :
				function( data ){
					if(paymentTypeName =='지급'){
					    viewYn = true;
					    $('#detailGrid').hide();
					    $('#detailGridPv').show();
					}else{
					    viewYn = false;
					    $('#detailGrid').show();
					    $('#detailGridPv').hide();
					}
					fnBizCallback("select",data, paymentTypeName, pointDetailTypeName, amount);
				},
			isAction : 'select'
		});

	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});


    	//================ 상세구분 ===============
    	fnKendoDropDownList({
    			id  : 'searchPointDetailType',
    	        url : "/admin/comn/getCodeList",
    	        params : {"stCommonCodeMasterCode" : "POINT_PROCESS_TP", "useYn" :"Y"},
    	        textField :"NAME",
    			valueField : "CODE",
    			blank : "전체"
    	});

    	//================ 구분 ===============
		fnTagMkRadio({
			id    :  'searchPaymentType',
			tagId : 'searchPaymentType',
			chkVal: '',
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "POINT_PAYMENT_TP.PROVISION"	, "NAME":'지급' },
						{ "CODE" : "POINT_PAYMENT_TP.DEDUCTION"	, "NAME":'차감' }
					],
			style : {}
		});

		//================데이트 피커===============
        fnKendoDatePicker({
			id    : 'startDate',
			format: 'yyyy-MM-dd',
			defVal: fnGetDayMinus(fnGetToday(),6),
            defType : 'oneWeek'
		});
		fnKendoDatePicker({
			id    : 'endDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startDate',
			btnEndId : 'endDate',
			defVal: fnGetToday(),
            defType : 'oneWeek'
		});

	}


	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------
	function fnBizCallback( id, data, paymentTypeName, pointDetailTypeName, amount ){
		switch(id){
			case 'select':
				$('#inputForm').bindingForm(data, "rows", true);

				$('#paymentTypeName').val(paymentTypeName);
				$('#pointDetailTypeName').val(pointDetailTypeName);
				$('#amount').val(numberFormat(amount) + '원');
                fnPointDetailHistoryInit();
                if(viewYn == true){
				    $("#detailGridPv").data("kendoGrid").dataSource.data( data.rows );
                }else{
				    $("#detailGrid").data("kendoGrid").dataSource.data( data.rows );
                }

				fnKendoInputPoup({height:"500px" ,width:"700px",title:{key :"5876",nullMsg :'적립금 상세 내역' } });

				break;
		}
	}

    function fnExcel(){
		var excelDownType = $("#excelDownType option:selected").val();
		if(excelDownType != "") {
			var data = $('#searchForm').formSerialize(true);
			data.excelDownType = excelDownType;
			fnExcelDownload('/admin/promotion/pointHistory/getPointHistoryListExportExcel', data);
		} else {
			fnKendoMessage({message : "엑셀 다운로드 양식을 선택해주세요."});
			return;
		}
	}

    

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear = function( ){	fnClear();};
	/** Common Excel*/
	$scope.fnExcel = function(){	 fnExcel();};


	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
