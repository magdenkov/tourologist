(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblDownloadDetailController', BubblDownloadDetailController);

    BubblDownloadDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BubblDownload', 'User', 'Bubbl'];

    function BubblDownloadDetailController($scope, $rootScope, $stateParams, previousState, entity, BubblDownload, User, Bubbl) {
        var vm = this;

        vm.bubblDownload = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:bubblDownloadUpdate', function(event, result) {
            vm.bubblDownload = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
