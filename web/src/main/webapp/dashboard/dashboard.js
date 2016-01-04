'use strict';

spaceSimulatorApp.factory('Simulation', ['$resource',
    function($resource){
	    return $resource('api/simulation/:uuid', {uuid: '@uuid'}, {
			query: {method:'GET', isArray:true},
			save: {method: 'POST'},
			remove: {method: 'DELETE'}
	});
}]);

spaceSimulatorApp.factory('Definition', ['$resource',
    function($resource){
    	return $resource('api/definition/', {}, {
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
	
	$scope.deleteSimulation = function(simulation) {
		Simulation.remove({uuid: simulation.uuid}, function() {
			$scope.simulations = Simulation.query();
			$scope.showNewSimulationForm = false;
			$scope.showDetailSimulationForm = false;
		});
	}

	$scope.saveSimulation = function() {
		Simulation.save($scope.simulation, function() {
			$scope.simulations = Simulation.query();
			$scope.showNewSimulationForm = false;
			$scope.showDetailSimulationForm = false;
		});
	}
	
}]);

