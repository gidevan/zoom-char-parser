var messages = {};

document.addEventListener("DOMContentLoaded", () => {
  var stored = localStorage.getItem("outputFolder");
  if (stored) {
    document.getElementById("outputFolder").value = localStorage.getItem("outputFolder");
  }

});

function validate() {
    document.getElementById("errors").innerHTML = "";
    var errors = [];
    var outputFolder = document.getElementById("outputFolder").value;
    if (!outputFolder) {
        errors.push("Поле 'Папка с результатом' не может быть пустой");
    }

    if (document.getElementById("filePath").files.length ==0) {
        errors.push("Файл не выбран");
    }

    document.getElementById("errors").innerHTML = errors.join('<br/>')
    return errors;
}

function parseChat() {
    document.getElementById("errors").innerHTML = "";
    document.getElementById("resultData").innerHTML = "";
    var error = validate();
    if (error.length == 0) {
        var outputFolder = document.getElementById("outputFolder").value;
        localStorage.setItem("outputFolder", outputFolder);

        const formData = new FormData();
        formData.append("outputFolder", document.getElementById("outputFolder").value);
        formData.append("multipartFile", document.getElementById("filePath").files[0]);
        fetch("http://localhost:8080/parse", {
              method: "POST",
              body: formData,

        })
        .then(response => response.json())
        .then(json => {
                console.log(json);
                messages = json;
                document.getElementById("resultData").innerHTML = "Файл обработан";
        });
    }

}