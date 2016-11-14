(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('InterestDialogController', InterestDialogController);

    InterestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Interest', 'Tour'];

    function InterestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Interest, Tour) {
        var vm = this;

        vm.interest = entity;
        vm.clear = clear;
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
            if (vm.interest.id !== null) {
                Interest.update(vm.interest, onSaveSuccess, onSaveError);
            } else {
                Interest.save(vm.interest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:interestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
