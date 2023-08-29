window.onload = start;

function start() {
    var image = 'data:image/png;base64,';
    var imgElement = document.getElementById('img-sign');

    $.ajax({
        url: domain + '/work/' + window.location.search.substring(1) + '/signature',
        type: 'GET',
        method: 'GET',
        success: function (data) {
            imgElement.src = image + data;
        },
        error: function (error) {
            alert("Nie można pobrać podpisu");
        }
    });
}