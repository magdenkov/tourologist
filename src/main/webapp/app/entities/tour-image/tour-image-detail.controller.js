(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourImageDetailController', TourImageDetailController);

    TourImageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TourImage', 'Tour'];

    function TourImageDetailController($scope, $rootScope, $stateParams, previousState, entity, TourImage, Tour) {
        var vm = this;

        vm.tourImage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:tourImageUpdate', function(event, result) {
            vm.tourImage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
