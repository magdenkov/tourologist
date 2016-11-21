(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblRatingDetailController', BubblRatingDetailController);

    BubblRatingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BubblRating', 'User', 'Bubbl'];

    function BubblRatingDetailController($scope, $rootScope, $stateParams, previousState, entity, BubblRating, User, Bubbl) {
        var vm = this;

        vm.bubblRating = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:bubblRatingUpdate', function(event, result) {
            vm.bubblRating = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
