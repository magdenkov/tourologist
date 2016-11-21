(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('PayloadDetailController', PayloadDetailController);

    PayloadDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Payload', 'Bubbl'];

    function PayloadDetailController($scope, $rootScope, $stateParams, previousState, entity, Payload, Bubbl) {
        var vm = this;

        vm.payload = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:payloadUpdate', function(event, result) {
            vm.payload = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
