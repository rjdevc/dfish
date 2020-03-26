define.template({
    type: 'View', node: {
        type: 'Horizontal', scroll: true, nodes: [
            {
                type: 'Vertical', width: '*', style: 'background:#e9e9e9;', nodes: [
                    {
                        type: 'Horizontal',
                        cls: 'index-head',
                        style: 'background:#2498ff;',
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
                                        cls: 'index-head-nav',
                                        width: '*',
                                        space: 0,
                                        pub: {height: '*', width: 100},
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