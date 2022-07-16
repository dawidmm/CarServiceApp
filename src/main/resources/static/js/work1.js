window.onload = start2;

// function start() {
//     document.querySelector('#submit').disabled = true;
//     $.getJSON('http://localhost:8080/allcars', function (data) {
//         for (var i = 0; i < data.length; i++) {
//             var select = document.getElementById('car');
//             var opt = document.createElement('option');
//             opt.value = data[i].id;
//             var name = data[i].plateNumber;
//             opt.innerHTML = name;
//             select.appendChild(opt);
//         }
//     });
// }

function disableBtn() {
    var valuePrice = document.getElementById('price').value;
    if (!isNaN(valuePrice))
        document.querySelector('#submit').disabled = false;
    else
        document.querySelector('#submit').disabled = true;
}

function start2() {
    $.getJSON('http://localhost:8080/allcars', function (data) {
        for (var i = 0; i < data.length; i++) {
            var select = document.getElementById('car');
            var opt = document.createElement('option');
            $(opt).attr('data-id', data[i].id);
            var name = 'NR REJ: ' + data[i].plateNumber + ' VIN: ' + data[i].vin;
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

function postWork() {
    var ownerSt;
    var descSt = document.getElementById('desc').value;
    var priceSt = document.getElementById('price').value;

    if (!(document.getElementById('car2').value == ""))
        ownerSt = getDataListSelectedOption('car2', 'car');

    $.post("http://localhost:8080/add_work", {
        car: ownerSt,
        desc: descSt,
        price: priceSt
    }, function (data) {
        if (data === 200) {
            window.location.replace("http://localhost:8080/add_work?add");
        } else {
            window.location.replace("http://localhost:8080/add_work?error");
        }
    });

}