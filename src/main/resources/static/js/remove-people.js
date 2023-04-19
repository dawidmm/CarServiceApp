window.onload = start2;

var id = 0;

function start2() {
    $("#tip").hide();
    start();
}

function start() {
    $.getJSON(domain + '/allpeople', function (data) {
        for (var i = 0; i < data.length; i++) {
            var select = document.getElementById('people');
            var opt = document.createElement('option');
            opt.setAttribute('id', 'optionId');
            opt.value = data[i].id;
            var name = data[i].name + " " + data[i].sureName;
            opt.innerHTML = name;
            select.appendChild(opt);
        }
    });
}

function findPeople() {
    id = document.getElementById('people').value;
    $("#tip").show();

    $.getJSON(domain + '/people/car/' + id, function (data) {
        document.getElementById("tipDiv").innerHTML = 'Uwaga! Usuniesz ' + data + ' pojazd/ów i ';

        $.getJSON(domain + '/people/work/' + id, function (data) {
            document.getElementById("tipDiv").innerHTML = document.getElementById("tipDiv").innerHTML + ' ' + data + ' zleceń powiązasnych z tym użytkownikiem.';
        });
    });
}

function deletePeople() {
    if (id > 0) {
        $.ajax({
            url: domain + '/people/' + id,
            type: 'DELETE',
            success: function (result) {
                $("#people #optionId").remove();
                start();
                document.getElementById("tipDiv").innerHTML = 'Usunięto prawidłowo!'
            }
        });
    }
}