(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourRoutePointDeleteController',TourRoutePointDeleteController);

    TourRoutePointDeleteController.$inject = ['$uibModalInstance', 'entity', 'TourRoutePoint'];

    function TourRoutePointDeleteController($uibModalInstance, entity, TourRoutePoint) {
        var vm = this;

        vm.tourRoutePoint = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TourRoutePoint.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
