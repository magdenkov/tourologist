(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('TlConfigurationController', TlConfigurationController);

    TlConfigurationController.$inject = ['$filter','TlConfigurationService'];

    function TlConfigurationController (filter,TlConfigurationService) {
        var vm = this;

        vm.allConfiguration = null;
        vm.configuration = null;

        TlConfigurationService.get().then(function(configuration) {
            vm.configuration = configuration;
        });
        TlConfigurationService.getEnv().then(function (configuration) {
            vm.allConfiguration = configuration;
        });
    }
})();
