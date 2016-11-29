(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('PayloadDialogController', PayloadDialogController);

    PayloadDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Payload', 'Bubbl'];

    function PayloadDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Payload, Bubbl) {
        var vm = this;

        vm.payload = entity;
        vm.clear = clear;

        vm.save = save;
        vm.bubbls = Bubbl.query();


        console.log(vm.bubbls)
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {

            var formData = new FormData();
            formData.append('file', vm.payload.fileForUpload);
            vm.isSaving = true;
            Bubbl.createPayload(vm.payload.bubblId, formData, onSaveSuccess, onSaveError);

        }

        function onSaveSuccess(result) {
            $scope.$emit('tourologistApp:payloadUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
