(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblImageDetailController', BubblImageDetailController);

    BubblImageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BubblImage', 'Bubbl'];

    function BubblImageDetailController($scope, $rootScope, $stateParams, previousState, entity, BubblImage, Bubbl) {
        var vm = this;

        vm.bubblImage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:bubblImageUpdate', function(event, result) {
            vm.bubblImage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
