window.onload = start;

function start() {
    $.getJSON('http://localhost:8080/allpeople', function (data) {
        for (var i = 0; i < data.length; i++) {
            var select = document.getElementById('owner');
            var opt = document.createElement('option');
            opt.value = data[i].id;
            var name = data[i].name + ' ' + data[i].sureName;
            opt.innerHTML = name;
            select.appendChild(opt);
        }
    });
}