define.template({
    type:'View',src:'exceptionViewer/list',

    node: {
        type: 'Horizontal', height:"*",scroll: true, nodes: [
            {
                type: 'Vertical', scroll: true,width: '*', style: 'background:#F8F8F8;', nodes: [
                    {type:'Table',height:'*',face: 'line',
                        columns:[
                            {field: 'exptRepetitions', width: "80"},
                            {field: 'typeName', width: "*",format:"($typeId)$typeName"},
                            {field: 'exptMsg', width: "80"},
                            {field: 'eventTime', width: 150, align: 'center'},
                            {field: 'oper',width: 120, align: 'center',format:"<a href='#' onclick=\"VM(this).cmd('pop','$recId')\">详细</a>"},
                        ],
                        tHead:{nodes:[
                            {exptRepetitions:"次数",typeName:"类型",exptMsg:'信息',eventTime:"时间",oper:"操作"}
                        ]},
                        tBody:{"@nodes":'$data'}
                    },
                    {type:'PageBar',height:40,buttonCount :7,'@limit':'$header.limit','@offset':'$header.offset','@size':'$header.size',
                        src:'exceptionViewer/list?offset=$0'}
                ]
            }
        ]
    },
    commands:{
        pop:{type:'Dialog',title:'详情',height:560,width:900,preload:'g/std',template:'pulgin/exception/form',src:'exceptionViewer/get?recId=$0'}
    }
});