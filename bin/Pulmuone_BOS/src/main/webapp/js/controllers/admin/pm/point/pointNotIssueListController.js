/**-----------------------------------------------------------------------------
 * description 		 : 적립금 미지급 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.05.03		안치열          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var detailGridDs, detailGridOpt, detailGrid;
var maxPoint;
$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------


	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'pointNotIssueList',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();

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


    function numberFormat(inputNumber) {
 	   return inputNumber.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
 	}


    function fnExcel(){

    	var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/promotion/notissue/getPointNotIssueListExportExcel', data);

    }

    // 적립금 지급
    function fnIssuePoint(pmPointNotIssueId, sumAmount, redepositPointVal, issueValue, urUserId){
    	var today = fnGetToday();
    	if(Number(sumAmount) > maxPoint){
    		fnKendoMessage({ message : "보유 적립금 50만원으로 지급 할 수 없습니다." });
    	    return;
    	}else{
			fnAjax({
				url     : '/admiun/promotion/notissue/depositNotIssuePoints',
				params : {"pmPointNotIssueId" : pmPointNotIssueId, "urUserId" : urUserId},
				success :
					function( data ){

						if( (Number(sumAmount) + Number(redepositPointVal)) < maxPoint ){
			    			fnKendoMessage({ message : numberFormat(String(redepositPointVal)) + "원이 지급 되었습니다. *보유 적립금 : " + numberFormat(String(Number(sumAmount) + Number(redepositPointVal))) + "원"});
			    			fnSearch();
			    		}else{
			    			fnKendoMessage({ message : "부분 적립금 " + numberFormat(String(maxPoint - Number(sumAmount))) + "원이 지급 되었습니다. *잔여 적립금 : " + numberFormat(String(Number(redepositPointVal) - (maxPoint - Number(sumAmount)))) + "원"});
			    			fnSearch();
			    		}


					},
					isAction : 'batch'
			});

    	}
    }


	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------

	function fnInitGrid(){

		aGridDs = fnGetPagingDataSource({
			url		: '/admin/promotion/notissue/getPointNotIssueList',
			pageSize: PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			,scrollable: true
//			,height:400
			,columns   : [
				 { field:'no'					,title : 'No'				, width:'50px',attributes:{ style:'text-align:center' }, template:"<span class='row-number'></span>"}
				,{ field:'pmPointNotIssueId'	,hidden:true}
				,{ field:'userNm'				,title : '회원명'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'loginId'				,title : '회원ID'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'pointProcessTpName'	,title : '지급 구분'			, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'issueVal'				,title : '미지급 금액'			, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						let returnValue;
							returnValue = numberFormat(dataItem.issueVal) + '원';
						return returnValue;
					}
				}
				,{ field:'partPointVal'			,title : '부분지급 금액'		, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						let returnValue;
							returnValue = numberFormat(dataItem.partPointVal) + '원';
						return returnValue;
					}
				}
				,{ field:'redepositPointVal'	,title : '잔여지급 금액'		, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						let returnValue;
							returnValue = numberFormat(dataItem.redepositPointVal) + '원';
						return returnValue;
					}
				}
				,{ field:'organizationNm'		,title : '분담조직'			, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'createDt'				,title : '등록일자'			, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'expirationDt'			,title : '유효기간'			, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'pointUsedMsg'			,title : '관리'				, width:'150px',attributes:{ style:'text-align:center' },
					 command : [
	                        { text    : '적립금 지급'    , className: "btn-gray btn-s"
	                        , click   : function(e) {
	                                      e.preventDefault();
	                                      var tr = $(e.target).closest("tr");
	                                      var data = this.dataItem(tr);
	                                      fnKendoMessage({message:'적립금을 지급하시겠습니까?',type :"confirm",ok : function(){fnIssuePoint(data.pmPointNotIssueId, data.sumAmount, data.redepositPointVal, data.issueValue, data.urUserId)}});
	                                    }
	                        , visible : function(dataItem) { return dataItem.redepositPointVal > 0  && fnGetToday() < dataItem.expirationDt}
	                        }
	                      ]
				}
			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function(){
	        	var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
	        	var totalAmount = 0;

				$("#aGrid tbody > tr .row-number").each(function(index){
					$(this).html(row_num);
					row_num--;
					if(fnGetToday() < aGrid._data[index].depositDt){
						totalAmount = totalAmount + Number(aGrid._data[index].redepositPointVal);
					}
				});

	        	$('#totalCnt').text(aGridDs._total);
	        	$('#totalAmount').text(numberFormat(String(totalAmount)));

	        	if(aGrid._data != undefined){
	        		maxPoint = aGrid._data[0].maxPoint;
	        	}
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


    	//=============== 검색어 구분 ==============
    	fnKendoDropDownList({
			id    : 'searchSelect',
			data  : [{ "CODE" : "SEARCH_SELECT.USER_NAME" , "NAME":"회원명"},
				     { "CODE" : "SEARCH_SELECT.USE_ID" , "NAME":"회원ID"}
					]
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

				$("#detailGrid").data("kendoGrid").dataSource.data( data.rows );

				fnKendoInputPoup({height:"500px" ,width:"700px",title:{key :"5876",nullMsg :'적립금 상세 내역' } });

				break;
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
