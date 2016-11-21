(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bubbl-rating', {
            parent: 'entity',
            url: '/bubbl-rating?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.bubblRating.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bubbl-rating/bubbl-ratings.html',
                    controller: 'BubblRatingController',
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
                    $translatePartialLoader.addPart('bubblRating');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bubbl-rating-detail', {
            parent: 'entity',
            url: '/bubbl-rating/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.bubblRating.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bubbl-rating/bubbl-rating-detail.html',
                    controller: 'BubblRatingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bubblRating');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BubblRating', function($stateParams, BubblRating) {
                    return BubblRating.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bubbl-rating',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bubbl-rating-detail.edit', {
            parent: 'bubbl-rating-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-rating/bubbl-rating-dialog.html',
                    controller: 'BubblRatingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BubblRating', function(BubblRating) {
                            return BubblRating.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bubbl-rating.new', {
            parent: 'bubbl-rating',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-rating/bubbl-rating-dialog.html',
                    controller: 'BubblRatingDialogController',
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
                    $state.go('bubbl-rating', null, { reload: 'bubbl-rating' });
                }, function() {
                    $state.go('bubbl-rating');
                });
            }]
        })
        .state('bubbl-rating.edit', {
            parent: 'bubbl-rating',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-rating/bubbl-rating-dialog.html',
                    controller: 'BubblRatingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BubblRating', function(BubblRating) {
                            return BubblRating.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bubbl-rating', null, { reload: 'bubbl-rating' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bubbl-rating.delete', {
            parent: 'bubbl-rating',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-rating/bubbl-rating-delete-dialog.html',
                    controller: 'BubblRatingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BubblRating', function(BubblRating) {
                            return BubblRating.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bubbl-rating', null, { reload: 'bubbl-rating' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
