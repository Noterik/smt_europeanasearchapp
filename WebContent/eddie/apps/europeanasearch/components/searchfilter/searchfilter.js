var Searchfilter = function(options){
	var self = {};
	var settings = {}
	
	$.extend(settings, options);

	function callSearch() {
	
		$( "#filtertype" ).change(function() {
			var value = $(this).val();
			eddie.putLou('', 'searchfilter('+ value +')');
			console.log(value);
		});
	
	};
	
	callSearch();
	return self;
}

