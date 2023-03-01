/**-----------------------------------------------------------------------------
 * system           : 쇼핑몰 클레임 사유
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.20		천혜현          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var claimGridDs, claimGridOpt, claimGrid;
var searchLClaimCtgryDropDownList, searchMClaimCtgryDropDownList, searchSClaimCtgryDropDownList;
var LClaimCtgryDropDownList, MClaimCtgryDropDownList, SClaimCtgryDropDownList;
var supplierList = new Object();
var sClaimCtgryDropDownListData = new Object();

$(document).ready(function() {
	importScript("/js/controllers/admin/ps/claim/policyClaimBosFunction.js", function (){
		fnInitialize();
	});

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });
        fnPageInfo({ PG_ID  : "policyClaimBos", callback : fnUI});
    };

    // 화면 UI 초기화
    function fnUI(){
        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitOptionBox();//Initialize Option Box ------------------------------------
        fnInitGrid();   //Initialize Grid ------------------------------------
        claimBosSubmitUtil.search();
        bindEvents();
    };

	//--------------------------------- Button Start---------------------------------
    // 버튼 초기화
	function fnInitButton(){
		$("#fnSearch, #fnClear").kendoButton();
	};

    // 초기화
    function fnClear() {
        $("#searchForm").formClear(true);
    };

    // 조회
    function fnSearch(){
        let data = $("#searchForm").formSerialize(true);
        let query = { page : 1,
                      pageSize : PAGE_SIZE,
                      filterLength : fnSearchData(data).length,
                      filter : { filters : fnSearchData(data) }
        };
        claimGridDs.query(query);
    };
    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    	claimGridDs = fnGetEditPagingDataSource({
            url      	  : "/admin/policy/claim/getPolicyClaimBosList",
            pageSize 	  : PAGE_SIZE
        });

    	claimGridOpt = {
            dataSource : claimGridDs,
            pageable : { pageSizes: [20, 30, 50], buttonCount : 10 },
            navigatable: true,
            columns   : [
			               { field: 'rowNumber'				, title: 'No'					, width: '30px'		, attributes : { style: 'text-align:center' }
			               			, template: function (dataItem){ return fnKendoGridPagenation(claimGrid.dataSource,dataItem); }}
			            , { field : "lclaimCtgryName"		, title : "클레임 사유(대)"			, width : "40px"	, attributes : {style : "text-align:center"}}
			            , { field : "mclaimCtgryName"		, title : "클레임 사유(중)"			, width : "40px"	, attributes : {style : "text-align:center"} }
			            , { field : "sclaimCtgryName"		, title : "귀책처"				, width : "40px"	, attributes : {style : "text-align:center"}
			            		, template : function (dataItem){
			            			var targetType = dataItem.targetType == 'B' ? '(구매자 귀책)' : '(판매자 귀책)' ;
			            			return dataItem.sclaimCtgryName + '</br>' + targetType;
			            		}}
			            , { field : "claimSupplierList"		, title : "공급업체 사유 (반품회수)"	, width : "120px"	, attributes : {style : "text-align:center"}
					            , template: function (dataItem){

					            		var claimList = dataItem.claimSupplierList;
					            		var str = "";
						            	for(var i = 0; i < claimList.length; i++){
											var claimName = claimList[i].claimName != null ? claimList[i].claimName : '';
						            		str +="<div style='text-align:left;'>"
						            		str += claimList[i].supplierName + " : " + claimName;
						            		str +="</div>";
						            	}
						            	return str;
										}}
			            , { field : "nonClaimSupplierList"	, title : "공급업체 사유 (반품 미회수)"	, width : "150px"	, attributes : {style : "text-align:center"}
						    	, template: function (dataItem){

				            		var nonClaimList = dataItem.nonClaimSupplierList;
				            		var str = "";
					            	for(var i = 0; i < nonClaimList.length; i++){
					            		var nonClaimName = nonClaimList[i].nonClaimName != null ? nonClaimList[i].nonClaimName : '';
					            		str +="<div style='text-align:left;'>"
					            		str += nonClaimList[i].supplierName + " : " + nonClaimName;
					            		str +="</div>";
					            	}
					            	return str;
									}}
			            , { field:'management'      , title: '관리'         , width:"40px" , attributes: {style:'text-align:center;'}
							     , template: function(dataItem) {
							    	 var btnStr = '';
							    	 if(fnIsProgramAuth("SAVE")) {
										 btnStr += '<button type="button" class="btn-point btn-s" kind="policyclaimBosUpdate">수정 </button>';
									 }
							    	 if(fnIsProgramAuth("DELETE") && dataItem.mallClaimReasonList.length == 0) {
											 btnStr += '<button type="button" class="btn-red btn-s marginL5" kind="policyclaimBosDelete">삭제 </button>'
									 }
							    	 return btnStr;
							     }}
            ],
        };

    	claimGrid = $("#claimGrid").initializeKendoGrid( claimGridOpt ).cKendoGrid();
    	claimGrid.bind("dataBound", function() {
            $('#totalCnt').text(claimGridDs._total);
        });

    	// 수정 버튼 클릭 event
        $('#claimGrid').on("click", 'button[kind=policyclaimBosUpdate]', function(e) {
            e.preventDefault();
            let dataItem = claimGrid.dataItem($(e.currentTarget).closest('tr'));
            claimBosEventUtil.gridRowClick(dataItem);
        });

        // 삭제 버튼 클릭 event
        $('#claimGrid').on("click", 'button[kind=policyclaimBosDelete]', function(e) {
        	e.preventDefault();
        	let dataItem = claimGrid.dataItem($(e.currentTarget).closest('tr'));

        	fnKendoMessage({
				type : "confirm",
				message : "선택한 항목을 삭제하시겠습니까?",
				ok : function() {
					claimBosEventUtil.delPolicyClaimBos(dataItem);
				}, cancel : function() {
					return;
				}
			});

        });
    };
    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {

    	searchLClaimCtgryDropDownList = fnKendoDropDownList({
 			id         : 'searchLClaimCtgryId',
 			url        : "/admin/policy/claim/searchPsClaimCtgryList",
 			params 	   : {"categoryCode" : "10"},
 			blank      : '전체',
 			valueField : "psClaimCtgryId",
 			textField  : "claimName"
 		});

    	searchMClaimCtgryDropDownList = fnKendoDropDownList({
  			id         : 'searchMClaimCtgryId',
  			url        : "/admin/policy/claim/searchPsClaimCtgryList",
  			params	   : {"categoryCode" : "20"},
  			blank      : '전체',
  			valueField : "psClaimCtgryId",
  			textField  : "claimName"
  		});

    	searchSClaimCtgryDropDownList = fnKendoDropDownList({
  			id         : 'searchSClaimCtgryId',
  			url        : "/admin/policy/claim/searchPsClaimCtgryList",
  			params	   : {"categoryCode" : "30"},
  			blank      : '전체',
  			valueField : "psClaimCtgryId",
  			textField  : "claimName"
  		});

    	fnTagMkRadio({
			id    : 'searchTargetType',
			tagId : 'searchTargetType',
			data : [
				{ "CODE" : ""	, "NAME":'전체' },
	            { "CODE" : "B"	, "NAME":'구매자 귀책' },
	            { "CODE" : "S"	, "NAME":'판매자 귀책' }
            ],
			chkVal: ''
		});

		fnKendoDropDownList({
			id    : 'searchCondition',
			data  : [{ "CODE" : "CLAIM_CODE"		, "NAME":"공급업체 사유"},
				     { "CODE" : "SUPPLIER_CODE" 	, "NAME":"공급업체 Code"}
					]
		});

    	LClaimCtgryDropDownList  =  fnKendoDropDownList({
 			id         : 'lclaimCtgryId',
 			tagId	   : 'lclaimCtgryId',
 			url        : "/admin/policy/claim/searchPsClaimCtgryList",
 			params 	   : {"categoryCode" : "10"},
 			blank      : '선택해주세요.',
 			valueField : "psClaimCtgryId",
 			textField  : "claimName"
 		});

    	MClaimCtgryDropDownList  = fnKendoDropDownList({
			id         : 'mclaimCtgryId',
			tagId      : 'mclaimCtgryId',
			url        : "/admin/policy/claim/searchPsClaimCtgryList",
			params	   : {"categoryCode" : "20"},
			blank      : '선택해주세요.',
			valueField : "psClaimCtgryId",
			textField  : "claimName"
		});

    	SClaimCtgryDropDownList = fnKendoDropDownList({
  			id         : 'sclaimCtgryId',
  			tagId      : 'sclaimCtgryId',
  			url        : "/admin/policy/claim/searchPsClaimCtgryList",
  			params	   : {"categoryCode" : "30"},
  			blank      : '선택해주세요.',
  			valueField : "psClaimCtgryId",
  			textField  : "claimName",
			dataBound : function(e) {
				const _ds = this.dataSource;
			    const _data = _ds.data();
			    if (_data != undefined && _data != null && _data.length > 0) {
			    	sClaimCtgryDropDownListData = _data.toJSON().slice();
			    }
			}
  		});

    	MClaimCtgryDropDownList.enable(false);
    	SClaimCtgryDropDownList.enable(false);

    	LClaimCtgryDropDownList.bind('change', function(e){
    		MClaimCtgryDropDownList.enable(true);
    	});

    	MClaimCtgryDropDownList.bind('change', function(e){
    		SClaimCtgryDropDownList.enable(true);
    	});

    	SClaimCtgryDropDownList.bind('change', function(e){
    		var psClaimCtgryId = this.value();
    		var claimName = this.text();

    		for(var i = 0; i < sClaimCtgryDropDownListData.length; i++){
    			if(sClaimCtgryDropDownListData[i].psClaimCtgryId == psClaimCtgryId){
    				$("#targetType").val('귀책유형 : '+sClaimCtgryDropDownListData[i].targetTypeName);
    			}
    		}

    	});

    	fnTagMkRadio({
			id    : 'useYn',
			tagId : 'useYn',
			data : [
	            { "CODE" : "Y"	, "NAME":'사용' },
	            { "CODE" : "N"	, "NAME":'미사용' }
            ],
			chkVal: 'Y'
		});

		// 팝업 초기화
    	claimBosPopupUtil.popupInit();
    };
    //---------------Initialize Option Box End ------------------------------------------------
    //-------------------------------  Common Function start -------------------------------
    function bindEvents(){

    	// 반품회수사유 동일 체크박스
    	$('#ng-view').on("click","input[name=sameBosClaimSupplier]",function(){

    		var $sameCheckBox = $(this);

    		if($sameCheckBox.is(":checked")){
    			// 반품회수 체크박스 확인
    			$.each(claimBosEventUtil.supplierObj.find("li"), function (i, item) {
    			    var that = $(this);
    			    var claimCode = $.trim(that.find("input[name='bosClaimSupplys']").val());
    			    var supplierCode = $.trim(that.find("input[name='supplierCodes']").val());
		            if (claimCode == "") {
		                fnKendoMessage({message: "공급업체 사유(반품회수)를 선택해주세요."});
		                $sameCheckBox.attr("checked",false);
		                return false;
		            }
		    		var nonClaimDropDown = $("#bosNonClaimSupply_"+supplierCode).data("kendoDropDownList");
		    		nonClaimDropDown.value(claimCode);
		    		nonClaimDropDown.trigger("change");
    			});
    		}
		});

    }
    function fnBosClaimCtgry(){
		fnKendoPopup({
			id     : 'policyClaimCtgryPopup',
			title  : 'BOS 사유 관리',
			src    : '#/policyClaimCtgryPopup',
			width  : '680px',
			height : '470px',
			success: function( id, data ){
				fnDropDownListRefresh();
				fnSearch();

			}
		});
    }

    function fnDropDownListRefresh(){
    	searchLClaimCtgryDropDownList.dataSource.read();
    	searchMClaimCtgryDropDownList.dataSource.read();
    	searchSClaimCtgryDropDownList.dataSource.read();
    	LClaimCtgryDropDownList.dataSource.read();
		MClaimCtgryDropDownList.dataSource.read();
		SClaimCtgryDropDownList.dataSource.read();
    }
    //-------------------------------  Common Function end -------------------------------
    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSearch = function() { fnSearch(); }; // 조회
    $scope.fnClear = function() { fnClear(); }; // 초기화
	$scope.fnSave = function(){  claimBosSubmitUtil.save();}; // 저장
	$scope.fnBosClaimCtgry = function(){  fnBosClaimCtgry();}; // BOS사유 관리 팝업

    //------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END

