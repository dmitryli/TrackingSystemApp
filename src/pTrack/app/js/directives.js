'use strict';

var ptDirectives = angular.module('ptDirectives', []);

ptDirectives.directive('ptCarrierLink', function(){
	var carrierUrls = {
		UPS: 'http://wwwapps.ups.com/WebTracking/processRequest?tracknum=',
		USPS: 'https://tools.usps.com/go/TrackConfirmAction.action?tLabels='
	};
	// Runs during compile
	return {
		// name: '',
		// priority: 1,
		// terminal: true,
		// scope: {}, // {} = isolate, true = child, false/undefined = no change
		// controller: function($scope, $element, $attrs, $transclude) {},
		// require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
		restrict: 'A', // E = Element, A = Attribute, C = Class, M = Comment
		// template: '',
		// templateUrl: '',
		// replace: true,
		// transclude: true,
		// compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),
		link: function($scope, iElm, iAttrs, controller) {

			var url = carrierUrls[$scope.delivery.carrier] + $scope.delivery.id;
			
			iElm.attr('href', url);
		}
	};
});

ptDirectives.directive('ptCarrierLogo', function(){
	var carrierLogo = {
		UPS: 'images/ups-logo.png',
		USPS: 'images/usps-logo2.png'
	};
	// Runs during compile
	return {
		// name: '',
		// priority: 1,
		// terminal: true,
		// scope: {}, // {} = isolate, true = child, false/undefined = no change
		// controller: function($scope, $element, $attrs, $transclude) {},
		// require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
		restrict: 'A', // E = Element, A = Attribute, C = Class, M = Comment
		// template: '',
		// templateUrl: '',
		// replace: true,
		// transclude: true,
		// compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),
		link: function($scope, iElm, iAttrs, controller) {

			var url = carrierLogo[$scope.delivery.carrier];
			
			iElm.attr('src', url);
		}
	};
});