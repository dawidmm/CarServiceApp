window.onload = start2;

var id = 0;

function start2() {
    $("#tip").hide();
    start();
}

function start() {
    $.getJSON(domain + '/allcars', function (data) {
        for (var i = 0; i < data.length; i++) {
            var select = document.getElementById('car');
            var opt = document.createElement('option');
            opt.setAttribute('id', 'optionId');
            opt.value = data[i].id;
            opt.innerHTML = data[i].plateNumber;
            select.appendChild(opt);
        }
    });
}

function findCar() {
    id = document.getElementById('car').value;
    $("#tip").show();

    $.getJSON(domain + '/car/work/' + id, function (data) {
        document.getElementById("tipDiv").innerHTML = 'Uwaga! Usuniesz ' + data + ' zleceń!';
    });
}

function deleteCar() {
    if (id > 0) {
        $.ajax({
            url: domain + '/car/' + id,
            type: 'DELETE',
            success: function (result) {
                $("#car #optionId").remove();
                start();
                document.getElementById("tipDiv").innerHTML = 'Usunięto prawidłowo!'
            }
        });
    }
}