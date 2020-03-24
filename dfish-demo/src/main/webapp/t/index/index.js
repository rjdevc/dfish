define.template({
    type: 'View', node: {
        type: 'Horizontal', scroll: true, nodes: [
            {
                type: 'Vertical', width: '*', style: 'background:#e9e9e9;', nodes: [
                    {
                        type: 'Horizontal',
                        cls: 'index-head',
                        style: 'background:#009bdb;',
                        height: 50,
                        nodes: [
                            {
                                type: 'Horizontal', nodes: [
                                    {
                                        type: 'Img',
                                        src: '.i-logo',
                                        width: 210,
                                        height: '*',
                                        style: 'padding-right:10px;',
                                        widthMinus: 10
                                    },
                                    {
                                        type: 'ButtonBar',
                                        id: 'menuBar',
                                        width: '*',
                                        space: 0,
                                        cls: 'index-head-nav',
                                        pub: {height: '*', width: 100, focusable: true},
                                        nodes: [
                                            {text: '范例', target: 'demo', focus: true}
                                        ]
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        type: 'Frame', id: 'main', height: '*', nodes: [
                            {type: 'View', id: 'demo', template: 'demo/index'},
                        ]
                    }
                ]
            }
        ]
    }
});