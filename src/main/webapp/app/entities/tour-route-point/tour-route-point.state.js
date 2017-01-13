(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tour-route-point', {
            parent: 'entity',
            url: '/tour-route-point?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourRoutePoint.home.title'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/entities/tour-route-point/tour-route-points.html',
                    controller: 'TourRoutePointController',
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
                    $translatePartialLoader.addPart('tourRoutePoint');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tour-route-point-detail', {
            parent: 'entity',
            url: '/tour-route-point/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourRoutePoint.detail.title'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/entities/tour-route-point/tour-route-point-detail.html',
                    controller: 'TourRoutePointDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tourRoutePoint');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TourRoutePoint', function($stateParams, TourRoutePoint) {
                    return TourRoutePoint.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tour-route-point',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tour-route-point-detail.edit', {
            parent: 'tour-route-point-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-route-point/tour-route-point-dialog.html',
                    controller: 'TourRoutePointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourRoutePoint', function(TourRoutePoint) {
                            return TourRoutePoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-route-point.new', {
            parent: 'tour-route-point',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-route-point/tour-route-point-dialog.html',
                    controller: 'TourRoutePointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                lat: null,
                                lng: null,
                                orderNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tour-route-point', null, { reload: 'tour-route-point' });
                }, function() {
                    $state.go('tour-route-point');
                });
            }]
        })
        .state('tour-route-point.edit', {
            parent: 'tour-route-point',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-route-point/tour-route-point-dialog.html',
                    controller: 'TourRoutePointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourRoutePoint', function(TourRoutePoint) {
                            return TourRoutePoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-route-point', null, { reload: 'tour-route-point' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-route-point.delete', {
            parent: 'tour-route-point',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-route-point/tour-route-point-delete-dialog.html',
                    controller: 'TourRoutePointDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TourRoutePoint', function(TourRoutePoint) {
                            return TourRoutePoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-route-point', null, { reload: 'tour-route-point' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
