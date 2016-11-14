(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bubbl-download', {
            parent: 'entity',
            url: '/bubbl-download?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.bubblDownload.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bubbl-download/bubbl-downloads.html',
                    controller: 'BubblDownloadController',
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
                    $translatePartialLoader.addPart('bubblDownload');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bubbl-download-detail', {
            parent: 'entity',
            url: '/bubbl-download/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tourologistApp.bubblDownload.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bubbl-download/bubbl-download-detail.html',
                    controller: 'BubblDownloadDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bubblDownload');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BubblDownload', function($stateParams, BubblDownload) {
                    return BubblDownload.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bubbl-download',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bubbl-download-detail.edit', {
            parent: 'bubbl-download-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-download/bubbl-download-dialog.html',
                    controller: 'BubblDownloadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BubblDownload', function(BubblDownload) {
                            return BubblDownload.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bubbl-download.new', {
            parent: 'bubbl-download',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-download/bubbl-download-dialog.html',
                    controller: 'BubblDownloadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                time: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bubbl-download', null, { reload: 'bubbl-download' });
                }, function() {
                    $state.go('bubbl-download');
                });
            }]
        })
        .state('bubbl-download.edit', {
            parent: 'bubbl-download',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-download/bubbl-download-dialog.html',
                    controller: 'BubblDownloadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BubblDownload', function(BubblDownload) {
                            return BubblDownload.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bubbl-download', null, { reload: 'bubbl-download' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bubbl-download.delete', {
            parent: 'bubbl-download',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bubbl-download/bubbl-download-delete-dialog.html',
                    controller: 'BubblDownloadDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BubblDownload', function(BubblDownload) {
                            return BubblDownload.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bubbl-download', null, { reload: 'bubbl-download' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
