(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblRatingDialogController', BubblRatingDialogController);

    BubblRatingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BubblRating', 'User', 'Bubbl'];

    function BubblRatingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BubblRating, User, Bubbl) {
        var vm = this;

        vm.bubblRating = entity;
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
            if (vm.bubblRating.id !== null) {
                BubblRating.update(vm.bubblRating, onSaveSuccess, onSaveError);
            } else {
                BubblRating.save(vm.bubblRating, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:bubblRatingUpdate', result);
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
