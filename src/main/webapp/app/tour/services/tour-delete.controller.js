(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .controller('TourDeleteController', TourDeleteController);

    TourDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tour'];

    function TourDeleteController($uibModalInstance, entity, Tour) {
        var vm = this;

        vm.tour = entity;

        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.confirmDelete = function (id) {
            Tour.delete({id: id}).$promise.then(
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
