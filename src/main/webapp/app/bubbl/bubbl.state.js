(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('bubbl', {
                parent: 'app',
                url: '/bubbl?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tourologistApp.bubbl.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bubbl/bubbls.html',
                        controller: 'BubblController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'id,asc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bubbl');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('bubbl-detail', {
                parent: 'app',
                url: '/bubbl/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tourologistApp.bubbl.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bubbl/bubbl-detail.html',
                        controller: 'BubblDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bubbl');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Bubbl', function ($stateParams, Bubbl) {
                        return Bubbl.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'bubbl',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('bubbl-detail.edit', {
                parent: 'bubbl-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/bubbl/bubbl-dialog.html',
                        controller: 'BubblDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Bubbl', function (Bubbl) {
                                return Bubbl.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('bubbl.new', {
                parent: 'bubbl',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bubbl/bubbl-new.html',
                        controller: 'CreateBubblController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: null,
                            description: null,
                            status: null,
                            lat: null,
                            lng: null,
                            radiusMeters: null,
                            createdDate: null,
                            lastModified: null,
                            deleted: null,
                            id: null
                        };
                    }
                }

            })
            .state('bubbl.edit', {
                parent: 'bubbl',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/bubbl/bubbl-dialog.html',
                        controller: 'BubblDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Bubbl', function (Bubbl) {
                                return Bubbl.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('bubbl', null, {reload: 'bubbl'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('bubbl.delete', {
                parent: 'bubbl',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/bubbl/bubbl-delete-dialog.html',
                        controller: 'BubblDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Bubbl', function (Bubbl) {
                                return Bubbl.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('bubbl', null, {reload: 'bubbl'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
