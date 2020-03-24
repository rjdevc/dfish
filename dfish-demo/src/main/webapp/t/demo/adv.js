define.template({
    type: 'View',
    node: {
        type: 'Tabs',
        position: 'l',
        nodes: [
            {
                text: '拖拽',
                target: {
                    type: 'ButtonBar',
                    pub: {
                        on: {
                            ready: '$.droppable(this,{sort:true,isDisabled:function(event,ui){' +
                                'console.log("ui.droppable.nodeIndex:"+ui.droppable.nodeIndex);return ui.droppable.nodeIndex>1;},drop:function(event,ui){$.alert(ui.droppable.attr("text"));}});'
                        }
                    },
                    nodes: [
                        {text: '拖拽按钮', on: {ready: '$.draggable(this);'}},
                        {text: '接受按钮1'},
                        {text: '接受按钮2'},
                        {text: '接受按钮3'}
                    ]
                }
            }
        ]
    }
});