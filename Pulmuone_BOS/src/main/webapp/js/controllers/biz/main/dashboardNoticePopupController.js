﻿/**-----------------------------------------------------------------------------
 * description 		 : 공지사항 대시보드 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.10		최윤지       최초생성
 * @
 * **/
'use strict';

$(document).ready(function () {

    var pageParam = fnGetPageParam(); //GET Parameter 받기

    fnInitialize();

  //Initialize PageR
    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : false });

        fnPageInfo({
            PG_ID  : 'dashboardNoticePopup',
            callback : fnUI
        });
    }

    function fnUI(){
        // 팝업 페이지 min-width 초기화
        initStyle();

        fnInitButton(); //Initialize Button  ---------------------------------


        fnAjax({
             url: "/admin/comn/popup/getNoticePopup",
             params  : {csCompanyBbsId : pageParam.csCompanyBbsId},
             success:
                 function (data) {
                                 fnDashboardNoticePopup(data.getNoticePopupResultVo); // 대시보드 공지사항 상세내용 팝업
                 },
             isAction : 'batch'
         });
    }

    //버튼 초기화
    function fnInitButton(){
        $('#fnOk').kendoButton();

    };


        //대시보드 공지사항 상세내용 팝업
        function fnDashboardNoticePopup(params){
            if (params.popupDisplayTodayYn == 'N'){
                $("#content").html(fnTagConvert(params.content)); //내용
                $("#csCompanyBbsId").val(params.csCompanyBbsId); // index
                $("#popupDisplayTodayYn").css("display", "none");
                $("label[for = 'popupDisplayTodayYn']").css("display", "none");
            }

            else {
                $("#content").html(fnTagConvert(params.content)); //내용
                $("#csCompanyBbsId").val(params.csCompanyBbsId); // index
            }

            popupAutoResize();
        };


        // 팝업 오늘 하루 보기
        function fnOk(){

            if($("input:checkbox[id='popupDisplayTodayYn']").is(":checked")){
                setCookie("csCompanyBbsId_"+$("#csCompanyBbsId").val(), $("#csCompanyBbsId").val(), 1); // 각 csCompanyBbsId 값 set
                setCookie("popupDisplayTodayYn", $("input:checkbox[id='popupDisplayTodayYn']").is(":checked"), 1);
            } else {
                deleteCookie("csCompanyBbsId_"+$("#csCompanyBbsId").val());
                deleteCookie("popupDisplayTodayYn");
            }

            window.close();

        }

        //쿠키값 Set
        function setCookie(cookieName, value, exdays) {
            var exdate = new Date();
            exdate.setDate(exdate.getDate() + exdays);
            var cookieValue = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toGMTString());
            document.cookie = cookieName + "=" + cookieValue;
        }

        //쿠키값 Delete
        function deleteCookie(cookieName) {
            var expireDate = new Date();
            expireDate.setDate(expireDate.getDate() - 1);
            document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
        }

        // 팝업 페이지 min-width 기본값으로 설정
        function initStyle() {
            const ngView = $(document).find('#ng-view');
            ngView.css({
                'min-width': 'auto',
            });
        }

        // 팝업 사이즈 자동 조절
        function popupAutoResize() {
            var thisX = parseInt(document.body.scrollWidth);
            var thisY = parseInt(document.body.scrollHeight);

            var maxThisX = screen.width - 50;
            var maxThisY = screen.height - 50;

            var marginY = 0;
            var marginX = 0;

            // 브라우저별 높이 조절. (표준 창 하에서 조절해 주십시오.)
            if (navigator.userAgent.indexOf("MSIE 6") > 0) marginY = 45;        // IE 6.x

            else if(navigator.userAgent.indexOf("Firefox") > 0) marginY = 50;   // FF

            else if(navigator.userAgent.indexOf("Opera") > 0) marginY = 30;     // Opera

            else if(navigator.userAgent.indexOf("Netscape") > 0) marginY = -2;  // Netscape

            else marginY = 70;



            if (navigator.userAgent.indexOf("MSIE 6") > 0) marginX = 40;        // IE 6.x

            else if (navigator.userAgent.indexOf("MSIE 7") > 0) marginX = 40;        // IE 7.x

            else marginX = 20;


            if (thisX > maxThisX) {
                window.document.body.scroll = "yes";

                thisX = maxThisX;
            }

            // if (thisY > maxThisY - marginY) {
            //     window.document.body.scroll = "yes";

            //     thisX += 19;
            //     thisY = maxThisY - marginY;
            // }

            const $ngView = $('#ng-view');
            var contentHeight = $ngView.outerHeight();
            if( contentHeight > maxThisY - marginY ) {
                window.document.body.scroll = "yes";

                thisX += 19;
                thisY = maxThisY - marginY;
            } else {
                thisY = contentHeight + 50;
            }

            window.resizeTo(thisX + marginX, thisY + marginY);

            // 센터 정렬
            // var windowX = (screen.width - (thisX+10))/2;
            // var windowY = (screen.height - (thisY+marginY))/2 - 20;
            // window.moveTo(windowX,windowY);
         }


        /** Common OK*/
        $scope.fnOk = function( ){  fnOk();}; // 확인

}); // document ready - END
