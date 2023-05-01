function bodyOnLoad(screenCd) {
  console.log("Screen Code: " + screenCd);
}

function validateNumberOnly(evt) {
  var theEvent = evt || window.event;
  var key = theEvent.keyCode || theEvent.which;
  key = String.fromCharCode(key);

  var regex = /[0-9]|\./;
  if (!regex.test(key)) {
    theEvent.returnValue = false;
    if (theEvent.preventDefault) theEvent.preventDefault();
  }
}

$(document).ready(function () {
  $("input.disableCopyPaste").bind("copy paste", function (e) {
    e.preventDefault();
  });
});
