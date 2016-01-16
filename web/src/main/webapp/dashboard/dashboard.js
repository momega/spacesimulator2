var dashboardController = angular.module('spaceSimulatorApp').controller('dashboardController', ['$scope', 'Simulation', 'datetime', '$location', function($scope, Simulation, datetime, $location) {
	$scope.simulations = Simulation.query();

	$scope.deleteSimulation = function(simulation) {
		Simulation.remove({uuid: simulation.uuid}, function() {
			$scope.simulations = Simulation.query();
		});
	};
	
	$scope.exampleSimulations = function() {
		Simulation.example({}, function() {
			$scope.simulations = Simulation.query();
			$scope.showNewSimulationForm = false;
			$scope.showDetailSimulationForm = false;
		});
	}
	
}]);

