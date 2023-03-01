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
var LClaimCtgryDropDownList, MClaimCtgryDropDownList, SClaimCtgryDropDownList; //BOS클레임사유 드롭다운리스트
var sClaimCtgryDropDownListData = new Object();

$(document).ready(function() {
	importScript("/js/controllers/admin/ps/claim/searchCtgryComm.js", function (){
		fnInitialize();
	});

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });
        fnPageInfo({ PG_ID  : "policyClaimMall", callback : fnUI});
    };

    // 화면 UI 초기화
    function fnUI(){
        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitOptionBox();//Initialize Option Box ------------------------------------
        fnDropDownListInit();
        fnInitGrid();   //Initialize Grid ------------------------------------
        fnSearch();
        fnBindEvent();

    };

	//--------------------------------- Button Start---------------------------------
	function fnInitButton(){
		$("#fnSearch, #fnClear, #fnSave").kendoButton();
		$('#kendoPopup').kendoWindow({ visible: false, modal: true });
	};

    function fnClear() {
        $("#searchForm").formClear(true);
    };

    function fnSearch(){
        let data = $("#searchForm").formSerialize(true);
        let query = { page : 1,
                      pageSize : PAGE_SIZE,
                      filterLength :  fnSearchData(data).length,
                      filter : { filters :  fnSearchData(data) }
        };
        claimGridDs.query(query);
    };
	function fnSave(){

		var url = '/admin/policy/claim/addPsClaimMall';
	    var cbId = 'insert';

        if (OPER_TP_CODE == 'U') {
            url = '/admin/policy/claim/putPsClaimMall';
            cbId = 'update';
        }

        var data = $('#claimForm').formSerialize(true);

        if (data.rtnValid) {
            fnAjax({
                url: url,
                params: data,
                success:
                    function (data) {
                        fnBizCallback(cbId, data);
                    },
                isAction: 'insert'
            });
        }
	}

    function fnNew() {
    	$('#claimForm').formClear(true);
		fnKendoInputPoup({id:"kendoPopup",height:"400px" ,width:"800px",title:{nullMsg :'쇼핑몰 클레임 사유 등록/수정'} });
	};

    function fnClose(){
    	var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
    }
    function fnPolicyClaimMallPopUp(dataItem){

        fnAjax({
			url     : '/admin/policy/claim/getPolicyClaimMall',
			params  : {psClaimMallId : dataItem.psClaimMallId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
    }

    function fnBindEvent(){

		// 사유 구분 '취소'인 경우 -> 첨부파일 필수여부 '선택'만 가능
		$('#ng-view').on("click", "#reasonType", function(e) {
			if($('input[name="reasonType"]:checked').val() == 'C'){
				$('input[name="attcRequiredYn"][value="N"]').prop('checked',true);
				$('input[name="attcRequiredYn"][value="Y"]').prop('disabled','disabled');
			}else{
				$('input[name="attcRequiredYn"][value="Y"]').prop('disabled','');
			}
		});

		// 첨부파일 필수여부 비활성화일때 클릭할 경우 alert
		$('#ng-view').on("click", "#attcRequiredYn", function(e) {
			if($('input[name="attcRequiredYn"][value="Y"]').prop('disabled')){
				fnKendoMessage({message : '취소 사유는 첨부파일 필수로 변경 할 수 없습니다.'});
			}
		});
    }

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    	claimGridDs = fnGetPagingDataSource({
            url      : "/admin/policy/claim/getPolicyClaimMallList",
            pageSize : PAGE_SIZE
        });

    	claimGridOpt = {
            dataSource : claimGridDs,
            pageable : { pageSizes: [20, 30, 50], buttonCount : 10 },
            navigatable: true,
            columns   : [{ field: 'rowNumber'			, title: 'No'				, width: '30px'		, attributes : { style: 'text-align:center' }, template: function (dataItem){
			            		return fnKendoGridPagenation(claimGrid.dataSource,dataItem); }}
			            , { field : "reasonMessage"		, title : "쇼핑몰 클레임 사유"	, width : "100px"	, attributes : {style : "text-align:left"} }
			            , { field : "bosClaimCtgry"		, title : "BOS 클레임사유"		, width : "100px"	, attributes : {style : "text-align:left"} }
			            , { field : "reasonType"		, title : "사유 구분"			, width : "30px"	, attributes : {style : "text-align:center"}
				            , template : function(dataItem) {
				            	var reasonType = "";
				            	if(dataItem.reasonType == 'A'){  	  reasonType = "취소/반품";
				            	}else if(dataItem.reasonType == 'C'){ reasonType = "취소";
				            	}else{ 								  reasonType = "반품"; }
								return reasonType;
		                    }}
			            , { field : "attcRequiredYn"	, title : "첨부파일"			, width : "30px"	, attributes : {style : "text-align:center"}
		            		, template : function(dataItem){ return dataItem.attcRequiredYn == 'Y' ? '필수' : '선택'; }}
			            , { field : "useYn"				, title : "사용여부"			, width : "30px"	, attributes : {style : "text-align:center"}
			            	, template : function(dataItem){ return dataItem.useYn == 'Y' ? '사용' : '미사용'; }}
			            , { field:'management'      	, title: '관리'         		, width:"50px" , attributes: {style:'text-align:center;'}
							     , template: function(dataItem) {
							    	 return '<button type="button" class="f-grid-setting btn-white btn-s" kind="policyClaimMallPopUp">수정 </button>';
							     }}
			            , { field : "psClaimMallId"		, hidden: true}]
        };

    	claimGrid = $("#claimGrid").initializeKendoGrid( claimGridOpt ).cKendoGrid();
    	claimGrid.bind("dataBound", function() {
            $('#totalCnt').text(claimGridDs._total);
        });

    	// 수정 버튼 클릭 event
        $('#claimGrid').on("click", 'button[kind=policyClaimMallPopUp]', function(e) {
            e.preventDefault();
            let dataItem = claimGrid.dataItem($(e.currentTarget).closest('tr'));
            fnPolicyClaimMallPopUp(dataItem);
        });

    };

    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {

    	fnTagMkRadio({
			id    : 'searchResaonType',
			tagId : 'searchResaonType',
			data : [
                { "CODE" : ""	, "NAME" : "전체" },
                { "CODE" : "A"	, "NAME" : "취소/반품" },
                { "CODE" : "C"	, "NAME" : "취소" },
                { "CODE" : "R"	, "NAME" : "반품" }
            ],
			chkVal: ''
		});

    	fnTagMkRadio({
			id    : 'searchUseYn',
			tagId : 'searchUseYn',
			data : [
				{ "CODE" : ""	, "NAME":'전체' },
	            { "CODE" : "Y"	, "NAME":'예' },
	            { "CODE" : "N"	, "NAME":'아니오' }
            ],
			chkVal: ''
		});

    	fnTagMkRadio({
			id    : 'searchAttcRequiredYn',
			tagId : 'searchAttcRequiredYn',
			data : [
				{ "CODE" : ""	, "NAME":'전체' },
	            { "CODE" : "Y"	, "NAME":'필수' },
	            { "CODE" : "N"	, "NAME":'선택' }
            ],
			chkVal: ''
		});

    	fnTagMkRadio({
			id    : 'reasonType',
			tagId : 'reasonType',
			data : [
                { "CODE" : "A"	, "NAME" : "취소/반품" },
                { "CODE" : "C"	, "NAME" : "취소" },
                { "CODE" : "R"	, "NAME" : "반품" }
            ],
			chkVal: 'A'
		});

    	fnTagMkRadio({
			id    : 'attcRequiredYn',
			tagId : 'attcRequiredYn',
			data : [
	            { "CODE" : "Y"	, "NAME":'필수' },
	            { "CODE" : "N"	, "NAME":'선택' }
            ],
			chkVal: 'Y'
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

    };

    function fnDropDownListInit(){

    	LClaimCtgryDropDownList = searchCtgryCommonUtil.LClaimCtgryDropDownList();
    	MClaimCtgryDropDownList = searchCtgryCommonUtil.MClaimCtgryDropDownList();
		SClaimCtgryDropDownList = searchCtgryCommonUtil.SClaimCtgryDropDownList();

    	MClaimCtgryDropDownList.enable(false);
    	SClaimCtgryDropDownList.enable(false);

    	LClaimCtgryDropDownList.bind('change', function(e) {
			if(0 === this.selectedIndex) {
				MClaimCtgryDropDownList.enable(false);
				SClaimCtgryDropDownList.enable(false);
			}
			else {
				MClaimCtgryDropDownList.enable(true);
			}

    		$("#targetType").val("");
    	});

    	MClaimCtgryDropDownList.bind('change', function(e) {
			if(0 === this.selectedIndex) SClaimCtgryDropDownList.enable(false);
			else SClaimCtgryDropDownList.enable(true);

    		$("#targetType").val("");
    	});

    	SClaimCtgryDropDownList.bind('change', function(e){
    	    var psClaimCtgryId = this.value();
    	    var claimName = this.text();

    	    for(var i = 0; i < sClaimCtgryDropDownListData.length; i++){
    	        if(sClaimCtgryDropDownListData[i].psClaimCtgryId == psClaimCtgryId){
    	            $("#targetType").val(sClaimCtgryDropDownListData[i].targetTypeName);
    	            $("#psClaimBosId").val(sClaimCtgryDropDownListData[i].psClaimBosId);
    	        }
    	    }
    	});

    }

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------
    function fnBizCallback( id, data ){
    	switch(id){
    		case 'select':

    			fnKendoInputPoup({id:"kendoPopup",height:"400px" ,width:"800px",title:{nullMsg :'쇼핑몰 클레임 사유 등록/수정'} });
    			$('#claimForm').formClear(true);

        		// 등록정보 데이터 세팅
        		data.rows.createInfo = "등록일		: " + data.rows.createDate + " " + data.rows.createUserName + " ("+ data.rows.createId +")";
        		if(data.rows.modifyDate != null && data.rows.modifyId != null){
        			data.rows.modifyInfo = "최근수정일	: " + data.rows.modifyDate + " " + data.rows.modifyUserName + " ("+ data.rows.modifyId +")";
        		}

        		data.rows.targetType = data.rows.targetType == 'B'? '구매자 귀책' : '판매자 귀책';

        		$('#claimForm').bindingForm(data, 'rows', true);
    			break;
    		case 'insert':
				fnKendoMessage({message : fnGetLangData({nullMsg :'등록되었습니다.' }) ,ok :function(e){
					fnClose();
					fnSearch();
				}});;
    			break;
    		case 'update':
    			fnKendoMessage({message : fnGetLangData({nullMsg :'수정되었습니다.' }) ,ok :function(e){
					fnClose();
					fnSearch();
				}});;
    			break;
    	}
    }

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSearch = function() { fnSearch(); }; // 조회
    $scope.fnClear  = function() { fnClear(); }; // 초기화
    $scope.fnNew 	= function() { fnNew(); }; // 신규
	$scope.fnClose	= function() { fnClose();}; // 닫기
	$scope.fnSave 	= function() { fnSave();}; // 저장

    //------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END
