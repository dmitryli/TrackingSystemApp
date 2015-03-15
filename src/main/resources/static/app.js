(function() {
	var app = angular.module('store', []);

	app.controller('StoreController', function() {
		this.product = gems;
	});

	var gems = [ {
		name : 'Dodecahedron',
		price : 2.95,
		description : ' just description',
		show : true
	}, {
		name : 'Penthagonal Gem',
		price : 5.78,
		description : ' not just description',
		show : true
	} ]
})();
