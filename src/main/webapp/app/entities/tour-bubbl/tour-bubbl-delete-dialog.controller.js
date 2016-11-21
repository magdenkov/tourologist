(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourBubblDeleteController',TourBubblDeleteController);

    TourBubblDeleteController.$inject = ['$uibModalInstance', 'entity', 'TourBubbl'];

    function TourBubblDeleteController($uibModalInstance, entity, TourBubbl) {
        var vm = this;

        vm.tourBubbl = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TourBubbl.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
