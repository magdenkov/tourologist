(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourAdminReviewDetailController', TourAdminReviewDetailController);

    TourAdminReviewDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TourAdminReview', 'Tour', 'User'];

    function TourAdminReviewDetailController($scope, $rootScope, $stateParams, previousState, entity, TourAdminReview, Tour, User) {
        var vm = this;

        vm.tourAdminReview = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:tourAdminReviewUpdate', function(event, result) {
            vm.tourAdminReview = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
