(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourRoutePointDetailController', TourRoutePointDetailController);

    TourRoutePointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TourRoutePoint', 'Tour'];

    function TourRoutePointDetailController($scope, $rootScope, $stateParams, previousState, entity, TourRoutePoint, Tour) {
        var vm = this;

        vm.tourRoutePoint = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:tourRoutePointUpdate', function(event, result) {
            vm.tourRoutePoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
