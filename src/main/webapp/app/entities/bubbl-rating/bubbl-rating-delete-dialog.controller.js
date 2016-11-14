(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblRatingDeleteController',BubblRatingDeleteController);

    BubblRatingDeleteController.$inject = ['$uibModalInstance', 'entity', 'BubblRating'];

    function BubblRatingDeleteController($uibModalInstance, entity, BubblRating) {
        var vm = this;

        vm.bubblRating = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BubblRating.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
