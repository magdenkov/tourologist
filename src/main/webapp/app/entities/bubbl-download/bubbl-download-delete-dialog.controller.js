(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblDownloadDeleteController',BubblDownloadDeleteController);

    BubblDownloadDeleteController.$inject = ['$uibModalInstance', 'entity', 'BubblDownload'];

    function BubblDownloadDeleteController($uibModalInstance, entity, BubblDownload) {
        var vm = this;

        vm.bubblDownload = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BubblDownload.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
