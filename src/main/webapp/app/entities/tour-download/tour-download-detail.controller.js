(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourDownloadDetailController', TourDownloadDetailController);

    TourDownloadDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TourDownload', 'User', 'Tour'];

    function TourDownloadDetailController($scope, $rootScope, $stateParams, previousState, entity, TourDownload, User, Tour) {
        var vm = this;

        vm.tourDownload = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:tourDownloadUpdate', function(event, result) {
            vm.tourDownload = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
