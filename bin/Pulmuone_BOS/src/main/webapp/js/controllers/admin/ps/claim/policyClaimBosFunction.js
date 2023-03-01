/**-----------------------------------------------------------------------------
 * description 		 : BOS 클레임 사유 관련 함수
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.23		천혜현   최초생성
 * @
 * **/

var claimBosPopupUtil = {
    popupInit: function(){
        $('#kendoPopup').kendoWindow({
            visible: false,
            modal: true
        });
    }
}

var claimBosEventUtil = {
    supplierObj: $("#bosClaimSupplierList"),
    nonSupplierObj:$("#bosNonClaimSupplierList"),
    mallClaimReasonObj:$("#mallClaimReasonList"),

    close: function(){
        var kendoWindow =$('#kendoPopup').data('kendoWindow');
        kendoWindow.close();
    },

    gridRowClick: function(dataItem){
        fnAjax({
            url     : '/admin/policy/claim/getPolicyClaimBos',
            params  : {psClaimBosId : dataItem.psClaimBosId},
            success :
                function( data ){

            		var resultData = new Object();
            		resultData.rows = data;

            		// 등록정보 데이터 세팅
            		resultData.rows.createInfo = "등록일		: " + resultData.rows.createDate + " " + resultData.rows.createUserName + " ("+ resultData.rows.createId +")";
            		if(resultData.rows.modifyUserName != null ){
            			resultData.rows.modifyInfo = "최근수정일	: " + resultData.rows.modifyDate + " " + resultData.rows.modifyUserName + " ("+ resultData.rows.modifyId +")";
            		}

            		claimBosSubmitUtil.fnBizCallback("select", resultData);
                },
            isAction : 'select'
        });
    },

    delPolicyClaimBos: function(dataItem){
        fnAjax({
            url     : '/admin/policy/claim/delPolicyClaimBos',
            params  : {psClaimBosId : dataItem.psClaimBosId},
            success :
                function( data ){
            		claimBosSubmitUtil.fnBizCallback("delete");
                },
            isAction : 'delete'
        });
    },

    resetSupplierItem: function(){
        this.supplierObj.empty();
        this.nonSupplierObj.empty();
        this.mallClaimReasonObj.empty();
        $("#mallClaimReasonTr").css('display','none');
    	MClaimCtgryDropDownList.enable(false);
    	SClaimCtgryDropDownList.enable(false);

    },

    addSupplier: function(){

    	// 공급업체 리스트 조회
    	$.ajax({
			url     : '/admin/comn/getDropDownSupplierList',
			type : 'GET',
			data  	: { "claimReasonYn" : "Y" },
			contentType : 'application/json',
			async : false
		}).done(function (json) {

			supplierList = json.data.rows;

		}).fail(function (xhr, status, errorThrown) {
			fnKendoMessage({message : fnGetLangData({nullMsg :'시스템에러 발생, 관리자에게 문의하세요.' }) ,ok :function(e){}});;
		})

		var supplierStr = "";
    	var nonSupplierStr= "";
    	for(var i = 0; i < supplierList.length; i++){

			// 공급업체 사유(반품회수)
			supplierStr += '<li class="marginB5">';
			supplierStr += '<span style="display: inline-block;width: 110px;">'+supplierList[i].supplierName+' : </span>';
			supplierStr += '<input type="text"   id="bosClaimSupply_'+supplierList[i].supplierCode+'" name="bosClaimSupplys" class="fb__custom__select left-input" style="width:200px;">';
			supplierStr += '<input type="hidden" name="supplierCodes" value="'+ supplierList[i].supplierCode + '">';
			supplierStr += '</li>';

			// 공급업체 사유(반품미회수)
			nonSupplierStr += '<li class="marginB5">';
			nonSupplierStr += '<span style="display: inline-block;width: 110px;">'+supplierList[i].supplierName+' : </span>';
			nonSupplierStr += '<input type="text"  id="bosNonClaimSupply_'+supplierList[i].supplierCode+'" name="bosNonClaimSupplys" class="fb__custom__select left-input" style="width:200px;">';
			nonSupplierStr += '<input type="hidden" name="supplierCodes" value="'+ supplierList[i].supplierCode + '">';
			nonSupplierStr += '</li>';
    	}

    	this.supplierObj.append(supplierStr);
    	this.nonSupplierObj.append(nonSupplierStr);

    	for(var i = 0; i < supplierList.length; i++){

    		fnKendoDropDownList({
    			id         : 'bosClaimSupply_'+supplierList[i].supplierCode,
    			tagId	   : 'bosClaimSupply_'+supplierList[i].supplierCode,
    			url        : "/admin/policy/claim/getPsClaimSupplyCtgryList",
    			params 	   : {"supplierCode" : supplierList[i].supplierCode},
    			valueField : "claimCode",
    			textField  : "claimName",
    			blank	   : '선택해주세요.',
    			async : false
    		});

    		fnKendoDropDownList({
    			id         : 'bosNonClaimSupply_'+supplierList[i].supplierCode,
    			tagId	   : 'bosNonClaimSupply_'+supplierList[i].supplierCode,
    			url        : "/admin/policy/claim/getPsClaimSupplyCtgryList",
    			params 	   : {"supplierCode" : supplierList[i].supplierCode},
    			valueField : "claimCode",
    			textField  : "claimName",
    			blank	   : '선택해주세요.',
    			async : false
    		});
    	}


    	$('#ng-view').on("change","input[name=bosClaimSupplys]",function(){
    		var supplierCode = $(this).closest('li').find('input[name=supplierCodes]').val();
    		var claimCode = $(this).val();
			if($("input[name=sameBosClaimSupplier]").is(":checked")){
	    		var nonClaimDropDown = $("#bosNonClaimSupply_"+supplierCode).data("kendoDropDownList");
	    		nonClaimDropDown.value(claimCode);
	    		nonClaimDropDown.trigger("change");
			}
		});

    	// 공급업체 사유(반품 회수 안함) 영역 클릭
    	$('#ng-view').on("change","input[name=bosNonClaimSupplys]",function(){

    		var count = 0;
    		var index = 0;
    		$.each(claimBosEventUtil.supplierObj.find("li"), function (i, item) {

    		    var that = $(this);
    		    var claimCode = $.trim(that.find("input[name='bosClaimSupplys']").val());
    		    var supplierCode = $.trim(that.find("input[name='supplierCodes']").val());

    		    $.each(claimBosEventUtil.nonSupplierObj.find("li"), function (i, item) {
    		        var that = $(this);
    		        var nonClaimCode = $.trim(that.find("input[name='bosNonClaimSupplys']").val());
    		        var nonSupplierCode = $.trim(that.find("input[name='supplierCodes']").val());

    		        if(claimCode == nonClaimCode && supplierCode == nonSupplierCode){
    		        	count++;
    		        }
    		    });
    		    index++;
    		});

    		if(index == count){
    			$("input:checkbox[name=sameBosClaimSupplier]").prop('checked',true);
    		}else{
    			$("input:checkbox[name=sameBosClaimSupplier]").prop('checked',false);
    		}
    	});

    }
}

