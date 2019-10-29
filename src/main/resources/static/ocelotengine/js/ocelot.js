//var laddaCss = document.createElement('link');
//laddaCss.href = 'ocelotengine/ladda/ladda.css';
//document.head.appendChild(laddaCss);
//
//var spinJs = document.createElement('script');
//spinJs.src = 'ocelotengine/spin/spin.js';
//document.head.appendChild(spinJs);
//
//var laddaJs = document.createElement('script');
//laddaJs.src = 'ocelotengine/ladda/ladda.js';
//document.head.appendChild(laddaJs);
//
//var sweetAlerts = document.createElement('script');
//sweetAlerts.src = 'ocelotengine/sweet-alerts/sweetalert.min.js';
//document.head.appendChild(sweetAlerts);

function preventBack() {
    window.history.forward();
}
setTimeout("preventBack()", 0);
window.onunload = function () {
    null
};

var modal =
        '<div class="modal fade" id="ocelotModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true"> ' +
        '            <div class="modal-dialog modal-lg" role="document">' +
        '                <div class="modal-content">' +
        '                    <div class="modal-header">' +
        '                        <h4 id="finder-title"></h4>' +
        '                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
        '                            <span aria-hidden="true">&times;</span>' +
        '                        </button>' +
        '                    </div>' +
        '                    <div id="ocelot_modal_body" class="modal-body">' +
        '                    </div>' +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '<div style="display:none">' +
        '    <button id="modalLuncher" data-target="#ocelotModal" data-toggle="modal" endpoint="" type="button" class="finderLauncher">Modal launcher</button>' +
        '</div>';

$("body").append(modal);

$.ajaxSetup({
    timeout: 40000 //Time in milliseconds
});

$(document).on("keyup", '.uppercase', function (e) {
    $(this).val($(this).val().toUpperCase());
});
$(document).on("keyup", '.lowercase', function (e) {
    $(this).val($(this).val().toLowerCase());
});
$(document).on("keyup", '.wo_spaces', function (e) {
    $(this).val($(this).val().replace(" ", ""));
});
$(document).ready(function () {
    $(".sidenav-link").css("cursor", "pointer");
    $(".menu-item").css("cursor", "pointer");
});


$(document).on("keyup", '.ajaxForm', function (e) {
    var key = (document.all) ? e.keyCode : e.which;
    if (($(this).attr("validate-intro") != undefined && $(this).attr("validate-intro") == "true") && key == 13) {
        $("button:first", this).click();
    }
});
$(document).on("click", '.menu-item', function (event) {
    event.preventDefault();
    launchMenuItem($(this).attr("endpoint"));
});
$(document).on("click", ".ajaxGet", function (event) {
    if (!$(this).attr("no-ladda")) {
        var l = Ladda.create(this);
        l.start();
        setTimeout(function () {
            try {
                l.stop();
            } catch (err) {
                alert(err);
            }
        }, 40000);
    }
});
$(document).on("click", '.ajaxGet', function (event) {
    event.preventDefault();
    launchGetAction($(this).attr("endpoint"), "get");
});
$(document).on("click", '.finderLauncher', function (event) {
    event.preventDefault();
    $("#ocelot_modal_body").html(getLoading());
    $("#finder-title").html("");
    launchGetAction($(this).attr("endpoint"), "get");
});
function getLoading() {
    return '<div class="container">' +
            '<h2>Cargando..</h2>' +
            ' <div class="sk-wave sk-primary">' +
            '<div class="sk-rect sk-rect1 bg-primary" style="margin-right:5px"></div>' +
            '<div class="sk-rect sk-rect2 bg-success" style="margin-right:5px"></div>' +
            '<div class="sk-rect sk-rect3 bg-info" style="margin-right:5px"></div>' +
            '<div class="sk-rect sk-rect4 bg-warning" style="margin-right:5px"></div>' +
            '<div class="sk-rect sk-rect5 bg-danger" style="margin-right:5px"></div>' +
            '</div>' +
            ' </div>';
}

