(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblDetailController', BubblDetailController);

    BubblDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bubbl', 'User', 'Tour', 'Payload', 'BubblImage'];

    function BubblDetailController($scope, $rootScope, $stateParams, previousState, entity, Bubbl, User, Tour, Payload, BubblImage) {
        var vm = this;

        vm.bubbl = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:bubblUpdate', function(event, result) {
            vm.bubbl = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
