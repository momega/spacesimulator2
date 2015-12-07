var spaceSimulatorApp = angular.module('spaceSimulatorApp', ['ngRoute']);

spaceSimulatorApp.config(['$routeProvider', function($routeProvider) {
	$routeProvider.
	  when('/project', {templateUrl: 'project/project.html', controller: 'ProjectController'}).
	  otherwise({redirectTo: '/project'});
}]);