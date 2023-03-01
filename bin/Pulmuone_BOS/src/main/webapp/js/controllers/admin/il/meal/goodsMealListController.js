/**-----------------------------------------------------------------------------
 * description 		 : 일일상품 식단 등록 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.09.13		천혜현          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 50;
var aGridDs, aGridOpt, aGrid;
var CUR_SERVER_URL = fnGetServerUrl().mallUrl;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'goodsMealList',
			callback : fnUI
		});

	}


	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnInitBindEvent();

//		fnSearch();

	}




	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear').kendoButton();
	}

	function fnSearch(){
		$('#inputForm').formClear(false);
		var data;
		data = $('#searchForm').formSerialize(true);

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
	function fnClear(){
		$('#searchForm').formClear(true);
	}

	function fnSave(){


	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/il/meal/getGoodsMealList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50, 100],
				buttonCount : 5
			}
			,navigatable: true
			,columns   : [
				 { field:'no'	        ,title : 'No'	            , width:'50px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'ilItemCd'	    ,title : '품목코드<BR> 품목바코드'	, width:'150px',attributes:{ style:'text-align:center' }
				, template: function(dataItem) {
						let str = dataItem.ilItemCd;
						if(dataItem.itemBarcode != ''){
							str += ' /<BR>' + dataItem.itemBarcode;
						}
						return str;
					}}
				,{ field:'ilGoodsId'	,title : '상품코드'	    	, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'goodsNm'		,title : '프로모션명 /<BR> 상품명'	, width:'300px',attributes:{ style:'text-align:center;text-decoration: underline;color:blue;' }
					, template: function(dataItem) {
						let str = dataItem.goodsNm;
						if(dataItem.promotionNm != ''){
							str = dataItem.promotionNm+ ' /<BR>' + dataItem.goodsNm;
						}
						return str;
					}}
				,{ field:'compNm'	 	,title : '공급업체' 			, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'brandNm'	 	,title : '표준브랜드' 		, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'saleStatus'	,title : '판매상태' 			, width:'50px',attributes:{ style:'text-align:center' }}
				,{ field:'dispYn'	 	,title : '전시상태' 			, width:'50px',attributes:{ style:'text-align:center' }}
				,{ field:'mealPatternList',title : '식단스케쥴 정보(패턴명)' 	, width:'300px',attributes:{ style:'text-align:center' }
					, template: function(dataItem) {

						let htmlStr = "";
						if(dataItem.mealPatternList != null && dataItem.mealPatternList != ''){
							let mealPatternList = dataItem.mealPatternList.split('∀');
							mealPatternList.forEach(function(el, i){
								let patternCd = el.split('||')[0];
								let patternNm = el.split('||')[1];
								htmlStr += "<div id='"+ patternCd +"' name='patternCdDiv' value='"+ patternCd +"' style='text-align:center;text-decoration: underline;color:#0000ff;'>"+ patternNm +"</div>";
							});
						}

						return htmlStr;
					}}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("click", "tbody>tr>td", function () {

			var index = $(this).index();
			var map = aGrid.dataItem(aGrid.select());

			if(index == 3){
				// 상품상세 팝업
				fnGridClick(map);
			}
		});

		aGrid.bind("dataBound", function() {
			//row number
			var row_number = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(){
				$(this).html(row_number);
				row_number--;
			});

			//total count
            $('#totalCnt').text(aGridDs._total);
        });

	}




	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		// 식단분류
        fnKendoDropDownList({
            id : "mallDiv",
            tagId : "mallDiv",
            data : [ { "CODE" : ""	, "NAME":'전체' },
                     { "CODE" : "MALL_DIV.BABYMEAL", "NAME" : "베이비밀" },
                     { "CODE" : "MALL_DIV.EATSLIM", "NAME" : "잇슬림" }
                   ]
        });

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------
	function fnInitBindEvent(){

		// 식단패턴 클릭
		$('#ng-view').on("click","div[name=patternCdDiv]",function(){

			let patternCd = $(this).attr('value');

			fnKendoPopup({
				id: 'goodsMealMgm',
				title: fnGetLangData({ nullMsg: '식단 상세정보' }),
				param: { "patternCd": patternCd },
				src: '#/goodsMealMgm',
				width: '1300px',
				height: '900px',
				success: function(id, data) {

				}
			});
		});
	}

    // 상품상세 페이지 호출
	function fnGridClick(map){
		var mallUrl = CUR_SERVER_URL + "/shop/goodsView?goods=" + map.ilGoodsId+"&preview=Y";
		window.open(mallUrl);
	}

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				$('#inputForm').bindingForm(data, "rows", true);

				$(".supplierCompany-box__list__item").remove();

				fnKendoInputPoup({height:"1300px" ,width:"600px",title:{key :"5876",nullMsg :'컨텐츠 수정' } });

				break;
			case 'insert':
			case 'update':
				fnKendoMessage({
						message:"저장되었습니다.",
						ok:function(){
							fnSearch();
							fnClose();
						}
				});
			break;

		}
	}


	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
