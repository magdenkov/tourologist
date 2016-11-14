(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourRatingDialogController', TourRatingDialogController);

    TourRatingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TourRating', 'User', 'Tour'];

    function TourRatingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TourRating, User, Tour) {
        var vm = this;

        vm.tourRating = entity;
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
            if (vm.tourRating.id !== null) {
                TourRating.update(vm.tourRating, onSaveSuccess, onSaveError);
            } else {
                TourRating.save(vm.tourRating, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:tourRatingUpdate', result);
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
