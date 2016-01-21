angular.module('spaceSimulatorApp', ['ngRoute', 'my.resource', 'ui.bootstrap', 'datetime'])

.config(['$routeProvider', function($routeProvider) {
	$routeProvider.
	    when('/simulation', {templateUrl: 'simulation/simulation.html', controller: 'simulationController'}).
	    when('/simulation/:uuid', {templateUrl: 'simulation/simulation.html', controller: 'simulationController'}).
	    when('/dashboard', {templateUrl: 'dashboard/dashboard.html', controller: 'dashboardController'}).
	    otherwise({redirectTo: '/dashboard'});
}])

.directive('clickLink', ['$location', function($location) {
    return {
        link: function(scope, element, attrs) {
            element.on('click', function() {
                scope.$apply(function() {
                    $location.path(attrs.clickLink);
                });
            });
        }
    }
}])

.service('Simulation', ['Resource',  function($resource) {
        return $resource('api/simulation/:uuid', {uuid: '@uuid'}, {
		    example: {method:'GET', isArray:true, url:'api/simulation/example'}
 	    });
}])

.service('Execution', ['Resource',  function($resource) {
        return $resource('api/execution/:uuid', {uuid: '@uuid'}, {
		    run: {method:'PUT'}
 	    });
}])

.service('Definition', ['Resource', function($resource){
 	    return $resource('api/definition/', {}, {
 	        query: {method:'GET', isArray:true}
        });
}]);