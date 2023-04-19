window.onload = start;

var page = 0;

function start() {
    find();
}

function find() {
    $.getJSON(domain + '/calendar/' + 10 + '/' + page, function (data) {
        for (var i = 0; i < data.length; i++) {
            var desc = data[i].description;
            var plate = data[i].plateNumber;
            var date = data[i].date;
            var id = data[i].id;
            var row = "<tr id='tr' class='tr' onClick=''>" +
                "<td id='oneColumn'>" + plate + "</td>" +
                "<td id='twoColumn'>" + desc + "</td>" +
                "<td id='threeColumn'>" + date + "</td>" +
                "<td id='fourColumn' onclick=deleteById(" + id + ")>" + 'Usuń' + "</td>" +
                "</tr>";
            $("#table").append(row);
        }
    });
}

function deleteById(id) {
    console.log(id);
    if (id > 0) {
        $.ajax({
            url: domain + '/calendar/' + id,
            type: 'DELETE',
            success: function (result) {
                $("#table #tr").remove();
                find();
            }
        });
    }
}

function minusPage() {
    if (page > 0) {
        $("#table #tr").remove();
        page = page - 1;
        document.getElementById('pageCounter').innerHTML = page;
        find();
    }
}

function plusPage() {
    $("#table #tr").remove()
    page = page + 1;
    document.getElementById('pageCounter').innerHTML = page;
    find();
}