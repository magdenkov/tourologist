(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblImageDeleteController',BubblImageDeleteController);

    BubblImageDeleteController.$inject = ['$uibModalInstance', 'entity', 'BubblImage'];

    function BubblImageDeleteController($uibModalInstance, entity, BubblImage) {
        var vm = this;

        vm.bubblImage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BubblImage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
