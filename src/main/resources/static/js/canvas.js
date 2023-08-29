const canvas = document.getElementById('drawing');
const ctx = canvas.getContext('2d');

canvas.width = window.innerWidth;
canvas.height = window.innerHeight - 50;

let isPainting = false;
let lineWidth = 1;

const draw = (e) => {
    if (!isPainting) {
        return;
    }

    ctx.lineWidth = lineWidth;
    ctx.lineCap = 'round';

    ctx.lineTo(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop);
    ctx.stroke();
}

const drawMobile = (e) => {
    if (!isPainting) {
        return;
    }

    ctx.lineWidth = lineWidth;
    ctx.lineCap = 'round';

    for (let i = 0; i < e.changedTouches.length; i++) {
        ctx.lineTo(e.changedTouches[i].pageX, e.changedTouches[i].pageY);
        ctx.stroke();
    }
}

canvas.addEventListener('mousedown', e => {
    isPainting = true;
});

canvas.addEventListener('touchstart', e => {
    isPainting = true;
});

canvas.addEventListener('mouseup', e => {
    isPainting = false;
    ctx.stroke();
    ctx.beginPath();
});

canvas.addEventListener('touchend', e => {
    isPainting = false;
    ctx.stroke();
    ctx.beginPath();
});

canvas.addEventListener('mousemove', draw);
canvas.addEventListener('touchmove', drawMobile);

document.getElementById('clear').addEventListener('click', e => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
});

document.getElementById('abort').addEventListener('click', e => {
    window.history.back();
});

document.getElementById('save').addEventListener('click', e => {
    const img = canvas.toDataURL('image/png');
    img.src = "signature.png";

    var formData = new FormData();
    formData.append("signature", img);

    $.ajax({
            url: domain + '/work/accept/' + window.location.search.substring(1),
            type: 'POST',
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                alert("Zaakceptowano prawidłowo");
                window.location.href = "/find";
            },
            error: function (error) {
                alert("Błąd akceptacji podpisu");
            }
        });
});



