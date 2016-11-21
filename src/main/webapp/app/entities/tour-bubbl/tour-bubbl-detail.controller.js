(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourBubblDetailController', TourBubblDetailController);

    TourBubblDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TourBubbl', 'Bubbl', 'Tour', 'TourBubblRoutePoint'];

    function TourBubblDetailController($scope, $rootScope, $stateParams, previousState, entity, TourBubbl, Bubbl, Tour, TourBubblRoutePoint) {
        var vm = this;

        vm.tourBubbl = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:tourBubblUpdate', function(event, result) {
            vm.tourBubbl = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
