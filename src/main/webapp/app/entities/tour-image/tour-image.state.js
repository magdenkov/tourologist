(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tour-image', {
            parent: 'entity',
            url: '/tour-image?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourImage.home.title'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/entities/tour-image/tour-images.html',
                    controller: 'TourImageController',
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
                    $translatePartialLoader.addPart('tourImage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tour-image-detail', {
            parent: 'entity',
            url: '/tour-image/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.tourImage.detail.title'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/entities/tour-image/tour-image-detail.html',
                    controller: 'TourImageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tourImage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TourImage', function($stateParams, TourImage) {
                    return TourImage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tour-image',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tour-image-detail.edit', {
            parent: 'tour-image-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-image/tour-image-dialog.html',
                    controller: 'TourImageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourImage', function(TourImage) {
                            return TourImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-image.new', {
            parent: 'tour-image',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-image/tour-image-dialog.html',
                    controller: 'TourImageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                url: null,
                                thumbUrl: null,
                                mimeType: null,
                                master: null,
                                uploaded: null,
                                lastModified: null,
                                deleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tour-image', null, { reload: 'tour-image' });
                }, function() {
                    $state.go('tour-image');
                });
            }]
        })
        .state('tour-image.edit', {
            parent: 'tour-image',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-image/tour-image-dialog.html',
                    controller: 'TourImageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TourImage', function(TourImage) {
                            return TourImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-image', null, { reload: 'tour-image' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tour-image.delete', {
            parent: 'tour-image',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tour-image/tour-image-delete-dialog.html',
                    controller: 'TourImageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TourImage', function(TourImage) {
                            return TourImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tour-image', null, { reload: 'tour-image' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
