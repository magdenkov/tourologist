(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payload', {
            parent: 'entity',
            url: '/payload?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.payload.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payload/payloads.html',
                    controller: 'PayloadController',
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
                    $translatePartialLoader.addPart('payload');
                    $translatePartialLoader.addPart('payloadType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('payload-detail', {
            parent: 'entity',
            url: '/payload/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.payload.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payload/payload-detail.html',
                    controller: 'PayloadDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('payload');
                    $translatePartialLoader.addPart('payloadType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Payload', function($stateParams, Payload) {
                    return Payload.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payload',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payload-detail.edit', {
            parent: 'payload-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payload/payload-dialog.html',
                    controller: 'PayloadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Payload', function(Payload) {
                            return Payload.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payload.new', {
            parent: 'payload',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payload/payload-dialog.html',
                    controller: 'PayloadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                payloadType: null,
                                name: null,
                                url: null,
                                thumbUrl: null,
                                mimeType: null,
                                createdDate: null,
                                lastModified: null,
                                deleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('payload', null, { reload: 'payload' });
                }, function() {
                    $state.go('payload');
                });
            }]
        })
        .state('payload.edit', {
            parent: 'payload',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payload/payload-dialog.html',
                    controller: 'PayloadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Payload', function(Payload) {
                            return Payload.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payload', null, { reload: 'payload' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payload.delete', {
            parent: 'payload',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payload/payload-delete-dialog.html',
                    controller: 'PayloadDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Payload', function(Payload) {
                            return Payload.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payload', null, { reload: 'payload' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
