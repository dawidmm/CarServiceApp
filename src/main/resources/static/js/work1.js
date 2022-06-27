window.onload = start;

function start(){
    document.querySelector('#submit').disabled = true;
    $.getJSON('http://localhost:8080/allcars', function(data){
        for (var i = 0; i < data.length; i++){
            var select = document.getElementById('car');
            var opt = document.createElement('option');
                opt.value = data[i].id;
            var name = data[i].plateNumber;
                opt.innerHTML = name;
            select.appendChild(opt);
        }
      });
}

function disableBtn() {
var valuePrice = document.getElementById('price').value;
    if (!isNaN(valuePrice))
        document.querySelector('#submit').disabled = false;
    else
        document.querySelector('#submit').disabled = true;
}