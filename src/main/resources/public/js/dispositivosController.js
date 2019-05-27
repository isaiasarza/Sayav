
/*$(document).ready(function() {
	$.get( "/dispositivos", function( data ) {
	
		console.log("Obteniendo dispositivos");
		console.log(data);
		var dispositivos = JSON.parse(data);
		$.each(dispositivos, function (index, disp) {
			console.log("Nombre: " + disp.nombre);
			console.log("Apellido: " + disp.apellido);
			console.log("Token: " + disp.token);
		     var eliminar = "<button onClick=eliminarDispositivo("+ disp.token +") class=\"w3-button\">Eliminar</button>" ;

		     var eachrow = "<tr>"
		                 + "<td>" + disp.nombre + " "+ disp.apellido +"</td>"
	                 + "<td>" + eliminar + "</td>";
		                 + "</tr>";
		     $('#tDispositivosBody').append(eachrow);		
		});	
	});
});*/

function eliminarDispositivo(token){
	console.log("Eliminando dispositivo " + token);
}  

