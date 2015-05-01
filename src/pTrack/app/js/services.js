'use strict';

var ptServices = angular.module('ptServices', ['ngResource']);

ptServices.factory('Deliveries', ['$resource', function ($resource) {

	return $resource('tracking/accounts/:accountId/deliveries', null, {
		'add': {
			method: 'POST',
			isArray: true			
		},
		updateThreshold: {
			method: 'POST',
			isArray: false,
			url: 'tracking/accounts/:accountId/deliveries/:deliveryId/threshold'
		},
		updateMute: {
			method: 'POST',
			isArray: false,
			url: 'tracking/accounts/:accountId/deliveries/:deliveryId/mute'
		},
		updateArchived: {
			method: 'POST',
			isArray: false,
			url: 'tracking/accounts/:accountId/deliveries/:deliveryId/state'
		}
	});
}]);


ptServices.service('DeliveriesCounters', ['$filter', function ($filter) {
	this.counters = function (list) {

		return {
			overdue: $filter('DeliveryStatus')(list, 'overdue').length,
			onTrack: $filter('DeliveryStatus')(list, 'on-track').length,
			archived: $filter('DeliveryStatus')(list, 'archived').length
		};
	};
}]);

ptServices.factory('Registration', ['$resource', function ($resource) {

	return $resource('tracking/registration', null, {
		create: {
			method: 'POST',
			isArray: true
		}
	});
}]);

ptServices.factory('Users', ['$resource', function ($resource) {

	return $resource('tracking/users/:userId', null, {
		update: { method: 'POST' },
		signin: {
			method: 'GET',
			isArray: false,
			url: 'tracking/users?email=:email'
		}
	});
}]);

ptServices.factory('AuthService', ['$location', 'Users', 'Store', 'currentUser', '$rootScope',
	function($location, Users, Store, currentUser, $rootScope){

		var sessionUser = Store.getFromSession('currentUser');
		if (sessionUser) {
			currentUser.user = sessionUser;
			$rootScope.isLoggedIn = true;
		}

		return {
			login: function (userUrl) {
				var arr = userUrl.split('/');
				var userId = arr[arr.length-1];

				Users.get({
					userId: userId
				}, function (user) {
					currentUser.user = user;
					Store.setToSession('currentUser', user);
					$rootScope.isLoggedIn = true;
					$location.path('deliveries');
				});
			},
			logout: function () {
				currentUser.user = null;
				Store.setToSession('currentUser', null);
				$rootScope.isLoggedIn = false;
				$location.path('home');
			}
		};
}]);

ptServices.factory('Store', ['$window', function($window){
	return {
		set: function (key, val) {
			$window.localStorage.setItem(key, angular.toJson(val, true));
		},
		get: function (key) {
			return angular.fromJson($window.localStorage.getItem(key));
		},
		setToSession: function (key, val) {
			$window.sessionStorage.setItem(key, angular.toJson(val, true));
		},
		getFromSession: function (key) {
			return angular.fromJson($window.sessionStorage.getItem(key));
		}
	};
}]);

ptServices.factory('Feedback', ['$resource', function ($resource) {

	return $resource('tracking/feedbacks');
}]);

ptServices.factory('Auth', ['userId', function (userId) {

	return {
		isLoggedIn: function () {
			return userId.value !== '';
		}
	};
}]);
