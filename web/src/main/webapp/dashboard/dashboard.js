'use strict';

spaceSimulatorApp.factory('Dashboard', ['$resource',
    function($resource){
	    return $resource('dashboard/list', {}, {
			query: {method:'GET', isArray:true}
	});
}]);

spaceSimulatorApp.controller('DashboardController', ['$scope', 'Dashboard', function($scope, Dashboard) {
	
	$scope.beans = Dashboard.query();
	
}]);

