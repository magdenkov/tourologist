(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tour', {
            parent: 'app',
            url: '/tour?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tour.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/tour/tours.html',
                    controller: 'TourController',
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
                    $translatePartialLoader.addPart('tour');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('tourType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tour-detail', {
            parent: 'app',
            url: '/tour/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tour.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/tour/tour-detail.html',
                    controller: 'TourDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tour');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('tourType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tour', function($stateParams, Tour) {
                    return Tour.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tour',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tour-detail.edit', {
            parent: 'tour-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/tour/tour-dialog.html',
                    controller: 'TourDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tour', function(Tour) {
                            return Tour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour.new', {
            parent: 'tour',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/tour/tour-new.html',
                    controller: 'CreateTourController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                status: null,
                                tourType: null,
                                price: null,
                                createdDate: null,
                                lastModified: null,
                                deleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tour', null, { reload: 'tour' });
                }, function() {
                    $state.go('tour');
                });
            }]
        })
        .state('tour.edit', {
            parent: 'tour',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/tour/tour-new.html',
                    controller: 'CreateTourController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tour', function(Tour) {
                            return Tour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour', null, { reload: 'tour' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour.delete', {
            parent: 'tour',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/tour/tour-delete-dialog.html',
                    controller: 'TourDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tour', function(Tour) {
                            return Tour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour', null, { reload: 'tour' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();