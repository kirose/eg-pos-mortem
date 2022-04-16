function populateTableExecutions(){
	$.get( "/matcher/getAll").done(function( data ) {
		var tbody =$('#tblMatches>tbody');
		tbody.empty();
		if (!data || !data.length){
			tbody.append('<tr><td colspan="4" class="container text-center">No hay resutados de ejecuciones</td></tr>');
			return;
		}
		for(var i = 0; i < data.length; i++){
			var html = `<tr><td>${i+1}</td><td>${data[i].start}</td><td>${data[i].end}</td><td>${data[i].size}</td></tr>`;
			tbody.append(html);
		}
		tbody.find('tr').on('click', function(){
			var row = $(this);
			if (row.hasClass('table-info')) {
				row.removeClass('table-info');
			} else {
				row.addClass('table-info');
			}
		});
	});
}
$(document).ready(function() {
	$('#startDate').datepicker({
		language: "es",
		todayHighlight: true
	});
	$('#endDate').datepicker({
		language: "es",
		todayHighlight: true
	});
	$('#startDate').datepicker('setDate',new Date());
	$('#endDate').datepicker('setDate',new Date())
	$('#btnAnalysis').click(function(){
		var d1 = $('#startDate').datepicker('getDate');
		var d2 = $('#endDate').datepicker('getDate');
		if (!d1 || !d2) {
			modalInfo('Error','Seleccionar una fecha').show();
			return;
		}
		var time1 = $('#startTime').val();
		var time2 = $('#endTime').val();
		var date1 = formatDateyyyy_mm_dd(d1);
		var date2 = formatDateyyyy_mm_dd(d2);
		var startDate = date1 +' ' + (time1||'00:00:00')
		var endDate = date2 +' '+ (time2||'23:59:59');
		$('#btnAnalysis').prop('disabled', true);
		$.get( "/matcher/analyze", { start: startDate, end: endDate })
		.done(function(data) {
			$('#btnAnalysis').prop('disabled', false);
			populateTableExecutions();
			modalInfo('Ejecucion exitosa',data.id).show();
		})
		.fail(function(xhr, status, error) {
			$('#btnAnalysis').prop('disabled', false);
			modalInfo('Error',xhr.responseText).show();
	    });
	});
	$('#btnDeleteAll').click(function(){
		modalConfirmation('Confirmacion','¿Desea eliminar todos los registros?')
		.ok(function(){
			$.get( "/matcher/deleteAll")
			.done(function(data) {
				populateTableExecutions();
				modalInfo('Ejecucion exitosa',data).show();
			})
			.fail(function(xhr, status, error) {
				modalInfo('Error',xhr.responseText).show();
		    });			
		}).show();
	});
	populateTableExecutions();
});