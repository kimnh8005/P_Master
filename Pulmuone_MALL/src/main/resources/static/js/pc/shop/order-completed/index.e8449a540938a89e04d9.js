(function(t){function e(e){for(var s,n,i=e[0],c=e[1],o=e[2],d=0,p=[];d<i.length;d++)n=i[d],Object.prototype.hasOwnProperty.call(r,n)&&r[n]&&p.push(r[n][0]),r[n]=0;for(s in c)Object.prototype.hasOwnProperty.call(c,s)&&(t[s]=c[s]);l&&l(e);while(p.length)p.shift()();return _.push.apply(_,o||[]),a()}function a(){for(var t,e=0;e<_.length;e++){for(var a=_[e],s=!0,i=1;i<a.length;i++){var c=a[i];0!==r[c]&&(s=!1)}s&&(_.splice(e--,1),t=n(n.s=a[0]))}return t}var s={},r={"pc/shop/order-completed/index":0},_=[];function n(e){if(s[e])return s[e].exports;var a=s[e]={i:e,l:!1,exports:{}};return t[e].call(a.exports,a,a.exports,n),a.l=!0,a.exports}n.m=t,n.c=s,n.d=function(t,e,a){n.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:a})},n.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},n.t=function(t,e){if(1&e&&(t=n(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var a=Object.create(null);if(n.r(a),Object.defineProperty(a,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var s in t)n.d(a,s,function(e){return t[e]}.bind(null,s));return a},n.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return n.d(e,"a",e),e},n.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},n.p="/";var i=window["webpackJsonp"]=window["webpackJsonp"]||[],c=i.push.bind(i);i.push=e,i=i.slice();for(var o=0;o<i.length;o++)e(i[o]);var l=c;_.push([177,"chunk-commons"]),a()})({"0d89":function(t,e,a){"use strict";a.r(e);var s=a("5530"),r=(a("e260"),a("e6cf"),a("cca6"),a("a79d"),function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("main",{staticClass:"fb__order"},[a("fb-header",{attrs:{title:t.header.title,buttons:t.header.buttons,actions:t.header.actions}}),a("header",{staticClass:"fb__order__header"},[a("h2",{staticClass:"order__header__title"},[t._v(" "+t._s(t.header.title)+" ")]),t.error.code?t._e():a("nav",{staticClass:"fb__order__step__area"},[a("span",{staticClass:"fb__order__step"},[t._v("배송방법 선택")]),a("span",{staticClass:"fb__order__step"},[t._v("주문서 작성")]),a("span",{staticClass:"fb__order__step fb__order__step--active"},[t._v(t._s(t.header.title))])])]),t.error.code?a("section",{staticClass:"failed__wrapper"},[a("h2",{staticClass:"failed__title"},[t._v("주문이 실패되었습니다.")]),a("div",{staticClass:"failed__result"},[a("dl",[a("dt",[t._v("실패사유")]),a("dd",{attrs:{color:"danger"}},[t._v(t._s(t.error.message))])]),a("dl",[a("dt",[t._v("고객기쁨센터")]),a("dd",[a("a",{attrs:{href:"tel:"+t.$APP_CONFIG.SERVICE_CENTER.TEL}},[a("em",[t._v(t._s(t.$APP_CONFIG.SERVICE_CENTER.TEL))])])])])]),a("div",{staticClass:"fb__btn-wrap failed__action__area"},[a("a",{staticClass:"fb__btn-margin--green failed__action__move-cart",attrs:{href:t.$APP_CONFIG.PAGE_URL.CART+"?cartType=NORMAL",tabindex:"0"}},[t._v("장바구니로 이동")])])]):a("section",{staticClass:"completed__wrapper",class:{"completed__wrapper--gifting":t.isGifting}},[t.isVirtualBank?[t.$FB_CODES.FETCHES.WAIT===t.fetches.virtualAccount?a("div",{staticClass:"fb__fetching"}):t._e(),t.$FB_CODES.FETCHES.SUCCESS===t.fetches.virtualAccount?[a("h2",{staticClass:"completed__title"},[t._v("주문이 정상적으로 완료 되었습니다.")]),t.userSession.isLogin?a("p",{staticClass:"completed__desc"},[t._v("주문하신 내역은 마이페이지의 주문조회에서 확인하실 수 있습니다.")]):a("p",{staticClass:"completed__desc"},[t._v("주문하신 내역은 로그인의 비회원 주문조회에서 확인하실 수 있습니다.")]),a("div",{staticClass:"completed__virtual-account__guide"},[a("dl",[a("dt",[t._v("입금기한")]),a("dd",[t.isToday(t.virtualAccount.paidDueDate)?a("span",{staticClass:"completed__virtual-account__today"},[t._v(" 오늘까지"),a("em",{attrs:{color:"point",font:"lato"}},[t._v("("+t._s(t.virtualAccount.paidDueDate)+")")])]):a("em",{attrs:{color:"point"}},[t._v(t._s(t.virtualAccount.paidDueDate))])])]),a("dl",[a("dt",[t._v("은행명")]),a("dd",[t._v(t._s(t.virtualAccount.bankName))])]),a("dl",[a("dt",[t._v("계좌번호")]),a("dd",[a("em",{attrs:{font:"lato"}},[t._v(t._s(t.virtualAccount.info))]),t._v("(예금주: "+t._s(t.virtualAccount.paidHolder)+")")])]),a("dl",{staticClass:"price-area"},[a("dt",[t._v("입금액")]),a("dd",[a("em",{attrs:{font:"lato"}},[t._v(t._s(t._f("price")(t.virtualAccount.paymentPrice)))]),t._v("원")])])]),a("p",{staticClass:"fb__noticecaption fb__noticecaption--guide"},[t._v("입금 기한 내 입금이 되지 않았을 경우, 주문이 취소됩니다.")]),a("div",{staticClass:"fb__btn-wrap completed__action__area"},[a("a",{staticClass:"fb__btn-margin--white completed__btn",attrs:{href:t.$APP_CONFIG.PAGE_URL.ROOT,tabindex:"0"}},[t._v("메인으로")]),a("a",{staticClass:"fb__btn-margin--green completed__btn",attrs:{href:t.orderHistoryURL,tabindex:"0"}},[t._v("주문내역 확인")])])]:t.$FB_CODES.FETCHES.ERROR===t.fetches.virtualAccount?a("error-layout",{staticClass:"fb__error--child",attrs:{"error-type":"default"}},[[a("div",{staticClass:"fb__btn-wrap margin"},[a("button",{staticClass:"fb__btn-margin--white",attrs:{type:"button",tabindex:"0"},on:{click:function(e){return t.reload(e,"virtualAccount")}}},[t._v("새로고침")]),a("button",{staticClass:"fb__btn-margin--green",attrs:{type:"button",tabindex:"0"},on:{click:function(e){return t.goToRoute(t.$APP_CONFIG.PAGE_URL.ROOT)}}},[t._v("홈으로")])])]],2):t._e()]:[t.isRegularOrder?[a("h2",{staticClass:"completed__title"},[t._v("정기배송 신청이 완료되었습니다.")]),a("p",{staticClass:"completed__desc"},[t._v("신청하신 내역은 마이페이지 > 정기배송관리에서 확인하실 수 있습니다.")])]:t.isGifting?[a("h2",{staticClass:"completed__title"},[t._v("선물 주문이 완료 되었습니다.")]),a("p",{staticClass:"completed__desc"},[t._v("신청하신 내역은 마이페이지 > 보낸선물함에서 확인하실 수 있습니다.")])]:t.isRentalOrder?[a("h2",{staticClass:"completed__title"},[t._v("렌탈상품 상담신청이 정상적으로 완료되었습니다.")]),a("p",{staticClass:"completed__desc completed__desc--happy-call"},[t._v("신청 후 "+t._s(t.rental.withIn)+"일 이내 해피콜 예정")]),a("p",{staticClass:"completed__desc"},[t._v("신청하신 내역은 마이페이지 > 주문조회에서 확인하실 수 있습니다.")])]:[a("h2",{staticClass:"completed__title"},[t._v("결제가 정상적으로 완료 되었습니다.")]),t.userSession.isLogin?a("p",{staticClass:"completed__desc"},[t._v("주문하신 내역은 마이페이지의 주문조회에서 확인하실 수 있습니다.")]):a("p",{staticClass:"completed__desc"},[t._v("결제하신 내역은 로그인의 비회원 주문조회에서 확인하실 수 있습니다.")])],a("div",{staticClass:"fb__btn-wrap completed__action__area"},[a("a",{staticClass:"fb__btn-margin--white completed__btn",attrs:{href:t.$APP_CONFIG.PAGE_URL.ROOT,tabindex:"0"}},[t._v("메인으로")]),t.isRegularOrder?a("a",{staticClass:"fb__btn-margin--green completed__btn",attrs:{href:t.$APP_CONFIG.PAGE_URL.MYPAGE_REGULAR_MANAGE,tabindex:"0"}},[t._v("신청내역 확인")]):t.isGifting?a("a",{staticClass:"fb__btn-margin--green completed__btn",attrs:{href:t.$APP_CONFIG.PAGE_URL.MYPAGE_ORDER_HISTORY+"?div=present",tabindex:"0"}},[t._v("보낸선물함 확인")]):a("a",{staticClass:"fb__btn-margin--green completed__btn",attrs:{href:t.orderHistoryURL,tabindex:"0"}},[t._v("주문내역 확인")])])]],2),a("fb-footer")],1)}),_=[],n=a("f147"),i=a("dcbe"),c=a("4402"),o=a("3540"),l={name:"order-completed",extends:n["a"],components:{fbHeader:i["a"],fbFooter:c["a"],errorLayout:o["a"]}},d=l,p=(a("f83a"),a("2877")),u=Object(p["a"])(d,r,_,!1,null,null,null),f=u.exports,b=a("8f7d");b["c"].components=Object(s["a"])({App:f},b["c"].components),b["d"].dispatch("init").then((function(){return new b["a"](b["c"])}))},177:function(t,e,a){t.exports=a("0d89")},b47c:function(t,e,a){},f83a:function(t,e,a){"use strict";var s=a("b47c"),r=a.n(s);r.a}});