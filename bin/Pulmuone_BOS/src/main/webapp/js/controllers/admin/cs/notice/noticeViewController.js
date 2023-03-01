/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 관리자공지사항리스트
 *
 * @ 실제 처리는 noticeController.js, notice.html
 *
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.04.16    dgyoun        최초생성
 * @
 ******************************************************************************/

'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();		//Initialize Page Call ---------------------------------

	function fnInitialize(){		//Initialize PageR
		$scope.$emit('fnIsMenu', { flag : 'true' });
		$("#lnb, #lnb-closeBtn").css({"display": "none"});
		fnPageInfo({ PG_ID  : 'noticeView', callback : fnUI });
	}

	function fnUI(){
		fnTranslate();		//다국어 변환--------------------------------------------
		fnInitButton();		//Initialize Button  ---------------------------------
		fnInitOptionBox();		//Initialize Option Box ------------------------------------
		fnInitGrid();		//Initialize Grid ------------------------------------

		fnSearch();
	}

	function fnSearch(){
		var data = {useYn : 'Y'}; // 공지사항 목록에서는 사용여부가 'Y'인 목록만 조회한다.

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

	//--------------------------------- Button Start---------------------------------
	function fnInitButton(){
	}
	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url : '/admin/cs/notice/getNoticeList'
			,pageSize : PAGE_SIZE
		});


		aGridOpt = {
			dataSource : aGridDs
			, pageable : { pageSizes : [20, 30, 50], buttonCount : 5}
			, navigatable: true
			//,height:550
			,columns : [
				{ field : 'no', title : 'NO', width:'50px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;" }, filterable : { cell : { showOperators: false }}}
				, { field : 'title', title : '제목', width:'350px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;"}}
				, { field : 'createDate', title : '등록일자', width:'150px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;"}}
				, { field : 'views', title : '조회수', width:'50px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;"}}
				, { field : 'csCompanyBbsId', hidden:true }
			]
			, change : fnGridClick
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function(){
			$('#totalCnt').text(aGridDs._total);
		});
	}

	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
		var option = new Object();
		option.menuId = 200;
		option.data = {
			csCompanyBbsId	:map.csCompanyBbsId,
		};
		option.url = "#/noticePopupView";
		option.target = '_blank';

		fnGoNewPage(option);  // 새페이지(탭)
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
	}
	//---------------Initialize Option Box End ------------------------------------------------


	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
	}
	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END