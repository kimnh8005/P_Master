(function(t){function e(e){for(var n,o,i=e[0],c=e[1],l=e[2],u=0,f=[];u<i.length;u++)o=i[u],Object.prototype.hasOwnProperty.call(s,o)&&s[o]&&f.push(s[o][0]),s[o]=0;for(n in c)Object.prototype.hasOwnProperty.call(c,n)&&(t[n]=c[n]);p&&p(e);while(f.length)f.shift()();return a.push.apply(a,l||[]),r()}function r(){for(var t,e=0;e<a.length;e++){for(var r=a[e],n=!0,i=1;i<r.length;i++){var c=r[i];0!==s[c]&&(n=!1)}n&&(a.splice(e--,1),t=o(o.s=r[0]))}return t}var n={},s={"any/main-gate/index":0},a=[];function o(e){if(n[e])return n[e].exports;var r=n[e]={i:e,l:!1,exports:{}};return t[e].call(r.exports,r,r.exports,o),r.l=!0,r.exports}o.m=t,o.c=n,o.d=function(t,e,r){o.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:r})},o.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},o.t=function(t,e){if(1&e&&(t=o(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var r=Object.create(null);if(o.r(r),Object.defineProperty(r,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var n in t)o.d(r,n,function(e){return t[e]}.bind(null,n));return r},o.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return o.d(e,"a",e),e},o.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},o.p="/";var i=window["webpackJsonp"]=window["webpackJsonp"]||[],c=i.push.bind(i);i.push=e,i=i.slice();for(var l=0;l<i.length;l++)e(i[l]);var p=c;a.push([4,"chunk-commons"]),r()})({"0607":function(t,e,r){"use strict";var n=r("ad10"),s=r.n(n);s.a},"24fe":function(t,e,r){"use strict";r.r(e);var n=r("5530"),s=(r("e260"),r("e6cf"),r("cca6"),r("a79d"),function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("main",{staticClass:"fb__gate"},[r("div",{staticClass:"fb__gate__description"},[t._m(0),r("div",{staticClass:"description__content"},[t._m(1),r("div",{staticClass:"progress"},[r("div",{staticClass:"progress__bar"},[r("span",{ref:"progressBar"})]),r("button",{staticClass:"progress__btn",class:{stop:t.isStop},attrs:{type:"button"},on:{click:function(e){t.isStop=!t.isStop}}},[t._v("이동 재생/일시정지")])])]),r("div",{staticClass:"description__benefit"},[r("figure",[r("img",{attrs:{src:"/assets/"+("desktop"==t.platform?"pc":"mobile")+"/images/any/gate-gift-new-member.png",alt:""}})])]),r("div",{staticClass:"description__btn"},[r("a",{attrs:{href:t.$APP_CONFIG.OLD_MALL.PULMUONE_ORDER_LIST,target:"_blank"},on:{click:function(e){return t.setOpenExternalBrowser(e)}}},[t._v("구)풀무원샵 주문 조회")]),r("a",{attrs:{href:t.$APP_CONFIG.OLD_MALL.ORGA_ORDER_LIST_PC,target:"_blank"},on:{click:function(e){return t.setOpenExternalBrowser(e)}}},[t._v("구)올가 주문 조회")])])])])}),a=[function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"description__logo"},[r("img",{attrs:{src:"/assets/pc/images/any/deep-gate-logo.svg",alt:""}}),r("p",[t._v("GRAND OPEN")])])},function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("p",[t._v("새로워진 "),r("strong",[t._v("#풀무원")]),t._v("으로 이동중입니다.")])}],o=(r("d3b7"),r("ac1f"),r("3ca3"),r("841c"),r("ddb0"),r("2b3d"),r("1891")),i={name:"deep-gate",mixins:[o["a"]],components:{},data:function(){return{isStop:!0,delay:5e3,time:0}},created:function(){var t;this.delay=null!==(t=new URLSearchParams(location.search).get("delay"))&&void 0!==t?t:this.delay,this.isStop=!1,window.setProgress=null},watch:{isStop:{handler:function(t){t?clearInterval(window.setProgress):window.setProgress=setInterval(this.setProgress,parseFloat(this.delay)/100)}}},methods:{setProgress:function(){if(!this.isStop&&this.$refs["progressBar"]){this.time++;var t=1,e=parseFloat(this.$refs["progressBar"].style.width)?parseFloat(this.$refs["progressBar"].style.width):0;e>=100?(clearInterval(window.setProgress),this.goToRoute(this.$APP_CONFIG.PAGE_URL.ROOT)):this.$refs["progressBar"].style.width="".concat(e+t>=100?100:e+t,"%")}}}},c=i,l=(r("0607"),r("2877")),p=Object(l["a"])(c,s,a,!1,null,null,null),u=p.exports,f=r("633d");f["c"].components=Object(n["a"])({App:u},f["c"].components),f["d"].dispatch("init").then((function(){return new f["a"](f["c"])}))},4:function(t,e,r){t.exports=r("24fe")},ad10:function(t,e,r){}});