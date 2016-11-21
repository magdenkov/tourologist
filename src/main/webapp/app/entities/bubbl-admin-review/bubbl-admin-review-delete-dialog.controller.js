(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblAdminReviewDeleteController',BubblAdminReviewDeleteController);

    BubblAdminReviewDeleteController.$inject = ['$uibModalInstance', 'entity', 'BubblAdminReview'];

    function BubblAdminReviewDeleteController($uibModalInstance, entity, BubblAdminReview) {
        var vm = this;

        vm.bubblAdminReview = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BubblAdminReview.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