$(document).on("click", '.ajaxPost', function (event) {
    event.preventDefault();
    reset = false;
    if ($(this).attr("reset")) {
        reset = true;
    }

    var l = Ladda.create(this);
    if (!$(this).attr("no-ladda")) {
        l.start();
        setTimeout(function () {
            try {
                l.stop();
            } catch (err) {
                alert(err);
            }
        }, 40000);
    }
    launchPostAction($(this).attr("endpoint"), $(this).attr("form-name"), reset, l);
});
$(document).on("click", ".ajaxDelete", function (event) {

    var myTittle = "¿Desea eliminar el registro?"

    if ($(this).attr("war-message")) {
        myTittle = $(this).attr("war-message");
    }

    swal({
        title: myTittle,
        text: "",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
            .then((willDelete) => {
                if (willDelete) {
                    var l = Ladda.create(this);
                    l.start();
                    launchGetAction($(this).attr("endpoint"), "delete");
                }
            });
});
$(document).on("click", ".paginate_button", function (event) {

    var idForm = $(this).attr("id_form");
    var endpoint = $(this).attr("endpoint");
    sendFormPageable(idForm, endpoint);
});
$(document).on("blur", "input[type=date]", function (event) {

    if ($(this).attr("max-range")) {
        $("#" + $(this).attr("max-range")).attr("min", $(this).val())
        return false;
    }

    if ($(this).attr("min-range")) {
        $("#" + $(this).attr("min-range")).attr("max", $(this).val())
        return false;
    }

    return false;
});
function stringToDate(str) {
    vectorDate = str.split("-");
    return new Date(parseInt(vectorDate[0]), parseInt(vectorDate[1]) - 1, parseInt(vectorDate[2]));
}

function sendFormPageable(idform, url_endpoint) {

    var fields = $("#" + idform).find('input, textarea, select');
    var datastring = collectFormData(fields);
    $.ajax({
        type: "POST",
        url: url_endpoint,
        data: datastring,
        cache: false,
        contentType: false,
        processData: false,
        success: function (j) {
            for (var i = 0; i < j.length; i++) {
                $("#" + j[i].label).css("display", "none");
                $("#" + j[i].label).html(j[i].value);
                $("#" + j[i].label).fadeIn();
            }

        },
        error: function (jqXHR) {
            showErrorMessage(jqXHR);
        }
    });
}


function launchGetAction(endpoint, action) {
    var showConfirm = true;
    $.getJSON(endpoint + "", function (j) {

        for (var i = 0; i < j.length; i++) {
            $("#" + j[i].label).css("display", "none");
            if (j[i].eventType === "REPLACE") {
                $("#" + j[i].label).html(j[i].value);
            }

            if (j[i].eventType === "APPEND") {
                $("#" + j[i].label).append(j[i].value);
            }

            if (j[i].eventType === "DELETE") {
                $("#" + j[i].label).html("");
            }

            if (j[i].eventType === "ERROR") {
                $("#" + j[i].label).html("");
                showConfirm = false;
            }

            if (j[i].eventType === "TAG") {
                $(j[i].label).html(j[i].value);
                showConfirm = false;
            }

            $("#" + j[i].label).fadeIn();
        }

    }).done(function (data) {
        loadActions();
        if (action === "delete" && showConfirm === true) {
            swal("El registro ha sido eliminado!", {
                icon: "success",
            });
        }
    }).fail(function (jqXHR) {
        showErrorMessage(jqXHR);
    });
}

function launchPostAction(endpoint, idform, reset, ladda) {
    $("#ocelot-hidden-alert").html("");
    $("#ocelot-default-alert").html("");
    if (validateForm(idform) == false) {
        ladda.stop();
        return false;
    }

    var fields = $("#" + idform).find('input, textarea, select');
    var datastring = collectFormData(fields);
    $.ajax({
        type: "POST",
        url: endpoint,
        data: datastring,
        cache: false,
        contentType: false,
        processData: false,
        success: function (j) {
            for (var i = 0; i < j.length; i++) {
                $("#" + j[i].label).css("display", "none");
                $("#" + j[i].label).html(j[i].value);
                $("#" + j[i].label).fadeIn();

                if (j[i].label == "ocelot-error-no-reset") {
                    reset = false;
                }

            }
            if (reset == true) {
                $("#" + idform).trigger("reset");
                $("#" + idform + " :input[type=hidden]").each(function () {
                    if (!$(this).attr("no-reset")) {
                        $(this).val("");
                    }

                });
                $("#" + idform).each(function () {

                    $(this).find('fieldset').each(function () {
                        if ($(this).hasClass("ocelot-privileges") === false) {
                            $(this).html("");
                        }
                    });
                    $(this).find('select').each(function () {
                        if ($(this).prop("tagName").toLowerCase() === "select") {
                            $(this).find('option:eq(0)').prop('selected', true);
                        } else {
                            $(this).html("");
                        }
                    });
                });
            }

            ladda.stop();
        },
        error: function (jqXHR) {
            ladda.stop();
            showErrorMessage(jqXHR);
        }
    });
}

function launchMenuItem(endpoint) {
    var target = endpoint.replace("/", "-");
    window.history.pushState(null, "", target + ".mod");
    $.getJSON(endpoint + "", function (j) {

        for (var i = 0; i < j.length; i++) {
            $("#" + j[i].label).css("display", "none");
            if (j[i].eventType === "TAG") {
                $(j[i].label).html(j[i].value);
                showConfirm = false;
            } else {
                $("#" + j[i].label).html(j[i].value);
            }
            $("#" + j[i].label).fadeIn();
        }

    }).done(function (data) {
        loadActions();
    }).fail(function (jqXHR) {
        showErrorMessage(jqXHR);
    });
}

function modifyUrl(urlPath) {
    window.history.pushState({}, "", urlPath);
}

function loadActions() {
    addAsterisk();
    controlateIntro();
}

function addAsterisk() {
    $("input, select, textarea").each(function (e) {
        if ($(this).attr("required") == "true" || $(this).attr("required") == "required") {
            var label = $("label[for=" + $(this).attr("id") + "]");
            var labelValue = label.html()
            try {
                if (!labelValue.includes("*")) {
                    label.html("* " + label.html());
                }
            } catch (e) {
            }


        }
    });
}

function controlateIntro() {
    $("input,textarea").each(function () {
        $(this).attr("onkeypress", "return pulsar(event)");
    });
}

function validateForm(form) {
    $(".error_small").html("");
    var isValid = true;
    $("#" + form + " :input").each(function () {

        if ($(this).attr("type") != "button" && $(this).attr("type") != "submit" && ($(this).attr("required") != null && $(this).attr("required") != "undefined" && $(this).attr("required") != "") && $(this).val() == "") {
            $("#" + $(this).attr("id") + "_errors").css("color", "red");
            $("#" + $(this).attr("id") + "_errors").html("<li>Ingrese un valor para el campo <span class='glyphicon glyphicon-remove'></span></li>");
            isValid = false;
        }

        if ($(this).attr("equal-to")) {
            var eqId = $(this).attr("equal-to");
            if ($("#" + eqId).val() !== $(this).val()) {
                $("#" + $(this).attr("id") + "_errors").css("color", "red");
                $("#" + $(this).attr("id") + "_errors").html("<li>La contrase&ntilde;a y su confirmaci&oacute;n son diferentes <span class='glyphicon glyphicon-remove'></span></li>");
                isValid = false;
            }
        }

        var minlength = $(this).attr('minlength');
        if (typeof minlength !== typeof undefined) {
            var minVal = parseInt(minlength);
            var value = $.trim($(this).val());
            if (value.length < minVal) {
                $("#" + $(this).attr("id") + "_errors").css("color", "red");
                $("#" + $(this).attr("id") + "_errors").html("<li>Este campo debe contener m&iacute;nimo " + minlength + " caracteres<span class='glyphicon glyphicon-remove'></span></li>");
                isValid = false;
            }

        }

        var maxlength = $(this).attr('maxlength');
        if (typeof maxlength !== typeof undefined) {
            var maxVal = parseInt(maxlength);
            var value = $.trim($(this).val());
            if (value.length > maxVal) {
                $("#" + $(this).attr("id") + "_errors").css("color", "red");
                $("#" + $(this).attr("id") + "_errors").html("<li>Este campo debe contener m&aacute;ximo " + maxVal + " caracteres<span class='glyphicon glyphicon-remove'></span></li>");
                isValid = false;
            }

        }

        if ($(this).attr("type") === "date" && $(this).attr("required") != "" && $(this).val() !== "" && isValidDate($(this).val() === false)) {
            $("#" + $(this).attr("id") + "_errors").css("color", "red");
            $("#" + $(this).attr("id") + "_errors").html("<li>Ingrese una fecha v&aacute;lida en formato año-mes-dia (aaaa-mm-dd) <span class='glyphicon glyphicon-remove'></span></li>");
            isValid = false;
        }

        console.log($(this).attr("name") + " --- " + $(this).val());

    });
    var caract = new RegExp(/^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/);
    $("#" + form + " :input[type=email]").each(function () {
        if ($(this).val() != "" && caract.test($(this).val()) == false) {
            $("#" + $(this).attr("id") + "_errors").html("<li>Ingrese un email v&aacute;lido <span class='glyphicon glyphicon-envelope'></span></li>");
            isValid = false;
        }
    });


    return isValid;
}

function isValidDate(dateString) {
    var regEx = new RegExp(/^\d{4}-\d{2}-\d{2}$/);
    if (!regEx.test(dateString)) {
        return false;  // Invalid format
    }
    var d = new Date(dateString);
    var dNum = d.getTime();
    if (!dNum && dNum !== 0)
        return false; // NaN value, Invalid date
    return d.toISOString().slice(0, 10) === dateString;
}


function collectFormData(fields) {
    var formData = new FormData();
    for (var i = 0; i < fields.length; i++) {
        var $item = $(fields[i]);
        var type = $item.attr('type');
        if (type === "file") {
            var files = $item[0].files;
            for (var i2 = 0; i2 < files.length; i2++) {
                formData.append($item.attr('name'), files[i2]);
            }

        } else if (type === "checkbox") {
            if ($item.is(':checked')) {
                formData.append($item.attr('name'), $item.val());
            }

        } else if (type === "hidden" && $item.attr('summernote')) {

            var svalue = $($item.attr('summernote')).summernote('code');
            formData.append($item.attr('name'), svalue);
        } else {
            formData.append($item.attr('name'), $item.val());
        }
    }
    return formData;
}

function pulsar(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    return (tecla != 13);
}

function addValuesFinder(field, visible_value, hidden_value) {
    $("#" + field + "_visible").html(visible_value);
    $("#" + field).val(hidden_value);
}

function clearValuesFinder(field) {
    $("#" + field + "_visible").html("");
    $("#" + field).val("");
}

function showErrorMessage(jqXHR) {
    if (jqXHR.status == 401 || jqXHR.status == 200) {
        /* sweetAlert("Oops...", "Su sesi&oacute;n ha expirado!", "error");
         swal({
         title: "Oops...",
         text: "Su sesi&oacute;n ha expirado!",
         type: "error",
         confirmButtonColor: "#DD6B55",
         confirmButtonText: "OK",
         closeOnConfirm: true
         },
         function (isConfirm) {
         window.location = "/";
         });
         
         return false;
         */

        swal({
            title: "Oops...",
            text: "Su sesión ha expirado!",
            type: "error",
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "OK",
            closeModal: true,
        }).then((isConfirm) => {
            if (isConfirm) {
                window.location = "login?logout";
            }
        });
        return false;
    }

    if (jqXHR.status == 0) {
        sweetAlert("Problemas de conexión.", "Ha sido detectada lentitud en la comunicación o ausencia de esta, por favor reintente la acción realizada, si el problema persiste contacte al administrador.", "error");
        return false;
    }

    sweetAlert("Oops...", "Ha ocurrido un error " + jqXHR.status + " " + jqXHR.textStatus, "error");
}