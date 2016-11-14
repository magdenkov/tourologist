(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tour-admin-review', {
            parent: 'entity',
            url: '/tour-admin-review?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourAdminReview.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tour-admin-review/tour-admin-reviews.html',
                    controller: 'TourAdminReviewController',
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
                    $translatePartialLoader.addPart('tourAdminReview');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tour-admin-review-detail', {
            parent: 'entity',
            url: '/tour-admin-review/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourAdminReview.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tour-admin-review/tour-admin-review-detail.html',
                    controller: 'TourAdminReviewDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tourAdminReview');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TourAdminReview', function($stateParams, TourAdminReview) {
                    return TourAdminReview.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tour-admin-review',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tour-admin-review-detail.edit', {
            parent: 'tour-admin-review-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-admin-review/tour-admin-review-dialog.html',
                    controller: 'TourAdminReviewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourAdminReview', function(TourAdminReview) {
                            return TourAdminReview.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-admin-review.new', {
            parent: 'tour-admin-review',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-admin-review/tour-admin-review-dialog.html',
                    controller: 'TourAdminReviewDialogController',
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
                    $state.go('tour-admin-review', null, { reload: 'tour-admin-review' });
                }, function() {
                    $state.go('tour-admin-review');
                });
            }]
        })
        .state('tour-admin-review.edit', {
            parent: 'tour-admin-review',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-admin-review/tour-admin-review-dialog.html',
                    controller: 'TourAdminReviewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourAdminReview', function(TourAdminReview) {
                            return TourAdminReview.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-admin-review', null, { reload: 'tour-admin-review' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-admin-review.delete', {
            parent: 'tour-admin-review',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-admin-review/tour-admin-review-delete-dialog.html',
                    controller: 'TourAdminReviewDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TourAdminReview', function(TourAdminReview) {
                            return TourAdminReview.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-admin-review', null, { reload: 'tour-admin-review' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
