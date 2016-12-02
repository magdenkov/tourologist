(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblDetailController', BubblDetailController);

    BubblDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bubbl', 'User', 'Interest', 'BubblRating', 'BubblDownload', 'Payload', 'BubblAdminReview', 'TourBubbl'];

    function BubblDetailController($scope, $rootScope, $stateParams, previousState, entity, Bubbl, User, Interest, BubblRating, BubblDownload, Payload, BubblAdminReview, TourBubbl) {
        var vm = this;

        vm.bubbl = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tourologistApp:bubblUpdate', function (event, result) {
            vm.bubbl = result;
        });

        $scope.approve = function () {

            var approve = {

                description: vm.bubbl.description,
                id: vm.bubbl.id,
                lat: vm.bubbl.lat,
                lng: vm.bubbl.lng,
                name: vm.bubbl.name,
                radiusMeters: vm.bubbl.radiusMeters,
                status: "APPROVED",
                interests:vm.bubbl.interests,
                userEmail: vm.bubbl.userEmail,
                userId: vm.bubbl.userId

            };
            if (vm.bubbl.id !== null) {
                Bubbl.update(approve, onSaveSuccess, onSaveError);
            }
        };

        $scope.rejectBubbl = function () {
            var approve = {

                description: vm.bubbl.description,
                id: vm.bubbl.id,
                lat: vm.bubbl.lat,
                lng: vm.bubbl.lng,
                name: vm.bubbl.name,
                radiusMeters: vm.bubbl.radiusMeters,
                status: "REJECTED",
                interests:vm.bubbl.interests,
                userEmail: vm.bubbl.userEmail,
                userId: vm.bubbl.userId

            };
            if (vm.bubbl.id !== null) {
                Bubbl.update(approve, onSaveSuccess, onSaveError);
            }

        };

        function onSaveSuccess(result) {
            vm.isSaving = true;

            $scope.$emit('tourologistApp:bubblUpdate', result);


        }

        function onSaveError() {
            vm.isSaving = false;

        }

        $scope.$on('$destroy', unsubscribe);
    }
})();
