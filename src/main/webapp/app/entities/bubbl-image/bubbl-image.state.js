(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bubbl-image', {
            parent: 'entity',
            url: '/bubbl-image?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.bubblImage.home.title'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/entities/bubbl-image/bubbl-images.html',
                    controller: 'BubblImageController',
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
                    $translatePartialLoader.addPart('bubblImage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bubbl-image-detail', {
            parent: 'entity',
            url: '/bubbl-image/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.bubblImage.detail.title'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/entities/bubbl-image/bubbl-image-detail.html',
                    controller: 'BubblImageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bubblImage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BubblImage', function($stateParams, BubblImage) {
                    return BubblImage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bubbl-image',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bubbl-image-detail.edit', {
            parent: 'bubbl-image-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-image/bubbl-image-dialog.html',
                    controller: 'BubblImageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BubblImage', function(BubblImage) {
                            return BubblImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bubbl-image.new', {
            parent: 'bubbl-image',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-image/bubbl-image-dialog.html',
                    controller: 'BubblImageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                uploaded: null,
                                s3Key: null,
                                s3Bucket: null,
                                s3ThumbKey: null,
                                s3Region: null,
                                type: null,
                                master: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bubbl-image', null, { reload: 'bubbl-image' });
                }, function() {
                    $state.go('bubbl-image');
                });
            }]
        })
        .state('bubbl-image.edit', {
            parent: 'bubbl-image',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-image/bubbl-image-dialog.html',
                    controller: 'BubblImageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BubblImage', function(BubblImage) {
                            return BubblImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bubbl-image', null, { reload: 'bubbl-image' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bubbl-image.delete', {
            parent: 'bubbl-image',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-image/bubbl-image-delete-dialog.html',
                    controller: 'BubblImageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BubblImage', function(BubblImage) {
                            return BubblImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bubbl-image', null, { reload: 'bubbl-image' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
