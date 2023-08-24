window.onload = start;

var id = window.location.search;

function start() {
    $.getJSON(domain + '/files/names/' + id.substring(1), function (data) {
        data.forEach((v) => $("#forFiles")
            .prepend("<div id='singleFiles' class='rounded' onClick='getFile(\"" + v + "\")'><img id='img-files' src='img/file.svg' alt='" + v + "'>" +
            "<p style='color: silver;'>" + v + "</p></div>"));
    });
}

function getFile(v) {
    fetch(domain + '/files' + '/' + id.substring(1) + '/' + v)
      .then(resp => resp.blob())
      .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = v;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      });
}