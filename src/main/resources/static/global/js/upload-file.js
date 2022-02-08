'use strict';

function uploadProgress(evt) {
    if (evt.lengthComputable) {
        const percent = Math.round(evt.loaded * 100 / evt.total);
        // document.getElementById('progress').innerHTML = percent.toFixed(2) + '%';
        document.getElementById('progress').style.width = percent.toFixed(2) + '%';
    } else {
        document.getElementById('progress').innerHTML = 'unable to compute';
    }
}

function uploadComplete(evt) {
    /* 服务器端返回响应时候触发event事件*/
    // console.log(evt);
    const responseText = evt.target.responseText;
    const parse = JSON.parse(responseText);
    const $infoHelp = $('#infoHelp');
    if (Object.is(200, parse['code'])) {
        $infoHelp.addClass('text-success fw-bold').text(parse['msg']);
        $('#case-upload-modal').modal('hide');
        refresh_table();
    } else {
        const split = parse['data'].split(',');
        $infoHelp.append('<a class="text-danger" data-bs-toggle="collapse" href="#collapseExample1" aria-expanded="false" aria-controls="collapseExample1">' + parse['msg'] + '</a>');
        $infoHelp.append('<div class="collapse" id="collapseExample1"></div>');
        for (let i = 0; i < split.length; i++) {
            let _msg = split[i];
            if (_msg.substr(0, 1) === '[') {
                _msg = _msg.substr(1);
            }
            if (_msg.substr(-1) === ']') {
                _msg = _msg.substr(0, _msg.length - 1);
            }
            $('#collapseExample1').append('<div id="error_' + i + '" class="form-text text-danger fw-bold">' + _msg + '</div>');
        }
    }
}

function uploadFailed(evt) {
    alert("There was an error attempting to upload the file.");
}

function uploadCanceled(evt) {
    alert("The upload has been canceled by the user or the browser dropped the connection.");
}

const upload_with_progress_bar = (url, formData) => {
    const xhr = new XMLHttpRequest();
    xhr.upload.addEventListener("progress", uploadProgress, false);
    xhr.addEventListener("load", uploadComplete, false);
    xhr.addEventListener("error", uploadFailed, false);
    xhr.addEventListener("abort", uploadCanceled, false);
    xhr.open("POST", url);
    xhr.send(formData);
};