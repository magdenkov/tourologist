(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TourDownloadDeleteController',TourDownloadDeleteController);

    TourDownloadDeleteController.$inject = ['$uibModalInstance', 'entity', 'TourDownload'];

    function TourDownloadDeleteController($uibModalInstance, entity, TourDownload) {
        var vm = this;

        vm.tourDownload = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TourDownload.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
