dfish.config({
    // 调试模式
    debug: true,
    // 模板目录
    template_dir: 't/',
    // 预装载目录
    preload_dir: 'p/',
    // 皮肤
    skin: {
        // 样式目录
        dir: 'skin/'
    },
    //表单验证效果 可选值: red,tip,alert
    validate_effect: 'red,tip',
    // 所有表单提示信息按照系统规则显示,比如:请输入XXX
    auto_placeholder: true,
    // 每个 widget 类都可以定义默认属性，以 widget type 作为 key
    default_option: {
        'alert': {preload: 'g/alert'},
        'confirm': {preload: 'g/alert'},
        'dialog': {preload: 'g/std', resizable: true, width: 980, height: 550},
        'dialog.dlg-large': {width: 980, height: 550},
        'dialog.dlg-medium': {width: 680, height: 410},
        'dialog.dlg-small': {width: 440, height: 250},
        'dialog.dlg-alert': {preload: 'g/alert'},
        'dialog.dlg-std': {preload: 'g/std'},
        'dialog.dlg-stdx': {preload: 'g/stdx'},
        'dialog.dlg-form': {preload: 'g/form'},
        'dialog.dlg-none': {preload: 'g/none'},

        'tree': {scroll: true, ellipsis: true},
        'leaf': {tip: true, style: 'padding-right:10px;', wmin: 10},

        'buttonbar': {space: 10, pub: {tip: true, height: 30}},
        'buttonbar.dlg-head-oper': {space: 0},
        'buttonbar.x-breadcrumb': {space: 0},
        'buttonbar.face-text': {space: 0},
        'tabs': {wmin: 40},
        'buttonbar.w-tabbar': {space: 0, pub: { height: "*"}, wmin: 20},

        'grid': {scroll: true, nobr: true, pub: {focusable: true}, face: "cell"},
        'column': {tip: true},
        // 小分页模式暂时定于全数据关联的分页栏使用,所以不设置src
        'page/mini': {align: 'right', btncount: 5, wmin: 40},
        'page/buttongroup': {
            align: 'right',
            btncount: 5,
            wmin: 40,
            name: 'currentPage'
        },
        'page/text': {
            align: 'right',
            btncount: 5,
            wmin: 40,
            name: 'currentPage',
            jump: true
        },

        'form': {scroll: true, pub: {colspan: -1}},
        'form.x-form1': {pub: {colspan: -1}},
        'form.x-form2': {pub: {colspan: 6}},
        'form.x-form3': {pub: {colspan: 4}},
        'form.x-form4': {pub: {colspan: 3}},

        'label': {width: 120},
        'textarea': {height: 100},
        'date': {format: 'yyyy-mm-dd'},
        'spinner': {step: 10, decimal: 0, validate: {minvalue: 1, maxvalue: 99999999}},
        'combo': {keepshow: true},
        'slider': {tip: true},
        'upload/file': {minfilesize: '1B'},
        'upload/image': {minfilesize: '1B', scheme: 'DEFAULT'},

        'toggle': {
            height: 40,
            icon: '.i-toggle-icon',
            openicon: '.i-toggle-openicon',
            cls: 'bd-split bd-onlybottom',
            hmin: 1
        }
    },
    // 自定义widget
    alias: {
        'amap': 'pl/amap/amap.dfish.js',
        'amap/picker': 'pl/amap/amap.dfish.js',
        'echarts': 'pl/echarts/echarts.dfish.js',
        'ueditor': 'pl/ueditor/ueditor.dfish.js',
        'flowplayer': 'pl/flowplayer/flowplayer.dfish.js',
        'carousel': 'pl/carousel/carousel.dfish.js'
    },
    src_filter: function (response) {
        if (response.error) {
            app.error(response.error);
        } else {
            if (!response.data) {
                response.data = {};
            }
            return response;
        }
    },
    // 一个汉字算3个字节
    cn_bytes: 3
});