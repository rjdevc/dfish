define.template({
    type: 'Progress',
    '@src': '"progress/reload/"+$data.progressKey',
    '@delay': '$data.delay',
    template: 'g/progress/multiple',
    success: 'if($response.data.finish){$.alert("进度完成",5,5000);}',
    complete: 'if($response.error||$response.data.finish){$.close(this);}',
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