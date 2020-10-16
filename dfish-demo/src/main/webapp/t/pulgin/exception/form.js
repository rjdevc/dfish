define.template({type:'Dialog',node:{
    type: 'View', node: {
        type: 'Vertical', width: '*', style: 'background:#F8F8F8;', nodes: [
            {
                type: 'Form', height: '*', face: 'cell',
                nodes: [
                    {colSpan:6,node:{type:'FormLabel',label:{text:'编号'},'@text':'$data.recId'}},
                    {colSpan:6,node:{type:'FormLabel',label:{text:'时间'},'@text':'$data.eventTime'}},
                    {colSpan:6,node:{type:'FormLabel',label:{text:'类型'},'@text':'"("+$data.typeId+") "+$data.typeName'}},
                    {colSpan:6,node:{type:'FormLabel',label:{text:'信息'},'@text':'$data.exptMsg'}},
                    {colSpan:12,node:{type:'Html','@text':'"<pre style=\'word-wrap:break-word;\'>"+$data.stack+"</pre>"'}}
                ]
            }
        ]
    }
}});