(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('tl-configuration', {
            parent: 'admin',
            url: '/configuration',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'configuration.title'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/admin/configuration/configuration.html',
                    controller: 'TlConfigurationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('configuration');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
