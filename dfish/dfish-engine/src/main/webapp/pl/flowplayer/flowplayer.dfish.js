/* flowplayer
 * { "type": "flowplayer", "option": { "src": "/test.mp4", "type": "video/mp4" } },
 */

var $ = require( 'dfish' );
//require.css( './skin/skin.css' );
//var flowplayer = require( './flowplayer' );

define.widget( 'flowplayer', {
	Listener: {
		body: {
			ready: function() {
				this.x.option && this.init( this.x.option );
			},
			resize: function() {
				var e = this.$( 'v' );
				e && $.css( e, { width: this.innerWidth(), height: this.innerHeight() } );
			}
		}
	},
	Prototype: {
		init: function( opt ) {
			if ( ! $.br.ie || $.br.ieVer > 9 ) {
				require.css( './skin/skin.css' );
				var fp = require( './flowplayer.min' );
				this.flowplayer = fp( this.$(), {
					swf: require.resolve( './flowplayer.swf' ),
					clip: { sources: [ { src: opt.src, type: opt.type } ] }
				} );
			}
		},
		html_nodes: function() {
			if ( $.br.ie && $.br.ieVer <= 9 ) {
				var w = this.innerWidth(), h = this.innerHeight(), u = this.x.option.src;
				return '<object id=' + this.id + 'v classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" bgcolor="#000000" width="' + w + '" height="' + h + '">' +
					'<param name="quality" value="high"/><param name="allowFullScreen" value="true"/>' +
					'<param name="movie" value="' + $.LIB + 'wg/upload/flvplayer.swf"/>' +
					'<param name="FlashVars" value="vcastr_file=' + u + '"/>' +
					'<embed src="' + $.LIB + 'wg/upload/flvplayer.swf" allowfullscreen="true" flashvars="vcastr_file=' + u + '" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="' + w + '" height="' + h + '"></embed></object>';
			}
			return '';
		}
	}
} );
