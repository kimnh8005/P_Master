/**-----------------------------------------------------------------------------
 * description 		 : 주문정산 관련 공통 함수
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.05		이명수   최초생성
 * **/

var calPovProcessDisplayUtil = {

	clear : function() {
		this.setButtonStatus(false, false, false, false);
        this.setText('-', '-', '-', '-');
	},
	setStatus : function(data) {
		this.setButtonStatus(
				data.canTemporaryUpload,
				data.canTemporaryInterface,
				data.canFinalUpload,
				data.canFinalInterface);
		this.setText(
				data.temporaryUploadText,
				data.temporaryInterfaceText,
				data.finalUploadText,
				data.finalInterfaceText);
	},

	setButtonStatus : function (btn1, btn2, btn3, btn4) {
        this.setButtonStatusAndAction("btnTemporaryUpload", btn1);
        this.setButtonStatusAndAction("btnTemporaryInterface", btn2);
        this.setButtonStatusAndAction("btnFinalUploadFile", btn3);
        this.setButtonStatusAndAction("btnFinalInterface", btn4);
    },
	setButtonStatusAndAction : function(buttonId, isOn) {
		$("#" + buttonId).prop("disabled", !isOn);
	},
	setText : function (text1, text2, text3, text4){
		$("#tempUploadText").text(text1);
        $("#tempInterfaceText").text(text2);
        $("#finalUploadText").text(text2);
        $("#finalInterfaceText").text(text4);
	}
}

var calPovGridEventUtil = {

    gridInit: function(grilColumns){
        defaultGridOpt = {
        		dataSource: {
            		aggregate: [
            			{ field: "tempMeCost", aggregate: "sum" },
            			{ field: "tempOvCost", aggregate: "sum" },
            			{ field: "tempVdcCost", aggregate: "sum" },
                        { field: "tempMogeCost", aggregate: "sum" },
            			{ field: "finalMeCost", aggregate: "sum" },
            			{ field: "finalOvCost", aggregate: "sum" },
            			{ field: "finalVdcCost", aggregate: "sum" },
                        { field: "finalMogeCost", aggregate: "sum" },
            			{ field: "diffMeCost", aggregate: "sum" },
            			{ field: "diffOvCost", aggregate: "sum" },
            			{ field: "diffVdcCost", aggregate: "sum" },
                        { field: "diffMogeCost", aggregate: "sum" }
            		]
                },
            navigatable : true,
            columns : grilColumns
        };

        defaultGrid = $('#defaultGrid').initializeKendoGrid( defaultGridOpt ).cKendoGrid();
    },
}


var calPovUploadUtil = {
	uploadFileTagIdList : ['temporaryUploadFile', 'finalUploadFile'],
	uploadInit: function(){
        var uploadFileTagIdList = this.uploadFileTagIdList;

        var selectFunction = function(e) {
            if (e.files && e.files[0]) {
                // 엑셀 파일 선택시
                // --------------------------------------------------------------------
                // 확장자 2중 체크 위치
                // --------------------------------------------------------------------
                // var imageExtension = e.files[0]['extension'].toLowerCase();
                // 전역변수에 선언한 허용 확장자와 비교해서 처리
                // itemMgmController.js 의 allowedImageExtensionList 참조

                //  켄도 이미지 업로드 확장자 검사
                if(!calPovUploadUtil.validateExtension(e)) {
                    fnKendoMessage({
                        message : '허용되지 않는 확장자 입니다.',
                        ok : function(e) {}
                    });
                    return;
                }

                gFileTagId = e.sender.element[0].id;
                let reader = new FileReader();


                $("#fileInfoDiv_"+ gFileTagId).text(e.files[0].name);

                reader.onload = function(ele) {
                    var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
                    gFile = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

                };

                reader.readAsDataURL(e.files[0].rawFile);

            } // End of if (e.files && e.files[0])
        } // End of var selectFunction = function(e)

        for (var i = 0; i < uploadFileTagIdList.length; i++) {
            fnKendoUpload({
                id : uploadFileTagIdList[i]
                , select : selectFunction
            });
        } // End of for (var i = 0; i < uploadFileTagIdList.length; i++)
    },

    validateExtension: function(e){

        var allowedExt = '';
        var ext = e.files[0].extension;
        var $el = e.sender.element;

        if( !$el.length ) return;

        if( $el[0].accept && $el[0].accept.length ) {
            // 공백 제거
            allowedExt = $el[0].accept.replace(/\s/g, '');
            allowedExt = allowedExt.split(',');
        } else {
            allowedExt = allowedImageExtensionList;
        }

        return allowedExt.includes(ext);
    },

    fnExcelUploadRun: function(uploadUrl, uploadType){
        if(gFile == undefined || gFile == ""){
            fnKendoMessage({
                message : "파일을 선택해주세요.",
                ok : function(e) {
                }
            });
            return;
        }
        calPovUploadUtil.fnExcelUpload(gFile, gFileTagId, uploadUrl, uploadType);
    },

    uploadReset: function (){
    	for (var i = 0; i < this.uploadFileTagIdList.length; i++) {
    		$('#'+this.uploadFileTagIdList[i]).data("kendoUpload").removeAllFiles();
    		gFile = "";
    		gFileTagId = "";
    		$("#fileInfoDiv_"+ this.uploadFileTagIdList[i]).text('');
        }
    },

    fnExcelUpload: function (file, fileTagId, uploadUrl, uploadType) {

        var formData = new FormData();

        var reqData = calPovSubmitUtil.getReqData();

        formData.append('uploadFile', file);
        formData.append('scenario', uploadType);
        formData.append('authMenuID', "1100");
        formData.append('year', reqData.findYear);
        formData.append('month', reqData.findMonth);

        $.ajax({
            url         : uploadUrl
            , data        : formData
            , type        : 'POST'
            , contentType : false
            , processData : false
            , async       : false
            , success     : function(data) {
				if (data.code == "0000") {
					calPovUploadUtil.uploadReset();
					calPovSubmitUtil.search();
				} else {
					fnKendoMessage({
						message : data.message
					});
				}
            }
        });
    },
}
