angular.module('spaceSimulatorApp', ['ngRoute', 'my.resource', 'ui.bootstrap', 'datetime']);

angular.module('spaceSimulatorApp').config(['$routeProvider', function($routeProvider) {
	$routeProvider.
	    when('/simulation', {templateUrl: 'simulation/simulation.html', controller: 'simulationController'}).
	    when('/simulation/:uuid', {templateUrl: 'simulation/simulation.html', controller: 'simulationController'}).
	    when('/dashboard', {templateUrl: 'dashboard/dashboard.html', controller: 'dashboardController'}).
	    otherwise({redirectTo: '/dashboard'});
}]);

angular.module('spaceSimulatorApp').directive('clickLink', ['$location', function($location) {
    return {
        link: function(scope, element, attrs) {
            element.on('click', function() {
                scope.$apply(function() {
                    $location.path(attrs.clickLink);
                });
            });
        }
    }
}]);

angular.module('spaceSimulatorApp').service('Simulation', ['Resource',  function($resource) {
        return $resource('api/simulation/:uuid', {uuid: '@uuid'}, {
		    example: {method:'GET', isArray:true, url:'api/simulation/example'}
 	    });
}]);

angular.module('spaceSimulatorApp').service('Definition', ['Resource', function($resource){
 	    return $resource('api/definition/', {}, {
 	        query: {method:'GET', isArray:true}
        });
}]);