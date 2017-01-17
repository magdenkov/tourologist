(function () {
    'use strict';

    angular.module('tourologistApp.diy-tour')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('app.diy-tour', {
                url: '/diy-tour?startPosition&endPosition',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_USER'],
                    pageTitle: 'app.diy-tour.home.title'
                },
                views: {
                    'content@app': {
                        templateUrl: 'app/diy-tour/views/body.html',
                        controller: 'DiyTourController',
                        controllerAs: 'vm'
                    }
                },
                params: {},
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('diyTour');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    }

})();
