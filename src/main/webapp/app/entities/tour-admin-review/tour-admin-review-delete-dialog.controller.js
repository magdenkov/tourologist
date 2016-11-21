(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourAdminReviewDeleteController',TourAdminReviewDeleteController);

    TourAdminReviewDeleteController.$inject = ['$uibModalInstance', 'entity', 'TourAdminReview'];

    function TourAdminReviewDeleteController($uibModalInstance, entity, TourAdminReview) {
        var vm = this;

        vm.tourAdminReview = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TourAdminReview.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
