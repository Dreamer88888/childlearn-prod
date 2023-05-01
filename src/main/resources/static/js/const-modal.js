var templateDeleteModal = `
<div class="modal-dialog modal-choose unselectable">
  <div class="modal-content">
    <div class="modal-header flex-column">
      <div class="icon-box icon-box-red"><i class="material-icons">&#xE5CD;</i></div>
      <h4 class="modal-title w-100">Apakah Anda Yakin?</h4>
    </div>
    <div class="modal-body">
      <p>
        Data yang telah dihapus tidak dapat dikembalikan
      </p>
    </div>
    <div class="modal-footer justify-content-center">
      <button id="modal-action-button" type="submit" class="btn btn-danger">Ya</button>
      <button type="button" class="btn btn-secondary" data-dismiss="modal">
        Tidak
      </button>
    </div>
  </div>
</div>
`;

var templateUpdateModal = `
<div class="modal-dialog modal-choose unselectable">
  <div class="modal-content">
    <div class="modal-header flex-column">
      <div class="icon-box icon-box-yellow"><i class="material-icons">&#10227;</i></div>
      <h4 class="modal-title w-100">Apakah Anda Yakin?</h4>
    </div>
    <div class="modal-body">
      <p>
        Data yang telah diubah tidak dapat dikembalikan
      </p>
    </div>
    <div class="modal-footer justify-content-center">
      <button id="modal-action-button" type="submit" class="btn btn-warning">Ya</button>
      <button type="button" class="btn btn-secondary" data-dismiss="modal">
        Tidak
      </button>
    </div>
  </div>
</div>
`;

var templateInsertModal = `
<div class="modal-dialog modal-choose unselectable">
  <div class="modal-content">
    <div class="modal-header flex-column">
      <div class="icon-box icon-box-blue"><i class="material-icons">&#xE5CD;</i></div>
    </div>
    <div class="modal-body">
      <h4 class="modal-title w-100">Lanjut Menambahkan Data?</h4>
    </div>
    <div class="modal-footer justify-content-center">
      <button id="modal-action-button" type="submit" class="btn btn-primary">Ya</button>
      <button type="button" class="btn btn-secondary" data-dismiss="modal">
        Tidak
      </button>
    </div>
  </div>
</div>
`;

var templateSuccessModal = `
<div class="modal-dialog modal-confirm unselectable">
    <div class="modal-content">
        <div class="modal-header">
            <div class="icon-box">
                <i class="material-icons">&#xE876;</i>
            </div>
        </div>
        <div class="modal-body">
            <h4 class="modal-title">MSG</h4>
        </div>
        <div class="modal-footer">
            <button id="modal-action-button" class="btn btn-success btn-block" data-dismiss="modal">OK</button>
        </div>
    </div>
</div>
`;

function modalOk(event, message, continueProcess) {
  event.preventDefault();

  $("#action-modal-container").html(
    templateSuccessModal.replace("MSG", message)
  );

  let targetEndpoint = $(event.currentTarget).data("href");
  if (continueProcess) {
    $("#modal-action-button").on("click", function () {
      window.location.href = targetEndpoint;
    });
  }
}

function modalDelete(event) {
  event.preventDefault();

  let targetEndpoint = $(event.currentTarget).data("href");
  $("#action-modal-container").html(templateDeleteModal);

  $("#modal-action-button").on("click", function () {
    window.location.href = targetEndpoint;
  });
}

function modalUpdate(event) {
  event.preventDefault();

  let targetEndpoint = $(event.currentTarget).data("href");
  $("#action-modal-container").html(templateUpdateModal);

  $("#modal-action-button").on("click", function () {
    window.location.href = targetEndpoint;
  });
}

function triggerModal(event, type) {
  if (null == event.submitter) return true;
  event.preventDefault();
  showModal(type);
  $("#modal-action-button").on("click", function () {
    document.getElementById("submission-form").requestSubmit();
  });
}

function showModal(type) {
  if (type === "u")
    $("#action-modal-container").html(templateUpdateModal).modal("show");
  else if (type === "d")
    $("#action-modal-container").html(templateDeleteModal).modal("show");
  else if (type === "i")
    $("#action-modal-container").html(templateInsertModal).modal("show");
  else
    $("#action-modal-container")
      .html(templateSuccessModal.replace("MSG", type))
      .modal("show");
}
