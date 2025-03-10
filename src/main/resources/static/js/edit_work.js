window.onload = start;

function start() {
    $.getJSON(domain + '/work/' + window.location.search.substring(1), function (data) {
        document.getElementById('desc').value = data.work.description;
    });
}

function editPostWork() {
    var descSt = document.getElementById('desc').value;
    $.ajax({
        url: domain + "/edit_work/" + window.location.search.substring(1),
        type: 'POST',
        method: 'POST',
        data: descSt,
        processData: false,
        contentType: false,
        success: function (data) {
            window.location.replace(domain + "/find");
        },
        error: function (error) {
            window.location.replace(domain + "/find");
        }
    });
}