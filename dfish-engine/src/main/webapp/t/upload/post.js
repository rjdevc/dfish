define.template({
    type: 'upload/post',
    src: 'file/upload/file?scheme=$scheme',
    result: {
        '@id': '$data.id',
        '@name': '$data.name'
    }
});