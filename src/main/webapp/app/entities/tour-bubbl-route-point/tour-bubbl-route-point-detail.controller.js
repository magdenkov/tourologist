(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourBubblRoutePointDetailController', TourBubblRoutePointDetailController);

    TourBubblRoutePointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TourBubblRoutePoint', 'TourBubbl'];

    function TourBubblRoutePointDetailController($scope, $rootScope, $stateParams, previousState, entity, TourBubblRoutePoint, TourBubbl) {
        var vm = this;

        vm.tourBubblRoutePoint = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:tourBubblRoutePointUpdate', function(event, result) {
            vm.tourBubblRoutePoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
