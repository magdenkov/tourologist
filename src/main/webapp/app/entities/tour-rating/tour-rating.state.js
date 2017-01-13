(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tour-rating', {
            parent: 'entity',
            url: '/tour-rating?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourRating.home.title'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/entities/tour-rating/tour-ratings.html',
                    controller: 'TourRatingController',
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
                    $translatePartialLoader.addPart('tourRating');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tour-rating-detail', {
            parent: 'entity',
            url: '/tour-rating/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourRating.detail.title'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/entities/tour-rating/tour-rating-detail.html',
                    controller: 'TourRatingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tourRating');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TourRating', function($stateParams, TourRating) {
                    return TourRating.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tour-rating',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tour-rating-detail.edit', {
            parent: 'tour-rating-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-rating/tour-rating-dialog.html',
                    controller: 'TourRatingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourRating', function(TourRating) {
                            return TourRating.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-rating.new', {
            parent: 'tour-rating',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-rating/tour-rating-dialog.html',
                    controller: 'TourRatingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                rate: null,
                                feedback: null,
                                time: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tour-rating', null, { reload: 'tour-rating' });
                }, function() {
                    $state.go('tour-rating');
                });
            }]
        })
        .state('tour-rating.edit', {
            parent: 'tour-rating',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-rating/tour-rating-dialog.html',
                    controller: 'TourRatingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourRating', function(TourRating) {
                            return TourRating.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-rating', null, { reload: 'tour-rating' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-rating.delete', {
            parent: 'tour-rating',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-rating/tour-rating-delete-dialog.html',
                    controller: 'TourRatingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TourRating', function(TourRating) {
                            return TourRating.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-rating', null, { reload: 'tour-rating' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
