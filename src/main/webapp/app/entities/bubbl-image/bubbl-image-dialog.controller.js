(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblImageDialogController', BubblImageDialogController);

    BubblImageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BubblImage', 'Bubbl'];

    function BubblImageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BubblImage, Bubbl) {
        var vm = this;

        vm.bubblImage = entity;
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
            if (vm.bubblImage.id !== null) {
                BubblImage.update(vm.bubblImage, onSaveSuccess, onSaveError);
            } else {
                BubblImage.save(vm.bubblImage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:bubblImageUpdate', result);
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
