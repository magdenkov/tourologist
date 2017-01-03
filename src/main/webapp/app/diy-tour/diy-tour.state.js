(function () {
    'use strict';

    angular.module('tourologistApp.diy-tour')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('app.diy-tour', {
                url: '/diy-tour',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'app.diy-tour.home.title'
                },
                views: {
                    'content@': {
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
