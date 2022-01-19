'use script'
function runCase(id) {
    $.ajax({
        url: '/auto/loginMainProcess?time=' + new Date().getTime(),
        type: 'get',
        success: function (result) {
            console.log(result);
        }
    });
}