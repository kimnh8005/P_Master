/**-----------------------------------------------------------------------------
 * description 		 : 정책관리 - 약관관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.03		안치열          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var bGridDs, bGridOpt, bGrid;
var clauseType = 'SCHEDULE1';

var clauseHeight;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'caluse',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

		fnSetPdfFont();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear, #fnClose, #fnView, #fnDownload1, #fnDownload2').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
	}
	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
		data["USE_YN"] ="Y";
		aGridDs.read(data);
		$('#bGrid').gridClear(true);
	}
	function fnClear(){
		$('#searchForm').formClear(true);
	}

	function fnNew(){
		var aMap = aGrid.dataItem(aGrid.select());
		var bMap = bGrid.dataItem(bGrid.select());

		if(aMap ==null){
			fnKendoMessage({message : '약관그룹을 선택하신 후 버튼 실행 시 해당 약관내용을 수정 하실 수 있습니다.' });
			return;
		}

		if(bGridDs.data().length != 0) {
			let isNew = true;
			for(let i = 0; i < bGridDs.data().length; i++){
				if(bGridDs.data()[i].executeType == "SCHEDULE") {
					isNew = false
					break;
				}
			}
			if(isNew) {
				fnClauseNew();
			} else {
				fnKendoMessage({message : '이미 예약된 약관정보가 있습니다. 해당 예약 약관정보를 수정해주세요.' });
			}
		} else {
			fnClauseNew();
		}
	}

	function fnClauseNew() {

		for(let i = 0; i < bGridDs.data().length; i++){
			if(bGridDs.data()[i].executeType == "RUN") {
	            $("#bGrid").data("kendoGrid").select("tr:eq(" + i +")");
			}
		}

		var aMap = aGrid.dataItem(aGrid.select());
		var bMap = bGrid.dataItem(bGrid.select());

		if(aMap ==null){
			fnKendoMessage({message : '약관그룹을 선택하신 후 버튼 실행 시 해당 약관내용을 수정 하실 수 있습니다.' });
			return ;
		}

		let clauseParam = {originContent:aMap.content
				, originClauseInfo:aMap.clauseInfo
				, psClauseGroupCd :aMap.psClauseGroupCd
				, clauseGroupName :aMap.clauseGroupName}

		if(bMap) {
			clauseParam.psClauseId = bMap.psClauseId;
		}

		fnKendoPopup({
			id     : 'clauseNew',
			title  : fnGetLangData({key :"27",nullMsg :'약관관리'}),
			width  : '900px',
			height : '750px',
			src    : '#/clauseNew',
			param  : clauseParam,
			success: function( id, data ){
				fnAGridClick();
			}
		});
	}

	function fnSave(){
		var url  = '/biz/ps/up/psup020/insert';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/biz/ps/up/psup020/update';
			cbId= 'update';
		}

		var data = $('#inputForm').formSerialize(true);

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					}
			});
		}else{

		}
	}

	function fnView(e){
		e.preventDefault();
		var bMap = bGrid.dataItem($(e.currentTarget).closest("tr"));

		if(bMap ==null){
			fnKendoMessage({message : '약관관리를 선택하신 후 버튼 실행 시 해당 약관내용을 수정 하실 수 있습니다.' });
			return ;
		}
		fnKendoPopup({
			id     : 'clauseView',
			title  : fnGetLangData({key :"27",nullMsg :'약관관리'}),
			width  : '900px',
			height : '750px',
			src    : '#/clauseView',
			param  : {psClauseId: bMap.psClauseId, psClauseGroupCd :bMap.psClauseGroupCd},
			success: function( id, data ){
				fnAGridClick();
			}
		});
		//window.open("#/clauseView?psClauseId="+bMap.psClauseId,"_blank", "fullscreen:no, width=500, height:100, scrollvars=yes");
    	//window.open("/layout.html#/itemMgm?itemId=" + dataItem.IL_ITEM_ID,"_blank");

	}

	function fnModify(e){
		e.preventDefault();
		if(status) {
			for(let i = 0; i < bGridDs.data().length; i++){
				if(bGridDs.data()[i].executeType == status) {
					$("#bGrid").data("kendoGrid").select("tr:eq(" + i + ")");
				}
			}
		}
		var aMap = aGrid.dataItem(aGrid.select());
		var bMap = bGrid.dataItem($(e.currentTarget).closest("tr"));

		if(bMap ==null){
			fnKendoMessage({message : '약관관리를 선택하신 후 버튼 실행 시 해당 약관내용을 수정 하실 수 있습니다.' });
			return ;
		}
		fnKendoPopup({
			id     : 'clauseModifyView',
			title  : fnGetLangData({key :"27",nullMsg :'약관관리'}),
			width  : '900px',
			height : '750px',
			src    : '#/clauseModifyView',
			param  : {originContent:aMap.content, originClauseInfo:aMap.clauseInfo, psClauseGroupCd :aMap.psClauseGroupCd, clauseGroupName :aMap.clauseGroupName , psClauseId : bMap.psClauseId},
			success: function( id, data ){
				fnAGridClick();
			}
		});
	}

	function fnDel(e){
		e.preventDefault();
		var bMap = bGrid.dataItem($(e.currentTarget).closest("tr"));
		if(bMap ==null){
			fnKendoMessage({message : '약관관리를 선택하신 후 버튼 실행 시 해당 약관내용을 수정 하실 수 있습니다.' });
			return ;
		}

//		fnKendoMessage({message:fnGetLangData({key :"4489",nullMsg :'삭제 하시겠습니까?' }), type : "confirm" ,ok :fnDelApply(e)  });
        fnKendoMessage({
            type    : "confirm",
            message : "삭제 하시겠습니까?",
            ok      : function()
            {
        		var url  = '/admin/policy/clause/delClause';
        		var cbId = 'delete';
        		var data = $('#inputForm').formSerialize(true);
        		var map = bGrid.dataItem($(e.currentTarget).closest("tr"));
        		map = {psClauseId: map.psClauseId, histType : "D"};
        		if( map.psClauseId ){
        			fnAjax({
        				url     : url,
        				params  : map,
        				success :
        					function( data ){
        						fnBizCallback(cbId, map);
        					}
        			});
        		}
            },
            cancel  : function(){

            }
        });
	}

	function fnDelApply(e){
		e.preventDefault();
		var url  = '/admin/policy/clause/delClause';
		var cbId = 'delete';
		var data = $('#inputForm').formSerialize(true);
		var map = bGrid.dataItem($(e.currentTarget).closest("tr"));
		map = {psClauseId: map.psClauseId, histType : "D"};
		if( map.psClauseId ){
			fnAjax({
				url     : url,
				params  : map,
				success :
					function( data ){
						fnBizCallback(cbId, map);
					}
			});
		}else{

		}
	}
	function fnClose(){

	}

	function fnDownload(e) {
		const downloadTarget = $(e.target).data("downloadTarget");

		const pdfArea = "#" + downloadTarget;

		clauseHeight = $(pdfArea).outerHeight();

		$(pdfArea).css("height", "auto");

		getPDF(pdfArea);

		// alert("워드 다운로드 기능 개발중입니다.");
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		fnShopKendoDropDownList({id:"ST_SHOP_ID"});

		aGridDs = fnGetDataSource({
			url      : "/admin/policy/clause/getClauseGroupNameList"
		});
		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			,height:550
			,scrollable: true
			,columns   : [
				{ field:'psClauseGroupCd'	,title : "Code"	, width:'50px',attributes:{ style:'text-align:center' }}
				,{ field:'clauseGroupName'	,title : "약관그룹명", width:'100px',attributes:{ style:'text-align:left' }}


			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("click", "tbody>tr", function () {
				fnAGridClick();
		});

		bGridDs = fnGetDataSource({
			url      : "/admin/policy/clause/getClauseList"
		});
		bGridOpt = {
			dataSource: bGridDs
			,navigatable: true
			,height:200
			,scrollable: true
			,columns   : [
					{ field:'createName'	,title : '작성자 정보'	, width:'100px'	,attributes:{ style:'text-align:center' }}
				,	{ field:'modifyName'	,title : '수정자 정보'	, width:'100px'	,attributes:{ style:'text-align:center' }}
				,	{ field:'clauseVersion'	,title : '버전정보'	, width:'100px'	,attributes:{ style:'text-align:center' }}
				,	{ field:'executeDate'	,title : '시행일시'	, width:'100px'	,attributes:{ style:'text-align:center' }}
				,	{ field:'modifyDate'	,title : '수정일시'	, width:'100px'	,attributes:{ style:'text-align:center' }}
				,   { command: [{ text: '보기', click : function(e) {fnView(e);} ,className: "btn-point btn-s"},
								{ text: '수정', click : function(e) {fnModify(e);} ,className: "btn-point btn-s" , visible: function(dataItem) { return dataItem.executeType==="SCHEDULE" }},
								{ text: '삭제', click: function(e) {fnDel(e);},className: "btn-red btn-s", visible: function(dataItem) { return dataItem.executeType ==="SCHEDULE" }}]
								, title: '관리', width: "210px", attributes:{ style:'text-align:left'  , class:'forbiz-cell-readonly #:#' }}   // EXECUTE_TYPE 별 버튼 제어 처리 확인 필요
				,	{field:'mandatoryYn', hidden: true}
				,	{field: 'executeType', hidden: true}
				]
		};
		bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();

		bGrid.bind('dataBound' , function(e){
			var thisGrid = bGrid.dataSource;
			var check = false;
			$.each(thisGrid._data,function(index,item){

					$('#serviceCaluse').html(item.content);
					check = true;

			});

		});

		$("#bGrid").on("click", "tbody>tr", function (e) {
			e.preventDefault();
            fnBGridClick(e);
		});
	}
	function fnAGridClick(){

		let aMap = aGrid.dataItem(aGrid.select());

		bGridDs.read( {"psClauseGroupCd":aMap.psClauseGroupCd}).then(() => {
			let bMap = $("#bGrid").data("kendoGrid");
			let bMapData = bMap.dataSource.data();

			if(bMapData[0]) {
				$("#serviceCaluse").html(fnTagConvert(bMapData[0].content))

				if(bMapData[0].mandatoryYn == "Y") {
					$(".clauseInfoBox").show();
					$("#clauseInfo").html(fnTagConvert(bMapData[0].clauseInfo))
				} else {
					$(".clauseInfoBox").hide();
				}
			} else {
				$("#serviceCaluse").html("");
				$("#clauseInfo").html("");
				$(".clauseInfoBox").hide();
			}

		});
	}

	function fnBGridClick(e){
		var aMap = aGrid.dataItem(aGrid.select());
		var map = bGrid.dataItem($(e.currentTarget).closest("tr"));
		$('#serviceCaluse').html(fnTagConvert(map.content));
		if(map.mandatoryYn == "Y"){
			$(".clauseInfoBox").show();
			$("#clauseInfo").html(fnTagConvert(map.clauseInfo))
		} else {
			$(".clauseInfoBox").hide();
		}
	};



	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#input1').focus();
	}
	function condiFocus(){
		$('#condition1').focus();
	}

		/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#searchForm').bindingForm(data, 'rows', true);
				break;
			case 'insert':
				if(data.rows =="DUP_DATA"){
					fnKendoMessage({message : fnGetLangData({key :"370",nullMsg :'중복입니다.' })  ,ok :inputFocus});
				}else{
					aGridDs.insert(data.rows);;
					fnKendoMessage({message : fnGetLangData({key :"369",nullMsg :'입력되었습니다.' }) ,ok :fnNew});
				}
				break;
			case 'save':
				fnKendoMessage({message : fnGetLangData({key :"368",nullMsg :'저장되었습니다.' })});
				break;
			case 'update':
				fnUpdateGrid(data,$("#aGrid"),"rows");
				fnKendoMessage({message : fnGetLangData({key :"367",nullMsg :'수정되었습니다.' }) });
				break;
			case 'delete':
				aGridDs.remove(data);
				fnKendoMessage({message : fnGetLangData({key :"366",nullMsg :'삭제되었습니다.' })});
				//fnSearch();
				fnAGridClick();
				break;

		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnView = function(){	 fnView();};
	$scope.fnClose = function( ){  fnClose();};
	/** Word Download */
	$scope.fnDownload = (e) => {
		fnDownload(e);
	}

	function fnSetPdfFont() {
		kendo.pdf.defineFont({
			"Noto Sans": "/contents/fonts/NotoSans.ttf",
			"Noto Sans|Light": "/contents/fonts/NotoSans-Light.ttf",
			"Noto Sans|Bold": "/contents/fonts/NotoSans-Bold.ttf",
			"Noto Sans|Italic": "/contents/fonts/NotoSans-Italic.ttf",
			"WebComponentsIcons": "https://kendo.cdn.telerik.com/2017.1.223/styles/fonts/glyphs/WebComponentsIcons.ttf"
		});
	}

	function getPDF(selector) {
		// kendo.drawing.drawDOM($(selector)).then(function (group) {
		// 		kendo.drawing.pdf.saveAs(group, "clause.pdf");
		// });
		kendo.drawing.drawDOM($(selector))
        .then(function(group) {
            // Render the result as a PDF file
            return kendo.drawing.exportPDF(group, {
                paperSize: "auto",
                margin: { left: "1cm", top: "1cm", right: "1cm", bottom: "1cm" }
            });
        })
        .done(function(data) {
            // Save the PDF file
            kendo.saveAs({
                dataURI: data,
                fileName: "clause.pdf",
                proxyURL: "https://demos.telerik.com/kendo-ui/service/export",
						});

						const _height = clauseHeight ? clauseHeight : 241 + "px";

						$(selector).css("height", _height);
        });
	}

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
