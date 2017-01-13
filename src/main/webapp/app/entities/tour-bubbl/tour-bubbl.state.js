(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tour-bubbl', {
            parent: 'entity',
            url: '/tour-bubbl?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourBubbl.home.title'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/entities/tour-bubbl/tour-bubbls.html',
                    controller: 'TourBubblController',
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
                    $translatePartialLoader.addPart('tourBubbl');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tour-bubbl-detail', {
            parent: 'entity',
            url: '/tour-bubbl/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourBubbl.detail.title'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/entities/tour-bubbl/tour-bubbl-detail.html',
                    controller: 'TourBubblDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tourBubbl');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TourBubbl', function($stateParams, TourBubbl) {
                    return TourBubbl.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tour-bubbl',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tour-bubbl-detail.edit', {
            parent: 'tour-bubbl-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-bubbl/tour-bubbl-dialog.html',
                    controller: 'TourBubblDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourBubbl', function(TourBubbl) {
                            return TourBubbl.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-bubbl.new', {
            parent: 'tour-bubbl',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-bubbl/tour-bubbl-dialog.html',
                    controller: 'TourBubblDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                orderNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tour-bubbl', null, { reload: 'tour-bubbl' });
                }, function() {
                    $state.go('tour-bubbl');
                });
            }]
        })
        .state('tour-bubbl.edit', {
            parent: 'tour-bubbl',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-bubbl/tour-bubbl-dialog.html',
                    controller: 'TourBubblDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourBubbl', function(TourBubbl) {
                            return TourBubbl.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-bubbl', null, { reload: 'tour-bubbl' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-bubbl.delete', {
            parent: 'tour-bubbl',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-bubbl/tour-bubbl-delete-dialog.html',
                    controller: 'TourBubblDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TourBubbl', function(TourBubbl) {
                            return TourBubbl.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-bubbl', null, { reload: 'tour-bubbl' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
