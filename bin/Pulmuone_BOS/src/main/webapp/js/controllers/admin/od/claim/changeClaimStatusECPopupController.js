/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 클레임 관리 > 미출 주문상세리스트 > 일괄 재배송 팝업
 * **/
"use strict";

var PAGE_SIZE = 20;
var paramData;
var searchGoodsItem = new Object();
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

console.log("paramData" , paramData);

$(document).ready(function() {

	importScript("/js/service/od/order/orderCommSearch.js", function (){
		fnInitialize();
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({ PG_ID  : "changeClaimStatusECPopup", callback : fnUI });
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
			h += '<td style="text-align: center;"><input type="checkbox" id="checkbox_'+paramData.dataItemList[i].ifUnreleasedInfoId+'" data-ifUnreleasedInfoId="'+paramData.dataItemList[i].ifUnreleasedInfoId+'" data-claimCnt="'+paramData.dataItemList[i].orderCnt+'" name="rowCheckbox" class="k-checkbox" /></td>';
			h += '<td style="text-align: center;">'+ paramData.dataItemList[i].odid +'</td>';
			h += '<td style="text-align: center;">'+ paramData.dataItemList[i].odOrderDetlId +'</td>';
			h += '<td>'+ paramData.dataItemList[i].goodsNm + ' / 클레임 수량 : ' + paramData.dataItemList[i].orderCnt + '개'
				+'<button id="fnSearchGoodsPopupButton_'+paramData.dataItemList[i].ifUnreleasedInfoId+'" name="gridSearchGoodsPopupButton" class="fb__search-btn marginL10" type="button" onClick="$scope.fnSearchGoodsPopupButton(this)" data-ifUnreleasedInfoId="'+paramData.dataItemList[i].ifUnreleasedInfoId+'" data-claimCnt="'+paramData.dataItemList[i].orderCnt+'"></button><br>'
				+'<input type="text" id="gridGoodsInfo_'+paramData.dataItemList[i].ifUnreleasedInfoId+'" name="gridGoodsInfo" value="" style="display:none; width:50%; border:none; background-color:#FFFFFF" data-ifUnreleasedInfoId="'+paramData.dataItemList[i].ifUnreleasedInfoId+'" data-claimCnt="'+paramData.dataItemList[i].orderCnt+'">'
				+'</td>';
			h += '</tr>';
			claimTbody.append(h);

			$("#searchLClaimCtgryId_"+paramData.dataItemList[i].ifUnreleasedInfoId+", #searchMClaimCtgryId_"+paramData.dataItemList[i].ifUnreleasedInfoId+"").on("change", function(){
				var that = $(this);
				var obj = $("#divSearchLClaimCtgryIdArea input[value='"+ that.val() +"']");
				var ifUnreleasedInfoId = that.data('ifunreleasedinfoid');
				var depthData = "";
				if (obj.data("categorycode")  == "10"){
					depthData	= obj.data("depth2");
				} else if (obj.data("categorycode")  == "20"){
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

    // 상품검색 팝업
    function fnSearchGoodsPopupButton(btnParamData){

    	console.log("btnParamData ::  " , btnParamData);

    	var ifUnreleasedInfoId;
    	if(btnParamData != undefined){
    		ifUnreleasedInfoId = btnParamData.dataset.ifunreleasedinfoid;
    	}

    	var params = {};
		//params.goodsType = "";
		params.goodsType = "GOODS_TYPE.NORMAL,GOODS_TYPE.DISPOSAL";	// 상품유형(복수 검색시 , 로 구분)
		params.saleStatus = 'SALE_STATUS.ON_SALE';                  // 판매상태:판매중
		params.selectType = "single";								// 선택유형:단일선택
		params.selectMultiRowYn = false;                            // 다중선택여부
		params.columnNameHidden = false;
		params.columnAreaShippingDeliveryYnHidden = false;
		params.columnDpBrandNameHidden = false;
		params.columnStardardPriceHidden = false;
		params.columnRecommendedPriceHidden = false;
		params.columnSalePriceHidden = false;
		params.columnSaleStatusCodeNameHidden = false;
		params.columnGoodsDisplyYnHidden = false;

        fnKendoPopup({
            id         : "goodsSearchPopup",
            title      : "상품조회",  // 해당되는 Title 명 작성
            width      : "1300px",
            height     : "800px",
            scrollable : "yes",
            src        : "#/goodsSearchPopup",
            param      : params,
            success    : function( id, data ){

            	// 선택하지 않은 경우
            	if (data == undefined || data == null || data == '' || data.length <= 0) {
            		return false;
                }
            	if (data.length == undefined || data.length == null || data.length == '') {
            		return false;
                }
                if (data.length > 0) {
                	if (data[0].goodsId == undefined || data[0].goodsId == null || data[0].goodsId == '' || data[0].goodsId <= 0) {
                		return false;
                    }
                }

        		searchGoodsItem = data[0];

        		// 상품선택 영역에서 상품 검색했을 경우
        		if(ifUnreleasedInfoId == undefined){
        			$("#searchGoodsInfo").val(data[0].goodsName);
        			$("#searchGoodsInfo").data('goodsId',data[0].goodsId);
        			$("#searchGoodsInfo").css("display","");

        		// 그리드 내에서 상품 검색했을 경우
        		}else{
        			var changeGoodsInfo = " ㄴ " + data[0].goodsName + " / 수량 : " + $("#gridGoodsInfo_"+ifUnreleasedInfoId).data('claimcnt') + "개";
        			$("#gridGoodsInfo_"+ifUnreleasedInfoId).val(changeGoodsInfo);
        			$("#gridGoodsInfo_"+ifUnreleasedInfoId).css("display","");
        			$("#gridGoodsInfo_"+ifUnreleasedInfoId).data('goodsId',data[0].goodsId);

        			// 체크박스 체크되어있을 시 해제
        			if($('#checkbox_'+ifUnreleasedInfoId).is(":checked")){
        				$("#checkBoxAll").prop("checked", false);
        				$('#checkbox_'+ifUnreleasedInfoId).prop("checked", false);
        			}
        		}

            }
        });


    }

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------

	$scope.fnChange =function(){ orderSubmitUtil.changeClaimStatusECChange(); };

	$scope.fnNext =function(){	 orderSubmitUtil.changeClaimStatusECNext();	};

	$scope.fnSave =function(){	 orderSubmitUtil.changeClaimStatusECSave();	};

	$scope.fnSearchGoodsPopupButton =function(btnParamData){ fnSearchGoodsPopupButton(btnParamData);	};


	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END



