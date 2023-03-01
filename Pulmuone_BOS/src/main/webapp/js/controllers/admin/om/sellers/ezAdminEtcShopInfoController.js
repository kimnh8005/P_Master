﻿/**-----------------------------------------------------------------------------
 * system            : EZ Admin 외부몰 외부몰 코드 목록 조회  팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.08     천혜현          최초생성
 * @
 * **/
'use strict';

var outmallShopCodeGridDs, outmallShopCodeGridOpt, outmallShopCodeGrid;
var paramData ;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'ezAdminEtcShopInfo',
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
		outmallShopCodeGridDs.query();
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

		outmallShopCodeGridDs = new kendo.data.DataSource({
	        transport: {
	        	read : {
	                dataType : 'json',
	                type     : 'POST',
	                url: "/admin/outmall/sellers/getEZAdminEtcShopInfo",
                    beforeSend: function(req) {
                        req.setRequestHeader('authMenuID', CLICKED_MENU_ID);
                    }
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

		outmallShopCodeGridOpt = {
			dataSource: outmallShopCodeGridDs
			,navigatable: true
			,height:380
			,columns   : [
				{ 				 title : 'No'	, width: '30px'		, attributes : {style : 'text-align:center'}, template : '<span class="row-number"></span>'}
			   ,{ field: 'name'	,title : '외부몰'	, width: '150px'	, attributes : { style:'text-align:center' }}
			   ,{ field: 'code'	,title : '코드'	, width: '50px'		, attributes : { style:'text-align:center' }}
			   ,{ title:  fnGetLangData({key :"660",nullMsg :'관리' }), width: "60px", attributes:{ style:'text-align:center;'  , class:'forbiz-cell-readonly' }
			   			,command: [ { name: 'cEdit',  text: '선택'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon", click: fnOutmallShopCode,
							click: function(e) {
								 e.preventDefault();
						            var tr = $(e.target).closest("tr"); // get the current table row (tr)
						            var data = this.dataItem(tr);
						            fnOutmallShopCode(data);
							}
					}]
				}
			]
		};
		outmallShopCodeGrid = $('#outmallShopCodeGrid').initializeKendoGrid( outmallShopCodeGridOpt ).cKendoGrid();
		outmallShopCodeGrid.bind("dataBound", function() {
			var row_num = outmallShopCodeGridDs._total;
    		$("#outmallShopCodeGrid tbody > tr .row-number").each(function(index){
    			$(this).html(row_num);
    			row_num--;
    		});
        });
		$("#outmallShopCodeGrid").on("dblclick", "tbody>tr", function () {
				//fnGridClick();
		});

	}
	function fnOutmallShopCode(param){
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

