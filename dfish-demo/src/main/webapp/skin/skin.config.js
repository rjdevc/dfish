dfish.config({
    skin: {
        // 主题风格
        theme: 'matrix',
        // 颜色
        color: 'blue'
    },
    default_option: {
        'xsrc.x-split-side': {wmin: 30, hmin: 20},
        'horizontal.x-main-head': {hmin: 1},
        'vertical.x-main-nav': {wmin: 1, width: 300},
        'vertical.x-split-side': {wmin: 30, hmin: 20},
        'split.x-main-split': {width: 10, icon: '.i-split', openicon: '.i-split-col', range: '0,400'},

        'tabs.x-split-side': {wmin: 30},

        'buttonbar.x-main-oper': {wmin: 40, align: 'right'},

        'grid': {face: "dot"},
        'grid.x-main-grid': {wmin: 40, hmin: 20},
        'grid.x-split-side': {wmin: 30, hmin: 20},

        'page/buttongroup': {
            src: 'javascript:this.cmd("search",$0);'
        },
        'page/text': {
            src: 'javascript:this.cmd("search",$0);'
        },
        'page/text.x-split-side': {
            wmin: 30, hmin: 20
        },

        'html.x-main-title': {wmin: 40},
        'html.x-welcome': {wmin: 40, hmin: 20},
        'html.x-split-side': {wmin: 30, hmin: 20},

        'form.x-main-form': {wmin: 40, hmin: 20},
        'form.x-main-search': {pub: {colspan: 4, labelwidth: 100}},
        'form.x-split-side': {wmin: 30, hmin: 20},

        'switch': {checkedtext: '是', uncheckedtext: '否'}
    }
});