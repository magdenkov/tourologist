(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bubbl-admin-review', {
            parent: 'entity',
            url: '/bubbl-admin-review?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.bubblAdminReview.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bubbl-admin-review/bubbl-admin-reviews.html',
                    controller: 'BubblAdminReviewController',
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
                    $translatePartialLoader.addPart('bubblAdminReview');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bubbl-admin-review-detail', {
            parent: 'entity',
            url: '/bubbl-admin-review/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.bubblAdminReview.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bubbl-admin-review/bubbl-admin-review-detail.html',
                    controller: 'BubblAdminReviewDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bubblAdminReview');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BubblAdminReview', function($stateParams, BubblAdminReview) {
                    return BubblAdminReview.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bubbl-admin-review',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bubbl-admin-review-detail.edit', {
            parent: 'bubbl-admin-review-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-admin-review/bubbl-admin-review-dialog.html',
                    controller: 'BubblAdminReviewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BubblAdminReview', function(BubblAdminReview) {
                            return BubblAdminReview.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bubbl-admin-review.new', {
            parent: 'bubbl-admin-review',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-admin-review/bubbl-admin-review-dialog.html',
                    controller: 'BubblAdminReviewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                reason: null,
                                approved: null,
                                time: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bubbl-admin-review', null, { reload: 'bubbl-admin-review' });
                }, function() {
                    $state.go('bubbl-admin-review');
                });
            }]
        })
        .state('bubbl-admin-review.edit', {
            parent: 'bubbl-admin-review',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-admin-review/bubbl-admin-review-dialog.html',
                    controller: 'BubblAdminReviewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BubblAdminReview', function(BubblAdminReview) {
                            return BubblAdminReview.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bubbl-admin-review', null, { reload: 'bubbl-admin-review' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bubbl-admin-review.delete', {
            parent: 'bubbl-admin-review',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-admin-review/bubbl-admin-review-delete-dialog.html',
                    controller: 'BubblAdminReviewDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BubblAdminReview', function(BubblAdminReview) {
                            return BubblAdminReview.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bubbl-admin-review', null, { reload: 'bubbl-admin-review' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
