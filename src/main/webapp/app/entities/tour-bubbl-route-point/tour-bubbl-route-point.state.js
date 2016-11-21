(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tour-bubbl-route-point', {
            parent: 'entity',
            url: '/tour-bubbl-route-point?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourBubblRoutePoint.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tour-bubbl-route-point/tour-bubbl-route-points.html',
                    controller: 'TourBubblRoutePointController',
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
                    $translatePartialLoader.addPart('tourBubblRoutePoint');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tour-bubbl-route-point-detail', {
            parent: 'entity',
            url: '/tour-bubbl-route-point/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourBubblRoutePoint.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tour-bubbl-route-point/tour-bubbl-route-point-detail.html',
                    controller: 'TourBubblRoutePointDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tourBubblRoutePoint');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TourBubblRoutePoint', function($stateParams, TourBubblRoutePoint) {
                    return TourBubblRoutePoint.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tour-bubbl-route-point',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tour-bubbl-route-point-detail.edit', {
            parent: 'tour-bubbl-route-point-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-bubbl-route-point/tour-bubbl-route-point-dialog.html',
                    controller: 'TourBubblRoutePointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourBubblRoutePoint', function(TourBubblRoutePoint) {
                            return TourBubblRoutePoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-bubbl-route-point.new', {
            parent: 'tour-bubbl-route-point',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-bubbl-route-point/tour-bubbl-route-point-dialog.html',
                    controller: 'TourBubblRoutePointDialogController',
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
                    $state.go('tour-bubbl-route-point', null, { reload: 'tour-bubbl-route-point' });
                }, function() {
                    $state.go('tour-bubbl-route-point');
                });
            }]
        })
        .state('tour-bubbl-route-point.edit', {
            parent: 'tour-bubbl-route-point',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-bubbl-route-point/tour-bubbl-route-point-dialog.html',
                    controller: 'TourBubblRoutePointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourBubblRoutePoint', function(TourBubblRoutePoint) {
                            return TourBubblRoutePoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-bubbl-route-point', null, { reload: 'tour-bubbl-route-point' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-bubbl-route-point.delete', {
            parent: 'tour-bubbl-route-point',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-bubbl-route-point/tour-bubbl-route-point-delete-dialog.html',
                    controller: 'TourBubblRoutePointDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TourBubblRoutePoint', function(TourBubblRoutePoint) {
                            return TourBubblRoutePoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-bubbl-route-point', null, { reload: 'tour-bubbl-route-point' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
