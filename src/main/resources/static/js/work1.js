window.onload = start2;

// function start() {
//     document.querySelector('#submit').disabled = true;
//     $.getJSON(domain + '/allcars', function (data) {
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
    $.getJSON(domain + '/allcars', function (data) {
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
    var formData = new FormData();
    var filesSt = $("#files")[0].files;

    if (!(document.getElementById('car2').value == ""))
        ownerSt = getDataListSelectedOption('car2', 'car');

    for (let i = 0; i < filesSt.length; i++) {
        formData.append("files", filesSt[i]);
    }

    formData.append("car", ownerSt);
    formData.append("desc", descSt);
    formData.append("price", priceSt);

    $.ajax({
        url: domain + "/add_work",
        type: 'POST',
        method: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            window.location.replace(domain + "/add_work?add");
        },
        error: function (error) {
            window.location.replace(domain + "/add_work?error");
        }
    });
}