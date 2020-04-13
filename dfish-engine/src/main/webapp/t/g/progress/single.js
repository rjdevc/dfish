define.template({
    type: 'Progress',
    '@src': '"progress/reload/"+$data.progressKey',
    '@delay': '$data.delay',
    template: 'g/progress/single',
    success: 'if($response.data.finish){$.alert("进度完成",5,5000);}',
    complete: 'if($response.error||$response.data.finish){$.close(this);}',
    nodes: [
        {
            '@text': '$data.progressText',
            '@percent': '$data.stepPercent'
        }
    ]
});