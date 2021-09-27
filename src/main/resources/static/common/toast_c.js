const alterDiv = document.getElementById('toast-div');
function toastInfo(msg, type) {
    const wrapper = document.createElement('div');
    wrapper.innerHTML = '<div class="alert alert-' + type + ' alert-dismissible position-absolute top-0 end-0" role="alert">' + msg + '</div>';
    alterDiv.append(wrapper);
}
