function formatDateyyyy_mm_dd(d) {
	var month = '' + (d.getMonth() + 1),
	day = '' + d.getDate(),
	year = d.getFullYear();

	if (month.length < 2) 
		month = '0' + month;
	if (day.length < 2) 
		day = '0' + day;

	return [year, month, day].join('-');
}
function modalInfo(title, text) {
	$('#modal').remove();
	//$('body').removeClass();
	//$('body').removeAttr("style");

	var htmlModal =
		`<div  id="modal" class="modal modal-alert" tabindex="-1" role="dialog">
			<div class="modal-dialog modal-dialog-centered">
				<div class="modal-content rounded-4 shadow">
					<div class="modal-body p-4 text-center">
						<h5 class="mb-0">${title}</h5>
						<p class="mb-0">${text}</p>
					</div>
					<div class="modal-footer flex-nowrap p-0">
						<button type="button" class="btn btn-lg btn-link fs-6 text-decoration-none col-12 m-0 rounded-0 border-right" data-bs-dismiss="modal"><strong>OK</strong></button>
					</div>
				</div>
			</div>
		</div>`;
	$('body').append(htmlModal);
	var jmodal = $('#modal');
	var modal = new bootstrap.Modal(jmodal[0], {});
	return modal;
}

function modalConfirmation(title, text) {
	$('#modal').remove();
	//$('body').removeClass();
	//$('body').removeAttr("style");
	
	var htmlModal =
		`<div  id="modal" class="modal modal-alert" tabindex="-1" role="dialog">
			<div class="modal-dialog modal-dialog-centered">
				<div class="modal-content rounded-4 shadow">
					<div class="modal-body p-4 text-center">
						<h5 class="mb-0">${title}</h5>
						<p class="mb-0">${text}</p>
					</div>
					<div class="modal-footer flex-nowrap p-0">
						<button id="modalOkBtn" type="button" class="btn btn-lg btn-link fs-6 text-decoration-none col-6 m-0 rounded-0 border-right"><strong>Ok</strong></button>
						<button type="button" class="btn btn-lg btn-link fs-6 text-decoration-none col-6 m-0 rounded-0" data-bs-dismiss="modal">Cancel</button>
					</div>
				</div>
			</div>
		</div>`;
	$('body').append(htmlModal);
	var jmodal = $('#modal');
	var modal = new bootstrap.Modal(jmodal[0], {});
	modal.ok = function(fn){
		var btn = $('#modalOkBtn');
		btn.click(function(e){
			fn(e);
			//$('#modal').remove();
			modal.hide();
		});
		return modal;
	};
	return modal;
}