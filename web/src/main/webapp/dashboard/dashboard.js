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
	$scope.fieldValues = [];
	$scope.selectedDefinition = {};
	$scope.simulationName = "";
	$scope.simulationUuid = "";
	
	$scope.openNewSimulation = function() {
		$scope.showNewSimulationForm = true;
		$scope.showDetailSimulationForm = true;
	}
	
	$scope.openEditSimulation = function(simulation) {
		$scope.showNewSimulationForm = false;
		$scope.showDetailSimulationForm = true;
		$scope.simulationName = simulation.name;
		$scope.simulationUuid = simulation.uuid;
		$scope.fieldValues = angular.copy(simulation.fieldValues);
	}
	
	$scope.copyFields = function(definition) {
		$scope.fieldValues = angular.copy(definition.fields);
		$scope.simulationName = definition.name;
	}

	$scope.newSimulation = function() {
		var newSimulation = { name: $scope.simulationName };
		newSimulation.fieldValues = angular.copy($scope.fieldVals);
		Simulation.create(newSimulation, function() {
			$scope.simulations = Simulation.query();
			$scope.showNewSimulationForm = false;
			$scope.showDetailSimulationForm = false;
		});
	}
	
}]);

