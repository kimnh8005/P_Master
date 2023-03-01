﻿/**-----------------------------------------------------------------------------
 * system            : EZ Admin 외부몰 택배사 코드 목록 조회  팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.23     박승현          최초생성
 * @
 * **/
'use strict';

var outmallShippingCompGridDs, outmallShippingCompGridOpt, outmallShippingCompGrid;
var paramData ;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'ezAdminEtcTransInfo',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnSearch();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnClose').kendoButton();
	}

	function fnSearch(){
		outmallShippingCompGridDs.query();
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

		outmallShippingCompGridDs = new kendo.data.DataSource({
	        transport: {
	        	read : {
	                dataType : 'json',
	                type     : 'POST',
	                url: "/admin/policy/shippingcomp/getEZAdminEtcTransInfo"
	            }
	        },
	        schema: {
	        	data  : function(response) {
        			return response.data.data
	            }
	        	, total : function(response) {
	    			return response.data.total
	            }
	        }
		});

		outmallShippingCompGridOpt = {
			dataSource: outmallShippingCompGridDs
			,navigatable: true
			,height:380
			,columns   : [
				{ title: "No", width: "30px", attributes : {style : "text-align:center"},
                    template : "<span class='row-number'></span>"
                  }
				,{ field:'name'		,title : '택배사명'	, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'code'	,title : '코드'		, width:'50px'	,attributes:{ style:'text-align:center' }}
				,{ title : "관리", width: "90px", attributes:{ style:'text-align:center;'  , class:'forbiz-cell-readonly' }
					,command: [ { name: 'cEdit',  text: '선택'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon", click: fnOutmallShippingComp,
							click: function(e) {
								 e.preventDefault();
						            var tr = $(e.target).closest("tr"); // get the current table row (tr)
						            var data = this.dataItem(tr);
						            fnOutmallShippingComp(data);
							}
					}]
				}
			]
		};
		outmallShippingCompGrid = $('#outmallShippingCompGrid').initializeKendoGrid( outmallShippingCompGridOpt ).cKendoGrid();
		outmallShippingCompGrid.bind("dataBound", function() {
			var row_num = outmallShippingCompGridDs._total;
    		$("#outmallShippingCompGrid tbody > tr .row-number").each(function(index){
    			$(this).html(row_num);
    			row_num--;
    		});
        });
		$("#outmallShippingCompGrid").on("dblclick", "tbody>tr", function () {
				//fnGridClick();
		});

	}
	function fnOutmallShippingComp(param){
		fnClose(param);
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnPopupButton = function(data){ fnPopupButton(data); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END

