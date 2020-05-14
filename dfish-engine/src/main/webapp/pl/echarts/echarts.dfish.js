/* echarts
 * {type: 'echarts', option: {}}
 */

var Q = require( 'jquery' );

define( 'echarts', require( './echarts.min' ) );
require( './echarts-wordcloud.min' );


define.widget( 'ECharts', {
	Listener: {
		body: {
			ready: function() {
				this.x.option && this.init( this.x.option );
			},
			resize: function() {
				this.echarts && this.echarts.resize();
			}
		}
	},
	Prototype: {
		init: function( opt ) {
			(this.echarts = require( 'echarts' ).init( this.$() )).setOption( this.parseOption( opt ) );
		},
		// ½âÎö "javscript:return"
		parseOption: function( o ) {
			for ( var k in o ) {
				if ( typeof o[ k ] === 'string' ) {
					if ( o[ k ].indexOf( 'javascript:' ) === 0 ) o[ k ] = this.formatJS( o[ k ] );
				} else if ( Q.isPlainObject( o[ k ] ) )
					this.parseOption( o[ k ] );
			}
			return o;
		}
	}
} );
