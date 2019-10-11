/*!
 * dfish.js v3
 * (c) 2015-2018 Mingyuan Chen
 * Released under the MIT License.
 */

( function( global, factory ) {
	/* 在 CommonJS 环境下返回 dfish，不创建全局变量。 */
	if ( typeof module === 'object' && typeof module.exports === 'object' ) {
		module.exports = factory( global, true );
	} else if ( typeof define === 'function' ) {
		define( function() { return factory( global, true ); } );
	} else {
		factory( global );
	}
} )( this, function( win, noGlobal ) {
	
var
A = [], O = {}, N = null, T = true, F = false, U,

_path, _ui_path, _lib, _cfg = {}, _alias = {}, _$ = win.$, _ver = '', _expando = 'dfish', version = '3.3.0',

_STR = 'string', _OBJ = 'object', _NUM = 'number', _FUN = 'function', _PRO = 'prototype',

doc = win.document, cvs = doc.documentElement,

$ = dfish = function( a ) {
	if ( a != N ) return a.isWidget ? a.$() : a.nodeType ? a : doc.getElementById( a );
},

//获取dfish所在目录
getPath = function() {
	var u = location.href.substring( 0, location.href.lastIndexOf( '/' ) + 1 ).replace( location.protocol + '//' + location.host, '' );
	_path = u.substring( 0, u.lastIndexOf( '/' ) + 1 ) || './';
	if ( _path.indexOf( './' ) === 0 || _path.indexOf( '../' ) === 0 ) {
		_path = _urlLoc( location.pathname, _path );
	}
	u = doc.currentScript ? doc.currentScript.src : (function() {
		var js = doc.scripts, l = js.length - 1, src;
		for ( var i = l; i > 0; i -- ) {
			if( js[ i ].readyState === 'interactive' ) {
				src = js[ i ].src;
       			break;
			}
		}
		return src || js[ l ].src;
	})();
	_lib = u.substring( 0, u.lastIndexOf( '/' ) + 1 ).replace( location.protocol + '//' + location.host, '' );		
},

// 浏览器信息
br = $.br = (function() {
	var u = navigator.userAgent.toLowerCase(),
		d = doc.documentMode,
		n = u.indexOf( 'trident' ) > 0 && d > 10,
		ie = navigator.appName === 'Microsoft Internet Explorer', // ie version <= 10
		iv = ie && (d || parseFloat( u.substr( u.indexOf( 'msie' ) + 5 ) )),
		chm = u.match( /\bchrome\/(\d+)/ );
	// 提示内容：您的浏览器版本过低，建议您升级到IE7以上或安装谷歌浏览器。
	ie && iv < 6 && alert( '\u60a8\u7684\u6d4f\u89c8\u5668\u7248\u672c\u8fc7\u4f4e\uff0c\u5efa\u8bae\u60a8\u5347\u7ea7\u5230\u0049\u0045\u0037\u4ee5\u4e0a\u6216\u5b89\u88c5\u8c37\u6b4c\u6d4f\u89c8\u5668\u3002' );
	return {
		ie		: ie,
		ieVer	: iv,
		ie7		: ie && d < 8, // ie6,ie7,兼容模式
		ie10	: ie && d === 10,
		ms		: ie || n, // 微软的浏览器( ie所有系列 )
		chm		: chm && parseFloat( chm[ 1 ] ),
		mobile  : !!u.match( /\bmobile\b/ ),
		fox		: u.indexOf( 'firefox' ) > 0,
		safari  : u.indexOf( 'safari' ) > 0,
		css3	: !(ie && d < 9),
		scroll	: 17,
		chdiv	: function( a, b, c ) {
			if ( typeof b === _FUN ) {
				c = b, b = '1';
			}
			var o = _div( b == N ? '1' : b );
			o.className = a;
			o.style.cssText = 'position:absolute;width:50px;top:-50px;';
			_db( o );
			c && c.call( o );
			_rm( o );
		}
	};
})(),

ie = br.ie,

// a继承b的属性。不覆盖a的既有同名属性
_extend = $.extend = function ( a ) {
	for ( var i = 1, c; i < arguments.length; i ++ ) {
		if ( c = arguments[ i ] ) {
			for ( var k in c ) if ( !(k in a) ) a[ k ] = c[ k ];
		}
	}
	return a;
},
// 递归继承
_extendDeep = $.extendDeep = function ( a ) {
	for ( var j = 1, l = arguments.length, c, i; j < l; j ++ ) if ( c = arguments[ j ] ) {
		for ( i in c )
			if ( ! ( i in a ) ) {
				if ( c[ i ] != N && c[ i ].constructor === Object ) {
					_extendDeep( a[ i ] = {}, c[ i ] );
				} else
					a[ i ] = c[ i ];
			} else if ( a[ i ] != N && a[ i ].constructor === Object )
				_extendDeep( a[ i ], c[ i ] );
	}
	return a;
},
// a拷贝b的属性
_merge = $.merge = function( a ) {
	for ( var i = 1, c, l = arguments.length; i < l; i ++ ) {
		if ( c = arguments[ i ] ) {
			for ( var k in c ) a[ k ] = c[ k ];
		}
	}
	return a;
},
// a拷贝b的属性
_mergeDeep = $.mergeDeep = function( a ) {
	for ( var i = 1, c, l = arguments.length; i < l; i ++ ) {
		if ( c = arguments[ i ] ) {
			for ( var k in c ) {
				if ( typeof a[ k ] === _OBJ )
					_mergeDeep( a[ k ], c[ k ] );
				else
					a[ k ] = c[ k ];
			}
		}
	}
	return a;
},
// 创建类
_createClass = $.createClass = function( a, b ) {
	var n;
	b && (n = a, a = b);
	var c = a.Const, d = a.Extend, e = a.Listener, f = a.Prototype;
	e && ! e.body && (e.body = {});
	_mergeDeep( c, a );
	if ( d ) {
		if ( typeof d === _FUN )
			d = [ d ];
		for ( var i = 0, l = d.length; i < l; i ++ ) {
			_extend( c[ _PRO ], d[ i ][ _PRO ] );
			if ( d[ i ].Listener )
				_extendDeep( e || (e = c.Listener = {body:{}}), d[ i ].Listener );
			if ( d[ i ].Default )
				_extend( c.Default || (c.Default = {}), d[ i ].Default );
		}
	}
	if ( f ) {
		if ( d ) {
			_merge( c[ _PRO ], f );
		} else {
			var g = new Function;
			g[ _PRO ] = f;
			c[ _PRO ] = new g;
		}
	}
	if ( n ) {
		c[ _PRO ].type = c.type = n;
	}
	a.Helper && _mergeDeep( c, a.Helper );
	c[ _PRO ].Const = c;
	return c;
},

/* CMD模块规范 */
_moduleCache = {},
_templateCache = {},
_preloadCache = {},
//@a -> path, b -> id, f -> affix
_mod_uri = function( a, b, f ) {
	var c = b.charAt( 0 ) === '.' ? _urlLoc( a, b ) : b.charAt( 0 ) !== '/' ? _path + b : b;
	// 若文件名没有后缀，则加上.js
	if ( ! f )
		f = 'js';
	if ( _strFrom( c, '.', T ) !== f )
		c += '.' + f;
	return c;
},
// 每个模块的运行环境下都会生成一个Module的实例 /@a -> uri
Module = _createClass( {
	Const: function( a ) {
		this.id   = a;
		this.url  = a;
		this.path = _strTo( a, '/', T ) + '/';
		this.exports = {};
	}
} ),
//每个模块的运行环境下都会生成一个Require的实例 /@a -> path
Require = function( a ) {
	// @b -> js css array, f -> fn?, s -> async?
	var	r = function( b, f, s ) {
		if ( typeof b === _STR ) {
			var c = _mod_uri( a, b );
			if ( _moduleCache[ c ] ) {
				f && f( _moduleCache[ c ] );
				return _moduleCache[ c ];
			}
			b = [ b ];
		} else
			b = b.concat();
		for ( var i = 0, c = [], d = [], l = b.length; i < l; i ++ )
			(_strFrom( b[ i ], '.', T ) === 'css' ? c : d).push( b[ i ] );
		// 同步时，由于css目前只能异步加载，所以css_ok直接设为true
		var css_ok = ! s || ! c.length, js_ok = ! d.length,
			g = function() { js_ok && css_ok && f && f(); };
		c.length && r.css( c, function() { css_ok = T, g() } );
		return d.length && r.js( d, function() { js_ok = T, g() }, s );
	},
	exe = function( b, f ) {
		for ( var i = 0, g = []; i < b.length; i ++ )
			g.push( _moduleCache[ b[ i ] ] );
		f.apply( win, g );
	};
	// @ b -> js array, f -> fn, s -> async?
	r.js = function( b, f, s ) {
		for ( var i = 0, d = [], e = [], u; i < b.length; i ++ ) {
			b[ i ] = _mod_uri( a, b[ i ] );
			if ( (u = _alias[ b[ i ] ] || b[ i ]) === a ) u = b[ i ];
			u !== a && ! _moduleCache[ b[ i ] ] && (d.push( b[ i ] ), e.push( u ));
		}
		if ( d.length ) {
			_loadJs( e, function( g ) {
				for ( var i = 0; i < g.length; i ++ ) {
					! _moduleCache[ d[ i ] ] && _onModuleLoad( d[ i ], e[ i ], g[ i ] );
				}
				f && exe( b, f );
			}, ! s );
		} else {
			f && exe( b, f );
		}
		return _moduleCache[ b[ 0 ] ];
	};
	// require.css(): 装载css
	r.css = function( b, f ) {
		typeof b === _STR && (b = [ b ]);
		for ( var i = 0; c = [], d = [], i < b.length; i ++ )
			b[ i ] = _mod_uri( a, b[ i ], 'css' );
		_loadCss( b, N, f );
	};
	// require.async(): 异步装载js
	r.async = function( b, f ) {
		return r( b, f, T );
	};
	return r;
},
//每个模块的运行环境下都会生成一个Define的实例 /@a -> uri
Define = function( a ) {
	var r = Require( a ),
		// @n -> id, p -> dependents, f -> fn
		b = function( n, p, f ) {
			var m = new Module( a ), u = a;
			if ( arguments.length === 1 )
				f = n;
			else if ( arguments.length === 2 )
				f = p, u = _mod_uri( a, n );
			else { //3
				r( p );
				u = _mod_uri( a, n );
			}
			return _moduleCache[ u ] = typeof f === _FUN ? ( f.call( r, m.exports, m ) || m.exports ) : f;
		};
	// widget模块定义，默认继承 widget 类
	b.widget = function( c, d ) {
		var e = d.Extend, f = d.Prototype || (d.Prototype = {});
		e = d.Extend = ! e ? [ 'widget' ] : ! _isArray( e ) ? [ e ] : e;
		for ( var i = 0; i < e.length; i ++ ) {
			if ( typeof e[ i ] === _STR ) e[ i ] = r( e[ i ] );
		}
		if ( ! d.Const )
			d.Const = function() { e[ 0 ].apply( this, arguments ) };
		return _moduleCache[ _mod_uri( a, c ) ] = _createClass( c, d );
	}
	b.template = function( c, d ) {
		d == N && (d = c, c = N);
		_moduleCache[ c ? _mod_uri( a, (_cfg.template_dir || '') + c ) : a ] = d;
	}
	b.preload = function( c, d ) {
		d == N && (d = c, c = N);
		_moduleCache[ c ? _mod_uri( a, (_cfg.preload_dir || '') + c ) : a ] = d;
	}
	return b;
},
//@a -> id, b -> uri, c -> script text
_onModuleLoad = function( a, b, c ) {
	var m = new Module( b );
	try {
		Function( 'define,require,module,exports', c + '\n//@ sourceURL=' + a ).call( win, Define( b ), Require( b ), m, m.exports );
	} catch( e ) {
		throw new Error( '"' + a + '": ' + e.message );
	}
	if ( ! _moduleCache[ a ] )
		_moduleCache[ a ] = m.exports;
},
// a -> src, b -> callback, c -> sync?
_loadJs = function( a, b, c ) {
	if ( typeof a === _STR )
		a = [ a ];
	var i = a.length, f = i,
		g = function() {
			if( -- f === 0 ) {
				for ( var j = 0, r = []; j < a.length; j ++ )
					r.push( _ajax_cache[ _ajax_url( a[ j ] ) ].response );
				b && b( r );
			}
		};
	while ( i -- )
		$.ajax( { src: a[ i ], sync: c, success: g, cache: T } );
},
// @a -> src, b -> id?, c -> fn?
_loadCss = function( a, b, c ) {
	typeof a === _STR && (a = [ a ]);
	for ( var i = 0, l = a.length, n = l, e; i < l; i ++ ) {
		e = doc.createElement( 'link' );
		e.rel  = 'stylesheet';
		e.href = a[ i ] + _ver;
		b && ($.remove( b[ i ] ), e.id = b[ i ]);
		if ( c ) {
			if ( (br.chm && br.chm < 19) || br.safari ) { // 版本低于19的chrome浏览器，link的onload事件不会触发。借用img的error事件来执行callback
			    var img = doc.createElement( 'img' );
		        img.onerror = function(){ --n === 0 && c() };
		        img.src = a[ i ] + _ver;
		    } else {
		    	c && (e.onload = function() { --n === 0 && c() });
		    }
	    }
		_tags( 'head' )[ 0 ].appendChild( e );
	}
},
_loadStyle = function( a, b ) {
	var c = document.createElement( 'style' );
	c.setAttribute( 'type', 'text/css' );
	if ( b ) {
		$.remove( b );
		c.id = b;
	}
	if( c.styleSheet ) {
		c.styleSheet.cssText = a;
	} else {
	    c.appendChild( document.createTextNode( a ) );
	}
	$.query( 'head' ).append( c );
},

/* 辅助方法 */
_uidCnt = 0,
_guid  = function() { return (_uidCnt ++).toString( 36 ) + ':' },
_arrfn = function( a ) { return Function( 'v,i,r', 'return(' + a + ')' ) },
_fnapply = $.fnapply = function( a, b, c, d ) {
	if ( typeof a === _FUN ) {
		return a.apply( b, d || A );
	} else {
		for ( var i = 0, r = {}, g = c.split(','), l = (d || A).length; i < l; i ++ )
			r[ g[ i ] ] = d[ i ];
		return b && b.isWidget && b.formatJS( a, r );
	}
},
// 获取/设置全局唯一ID
_uid = $.uid = function( o ) {
	if ( o )
		return o.isWidget ? (o.id || (o.id = _guid())) : (o[ _expando ] || (o[ _expando ] = _guid()));
	return _guid();
},
_isNumber = $.isNumber = function( a ) {
	return a != N && ! isNaN( a );
},
_number = $.number = function( a ) {
	var r = typeof a === _STR ? parseFloat( a.replace( ',', '' ) ) : + a;
	return isNaN( r ) ? 0 : r;
},
// 如果 a 的大小在 b 和 c 之间，则返回 a。否则返回 b 或 c
_numRange = $.numRange =  function( a, b, c ) {
	return (b != N && a < b) ? b : (c != N && a > c) ? c : a;
},
// js小数相加运算出现多位小数的解决办法
_numAdd = $.numAdd = function( a, b ) {
	 var c = _strFrom( a + '', '.' ).length, d = _strFrom( b + '', '.' ).length, m = Math.pow( 10, Math.max( c, d ) );
	 return Math.round( (a + b) * m ) / m;			
},
// 给数字加上分隔符  /@a -> str, b -> length?, c -> separator?, d -> rightward?
_numFormat = $.numFormat = function( a, b, c, d ) {
	a = String( a );
	b == N && (b = 3);
	c == N && (c = ',');
	var e = a.replace( /[^.\d]/g, '' ).split( '.' ), s = e[ 0 ], l = s.length, i = d ? 0 : l, t = '', n = a.charAt(0) === '-' ? '-' : '';
	if ( d ) {
		do {
			i += b;
			t += (i < l ? s.substr( i - b, b ) + c : s.substr( i - b ));
		} while ( i < l );
	} else {
		do {
			i -= b;
			t = (i > 0 ? c + s.substr( i, b ) : s.substr( 0, b + i )) + t;
		} while ( i > 0 );
	}
	return n + t + (e.length > 1 ? '.' + _strFrom( a, '.' ).replace( RegExp( '[.' + c + ']', 'g' ), '' ) : '');
},
// 调整小数位数
_numDecimal = $.numDecimal = function( a, b ) {
	if ( ! b ) {
		return a;
	} else if ( b < 0 ) {
		return a;
	} else {
		var f = _strFrom( '' + a, '.' );
		return f ? _strTo( '' + a, '.' ) + '.' + f.slice( 0, b ) : a;
	}
},
_strTrim = $.strTrim = function (a ) {
	return String( a ).replace( /^\s+|\s+$/g, '' );
},
// @a -> str, b -> no html entities?
_strQuot = $.strQuot = function( a, b ) {
	a = String( a );
	b && (a = a.replace( /<[^>]+>/g, '' ));
	return a.replace( /\"/g, '&quot;' );
},
// 在a中取以b开始的字符串(不包括b) /@ c -> last indexOf ?
_strFrom = $.strFrom = function( a, b, c ) {
	var d = c ? a.lastIndexOf( b ) : a.indexOf( b );
	return d < 0 ? '' : a.substr( d + b.length );
},
// 在a中取以b结束的字符串(不包括b) /@ c -> last indexOf ?
_strTo = $.strTo = function( a, b, c ) {
	var d = c ? a.lastIndexOf( b ) : a.indexOf( b );
	return d < 0 ? '' : a.substr( 0, d );
},
// 在a中取以b开始，到以c结束的字符串(不包括b和c)
_strRange = $.strRange = function( a, b, c ) {
	return _strTo( _strFrom( a, b ), c, T );
},
// 数字前加0  /@ a -> num, b -> 补成多少长度的字符串？
_strPad = $.strPad = function( a, b, c ) {
	c = c == N ? '0' : (c + '');
	if ( ! b )
		return a < 10 ? c + a : a;
	a = a + '';
	while ( a.length < b )
		a = c + a;
	return a;
},
// a字符重复b次
_strRepeat = $.strRepeat = function( a, b ) {
	for ( var i = 0, r = a, l = b || 0; i < l; i ++ )
		r += a;
	return r;
},

// 取字符串字节数(一个汉字算双字节)  /@ s -> str, b -> 一个汉字算几个字节？默认2个
_strLen = $.strLen = function( s, b ) {
	if ( ! s ) return 0;
	if ( ! b ) b = _cfg.cn_bytes || 2;
	for ( var i = 0, d = 0, l = s.length; i < l; i ++ )
		d += s.charCodeAt( i ) > 128 ? b : 1;
	return d;
},
// 按照字节数截取字符串 / @a -> str, b -> len, c -> trancateExt 后缀, n -> html entries?
_strSlice = $.strSlice = function( a, b, c, n ) {
	var d = 0, e, f = c ? _strLen( c ) : 0, y = _cfg.cn_bytes || 2, k, i = 0, l = a.length;
	for ( ; i < l; i ++ ) {
		e = a.charCodeAt( i ) > 128 ? y : 1;
		if ( n && a.charAt( i ) === '&' ) {
			if ( k = a.slice( i ).match( /^&(?:#(\d{2,5})|[a-z]{2,});/i ) ) {
				if ( ( k[ 1 ] && k[ 1 ] > 256 ) )
					e = y;
				i += k[ 0 ].length - 1;
			}
		}
		if ( d + e > b - f ) { break; }
		d += e;
		if ( d === b - f ) { i ++; break; }
	}
	if ( i === a.length ) return a;
	if ( c ) {
		e = a.slice( i );
		k = arguments.callee( e, f, F, n );
		return a.slice( 0, i ) + ( e === k ? e : c );
	}
	return a.substr( 0, i );
},
// 对html标签解码
_strUnescape = $.strUnescape = function( s ) {
	return s != N ? (s + '').replace( /&amp;/g, '&' ).replace( /&lt;/g, '<' ).replace( /&gt;/g, '>' ).replace( /&quot;/g, '"' ) : '';
},
// 对html标签编码
_strEscape = $.strEscape = function( s ) {
	return s != N ? (s + '').replace( /&/g, '&amp;' ).replace( /</g, '&lt;' ).replace( />/g, '&gt;' ).replace( /\"/g, '&quot;' ) : '';
},
// 在 a 中查找 b 并给 b 加上标签样式 c /@ a -> str, b -> key, c -> matchLength?, d -> key cls?
_strHighlight = $.strHighlight = function( a, b, c, d ) {
	return (a = a + '').replace( RegExp( '(<[\\/!]?\\w+(?::\\w+)?\\s*(?:[-\\w]+(?:=(\'|")?.*?\\2)?\\s*)*>)|(' + _strSplitword( b, c, T ).join( '|' ) + ')', 'ig' ), function($0, $1, $2, $3) {
	   	return $3 === U ? $1 : '<em class="' + (d || 'f-keyword') + '">' + $3 + '</em>';
	} );
},
// 字符转为regexp支持的模式
_s_regexp = function( a ) {
	return a.replace( /([-.*+?^${}()|[\]\/\\])/g, '\\\$1' );
},
// @a -> key, b -> matchLength, c -> regexp?
_strSplitword = $.strSplitword = function( a, b, c ) {
	var e = [ c ? _s_regexp( a ) : a ], m = a.length;
	if ( b > 0 && m > b ) {
		for ( var j = m - 1; j >= b; j -- ) {
			for ( var i = 0, l = m - j, f; i <= l; i ++ ) {
				f = a.substr( i, j );
				e.push( c ? _s_regexp( f ) : f );
			}
		}
	}
	return e;
},
// 替换带$变量的字符串，如 "a.sp?id=$0" /@a -> url, b -> array/object, c -> urlEncode?
_strFormat = $.strFormat = function( a, b, c ) {
	a = a == N ? '' : '' + a;
	if ( ! b )
		return a;
	if ( _isArray( b ) ) {
		return a.replace( /\$\{?(\d+)\}?/g, function( $0, $1 ) { return b[ $1 ] == N ? '' : c ? _urlEncode( b[ $1 ] ) : b[ $1 ] } );
	} else {
		return a.replace( /\$\{?([a-z_]\w*)\}?/gi, function( $0, $1 ) { return b[ $1 ] == N ? '' : c ? _urlEncode( b[ $1 ] ) : b[ $1 ] } );
	}
},
/*  下面定义ID连接字符串的方法 ( 如: 001,002,003 )  */
// 添加一个ID
_idsAdd = $.idsAdd = function( s, n, p ) {
	if ( ! p ) p = ',';
	return _idsAny( s, n, p ) ? s : ( s ? s + p + n : n );
},
// 删除一个ID
_idsRemove = $.idsRemove = function( s, n, p ) {
	if ( s == N ) return '';
	if ( ! p ) p = ',';
	return ( p + s + p ).replace( p + n + p, p ).slice( 1, -1 );
},
// s是否包含n。如果 n 也是逗号隔开，那么只需n中有匹配到s中的一项即返回true
_idsAny = $.idsAny = function( s, n, p ) {
	if ( ! s ) return F;
	if ( ! n || s == n ) return T;
	if ( ! p ) p = ',';
	if ( (n = String( n )).indexOf( p ) > -1 ) {
		for ( var i = 0, b = n.split( p ), l = b.length; i < l; i ++ )
			if ( (p + s + p).indexOf( p + b[ i ] + p ) > -1 ) return T;
	} else
		return (p + s + p).indexOf( p + n + p ) > -1;
},
// s是否包含n。如果 n 也是逗号隔开，那么只需n中有匹配到s中的一项即返回true
_idsAll = $.idsAll = function( s, n, p ) {
	if ( ! s ) return F;
	if ( ! n || s == n ) return T;
	if ( ! p ) p = ',';
	if ( (n = String( n )).indexOf( p ) > -1 ) {
		for ( var i = 0, b = n.split( p ), l = b.length; i < l; i ++ )
			if ( (p + s + p).indexOf( p + b[ i ] + p ) === -1 ) return F;
		return T;
	} else
		return (p + s + p).indexOf( p + n + p ) > -1;
},
// $.scale的辅助方法
_scaleRange = $.scaleRange = function( a, b ) {
	if ( b != N ) {
		if ( b.min && ! isNaN( b.min ) ) a = Math.max( a, b.min );
		if ( b.max && ! isNaN( b.max ) ) a = Math.min( a, b.max );
	}
	return a;
},
// 把数字a按照数组b切割 / @a -> num, b -> array, c -> cover?(如果切割依据都是数字，那么可能会有剩余。设置此参数为true，剩余的数据平摊到结果中)
// @example: _scale( 100, [ {value:'*',max:10}, {value:60}, {value:'*'} ] )
//   返回: [ 10, 60, 30 ]
// @example: _scale( null, [ '*', '60', '*' ] )
//   返回: [ null, 60, null ]
_scale = $.scale = function( a, b, c ) {
	var d = 0, e = a, l = b.length, i = 0, p, s, v, t = 0, r = Array( l );
	for ( ; i < l; i ++ ) {
		v = typeof b[ i ] === _OBJ ? b[ i ].value : b[ i ];
		if ( v != N && isNaN( v ) ) {
			a == N ? (r[ i ] = N) : v === '*' ? (s = i, t ++) : p = i;
		} else
			d += (r[ i ] = v == N || v < 0 ? N : + v);
	}
	if ( a == N ) return r;
	if ( p !== U ) {
		a = Math.max( 0, a - d );
		var z = 0;
		for ( i = d = 0; i <= p; i ++ ) {
			v = typeof b[ i ] === _OBJ ? b[ i ].value : b[ i ];
			if ( r[ i ] === U && typeof v === _STR && v.indexOf( '%' ) > 0 ) {
				z += (v = parseFloat( v ));
				d += (r[ i ] = _scaleRange( Math.floor( a * v / 100 ), b[ i ] ));
			}
		}
		if ( s === U && z == 100 )
			r[ p ] = _scaleRange( Math.max( 0, r[ p ] + a - d ), b[ i ] );
	}
	if ( s !== U ) {
		a = Math.max( 0, a - d );
		for ( i = d = 0; i <= s; i ++ ) {
			if ( r[ i ] === U ) d += (r[ i ] = _scaleRange( Math.floor( a / t ), b[ i ] ));
		}
		r[ s ] = _scaleRange( Math.max( 0, r[ s ] + a - d ), b[ s ] );
	}
	if ( c && p === U && s === U ) {
		for ( i = 0, l = r.length, d = 0, t = 0; i < l; i ++ ) {
			_isNumber( r[ i ] ) && (d += r[ i ], t ++);
		}
		if ( d < e ) {
			var f = Math.floor( (e - d) / t );
			for ( i = 0; i < l; i ++ )
				_isNumber( r[ i ] ) && (r[ i ] += f);
			for ( i = l; i >= 0; i -- )
				if ( _isNumber( r[ i ] ) ) { (r[ i ] += e - d - (f * t)); break; }
		}
	}
	return r;
},
// 把函数b应用于所有a数组元素 /a -> array, b -> function, c -> break?
_each = $.each = function( a, b, c ) {
	if ( typeof b === _STR ) b = _arrfn( b );
    for( var i = 0, l = a.length; i < l; i ++ )
    	 if( F === b.call( a[ i ], a[ i ], i, a ) ) { if ( c ) break; }
    return a;
},
// 获取b在数组a的位置序号
_arrIndex = $.arrIndex = function( a, b ) {
	if ( a.indexOf ) return a.indexOf( b );
	for ( var i = 0, l = a.length; i < l; i ++ )
		if ( a[ i ] === b ) return i;
	return -1;
},
// b是否存在于数组a中
_inArray = $.inArray = function( a, b ) { return _arrIndex( a, b ) > -1 },
// 是否数组
_isArray = $.isArray = Array.isArray || function( a ) { return O.toString.call( a ) === '[object Array]' },
_arrMake = $.arrMake = function( a ) { return _isArray( a ) ? a : a == N ? [] : (typeof a.length === _NUM) ? A.slice.call( a ) : [ a ] },
// 选择符合条件的元素，返回新数组
_arrSelect = $.arrSelect = function( a, b, c ) {
	if ( typeof b === _STR ) b = _arrfn( b );
    for( var i = 0, l = a.length, r = [], d; i < l; i ++ ) {
    	if ( d = b.call( a[ i ], a[ i ], i, a ) ) r.push( c ? d : a[ i ] );
    }
    return r;
},
// 找到符合条件的元素并返回该元素 /@a -> array, b -> fn, d -> result?
_arrFind = $.arrFind = function( a, b, c ) {
	if ( typeof b === _STR ) b = _arrfn( b );
    for( var i = 0, l = a.length, d; i < l; i ++ )
    	if ( d = b.call( a[ i ], a[ i ], i, a ) ) return c ? d : a[ i ];
},
// 从数组中移除一项
_arrPop = $.arrPop = function( a, b ) {
	if ( a && _arrIndex( a, b ) > -1 ) a.splice( _arrIndex( a, b ), 1 );
},
// 最后一个属性是数组 / a -> obj, b -> target
// example: _jsonArray( obj, target, 'a', 'b' );
// 等同于: target[ 'a' ][ 'b' ].push( obj );
_jsonArray = $.jsonArray = function( a, b ) {
	for ( var i = 2, l = arguments.length - 1; i < l; i ++ )
		b = b[ arguments[ i ] ] || ( b[ arguments[ i ] ] = {} );
	( b[ arguments[ l ] ] || (b[ arguments[ l ] ] = []) ).push( a );
	return b[ arguments[ l ] ];
},
// example: _jsonChain( obj, target, 'a', 'b' );
// 等同于: target[ 'a' ][ 'b' ] = obj;
_jsonChain = $.jsonChain = function( a, b ) {
	for ( var i = 2, l = arguments.length - 1, c = b; i < l; i ++ )
		c = c[ arguments[ i ] ] || (c[ arguments[ i ] ] = {});
	c[ arguments[ l ] ] = a;
	return c;
},
// a拷贝b的属性，并移除b的相同属性c /@ c -> "prop1,prop2...propN"
_jsonCut = $.jsonCut = function( a, b, c ) {
	for ( var i = 0, c = c.split(','), d, l = c.length; i < l; i ++ ) {
		if ( (d = c[ i ]) in b ) {
			a[ d ] = b[ d ];
			delete b[ d ];
		}
	}
	return a;
},

// 把 json 转为字符串 /@ a -> value, b -> replacer, c -> space
_jsonString = $.jsonString = function( a, b, c ) {
	return JSON.stringify( a, b, c );
},
// 把字符串 转为 json /@ a -> str
_jsonParse = $.jsonParse = function( a ) {
	return ! a ? N : JSON.parse( a );
},
// 复制 json
_jsonClone = $.jsonClone = function( a ) {
	return typeof a === _OBJ ? _jsonParse( _jsonString( a ) ) : a;
},
// 一天的毫秒数
_date_D = $.DATE_DAY = 86400000,
// 标准格式化字符串
_date_sf = 'yyyy-mm-dd hh:ii:ss',
// 格式化日期
_dateFormat = $.dateFormat = function( a, b ) {
	if ( typeof a === _STR )
		a = _dateParse( a, b );
	var o = { y : a.getFullYear(), m : a.getMonth(), d : a.getDate(), h : a.getHours(), i : a.getMinutes(), s : a.getSeconds(), w : ( a.getDay() || 7 ) };
	return (b || _date_sf).toLowerCase().replace( 'yyyy' , o.y ).replace( 'yy', o.y % 100 ).replace( 'mm', _strPad( o.m + 1 ) ).replace( 'dd', _strPad( o.d ) ).replace( 'hh', _strPad( o.h ) )
		.replace( 'ii', _strPad( o.i ) ).replace( 'ss', _strPad( o.s ) ).replace( 'm', o.m + 1 ).replace( 'd', o.d ).replace( 'h', o.h ).replace( 'i', o.i ).replace( 's', o.s );
},
// 字串型转为日期型 /@s -> str, f -> format?
_dateParse = $.dateParse = function( s, f ) {
	s = '' + s;
	if ( f ) {
		var g = f.charAt( 0 );
		if ( g == 'm' )
			s = '2017-' + s;
		else if ( g == 'd' )
			s = '2017-03-' + s;
		else if ( g == 'h' )
			s = '2017-03-01 ' + s;
		else if ( g == 'i' )
			s = '2017-03-01 00:' + s;
	}
	var b = s.split( '-' );
	if ( b.length === 1 )
		s += '-03-01';
	else if ( b.length === 2 )
		s += '-01';
	var a = new Date( s.replace( /-/g, '/' ) );
	if ( isNaN( a ) )
		a = new Date( s.split( ' ' )[ 0 ].replace( /-/g, '/' ) );
	return isNaN( a ) ? new Date : a;
},
// 日期增减  /@ a -> date, b -> type enum ( y/m/d/h/i/s ), c -> value
_dateAdd = $.dateAdd = function( a, b, c ) {
	var e = a;
	if ( c ) {
		var d = 'y/m/d/h/i/s'.replace( b, c ).replace( /[a-z]/g, '0' ).split( '/' ),
			e = new Date( a.getFullYear() + parseInt( d[ 0 ] ), a.getMonth() + parseInt( d[ 1 ] ), a.getDate() + parseInt( d[ 2 ] ), a.getHours() + parseInt( d[ 3 ] ), a.getMinutes() + parseInt( d[ 4 ] ), a.getSeconds() + parseInt( d[ 5 ] ) );
		if ( ( b === 'y' || b === 'm' ) && e.getDate() != a.getDate() )
			e = new Date( e.getTime() - e.getDate() * _date_D );
	}
	if ( arguments.length > 3 ) {
		for ( var i = 3; i < arguments.length; i += 2 )
			if ( arguments[ i + 1 ] )
				e = arguments.callee.call( this, e, arguments[ i ], arguments[ i + 1 ] );
	}
	return e;
},
// 取得一个日期对象的week相关信息。返回一个包含四个元素的数组，【 年份，第几周，周一的日期，周日的日期 】
// @a -> date, b -> 周的重心(星期几), c -> 周的第一天(星期几)
_dateWeek = $.dateWeek = function( a, b, c ) {
	if ( b == N )
		b = 1;
	if ( c == N )
		c = 1;
	var w = a.getDay();
	if( !w ) w = 7;
	var	d = new Date( a.getFullYear(), a.getMonth(), a.getDate() - w + b ),
		e = Math.ceil( ( d - new Date( d.getFullYear(), 0, 0 ) ) / _date_D ),
		f = new Date( a.getFullYear(), a.getMonth(), a.getDate() - w + c );
	return [ d.getFullYear(), Math.floor( ( e + 6 ) / 7 ), f, new Date( f.getTime() + ( 7 * _date_D ) - 1 ) ];
},
// 判断一个字符串格式的日期是否正确  /@ a -> date string, b -> date format string
_dateValid = $.dateValid = function( a, b ) {
	a = a.replace( /\b(\d)\b/g, '0$1' );
	var d = _dateParse( a, b ), y = d.getFullYear();
	return y >= (_cfg.min_year || (_cfg.min_year = 1000)) && y <= (_cfg.max_year || (_cfg.max_year = 3000)) && a == _dateFormat( d, b );
},
// url 编码
_urlEncode = $.urlEncode = function( a ) { return a == N ? '' : encodeURIComponent( a ) },
// url 解码
_urlDecode = $.urlDecode = function( a ) { return a == N ? '' : decodeURIComponent( a.replace( /\+/g, ' ' ) ) },
// 替换带$变量的url，如 "a.sp?id=$0" /@a -> url, b -> array/object
_urlFormat = $.urlFormat = function( a, b ) {
	return _strFormat( a, b, T );
},
// 以 a 为基础，解析出 b 的路径
_urlLoc = $.urlLoc = function( a, b ) {
	a = _strTo( a, '/', T );
	return b.indexOf( './' ) === 0 ? _urlLoc( a + '/', b.slice( 2 ) ) : b.indexOf( '../' ) === 0 ? _urlLoc( a, b.slice( 3 ) ) : b.charAt( 0 ) === '/' ? b : (b.indexOf( 'http:' ) === 0 || b.indexOf( 'https:' ) === 0) ? b : a + '/' + b;
},
// @ a-> url, b -> name/object
_urlParam = $.urlParam = function( a, b ) {
	var r = a.split( '#' ), u = r[ 0 ], h = r[ 1 ];
	if ( typeof b === _STR ) {
		return b === '#' ? (h || '') : (u.match( new RegExp('[\\?&]' + b + '=([^&]*)'), 'g' ) || A)[ 1 ];
	} else if ( b && typeof b === _OBJ ) {
		for ( var k in b ) {
			if ( k === '#' ) {
				h = b[ k ];
			} else {
				var c = _urlEncode( b[ k ] ), d = new RegExp( '\\b' + k + '=[^&]*' );
				u = d.test( u ) ? u.replace( d, k + '=' + c ) : u + (u.indexOf( '?' ) < 0 ? '?' : '&') + k + '=' + c;
			}
		}
	} else if ( arguments.length === 1 ) {
		var r = {}, c = u.split( '?' );
		c = c[ c.length - 1 ];
		if ( c ) {
			c = c.split( '&' );
		    for ( var d = 0, e, f, g; d < c.length; d ++ ) {
		        e = c[ d ].indexOf( '=' );
		        if ( e == -1 ) continue;
		        f = c[ d ].substring( 0, e );
		        g = c[ d ].substring( e + 1 );
		        r[ f ] = _urlDecode( g.replace( /\+/g, ' ' ) );
			}
		}
		return r;
	}
	return u + (h ? '#' + h : '');
},
// @a -> fn(得到0-1的参数), b -> milliseconds, c -> times
_ease = $.ease = function( a, b, c ) {
	a( 0 );
	if ( b == N || b === T || b === 'fast' )
		b = 200;
	else if ( b === 'normal' )
		b = 400;
	else if ( b === 'slow' )
		b = 900;
	if ( c == N )
		c = Math.ceil( b / 10 );
	var p  = Math.ceil( b / c ),
		cr = 0,
		d1 = new Date().getTime(),
		it = setInterval( function() {
			var d2 = new Date().getTime(),
				ri = d2 - d1,
				st = Math.floor( ri / p );
			if ( cr != st ) {
				if ( st < c ) {
					a( ( - Math.cos( ( st / c ) * Math.PI ) / 2 ) + 0.5 );
				} else {
					clearInterval( it );
					a( 1 );
				}
				cr = st;
			}
		}, Math.max( p - 10, 5 ) );
	return it;
},
// 读写cookie  /@ a -> cookie name, b -> cookie value, c -> expireDay, d -> sPath
_cookie = $.cookie = function( a, b, c, d ) {
	if ( arguments.length === 1 )
		return _cookie_get( a );
	if ( b == N ) // 删除
		_cookie_set( a, '', -1 );
	else
		_cookie_set( a, b, c, d );
},
// 写cookie  /@ a -> cookie name, b -> cookie value, c -> expireDay, d -> sPath
_cookie_set = function( a, b, c, d ) {
	var e = '';
	if ( c ) {
		var g = new Date();
		g.setTime( g.getTime() + ( c * 24 * 60 * 60 * 1000 ) );
		e = ';expires=' + g.toGMTString();
	}
	doc.cookie = a + '=' + _urlEncode( b ) + e + ( d ? ( ';path=' + d ) : '' );
},
// 读cookie  /@ a -> cookie name
_cookie_get = function( a ) {
	var s  = a + '=',
		c = document.cookie.split( ';' );
	for( var i = 0; i < c.length; i ++ ) {
		var d = c[ i ];
		while( d.charAt( 0 ) === ' ' )
			d = d.substring( 1, d.length );
		if( d.indexOf( s ) === 0 )
			return _urlDecode( d.substring( s.length, d.length ) );
	}
	return N;
},

/* dom 方法 */
// 获取元素所在的window对象
_win = function( o ) { return o && (o = (o.ownerDocument || o)) ? (o.defaultView || o.parentWindow || o) : win },
// 往 document.body 内写入内容
_db = $.db = function( a, b ) { return a ? _append( b || doc.body, a ) : doc.body },
// 简写 getElementsByTagName
_tags = $.tags = function( a ) {
	for ( var i = 0, c = doc.getElementsByTagName( a ), r = [], l = c.length; i < l; i ++ ) r.push( c[ i ] );
	return r;
},
// 创建一个div /@ s -> html
_div = function( s ) {
	var o = doc.createElement( 'div' );
	if ( s ) o.innerHTML = s;
	try { return o } catch( e ) { return F }
	finally { o = N }
},
// 创建一个fragment, 把内容放进去
_frag = $.frag = function( s ) {
	var d = typeof s === _STR ? _div( s ) : s, f = doc.createDocumentFragment();
	while ( d.firstChild )
		f.appendChild( d.firstChild );
	try { return f } catch( e ) { return F }
	finally { d = f = N }
},
_css_camelize = (function() {
	var a = _div( F ), b;
	a.style.cssText = 'float:left';
	b = { 'float' : a.style.cssFloat ? 'cssFloat' : 'styleFloat' };
	return function( s ) {
		return b[ s ] || s.replace( /-./g, function( $0 ) { return $0.charAt(1).toUpperCase() } );
	}
})(),
/*
  @example:
    var wd = _css( oDiv, 'width' );
  @example:
    _css( oDiv, 'width', 100, 'height', 100 );
  @example:
    _css( oDiv, { width: 100, height: 100 } );
*/
_css = $.css = function( o, s, n ) {
	if ( ! o || ! s )
		return N;
	if ( n != N ) {
		for ( var i = 1, l = arguments.length; i < l; i += 2 )
			_set_style( o, arguments[ i ], arguments[ i + 1 ] );
	} else {
		if ( typeof s === _STR ) {
			if ( s.indexOf( ':' ) > 0 ) {
				for ( var i = 0, b = s.split( ';' ), l = b.length, k, v; i < l; i ++ )
					(k = $.strTo( b[ i ], ':' )) && (v = $.strFrom( b[ i ], ':' )) && _set_style( o, k, v );
			} else
				return o.style[ s ] || o.currentStyle[ s ];
		}
		for ( var i in s ) _set_style( o, i, s[ i ] );
	}
},
// @o -> el, n -> name, v -> value
_set_style = function( o, n, v ) {
	// 如属性名前面带 += 号，则执行 += 操作
	if ( typeof v === _STR && v.charAt( 1 ) === '=' ) {
		var	n = _css_camelize( n ),
			c = o.style[ n ] || o.currentStyle[ n ],
			v = _number( v.replace( '=', '' ) );
		if ( n === 'width' || n === 'height' ) {
			c = _number( c && c !== 'auto' ? c : n.charAt( 0 ) === 'w' ? _boxwd( o, o.offsetWidth ) : _boxht( o, o.offsetHeight ) );
			if ( c < 0 ) c = 0;
		} else
			c = _number( c );
		o.style[ n ] = ( c + v ) + ( n === 'zIndex' ? 0 : 'px' );
	} else {
		n = _css_camelize( n );
		if ( n === 'width' || n === 'height' )
			v = Math.max( _number( v ), 0 );
		if ( v && !isNaN( v ) && n !== 'zIndex' )
			v += 'px';
		o.style[ n ] = v;
	}
	return o;
},
// 以IeBox模式计算盒模型的应有宽高 / o -> HTML element, w -> width(对象o的期望宽度)
_boxwd = $.boxwd = function( o, w ) {
	w = w == N ? o.offsetWidth : parseFloat( w );
	var c = o.currentStyle, ar = [ c.borderLeftWidth, c.borderRightWidth, c.paddingLeft, c.paddingRight ];
	for( var i = 0, n; i < 4; i ++ ) {
		n = parseInt( ar[ i ] );
		if ( !isNaN( n ) )
			w = w - n < 0 ? 0 : ( w - n );
	}
	if ( o.colSpan && ie )
		w -= o.colSpan - 1;
	return w;
},
// 计算盒模型高度  /@ o -> HTML element, h -> 期望高度
_boxht = $.boxht = function( o, h ) {
	h = h == N ? o.offsetHeight : parseFloat( h );
	var c = o.currentStyle, ar = [ c.borderTopWidth, c.borderBottomWidth, c.paddingTop, c.paddingBottom ];
	for( var i = 0, n; i < 4; i ++ ) {
		n = parseInt( ar[ i ] );
		if ( ! isNaN( n ) )
			h = h - n < 0 ? 0 : ( h - n );
	}
	return h;
},
_adjacent_where = [ 'afterend', 'afterbegin', 'beforebegin', 'beforeend' ],
_adjacent_query = [ 'after', 'prepend', 'before', 'append' ],
// o -> el, s -> html string/object, r -> where[ `after`, `prepend`, `before`, `append` ]
_html = $.html = function( o, s, r ) {
	if ( r == N ) {
		if ( s == N ) return o.innerHTML;
		try { o.innerHTML = s } catch( ex ) {
			while ( o.firstChild )
				o.removeChild( o.firstChild );
			_html( o, s, 3 );
		}
	} else {
		var b;
		if ( typeof s === _STR ) {
			try {
				o.nodeType === 3 || ie ? $.query( o )[ _adjacent_query[ r ] ]( s ) : o.insertAdjacentHTML( _adjacent_where[ r ], s );
			} catch ( ex ) {
				_html( o, _frag( s ), r );
			}
		} else {
			var p = r % 2 ? o : o.parentNode;
			p && p.insertBefore( s, r === 0 ? o.nextSibling : r === 1 ? o.firstChild : r === 2 ? o : N );
	}
	}
},
_append  = $.append  = function( o, s ) { _html( o, s, 3 ); return o.lastElementChild || o.lastChild },	
_prepend = $.prepend = function( o, s ) { _html( o, s, 1 ); return o.firstElementChild || o.firstChild },
_before  = $.before  = function( o, s ) { _html( o, s, 2 ); return o.previousElementSibling || o.previousSibling },
_after   = $.after   = function( o, s ) { _html( o, s, 0 ); return o.nextElementSibling || o.nextSibling },
_replace = $.replace = function( o, s ) {
	var a = o.nextSibling;
	if ( a ) {
		_rm( o );
		return _before( a, s );
	} else {
		var p = o.parentNode;
		_rm( o );
		return _append( p, s );
	}
},
// 添加一个class  /@ a -> el, b -> className, c -> add?(默认添加)
_classAdd = $.classAdd = function( a, b, c ) {
	var s = a.className;
	if ( b.indexOf( ' ' ) > -1 ) {
		b = _strTrim( b ).split( / +/ );
		for ( var i = 0, l = b.length; i < l; i ++ )
			s = (c === F ? _idsRemove : _idsAdd)( s, b[ i ], ' ' );
	} else
		s = (c === F ? _idsRemove : _idsAdd)( s, b, ' ' );
	a.className = s;
},
// 删除一个class  /@ a -> el, b -> className
_classRemove = $.classRemove = function( a, b ) {
	if ( b.indexOf( ' ' ) > -1 ) {
		_classAdd( a, b, F );
	} else
		a.className = _idsRemove( a.className, b, ' ' );
},
// 是否包含任意一个class(多个是“或”的关系)  /@ a -> el or className
_classAny = $.classAny = function( a, b ) {
	var s = typeof a === _STR ? a : a.className;
	if ( b.indexOf( ' ' ) > -1 ) {
		b = _strTrim( b ).split( / +/ );
		for ( var i = 0, l = b.length; i < l; i ++ )
			if ( _idsAny( s, b[ i ], ' ' ) )
				return i + 1;
	} else
		return _idsAny( s, b, ' ' );
},
// 替换样式 / a -> el, b -> old className, c -> new className
_classReplace = $.classReplace = function( a, b, c ) {
	_classRemove( a, b ), _classAdd( a, c )
},
_rm = $.remove = function( a ) {
	if ( typeof a === _STR ) a = $( a );
	a && a.parentNode && a.parentNode.removeChild( a );
},
_bcr = $.bcr = function( a ) {
	var b = cvs.clientLeft, c = cvs.scrollLeft, d = cvs.clientTop, e = cvs.scrollTop, o = a && a != doc && (a.isWidget ? a.$() : a), r = o && o.getBoundingClientRect();
	return r ? { left : r.left - b + c, top : r.top - d + e, right : r.right - b + c, bottom : r.bottom - d + e, width : r.right - r.left, height : r.bottom - r.top } :
		a == doc ? { left: c, top: e, right: cvs.clientWidth + c, bottom: cvs.clientHeight + e, width: cvs.clientWidth, height: cvs.clientHeight } : N;
},
_offset = function( o ) {
	var a = _bcr( o );
	a.right  = cvs.clientWidth - a.right;
	a.bottom = cvs.clientHeight - a.bottom;
	return a;
},
_arr_comma = function( a ) { return a.split( ',' ) },
_snaptype = {
	h : _arr_comma( '21,12,34,43' ),
	v : _arr_comma( '41,14,32,23' ),
	a : _arr_comma( '41,32,14,23,21,34,12,43,11' )
},
_snapIndent = { h: {'21':-1,'34':-1,'12':1,'43':1}, v: {'14':1,'23':1,'41':-1,'32':-1} },
_snapMag = (function() {
	var r = [ 23,32,12,43,22,33,'rr','lr' ], b = [ 14,23,34,43,33,44,'bb','tb' ], i, o = { pix: { r:{}, b:{} }, mag: { l:{}, r:{}, t:{}, b:{} } };
	i = r.length; while ( i -- ) o.pix.r[ r[ i ] ] = T;
	i = b.length; while ( i -- ) o.pix.b[ b[ i ] ] = T;
	var r = [ 21,34,22,33,'rr','rl' ], b = [ 41,32,44,33,'bt','bb' ], l = [ 12,43,11,44,'lr','ll' ], t = [ 14,23,11,22,'tb','tt' ];
	i = r.length; while ( i -- ) o.mag.r[ r[ i ] ] = T;
	i = b.length; while ( i -- ) o.mag.b[ b[ i ] ] = T;
	i = l.length; while ( i -- ) o.mag.l[ l[ i ] ] = T;
	i = t.length; while ( i -- ) o.mag.t[ t[ i ] ] = T;
	return o;
})(),
// 获取对齐吸附模式的位置参数。元素四个角，左上为1，右上为2，右下为3，左下为4。目标元素在前，浮动元素在后。如"41"代表目标元素的左下角和浮动元素的左上角粘合。
// @ a -> width, b -> height, c -> toObj offset, d -> adsorb type, e - > fit?[是否自动选择位置，让可见区域最大化], f -> indent[缩进多少像素], u -> range?[浮动元素的限定范围元素]
_snap = $.snap = function( a, b, c, d, e, f, u ) {
	if ( ! c )
		c = _bcr( doc );
	else if ( c.nodeType )
		c = _bcr( c );
	else if ( c.isWidget )
		c = _bcr( c.$() );
	else if ( c.originalEvent || c.clientX != N ) {
		c = c.originalEvent || c;
		c = { left: c.clientX, right: c.clientX, top: c.clientY, bottom: c.clientY };
	}
	var t = [], l = [], g, h, k = -1, f = _number( f ), ew = cvs.clientWidth, eh = cvs.clientHeight,
		s = d ? (_snaptype[ d ] || ('' + d).split( ',' )) : _snaptype.a;
	if ( /[1-4]/.test( s[ 0 ] ) ) { // 1-4是边角对齐 .
		for ( var i = 0, o, p, m, n = 0; i < s.length; i ++ ) {
			g = + s[ i ].charAt( 0 ), h = + s[ i ].charAt( 1 );
			t.push( ( g === 1 || g === 2 ? c.top : c.bottom ) - ( h === 3 || h === 4 ? b : 0 ) + (f && (_snapIndent.v[ s[ i ] ] || 0) * f) );
			l.push( ( g === 1 || g === 4 ? c.left : c.right ) - ( h === 2 || h === 3 ? a : 0 ) + (f && (_snapIndent.h[ s[ i ] ] || 0) * f) );
			o = t[ i ] >= 0 && t[ i ] + b <= eh;
			p = l[ i ] + a <= ew && l[ i ] >= 0;
			if ( o && p ) {
				k = i;
				break;
			} else {
				// 计算溢出面积，取最小的那个
				var ot = t[ i ] < 0 ? ( -t[ i ] * a ) : 0,
					or = l[ i ] + a > ew ? (l[ i ] + a - ew) * b : 0,
					ob = t[ i ] + b > eh ? (t[ i ] + b - eh) * a : 0,
					ol = l[ i ] < 0 ? ( -l[ i ] * b ) : 0;
				m = s[ i ] === '14' || s[ i ] === '34' ? ot + or : s[ i ] === '23' || s[ i ] === '43' ? ot + ol : s[ i ] === '32' || s[ i ] === '12' ? ob + ol : ob + or;
				if ( ! n || n > m ) {
					n = m;
					k = i;
				}
			}
		}
	} else { // t r b l 是边线中点对齐 .
		for ( var i = 0, o, p, m, n = 0; i < s.length; i ++ ) {
			g = s[ i ].charAt( 0 ), h = s[ i ].charAt( 1 );
			t.push( ( g === 'r' || g === 'l' || g === 'c' ? ( c.top + c.bottom ) / 2 : ( g === 't' ? c.top + f : c.bottom - f ) ) - ( h === 'r' || h === 'l' || h === 'c' ? b / 2 : ( h === 'b' ? b : 0 ) ) );
			l.push( ( g === 't' || g === 'b' || g === 'c' ? ( c.left + c.right ) / 2 : ( g === 'r' ? c.right - f : c.left + f ) ) - ( h === 't' || h === 'b' || h === 'c' ? a / 2 : ( h === 'r' ? a : 0 ) ) );
			o = t[ i ] >= 0 && t[ i ] + b <= eh;
			p = l[ i ] + a <= ew && l[ i ] >= 0;
			if ( o && p ) {
				k = i;
				break;
			} else if ( o || p ) {
				if ( d ) {
					m = d == _snaptype.v ? ( g == 1 || g == 2 ? - t[ i ] : t[ i ] + b - eh ) : ( g == 1 || g == 4 ? - l[ i ] : l[ i ] + a - ew );
					if ( ! n || n > m ) {
						n = m;
						k = i;
					}
				} else if ( k < 0 )
					k = i;
			}
		}
	}
	if ( k < 0 ) k = 0;
	var y = s[ k ], o = { left : l[ k ], top : t[ k ], right: l[ k ] + a, bottom: eh - (t[ k ] + b), right: ew - (l[ k ] + a), type : y };
	_snapMag.pix.r[ y ] && (o.pix_r = T);
	_snapMag.pix.b[ y ] && (o.pix_b = T);
	_snapMag.mag.r[ y ] && (o.mag_r = T, o.mag = 'r');
	_snapMag.mag.b[ y ] && (o.mag_b = T, o.mag = 'b');
	_snapMag.mag.l[ y ] && (o.mag_l = T, o.mag = 'l');
	_snapMag.mag.t[ y ] && (o.mag_t = T, o.mag = 't');
	if ( e ) { // 自适应位置
		var l = o.left, r = o.right, b = o.bottom, t = o.top;
		if ( o.mag_t || o.mag_b ) {
			if ( o.pix_r )
				r = Math.max( 0, l < 0 ? r + l : r ), l += o.right - r;
			else
				l = Math.max( 0, r < 0 ? r + l : l ), r += o.left - l;
		}
		if ( o.mag_l || o.mag_r ) {
			if ( o.pix_b )
				b = Math.max( 0, t < 0 ? b + t : b ), t += o.bottom - b;
			else
				t = Math.max( 0, b < 0 ? b + t : t ), b += o.top - t;
		}
		o.left = l, o.right = r, o.top = t, o.bottom = b;
	}
	if ( d === 'cc' && o.top < 0 )
		o.top = 0;
	if ( u ) {
		var s = _offset( u );
		o.top -= (s.top + 1);
		o.left -= (s.left + 1);
		o.right -= (s.right + 1);
		o.bottom -= (s.bottom + 1);
	}
	o.width  = a;
	o.height = b;
	o.target = c;
	return o;
},
// @a -> el, b -> snap option
_snapTo = $.snapTo = function( a, b ) {
	var e = b.type;
	if ( e ) {
		if ( b.pix_r ) {
			a.style.right = b.right + 'px';
			a.style.left  = '';
		} else {
			a.style.left  = b.left + 'px';
			a.style.right = '';
		}
		if ( b.pix_b ) {
			a.style.bottom = b.bottom + 'px';
			a.style.top = '';
		} else {
			a.style.top = b.top + 'px';
			a.style.bottom = '';
		}
	} else
		_css( a, b );
},
// a -> el, b -> event type, c -> fn, d -> T(attach),F(detach)
_attach = $.attach = function( a, b, c, d ) {
	if ( d === F )
		return _detach( a, b, c );
	if ( ~b.indexOf( ' ' ) ) {
		for ( var i = 0, d = b.split( ' ' ); i < d.length; i ++ )
			d[ i ] && _attach( a, d[ i ], c );
	}
	ie ? a.attachEvent( 'on' + b, c ) : a.addEventListener( b, c, F );
},
// a -> el, b -> event type, c -> fn
_detach = $.detach = function( a, b, c, d ) {
	if ( ~b.indexOf( ' ' ) ) {
		for ( var i = 0, d = b.split( ' ' ); i < d.length; i ++ )
			d[ i ] && _detach( a, d[ i ], c );
	}
	ie ? a.detachEvent( 'on' + b, c ) : a.removeEventListener( b, c, F );
},
_stop = $.stop = function( a ) {
	if ( a || ( a = win.event ) )
		a.stopPropagation ? (a.stopPropagation(), a.preventDefault()) : (a.cancelBubble = T, a.returnValue = F);
},
_cancel = $.cancel = function( a ) {
	if ( a || ( a = win.event ) )
		a.stopPropagation ? a.stopPropagation() : (a.cancelBubble = T);
},
// 记录鼠标事件发生的坐标
_event_click = { click: T, dblclick: T, contextmenu: T, mousedown: T, mouseup: T },
_point = $.point = function( e ) {
	if( (e || (e = window.event)) && _event_click[ e.type ] ) {
		_point.srcElement = e.srcElement, _point.offsetX = e.offsetX, _point.offsetY = e.offsetY, _point.clientX = e.clientX, _point.clientY = e.clientY;
	}
},
_rngSelection = $.rngSelection = (function() {
	return win.getSelection ? function() { var s = win.getSelection(); return s.rangeCount && s.getRangeAt( 0 ); } : function() { return doc.selection.createRange(); };
})(),
_rngElement = $.rngElement = (function() {
	return win.getSelection ? function() { var s = _rngSelection(); return s && s.startContainer.parentNode; } : function() { return doc.selection.createRange().parentElement(); };
})(),
_rngCursor = $.rngCursor = function( a, b ) {
	var r = a;
	if ( win.getSelection ) {
		if ( typeof a.selectionStart === _NUM && typeof a.selectionEnd === _NUM ) {
			a.selectionStart = a.selectionEnd = b != N ? b : a.value.length; 
		} else {
			var s = win.getSelection();
			if ( a.nodeType === 1 )
				(r = doc.createRange()).selectNodeContents( a );
			if ( b != N )
				r.movePoint( b );
			s.removeAllRanges();
			s.addRange( r );
			s.collapseToEnd();
		}
	} else {
		if ( a.nodeType === 1 )
			a.createTextRange ? (r = a.createTextRange()) : (r = doc.body.createTextRange()).moveToElementText( a );
		b != N && r.moveStart( 'character', b );
		r.collapse( b != N );
		r.select();
	}
	return r;
},
_rngCursorOffset = $.rngCursorOffset = function() {
	var n = _rngSelection(), c = n.startOffset;
	if ( c != N )
		return c;
	var r = doc.body.createTextRange();
	r.moveToElementText( n.parentElement() );
	r.setEndPoint( 'EndToStart', n );
	return _rngText( r ).length;
},
// @a ->range, b -> text?
_rngText = $.rngText = function( a, b ) {
	if ( a == N )
		a = _rngSelection();
	if ( b != N ) {
		if ( a.deleteContents ) {
			a.deleteContents();
			a.insertNode( doc.createTextNode( b ) );
		} else {
			a.text = b;
		}
	}
	return a.text === U ? a.toString() : a.text;
},
_xmlParse = $.xmlParse = function( a ) {
	var d = 'ActiveXObject' in win ? new ActiveXObject( 'MSXML2.DOMDocument' ) : doc.implementation.createDocument( '', '', N );
	if ( 'setProperty' in d )
		d.setProperty( 'SelectionLanguage', 'XPath' );
	if ( a ) {
		d.loadXML( a );
		return d.documentElement;
	}
	return d;
},
_xmlQuery = $.xmlQuery = function( a, b ) { return a.selectSingleNode( b ) },
_xmlQueryAll = $.xmlQueryAll = function( a, b ) { return a.selectNodes( b ) },

_EventUser = {},
_event_remove = function( k ) {
	if ( k.fn ) {
		_arrPop( _EventUser[ k.id ][ k.type ], k );
		k.type = k.fn = k.pvdr = k.args = N;
	}
},
_event_name = function( a ) {
	return a.indexOf( '.' ) > -1 ? _strTo( a, '.' ) : a;
},
_event_space = function( a ) {
	return a.indexOf( '.' ) > -1 ? '.' + _strFrom( a, '.' ).split( '.' ).sort().join( '.' ) + '.' : '';
},
_event_handlers = function( a, b ) {
	var c = _EventUser[ _uid( a ) ];
	if ( ! c ) return;
	var type = _event_name( b ), ns = _event_space( b );
	if ( type ) {
		return c[ type ] && ( ns ? _arrSelect( c[ type ], 'v.ns.indexOf("' + ns + '")>-1' ) : c[ type ] );
	}
	var r = [];
	for ( type in c ) {
		r.push.apply( r, _arrSelect( c[ type ], 'v.ns.indexOf("' + ns + '")>-1' ) );
	}
	return r;
},
/* `Event` */
_Event = $.Event = _createClass( {
	Const: function() {},
	Prototype: {
		// type -> event type, fn -> fn, pvdr -> context, one -> exec once [T/F]
		addEvent: function( type, fn, pvdr, one ) {
			var k = { id: _uid( this ), type: _event_name( type ), ns: _event_space( type ),
					fn: fn, pvdr: pvdr, one: one };
			_jsonArray( k, _EventUser, k.id, k.type ).ori = T;
			return this;
		},
		addEventOnce: function( type, fn, pvdr ) {
			return this.addEvent( type, fn, pvdr, T );
		},
		// e -> event, r -> args[array]
		fireEvent: function( e, r ) {
			var a = _event_handlers( this, e.type || e );
			if ( a ) {
				for ( var i = 0, k, l = a.length; i < l; i ++ ) {
					if ( (k = a[ i ]) && k.fn ) {
						k.fn.apply( k.pvdr || this, r ? [ e ].concat( r ) : [ e ] );
						if ( k.one ) {
							_event_remove( k );
							a.ori && (i --, l --);
						}
					}
				}
			}
			return this;
		},
		// a -> event type, b -> fn, c -> pvdr
		removeEvent: function( a, b, c ) {
			if ( a ) {
				var d = _event_handlers( this, a );
				if ( d ) {
					var i = d.length;
					while ( i -- )
						if ( b ? (d[ i ].fn === b && (! c || c === d[ i ].pvdr)) : c ? c === d[ i ].pvdr : T ) _event_remove( d[ i ] );
				}
			} else {
				var c = _EventUser[ _uid( this ) ], i;
				for ( i in c ) this.removeEvent( i );
				delete _EventUser[ _uid( this ) ];
			}
			return this;
		}
	}
} ),

/* draggable */
_drag_cache = {},
_drop_cache = {},
Drag = _createClass( {
	// @a -> widget, b -> option
	Const: function( a, b ) {
		this.wg  = a;
		this.cst = b;
		_drag_cache[ a.id ] = this;
	}
} ),
Drop = _createClass( {
	// @a -> widget, b -> option
	Const: function( a, b ) {
		this.wg  = a;
		this.cst = b;
		_drop_cache[ a.id ] = this;
	}
} );
// @a -> widget, b -> option
$.draggable = function( a, b ) {
	if ( _drag_cache[ a.id ] )
		return;
	b = b || {};
	new Drag( a, b );
	$.query( a.$() ).on( 'mousedown', function( e ) {
		var ix = e.pageX, iy = e.pageY, sn, t, h, p, q, d = '_dndhelper', g = d + ':g',
			dp = function( c ) {
				return c.dnd_wid ? $.all[ c.dnd_wid ] : ((c = $.widget( c )) && c.closest( function() { var d = _drop_cache[ this.id ]; return d && (!d.cst.scope || !b.scope || _idsAny( d.cst.scope, b.scope )); } ))
			};
		$.moveup( function( e ) {
			var x = e.pageX, y = e.pageY, m = e.srcElement;
			// 鼠标按下并移动5像素后才能触发拖动，避免误操作
			! t && (Math.abs(x - ix) >= 5 || Math.abs(y - iy) >= 5) && (t = _db( '<div style="position:absolute;background:#f2f2f2;padding:2px;border:1px dashed #ddd;opacity:.6;z-index:2">' + (a.attr( 'text' ) || '&nbsp;') + '</div>' ));
			if ( t ) {
				t.style.left = (e.pageX + 7) + 'px';
				t.style.top  = (e.pageY + 7) + 'px';
				if ( p = dp( m ) ) {
					if ( p == a || a.contains( p ) ) {
						_classAdd( p.$(), 'f-dnd-notallowed' );
					} else {
						e.runType = 'dnd_over';
						p.triggerListener( e );
						if ( _drop_cache[ p.id ].cst.sort ) {
							var c = $.bcr( p.$() ), f = p.type == 'tr' ? '<tr id=' + d + ' class=f-dnd-helper><td id=' + g + ' class=_g colspan=' + p.rootNode.colgrps[ 0 ].length + '></tr>' : '<div id=' + d + ' class=f-dnd-helper><div id=' + g + ' class=_g></div></div>';
							if ( c.top < y && y - c.top <= 5 ) $.query( p.$() )[ sn = 'before' ]( $( d ) || f );
							else if ( c.bottom > y && c.bottom - y <= 5 ) $.query( p.$() )[ sn = 'after' ]( $( d ) || f );
							else if ( ! $( g ) || $( g ) != m )
								sn = N;
							if ( $( d ) ) {
								$( g ).dnd_wid = p.id;
								$( d ).style.display = sn ? '' : 'none';
							}
							p.trigger( 'dnd_sort', h );
						}
					}
					q = p;
				} else {
					_rm( d );
					if ( q ) {
						q.triggerListener( 'dnd_out' );
						q = N;
					}
				}
			}
		}, function( e ) {
			if ( t ) {
				if ( (p = dp( e.srcElement )) && p != a && ! a.contains( p ) ) {
					if ( (sn == 'before' && p == a.next()) || (sn == 'after' && p == a.prev()) || (!sn && p == a.parentNode) ) {
						$.alert( $.loc.tree_movefail1 );
					} else
						_drop_cache[ p.id ].cst.drop && _drop_cache[ p.id ].cst.drop.call( p, e, { draggable: a, droppable: $.widget( e.srcElement ), type: sn || 'append' } );
				}
				_rm( t ), _rm( '_dndhelper' ), $.query( '.f-dnd-notallowed' ).removeClass( 'f-dnd-notallowed' );
			}
		} );
	} );
}

$.droppable = function( a, b ) {
	if ( _drop_cache[ a.id ] )
		return;
	new Drop( a, b || {} );
}

var
_ajax_xhr = (function() {
	var a = function() { return new XMLHttpRequest() },
		b = function() { return new ActiveXObject( 'MSXML2.XMLHTTP' ) },
		c = function() { return new ActiveXObject( 'Microsoft.XMLHTTP' ) };
	// 有些ie浏览器的 XMLHttpRequest 实例化后不能调用方法，需要先试试open方法能不能用
	try { a().open( 'GET', '/', T ); return a; } catch( e ) {}
	try { b(); return b; } catch( e ) {}
	try { c(); return c; } catch( e ) {}
	$.winbox( 'Cannot create XMLHTTP object!' );
})(),
_ajax_url = function( a ) {
	return a.indexOf( './' ) == 0 || a.indexOf( '../' ) == 0 ? _urlLoc( _path, a ) : a.indexOf( 'http://' ) == 0 || a.indexOf( 'https://' ) == 0 ? a : (_cfg.server || '') + a;
},
_ajax_data = function( e ) {
	if ( e && typeof e === _OBJ ) {
		var s = [], i;
		for ( i in e ) {
			if ( _isArray( e[ i ] ) ) {
				for ( var j = 0, b = e[ i ], l = b.length; j < l; j ++ )
					s.push( i + '=' + _urlEncode( b[ j ] ) );
			} else
				s.push( i + '=' + _urlEncode( e[ i ] ) );
		}
		e = s.join( '&' );
	}
	return e;
},
_ajax_httpmode = location.protocol === 'http:' || location.protocol === 'https:',
_ajax_cntp  = 'application/x-www-form-urlencoded; charset=UTF-8',
_ajax_ifmod = 'Thu, 01 Jan 1970 00:00:00 GMT',
_ajax_contexts = {},
_ajax_cache   = {},

/*! `Ajax` */
Ajax = _createClass( {
/* @ a->src, b->callback, c->context, d->sync?, e->data, f->error[false:ignore error,fn:callback], g->data type(text,xml,json)? */
	Const: function( a, b, c, d, e, f, g ) {
		var x = typeof a === _STR ? { src: a, success: b, context: c, sync: d, data: e, error: f } : a;
		!x.dataType && (x.dataType = g);
		(this.x = x).cache ? this.sendCache() : this.send();
	},
	Extend: _Event,
	Prototype: {
		sendCache: function() {
			var x = this.x, u = _ajax_url( x.src ), c = _ajax_cache[ x.src ];
			if ( c ) {
				if ( c.response != N ) {
					x.success && x.success.call( x.context, c.response );
				} else if ( c.errorCode ) {
					x.error && _fnapply( x.error, x.context, '$ajax', [ self ] );
				} else {
					c.addEvent( 'cache', this.sendCache, this );
				}
			} else {
				_ajax_cache[ u ] = this;
				this.send();
			}
		},
		send: function() {
			var x = this.x, a = _ajax_url( x.src ), b = x.success, c = x.context, d = _ajax_data( _cfg.ajax_data ), e = _ajax_data( x.data ),
				f = x.error != N ? x.error : _cfg.src_error, g = x.dataType || 'text', u = a, l, i, self = this;
			d && e ? (e = d + '&' + e) : (d && (e = d));
			// get url超过长度则转为post
			if ( ( a.length > 2000 && a.indexOf( '?' ) > 0 ) ) {
				e = (e ? e + '&' : '') + _strFrom( a, '?' );
				u = _strTo( a, '?' );
			}
			if ( x.base || _path )
				u = _urlLoc( x.base || _path, u );
			(l = _ajax_xhr()).open( e ? 'POST' : 'GET', u, ! x.sync );
			this.request = l;
			if ( x.beforesend && _fnapply( x.beforesend, c, '$ajax', [ self ] ) === F )
				return x.complete && x.complete.call( c, N, self );
			if ( g === 'xml' && br.ie10 )
				l.responseType = 'msxml-document';
			e && l.setRequestHeader( 'Content-Type', _ajax_cntp );
			l.setRequestHeader( 'If-Modified-Since', _ajax_ifmod );
			l.setRequestHeader( 'x-requested-with',  _expando );
			for ( i in x.headers )
				l.setRequestHeader( i, x.headers[ i ] );
			function _onchange() {
				if ( l.readyState === 4 && ! self._aborted ) { // ie9下执行abort()不会终止onchange进程并且读取status属性报错，因此加上_aborted参数协助判断
				    var m, r;
				    if ( c ) {
				    	//delete self.request;
				    	_arrPop( _ajax_contexts[ _uid( c ) ], self );
				    	if ( c._disposed )
				    		return;
				    }
				    if ( l.status < 400 ) {
				    	if ( g === 'json' ) {
							try { eval( 'm=' + l.responseText ) } catch( ex ) {	r = g; }
				    	} else if ( g === 'xml' ) {
							if ( m = l.responseXML.documentElement )
								('setProperty' in l.responseXML) && l.responseXML.setProperty( 'SelectionLanguage', 'XPath' );
							else
								r = g;
						} else
							m = l.responseText;
					} else
						r = g;
			        if ( r ) {
			        	self.errorCode = l.status;
						if ( f !== F && (_ajax_httpmode || l.status) ) {
							typeof f === _FUN && (f = _fnapply( f, c, '$ajax', [ self ] ));
							if ( f !== F ) {
								var s = 'ajax ' + l.status + ': ' + a;
								$.alert( _cfg.debug ? _strEscape( s ) + '\n\n' + ($.loc ? ($.loc.ajax[ l.status ] || $.loc.ajax[ r ] || r + ' error') : r + ' error') :
									$.loc ? $.loc.ps( l.status > 600 ? $.loc.internet_error : $.loc.server_error, l.status, ' data-title="' + _strEscape( s ) + '" onmouseover=dfish.tip(this)' ) : s );
								win.console && console.error( s + ((r = l.responseText) ? '\n' + r : '') );
							}
						}
				    } else {
				    	var t = x.filter;
				    	t && (m = _fnapply( t, c, '$response,$ajax', [ m, self ] ));
				    	self.response = m;
						(m || (g === 'text' && m == '')) && b && b.call( c, m, self );
						_ajax_cache[ a ] === self && self.fireEvent( 'cache' );
					}
					x.complete && x.complete.call( c, m, self );
				}
			}
			l.onreadystatechange = _onchange;
			if ( x.sync ) {
				l.send( e );
			} else {
				var p = _ajax_paused;
				this._timer = setTimeout( function() {
					if ( l.readyState > 0 ) _ajax_paused && ! p ? _ajax_add_pause( function() { l.send( e ) } ) : l.send( e );
				} );
				c && _jsonArray( this, _ajax_contexts, _uid( c ) );
			}
		},
		dispose: function() {
			$.ajaxAbort( this );
			delete _ajax_cache[ this.x.src ];
		}
		
	}
} ),
_ajax_paused    = F,
_ajax_pause_hdl = [],
_ajax_add_pause = function( a ) { _ajax_pause_hdl.push( a ) },
// 暂停目前为止的ajax请求
// 调此方法之后的ajax请求不受影响
_ajax_pause = function() { _ajax_paused = T },
_ajax_play  = function() {
	_each( _ajax_pause_hdl, 'setTimeout(v[i],i*5)' );
	_ajax_pause_hdl.length = 0;
	_ajax_paused = F;
};

// a -> url, b -> callback, c -> context, d -> sync?, e -> data, f -> error hdl, g -> type
$.ajax = function( a, b, c, d, e, f, g ) {
	return new Ajax( a, b, c, d, e, f, g );
}
// a -> url, b -> callback, c -> context, d -> sync?, e -> data, f -> error hdl
$.ajaxXML = function( a, b, c, d, e, f ) {
	return new Ajax( a, b, c, d, e, f, 'xml' );
}
// a -> url, b -> callback, c -> context, d -> sync?, e -> data, f -> error hdl
$.ajaxJSON = function( a, b, c, d, e, f, g ) {
	return new Ajax( a, b, c, d, e, f, 'json' );
}
$.ajaxAbort = function( a ) {
	var b = _uid( a ), c = _ajax_contexts[ b ];
	if ( c ) {
		var i = c.length;
		while ( i -- ) {
			 (c[ i ]._aborted = T), c[ i ].request.abort(), clearTimeout( c[ i ]._timer );
		}
		c.length = 0;
		delete _ajax_contexts[ b ];
	}
}
$.ajaxClean = function( a ) {
	delete _ajax_cache[ a ];
}
// 装载并运行script。这里js的运行环境是window /@a -> src, b -> callback
$.script = function( a, b ) {
	_loadJs( a, function( d ) {
		for ( var i = 0; i < d.length; i ++ )
			d[ i ] && (win.execScript ? execScript( d[ i ] ) : win.eval( d[ i ] ));
		b && b();
	}, ! b );
}

// 浏览器JS兼容
function _compatJS() {
	var u = _lib + 'cp/';
	! win.JSON && $.script( u + 'js-json.js' );
	'a'.split( /a/ ).length === 0 && $.script( u + 'js-split.js' );
}

// 浏览器DOM兼容
function _compatDOM() {
	br.mobile ? _compatDOMMobile() : _compatDOMPC();
	var tmp;
	if ( !('ActiveXObject' in win) ) {
		XMLDocument.prototype.loadXML = function( a ) {
			var d = (new DOMParser()).parseFromString( a, 'text/xml' );
			while ( this.hasChildNodes() )
				this.removeChild( this.lastChild );
			for ( var i = 0; i < d.childNodes.length; i ++ ) {
				this.appendChild( this.importNode( d.childNodes[ i ], T ) );
			}
		};
		Element.prototype.selectNodes =	function( s ) {
			var d = this.ownerDocument, k = d.evaluate( s, this, d.createNSResolver( this ), 5, N ), r = [], e;
			while ( e = k.iterateNext() ) r.push( e );
			return r;
		};
		Element.prototype.selectSingleNode = function( s ) { var d = this.ownerDocument; return d.evaluate( s, this, d.createNSResolver( this ), 9, N ).singleNodeValue };
		Element.prototype.__defineGetter__( 'xml', tmp = function() { return (new XMLSerializer()).serializeToString( this ) } );
		XMLDocument.prototype.__defineGetter__( 'xml', tmp );
	}
	// 增加 event 的支持
	if ( win.dispatchEvent ) {
		_attach( win, 'eventemu', function( e ) {
			if ( ! e.srcElement ) {
				var S = function( n ) { while (n && n.nodeType !== 1) n = n.parentNode; return n };
				Event.prototype.__defineGetter__( 'srcElement',  function() { return S( this.target ) } );
				Event.prototype.__defineGetter__( 'fromElement', function() { return S( this.type === 'mouseover' ? this.relatedTarget : this.type === 'mouseout' ? this.target : U ) } );
				Event.prototype.__defineGetter__( 'toElement',   function() { return S( this.type === 'mouseout' ? this.relatedTarget : this.type === 'mouseover' ? this.target : U ) } );
			}
			if ( ! win.event ) {
				for ( var i in $.white_events )
					doc.addEventListener( $.white_events[ i ], function( e ) { win.event = e; }, T );
			}
		} );
		(tmp = doc.createEvent( 'HTMLEvents' )).initEvent( 'eventemu', T, T );
		win.dispatchEvent( tmp );
	}	
}
function _compatDOMPC() {
	var tmp;
	if ( window.Range && ! Range.prototype.movePoint ) {
		if ( Range.prototype.__defineGetter__ ) {
			Range.prototype.__defineGetter__( 'text', function() { return this.toString() } );
			Range.prototype.__defineSetter__( 'text', function( a ) { this.deleteContents(); this.insertNode( doc.createTextNode( a ) ); } );
		}
		Range.prototype.movePoint = function( a, b ) {
			var c = this.startContainer.firstChild || this.startContainer, d, i = a, l = 0, b = b == N ? a : b;
			do {
				this.selectNodeContents( c );
				l = this.toString().length;
				i -= l;
			} while ( i > 0 && ( c = c.nextSibling ) );
			c = d = this.startContainer;
			i += l;
			var j = b - a + i, l = 0;
			do {
				this.selectNodeContents( d );
				l = this.toString().length;
				j -= l;
			} while ( j > 0 && ( d = d.nextSibling ) );
			this.setStart( c.firstChild || c, i );
			if ( d ) {
				var e = d;
				while ( e.firstChild ) e = e.firstChild;
				this.setEnd( e, l + j );
			}
		};
		Range.prototype.select = function() {
			var s = window.getSelection();
			s.removeAllRanges();
			s.addRange( this );
		};
		Range.prototype.parentElement = function() {
			return this.startContainer.parentNode;
		};
		Range.prototype.moveToElementText = Range.prototype.selectNodeContents;
	}
	(tmp = doc.createElement( 'div' )).innerHTML = '1';
	// for safari
	if ( ! tmp.innerText ) {
		HTMLElement.prototype.__defineSetter__( 'innerText', function( s ) { return this.textContent = s } );
		HTMLElement.prototype.__defineGetter__( 'innerText', function() { return this.textContent } );
	}
	if ( ! tmp.insertAdjacentHTML ) {
		HTMLElement.prototype.insertAdjacentHTML = function(sWhere, sHTML) {
			var r = this.ownerDocument.createRange();
			switch ((sWhere + '').toLowerCase()) {
				case 'beforebegin':
					r.setStartBefore(this);
					this.parentNode.insertBefore(r.createContextualFragment(sHTML), this);
					break;
				case 'afterbegin':
					r.selectNodeContents(this);
					r.collapse(true);
					this.insertBefore(r.createContextualFragment(sHTML), this.firstChild);
					break;
				case 'beforeend':
					r.selectNodeContents(this);
					r.collapse(false);
					this.appendChild(r.createContextualFragment(sHTML));
					break;
				case 'afterend':
					r.setStartAfter(this);
					this.parentNode.insertBefore(r.createContextualFragment(sHTML), this.nextSibling);
					break;
			}
		};
		HTMLElement.prototype.__defineGetter__('canHaveChildren', function() {
			switch( this.tagName ) {
				case "AREA": case "BASE": case "BASEFONT": case "COL": case "FRAME": case "HR": case "IMG": case "BR": case "INPUT": case "ISINDEX": case "LINK": case "META": case "PARAM": return F;
			}
			return T;
		});
		HTMLElement.prototype.__defineGetter__( 'outerHTML', function () {
			var attr, attrs = this.attributes, str = "<" + this.tagName;
			for ( var i = 0; i < attrs.length; i ++ ) {
				attr = attrs[ i ];
				if ( attr.specified )
					str += " " + attr.name + '="' + attr.value + '"';
			}
			if ( ! this.canHaveChildren )
				return str + ">";
			return str + ">" + this.innerHTML + "</" + this.tagName + ">";
		});
	}
	if ( ! tmp.currentStyle ) {
		HTMLElement.prototype.__defineGetter__( 'currentStyle', function() { return this.ownerDocument.defaultView.getComputedStyle( this, N ) } );
	}
	_rm( tmp );
	// 检测浏览器自带滚动条的宽度
	br.chdiv( 'f-scroll-overflow', function() { br.scroll = 50 - this.clientWidth; } );
}
function _compatDOMMobile() {
	// 实现tap事件
	var n, t, tmp;
	$.query( doc ).on( 'touchstart', function( e ) {
	    n = e; t = T;
	}).on( 'touchmove', function() {
	    t = F;
	}).on( 'touchend', function( e ) {
	    if ( t ) {
	    	n.type = 'tap';
	    	$.query( e.target ).trigger( n ); //实现这个代理event.type的功能，要修改jquery源码的trigger方法
	    }
	    n = t = N;
	});
	// 检测回退键
	if ( win.plus ) {
		plus.key.addEventListener( 'backbutton', function() { 
			$.closeAll( plus.webview.currentWebview() );
		} );
	}
}


/* 引导启动 */
var boot = {
	init: function( x ) {
		x && $.config( x );
		this.initEnv(), this.initDocView();
	},
	ready: function( a ) {
		this.fn = a;
		br.mobile && location.protocol == 'file:' ? doc.addEventListener( 'plusready', this.domReady ) : $.query( doc ).ready( this.domReady );
	},
	domReady: function() {
		boot.dom_ok = T;
		boot.callback();
	},
	cssReady: function() {
		boot.css_ok = T;
		boot.callback();
	},
	callback: function() {
		this.dom_ok && this.css_ok && this.fn();
	},
	initEnv: function() {
		if ( _cfg.path != N )
			_path = _cfg.path;
		else
			_cfg.path = _path;
		if ( _cfg.lib != N )
			_lib = _cfg.lib;
		_ver = _cfg.ver ? '?ver=' + _cfg.ver : '',
		_ui_path = _urlLoc( _path, _lib ) + 'ui/';
		
		_compatJS();
		
		if ( noGlobal || _cfg.no_conflict ) {
			(Date.$ = $).abbr = 'Date.$';
		}
		var _define  = new Define( _path ),
			_require = new Require( _path ),
			_wg_lib  = _urlLoc( _path, _lib ) + 'wg/',
			_loc     = _require( _wg_lib + 'loc/' + (_cfg.lang || 'zh_CN') ),
			_jq      = _loc && _require( _wg_lib + 'jquery/jquery-' + (br.mobile ? '3.3.1' : '1.12.4') );
		if ( _loc ) {
			_cfg.loc && $.mergeDeep( _loc, _cfg.loc );
		} else {
			return alert( 'path is not exist:\n{\n  path: "' + _path + '",\n  lib: "' + _lib + '"\n}' );
		}
		for ( var k in _cfg.alias ) {
			for ( var i = 0, b = k.split( ',' ); i < b.length; i ++ )
				_alias[ _mod_uri( _path, b[ i ] ) ] = _cfg.alias[ k ];
		}
		_define( 'dfish',  function() { return $ } );
		_define( 'jquery', function() { return _jq } );
		_define( 'loc',    function() { return _loc } );
		
		$.PATH = _path;
		$.LIB  = _lib;
		$.IMGPATH = _ui_path + 'g/';
		$.version = version;
		
		$.loc     = _loc;
		$.query   = _jq;
		$.define  = _define;
		$.require = _require;

		var w = _require( _wg_lib + 'widget' );
		
		if ( !(noGlobal || _cfg.no_conflict) ) {
			win.Q  = win.jQuery = _jq;
			win.VM = $.vm;
		} else {
			_$ && (win.$ = _$);
		}
		
		$.skin( _cfg.skin, boot.cssReady );
	},
	initDocView: function() {
		$.ready( function() {
			_compatDOM();
			// 生成首页view
			if ( _cfg.view ) {
				$.widget( _extend( _cfg.view, { type: 'view', width: '*', height: '*' } ) ).render( _db() );
			} else {
				// 把 <d:wg> 标签转换为 widget
				for ( var i = 0, d = _tags( 'script' ), j, l = d.length; i < l; i ++ ) {
					if ( d[ i ].getAttribute( 'type' ) === 'dfish/widget' && (eval( 'j = ' + d[ i ].innerHTML.replace( /&lt;/g, '<' ).replace( '&gt;', '>' ) )) )
						$.widget( j ).render( d[ i ], 'replace' );
				}
			}
			// ie6及以下浏览器，弹出浮动升级提示
			if ( ie && br.ieVer < 7 ) {
				VM().cmd({ type: 'tip', cls: 'f-shadow', text: '<div style="float:left;padding:10px 30px 0 0;">' + $.loc.browser_upgrade + '</div><div style="float:left;line-height:4"><a target=_blank title=Chrome href=' + (_cfg.support_url ? _urlFormat( _cfg.support_url, ['chrome'] ) : 'https://www.baidu.com/s?wd=%E8%B0%B7%E6%AD%8C%E6%B5%8F%E8%A7%88%E5%99%A8%E5%AE%98%E6%96%B9%E4%B8%8B%E8%BD%BD') + '>' +
					$.image( '.f-i-chrome' ) + '</a> &nbsp; <a target=_blank title=IE href=' + (_cfg.support_url ? _urlFormat( _cfg.support_url, ['ie'] ) : 'https://support.microsoft.com/zh-cn/help/17621/internet-explorer-downloads') + '>' + $.image( '.f-i-ie' ) + '</a></div>', width: '*', snap: doc.body, snaptype: 'tt', prong: F});
			}
		} );
		// 调试模式
		if ( _cfg.debug ) {
			$.query( doc ).contextmenu( function( e ) {
				if ( e.ctrlKey && ! $( ':develop' ) ) {
					var m = $.vm( e.target ), c = $.bcr( m.$() ),
						s = '<div class="f-develop" style="width:' + (c.width - 4) + 'px;height:' + (c.height - 4) + 'px;left:' + c.left + 'px;top:' + c.top + 'px;"></div>',
						d = m.closest( 'dialog' ),
						t = 'path: ' + m.path + (d ? '\ndialog: ' + (d.x.id || '') : '') + '\nsrc: ' + (m.x.src || '');
					if ( m.x.template )
						t += '\ntemplate: ' + m.x.template;
					if ( br.css3 ) {
						Q( e.target ).closest( '[w-type="xsrc"]' ).each( function() {
							var g = $.all[ this.id ], c = $.bcr( this );
							s += '<div class="f-develop z-tpl" style="width:' + (c.width - 6) + 'px;height:' + (c.height - 6) + 'px;left:' + (c.left + 1) + 'px;top:' + (c.top + 1) + 'px;">' +
								'<div class=_t><span class=_s>id: ' + (g.x.id || '') + '<br>template: ' + (g.x.template || '') + '<br>src: ' + (g.x.src || '') + '</span></div>' +
								'<div>';
						} );
					}
					$.query( doc.body ).append( s );
					setTimeout( function() {
						$.vm ? $.vm().cmd( { type: 'alert', text: t, yes: function() { Q( '.f-develop' ).remove() } } ) : (alert( t ), Q( '.f-develop' ).remove());
					}, 50 );
					e.preventDefault();
				}
			} );
		}
		if ( ! br.mobile ) {
			// firefox 没有 window.event 对象，需要监听鼠标点击事件，记录每次点击的坐标
			br.fox && doc.addEventListener( 'mousedown', _point, T );
			// 检测回退键
			var k8;
			$.query( doc ).on( 'keydown', function( e ) { (k8 = e.keyCode === 8) && br.ms && e.target.readOnly && e.preventDefault(); } );
			$.query( doc ).on( 'keyup', function( e ) { k8 = F; } );
			$.query( win ).on( 'beforeunload', function( e ) { if ( k8 ) { k8 = F; return br.fox ? ' ' : ''; } } );
		}
	}
	
};

/* 初始化应用环境 */
_merge( $, {
	abbr: '$',
	alert_id: 'dfish:alert',
	_data: {},
	all: {},
	globals: {},
	// 事件白名单
	white_events: (function() {
		var a = [ 'all', 'click,contextmenu,dragstart,drag,dragend,dragenter,dragleave,dragover,drop,keydown,keypress,keyup,copy,cut,paste,scroll,select,selectstart,propertychange,beforepaste,beforedeactivate,' +
			(br.mobile ? 'touchstart,touchmove,touchend,tap' : 'mouseover,mouseout,mousedown,mouseup,mousemove,mousewheel,mouseenter,mouseleave,dblclick'), 'input', 'focus,blur,input', 'option', 'change' ];
		for ( var i = 0, r = {}, j, k, v; i < a.length; i += 2 ) {
			k = a[ i ], r[ k ] = {}, v = [];
			for ( j = 1; j < i + 2; j += 2 )
				v.push.apply( v, a[ j ].split( ',' ) );
			for( j = 0; j < v.length; j ++ )
				r[ k ][ v[ j ] ] = T;
		}
		return r;
	})(),
	// 设置全局配置，可多次调用
	config: function( a ) {
		this.x = _cfg = _extendDeep( a, _cfg );
		a.data && this._data && _extend( this._data, a.data );
	},
	// 设置全局配置，并初始化环境，只调一次
	init: function( x ) {
		boot.init( x );
	},
	ready: function( a ) {
		boot.ready( a );
	},
	use: function( a ) {
		return (new Require( _path || '' ))( a );
	},
	rt: function( a ) {
		return function() { return a };
	},
	// 存取临时变量
	data: function( a, b ) {
		return b === U ? this._data[ a ] : (this._data[ a ] = b);
	},
	// @a -> context, b -> fn
	proxy: function( a, b ) {
		typeof b === _STR && (b = Function( b ));
		return function() { return b.apply( a, arguments ) };
	},
	width: function() {
		return cvs.clientWidth;
	},
	height: function() {
		return cvs.clientHeight;
	},
	// a -> text, b -> pos, c -> time, d -> id
	alert: function( a, b, c, d ) {
		return $.vm ? $.vm().cmd( { type: 'alert', text: a, position: b, timeout: c, id: d !== U ? d : $.alert_id } ) : alert( a );
	},
	// a -> text, b -> yes, c -> no
	confirm: function( a, b, c ) {
		return $.vm().cmd( { type: 'confirm', text: a, yes: b, no: c } );
	},
	// 显示一个 tip /@a -> target, b -> feature?
	tip: function( a, b ) {
		VM(this).cmd( _extend( b || {}, { type: 'tip', text: _strEscape( a.getAttribute( 'data-title' ) ), snap: a, snaptype: 'a', timeout: 5 } ) );
	},	
	show: function( a, b ) {
		_classRemove( a, 'f-none' );
	},
	hide: function( a ) {
		_classAdd( a, 'f-none' );
	},
	zover: function( a ) {
		!a.contains( event.fromElement ) && _classAdd( a, 'z-hv' );
	},
	zout: function( a ) {
		!a.contains( event.toElement ) && _classRemove( a, 'z-hv' );
	},
	loadCss: function( a ) {
		return $.require.css( a );
	},
	// 根据expr获取单个元素 /a -> expr, b -> context
	get: function( a, b ) {
		return $.query.find( a, b )[ 0 ];
	},
	// 如果当前页面是通过 embedwindow widget 引入的，那么可以本方法来获取这个 embedwindow widget
	embedWindow: function( a ) {
		a = a == N ? win : a.nodeType ? _win( a ) : a.isWidget ? _win( a.$() ) : a;
		var f = a && a.frameElement;
		return f && _win( f )[ f.getAttribute( 'w-abbr' ) || 'dfish' ].all[ f.id ];
	},
	//关闭窗口
	close: function( a ) {
		if ( a = this.dialog( a ) ) {
			a.close();
		} else if ( win.plus ) {
			$.closeAll( plus.webview.currentWebview() );
		}
	},
	// 关闭当前webview打开的所有webview；
	closeOpened: function(webview) {
		var opened = webview.opened();
		if( opened ) {
			for( var i = 0, len = opened.length; i < len; i ++ ) {
				var openedWebview = opened[i];
				var open_open = openedWebview.opened();
				if(open_open && open_open.length > 0) {
					//关闭打开的webview
					$.closeOpened(openedWebview);
					//关闭自己
					openedWebview.close("none");
				} else {
					//如果直接孩子节点，就不用关闭了，因为父关闭的时候，会自动关闭子；
					if(openedWebview.parent() !== webview) {
						openedWebview.close('none');
					}
				}
			}
		}
	},
	closeAll: function( webview, aniShow ) {
		$.closeOpened(webview);
		aniShow ? webview.close( aniShow ) : webview.close();
	},
	cleanPop: function() {
		$.require( 'dialog' ).cleanPop();		
	},
	// @a -> src, b -> feature { id: '', cls: '', style: '', click: '', tip: '' }
	image: function( a, b ) {
		var d = a || '', e = ! d.indexOf( '.' ) && d.indexOf( '/' ) < 0, s = [];
		if ( ! e && d.indexOf( '%' ) > -1 ) {
			d = d.replace( '%img%', _ui_path + 'g' );
		}
		for ( var i = 1, c, t; i < arguments.length; i ++ ) {
			c = arguments[ i ], t = '';
			if ( c.id ) t += ' id=' + c.id;
			if ( c.cls ) t += ' class="' + c.cls + '"';
			if ( c.style ) t += ' style="' + c.style + '"';
			if ( c.click ) t += ' onclick="' + _strQuot( c.click ) + '"';
			if ( c.tip ) t += ' title="' + _strQuot( c.tip ) + '"';
			s.push( t );
		}
		var r = '<span' + (s[ 0 ] || '') + '>';
		if ( e ) {
			r += '<em class="_ico f-i ' + d.replace( /\./g, '' ) + '"' + (s[ 1 ] || '') + '></em>';
		} else {
			if ( d ) {
				r += '<img src="' + d + '" class="_ico f-va"' + (b && b.width ? ' width=' + b.width : '') + (b && b.height ? ' height=' + b.height : '') +
					(b && b.error ? ' onerror="' + b.error + '"' : '') + (b && b.load ? ' onload="' + b.load + '"' : '');
				if ( b && (b.maxwidth || b.maxheight) ) {
					var t = '';
					b.maxwidth && (t += 'max-width:' + b.maxwidth + 'px;');
					b.maxheight && (t += 'max-height:' + b.maxheight + 'px;');
					r += ' style="' + t + '"';
				}
				r += '>';
			}
		}
		return r + '<i class=f-vi></i></span>';
	},
	arrow: function( a, b ) {
		var c = b || a;
		if ( a && a.nodeType ) {
			a.className = 'f-arw f-arw-' + c;
		} else
			return '<em class="f-arw f-arw-' + (c || 'b1') + '"' + (b ? ' id=' + a : '') + '></em>';
	},
	colIndex: function( o ) {
	    var td = $.query( o ).closest( 'td' )[ 0 ], m = [], r,
	    	$table = $.query( td ).closest( 'table, thead, tbody, tfoot' );
	    $table.children( "tr" ).each( function( y, row ) {
	        $.query( row ).children( "td, th" ).each( function( x, cell ) {
	            var $cell = $.query( cell ),
	                cspan = $cell.attr( "colspan" ) | 0,
	                rspan = $cell.attr( "rowspan" ) | 0,
	                tx, ty;
	            cspan = cspan ? cspan : 1;
	            rspan = rspan ? rspan : 1;
	            for( ; m[y] && m[y][x]; ++x );  //skip already occupied cells in current row
	            for( tx = x; tx < x + cspan; ++tx ) {  //mark matrix elements occupied by current cell with true
	                for( ty = y; ty < y + rspan; ++ty ) {
	                    if( !m[ty] ) {  //fill missing rows
	                        m[ty] = [];
	                    }
	                    m[ty][tx] = true;
	                }
	            }
	            if ( cell === td )
	            	return (r = x);
	        } );
	        return r;
	    } );
	    return r;
	},
	// @a -> move fn, b -> up fn, c -> el
	moveup: function( a, b, c ) {
		var d, f;
		ie ? _attach( doc, 'selectstart', f = $.rt( F ) ) : _classAdd( cvs, 'f-unsel' );
		_attach( doc, 'mousemove', d = function( e ) { a( ie ? Q.event.fix( e ) : e ) }, T );
		_attach( doc, 'mouseup', function( e ) {
			b && b( ie ? Q.event.fix( e ) : e );
			_detach( doc, 'mousemove', d, T );
			_detach( doc, 'mouseup', arguments.callee, T );
			ie ? _detach( doc, 'selectstart', f ) : _classRemove( cvs, 'f-unsel' );
			c && _rm( c );
		}, T );
	},
	// @a -> el, b -> type, c -> fast|normal|slow (.2s|.5s|1s), d -> fn 结束后执行的函数
	animate: function( a, b, c, d ) {
		c = c || 'fast';
		var s = 'f-animated f-speed-' + (c || 'fast') + ' f-ani-' + b, e = { fast: 200, normal: 500, slow: 1000 };
		_classAdd( a, s );
		setTimeout( function() {
			_classRemove( a, s );
			d && d();
		}, e[ c ] || c );
	},	
	// @a -> src, b -> post json?
	download: function( a, b ) {
		var c = Q( '<div class=f-none><iframe src="about:blank" name=xx></iframe></div>' );
		if ( b ) {
			var f = document.createElement( 'form' ), u = '_download_' + $.uid();
			f.action = a;
			f.target = u;
			f.method = 'post';
			for ( var i in b ) {
				if ( _isArray( b[ i ] ) ) {
					for ( var j = 0; j < b[ i ].length; j ++ ) {
						var	o = document.createElement( 'textarea' );
						o.name = i;
						o.value = b[ i ][ j ];
						f.appendChild( o );
					}
				} else {
					var	o = document.createElement( 'textarea' );
					o.name = i;
					o.value = b[ i ];
					f.appendChild( o );
				}
			}
			c.find( 'iframe' ).prop( 'name', u );
			c.append( f );
			f.submit();
		}
		c.appendTo( document.body );
		b ? f.submit() : c.find( 'iframe' ).prop( 'src', a );
	},
	// 根据文件后缀名获取文件类型
	mimeType: (function() {
		var mimes = { ".323": "text/h323", ".3gp": "video/3gpp", ".aab": "application/x-authoware-bin", ".aam": "application/x-authoware-map", ".aas": "application/x-authoware-seg", ".acx": "application/internet-property-stream", ".ai": "application/postscript", ".aif": "audio/x-aiff", ".aifc": "audio/x-aiff", ".aiff": "audio/x-aiff", ".als": "audio/X-Alpha5", ".amc": "application/x-mpeg", ".apk": "application/vnd.android.package-archive", ".asc": "text/plain", ".asd": "application/astound", ".asf": "video/x-ms-asf", ".asn": "application/astound", ".asp": "application/x-asap", ".asr": "video/x-ms-asf", ".asx": "video/x-ms-asf", ".au": "audio/basic", ".avi": "video/x-msvideo", ".awb": "audio/amr-wb", ".axs": "application/olescript", ".bas": "text/plain", ".bcpio": "application/x-bcpio", ".bld": "application/bld", ".bld2": "application/bld2", ".bmp": "image/bmp", ".bz2": "application/x-bzip2", ".c": "text/plain", ".cal": "image/x-cals", ".cat": "application/vnd.ms-pkiseccat", ".ccn": "application/x-cnc", ".cco": "application/x-cocoa", ".cdf": "application/x-cdf", ".cer": "application/x-x509-ca-cert", ".cgi": "magnus-internal/cgi", ".chat": "application/x-chat", ".clp": "application/x-msclip", ".cmx": "image/x-cmx", ".co": "application/x-cult3d-object", ".cod": "image/cis-cod", ".conf": "text/plain", ".cpio": "application/x-cpio", ".cpp": "text/plain", ".cpt": "application/mac-compactpro", ".crd": "application/x-mscardfile", ".crl": "application/pkix-crl", ".crt": "application/x-x509-ca-cert", ".csh": "application/x-csh", ".csm": "chemical/x-csml", ".csml": "chemical/x-csml", ".css": "text/css", ".dcm": "x-lml/x-evm", ".dcr": "application/x-director", ".dcx": "image/x-dcx", ".der": "application/x-x509-ca-cert", ".dhtml": "text/html", ".dir": "application/x-director", ".dll": "application/x-msdownload", ".doc": "application/msword", ".docx": "application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".dot": "application/msword", ".dvi": "application/x-dvi", ".dwf": "drawing/x-dwf", ".dwg": "application/x-autocad", ".dxf": "application/x-autocad", ".dxr": "application/x-director", ".ebk": "application/x-expandedbook", ".emb": "chemical/x-embl-dl-nucleotide", ".embl": "chemical/x-embl-dl-nucleotide", ".eps": "application/postscript", ".epub": "application/epub+zip", ".eri": "image/x-eri", ".es": "audio/echospeech", ".esl": "audio/echospeech", ".etc": "application/x-earthtime", ".etx": "text/x-setext", ".evm": "x-lml/x-evm", ".evy": "application/envoy", ".fh4": "image/x-freehand", ".fh5": "image/x-freehand", ".fhc": "image/x-freehand", ".fif": "application/fractals", ".flr": "x-world/x-vrml", ".flv": "flv-application/octet-stream", ".fm": "application/x-maker", ".fpx": "image/x-fpx", ".fvi": "video/isivideo", ".gau": "chemical/x-gaussian-input", ".gca": "application/x-gca-compressed", ".gdb": "x-lml/x-gdb", ".gif": "image/gif", ".gps": "application/x-gps", ".gtar": "application/x-gtar", ".gz": "application/x-gzip",
			".h": "text/plain", ".hdf": "application/x-hdf", ".hdm": "text/x-hdml", ".hdml": "text/x-hdml", ".hlp": "application/winhlp", ".hqx": "application/mac-binhex40", ".hta": "application/hta", ".htc": "text/x-component", ".htm": "text/html", ".html": "text/html", ".hts": "text/html", ".htt": "text/webviewhtml", ".ice": "x-conference/x-cooltalk", ".ico": "image/x-icon", ".ief": "image/ief", ".ifm": "image/gif", ".ifs": "image/ifs", ".iii": "application/x-iphone", ".imy": "audio/melody", ".ins": "application/x-internet-signup", ".ips": "application/x-ipscript", ".ipx": "application/x-ipix", ".isp": "application/x-internet-signup", ".it": "audio/x-mod", ".itz": "audio/x-mod", ".ivr": "i-world/i-vrml", ".j2k": "image/j2k", ".jad": "text/vnd.sun.j2me.app-descriptor", ".jam": "application/x-jam", ".jar": "application/java-archive", ".java": "text/plain", ".jfif": "image/pipeg", ".jnlp": "application/x-java-jnlp-file", ".jpe": "image/jpeg", ".jpeg": "image/jpeg", ".jpg": "image/jpeg", ".jpz": "image/jpeg", ".js": "application/x-javascript", ".jwc": "application/jwc", ".kjx": "application/x-kjx", ".lak": "x-lml/x-lak", ".latex": "application/x-latex", ".lcc": "application/fastman", ".lcl": "application/x-digitalloca", ".lcr": "application/x-digitalloca", ".lgh": "application/lgh", ".lml": "x-lml/x-lml", ".lmlpack": "x-lml/x-lmlpack", ".log": "text/plain", ".lsf": "video/x-la-asf", ".lsx": "video/x-la-asf", ".m13": "application/x-msmediaview", ".m14": "application/x-msmediaview", ".m15": "audio/x-mod", ".m3u": "audio/x-mpegurl", ".m3url": "audio/x-mpegurl", ".m4a": "audio/mp4a-latm", ".m4b": "audio/mp4a-latm", ".m4p": "audio/mp4a-latm", ".m4u": "video/vnd.mpegurl", ".m4v": "video/x-m4v", ".ma1": "audio/ma1", ".ma2": "audio/ma2", ".ma3": "audio/ma3", ".ma5": "audio/ma5", ".man": "application/x-troff-man", ".map": "magnus-internal/imagemap", ".mbd": "application/mbedlet", ".mct": "application/x-mascot", ".mdb": "application/x-msaccess", ".mdz": "audio/x-mod", ".me": "application/x-troff-me", ".mel": "text/x-vmel", ".mht": "message/rfc822", ".mhtml": "message/rfc822", ".mi": "application/x-mif", ".mid": "audio/mid", ".midi": "audio/midi", ".mif": "application/x-mif", ".mil": "image/x-cals", ".mio": "audio/x-mio", ".mmf": "application/x-skt-lbs", ".mng": "video/x-mng", ".mny": "application/x-msmoney", ".moc": "application/x-mocha", ".mocha": "application/x-mocha", ".mod": "audio/x-mod", ".mof": "application/x-yumekara", ".mol": "chemical/x-mdl-molfile", ".mop": "chemical/x-mopac-input", ".mov": "video/quicktime", ".movie": "video/x-sgi-movie", ".mp2": "video/mpeg", ".mp3": "audio/mpeg", ".mp4": "video/mp4", ".mpa": "video/mpeg", ".mpc": "application/vnd.mpohun.certificate", ".mpe": "video/mpeg", ".mpeg": "video/mpeg", ".mpg": "video/mpeg", ".mpg4": "video/mp4", ".mpga": "audio/mpeg", ".mpn": "application/vnd.mophun.application", ".mpp": "application/vnd.ms-project", ".mps": "application/x-mapserver", ".mpv2": "video/mpeg", ".mrl": "text/x-mrml", ".mrm": "application/x-mrm", ".ms": "application/x-troff-ms", ".msg": "application/vnd.ms-outlook", ".mts": "application/metastream", ".mtx": "application/metastream", ".mtz": "application/metastream", ".mvb": "application/x-msmediaview", ".mzv": "application/metastream", ".nar": "application/zip", ".nbmp": "image/nbmp", ".nc": "application/x-netcdf", ".ndb": "x-lml/x-ndb", ".ndwn": "application/ndwn", ".nif": "application/x-nif", ".nmz": "application/x-scream", ".nokia-op-logo": "image/vnd.nok-oplogo-color", ".npx": "application/x-netfpx", ".nsnd": "audio/nsnd", ".nva": "application/x-neva1", ".nws": "message/rfc822",
			".oda": "application/oda", ".ogg": "audio/ogg", ".oom": "application/x-AtlasMate-Plugin", ".p10": "application/pkcs10", ".p12": "application/x-pkcs12", ".p7b": "application/x-pkcs7-certificates", ".p7c": "application/x-pkcs7-mime", ".p7m": "application/x-pkcs7-mime", ".p7r": "application/x-pkcs7-certreqresp", ".p7s": "application/x-pkcs7-signature", ".pac": "audio/x-pac", ".pae": "audio/x-epac", ".pan": "application/x-pan", ".pbm": "image/x-portable-bitmap", ".pcx": "image/x-pcx", ".pda": "image/x-pda", ".pdb": "chemical/x-pdb", ".pdf": "application/pdf", ".pfr": "application/font-tdpfr", ".pfx": "application/x-pkcs12", ".pgm": "image/x-portable-graymap", ".pict": "image/x-pict", ".pko": "application/ynd.ms-pkipko", ".pm": "application/x-perl", ".pma": "application/x-perfmon", ".pmc": "application/x-perfmon", ".pmd": "application/x-pmd", ".pml": "application/x-perfmon", ".pmr": "application/x-perfmon", ".pmw": "application/x-perfmon", ".png": "image/png", ".pnm": "image/x-portable-anymap", ".pnz": "image/png", ".pot,": "application/vnd.ms-powerpoint", ".ppm": "image/x-portable-pixmap", ".pps": "application/vnd.ms-powerpoint", ".ppt": "application/vnd.ms-powerpoint", ".pptx": "application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pqf": "application/x-cprplayer", ".pqi": "application/cprplayer", ".prc": "application/x-prc", ".prf": "application/pics-rules", ".prop": "text/plain", ".proxy": "application/x-ns-proxy-autoconfig", ".ps": "application/postscript", ".ptlk": "application/listenup", ".pub": "application/x-mspublisher", ".pvx": "video/x-pv-pvx", ".qcp": "audio/vnd.qcelp", ".qt": "video/quicktime", ".qti": "image/x-quicktime", ".qtif": "image/x-quicktime", ".r3t": "text/vnd.rn-realtext3d", ".ra": "audio/x-pn-realaudio", ".ram": "audio/x-pn-realaudio", ".ras": "image/x-cmu-raster", ".rc": "text/plain", ".rdf": "application/rdf+xml", ".rf": "image/vnd.rn-realflash", ".rgb": "image/x-rgb", ".rlf": "application/x-richlink", ".rm": "audio/x-pn-realaudio", ".rmf": "audio/x-rmf", ".rmi": "audio/mid", ".rmm": "audio/x-pn-realaudio", ".rmvb": "audio/x-pn-realaudio", ".rnx": "application/vnd.rn-realplayer", ".roff": "application/x-troff", ".rp": "image/vnd.rn-realpix", ".rpm": "audio/x-pn-realaudio-plugin", ".rt": "text/vnd.rn-realtext", ".rte": "x-lml/x-gps", ".rtf": "application/rtf", ".rtg": "application/metastream", ".rtx": "text/richtext", ".rv": "video/vnd.rn-realvideo", ".rwc": "application/x-rogerwilco", ".s3m": "audio/x-mod", ".s3z": "audio/x-mod", ".sca": "application/x-supercard", ".scd": "application/x-msschedule", ".sct": "text/scriptlet", ".sdf": "application/e-score", ".sea": "application/x-stuffit", ".setpay": "application/set-payment-initiation", ".setreg": "application/set-registration-initiation", ".sgm": "text/x-sgml", ".sgml": "text/x-sgml", ".sh": "application/x-sh", ".shar": "application/x-shar", ".shtml": "magnus-internal/parsed-html", ".shw": "application/presentations", ".si6": "image/si6", ".si7": "image/vnd.stiwap.sis", ".si9": "image/vnd.lgtwap.sis", ".sis": "application/vnd.symbian.install", ".sit": "application/x-stuffit", ".skd": "application/x-Koan", ".skm": "application/x-Koan", ".skp": "application/x-Koan", ".skt": "application/x-Koan", ".slc": "application/x-salsa", ".smd": "audio/x-smd", ".smi": "application/smil", ".smil": "application/smil", ".smp": "application/studiom", ".smz": "audio/x-smd", ".snd": "audio/basic", ".spc": "application/x-pkcs7-certificates", ".spl": "application/futuresplash", ".spr": "application/x-sprite", ".sprite": "application/x-sprite", ".sdp": "application/sdp", ".spt": "application/x-spt",
			".src": "application/x-wais-source", ".sst": "application/vnd.ms-pkicertstore", ".stk": "application/hyperstudio", ".stl": "application/vnd.ms-pkistl", ".stm": "text/html", ".svg": "image/svg+xml", ".sv4cpio": "application/x-sv4cpio", ".sv4crc": "application/x-sv4crc", ".svf": "image/vnd", ".svg": "image/svg+xml", ".svh": "image/svh", ".svr": "x-world/x-svr", ".swf": "application/x-shockwave-flash", ".swfl": "application/x-shockwave-flash", ".t": "application/x-troff", ".talk": "text/x-speech", ".tar": "application/x-tar", ".taz": "application/x-tar", ".tbp": "application/x-timbuktu", ".tbt": "application/x-timbuktu", ".tcl": "application/x-tcl", ".tex": "application/x-tex", ".texi": "application/x-texinfo", ".texinfo": "application/x-texinfo", ".tgz": "application/x-compressed", ".thm": "application/vnd.eri.thm", ".tif": "image/tiff", ".tiff": "image/tiff", ".tki": "application/x-tkined", ".tkined": "application/x-tkined", ".toc": "application/toc", ".toy": "image/toy", ".tr": "application/x-troff", ".trk": "x-lml/x-gps", ".trm": "application/x-msterminal", ".tsi": "audio/tsplayer", ".tsp": "application/dsptype", ".tsv": "text/tab-separated-values", ".ttz": "application/t-time", ".txt": "text/plain", ".uls": "text/iuls", ".ult": "audio/x-mod", ".ustar": "application/x-ustar", ".uu": "application/x-uuencode", ".uue": "application/x-uuencode", ".vcd": "application/x-cdlink", ".vcf": "text/x-vcard", ".vdo": "video/vdo", ".vib": "audio/vib", ".viv": "video/vivo", ".vivo": "video/vivo", ".vmd": "application/vocaltec-media-desc", ".vmf": "application/vocaltec-media-file", ".vmi": "application/x-dreamcast-vms-info", ".vms": "application/x-dreamcast-vms", ".vox": "audio/voxware", ".vqe": "audio/x-twinvq-plugin", ".vqf": "audio/x-twinvq", ".vql": "audio/x-twinvq", ".vre": "x-world/x-vream", ".vrml": "x-world/x-vrml", ".vrt": "x-world/x-vrt", ".vrw": "x-world/x-vream", ".vts": "workbook/formulaone", ".wav": "audio/x-wav", ".wax": "audio/x-ms-wax", ".wbmp": "image/vnd.wap.wbmp", ".wcm": "application/vnd.ms-works", ".wdb": "application/vnd.ms-works", ".web": "application/vnd.xara", ".wi": "image/wavelet", ".wis": "application/x-InstallShield", ".wks": "application/vnd.ms-works", ".wm": "video/x-ms-wm", ".wma": "audio/x-ms-wma", ".wmd": "application/x-ms-wmd", ".wmf": "application/x-msmetafile", ".wml": "text/vnd.wap.wml", ".wmlc": "application/vnd.wap.wmlc", ".wmls": "text/vnd.wap.wmlscript", ".wmlsc": "application/vnd.wap.wmlscriptc", ".wmlscript": "text/vnd.wap.wmlscript", ".wmv": "audio/x-ms-wmv", ".wmx": "video/x-ms-wmx", ".wmz": "application/x-ms-wmz", ".wpng": "image/x-up-wpng", ".wpt": "x-lml/x-gps", ".wri": "application/x-mswrite", ".wrl": "x-world/x-vrml", ".wrz": "x-world/x-vrml", ".ws": "text/vnd.wap.wmlscript", ".wsc": "application/vnd.wap.wmlscriptc", ".wv": "video/wavelet", ".wvx": "video/x-ms-wvx", ".wxl": "application/x-wxl", ".x-gzip": "application/x-gzip", ".xaf": "x-world/x-vrml", ".xar": "application/vnd.xara", ".xbm": "image/x-xbitmap", ".xdm": "application/x-xdma", ".xdma": "application/x-xdma", ".xdw": "application/vnd.fujixerox.docuworks", ".xht": "application/xhtml+xml", ".xhtm": "application/xhtml+xml", ".xhtml": "application/xhtml+xml", ".xla": "application/vnd.ms-excel", ".xlc": "application/vnd.ms-excel", ".xll": "application/x-excel", ".xlm": "application/vnd.ms-excel", ".xls": "application/vnd.ms-excel", ".xlsx": "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlt": "application/vnd.ms-excel", ".xlw": "application/vnd.ms-excel", ".xm": "audio/x-mod",
			".xml":"application/xml", ".xmz": "audio/x-mod", ".xof": "x-world/x-vrml", ".xpi": "application/x-xpinstall", ".xpm": "image/x-xpixmap", ".xsit": "text/xml", ".xsl": "text/xml", ".xul": "text/xul", ".xwd": "image/x-xwindowdump", ".xyz": "chemical/x-pdb", ".yz1": "application/x-yz1", ".z": "application/x-compress", ".zac": "application/x-zaurus-zac", ".json": "application/json", ".vsd":"application/vnd.visio", ".pot":"application/vnd.ms-powerpoint", ".et": "application/kset", ".wps": "application/kswps", ".dps": "application/ksdps" };
		return function( a ) {
			var b = _strFrom( a, '.', T );
			return mimes[ '.' + b ];
		}
	})(),
	//导入皮肤css /a -> { dir: 'css/', theme: 'classic', color: 'blue' }
	skin: (function() {
		var did = _uid(), gid = _uid(), tid = _uid(), cid = _uid(), y = {};
		return function( x, f ) {
			var s = [], d = [];
			if ( ! $( did ) ) {
				_classAdd( cvs, br.css3 ? 'f-css3' : 'f-css2' );
				var k = location.protocol + '//' + location.host + _ui_path + 'g/';
				_loadStyle( '.f-pic-prev-cursor{cursor:url(' + k + 'pic_prev.cur),auto}.f-pic-next-cursor{cursor:url(' + k + 'pic_next.cur),auto}' );
				s.push( _ui_path + 'dfish.css' );
				d.push( did );
				br.mobile && (s.push(_ui_path + 'mobile.css'), d.push(''));
			}
			if ( x ) {
				x = _extend( {}, x, y );
				if ( x.theme != y.theme ) {
					_rm( tid ), _rm( cid );
				} else if ( x.color != y.color ) {
					_rm( cid );
				}
				y = x;
				! $( gid ) && (s.push( _path + x.dir + 'global.css' ), d.push( gid ));
				! $( tid ) && (s.push( _path + x.dir + x.theme + '/' + x.theme + '.css' ), d.push( tid ));
				! $( cid ) && (s.push( _path + x.dir + x.theme + '/' + x.color + '/' + x.color + '.css' ), d.push( cid ));
			}
			_loadCss( s, d, f );
		}
	})(),
	// @a -> image array, b -> id
	previewImage: function( a, b ) {
		var w = Math.max( 600, $.width() - 100 ), h = Math.max( 400, $.height() - 100 );
		$.vm().cmd( { type: 'dialog', ownproperty: T, cls: 'f-dialog-preview', width: w, height: h, cover: T, pophide: T,
			node: { type: 'html', align: 'center', valign: 'middle', text: '<img src=' + a + ' style="max-width:' + (w - 30) + 'px;max-height:' + h + 'px">' +
				(b ? '<a class=_origin target=_blank href=' + b + '>' + $.loc.preview_orginal_image + '</a>' : '') +
				'<em class="f-i _dlg_x" onclick=' + $.abbr + '.close(this)></em>' } } );
	},
	/* ! 把range内的图片变成缩略图
	 * @range: htmlElement
	 * @width: 图片最大宽度。超出这个值会等比缩小
	 * @fn: boolean|String|function 可选，点击缩略图执行的动作
	 
	 *  范例1: 点击缩略图，使用引擎默认的预览效果
	 		$.thumbnail( htmlPanel.$(), 500 );
	 		
	 *  范例2: 点击缩略图，在新窗口打开预览图片
	 		$.thumbnail( htmlPanel.$(), 500, "imgpreview.jsp?src=$0&title=$1" );
	 		
	 *  范例3: 点击缩略图，执行一个函数
	 		$.thumbnail( htmlPanel.$(), 500, function(){ alert( this.src ) } );
	 
	 *  范例4: 只做缩略处理，不附加点击效果
	 		$.thumbnail( htmlPanel.$(), 500, false );
	 		
	 */
	thumbnail: (function() {
		function _thumb( img, maxWidth, fn ) {
			var w = img.width;
			if ( w === 0 ) {
				var o = new Image();
				o.src = img.src;
				w = o.width;
			}
			if ( w > maxWidth ) {
				img.style.maxWidth = maxWidth + 'px';
				img.style.height = 'auto';
				img.removeAttribute( 'height' );
				if ( ! img.title )
					img.title = $.loc.click_preview;
				if ( fn !== F ) {
					_classAdd( img, 'f-hand' );
					_classAdd( img, 'f-thumbnail' );
				}
				if ( fn && img.parentNode.tagName !== 'A' ) {
					if ( typeof fn === _STR ) { // fn范例: preview.jsp?src=$0&title=$1
						$.query( img ).wrap( '<a href="' + _urlFormat( fn, [ img.src, img.alt || img.title ] ) + '" target=_blank></a>' );
					} else if ( typeof fn === 'function' ) { // fn范例: function(){ window.open( this.src ) }
						$.query( img ).click( fn );
					}
				}
			}
		}
		function _prev( $img, g ) {
			var n = _arrIndex( $img, g );
			return n > 0 ? $img[ n - 1 ] : null;
		}
		function _next( $img, g ) {
			var n = _arrIndex( $img, g );
			return n < $img.length - 1 ? $img[ n + 1 ] : null;
		}
		function _btn( a, p, n, k ) {
			ie && (a.$().style.cursor = 'auto');
			var vis = p ? 'visible' : 'hidden';
			if ( vis !== $( 'f:thumbnail-prev' ).style.visibility ) {
				$( 'f:thumbnail-prev' ).style.visibility = vis;
			}
			vis = n ? 'visible' : 'hidden';
			if ( vis !== $( 'f:thumbnail-next' ).style.visibility ) {
				$( 'f:thumbnail-next' ).style.visibility = vis;
			}
			$( 'f:thumbnail-page' ).innerText = k + 1;
		}
		function _pop( $img, g ) {
			var w = $.width() * .9, h = $.height() - 60, p = _prev( $img, g ), n = _next( $img, g ), k = _arrIndex( $img, g ),
				s = '<table cellspacing=0 cellpadding=0 width=100% height=100%><tr><td align=center valign=middle><img id=f:thumbnail-img src=' + g.src + ' style="max-width:' + ( w - 40 ) +
				'px;max-height:' + ( h - 50 ) + 'px;"></table><div style="position:absolute;bottom:2px;left:50%;color:#fff"><span id=f:thumbnail-page>' + ( k + 1 ) + '</span>/' + $img.length +
				'</div><div class="f-opc0 f-abs f-pic-prev-cursor" id=f:thumbnail-prev style="visibility:' + ( p ? 'visible' : 'hidden' ) +
				';top:0;left:0;bottom:0;background:#000;width:' + ( w / 2 ) + 'px"></div><div class="f-opc0 f-abs f-pic-next-cursor" id=f:thumbnail-next style="visibility:' + ( n ? 'visible' : 'hidden' ) +
				';top:0;right:0;bottom:0;background:#000;width:' + ( w / 2 ) + 'px"></div><em class="f-i _dlg_x" onclick=' + $.abbr + '.close(this)></em>';
			var d = $.vm().cmd( { type: 'dialog', width: w, height: h, cls: 'f-dialog-preview', cover: true, pophide: T,
					node: { type: 'html', align: 'center', id: 'img', valign: 'middle', style: 'background:#000', text: s } } );
			$( 'f:thumbnail-prev' ).onclick = function() {
				$( 'f:thumbnail-img' ).src = p.src;
				n = _next( $img, p );
				p = _prev( $img, p );
				_btn( d.contentView.find( 'img' ).$(), p, n, -- k );
			}
			$( 'f:thumbnail-next' ).onclick = function() {
				$( 'f:thumbnail-img' ).src = n.src;
				p = _prev( $img, n );
				n = _next( $img, n );
				_btn( d.contentView.find( 'img' ).$(), p, n, ++ k );
			}	
		}
		return function( range, maxWidth, fn ) {
			range.$ && (range = range.$());
			var $img = $.query( 'img', range );
			$img.each( function() {
				if ( _classAny( this, 'f-thumbnail' ) )
					this.style.maxWidth = '';
				if ( this.complete ) {
					_thumb( this, maxWidth, fn );
				} else {
					$.query( this ).on( 'load', function() { _thumb( this, maxWidth, fn ); } );
				}
			} );
			if ( fn == null || fn === T ) {
				$.query( range ).click( function( ev ) {
					if ( _classAny( ev.target.className, 'f-thumbnail' ) && ! $.query( ev.target ).closest( 'a' ).length ) {
						_pop( $img, ev.target );
					} 
				} );
			}
		}
	})(),
	// 调试用的方法
	j: function( a ) {
		alert( $.jsonString( a ) );
	},
	debug: function() {
		if ( _cfg.debug ) debugger;
	},
	winbox: function( a ) {
		alert( a );
	},
	// @a -> widget|HTMLElement, y -> { print: T/F(是否打印), head: "head标签", input2text: T/F(把表单值转为文本)
	print: function( a, y ) {
		var d = a.$ ? (a.$( 'cont' ) || a.$()) : a, w = _win( d ).document, b = $.query( 'link[media=print]', w ), c = [], y = y || {};
		$.query( 'meta', w ).each( function() { c.push( this.outerHTML ) });
		(b.length > 0 ? b : $.query( 'link', w )).each( function() { c.push( '<link rel="stylesheet" type="text/css" href="' + this.getAttribute( 'href' ) + '"/>' ); });
		$.query( 'style', w ).each( function() { c.push( this.outerHTML ) });
		var s = d.outerHTML;
		s = s.replace( /<div[^>]+overflow-y[^>]+>/gi, function( $0 ) { return $0.replace( /height: \w+/gi, '' ); } );
		$.query( ':text,:password,textarea,select', d ).each( function() {
			var h = this.outerHTML, v, r;
			if ( y.input2text ) {
				var g = $.widget( this ), w = 'auto';
				if ( g && g.isFormWidget ) {
					h = g.$( 'f' ).outerHTML; v = g.text(), r = $.query( '.f-remark,.f-beforecontent,.f-aftercontent', g.$() ).html();
					if ( g.type !== 'date' && g.type !== 'spinner' )
						w = g.$( 'f' ).style.width;
				} else
					v = this.value;
				v = '<div class="f-inbl f-va f-wdbr" style="width:' + w + '">' + v + '</div>';
			} else {
				v = h.replace( 'value="' + this.defaultValue + '"', 'value="' + this.value + '"' );
			}
			s = s.replace( h, v + (r || '') );
		} );
		$.query( ':radio,:checkbox', d ).each( function() {
			if ( y.input2text ) {
				var g = $.widget( this );
				if ( g && g.isFormWidget ) {
					s = s.replace( g.$().outerHTML, g.isChecked() ? '<div class="f-inbl f-va f-wdbr">' + g.text() + '</div>' : '' );
				}
			} else {
				var h = this.outerHTML, n = h.replace( / checked=""/, '' );
				if ( this.checked )
					n = n.replace( />$/, ' checked>' );
				s = s.replace( h, n );
			}
		} );
		if ( y.input2text ) {
			$.query( 'input[type=hidden]', d ).each( function() {
				var g = $.widget( this );
				if ( g && g.isFormWidget && ! g.isHiddenWidget ) {
					s = s.replace( g.$('f').outerHTML, '<div class="f-inbl f-va f-wdbr" style="width:' + g.$( 'f' ).style.width + '">' + g.text() + '</div>' );
				}
			} );
		}
		w = window.open();
		d = w.document;
		d.open( 'text/html', 'replace' );
		d.write( '<!doctype html><html class=f-print><head><meta charset=utf-8><title>' + $.loc.print_preview + '</title><script>var $={e:function(){}}</script>' + c.join( '' ) +
			(y.head || '') + '</head><body>' + s +
			(! br.ms && y.print ? '<script>window.print();window.close()</script>' : '') +
			'</body></html>' );
		d.close();
		if ( br.ms && y.print ) {
			w.print(), w.close();
		}
		return d;
	}
} );

if ( ! noGlobal ) {
	win.dfish = win.$ = dfish;
}
// 获取引擎路径
getPath();

return dfish;
});