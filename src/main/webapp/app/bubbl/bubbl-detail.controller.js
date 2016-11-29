(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblDetailController', BubblDetailController);

    BubblDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bubbl', 'User', 'Interest', 'BubblRating', 'BubblDownload', 'Payload', 'BubblAdminReview', 'TourBubbl'];

    function BubblDetailController($scope, $rootScope, $stateParams, previousState, entity, Bubbl, User, Interest, BubblRating, BubblDownload, Payload, BubblAdminReview, TourBubbl) {
        var vm = this;

        vm.bubbl = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:bubblUpdate', function(event, result) {
            vm.bubbl = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
