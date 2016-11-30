(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('PayloadDeleteController',PayloadDeleteController);

    PayloadDeleteController.$inject = ['$uibModalInstance', 'entity', 'Payload'];

    function PayloadDeleteController($uibModalInstance, entity, Payload) {
        var vm = this;

        vm.payload = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Payload.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
