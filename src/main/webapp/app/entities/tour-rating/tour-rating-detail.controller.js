(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourRatingDetailController', TourRatingDetailController);

    TourRatingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TourRating', 'User', 'Tour'];

    function TourRatingDetailController($scope, $rootScope, $stateParams, previousState, entity, TourRating, User, Tour) {
        var vm = this;

        vm.tourRating = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:tourRatingUpdate', function(event, result) {
            vm.tourRating = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
