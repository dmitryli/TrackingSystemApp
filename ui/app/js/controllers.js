'use strict';

var ptControllers = angular.module('ptControllers', []);

ptControllers.controller('NavController', ['$scope', '$location', 'AuthService', 'currentUser',
	function ($scope, $location, AuthService, currentUser) {
		$scope.menuClass = function (page) {
			var current = $location.path().substring(1);
			return page === current ? "active" : "";
		};

		$scope.logout = function () {
			AuthService.logout();
		};

		$scope.currentUser = currentUser;
	}
]);

ptControllers.controller('HomeController', ['$scope', 'currentUser', function ($scope, currentUser) {
	$scope.$watch('isLoggedIn', function (isLoggedIn) {
		if ($scope.isLoggedIn) {
			$scope.title = 'Welcome to pTrack ' + currentUser.user.displayName;
		} else {
			$scope.title = 'Welcome to pTrack';
		}

	});
}]);

ptControllers.controller('AboutController', ['$scope', function ($scope) {
	// $scope.title = 'pTrack Home';
}]);

ptControllers.controller('SignInController', ['$scope', '$http', 'AuthService', 'Users', 'Store', 'Base64',
	function ($scope, $http, AuthService, Users, Store, Base64) {
		$scope.account = {
			userName: Store.get('userName'),
			password: ''
		};

		$scope.rememberMe = Store.get('rememberMe') || false;

		$scope.signin = function () {
			// encode base64
			var encStr = Base64.encode($scope.account.userName + ':' + $scope.account.password);
			// set default authorization header to be used in all subsequent requests.
			$http.defaults.headers.common.Authorization = 'Basic ' + encStr;

			Users.signin({
				email: $scope.account.userName
			}).$promise.then(function (result) {
				$scope.loginFrm.$setValidity('auth', true);

				if ($scope.rememberMe) {
					Store.set('userName', $scope.account.userName);
					Store.set('rememberMe', true);
				} else {
					Store.set('userName', null);
					Store.set('rememberMe', false);
				}

				if (result && result.href) {
					AuthService.login(result.href);
				}

			}, function (error) {
				$scope.loginFrm.$setValidity('auth', false);
				$scope.loginFrm.$setValidity();
			});
		};
	}
]);

ptControllers.controller('SignUpController', ['$scope', 'Registration', 'AuthService', function ($scope, Registration, AuthService) {
	$scope.newAccount = {
		userName: '',
		email: '',
		password: ''
	};

	$scope.password2 = '';

	$scope.signup = function () {

		if ($scope.newAccount.password !== $scope.password2) {
			$scope.signupFrm.password.$setValidity('password', false);

			$scope.$watch(function () {
				return $scope.newAccount.password === $scope.password2;
			}, function (newVal, oldVal) {
				if (newVal === true) {
					$scope.signupFrm.password.$setValidity('password', true);
				}
			});
		} else {
			$scope.signupFrm.password.$setValidity('password', true);
			if ($scope.signupFrm.$valid) {
				Registration.create({
					displayName: $scope.newAccount.userName,
					email: $scope.newAccount.email,
					password: $scope.newAccount.password
				}).$promise.then(function (results) {
					var userUrl = '';
					if (results && results.length > 0) {
						for (var i = 0; i < results.length; i++) {
							if (results[i].rel === 'user') {
								userUrl = results[i].href;
								break;
							}
						}
						if (userUrl) {
							AuthService.login(userUrl);
						}
					}
				});
			}
		}
	};
}]);

ptControllers.controller('DeliveriesController', ['$scope', 'currentUser', 'Deliveries', '$modal', 'DeliveriesCounters',
	function ($scope, currentUser, Deliveries, $modal, DeliveriesCounters) {
		$scope.title = 'Home';
		$scope.carrier = '';
		$scope.showModal = false;
		$scope.filterByState = 'ACTIVE';
		$scope.status = 'overdue';
		$scope.currentTime = Date.now();
		$scope.isEmptyList = true;

		$scope.counters = {
			overdue: 0,
			onTrack: 0,
			archived: 0
		};

		function isEmptyByStatus(status) {
			return $scope.counters[status] === 0;
		}

		$scope.selectCarrier = function (crr) {
			$scope.carrier = crr;
		};

		$scope.changeStatus = function (status) {
			$scope.status = status;
			$scope.isEmptyList = isEmptyByStatus(status);
		};

		$scope.sortedStatus = function (delivery) {
			switch (delivery.deliveryStatus) {
			case 'INTRANSIT':
				return 1;
			case 'DELIVERED':
				return 2;
			case 'UNKNOWN':
				return 3;
			}
		};

		$scope.loadOrders = function () {
			Deliveries.query({
				accountId: currentUser.user.accountId
			}, function (data) {
				$scope.deliveries = data;
				$scope.counters = DeliveriesCounters.counters(data);
				$scope.isEmptyList = isEmptyByStatus($scope.status);
			});
		};
		$scope.loadOrders();

		$scope.showHideDeliveryDetails = function (order) {
			order.isDetailsOpen = order.isDetailsOpen ? false : true;
		};

		$scope.showAddDelivery = function () {
			$modal.open({
					templateUrl: 'views/addDelivery.html',
					controller: 'AddDeliveryController',
					size: 'md'
				})
				.result.then(function (res) {
					$scope.loadOrders();
				});
		};

	}
]);

