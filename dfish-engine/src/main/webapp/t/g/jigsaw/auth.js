define.template({
    type: 'JigsawAuth',
    src: 'jigsaw/auth?offset=$value',
    result: {
        '@success': '$data.success',
        '@text': '$data.success?"验证通过":$data.text'
    }
});