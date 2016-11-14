(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourRoutePointDialogController', TourRoutePointDialogController);

    TourRoutePointDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TourRoutePoint', 'Tour'];

    function TourRoutePointDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TourRoutePoint, Tour) {
        var vm = this;

        vm.tourRoutePoint = entity;
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
            if (vm.tourRoutePoint.id !== null) {
                TourRoutePoint.update(vm.tourRoutePoint, onSaveSuccess, onSaveError);
            } else {
                TourRoutePoint.save(vm.tourRoutePoint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:tourRoutePointUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
