window.onload = start;


var plateInput = "";
var nameInput = "";

var page = 0;
var pageSize = 10;

var counter;

var one;
var two;
var three;
var four;
var five;
var six;

var row;

var name;
var phone;
var cars;
var description;
//var price;
var date;
var accepted;

var oneThCol = false;
var twoThCol = false;
var threeThCol = false;
var fourThCol = false;
//var fiveThCol = false;
var sixThCol = false;

var plateFlag = false;
var nameFlag = false;
var allFlag = false;

var sqlFlag = "";

var visablePanel = false;

var startDate = "";
var endDate = "";

var deleteBool = false;


function clearFindFlag() {
    plateFlag = false;
    nameFlag = false;
    allFlag = false;
    allFlag = false;
}

function start() {

    counter = document.getElementById("pageCounter");
    counter.innerHTML = page;
    reworkTable();

    $.getJSON(domain + '/allcars', function (data) {
        for (var i = 0; i < data.length; i++) {
            var select = document.getElementById('car');
            var opt = document.createElement('option');
//            opt.value = data[i].id;
            $(opt).attr('data-id', data[i].id);
            //alert($(opt).data('value'));
            var name = data[i].plateNumber;
//            opt.innerHTML = name;
            opt.value = name;
            select.appendChild(opt);
        }
    });
    start2();
    findAllWorks();
}

function start2() {
    counter.innerHTML = page;
    $.getJSON(domain + '/allpeople', function (data) {
        for (var i = 0; i < data.length; i++) {
            var select = document.getElementById('name');
            var opt = document.createElement('option');
            $(opt).attr('data-id', data[i].id);
            var name = data[i].name + " " + data[i].sureName;
            opt.value = name;
            select.appendChild(opt);
        }
    });
}

function findWithPlate(sqlFlag) {
    counter.innerHTML = page;

    startDate = document.getElementById('startDate').value;
    endDate = document.getElementById('endDate').value;

    $.getJSON(domain + '/workwithplate/' + page + '/' + pageSize, {
        id: plateInput,
        sql: sqlFlag,
        sdate: startDate,
        edate: endDate
    }, function (data) {
        for (var i = 0; i < data.content.length; i++) {
            name = data.content[i].cars.people.name + '<br>' + data.content[i].cars.people.sureName;
            phone = data.content[i].cars.people.phone;
            cars = data.content[i].cars.plateNumber;
            description = data.content[i].description;
//            price = data.content[i].price + "";
            date = data.content[i].date;
            accepted = data.content[i].accepted;
            rowBuilder(data.content[i].id);
//          row = "<tr id='tr' class='tr'><td>" + name + "</td><td>" + phone + "</td><td>" + cars + "</td><td class='td-breaker'>" + description + "</td><td>" + price + "</td><td>" + date + "</td></tr>";
            $("#table").append(row);
            reworkTable();
        }
        clearFindFlag();
        plateFlag = true;

    });
}

function findWithName(sqlFlag) {
    counter.innerHTML = page;

    startDate = document.getElementById('startDate').value;
    endDate = document.getElementById('endDate').value;

    $.getJSON(domain + '/workwithname/' + page + '/' + pageSize, {
        id: nameInput,
        sql: sqlFlag,
        sdate: startDate,
        edate: endDate
    }, function (data) {
        for (var i = 0; i < data.content.length; i++) {
            name = data.content[i].cars.people.name + '<br>' + data.content[i].cars.people.sureName;
            phone = data.content[i].cars.people.phone;
            cars = data.content[i].cars.plateNumber;
            description = data.content[i].description;
//            price = data.content[i].price + "";
            date = data.content[i].date;
            accepted = data.content[i].accepted;
            rowBuilder(data.content[i].id);
//          row = "<tr id='tr' class='tr'><td>" + name + "</td><td>" + phone + "</td><td>" + cars + "</td><td class='td-breaker'>" + description + "</td><td>" + price + "</td><td>" + date + "</td></tr>";
            $("#table").append(row);
            reworkTable();
        }
        clearFindFlag();
        nameFlag = true;

    });
}

