var spaceSimulatorApp = angular.module('spaceSimulatorApp', ['ngRoute', 'ngResource', 'ui.bootstrap']);

spaceSimulatorApp.config(['$routeProvider', function($routeProvider) {
	$routeProvider.
	  when('/project', {templateUrl: 'project/project.html', controller: 'ProjectController'}).
	  when('/dashboard', {templateUrl: 'dashboard/dashboard.html', controller: 'DashboardController'}).
	  otherwise({redirectTo: '/dashboard'});
}]);