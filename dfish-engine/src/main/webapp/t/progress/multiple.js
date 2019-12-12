define.template({
    type: 'progress',
    '@src': '"progress/reload/"+$data.progressKey',
    '@delay': '$data.delay',
    template: 'progress/multiple',
    success: 'if($response.data.finish){$.alert("进度完成",5,5000);}',
    complete: 'if($response.data.finish||$response.error){$.close(this);}',
    nodes: [
        {
            '@text': '"("+($data.stepIndex+1)+"/"+$data.steps+") "+$data.progressText',
            '@percent': '$data.stepPercent'
        },
        {
            '@percent': '$data.donePercent'
        }
    ]
});