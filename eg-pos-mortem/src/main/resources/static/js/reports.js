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
function populateTableReports(){
	$.get( "/report/getAll").done(function( data ) {
		var tbody =$('#tblReports>tbody');
		tbody.empty();
		if (!data || !data.length){
			tbody.append('<tr><td colspan="2" class="container text-center">No hay reportes</td></tr>');
			return;
		}
		for(var i = 0; i < data.length; i++){
			var html = `<tr><td>${i+1}</td><td>${data[i]}</td></tr>`;
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
	$('#btnReportMPUL').click(function(){
		$('#tblMatches').find('.table-info').each(function(index){
			$('#btnReportMPUL').prop('disabled', true);
			var row = $(this);
			var cols = row.find('td');
			var start = $(cols['1']).text().replaceAll(' ','').replaceAll(':','').replaceAll('-','');
			var end = $(cols['2']).text().replaceAll(' ','').replaceAll(':','').replaceAll('-','');
			var id = start+'-'+end;
			$.get( "/report/create", { name: 'MPUL', id: id })
			.done(function(data) {
				$('#btnReportMPUL').prop('disabled', false);
				populateTableReports();
				modalInfo('Ejecucion exitosa',data.name).show();
			})
			.fail(function(xhr, status, error) {
				$('#btnReportMPUL').prop('disabled', false);
				populateTableReports();
				modalInfo('Error',xhr.responseText).show();
		    });
		})
	});
	$('#btnDownloadReport').click(function(){
		$('#tblReports').find('.table-info').each(function(index){
			var row = $(this);
			var cols = row.find('td');
			var name = $(cols[1]).text();
			window.location.href = "report/download/"+name
		});
	});
	$('#btnDeleteReport').click(function(){
		modalConfirmation('Confirmacion','¿Desea eliminar los reportes seleccionados?')
		.ok(function(){
			var names = '';
			$('#tblReports').find('.table-info').each(function(index){
				var row = $(this);
				var cols = row.find('td');
				names = names + $(cols[1]).text() + ',';
			});
			if (names.length > 0){
				$('#btnDeleteReport').prop('disabled', true);
				names = names.substring(0,names.length-1);
				$.get( "/report/delete", { name: names })
				.done(function(data) {
					$('#btnDeleteReport').prop('disabled', false);
					populateTableReports();
					modalInfo('Reporte eliminado',name).show();
				})
				.fail(function(xhr, status, error) {
					$('#btnDeleteReport').prop('disabled', false);
					populateTableReports();
					modalInfo('Error',xhr.responseText).show();
				});
			}
		}).show();
	});
	$('#btnDeleteReportAll').click(function(){
		modalConfirmation('Confirmacion','¿Desea eliminar todos los reportes?')
		.ok(function(){
			$.get( "/report/deleteAll", { name: name })
			.done(function(data) {
				$('#btnDeleteReportAll').prop('disabled', false);
				populateTableReports();
				modalInfo('Rerpotes eliminados',name).show();
			})
			.fail(function(xhr, status, error) {
				$('#btnDeleteReportAll').prop('disabled', false);
				populateTableReports();
				modalInfo('Error',xhr.responseText).show();
		    });
		}).show();
	});
	populateTableExecutions();
	populateTableReports();
});