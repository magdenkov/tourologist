(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourDownloadDialogController', TourDownloadDialogController);

    TourDownloadDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TourDownload', 'User', 'Tour'];

    function TourDownloadDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TourDownload, User, Tour) {
        var vm = this;

        vm.tourDownload = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.tours = Tour.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tourDownload.id !== null) {
                TourDownload.update(vm.tourDownload, onSaveSuccess, onSaveError);
            } else {
                TourDownload.save(vm.tourDownload, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:tourDownloadUpdate', result);
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
