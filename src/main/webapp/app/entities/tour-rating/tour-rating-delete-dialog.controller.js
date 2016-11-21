(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourRatingDeleteController',TourRatingDeleteController);

    TourRatingDeleteController.$inject = ['$uibModalInstance', 'entity', 'TourRating'];

    function TourRatingDeleteController($uibModalInstance, entity, TourRating) {
        var vm = this;

        vm.tourRating = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TourRating.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
