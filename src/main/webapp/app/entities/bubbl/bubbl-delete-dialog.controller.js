(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblDeleteController',BubblDeleteController);

    BubblDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bubbl'];

    function BubblDeleteController($uibModalInstance, entity, Bubbl) {
        var vm = this;

        vm.bubbl = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bubbl.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
