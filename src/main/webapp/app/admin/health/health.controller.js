(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TlHealthCheckController', TlHealthCheckController);

    TlHealthCheckController.$inject = ['TlHealthService', '$uibModal'];

    function TlHealthCheckController (TlHealthService, $uibModal) {
        var vm = this;

        vm.updatingHealth = true;
        vm.getLabelClass = getLabelClass;
        vm.refresh = refresh;
        vm.showHealth = showHealth;
        vm.baseName = TlHealthService.getBaseName;
        vm.subSystemName = TlHealthService.getSubSystemName;

        vm.refresh();

        function getLabelClass (statusState) {
            if (statusState === 'UP') {
                return 'label-success';
            } else {
                return 'label-danger';
            }
        }

        function refresh () {
            vm.updatingHealth = true;
            TlHealthService.checkHealth().then(function (response) {
                vm.healthData = TlHealthService.transformHealthData(response);
                vm.updatingHealth = false;
            }, function (response) {
                vm.healthData =  TlHealthService.transformHealthData(response.data);
                vm.updatingHealth = false;
            });
        }

        function showHealth (health) {
            $uibModal.open({
                templateUrl: 'app/admin/health/health.modal.html',
                controller: 'HealthModalController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    currentHealth: function() {
                        return health;
                    },
                    baseName: function() {
                        return vm.baseName;
                    },
                    subSystemName: function() {
                        return vm.subSystemName;
                    }

                }
            });
        }

    }
})();
