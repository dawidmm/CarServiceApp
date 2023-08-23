window.onload = start;

var id = window.location.search;

function start() {
    //pobrac z resta nazwy plikow i wyswietlic

    $.getJSON(domain + '/files/names/' + id.substring(1), function (data) {
        data.forEach((v) => $("#forFiles")
            .prepend("<img src='img/file.svg' onClick='getFile(\"" + v + "\")' alt='" + v + "' style='max-width: 100%; height: auto;'><p style='width: 100%; color: silver;'>" + v + "</p>"));
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