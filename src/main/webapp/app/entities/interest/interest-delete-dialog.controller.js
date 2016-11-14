(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('InterestDeleteController',InterestDeleteController);

    InterestDeleteController.$inject = ['$uibModalInstance', 'entity', 'Interest'];

    function InterestDeleteController($uibModalInstance, entity, Interest) {
        var vm = this;

        vm.interest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Interest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
