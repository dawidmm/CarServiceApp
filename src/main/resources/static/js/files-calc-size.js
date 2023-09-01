var maxSingleSize = 10485760 - 1;
var maxTotalSize = 52428800 - 1;


var filesInput = document.getElementById('files');
var button = document.getElementById('submit');
var warningDiv = document.getElementById('warning');

filesInput.addEventListener("change", (event) => {
    var totalSize = 0;

    if (filesInput.files.length == 0) {
        button.disabled = false;
        warningDiv.style.display = 'none';
        return;
    }

    $('#files').each(function(i) {
        $.each(filesInput.files, function(i, file) {
            totalSize += file.size;

            if (file.size > maxSingleSize) {
                button.disabled = true;
                warningDiv.style.display = 'block';
                throw new Error("Maksymalny rozmiar 1 pliku to 10MB");
            } else {
                button.disabled = false;
                warningDiv.style.display = 'none';
            }
        });

        if(totalSize > maxTotalSize) {
            button.disabled = true;
            warningDiv.style.display = 'block';
        }
    });
});