ptControllers.controller('DeliveryProgressController', ['$scope', 'currentUser', 'Deliveries', function ($scope, currentUser, Deliveries) {

	function precent(val, max) {
		if (max) {
			if (val > max) {
				return 100;
			}
			return val / max * 100;
		}
		return 0;
	}

	var tmin = $scope.delivery.creationTime,
		tvalue = $scope.currentTime - tmin,
		threshold = $scope.delivery.threshold * 60 * 60 * 1000, // convert to ms
		tmax = (threshold * 2),
		perValue = precent(tvalue, tmax);

	$scope.thresholdTime = new Date(tmin + threshold);

	$scope.progress = {
		max: 100,
		onTrack: perValue < 49 ? perValue : 49,
		negOnTrack: perValue < 49 ? 49 - perValue : 0,
		overdue: perValue > 50 ? perValue - 50 : 0
	};

	$scope.open = function ($event) {
		$event.preventDefault();
		$event.stopPropagation();

		$scope.opened = true;
	};

	$scope.dateOptions = {
		formatDay: 'dd',
		formatMonth: 'MM',
		formatYear: 'yyyy',
		startingDay: 1
	};

	//$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	$scope.format = 'MM/dd/yyyy';

	$scope.changeTreshold = function (th) {
		var timeToAdd = th * 24 * 60 * 60 * 1000;

		Deliveries.updateThreshold({
			accountId: currentUser.user.accountId,
			deliveryId: $scope.delivery.id
		}, {
			threshold: $scope.delivery.threshold + (th * 24)
		}).$promise.then(function () {
			$scope.loadOrders();
		});

	};
	$scope.updateMute = function () {

		Deliveries.updateMute({
			accountId: currentUser.user.accountId,
			deliveryId: $scope.delivery.id
		}, {
			muteNotifications: !($scope.delivery.muteNotifications)
		}).$promise.then(function () {
			$scope.loadOrders();
		});

	};

	$scope.archived = function () {

		Deliveries.updateArchived({
			accountId: currentUser.user.accountId,
			deliveryId: $scope.delivery.id
		}, {
			state: "ARCHIVED"
		}).$promise.then(function () {
			$scope.loadOrders();
		});

	};

}]);

ptControllers.controller('AddDeliveryController', ['$scope', '$modalInstance', 'Deliveries', 'currentUser', function ($scope, $modalInstance, Deliveries, currentUser) {
	$scope.frm = {
		description: '',
		trackingId: '',
		carrier: '',
		threshold: 0
	};

	$scope.add = function () {
		if ($scope.addForm.$valid) {

			var dlv = new Deliveries();
			dlv.deliveries = [{
				id: $scope.frm.trackingId,
				description: $scope.frm.description,
				carrier: $scope.frm.carrier.toUpperCase(),
				threshold: $scope.frm.threshold * 24
			}];

			Deliveries.add({
				accountId: currentUser.user.accountId
			}, dlv).$promise.then(function (results) {
				$modalInstance.close(results);
			});
		}
	};
	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
}]);

ptControllers.controller('SettingsController', ['$scope', 'currentUser', 'Users', function ($scope, currentUser, Users) {

	var defaultProfile = {
		displayName: '',
		email: '',
		phoneNumber: '',
		password: null,
		emailWhenNewOrderAdded: true,
		emailWhenOrderStateChanges: true,
		emailWhenThresholdExceeded: true,
		emailNotificationTime: 0,
		defaultThreshold: {
			USPS: 96,
			UPS: 96
		},
		accountId: '',
		accountLink: {
			"rel": "account",
			"href": "http://localhost:8080/tracking/accounts/"
		}
	};

	$scope.title = 'Settings';

	$scope.tresholds = [{
		value: 24,
		text: '1 day'
	}, {
		value: 48,
		text: '2 days'
	}, {
		value: 72,
		text: '3 days'
	}, {
		value: 96,
		text: '4 days'
	}, {
		value: 120,
		text: '5 days'
	}, {
		value: 144,
		text: '6 days'
	}, {
		value: 168,
		text: '1 week'
	}];

	$scope.profile = defaultProfile;

	$scope.ups = 48;
	$scope.usps = 96;

	Users.get({
		userId: currentUser.user.id
	}, function (data) {
		$scope.profile = data;
	});

	$scope.reset = function () {
		Users.get({
			userId: currentUser.user.id
		}, function (data) {
			$scope.profile = data;
		});
	};

	$scope.update = function () {
		if ($scope.frmProfile.$valid) {
			Users.update({
				userId: currentUser.user.id
			}, $scope.profile);
		}
	};
}]);

ptControllers.controller('FeedbackController', ['$scope', 'Feedback', 'currentUser', function ($scope, Feedback, currentUser) {
	$scope.frm = {
		text: ''
	};
	$scope.isSent = false;

	$scope.sendFeedback = function () {
		if ($scope.fbForm.$valid) {

			var fb = new Feedback({
				text: $scope.frm.text,
				sourceAccoundId: currentUser.user.accountId
			});
			fb.$save().then(function () {
				$scope.isSent = true;
			});
		}
	};
}]);