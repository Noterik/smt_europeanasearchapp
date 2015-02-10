var Searchfilter = function(options){
	var self = {};
	var settings = {}
	
	$.extend(settings, options);

function callSearch() {

	$( "#filtertype" ).change(function() {
		var value = document.getElementById('filtertype').value;
		eddie.putLou('', 'searchfilter('+ value +')');
		console.log(value);
	});

};

callSearch();
	return self;
}

