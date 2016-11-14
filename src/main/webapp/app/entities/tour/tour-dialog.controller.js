(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourDialogController', TourDialogController);

    TourDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tour', 'User', 'Interest', 'Bubbl', 'TourImage', 'TourRoutePoint'];

    function TourDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tour, User, Interest, Bubbl, TourImage, TourRoutePoint) {
        var vm = this;

        vm.tour = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.interests = Interest.query();
        vm.bubbls = Bubbl.query();
        vm.tourimages = TourImage.query();
        vm.tourroutepoints = TourRoutePoint.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tour.id !== null) {
                Tour.update(vm.tour, onSaveSuccess, onSaveError);
            } else {
                Tour.save(vm.tour, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:tourUpdate', result);
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
