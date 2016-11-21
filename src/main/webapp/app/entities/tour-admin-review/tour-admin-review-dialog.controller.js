(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourAdminReviewDialogController', TourAdminReviewDialogController);

    TourAdminReviewDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TourAdminReview', 'Tour', 'User'];

    function TourAdminReviewDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TourAdminReview, Tour, User) {
        var vm = this;

        vm.tourAdminReview = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.tours = Tour.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tourAdminReview.id !== null) {
                TourAdminReview.update(vm.tourAdminReview, onSaveSuccess, onSaveError);
            } else {
                TourAdminReview.save(vm.tourAdminReview, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:tourAdminReviewUpdate', result);
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
