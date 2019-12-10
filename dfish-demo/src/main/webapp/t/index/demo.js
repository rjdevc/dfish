define.template({
    type: 'view',
    commands: {
        'submit': {
            type: 'submit',
            src: 'demo/submit'
        }
    },
    node: {
        type: 'vertical', nodes: [
            {
                type: 'horizontal',
                height: '*',
                nodes: [
                    {type: 'html', width: '*'},
                    {
                        type: 'form',
                        width: 1200,
                        pub: {colspan: -1},
                        nodes: [
                            {
                                type: 'buttonbar',
                                nodes: [
                                    {type: 'submitbutton', text: '提交', on: {click: 'this.cmd("submit")'}}
                                ]
                            },
                            {
                                type: 'upload/file',
                                name: 'fileJson',
                                label: {text: '附件上传'}
                            },
                            {
                                type: 'upload/image',
                                name: 'imageJson',
                                label: {text: '图片上传'}
                            },
                            {
                                type: 'ueditor',
                                name: 'lobContent',
                                label: {text: '测试文本'}
                            }
                        ]
                    },
                    {type: 'html', width: '*'}
                ]
            },
            {
                type: 'horizontal',
                height: 50,
                style: 'margin-top:10px;',
                cls: 'bg-white',
                hmin: 10,
                nodes: [
                    {type: 'html', width: '*'},
                    {
                        type: 'html',
                        width: 1200,
                        align: 'center',
                        valign: 'middle',
                        text: '版权所有 福建榕基软件股份有限公司 ©2019'
                    },
                    {type: 'html', width: '*'}
                ]
            }
        ]
    }
});