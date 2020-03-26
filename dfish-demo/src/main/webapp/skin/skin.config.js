dfish.config({
    skin: {
        // 主题风格
        theme: 'classic',
        // 颜色
        color: 'blue'
    },
    default_option: {
        'Section.x-split-side': {widthMinus: 30, heightMinus: 20},
        'Horizontal.x-main-head': {heightMinus: 1},
        'Vertical.x-main-nav': {widthMinus: 1, width: 300},
        'Vertical.x-split-side': {widthMinus: 30, heightMinus: 20},
        'Split.x-main-split': {width: 10, icon: '.i-split', expandedIcon: '.i-split-expanded', range: '0,400'},

        'Tabs.x-split-side': {widthMinus: 30},

        'ButtonBar.x-main-operation': {widthMinus: 40, align: 'right'},

        'Table': {face: "dot"},
        'Table.x-main-grid': {widthMinus: 40, heightMinus: 20},
        'Table.x-split-side': {widthMinus: 30, heightMinus: 20},

        'PageBar.z-normal': {
            src: 'javascript:this.cmd("search",$0);'
        },
        'PageBar.z-normal.x-split-side': {
            widthMinus: 30, heightMinus: 20
        },
        'PageBar.z-simple': {
            src: 'javascript:this.cmd("search",$0);'
        },

        'Html.x-main-title': {widthMinus: 40},
        'Html.x-welcome': {widthMinus: 40, heightMinus: 20},
        'Html.x-split-side': {widthMinus: 30, heightMinus: 20},

        'Form.x-main-form': {widthMinus: 40, heightMinus: 20},
        'Form.x-main-search': {pub: {colSpan: 4, labelWidth: 100}},
        'Form.x-split-side': {widthMinus: 30, heightMinus: 20},

        'Switch': {checkedText: '是', uncheckedText: '否'}
    }
});