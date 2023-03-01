/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 클레임 관리 > 미출 주문상세리스트 > 일괄 취소완료 팝업
 * **/
"use strict";

var PAGE_SIZE = 20;
var paramData;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

console.log("paramData ::" ,paramData);


$(document).ready(function() {

	importScript("/js/service/od/order/orderCommSearch.js", function (){
		fnInitialize();
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({ PG_ID  : "changeClaimStatusCCPopup", callback : fnUI });
	};

	// 화면 UI 초기화
	function fnUI(){

		fnInitGrid();
		fnInitOptionBox();

	};

	//--------------------------------- Button Start---------------------------------
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){

		var listLength = paramData.dataItemList.length;
		$('#totalCnt').text(listLength);

		var claimTbody = $("#claimTbody");

		for(var i = 0; i < listLength ; i++){
			var refundPrice = fnNumberWithCommas(paramData.dataItemList[i].salePrice*paramData.dataItemList[i].orderCnt);
			var h = "";
			h += '<tr>';
			h += '<td style="text-align: center;"><input type="checkbox" id="checkbox_'+paramData.dataItemList[i].ifUnreleasedInfoId+'" data-ifUnreleasedInfoId="'+paramData.dataItemList[i].ifUnreleasedInfoId+'" name="rowCheckbox" class="k-checkbox" /></td>';
			h += '<td style="text-align: center;">'+ paramData.dataItemList[i].odid +'</td>';
			h += '<td style="text-align: center;">'+ paramData.dataItemList[i].odOrderDetlId +'</td>';
			h += '<td>'+ paramData.dataItemList[i].goodsNm + ' / 클레임 수량 : ' + paramData.dataItemList[i].orderCnt + '개</br>'
				+'<select id="searchLClaimCtgryId_'+paramData.dataItemList[i].ifUnreleasedInfoId+'" name="searchLClaimCtgryId" class="searchLClaimCtgryId searchLClaimCtgryItem fb__custom__select left-input marginR5"  style="width:150px;" data-ifUnreleasedInfoId="'+paramData.dataItemList[i].ifUnreleasedInfoId+'"><option value="">클레임사유 (대)</option></select>'
				+'<select id="searchMClaimCtgryId_'+paramData.dataItemList[i].ifUnreleasedInfoId+'" name="searchMClaimCtgryId" class="searchMClaimCtgryId searchMClaimCtgryItem fb__custom__select left-input marginR5"  style="width:150px;" data-ifUnreleasedInfoId="'+paramData.dataItemList[i].ifUnreleasedInfoId+'"><option value="">클레임사유 (중)</option></select>'
				+'<select id="searchSClaimCtgryId_'+paramData.dataItemList[i].ifUnreleasedInfoId+'" name="searchSClaimCtgryId" class="searchSClaimCtgryId searchSClaimCtgryItem fb__custom__select left-input marginR5"  style="width:150px;" data-ifUnreleasedInfoId="'+paramData.dataItemList[i].ifUnreleasedInfoId+'"><option value="">귀책처</option></select>'
				+'<input type="text" id="claimPriceInput_'+paramData.dataItemList[i].ifUnreleasedInfoId+'" name="claimPriceInput" value="배송비 - 0원 / 환불 금액 :'+refundPrice+'원" style="display:none; width:100%; border:none; background-color:#FFFFFF" >';
				+'</td>';
			h += '</tr>';
			claimTbody.append(h);

			$("#searchLClaimCtgryId_"+paramData.dataItemList[i].ifUnreleasedInfoId+", #searchMClaimCtgryId_"+paramData.dataItemList[i].ifUnreleasedInfoId+"").on("change", function(){
				var that = $(this);
				var obj = $("#divSearchLClaimCtgryIdArea input[value='"+ that.val() +"']");
				var ifUnreleasedInfoId = that.data('ifunreleasedinfoid');
				var depthData = "";
				if (obj.data("categorycode")  == "10"){ // BSO 클레임 사유 1 Depth Code
					depthData	= obj.data("depth2");
				} else if (obj.data("categorycode")  == "20"){ // BSO 클레임 사유 2 Depth Code
					depthData	= obj.data("depth3");
				}
				setSelectBox(obj.data("categorycode"), ifUnreleasedInfoId, depthData);

				// 체크박스 체크되어있을 시 해제
				if($('#checkbox_'+ifUnreleasedInfoId).is(":checked")){
					$("#checkBoxAll").prop("checked", false);
					$('#checkbox_'+ifUnreleasedInfoId).prop("checked", false);
				}
			});
		}

	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		fnAjax({
            url     : "/admin/policy/claim/getPolicyClaimCtgryListForClaimPopup",
            isAction : 'select',
            success :
                function( data ){
            		var html = "";
            		for(var i = 0; i < data.length ; i++){
            			html += "<input type='hidden1' name='psClaimCtgryId' value='"+ data[i].psClaimCtgryId +
            					"'data-categoryCode='"+ data[i].categoryCode +"' " +
            					"data-targetType='"	+ data[i].targetType +"' " +
            					"data-claimCode='"+ data[i].claimCode +"' " +
            					"data-claimName='"+ data[i].claimName +"' " +
            					"data-depth2='"+ data[i].depth2 +"' " +
            					"data-depth3='"+ data[i].depth3 +"'" + "/> ";
            		}
            		$("#divSearchLClaimCtgryIdArea").html(html);
            		setSelectBox(0, "", "");
                }
        });

	};

	function setSelectBox(depthCd, rowId, depthData){
		var searchLClaimCtgryId = 10;
		var searchMClaimCtgryId = 20;
		var searchSClaimCtgryId = 30;

		var searchLClaimCtgryNm = ".searchLClaimCtgryId";
		var searchMClaimCtgryNm = ".searchMClaimCtgryId";
		var searchSClaimCtgryNm = ".searchSClaimCtgryId";

		if(rowId != ""){
			searchLClaimCtgryNm = "#searchLClaimCtgryId_" + rowId;
			searchMClaimCtgryNm = "#searchMClaimCtgryId_" + rowId;
			searchSClaimCtgryNm = "#searchSClaimCtgryId_" + rowId;
		}

		var nextDepth = 0;
		var nextClaimCtgry = "";
		var html = ""
		if (depthCd == 0){
			nextDepth		= searchLClaimCtgryId;
			nextClaimCtgry	= searchLClaimCtgryNm;
			html += "<option value=''>클레임사유 (대)</option>";

		} else if (depthCd == searchLClaimCtgryId){
			nextDepth		= searchMClaimCtgryId;
			nextClaimCtgry	= searchMClaimCtgryNm;
			html += "<option value=''>클레임사유 (중)</option>";
			$(searchSClaimCtgryNm).html("<option value=''>귀책처</option>");

		} else if (depthCd == searchMClaimCtgryId){
			nextDepth		= searchSClaimCtgryId;
			nextClaimCtgry	= searchSClaimCtgryNm;
			html += "<option value=''>귀책처</option>";
		}

		$("#divSearchLClaimCtgryIdArea input[data-categorycode='"+ nextDepth +"']").each(function(){
		    var that = $(this);
		    if (depthCd == 0){
		    	html += "<option value='"+ that.val()+"'>"+ that.data("claimname") +"</option>";
		    } else {

		    	if ((depthData+"").indexOf(",") >= 0){
		    		var depthArr = depthData;
		    		depthArr = depthArr.split(",");

		    		for(var i = 0; i<depthArr.length ; i++){
		    			if(that.val() == depthArr[i]){
		    				html += "<option value='"+ depthArr[i]+"'>"+ that.data("claimname") +"</option>";
		    			}
		    		}
		    	}else{
		    		if(that.val() == depthData){
		    			html += "<option value='"+ depthData+"'>"+ that.data("claimname") +"</option>";
	    			}
		    	}
		    }
		});
		$(nextClaimCtgry).html(html);
	}

	$("#searchLClaimCtgryId_form, #searchMClaimCtgryId_form").on("change", function(){
		var that = $(this);
		var obj = $("#divSearchLClaimCtgryIdArea input[value='"+ that.val() +"']");
		var depthData = "";
		if (obj.data("categorycode")  == "10"){
			depthData	= obj.data("depth2");
		} else if (obj.data("categorycode")  == "20"){
			depthData	= obj.data("depth3");
		}
		setSelectBox(obj.data("categorycode"), "form", depthData);
	});

    // 그리드 전체선택 클릭
    $("#checkBoxAll").on("click", function(index){
        if( $("#checkBoxAll").prop("checked") ){
            $("input[name=rowCheckbox]").prop("checked", true);
        }else{
            $("input[name=rowCheckbox]").prop("checked", false);
        }
    });

    // 그리드 체크박스 클릭
    $("#claimGrid").on("click", "[name=rowCheckbox]" , function(e){
        if( e.target.checked ){
            if( $("[name=rowCheckbox]").length == $("[name=rowCheckbox]:checked").length ){
                $("#checkBoxAll").prop("checked", true);
            }
        }else{
            $("#checkBoxAll").prop("checked", false);
        }
    });

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------

	$scope.fnChange =function(){ orderSubmitUtil.changeClaimStatusCCChange(); };

	$scope.fnNext =function(){	 orderSubmitUtil.changeClaimStatusCCNext();	};

	$scope.fnSave =function(){	 orderSubmitUtil.changeClaimStatusCCSave();	};


	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END



