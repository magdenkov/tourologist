(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('InterestDetailController', InterestDetailController);

    InterestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Interest', 'Tour', 'Bubbl'];

    function InterestDetailController($scope, $rootScope, $stateParams, previousState, entity, Interest, Tour, Bubbl) {
        var vm = this;

        vm.interest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:interestUpdate', function(event, result) {
            vm.interest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
