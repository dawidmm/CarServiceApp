$.getJSON(domain + '/allworks', function (data) {
    for (var i = 0; i < data.length; i++) {

        $(document).ready(function () {
            var description = data.description;
            var price = data.price;
            var date = data.date;
            var cars = data.cars.plateNumber;
            var row = "<tr><td>" + cars + "</td><td>" + description + +"</td><td>" + price + "</td><td>" + date + "</td></tr>";
            $("table tbody").append(row);
        });
    }
});
