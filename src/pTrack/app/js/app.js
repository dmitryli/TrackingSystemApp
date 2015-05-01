'use strict';

// Declare app level module which depends on views, and components
angular.module('pTrack', [
	'ngRoute',
	'ngResource',
	'angular.filter',
	'ngAnimate',
	'ui.bootstrap',
	'ptControllers',
	'ptFilters',
	'ptDirectives',
	'ptServices'
])
.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$routeProvider
		// .when('/home/:userId', {
		.when('/home', {
			templateUrl: 'views/home.html',
			controller: 'HomeController'
		})
		.when('/deliveries', {
			templateUrl: 'views/deliveries.html',
			controller: 'DeliveriesController'
		})
		.when('/settings', {
			templateUrl: 'views/settings.html',
			controller: 'SettingsController'
		})
		.when('/feedback', {
			templateUrl: 'views/feedback.html',
			controller: 'FeedbackController'
		})
		.when('/about', {
			templateUrl: 'views/about.html',
			controller: 'AboutController'
		})
		.otherwise({
			redirectTo: '/home'
		});

	// $locationProvider.html5Mode(true);
}])
.value('currentUser', { user: null });
// .value('userId', { value: '' })
// .value('accountId', { value: '' })
// .value('userId', '55388765d4c654feddb78379')
// .value('accountId', '553887b3d4c654feddb7837c');
