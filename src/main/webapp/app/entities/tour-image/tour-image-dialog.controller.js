(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourImageDialogController', TourImageDialogController);

    TourImageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TourImage', 'Tour'];

    function TourImageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TourImage, Tour) {
        var vm = this;

        vm.tourImage = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.tours = Tour.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tourImage.id !== null) {
                TourImage.update(vm.tourImage, onSaveSuccess, onSaveError);
            } else {
                TourImage.save(vm.tourImage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:tourImageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.uploaded = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
