/* echarts
 * {type: 'echarts', option: {}}
 */

var echarts = require( './echarts.min' );

define.widget( 'echarts', {
	Listener: {
		body: {
			ready: function() {
				this.x.option && this.init( this.x.option );
			},
			resize: function() {
				this.echarts.resize();
			}
		}
	},
	Prototype: {
		init: function( opt ) {
			(this.echarts = echarts.init( this.$() )).setOption( opt );
		}
	}
} );