var claimBosSubmitUtil = {
    search: function () {
        $('#claimForm').formClear(false);
        let data = $("#searchForm").formSerialize(true);
        let query = { page : 1,
                      pageSize : PAGE_SIZE,
                      filterLength : fnSearchData(data).length,
                      filter : { filters : fnSearchData(data) }
        };
        claimGridDs.query(query);
    },

    searchClear: function () {
        $('#searchForm').formClear(true);
    },

    new: function () {

    	$('#claimForm').formClear(true);
    	claimBosEventUtil.resetSupplierItem();
    	claimBosEventUtil.addSupplier();
		fnKendoInputPoup({id:"kendoPopup",height:"auto" ,width:"700px",title:{nullMsg :'BOS 클레임 사유 등록/수정'} });
    },

    save: function () {

        var url = '/admin/policy/claim/addPsClaimBos';
        var cbId = 'insert';

        if (OPER_TP_CODE == 'U') {
            url = '/admin/policy/claim/putPsClaimBos';
            cbId = 'update';
        }

        var isSubmit = true;
        var data = $('#claimForm').formSerialize(true);

        var claimSupplierList = [];
        var nonClaimSupplierList = [];

        var claimList = new Object();

        var claimCode = "";
        var supplierCode = "";
        var nonClaimCode = "";
        var nonSupplierCode = "";

        // 공급업체 사유(반품회수)
        $.each(claimBosEventUtil.supplierObj.find("li"), function (i, item) {
        	var that = $(this);
        	claimCode = $.trim(that.find("input[name='bosClaimSupplys']").val());
        	supplierCode = $.trim(that.find("input[name='supplierCodes']").val());

            if (claimCode == "") {
                fnKendoMessage({message: "공급업체 사유(반품회수)를 선택해주세요."});
                isSubmit = false;
                return false;
            }

        	claimList = new Object();
        	claimList.claimCode = claimCode;
        	claimList.supplierCode = supplierCode;

        	claimSupplierList.push(claimList);
        });

        // 공급업체 사유(반품미회수)
        $.each(claimBosEventUtil.nonSupplierObj.find("li"), function (i, item) {
        	var that = $(this);
        	nonClaimCode = $.trim(that.find("input[name='bosNonClaimSupplys']").val());
        	nonSupplierCode = $.trim(that.find("input[name='supplierCodes']").val());

            if (claimCode == "") {
                fnKendoMessage({message: "공급업체 사유(반품 회수 안함)를 선택해주세요."});
                isSubmit = false;
                return false;
            }

        	claimList = new Object();
        	claimList.nonClaimCode = nonClaimCode;
        	claimList.nonSupplierCode = nonSupplierCode;

        	nonClaimSupplierList.push(claimList);
        });

        if (isSubmit == true && data.rtnValid) {

        	data.claimSupplierList = claimSupplierList;
        	data.nonClaimSupplierList = nonClaimSupplierList;

            fnAjax({
                url: url,
                params: data,
                contentType: "application/json",
                success:
                    function (data) {
                        claimBosSubmitUtil.fnBizCallback(cbId, data);
                    },
                isAction: 'insert'
            });
        }
    },

    fnBizCallback: function (id, data) {

        switch (id) {
            case 'select':

            	claimBosEventUtil.resetSupplierItem();
            	claimBosEventUtil.addSupplier();

                $('#claimForm').bindingForm(data, "rows", true);

            	MClaimCtgryDropDownList.enable(true);
            	SClaimCtgryDropDownList.enable(true);

            	//귀책처에 따른 귀책유형
            	for(var i = 0; i < sClaimCtgryDropDownListData.length; i++){
            		if(sClaimCtgryDropDownListData[i].psClaimCtgryId == data.rows.sclaimCtgryId){
            			$("#targetType").val('귀책유형 : '+sClaimCtgryDropDownListData[i].targetTypeName);
            		}
            	}

            	//공급업체 사유(반품회수)
            	if(data.rows.claimSupplierList != null){
            		var claimSupplierList = data.rows.claimSupplierList;
            		for(var i = 0; i < claimSupplierList.length; i++){
            			 let claimDropDown = $("#bosClaimSupply_"+ claimSupplierList[i].supplierCode).data("kendoDropDownList");
            			 if(claimDropDown != undefined){
            				 claimDropDown.value(claimSupplierList[i].claimCode);
            			 }
            		}
            	}

            	//공급업체 사유(반품 회수 안함)
            	if(data.rows.nonClaimSupplierList != null){
            		var nonClaimSupplierList = data.rows.nonClaimSupplierList;
            		for(var i = 0; i < nonClaimSupplierList.length; i++){
            			 let nonClaimDropDown = $("#bosNonClaimSupply_"+ nonClaimSupplierList[i].nonSupplierCode).data("kendoDropDownList");
            			 if(nonClaimDropDown != undefined){
            				 nonClaimDropDown.value(nonClaimSupplierList[i].nonClaimCode);
            			 }
            		}
            	}

            	//공급업체 사유(반품회수)와 공급업체 사유(반품 회수 안함) 동일한 경우
            	if(data.rows.claimSupplierList != null && data.rows.nonClaimSupplierList != null){
            		var count = 0;
            		var index = 0;
            		var claimSupplierList = data.rows.claimSupplierList;
            		for(var i = 0; i < claimSupplierList.length; i++){
            		    if(claimSupplierList[i].claimCode == claimSupplierList[i].nonClaimCode
            		    		&& claimSupplierList[i].supplierCode == claimSupplierList[i].nonSupplierCode){
            		      	count++;
            		    }
            		    index++;
            		}

            		if(index == count){
            			$("input:checkbox[name=sameBosClaimSupplier]").prop('checked',true);
            		}else{
            			$("input:checkbox[name=sameBosClaimSupplier]").prop('checked',false);
            		}
            	}


            	//쇼핑몰 클레임 사유
            	if(data.rows.mallClaimReasonList.length > 0){
            		$("#mallClaimReasonTr").css('display','');
            		var str = "";
            		for(var i = 0 ; i < data.rows.mallClaimReasonList.length ; i++){
            			str += '<li>';
            			str += '<input value="'+data.rows.mallClaimReasonList[i].reasonMessage+'" name="mallClaimReasons" style="border:none; background-color:#FFFFFF; width: 100%;" class="comm-input" type="text" readonly>';
            			str += '</li>';
            		}
            		claimBosEventUtil.mallClaimReasonObj.append(str);
            	}

        		fnKendoInputPoup({id:"kendoPopup",height:"auto" ,width:"700px",title:{nullMsg :'BOS 클레임 사유 등록/수정'} });
                break;
            case 'insert':
            	fnKendoMessage({message : fnGetLangData({nullMsg :'등록되었습니다.' }) ,ok :function(e){
            		claimBosSubmitUtil.search();
            		claimBosEventUtil.close();
				}});;
				break;
            case 'update':
            	fnKendoMessage({message : fnGetLangData({nullMsg :'수정되었습니다.' }) ,ok :function(e){
            		claimBosSubmitUtil.search();
            		claimBosEventUtil.close();
				}});;
                break;
            case 'delete':
            	fnKendoMessage({message : fnGetLangData({nullMsg :'삭제되었습니다.' }) ,ok :function(e){
            		claimBosSubmitUtil.search();
            		claimBosEventUtil.close();
				}});;
                break;
        }
    }
}
