(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourBubblRoutePointDialogController', TourBubblRoutePointDialogController);

    TourBubblRoutePointDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TourBubblRoutePoint', 'TourBubbl'];

    function TourBubblRoutePointDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TourBubblRoutePoint, TourBubbl) {
        var vm = this;

        vm.tourBubblRoutePoint = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tourbubbls = TourBubbl.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tourBubblRoutePoint.id !== null) {
                TourBubblRoutePoint.update(vm.tourBubblRoutePoint, onSaveSuccess, onSaveError);
            } else {
                TourBubblRoutePoint.save(vm.tourBubblRoutePoint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:tourBubblRoutePointUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
