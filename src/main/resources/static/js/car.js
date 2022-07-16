window.onload = start2;

// function start() {
//     $.getJSON('http://localhost:8080/allpeople', function (data) {
//         for (var i = 0; i < data.length; i++) {
//             var select = document.getElementById('owner');
//             var opt = document.createElement('option');
//             opt.value = data[i].id;
//             var name = data[i].name + ' ' + data[i].sureName;
//             opt.innerHTML = name;
//             select.appendChild(opt);
//         }
//     });
// }

function start2() {
    $.getJSON('http://localhost:8080/allpeople', function (data) {
        for (var i = 0; i < data.length; i++) {
            var select = document.getElementById('owner');
            var opt = document.createElement('option');
            $(opt).attr('data-id', data[i].id);
            var name = data[i].name + " " + data[i].sureName;
            opt.value = name;
            select.appendChild(opt);
        }
    });
}

function getDataListSelectedOption(txt_input, data_list_options) {
    var shownVal = document.getElementById(txt_input).value;
    var value2send = document.querySelector("#" + data_list_options + " option[value='" + shownVal + "']").dataset.id;
    return value2send;
}

function postCar() {
    var ownerSt;
    var plateSt = document.getElementById('plate').value;
    var vinSt = document.getElementById('vin').value;

    if (!(document.getElementById('owner2').value == ""))
        ownerSt = getDataListSelectedOption('owner2', 'owner');

    $.post("http://localhost:8080/add_car", {
        owner: ownerSt,
        plate: plateSt,
        vin: vinSt
    }, function (data) {
        if (data === 200) {
            window.location.replace("http://localhost:8080/add_car?add");
        } else {
            window.location.replace("http://localhost:8080/add_car?error");
        }
    });

}