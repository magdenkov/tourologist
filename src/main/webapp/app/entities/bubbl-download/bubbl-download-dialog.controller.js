(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblDownloadDialogController', BubblDownloadDialogController);

    BubblDownloadDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BubblDownload', 'User', 'Bubbl'];

    function BubblDownloadDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BubblDownload, User, Bubbl) {
        var vm = this;

        vm.bubblDownload = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.bubbls = Bubbl.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bubblDownload.id !== null) {
                BubblDownload.update(vm.bubblDownload, onSaveSuccess, onSaveError);
            } else {
                BubblDownload.save(vm.bubblDownload, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:bubblDownloadUpdate', result);
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
