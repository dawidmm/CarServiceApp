window.onload = start;

var page = 0;

function start() {
    find();
}

function find() {
    $.getJSON(domain + '/calendar/' + 10 + '/' + page, function (data) {
        for (var i = 0; i < data.content.length; i++) {
            var desc = data.content[i].description;
            var plate = data.content[i].plateNumber;
            var date = data.content[i].date;
            var id = data.content[i].id;
            var row = "<tr id='tr' class='tr'>" +
                "<td id='oneColumn' style='width: 15%;'>" + plate + "</td>" +
                "<td id='twoColumn'>" + desc + "</td>" +
                "<td id='threeColumn' style='width: 10%;'>" + date + "</td>" +
                "<td id='fourColumn' onclick=deleteById(" + id + ") style='width: 32px;'><img src='img/trash.png' style='width: 32px; height: 32px;' /></td>" +
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
    getPageRow = document.getElementById("table").rows.length;

    if (getPageRow > pageSize) {
        $("#table #tr").remove()
        page = page + 1;
        document.getElementById('pageCounter').innerHTML = page;
        find();
    }
}