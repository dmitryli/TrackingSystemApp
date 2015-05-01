'use strict';

describe('ptControllers module', function () {
	
	var $scope;

	beforeEach(module('pTrack'));

	describe('home controller', function () {

		beforeEach(inject(function ($rootScope, $controller) {
			$scope = $rootScope.$new();
			$controller('HomeController', {
				$scope: $scope
			});
		}));

		it('should ....', inject(function ($controller) {
			expect($scope.title).toBe('Home');
		}));

	});
	
	describe('carriers controller', function () {

		beforeEach(inject(function ($rootScope, $controller) {
			$scope = $rootScope.$new();
			$controller('CarriersController', {
				$scope: $scope
			});
		}));

		it('should ....', inject(function ($controller) {
			expect($scope.title).toBe('carriers');
		}));

	});
});