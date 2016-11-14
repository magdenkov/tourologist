(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('PayloadDialogController', PayloadDialogController);

    PayloadDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Payload', 'Bubbl'];

    function PayloadDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Payload, Bubbl) {
        var vm = this;

        vm.payload = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.bubbls = Bubbl.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.payload.id !== null) {
                Payload.update(vm.payload, onSaveSuccess, onSaveError);
            } else {
                Payload.save(vm.payload, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:payloadUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.lastModified = false;
        vm.datePickerOpenStatus.deleted = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
