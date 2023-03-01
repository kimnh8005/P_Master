/**-----------------------------------------------------------------------------
 * description 		 : 판매처관련 함수
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.16		이명수   최초생성
 * @
 * **/

var sellersPopupUtil = {
    popupInit: function(){
        $('#kendoPopup').kendoWindow({
            visible: false,
            modal: true
        });

        $('#sellerSupplierPopup').kendoWindow({
            visible: false,
            modal: true
        });
    }
}

var sellersEventUtil = {
    supplierObj: $("#ulSupplierList"),
    close: function(){
        var kendoWindow =$('#kendoPopup').data('kendoWindow');
        kendoWindow.close();
    },
    gridRowClick: function(){
        var aMap = sellersGrid.dataItem(sellersGrid.select());
        fnAjax({
            url     : '/admin/outmall/sellers/getSellers',
            params  : {omSellersId : aMap.omSellersId},
            success :
                function( data ){

                    $("#sellersCodeTr").css("display","");

                    // 수집몰코드 데이터 세팅
                    if(data.rows.interfaceYn == "Y"){
                        data.rows.outmallInfoView = data.rows.outmallName + " (" + data.rows.outmallCode + ")";
                    }

                    // 등록정보 데이터 세팅
                    data.rows.createInfo = "등록일		: " + data.rows.createDate + " " + data.rows.createUserName + " ("+ data.rows.createId +")";
                    data.rows.modifyInfo = "최근수정일	: " + data.rows.modifyDate + " " + data.rows.modifyUserName + " ("+ data.rows.modifyId +")";

                    sellersSubmitUtil.fnBizCallback("select", data);
                },
            isAction : 'select'
        });
    },
    addSupplier: function(){
        var addUrSupplierId = $.trim($("#supplierId").val());
        var addSupplierName = $.trim($("#addSupplierName").val());
        var supplierValidateYn = $.trim($("#supplierValidateYn").val());
        if (addUrSupplierId == "" || addUrSupplierId == "0") {
            fnKendoMessage({message: '공급업체를 선택 후 추가해주세요.'});
            $("#supplierId").focus();
            return;
        }

        if(sellersEventUtil.checkSupplierExist(addUrSupplierId) == false){
            sellersEventUtil.addSupplierItem(addUrSupplierId, addSupplierName, supplierValidateYn);
        }
    },
    resetSupplierItem: function(){
        this.supplierObj.empty();
    },
    addSupplierItem: function(addUrSupplierId, addSupplierName, supplierValidateYn){

        var h = "";
        var disabledText = "";


            if (supplierValidateYn == "N"){
                disabledText = "disabled='true'";
            }
            h += "<li class='marginT5'>";
            h += "<input type='text' name='supplierNms' class='comm-input marginR5' value='" + addSupplierName + "' style='width: 145px;' disabled='true'>";
            h += "<input type='hidden' id='erpSendYn_"+addUrSupplierId+"' class='comm-input marginR5' value='" + addUrSupplierId + "'>";
            h += "<input type='hidden' name='urSupplierIds' class='comm-input marginR5' value='" + addUrSupplierId + "'>";
            h += "<input type='hidden' name='supplierValidateYns' class='comm-input marginR5' value='" + supplierValidateYn + "'>";

            h += "<input type='text' id='supplierCd_"+addUrSupplierId+"' name='supplierCds' class='comm-input marginR5' style='width: 145px;' "+ disabledText +">";
            h += "<button type='button' id='supplierCodeBtn_"+addUrSupplierId+"' name='supplierCodeBtns' class='k-button k-button-icontext btn-point btn-s k-grid-remove marginR5' onclick='javascript:sellersEventUtil.getSupplierCode("+addUrSupplierId+");'  "+ disabledText +">확인</button>";
            h += "<input type='text' id='calcTypes_"+addUrSupplierId+"' name='calcTypes' class='marginR5' style='width: 100px;' >";
			h += "<span><input type='text' id='fees' name='fees' class='comm-input marginR5' style='width: 105px;' maxlength='3'></span>% ";
			if(fnIsProgramAuth("DELETE")){
				h += "<button type='button' name='delSupplierBtns' class='k-button k-button-icontext btn-red btn-s k-grid-remove' onclick='javascript:sellersEventUtil.delSupplier(this);'>삭제</button>";
			}
            h += "</li>";
            this.supplierObj.append(h);

            fnKendoDropDownList({ id : 'calcTypes_'+addUrSupplierId , data : sellersSearchUtil.searchCalcTypeData(), textField :"NAME", valueField : "CODE", blank : "정산방식" });

            $("#addSupplierCode").val("");
            $("#addFee").val("");

            changeSellersSupplierUtil.reset();
    },
    putSupplierItem: function(addUrSupplierId, addSupplierName, urSupplierCode, calcType, fee){

    	var putCalcType = calcType == 'S' ? '판매가 정산' : '공급가 정산';

        if (supplierValidateYn == "N"){
            disabledText = "disabled='true'";
        }
        h += "<li class='marginT5'>";
        h += "<input type='text' name='supplierNms' class='comm-input marginR5' value='" + addSupplierName + "' style='width: 145px;' disabled='true'>";
        h += "<input type='hidden' id='erpSendYn_"+addUrSupplierId+"' class='comm-input marginR5' value='" + addUrSupplierId + "'>";
        h += "<input type='hidden' name='urSupplierIds' class='comm-input marginR5' value='" + addUrSupplierId + "'>";
        h += "<input type='hidden' name='supplierValidateYns' class='comm-input marginR5' value='" + supplierValidateYn + "'>";

        h += "<input type='text' id='supplierCd_"+addUrSupplierId+"' name='supplierCds' class='comm-input marginR5' style='width: 145px;' "+ disabledText +">";
        h += "<button type='button' id='supplierCodeBtn_"+addUrSupplierId+"' name='supplierCodeBtns' class='k-button k-button-icontext btn-point btn-s k-grid-remove marginR5' onclick='javascript:sellersEventUtil.getSupplierCode("+addUrSupplierId+");'  "+ disabledText +">확인</button>";
        h += "<input type='text' id='calcTypes_"+addUrSupplierId+"' name='calcTypes' class='marginR5' style='width: 100px;' >";
        h += "<span><input type='text' id='fees' name='fees' class='comm-input marginR5' style='width: 105px;' maxlength='3'></span>% ";
        if(fnIsProgramAuth("DELETE")){
            h += "<button type='button' name='delSupplierBtns' class='k-button k-button-icontext btn-red btn-s k-grid-remove' onclick='javascript:sellersEventUtil.delSupplier(this);'>삭제</button>";
        }
        h += "</li>";
        this.supplierObj.append(h);

        fnKendoDropDownList({ id : 'calcTypes_'+addUrSupplierId , data : sellersSearchUtil.searchCalcTypeData(), textField :"NAME", valueField : "CODE", blank : "정산방식" });

        $("#addSupplierCode").val("");
        $("#addFee").val("");

        changeSellersSupplierUtil.reset();
    },
    putSupplierItem: function(addUrSupplierId, addSupplierName, urSupplierCode, calcType, fee){

        var putCalcType = calcType == 'S' ? '판매가 정산' : '공급가 정산';

        var supplierValidateYn = $.trim($("#inputSupplierId_"+addUrSupplierId).val());

        var h = "";
        h += "<li class='marginT5'>";
        h += "<input type='text' name='supplierNms' class='comm-input marginR5' value='" + addSupplierName + "' style='width: 145px;' disabled='true'>";
        h += "<input type='hidden' name='urSupplierIds' class='comm-input marginR5' value='" + addUrSupplierId + "'>";
        h += "<input type='text' id='supplierCd_"+addUrSupplierId+"' name='supplierCds' class='comm-input marginR5' value='" + urSupplierCode + "' style='width: 145px;' disabled='true'>";
        h += "<button type='button' name='supplierCodeBtns' id='supplierCodeBtn_"+addUrSupplierId+"' class='k-button k-button-icontext btn-point btn-s k-grid-remove marginR5' onclick='javascript:sellersEventUtil.getSupplierCode(" + addUrSupplierId + ");'>확인</button>";
        h += "<input type='text' id='calcTypes_"+addUrSupplierId+"' name='calcTypes' value='"+putCalcType+"' class='marginR5' style='width: 100px;' disabled='true' >";
        h += "<input type='hidden' name='supplierValidateYns' class='comm-input marginR5' value='" + supplierValidateYn + "'>";

		h += "<span onclick='sellersEventUtil.feeClick();'><input type='text' id='fees' name='fees' class='comm-input marginR5' value='" + fee + "'style='width: 105px;'  disabled></span>% ";
		if(fnIsProgramAuth("DELETE")){
			h += "<button type='button' name='delSupplierBtns' class='k-button k-button-icontext btn-red btn-s k-grid-remove' onclick='javascript:sellersEventUtil.delSupplier(this);'>삭제</button>";
		}

        h += "</li>";
        this.supplierObj.append(h);

        $("#addSupplierCode").val("");
        $("#addFee").val("");

        changeSellersSupplierUtil.reset();
    },
    checkSupplierExist: function(addUrSupplierId){
        var isCheckCompanyExist = false;

        if (this.supplierObj.find("input[name='urSupplierIds'][value='"+addUrSupplierId+"']").length > 0){
            fnKendoMessage({message: "공급처값이 존재합니다."});
            return true;
        }
        return isCheckCompanyExist;
    },
    delSupplier: function(obj){
        if(confirm("삭제하시겠습니까?")){
            obj.closest("li").remove();
        }
    },
    supplierItemCnt: function(){
        return this.supplierObj.find("li").length;
    },
    feeClick: function(){
        fnKendoMessage({message:"수수료는 수수료 관리에서 수정가능합니다. 이동하시겠습니까?", type : "confirm" , ok : function(){
                location.href = "/layout.html#/omBasicFee";
                sellersEventUtil.close();
            }});
    },
    sellerSupplierClick: function(){
        var aMap = sellersGrid.dataItem(sellersGrid.select());
        $('#sellerSupplierPopup').bindingForm( {'rows':aMap},'rows', true);

        sellerSuppliersGridDs.read({'omSellersId' : aMap.omSellersId});
        fnKendoInputPoup({id: "sellerSupplierPopup", height: "auto", width: "400px", title: {nullMsg: '공급업체 상세 정보'}});
    },
    getSupplierCode: function(addUrSupplierId){


        var addSupplierCode = $("#supplierCd_"+addUrSupplierId).val();

        fnAjax({
            url     : '/admin/api/getIfShiptoSrchByErp',
            params  : {splId : addSupplierCode},
            method : 'GET',
            success :
                function( data ){

                    fnKendoMessage({message: "사용가능한 코드 입니다.<br>"+"("+data.splNam+")"});
                    $("#supplierCodeBtn_"+addUrSupplierId).remove();
                    $("#supplierCd_"+addUrSupplierId).prop('disabled',true);
                },
            isAction : 'select'
        });
    },
    selectModification : function(e){ // 선택수정
        // e.preventDefault();

        if (!fnIsProgramAuth("EDIT_SELECTED")) {
            fnKendoMessage({ message : "권한이 없습니다." });
            return;
        }

        let selectRows  = $("#sellersGrid").find("input[name=rowCheckbox]:checked").closest("tr");
        let selectSellerCombo  = $("#gridErpInterfaceStatus").val();
        let confirmMessage = "물류I/F 연동여부가 일괄변경이 됩니다. 진행하시겠습니까?";
        let params = {};
        params.omSellersIdList = [];
        params.erpInterfaceYn = selectSellerCombo;

        if( selectRows.length == 0 ){
            fnKendoMessage({ message : "선택된 외부몰이 없습니다." });
            return;
        }

        if( selectSellerCombo == '' ){
            fnKendoMessage({ message : "선택된 연동여부 상태가 없습니다." });
            return;
        }

        fnKendoMessage({
            type    : "confirm",
            message : confirmMessage,
            ok      : function(){
                for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
                    let dataItem = sellersGrid.dataItem($(selectRows[i]));
                    params.omSellersIdList[i] = dataItem.omSellersId;
                }

                fnAjax({
                    url     : "/admin/outmall/sellers/putErpInterfaceStatusChg",
                    params  : params,
                    contentType : "application/json",
                    success : function( data ){
                        if (data > 0) {
                            var returnMsg = '저장이 완료되었습니다.';
                            fnKendoMessage({
                                message: returnMsg, ok: function () {
                                    changeSellersSupplierUtil.reset();
                                    sellersSubmitUtil.search();
                                }
                            });
                        }
                        else {
                            fnKendoMessage({ message : "저장된 내용이 없습니다." });
                        }
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "update"
                });
            },
            cancel  : function(){

            }
        });
    }
}