function findAllWorks(sqlFlag) {
    counter.innerHTML = page;

    startDate = document.getElementById('startDate').value;
    endDate = document.getElementById('endDate').value;

    $.getJSON(domain + '/allworks/' + page + '/' + pageSize, {
        sql: sqlFlag,
        sdate: startDate,
        edate: endDate
    }, function (data) {
        for (var i = 0; i < data.content.length; i++) {
            name = data.content[i].cars.people.name + '<br>' + data.content[i].cars.people.sureName;
            phone = data.content[i].cars.people.phone;
            cars = data.content[i].cars.plateNumber;
            description = data.content[i].description;
//            price = data.content[i].price + "";
            date = data.content[i].date;
            accepted = data.content[i].accepted;
            rowBuilder(data.content[i].id);
            $("#table").append(row);
            reworkTable();
        }
        clearFindFlag();
        allFlag = true;

    });

}

function getDataListSelectedOption(txt_input, data_list_options) {
    var shownVal = document.getElementById(txt_input).value;
    var value2send = document.querySelector("#" + data_list_options + " option[value='" + shownVal + "']").dataset.id;
    return value2send;
}

function search() {

    plateInput = "";
    nameInput = "";

    if (!(document.getElementById('car2').value == ""))
        plateInput = getDataListSelectedOption('car2', 'car');

    if (!(document.getElementById('name2').value == ""))
        nameInput = getDataListSelectedOption('name2', 'name');

    document.getElementById("car2").value = "";
    document.getElementById("name2").value = "";

    page = 0;

    if (plateInput == "" & nameInput != "") {
        $("#table #tr").remove();
        findWithName();
    }

    if (plateInput != "" & nameInput == "") {
        $("#table #tr").remove();
        findWithPlate();
    }

    if (plateInput == "" & nameInput == "") {
        $("#table #tr").remove();
        findAllWorks();
    }
}

function loadWork() {
    if (plateInput == "" & nameInput != "") {
        findWithName(sqlFlag);
    }

    if (plateInput != "" & nameInput == "") {
        findWithPlate(sqlFlag);
    }
    if (plateInput == "" & nameInput == "") {
        findAllWorks(sqlFlag);
    }
}

function plusPage() {
    getPageRow = document.getElementById("table").rows.length;

    if (getPageRow > pageSize) {
        $("#table #tr").remove();
        page = page + 1;
        loadWork();
    }
}

function minusPage() {
    if (page != 0) {
        $("#table #tr").remove();
        page = page - 1;
        loadWork();
    }
}

function reworkTable() {
    one = document.getElementById('imie').checked;
    two = document.getElementById('kontakt').checked;
    three = document.getElementById('rejestracja').checked;
    four = document.getElementById('opis').checked;
//    five = document.getElementById('cena').checked;
    six = document.getElementById('data').checked;

    if (!one) {
        $("#table #thImie").hide();
        $("#table #oneColumn").hide();
    } else {
        $("#table #thImie").show();
        $("#table #oneColumn").show();
    }
    if (!two) {
        $("#table #thKontakt").hide();
        $("#table #twoColumn").hide();
    } else {
        $("#table #thKontakt").show();
        $("#table #twoColumn").show();
    }
    if (!three) {
        $("#table #thRejestracja").hide();
        $("#table #threeColumn").hide();
    } else {
        $("#table #thRejestracja").show();
        $("#table #threeColumn").show();
    }
    if (!four) {
        $("#table #thOpis").hide();
        $("#table #fourColumn").hide();
    } else {
        $("#table #thOpis").show();
        $("#table #fourColumn").show();
    }
//    if (!five) {
//        $("#table #thCena").hide();
//        $("#table #fiveColumn").hide();
//    } else {
//        $("#table #thCena").show();
//        $("#table #fiveColumn").show();
//    }
    if (!six) {
        $("#table #thData").hide();
        $("#table #sixColumn").hide();
    } else {
        $("#table #thData").show();
        $("#table #sixColumn").show();
    }
}

function rowBuilder(id) {
    row = "<tr id='tr' class='tr' onClick='deleteWorkRow(" + id + ")'><td id='oneColumn'>" +
        name + "</td><td id='twoColumn'>" +
        phone + "</td><td id='threeColumn'>" +
        cars + "</td><td id='fourColumn' class='td-breaker'>" +
        description + "</td>" +
        //<td id='fiveColumn'>" + price + "</td>
        "<td id='sixColumn'>" +
        date + "</td>" +
        "<td><img src='img/file.svg' class='rounded p-2' onClick='goToFilePage(" + id + ")'></td>";

        if (accepted) {
            row += "<td><img src='img/contract.png' class='rounded p-2' onClick='showSignature(" + id + ")'></td>";
        } else {
            row += "<td><img src='img/accepted.png' class='rounded p-2' onClick='goToAcceptPage(" + id + ")'></td>";
        }
        row += "<td><img src='img/wrench2.png' class='rounded p-2' onClick='openEdit(" + id + ")'></td></tr>"
}

