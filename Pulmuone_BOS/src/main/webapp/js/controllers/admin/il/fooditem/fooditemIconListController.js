/**-----------------------------------------------------------------------------
 * description 		 : 상품인증정보관리
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var viwImageSrc = "";

$(document).ready(function() {
	var imageUploadMaxLimit = 512000;  // 이미지 업로드 최대 크기 (단위: byte)

	var publicStorageUrl = null;  // image url path

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'ilCertification',
			callback : fnUI
		});

	}

	// 화면 설정
	function fnUI(){

		fnInitPublicStorageUrl();  // get public storage url ---------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitMaskTextBox();  // Initialize Input Mask ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();
	};

	//--------------------------------- get public storage url Start---------------------------------

	// 이미지 URL 조회
	function fnInitPublicStorageUrl() {
		publicStorageUrl = fnGetPublicStorageUrl();
	};

	//--------------------------------- get public storage url End---------------------------------


	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$('#fnSearch,  #fnClear').kendoButton();
	};

	// 조회
	function fnSearch(){

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
	};

	// 화면 초기화
	function fnClear(){
		$('#searchForm').formClear(true);
		$("span#searchUseYn input:radio").eq(0).click();	//radio init
	};

	// 신규 팝업
	function fnNew(){
		viwImageSrc = "";
		$('#inputForm').formClear(true);

		$(".fileText").html("");

		$("#uploadUserFile").val("");
		$("#viewImage").attr("src", "/contents/images/noimg.png");

		$("tr[addArea]").hide();
		//$("#viewImage").hide();
		$("#uploadUserFile").prop("required", true);
		$("#createDate").html("");
		$("#modifyDate").html("");

		fnKendoInputPoup({height:"780px" ,width:"1000px", title:{ nullMsg :'식단품목 아이콘 등록' } });
	};

	// 저장
	function fnSave(){
		var url  = '/admin/goods/fooditem/addFooditemIcon';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/goods/fooditem/putFooditemIcon';
			cbId= 'update';

			$("#uploadUserFile").prop("required", false);
		}

		var data = $('#inputForm').formSerialize(true);

		if( data.rtnValid ) {
			if( $("#uploadUserFile").val() ){
				fnAjaxSubmit({
					   form    : 'inputForm',
					   fileUrl : "/fileUpload",
					   url     : url,
					   params  : data,
					   storageType : "public",
					   domain : "il",
					   success : function(data){
					      fnBizCallback(cbId, data);
					   },
					   isAction : 'batch'
				});
			} else {
				fnAjax({
					url     : url,
					params  : data,
					success :
						function( data ){
							fnBizCallback(cbId, data);
						},
						isAction : 'batch'
				});
			}
		}
	};

	// 삭제
	function fnDel(){
		fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
			fnAjax({
				url     : '/admin/goods/fooditem/delFooditemIcon',
				contentType : "application/json",
				params  : {ilFooditemIconId : $("#ilFooditemIconId").val()},
				success :
					function( data ){
						fnBizCallback("delete",data);
					},
				isAction : 'delete'
			});
		}});
	};

	// 닫기
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	};

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------

	// 그리드 초기화
	function fnInitGrid(){

	    aGridDs = fnGetPagingDataSource({
			url      : '/admin/goods/fooditem/getFooditemIconList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
//			        ,height:550
			,columns   : [
				 {title : "NO", width : "50px", attributes : {style : "text-align:center"}, template : "<span class='row-number'></span>" }
				,{ field:'fooditemType'			,title : '식단분류'						, width:'40px'	, attributes:{ style:'text-align:center' }}
				,{title : '아이콘명', attributes:{ style:'text-align:center' },
					 columns: [  {field: "imageName"								, width: 50		, attributes:{ style:'text-align:center' },
						 					template: function(dataItem) {
						 						var imageUrl = dataItem.imageName ? publicStorageUrl + dataItem.imagePath + dataItem.imageName : '/contents/images/noimg.png';
						 						return "<img src='" + imageUrl + "' onerror='this.src=\"/contents/images/noimg.png\"' width='126' />";
						 					}}
					,{field: "fooditemIconName", width: 60, attributes:{ style:'text-align:center' }}]	}
				,{title : '등록일/수정일'						, width:'100px'	, attributes:{ style:'text-align:center' }
						,template: function(dataItem) {
							  var html = kendo.htmlEncode(dataItem.createDate);
							  if (dataItem.modifyDate)
								 html += " / " + kendo.htmlEncode(dataItem.modifyDate);
							  return html;
						}}
				,{ field:'useYn'			,title : '사용여부'						, width:'40px'	, attributes:{ style:'text-align:center' }}
				,{ command: [{ text: "수정", click: fnModifyClick, visible: function() { return fnIsProgramAuth("SAVE")} }]
					, title : '관리'		, width:'40px'	, attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
			]
			,selectable : false
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function() {
            let rowNum = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);

            $("#aGrid tbody > tr .row-number").each(function(index){
                $(this).html(rowNum);
                rowNum--;
            });

            $('#countTotalSpan').text( kendo.toString(aGridDs._total, "n0") );
        });

		$(aGrid.thead).find("tr").eq(1).hide();
	};

	// 수정 버튼 클릭
	function fnModifyClick(e) {
		e.preventDefault();

		$("#uploadUserFile").prop("required", false);

		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));

		fnAjax({
			url     : '/admin/goods/fooditem/getFooditemIcon',
			method  : 'GET',
			params  : {ilFooditemIconId : dataItem.ilFooditemIconId},
			success :
				function( data ){
					fnBizCallback("select", data);
				},
			isAction : 'select'
		});
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Input Mask Start ------------------------------------------------

	// 입력제어
	function fnInitMaskTextBox() {
		fnInputValidationForHangulAlphabetNumberSpace("fooditemIconName");
		fnInputValidationForHangulAlphabetNumberSpace("titleNm");
	};

	//---------------Initialize Input Mask End ------------------------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		// 검색 - 입고예정수량
		fnTagMkRadio({
			id    :  'searchUseYn',
			tagId : 'searchUseYn',
			chkVal: 'ALL',
			data  : [   { "CODE" : "ALL"	, "NAME":'전체' },
						{ "CODE" : "Y"		, "NAME":'예' },
						{ "CODE" : "N"		, "NAME":'아니오' }
					],
			style : {}
		});

		// 신규/수정 폼 사용여부
		fnTagMkRadio({
			id    :  'useYn',
			tagId : 'useYn',
			chkVal: 'Y',
			data  : [   { "CODE" : "Y"		, "NAME":'예' },
						{ "CODE" : "N"		, "NAME":'아니오' }
					],
			style : {}
		});

		// 식단분류
		fnKendoDropDownList({
			id: "fooditemType",
			data: [
				{ "CODE" : "", "NAME" : "식단분류" }
				, { "CODE" : "B", "NAME" : "베이비밀" }
				, { "CODE" : "E", "NAME" : "잇슬림" }
			],
			valueField: "CODE",
			textField: "NAME"
		});

		// 파일선택
		$("#uploadUserFile").on("change", function(e) {
			if (imageUploadMaxLimit < this.files[0].size) { // 이미지 업로드 용량 체크
				fnKendoMessage({
					message : '이미지 업로드 허용 최대 용량은 ' + parseInt(imageUploadMaxLimit / 1024) + ' kb 입니다.',
					ok : function(e) {}
				});
				return;
			}

			var filetype = this.value.substring(this.value.lastIndexOf(".") + 1);
		    if (['jpg','jpeg','gif','png'].indexOf(filetype) == -1) {
		    	fnKendoMessage({ message : "jpg, jpeg, gif, png 만 업로드 가능한 확장자입니다."});
		    	this.value= "";
		    } else {
		    	let reader = new FileReader();

                reader.onload = function(ele) {
					var img = new Image();
					img.src = ele.target.result;
					img.onload = function () {
						if(this.width > 300 || this.height > 300 ) {
							fnKendoMessage({
								message : "이미지 사이즈는 300px*300px까지 첨부가능합니다.(현재사이즈:"+this.width+"px*"+this.height+"px)"
							});
							$("#uploadUserFile").val("");
							$("#viewImage").attr("src", viwImageSrc);
						} else {
							$("#viewImage").attr("src", ele.target.result); // FileReader 로 생성한 상품 이미지 url
						}
					};
                };

                reader.readAsDataURL(this.files[0]);
		    }
		});

		// 켄도팝업 초기화
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#inputForm').bindingForm(data, 'fooditemIconVo', true);

				$(".fileText").html("");
				$("#uploadUserFile").val("");

				var detailData = data.fooditemIconVo;

				$("#createDate").html(detailData.createDate + " / " + detailData.createLoginName + " (" + detailData.createLoginId + ")");

				if( detailData.modifyLoginId != null ){
				    $("#modifyDate").html(detailData.modifyDate + " / " + detailData.modifyLoginName + " (" + detailData.modifyLoginId + ")");
				}else{
				    $("#modifyDate").html("");
				}

				viwImageSrc = publicStorageUrl + detailData.imagePath + detailData.imageName;
				$("#viewImage").prop("src", viwImageSrc);
				$("tr[addArea]").show();
				$("#viewImage").show();

				$("#imageOriginName").html(detailData.imageOriginName);

				$('#fnDel').kendoButton().data("kendoButton").enable(true);
				fnKendoInputPoup({height:"780px" ,width:"1000px", title:{ nullMsg :'식단품목 아이콘 수정' } });
				break;
			case 'insert':
				fnKendoMessage({message : '저장되었습니다.'});
				$('#kendoPopup').data('kendoWindow').close();
				fnSearch();
				break;
			case 'update':
				aGridDs.query();
				fnKendoMessage({message : '수정되었습니다.'});
				fnClose();
				break;
			case 'delete':
				aGridDs.query();
				fnClose();
				fnKendoMessage({message : '삭제되었습니다.'});
				break;

		}
	};

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
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
