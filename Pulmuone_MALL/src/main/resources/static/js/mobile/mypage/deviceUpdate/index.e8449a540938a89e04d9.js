(function(e){function t(t){for(var r,i,c=t[0],s=t[1],u=t[2],l=0,d=[];l<c.length;l++)i=c[l],Object.prototype.hasOwnProperty.call(a,i)&&a[i]&&d.push(a[i][0]),a[i]=0;for(r in s)Object.prototype.hasOwnProperty.call(s,r)&&(e[r]=s[r]);f&&f(t);while(d.length)d.shift()();return o.push.apply(o,u||[]),n()}function n(){for(var e,t=0;t<o.length;t++){for(var n=o[t],r=!0,c=1;c<n.length;c++){var s=n[c];0!==a[s]&&(r=!1)}r&&(o.splice(t--,1),e=i(i.s=n[0]))}return e}var r={},a={"mobile/mypage/deviceUpdate/index":0},o=[];function i(t){if(r[t])return r[t].exports;var n=r[t]={i:t,l:!1,exports:{}};return e[t].call(n.exports,n,n.exports,i),n.l=!0,n.exports}i.m=e,i.c=r,i.d=function(e,t,n){i.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},i.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},i.t=function(e,t){if(1&t&&(e=i(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(i.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var r in e)i.d(n,r,function(t){return e[t]}.bind(null,r));return n},i.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return i.d(t,"a",t),t},i.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},i.p="/";var c=window["webpackJsonp"]=window["webpackJsonp"]||[],s=c.push.bind(c);c.push=t,c=c.slice();for(var u=0;u<c.length;u++)t(c[u]);var f=s;o.push([54,"chunk-commons"]),n()})({54:function(e,t,n){e.exports=n("fe1d")},e569:function(e,t,n){"use strict";var r=n("f74f"),a=n.n(r);a.a},f74f:function(e,t,n){},fe1d:function(e,t,n){"use strict";n.r(t);var r=n("5530"),a=(n("e260"),n("e6cf"),n("cca6"),n("a79d"),function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("main",{staticClass:"fb__mypage "},[n("fb-header",{attrs:{type:"sub",title:"APP 업데이트",buttons:["back","home","setting"]}}),n("div",{staticClass:"deviceUpdate"},[n("div",{staticClass:"deviceUpdate__inner"},[e._m(0),e.device.isIOS()?n("a",{staticClass:"deviceUpdate__submit",attrs:{href:e.$APP_CONFIG.DEVICE_UPDATE_URL,target:"_blank"},on:{click:function(t){return e.setOpenExternalBrowser(t)}}},[e._v("IOS 업데이트")]):e._e(),e.device.isAOS()?n("a",{staticClass:"deviceUpdate__submit",attrs:{href:e.$APP_CONFIG.DEVICE_UPDATE_URL,target:"_blank"},on:{click:function(t){return e.setOpenExternalBrowser(t)}}},[e._v("Android 업데이트")]):e._e()])]),n("fb-footer"),n("fb-dockbar")],1)}),o=[function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("header",{staticClass:"deviceUpdate__header"},[n("h3",{staticClass:"deviceUpdate__title"},[e._v(" 원활한 APP 사용을 위하여"),n("br"),e._v("업데이트를 진행해주세요. ")])])}],i=n("a861"),c=n("8e32"),s=n("629b"),u=n("cbaa"),f=n("0bf4"),l={name:"mypage-deviceUpdate",mixins:[f["a"]],components:{fbHeader:c["a"],fbFooter:s["a"],fbDockbar:u["a"]},data:function(){return{device:new i["a"]}}},d=l,p=(n("e569"),n("2877")),b=Object(p["a"])(d,a,o,!1,null,null,null),_=b.exports,v=n("dd69");v["c"].components=Object(r["a"])({App:_},v["c"].components),v["d"].dispatch("init").then((function(){return new v["a"](v["c"])}))}});