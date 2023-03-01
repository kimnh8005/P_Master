/**-----------------------------------------------------------------------------
 * description 		 : 통계관리 > 데이터추출 > 데이터 추출 다운로드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.06.09		배민영          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'staticsDataDownload',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnInitOptionBox();// Initialize Option Box
		fnDefaultSet();
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch', '#fnClear', '#fnExcelDownload').kendoButton();
	}

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
		if(data.startDt == "" || data.endDt == ""){
            fnKendoMessage({message : '기준 기간을 입력해주세요.'});
            return;
        }
        if($('input[name="dataDownloadFilter"]:checked').val() == undefined) {
            fnKendoMessage({message : '추출 유형을 선택해주세요.'});
            return;
        }
        $("#divDataWrap").empty();
        $('#div-data-loading')[0].style.display = "";
        fnAjax({
            url     : '/admin/statics/data/getDataDownloadStaticsList',
            params  : data,
            success :
                function( data ){
                    $('#div-data-loading')[0].style.display = "none";
                    callBackDataDownloadStaticsList(data);
                },
            error : function ( xhr, status, strError ) {
                    $('#div-data-loading')[0].style.display = "none";
                    if(xhr.status === '502') {
                        fnMessage('응답이 원활하지 않아 오류가 발생하였습니다.<br />같은 문제가 계속 발생할 시 관리자에게 문의해 주세요.');
                    } else {
                        fnMessage('시스템 오류가 발생했습니다.<br />같은 문제가 계속 발생할 시 관리자에게 문의해 주세요.');
                    }
                },
            isAction : 'select'
        });
	}

    // fnSearch() 콜백 함수
    function callBackDataDownloadStaticsList(data){
        var formData = $('#searchForm').formSerialize(true);
        var endDate = formData.endDt
        if(endDate.length == 8) {
            endDate = endDate.substr(0,4) + "-" + endDate.substr(4,2) + "-" + endDate.substr(6,2);
        }
        console.log(data.dataDownloadTypeResult);
        if(data == null || data == undefined || data.dataDownloadTypeResult == undefined) {
            return;
        }
        var obj = data.dataDownloadTypeResult;
        var strHtml = '';
        for (var i = 0; i < obj.length; i++) {
            var arrData = obj[i].split('___');
            // 추출유형_추출건수_합계금액
            if (arrData != null && arrData.length == 4) {
                var strTitle = '';
                var strSumTitle = '잔여적립금';
                switch(arrData[0]) {
                    case '01':
                        strTitle = '적립금 회원 잔액 (기준기간 시작일은 온라인몰 오픈일자이며, ['+ endDate + '] 일 기준으로 추출 됨)';
                        break;
                    case '02':
                        strTitle = '적립금 임직원 잔액 (기준기간 시작일은 온라인몰 오픈일자이며, ['+ endDate + '] 일 기준으로 추출 됨)';
                        break;
                    case '03':
                        strTitle = '적립금 정산';
                        strSumTitle = '사용금액';
                        break;
                    case '04':
                        strTitle = '쿠폰 정산';
                        strSumTitle = '사용금액';
                        break;
                    case '05':
                        strTitle = '내부회계통제용 쿠폰 지급';
                        strSumTitle = '최대할인금액';
                        break;
                    case '06':
                        strTitle = '쿠폰비용 사용';
                        strSumTitle = '쿠폰비용';
                        break;
                    case '07':
                        strTitle = '적립금비용 사용';
                        strSumTitle = '적립금사용';
                        break;
                    case '08':
                        strTitle = '임직원 할인지원액';
                        strSumTitle = '사용한도액';
                        break;
                    case '09':
                        strTitle = '용인물류 품목별 폐기 기준 (['+ endDate + '] 일 기준으로 추출 됨)';
                        strSumTitle = '폐기기준일';
                        break;
                    case '10':
                        strTitle = '객단가';
                        strSumTitle = '주문금액';
                        break;
                }

                strHtml += '<div> ';
                strHtml += '    <div class="wrap-tit"> ';
                strHtml += '        <div class="ea-title"> ';
                strHtml += '            <span>▶ ' + strTitle + '</span> ';
                strHtml += '        </div> ';
                strHtml += '        <div class="btn-area"> ';
                strHtml += '            <button type="button" class="fb__btn-excel ml--default" onclick="$scope.fnExcelDownload(\''+ arrData[3].replace(/\\/ig,"/") +'\')">엑셀 다운로드</button> ';
                strHtml += '        </div> ';
                strHtml += '    </div> ';
                strHtml += '    <table class="datatable v-type"> ';
                strHtml += '        <colgroup> ';
                strHtml += '            <col style="width:20%"> ';
                strHtml += '            <col style="width:30%"> ';
                strHtml += '            <col style="width:20%"> ';
                strHtml += '            <col style="width:30%"> ';
                strHtml += '        </colgroup> ';
                strHtml += '        <tbody> ';
                strHtml += '        <tr> ';
                strHtml += '            <th scope="row">추출 건수</th> ';
                if(arrData[0] == '09') {
                    strHtml += '            <td colspan="3"> 총 ' + numberFormat(arrData[1]) + ' 건</td> ';
                } else {
                    strHtml += '            <td> 총 ' + numberFormat(arrData[1]) + ' 건</td> ';
                    strHtml += '            <th scope="row">합계 (' + strSumTitle + ')</th> ';
                    strHtml += '            <td> ' + numberFormat(arrData[2]) + ' 원</td> ';
                }                
                strHtml += '        </tr> ';
                strHtml += '        </tbody> ';
                strHtml += '    </table> ';
                strHtml += '</div> ';
                strHtml += '<br><br><br> ';
            }
        }
        $('#divDataWrap').append(strHtml);
    }

    // 천단위 (,) 로 표시
    function numberFormat(inputNumber) {
        return inputNumber.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

	function fnClear(){
		$('#searchForm').formClear(true);
		fnDefaultSet();
	}

	function fnGetCheckBoxText(id){
	    var value = "";
        $('form[id=searchForm] :checkbox[name='+ id +']:checked').each(function() {
            value += $(this).closest('label').find('span').text() + ","
        });
        if (value == null)
            value = "";
        value = value.substring(0, value.length - 1);
        return value;
	}

    // 옵션 초기화
	function fnInitOptionBox(){
        // 기준기간 시작일/종료일 [날짜]
        fnKendoDatePicker({
            id          : 'startDt'
          , format      : 'yyyy-MM-dd'
          , btnStartId  : 'startDt'
          , btnEndId    : 'endDt'
          , defVal: fnGetDayMinus(fnGetToday(),30)
          , defType : 'oneMonth'
        });
        fnKendoDatePicker({
            id          : 'endDt'
            , format      : 'yyyy-MM-dd'
            , btnStyle    : true     //버튼 노출
            , btnStartId  : 'startDt'
            , btnEndId    : 'endDt'
            , defVal: fnGetToday()
            , defType : 'oneMonth'
        });

        // 추출유형 [체크]
        fnTagMkChkBox({
            id        : 'dataDownloadFilter'
          , tagId     : 'dataDownloadFilter'
          , data      : [
                        {'CODE' : '01','NAME':'적립금 회원 잔액'}
                        ,{'CODE' : '02','NAME':'적립금 임직원 잔액'}
                        ,{'CODE' : '03','NAME':'적립금 정산'}
                        ,{'CODE' : '04','NAME':'쿠폰 정산'}
                        ,{'CODE' : '05','NAME':'내부회계통제용 쿠폰 지급'}
                        ,{'CODE' : '06','NAME':'쿠폰비용 사용'}
                        ,{'CODE' : '07','NAME':'적립금비용 사용'}
                        ,{'CODE' : '08','NAME':'임직원 할인지원액'}
                        ,{'CODE' : '09','NAME':'용인물류 품목별 폐기 기준'}
                        ,{'CODE' : '10','NAME':'객단가'}
                        ]
          , chkVal    : 'Y'
          , style     : {}
        });

        fbCheckboxChange(); //[공통] checkBox
	};

	// 기본 설정
	function fnDefaultSet(){
	    $("input[name=dataDownloadFilter]").eq(0).prop("checked", true).trigger("change");
        setDefaultDatePicker();
	};

    function setDefaultDatePicker() {
        $(".date-controller button").each(function() {
            $(this).attr("fb-btn-active", false);
        })

        $("button[data-id='fnDateBtn5']").attr("fb-btn-active", true);

        var today = fnGetToday();

        $("#startDt").data("kendoDatePicker").value(fnGetDayMinus(today, 30));
        $("#endDt").data("kendoDatePicker").value(today);
    }

    // --------------------------------- Button
	// End---------------------------------

	// ------------------------------- Grid Start
	// -------------------------------

	// ------------------------------- Grid End -------------------------------

	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------

	/** Common Search */
	$scope.fnSearch = function(){	fnSearch();	};
	/** Common Clear */
	$scope.fnClear = function(){	 fnClear();	};

	/** Button fnExcelDownload */
    $scope.fnExcelDownload = function( fileFullPath ) {
        var filePath = fileFullPath.substring(0, fileFullPath.lastIndexOf("/"));
        var fileName = fileFullPath.substring(fileFullPath.lastIndexOf("/")+1);
        var opt = {
            filePath: filePath,
            physicalFileName: fileName,
            originalFileName: fileName
        }
        console.log("@@@@@ ", opt);

        var event = window.event;
        var targetElement = event.target || event. srcElement;

        fnDownloadPublicFileCallback(opt, function(){
            // fnDeletePublicFile(opt); // 다운로드 완료된 파일 삭제
            // targetElement.style.display = "none"; // 다운로드 버튼 hidden 처리
        });
    };

    // fnExcelDownload() 콜백 함수
    function fnDownloadPublicFileCallback(opt, fnCallback) {  // Public 저장소의 파일 다운로드
        var filePath = opt.filePath; // 다운로드할 파일의 하위 경로
        var physicalFileName = opt.physicalFileName; // 업로드시 저장된 물리적 파일명
        var originalFileName = opt.originalFileName; // 원본 파일명 또는 다운로드시 지정할 파일명

        // 파일 다운로드시 Response Header 에 인코딩된 파일명을 지정하는 Key 값
        var downloadFileNameResponseHeaderKey = 'file-name';
        var downloadUrl = '/comn/fileDownload'; // 파일 다운로드 API
        downloadUrl += '?' + 'filePath' + '=' + filePath;
        downloadUrl += '&' + 'physicalFileName' + '=' + physicalFileName;
        downloadUrl += '&' + 'originalFileName' + '=' + originalFileName;

        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {  // 요청에 대한 콜백
            if (xhr.readyState === xhr.DONE) { // 요청 완료시
                // 다운로드 완료시 서버에서 전달한 파일명
                var downloadFileName = decodeURIComponent( //
                    decodeURI( //
                        xhr.getResponseHeader(downloadFileNameResponseHeaderKey) //
                    ) //
                        .replace(/\+/g, " ") // 파일명에서 공백이 + 로 표시 : " " 으로 replace 처리
                );
                if( xhr.status === 200 || xhr.status === 201 ) { // 다운로드 성공
                    if (typeof window.navigator.msSaveBlob !== 'undefined') { // IE 사용시
                        window.navigator.msSaveBlob(xhr.response, downloadFileName);
                    } else {
                        var a = document.createElement('a');
                        a.href = window.URL.createObjectURL(xhr.response);
                        a.download = downloadFileName;
                        a.style.display = 'none';
                        document.body.appendChild(a);
                        a.click();
                    }
                    fnCallback(); // callback 추가
                } else { // 다운로드 실패
                    var reader = new FileReader();
                    reader.addEventListener('loadend', (e) => {
                        var errorMessage = JSON.parse(e.srcElement['result']);
                        console.log(errorMessage);
                    });
                    reader.readAsText(xhr.response);
                }
            }
        } // xhr.onreadystatechange end

        xhr.open('GET', downloadUrl);
        xhr.responseType = 'blob';
        xhr.send();
    }

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
