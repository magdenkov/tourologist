(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourImageDeleteController',TourImageDeleteController);

    TourImageDeleteController.$inject = ['$uibModalInstance', 'entity', 'TourImage'];

    function TourImageDeleteController($uibModalInstance, entity, TourImage) {
        var vm = this;

        vm.tourImage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TourImage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
