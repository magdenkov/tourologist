(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblAdminReviewDetailController', BubblAdminReviewDetailController);

    BubblAdminReviewDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BubblAdminReview', 'Bubbl', 'User'];

    function BubblAdminReviewDetailController($scope, $rootScope, $stateParams, previousState, entity, BubblAdminReview, Bubbl, User) {
        var vm = this;

        vm.bubblAdminReview = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:bubblAdminReviewUpdate', function(event, result) {
            vm.bubblAdminReview = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
