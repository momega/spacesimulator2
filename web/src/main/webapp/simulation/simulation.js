var simulationController = angular.module('spaceSimulatorApp').controller('simulationController', ['$scope', '$routeParams', '$location', 'Simulation', 'Definition', function($scope, $routeParams, $location, Simulation, Definition) {
	
	$scope.showNewSimulationForm = true;
	$scope.simulation = new Simulation({fieldValues: [], name: "", uuid: ""});
	if ($routeParams.uuid != null) {
		$scope.simulation = Simulation.get({uuid : $routeParams.uuid});
		$scope.showNewSimulationForm = false;
	}
	$scope.definitions = Definition.query();
	
	$scope.copyFields = function(definition) {
		$scope.simulation.fieldValues = angular.copy(definition.fields);
		$scope.simulation.name = definition.name;
	}	
	
	$scope.saveSimulation = function() {
		$scope.simulation.$save(function() {
			$scope.showNewSimulationForm = false;
			$location.path("/dashboard");
		});
	}	
	
}]);

