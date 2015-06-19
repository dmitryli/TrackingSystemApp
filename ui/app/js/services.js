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
		update: {
			method: 'POST'
		},
		signin: {
			method: 'GET',
			isArray: false,
			url: 'tracking/users?email=:email'
		}
	});
}]);

ptServices.factory('AuthService', ['$location', 'Users', 'Store', 'currentUser', '$rootScope',

	function ($location, Users, Store, currentUser, $rootScope) {

		var sessionUser = Store.getFromSession('currentUser');
		if (sessionUser) {
			currentUser.user = sessionUser;
			$rootScope.isLoggedIn = true;
		}

		return {
			login: function (userUrl) {
				var arr = userUrl.split('/');
				var userId = arr[arr.length - 1];

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
	}
]);

ptServices.factory('Store', ['$window', function ($window) {
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

ptServices.factory('Base64', function () {
	var keyStr = 'ABCDEFGHIJKLMNOP' +
		'QRSTUVWXYZabcdef' +
		'ghijklmnopqrstuv' +
		'wxyz0123456789+/' +
		'=';
	return {
		encode: function (input) {
			var output = "";
			var chr1, chr2, chr3 = "";
			var enc1, enc2, enc3, enc4 = "";
			var i = 0;

			do {
				chr1 = input.charCodeAt(i++);
				chr2 = input.charCodeAt(i++);
				chr3 = input.charCodeAt(i++);

				enc1 = chr1 >> 2;
				enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
				enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
				enc4 = chr3 & 63;

				if (isNaN(chr2)) {
					enc3 = enc4 = 64;
				} else if (isNaN(chr3)) {
					enc4 = 64;
				}

				output = output +
					keyStr.charAt(enc1) +
					keyStr.charAt(enc2) +
					keyStr.charAt(enc3) +
					keyStr.charAt(enc4);
				chr1 = chr2 = chr3 = "";
				enc1 = enc2 = enc3 = enc4 = "";
			} while (i < input.length);

			return output;
		},

		decode: function (input) {
			var output = "";
			var chr1, chr2, chr3 = "";
			var enc1, enc2, enc3, enc4 = "";
			var i = 0;

			// remove all characters that are not A-Z, a-z, 0-9, +, /, or =
			var base64test = /[^A-Za-z0-9\+\/\=]/g;
			if (base64test.exec(input)) {
				alert("There were invalid base64 characters in the input text.\n" +
					"Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
					"Expect errors in decoding.");
			}
			input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

			do {
				enc1 = keyStr.indexOf(input.charAt(i++));
				enc2 = keyStr.indexOf(input.charAt(i++));
				enc3 = keyStr.indexOf(input.charAt(i++));
				enc4 = keyStr.indexOf(input.charAt(i++));

				chr1 = (enc1 << 2) | (enc2 >> 4);
				chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
				chr3 = ((enc3 & 3) << 6) | enc4;

				output = output + String.fromCharCode(chr1);

				if (enc3 != 64) {
					output = output + String.fromCharCode(chr2);
				}
				if (enc4 != 64) {
					output = output + String.fromCharCode(chr3);
				}

				chr1 = chr2 = chr3 = "";
				enc1 = enc2 = enc3 = enc4 = "";

			} while (i < input.length);

			return output;
		}
	};
});