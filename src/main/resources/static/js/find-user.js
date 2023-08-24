window.onload = start;

function start() {
    reworkTable();
    findUser();
}

var row;

var page = 0;
var pageSize = 10;

var counter;

var one;
var two;
var three;
var four;

var name;
var phone;
var cars;
var works;

var oneThCol = false;
var twoThCol = false;
var threeThCol = false;
var fourThCol = false;

var sqlFlag = "";
var key;

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

function clearTh() {
    oneThCol = false;
    twoThCol = false;
    threeThCol = false;
    fourThCol = false;
}

function useGoodSearch(sqlArgs) {
    findUserWithSql(sqlFlag);
}

function findUserWithSql(sqlFlag) {
    counter.innerHTML = page;
    key = document.getElementById('search-user').value;
    $("#table #tr").remove();
    $.getJSON(domain + '/allpeople/' + page + '/' + pageSize, {sql: sqlFlag, key: key}, function (data) {

        for (var i = 0; i < data.content.length; i++) {
            name = data.content[i].name;
            phone = data.content[i].phone;
            cars = data.content[i].cars;
            works = data.content[i].works;
            rowBuilder(data.content[i].id);
            $("#table").append(row);
            reworkTable();
        }
    });
}

function reworkTable() {
    one = document.getElementById('imie').checked;
    two = document.getElementById('kontakt').checked;
    three = document.getElementById('ilosc_samochodow').checked;
    four = document.getElementById('ilosc_zlecen').checked;
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
        $("#table #thIloscSamochodow").hide();
        $("#table #threeColumn").hide();
    } else {
        $("#table #thIloscSamochodow").show();
        $("#table #threeColumn").show();
    }
    if (!four) {
        $("#table #thIloscZlecen").hide();
        $("#table #fourColumn").hide();
    } else {
        $("#table #thIloscZlecen").show();
        $("#table #fourColumn").show();
    }
}

function rowBuilder(id) {
    row = "<tr id='tr' class='tr' onClick='deleteWorkRow(" + id + ")'><td id='oneColumn'>" +
        name + "</td><td id='twoColumn'>" +
        phone + "</td><td id='threeColumn'>" +
        cars + "</td><td id='fourColumn'>" +
        works + "</td></tr>";
}

function findUser() {
    counter = document.getElementById("pageCounter");
    counter.innerHTML = page;
    findUserWithSql();
}

function plusPage() {
    getPageRow = document.getElementById("table").rows.length;

    if (getPageRow > pageSize) {
        $("#table #tr").remove();
        page = page + 1;
        useGoodSearch(sqlFlag);
    }
}

function minusPage() {
    if (page != 0) {
        $("#table #tr").remove();
        page = page - 1;
        useGoodSearch(sqlFlag);
    }
}

var deleteBool = false;
var hidePanel = false;

function deleteMode() {
    if (deleteBool)
        deleteBool = false;
    else
        deleteBool = true;
    disableAll();
}

function disableAll() {
    if (hidePanel) {
        $('html').css("background-color", "#5c5c5c");
        $('body').css({'cssText': 'background-color: #5c5c5c !important'});
        $("#hideId").show();
        hidePanel = false;
    } else {
        $('html').css("background-color", "#583f3f");
        $('body').css({'cssText': 'background-color: #583f3f !important'});
        $("#hideId").hide();
        hidePanel = true;
    }
}

function deleteWorkRow(id) {
    if (deleteBool) {
        $.ajax({
            url: domain + '/people/' + id,
            type: 'DELETE',
            success: function (result) {
                $("#table #tr").remove();
                findUserWithSql(sqlFlag);
            }
        });
    }
}