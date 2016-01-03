'use strict';

spaceSimulatorApp.factory('Simulation', ['$resource',
    function($resource){
	    return $resource('simulation/list', {}, {
			query: {method:'GET', isArray:true},
			create: {method: 'POST'}
	});
}]);

spaceSimulatorApp.factory('Definition', ['$resource',
    function($resource){
    	return $resource('definition/list', {}, {
    		query: {method:'GET', isArray:true}
    	});
}]);

spaceSimulatorApp.controller('DashboardController', ['$scope', 'Simulation', 'Definition', function($scope, Simulation, Definition) {
	
	$scope.simulations = Simulation.query();
	$scope.definitions = Definition.query();
	$scope.showNewSimulationForm = false;
	$scope.showDetailSimulationForm = false;
	$scope.simulation = {fieldValues: [], name: "", uuid: ""};
	
	$scope.openNewSimulation = function() {
		$scope.showNewSimulationForm = true;
		$scope.showDetailSimulationForm = true;
		$scope.simulation.fieldValues = [];
	}
	
	$scope.openEditSimulation = function(simulation) {
		$scope.showNewSimulationForm = false;
		$scope.showDetailSimulationForm = true;
		
		$scope.simulation.name = simulation.name;
		$scope.simulation.uuid = simulation.uuid;
		$scope.simulation.fieldValues = angular.copy(simulation.fieldValues);
	}
	
	$scope.copyFields = function(definition) {
		$scope.simulation.fieldValues = angular.copy(definition.fields);
		$scope.simulation.name = definition.name;
	}

	$scope.newSimulation = function() {
		var newSimulation = $scope.simulation;
		Simulation.create(newSimulation, function() {
			$scope.simulations = Simulation.query();
			$scope.showNewSimulationForm = false;
			$scope.showDetailSimulationForm = false;
		});
	}
	
}]);

