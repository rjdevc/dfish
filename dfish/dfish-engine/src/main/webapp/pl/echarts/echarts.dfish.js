/* echarts
 * {type: 'echarts', option: {}}
 * {type: 'echarts', on: {ready: 'this.init(option)'}}
 */

var echarts = require( './echarts.min' );

define.widget( 'echarts', {
	Listener: {
		body: {
			ready: function() {
				this.x.option && this.init( this.x.option );
			}
		}
	},
	Prototype: {
		init: function( opt ) {
			(this.echarts = echarts.init( this.$() )).setOption( opt );
		}
	}
} );
