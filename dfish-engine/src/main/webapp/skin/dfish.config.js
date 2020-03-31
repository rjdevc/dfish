dfish.config({
    // 调试模式
    debug: true,
    // 模板目录
    templateDir: 't/',
    // 预装载目录
    preloadDir: 'p/',
    // 皮肤
    skin: {
        // 样式目录
        dir: 'skin/',
        // 主题风格
        theme: 'classic',
        // 色调
        color: 'blue'
    },
    //表单验证效果 可选值: red,tip,alert
    validate: {
        effect: 'red,tip'
    },
    // 所有表单提示信息按照系统规则显示,比如:请输入XXX
    autoPlaceholder: true,
    // 每个 widget 类都可以定义默认属性，以 widget type 作为 key
    defaultOptions: {
        'Alert': {preload: 'g/alert'},
        'Confirm': {preload: 'g/alert'},
        'Dialog': {preload: 'g/std', width: 680, height: 410, error: "this.close();"},
        'Dialog.dlg-large': {width: 980, height: 550},
        'Dialog.dlg-medium': {width: 680, height: 410},
        'Dialog.dlg-small': {width: 440, height: 250},
        'Dialog.dlg-alert': {preload: 'g/alert'},
        'Dialog.dlg-std': {preload: 'g/std'},
        'Dialog.dlg-stdx': {preload: 'g/stdx'},
        'Dialog.dlg-form': {preload: 'g/form'},
        'Dialog.dlg-picker': {preload: 'g/picker'},
        'Dialog.dlg-none': {preload: 'g/none'},

        'Tree': {ellipsis: true},
        'Leaf': {tip: true, style: 'padding-right:10px;', widthMinus: 10},
        'Blank': {width: '*', height: '*'},

        'ButtonBar': {space: 10, pub: {tip: true, height: 30}},
        'ButtonBar.dlg-head-operation': {space: 0},
        'ButtonBar.x-breadcrumb': {space: 0},
        'ButtonBar.x-face-text': {space: 0},
        'Button': {tip: true, widthMinus: 2, heightMinus: 2},
        'SubmitButton': {tip: true, widthMinus: 2, heightMinus: 2},
        // 'Tabs': {widthMinus: 2, heightMinus: 2},
        // 'TabBar': {widthMinus: 20},
        // 'TabBar.z-position-left': {widthMinus: 0, heightMinus: 20},
        // 'TabBar.z-position-right': {widthMinus: 0, heightMinus: 20},

        // 'ButtonBar.w-tabbar': {space: 0, widthMinus: 20},
        // 'ButtonBar.w-tabbar.z-position-left': {widthMinus: 0, heightMinus: 20},
        // 'ButtonBar.w-tabbar.z-position-right': {widthMinus: 0, heightMinus: 20},

        'Table': {face: "cell"},
        'TBody': {br: false, pub: {focusable: true}},
        'Column': {tip: true},
        'PageBar': {align: 'right', buttonCount: 5},
        // 'PageBar.z-normal': {align: 'right', buttonCount: 5, widthMinus: 40, name: 'currentPage'},
        // 'PageBar.z-mini': {align: 'right', buttonCount: 5, widthMinus: 40, name: 'currentPage'},
        // 'PageBar.z-simple': {align: 'right', buttonCount: 5, widthMinus: 40, name: 'currentPage'},

        'Form': {pub: {colSpan: -1}},
        'Form.x-form1': {pub: {colSpan: -1}},
        'Form.x-form2': {pub: {colSpan: 6}},
        'Form.x-form3': {pub: {colSpan: 4}},
        'Form.x-form4': {pub: {colSpan: 3}},

        'Label': {width: 120},
        'Textarea': {height: 100},
        'DatePicker': {format: 'yyyy-mm-dd'},
        'Spinner': {step: 10, decimal: 0, validate: {minValue: 1, maxValue: 9999999999999999}},
        // 'Combo': {keepShow: true},
        'Slider': {tip: true},
        'Jigsaw': {
            img: {type: 'JigsawImg', template: 'jigsaw/img'},
            auth: {type: 'JigsawAuth', template: 'jigsaw/auth'}
        },
        'FileUpload': {
            minFileSize: '1B',
            maxFileSize: '10M',
            data: {'scheme': 'DEFAULT'},
            post: {type: 'UploadPost', template: 'upload/post'},
            download: 'file/download?fileId=$id&scheme=$scheme',
            fileTypes: '*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.jpg;*.gif;*.png;*.vsd;*.txt;*.rtf;*.pdf;*.wps;',
            uploadButtons: [{type: 'UploadButton', text: '选择文件'}]
        },
        'ImageUpload': {
            minFileSize: '1B',
            maxFileSize: '10M',
            data: {'scheme': 'DEFAULT'},
            post: {type: 'UploadPost', src: 'file/upload/image?scheme=$scheme', template: 'upload/post'},
            download: 'file/download?fileId=$id&scheme=$scheme',
            thumbnail: 'file/thumbnail?fileId=$id&scheme=$scheme',
            preview: {type: 'JS', text: '$.previewImage("file/thumbnail?fileId="+$id+"&scheme="+$scheme);'},
            fileTypes: '*.png;*.jpg;*.jpeg;',
            uploadButtons: [{type: 'UploadButton', text: '+'}]
        },

        // 'Toggle': {
        //     height: 40,
        //     icon: '.i-toggle-icon',
        //     expandedIcon: '.i-toggle-expanded',
        //     cls: 'bd-split bd-onlybottom',
        //     heightMinus: 1
        // },
        // 'Toggle.z-hr': {
        //     cls: null,
        //     heightMinus: 0
        // },
        // 'TableToggle': {
        //     height: 40,
        //     icon: '.i-toggle-icon',
        //     expandedIcon: '.i-toggle-expanded',
        //     cls: 'bd-split bd-onlybottom',
        //     heightMinus: 1
        // },
        // 'TableToggle.z-hr': {
        //     cls: null,
        //     heightMinus: 0
        // }
    },
    // 自定义widget
    alias: {
        'AMap': 'pl/amap/amap.dfish.js',
        'AMapPicker': 'pl/amap/amap.dfish.js',
        'ECharts': 'pl/echarts/echarts.dfish.js',
        'UEditor': 'pl/ueditor/ueditor.dfish.js',
        'UEditorHtml': 'pl/ueditor/ueditorhtml.dfish.js',
        'FlowPlayer': 'pl/flowplayer/flowplayer.dfish.js',
        'Carousel': 'pl/carousel/carousel.dfish.js'
    },
    ajaxFilter: function (response) {
        if (response.error) {
            app.response.error(response);
        } else {
            if (!response.data) {
                response.data = {};
            }
            return response;
        }
    },
    // 一个汉字算3个字节
    cnBytes: 3
});