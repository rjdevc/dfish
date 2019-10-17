dfish.config({
    skin: {
        // 主题风格
        theme: 'matrix',
        // 颜色
        color: 'blue'
    },
    default_option: {
        'horizontal.x-main-head': {hmin: 1},
        'vertical.x-main-nav': {wmin: 1, width: 300},
        'vertical.x-split-side': {wmin: 30, hmin: 20},
        'split.x-main-split': {width: 10, icon: '.i-split', openicon: '.i-split-col', range: '0,400'},

        'tabs.x-split-side': {wmin: 30},

        'grid.x-main-grid': {wmin: 40, hmin: 20},
        'grid.x-split-side': {wmin: 30, hmin: 20},

        'page/buttongroup': {
            src: 'javascript:this.cmd("search",$0);'
        },
        'page/text': {
            src: 'javascript:this.cmd("search",$0);'
        },

        'html.x-main-title': {wmin: 40},
        'html.x-welcome': {wmin: 40, hmin: 20},
        'html.x-split-side': {wmin: 30, hmin: 20},

        'form.x-main-form': {wmin: 40, hmin: 20},
        'form.x-split-side': {wmin: 30, hmin: 20},
        'form.x-main-search': {pub: {colspan: 4, labelwidth: -1}},
    }
});