define.template({
    type: 'View',
    commands: {
        'submit': {
            type: 'Submit',
            src: 'demo/submit',
            success: '$.alert("提交成功",null,5000);'
        },
        'loading': {
            type: 'Loading',
            node: {
                type: 'ProgressLoader',
                guide: 'demo/loading',
                template: 'g/progress/multiple',
                success: 'if($response.data.finish){$.close(this);$.alert("完成了!",null,5000);}'
            }
        }
    },
    // node: {
    //     type: 'Horizontal',
    //     nodes: [
    //         {
    //             type: 'ButtonBar', dir: 'v', width: -1,
    //             nodes: [
    //                 {text: '布局类', target: 'layout'},
    //                 {text: '表格类', target: 'table'},
    //                 {text: '表单类', target: 'form'},
    //                 {text: '命令类', target: 'command'},
    //                 {text: '功能类', target: 'widget'},
    //                 {text: '高级功能', target: 'adv'}
    //             ]
    //         },
    //         {
    //             type: 'Frame',
    //             width: '*',
    //             nodes: [
    //                 {type: 'View', id: 'layout', template: 'demo/layout'},
    //                 {type: 'View', id: 'table', template: 'demo/table'},
    //                 {type: 'View', id: 'form', template: 'demo/form'},
    //                 {type: 'View', id: 'command', template: 'demo/command'},
    //                 {type: 'View', id: 'widget', template: 'demo/widget'},
    //                 {type: 'View', id: 'adv', template: 'demo/adv'}
    //             ]
    //         }
    //     ]
    // }
    node: {
        type: 'Tabs',
        cls: 'bg-white',
        position: 'b',
        nodes: [
            {
                text: '布局类',
                target: {
                    type: 'View',
                    template: 'demo/layout'
                }
            },
            {
                text: '表格类',
                target: {
                    type: 'View',
                    template: 'demo/table'
                }
            },
            {
                text: '表单类',
                target: {
                    type: 'View',
                    template: 'demo/form'
                }
            },
            {
                text: '命令类',
                target: {
                    type: 'View',
                    template: 'demo/command'
                }
            },
            {
                text: '功能类',
                target: {
                    type: 'View',
                    template: 'demo/widget'
                }
            },
            {
                text: '高级功能',
                target: {
                    type: 'View',
                    template: 'demo/adv'
                }
            }
        ]
    }
});