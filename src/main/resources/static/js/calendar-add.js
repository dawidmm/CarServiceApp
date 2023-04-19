function postCalendar() {
    var desc = document.getElementById('desc').value;
    var plateSt = document.getElementById('plate').value;
    var date = document.getElementById('date').value;


    $.post(domain + "/calendar", {
        desc: desc,
        plate: plateSt,
        date: date
    }, function (data) {
        if (data === 201) {
            window.location.replace(domain + "/add_calendar?add");
        } else {
            window.location.replace(domain + "/add_calendar?error");
        }
    });
}