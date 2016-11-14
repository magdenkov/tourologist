(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('InterestDetailController', InterestDetailController);

    InterestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Interest', 'Tour'];

    function InterestDetailController($scope, $rootScope, $stateParams, previousState, entity, Interest, Tour) {
        var vm = this;

        vm.interest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:interestUpdate', function(event, result) {
            vm.interest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