function goToFilePage(id) {
    window.location.href = "/files?" + id;
}

function goToAcceptPage(id) {
    window.location.href = "/canvas?" + id;
}

function showSignature(id) {
    window.location.href = "/signature?" + id;
}

function openEdit(id) {
     window.location.href = "/edit_work?" + id;
}

function oneTh() {
    if (oneThCol) {
        //jezeli juz jest znaznaczone
        clearTh();
        useGoodSearch(sqlFlag = '1+');
    } else {
        oneThCol = true;
        useGoodSearch(sqlFlag = '1-');
        //jezeli nie byl zaznaczony
    }
}

function twoTh() {
    if (twoThCol) {
        //jezeli juz jest znaznaczone
        clearTh();
        useGoodSearch(sqlFlag = '2+');
    } else {
        twoThCol = true;
        useGoodSearch(sqlFlag = '2-');
        //jezeli nie byl zaznaczony
    }
}

function threeTh() {
    if (threeThCol) {
        //jezeli juz jest znaznaczone
        clearTh();
        useGoodSearch(sqlFlag = '3+');
    } else {
        threeThCol = true;
        useGoodSearch(sqlFlag = '3-');
        //jezeli nie byl zaznaczony
    }
}

function fourTh() {
    if (fourThCol) {
        //jezeli juz jest znaznaczone
        clearTh();
        useGoodSearch(sqlFlag = '4+');
    } else {
        fourThCol = true;
        useGoodSearch(sqlFlag = '4-');
        //jezeli nie byl zaznaczony
    }
}

//function fiveTh() {
//    if (fiveThCol) {
//        //jezeli juz jest znaznaczone
//        clearTh();
//        useGoodSearch(sqlFlag = '5+');
//    } else {
//        fiveThCol = true;
//        useGoodSearch(sqlFlag = '5-');
//        //jezeli nie byl zaznaczony
//    }
//}

function sixTh() {
    if (sixThCol) {
        //jezeli juz jest znaznaczone
        clearTh();
        useGoodSearch(sqlFlag = '6+');
    } else {
        sixThCol = true;
        useGoodSearch(sqlFlag = '6-');
        //jezeli nie byl zaznaczony
    }
}

function clearTh() {
    oneThCol = false;
    twoThCol = false;
    threeThCol = false;
    fourThCol = false;
//    fiveThCol = false;
    sixThCol = false;
}

function useGoodSearch(sqlArgs) {
    if (nameFlag) {
        $("#table #tr").remove();
        findWithName(sqlFlag);
    } else if (plateFlag) {
        $("#table #tr").remove();
        findWithPlate(sqlFlag);
    } else if (allFlag) {
        $("#table #tr").remove();
        findAllWorks(sqlFlag);
    }
}

function changeVisible() {
    if (visablePanel) {
        $("#hideId").show();
        $("#hideThis").show();
        visablePanel = false;
    } else {
        $("#hideId").hide();
        $("#hideThis").hide();
        visablePanel = true;
    }
}

function deleteWorkRow(id) {
    if (deleteBool) {
        $.ajax({
            url: domain + '/work/' + id,
            type: 'DELETE',
            success: function (result) {
                $("#table #tr").remove();
                loadWork();
            }
        });
    }
}

function deleteMode() {
    if (deleteBool)
        deleteBool = false;
    else
        deleteBool = true;
    disableAll();
}

function disableAll() {
    if (deleteBool) {
        $("#topMenu *").prop('disabled', true);
        $("#searchMenu *").prop('disabled', true);
        $("#topTable *").prop('disabled', true);
        $("#botTable *").prop('disabled', true);
        document.getElementById("delBtn").disabled = false;
        $('html').css("background-color", "#583f3f");
        $('body').css({'cssText': 'background-color: #583f3f !important'});
        $("#hideId").hide();
        $("#hideThis").hide();
        visablePanel = true;
    } else {
        $("#topMenu *").prop('disabled', false);
        $("#searchMenu *").prop('disabled', false);
        $("#topTable *").prop('disabled', false);
        $("#botTable *").prop('disabled', false);
        $('html').css("background-color", "#5c5c5c");
        $('body').css({'cssText': 'background-color: #5c5c5c !important'});
        $("#hideId").show();
        $("#hideThis").show();
        visablePanel = false;
    }
}