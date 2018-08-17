/*!
 *  dfish
 *	@version	3.0
 *	@author		cmy
 *  @modified	2016-03-31
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

_cfg = {}, _path, _ui_path, _alias = {}, _ver = '', _expando = 'dfish',

_STR = 'string', _OBJ = 'object', _NUM = 'number', _FUN = 'function', _PRO = 'prototype',

doc = win.document, cvs = doc.documentElement,

$ = dfish = function( a ) {
	if ( a != N ) return a.isWidget ? a.$() : a.nodeType ? a : doc.getElementById( a );
},

// 浏览器信息
br = (function() {
	var u = navigator.userAgent.toLowerCase(),
		d = doc.documentMode,
		n = u.indexOf( 'trident' ) > 0 && d > 10,
		ie = navigator.appName === 'Microsoft Internet Explorer', // ie version <= 10
		iv = ie && (d || parseFloat( u.substr( u.indexOf( 'msie' ) + 5 ) ));

	// 提示内容：您的浏览器版本过低，建议您升级到IE7以上或安装谷歌浏览器。
	ie && iv < 6 && alert( '\u60a8\u7684\u6d4f\u89c8\u5668\u7248\u672c\u8fc7\u4f4e\uff0c\u5efa\u8bae\u60a8\u5347\u7ea7\u5230\u0049\u0045\u0037\u4ee5\u4e0a\u6216\u5b89\u88c5\u8c37\u6b4c\u6d4f\u89c8\u5668\u3002' );
	return {
		ie		: ie,
		ieVer	: iv,
		ie7		: ie && d < 8, // ie6,ie7,兼容模式
		ie10	: ie && d === 10,
		ms		: ie || n, // 微软的浏览器( ie所有系列 )
		mobile  : !!u.match( /\bmobile\b/ ),
		fox		: u.indexOf( 'firefox' ) > 0,
		css3	: !(ie && d < 9),
		scroll	: 17,
		chdiv	: function( a, b, c ) {
			if ( typeof b === _FUN ) {
				c = b, b = '1';
			}
			var o = _div( b == N ? '1' : b );
			o.className = a;
			o.style.cssText = 'position:absolute;width:50px;top:-50px;';
			db( o );
			c && c.call( o );
			_rm( o );
		}
	};
})(),
ie = br.ie,

// a继承b的属性。不覆盖a的既有同名属性
_extend = function( a ) {
	for ( var i = 1, c; i < arguments.length; i ++ ) {
		if ( c = arguments[ i ] ) {
			for ( var k in c ) if ( ! ( k in a ) ) a[ k ] = c[ k ];
		}
	}
	return a;
},
// 递归继承
_extendDeep = function( a ) {
	for ( var j = 1, l = arguments.length, c, i; j < l; j ++ ) {
		c = arguments[ j ];
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
_merge = function( a ) {
	for ( var i = 1, c, l = arguments.length; i < l; i ++ ) {
		if ( c = arguments[ i ] ) {
			for ( var k in c ) a[ k ] = c[ k ];
		}
	}
	return a;
},
// a拷贝b的属性
_mergeDeep = function( a ) {
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
_createClass = function( a, b ) {
	var n;
	if ( b )
		n = a, a = b;
	var c = a.Const, d = a.Extend, e = a.Listener, f = a.Prototype;
	if ( e && ! e.body )
		e.body = {};
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
			if ( d[ i ].Rooter && c.Rooter === U )
				c.Rooter = d[ i ].Rooter;
			if ( d[ i ].Child && c.Child === U )
				c.Child = d[ i ].Child;
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
	if ( e && (e = e.body) ) {
		for ( var k in e ) {
			var p = e[ k ] && e[ k ].proxy;
			if ( p ) {
				if ( typeof e[ k ] === _FUN )
					e[ k ] = { method: e[ k ] };
				for ( var i = 0, q = p.split( ' ' ); i < q.length; i ++ )
					_jsonChain( T, c, 'ListenerProxy', q[ i ], k );
			}
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
_moduleCache = {}, _cssCache = {},
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
	// @ b -> id, f -> fn, s -> async?
	var r = function( b, f, s ) {
		if ( typeof b === _STR ) {
			var c = _mod_uri( a, b );
			if ( _moduleCache[ c ] ) {
				f && f( _moduleCache[ c ] );
				return _moduleCache[ c ];
			}
			b = [ b ];
		} else
			b = b.concat();
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
				f && t( b, f );
			}, ! s );
		} else {
			f && t( b, f );
		}
		return _moduleCache[ b[ 0 ] ];
	}, t = function( b, f ) {
		for ( var i = 0, g = []; i < b.length; i ++ )
			g.push( _moduleCache[ b[ i ] ] );
		f.apply( win, g );
	};
	// require.async(): 当传入第二个参数回调函数时，将异步装载js
	r.async = function( b, f ) { return r( b, f, T ) };
	// require.resolve(): 把相对路径解析为绝对路径
	r.resolve = function( b ) { return _urlLoc( a, b ) };
	// require.css(): 装载css
	r.css = function( b ) {
		var c = _mod_uri( a, b, 'css' );
		return _cssCache[ c ] || _loadCss( c );
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
		e = d.Extend = ! e ? [ 'widget' ] : ! _arrIs( e ) ? [ e ] : e;
		for ( var i = 0; i < e.length; i ++ ) {
			if ( typeof e[ i ] === _STR ) e[ i ] = r( e[ i ] );
		}
		if ( ! d.Const )
			d.Const = function() { e[ 0 ].apply( this, arguments ) };
		return _moduleCache[ _mod_uri( a, c ) ] = _createClass( c, d );
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
					r.push( _ajax_cache[ a[ j ] ].response );
				b && b( r );
			}
		};
	while ( i -- )
		_ajax( { src: a[ i ], sync: c, success: g, cache: T } );
},
_loadCss = function( a ) {
	var l = doc.createElement( 'link' );
	l.setAttribute( 'rel', 'stylesheet' );
	l.setAttribute( 'href', a + _ver );
	_tags( 'head' )[ 0 ].appendChild( l );
	return _cssCache[ a ] = a;
},
_loadStyle = function( a, b ) {
	if ( ! b || ! this( b ) ) {
		var c = document.createElement( "style" );
		c.setAttribute( "type", "text/css" );
		if( c.styleSheet ) {   //For IE
			c.styleSheet.cssText = a;
		} else {
		    c.appendChild( document.createTextNode( a ) );
		}
		if ( b ) c.id = b;
		$.query( 'head' ).append( c );
	}
},


/* 辅助方法 */
_uidCnt = 0,
_guid   = function() { return (_uidCnt ++).toString( 36 ) + ':' },
// 获取/设置全局唯一ID
_uid = function( o ) {
	if ( o )
		return o.isWidget ? ( o.id || (o.id = _guid()) ) : ( o[ _expando ] || (o[ _expando ] = _guid()) );
	return _guid();
},
// 取随机ID名 /@a -> len, b -> prefix
_rand = function( a, b ) {
	var c = Math.random(), d = '' + c;
	return ( b || '' ) + Math.floor( c * 9 + 1 ) + d.slice( 2, a ? a + 1 : 12 );
},
// @a -> context, b -> fn|str
_proxy = function( a, b ) {
	if ( typeof b === _STR )
		b = Function( b );
	return function() { return b.apply( a, arguments ) };
},
_fncall = function( a, b, c, d ) {
	return ( typeof a === _FUN ? a : Function( a ) ).call( b, c, d );
},
_fnapply = function( a, b, c, d ) {
	return ( typeof a === _FUN ? a : Function( d || '', a ) ).apply( b, c || A );
},
_fncreate = function( a, b, c ) {
	return ( typeof a === _FUN ? a : Function( c || '', a ) );
},
// a -> fn, b -> delay, c -> exclusion
_delay = function( a, b, c ) {
    var t;
    return function ( k ) {
    	c && clearTimeout( t );
        if ( k !== F )
        	t = setTimeout( a, b );
    };
},
_returnTrue = function() { return T },
_returnFalse = function() { return F },
_returnEmpty = function() { return '' },
_returnNull = function() { return N },
_argu = function() { return A.slice.apply( arguments.callee.caller.arguments, arguments ) },
_arrfn = function( a ) { return Function( 'v,i,r', 'return(' + a + ')' ) },
_number = function( a ) {
	var r = typeof a === _STR ? parseFloat( a ) : + a;
	return isNaN( r ) ? 0 : r;
},
_numRange = function( a, b, c ) {
	if ( b != N && a < b ) return b;
	if ( c != N && a > c ) return c;
	return a;
},
// js小数相加运算出现多位小数的解决办法
_numAdd = function( a, b ) {
	 var c = _strFrom('' + a, '.').length, d = _strFrom('' + b, '.').length, m = Math.pow(10, Math.max(c, d));
	 return Math.round((a + b) * m) / m;			
},
_strTrim = function( a ) {
	return ('' + a).replace( /^\s+|\s+$/g, '' );
},
_strQuot = function( a ) {
	return ('' + a).replace( /\"/g, '&quot;' );
},
// 在a中取以b开始的字符串(不包括b) /@ c -> last indexOf ?
_strFrom = function( a, b, c ) {
	var d = c ? a.lastIndexOf( b ) : a.indexOf( b );
	return d < 0 ? '' : a.substr( d + b.length );
},
// 在a中取以b结束的字符串(不包括b) /@ c -> last indexOf ?
_strTo = function( a, b, c ) {
	var d = c ? a.lastIndexOf( b ) : a.indexOf( b );
	return d < 0 ? '' : a.substr( 0, d );
},
// 数字前加0  /@ a -> num, b -> 补成多少长度的字符串？
_strPad = function( a, b, c ) {
	c = c == N ? '0' : String( c );
	if ( ! b )
		return a < 10 ? c + a : a;
	a = '' + a;
	while ( a.length < b )
		a = c + a;
	return a;
},
// 取字符串字节数(一个汉字算双字节)  /@ s -> string value, b -> 一个汉字算几个字节？默认2个
_strLen = function( s, b ) {
	if ( ! s ) return 0;
	if ( ! b ) b = _cfg.cn_bytes || 2;
	for ( var i = 0, d = 0, l = s.length; i < l; i ++ )
		d += s.charCodeAt( i ) > 128 ? b : 1;
	return d;
},
// 按照字节数截取字符串 / @a -> str, b -> len, c -> trancateExt 后缀, n -> html entries?
_strSlice = function( a, b, c, n ) {
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
_strUnescape = function( s ) {
	return s != N ? String( s ).replace( /&amp;/g, '&' ).replace( /&lt;/g, '<' ).replace( /&gt;/g, '>' ).replace( /&quot;/g, '"' ) : '';
},
// 对html标签编码
_strEscape = function( s ) {
	return s != N ? String( s ).replace( /&/g, '&amp;' ).replace( /</g, '&lt;' ).replace( />/g, '&gt;' ).replace( /\"/g, '&quot;' ) : '';
},
// 在 a 中查找 b 并给 b 加上标签样式 c /@ a -> str, b -> key, c -> matchLength?, d -> key cls?
_strHighlight = function( a, b, c, d ) {
	return (a = '' + a).replace( RegExp( '(<[\\/\\!]?\\w+(?::\\w+)?\\s*(?:\\w+(?:=(\'|").*?\\2)?\\s*)*>)|(' + _strSplitword( b, c, T ).join( '|' ) + ')', 'ig' ),  function($0, $1, $2, $3) {
	   	return $3 === U ? $1 : '<em class="' + (d || 'f-keyword') + '">' + $3 + '</em>';
	} );
},
// @a -> key, b -> matchLength, c -> regexp?
_strSplitword = function( a, b, c ) {
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
_strFormat = function( a, b, c ) {
	if ( ! b )
		return a;
	if ( b.isWidget ) {
		var x = b.x, d = b.x.data;
		return a.replace( /\$\{?([a-z_]\w*)\}?/gi, function( e, f ) {
			var v = d && d[ f ];
			v == N && (v = x[ f ]);
			return v == N ? '' : c ? v : _urlEncode( v ) } );
	} else {
		if ( _arrIs( b ) ) {
			if ( a.indexOf( 'javascript:' ) === 0 )
				return Function( '$0,$1,$2,$3,$4,$5,$6,$7,$8,$9', a.slice( 11 ) ).apply( win, b || A );
			return a.replace( /\$\{?(\w+)\}?/g, function( $0, $1 ) { return b[ $1 ] == N ? '' : c ? _urlEncode( b[ $1 ] ) : b[ $1 ] } );
		} else {
			return a.replace( /\$\{?([a-z_]\w*)\}?/gi, function( $0, $1 ) { return b[ $1 ] == N ? '' : c ? _urlEncode( b[ $1 ] ) : b[ $1 ] } );
		}
	}
},
// 字符转为regexp支持的模式
_s_regexp = function( a ) {
	return a.replace( /([-.*+?^${}()|[\]\/\\])/g, '\\\$1' );
},
/*  下面定义ID连接字符串的方法 ( 如: 001,002,003 ) 方法名前缀: ids_  */
// 添加一个ID
_idsAdd = function( s, n, p ) {
	if ( ! p ) p = ',';
	return _idsAny( s, n, p ) ? s : ( s ? s + p + n : n );
},
// s是否包含n
_idsAny = function( s, n, p ) {
	if ( s == N || n == N ) return F;
	if ( s == n ) return T;
	if ( ! p ) p = ',';
	return (p + s + p).indexOf( p + n + p ) > -1;
},
// s和n都是逗号隔开的字串，其中有一个相同即可。例如 _idsMatch( "a,b,c", "e,f,a" ); 其中有相同的字符a，结果为true
_idsPair = function( s, n, p ) {
	if ( s == n || ! s || ! n ) return T;
	if ( ! p ) p = ',';
	if ( n.indexOf( p ) > -1 ) {
		for ( var i = 0, b = n.split( p ), l = b.length; i < l; i ++ )
			if ( (p + s + p).indexOf( p + b[ i ] + p ) > -1 ) return T;
	} else
		return (p + s + p).indexOf( p + n + p ) > -1;
},
// 删除一个ID
_idsRemove = function( s, n, p ) {
	if ( s == N ) return '';
	if ( ! p ) p = ',';
	return ( p + s + p ).replace( p + n + p, p ).slice( 1, -1 );
},
// 把数组b压入数组a
_concat = function( a, b ) {
	for ( var i = 0, l = b.length; i < l; i ++ ) a.push( b[ i ] );
	return a;
},
// $.scale的辅助方法
_scaleRange = function( a, b ) {
	if ( b != N ) {
		if ( b.min && ! isNaN( b.min ) ) a = Math.max( a, b.min );
		if ( b.max && ! isNaN( b.max ) ) a = Math.min( a, b.max );
	}
	return a;
},
// 把数字a按照数组b切割
// @example: _scale( 100, [ {value:'*',max:10}, {value:60}, {value:'*'} ] )
//   返回: [ 10, 60, 30 ]
// @example: _scale( null, [ '*', '60', '*' ] )
//   返回: [ null, 60, null ]
_scale = function( a, b ) {
	var l = b.length, i = 0, c = 0, p, s, v, t = 0, r = Array( l );
	for ( ; i < l; i ++ ) {
		v = typeof b[ i ] === _OBJ ? b[ i ].value : b[ i ];
		if ( v != N && isNaN( v ) ) {
			a == N ? (r[ i ] = N) : v === '*' ? ( s = i, t ++ ) : p = i;
		} else
			c += ( r[ i ] = v == N || v < 0 ? N : + v );
	}
	if ( a == N ) return r;
	if ( p !== U ) {
		a = Math.max( 0, a - c );
		var z = 0;
		for ( i = c = 0; i <= p; i ++ ) {
			v = typeof b[ i ] === _OBJ ? b[ i ].value : b[ i ];
			if ( r[ i ] === U && typeof v === _STR && v.indexOf( '%' ) > 0 ) {
				z += (v = parseFloat( v ));
				c += (r[ i ] = _scaleRange( Math.floor( a * v / 100 ), b[ i ] ));
			}
		}
		if ( s === U && z == 100 )
			r[ p ] = _scaleRange( Math.max( 0, r[ p ] + a - c ), b[ i ] );
	}
	if ( s !== U ) {
		a = Math.max( 0, a - c );
		for ( i = c = 0; i <= s; i ++ ) {
			if ( r[ i ] === U ) c += (r[ i ] = _scaleRange( Math.floor( a / t ), b[ i ] ));
		}
		r[ s ] = _scaleRange( Math.max( 0, r[ s ] + a - c ), b[ s ] );
	}
	return r;
},
// 获取b在数组a的位置序号
_index = function( a, b ) {
	if ( a.indexOf ) return a.indexOf( b );
	for ( var i = 0, l = a.length; i < l; i ++ )
		if ( a[ i ] === b ) return i;
	return -1;
},
// b是否存在于数组a中
_arrIn = function( a, b ) { return _index( a, b ) > -1 },
// 是否数组
_arrIs = Array.isArray || function( a ) { return O.toString.call( a ) === '[object Array]' },
_arrMake = function( a ) { return _arrIs( a ) ? a : a == N ? [] : (typeof a.length === _NUM) ? A.slice.call( a ) : [ a ] },
// 转为数组
_map = function( a, b ) {
	 var i = 0, l = a.length, r = [];
	if ( b == N ) {
		for (; i < l; r.push( a[ i ++ ] ) );
	} else {
		if ( typeof b === _STR ) b = _arrfn( b );
	    for( ; i < l; i ++ )
	    	 r.push( b.call( a[ i ], a[ i ], i, a ) );
	}
    return r;
},
// 把函数b应用于所有a数组元素 /a -> array, b -> function, c -> break?
_each = function( a, b, c ) {
	if ( typeof b === _STR ) b = _arrfn( b );
    for( var i = 0, l = a.length; i < l; i ++ )
    	 if( F === b.call( a[ i ], a[ i ], i, a ) ) { if ( c ) break; }
    return a;
},
// 选择符合条件的元素，返回新数组
_arrSelect = function( a, b, c ) {
	if ( typeof b === _STR ) b = _arrfn( b );
    for( var i = 0, l = a.length, r = [], d; i < l; i ++ ) {
    	 if ( d = b.call( a[ i ], a[ i ], i, a ) ) r.push( c ? d : a[ i ] );
    }
    return r;
},
// 找到符合条件的元素并返回该元素 /@a -> array, b -> fn, d -> result?
_arrFind = function( a, b, c ) {
	if ( typeof b === _STR ) b = _arrfn( b );
    for( var i = 0, l = a.length, d; i < l; i ++ )
    	if ( d = b.call( a[ i ], a[ i ], i, a ) ) return c ? d : a[ i ];
},
// 从数组中移除一项
_arrPop = function( a, b ) {
	if ( a && _index( a, b ) > -1 ) a.splice( _index( a, b ), 1 );
},
// 最后一个属性是数组 / a -> obj, b -> target
// example: _jsonArray( obj, target, 'a', 'b' );
// 等同于: target[ 'a' ][ 'b' ].push( obj );
_jsonArray = function( a, b ) {
	for ( var i = 2, l = arguments.length - 1; i < l; i ++ )
		b = b[ arguments[ i ] ] || ( b[ arguments[ i ] ] = {} );
	( b[ arguments[ l ] ] || (b[ arguments[ l ] ] = []) ).push( a );
	return b[ arguments[ l ] ];
},
// example: _jsonChain( obj, target, 'a', 'b' );
// 等同于: target[ 'a' ][ 'b' ] = obj;
_jsonChain = function( a, b ) {
	for ( var i = 2, l = arguments.length - 1, c = b; i < l; i ++ )
		c = c[ arguments[ i ] ] || (c[ arguments[ i ] ] = {});
	c[ arguments[ l ] ] = a;
	return c;
},
// example: var val = _jsonAttr( target, 'a', 'b' );
// 等同于:  var val = target[ 'a' ][ 'b' ];
_jsonAttr = function( a ) {
	for ( var i = 1, l = arguments.length, b = a; i < l; i ++ )
		if ( (b = b[ arguments[ i ] ]) == N ) break;
	return b;
},
// a拷贝b的属性，并移除b的相同属性c /@ c -> "prop1,prop2...propN"
_jsonCut = function( a, b, c ) {
	for ( var i = 0, c = c.split(','), d, l = c.length; i < l; i ++ ) {
		if ( (d = c[ i ]) in b ) {
			a[ d ] = b[ d ];
			delete b[ d ];
		}
	}
	return a;
},

// 把 json 转为字符串
_jsonString = (function() {
	if ( win.JSON )
		return function() { return JSON.stringify.apply( JSON, arguments ) };
	var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
	    es = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
	    meta = { '\b': '\\b', '\t': '\\t', '\n': '\\n', '\f': '\\f', '\r': '\\r', '"' : '\\"', '\\': '\\\\' },
		gap, idn, rep;
	function quote( s ) {
	    es.lastIndex = 0;
	    return es.test( s ) ? '"' + s.replace( es, function( a ) {
	            var c = meta[a];
	            return typeof c === _STR ? c : '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
	        }) + '"' : '"' + s + '"';
	}
	function str( key, h ) {
	    var i, k, v, length, mind = gap, _partial, u = h[ key ];
	    if ( u && typeof u === _OBJ && u instanceof Date )
	        u = _dateFormat( u, 'yyyy-mm-ddThh:ii:ssZ', T );
	    if ( typeof rep === _FUN )
	        u = rep.call(h, key, u);
	    switch ( typeof u ) {
	    case _STR:
	        return quote( u );
	    case _NUM:
	        return isFinite( u ) ? String( u ) : 'null';
	    case 'boolean':
	    case 'null':
	        return String( u );
	    case _OBJ:
	        if ( ! u )
	            return 'null';
	        gap += idn;
	        _partial = [];
	       if ( _arrIs( u ) ) {
	            length = u.length;
	            for ( i = 0; i < length; i ++ ) {
	                _partial[ i ] = str( i, u ) || 'null';
	            }
	            v = _partial.length === 0 ? '[]' : gap ? '[\n' + gap + _partial.join(',\n' + gap) + '\n' + mind + ']' : '[' + _partial.join(',') + ']';
	            gap = mind;
	            return v;
	        }
	        if ( rep && typeof rep === _OBJ ) {
	            length = rep.length;
	            for ( i = 0; i < length; i ++ ) {
	                k = rep[ i ];
	                if ( typeof k === _STR && (v = str(k, u)) ) {
	                	_partial.push(quote(k) + (gap ? ': ' : ':') + v);
	                }
	            }
	        } else {
	            for ( k in u ) {
	                if ( Object.hasOwnProperty.call( u, k ) ) {
	                    if ( v = str( k, u ) )
	                        _partial.push( quote( k ) + ( gap ? ': ' : ':' ) + v );
	                }
	            }
	        }
	        v = _partial.length === 0 ? '{}' : gap ? '{\n' + gap + _partial.join( ',\n' + gap ) + '\n' + mind + '}' : '{' + _partial.join(',') + '}';
	        gap = mind;
	        return v;
	    }
	}
	return function( u, r, s ) {
	    var i;
	    gap = '', idn = '';
	    if ( typeof s === _NUM ) {
	        for ( i = 0; i < s; i ++ )
	            idn += ' ';
	    } else if ( typeof s === _STR ) {
	        idn = s;
	    }
	    rep = r;
	    if ( r && typeof r !== _FUN && (typeof r !== _OBJ || typeof r.length !== _NUM ) ) {
	        throw new Error( 'JSON.stringify' );
	    }
	    return str( '', { '' : u } );
	};
}()),
_jsonParse = (function() {
	return win.JSON ? function( a ) { return ! a ? N : JSON.parse.call( JSON, a ) } : function( a ) { return a == N ? N : (Function('return ' + a))() }
})(),
_jsonClone = (function() {
	return win.JSON ? function( a ) { return typeof a === _OBJ ? _jsonParse( _jsonString( a ) ) : a } :
		function( a ) {
		    if( typeof a === _OBJ ) {
		        if ( _arrIs( a ) ) {
		            for( var i = 0, b = []; i < a.length; i ++ )
		            	b.push( _jsonClone( a[ i ] ) );
		            return b;
		        } else {
		            var o = {}, k;
		            for ( k in a )
		                o[ k ] = _jsonClone( a[ k ] );
		            return o;
		        }
		    }
		    return a;
		}
})(),
_now = function() { return ( new Date() ).getTime() },
// 一分钟的毫秒数
_date_I = 60000,
// 一小时的毫秒数
_date_H = 3600000,
// 一天的毫秒数
_date_D = 86400000,
// 标准格式化字符串
_date_sf = 'yyyy-mm-dd hh:ii:ss',
// 格式化日期
_dateFormat = function( a, b ) {
	if ( typeof a === _STR )
		a = _dateParse( a, b );
	var o = { y : a.getFullYear(), m : a.getMonth(), d : a.getDate(), h : a.getHours(), i : a.getMinutes(), s : a.getSeconds(), w : ( a.getDay() || 7 ) };
	return (b || _date_sf).toLowerCase().replace( 'yyyy' , o.y ).replace( 'yy', o.y % 100 ).replace( 'mm', _strPad( o.m + 1 ) ).replace( 'dd', _strPad( o.d ) ).replace( 'hh', _strPad( o.h ) )
		.replace( 'ii', _strPad( o.i ) ).replace( 'ss', _strPad( o.s ) ).replace( 'm', o.m + 1 ).replace( 'd', o.d ).replace( 'h', o.h ).replace( 'i', o.i ).replace( 's', o.s );
},
// 字串型转为日期型 /@s -> str, f -> format?
_dateParse = function( s, f ) {
	s = '' + s;
	if ( f ) {
		var g = f.charAt( 0 );
		if ( g == 'm' )
			s = '2017-' + s;
		else if ( g == 'd' )
			s = '2017-01-' + s;
		else if ( g == 'h' )
			s = '2017-01-01 ' + s;
		else if ( g == 'i' )
			s = '2017-01-01 00:' + s;
	}
	var b = s.split( '-' );
	if ( b.length === 1 )
		s += '-02-01';
	else if ( b.length === 2 )
		s += '-01';
	var a = new Date( s.replace( /-/g, '/' ) );
	if ( isNaN( a ) )
		a = new Date( s.split( ' ' )[ 0 ].replace( /-/g, '/' ) );
	return isNaN( a ) ? new Date : a;
},
// 日期增减  /@ a -> date, b -> type enum ( y/m/d/h/i/s ), c -> value
_dateAdd = function( a, b, c ) {
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
_dateWeek = function( a, b, c ) {
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
_dateValid = function( a, b ) {
	a = a.replace( /\b(\d)\b/g, '0$1' );
	var d = _dateParse( a, b ), y = d.getFullYear();
	return y >= (_cfg.min_year || (_cfg.min_year = 1000)) && y <= (_cfg.max_year || (_cfg.max_year = 3000)) && a == _dateFormat( d, b );
},
// url 编码
_urlEncode = function( a ) { return a == N ? '' : encodeURIComponent( a ) },
// url 解码
_urlDecode = function( a ) { return a == N ? '' : decodeURIComponent( a.replace( /\+/g, ' ' ) ) },
// 替换带$变量的url，如 "a.sp?id=$0" /@a -> url, b -> array/object
_urlFormat = function( a, b ) {
	return _strFormat( a, b, T );
},
// 以 a 为基础，解析出 b 的路径
_urlLoc = function( a, b ) {
	a = _strTo( a, '/', T );
	return b.indexOf( './' ) === 0 ? _urlLoc( a + '/', b.slice( 2 ) ) : b.indexOf( '../' ) === 0 ? _urlLoc( a, b.slice( 3 ) ) : b.charAt( 0 ) === '/' ? b : a + '/' + b;
},
// @ a-> url, b -> name/object
_urlParam = function( a, b ) {
	var r = a.split( '#' ), u = r[ 0 ], h = r[ 1 ];
	if ( typeof b === _STR ) {
		return b === '#' ? h : (u.match( new RegExp('[\\?&]' + b + '=([^&]*)'), 'g' ) || A)[ 1 ];
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
_ease = function( a, b, c ) {
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
_cookie = function( a, b, c, d ) {
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
_win = function( o ) { return o ? _doc( o )[ ie ? 'parentWindow' : 'defaultView' ] : win },
_doc = function( o ) { return o != N && o.ownerDocument || o },
_cvs = function( o ) { return o ? o.ownerDocument.documentElement : cvs },
// s -> html string
_div = function( s ) {
	var o = doc.createElement( 'div' );
	if ( s ) o.innerHTML = s;
	try { return o } catch( e ) { return F }
	finally { o = N }
},
// 创建一个fragment, 把内容放进去
_frag = function( s ) {
	var d = typeof s === _STR ? _div( s ) : s, f = doc.createDocumentFragment();
	while ( d.firstChild )
		f.appendChild( d.firstChild );
	try { return f } catch( e ) { return F }
	finally { d = f = N }
},
db = function( a, b ) { return a ? ( _append( b || doc.body, a ), doc.body.lastChild ) : doc.body },
// 简写 getElementsByTagName
_tags = function( a, b ) { return (b || doc).getElementsByTagName( a ) },
// 获取nextSibling元素
_next = function( a ) {
	while ( ( a = a.nextSibling ) )
		if ( a.nodeType === 1 ) return a;
},
// 获取previousSibling元素
_prev = function( a ) {
	while ( ( a = a.previousSibling ) )
		if ( a.nodeType === 1 ) return a;
},
// 是否html节点
_isHTML = function( a ) {
	var b = a && (a.ownerDocument || a).documentElement;
	return b ? b.nodeName === 'HTML' : F;
},
_parseHTML = function( a, b ) {
	if ( (a = String( a )).indexOf( '<d:wg>' ) > -1 ) {
		return a.replace( /<d:wg>([\s\S]+?)<\/d:wg>/gi, function( $0, $1 ) {
			return (b || $.vm()).add( Function( $1.indexOf( 'javascript:' ) === 0 ? $1 : 'return(' + $1 + ')' ).call( b || $.vm() ), -1 ).html();
		} );
	}
	return a;
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
    _css( oDiv, { 'width' : 100, 'height' : 100 } );
*/
_css = function( o, s ) {
	if ( ! o || ! s )
		return N;
	var l = arguments.length, i;
	if ( l > 2 ) {
		for ( i = 1; i < l; i += 2 )
			_set_style( o, arguments[ i ], arguments[ i + 1 ] );
	} else {
		if ( typeof s === _STR )
			return o.style[ s ] || o.currentStyle[ s ];
		for ( i in s ) _set_style( o, i, s[ i ] );
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
_boxwd = function( o, w ) {
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
_boxht = function( o, h ) {
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
_html = function( o, s, r ) {
	if ( r == N ) {
		if ( s == N ) return o.innerHTML;
		try { o.innerHTML = s } catch( ex ) { _html( _empty( o ), s, 3 ) }
	} else {
		if ( typeof s === _STR ) {
			if ( s.indexOf( '<d:wg>' ) > -1 )
				s = _parseHTML( s, $.w( r % 2 ? o : o.parentNode ) );
			try {
				o.nodeType === 3 || br.ie ? $.query( o )[ _adjacent_query[ r ] ]( s ) : o.insertAdjacentHTML( _adjacent_where[ r ], s );
			} catch ( ex ) {
				_html( o, _frag( s ), r );
			}
		} else {
			var p = r % 2 ? o : o.parentNode;
			p && p.insertBefore( s, r === 0 ? o.nextSibling : r === 1 ? o.firstChild : r === 2 ? o : N );
	}
	}
},
_append  = function( o, s ) { _html( o, s, 3 ); return o.lastChild },	
_prepend = function( o, s ) { _html( o, s, 1 ); return o.firstChild },
_before  = function( o, s ) { _html( o, s, 2 ); return o.previousSibling },
_after   = function( o, s ) { _html( o, s, 0 ); return o.nextSibling },
_replace = function( o, s ) {
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
_empty = function( o ) {
	while ( o.firstChild )
		o.removeChild( o.firstChild );
	return o;
},
_show = function( a, b ) { _classRemove( a, 'f-none' ) },
_hide = function( a ) { _classAdd( a, 'f-none' ) },
// 添加一个class  /@ a -> el, b -> className, c -> add?(默认添加)
_classAdd = function( a, b, c ) {
	var s = a.className;
	if ( b.indexOf( ' ' ) > -1 ) {
		b = _strTrim( b ).split( / +/ );
		for ( var i = 0, l = b.length; i < l; i ++ )
			s = ( c === F ? _idsRemove : _idsAdd )( s, b[ i ], ' ' );
	} else
		s = ( c === F ? _idsRemove : _idsAdd )( s, b, ' ' );
	a.className = s;
},
// 删除一个class  /@ a -> el, b -> className
_classRemove = function( a, b ) {
	if ( b.indexOf( ' ' ) > -1 ) {
		_classAdd( a, b, F );
	} else
		a.className = _idsRemove( a.className, b, ' ' );
},
// 是否包含任意一个class(多个是“或”的关系)  /@ a -> el or className
_classAny = function( a, b ) {
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
_classReplace = function( a, b, c ) {
	_classRemove( a, b ), _classAdd( a, c )
},
// 有则删除，无则添加 / a -> el, b -> className
_classToggle = function( a, b ) {
	_classAdd( a, b, ! _classAny( a, b ) );
},
_rm = function( a ) {
	if ( typeof a === _STR ) a = $( a );
	a && a.parentNode && a.parentNode.removeChild( a );
},
_bcr = function( o ) {
	var v = _cvs(),  b = v.clientLeft, c = v.scrollLeft, d = v.clientTop, e = v.scrollTop, a = o && o !== doc && (o.isWidget ? o.$() : o).getBoundingClientRect();
	return a ? { left : a.left - b + c, top : a.top - d + e, right : a.right - b + c, bottom : a.bottom - d + e, width : a.right - a.left, height : a.bottom - a.top } :
		{ left: c, top: e, right: v.clientWidth + c, bottom: v.clientHeight + e, width: v.clientWidth, height: v.clientHeight };
},
_offset = function( o ) {
	var v = _cvs(), a = _bcr( o );
	a.right  = v.clientWidth - a.right;
	a.bottom = v.clientHeight - a.bottom;
	return a;
},
_width = function() { return cvs.clientWidth },
_height = function() { return cvs.clientHeight },
_topWidth = function() { return cvs.clientWidth + cvs.scrollLeft },
_topHeight = function() { return cvs.clientHeight + cvs.scrollTop },
_arr_comma = function( a ) { return a.split( ',' ) },
_snaptype = {
	h : _arr_comma( '21,12,34,43' ),
	v : _arr_comma( '41,14,32,23' ),
	a : _arr_comma( '41,32,14,23,21,34,12,43,11' )
},
_snapindent = { h: {'21':-1,'34':-1,'12':1,'43':1}, v: {'14':1,'23':1,'41':-1,'32':-1} },
// 获取对齐吸附模式的位置参数。元素四个角，左上为1，右上为2，右下为3，左下为4。目标元素在前，浮动元素在后。如"41"代表目标元素的左下角和浮动元素的左上角粘合。
// @ a -> width, b -> height, c -> toObj offset, d -> adsorb type, e - > fit?[是否自动选择位置，让可见区域最大化], f -> indent[缩进多少像素], u -> range?[浮动元素的限定范围元素]
_snap = function( a, b, c, d, e, f, u ) {
	if ( ! c )
		c = _bcr();
	else if ( c.nodeType )
		c = _bcr( c );
	else if ( c.isWidget )
		c = _bcr( c.$() );
	else if ( c.clientX != N )
		c = { left: c.clientX, right: c.clientX, top: c.clientY, bottom: c.clientY };
	var t = [], l = [], g, h, k = -1, f = _number( f ), ew = _width(), eh = _height(),
		s = d ? (_snaptype[ d ] || ('' + d).split( ',' )) : _snaptype.a;
	if ( /[1-4]/.test( s[ 0 ] ) ) { // 1-4是边角对齐 .
		for ( var i = 0, o, p, m, n = 0; i < s.length; i ++ ) {
			g = + s[ i ].charAt( 0 ), h = + s[ i ].charAt( 1 );
			t.push( ( g === 1 || g === 2 ? c.top : c.bottom ) - ( h === 3 || h === 4 ? b : 0 ) + (f && (_snapindent.v[ s[ i ] ] || 0) * f) );
			l.push( ( g === 1 || g === 4 ? c.left : c.right ) - ( h === 2 || h === 3 ? a : 0 ) + (f && (_snapindent.h[ s[ i ] ] || 0) * f) );
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
// @a -> el, b -> snap option
_snapTo = function( a, b ) {
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
// @a -> src, b -> post json?
_download = function( a, b ) {
	var c = Q( '<div class=f-none><iframe src="about:blank" name=xx></iframe></div>' );
	if ( b ) {
		var f = document.createElement( 'form' ), u = '_download_' + $.uid();
		f.action = a;
		f.target = u;
		f.method = 'post';
		for ( var i in b ) {
			if ( _arrIs( b[ i ] ) ) {
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
// a -> el, b -> event type, c -> fn, d -> T(attach),F(detach)
_attach = function( a, b, c, d ) {
	if ( d === F )
		return _detach( a, b, c );
	if ( ~b.indexOf( ' ' ) ) {
		for ( var i = 0, d = b.split( ' ' ); i < d.length; i ++ )
			d[ i ] && _attach( a, d[ i ], c );
	}
	ie ? a.attachEvent( 'on' + b, c ) : a.addEventListener( b, c, F );
},
// a -> el, b -> event type, c -> fn
_detach = function( a, b, c, d ) {
	if ( ~b.indexOf( ' ' ) ) {
		for ( var i = 0, d = b.split( ' ' ); i < d.length; i ++ )
			d[ i ] && _detach( a, d[ i ], c );
	}
	ie ? a.detachEvent( 'on' + b, c ) : a.removeEventListener( b, c, F );
},
_stop = function( a ) {
	if ( a || ( a = win.event ) )
		a.stopPropagation ? (a.stopPropagation(), a.preventDefault()) : (a.cancelBubble = T, a.returnValue = F);
},
_cancel = function( a ) {
	if ( a || ( a = win.event ) )
		a.stopPropagation ? a.stopPropagation() : (a.cancelBubble = T);
},
// 记录鼠标事件发生的坐标
_event_click = { click: T, dblclick: T, contextmenu: T, mousedown: T, mouseup: T },
_point = function( e ) {
	if( (e || (e = window.event)) && _event_click[ e.type ] )
		_point.clientX = e.clientX, _point.clientY = e.clientY;
},
_rngSelection = (function() {
	return win.getSelection ? function() { var s = win.getSelection(); return s.rangeCount && s.getRangeAt( 0 ); } : function() { return doc.selection.createRange(); };
})(),
_rngElement = (function() {
	return win.getSelection ? function() { var s = _rngSelection(); return s && s.startContainer.parentNode; } : function() { return doc.selection.createRange().parentElement(); };
})(),
_rngCursor = function( a, b ) {
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
},
_rngCursorOffset = function() {
	var n = _rngSelection(), c = n.startOffset;
	if ( c != N )
		return c;
	var r = doc.body.createTextRange();
	r.moveToElementText( n.parentElement() );
	r.setEndPoint( 'EndToStart', n );
	return _rngText( r ).length;
},
// @a ->range, b -> text?
_rngText = function( a, b ) {
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
_xmlParse = function( a ) {
	var d = 'ActiveXObject' in win ? new ActiveXObject( 'MSXML2.DOMDocument' ) : doc.implementation.createDocument( '', '', N );
	if ( 'setProperty' in d )
		d.setProperty( 'SelectionLanguage', 'XPath' );
	if ( a ) {
		d.loadXML( a );
		return d.documentElement;
	}
	return d;
},
_xmlQuery = function( a, b ) { return a.selectSingleNode( b ) },
_xmlQueryAll = function( a, b ) { return a.selectNodes( b ) },
// @a -> move fn, b -> up fn, c -> el
_moveup = function( a, b, c ) {
	var d;
	ie ? _attach( doc, 'selectstart', _returnFalse ) : _classAdd( cvs, 'f-unsel' );
	_attach( doc, 'mousemove', d = function( e ) { a( ie ? Q.event.fix( e ) : e ) }, T );
	_attach( doc, 'mouseup', function( e ) {
		b && b( ie ? Q.event.fix( e ) : e );
		_detach( doc, 'mousemove', d, T );
		_detach( doc, 'mouseup', arguments.callee, T );
		ie ? _detach( doc, 'selectstart', _returnFalse ) : _classRemove( cvs, 'f-unsel' );
		c && _rm( c );
	}, T );
},
_animate_speed = { fast: 200, normal: 500, slow: 1000 },
// @a -> el, b -> type, c -> fast|normal|slow (.2s|.5s|1s), d -> fn 结束后执行的函数
_animate = function( a, b, c, d ) {
	c = c || 'fast';
	var s = 'f-animated f-speed-' + ( c || 'fast' ) + ' f-ani-' + b;
	_classAdd( a, s );
	setTimeout( function() {
		_classRemove( a, s );
		d && d();
	}, _animate_speed[ c ] || c );
},
_img_dot = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==',
// @a -> src, b -> feature { id: '', cls: '', style: '', click: '', tip: '' }
_image = function( a, b, c ) {
	var d = a, e = ! d.indexOf( '.' ) && d.indexOf( '/' ) < 0, s = t = '';
	if ( ! e && d.indexOf( '%' ) > -1 ) {
		d = d.replace( '%img%', _ui_path + 'g' );
	}
	if ( b ) {
		if ( b.id ) s += ' id=' + b.id;
		if ( b.cls ) s += ' class="' + b.cls + '"';
		if ( b.style ) s += ' style="' + b.style + '"';
		if ( b.click ) s += ' onclick="' + _strQuot( b.click ) + '"';
	}
	if ( c ) {
		if ( c.id ) t += ' id=' + c.id;
		if ( c.cls ) t += ' class="' + c.cls + '"';
		if ( c.style ) t += ' style="' + c.style + '"';
		if ( c.click ) t += ' onclick="' + _strQuot( c.click ) + '"';
		if ( c.tip ) t += ' title="' + _strQuot( c.tip ) + '"';
	}
	return '<span' + s + '>' + ( e ? '<em class="_ico f-i ' + d.replace( /\./g, '' ) + '"' + t + '></em>' : (d ? '<img src="' + d + '" class="_ico f-va"' + t + (b && b.width ? ' width=' + b.width : '') + (b && b.height ? ' height=' + b.height : '') + '>' : '') ) + '<i class=f-vi></i></span>';
},
_arrow = function( a, b ) {
	var c = b || a;
	if ( a && a.nodeType )
		a.className = 'f-arw f-arw-' + c;
	else
		return '<em class="f-arw f-arw-' + ( c || 'b1' ) + '"' + ( b ? ' id=' + a : '' ) + '></em>';
},
// @a -> image array, b -> id
_previewImage = function( a, b ) {
	if ( typeof a === _STR )
		a = _jsonParse( a );
	for ( var i = 0, d = a[ 0 ]; i < a.length; i ++ ) {
		if ( a[ i ].id == b ) {
			d = a[ i ];
			break;
		}
	}
	var w = Math.max( 600, _width() - 300 ), h = Math.max( 400, _height() - 100 ),
		d = $.vm().cmd( { type: 'dialog', cls: 'f-dialog-preview', width: w, height: h, cover: T, pophide: T,
			node: { type: 'html', align: 'center', valign: 'middle', text: '<img src=' + (d.url || d.thumbnail) + ' style="max-width:' + (w - 30) + 'px;max-height:' + h + 'px"><em class="f-i _dlg_x" onclick=' + $.abbr + '.close(this)></em>' } } );
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
_thumbnail = (function() {
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
				if ( typeof fn === 'string' ) { // fn范例: preview.jsp?src=$0&title=$1
					$.query( img ).wrap( '<a href="' + _urlFormat( fn, [ img.src, img.alt || img.title ] ) + '" target=_blank></a>' );
				} else if ( typeof fn === 'function' ) { // fn范例: function(){ window.open( this.src ) }
					$.query( img ).click( fn );
				}
			}
		}
	}
	function _prev( $img, g ) {
		var n = _index( $img, g );
		return n > 0 ? $img[ n - 1 ] : null;
	}
	function _next( $img, g ) {
		var n = _index( $img, g );
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
		var w = $.width() * .9, h = $.height() - 60, p = _prev( $img, g ), n = _next( $img, g ), k = _index( $img, g ),
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

// Event Node 是 widget 的基础类
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
		_concat( r, _arrSelect( c[ type ], 'v.ns.indexOf("' + ns + '")>-1' ) );
	}
	return r;
},
// 节点类
_Node = _createClass( {
	Const: function() {},
	Helper: {
		// 对n节点的祖先元素逐个判断是否符合条件，条件有连续性，一旦连续性终止，返回最后一个符合条件的祖先元素 
		edge: function( n, a ) {
			if ( typeof a === _STR ) a = _arrfn( a );
			var b = n;
			while ( b = b.parentNode ) {
				if ( ! a.call( b.parentNode ) )
					return b;
			}
		}
	},
	Prototype: {
		length: 0,
		// 添加一个子节点  /@ a -> node, b -> nodeIndex [ number: 节点序号; /-1: 离散节点
		addNode: function( a, b ) {
			// 已有父节点时，先从父节点中解除
			if ( a.nodeIndex != N )
				_Node.prototype.removeNode.call( a, T );
			var l = this.length,
				n = b == N ? l : Math.max( -1, Math.min( l, b ) );
			a.parentNode = this;
			if ( a.Const.Rooter ) {
				var r = this.rootNode || (this.NODE_ROOT && this);
				r && a.Const.Rooter === r.type && b !== -1 && (a.rootNode = r);
			}
			if ( n != N ) {
				a.nodeIndex = n;
				if ( n === -1 ) {
					( this.discNodes || (this.discNodes = {}) )[ _uid( a ) ] = a;
				} else {
					// 重新整理并编号
					for ( var i = l; i > n; i -- )
						( this[ i ] = this[ i - 1 ] ).nodeIndex = i;
					this.length ++;
					this[ n ] = a;
				}
			}
			return a;
		},
		// a -> 设为true, 只解除当前节点的关系
		removeNode: function( a ) {
			if ( a !== T ) {
				for ( var i = this.length - 1; i >= 0; i -- )
					arguments.callee.call( this[ i ] );
			}
			var p = this.parentNode;
			if ( p ) {
				if ( this.nodeIndex > -1 ) {
					for ( var i = this.nodeIndex, l = p.length - 1; i < l; i ++ )
						( p[ i ] = p[ i + 1 ] ).nodeIndex = i;
					p.length --;
					delete p[ i ];
				} else if ( p.discNodes )
					delete p.discNodes[ this.id ];
			}
			delete this.nodeIndex; delete this.parentNode; delete this.rootNode;
		}
	}
} ),
_Event = _createClass( {
	Const: function() {},
	Prototype: {
		// type -> event type, fn -> fn, pvdr -> context, one -> exec once [T/F]
		addEvent: function( type, fn, pvdr, one ) {
			var k = { id : _uid( this ), type : _event_name( type ), ns : _event_space( type ),
					fn : fn, pvdr : pvdr, one : one };
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
						if ( b ? (d[ i ].fn === b && (! c || c === d[ i ].pvdr)) : T ) _event_remove( d[ i ] );
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
} ),
// @a -> widget, b -> option
_draggable = function( a, b ) {
	if ( _drag_cache[ a.id ] )
		return;
	! b && (b = {});
	new Drag( a, b );
	$.query( a.$() ).on( 'mousedown', function( e ) {
		var ix = e.pageX, iy = e.pageY, sn, t, h, p, q, d = '_dndhelper', g = d + ':g',
			dp = function( c ) {
				return c.dnd_wid ? $.all[ c.dnd_wid ] : ((c = $.widget( c )) && c.closest( function() { return _drop_cache[ this.id ] && _idsPair( _drop_cache[ this.id ].cst.scope, b.scope ); } ))
			};
		$.moveup( function( e ) {
			var x = e.pageX, y = e.pageY, m = e.srcElement;
			// 鼠标按下并移动5像素后才能触发拖动，避免误操作
			! t && (Math.abs(x - ix) >= 5 || Math.abs(y - iy) >= 5) && (t = $.db( '<div style="position:absolute;background:#f2f2f2;padding:2px;border:1px dashed #ddd;opacity:.6;z-index:2">' + (a.attr( 'text' ) || '&nbsp;') + '</div>' ));
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
},
_droppable = function( a, b ) {
	if ( _drop_cache[ a.id ] )
		return;
	new Drop( a, b || {} );
},

_ajax_xhr = (function() {
	var a = function() { return new XMLHttpRequest() },
		b = function() { return new ActiveXObject( 'MSXML2.XMLHTTP' ) },
		c = function() { return new ActiveXObject( 'Microsoft.XMLHTTP' ) };
	// 有些ie浏览器的 XMLHttpRequest 实例化后不能调用方法，需要先试试open方法能不能用
	try { a().open( 'GET', '/', T ); return a; } catch( e ) {}
	try { b(); return b; } catch( e ) {}
	try { c(); return c; } catch( e ) {}
	alert( 'Cannot create XMLHTTP object!' );
})(),
_ajax_url = function( a ) {
	return a.indexOf( './' ) == 0 || a.indexOf( '../' ) == 0 ? _urlLoc( _cfg.path, a ) : a.indexOf( 'http://' ) == 0 || a.indexOf( 'https://' ) == 0 ? a : (_cfg.server || '') + a;
},
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
					x.error && _fnapply( x.error, x.context, [ self ], '$ajax' );
				} else {
					c.addEvent( 'cache', this.sendCache, this );
				}
			} else {
				_ajax_cache[ u ] = this;
				this.send();
			}
		},
		send: function() {
			var x = this.x, a = _ajax_url( x.src ), b = x.success, c = x.context, d = x.sync, e = x.data, f = x.error != N ? x.error : _cfg.ajax_error, g = x.dataType, u = a, l, i, self = this;
			if ( typeof e === _OBJ ) {
				var s = [];
				for ( i in e ) {
					if ( _arrIs( e[ i ] ) ) {
						for ( var j = 0; j < e[ i ].length; j ++ )
							s.push( i + '=' + _urlEncode( e[ i ][ j ] ) );
					} else
						s.push( i + '=' + _urlEncode( e[ i ] ) );
				}
				e = s.join( '&' );
			}
			// get url超过长度则转为post
			if ( ( a.length > 2000 && a.indexOf( '?' ) > 0 ) ) {
				e = _strFrom( a, '?' ) + ( e ? e + '&' : '' );
				u = _strTo( a, '?' );
			}
			if ( x.base || _path )
				u = _urlLoc( x.base || _path, u );
			(l = _ajax_xhr()).open( e ? 'POST' : 'GET', u, ! d );
			this.request = l;
			if ( x.beforesend && _fnapply( x.beforesend, c, [ self ], '$ajax' ) === F )
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
						r = l.status;
			        if ( r != N ) {
			        	self.errorCode = l.status;
						if ( f !== F && l.status ) {
							if ( f ) {
								_fnapply( f, c, [ self ], '$ajax' );
							} else {
								$.alert( _cfg.debug ? 'ajax error ' + l.status + ': ' + a + '\n\n' + ( $.loc ? $.loc.ajax[ r ] : r + ' error' ) : $.loc.internet_error );
								win.console && console.error( 'ajax error ' + l.status + ': ' + a + ((r = l.responseText) ? '\n' + r : '') );
							}
						}
				    } else {
				    	x.filter && (m = _fnapply( x.filter, c, [ m, self ], '$value,$ajax' ));
				    	self.response = m;
						b && b.call( c, m, self );
						_ajax_cache[ a ] === self && self.fireEvent( 'cache' );
					}
					x.complete && x.complete.call( c, m, self );
				}
			}
			l.onreadystatechange = _onchange;
			if ( d ) {
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
			_ajaxAbort( this );
			delete _ajax_cache[ this.x.src ];
		}
		
	}
} ),

// a -> url, b -> callback, c -> context, d -> sync?, e -> data, f -> error hdl, g -> type
_ajax = function( a, b, c, d, e, f, g ) {
	return new Ajax( a, b, c, d, e, f, g );
},
// a -> url, b -> callback, c -> context, d -> sync?, e -> data, f -> error hdl
_ajaxXML = function( a, b, c, d, e, f ) {
	return new Ajax( a, b, c, d, e, f, 'xml' );
},
// a -> url, b -> callback, c -> context, d -> sync?, e -> data, f -> error hdl
_ajaxJSON = function( a, b, c, d, e, f, g ) {
	return new Ajax( a, b, c, d, e, f, 'json' );
},
_ajaxAbort = function( a ) {
	var b = _uid( a ), c = _ajax_contexts[ b ];
	if ( c ) {
		var i = c.length;
		while ( i -- ) {
			 (c[ i ]._aborted = T), c[ i ].request.abort(), clearTimeout( c[ i ]._timer );
		}
		c.length = 0;
		delete _ajax_contexts[ b ];
	}
},
_ajaxClean = function( a ) {
	delete _ajax_cache[ a ];
},
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
},
// 装载并运行script。这里js的运行环境是window /@a -> src, b -> callback
_script = function( a, b ) {
	_loadJs( a, function( d ) {
		for ( var i = 0; i < d.length; i ++ )
			d[ i ] && (win.execScript ? execScript( d[ i ] ) : win.eval( d[ i ] ));
		b && b();
	}, ! b );
};

// 浏览器兼容
function _compat() {
	br.mobile ? _compatMobile() : _compatPC();
	var tmp;
	if ( ! ('ActiveXObject' in win) ) {
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
				for ( var i = 0, n = 'click,dblclick,mouseover,mouseout,mousedown,mouseup,mousemove,input,keydown,keypress,keyup,dragstart'.split( ',' ); i < n.length; i ++ )
					doc.addEventListener( n[ i ], function( e ) { win.event = e; }, T );
			}
		} );
		(tmp = doc.createEvent( 'HTMLEvents' )).initEvent( 'eventemu', T, T );
		win.dispatchEvent( tmp );
	}	
}
function _compatPC() {
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
	if ( ! tmp.innerText ) {
		HTMLElement.prototype.__defineSetter__( 'innerText', function( s ) { return this.textContent = s } );
		HTMLElement.prototype.__defineGetter__( 'innerText', function() { return this.textContent } );
	}
	if ( ! tmp.insertAdjacentHTML ) {
		HTMLElement.prototype.insertAdjacentHTML = function(sWhere, sHTML) {
			var r = this.ownerDocument.createRange();
			switch (String(sWhere).toLowerCase()) {
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
function _compatMobile() {
	// 实现tap事件
	var n, t, tmp;
	$.query( doc ).on( 'touchstart', function( e ) {
	    n = e; t = T;
	}).on( 'touchmove', function() {
	    t = F;
	}).on( 'touchend', function( e ) {
	    if ( t ) {
	    	n.type = 'tap';
	    	$.query( e.target ).trigger( n );
	    }
	    n = t = N;
	});
	// 检测回退键
	if ( win.plus ) {
		plus.key.addEventListener( 'backbutton', function() { 
			var w = plus.webview.currentWebview(), p = w.parent();
			$.closeAll(w);
		} );
	}
}

/* 初始化配置 */
function _initEnv() {
	_ver     = _cfg.ver ? '?ver=' + _cfg.ver : '',
	_path    = _cfg.path;
	_ui_path = _urlLoc( _path, _cfg.lib ) + 'ui/';
	if ( noGlobal || _cfg.no_conflict ) {
		(Date.$ = $).abbr = 'Date.$';
	}
	var _define  = new Define( _path ),
		_require = new Require( _path ),
		_lib     = _cfg.lib + 'wg/',
		_loc     = _require( _lib + 'loc/' + (_cfg.lang || 'zh_CN') ),
		_jq      = _loc && _require( _lib + 'jquery/jquery-' + (br.mobile ? '1.12.4' : '3.3.1') );
	if ( ! _loc )
		return alert( 'path is not exist:\n{\n  path: "' + _path + '",\n  lib: "' + _cfg.lib + '"\n}' );
	for ( var k in _cfg.alias ) {
		for ( var i = 0, b = k.split( ',' ); i < b.length; i ++ )
			_alias[ _mod_uri( _path, b[ i ] ) ] = _cfg.alias[ k ];
	}
	_define( 'dfish',  function() { return $ } );
	_define( 'jquery', function() { return _jq } );
	_define( 'loc',    function() { return _loc } );
	
	$.PATH = _path;
	$.IMGPATH = _ui_path + 'g/';
	
	$.loc     = _loc;
	$.query   = _jq;
	$.define  = _define;
	$.require = _require;
	$.skin( _cfg.skin );

	var w = _require( _lib + 'widget' );
	$.all    = w.all;
	$.vm     = w.vm;
	$.e      = w.e;
	$.widget = $.w = w.w;
	$.dialog = _require( 'dialog' ).get;
	$.scrollIntoView = w.scrollIntoView;
	
	if ( noGlobal || _cfg.no_conflict ) {
		('dfish' in $G) && (win.dfish = $G.dfish);
	} else {
		win.$  = win.dfish  = $;
		win.Q  = win.jQuery = _jq;
		win.VM = $.vm;
	}
	// ie 需要定义 xmlns:d="urn:dfish" 用以解析 <d:wg> 标签
	doc.namespaces && doc.namespaces.add( 'd', 'urn:dfish' );
}
function _initDocView( $ ) {
	$.ready( function() {
		_compat();
		// 生成首页view
		if ( _cfg.view ) {
			$.widget( _extend( _cfg.view, { type: 'view', width: '*', height: '*' } ) ).render( db() );
		} else {
			// 把 <d:wg> 标签转换为 widget
			for ( var i = 0, d = _map( _tags( br.css3 ? 'd:wg' : 'wg' ) ), j, l = d.length; i < l; i ++ ) {
				if ( eval( 'j = ' + d[ i ].innerHTML.replace( /&lt;/g, '<' ).replace( '&gt;', '>' ) ) )
					$.widget( j ).render( d[ i ], 'replace' );
			}
		}
		// ie6及以下浏览器，弹出浮动升级提示
		if ( br.ie && br.ieVer < 7 ) {
			VM().cmd({ type: 'tip', cls: 'f-shadow', text: '<div style="float:left;padding:10px 30px 0 0;">' + $.loc.browser_upgrade + '</div><div style="float:left;line-height:4"><a target=_blank title=Chrome href=' + (_cfg.support_url ? _urlFormat( _cfg.support_url, ['chrome'] ) : 'https://www.baidu.com/s?wd=%E8%B0%B7%E6%AD%8C%E6%B5%8F%E8%A7%88%E5%99%A8%E5%AE%98%E6%96%B9%E4%B8%8B%E8%BD%BD') + '>' +
				_image( '.f-i-chrome' ) + '</a> &nbsp; <a target=_blank title=IE href=' + (_cfg.support_url ? _urlFormat( _cfg.support_url, ['ie'] ) : 'https://support.microsoft.com/zh-cn/help/17621/internet-explorer-downloads') + '>' + _image( '.f-i-ie' ) + '</a></div>', width: '*', snap: doc.body, snaptype: 'tt', prong: F});
		}
	} );
	// 调试模式
	if ( _cfg.debug ) {
		$.query( doc ).contextmenu( function( e ) {
			if ( e.ctrlKey && ! $( ':develop' ) ) {
				var m = $.vm( e.target ), c = $.bcr( m.$() ),
					d = $.query( doc.body ).append( '<div id=:develop style="width:' + (c.width - 4) + 'px;height:' + (c.height - 4) + 'px;left:' + c.left + 'px;top:' + c.top + 'px;position:absolute;border:2px dashed red;z-index:2"></div>' ),
					g = m.closest( 'dialog' ), s = 'path: ' + m.path,
					t = 'path: ' + m.path + (g ? '\ndialog: ' + (g.x.id || '') : '') + '\nsrc: <span onclick=event.ctrlKey&&window.open(this.innerText)>' + (m.x.src || '') + '</span>';
				$.vm ? $.vm().cmd( { type: 'alert', text: t, on: { close: function() { _rm( ':develop' ) } } } ) : alert( t );
				e.preventDefault();
			}
		} );
		/*document.onkeydown = ( function( e ) {
			if ( e.keyCode == 65 && e.altKey ) {
				e.stopPropagation(); e.preventDefault();
			}
		} );*/
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

/* 初始化应用环境 */
var $G = {};
_merge( $, {
	//dfish对象名，默认为"$"
	abbr: '$',
	ID_ALERT: 'dfish:alert',
	_data: {},
	// 提示
	// 初始配置
	config: function( a ) {
		this.x = _cfg = _extendDeep( a, _cfg );
		a.data && this._data && _extend( this._data, a.data );
	},
	init: function( x ) {
		x && this.config( x );
		_initEnv(), _initDocView( this );
	},
	use: function( a ) {
		return (new Require( _cfg.path || '' ))( a );
	},
	// a -> text, b -> pos, c -> time, d -> id
	alert: function( a, b, c, d ) {
		return $.vm ? $.vm().cmd( { type: 'alert', text: a, position: b, timeout: c, id: d !== U ? d : $.ID_ALERT } ) : alert( a );
	},
	// a -> text, b -> yes, c -> no
	confirm: function( a, b, c ) {
		return $.vm().cmd( { type: 'confirm', text: a, yes: b, no: c } );
	},
	cleanPop: function() {
		$.require( 'dialog' ).cleanPop();
	},
	// 存取临时变量
	data: function( a, b ) {
		return b === U ? this._data[ a ] : (this._data[ a ] = b);
	},
	winbox: function( a ) {
		return alert( a );
	},
	msg: (function() {
		function _parse( a, n ) {
			if ( $.query.isPlainObject( a ) ) {
				var s = '{', n = n || 0, i, j;
				for ( i in a ) {
					j = n;
					s += '\n';
					while ( j -- ) s += '\t';
					s += '"' + i + '": ' + (typeof a[ i ] === _STR ? '"' + a[ i ] + '"' : ($.query.isPlainObject( a[ i ] ) ? _parse( a[ i ], n + 1 ) : a[ i ])) + ',';
				}
				return s.replace( /,$/, '' ) + '}';
			} else
				return a;
		}
		return function( a ) { alert( _parse( a ) ) }
	})(),
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
		(a = this.dialog( a )) && a.close();
	},
	ready: function( a ) {
		return br.mobile && location.protocol == 'file:' ? doc.addEventListener( 'plusready', a ) : $.query( doc ).ready( a );
	},
	//导入皮肤css /a -> { dir: 'css/', theme: 'classic', color: 'blue' }
	skin: (function() {
		var did = _uid(), gid = _uid(), tid = _uid(), cid = _uid(), y = {};
		return function( x ) {
			_classAdd( cvs, br.css3 ? 'f-css3' : 'f-css2' );
			if ( ! $( did ) ) {
				$.query( 'head' ).append( '<link rel="stylesheet" id=' + did + ' href="' + _ui_path + 'dfish.css' + _ver + '">' );
				br.mobile && $.query( 'head' ).append( '<link rel="stylesheet" href="' + _ui_path + 'mobile.css' + _ver + '">' );
			}
			var k = location.protocol + '//' + location.host + _ui_path + 'g/';
			_loadStyle( '.f-pic-prev-cursor{cursor:url(' + k + 'pic_prev.cur),auto}.f-pic-next-cursor{cursor:url(' + k + 'pic_next.cur),auto}' );
			if ( x ) {
				x = _extend( {}, x, y );
				if ( x.theme != y.theme ) {
					_rm( tid ), _rm( cid );
				} else if ( x.color != y.color ) {
					_rm( cid );
				}
				y = x;
				if ( ! $( gid ) )
					$.query( 'head' ).append( '<link id=' + gid + ' rel="stylesheet" href="' + _cfg.path + x.dir + 'global.css' + _ver + '">' );
				if ( ! $( tid ) )
					$.query( $( gid ) ).after( '<link id=' + tid + ' rel="stylesheet" href="' + _cfg.path + x.dir + x.theme + '/' + x.theme + '.css' + _ver + '">' );
				if ( ! $( cid ) )
					$.query( $( tid ) ).after( '<link id=' + cid + ' rel="stylesheet" href="' + _cfg.path + x.dir + x.theme + '/' + x.color + '/' + x.color + '.css' + _ver + '">' );
			}
		}
	})(),
	// @a -> widget|HTMLElement, y -> print?, t -> head tag
	print: function( a, z, y ) {
		var d = a.$ ? (a.$( 'cont' ) || a.$()) : a, w = _win( d ).document, b = $.query( 'link[media=print]', w ), c = [], s = d.outerHTML;
		$.query( 'meta', w ).each( function() { c.push( this.outerHTML ) });
		(b.length > 0 ? b : $.query( 'link', w )).each( function() { c.push( '<link rel="stylesheet" type="text/css" href="' + this.getAttribute( 'href' ) + '"/>' ); });
		$.query( 'style', w ).each( function() { c.push( this.outerHTML ) });
		s = s.replace( /<div[^>]+overflow-y[^>]+>/gi, function( $0 ) { return $0.replace( /height: \w+/gi, '' ); } );
		w = window.open();
		d = w.document;
		d.open( 'text/html', 'replace' );
		d.write( '<!doctype html><html><head><meta charset=utf-8><title>' + $.loc.print_preview + '</title><script>var $={e:function(){}}</script>' + c.join( '' ) +
			(y || '') + '</head><body>' + s +
			(! br.ms && z ? '<script>window.print();window.close()</script>' : '') +
			'</body></html>' );
		d.close();
		if ( br.ms && z ) {
			w.print(), w.close();
		}
		return d;
	},
	br: br, globals: {},
	Event: _Event, Node: _Node, uid: _uid,
	proxy: _proxy, fncall: _fncall, fnapply: _fnapply, fncreate: _fncreate, extend: _extend, extendDeep: _extendDeep, merge: _merge, mergeDeep: _mergeDeep, createClass: _createClass, 
	ajax: _ajax, ajaxXML: _ajaxXML, ajaxJSON: _ajaxJSON, ajaxAbort: _ajaxAbort, ajaxClean: _ajaxClean, script: _script, delay: _delay, cookie: _cookie,
	arrIs: _arrIs, arrIn: _arrIn, arrIndex: _index, arrMake: _arrMake, arrEach: _each, arrMap: _map, arrSelect: _arrSelect, arrPop: _arrPop, arrFind: _arrFind, isArray: _arrIs, inArray: _arrIn,
	idsAdd: _idsAdd, idsRemove: _idsRemove, idsAny: _idsAny, number: _number, numRange: _numRange, numAdd: _numAdd, scale: _scale, scaleRange: _scaleRange,
	strFrom: _strFrom, strTo: _strTo, strTrim: _strTrim, strQuot: _strQuot, strPad: _strPad, strLen: _strLen, strSlice: _strSlice, strEscape: _strEscape, strUnescape: _strUnescape,
	strHighlight: _strHighlight, strSplitword: _strSplitword, strFormat: _strFormat,
	urlEncode: _urlEncode, urlDecode: _urlDecode, urlFormat: _urlFormat, urlLoc: _urlLoc, urlParam: _urlParam,
	dateFormat: _dateFormat, dateParse: _dateParse, dateAdd: _dateAdd, dateValid: _dateValid, dateWeek: _dateWeek,
	rngSelection: _rngSelection, rngElement: _rngElement, rngCursor: _rngCursor, rngCursorOffset: _rngCursorOffset, rngText: _rngText,
	jsonParse: _jsonParse, jsonString: _jsonString, jsonClone: _jsonClone, jsonChain: _jsonChain, jsonArray: _jsonArray, jsonCut: _jsonCut, jsonAttr: _jsonAttr,
	db: db, canvas: _cvs, tags: _tags, html: _html, append: _append, prepend: _prepend, before: _before, after: _after, replace: _replace, frag: _frag, parseHTML: _parseHTML, download: _download,
	css: _css, remove: _rm, empty: _empty, attach: _attach, detach: _detach, width: _width, topHeight: _topHeight, topWidth: _topWidth, height: _height, bcr: _bcr, offset: _offset, snap: _snap, snapTo: _snapTo,
	classAdd: _classAdd, classRemove: _classRemove, classAny: _classAny, classToggle: _classToggle, classReplace: _classReplace, ease: _ease, boxwd: _boxwd, boxht: _boxht,
	stop: _stop, cancel: _cancel, moveup: _moveup, point: _point, animate: _animate, show: _show, hide: _hide,
	xmlParse: _xmlParse, xmlQuery: _xmlQuery, xmlQueryAll: _xmlQueryAll,
	image: _image, arrow: _arrow, previewImage: _previewImage, thumbnail: _thumbnail, draggable: _draggable, droppable: _droppable,
	rt_null: _returnNull, rt_true: _returnTrue, rt_false: _returnFalse, rt_empty: _returnEmpty
} );

if ( typeof win.dfish !== 'undefined' )
	$G.dfish = win.dfish;
if ( ! noGlobal )
	win.dfish = dfish;
	
return dfish;
});