var changeSellersSupplierUtil = {
    obj: $("#supplierId_listbox"),
    init: function(){
        this.obj.off("change").unbind("change");
        $("#supplierId_listbox").on("click", function(){

            changeSellersSupplierUtil.changeSellersSupplier();
        });
    },
    reset: function(){
        $("#supplierId-list div.k-list-optionlabel").trigger("click");
        $("#addUrSupplierId").val("0");
        $("#addSupplierName").val("");
        $("#supplierValidateYn").val("");
        $("#addCalcType-list div.k-list-optionlabel").trigger("click");
        $("#gridErpInterfaceStatus").data("kendoDropDownList").select(0);
    },
    changeSellersSupplier: function(){
        var thisValue = $("#supplierId").val();

        if (thisValue != "") {
            var thisText = $("#supplierId_listbox li[aria-selected='true']").html();

            $("#addUrSupplierId").val(thisValue);
            $("#addSupplierName").val(thisText);
            $("#supplierValidateYn").val($("#inputSupplierId_"+thisValue).val());
        }
    }
}


var sellersSubmitUtil = {
    search: function () {
        $('#sellersForm').formClear(false);
        var data;
        data = $('#searchForm').formSerialize(true);

        if(data.findKeyword != ""){
            data.findKeyword = data.findKeyword.replace(/_/gi,'\\_');
        }

        var query = {
            page: 1,
            pageSize: PAGE_SIZE,
            filterLength: fnSearchData(data).length,
            filter: {
                filters: fnSearchData(data)
            }
        };
        sellersGridDs.query(query);
    },
    searchClear: function () {
        $('#searchForm').formClear(true);
    },
    new: function () {
        document.documentElement.scrollTop = 0;
        document.documentElement.scrollTop = 0;
        $('#sellersForm').formClear(true);

        $('.marginT5').remove();
        $("#sellersCodeTr").css("display","none");


        fnKendoInputPoup({height: "auto", width: "900px", title: {nullMsg: '외부몰 등록/수정'}});
    },
    save: function () {
        var url = '/admin/outmall/sellers/addSellers';
        var cbId = 'insert';

        if (OPER_TP_CODE == 'U') {
            url = '/admin/outmall/sellers/putSellers';
            cbId = 'update';
        }

        var isSubmit = true;
        var data = $('#sellersForm').formSerialize(true);
        var cnt = sellersEventUtil.supplierItemCnt();

        if(data.sellersGroupCode == ""){
            fnKendoMessage({message: "판매처그룹은 필수 선택 항목입니다."});
            return;
        }

        if (cnt <= 0) {
            fnKendoMessage({message: "공급업체를 추가해주세요."});
            return;
        }

        if($("input[name='interfaceYn']:checked").val() == 'Y' && $("#outmallInfoView").val() == ''){
            fnKendoMessage({message: "수집몰 코드를 선택해주세요."});
            return;
        }

        var sellersSuppiler = new Object();
        var sellersSupplierList = [];
        var urSupplierId = "";


        var supplierName = "";
        var supplierCode = "";
        var calcType = "";
        var fee = "";
        var supplierCodeBtn = "";
        var supplierValidateYn = "";
        $.each(sellersEventUtil.supplierObj.find("li"), function (i, item) {
            var that = $(this);
            urSupplierId = $.trim(that.find("input[name='urSupplierIds']").val());
            supplierName = $.trim(that.find("input[name='supplierNms']").val());
            supplierCode = $.trim(that.find("input[name='supplierCds']").val());
            supplierValidateYn = $.trim(that.find("input[name='supplierValidateYns']").val());
            calcType = $.trim(that.find("input[name='calcTypes']").val());
            fee = $.trim(that.find("input[name='fees']").val());
            supplierCodeBtns = $(that.find("button[name='supplierCodeBtns']")).length;

            if (supplierName == "") {
                fnKendoMessage({message: "공급처명에 공백이 존재합니다."});
                isSubmit = false;
                return false;
            }

            if (supplierValidateYn == "Y" && supplierCode == "") {
                fnKendoMessage({message: "공급처코드는 필수 입력 항목입니다."});
                isSubmit = false;
                return false;
            }
            if (calcType == "") {
                fnKendoMessage({message: "정산 방식을 선택해주세요."});
                isSubmit = false;
                return false;
            }
            if (fee == "") {
                fnKendoMessage({message: "수수료는 필수 입력 항목입니다."});
                isSubmit = false;
                return false;
            }

            if(supplierValidateYn == "Y" && supplierCodeBtns > 0){
                fnKendoMessage({message: "공급업체 코드를 확인 바랍니다."});
                isSubmit = false;
                return false;
            }

            sellersSuppiler = new Object();
            sellersSuppiler.urSupplierId = urSupplierId;
            sellersSuppiler.supplierNm = supplierName;
            sellersSuppiler.supplierCd = supplierCode;
            sellersSuppiler.calcType = calcType;
            sellersSuppiler.fee = fee;

            sellersSupplierList.push(sellersSuppiler);
        });

        if (isSubmit == true &&  data.rtnValid) {

            data.sellersSupplierList = sellersSupplierList;

            fnAjax({
                url: url,
                params: data,
                contentType: "application/json",
                success:
                    function (data) {
                        sellersSubmitUtil.fnBizCallback(cbId, data);
                    },
                isAction: 'insert'
            });
        }
    },
    fnBizCallback: function (id, data) {

        switch (id) {
            case 'select':
                $('#sellersForm').bindingForm(data, "rows", true);
                sellersEventUtil.resetSupplierItem();
                $.each(data.rows.sellersSupplierList, function(i, item){

                    sellersEventUtil.putSupplierItem(item.urSupplierId, item.urSupplierName, item.urSupplierCode, item.calcType, item.fee);
                });

                $("button[name='supplierCodeBtns']").remove();
                $("button[name='delSupplierBtns']").remove();


                fnKendoInputPoup({height: "auto", width: "900px", title: {nullMsg: '외부몰 등록/수정'}});

                break;
            case 'insert':
            case 'update':
                fnKendoMessage({
                    message: "저장되었습니다.",
                    ok: function () {
                        sellersSubmitUtil.search();
                        sellersEventUtil.close();
                    }
                });
                break;
        }
    }
}

var sellersEZAdminPopupUtil = {
    fnFindOutmallInfo: function (){

        // 수집몰 연동여부가 Y일 경우에만 호출
        if($("input[name='interfaceYn']:checked").val() == 'Y'){
            fnKendoPopup({
                id     : 'ezAdminEtcShopInfo',
                title  : '수집몰 코드 조회',
                src    : '#/ezAdminEtcShopInfo',
                width  : '700px',
                height : '800px',
                //scrollable : "yes",
                success: function( stMenuId, data ){
                    if(data.code != undefined){
                        $('#outmallCode').val(data.code);
                        $('#outmallName').val(data.name);
                        $('#outmallInfoView').val(data.name + " (" + data.code + ")");
                    }
                }
            });
        }
    }
}
