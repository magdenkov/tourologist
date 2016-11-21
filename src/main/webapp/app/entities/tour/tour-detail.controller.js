(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourDetailController', TourDetailController);

    TourDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tour', 'User', 'Interest', 'TourRating', 'TourDownload', 'TourImage', 'TourAdminReview', 'TourRoutePoint', 'TourBubbl'];

    function TourDetailController($scope, $rootScope, $stateParams, previousState, entity, Tour, User, Interest, TourRating, TourDownload, TourImage, TourAdminReview, TourRoutePoint, TourBubbl) {
        var vm = this;

        vm.tour = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:tourUpdate', function(event, result) {
            vm.tour = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
