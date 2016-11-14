(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblAdminReviewDialogController', BubblAdminReviewDialogController);

    BubblAdminReviewDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BubblAdminReview', 'Bubbl', 'User'];

    function BubblAdminReviewDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BubblAdminReview, Bubbl, User) {
        var vm = this;

        vm.bubblAdminReview = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.bubbls = Bubbl.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bubblAdminReview.id !== null) {
                BubblAdminReview.update(vm.bubblAdminReview, onSaveSuccess, onSaveError);
            } else {
                BubblAdminReview.save(vm.bubblAdminReview, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:bubblAdminReviewUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.time = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
