(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourBubblDialogController', TourBubblDialogController);

    TourBubblDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TourBubbl', 'Bubbl', 'Tour', 'TourBubblRoutePoint'];

    function TourBubblDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TourBubbl, Bubbl, Tour, TourBubblRoutePoint) {
        var vm = this;

        vm.tourBubbl = entity;
        vm.clear = clear;
        vm.save = save;
        vm.bubbls = Bubbl.query();
        vm.tours = Tour.query();
        vm.tourbubblroutepoints = TourBubblRoutePoint.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tourBubbl.id !== null) {
                TourBubbl.update(vm.tourBubbl, onSaveSuccess, onSaveError);
            } else {
                TourBubbl.save(vm.tourBubbl, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tourologistApp:tourBubblUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
