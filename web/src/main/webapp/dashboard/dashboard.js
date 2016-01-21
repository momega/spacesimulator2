var dashboardController = angular.module('spaceSimulatorApp').controller('dashboardController', ['$scope', 'Simulation', 'datetime', '$location', 'Execution', function($scope, Simulation, datetime, $location, Execution) {
	$scope.simulations = Simulation.query();

	$scope.deleteSimulation = function(simulation) {
		Simulation.remove({uuid: simulation.uuid}, function() {
			$scope.simulations = Simulation.query();
		});
	};
	
	$scope.runSimulation = function(simulation) {
		Execution.run({uuid: simulation.uuid}, function() {
			scope.simulations = Simulation.query();
		});
	}
	
	$scope.exampleSimulations = function() {
		Simulation.example({}, function() {
			$scope.simulations = Simulation.query();
			$scope.showNewSimulationForm = false;
			$scope.showDetailSimulationForm = false;
		});
	}
	
}]);

