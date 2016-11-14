(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourDetailController', TourDetailController);

    TourDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tour', 'User', 'Interest', 'Bubbl', 'TourImage', 'TourRoutePoint'];

    function TourDetailController($scope, $rootScope, $stateParams, previousState, entity, Tour, User, Interest, Bubbl, TourImage, TourRoutePoint) {
        var vm = this;

        vm.tour = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:tourUpdate', function(event, result) {
            vm.tour = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
