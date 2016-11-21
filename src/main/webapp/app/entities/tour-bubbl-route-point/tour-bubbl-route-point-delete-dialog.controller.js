(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourBubblRoutePointDeleteController',TourBubblRoutePointDeleteController);

    TourBubblRoutePointDeleteController.$inject = ['$uibModalInstance', 'entity', 'TourBubblRoutePoint'];

    function TourBubblRoutePointDeleteController($uibModalInstance, entity, TourBubblRoutePoint) {
        var vm = this;

        vm.tourBubblRoutePoint = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TourBubblRoutePoint.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
