(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblEditController', BubblDialogController);

    BubblDialogController.$inject = ['$timeout', '$scope', '$state', 'entity', 'Bubbl', 'User', 'Interest', 'BubblRating', 'BubblDownload', 'Payload', 'BubblAdminReview', 'TourBubbl'];

    function BubblDialogController($timeout, $scope, $state, entity, Bubbl, User, Interest, BubblRating, BubblDownload, Payload, BubblAdminReview, TourBubbl) {
        var vm = this;

        vm.bubbl = entity;
        vm.clear = clear;

        vm.save = save;
        vm.users = User.query();
        vm.interests = Interest.query();
        vm.bubblratings = BubblRating.query();
        vm.bubbldownloads = BubblDownload.query();
        vm.payloads = Payload.query();
        vm.bubbladminreviews = BubblAdminReview.query();
        vm.tourbubbls = TourBubbl.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.bubbl.id !== null) {
                Bubbl.update(vm.bubbl, onSaveSuccess, onSaveError);
            } else {
                Bubbl.save(vm.bubbl, onSaveSuccess, onSaveError);
            }
        }

        $scope.back = function () {
            window.history.back();
        };

        function onSaveSuccess(result) {
            $scope.$emit('tourologistApp:bubblUpdate', result);
            $state.go('bubbl');
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
